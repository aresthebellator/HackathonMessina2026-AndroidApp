---
name: create-domain-feature
description: "Use when building a general Laravel domain feature that spans routes, UI, API, actions, policies, DTOs, events, jobs, or tests."
license: MIT
metadata:
  author: Ali Harb
---

# Create Domain Feature

## When To Use

Use this skill for cohesive behavior that touches multiple layers.

## Inputs Needed

- Boundary, actor, resource, use cases, clients, data contracts, authorization, validation, side effects, async needs, and tests.

## Workflow

1. Define the feature boundary and responsibility first.
2. Inspect existing conventions and related code.
3. Create DTOs for meaningful input or output contracts.
4. Create Actions for business operations.
5. Create Policies for protected resources.
6. Create Filament resources, relation managers, pages, and widgets when the feature has admin-facing UX.
7. Inject dependencies into Actions, Services, Jobs, commands, and controllers instead of resolving them with `app()`.
8. Use Laravel 13 attributes for controller middleware/authorization, container injection, model metadata, Form Requests, resources, jobs, and Livewire when they clarify framework metadata.
9. Create Events for useful completed facts.
10. Create Jobs only for asynchronous work.
11. Add controllers, Livewire components, commands, or API endpoints as thin clients.
12. Add tests across behavior, authorization, validation, API, Livewire, events, jobs, and failure paths as needed.
13. Add documentation only when behavior is important or requested.

## Files That May Be Created

- DTOs, Actions, Policies, Events, Jobs, Resources, Requests, Controllers, Livewire components, migrations, and tests.

## Files That May Be Modified

- Routes, providers, models, views, config, and tests.

## Architecture Rules

- Client layers orchestrate; reusable layers own behavior.
- Keep web, API, Livewire, mobile, desktop, command, and third-party reuse in mind.
- Dependency injection is preferred over `app()` throughout reusable code.
- Use Filament resource, relation manager, and page reuse patterns when the feature is admin-facing.

## Testing Requirements

- Test meaningful behavior, protected access, invalid input, failures, and client contracts.

## Security Requirements

- Validate input, authorize behavior, protect sensitive output, and use safe queue and event payloads.

## Review Checklist

- Is the boundary clear?
- Is logic reusable?
- Are protected paths denied by default?
- Are tests proportional to risk?

## Common Mistakes

- Starting with UI before defining reusable behavior.
- Creating jobs for synchronous work.
- Duplicating logic for API and web clients.
