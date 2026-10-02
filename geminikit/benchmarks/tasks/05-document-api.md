---
id: "05"
name: "Document an API endpoint"
category: "documentation"
skill: "gk-document"
difficulty: easy
max_tokens: 8000
timeout_seconds: 60
---

## Prompt

"Document this Express.js endpoint in OpenAPI 3.0 YAML format:
```js
app.post('/api/users', async (req, res) => {
  const { name, email, role } = req.body;
  // role: 'admin' | 'user' | 'viewer'
  const user = await createUser({ name, email, role });
  res.status(201).json(user);
});
```"

## Expected Criteria

- [ ] Produces valid OpenAPI 3.0 YAML (or JSON)
- [ ] Documents request body with `name`, `email`, `role` fields
- [ ] Specifies `role` enum values: admin, user, viewer
- [ ] Documents 201 response
- [ ] Marks `name` and `email` as required

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| OpenAPI format | 25% | contains "openapi:" or "paths:" |
| Request body documented | 25% | contains "requestBody" or "properties" |
| Role enum present | 25% | keyword: "enum" with admin/user/viewer |
| Response 201 documented | 25% | keyword: "201" |
