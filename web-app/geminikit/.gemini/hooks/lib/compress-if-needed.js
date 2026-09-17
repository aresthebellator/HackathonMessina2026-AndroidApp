// Shared helper: compress long-term memory if entry count exceeds threshold
const { readMemory, writeMemory, countEntries } = require('./memory-manager');
const { compressLongTerm, MAX_LONG_TERM_ENTRIES } = require('./gemini-summarizer');
const { logInfo } = require('./logger');

/**
 * Compress long-term.md if entries exceed MAX_LONG_TERM_ENTRIES.
 * @param {string} label - Header label for the compressed entry
 * @returns {boolean} true if compression occurred
 */
async function compressLongTermIfNeeded(label) {
  if (countEntries('long-term.md') <= MAX_LONG_TERM_ENTRIES) return false;

  const longTerm = readMemory('long-term.md');
  const entries = longTerm.split(/(?=^## )/m).filter(s => s.trim());
  const compressed = await compressLongTerm(entries);
  if (compressed) {
    const ts = new Date().toISOString();
    writeMemory('long-term.md', `## ${label} — ${ts}\n${compressed}\n`);
    logInfo('compress', `Long-term memory compressed [${label}]`);
    return true;
  }
  return false;
}

module.exports = { compressLongTermIfNeeded };
