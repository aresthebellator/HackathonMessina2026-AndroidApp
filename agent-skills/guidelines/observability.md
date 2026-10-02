# Observability Guidelines

## Purpose

Make Laravel systems easier to operate and debug.

## Rules

- Add structured logs for important state transitions and failures.
- Track metrics for queues, external APIs, jobs, slow queries, and user-facing flows.
- Avoid logging secrets or full sensitive payloads.
- Add health checks for critical dependencies.
- Document alert meaning and owner action.

## Anti-Patterns

- Logging too much unstructured noise.
- Creating dashboards without actionable thresholds.
