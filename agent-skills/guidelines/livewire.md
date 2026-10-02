# Livewire Guidelines

## Purpose

Keep Livewire components focused on UI state, interaction, and authorization while reusable logic lives elsewhere.

## Required Standards

- Livewire components should manage state and call Actions.
- Keep authoritative validation and business rules in reusable layers.
- Treat public properties as user input.
- Authorize explicit state-changing behavior.
- Use modern Livewire 4 features only when they fit the installed version and the current component style.
- Prefer single-file components for most new UI, multi-file components for large or JS-heavy components, and class-based components for migrations or teams that already standardize on the traditional split.
- Use the project-wide default Livewire component format consistently, and override it only when the component needs a different format.
- Prefer deferred, lazy, async, or island-style features where they improve UX and performance.
- Avoid inline business logic or request handling inside component methods.
- Keep component views small, localized, accessible, and reusable; prefer shared UI pieces over repeated markup.

## Allowed Patterns

- Component state, computed properties, event hooks, validation, and UI-only behavior.
- Attribute-driven Livewire features when they clarify the component.
- Thin calls to Actions and Services.

## Forbidden Patterns

- Model writes directly from large component methods.
- Business workflows in component methods.
- Trusting public state without validation or authorization.

## Naming Conventions

- Components should describe the page or interaction they own.

## Testing Expectations

- Test rendering, state transitions, validation, authorization, and action effects.

## Review Checklist

- Is the component thin?
- Are actions extracted?
- Are public properties treated as input?

## Acceptable Examples

- A component that validates input, authorizes, and calls an Action.
- A computed property that loads a small slice of data.

## Unacceptable Examples

- A component that runs the entire business workflow in `save()`.
