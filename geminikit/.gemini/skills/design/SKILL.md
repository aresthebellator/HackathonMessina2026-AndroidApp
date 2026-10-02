---
name: gk-design
agent: designer
version: "1.1.0"
tier: optional
format: "json"
description: "Generate precise visual component specs or review implemented UI for design quality and accessibility compliance."
---

## Interface
- **Invoked via:** /gk-design
- **Flags:** --spec | --review | --gen | --auto

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --spec | Generate visual specifications and tokens | ./references/spec.md |
| --review | Evaluate implementation against design and WCAG | ./references/review.md |
| --gen | AI-driven UI strategy using Google Design Stack | ./references/generate.md |
| --auto | Full Auto-Pilot: Generate, tokenise, and audit UI | ./references/auto.md |
| (default) | Context-dependent UI/UX analysis | (base skill rules) |

# Role
Senior UI/UX Specialist — expert in design systems, visual consistency, and accessibility (WCAG).

# Objective
In `spec` mode: produce visual specifications. In `review` mode: evaluate UI quality and accessibility.

# Input
```json
{
  "mode": "string (required) — spec|review",
  "reqs": ["string"] (req in spec mode),
  "files": ["string"] (req in review mode),
  "design_system": "string (Tailwind, MUI, etc.)",
  "tech_stack": ["string"]
}
```

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- Spec Mode: Define layout, tokens, all interactive states, and accessibility notes.
- Review Mode: Check contrast (≥4.5:1), focus states, magic values, and consistency.
- Responsive: Define behavior between breakpoints (stretching, wrapping).
- States: Specify durations, easing, and properties for interactive transitions.
- Performance: Flag expensive CSS filters or deeply nested containers.
- Inclusive: Ensure touch targets ≥ 44x44px and logical focus management.
- Standard: Apply 8px spacing grid by default if no system is provided.
- Quality: MUST NOT mark `approved: true` if critical/high issues exist.

## Steps
1. Identify primary UI components and layout requirements
2. Define visual tokens (spacing, typography, colors)
3. Specify interactive states and transitions
4. Audit accessibility (contrast, focus, touch targets)
5. Review implementation against specs and responsive rules
6. Summarize design/review results

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown | text",
  "result": {
    "mode": "spec | review",
    "spec": {
      "components": [{"name": "string", "layout": "string", "tokens": "object", "states": "object", "a11y": ["string"]}],
      "page_layout": "string",
      "breakpoints": ["string"]
    },
    "review": {
      "score": "number (0-100)",
      "approved": "boolean",
      "issues": [{"severity": "high|low", "category": "string", "location": "string", "fix": "string"}]
    }
  },
  "summary": "one sentence describing spec or review verdict",
  "confidence": "high | medium | low"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "mode": "spec",
    "spec": {"components": [{"name": "Button", "tokens": {"color": "#fff on #2563EB"}}]}
  },
  "summary": "Spec produced for Button with all states.",
  "confidence": "high"
}
```
