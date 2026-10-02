// Silent error logger — writes to .gemini/logs/hooks-error.log, never throws
const fs = require('fs');
const path = require('path');

// Resolve logs dir from project root (2 levels up from lib/)
const LOGS_DIR = path.join(__dirname, '..', '..', 'logs');
const LOG_FILE = path.join(LOGS_DIR, 'hooks-error.log');

function logError(context, error) {
  try {
    fs.mkdirSync(LOGS_DIR, { recursive: true });
    const ts = new Date().toISOString();
    const msg = `[${ts}] ERROR [${context}]: ${error?.message || error}\n`;
    fs.appendFileSync(LOG_FILE, msg);
  } catch (_) { /* intentionally swallowed */ }
}

function logInfo(context, message) {
  // No-op by default; enable via LOG_LEVEL=debug env
  if (process.env.LOG_LEVEL === 'debug') {
    try {
      fs.mkdirSync(LOGS_DIR, { recursive: true });
      const ts = new Date().toISOString();
      fs.appendFileSync(LOG_FILE, `[${ts}] INFO [${context}]: ${message}\n`);
    } catch (_) {}
  }
}

module.exports = { logError, logInfo };
