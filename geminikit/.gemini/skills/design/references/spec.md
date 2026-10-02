---
mode: spec
extends: ui
version: "1.0.0"
---

# Extra Rules

- Focus on generating precise visual specifications and design tokens.
- Define exact spacing (padding, margin), typography (font-family, size, weight), and color (hex, variables) for all components.
- Specify interactive states (hover, active, focus, disabled).
- Provide accessibility requirements (aria-labels, contrast ratios) as part of the spec.
- Identify reusable design tokens for the theme.

# Extra Output

```json
{
  "visual_spec": {
    "spacing": ["string"],
    "typography": ["string"],
    "colors": ["string"]
  },
  "tokens": ["string"]
}
```

## Steps
1. Identify all UI components
2. Define exact visual properties and tokens
3. Specify interactive states and accessibility
4. Summarize the implementation specs

## Examples
**Input:** `/gk-design --spec Login page`
**Expected behavior:** Detailed visual specs including spacing, tokens, and accessibility.
