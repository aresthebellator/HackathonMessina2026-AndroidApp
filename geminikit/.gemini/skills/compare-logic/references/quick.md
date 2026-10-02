---
mode: quick
extends: compare-logic
version: "1.0.0"
---

# Extra Rules

- Focus only on function/method signatures and core routing.
- Skip line-by-line comparison of implementation details within functions.
- Assume functionality is a "Match" if signatures and names align perfectly.
- Prioritize identifying missing files and major structural gaps.

# Process Adjustment

1. **Phase 2 (Logic Discovery):** Limit mapping to high-level controllers and public API entry points only.
2. **Phase 3 (Detailed Comparison):** Only compare function headers/parameters. Do not read or analyze the internal code of the functions.
3. **Phase 4 (Reporting):** The report should explicitly state "Signatures Matched" instead of a detailed logic comparison.

# Extra Output

```json
{
  "comparison_level": "signature",
  "skipped_details": true
}
```

## Steps
1. Map high-level controllers and public API entry points
2. Compare function headers and parameters only
3. Verify signature alignment (names, types, count)
4. Identify missing files or major structural gaps
5. Report "Signatures Matched" for consistent headers

## Examples
**Input:** `/gk-compare-logic --quick old_src/ new_src/`
**Expected behavior:** High-level summary of signature matches and structural gaps only.

