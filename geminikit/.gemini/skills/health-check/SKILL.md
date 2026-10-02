---
name: gk-health-check
agent: maintenance
version: "1.0.0"
description: "Validate framework compliance across all agents and skills."
tier: internal
---

## Interface
- **Invoked via:** /gk-health-check
- **Flags:** none
- **Errors:** COMPLIANCE_FAILED, REGISTRY_DESYNC

# Role
Framework Integrity Engineer — expert in Gemini Kit standards, core rules, and system architecture.

# Objective
Run automated checks against all registered agents and skills to ensure adherence to core rules, artifact management, and registry synchronization.

# Input
```json
{
  "fix": "boolean (optional, default: false) — whether to attempt automatic fixes for registry sync"
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read all `SKILL.md` files in a single pass — Gemini's 1M window allows full registry scan without batching.
- **Google Search:** N/A — health check validates internal framework compliance only.
- **Code Execution:** MUST run `node .gemini/scripts/sync-registry.js` via `run_shell_command` to verify registry sync status.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<health_check_rules>
**ALWAYS enforced — no partial scans:**
- MUST NOT skip any registered skill or agent during the scan.
- MUST report the exact rule or section missing for each failure.
- MUST verify that all skill directories on disk have a valid `SKILL.md` with required frontmatter fields.
- MUST verify that `REGISTRY.md` is in sync with the skills on disk (run `node .gemini/scripts/sync-registry.js` to fix).
</health_check_rules>

## Steps
1. Scan `.gemini/skills/*/SKILL.md` — check required frontmatter fields (name, agent, description).
2. Run `node .gemini/scripts/sync-registry.js` to verify REGISTRY.md sync.
3. Save the full report as **Markdown** to `reports/health-check/{YYMMDD-HHmm}-health-report.md`. Format:
   ```markdown
   # Health Check Report — {YYMMDD-HHmm}

   ## Summary
   | Total Skills | Passed | Failed | Registry Sync |
   |-------------|--------|--------|---------------|
   | {N}         | {P}    | {F}    | ✓ synced / ✗ out of sync |

   ## Issues
   - ✗ {skill-name}: {exact rule or field missing}
   - ...

   ## Passed
   - ✓ {skill-name}
   - ...
   ```
4. Present results to user as a human-readable summary (counts, key issues).
5. If failures exist, list the critical non-compliance issues in plain text.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "markdown",
  "result": {
    "report_path": "reports/health-check/{YYMMDD-HHmm}-health-report.md",
    "summary": {
      "total_skills": "number",
      "passed": "number",
      "failed": "number"
    },
    "registry_sync": "pass | fail",
    "critical_issues": ["string"]
  },
  "summary": "Health check completed: X passed, Y failed.",
  "confidence": "high"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "markdown",
  "result": {
    "report_path": "reports/health-check/260324-1200-health-report.md",
    "summary": { "total_skills": 25, "passed": 23, "failed": 2 },
    "registry_sync": "pass",
    "critical_issues": ["Skill 'git' missing PowerShell rule", "Skill 'analyze' exceeds 200 lines"]
  },
  "summary": "Health check completed: 23 passed, 2 failed.",
  "confidence": "high"
}
```
