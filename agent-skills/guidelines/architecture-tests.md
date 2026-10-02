# Architecture Test Guidelines

## Purpose

Use architecture tests to keep Laravel layers separated and to prevent accidental drift over time.

## Required Standards

- Treat architecture tests as part of the project contract, not optional extra coverage.
- Enforce boundaries that matter to the app: controllers, Livewire, models, routes, requests, services, actions, builders, DTOs, policies, events, jobs, and Blade views.
- Keep rules strict for app-owned code and tolerant of framework or vendor internals unless the project explicitly owns them.
- Prefer dependency-direction checks over implementation-detail checks.
- Update architecture tests whenever the standards change.

## Allowed Patterns

- Pest architecture tests for layer dependencies.
- Reflection-based checks for model methods, scope placement, and thin layers.
- Dependency assertions for client-layer isolation.
- Exceptions or ignore lists for framework-generated or vendor-owned classes.

## Forbidden Patterns

- Brittle tests that fail on harmless vendor traits or generated framework code.
- Tests that enforce one exact implementation when several compliant options exist.
- Rules that are too broad to be useful.

## Naming Conventions

- Test names should describe the boundary being protected.
- Keep failures readable and actionable.

## Testing Expectations

- Add a test when a new architecture rule is introduced.
- Add a regression test when a layer leaks logic into another layer.

## Review Checklist

- Does the test protect a real boundary?
- Is it strict for app code and tolerant for vendor code?
- Would the failure help a developer fix the problem quickly?

## Acceptable Examples

- Controllers do not depend on Livewire components.
- Models do not contain app-owned query scopes.
- Domain code does not depend on request or response classes.

## Unacceptable Examples

- A test that fails because a vendor trait adds a helper method.
- A test that only asserts file names instead of behavior boundaries.
