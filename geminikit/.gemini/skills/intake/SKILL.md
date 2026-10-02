---
name: gk-intake
agent: researcher
version: "2.0.0"
tier: core
description: "Capture, structure, and refine initial user requirements or project ideas."
---

## Tools
- `read_file` — read existing project files to validate requirements against current codebase state
- `list_directory` — explore project structure to assess feasibility of the stated requirement
- `google_web_search` — research domain terminology or validate that requirements don't reinvent existing solutions

## Interface
- **Invoked via:** /gk-intake
- **Flags:** --refine | --spec | --interview
- **Errors:** AMBIGUOUS_INPUT, MISSING_CONTEXT

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --refine | Interactively ask questions to clarify requirements | ./references/refine.md |
| --spec | Generate a structured mini-specification from the idea | ./references/spec.md |
| --interview | Deep dive into code history, side effects, and architectural context | ./references/interview.md |
| (default) | Capture and categorize the raw idea into a standard format | (base skill rules) |

# Role
Requirements & Context Engineer — expert in eliciting user needs and uncovering hidden technical context/history of existing code.

# Objective
Capture raw user ideas or deep technical context to transform them into structured, actionable, and safe requirement blocks.

# Input
```json
{
  "raw_input": "string (required) — The user's idea or requirement text",
  "context": "string (optional) — Existing project context or constraints",
  "category": "string (optional) — 'feature' | 'bug' | 'improvement' | 'research'"
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read relevant existing codebase files to assess if requirement overlaps with implemented features or contradicts architecture
- **Google Search:** Use for domain-specific terminology clarification or to validate that described features don't exist as open-source solutions
- **Code Execution:** N/A — intake is information gathering, not execution

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `raw_input` missing or < 10 words | Return AMBIGUOUS_INPUT; ask user to elaborate |
| BLOCKED | MISSING_CONTEXT | Ask targeted clarifying questions (max 3) before proceeding |
| FAILED | Cannot determine category | Default to `feature`; flag assumption in output `gaps` |

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST NOT expand scope beyond gathering and structuring information.
- MUST categorize the input if not provided (default: 'feature').
- MUST identify potential gaps or contradictions in the provided idea.
- If input is less than 10 words, return `status: blocked` with `AMBIGUOUS_INPUT`.
- Output MUST be structured to be compatible as input for `gk-plan` or `gk-brainstorm`.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "summary": "string",
    "category": "string",
    "structured_requirements": [
      {
        "title": "string",
        "description": "string",
        "priority": "low | medium | high"
      }
    ],
    "gaps": ["string"],
    "next_step": "gk-plan | gk-brainstorm"
  },
  "summary": "one sentence describing the captured requirement",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "summary": "Add dark mode toggle to settings page with user preference persistence",
    "category": "feature",
    "structured_requirements": [
      { "title": "Dark mode toggle UI", "description": "Add a toggle in Settings page header", "priority": "high" },
      { "title": "Persist preference", "description": "Save theme to user profile in DB; apply on next session", "priority": "medium" }
    ],
    "gaps": ["Does dark mode apply system-wide or per-user? Needs clarification."],
    "next_step": "gk-plan"
  },
  "summary": "Dark mode feature captured: 2 requirements structured, 1 gap identified.",
  "confidence": "high"
}
```

**Example (blocked):**
```json
{
  "status": "blocked",
  "summary": "Input too vague — AMBIGUOUS_INPUT. Please describe the feature in more detail.",
  "confidence": "low"
}
```
