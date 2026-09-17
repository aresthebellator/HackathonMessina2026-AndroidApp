# Tool: script-tool

## Purpose
Shell script execution wrapper. Runs whitelisted commands inside the project
directory with output capture, timeout enforcement, and sandbox restrictions.
Always requires explicit user approval.

## Requires Approval
Yes — every invocation requires `confirmed: true`. No exceptions.

## Input

```json
{
  "script": "string (required) — shell command or script body to execute",
  "cwd": "string (optional, default: working_dir) — execution directory (must be within working_dir)",
  "env": "object (optional) — additional environment variables { KEY: value }",
  "timeout_ms": "integer (optional, default: 30000) — max 60000",
  "shell": "string (optional, default: sh) — sh | bash | zsh | pwsh",
  "confirmed": "boolean (required) — must be true to execute"
}
```

## Output

```json
{
  "status": "ok | error",
  "stdout": "string — standard output (truncated to 100KB if exceeded)",
  "stderr": "string — standard error output",
  "exit_code": "integer — process exit code (0 = success)",
  "duration_ms": "integer — wall-clock execution time",
  "error": "string | null — wrapper-level error (timeout, sandbox violation, etc.)"
}
```

## Safety Constraints

| Rule | Detail |
|------|--------|
| Always confirmed | `confirmed: true` mandatory — blocked otherwise |
| Sandbox to project | `cwd` must be within `working_dir`; `../` escape rejected |
| Command blocklist | Hard-blocked patterns regardless of `confirmed` status |
| No privilege escalation | `sudo`, `su`, `doas`, `runas` always blocked |
| Timeout hard cap | 60 000 ms maximum; process killed with SIGTERM then SIGKILL |
| Output cap | stdout/stderr each truncated to 100 KB |
| No env credential leak | Env vars named `*_TOKEN`, `*_KEY`, `*_SECRET`, `*_PASSWORD` redacted in logs |

## Command Blocklist (always blocked)

```
rm -rf /
rm -rf ~
mkfs
dd if=
curl | sh
wget | sh
:(){ :|:& };:      # fork bomb
chmod -R 777 /
> /etc/passwd
```

## Allowed Command Examples

```bash
# Build and test
npm run build
npm test
python -m pytest tests/

# File operations (within project)
cp src/file.ts dist/file.ts
mkdir -p src/new-module

# Git operations
git status
git diff HEAD
git log --oneline -10

# Linting
npm run lint
ruff check .
```

## Example Call

```json
{
  "script": "npm run build && npm test",
  "cwd": "/Users/dev/geminikit",
  "env": { "NODE_ENV": "test" },
  "timeout_ms": 60000,
  "shell": "bash",
  "confirmed": true
}
```

## Terminal Rules
→ See `.gemini/tools/terminal-rules.md`

## Notes
- Prefer individual tool calls (`read_file`, `write_file`) over scripts where possible.
- Use `script-tool` only when a sequence of shell operations is necessary.
- Output over 100 KB is truncated with notice: `[OUTPUT TRUNCATED — 100KB limit]`
