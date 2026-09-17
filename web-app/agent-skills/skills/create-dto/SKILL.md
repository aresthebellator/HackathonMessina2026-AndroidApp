---
name: create-dto
description: "Use when creating or changing DTOs, data objects, value objects, or input/output contracts for Laravel features."
license: MIT
metadata:
  author: Ali Harb
---

# Create DTO Or Data Object

## When To Use

Use this skill when data crosses a boundary or must be reused by web, API, Livewire, jobs, commands, or integrations.

## Inputs Needed

- Fields, types, validation rules, source boundaries, output consumers, defaults, and serialization needs.

## Workflow

1. Inspect existing data object conventions.
2. Use Spatie Laravel Data by default now that it is installed. If a different data style already exists in a nearby module, follow the local convention.
3. Prefer `final readonly class` with constructor property promotion for immutable DTOs.
4. Use Spatie Data attributes where they fit the property:
   - `#[MapInputName]`
   - `#[MapOutputName]`
   - `#[WithCast]`
   - `#[WithTransformer]`
   - `#[Computed]`
   - `#[ValidationAttribute]` or package validation attributes
   - `#[DataCollectionOf]`
   - `#[Lazy]`
   - `#[Hidden]`
   - `#[LoadRelation]` and `#[LoadMissing]`
   - `#[Inject]` or property injection attributes when the package or project convention uses them
5. Use enums for closed sets and typed class constants for reusable scalar contracts.
6. Keep DTOs client-agnostic.
7. Add named constructors such as `fromArray`, `fromRequest`, or `fromModel` only when useful.
8. Place validation rules near DTOs when the project convention supports it.
9. Avoid persistence, authorization, or workflow logic.
10. Add tests for construction, validation rules, serialization, and edge cases.

## Files That May Be Created

- `app/Data/*.php`, `app/DTOs/*.php`, or the existing local equivalent.
- DTO tests.

## Files That May Be Modified

- Form Requests, Actions, Resources, Livewire components, controllers, jobs, and tests.

## Architecture Rules

- DTOs represent input or output contracts.
- DTOs support API and UI reuse.
- DTOs must not depend on Livewire, Blade, controllers, or responses.
- DTOs should use modern PHP typing instead of unstructured arrays whenever practical.
- Use Spatie Data collections/resources when a feature needs nested, transformed, or reusable data shapes.
- Prefer Spatie Data validation attributes before ad hoc array validation inside reusable code.

## Testing Requirements

- Test required fields, type conversion, defaults, invalid data, and serialization if exposed.

## Security Requirements

- Do not trust DTO construction as authorization.
- Avoid including sensitive fields in output DTOs unless explicitly intended.

## Review Checklist

- Is the DTO stable and client-agnostic?
- Are fields typed?
- Are validation rules close enough to stay synchronized?

## Examples

### Good Example
```php
namespace App\Data;

use Spatie\LaravelData\Data;
use Spatie\LaravelData\Attributes\Validation\Email;
use Spatie\LaravelData\Attributes\Validation\Required;
use Spatie\LaravelData\Attributes\Validation\Max;

final readonly class RegisterUserData extends Data
{
    public function __construct(
        #[Required, Max(255)]
        public string $name,

        #[Required, Email]
        public string $email,
    ) {}
}
```

### Bad Example
```php
// ❌ Poor type safety, no validation context, difficult to reuse in background jobs.
class RegisterController extends Controller
{
    public function store(Request $request)
    {
        $this->action->handle($request->all());
    }
}
```
