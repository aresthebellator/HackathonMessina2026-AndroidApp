---
id: "02"
name: "Debug a null pointer error"
category: "debugging"
skill: "gk-debug"
difficulty: easy
max_tokens: 8000
timeout_seconds: 60
---

## Prompt

"My Node.js API throws 'Cannot read properties of undefined (reading 'id')' at routes/users.js:42. The route is: `app.get('/users/:id', async (req, res) => { const user = await db.find(req.params.id); res.json({ id: user.id, name: user.name }); })`. Debug this."

## Expected Criteria

- [ ] Identifies root cause: `db.find()` may return `undefined`/`null`
- [ ] Suggests null-check before accessing `user.id`
- [ ] Proposes a concrete fix (guard clause or optional chaining)
- [ ] Mentions 404 response for missing user

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| Root cause identified | 35% | keyword: "null" or "undefined" or "not found" |
| Fix proposed (code) | 30% | contains code block |
| 404 handling mentioned | 20% | keyword: "404" or "not found" |
| Response concise | 15% | ≤ 8K tokens |
