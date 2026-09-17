---
id: "01"
name: "Plan a feature"
category: "planning"
skill: "gk-plan"
difficulty: medium
max_tokens: 15000
timeout_seconds: 120
---

## Prompt

"Plan the implementation of a user authentication system with JWT and refresh tokens for an Express.js API. The system should support email/password login and secure token rotation."

## Expected Criteria

- [ ] Produces a plan with ≥3 phases
- [ ] Identifies JWT + refresh token implementation steps
- [ ] Mentions token expiry and rotation strategy
- [ ] Includes security considerations (hashing, HTTPS, token storage)
- [ ] Response stays within 15,000 tokens

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| Phase count ≥3 | 25% | count "## Phase" or "### Phase" headings |
| JWT + refresh token mentioned | 20% | keyword: "refresh token" |
| Security section present | 25% | keyword: "security" or "HTTPS" |
| Structured output (markdown) | 15% | has headers + lists |
| Concise (not bloated) | 15% | response ≤ 15K tokens |
