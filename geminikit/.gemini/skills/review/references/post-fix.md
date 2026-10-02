---
mode: post-fix
extends: review
version: "1.0.0"
---

# Extra Rules

- **Primary question:** Does the fix address the actual root cause, not just the symptom?
- Check that the fix does NOT introduce unintended side-effects in callers or dependent modules.
- Verify a regression test exists and correctly reproduces the original bug scenario.
- Flag if the fix is a workaround (symptom patch) rather than a root-cause resolution — mark as `high` severity.
- Check for similar bug patterns in adjacent code (same class, same module) that may harbor the same defect.
- Security scan is MANDATORY — fixes that patch injection or auth bugs MUST be validated for completeness.
- Skip style and naming issues unless directly related to the bug cause.

# Extra Output

```json
{
  "root_cause_addressed": "boolean",
  "side_effects_detected": ["string"],
  "regression_test_present": "boolean",
  "similar_patterns_found": ["string (file:line)"]
}
```

## Steps
1. Confirm root cause is clearly identified and addressed in the fix
2. Trace callers and dependents for unintended side-effects
3. Verify regression test exists and would have caught the original bug
4. Scan adjacent code for the same defect pattern
5. Run mandatory security check if fix touches auth/input handling
6. Report `root_cause_addressed: false` as critical finding if not resolved

## Blocking Behavior
- `root_cause_addressed: false` → **BLOCKING** — fix must be reworked
- Critical security finding → **BLOCKING**
- All other findings → advisory

## Examples
**Input:** `/gk-review --post-fix src/auth.js`
**Expected:** Confirms null-guard fix addresses root cause; checks no other callers rely on the null value; verifies regression test covers the null-user scenario.
