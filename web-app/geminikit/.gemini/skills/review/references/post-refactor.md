---
mode: post-refactor
extends: review
version: "1.0.0"
---

# Extra Rules

- **Primary question:** Is the external behavior provably identical before and after the refactor?
- Check that ALL existing tests still pass after the refactor — if test suite was run, report pass/fail count.
- Flag any behavioral change (output difference, error type change, side-effect change) as `critical`.
- Verify no public API surface was broken — function signatures, return types, exported names.
- Evaluate if the refactor actually improves clarity or maintainability — flag if net result is neutral or negative.
- Check for dead code left behind from the refactor (unused variables, orphaned helper functions).
- Security scan is MANDATORY per base skill rules — always run regardless of refactor scope.
- Skip performance review unless refactor explicitly targeted perf (flag `--perf` instead).

# Extra Output

```json
{
  "behavioral_equivalence": "confirmed | suspected_change | unverified",
  "api_surface_broken": ["string (symbol:change)"],
  "dead_code_found": ["string (file:line)"],
  "maintainability_gain": "positive | neutral | negative",
  "test_suite_result": "pass | fail | not_run"
}
```

## Steps
1. Compare public API surface (exported symbols, function signatures, return types) before/after
2. Check test suite result — if available in context, extract pass/fail count
3. Scan for dead code left behind: unused imports, orphaned functions, unreachable branches
4. Evaluate maintainability gain — is the refactored code simpler, more readable, or better structured?
5. Run mandatory security scan if refactor touches auth/input-handling paths
6. Report `behavioral_equivalence` based on evidence

## Blocking Behavior
- `behavioral_equivalence: suspected_change` → **BLOCKING** — must prove parity before merging
- `api_surface_broken` with entries → **BLOCKING** — breaking change
- `test_suite_result: fail` → **BLOCKING**
- `maintainability_gain: negative` → advisory warning
- Dead code, style issues → advisory

## Examples
**Input:** `/gk-review --post-refactor src/routes/users.ts`
**Expected:** Confirms strategy-map refactor preserves all 7 route behaviors; detects orphaned `handleLegacyRoute` function left unreferenced; verifies 14 tests still pass.
