// skill-state-manager.js
// Read/write .gemini/.skill-state.json for hook-skill communication
// Fail-open: all errors are caught and return null/false

const fs = require('fs');
const path = require('path');

const STATE_FILE = '.gemini/.skill-state.json';
const STALE_TTL_MS = 2 * 60 * 60 * 1000; // 2 hours

function getStatePath(cwd) {
  return path.join(cwd || process.cwd(), STATE_FILE);
}

// Read current skill state — returns null if missing, stale (>2h), or unreadable
function readSkillState(cwd) {
  try {
    const raw = fs.readFileSync(getStatePath(cwd), 'utf8');
    const state = JSON.parse(raw);
    const ageMs = Date.now() - new Date(state.timestamp).getTime();
    if (ageMs > STALE_TTL_MS) return null;
    return state;
  } catch {
    return null;
  }
}

// Write skill state — called by skills at session start
function writeSkillState(cwd, state) {
  try {
    const filePath = getStatePath(cwd);
    fs.mkdirSync(path.dirname(filePath), { recursive: true });
    fs.writeFileSync(filePath, JSON.stringify({
      ...state,
      timestamp: new Date().toISOString()
    }, null, 2));
    return true;
  } catch {
    return false;
  }
}

// Clear skill state — called after skill session ends
function clearSkillState(cwd) {
  try {
    fs.unlinkSync(getStatePath(cwd));
  } catch { /* ignore */ }
}

module.exports = { readSkillState, writeSkillState, clearSkillState };
