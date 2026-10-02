---
mode: security
extends: analyze
version: "1.0.0"
---

# Extra Rules

- Set type to "security" automatically — security analysis takes priority over all other findings
- Check all OWASP Top 10 vectors regardless of code type or language
- Report every injection vector with exploitability score (high/medium/low) and attack scenario
- Flag missing input sanitization on all external inputs (HTTP params, env vars, file reads, CLI args)
- Detect secrets in code: API keys, passwords, tokens, private keys via pattern matching
- Validate authentication checks exist on all state-changing operations
- Flag insecure defaults: debug mode enabled, weak crypto, missing HTTPS enforcement
- Check third-party dependency versions against known vulnerability patterns

# Extra Output

```json
{
  "owasp_coverage": {
    "checked": ["string"],
    "flagged": ["string"]
  },
  "injection_vectors": [
    {
      "location": "string",
      "type": "sql|xss|command|path|ldap|other",
      "exploitability": "high|medium|low",
      "attack_scenario": "string"
    }
  ],
  "exposed_secrets": ["string"],
  "missing_auth_checks": ["string"],
  "insecure_defaults": ["string"]
}
```

## Steps
1. Set analysis type to security
2. Check OWASP Top 10 categories
3. Scan for injection vectors (SQL, XSS, command)
4. Detect exposed secrets and hardcoded credentials
5. Validate authentication and authorization checks
6. Flag insecure defaults and misconfigurations

## Examples
**Input:** `/gk-analyze --security src/auth/`
**Expected behavior:** OWASP coverage report, injection vectors, exposed secrets, auth gap list
