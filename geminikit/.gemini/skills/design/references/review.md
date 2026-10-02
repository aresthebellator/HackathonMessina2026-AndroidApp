---
mode: review
extends: ui
version: "1.0.0"
---

# Extra Rules

- Evaluate implementation against design requirements and WCAG accessibility standards.
- Check for visual regressions, misalignment, and incorrect theme application.
- Validate responsive behavior across multiple viewport sizes.
- Identify accessibility gaps (contrast, screen reader support, keyboard nav).
- Report UI/UX inconsistencies and provide specific design fixes.

# Extra Output

```json
{
  "visual_regressions": ["string"],
  "accessibility_audit": ["string"],
  "responsive_issues": ["string"],
  "ux_improvements": ["string"]
}
```

## Steps
1. Compare implementation with design requirements
2. Audit accessibility (WCAG)
3. Validate responsiveness and theme
4. Report inconsistencies and recommended fixes

## Examples
**Input:** `/gk-design --review src/Header.tsx`
**Expected behavior:** Audit of UI against design specs and accessibility rules.
