---
name: create-eloquent-builder
description: "Use when creating or changing an Eloquent builder or query object so reusable query constraints live outside the model."
license: MIT
metadata:
  author: Ali Harb
---

# Create Eloquent Builder

## When To Use

Use this skill when reusable query logic, filtering, ordering, eager loading, search, or model-specific constraints should be centralized away from the model class.

## Inputs Needed

- Model name, query concerns, reusable filters, relationship loading needs, naming, and tests.

## Workflow

1. Inspect the model and nearby query patterns.
2. Create a custom builder or query object when reusable query logic is needed.
3. Keep the model focused on persistence, relations, casts, and small invariants.
4. Move repeated query constraints out of controllers, Livewire, routes, and model methods.
5. Name query methods like filters or capabilities, not like one-off implementation details.
6. Use `#[UseEloquentBuilder]`, `#[UsePolicy]`, `#[UseResource]`, or related Laravel attributes when they make the model contract explicit.
7. Add tests for the query behavior.

## Files That May Be Created

- `app/Builders/*`
- `app/Queries/*`
- `app/Models/*` model attribute updates
- Tests for query behavior.

## Files That May Be Modified

- Models, Actions, controllers, Livewire components, Resources, policies, and tests.

## Architecture Rules

- Models should not contain reusable business scopes in new code.
- Query logic should be reusable and explicit.
- Builder methods should be typed and composable.
- Avoid `app()` in query helpers; inject collaborators if needed.

## Testing Requirements

- Test filter behavior, ordering, eager loading, edge cases, and regression coverage for moved query logic.

## Security Requirements

- Avoid exposing private data through broad query defaults.
- Keep authorization out of the builder; builders filter data, policies authorize access.

## Review Checklist

- Does the model stay slim?
- Is the query logic reusable and named clearly?
- Are scopes or filters centralized in the builder or query object?
- Are tests proving the same behavior after the move?

## Common Mistakes

- Leaving reusable query logic in the model.
- Calling the builder from presentation layers and duplicating filters elsewhere.
- Using a builder to hold business workflows instead of query behavior.
