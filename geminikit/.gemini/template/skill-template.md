---
name: gk-[skill-name]
agent: [primary-agent-name]
version: "1.0.0"
tier: core                   # core | optional | internal
description: "One sentence: what this skill does (action-oriented)"
---

<!-- Save as: .gemini/skills/<skill-name>/SKILL.md -->

## Tools
- `read_file` — read specific files or code blocks to understand implementation
- `grep_search` — identify patterns or symbols across the codebase
- `google_web_search` — research external documentation, CVEs, or best practices
- `run_shell_command` — execute build, test, or lint commands to verify changes
- `write_file` — implement or update files once changes are verified

## Interface
- **Invoked via:** /gk-[skill-name]
- **Flags:** --flag1 | --flag2
- **Errors:** ERROR_CODE_1, ERROR_CODE_2

## Mode Mapping (Optional — remove if skill has no flags)
| Flag | Description | Reference |
|------|-------------|-----------|
| --mode-name | What this mode does | ./references/mode-name.md |
| (default) | Base skill behavior | (base skill rules) |

## Initialization (Optional — remove if skill doesn't track state)
Before starting, write skill state to enable hook context injection:
```json
{
  "skill": "gk-[skill-name]",
  "session_id": "<current-session-id>",
  "timestamp": "<ISO-timestamp>",
  "slug": "<task-slug>"
}
```
Write to: `.gemini/.skill-state.json`

# Role
[Specific expert role — e.g., "Senior Technical Architect"]

# Objective
[Single, clear task — one sentence only. Start with a verb. E.g., "Analyze codebase for architectural patterns."]

## Gemini-Specific Optimizations
- **Long Context:** Leverage 1M+ token window by reading entire files or the full plan context before executing — avoid partial reads that miss dependencies.
- **Google Search:** Use for real-time information (API docs, latest library versions, CVE details) that might not be in the training data.
- **Code Execution:** MUST run build and test commands via `run_shell_command` to verify each task before marking as complete.

# Input
```json
{
  "project_path": "string (required) — path to root",
  "task_description": "string (required) — details of the request",
  "constraints": "object (optional) — { budget, timeline, stack }"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `required_field` missing | Ask user to provide the missing field via `ask_user`. |
| FAILED | Tool execution error | Diagnose error output, backtrack 1 step, and try an alternative approach. |
| FAILED | Verification fails | Re-examine implementation, fix issues, and re-run verification. |

## Steps
1. **Intake:** Validate input parameters and clarify ambiguous requirements with the user.
2. **Research:** Use `grep_search` and `read_file` to map the codebase and understand current patterns.
3. **Strategy:** Formulate a plan and share a concise summary with the user.
4. **Execution:** Apply surgical changes using `write_file` or `replace`.
5. **Verification:** Run tests/lint/build via `run_shell_command` to confirm correctness.
6. **Finalize:** Save execution report and return the structured JSON result.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **JSON Output Ban** — NEVER render raw JSON blocks in user-facing responses. Use Markdown for all communication.
- **Verify Before Update** — MUST run relevant tests or build commands before marking a task as completed.
- **Context Economy** — Use targeted reads (line ranges) for large files but read enough to ensure correctness.
- **Security First** — Redact secrets/PII from all outputs and logs.

# Output
```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "report_path": "string (optional) — Path to the execution report",
    "artifacts": ["string"]
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
  "result": { "report_path": "reports/skill/260427-task.md" },
  "summary": "Task completed successfully after verification.",
  "confidence": "high"
}
```
