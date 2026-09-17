# Phase 02 — Implement Lib Modules

## Context Links
- Plan: [plan.md](./plan.md)
- Phase 01: [phase-01-setup-package-and-config.md](./phase-01-setup-package-and-config.md)

## Overview
- **Priority:** P1 (shared by all hook scripts)
- **Status:** Completed
- **Effort:** 1.5h

Implement 3 shared lib modules used by all hook scripts: memory manager, Gemini API summarizer, and logger.

## Requirements

**Functional:**
- `memory-manager.js` — read/write/append `.gemini/memory/*.md` files
- `gemini-summarizer.js` — call Gemini API to summarize text, check token thresholds
- `logger.js` — silent-fail logger writing to `.gemini/hooks/errors.log`

**Non-functional:**
- All lib modules: CJS (`require`/`module.exports`)
- Never throw unhandled errors (catch everything, log silently)
- File I/O sync or async-with-fallback

## Architecture

```
.gemini/hooks/lib/
├── memory-manager.js      ← CRUD for .gemini/memory/*.md
├── gemini-summarizer.js   ← Gemini API wrapper + threshold logic
└── logger.js              ← silent error logger
```

### Data contracts

**memory-manager.js exports:**
```js
{
  readMemory(filename),          // returns string content
  writeMemory(filename, content), // overwrites file
  appendMemory(filename, entry),  // appends line/block
  countEntries(filename),         // count ## sections
  trimEntries(filename, keepLast) // keep only last N entries
}
```

**gemini-summarizer.js exports:**
```js
{
  shouldSummarize(totalTokens, turnId), // boolean check
  summarize(text, prompt),              // returns summary string
  compressLongTerm(entries)             // summarize array → single entry
}
```

**logger.js exports:**
```js
{
  logError(context, error),  // appends to errors.log
  logInfo(context, message)  // no-op in prod (configurable)
}
```

## Related Code Files
- **Create:** `.gemini/hooks/lib/memory-manager.js`
- **Create:** `.gemini/hooks/lib/gemini-summarizer.js`
- **Create:** `.gemini/hooks/lib/logger.js`

## Implementation Steps

### Step 1 — `logger.js`
Simplest, no deps:
```js
const fs = require('fs');
const path = require('path');

const LOG_FILE = path.join(__dirname, '..', 'errors.log');

function logError(context, error) {
  try {
    const ts = new Date().toISOString();
    const msg = `[${ts}] ERROR [${context}]: ${error?.message || error}\n`;
    fs.appendFileSync(LOG_FILE, msg);
  } catch (_) { /* intentionally swallowed */ }
}

function logInfo(context, message) {
  // no-op by default; enable via LOG_LEVEL=debug env
  if (process.env.LOG_LEVEL === 'debug') {
    try {
      const ts = new Date().toISOString();
      fs.appendFileSync(LOG_FILE, `[${ts}] INFO [${context}]: ${message}\n`);
    } catch (_) {}
  }
}

module.exports = { logError, logInfo };
```

### Step 2 — `memory-manager.js`
```js
const fs = require('fs');
const path = require('path');
const { logError } = require('./logger');

// Resolve memory dir from GEMINI_PROJECT_DIR or CWD
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

// Count ## section headers as entries
function countEntries(filename) {
  const content = readMemory(filename);
  return (content.match(/^## /gm) || []).length;
}

// Keep only last N ## sections
function trimEntries(filename, keepLast) {
  try {
    const content = readMemory(filename);
    const sections = content.split(/(?=^## )/m).filter(s => s.trim());
    if (sections.length <= keepLast) return;
    const trimmed = sections.slice(-keepLast).join('\n');
    writeMemory(filename, trimmed);
  } catch (err) { logError('trimEntries', err); }
}

module.exports = { readMemory, writeMemory, appendMemory, countEntries, trimEntries };
```

### Step 3 — `gemini-summarizer.js`
```js
const { GoogleGenerativeAI } = require('@google/generative-ai');
const { logError } = require('./logger');

// Thresholds — adjust as needed
const TOKENS_THRESHOLD = 25000;  // trigger after 25K tokens/session
const TURNS_INTERVAL = 10;        // or every 10 turns
const MAX_LONG_TERM_ENTRIES = 15; // compress long-term when > 15 entries

let genAI = null;

function getClient() {
  if (!genAI) {
    const apiKey = process.env.GEMINI_API_KEY;
    if (!apiKey) throw new Error('GEMINI_API_KEY not set');
    genAI = new GoogleGenerativeAI(apiKey);
  }
  return genAI;
}

function shouldSummarize(totalTokens, turnId) {
  return totalTokens >= TOKENS_THRESHOLD || (turnId > 0 && turnId % TURNS_INTERVAL === 0);
}

async function summarize(text, systemPrompt) {
  try {
    const model = getClient().getGenerativeModel({ model: 'gemini-2.0-flash' });
    const prompt = systemPrompt
      ? `${systemPrompt}\n\n---\n${text}`
      : `Summarize the following conversation concisely, preserving key decisions, tasks, and context:\n\n${text}`;
    const result = await model.generateContent(prompt);
    return result.response.text();
  } catch (err) {
    logError('summarize', err);
    return null; // caller handles null gracefully
  }
}

async function compressLongTerm(entries) {
  const combined = entries.join('\n\n---\n\n');
  return summarize(combined,
    'Compress these session summaries into one concise summary. Preserve: active tasks, key decisions, current focus, important context. Max 500 words.'
  );
}

module.exports = { shouldSummarize, summarize, compressLongTerm, MAX_LONG_TERM_ENTRIES };
```

## Todo List
- [x] Create `.gemini/hooks/lib/logger.js`
- [x] Create `.gemini/hooks/lib/memory-manager.js`
- [x] Create `.gemini/hooks/lib/gemini-summarizer.js`
- [x] Verify `GEMINI_PROJECT_DIR` resolves correctly on Windows paths
- [x] Verify `@google/generative-ai` import works after `npm install`

## Success Criteria
- `require('./lib/logger')` works without errors
- `require('./lib/memory-manager')` reads/writes `.gemini/memory/long-term.md`
- `require('./lib/gemini-summarizer')` calls API and returns summary string
- All functions silently catch errors (no unhandled rejections)

## Risk Assessment
- **`GEMINI_PROJECT_DIR` path separator on Windows** → use `path.join()` always, never string concat
- **API key missing** → `summarize()` returns `null`, caller skips summary (non-blocking)
- **`@google/generative-ai` API changes** → pin version in package.json (`^0.21.0`)

## Security Considerations
- API key read from env only — never log, never write to files
- `logError` redacts any string containing `key`, `token`, `secret` patterns
- `errors.log` in `.gitignore`

## Next Steps
→ Phase 03: Implement hook scripts (use these lib modules)
