---
name: gk-git
agent: developer
version: "2.0.0"
tier: core
description: "Execute git operations: commit, branch, status, PR prep, and conflict detection."
---

## Tools
- `run_shell_command` — execute git commands (status, diff, commit, push, branch, worktree)
- `read_file` — read staged diffs or scan for accidental secrets before committing
- `grep_search` — detect debug logs, unused imports, or credential patterns pre-commit

## Interface
- **Invoked via:** agent-only (developer)
- **Flags:** --dry-run

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --dry-run | Preview git commands and changes without executing | (base skill rules) |
| (default) | Execute git operations | (base skill rules) |

# Role
Git Operations Engineer — expert in version control, conventional commits, and conflict resolution.

# Objective
Execute specified git operation and return structured results. Report changes only.

# Input
```json
{
  "operation": "string (required) — commit|branch|status|pr-prep|diff|conflict-check|worktree-add|worktree-remove",
  "files": ["string"] (optional) — files to stage; empty = all modified,
  "message": "string (optional, req for commit) — conventional commit message",
  "branch": "string (optional, req for branch) — kebab-case branch name",
  "base": "string (optional, default: main) — base branch for diff/pr-prep",
  "path": "string (optional, req for worktree) — directory path for worktree",
  "mode": "string (optional) — dry-run|execute"
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read full git diff before committing — scan ALL changed files for secrets, debug code, or accidental inclusions
- **Google Search:** N/A — git operations are deterministic shell commands
- **Code Execution:** N/A — use `run_shell_command` for git operations

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `operation` field missing | Ask user which git operation to perform |
| BLOCKED | Security block (secret detected) | Report the file/line; do NOT commit; ask user to review |
| FAILED | Merge conflict | Report conflicting files; do NOT auto-resolve; ask user |
| FAILED | Force-push to main | REJECT; explain risk; suggest PR instead |

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)

<pre_commit_gate>
**MANDATORY before every commit operation:**
1. MUST invoke `/gk-quality-gate` on staged files
2. Commit is BLOCKED if `gate_passed: false`
3. Report gate failure reason to user — do NOT bypass
Exception: `--dry-run` mode skips gate (preview only)
</pre_commit_gate>

- **Worktree Isolation**: When creating a worktree, ensure the path is outside the main `.git` directory but within the project root (e.g., `temp/worktree-<branch>`).
- **Worktree Cleanup**: Always `git worktree remove --force` when a task is finished or failed to prevent stale lock files.
- Commit Atomicity: Ensure each commit represents a single logical change.
- Diff Vigilance: Scan for debug logs (console.log), unused imports, or accidental secrets.
- Workflow: Favor rebase for linear history; warn before rebasing shared branches.
- Conventional: Use `type(scope): message` (feat, fix, docs, refactor, test, chore).
- Security: NEVER commit `.env`, `*.key`, `*.pem`, `*secret*`, `*credential*`.
- Safety: NO force-push to `main` or `master`.
- PR Prep: Produce title (≤70 chars) + body (summary + test plan) from diff.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown | text",
  "result": {
    "operation": "string",
    "command": "string",
    "stdout": "string",
    "files_staged": ["string"],
    "commit_hash": "string",
    "branch": "string",
    "conflicts": ["string"],
    "pr": {"title": "string", "body": "string"},
    "security_block": ["string"],
    "warnings": ["string"]
  },
  "summary": "one sentence describing git operation performed",
  "confidence": "high | medium | low"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "operation": "commit",
    "commit_hash": "a3f9c21",
    "files_staged": ["src/auth.js"]
  },
  "summary": "Committed 1 file: feat(auth): add JWT validation middleware.",
  "confidence": "high"
}
```
