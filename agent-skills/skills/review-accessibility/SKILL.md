---
name: review-accessibility
description: "Use when reviewing Blade, Livewire, Inertia, Tailwind, Flux, or Filament UI for accessibility."
license: MIT
metadata:
  author: Ali Harb
---

# Review Accessibility

## Workflow

1. Check semantic HTML, labels, headings, focus order, keyboard access, and ARIA usage.
2. Verify form errors are announced and connected to inputs.
3. Check color contrast, reduced motion, responsive behavior, and RTL impact.
4. Ensure buttons and links communicate their purpose.
5. Add tests or snapshots where the project already uses accessibility tooling.

## Verification

- UI remains keyboard usable.
- Critical controls have labels and visible focus states.

## Common Mistakes

- Using ARIA to compensate for incorrect HTML.
- Hiding focus outlines.
