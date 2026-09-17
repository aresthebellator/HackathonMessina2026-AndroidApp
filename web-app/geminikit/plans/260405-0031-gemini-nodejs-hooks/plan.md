---
title: "Gemini CLI Node.js Hooks for Auto-Summarization"
description: "Implement Node.js hook scripts triggered by Gemini CLI native hooks to auto-summarize conversations, log tool calls, and compress context"
status: completed
priority: P2
effort: 5h
issue:
branch: main
tags: [infra, feature, gemini]
created: 2026-04-05
---

# Gemini CLI Node.js Hooks for Auto-Summarization

## Overview

Implement Node.js scripts that integrate with Gemini CLI's native hook system (v0.26.0+) to automate conversation summarization, tool call logging, and context compression. Scripts communicate via JSON stdin/stdout and persist state to `.gemini/memory/`.

## Architecture

```
settings.json hooks → Node.js scripts → .gemini/memory/ ← AI reads
                           ↓
                    Gemini API (summarize)
```

## Phases

| # | Phase | Status | Effort | Link |
|---|-------|--------|--------|------|
| 1 | Setup package & config | Completed | 30m | [phase-01](./phase-01-setup-package-and-config.md) |
| 2 | Implement lib modules | Completed | 1.5h | [phase-02](./phase-02-implement-lib-modules.md) |
| 3 | Implement hook scripts | Completed | 2h | [phase-03-implement-hook-scripts.md](./phase-03-implement-hook-scripts.md) |
| 4 | Test & validate | Completed | 1h | [phase-04-testing-and-validation.md](./phase-04-testing-and-validation.md) |

## Files Created/Modified

**New:**
- `.gemini/hooks/package.json`
- `.gemini/hooks/lib/memory-manager.js`
- `.gemini/hooks/lib/gemini-summarizer.js`
- `.gemini/hooks/lib/logger.js`
- `.gemini/hooks/session-start.js`
- `.gemini/hooks/after-model.js`
- `.gemini/hooks/pre-compress.js`
- `.gemini/hooks/after-tool.js`
- `.gemini/hooks/session-end.js`

**Modified:**
- `.gemini/settings.json` — add hooks config

**Unchanged:**
- `.gemini/hooks/session-init.md`
- `.gemini/hooks/pre-tool.md`
- `.gemini/hooks/post-tool.md`

## Dependencies

- `@google/generative-ai` npm package
- `GEMINI_API_KEY` env var
- Gemini CLI v0.26.0+
- Node.js 18+
