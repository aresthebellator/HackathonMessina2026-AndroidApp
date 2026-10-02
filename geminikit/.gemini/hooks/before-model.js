// BeforeModel hook — trims stale tool responses, logs token estimate
// API: { session_id, llm_request: { contents, systemInstruction, ... }, cwd }
// Fail-open: errors → empty output, never block model
const { readStdin } = require('./lib/read-stdin');
const { logError, logInfo } = require('./lib/logger');

const STALE_TURN_THRESHOLD = 10;
const STUB = '[stale — removed for token efficiency]';

function trimStaleResponses(contents) {
  if (!Array.isArray(contents) || contents.length <= STALE_TURN_THRESHOLD) return contents;

  const staleFrom = 0;
  const staleTo = contents.length - STALE_TURN_THRESHOLD;
  let trimCount = 0;

  const modified = contents.map((turn, idx) => {
    if (idx >= staleTo) return turn; // recent turns — keep as-is
    if (!Array.isArray(turn.parts)) return turn;

    const parts = turn.parts.map(part => {
      if (part?.functionResponse) {
        trimCount++;
        return { ...part, functionResponse: { ...part.functionResponse, response: STUB } };
      }
      return part;
    });
    return { ...turn, parts };
  });

  if (trimCount > 0) {
    logInfo('before-model', `Trimmed ${trimCount} stale tool responses (older than ${STALE_TURN_THRESHOLD} turns)`);
  }
  return modified;
}

function estimateTokens(contents) {
  if (!Array.isArray(contents)) return 0;
  let chars = 0;
  for (const turn of contents) {
    if (!Array.isArray(turn.parts)) continue;
    for (const part of turn.parts) {
      if (typeof part === 'string') chars += part.length;
      else if (part?.text) chars += part.text.length;
      else if (part?.functionResponse) chars += JSON.stringify(part.functionResponse).length;
    }
  }
  return Math.round(chars / 4);
}

async function main() {
  try {
    const input = readStdin();
    const { llm_request } = input;

    if (!llm_request?.contents) {
      process.stdout.write(JSON.stringify({}));
      process.exit(0);
    }

    const estimated = estimateTokens(llm_request.contents);
    logInfo('before-model', `estimated tokens=${estimated}`);

    if (estimated > 50000) {
      logInfo('before-model', 'WARNING: context exceeds 50K tokens — consider caching');
    }

    const trimmedContents = trimStaleResponses(llm_request.contents);
    const modified = { ...llm_request, contents: trimmedContents };

    process.stdout.write(JSON.stringify({
      hookSpecificOutput: { llmRequest: modified }
    }));
  } catch (err) {
    logError('before-model', err);
    process.stdout.write(JSON.stringify({}));
  }
  process.exit(0);
}

main();
