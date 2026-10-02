---
name: create-livewire-component
description: "Use when creating or changing Livewire pages or components in a Laravel application."
license: MIT
metadata:
  author: Ali Harb
---

# Create Livewire Page Or Component

## When To Use

Use this skill for Livewire UI state, forms, interactions, pages, and component behavior.

## Inputs Needed

- Component format, state, actions, validation, authorization, view, route, and tests.

## Workflow

1. Inspect existing Livewire component format and config.
2. Create the component with Artisan using the existing convention.
3. Keep UI state and interaction in Livewire.
4. Put business rules in Actions, DTOs, Policies, Services, Events, or Jobs.
5. Prefer Livewire 4 attributes when they clarify component behavior: `#[Authorize]`, `#[Validate]`, `#[Url]`, `#[Session]`, `#[Locked]`, `#[Computed]`, `#[Async]`, `#[Renderless]`, `#[Lazy]`, and `#[Defer]`.
6. Choose the component format deliberately:
   - `sfc` for most new components and small pages
   - `mfc` for large components, significant JavaScript or CSS, or code that benefits from colocation
   - `class` for migration work or when the project intentionally keeps the classic split
7. Use the project-wide default format unless the component’s complexity justifies a different one.
8. Use UX validation in Livewire when helpful.
9. Keep authoritative validation in reusable layers.
10. Authorize explicitly before protected behavior.
11. Use `rules()` instead of `#[Validate]` for dynamic rules or Laravel `Rule` objects.
12. Use schema-based Filament APIs or Livewire 4 conventions that match the installed version; avoid older `Form $form` style examples when the project expects newer signatures.
13. Add Livewire tests and Action tests where behavior belongs.

## Files That May Be Created

- Livewire component files, Blade views, Actions, DTOs, policies, and tests.

## Files That May Be Modified

- Routes, views, Actions, policies, tests, and navigation.

## Architecture Rules

- Livewire manages UI state only.
- Livewire calls Actions for business operations.
- No request/response or API-specific assumptions inside Actions called by Livewire.
- Attributes are for declarative UI behavior, authorization, validation, and state persistence; they do not replace Actions.
- Keep blade views free of inline `@php` imports unless the component has no accompanying PHP class and the framework requires it.
- Use `php artisan make:livewire` with `--sfc`, `--mfc`, or `--class` intentionally rather than relying on accidental defaults.

## Testing Requirements

- Test component rendering, state changes, validation errors, authorization denial, Action effects, and events.

## Security Requirements

- Treat Livewire public properties as user input.
- Authorize server-side; do not rely on hidden buttons.

## Review Checklist

- Is business logic outside the component?
- Are public properties validated?
- Are loops keyed and loading states considered?
- Is the component format chosen intentionally for its size and complexity?

## Common Mistakes

- Saving models directly from complex Livewire methods.
- Treating Livewire validation as the only authoritative validation.
- Forgetting policy checks in component actions.
