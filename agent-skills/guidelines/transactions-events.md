# Transactions And Events Guidelines

## Purpose

Keep transactional behavior, jobs, events, notifications, and broadcasts aligned so work only escapes a transaction when it is safe.

## Required Standards

- Use database transactions for multi-model writes and related state changes.
- Dispatch jobs, listeners, events, notifications, and broadcasts after commit when their work depends on committed database state.
- Prefer `afterCommit()` on jobs when the queue connection is not globally set to `after_commit`.
- Prefer `ShouldDispatchAfterCommit` for events that must not fire before the transaction commits.
- Prefer `ShouldQueueAfterCommit` for queued listeners that must wait for committed data.
- Keep side effects and retries explicit and testable.

## Allowed Patterns

- `DB::transaction()`
- `afterCommit()` and `beforeCommit()`
- `ShouldDispatchAfterCommit`
- `ShouldQueueAfterCommit`
- queued jobs, listeners, and notifications when the behavior needs async delivery

## Forbidden Patterns

- Dispatching state-dependent side effects before the transaction is committed.
- Assuming queued work will see uncommitted database state.
- Hiding commit behavior in random helper methods.

## Naming Conventions

- Event names should describe completed facts.
- Job names should describe the work being performed.

## Testing Expectations

- Add regression tests for commit-sensitive behavior.
- Add failure-path tests for rollback cases when side effects should not run.

## Review Checklist

- Does the side effect depend on committed data?
- Is the commit behavior explicit?
- Are retries safe after commit?

## Acceptable Examples

- `OrderShipped implements ShouldDispatchAfterCommit`
- `ProcessPodcast::dispatch($podcast)->afterCommit();`

## Unacceptable Examples

- Dispatching a job inside a transaction without commit awareness when the job reads freshly written rows.
