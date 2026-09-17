# API Patterns

- **REST Semantics:** Standard HTTP methods (GET, POST, PUT, PATCH, DELETE) with appropriate status codes (200, 201, 204, 400, 401, 403, 404, 422, 500).
- **Error Responses:** Use RFC 7807 (Problem Details): `{ type, title, status, detail, instance }`.
- **Validation:** Always use Zod schemas for request body, query params, and headers. Validate at the entry point.
- **GraphQL:** Use DataLoader for N+1 queries. Define schema clearly; prefer specific input types.
- **Middleware:** Order matters. Global middleware (logging, error handling) first, route-specific (auth, validation) last.
