---
name: filament-development
description: "Use when creating or changing Filament v5 panels, resources, relation managers, pages, widgets, forms, tables, infolists, actions, notifications, query-builder filters, or Filament-related extensions."
license: MIT
metadata:
  author: Ali Harb
---

# Filament Development

## When To Use

Use this skill for Filament v5 admin panels, CRUD resources, dashboard widgets, custom pages, relation managers, forms, tables, infolists, query-builder UIs, imports/exports, and plugin-driven admin UX.

## Inputs Needed

- Panel goal, models, permissions, reusable actions, DTOs, query behavior, localization, UI flow, and tests.

## Workflow

1. Inspect existing Filament conventions, panel providers, resources, and stubs.
2. Decide whether the feature belongs in a resource, page, widget, relation manager, or custom Livewire component.
3. Keep all business logic in Actions, Services, DTOs, Policies, Events, Jobs, or builders.
4. Use Filament’s declarative APIs for forms, tables, infolists, actions, and navigation.
5. Localize labels, descriptions, placeholders, tooltips, empty states, and notifications.
6. Use custom Eloquent builders or query objects for reusable filtering and ordering.
7. Authorize every protected record, action, or page.
8. Use Spatie Data for payloads, defaults, validation, and serialization when the project is already using it.
9. Add tests for admin behaviors and regressions.

## Files That May Be Created

- `app/Filament/Resources/*`
- `app/Filament/Resources/*/Pages/*`
- `app/Filament/Resources/*/RelationManagers/*`
- `app/Filament/Pages/*`
- `app/Filament/Widgets/*`
- `app/Providers/Filament/*PanelProvider.php`
- `app/Forms/*`, `app/Tables/*`, `app/Infolists/*`, `app/Actions/*`, `app/Data/*`, `app/DTOs/*`, and tests

## Files That May Be Modified

- Filament resources, pages, widgets, relation managers, builders, policies, Actions, DTOs, data objects, localization files, and tests.

## Architecture Rules

- Filament orchestrates application code; it does not own business workflows.
- Reusable query logic belongs in builders or query objects, not model scopes.
- Use policies or authorization callbacks for protected behavior.
- Prefer dependency injection over `app()` inside Filament support code.
- Use attribute-based Laravel metadata when it improves panel, model, or query clarity.

## Testing Requirements

- Test resource and page authorization, validation, action execution, filters, sorting, persistence, and deletion flows.
- Test custom table data sources, relation managers, and query-builder behavior.
- Test localization keys where user-visible copy is part of the behavior.

## Security Requirements

- Protect records and actions with policies and explicit checks.
- Never trust Filament form state without validation.
- Avoid leaking private fields into tables, infolists, exports, or notifications.

## Review Checklist

- Is the panel layer thin?
- Are reusable actions and builders used?
- Is all visible text localized?
- Are protected behaviors authorized?
- Are premium/plugin features only used if the package exists?
- Are imports/exports and notifications safe for retries and data exposure?

## Common Mistakes

- Putting business rules in resource closures.
- Duplicating the same validation in forms, pages, and actions.
- Using model scopes instead of builders for new reusable queries.
- Hardcoding labels or confirmation text.
- Depending on premium or plugin APIs that are not installed.
