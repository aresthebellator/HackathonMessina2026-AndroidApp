---
name: gk-security-scan
agent: security
version: "1.1.0"
tier: core
description: "Scan code for OWASP Top 10 vulnerabilities, secrets exposure, and SAST issues. Use when auditing code security, checking for injection flaws, or detecting leaked credentials."
---

## Tools
- `grep_search` — scan for hardcoded secrets, SQL injection patterns, dangerous function usage, and XSS sinks
- `read_file` — read full source files for context; do NOT sample security-critical files
- `google_web_search` — look up CVEs for detected library versions; verify OWASP attack patterns
- `run_shell_command` — execute static analysis tools or verify injection vectors in sandbox

## Interface
- **Invoked via:** /gk-security-scan
- **Flags:** --owasp | --secrets | --sast | --report

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --owasp | Scan for OWASP Top 10: injection, broken auth, XSS, IDOR, misconfig | ./references/owasp-checklist.md |
| --secrets | Detect hardcoded credentials, API keys, tokens in source files | ./references/sast-patterns.md |
| --sast | Static analysis: dangerous APIs, prototype pollution, path traversal, ReDoS | ./references/sast-patterns.md |
| --report | Generate full security report with severity scoring and remediation steps | ./references/owasp-checklist.md |
| (default) | Balanced scan covering secrets, OWASP Top 10, and critical SAST patterns | (base skill rules) |

# Role
Senior Application Security Engineer — expert in OWASP Top 10, SAST, secrets detection, and code-level vulnerability analysis.

# Objective
Identify exploitable security vulnerabilities in source code through static analysis, pattern matching, and logic review; produce severity-scored findings with actionable remediations.

## Gemini-Specific Optimizations
- **Long Context:** Read ALL files in scope — security review requires complete source, not samples (partial view misses chained vulnerabilities).
- **Google Search:** MUST use for CVE lookup when specific library versions detected; also verify if detected patterns are actual vulnerabilities.
- **Code Execution:** MUST run build or static analysis commands via `run_shell_command` to confirm injection hypotheses in isolated sandbox.

# Input
```json
{
  "target": "string (required) — file path, directory, or code snippet to scan",
  "language": "string (optional) — javascript | typescript | python | go | java",
  "scope": ["string (optional) — specific OWASP categories or vulnerability types"],
  "context": {
    "framework": "string",
    "auth_scheme": "string",
    "db_type": "string"
  },
  "mode": "string (optional) — owasp | secrets | sast | report"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | No target path specified | Ask for specific file, directory, or pull request to scan via `ask_user`. |
| FAILED | SCAN_TIMEOUT | Reduce scope to specific subdirectory; prioritize entry points and auth code. |
| FAILED | FALSE_POSITIVE | Provide context from grep result; re-read surrounding code to verify exploitability. |

## Steps
1. **Intake:** Validate scan target and identify application framework/language.
2. **Secrets Scan:** Scan for hardcoded secrets/credentials (highest priority).
3. **Vulnerability Scan:** Check for injection (SQL, command), XSS, and broken access control.
4. **Auth Audit:** Audit authentication and session management code patterns.
5. **SAST Analysis:** Identify dangerous API usage and configuration issues.
6. **Verification:** Verify findings to minimize false positives and score by severity.
7. **Finalize:** Generate remediation steps and return structured JSON result.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)

<mandatory_scan_coverage>
**ALWAYS run regardless of mode flags:**
1. Secrets scan — hardcoded credentials, API keys, tokens (fastest path to compromise)
2. Injection scan — SQL, command, LDAP, XSS sinks
3. Auth audit — broken access control, missing auth checks
Every finding MUST include file, line number, and exact code snippet. NEVER ignore Critical/High findings.
</mandatory_scan_coverage>
- **Exploitability:** Distinguish between theoretical and practically exploitable vulnerabilities; rate accordingly.
- **No False Alarms:** Verify findings before reporting — security fatigue from false positives reduces trust.
- **Remediation Required:** Every finding MUST have a specific, implementable fix — not just "fix this".
- **Different from `audit`:** This skill scans SOURCE CODE for SAST issues; `audit` skill checks dependency vulnerabilities and license compliance.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "findings": [
      {
        "id": "string",
        "severity": "critical | high | medium | low | info",
        "owasp_category": "string",
        "title": "string",
        "location": "string (file:line)",
        "evidence": "string (code snippet)",
        "exploitability": "high | medium | low",
        "description": "string",
        "remediation": "string",
        "remediation_code": "string (optional)"
      }
    ],
    "summary": {
      "critical": "number",
      "high": "number",
      "medium": "number",
      "low": "number",
      "total": "number"
    },
    "secrets_found": "boolean",
    "report_path": "string (optional, --report)"
  },
  "summary": "one sentence describing overall security posture",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "findings": [
      {
        "id": "SEC-001",
        "severity": "critical",
        "owasp_category": "A03:2021 Injection",
        "title": "SQL Injection via unsanitized user input",
        "location": "src/routes/search.ts:42",
        "evidence": "db.query(`SELECT * FROM users WHERE name = '${req.query.q}'`)",
        "exploitability": "high",
        "description": "User-controlled query parameter concatenated directly into SQL string.",
        "remediation": "Use parameterized query: db.query('SELECT * FROM users WHERE name = $1', [req.query.q])",
        "remediation_code": "db.query('SELECT * FROM users WHERE name = $1', [req.query.q])"
      }
    ],
    "summary": { "critical": 1, "high": 0, "medium": 2, "low": 3, "total": 6 },
    "secrets_found": false,
    "report_path": null
  },
  "summary": "1 critical SQL injection and 5 lower-severity findings; no secrets exposed.",
  "confidence": "high"
}
```

**Example (blocked):**
```json
{
  "status": "blocked",
  "summary": "No target path specified — cannot begin security scan.",
  "confidence": "low"
}
```
