---
mode: deep
extends: plan
version: "1.0.0"
---

# Extra Rules

- Exhaustive decomposition into granular subtasks (up to 7, using phases if more).
- Detailed mapping of all dependencies, including transitive ones.
- Explicitly list all required tools, files, and specific deliverables for each task.
- Quantify all risks and provide a clear mitigation strategy for each.
- Identify the exact critical path and provide a timeline with parallel task groups.
- Establish a clear foundation (e.g., schemas, interfaces, env) as the first priority.

# Extra Output

```json
{
  "granular_dependency_map": ["string"],
  "risk_mitigation_plan": ["string"],
  "specific_deliverables_map": ["string"]
}
```

## Steps
1. Perform deep decomposition (7+ tasks)
2. Map all transitive dependencies
3. Identify granular file-level impact
4. Provide detailed risk and mitigation strategy
5. Define exact parallel execution groups

## Examples
**Input:** `/gk-plan --deep Full system migration`
**Expected behavior:** Detailed multi-phase plan with granular tasks, dependencies, and risk assessments.
