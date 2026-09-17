# Webhooks Guidelines

## Purpose

Implement webhook flows that are secure, idempotent, and observable.

## Rules

- Verify provider signatures before processing payloads.
- Store event IDs or idempotency keys.
- Dispatch slow processing to queues.
- Log safe event metadata, not full sensitive payloads.
- Test duplicates, invalid signatures, unknown events, and success paths.

## Anti-Patterns

- Trusting unsigned payloads.
- Mutating state before validation.
- Failing on provider retries.
