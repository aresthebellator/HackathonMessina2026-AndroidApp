---
name: gk-journal
agent: developer
version: "1.0.0"
tier: optional
description: "Write technical journal entries about session work, changes made, and lessons learned. Use after completing features, fixing bugs, or finishing implementation sessions."
---

## Tools
- `read_file` — read `execution.md` to capture context for the journal entry
- `write_file` — save the journal entry to the `plans/journals/` directory
- `run_shell_command` — check git status to ensure entry captures recent changes

## Interface
- **Invoked via:** /gk-journal
- **Flags:** --today | --feature | --bug | --lesson

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --today | Quick summary of daily progress | ./references/journal-templates.md |
| --feature | Detailed record of feature implementation details | ./references/journal-templates.md |
| --bug | Technical breakdown of a bug, root cause, and fix analysis | ./references/journal-templates.md |
| --lesson | Record architectural decisions or "aha!" moments | ./references/journal-templates.md |
| (default) | Standard journal entry format | (base skill rules) |

# Role
Technical Writer / Lead Developer — expert in maintaining project history, documenting architectural decisions, and capturing engineering insights.

# Objective
Create structured, searchable technical journal entries that document progress, decisions, and engineering insights for future reference.

## Gemini-Specific Optimizations
- **Long Context:** Read the `execution.md` file to gather session context before prompting for entry content.
- **Google Search:** N/A — focuses on internal project knowledge.
- **Code Execution:** MUST check current git status to confirm entry captures accurate work context.

# Input
```json
{
  "title": "string (required) — brief title for the entry",
  "content": "string (required) — details/insights to record",
  "tags": ["string (optional)"],
  "mode": "string (optional) — today | feature | bug | lesson"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | Missing title/content | Ask user for title and content details before saving entry via `ask_user`. |
| FAILED | WRITE_FAILURE | Check permissions for `plans/journals/` directory. |

## Steps
1. **Intake:** Validate journal title and content.
2. **Context:** Read `execution.md` to gather session context.
3. **Draft:** Format journal entry with timestamp, session context, and content.
4. **Validation:** Check `git status` to ensure entry content matches repository activity.
5. **Execution:** Write entry to `plans/journals/YYYY-MM-DD-slug.md`.
6. **Finalize:** Return structured result confirming save path.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<journal_integrity_rules>
**ALWAYS enforced:**
- **Structure:** Entries MUST include: Title, Timestamp, Context, Details, Tags.
- **Integrity:** Entries MUST accurately reflect the session's work; do not embellish.
- **Persistence:** All entries MUST be stored in `plans/journals/`.
</journal_integrity_rules>
- **Searchability:** Use descriptive, searchable titles (e.g., `260427-auth-flow-fix.md`).
- **Traceability:** Link to commit IDs or PR numbers if available.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "file_path": "string",
    "timestamp": "string",
    "tags": ["string"]
  },
  "summary": "one sentence summarizing the entry",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "file_path": "plans/journals/260427-auth-flow-refactor.md",
    "timestamp": "2026-04-27T14:35:00Z",
    "tags": ["auth", "refactor", "jwt"]
  },
  "summary": "Journal entry written: auth flow refactor — JWT refresh rotation and PKCE implementation notes.",
  "confidence": "high"
}
```
