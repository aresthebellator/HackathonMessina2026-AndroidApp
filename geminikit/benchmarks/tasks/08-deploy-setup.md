---
id: "08"
name: "Set up Docker deployment"
category: "devops"
skill: "gk-deploy"
difficulty: medium
max_tokens: 10000
timeout_seconds: 90
---

## Prompt

"Generate a production-ready Dockerfile and docker-compose.yml for a Node.js Express API (Node 20, port 3000) with a PostgreSQL 15 database. Include health checks and proper environment variable handling."

## Expected Criteria

- [ ] Dockerfile uses multi-stage build (build + production stages)
- [ ] Dockerfile uses `node:20-alpine` or similar slim base
- [ ] docker-compose.yml defines both app and postgres services
- [ ] Health check defined for the app service
- [ ] Environment variables via `.env` or compose environment section
- [ ] Non-root user in Dockerfile

## Scoring

| Criterion | Weight | Check |
|-----------|--------|-------|
| Multi-stage Dockerfile | 25% | keyword: "FROM ... AS" (two FROM statements) |
| Alpine/slim base | 15% | keyword: "alpine" or "slim" |
| docker-compose with postgres | 25% | keyword: "postgres" in compose file |
| Health check defined | 20% | keyword: "healthcheck" |
| Non-root user | 15% | keyword: "USER" instruction |
