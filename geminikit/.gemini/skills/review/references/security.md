---
mode: security
extends: review
version: "1.0.0"
---

# Extra Rules

- Prioritize OWASP Top 10 vulnerabilities (Injection, Auth, XSS, etc.).
- Scan all user-controlled inputs for missing sanitization or validation.
- Identify all exposed secrets, API keys, and hardcoded credentials.
- Validate authentication and authorization logic for state-changing endpoints.
- Check for insecure defaults, weak cryptography, and missing HTTPS enforcement.
- Evaluate dependency tree for known vulnerable packages.
- Flag any use of dangerous functions or unsafe memory access.

# Extra Output

```json
{
  "owasp_flagged": ["string"],
  "vulnerable_functions": ["string"],
  "security_recommendations": ["string"],
  "exposed_secrets_list": ["string"]
}
```

## Steps
1. Set review type to security
2. Scan for OWASP Top 10 vulnerabilities
3. Identify exposed secrets
4. Validate auth/auth checks
5. Review input validation and sanitization

## Examples
**Input:** `/gk-review --security src/auth/service.js`
**Expected behavior:** Detailed list of all potential security vulnerabilities and recommended fixes.
