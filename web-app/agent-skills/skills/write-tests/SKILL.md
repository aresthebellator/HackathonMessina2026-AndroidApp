---
name: write-tests
description: "Use when writing or updating Pest tests for Laravel features, actions, APIs, Livewire components, authorization, architecture, jobs, events, or regressions."
license: MIT
metadata:
  author: Ali Harb
---

# Write Tests

## When To Use

Use this skill for meaningful behavior changes, regressions, refactors, authorization, validation, API contracts, Livewire interactions, jobs, events, and architecture rules.

## Inputs Needed

- Behavior, risk, actors, input, output, side effects, failure paths, existing tests, and command to run.

## Workflow

1. Inspect existing test style and factories.
2. Choose the smallest useful test type.
3. Use feature tests for requests, APIs, Livewire, authorization, validation, and integration behavior.
4. Use unit tests for pure Actions, Services, DTOs, Value Objects, and support code.
5. Use API tests for status codes, JSON contracts, errors, and authentication.
6. Use architecture tests for dependency direction and layer boundaries where practical.
7. Use authorization tests for allow and deny paths.
8. Use failure-path tests for invalid state, invalid input, external failure, and queue retry behavior.
9. Use regression tests for known bugs.
10. Test attribute consumers when custom or framework attributes enforce behavior.
11. Include architecture tests for boundaries such as controllers, Livewire, models, builders, requests, and domain code when the project standard depends on them.
12. Use `#[\Override]` in PHPUnit-style lifecycle methods when present, but prefer Pest style already used by the project.
13. Run the smallest relevant test set with `php artisan test --compact`.

## Files That May Be Created

- Pest feature, unit, API, Livewire, architecture, and regression tests.

## Files That May Be Modified

- Existing tests, factories, seeders, test helpers, and Pest configuration.

## Architecture Rules

- Tests should verify behavior, not require business logic to stay in client layers.
- Architecture tests should enforce meaningful boundaries without brittle implementation assumptions.
- Keep architecture tests tolerant of third-party package traits or framework internals unless the project explicitly owns that code.

## Testing Requirements

- Test success, failure, validation, authorization, side effects, and regression paths proportional to risk.
- Test Laravel and custom attribute behavior through the code that consumes the attribute.

## Security Requirements

- Include denial-path tests for protected behavior and malicious input where relevant.

## Review Checklist

- Does each test prove behavior?
- Are factories used consistently?
- Are fakes applied after model setup when needed?
- Is the test focused and stable?

## Common Mistakes

- Testing implementation details only.
- Missing denial paths.
- Creating brittle architecture tests that fail on harmless code.
