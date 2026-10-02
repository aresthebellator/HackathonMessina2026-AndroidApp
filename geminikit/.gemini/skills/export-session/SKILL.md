---
name: gk-export-session
agent: developer
version: "1.2.0"
tier: internal
format: "json"
description: "Exports the current session state and conversation summary for continuation in a new session."
---

## Tools
- `read_file` — read `.gemini/memory/execution.md` for current task state
- `write_file` — save export file to `reports/export-session/`

## Interface
- **Invoked via:** /gk-export-session
- **Flags:** none

# Role
Session Portability Specialist — expert in packaging current session state into a portable, resumable format.

# Objective
Generate a structured export block containing task state from `execution.md` and a compressed conversation summary. Allows resuming work in a new session without context loss.

# Input
```json
{
  "conversation_history": "string (required) — full history of the current conversation"
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read the entire `execution.md` and conversation history in one pass — no sampling
- **Google Search:** N/A — session export is local state packaging
- **Code Execution:** N/A

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `conversation_history` missing | Ask user to provide conversation context |
| FAILED | `execution.md` not found | Report missing file; create minimal state from conversation summary |

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Artifact Management (Rule 05_6):** Save export to `reports/export-session/{YYMMDD-HHmm}-session-export.md`.
- MUST read `.gemini/memory/execution.md` before composing export.
- MUST invoke `gk-summarize` with `type: "session"` to compress conversation history.
- MUST produce a self-contained block the user can paste as first prompt in a new session.
- MUST include instructions for how to use the exported block.

<export_scope_rule>
**Scope distinct from gk-summarize and gk-watzup:**
- gk-summarize: internal memory compression for agent use
- gk-watzup: session wrap-up with next steps for current session
- gk-export-session: portable block for starting a NEW session
</export_scope_rule>

## Steps

<mandatory_steps>
1. Read `.gemini/memory/execution.md` — capture current task_id, status, subtasks, and tool_log
2. Invoke `gk-summarize` with `type: "session"` on `conversation_history` — get compressed summary
3. Compose export block combining task state + summary with resume instructions
4. Save export block to `reports/export-session/{YYMMDD-HHmm}-session-export.md`
5. Return structured JSON result with `export_path` and `display` markdown copy-paste block
</mandatory_steps>

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "export_path": "string — path to saved export file",
    "task_state": {
      "task_id": "string",
      "status": "string",
      "subtasks_completed": "number",
      "subtasks_total": "number"
    },
    "summary": {
      "decisions": ["string"],
      "artifacts": ["string"],
      "blockers": ["string"],
      "next_steps": ["string"]
    },
    "resume_instructions": "string — how to use the export block"
  },
  "summary": "Session exported with N subtasks and M decisions; paste export to resume.",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "export_path": "reports/export-session/260427-1430-session-export.md",
    "task_state": { "task_id": "add-auth", "status": "in_progress", "subtasks_completed": 3, "subtasks_total": 7 },
    "summary": {
      "decisions": ["Using JWT with 15-min expiry", "Refresh tokens stored in HttpOnly cookies"],
      "artifacts": ["src/auth.js", "src/middleware/jwt.js"],
      "blockers": [],
      "next_steps": ["Implement refresh token endpoint", "Add rate limiting to /login"]
    },
    "resume_instructions": "Paste the content of reports/export-session/260427-1430-session-export.md as your first message in a new session."
  },
  "summary": "Session exported: 3/7 subtasks done, 2 decisions recorded; ready to resume.",
  "confidence": "high"
}
```
