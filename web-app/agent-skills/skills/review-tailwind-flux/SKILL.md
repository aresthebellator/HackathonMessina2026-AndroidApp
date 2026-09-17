---
name: review-tailwind-flux
description: "Use when reviewing Tailwind CSS 4 and Flux UI for accessibility, responsiveness, RTL, and maintainability."
license: MIT
metadata:
  author: Ali Harb
---

# Review Tailwind And Flux UI

## When To Use

Use this skill when reviewing or refining Blade UI that uses Tailwind CSS 4 and Flux components.

## Inputs Needed

- Screen, responsive goals, RTL requirements, theme behavior, component reuse, and visible copy.

## Workflow

1. Inspect the current component or page markup.
2. Check the layout at small, medium, and large widths.
3. Check light, dark, and RTL behavior.
4. Check semantics, labels, keyboard support, and focus states.
5. Check whether repeated UI should be extracted.
6. Check that classes are sorted and the styling is easy to maintain.
7. Check that all visible strings are localized.

## Files That May Be Created

- Usually none; sometimes shared components, styles, or tests.

## Files That May Be Modified

- Blade views, shared components, stylesheets, and tests.

## Architecture Rules

- UI should remain reusable and thin.
- Flux should enhance accessibility and consistency, not hide duplication.

## Testing Requirements

- Add UI tests or browser checks when the behavior is important or easy to regress.

## Review Checklist

- Is it responsive?
- Is it RTL-safe?
- Does it work in dark mode?
- Is it accessible?
- Is the markup duplicated anywhere?

## Common Mistakes to Avoid

- Heavy template files.
- Hardcoded strings.
- Styling only one theme or one writing direction.
