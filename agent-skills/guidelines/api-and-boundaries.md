# API And Boundary Guidelines

## Purpose

Make every feature ready for API, UI, queue, command, and integration use without duplicating logic across clients.

## Required Standards

- API routes should be versioned, such as `api/v1`, unless the existing application has a different established convention.
- Use named routes for generated links and stable route names for client-facing endpoints.
- Use request validation, DTOs, Actions, Policies, and Resources instead of embedding logic in endpoints.
- Responses should use a consistent shape for success, validation failure, authorization failure, and domain errors.
- Livewire UX validation may improve interaction, but authoritative validation must be reusable outside Livewire.
- Database writes must protect integrity with constraints, indexes, transactions, and idempotency where needed.
- Security-sensitive operations require rate limiting, authorization, auditability where appropriate, and safe error messages.
- Performance-sensitive reads must consider eager loading, selected columns, indexes, pagination, caching, and queue boundaries.

## Allowed Patterns

- `Route::prefix('v1')->group(...)` or existing versioning conventions for API routes.
- Form Requests at HTTP boundaries and DTOs passed into Actions.
- API Resources for model serialization.
- `Gate::authorize()` or `$this->authorize()` before protected operations.
- Jobs for asynchronous, slow, external, retryable, or failure-tolerant work.
- Events after successful state changes to decouple side effects.

## Forbidden Patterns

- Returning raw models from public APIs when resource shape matters.
- Reusing Blade, Livewire, or HTTP-specific objects inside domain/application classes.
- Allowing hidden mass assignment through unvalidated input.
- Performing external network calls inside database transactions unless unavoidable and documented.
- Querying inside loops or Blade templates.
- Dispatching events to request behavior before the state change has succeeded.

## Naming Conventions

- API controllers describe resources or operations clearly, such as `RecordController` or `ApproveRecordController`.
- Form Requests use operation names, such as `StoreRecordRequest`.
- Resources use Laravel conventions, such as `RecordResource`.
- DTO factory methods may be named for boundaries, such as `fromRequest`, `fromArray`, or `fromModel`.

## Testing Expectations

- Test API status codes, response shape, validation failures, authorization failures, and success paths.
- Test that web and Livewire layers reuse the same Actions as API endpoints when they share behavior.
- Test queue dispatch, event dispatch, and transaction-sensitive behavior where relevant.
- Add performance-oriented tests only when they prove a stable expectation without brittleness.

## Review Checklist

- Is the endpoint versioned and named consistently?
- Is request data validated before use?
- Is authorization checked before state changes or sensitive reads?
- Is the Action client-agnostic?
- Is output serialized through a stable contract?
- Are indexes and pagination present for growing datasets?
- Are failures safe for API clients and logs?

## Acceptable Examples

```php
Route::prefix('v1')->name('api.v1.')->group(function (): void {
    Route::apiResource('records', RecordController::class);
});
```

```php
return response()->json([
    'data' => new RecordResource($record),
]);
```

## Unacceptable Examples

```php
Route::post('/records', fn (Request $request) => Record::create($request->all()));
```

```php
return response()->json(Record::with('privateNotes')->get());
```
