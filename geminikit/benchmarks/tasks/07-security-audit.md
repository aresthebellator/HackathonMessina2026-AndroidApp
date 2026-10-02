---
id: "07"
name: "Security audit dependencies"
category: "security"
skill: "gk-audit"
difficulty: medium
max_tokens: 10000
timeout_seconds: 120
---

## Prompt

"Perform a security audit of a Node.js project with these dependencies in package.json:
```json
{
  \"dependencies\": {
    \"express\": \"4.18.2\",
    \"jsonwebtoken\": \"8.5.1\",
    \"bcrypt\": \"5.1.0\",
    \"lodash\": \"4.17.20\",
    \"axios\": \"0.21.1\"
  }
}
```
Identify any known CVEs or security risks."

## Expected Criteria

- [ ] Identifies `axios 0.21.1` as vulnerable (SSRF, prototype pollution CVEs)
- [ ] Identifies `lodash 4.17.20` as potentially vulnerable (prototype pollution)
- [ ] Recommends version upgrades
- [ ] Provides severity ratings
- [ ] Suggests mitigation steps

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| Axios vulnerability identified | 30% | keyword: "axios" + ("CVE" or "vulnerable" or "upgrade") |
| Lodash flagged | 20% | keyword: "lodash" + ("prototype" or "CVE") |
| Upgrade recommendations | 25% | specific version numbers suggested |
| Severity ratings | 25% | keyword: "critical" or "high" or "medium" or "low" |
