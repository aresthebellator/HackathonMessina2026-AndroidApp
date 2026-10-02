# AI Development Guidelines

## Purpose

Guide AI and code assistants toward small, Laravel-native, maintainable changes that preserve architecture boundaries.

## Required Standards

- Inspect existing code, tests, routes, models, components, and sibling files before generating new code.
- Follow existing conventions unless they directly conflict with these guidelines or Laravel best practices.
- Use Laravel Boost `search-docs` before Laravel ecosystem code changes.
- Do not introduce packages, base folders, services, queues, or infrastructure without approval.
- Do not place business logic in controllers, Livewire components, routes, Blade views, or models.
- Prefer small, focused changes that solve the requested problem.
- Add or update tests for behavior changes.
- Explain meaningful architectural trade-offs when there is more than one reasonable path.
- Avoid speculative abstractions and unrelated feature work.
- Keep Actions, Services, DTOs, jobs, and domain code client-agnostic where possible.
- Prefer artisan generators and framework commands over hand-created files when a command exists.
- Keep files slim; split fat models, providers, components, or support classes into concerns, builders, actions, DTOs, or helpers.
- Keep architecture tests updated whenever a rule changes so the standards stay enforceable.
- Prefer shared components, helpers, builders, casts, and data objects over duplicated code.
- If a package or framework feature is version-sensitive, audit the installed version before using or documenting it.
- For Blade and frontend work, prefer shared components, localized strings, Tailwind 4 utilities, oklch theme colors, RTL support, and accessible markup over one-off heavy templates.

## Allowed Patterns

- Read first, then edit.
- Use Artisan generators where Laravel conventions expect them.
- Use Actions for reusable operations and Services only when coordination or external integration warrants them.
- Add Pest feature, unit, API, authorization, or architecture tests based on risk.
- Run Pint after PHP changes and the smallest relevant test set.

## Forbidden Patterns

- Creating application features beyond the request.
- Rewriting broad areas to satisfy a narrow task.
- Guessing framework APIs when Boost documentation can be searched.
- Installing dependencies without explicit approval.
- Creating documentation files unless requested.
- Moving user code or reverting user changes without permission.

## Naming Conventions

- Use descriptive class, method, variable, and test names.
- Match Laravel and existing project conventions for namespaces and directories.
- Prefer operation names that describe intent over implementation.

## Testing Expectations

- Behavior changes require tests.
- Refactors require regression coverage around preserved behavior.
- Security and authorization changes require denial-path tests.
- If tests are not run, state why and what should be run next.

## Review Checklist

- Did the assistant inspect the current project structure?
- Are changes scoped to the request?
- Are architecture boundaries preserved?
- Are tests and formatting run?
- Are assumptions called out clearly?
- Is the response concise and focused on outcomes?

## Acceptable Examples

```php
final readonly class UpdateRecordStatus
{
    public function handle(User $actor, Record $record, RecordStatus $status): Record
    {
        Gate::forUser($actor)->authorize('update', $record);

        $record->forceFill(['status' => $status])->save();

        return $record;
    }
}
```

## Unacceptable Examples

```php
public function update(Request $request, Record $record)
{
    $record->status = $request->status;
    $record->save();
    dispatch(new SyncRecord($record));

    return back();
}
```
