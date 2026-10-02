---
mode: deep
extends: compare-logic
version: "1.0.0"
---

# Extra Rules

- Perform exhaustive line-by-line comparison of all matching files.
- Analyze transitive calls within business logic functions to ensure underlying helper logic is also migrated correctly.
- Compare data structures (schemas, classes, interfaces) used as inputs and outputs for logic blocks.
- Identify subtle changes in validation rules or error handling logic.
- Document and flag any differences in performance complexity (e.g., O(n) to O(n^2)).

# Process Adjustment

1. **Phase 2 (Logic Discovery):** Map ALL files, including private helpers and utility modules.
2. **Phase 3 (Detailed Comparison):** Use `read_file` to analyze the entire content. Trace internal dependencies to verify consistent behavior across layers.
3. **Phase 4 (Reporting):** Include a "Deep Analysis" section for each file pair, highlighting specific code-level logic shifts.

# Extra Output

```json
{
  "comparison_level": "deep_analysis",
  "trace_depth": "transitive"
}
```

## Steps
1. Perform exhaustive directory mapping including helpers
2. Perform line-by-line comparison of all matching files
3. Trace transitive calls to internal dependencies
4. Identify schema and data structure differences
5. Highlight subtle validation and error logic shifts

## Examples
**Input:** `/gk-compare-logic --deep legacy/ modern/`
**Expected behavior:** Line-by-line logic comparison, transitive dependency mapping, and data structure analysis.

