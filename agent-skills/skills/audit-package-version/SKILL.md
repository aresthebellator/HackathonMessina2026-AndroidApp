---
name: audit-package-version
description: "Use when checking whether a Laravel package or feature should be used and which version-specific APIs are safe."
license: MIT
metadata:
  author: Ali Harb
---

# Audit Package Version

## When To Use

Use this skill before using or documenting a package feature, especially after a framework or package major upgrade.

## Inputs Needed

- Package name, installed version, feature area, and the code path that will consume it.

## Workflow

1. Inspect installed versions and current project usage.
2. Read version-specific docs before copying examples.
3. Check for deprecated or removed APIs.
4. Prefer native framework options if they solve the same problem.
5. Add the package only when it is clearly justified and already approved.
6. Document the dependency on the package in the relevant guideline or skill.

## Files That May Be Created

- Architecture notes, guidelines, skills, or tests.

## Files That May Be Modified

- Package-consuming code, panel providers, resources, requests, and tests.

## Architecture Rules

- Use the installed package version, not a nearby major version.
- Keep package usage narrow and intentional.

## Testing Requirements

- Add tests that cover the integration point.

## Review Checklist

- Is this the correct version?
- Is there a simpler native option?
- Did we avoid deprecated APIs?

## Common Mistakes to Avoid

- Copying code from the wrong package version.
- Using a package feature without checking whether it exists in this app.
