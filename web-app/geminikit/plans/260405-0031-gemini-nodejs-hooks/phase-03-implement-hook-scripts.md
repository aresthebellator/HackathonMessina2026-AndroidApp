# Phase 03 — Implement Hook Scripts

## Context Links
- Plan: [plan.md](./plan.md)
- Phase 02 (lib modules): [phase-02-implement-lib-modules.md](./phase-02-implement-lib-modules.md)
- Gemini CLI hook spec: [plans/reports/researcher-260405-0025-gemini-cli-hooks-specification.md](../../plans/reports/researcher-260405-0025-gemini-cli-hooks-specification.md)

## Overview
- **Priority:** P1
- **Status:** Completed
- **Effort:** 2h

Implement 5 Node.js hook scripts. Each reads JSON from stdin, performs its action, writes JSON to stdout, always exits 0.

## Requirements

**Functional:**
- All scripts: read JSON stdin → process → write JSON stdout → exit 0
- `session-start.js`: load pinned + last 3 long-term entries
- `after-model.js`: check token threshold → summarize → append to long-term.md
- `pre-compress.js`: save short-term.md snapshot before Gemini prunes history
- `after-tool.js`: log tool call (name + status) to execution.md
- `session-end.js`: final long-term persist, clear short-term.md

**Non-functional:**
- Never exit non-zero (non-blocking behavior)
- Never write to stdout except final JSON output (breaks CLI parsing)
- Max 200 lines per script (modularize to lib if growing)

## Architecture

```
Gemini CLI
    │
    ├─ SessionStart ──→ session-start.js  → reads long-term.md + pinned.md
    │                                       writes short-term.md (session context)
    │
    ├─ AfterModel ───→ after-model.js    → checks token threshold
    │                                       calls gemini-summarizer.js if needed
    │                                       appends to long-term.md
    │
    ├─ PreCompress ──→ pre-compress.js   → snapshots short-term.md
    │                                       saves important context before prune
    │
    ├─ AfterTool ────→ after-tool.js     → appends tool log to execution.md
    │
    └─ SessionEnd ───→ session-end.js    → final compress long-term if needed
                                           clears short-term.md
```

### stdin/stdout protocol
All scripts follow this pattern:
```js
// Read JSON from stdin
const raw = fs.readFileSync('/dev/stdin', 'utf8');
const input = JSON.parse(raw);

// ... do work ...

// Write JSON to stdout (required by Gemini CLI)
process.stdout.write(JSON.stringify({ status: 'ok' }));
process.exit(0);
```

**Windows note:** `/dev/stdin` works on Git Bash / WSL. For native Windows cmd, use `process.stdin` with stream reading instead.

## Related Code Files
- **Create:** `.gemini/hooks/session-start.js`
- **Create:** `.gemini/hooks/after-model.js`
- **Create:** `.gemini/hooks/pre-compress.js`
- **Create:** `.gemini/hooks/after-tool.js`
- **Create:** `.gemini/hooks/session-end.js`

## Implementation Steps

### Step 1 — Shared stdin reader helper
Add to each script (or extract to `lib/read-stdin.js`):
```js
function readStdin() {
  try {
    // Cross-platform stdin read
    return JSON.parse(require('fs').readFileSync(0, 'utf8')); // fd 0 = stdin
  } catch (err) {
    return {}; // fallback: empty input
  }
}
```
`readFileSync(0)` reads fd 0 (stdin) synchronously — works on both Unix and Windows.

### Step 2 — `session-start.js`
**Trigger:** SessionStart — fires on startup, session resume, or /clear

```js
const { readMemory, writeMemory } = require('./lib/memory-manager');
const { logError } = require('./lib/logger');

async function main() {
  try {
    const input = readStdin(); // { event: 'SessionStart', sessionId: '...' }

    // Load pinned context (immutable rules)
    const pinned = readMemory('pinned.md');

    // Load last 3 entries from long-term memory
    const longTerm = readMemory('long-term.md');
    const entries = longTerm.split(/(?=^## )/m).filter(s => s.trim());
    const recent = entries.slice(-3).join('\n');

    // Write initialized short-term context
    const sessionContext = `# Session Context\nLoaded: ${new Date().toISOString()}\nSession: ${input.sessionId || 'unknown'}\n\n## Recent History\n${recent}\n`;
    writeMemory('short-term.md', sessionContext);

  } catch (err) {
    logError('session-start', err);
  }
  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
```

### Step 3 — `after-model.js`
**Trigger:** AfterModel — fires after every LLM response

```js
const { readMemory, appendMemory, countEntries, trimEntries } = require('./lib/memory-manager');
const { shouldSummarize, summarize, compressLongTerm, MAX_LONG_TERM_ENTRIES } = require('./lib/gemini-summarizer');
const { logError, logInfo } = require('./lib/logger');

async function main() {
  try {
    const input = readStdin();
    // Expected: { event, sessionId, turnId, response: { text }, stats: { totalTokens, promptTokens, responseTokens } }

    const { turnId = 0, stats = {}, response = {} } = input;
    const totalTokens = stats.totalTokens || 0;

    logInfo('after-model', `turn=${turnId} tokens=${totalTokens}`);

    if (shouldSummarize(totalTokens, turnId)) {
      // Summarize recent short-term context
      const shortTerm = readMemory('short-term.md');
      if (shortTerm.trim()) {
        const summary = await summarize(shortTerm);
        if (summary) {
          const ts = new Date().toISOString();
          const entry = `## Turn ${turnId} — ${ts}\n${summary}\n`;
          appendMemory('long-term.md', entry);
          logInfo('after-model', `Appended summary for turn ${turnId}`);
        }
      }

      // Compress long-term if too many entries
      if (countEntries('long-term.md') > MAX_LONG_TERM_ENTRIES) {
        const longTerm = readMemory('long-term.md');
        const entries = longTerm.split(/(?=^## )/m).filter(s => s.trim());
        const compressed = await compressLongTerm(entries);
        if (compressed) {
          const ts = new Date().toISOString();
          writeMemory('long-term.md', `## Compressed — ${ts}\n${compressed}\n`);
          logInfo('after-model', 'Long-term memory compressed');
        }
      }
    }

  } catch (err) {
    logError('after-model', err);
  }
  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
```

### Step 4 — `pre-compress.js`
**Trigger:** PreCompress — fires before Gemini CLI auto-compresses history

```js
const { readMemory, appendMemory } = require('./lib/memory-manager');
const { logError, logInfo } = require('./lib/logger');

async function main() {
  try {
    // Snapshot short-term before it gets wiped by compression
    const shortTerm = readMemory('short-term.md');
    if (shortTerm.trim()) {
      const ts = new Date().toISOString();
      const snapshot = `## Pre-Compress Snapshot — ${ts}\n${shortTerm}\n`;
      appendMemory('long-term.md', snapshot);
      logInfo('pre-compress', 'Saved pre-compress snapshot to long-term.md');
    }
  } catch (err) {
    logError('pre-compress', err);
  }
  // IMPORTANT: exit 0 to allow compression to proceed
  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
```

### Step 5 — `after-tool.js`
**Trigger:** AfterTool — fires after every tool invocation

```js
const { appendMemory } = require('./lib/memory-manager');
const { logError } = require('./lib/logger');

async function main() {
  try {
    const input = readStdin();
    // Expected: { event, toolName, status: 'success'|'error', durationMs }

    const { toolName = 'unknown', status = 'unknown', durationMs = 0 } = input;
    const ts = new Date().toISOString();
    const logLine = `[${ts}] TOOL ${toolName} → ${status} (${durationMs}ms)\n`;
    appendMemory('execution.md', logLine);

  } catch (err) {
    logError('after-tool', err);
  }
  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
```

### Step 6 — `session-end.js`
**Trigger:** SessionEnd — fires on CLI exit or /clear

```js
const { readMemory, writeMemory, appendMemory, countEntries } = require('./lib/memory-manager');
const { compressLongTerm, MAX_LONG_TERM_ENTRIES } = require('./lib/gemini-summarizer');
const { logError, logInfo } = require('./lib/logger');

async function main() {
  try {
    // Final compress if long-term is overloaded
    if (countEntries('long-term.md') > MAX_LONG_TERM_ENTRIES) {
      const longTerm = readMemory('long-term.md');
      const entries = longTerm.split(/(?=^## )/m).filter(s => s.trim());
      const compressed = await compressLongTerm(entries);
      if (compressed) {
        const ts = new Date().toISOString();
        writeMemory('long-term.md', `## Session End Compress — ${ts}\n${compressed}\n`);
      }
    }

    // Clear short-term memory for next session
    writeMemory('short-term.md', `# Short-term Memory\n_Cleared at session end: ${new Date().toISOString()}_\n`);
    logInfo('session-end', 'Session cleanup complete');

  } catch (err) {
    logError('session-end', err);
  }
  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
```

## Todo List
- [x] Implement stdin reader helper (shared pattern in each script)
- [x] Create `.gemini/hooks/session-start.js`
- [x] Create `.gemini/hooks/after-model.js`
- [x] Create `.gemini/hooks/pre-compress.js`
- [x] Create `.gemini/hooks/after-tool.js`
- [x] Create `.gemini/hooks/session-end.js`
- [x] Test: verify `readFileSync(0)` works on Windows with bash terminal

## Success Criteria
- Each script: runs without error when given valid JSON via stdin
- `session-start.js`: writes to `.gemini/memory/short-term.md`
- `after-model.js`: appends to long-term.md when threshold met
- `pre-compress.js`: snapshots short-term to long-term before compression
- `after-tool.js`: appends tool log line to execution.md
- `session-end.js`: clears short-term.md
- All scripts: exit 0 even on API failure

## Risk Assessment
- **stdin fd 0 on Windows** — `readFileSync(0)` works in Git Bash but not cmd.exe. Mitigation: document requirement for Git Bash/WSL; add stream-based fallback
- **AfterModel JSON schema differs from spec** — stdin fields may vary. Mitigation: use optional chaining + defaults for all fields
- **API rate limits during summarization** — `after-model.js` fires every turn. Mitigation: token threshold (25K) prevents excessive API calls

## Security Considerations
- `after-tool.js`: redact sensitive tool inputs before logging (password, token, key fields)
- Never `console.log()` — only `process.stdout.write(JSON)` for CLI, errors go to log file

## Next Steps
→ Phase 04: Testing & validation
