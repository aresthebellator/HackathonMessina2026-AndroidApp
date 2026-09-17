---
mode: post-implement
extends: review
version: "1.0.0"
---

# Extra Rules

- **Primary question:** Does the implementation match the plan's acceptance criteria and architecture?
- Cross-check implemented code against the plan phase file — flag any missing requirements as `high` severity.
- Verify the implementation follows existing architectural patterns in the codebase (no rogue abstractions).
- Check for over-engineering or speculative features beyond the plan scope (YAGNI violation).
- Validate error handling at system boundaries (user input, external API calls, DB operations).
- Security scan is MANDATORY — all new code paths must be checked for injection and auth gaps.
- Flag incomplete implementations (TODOs, stubs, placeholder logic) as `high` severity.
- Style and naming issues — report but advisory only.

# Extra Output

```json
{
  "plan_alignment": "full | partial | missing",
  "missing_requirements": ["string"],
  "over_engineering_flags": ["string"],
  "incomplete_implementations": ["string (file:line)"]
}
```

## Steps
1. Read plan phase file to extract acceptance criteria; if no plan file, infer requirements from task description in input `context` field
2. Verify each requirement is implemented and testable
3. Check architectural consistency with existing codebase patterns
4. Flag YAGNI violations — features or abstractions not in the plan
5. Validate error handling at all external boundaries
6. Run mandatory security scan on all new code paths
7. Summarize `plan_alignment` score

## Blocking Behavior
- `plan_alignment: missing` → **BLOCKING** — critical requirements unimplemented
- Critical security finding → **BLOCKING**
- `plan_alignment: partial` → advisory with list of missing items
- All other findings → advisory

## Examples
**Input:** `/gk-review --post-implement src/users/`
**Expected:** Checks implementation against plan phase; flags missing pagination on list endpoint; flags speculative caching logic not in plan scope.
