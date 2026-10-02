---
id: "09"
name: "Brainstorm architecture decision"
category: "architecture"
skill: "gk-brainstorm"
difficulty: hard
max_tokens: 15000
timeout_seconds: 120
---

## Prompt

"I'm building a chat application expecting 10K concurrent users. Should I use WebSockets, Server-Sent Events (SSE), or long-polling? We're on Node.js + Redis. Brainstorm the options with trade-offs."

## Expected Criteria

- [ ] Covers all 3 approaches: WebSockets, SSE, long-polling
- [ ] Provides trade-offs for each (pros + cons)
- [ ] Gives a final recommendation with reasoning
- [ ] Mentions Redis for pub/sub or session management
- [ ] Considers scale (10K concurrent users)

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| All 3 approaches covered | 30% | keywords: "WebSocket" + "SSE" + "long-polling" |
| Trade-offs listed | 25% | both pros and cons per approach |
| Final recommendation | 25% | clear winner chosen with reasoning |
| Redis integration mentioned | 20% | keyword: "Redis" + (pub/sub or horizontal scaling) |
