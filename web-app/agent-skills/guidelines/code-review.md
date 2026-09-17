# Code Review Guidelines

## Purpose

Provide a reusable checklist for reviewing Laravel features, refactors, and AI-generated code.

## Required Standards

- Lead with correctness, security, data integrity, and maintainability risks.
- Verify architecture boundary compliance before style concerns.
- Confirm protected behavior is authorized and validation is authoritative.
- Confirm business logic is reusable through Actions, Services, DTOs, Policies, Events, Jobs, Value Objects, or Support classes.
- Confirm large models, providers, and components are decomposed when they begin to get fat.
- Confirm dependencies are injected instead of resolved through `app()`, `resolve()`, or service locators.
- Confirm Laravel 13 and PHP 8.3+ attributes are used where they clarify framework metadata without hiding business logic.
- Check that tests prove meaningful success, failure, authorization, and regression paths.

## Allowed Patterns

- Thin controllers, routes, Blade views, Livewire components, and models.
- Actions for single operations and Services for coordination.
- Policies for protected resources.
- Form Requests, DTOs, and domain validation for input contracts.
- Resources and versioned routes for APIs.
- Jobs with retry, timeout, uniqueness, and failure behavior when asynchronous work is used.

## Forbidden Patterns

- Business workflows in client or presentation layers.
- Missing authorization on sensitive reads or writes.
- Unvalidated input, raw SQL with user input, unsafe file handling, or leaking sensitive errors.
- N+1 queries, unbounded datasets, missing pagination, or missing indexes on growing queries.
- Queued jobs that are not idempotent enough for retries.
- Tests that only assert implementation details without behavior value.

## Naming Conventions

- Names should expose intent and domain meaning.
- Avoid vague classes such as `Manager`, `Helper`, or `Processor` unless the responsibility is narrow and clear.
- Test names should describe expected behavior.

## Testing Expectations

- Feature tests cover request, response, validation, authorization, and integration behavior.
- Unit tests cover isolated Actions, Services, DTOs, Value Objects, and support code.
- API tests cover versioned routes and response contracts.
- Architecture tests enforce dependency direction where practical.
- Failure-path tests cover invalid state, denied users, missing records, external failures, and queue retries where relevant.

## Review Checklist

- Architecture boundaries: no workflows in controllers, routes, Blade, Livewire, or models.
- Business logic placement: operation lives in a reusable class.
- Authorization: explicit policy or gate checks exist and are tested.
- Validation: input is validated at the boundary and reusable where needed.
- DTO usage: data contracts are client-agnostic.
- Action/service usage: Actions stay single-purpose; Services do not become dumping grounds.
- Dependency injection: collaborators are injected; `app()` is limited to framework boundaries or genuine dynamic resolution.
- Modern Laravel/PHP: attributes, enums, typed constants, readonly objects, and `#[\Override]` are used where they improve clarity.
- Modern framework usage: builders, after-commit dispatch, container attributes, and current-version APIs are preferred over outdated patterns.
- Localization: user-facing text is translated, and app copy lives in app-owned language files rather than hardcoded strings.
- Architecture tests: boundary rules exist for the important layers and are not brittle.
- Frontend quality: Blade, Tailwind, and Flux UI are responsive, RTL-aware, accessible, localized, and not duplicated across heavy template files.
- Database: constraints, indexes, transactions, casts, and relationships support the behavior.
- Queue safety: jobs are retryable, time-bounded, idempotent where possible, and safe after commits.
- Event usage: events represent facts and listeners handle side effects.
- Commit safety: jobs, listeners, events, broadcasts, and notifications that depend on committed data are deferred until the transaction commits.
- API reuse: web, Livewire, API, commands, and jobs do not duplicate logic.
- Error handling: domain errors are intentional and boundary responses are safe.
- Security: secrets, mass assignment, file uploads, escaping, throttling, and raw queries are safe.
- Performance: eager loading, pagination, caching, and query shape are appropriate.
- Tests: success, failure, authorization, validation, and regression paths are covered.
- Documentation: important behavior is documented only when useful or requested.
- Naming: classes and methods are descriptive.
- Formatting and static analysis: Pint and relevant analysis tools pass.

## Acceptable Examples

```php
$this->authorize('delete', $record);

app(DeleteRecord::class)->handle($request->user(), $record);
```

## Unacceptable Examples

```php
@if ($user->isAdmin())
    <form method="POST" action="/records/{{ $record->id }}/delete">
        @csrf
    </form>
@endif
```
