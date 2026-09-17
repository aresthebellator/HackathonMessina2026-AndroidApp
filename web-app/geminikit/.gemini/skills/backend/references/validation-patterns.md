# Validation Patterns

- **Schema Definition:** Define schemas in `schemas/` directory. Export them for use in API routes and frontend forms.
- **Strict Validation:** Use `.strict()` on Zod objects to prevent unexpected extra fields.
- **Type Inference:** Always derive TypeScript types from Zod schemas (`type User = z.infer<typeof UserSchema>`).
- **Coercion:** Use `z.coerce` for query parameters (e.g., `z.coerce.number()`).
- **Error Mapping:** Map Zod `ZodError` to RFC 7807 problem details in API responses.
- **Reusability:** Break large schemas into smaller, reusable building blocks.
