// Cross-platform stdin reader using fd 0 (works on Unix, Windows Git Bash, WSL)
function readStdin() {
  let raw = '';
  try {
    raw = require('fs').readFileSync(0, 'utf8');
    return JSON.parse(raw);
  } catch (_) {
    if (raw.trim()) {
      // Non-empty but unparseable — log to stderr for debugging
      process.stderr.write(`[read-stdin] WARNING: malformed JSON on stdin (${raw.length} bytes)\n`);
    }
    return {}; // fail-open: hooks must not block the pipeline
  }
}

module.exports = { readStdin };
