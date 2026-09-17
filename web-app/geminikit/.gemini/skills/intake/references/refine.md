---
mode: refine
extends: gk-intake
version: "1.0.0"
---

# Extra Rules
- MUST generate 3-5 targeted questions to fill identified gaps.
- MUST focus questions on constraints, target audience, and core functionality.
- MUST NOT provide solutions yet; stay in the discovery phase.
- **NEVER embed questions in JSON output** — call `ask_user` for each question directly. Example:
  ```
  ask_user("Who is the primary user — internal team, customers, or developers?")
  ask_user("Does this need to persist data, or is it session-only?")
  ```
- Only output the JSON result AFTER all questions have been answered by the user.

## Steps
1. Analyze the `raw_input` for missing critical information (Who, What, Why, How).
2. Call `ask_user` for each identified gap — one question at a time, wait for answer before next.
3. Suggest 2-3 optional "nice-to-have" features based on the core idea after receiving answers.

## Examples
**Input:** `/gk-intake --refine "I want to build a simple todo app"`
**Expected behavior:** Instead of just structuring, it asks about persistence, platforms, and specific features (reminders, tags, etc.).
