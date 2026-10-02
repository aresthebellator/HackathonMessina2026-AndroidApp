---
name: gk-context-engineering
agent: developer
version: "1.0.0"
tier: core
description: "Monitor and optimize context usage. Use to audit token count, implement compression strategies, and manage long-running session context."
---

## Tools
- `read_file` — analyze `execution.md`, `long-term.md`, and session logs
- `write_file` — update compression status or archive session logs
- `run_shell_command` — measure token usage via CLI hooks (if available)

## Interface
- **Invoked via:** /gk-context-engineering
- **Flags:** --audit | --compress | --archive

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --audit | Audit current context usage and identify token-heavy artifacts | ./references/context-strategies.md |
| --compress | Trigger automated summarization and archiving of session history | ./references/context-strategies.md |
| --archive | Explicitly archive long-term memory to keep active context lean | ./references/context-strategies.md |
| (default) | Current token usage and pruning recommendations | (base skill rules) |

# Role
Senior Infrastructure Engineer — expert in LLM context management, token optimization, and system state persistence.

# Objective
Minimize session token overhead through active monitoring and scheduled compression, ensuring high-quality context for complex tasks.

## Gemini-Specific Optimizations
- **Long Context:** Aggregate session history from `execution.md` and memory logs to build compression summaries.
- **Google Search:** N/A — focused on internal context metrics.
- **Code Execution:** Check file sizes and log entries via `run_shell_command`.

# Input
```json
{
  "action": "string (required) — audit | compress | archive",
  "threshold_percentage": "number (optional) — trigger limit, default 80",
  "mode": "string (optional) — audit | compress | archive"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | Log files inaccessible | Check `.geminiignore` configuration. |
| FAILED | Compression failed | Fall back to manual summarization using `/gk-summarize`. |

## Steps
1. **Audit:** Measure total tokens in active files and memory.
2. **Analysis:** Identify artifacts causing token inflation (verbose logs, old task details).
3. **Action:** If `--compress`, move detailed history to `long-term.md` and truncate active logs.
4. **Finalize:** Report updated token metrics and compression status.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<context_safety_rules>
**ALWAYS enforced:**
- **Token Economy:** Active context MUST remain < 2000 tokens for optimal performance.
- **No Data Loss:** Summarize context before archiving; retain critical findings/plans in `pinned.md`.
- **Transparency:** Always inform user when context is compressed or archived.
</context_safety_rules>
- **Scheduled Pruning:** Automate pruning for sessions > 2 hours.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "tokens_before": "number",
    "tokens_after": "number",
    "savings": "number",
    "artifacts_archived": ["string"]
  },
  "summary": "one sentence describing context compression results",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "tokens_before": 4200,
    "tokens_after": 980,
    "savings": 3220,
    "artifacts_archived": ["execution.md session log (2026-04-20)", "task details from sprint-11"]
  },
  "summary": "Context compressed from 4200 to 980 tokens (77% reduction); 2 artifacts archived.",
  "confidence": "high"
}
```
