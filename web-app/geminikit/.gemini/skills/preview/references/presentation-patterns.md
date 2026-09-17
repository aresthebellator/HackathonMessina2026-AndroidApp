---
mode: slides
extends: preview
version: "1.0.0"
---

# Extra Rules
- Structure as: Title slide → Agenda → Content slides (3-7) → Summary → Next Steps
- Each slide: 1 header + max 5 bullet points OR 1 code block OR 1 diagram
- Use `---` separator between slides (Marp/Reveal.js compatible)
- Bullet points MUST be action-oriented: "Use X to achieve Y" not "X is Y"
- Add speaker notes as `<!-- Note: ... -->` after each slide
- No slide longer than 10 lines of content
- Code examples: prefer short (≤15 lines), annotated with `# ←` inline comments

# Slide Template
```markdown
---
# [Slide Title]

- Key point 1 — brief explanation
- Key point 2 — brief explanation
- Key point 3 — brief explanation

<!-- Note: Speaker context for this slide -->
```

# Extra Output
```json
{
  "slide_count": "number",
  "has_code_examples": "boolean",
  "has_diagrams": "boolean",
  "estimated_minutes": "number — slide_count × 1.5"
}
```

## Steps
1. Extract 3-7 key concepts from topic
2. Draft slide sequence: Title → Agenda → Content × N → Summary → Next Steps
3. Write each slide: 1 heading + ≤5 bullets or code block
4. Add speaker notes
5. Estimate presentation time

## Examples
**Input:** `/gk-preview --slides "how JWT authentication works"`
**Expected behavior:** 6 slides: What is JWT | Token structure | Signing & verification | Refresh token flow | Security pitfalls | Summary + code
