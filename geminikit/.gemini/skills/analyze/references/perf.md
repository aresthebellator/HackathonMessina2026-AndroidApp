---
mode: perf
extends: analyze
version: "1.0.0"
---

# Extra Rules

- Focus exclusively on performance bottlenecks: O(n²)+ loops, N+1 queries, blocking I/O, memory leaks
- Identify hot paths — functions with highest cyclomatic complexity or called most frequently
- Flag synchronous operations that block the event loop or main thread and should be async
- Check for missing caching on repeated identical computations or external calls
- Report memory allocation patterns: large allocations, retained references, potential leak sources
- Estimate relative impact of each bottleneck: high (>50% overhead), medium (10-50%), low (<10%)

# Extra Output

```json
{
  "hot_paths": [
    {
      "function": "string",
      "complexity": "string",
      "call_frequency": "string",
      "impact": "high|medium|low"
    }
  ],
  "bottlenecks": [
    {
      "type": "loop|query|io|memory|sync|allocation",
      "location": "string",
      "description": "string",
      "impact": "high|medium|low",
      "fix": "string"
    }
  ],
  "caching_opportunities": ["string"],
  "async_candidates": ["string"],
  "memory_risks": ["string"]
}
```

## Steps
1. Identify hot paths and critical code sections
2. Find O(n²)+ loops and algorithmic inefficiencies
3. Detect N+1 query patterns
4. Check for blocking I/O operations
5. Report memory allocation risks
6. Suggest caching and batching opportunities

## Examples
**Input:** `/gk-analyze --perf src/services/data-processor.ts`
**Expected behavior:** Bottleneck report with impact scores, N+1 queries, blocking I/O, caching suggestions
