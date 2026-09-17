---
name: create-policy
description: "Use when creating or changing Laravel policies, gates, or authorization checks for protected resources and operations."
license: MIT
metadata:
  author: Ali Harb
---

# Create Policy

## When To Use

Use this skill for protected resources, sensitive reads, state changes, exports, impersonation, billing, admin behavior, and external side effects.

## Inputs Needed

- Resource, actor, operation, ownership rules, roles or permissions, edge cases, and denial behavior.

## Workflow

1. Inspect existing policies and authorization conventions.
2. Generate or update the policy with Laravel conventions.
3. Add methods for each protected operation.
4. Use `#[\Override]` for policy methods that override a parent policy method.
5. Use Laravel 13 controller `#[Authorize]` or Livewire `#[Authorize]` attributes at client boundaries when they make authorization more visible.
6. Call policies before state-changing operations and sensitive reads.
7. Keep authorization out of Blade-only visibility checks.
8. Add denial and allow tests.

## Files That May Be Created

- `app/Policies/*Policy.php`
- Authorization tests.

## Files That May Be Modified

- Models, controllers, Livewire components, Actions, providers, routes, and tests.

## Architecture Rules

- Every protected resource needs authorization.
- Policies decide access; Actions perform operations.
- Views may hide controls, but server-side checks must enforce access.
- Attribute-based authorization is allowed at controllers and Livewire boundaries, but Actions still need explicit protection when reused directly.

## Testing Requirements

- Test allowed actors, denied actors, unauthenticated users where relevant, and boundary callers.

## Security Requirements

- Default to deny when uncertain.
- Avoid relying on client-submitted ownership or role values.

## Review Checklist

- Is every protected operation covered?
- Are checks made before mutations?
- Are denial paths tested?

## Common Mistakes

- Authorizing only in Blade.
- Checking authorization after mutation.
- Duplicating policy rules in controllers or Livewire components.
