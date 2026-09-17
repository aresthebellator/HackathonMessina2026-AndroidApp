// CRUD operations for .gemini/memory/*.md files
const fs = require('fs');
const path = require('path');
const { logError } = require('./logger');

// Resolve memory dir from GEMINI_PROJECT_DIR env or CWD
const MEMORY_DIR = path.join(
  process.env.GEMINI_PROJECT_DIR || process.cwd(),
  '.gemini', 'memory'
);

function memPath(filename) {
  return path.join(MEMORY_DIR, filename);
}

function readMemory(filename) {
  try {
    return fs.readFileSync(memPath(filename), 'utf8');
  } catch (err) {
    if (err.code !== 'ENOENT') logError('readMemory', err);
    return '';
  }
}

function writeMemory(filename, content) {
  try {
    fs.mkdirSync(MEMORY_DIR, { recursive: true });
    fs.writeFileSync(memPath(filename), content, 'utf8');
  } catch (err) { logError('writeMemory', err); }
}

function appendMemory(filename, entry) {
  try {
    fs.mkdirSync(MEMORY_DIR, { recursive: true });
    fs.appendFileSync(memPath(filename), '\n' + entry, 'utf8');
  } catch (err) { logError('appendMemory', err); }
}

// Extract only ## Turn N sections from a memory file (excludes headers, schema, pinned context)
function extractTurns(filename) {
  const content = readMemory(filename);
  const turns = content.split(/(?=^## Turn \d+)/m).filter(s => /^## Turn \d+/.test(s));
  return turns.join('\n').trim();
}

// Count ## section headers as logical entries
function countEntries(filename) {
  const content = readMemory(filename);
  return (content.match(/^## /gm) || []).length;
}

// Keep only last N ## sections, discard older ones
function trimEntries(filename, keepLast) {
  try {
    const content = readMemory(filename);
    const sections = content.split(/(?=^## )/m).filter(s => s.trim());
    if (sections.length <= keepLast) return;
    const trimmed = sections.slice(-keepLast).join('\n');
    writeMemory(filename, trimmed);
  } catch (err) { logError('trimEntries', err); }
}

module.exports = { readMemory, writeMemory, appendMemory, countEntries, trimEntries, extractTurns };
