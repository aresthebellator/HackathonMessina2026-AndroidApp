---
mode: diagram
extends: preview
version: "1.0.0"
---

# Extra Rules
- Default to Mermaid.js v11 syntax — always use `%%{init: ...}%%` for config when needed
- Choose diagram type by content:
  - System architecture → `C4Context` or `graph LR`
  - Sequence/request flow → `sequenceDiagram`
  - State machine → `stateDiagram-v2`
  - DB schema → `erDiagram`
  - Git branching → `gitGraph`
  - Timeline/process → `flowchart TD`
- MUST validate: no duplicate node IDs, quoted labels with special chars, max ~30 nodes
- Wrap output in ` ```mermaid ``` ` fenced block
- Add a plain-English legend below the diagram for any non-obvious notation

# Extra Output
```json
{
  "diagram_type": "string — flowchart | sequenceDiagram | erDiagram | stateDiagram-v2 | C4Context",
  "node_count": "number",
  "mermaid_source": "string"
}
```

## Steps
1. Identify relationships and flow from source code or topic
2. Select appropriate Mermaid diagram type
3. Draft node/edge structure (max 30 nodes)
4. Validate syntax: no duplicate IDs, special chars quoted
5. Add legend for non-obvious notation
6. Save to `reports/preview/` as `.md` file

## Examples
**Input:** `/gk-preview --diagram "auth middleware flow"`
**Expected behavior:** `sequenceDiagram` showing Client→Middleware→JWTValidator→Controller with alt blocks for valid/invalid token
