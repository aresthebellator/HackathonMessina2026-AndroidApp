# Container Attributes Guidelines

## Purpose

Use Laravel's container attributes to reduce boilerplate while keeping dependencies explicit.

## Required Standards

- Prefer dependency injection over `app()` wherever practical.
- Use contextual attributes for config, cache, storage, DB, logging, auth, route parameters, tags, and current user injection when they improve clarity.
- Keep custom attributes small and focused if the project defines any.
- Use attributes when they describe framework metadata, not business rules.

## Allowed Patterns

- `#[Config]`
- `#[Storage]`
- `#[Cache]`
- `#[DB]`
- `#[Log]`
- `#[Give]`
- `#[Tag]`
- `#[Auth]`
- `#[RouteParameter]`
- `#[CurrentUser]`
- Project-specific contextual attributes when they are documented

## Forbidden Patterns

- Using attributes to hide business logic.
- Replacing all constructor injection with attributes just because they exist.
- Creating custom attributes without a clear project use case.

## Naming Conventions

- Attribute names should explain the injected concern.

## Testing Expectations

- Test classes that rely on attributes through the code path that consumes them.

## Review Checklist

- Is the attribute improving clarity?
- Could normal constructor injection be simpler?
- Is the dependency still easy to fake or override?

## Acceptable Examples

- Injecting a specific cache store with `#[Cache('redis')]`.
- Injecting route-model parameters with `#[RouteParameter('photo')]`.

## Unacceptable Examples

- Using attributes to smuggle workflow decisions into constructors.
