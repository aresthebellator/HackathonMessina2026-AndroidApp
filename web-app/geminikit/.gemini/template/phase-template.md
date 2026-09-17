---
agent: "[planner|developer|tester|reviewer|researcher|documenter|comparator|designer]"
skill: "[optional — skill name to invoke, e.g. gk-debug, gk-test]"
priority: "P2"
status: pending
depends_on: []
---

<!--
  INSTRUCTIONS FOR USE:
  1. Fill every [placeholder] — do not leave brackets in the final file
  2. Save as plans/{date}-{slug}/phase-{NN}-{name}.md
  3. frontmatter 'agent' must match task-schema agent enum
  4. frontmatter 'depends_on' lists phase IDs e.g. ["phase-01", "phase-02"]
  5. Sections '## Related Code Files' and '## Success Criteria' are REQUIRED by claude-plan-schema
  6. Delete this instruction block before saving
-->

# [Phase Title — imperative verb phrase, e.g. "Implement Authentication API"]

## Overview

**Priority:** [P1 critical | P2 normal | P3 low]
**Status:** pending
**Description:** [1-2 sentences describing what this phase accomplishes and its scope.]

## Key Insights

- [Important finding or constraint relevant to this phase]
- [Critical consideration, e.g. "Must not break existing /auth endpoints"]

## Requirements

**Functional:**
- [What the implementation must do]

**Non-functional:**
- [Performance, security, compatibility constraints]

## Architecture

[Brief description of the design: components involved, data flow, key decisions.
Include a simple diagram in ASCII or Mermaid if helpful.]

## Related Code Files

**Modify:**
- `[path/to/existing/file.py]` — [reason]

**Create:**
- `[path/to/new/file.py]` — [purpose]

**Delete:**
- `[path/to/obsolete/file.py]` — [reason]

## Implementation Steps

1. [First concrete action — specific enough for an agent to execute]
2. [Second action]
3. [Third action]

## Tasks

| ID | Task | Agent | Skill | Effort | Depends | Status |
|----|------|-------|-------|--------|---------|--------|
| T1 | [Task description] | [agent] | [skill or —] | XS\|S\|M\|L\|XL | — | pending |
| T2 | [Task description] | [agent] | [skill or —] | XS\|S\|M\|L\|XL | T1 | pending |
| T3 | [Task description] | [agent] | [skill or —] | XS\|S\|M\|L\|XL | T1,T2 | pending |

> For tasks requiring detailed steps, use `.gemini/template/task-template.md` and embed below.

## Success Criteria

- [ ] [Verifiable outcome 1 — e.g. "All unit tests pass"]
- [ ] [Verifiable outcome 2 — e.g. "API returns 200 for valid input"]
- [ ] [Verifiable outcome 3]

## Risk Assessment

| Risk | Likelihood | Mitigation |
|------|-----------|------------|
| [Risk] | high\|medium\|low | [Strategy] |

## Security Considerations

- [Auth/authorization concern — or "N/A"]
- [Data protection concern — or "N/A"]

## Next Steps

- Depends on: [phase IDs this phase unlocks, or "None"]
- Blocks: [phase IDs that depend on this phase completing]
