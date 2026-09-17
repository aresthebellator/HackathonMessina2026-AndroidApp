---
name: gk-watzup
agent: developer
version: "1.0.0"
tier: core
description: "Review recent changes and wrap up the current work session. Use when finishing a coding session, summarizing work done, or planning next steps."
---

## Tools
- `run_shell_command` — execute `git log` and `git diff` to analyze session changes
- `read_file` — review `execution.md` to identify unresolved tasks
- `google_web_search` — identify standard changelog formatting patterns

## Interface
- **Invoked via:** /gk-watzup
- **Flags:** --summary | --plan

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --summary | Generate detailed summary of changes and accomplishments | ./references/wrap-up-patterns.md |
| --plan | Draft next steps for upcoming work sessions | ./references/wrap-up-patterns.md |
| (default) | Provide summary of recent changes and pending work | (base skill rules) |

# Role
Senior Developer Productivity Engineer — expert in summarizing technical work and preparing seamless handoffs between sessions.

# Objective
Summarize the current work session’s progress, identify completion status, and prepare clear plans for continuation.

## Gemini-Specific Optimizations
- **Long Context:** Read the full history of the current session to extract key accomplishments and blockers — ensure summary reflects actual work done.
- **Google Search:** N/A — focuses on internal session metadata and git logs.
- **Code Execution:** MUST run `git log` and `git diff` via `run_shell_command` to retrieve actual changes.

# Input
```json
{
  "task": "string (optional) — specific accomplishments to highlight",
  "next_steps": "string (optional) — plan for continuation",
  "mode": "string (optional) — summary | plan"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | No git repository found | Log session summary from memory only; skip git operations. |
| FAILED | Git command fails | Check if HEAD exists; report summarized activity from memory logs. |

## Steps
1. **Intake:** Check session duration and intent.
2. **Analysis:** Run `git log` and `git diff` since session start to summarize changes.
3. **Synthesis:** Read `execution.md` to compare plan status with actual outcomes.
4. **Wrap-up:** Draft session summary: what was accomplished, blockers encountered, unfinished tasks.
5. **Planning:** Propose next steps based on current progress.
6. **Finalize:** Return structured report with activity summary and continuation plan.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<watzup_accuracy_rules>
**ALWAYS enforced:**
- **Accuracy:** Accomplishments MUST map directly to git commits or documented tasks — no fabricated progress.
- **Privacy:** Redact sensitive info (secrets, PII) from logs before output.
</watzup_accuracy_rules>
- **Tone:** Professional, objective, and action-oriented.
- **Clarity:** Unfinished items MUST be clearly listed as "Continuation Tasks" for next session.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "session_summary": "string",
    "accomplishments": ["string"],
    "continuation_tasks": ["string"],
    "session_metrics": { "duration": "string", "commits": "number" }
  },
  "summary": "one sentence session summary",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "session_summary": "Implemented JWT refresh rotation and GitHub OAuth2 PKCE; refactored bridge.js into 3 modules.",
    "accomplishments": [
      "feat: JWT refresh rotation (src/auth/jwt-utils.ts)",
      "feat: GitHub OAuth2 PKCE flow (src/auth/github-oauth.ts)",
      "refactor: bridge.js → bridge-utils.js + bridge-manage.js"
    ],
    "continuation_tasks": [
      "Add rate limiting to /auth/refresh endpoint",
      "Write E2E tests for OAuth callback"
    ],
    "session_metrics": { "duration": "2h 15m", "commits": 3 }
  },
  "summary": "Session wrapped: 3 commits across auth and bridge refactor; 2 items deferred.",
  "confidence": "high"
}
```
