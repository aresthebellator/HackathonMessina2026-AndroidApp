---
id: "06"
name: "Refactor duplicated code"
category: "refactoring"
skill: "gk-refactor"
difficulty: medium
max_tokens: 10000
timeout_seconds: 90
---

## Prompt

"Refactor this code to eliminate duplication (DRY principle):
```js
function getAdminUsers() {
  const users = db.query('SELECT * FROM users WHERE role = ?', ['admin']);
  return users.map(u => ({ id: u.id, name: u.name, email: u.email }));
}

function getViewerUsers() {
  const users = db.query('SELECT * FROM users WHERE role = ?', ['viewer']);
  return users.map(u => ({ id: u.id, name: u.name, email: u.email }));
}

function getRegularUsers() {
  const users = db.query('SELECT * FROM users WHERE role = ?', ['user']);
  return users.map(u => ({ id: u.id, name: u.name, email: u.email }));
}
```"

## Expected Criteria

- [ ] Extracts a shared `getUsersByRole(role)` function
- [ ] Original 3 functions become thin wrappers or are removed
- [ ] No functional behavior change
- [ ] Explains the refactoring decision

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| Shared function extracted | 40% | keyword: "getUsersByRole" or similar generic function |
| DRY principle applied | 30% | duplication eliminated (1 map + 1 query block) |
| Original API preserved | 20% | getAdminUsers, getViewerUsers, getRegularUsers still callable |
| Explanation provided | 10% | has prose explanation of change |
