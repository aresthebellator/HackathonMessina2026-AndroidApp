# Package Audit Guidelines

## Purpose

Help AI assistants decide when a package is appropriate and how to verify it before adding or using it.

## Required Standards

- Inspect the installed package list and existing conventions before using a package feature.
- Prefer native Laravel, PHP, and project code first.
- Only use a package when it clearly matches the architecture and solves a real need.
- Check docs for the installed version before using newer APIs or examples.
- Avoid deprecated or removed methods.
- Document the package choice in the relevant skill or guideline when the package becomes part of the standard.

## Allowed Patterns

- Version-specific documentation lookup.
- Package-aware architecture decisions.
- Small, justified package additions.

## Forbidden Patterns

- Adding a package just because it is popular.
- Using package features that are not installed or not enabled.
- Copying examples from the wrong major version.

## Naming Conventions

- Use the exact package and version when documenting a package standard.

## Testing Expectations

- Add tests that prove package integration works at the boundary being used.

## Review Checklist

- Is the package already installed?
- Is there a native Laravel option first?
- Are we using the correct major version and API?

## Acceptable Examples

- Using the installed Filament v5 API after checking the v5 docs.
- Using Spatie Data because the project standard already includes it.

## Unacceptable Examples

- Installing a package before checking if Laravel already provides the feature.
- Copying a Filament v4 example into a Filament v5 project.
