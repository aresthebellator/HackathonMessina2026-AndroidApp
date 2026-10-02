---
mode: gen
extends: ui
version: "1.0.0"
---

# Extra Rules
- Focus on leveraging Google's AI Design ecosystem (Stitch, Material 3, AI Studio).
- Provide a structured Information Architecture (IA) before visual ideation.
- Generate optimized prompts for Google Stitch (Text-to-UI).
- Create Material Design 3 (M3) color palette strategy.
- Suggest "Design-to-Code" paths using Google AI Studio.

# Extra Output
```json
{
  "strategy": {
    "ia_structure": ["string"],
    "stitch_prompts": ["string"],
    "m3_palette": {
      "primary": "string",
      "secondary": "string",
      "surface": "string"
    },
    "external_tools": [
      {"name": "string", "url": "string", "action": "string"}
    ]
  }
}
```

## Steps
1. Define Information Architecture (IA) based on requirements
2. Generate specific UI generation prompts for Google Stitch
3. Define Material Design 3 color and typography strategy
4. Provide links and instructions for external Google AI tools
5. Summarize the end-to-end design strategy

## Examples
**Input:** `/gk-design --gen Fitness tracking dashboard`
**Expected behavior:** Returns a full IA, Stitch prompts, and M3 theme strategy.
