---
mode: depth | assumption | refine
extends: sequential-thinking
version: "1.0.0"
---

# Mode: --depth

Force multi-step decomposition of every requirement.

## Extra Rules
- MUST decompose every requirement ≥ 2 levels deep before evaluating feasibility
- Each step MUST have an explicit pre-condition and post-condition
- Reject "do X" steps — replace with "given A, apply B to produce C"
- Flag any step with zero dependencies as potentially missing context
- Max 10 steps per decomposition level; group into sub-problems if exceeded

## Extra Output
```json
{
  "depth_levels": "number — how many layers were decomposed",
  "step_tree": [{ "id": "string", "parent": "string", "action": "string" }]
}
```

## Steps
1. Parse each requirement as a separate branch
2. Decompose each branch 2+ levels: goal → strategy → action → verification
3. Check pre/post conditions at each node
4. Identify shared sub-steps for reuse
5. Report total depth and longest branch

## Examples
**Input:** `/gk-sequential-thinking --depth "Design a rate limiter"`
**Expected behavior:** Multi-level decomposition: algorithm choice → data structure → edge cases → verification steps

---

# Mode: --assumption

Surface and validate implicit system assumptions before analysis.

## Extra Rules
- MUST enumerate ALL assumptions before step 1 of analysis
- Tag each assumption as: `[verified]` (can be checked now) or `[unverified]` (needs external input)
- Block progress on steps that depend on `[unverified]` assumptions
- After enumeration, ask user to confirm or deny each `[unverified]` assumption via `ask_user`

## Extra Output
```json
{
  "assumptions": [
    { "statement": "string", "status": "verified | unverified", "depends_on_steps": ["string"] }
  ],
  "blocked_steps": ["string"]
}
```

## Steps
1. Extract all implicit assumptions from the problem statement
2. Tag each as verified or unverified
3. Ask user to resolve unverified assumptions
4. Re-run analysis with confirmed assumptions only

## Examples
**Input:** `/gk-sequential-thinking --assumption "Migrate auth from sessions to JWT"`
**Expected behavior:** Lists assumptions (e.g., "existing sessions can be invalidated safely") before any migration steps

---

# Mode: --refine

Iterative refinement of an existing analysis.

## Extra Rules
- MUST read the previous analysis before generating any refinement
- Identify which steps changed confidence level since last run
- Only regenerate steps affected by new information — preserve unchanged steps
- Append a `diff_summary` explaining what changed and why

## Extra Output
```json
{
  "refined_steps": ["number — step IDs that were changed"],
  "diff_summary": "string — what changed and why",
  "confidence_delta": [{ "step": "number", "before": "string", "after": "string" }]
}
```

## Steps
1. Read previous analysis output
2. Identify new information or changed constraints
3. Regenerate only affected steps
4. Report diff between old and new analysis

## Examples
**Input:** `/gk-sequential-thinking --refine` (after receiving new requirement)
**Expected behavior:** Updates only the affected steps; keeps unchanged ones intact; shows delta
