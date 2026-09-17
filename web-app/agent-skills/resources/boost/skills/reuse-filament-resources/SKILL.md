---
name: reuse-filament-resources
description: "Use when building Filament relation managers, pages, or admin screens that should reuse existing resource form and table definitions."
license: MIT
metadata:
  author: Ali Harb
---

# Reuse Filament Resources

## When To Use

Use this skill when a relation manager, page, or secondary admin surface needs the same form or table behavior as a resource.

## Inputs Needed

- Resource, related manager, page, duplicated UI, and tests.

## Workflow

1. Inspect the existing resource form, table, and infolist.
2. Identify the shared UI that can be reused directly.
3. Delegate to the resource’s form or table methods when the structure is the same.
4. Extract shared schema fragments when only part of the UI differs.
5. Keep the relation manager or page thin.
6. Test the reused behavior from both the resource and the relation manager.

## Files That May Be Created

- Shared schema helpers, tests, or thin relation-manager/page wrappers.

## Files That May Be Modified

- Filament resources, relation managers, pages, and tests.

## Architecture Rules

- Reuse the resource definition before rewriting the same form or table twice.
- Keep admin duplication low and intentional.

## Testing Requirements

- Test the resource and the relation manager/page that reuses it.

## Review Checklist

- Can the resource method be reused directly?
- Is any duplication still necessary?
- Is the wrapper thin?

## Common Mistakes to Avoid

- Copying a resource form into a relation manager.
- Rewriting identical columns or actions in multiple places.
