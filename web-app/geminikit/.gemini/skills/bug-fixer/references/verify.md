---
mode: verify
extends: bug-fixer
version: "1.0.0"
---

# Extra Rules

- Mandatory creation of a regression test script to definitively reproduce the bug.
- Perform automated verification of the fix by running the reproduction script.
- Report any side effects or regressions introduced by the fix.
- Ensure the fix passes all existing project tests related to the modified module.
- Provide a "Validation Report" confirming the fix's correctness and stability.

# Extra Output

```json
{
  "reproduction_script": "string",
  "verification_status": "passed | failed",
  "regression_check": ["string"],
  "validation_report": "string"
}
```

## Steps
1. Create a reproduction test script
2. Apply the proposed fix
3. Run the reproduction script to verify the fix
4. Check for side effects or regressions
5. Summarize the verification results

## Examples
**Input:** `/gk-fix-bug --verify Fix login timeout`
**Expected behavior:** Automated reproduction script, fix application, and verification report.
