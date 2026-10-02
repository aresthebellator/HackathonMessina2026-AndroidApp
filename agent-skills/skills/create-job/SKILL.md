---
name: create-job
description: "Use when creating or changing Laravel queued jobs, batches, asynchronous workflows, or retryable background work."
license: MIT
metadata:
  author: Ali Harb
---

# Create Job

## When To Use

Use this skill for asynchronous, slow, retryable, external, scheduled, or failure-tolerant work.

## Inputs Needed

- Work to perform, payload, queue, timeout, retries, uniqueness, idempotency, failure handling, and tests.

## Workflow

1. Inspect existing queue conventions.
2. Create the job with Artisan.
3. Prefer queue attributes such as `#[Queue]`, `#[Connection]`, `#[Tries]`, `#[Backoff]`, `#[Timeout]`, `#[MaxExceptions]`, `#[Delay]`, `#[UniqueFor]`, `#[FailOnTimeout]`, `#[DeleteWhenMissingModels]`, and `#[WithoutRelations]` for simple job metadata.
4. Pass IDs or small serializable data instead of large mutable objects when practical.
5. Inject services into `handle()` instead of calling `app()` from the job body.
6. Set timeout, tries or retry strategy, backoff, and failure behavior.
7. Make retries idempotent where possible.
8. Dispatch after commit when the job depends on newly written data, or set queue connection `after_commit` behavior intentionally.
9. Add tests with queue fakes or synchronous execution as appropriate.

## Files That May Be Created

- `app/Jobs/*`
- Job tests.

## Files That May Be Modified

- Actions, listeners, commands, schedules, queue config, and tests.

## Architecture Rules

- Jobs run background work; Actions own primary business operations.
- Jobs may call Actions or Services.
- Jobs should not contain UI or HTTP response logic.
- Job dependencies should be method-injected into `handle()` unless they must be serialized.

## Testing Requirements

- Test dispatch, payload, retry-sensitive behavior, failure handling, and idempotency where relevant.

## Security Requirements

- Re-authorize or validate assumptions for delayed work when permissions or state may change.
- Avoid logging secrets.

## Review Checklist

- Is async work necessary?
- Is the job retry-safe?
- Are timeout and retry settings explicit?

## Common Mistakes

- Queueing non-idempotent work without safeguards.
- Dispatching inside a transaction too early.
- Storing user-provided secrets in job payloads.
