# Frontend Styling Guidelines

## Purpose

Keep Blade, Tailwind CSS 4, and Flux-based UI flexible, accessible, localized, and easy to maintain.

## Required Standards

- Use the latest supported Tailwind CSS 4 conventions available in the project.
- Use only `oklch()` color values in app-owned CSS theme definitions and tokens.
- Configure dark mode with selector-based support so Flux and Tailwind classes can follow the same light/dark behavior.
- Ensure Blade components and views are responsive, RTL-friendly, and accessible by default.
- Prefer shared components, partials, or view components over duplicated markup.
- Keep templates small and easy to scan.
- Prefer the smallest reusable Blade component or partial that removes duplication.
- Keep user-facing strings localized.
- Avoid hardcoded copy in Blade when a translation key is available.
- Preserve semantic HTML, label associations, focus states, keyboard access, and readable contrast.

## Allowed Patterns

- Tailwind 4 utility classes.
- Flux components and controls when the package is installed.
- Shared Blade components and class-based components.
- RTL-aware utilities and layout flipping.
- Locale-driven text and validation messages.
- Prettier with Tailwind class sorting.

## Forbidden Patterns

- Mixing hardcoded colors, raw hex values, and non-oklch theme tokens into app-owned CSS.
- Duplicating the same markup across many Blade files.
- Building layouts that only work in one direction or one color mode.
- Hiding interactive controls from keyboard users.
- Putting heavy logic in Blade or creating fat view files.

## Naming Conventions

- Component and partial names should describe the UI role.
- Translation keys should follow the feature or component name.

## Testing Expectations

- Add browser, feature, or component tests for interactive UI and accessibility-sensitive behavior when practical.
- Test localization keys when they are part of the user-visible contract.

## Review Checklist

- Is the layout responsive at small and large widths?
- Does RTL still read naturally?
- Are light and dark themes both usable?
- Are colors sourced from oklch tokens?
- Is the UI accessible with keyboard and screen-reader usage?
- Is repeated markup extracted?

## Acceptable Examples

- A small shared Blade component used across multiple forms.
- A class-based or anonymous Blade component with a narrow API.
- Tailwind classes sorted by Prettier.
- A layout that uses `dark:` and RTL-aware utilities.

## Unacceptable Examples

- Hardcoded colors in app CSS outside the oklch theme system.
- One-off Blade snippets repeated in many files.
- A control that only works with a mouse.
