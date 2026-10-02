# Code Styling & Formatting (Pint) Guidelines

## Purpose

Enforce code formatting and syntax styling standards consistently across the codebase using Laravel Pint, resolving style drift before commits are pushed.

## Required Standards

- **Enforce Pint Styling**: All PHP code must be formatted using Laravel Pint before commits are pushed to the remote repository.
- **Pint Configuration Profiles**: Projects must maintain a `pint.json` at the root, defaulting to the `laravel` style preset. Custom styling overrides must be declared explicitly in this file.
- **Import Sorting**: Organize imports alphabetically and prune unused `use` statements from PHP classes to keep import namespaces clean and readable.
- **Strict Formatting Audits**: Prior to code reviews, pull requests should be validated with Pint audits (`./vendor/bin/pint --test`) in CI pipelines to prevent formatting regressions.

## Allowed Patterns

```json
// pint.json
{
    "preset": "laravel",
    "rules": {
        "concat_space": {
            "spacing": "one"
        },
        "simplified_null_return": true,
        "array_syntax": {
            "syntax": "short"
        }
    }
}
```

## Forbidden Patterns

- Introducing pull requests with layout drift, trailing spaces, or raw formatting exceptions.
- Disabling the Pint formatting checks on class namespaces because of personal IDE preferences.
- Manually adjusting indentation structures in an inconsistent way.
