---
name: gk-execute
agent: developer
version: "2.0.0"
tier: core
description: "Execute Markdown-based implementation plans by parsing, executing tasks, and updating status."
---

## Tools
- `read_file` — read plan.md and phase files; read source files before implementing tasks
- `write_file` — implement code changes and update task status in plan files
- `run_shell_command` — run build/test/lint commands to verify task completion
- `list_directory` — explore project structure when context is unclear

## Interface
- **Command:** `/gk-execute`
- **Agent:** `developer`

| Flag | Mode | Description |
|------|------|-------------|
| `--dry-run` | `dry-run` | Parse and simulate execution without modifying any files. |
| `--verbose` | `verbose` | Provide detailed logs and intermediate states during execution. |

## References
- `references/plan-format.md` — Guide on Markdown plan structure and status icons.

# Role
Senior Execution Engineer & Plan Orchestrator

# Objective
Systematically process and execute tasks from a Markdown implementation plan while maintaining real-time status updates.

# Input
```json
{
  "plan_path": "string (required) — Path to the Markdown plan file",
  "task_id": "string (optional, default: next) — Specific task description to execute",
  "dry_run": "boolean (optional, default: false) — If true, only parse and report without executing"
}
```

## Initialization (Required)
Before starting, write skill state to enable hook context injection:
```json
{
  "skill": "gk-execute",
  "session_id": "<current-session-id>",
  "timestamp": "<ISO-timestamp>"
}
```
Write to: `.gemini/.skill-state.json`
The BeforeAgent hook will inject the active plan path and pending phases — **do NOT scan `plans/` manually**.

## Gemini-Specific Optimizations
- **Long Context:** Read the ENTIRE plan file and all referenced source files before executing — prevents partial implementation from missing dependencies
- **Google Search:** N/A for plan execution — implementation follows the plan spec, not external research
- **Code Execution:** MUST run build and test commands via `run_shell_command` to verify each task before marking complete

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `plan_path` missing or file not found | Ask user to provide the plan file path |
| BLOCKED | No pending tasks in plan | Report completion status; do not fabricate tasks |
| FAILED | Build/test fails after implementation | Update task to `status: failed`; report error; do NOT mark as complete |
| FAILED | Task scope unclear | Read full plan context; ask user to clarify if still ambiguous |

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Verify Before Update** — MUST run relevant tests or build commands before marking a task as completed `[x]`.
- **Deterministic Status** — MUST use `scripts/parse_plan.js` to identify tasks and `scripts/update_status.js` for updates to ensure consistency.
- **No Scope Expansion** — MUST only perform the specific task identified in the plan.
- MUST flag uncertainty: include `"confidence": "low"` when evidence or execution results are ambiguous.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "report_path": "string (optional) — Path to the execution report",
    "current_task": "string — The description of the task processed",
    "progress": "object — { total, completed, percentage }",
    "next_task": "string (optional) — The next task in the queue"
  },
  "summary": "one sentence describing the execution result",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "report_path": "reports/gk-execute/260427-1430-report.md",
    "current_task": "Implement POST /users endpoint with Zod validation",
    "progress": { "total": 8, "completed": 3, "percentage": 37 },
    "next_task": "Add rate limiting middleware to auth routes"
  },
  "summary": "Task 3/8 completed: POST /users endpoint implemented and tests pass.",
  "confidence": "high"
}
```

<required_verification>
## Workflow Details — Verify step is MANDATORY before marking complete
1. **Parse** — `node .gemini/skills/gk-execute/scripts/parse_plan.js <plan_path>`
2. **Execute** — perform actions in `currentTask`
3. **VERIFY** — run tests/lint/build; task is BLOCKED if verification fails
4. **Review** — invoke `/gk-review --post-implement` on modified files; block if `plan_alignment: missing` or critical security finding
5. **Update** — `node .gemini/skills/gk-execute/scripts/update_status.js <plan_path> "<task>" "x"` ONLY after steps 3 and 4 pass
6. **Log** — create report in `reports/gk-execute/` per Rule 05_6
**NEVER mark a task `[x]` without passing verification (step 3) AND review (step 4).**
</required_verification>
