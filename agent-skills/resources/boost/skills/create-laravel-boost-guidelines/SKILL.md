---
name: create-laravel-boost-guidelines
description: "Use when adding Laravel Boost guidelines for a package or application."
license: MIT
metadata:
  author: Ali Harb
---

# Create Laravel Boost Guidelines

## Workflow

1. Inspect the package or application API, configuration, docs, and common pitfalls.
2. Create `resources/boost/guidelines/core.blade.php`.
3. Explain installation, core concepts, version-sensitive behavior, safety rules, and common usage patterns.
4. Keep examples short and accurate.
5. Avoid marketing copy inside agent-facing guidelines.
6. Link to deeper docs when details are long.

## Verification

- The guideline is valid Blade/XML-like markup.
- The package README references Boost support.

## Common Mistakes

- Repeating the full README instead of agent instructions.
- Including stale API examples.
