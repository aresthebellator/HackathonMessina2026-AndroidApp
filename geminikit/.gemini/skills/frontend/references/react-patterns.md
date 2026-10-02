# React Patterns

- **RSC First:** Default to React Server Components. Use `"use client"` only for interactivity, hooks, or browser-specific APIs.
- **Component Colocation:** Place tests, styles, and hooks in the same directory as the component.
- **Strict Typing:** All components require explicit prop types (using `interface` or `type`). No `any`. Use discriminated unions for variant-based components.
- **shadcn/ui Composition:** Extend existing components using `cn()` utility. Avoid recreating basic primitives.
- **Data Fetching:** Use `use` hook or `React.cache` for data fetching in server components to avoid waterfalls.
- **Accessibility:** Ensure ARIA roles (e.g., `role="button"`), labels, and keyboard focus are implemented for custom components.
