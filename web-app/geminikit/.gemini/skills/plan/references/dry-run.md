---
mode: dry-run
extends: plan
version: "1.0.0"
---

# Extra Rules

- DO NOT commit or save any files; focus only on validation.
- Perform a simulated decomposition to check for logic errors or gaps.
- Report all potential plan issues, missing context, and blocker conflicts.
- Validate the critical path and effort estimates for feasibility.
- Identify all files that WOULD be affected without modifying them.

# Extra Output

```json
{
  "dry_run_validation": "valid | invalid",
  "potential_issues": ["string"],
  "simulation_summary": "string"
}
```

## Steps
1. Simulate the task decomposition
2. Validate dependency and effort logic
3. Identify potential file impacts
4. Report simulation findings without saving

## Examples
**Input:** `/gk-plan --dry-run Refactor src/`
**Expected behavior:** Validation of the proposed refactor plan without actual file creation.
