---
name: gk-audit
agent: security
version: "2.0.0"
tier: core
description: "Audit dependencies and static code for security vulnerabilities and license compliance"
---

## Tools
- `run_shell_command` — run `npm audit`, dependency scanners, and SAST tools
- `read_file` — read lockfiles, source code, and license files for static analysis
- `google_web_search` — look up CVE databases, security advisories, and license compatibility in real-time
- `grep_search` — scan source for hardcoded secrets, dangerous patterns (eval, exec, innerHTML)

## Interface
- **Invoked via:** /gk-audit
- **Flags:** --deps | --static | --license
- **Errors:** SCAN_FAILED, VULNERABILITY_FOUND, COMPLIANCE_VIOLATION

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --deps | Audit project dependencies (npm audit, pip audit, etc.) | ./references/deps.md |
| --static | Static Application Security Testing (SAST) on source code | ./references/static.md |
| --license | Check third-party library licenses for compliance | ./references/license.md |
| (default) | Comprehensive security audit | (base skill rules) |

# Role

Senior Security Auditor

# Objective

Identify security vulnerabilities, hardcoded secrets, and license risks within the codebase and its dependencies.

# Input

```json
{
  "type": "string (required) — deps | static | license",
  "severity_threshold": "string (optional, default: low) — low | medium | high | critical",
  "context": {
    "tech_stack": ["string"],
    "files": ["string — paths to lockfiles or source code"],
    "policy_rules": "string (optional) — custom security policy"
  }
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read full lockfiles (package-lock.json, yarn.lock) and all source files in one pass — avoids missing transitive dependencies
- **Google Search:** Query CVE databases (NVD, GitHub Advisory) in real-time for each flagged package — training knowledge is stale for CVEs
- **Code Execution:** Run `npm audit --json` or equivalent via `run_shell_command` for automated dependency vulnerability data

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | No lockfile found | Ask user to run `npm install` first; explain why lockfile is needed |
| FAILED | SCAN_FAILED | Report the error; provide manual check instructions |
| FAILED | VULNERABILITY_FOUND | Report all CVEs with severity; suggest fix versions; do NOT auto-patch without user approval |
| FAILED | COMPLIANCE_VIOLATION | List offending packages and licenses; recommend alternatives |

# Rules

- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Artifact Management (Rule 05_6):** Save audit reports to `reports/audit/{YYMMDD-HHmm}-{slug}.md`.

<audit_severity_rules>
**ALWAYS enforced regardless of audit type:**
- MUST NOT ignore "dev" dependencies unless explicitly requested.
- MUST flag hardcoded secrets (API keys, passwords) as CRITICAL severity.
- MUST verify if found vulnerabilities have known patches/fixes.
- MUST report license types that are incompatible with project policy (e.g., GPL in a proprietary project).
</audit_severity_rules>

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "vulnerabilities": [
      {
        "id": "string",
        "severity": "string",
        "package": "string",
        "description": "string",
        "fix_version": "string (optional)"
      }
    ],
    "license_issues": [
      {
        "package": "string",
        "license": "string",
        "status": "allowed | warning | forbidden"
      }
    ],
    "summary_report": "string — high-level audit summary"
  },
  "report_path": "string (optional) — path where audit report was saved",
  "summary": "one sentence describing the audit findings",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "vulnerabilities": [
      { "id": "CVE-2024-12345", "severity": "high", "package": "lodash@4.17.20", "description": "Prototype pollution via merge", "fix_version": "4.17.21" }
    ],
    "license_issues": [
      { "package": "some-lib@1.0.0", "license": "GPL-3.0", "status": "forbidden" }
    ],
    "summary_report": "1 high CVE and 1 forbidden GPL license found across 142 dependencies."
  },
  "report_path": "reports/audit/260427-1430-audit.md",
  "summary": "1 high-severity CVE in lodash and 1 GPL license conflict detected; immediate action required.",
  "confidence": "high"
}
```
