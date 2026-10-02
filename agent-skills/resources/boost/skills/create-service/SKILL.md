---
name: create-service
description: "Use when creating or changing a Laravel service class that coordinates workflows, external integrations, or multiple Actions without becoming a dumping ground."
license: MIT
metadata:
  author: Ali Harb
---

# Create Service

## When To Use

Use this skill when behavior coordinates multiple operations or integrates with an external system. Prefer an Action for a single operation.

## Inputs Needed

- Responsibility, collaborators, external systems, failure behavior, transaction boundaries, and tests.

## Workflow

1. Inspect existing services and support classes.
2. Define one clear responsibility.
3. Compose Actions, support classes, clients, repositories, or external integrations.
4. Keep controllers, Livewire components, requests, and responses out of the service API.
5. Add typed methods and return values.
6. Use constructor property promotion and `final readonly` when the service has dependencies only.
7. Use Laravel container contextual attributes such as `#[Config]`, `#[Storage]`, `#[Cache]`, `#[DB]`, `#[Log]`, `#[Give]`, and `#[Tag]` when they make dependency injection clearer.
8. Use `#[Scoped]` for services that should resolve once per request or job lifecycle.
9. Keep state minimal and dependencies constructor-injected.
10. Add tests for coordination and failure behavior.

## Files That May Be Created

- `app/Services/<Capability>.php`
- Tests for service behavior.

## Files That May Be Modified

- Actions, controllers, jobs, commands, Livewire components, service providers, config, and tests.

## Architecture Rules

- Services coordinate workflows; Actions perform single operations.
- Services should not become generic managers or helpers.
- External calls should have explicit timeout, retry, error, and test fake behavior.
- Prefer Laravel 13 container attributes over service-provider boilerplate when the binding is simple and local.
- Constructor injection is the default. Avoid `app()` unless resolving a dynamic implementation is the explicit purpose of the service.
- If the service starts accumulating unrelated responsibilities, split it into smaller Actions or services.

## Testing Requirements

- Test happy path, collaborator calls, failure paths, retries, and idempotency where relevant.

## Security Requirements

- Do not bypass policies or validation. Treat external data as untrusted.

## Review Checklist

- Is the service responsibility narrow?
- Would an Action be simpler?
- Are dependencies injectable and mockable or fakeable?

## Common Mistakes

- Adding unrelated methods over time.
- Mixing UI formatting with business coordination.
- Calling `app()` instead of injecting dependencies.
