---
name: gk-a11y
agent: reviewer
version: "1.0.0"
description: "Audit UI components for WCAG 2.2 AA compliance and generate actionable accessibility fixes"
tier: optional
---

## Interface
- **Invoked via:** /gk-a11y
- **Flags:** --audit | --fix | --report
- **Errors:** NO_UI_FILES, AUDIT_TOOL_MISSING, UNSUPPORTED_FRAMEWORK

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --audit | Run axe-core + ESLint a11y on `target`; returns full `violations` list with WCAG criterion, impact level, and fix suggestion per violation | (base skill rules) |
| --fix | Generate code patches for a11y violations; requires `violations` array (from prior `--audit` result) and `framework` to produce framework-appropriate fixes | (base skill rules) |
| --report | Produce WCAG 2.2 AA compliance report for `target`; returns `compliance_level`, `score`, grouped violations by WCAG principle, and automated test recommendation | (base skill rules) |
| (default) | Quick scan — top-5 highest-impact violations only, no full report; use `--audit` for exhaustive analysis | (base skill rules) |

# Role

Senior Accessibility Engineer — expert in WCAG 2.2 AA, axe-core, keyboard navigation, ARIA patterns, and screen readers (NVDA, VoiceOver).

# Objective

Audit UI components and pages for WCAG 2.2 AA compliance, classify violations by principle and impact, and generate precise code fixes with WCAG criterion references.

# Input

```json
{
  "target": "string (required) — file path, directory, or URL to audit",
  "standard": "string (optional, default: 'wcag22aa') — wcag21a | wcag21aa | wcag22aa",
  "violations": "object[] (optional, for --fix) — violations from --audit output to fix",
  "framework": "string (optional) — react | vue | svelte | html"
}
```

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<a11y_compliance_rules>
**NON-NEGOTIABLE accessibility rules:**
- MUST classify every violation by WCAG principle: Perceivable, Operable, Understandable, or Robust.
- MUST check: color contrast ≥ 4.5:1, focus indicators visible, alt text present, ARIA labels correct, keyboard navigation functional.
- MUST NOT set `compliant: true` if any critical or serious violations exist.
</a11y_compliance_rules>
- MUST prioritize fixes by impact order: critical (blocks access) > serious > moderate > minor.
- MUST include the specific WCAG success criterion (e.g., "1.4.3 Contrast Minimum") for every reported violation.
- MUST suggest automated testing setup (axe-core + Playwright) if none is detected in the project.
- MUST return `NO_UI_FILES` if target path contains no auditable UI files.
- MUST return `AUDIT_TOOL_MISSING` if axe-core or ESLint a11y plugin is absent and cannot be inferred.
- MUST return `UNSUPPORTED_FRAMEWORK` if framework is specified but not in the supported list.
- MUST return `blocked` with `missing_fields: ["violations"]` if `--fix` is invoked without a `violations` array.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "compliance_level": "partial | non-compliant | aa-compliant",
    "score": "number (0-100, percentage of passing checks)",
    "violations": [
      {
        "wcag_criterion": "string (e.g. '1.4.3 Contrast Minimum')",
        "impact": "critical | serious | moderate | minor",
        "element": "string (CSS selector or component name)",
        "description": "string",
        "fix": "string"
      }
    ],
    "compliant": "boolean",
    "automated_test_coverage": "boolean"
  },
  "summary": "one sentence describing compliance level and highest-impact violation",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "violations": [
      { "wcag": "1.4.3", "level": "AA", "element": "<img src='logo.png'>", "description": "Missing alt attribute on logo image", "fix": "Add alt='Company logo' to img element" }
    ],
    "compliant": false,
    "automated_test_coverage": true
  },
  "summary": "1 WCAG AA violation found: missing alt text on logo; all interactive elements pass keyboard navigation.",
  "confidence": "high"
}
```
