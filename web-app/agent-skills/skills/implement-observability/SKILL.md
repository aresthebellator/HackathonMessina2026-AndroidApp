---
name: implement-observability
description: "Use when adding logs, metrics, traces, health checks, Pulse recorders, or operational dashboards."
license: MIT
metadata:
  author: Ali Harb
---

# Implement Observability

## Workflow

1. Identify operational questions the app must answer.
2. Add structured logs around failures and important state transitions.
3. Add metrics for queues, external APIs, slow queries, jobs, and user-facing flows.
4. Avoid logging secrets or sensitive payloads.
5. Add health checks for critical dependencies.
6. Document how operators should interpret alerts and dashboards.

## Verification

- Logs and metrics appear in local/test observability tooling.
- Sensitive data is not emitted.

## Common Mistakes

- Logging entire request payloads.
- Adding dashboards without actionable thresholds.
