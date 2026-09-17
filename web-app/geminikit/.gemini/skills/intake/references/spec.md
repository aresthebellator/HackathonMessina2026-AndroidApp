---
mode: spec
extends: gk-intake
version: "1.0.0"
---

# Extra Rules
- MUST follow a standard mini-spec template: Overview, User Stories, Technical Constraints.
- MUST suggest a potential tech stack if not specified.

# Extra Output
```json
{
  "mini_spec": {
    "overview": "string",
    "user_stories": ["string"],
    "technical_constraints": ["string"],
    "suggested_stack": ["string"]
  }
}
```

## Steps
1. Synthesize the `raw_input` into a formal overview.
2. Break down the idea into at least 3 core User Stories (As a... I want... So that...).
3. List common technical hurdles for this type of project.

## Examples
**Input:** `/gk-intake --spec "A tool to sync local files to S3"`
**Expected behavior:** Generates a document with user stories for auth, sync triggers, and error handling.
