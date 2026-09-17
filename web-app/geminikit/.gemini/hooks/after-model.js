// AfterModel hook — accumulates response into short-term, summarizes when threshold met, then resets
// Fires per-chunk during streaming; only acts on last chunk (finishReason present)
const { writeMemory, appendMemory, extractTurns } = require('./lib/memory-manager');
const { shouldSummarize, summarize } = require('./lib/gemini-summarizer');
const { compressLongTermIfNeeded } = require('./lib/compress-if-needed');
const { readStdin } = require('./lib/read-stdin');
const { logError, logInfo } = require('./lib/logger');
const { readSkillState } = require('./lib/skill-state-manager');
const fs = require('fs');
const path = require('path');

// ---------------------------------------------------------------------------
// Token log helpers
// ---------------------------------------------------------------------------

// Resolve project dir — hooks run from .gemini/hooks/, project root is two levels up
const PROJECT_DIR = process.env.GEMINI_PROJECT_DIR || path.resolve(__dirname, '..', '..');
const TOKEN_LOG_DIR = path.join(PROJECT_DIR, '.gemini', 'logs');
const TOKEN_LOG_FILE = path.join(TOKEN_LOG_DIR, 'token-usage.jsonl');

// Per-session turn counter stored in /tmp to avoid O(n) JSONL scan each invocation.
function getSessionTurnCounterPath(sessionId) {
  const safeId = String(sessionId).replace(/[^a-zA-Z0-9-_]/g, '_');
  return path.join('/tmp', `gk-turn-${safeId}.json`);
}

function getSessionTurnCount(sessionId) {
  try {
    const counterPath = getSessionTurnCounterPath(sessionId);
    if (fs.existsSync(counterPath)) {
      const { turn } = JSON.parse(fs.readFileSync(counterPath, 'utf8'));
      return (turn || 0) + 1;
    }
    return 1;
  } catch {
    return 1;
  }
}

function saveSessionTurnCount(sessionId, turn) {
  try {
    fs.writeFileSync(getSessionTurnCounterPath(sessionId), JSON.stringify({ turn }));
  } catch { /* ignore */ }
}

const MAX_LOG_LINES = 10000;
const KEEP_LOG_LINES = 8000;

function rotateTokenLogIfNeeded() {
  try {
    if (!fs.existsSync(TOKEN_LOG_FILE)) return;
    const content = fs.readFileSync(TOKEN_LOG_FILE, 'utf8');
    const lines = content.split('\n').filter(l => l.trim());
    if (lines.length > MAX_LOG_LINES) {
      // Write to temp file then rename atomically to avoid data loss on concurrent writes
      const tmp = TOKEN_LOG_FILE + '.tmp';
      fs.writeFileSync(tmp, lines.slice(-KEEP_LOG_LINES).join('\n') + '\n', 'utf8');
      fs.renameSync(tmp, TOKEN_LOG_FILE);
      logInfo('after-model:token-log', `rotated: kept last ${KEEP_LOG_LINES} of ${lines.length} entries`);
    }
  } catch (err) {
    logError('after-model:token-log:rotate', err);
  }
}

function appendTokenLog(entry) {
  try {
    fs.mkdirSync(TOKEN_LOG_DIR, { recursive: true });
    fs.appendFileSync(TOKEN_LOG_FILE, JSON.stringify(entry) + '\n', 'utf8');
    rotateTokenLogIfNeeded();
  } catch (err) {
    logError('after-model:token-log', err);
  }
}

async function main() {
  try {
    const input = readStdin();
    // Official AfterModel API: { session_id, llm_response: { candidates, usageMetadata }, ... }

    const { llm_response } = input;
    const candidate = llm_response?.candidates?.[0];
    const parts = candidate?.content?.parts || [];
    // Parts may be Part objects {text: string} or plain strings depending on Gemini CLI version
    const responseText = Array.isArray(parts)
      ? parts.map(p => (typeof p === 'string' ? p : (p?.text || ''))).join('')
      : String(parts || '');
    const totalTokens = llm_response?.usageMetadata?.totalTokenCount || 0;
    // finishReason present ("STOP", "MAX_TOKENS", etc.) only on last chunk
    const isLastChunk = !!candidate?.finishReason;

    logInfo('after-model', `tokens=${totalTokens} lastChunk=${isLastChunk}`);

    // Accumulate chunk text into short-term buffer
    if (responseText) {
      const ts = new Date().toISOString();
      appendMemory('short-term.md', `## Chunk — ${ts}\n${responseText}\n`);
    }

    // Detect display-field usage for observability
    if (isLastChunk && responseText.includes('"display"')) {
      logInfo('after-model', 'display-field detected in response');
    }

    // Detect raw JSON blocks in user-facing response (violation monitoring)
    if (isLastChunk) {
      const jsonBlockPattern = /```json[\s\S]*?```/;
      // Use non-multiline $ to avoid false positives from code blocks containing { }
      const jsonObjectPattern = /^\s*\{[^\n]{20,}\}\s*$/;
      if (jsonBlockPattern.test(responseText) || jsonObjectPattern.test(responseText)) {
        logInfo('after-model', 'WARNING: raw JSON detected in user-facing response — rule violation');
      }
    }

    // Compute turn info once — needed for both summarization trigger and token log
    const sessionId = input.session_id || 'unknown';
    const turnNumber = isLastChunk ? getSessionTurnCount(sessionId) : 0;

    // Only summarize + reset on last chunk to avoid mid-stream processing
    let didSummarize = false;
    if (isLastChunk && shouldSummarize(totalTokens, turnNumber)) {
      const turns = extractTurns('short-term.md');
      if (turns) {
        const summary = await summarize(turns);
        if (summary) {
          const ts = new Date().toISOString();
          appendMemory('long-term.md', `## Summary — ${ts}\n${summary}\n`);
          logInfo('after-model', 'Appended summary to long-term');
          writeMemory('short-term.md', `# Short-term Memory\n_Reset after summarization: ${ts}_\n`);
          didSummarize = true;
        }
      }
      await compressLongTermIfNeeded('Compressed');
    }

    // Append token usage entry to .gemini/logs/token-usage.jsonl on every last chunk
    if (isLastChunk) {
      const skillState = readSkillState(PROJECT_DIR);
      const activeSkill = skillState?.skill || 'unknown';

      const logEntry = {
        ts: new Date().toISOString(),
        tokens: totalTokens || 0,
        session_id: sessionId,
        skill: activeSkill,
        turn: turnNumber,
        compressed: didSummarize
      };
      appendTokenLog(logEntry);
      saveSessionTurnCount(sessionId, turnNumber);
      logInfo('after-model', `token-log turn=${turnNumber} skill=${activeSkill} tokens=${totalTokens} compressed=${didSummarize}`);
    }

  } catch (err) {
    logError('after-model', err);
  }

  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
