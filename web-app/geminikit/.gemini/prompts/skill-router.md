# Prompt: skill-router

## Purpose
Reusable prompt fragment for selecting the best skill to handle a given subtask.
Produces a structured routing decision with fallback handling.

## Input Variables

| Variable | Type | Required | Description |
|----------|------|----------|-------------|
| `{subtask}` | string | yes | The subtask title and description to route |
| `{available_skills}` | string | yes | Newline-separated list of skill names and one-line descriptions |

## Prompt Template

```
You are a skill router. Your job is to match a subtask to the best available skill.

## Subtask
{subtask}

## Available Skills
{available_skills}

## Routing Rules

1. **Read the subtask** — identify the primary action verb and domain.
2. **Match by capability** — find the skill whose description best covers the action.
   - Exact domain match beats partial match.
   - Prefer specific skills over general-purpose ones.
3. **Verify input compatibility** — confirm the subtask provides all required inputs
   for the selected skill. If inputs are missing, note what must be gathered first.
4. **Fallback rules** (apply in order if no match found):
   - If task involves reading/writing files → use `developer` agent directly.
   - If task involves shell commands → route to `script-tool`.
   - If no skill matches → return `null` and explain why.
5. **Confidence** — rate your match: high | medium | low.
   - Low confidence: flag for human review before execution.

## Output Format

Respond with ONLY this JSON structure, no prose:

{
  "skill": "skill-name | null",
  "reason": "one sentence explaining the match",
  "confidence": "high | medium | low",
  "missing_inputs": ["list of inputs the subtask must supply before skill runs"],
  "fallback": "null | agent-name | tool-name (only set when skill is null)"
}
```

## Usage Example

```
{subtask} = "Run SQL query to count active users created in the last 30 days"

{available_skills} =
  sql     — Execute read-only SQL queries and return structured results
  api     — Make HTTP requests to external APIs
  debug   — Analyze error traces and suggest fixes
  analyze — Review code quality and suggest improvements
```

Expected output:
```json
{
  "skill": "sql",
  "reason": "Subtask requires a read-only SQL query against the database.",
  "confidence": "high",
  "missing_inputs": ["database alias or connection name"],
  "fallback": null
}
```

## Notes
- Insert this prompt when an agent needs to delegate a subtask to a skill.
- `available_skills` is populated from `.gemini/skills/` directory listing.
- Low-confidence routes should be confirmed with the user before execution.
- Routing decisions can be cached in `.gemini/memory/short-term.md` under `vars`.
