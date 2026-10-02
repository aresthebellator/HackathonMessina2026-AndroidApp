---
mode: ascii
extends: preview
version: "1.0.0"
---

# Extra Rules
- Use standard box-drawing characters: `─ │ ┌ ┐ └ ┘ ├ ┤ ┬ ┴ ┼ →  ←  ↑ ↓`
- Target width: 80 chars max (terminal-safe)
- Arrow conventions: `→` for data flow, `--→` for optional/dashed, `═══` for critical path
- Box labels: UPPER_CASE for systems, Title_Case for components, lower_case for data
- MUST be readable in a monospace terminal font — test alignment manually
- Add a key/legend below if using custom symbols

# Diagram Patterns

## Layer diagram
```
┌─────────────────┐
│   Presentation  │
├─────────────────┤
│   Business      │
├─────────────────┤
│   Data          │
└─────────────────┘
```

## Pipeline diagram
```
[Input] ──→ [Process A] ──→ [Process B] ──→ [Output]
                │
                └──→ [Side Effect]
```

## Tree structure
```
root/
├── src/
│   ├── components/
│   └── utils/
└── tests/
```

# Extra Output
```json
{
  "width": "number — widest line in chars",
  "diagram_type": "string — layer | pipeline | tree | network | timeline"
}
```

## Steps
1. Identify structure type (hierarchy, pipeline, network, layers)
2. Draft ASCII layout within 80-char width
3. Apply consistent box-drawing characters
4. Add legend if custom symbols used
5. Verify monospace alignment

## Examples
**Input:** `/gk-preview --ascii "microservices architecture"`
**Expected behavior:** ASCII network diagram showing services as boxes with `──→` arrows for API calls, `══→` for event streams
