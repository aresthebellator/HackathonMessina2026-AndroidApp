---
mode: quick
extends: review
version: "1.0.0"
---

# Extra Rules

- Focus ONLY on critical and high-severity issues — skip medium and low.
- Summarize findings into a high-signal report for rapid iteration.
- Ignore minor style or naming inconsistencies unless they impact readability or security.
- Prioritize identifying security gaps and fundamental correctness errors.
- Limit strengths to top 2 or fewer.

# Extra Output

```json
{
  "critical_impact": ["string"],
  "high_priority_fixes": ["string"]
}
```

## Steps
1. Filter findings for high/critical severity only
2. Summarize key blockers
3. Provide top-3 critical fixes
4. Skip style and architectural minor notes

## Examples
**Input:** `/gk-review --quick src/login.js`
**Expected behavior:** High-level summary focusing only on critical security or correctness issues.
