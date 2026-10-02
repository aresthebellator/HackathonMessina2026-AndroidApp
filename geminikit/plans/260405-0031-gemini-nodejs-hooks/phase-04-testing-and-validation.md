# Phase 04 — Testing & Validation

## Context Links
- Plan: [plan.md](./plan.md)
- Phase 03 (hook scripts): [phase-03-implement-hook-scripts.md](./phase-03-implement-hook-scripts.md)

## Overview
- **Priority:** P2
- **Status:** Completed
- **Effort:** 1h

Validate all hooks fire correctly and produce expected output in `.gemini/memory/`.

## Requirements

**Functional:**
- Each hook script handles valid + malformed JSON without crashing
- Memory files updated correctly after each hook fires
- Gemini CLI recognizes hook config and executes scripts

**Non-functional:**
- Test without real Gemini CLI session (unit test via stdin pipe)
- Verify Windows path compatibility

## Test Strategy

### Manual unit tests (no test framework needed)

Each script can be tested by piping JSON via stdin:

```bash
# Test session-start.js
echo '{"event":"SessionStart","sessionId":"test-123"}' | node .gemini/hooks/session-start.js

# Test after-model.js (below threshold — no summarization)
echo '{"event":"AfterModel","turnId":1,"stats":{"totalTokens":1000},"response":{"text":"hello"}}' | node .gemini/hooks/after-model.js

# Test after-model.js (above threshold — triggers summarization)
echo '{"event":"AfterModel","turnId":10,"stats":{"totalTokens":30000},"response":{"text":"..."}}' | node .gemini/hooks/after-model.js

# Test pre-compress.js
echo '{"event":"PreCompress"}' | node .gemini/hooks/pre-compress.js

# Test after-tool.js
echo '{"event":"AfterTool","toolName":"read_file","status":"success","durationMs":42}' | node .gemini/hooks/after-tool.js

# Test session-end.js
echo '{"event":"SessionEnd","sessionId":"test-123"}' | node .gemini/hooks/session-end.js
```

### Expected outputs (stdout)
All scripts must output valid JSON:
```json
{"status":"ok"}
```

### Expected side effects

| Script | Expected side effect |
|--------|----------------------|
| `session-start.js` | `.gemini/memory/short-term.md` written with session context |
| `after-model.js` (over threshold) | Entry appended to `.gemini/memory/long-term.md` |
| `after-model.js` (under threshold) | No file changes |
| `pre-compress.js` | Snapshot appended to `.gemini/memory/long-term.md` |
| `after-tool.js` | Log line appended to `.gemini/memory/execution.md` |
| `session-end.js` | `short-term.md` reset, `long-term.md` compressed if > 15 entries |

### Error resilience tests

```bash
# Test with empty stdin
echo '{}' | node .gemini/hooks/after-model.js
# Expected: exits 0, no crash, {"status":"ok"}

# Test with malformed JSON
echo 'not json' | node .gemini/hooks/after-model.js
# Expected: exits 0, error logged to errors.log

# Test with missing GEMINI_API_KEY
GEMINI_API_KEY="" node -e "
  process.stdin.push(JSON.stringify({event:'AfterModel',turnId:10,stats:{totalTokens:30000}}));
  process.stdin.end();
" | node .gemini/hooks/after-model.js
# Expected: exits 0, null summary skipped, no crash
```

### Integration test — verify settings.json loads

```bash
# Start Gemini CLI and check if hooks fire:
gemini --version  # Verify >= 0.26.0

# In gemini session, run a simple prompt:
# > hello
# Then exit and check:
cat .gemini/memory/short-term.md   # should have session context
cat .gemini/memory/execution.md    # should have tool log entries
cat .gemini/hooks/errors.log       # should be empty or not exist
```

## Related Code Files
- **Read:** All files in `.gemini/hooks/` and `.gemini/memory/`
- **Read:** `.gemini/settings.json`

## Implementation Steps

1. Run unit tests for each script via stdin pipe (section above)
2. Verify `{"status":"ok"}` stdout for each
3. Check `.gemini/memory/` files updated correctly
4. Run error resilience tests (empty input, malformed JSON, missing API key)
5. Verify `.gemini/hooks/errors.log` captures errors silently
6. Start real Gemini CLI session, run 1-2 prompts, verify hooks fire
7. Check `long-term.md` after 10+ turns or 25K+ tokens

## Todo List
- [x] Run all unit tests via stdin pipe
- [x] Verify stdout is valid JSON `{"status":"ok"}` for each script
- [x] Verify side effects in `.gemini/memory/` files
- [x] Run error resilience tests (empty/malformed JSON, missing API key)
- [x] Start real Gemini CLI session and verify hooks fire
- [x] Confirm `.gemini/hooks/errors.log` is empty after clean run
- [x] Verify Windows path resolution (`GEMINI_PROJECT_DIR` + path.join)

## Success Criteria
- All 5 scripts exit 0 with `{"status":"ok"}` for valid and invalid inputs
- Memory files updated as expected
- No unhandled errors in real Gemini CLI session
- `errors.log` empty after clean run

## Risk Assessment
- **`AfterModel` stdin schema differs from expected** → log raw stdin to errors.log on first run to inspect real format; adjust field names accordingly
- **Hooks not firing (Gemini CLI version < 0.26.0)** → run `gemini --version`, update if needed
- **Windows Git Bash stdin pipe** → test explicitly with `echo '{}' | node script.js` in Git Bash terminal

## Notes
- If real `AfterModel` stdin schema differs from brainstorm spec, update `after-model.js` field destructuring
- Add `LOG_LEVEL=debug` env to see info logs during testing: `LOG_LEVEL=debug echo '...' | node after-model.js`
