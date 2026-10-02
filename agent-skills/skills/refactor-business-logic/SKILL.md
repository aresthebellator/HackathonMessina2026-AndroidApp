---
name: refactor-business-logic
description: "Use when moving business logic out of controllers, Livewire components, models, routes, Blade views, or duplicated client layers."
license: MIT
metadata:
  author: Ali Harb
---

# Refactor Business Logic

## When To Use

Use this skill when behavior is in the wrong layer, duplicated, hard to test, or not reusable across clients.

## Inputs Needed

- Current behavior, callers, desired boundary, preserved outputs, risks, and tests.

## Workflow

1. Identify logic inside controllers, Livewire components, models, routes, or views.
2. Add or locate regression tests before moving risky behavior.
3. Extract single operations to Actions.
4. Extract coordination or external integration to Services.
5. Extract contracts to DTOs, Data objects, Value Objects, casts, or concerns.
6. Extract access rules to Policies.
7. Extract side effects to Events, Listeners, or Jobs when useful.
8. Update callers to use the new reusable class.
9. Preserve behavior and avoid unrelated rewrites.
10. Run focused tests and formatting.

## Files That May Be Created

- Actions, Services, DTOs, Policies, Events, Jobs, Support classes, and tests.

## Files That May Be Modified

- Existing controllers, Livewire components, models, routes, Blade views, commands, jobs, and tests.

## Architecture Rules

- Refactors should reduce client-layer behavior and improve reuse.
- Preserve public contracts unless the task explicitly changes them.
- Prefer builders for query extraction, concerns for shared model behavior, and shared components for repeated UI.

## Testing Requirements

- Regression tests must prove preserved behavior.
- Add focused unit tests for newly extracted pure logic when useful.

## Security Requirements

- Do not lose authorization, validation, transactions, escaping, or throttling during extraction.

## Review Checklist

- Is behavior preserved?
- Is the new location more reusable?
- Are changes focused?
- Are existing callers updated consistently?

## Common Mistakes

- Broad rewrites during a narrow extraction.
- Moving code without tests.
- Extracting to vague helpers instead of clear operations.
