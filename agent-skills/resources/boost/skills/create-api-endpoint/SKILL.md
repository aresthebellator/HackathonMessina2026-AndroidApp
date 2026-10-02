---
name: create-api-endpoint
description: "Use when creating or changing Laravel API routes, controllers, requests, resources, or endpoint behavior."
license: MIT
metadata:
  author: Ali Harb
---

# Create API Endpoint

## When To Use

Use this skill for HTTP API behavior, mobile or third-party client support, JSON resources, and public or private API contracts.

## Inputs Needed

- Version, route, method, authentication, authorization, request fields, response shape, Action, resource, errors, and tests.

## Workflow

1. Inspect existing API route conventions.
2. Use versioned API routes unless the project has an established alternative.
3. Validate input with a Form Request or reusable rules.
4. Convert input to a DTO when behavior is meaningful.
5. Authorize with a policy, gate, or Laravel 13 `#[Authorize]` controller attribute.
6. Use Laravel 13 `#[Middleware]` controller attributes when they make endpoint protection clearer than route-level middleware.
7. Call an Action for business behavior.
8. Return a Resource or consistent JSON shape.
9. Prefer enums, DTOs, typed constants, and `json_validate()` where they improve API contracts.
10. Add API tests for success, validation, authorization, and error paths.

## Files That May Be Created

- API route file entries.
- Controllers, Form Requests, Resources, DTOs, Actions, Policies, and tests.

## Files That May Be Modified

- Routes, policies, providers, Actions, Resources, and tests.

## Architecture Rules

- No duplicated logic from Livewire or web layers.
- No business logic in routes or controllers.
- API output must not accidentally expose private model fields.
- Attribute metadata may protect or describe the endpoint, but business behavior remains in reusable classes.

## Testing Requirements

- Test status codes, JSON shape, validation, authorization, authentication, and side effects.

## Security Requirements

- Use authentication, authorization, throttling, safe validation, and escaped or serialized output.

## Review Checklist

- Is the route versioned?
- Is the response contract stable?
- Is the Action reusable?
- Are failures safe and tested?

## Common Mistakes

- Returning raw Eloquent models.
- Creating API-only business logic.
- Accepting `$request->all()` into models.
