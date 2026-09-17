# Composer Dependencies Guidelines

## Purpose

Keep Laravel dependency constraints honest, secure, and maintainable.

## Rules

- Prefer tested version ranges over broad optimistic constraints.
- Respect Composer security advisories.
- Use `composer why-not` for solver conflicts.
- Keep dev dependencies aligned with CI matrices.
- Update README requirements when constraints change.

## Anti-Patterns

- Ignoring advisories to keep outdated support.
- Pinning framework versions unnecessarily.
- Adding packages without a clear reason.

## Verification

- `composer validate`
- `composer update --dry-run`
