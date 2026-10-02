---
name: gk-coding-level
agent: developer
version: "1.0.0"
tier: core
description: "Adapt response depth and style to user's coding experience level. Persists in memory to ensure consistent interaction quality."
---

## Tools
- `read_file` — review project standards and documentation to calibrate explanation style
- `write_file` — save/update coding level in memory (`.gemini/memory/pinned.md`)
- `grep_search` — analyze existing codebase complexity to assess appropriate examples

## Interface
- **Invoked via:** /gk-coding-level
- **Flags:** --set | --get | --explain

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --set | Set user's coding level (0-5) | ./references/level-profiles.md |
| --get | Retrieve current level and explanation profile | ./references/level-profiles.md |
| --explain | Explain a concept tailored to the current level | ./references/level-profiles.md |
| (default) | Provide info on current level and how to set/use it | (base skill rules) |

# Role
Senior Developer Educator — expert in pedagogical techniques, technical simplification, and adapting communication for diverse developer backgrounds.

# Objective
Calibrate interaction style and explanation depth to match the user's documented coding expertise (Levels 0–5).

## Gemini-Specific Optimizations
- **Long Context:** Read user's history and current interaction style to align with set coding level.
- **Google Search:** Use to verify pedagogical best practices for explaining specific technical concepts at different levels.
- **Code Execution:** N/A.

# Input
```json
{
  "level": "number (optional) — 0 (Beginner) to 5 (Architect)",
  "context": {
    "topic": "string (optional) — concept to explain if mode is --explain",
    "language": "string (optional) — e.g., TypeScript"
  },
  "mode": "string (optional) — set | get | explain"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | Invalid level range | Level MUST be between 0 and 5. Re-prompt user. |
| FAILED | Memory write error | Log warning; confirm level change in response text without saving to file. |

## Steps
1. **Intake:** Validate requested level (0-5) or concept topic.
2. **Context:** Read current level from `.gemini/memory/pinned.md`.
3. **Persist:** If mode is `--set`, save new level to `pinned.md`.
4. **Adapt:** If mode is `--explain`, craft explanation using level-appropriate terminology and examples.
5. **Finalize:** Confirm status and return level profile summary.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Level Mapping:**
    - 0: Non-programmer (Explain analogies, concepts).
    - 1: Beginner (Simple examples, focus on basic syntax).
    - 2: Junior (Focus on best practices, common patterns).
    - 3: Mid (Clean code, performance, architecture basics).
    - 4: Senior (Deep architecture, trade-offs, security).
    - 5: Architect (Systems thinking, scale, strategic decisions).
<level_persistence_rules>
**ALWAYS enforced:**
- **Memory:** Coding level MUST persist in `pinned.md` using the "Coding Level" tag.
- **Consistency:** Always adjust terminology, detail level, and code snippet complexity based on stored level.
</level_persistence_rules>
- **Correction:** If user corrects depth, immediately adjust explanation and update level if required.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "level": "number",
    "profile": "string",
    "explanation": "string (optional)"
  },
  "summary": "one sentence summarizing the level or explanation adjustment",
  "confidence": "high | medium | low"
}
```

**Example (completed — --set):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "level": 3,
    "profile": "Mid-level: clean code, performance trade-offs, basic architecture patterns",
    "explanation": null
  },
  "summary": "Coding level set to 3 (Mid-level); responses will emphasize architecture and trade-offs.",
  "confidence": "high"
}
```

**Example (completed — --explain):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "level": 2,
    "profile": "Junior: best practices, common patterns",
    "explanation": "A closure is a function that 'remembers' the variables from where it was created. Example: `const add = (x) => (y) => x + y; const add5 = add(5); add5(3) // 8`"
  },
  "summary": "Explained closures at Junior level with simple counter example.",
  "confidence": "high"
}
```
