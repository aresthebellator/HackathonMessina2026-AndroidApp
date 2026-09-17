---
name: create-architecture-test
description: "Use when creating or updating Pest architecture tests for Laravel layer boundaries."
license: MIT
metadata:
  author: Ali Harb
---

# Create Architecture Test

## When To Use

Use this skill when you need to enforce layer boundaries, dependency direction, or code placement rules.

## Inputs Needed

- Rule to enforce, affected namespaces, allowed exceptions, and regression target.

## Workflow

1. Inspect existing architecture tests and the relevant code layers.
2. Identify the smallest meaningful boundary to protect.
3. Write the test for app-owned code first.
4. Add ignore rules only for framework-generated or vendor-owned code when needed.
5. Keep assertions stable and easy to read.
6. Add a companion behavior test if the architecture rule protects something user-visible.

## Files That May Be Created

- Pest architecture tests.

## Files That May Be Modified

- Existing architecture tests and related tests.

## Architecture Rules

- Test the boundary, not incidental implementation details.
- Keep the test strict for the project’s own code.
- Avoid false positives from vendor traits or framework internals unless the project owns that behavior.

## Testing Requirements

- Run the smallest relevant test set after adding or updating the rule.

## Review Checklist

- Does the test catch the actual architectural drift?
- Is the failure message actionable?
- Are vendor or framework exceptions narrowly scoped?

## Common Mistakes to Avoid

- Asserting exact file layout when the rule is about behavior.
- Forgetting to exempt framework-owned internals.
