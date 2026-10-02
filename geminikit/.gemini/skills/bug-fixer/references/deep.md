---
mode: deep
extends: bug-fixer
version: "1.0.0"
---

# Extra Rules

- Perform a multi-layer analysis of the error (application/framework/runtime/OS).
- Identify root causes that span multiple files or modules.
- Evaluate the long-term impact of the fix on system stability and scalability.
- Trace the error through complex asynchronous flows and distributed systems.
- Identify potential related bugs or similar patterns across the codebase.
- Recommend a architectural fix if the bug is a symptom of a deeper design flaw.

# Extra Output

```json
{
  "multi_layer_analysis": ["string"],
  "long_term_impact": "string",
  "pattern_matching_results": ["string"],
  "architectural_recommendations": ["string"]
}
```

## Steps
1. Trace the error across multiple layers and modules
2. Analyze complex async flows and data dependencies
3. Identify related patterns and bugs codebase-wide
4. Summarize deep root-cause and structural fix recommendations

## Examples
**Input:** `/gk-fix-bug --deep Intermittent 500 error in auth flow`
**Expected behavior:** Detailed trace of the error across layers, identifying a deep race condition or design flaw.
