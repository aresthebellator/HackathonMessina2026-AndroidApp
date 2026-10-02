# GitHub Actions Guidelines

## Purpose

Keep CI fast, honest, and aligned with supported package versions.

## Rules

- Test every advertised PHP/Laravel/package support lane.
- Use Composer validation, Pint, PHPStan, and tests where relevant.
- Prefer fixing root causes over weakening workflows.
- Keep matrix names readable.
- Confirm latest runs after pushing.

## Anti-Patterns

- Removing failing lanes without changing support docs.
- Running only style checks for packages with behavior.
- Ignoring failures on release tags.
