// PreCompress hook — summarizes short-term memory into long-term before Gemini CLI prunes history
const { appendMemory, extractTurns } = require('./lib/memory-manager');
const { summarize } = require('./lib/gemini-summarizer');
const { readStdin } = require('./lib/read-stdin');
const { logError, logInfo } = require('./lib/logger');

async function main() {
  try {
    readStdin(); // consume stdin even if unused

    // Extract only ## Turn N sections — exclude pinned context / session headers
    const turns = extractTurns('short-term.md');
    if (turns) {
      const ts = new Date().toISOString();
      // Only persist if summarization succeeds — raw turns contain ## headers that pollute long-term.md
      const summary = await summarize(turns);
      if (summary) {
        appendMemory('long-term.md', `## Pre-Compress Snapshot — ${ts}\n${summary}\n`);
        logInfo('pre-compress', 'Saved summarized pre-compress snapshot');
      } else {
        logInfo('pre-compress', 'Skipped pre-compress snapshot (API unavailable)');
      }
    }

  } catch (err) {
    logError('pre-compress', err);
  }

  // Always exit 0 to allow Gemini CLI compression to proceed
  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
