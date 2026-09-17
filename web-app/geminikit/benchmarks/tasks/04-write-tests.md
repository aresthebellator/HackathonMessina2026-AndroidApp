---
id: "04"
name: "Write unit tests"
category: "testing"
skill: "web-testing"
difficulty: medium
max_tokens: 12000
timeout_seconds: 90
---

## Prompt

"Write Vitest unit tests for this function:
```js
function calculateDiscount(price, discountPercent) {
  if (price <= 0) throw new Error('Price must be positive');
  if (discountPercent < 0 || discountPercent > 100) throw new Error('Discount must be 0-100');
  return price * (1 - discountPercent / 100);
}
```
Cover happy path and edge cases."

## Expected Criteria

- [ ] Tests happy path (normal discount calculation)
- [ ] Tests edge case: price = 0 (throws)
- [ ] Tests edge case: discount = 100 (free)
- [ ] Tests edge case: discount > 100 (throws)
- [ ] Uses `describe` + `it`/`test` structure
- [ ] Uses `expect` assertions with correct values

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| Happy path test | 20% | test with valid price + discount |
| Error throwing tests | 30% | `expect(...).toThrow()` present |
| Edge case coverage | 25% | discount=0, discount=100 covered |
| Vitest syntax correct | 25% | `import { describe, it, expect }` or `from 'vitest'` |
