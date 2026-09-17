---
mode: perf
extends: review
version: "1.0.0"
---

# Extra Rules

- Focus on performance bottlenecks, resource efficiency, and scalability.
- Identify N+1 query patterns in database or API calls.
- Flag inefficient algorithms (e.g., O(n²) or worse) and unnecessary data processing.
- Check for memory leaks, blocking I/O, or long-running synchronous operations.
- Evaluate caching strategies and potential for optimization (e.g., memoization).
- Flag redundant or heavy network requests that could be batched.
- Review resource usage (e.g., file descriptors, database connections, threads).

# Extra Output

```json
{
  "performance_bottlenecks": ["string"],
  "n_plus_1_queries": ["string"],
  "complexity_alerts": ["string"],
  "optimization_suggestions": ["string"]
}
```

## Steps
1. Set review focus to performance
2. Scan for N+1 queries and inefficient loops
3. Identify blocking I/O or memory risks
4. Suggest caching and optimization strategies

## Examples
**Input:** `/gk-review --perf src/data-loader.ts`
**Expected behavior:** Detailed performance analysis and recommendations for optimization.
