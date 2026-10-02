---
name: design-blade-component
description: "Use when creating or refactoring Blade components, partials, and reusable view pieces."
license: MIT
metadata:
  author: Ali Harb
---

# Design Blade Component

## When To Use

Use this skill when a Blade view is getting repetitive, too large, or hard to reuse across the app.

## Inputs Needed

- UI purpose, repeated markup, slots, props, localization needs, accessibility concerns, and tests.

## Workflow

1. Inspect existing Blade components and partials.
2. Decide whether the UI belongs in a class-based component, anonymous component, or partial.
3. Prefer the smallest reusable unit that removes duplication.
4. Keep component APIs small and obvious.
5. Localize labels and messages.
6. Keep presentation logic out of the template whenever possible.
7. Add tests when the component drives behavior or important UI states.

## Files That May Be Created

- Blade component classes, component views, partials, and tests.

## Files That May Be Modified

- Existing views, translation files, and tests.

## Architecture Rules

- Blade should render presentation only.
- Shared markup belongs in shared components instead of repeated snippets.
- Keep templates small enough that they remain easy to scan.

## Testing Requirements

- Test rendering, visible states, localization, and accessibility-sensitive behavior when relevant.

## Review Checklist

- Is the component reusable?
- Is the public API small?
- Would a partial or slot make the code cleaner?

## Common Mistakes to Avoid

- Duplicating the same markup across many views.
- Putting business logic in the template.
- Creating a component when a simple partial would be clearer.
