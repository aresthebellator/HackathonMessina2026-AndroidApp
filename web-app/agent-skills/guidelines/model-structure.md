# Model Structure Guidelines

## Purpose

Keep Eloquent models small, readable, and focused on persistence concerns rather than application workflows.

## Required Standards

- Models should stay lightweight.
- Put reusable query logic in builders or query objects instead of model scopes in new code.
- Move business workflows out of models and into Actions, Services, Policies, Events, Jobs, DTOs, Value Objects, or Support classes.
- Use concerns, casts, accessors, mutators, and relations to keep model files organized when they grow.
- Split large models before they become difficult to scan.

## Allowed Patterns

- Relations.
- Casts and attribute helpers.
- Small invariants that belong directly to the persistence model.
- Traits or concerns for reusable model behavior.
- Custom builders for query behavior.

## Forbidden Patterns

- Workflow orchestration inside models.
- Large piles of unrelated helper methods.
- Reusable query scopes in new model code.
- Hidden authorization or side effects in model events when a clearer layer exists.

## Naming Conventions

- Concerns should describe the behavior they add.
- Builder methods should read like filters or ordering helpers.

## Testing Expectations

- Test builders, model helpers, and any small invariants that remain in the model.
- Add regression tests when model extraction changes behavior.

## Review Checklist

- Is the model still easy to scan?
- Could any workflow move to a reusable class?
- Would a builder or concern reduce duplication?

## Acceptable Examples

- A model with relations, casts, and a custom builder.
- A concern that groups shared model behavior.

## Unacceptable Examples

- A model that creates records, sends notifications, and dispatches jobs.
- A model that contains many reusable query scopes.
