---
mode: fast
extends: plan
version: "1.0.0"
---

# Extra Rules

- Limit decomposition to top-level tasks only (max 3-5 subtasks).
- Provide high-level effort estimates (S/M/L) without detailed breakdown.
- Focus on the primary critical path and ignore minor or optional subtasks.
- Skip detailed deliverables and focus on overall phase completion.
- Assume standard defaults for dependencies and blockers.

# Extra Output

```json
{
  "high_level_phases": ["string"],
  "estimated_total_time": "string"
}
```

## Steps
1. Identify 3-5 primary tasks
2. Assign high-level effort estimates
3. Map the main critical path
4. Summarize the rapid execution strategy

## Examples
**Input:** `/gk-plan --fast Implement user auth`
**Expected behavior:** Brief 3-5 task breakdown with high-level effort estimates.
