---
id: "03"
name: "Code review a function"
category: "review"
skill: "gk-review"
difficulty: medium
max_tokens: 10000
timeout_seconds: 90
---

## Prompt

"Review this Node.js function:
```js
async function getUser(userId) {
  const query = `SELECT * FROM users WHERE id = ${userId}`;
  const result = await db.query(query);
  return result.rows[0];
}
```
Provide a scored code review."

## Expected Criteria

- [ ] Identifies SQL injection vulnerability (critical)
- [ ] Recommends parameterized queries
- [ ] Provides a severity score or rating
- [ ] Gives ≥3 specific findings
- [ ] Suggests concrete fix with code

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| SQL injection identified | 40% | keyword: "SQL injection" or "parameterized" |
| Score/rating provided | 20% | contains number score or rating |
| ≥3 findings | 20% | ≥3 bullet points or numbered items |
| Fix with code | 20% | contains code block with parameterized query |
