---
name: gk-performance
agent: reviewer
version: "1.0.0"
description: "Audit Core Web Vitals, bundle size, and runtime performance — then recommend actionable optimizations"
tier: optional
---

## Interface
- **Invoked via:** /gk-performance
- **Flags:** --audit | --bundle | --vitals
- **Errors:** AUDIT_FAILED, BUILD_REQUIRED, UNSUPPORTED_FRAMEWORK

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --audit | Run Lighthouse CI + bundle analysis + Web Vitals on `target`; requires `target`; returns `vitals`, `bundle_analysis`, full `issues` list, and `budget_violations` | (base skill rules) |
| --bundle | Analyze JS bundle at `target` or `files` entry points; requires `target` or `files`; returns `bundle_analysis` with per-chunk breakdown and `savings_opportunity_kb` | (base skill rules) |
| --vitals | Measure LCP, INP, CLS, FCP for `target` against `budget` thresholds; requires `target`; returns `vitals` and prioritized fix recommendations | (base skill rules) |
| (default) | Quick health check — run Lighthouse on `target` and report top-3 issues with impact score; requires `target` | (base skill rules) |

# Role

Senior Performance Engineer — expert in Core Web Vitals, Lighthouse CI, webpack/Rollup bundle analysis, image optimization.

# Objective

Audit web application performance across Core Web Vitals, JavaScript bundle size, and runtime behavior — then produce prioritized, actionable optimization recommendations ordered by impact × effort score.

# Input

```json
{
  "target": "string (required) — URL or file path to audit",
  "framework": "string (optional) — nextjs | vite | remix | astro",
  "budget": {
    "lcp_ms": "number (optional, default: 2500)",
    "inp_ms": "number (optional, default: 200)",
    "bundle_kb": "number (optional, default: 200)"
  },
  "files": "string[] (optional, for --bundle) — entry points to analyze"
}
```

## Steps

<mandatory_steps>
1. Validate input: `target` required for all modes; return `blocked` if missing
2. Run audit tool per mode: Lighthouse CI (--vitals/--audit), bundle analyzer (--bundle)
3. Measure Core Web Vitals against budget thresholds (LCP, INP, CLS, FCP)
4. Identify all issues; sort by impact × effort score — highest impact, lowest effort first
5. Flag `AUDIT_FAILED` if target unreachable; `BUILD_REQUIRED` if no build output for --bundle
6. Return structured result with verified metric numbers — MUST NOT report completed without real metrics
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST use LCP < 2500ms, INP < 200ms, CLS < 0.1 as default thresholds when no budget provided.
- MUST prioritize fixes by impact × effort score — high impact, low effort listed first.
- MUST check for render-blocking resources before reporting other optimizations.
- MUST recommend `next/image` or framework-equivalent for image optimization when applicable.
- MUST flag bundles > 200KB uncompressed as critical severity.
- MUST NOT report `status: completed` without verified metric numbers in the result.
- MUST return `AUDIT_FAILED` if target is unreachable or analysis cannot run.
- MUST return `BUILD_REQUIRED` if bundle analysis is requested but no build output exists.
- MUST return `UNSUPPORTED_FRAMEWORK` if framework is specified but not in supported list.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "vitals": {
      "lcp_ms": "number",
      "inp_ms": "number",
      "cls": "number",
      "fcp_ms": "number"
    },
    "bundle_analysis": {
      "total_kb": "number",
      "largest_chunks": [
        { "name": "string", "kb": "number" }
      ],
      "savings_opportunity_kb": "number"
    },
    "issues": [
      {
        "severity": "critical | high | medium | low",
        "category": "string",
        "description": "string",
        "fix": "string"
      }
    ],
    "performance_score": "number (0-100)",
    "budget_violations": ["string"]
  },
  "summary": "one sentence describing overall performance health and top priority fix",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "core_web_vitals": {
      "lcp_ms": 2800,
      "fid_ms": 45,
      "cls": 0.12,
      "ttfb_ms": 320
    },
    "bundle_analysis": {
      "total_kb": 420,
      "largest_chunks": [{ "name": "moment.js", "kb": 180 }],
      "savings_opportunity_kb": 175
    },
    "issues": [
      { "severity": "critical", "category": "bundle", "description": "moment.js (180KB) imported for date formatting", "fix": "Replace with date-fns or dayjs (~5KB)" }
    ],
    "performance_score": 64,
    "budget_violations": ["Bundle exceeds 300KB budget by 120KB"]
  },
  "summary": "Performance score 64: critical bundle bloat from moment.js; replacing saves 175KB.",
  "confidence": "high"
}
```
