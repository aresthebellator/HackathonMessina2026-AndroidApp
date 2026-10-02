# Application Architecture Guidelines

## Purpose

Keep Laravel code reusable across web, API, mobile, desktop, command, queue, MCP, and third-party clients by placing behavior in client-agnostic application layers.

## Required Standards

- Controllers orchestrate requests, validation results, authorization, actions, resources, redirects, and responses only.
- Livewire components manage UI state and call Actions. They must not contain domain workflows.
- Routes map traffic to controllers, Livewire pages, invokable classes, or closures for trivial static responses only.
- Blade views render presentation only. They must not query, authorize, mutate state, or execute workflows.
- Blade views should stay small, localized, and reusable; repeated markup belongs in shared components or partials.
- Models describe persistence, relationships, casts, and small invariants. They must not coordinate business workflows.
- Prefer custom Eloquent builders or query objects for reusable query constraints; avoid putting business scopes directly on models in new code.
- Business logic belongs in Actions, Services, Domain classes, Policies, Events, Jobs, DTOs, Value Objects, or Support classes.
- Large or repetitive models should be decomposed into concerns, builders, casts, value objects, or support classes instead of growing indefinitely.
- Protected behavior must run explicit authorization checks before sensitive reads, writes, dispatches, exports, or external calls.
- New meaningful behavior must be API-ready by default and reusable without duplicating web or Livewire logic.
- Validation must happen at boundaries and authoritative rules must live in reusable request, DTO, Action, or domain layers.
- Complex multi-model writes must be transactional in the Action or Service that owns the operation.
- Prefer dependency injection over `app()`, `resolve()`, service locators, and static facades inside application logic.
- Use `app()` only at narrow framework boundaries, factories, tests, bootstrapping code, or when dynamic runtime resolution is genuinely required and simpler injection is not practical.

## Allowed Patterns

- `app/Actions` for one business operation with a public `handle()` method.
- `app/Services` for workflow coordination, external integrations, or composition of multiple Actions.
- `app/Data`, `app/DTOs`, or existing local data-object directories for client-agnostic input and output contracts.
- Policies and gates for authorization decisions.
- Events for facts that already happened, listeners for side effects, and jobs for asynchronous or retryable work.
- Eloquent API Resources for API output when returning models or aggregates.
- Form Requests, DTO rule methods, or Action validation helpers for boundary validation.
- Constructor injection for long-lived collaborators.
- Method injection for request-scoped dependencies in controllers, commands, jobs, and invokable handlers.
- Laravel container contextual attributes for config, disks, guards, cache stores, route parameters, tagged services, logs, database connections, and current users.
- Localized strings should use `__('group.key')`, `@lang()`, `trans_choice()`, or similar helpers, and translation files should be organized by domain or feature.
- Before adding custom localization files, run `php artisan lang:publish` and keep application-specific translations in `lang/admin/**` instead of editing Laravel's published core language files unless a core override is truly required.

## Forbidden Patterns

- Business workflows in controllers, Livewire components, routes, Blade views, migrations, factories, seeders, or model event hooks.
- Passing `Request`, `JsonResponse`, `RedirectResponse`, Livewire component instances, or Blade view data into Actions or domain classes.
- Hiding authorization inside Blade conditionals without server-side enforcement.
- Copying logic between web, API, Livewire, jobs, commands, and MCP handlers.
- Dispatching queued jobs before required database commits are durable.
- Adding packages or new base architecture folders without approval.
- Calling `app()` inside Actions, Services, DTOs, Value Objects, Policies, Events, or Jobs when the dependency can be injected.
- Using facades for dependencies that need test doubles or runtime variation when constructor injection is clearer.
- Putting reusable query constraints in model methods instead of builders or query objects in new code.
- Writing inline imports inside Blade, docs snippets, or generated examples when a fully qualified class name keeps the file clearer.

## Naming Conventions

- Actions use imperative names with no suffix, such as `CreateAccount`, `ArchiveRecord`, or `SyncExternalProfile`.
- Services use focused capability names, such as `InvoiceNumberGenerator` or `PaymentGateway`.
- DTOs and Value Objects use nouns that describe contracts or concepts, such as `CreateRecordData` or `Money`.
- Policies use Laravel model policy conventions.
- Events use past-tense facts, such as `RecordApproved`.
- Jobs use imperative names, such as `SendApprovalNotification`.
- Tests describe behavior, not implementation details.
- Model builders should be named after the model, such as `UserBuilder`, and reusable query methods should read like filters.
- Avoid hardcoded user-facing strings in source code when a translation key can express the same value.

## Testing Expectations

- Add or update tests for meaningful behavior changes.
- Prefer feature tests for request, Livewire, authorization, validation, API, and job dispatch behavior.
- Add unit tests for pure Actions, Services, DTOs, Value Objects, and support classes when behavior is isolated.
- Add authorization tests for protected resources and state-changing operations.
- Add failure-path and regression tests for previously broken or risky behavior.
- Use architecture tests where practical to enforce dependency direction and prevent client-layer leakage.
- Add architecture tests when a new layer rule is introduced or a boundary has historically drifted.
- If the feature relies on commit-sensitive side effects, ensure jobs, listeners, events, notifications, or broadcasts are dispatched after commit.

## Review Checklist

- Is the business operation reusable outside the initiating client?
- Are controllers, routes, Blade views, models, and Livewire components thin?
- Are authorization and validation explicit and tested?
- Can the same Action be called from web, API, command, job, MCP, or Livewire?
- Are transactions, events, and jobs placed at the owning boundary?
- Are database constraints, indexes, and casts aligned with the behavior?
- Are errors handled with appropriate exceptions or responses at the boundary?
- Would the feature benefit from a reusable builder, concern, or value object instead of more model code?

## Acceptable Examples

```php
$this->authorize('create', Record::class);

$record = $createRecord->handle(
    user: $request->user(),
    data: CreateRecordData::fromRequest($request),
);

return new RecordResource($record);
```

```php
final readonly class CreateRecord
{
    public function __construct(private RecordRepository $records)
    {
        //
    }

    public function handle(User $actor, CreateRecordData $data): Record
    {
        return DB::transaction(fn (): Record => $this->records->create([
            'user_id' => $actor->id,
            'name' => $data->name,
        ]));
    }
}
```

## Unacceptable Examples

```php
Route::post('/records', function (Request $request) {
    $record = Record::create($request->all());
    Mail::to($record->user)->send(new RecordCreatedMail($record));

    return $record;
});
```

```php
public function save(): void
{
    $record = app(CreateRecord::class)->handle(auth()->user(), $this->form);
    $record->approve();
    Http::post(config('services.example.url'), $record->toArray());
}
```
