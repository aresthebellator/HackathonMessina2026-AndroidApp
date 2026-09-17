---
name: implement-webhooks
description: "Use when implementing inbound or outbound webhooks with validation, signatures, retries, idempotency, and tests."
license: MIT
metadata:
  author: Ali Harb
---

# Implement Webhooks

## Workflow

1. Identify provider, event types, signature mechanism, retry behavior, and idempotency key.
2. Create a dedicated route, controller, request validation, and processing action/job.
3. Verify signatures before parsing trusted data.
4. Store processed event IDs to prevent duplicate effects.
5. Dispatch slow work to queues after validation.
6. Add tests for valid, invalid signature, duplicate, unknown event, and retry-safe behavior.

## Verification

- Webhook tests pass.
- Signature failure never mutates state.

## Common Mistakes

- Processing unsigned payloads.
- Performing non-idempotent writes on retries.
