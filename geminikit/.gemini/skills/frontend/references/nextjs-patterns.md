# Next.js Patterns

- **App Router:** Use `layout.tsx`, `page.tsx`, `loading.tsx`, `error.tsx` conventions.
- **Caching:** Prefer segment config (`revalidate`, `dynamic`) over fetch options where possible.
- **Metadata:** Define `metadata` object in each page/layout. Use template strings for dynamic titles.
- **Route Handlers:** Use `GET`, `POST` exported functions. Validate input with Zod.
- **Loading States:** Leverage `Suspense` and `loading.tsx` for streaming UI.
- **Server Actions:** Define actions in dedicated files or colocate in component with `"use server"`. Validate all arguments.
