---
mode: parallel
extends: plan
version: "1.0.0"
---

# Extra Rules

- Focus on identifying tasks that can be performed concurrently.
- Explicitly group tasks into parallel execution lanes.
- Highlight any resource conflicts or shared dependencies that block parallelism.
- Provide a "Concurrent Execution" strategy to minimize total time.
- Identify early "quick wins" that unblock multiple future tasks.

# Extra Output

```json
{
  "parallel_lanes": [["string"]],
  "resource_conflicts": ["string"],
  "concurrency_unblockers": ["string"]
}
```

## Steps
1. Identify all independent tasks
2. Map shared dependencies that limit parallelism
3. Group tasks into concurrent lanes
4. Summarize the parallel strategy

## Examples
**Input:** `/gk-plan --parallel Build frontend and backend`
**Expected behavior:** Parallel-focused task breakdown with concurrent groups and unblockers.
