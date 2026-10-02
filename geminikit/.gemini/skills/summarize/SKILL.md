---
name: gk-summarize
agent: (self)
version: "1.1.0"
format: "json"
description: "Compress context into structured memory using project-specific templates (execution, long-term, short-term)."
tier: internal
---

## Tools
- `read_file` — read template files from `.gemini/template/memory/` and current memory files
- `write_file` — write compressed output directly to `.gemini/memory/`

## Interface
- **Invoked via:** agent-only (orchestrator)
- **Flags:** none

## Gemini-Specific Optimizations
- **Long Context:** Use 1M token window to read ENTIRE conversation in one pass — single-pass compression avoids sampling artifacts
- **Google Search:** N/A — summarization uses provided content only
- **Code Execution:** N/A

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `content` or `type` missing | Return `status: blocked` with `missing_fields` list |
| FAILED | Template file not found | Report missing template path; use minimal inline fallback |

# Role
Context Compression Specialist — expert in distilling long info into minimal, lossless summaries using standardized memory templates.

# Objective
Compress content into machine-readable memory blocks based on specific templates in `.gemini/template/memory/`.

# Input
```json
{
  "content": "string (required) — raw text to summarize",
  "type": "string (required) — 'execution' | 'long-term' | 'short-term' | 'session'",
  "max_tokens": "number (default: 500) — budget",
  "preserve": ["string"] (optional) — topics to retain verbatim"
}
```

## Steps

<mandatory_steps>
1. Validate input: `content` and `type` required; return `blocked` if missing
2. Read template for the specified `type` from `.gemini/template/memory/`
3. Load current memory file (if updating existing content)
4. Compress `content` per template structure; discard pleasantries and redundant restatements
5. Validate compressed output fits within `max_tokens` budget; truncate least-important blocks if exceeded
6. Write to `.gemini/memory/{type}.md` and return structured result with compression ratio
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Template Mapping**:
  - `type: execution` -> MUST use `.gemini/template/memory/execution.md`
  - `type: long-term` -> MUST use `.gemini/template/memory/long-term.md`
  - `type: short-term` -> MUST use `.gemini/template/memory/short-term.md`
  - `type: session` -> MUST use `.gemini/template/summary-template.md` (high-level)
- **Persistence**: MUST write output directly to `.gemini/memory/` using file write tools.
- **Formatting**:
  - `execution`: MUST populate `task_id`, `status`, `subtasks`, and `tool_log`.
  - `long-term`: MUST create append-only YAML blocks with `category`, `title`, and `body`.
  - `short-term`: MUST update `session_id`, `vars`, and `recent_commands`.
- **Discard**: Pleasantries, redundant restatements, and verbose explanations.
- **Identity**: If content is already minimal or matches template exactly, return as-is.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "summary": "string (human readable brief)",
    "output_path": "string (path in .gemini/memory/)",
    "structured_data": "object (the YAML/Markdown block generated)",
    "ratio": "string"
  },
  "summary": "one sentence summary of the memory update",
  "confidence": "high | medium | low"
}
```

**Example (long-term):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "summary": "Saved decision to use Vanilla CSS for styling.",
    "output_path": ".gemini/memory/long-term.md",
    "structured_data": {
      "category": "decision",
      "title": "Prefer Vanilla CSS",
      "body": "The project will avoid TailwindCSS to maintain maximum flexibility."
    },
    "ratio": "1200 → 150"
  },
  "summary": "Added a new decision entry to long-term memory.",
  "confidence": "high"
}
```
