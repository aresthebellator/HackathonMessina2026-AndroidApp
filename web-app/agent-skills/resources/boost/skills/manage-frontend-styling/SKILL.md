---
name: manage-frontend-styling
description: "Use when creating or updating Tailwind CSS 4, Flux, or Blade UI patterns and shared components."
license: MIT
metadata:
  author: Ali Harb
---

# Manage Frontend Styling

## When To Use

Use this skill for Blade UI, Tailwind styling, Flux controls, shared components, RTL behavior, dark mode, and accessibility-sensitive presentation.

## Inputs Needed

- Screen, user flow, responsive needs, locale requirements, design tokens, and reuse opportunities.

## Workflow

1. Inspect existing Blade, Flux, Tailwind, and component conventions.
2. Prefer shared components or partials when markup repeats.
3. Keep templates small and role-focused.
4. Use Tailwind 4 utilities and class sorting with Prettier.
5. Use only `oklch()` theme colors in app-owned CSS tokens.
6. Make layouts work in both light and dark modes.
7. Make layouts work in both LTR and RTL.
8. Localize all user-facing strings.
9. Keep accessibility in mind: semantics, labels, focus states, keyboard use, and contrast.
10. Add tests or snapshots when UI behavior is important.

## Files That May Be Created

- Blade components, partials, stylesheets, translation files, and tests.

## Files That May Be Modified

- `resources/views/**`
- `resources/css/**`
- translation files
- component classes
- frontend tests

## Architecture Rules

- Keep presentation code thin and reusable.
- Avoid logic that belongs in Actions, Services, DTOs, or controllers.
- Do not let template files become dumping grounds.

## Testing Requirements

- Test responsive behavior, accessibility-sensitive interactions, and localization when relevant.

## Review Checklist

- Is the markup shared instead of duplicated?
- Are classes sorted and readable?
- Does the UI remain usable in RTL and dark mode?
- Are strings translated?

## Common Mistakes to Avoid

- Hardcoded colors or copy.
- Repeating the same component markup many times.
- Writing heavy Blade files with too much logic.
