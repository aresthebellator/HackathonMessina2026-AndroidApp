---
mode: from
extends: plan
version: "1.0.0"
---

# Extra Rules

- Load and parse the base plan from the provided file path.
- Merge any newly requested tasks into the existing plan structure.
- Validate the loaded plan for schema consistency and existing progress.
- Maintain existing subtask IDs and dependencies while adding new ones.
- Flag any conflicts between the new request and the loaded plan.

# Extra Output

```json
{
  "loaded_plan_path": "string",
  "merged_task_count": "number"
}
```

## Steps
1. Load plan from specified path
2. Parse and validate the existing plan
3. Merge new task requirements
4. Generate an updated plan incorporating the old data

## Examples
**Input:** `/gk-plan --from current_plan.json Add tests`
**Expected behavior:** Existing plan is loaded and "Add tests" task is integrated.
