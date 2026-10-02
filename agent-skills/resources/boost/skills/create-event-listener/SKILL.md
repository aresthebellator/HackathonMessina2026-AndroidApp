---
name: create-event-listener
description: "Use when creating or changing Laravel events, listeners, subscribers, or event-driven side effects."
license: MIT
metadata:
  author: Ali Harb
---

# Create Event And Listener

## When To Use

Use this skill when a completed domain fact should trigger decoupled side effects.

## Inputs Needed

- Fact name, payload, dispatch point, listeners, queue needs, transaction timing, and tests.

## Workflow

1. Inspect existing event discovery and listener conventions.
2. Name events as past-tense facts.
3. Keep event payloads minimal and serializable.
4. Dispatch events after successful state changes and after commit when the payload depends on committed data.
5. Use listeners for side effects, not primary decision logic.
6. Queue listeners when work is slow, external, or retryable.
7. Prefer `ShouldDispatchAfterCommit` and `ShouldQueueAfterCommit` when transaction timing matters.
8. Add event and listener tests.

## Files That May Be Created

- `app/Events/*`
- `app/Listeners/*`
- Tests.

## Files That May Be Modified

- Actions, jobs, providers, tests, and config.

## Architecture Rules

- Events represent facts.
- Listeners react to facts.
- Actions remain the owner of the primary operation.

## Testing Requirements

- Test event dispatch, listener behavior, queued listeners, and transaction timing where relevant.

## Security Requirements

- Do not put secrets or unnecessary private data in events.
- Ensure listeners do not bypass authorization for user-triggered side effects.

## Review Checklist

- Is the event a fact that already happened?
- Is the payload minimal?
- Are queued listeners safe to retry?

## Common Mistakes

- Using events as commands.
- Dispatching before a transaction commits.
- Hiding critical business flow in listeners.
- Forgetting to make queue-sensitive listeners commit-aware.
