// SessionEnd hook — final long-term compress if needed, clears short-term memory
const { writeMemory } = require('./lib/memory-manager');
const { compressLongTermIfNeeded } = require('./lib/compress-if-needed');
const { readStdin } = require('./lib/read-stdin');
const { logError, logInfo } = require('./lib/logger');

async function main() {
  try {
    readStdin(); // consume stdin even if unused

    // Final compress if long-term memory is overloaded
    await compressLongTermIfNeeded('Session End Compress');

    // Reset short-term memory for next session
    writeMemory('short-term.md', `# Short-term Memory\n_Cleared at session end: ${new Date().toISOString()}_\n`);
    logInfo('session-end', 'Session cleanup complete');

  } catch (err) {
    logError('session-end', err);
  }

  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
