---
name: review-feature
description: "Use when reviewing a Laravel feature for architecture, authorization, validation, testing, API readiness, performance, security, and maintainability."
license: MIT
metadata:
  author: Ali Harb
---

# Review Feature

## When To Use

Use this skill for code review, pre-merge review, AI-generated feature review, or refactor assessment.

## Inputs Needed

- Changed files, intended behavior, routes, clients, data model, risks, and test results.

## Workflow

1. Understand the intended behavior.
2. Inspect changed files and adjacent conventions.
3. Check architecture boundaries.
4. Check authorization and validation.
5. Check dependency injection and flag unnecessary `app()` or service-locator usage.
6. Check whether Laravel 13 and PHP 8.3+ attributes would make framework metadata clearer.
7. Check error handling, API readiness, performance, security, and maintainability.
8. Check tests for success, failure, authorization, validation, and regression paths.
9. Report findings by severity with file and line references.

## Files That May Be Created

- None unless asked to fix findings.

## Files That May Be Modified

- None during review-only work.

## Architecture Rules

- Controllers, Livewire, routes, Blade, and models stay thin.
- Business logic lives in reusable classes.
- Dependencies are injected unless the code is at a framework boundary.
- Attributes express metadata; they do not hide business behavior.

## Testing Requirements

- Identify missing tests and risky untested paths.

## Security Requirements

- Treat missing authorization, unsafe input, sensitive data exposure, and unsafe external calls as high priority.

## Review Checklist

- Architecture compliance.
- Authorization.
- Validation.
- Testing.
- Error handling.
- API readiness.
- Performance.
- Security.
- Maintainability.
- Dependency injection.
- Modern Laravel/PHP feature usage.

## Common Mistakes

- Leading with style before correctness.
- Ignoring failure paths.
- Accepting duplicated client-layer logic.
