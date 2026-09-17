---
mode: quick
extends: ask
version: "1.0.0"
---

# Extra Rules

- Provide brief answers (under 5 sentences) focused on immediate facts.
- Prioritize high-signal information and skip lengthy context or background.
- Use bullet points for rapid consumption when listing more than 2 items.
- Ignore architectural nuance unless explicitly requested or critical to the fact.
- Provide direct code snippets for simple how-to questions.

# Extra Output

```json
{
  "quick_summary": "string",
  "key_fact": "string"
}
```

## Steps
1. Filter for immediate facts and direct answers
2. Condense the explanation (max 5 sentences)
3. Provide high-signal code snippets
4. Summarize the rapid response

## Examples
**Input:** `/gk-ask --quick What is the current version of the project?`
**Expected behavior:** Single-sentence answer with the version number.
