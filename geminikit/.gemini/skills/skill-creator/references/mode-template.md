---
mode: mode-name
extends: parent-skill-name
version: "1.0.0"
---

<!-- Save as: .gemini/skills/<skill-name>/references/<mode-name>.md -->

<!-- Mode files inherit ## Tools and ## Gemini-Specific Optimizations from the base skill. -->
<!-- Do NOT duplicate those sections here — only add mode-specific overrides when necessary. -->

# Extra Rules
<!-- MANDATORY: Add rules that narrow or extend behavior for this specific mode. -->
<!-- Rules should be additive and not override base skill rules unless necessary for accuracy. -->

- [Rule 1]
- [Rule 2]

# Extra Output
<!-- Optional: Add fields that are MERGED into base skill output schema. -->

```json
{
  "extra_field_1": "type — description of additional output field"
}
```

## Steps
<!-- MANDATORY: Provide a step-by-step execution process specific to this mode. -->

1. [Step 1]
2. [Step 2]

## Examples
**Input:** `/gk-<cmd> --<mode> <example target>`
**Expected behavior:** [Description of what this mode does differently]
