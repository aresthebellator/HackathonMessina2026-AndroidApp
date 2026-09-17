---
mode: phase
extends: plan
version: "1.0.0"
---

# Extra Rules

- Focus only on tasks within the specified phase by ID.
- Ignore all tasks and dependencies outside the specified phase.
- Provide more granular detail for the tasks within the targeted phase.
- Summarize the phase's deliverables and critical path independently.
- Flag any missing dependencies from previous phases that block the current one.

# Extra Output

```json
{
  "targeted_phase_id": "string",
  "phase_deliverables": ["string"],
  "phase_blockers": ["string"]
}
```

## Steps
1. Filter task decomposition for the specified phase ID
2. Deepen the detail for the targeted subtasks
3. Identify intra-phase dependencies
4. Summarize phase-specific outcomes

## Examples
**Input:** `/gk-plan --phase P2 Implement auth logic`
**Expected behavior:** Detailed breakdown of phase P2 tasks, ignoring others.
