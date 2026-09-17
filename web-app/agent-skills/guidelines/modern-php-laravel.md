# Modern PHP And Laravel 13 Guidelines

## Purpose

Prefer modern PHP 8.3+ and Laravel 13 features when they improve correctness, type safety, readability, reuse, security, or performance.

## Required Standards

- Use native PHP types before PHPDoc. Keep PHPDoc for generics, array shapes, templates, and non-obvious contracts.
- Use constructor property promotion for dependencies and simple value objects.
- Use property hooks (PHP 8.4+) for computed or validation-guarded properties when a property model is clearer than accessors/mutators.
- Use asymmetric visibility (PHP 8.4+) (e.g. `public private(set) string $name`) to expose properties as readable but not writable externally, avoiding getter boilerplate.
- Use `readonly` classes or properties for immutable DTOs, value objects, and dependency-only services.
- Use `new MyClass()->method()` syntax (PHP 8.4+) directly without surrounding parentheses when calling methods on instantiated objects.
- Use native array helpers (PHP 8.4+) (`array_find()`, `array_any()`, `array_all()`) instead of verbose foreach loops or closures.
- Use the `#[\Deprecated]` attribute (PHP 8.4+) to signal deprecated code paths to static analyzers and framework logs.
- Use `final` for application classes by default unless inheritance/extension is explicitly required.
- Use enums for closed sets of values instead of stringly typed status, type, mode, or state fields.
- Use typed class constants for PHP 8.3+ constants.
- Use `#[\Override]` whenever overriding a parent or interface method in PHP 8.3+.
- Use `match` for exhaustive value mapping and branching.
- Use null-safe access, named arguments, first-class callables, and spread syntax where appropriate.
- Use `json_validate()` when only JSON validity is needed before decoding.
- Use the PHP 8.5 pipe operator `|>` for readable left-to-right transformation chains when the right side is a single-argument callable.
- Use `#[\NoDiscard]` (PHP 8.5+) to enforce that return values of critical calculations or actions are not ignored.
- Use PHP 8.5 closure and callable support in constant expressions to define dynamic constants or attribute parameters.
- Use `Random\Randomizer` for testable or advanced random behavior; use Laravel helpers or `Str` where the framework already provides the right abstraction.
- Use Laravel 13 attributes where they express metadata cleanly and do not hide business behavior.
- Prefer attributes for declarative framework metadata; keep business decisions in Actions, Policies, Services, DTOs, Events, Jobs, Value Objects, or Support classes.
- Prefer dependency injection over `app()`, `resolve()`, and service-locator access.
- Prefer Laravel helpers and fluent objects: `Uri`, `Number`, `Str::of()`, `Arr`, `Collection`, `once()`, `defer()`, `Context`, `Concurrency`, `Cache::flexible()`, and HTTP client fakes.
- Prefer types and structure that keep PHPStan happy without suppressions.

## Laravel 13 Attributes To Prefer

- Controller authorization: `Illuminate\Routing\Attributes\Controllers\Authorize`.
- Controller middleware: `Illuminate\Routing\Attributes\Controllers\Middleware`.
- Container contextual injection: `Illuminate\Container\Attributes\Auth`, `Authenticated`, `Cache`, `Config`, `Context`, `CurrentUser`, `DB`, `Database`, `Give`, `Log`, `RouteParameter`, `Storage`, and `Tag`.
- Container binding and lifecycle: `Illuminate\Container\Attributes\Bind`, `Scoped`, and `Singleton`.
- Eloquent metadata: `Illuminate\Database\Eloquent\Attributes\Appends`, `CollectedBy`, `Connection`, `DateFormat`, `Fillable`, `Guarded`, `Hidden`, `ObservedBy`, `ScopedBy`, `Table`, `Touches`, `Unguarded`, `UseEloquentBuilder`, `UseFactory`, `UsePolicy`, `UseResource`, `UseResourceCollection`, `Visible`, `WithoutIncrementing`, and `WithoutTimestamps`.
- Eloquent casting and lifecycle helpers: `AsFluent`, `AsStringable`, `AsUri`, `AsArrayObject`, `AsCollection`, `AsEncryptedArrayObject`, `AsEncryptedCollection`, `AsEncryptedObject`, `AsEnumCollection`, `AsEnumArrayObject`, `AsStringable`, and related cast classes.
- Eloquent methods: `Illuminate\Database\Eloquent\Attributes\Scope`, `Boot`, and `Initialize` when they make model behavior more explicit.
- Console, request, and model metadata should be expressed with attributes whenever they are stable and declarative.
- Factory metadata: `Illuminate\Database\Eloquent\Factories\Attributes\UseModel`.
- Form Request metadata: `Illuminate\Foundation\Http\Attributes\ErrorBag`, `FailOnUnknownFields`, `RedirectTo`, `RedirectToRoute`, and `StopOnFirstFailure`.
- Console command metadata: `Illuminate\Console\Attributes\Aliases`, `Description`, `Help`, `Hidden`, `Signature`, and `Usage`.
- Queue metadata: `Illuminate\Queue\Attributes\Backoff`, `Connection`, `DebounceFor`, `Delay`, `DeleteWhenMissingModels`, `FailOnTimeout`, `MaxExceptions`, `Queue`, `Timeout`, `Tries`, `UniqueFor`, and `WithoutRelations`.
- API Resource metadata: `Illuminate\Http\Resources\Attributes\Collects` and `PreserveKeys`.
- Test metadata: `Illuminate\Foundation\Testing\Attributes\Seed`, `Seeder`, `SetUp`, `TearDown`, and `UnitTest` where Pest style does not already express the behavior better.
- Livewire authorization: `Livewire\Attributes\Authorize`.
- Livewire validation: `Livewire\Attributes\Validate` for simple UX rules; use `rules()` for dynamic rules or `Rule` objects.
- Livewire state and rendering: `Livewire\Attributes\Computed`, `Json`, `Layout`, `Locked`, `Modelable`, `Reactive`, `Session`, `Title`, `Transition`, and `Url`.
- Livewire events and JavaScript: `Livewire\Attributes\Js` and `On`.
- Livewire execution and loading: `Livewire\Attributes\Async`, `Defer`, `Isolate`, `Lazy`, and `Renderless` when they fit the interaction.
- Livewire validation compatibility: `Livewire\Attributes\Rule` may appear in older patterns, but prefer `Validate` or `rules()` for new Livewire 4 code.

## Localization And Strings

- Use Laravel translation helpers for all user-facing strings that need to be reused, translated, or organized by domain.
- Prefer `__('group.key')`, `@lang('group.key')`, or `trans_choice()` over hardcoded English text in reusable code.
- Organize translation keys by feature or domain, such as `__('users.profile.title')` or `__('billing.invoice.status.paid')`.
- Before writing app translations, run `php artisan lang:publish` once and keep app-specific language files under `lang/admin/**` or another app-owned language tree.
- For multi-file or large feature sets, split translation files into focused directories or grouped language files so keys stay discoverable.
- Keep dynamic labels, validation messages, mail copy, and notification text in localization files when the same string appears in more than one place.

## Laravel 13 Features To Prefer

- Prefer custom Eloquent builders or query objects for reusable query constraints instead of model scopes in new code.
- Use `withAttributes()` on builder methods or query helpers when the same attributes should constrain queries and seed created models.
- Use `casts()` methods and modern cast classes such as `AsFluent`, `AsStringable`, and `AsUri` where appropriate.
- Use `#[UsePolicy]` or Laravel policy discovery conventions to keep authorization discoverable.
- Use `#[UseResource]` and `#[UseResourceCollection]` when model-to-resource mapping should be explicit and reusable.
- Use queue attributes for simple job metadata, while keeping complex retry or middleware logic in job methods.
- Use Form Request attributes to express redirect, error bag, unknown-field, and stop-on-first-failure behavior.
- Use API Resource attributes for collection behavior and key preservation.
- Use `ShouldHandleEventsAfterCommit`, `ShouldDispatchAfterCommit`, `ShouldQueueAfterCommit`, or `afterCommit()` / `beforeCommit()` when side effects depend on committed data.
- Use named arguments for Laravel helpers where they clarify meaning, such as date intervals, cache options, and framework APIs with multiple booleans.
- Use `defer()` for post-response work that should not be a durable queued job.
- Use `Context` for request-scoped metadata that must flow through logs, jobs, or instrumentation.
- Use `Concurrency::run()` for independent parallel work when the environment supports it and errors are handled.
- Use `Cache::flexible()` for stale-while-revalidate caching on read-heavy data.
- Use `once()` for per-request memoization of deterministic expensive calculations.
- Use Laravel's HTTP client with explicit timeouts, retries, `throw()`, pools for independent requests, and fakes in tests.
- Use model `is()` and `isNot()` helpers for identity comparisons.
- Use `whereBelongsTo`, `withCount`, `withExists`, `withAggregate`, `addSelect` subqueries, and cursor/lazy iteration for efficient Eloquent code.
- Use `Route::resource`, `Route::apiResource`, implicit route model binding, scoped bindings, and named routes for stable APIs.
- Use Blade `@use` for imports in templates when it keeps templates cleaner without adding logic.
- Prefer fully qualified class names in Blade, docs examples, and generated snippets instead of inline imports.
- Use Livewire 4 islands, deferred/lazy bundled loading, async/renderless actions, `wire:sort`, `wire:intersect`, `wire:ref`, `data-loading`, and `.preserve-scroll` when they improve UX or performance.
- Use `#[\NoDiscard]` when a return value is important enough that ignoring it should be treated as a mistake.
- Use PHP 8.5 closure and callable support in constant expressions when it makes attribute parameters, defaults, or constants cleaner.
- Use `dispatch()->afterCommit()` and the after-commit interfaces when jobs, events, listeners, broadcasts, or notifications depend on committed database state.

## Custom PHP Attributes

- Create custom attributes only for stable metadata that can be read by framework code, tests, scanners, middleware, or support tooling.
- Attribute classes must be small, immutable, typed, and placed in `app/Attributes` unless the project has an existing convention.
- Attribute names should describe metadata, not commands. Prefer `Auditable`, `Sensitive`, `ExternalIntegration`, or `RequiresFeature` over `RunAudit` or `DoSync`.
- Custom attributes must not contain business workflows, database writes, HTTP calls, or authorization decisions.
- Every custom attribute needs at least one consumer or architecture/test use at creation time.

## Allowed Patterns

```php
final readonly class CreateRecordData
{
    public function __construct(
        public string $name,
        public RecordType $type,
    ) {}
}
```

```php
final class RecordPolicy extends Policy
{
    #[\Override]
    public function before(User $user, string $ability): ?bool
    {
        return $user->is_admin ? true : null;
    }
}
```

```php
use Illuminate\Routing\Attributes\Controllers\Authorize;
use Illuminate\Routing\Attributes\Controllers\Middleware;

#[Middleware('auth')]
final class RecordController
{
    #[Authorize('create', Record::class)]
    public function store(StoreRecordRequest $request): RecordResource
    {
        // Orchestrate only.
    }
}
```

```php
use Illuminate\Container\Attributes\Config;

final readonly class ExternalClient
{
    public function __construct(
        #[Config('services.external.timeout')] private int $timeout,
    ) {}
}
```

```php
use Illuminate\Container\Attributes\Storage;
use Illuminate\Contracts\Filesystem\Filesystem;

final readonly class StoreUploadedFile
{
    public function __construct(
        #[Storage('private')] private Filesystem $files,
    ) {}
}
```

```php
use Illuminate\Database\Eloquent\Attributes\Scope;
use Illuminate\Database\Eloquent\Attributes\UsePolicy;
use Illuminate\Database\Eloquent\Builder;

#[UsePolicy(RecordPolicy::class)]
final class Record extends Model
{
    #[Scope]
    protected function active(Builder $query): void
    {
        $query->where('active', true);
    }
}
```

```php
use Illuminate\Queue\Attributes\Backoff;
use Illuminate\Queue\Attributes\Queue;
use Illuminate\Queue\Attributes\Tries;

#[Queue('integrations')]
#[Tries(3)]
#[Backoff([10, 60, 300])]
final class SyncExternalRecord implements ShouldQueue
{
    //
}
```

```php
#[Attribute(Attribute::TARGET_CLASS | Attribute::TARGET_METHOD)]
final readonly class Auditable
{
    public function __construct(public string $event)
    {
        //
    }
}
```

```php
// PHP 8.4 Property Hooks & Asymmetric Visibility
final class Account
{
    public private(set) string $routingNumber;
    
    public string $accountNumber {
        get => $this->maskNumber($this->rawNumber);
        set (string $value) {
            if (strlen($value) < 8) {
                throw new InvalidArgumentException('Too short');
            }
            $this->rawNumber = $value;
        }
    }

    private string $rawNumber = '';

    public function __construct(string $routingNumber)
    {
        $this->routingNumber = $routingNumber;
    }

    private function maskNumber(string $number): string
    {
        return str_repeat('*', strlen($number) - 4) . substr($number, -4);
    }
}
```

```php
// PHP 8.4 Array Helpers, Deprecations & New without Parentheses
final class UserService
{
    #[\Deprecated('Use AccountService instead', '2.0.0')]
    public function findActiveUser(array $users): ?User
    {
        // PHP 8.4 array_find helper
        return array_find($users, fn(User $u) => $u->isActive);
    }

    public function createProfile(): string
    {
        // PHP 8.4 new without parentheses
        return new ProfileGenerator()->generateDefaultName();
    }
}
```

```php
// PHP 8.5 Pipe Operator & No-Discard Attribute
final readonly class StringSanitizer
{
    #[\NoDiscard]
    public function sanitize(string $input): string
    {
        return $input
            |> 'trim'
            |> 'strip_tags'
            |> fn($s) => htmlspecialchars($s, ENT_QUOTES, 'UTF-8');
    }
}
```

## Forbidden Patterns

- Using attributes to hide business logic that belongs in Actions or Policies.
- Creating custom attributes without a consumer.
- Using attributes for values that must be dynamic at runtime.
- Replacing clear code with clever attributes that future maintainers cannot discover.
- Using `#[Validate]` for complex dynamic rules that require Laravel `Rule` objects.
- Using `#[Authorize]` as the only UI visibility mechanism; pair it with `@can` where controls should be hidden.
- Avoiding DTOs, enums, typed constants, or value objects in favor of unstructured arrays.
- Calling `app()` from application logic when constructor or method injection can express the dependency.
- Using attributes as a blanket replacement for configuration, policies, middleware, or explicit code where those are clearer.

## Naming Conventions

- Attributes are adjectives or metadata nouns: `Auditable`, `Sensitive`, `RequiresFeature`, `ExternalIntegration`.
- Enums are singular nouns: `RecordStatus`, `DeliveryMethod`.
- DTOs end in `Data` only when that convention is already used or improves clarity.
- Typed constants use explicit visibility and type: `public const string ApiVersion = 'v1';`.
- Attribute constructor parameters use named arguments in call sites when more than one scalar is passed.

## Testing Expectations

- Test custom attribute consumers, not just attribute construction.
- Add architecture tests for dependency direction and forbidden framework dependencies.
- Add feature tests for controller and Livewire attributes that enforce authorization or middleware.
- Add static-analysis-aware refactors when weak typing would otherwise spread through the codebase.
- Add unit tests for enums, DTOs, value objects, and match mappings when they encode meaningful behavior.

## Review Checklist

- Are PHP 8.3+ and Laravel 13 features used where they improve clarity?
- Are attributes declarative rather than procedural?
- Are dependencies injected instead of resolved with `app()`?
- Are all overrides marked with `#[\Override]`?
- Are DTOs and value objects immutable where possible?
- Are closed sets modeled with enums?
- Are constants typed?
- Is JSON validated efficiently when decoding is not needed?
- Does every custom attribute have a consumer and tests?

## Unacceptable Examples

```php
#[SyncToExternalService]
public function save(): void
{
    // Attribute hides a business side effect.
}
```

```php
final class Statuses
{
    public const Active = 'active';
    public const Inactive = 'inactive';
}
```

```php
public function handle(array $data): array
{
    return match ($data['type']) {
        'a' => ['status' => 'ok'],
        default => ['status' => 'unknown'],
    };
}
```
