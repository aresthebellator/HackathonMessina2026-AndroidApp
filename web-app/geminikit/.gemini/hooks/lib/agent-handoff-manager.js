// agent-handoff-manager.js — read/write/clear agent handoff state
// File: .gemini/.agent-handoff-{session_id}.json
// Max size: 8KB. Fail-open on all operations.
const fs = require('fs');
const path = require('path');

const HANDOFF_DIR = '.gemini';
const MAX_SIZE = 8192;
const STALE_AGE_MS = 3600000; // 1 hour

const REQUIRED_FIELDS = ['from', 'to', 'status', 'summary'];
const VALID_STATUS = ['ready', 'blocked', 'partial', 'completed', 'failed'];

function getHandoffPath(cwd, sessionId) {
  const safeId = String(sessionId).replace(/[^a-zA-Z0-9-_]/g, '_');
  return path.join(cwd, HANDOFF_DIR, `.agent-handoff-${safeId}.json`);
}

function validate(handoff) {
  const missing = REQUIRED_FIELDS.filter(k => !handoff[k]);
  if (missing.length) return { ok: false, error: `missing required fields: ${missing.join(', ')}` };
  if (!VALID_STATUS.includes(handoff.status)) return { ok: false, error: `invalid status: ${handoff.status}` };
  return { ok: true };
}

function readHandoff(cwd, sessionId) {
  try {
    const p = getHandoffPath(cwd, sessionId);
    if (!fs.existsSync(p)) return null;
    const raw = fs.readFileSync(p, 'utf8');
    if (raw.length > MAX_SIZE * 2) return null; // likely corrupted
    return JSON.parse(raw);
  } catch { return null; }
}

// Returns { ok: boolean, error?: string }
function writeHandoff(cwd, sessionId, handoff) {
  try {
    const result = validate(handoff);
    if (!result.ok) return result;
    const payload = { v: 1, ...handoff };
    const json = JSON.stringify(payload, null, 0);
    if (json.length > MAX_SIZE) return { ok: false, error: `payload too large: ${json.length} > ${MAX_SIZE}` };
    fs.writeFileSync(getHandoffPath(cwd, sessionId), json);
    return { ok: true };
  } catch (err) {
    return { ok: false, error: err.message };
  }
}

function clearHandoff(cwd, sessionId) {
  try { fs.unlinkSync(getHandoffPath(cwd, sessionId)); } catch {}
}

// Clear all handoff files regardless of age (session start)
function clearAllHandoffs(cwd) {
  try {
    const dir = path.join(cwd, HANDOFF_DIR);
    fs.readdirSync(dir)
      .filter(f => f.startsWith('.agent-handoff-') && f.endsWith('.json'))
      .forEach(f => { try { fs.unlinkSync(path.join(dir, f)); } catch {} });
  } catch {}
}

// Clear only handoff files older than maxAgeMs (default 1h). Returns count removed.
function clearStaleHandoffs(cwd, maxAgeMs = STALE_AGE_MS) {
  let removed = 0;
  try {
    const dir = path.join(cwd, HANDOFF_DIR);
    const now = Date.now();
    fs.readdirSync(dir)
      .filter(f => f.startsWith('.agent-handoff-') && f.endsWith('.json'))
      .forEach(f => {
        try {
          const filePath = path.join(dir, f);
          const { mtimeMs } = fs.statSync(filePath);
          if (now - mtimeMs > maxAgeMs) {
            fs.unlinkSync(filePath);
            removed++;
          }
        } catch {}
      });
  } catch {}
  return removed;
}

module.exports = { readHandoff, writeHandoff, clearHandoff, clearAllHandoffs, clearStaleHandoffs };
