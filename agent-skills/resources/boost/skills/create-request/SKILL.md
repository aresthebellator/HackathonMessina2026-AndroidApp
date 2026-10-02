---
name: create-request
description: "Use when creating Laravel Form Request classes for validation and authorization."
license: MIT
metadata:
  author: Ali Harb
---

# Create Request

## When To Use

Use this skill for request validation, authorization, input normalization, and reusable HTTP boundary rules.

## Inputs Needed

- Request purpose, actor, validated fields, authorization rule, failure behavior, and tests.

## Workflow

1. Inspect nearby request classes and naming conventions.
2. Create the request with Artisan.
3. Put authorization in `authorize()`.
4. Put validation in `rules()` and custom messages in `messages()`.
5. Use `prepareForValidation()` only for input normalization.
6. Prefer reusable DTOs or Actions after validation when the same data is needed elsewhere.
7. Add tests for authorize allow/deny and validation failures.

## Files That May Be Created

- `app/Http/Requests/*`
- Request tests

## Files That May Be Modified

- Controllers, Livewire components, Actions, DTOs, and tests

## Architecture Rules

- Requests own HTTP boundary validation and authorization only.
- Requests should not contain business workflows.
- Keep request messages localized.

## Testing Requirements

- Test authorization, required fields, invalid data, and any normalization behavior.

## Review Checklist

- Is the request thin and reusable?
- Are authorization and validation both covered?
- Are messages localized?

## Common Mistakes to Avoid

- Putting business logic in `authorize()`.
- Duplicating the same rules in controllers and components.
- Skipping tests because the request looks simple.
