---
id: "10"
name: "Git commit and PR description"
category: "git"
skill: "gk-git"
difficulty: easy
max_tokens: 6000
timeout_seconds: 60
---

## Prompt

"Generate a git commit message and PR description for these changes: Added user authentication with JWT tokens. Created POST /auth/login and POST /auth/refresh endpoints. Added middleware to protect routes. Fixed password hashing to use bcrypt rounds=12."

## Expected Criteria

- [ ] Commit message follows conventional commits format (`feat:`, `fix:`, etc.)
- [ ] Commit subject ≤72 characters
- [ ] PR description has Summary section
- [ ] PR description mentions both new endpoints
- [ ] PR description has a test plan or checklist

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| Conventional commit format | 25% | starts with "feat:" or "fix:" or "chore:" etc. |
| Subject ≤72 chars | 20% | first line length |
| PR Summary section | 25% | keyword: "## Summary" or "## Description" |
| Endpoints mentioned | 15% | keywords: "/auth/login" and "/auth/refresh" |
| Test plan/checklist | 15% | contains "- [ ]" or "Test plan" section |
