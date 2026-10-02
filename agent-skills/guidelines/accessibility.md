# Accessibility Guidelines

## Purpose

Keep Laravel UI usable for keyboard, screen reader, low vision, mobile, and RTL users.

## Rules

- Use semantic HTML before ARIA.
- Connect labels, help text, and error messages to inputs.
- Preserve visible focus states.
- Check color contrast and reduced motion.
- Ensure controls are keyboard reachable.

## Anti-Patterns

- Clickable `div` elements instead of buttons or links.
- Hidden labels without accessible names.
- Removing focus outlines.
