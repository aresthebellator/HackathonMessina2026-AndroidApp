---
mode: explain
extends: preview
version: "1.0.0"
---

# Extra Rules
- Structure explanation as: Overview → How it works → Key components → Data flow → Edge cases
- Use headers and bullet points — avoid dense paragraphs
- Every concept MUST have a concrete, single-sentence TL;DR at the top
- For code flows: trace input→processing→output with named stages
- Add a "Common Misconceptions" section if the concept is frequently misunderstood
- Target length: 300-600 words for `developer`, 150-300 for `stakeholder`, 600+ for `beginner`

# Extra Output
```json
{
  "sections": ["string — list of sections generated"],
  "word_count": "number",
  "tldr": "string — one-sentence summary"
}
```

## Steps
1. Identify audience from `audience` field (default: developer)
2. Extract key concepts from source code or topic
3. Write TL;DR (1 sentence)
4. Build explanation: Overview → How it works → Key components → Data flow
5. Add Common Misconceptions if applicable
6. Tune vocabulary to audience level

## Examples
**Input:** `/gk-preview --explain "Next.js App Router data fetching"`
**Expected behavior:** Sections: Overview (RSC vs Client), How it works (fetch caching), Key components (layout.tsx, page.tsx, loading.tsx), Data flow (request → RSC → streaming), Misconceptions (cache invalidation)
