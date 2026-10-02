---
name: create-action
description: "Use when creating or changing an Action class for a Laravel business operation. Applies to reusable operations called from controllers, Livewire, jobs, commands, APIs, MCP handlers, or services."
license: MIT
metadata:
  author: Ali Harb
---

# Create Action

## When To Use

Use this skill for one reusable business operation.

## Inputs Needed

- Operation name, actor, inputs, output, authorization rule, transaction needs, events or jobs, and expected tests.

## Workflow

1. Inspect sibling Actions and tests.
2. Identify collaborators and inject them through the constructor instead of calling `app()` or using service locators.
3. Create the class under `app/Actions` unless the project already uses a narrower namespace.
4. Name the class as an imperative operation with no suffix.
5. Make the class `final readonly` when dependencies are constructor-injected and mutable state is not required.
6. Add one public `handle()` method with typed input and typed output.
7. Use enums, DTOs, value objects, typed class constants, `match`, and named arguments where they clarify the operation.
8. Use Laravel container contextual attributes on constructor parameters when they replace simple service-provider boilerplate.
9. Add `#[\Override]` to any method that intentionally overrides a parent or interface method.
10. Keep request, response, Livewire, and Blade concepts out of the Action.
11. Authorize before sensitive reads, writes, dispatches, or external calls.
12. Use `DB::transaction()` for complex multi-model writes.
13. Dispatch events only after the state change succeeds.
14. Add or update tests.

## Files That May Be Created

- `app/Actions/<Operation>.php`
- `tests/Feature/*Test.php`
- `tests/Unit/*Test.php`

## Files That May Be Modified

- Controllers, Livewire components, jobs, commands, services, policies, events, and tests that call the Action.

## Architecture Rules

- One business operation per Action.
- Public `handle()` method unless a framework contract requires otherwise.
- No UI logic, request objects, response objects, or route-specific behavior.
- Prefer DTOs or typed values over unstructured arrays for meaningful inputs.
- Prefer modern PHP 8.3+ type-safety features whenever they reduce ambiguity.
- Dependencies must be injected unless there is a clear framework-boundary reason not to.

## Testing Requirements

- Test success, authorization denial, validation or invalid input paths, side effects, and regression cases.

## Security Requirements

- Authorize protected behavior and avoid mass assignment from unvalidated input.

## Review Checklist

- Is the Action reusable by web, API, Livewire, jobs, commands, and MCP?
- Are inputs and outputs typed?
- Is authorization explicit?
- Are transactions and side effects correctly placed?

## Common Mistakes

- Creating a catch-all Action.
- Passing `Request` or Livewire state into `handle()`.
- Hiding authorization in the caller only when the Action can be called elsewhere.
- Calling `app()` or facades for collaborators that should be injected.
