---
name: terminal-rules
version: "1.1.0"
description: "Cross-skill rules for OS-aware, safe terminal command execution across Windows and Unix hosts."
---

# Tool: terminal-rules

## Purpose

Cross-skill reference for OS-aware terminal execution. Import into any skill or agent
that runs shell commands to ensure safe, portable behavior across Windows and Unix hosts.

## Terminal Rules

### OS & Shell Detection
- Detect OS before executing: Windows → use Git Bash (`bash.exe`) or cmd; Unix → sh/bash
- ALWAYS use forward slashes in paths within commands — let shell handle OS conversion
- MUST NOT hardcode shell paths (e.g., no `/bin/bash` on Windows)
- If a required CLI tool is not found: return `status: "blocked"`, `error.code: "<TOOL>_NOT_FOUND"`

### Error Handling
- On non-zero exit code: return `status: "failed"`, include `exit_code` and `stderr` in result
- On timeout: return `error.code: "COMMAND_TIMEOUT"`, kill process immediately
- To intentionally ignore failure (fire-and-forget): append `|| true` to command

### Safety
- MUST NOT run destructive commands without `confirmed: true`
- Hard-blocked patterns regardless of confirmation: `rm -rf /`, `format C:`, `mkfs`, `dd if=`

### Output Capture
- Capture stdout and stderr separately
- Truncate each stream at 10 KB, set `"truncated": true` when exceeded

## Usage in Skills / Agents

```markdown
- Terminal execution: → See .gemini/tools/terminal-rules.md
```

## Applies To

All agents (enforced via `rules/agent-rules.md` AR-7) and specifically:
- `.gemini/skills/git/SKILL.md`
- `.gemini/tools/script-tool.md`
