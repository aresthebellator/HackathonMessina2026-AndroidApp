# Demo: security-review catch

**Prompt to the agent:**

> Review this endpoint before merge.

## Finding (High)

`GET /api/invoices/:id` returns any invoice if you know the id — **no ownership check**.

```ts
// BEFORE — looks fine, ships a data leak
app.get("/api/invoices/:id", async (req, res) => {
  const invoice = await db.invoices.findById(req.params.id);
  if (!invoice) return res.status(404).end();
  return res.json(invoice);
});
```

With `/security-review` the agent should flag:

| Severity | Rule | Why | Fix |
|----------|------|-----|-----|
| High | Missing authorization | Client-supplied id ≠ proof of access | Scope by `req.user.id` (or org), 404 if not owned |

```ts
// AFTER — authorized
app.get("/api/invoices/:id", requireAuth, async (req, res) => {
  const invoice = await db.invoices.findFirst({
    where: { id: req.params.id, userId: req.user.id },
  });
  if (!invoice) return res.status(404).end();
  return res.json(invoice);
});
```
