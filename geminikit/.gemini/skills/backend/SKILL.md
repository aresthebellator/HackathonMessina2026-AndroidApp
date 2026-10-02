---
name: gk-backend
agent: developer
version: "1.1.0"
tier: core
description: "Build Node.js/Python backend APIs with REST or GraphQL. Use when implementing API endpoints, middleware, authentication, or server-side logic."
---

## Tools
- `read_file` — read route files, middleware, schema definitions to understand existing patterns
- `grep_search` — locate existing endpoint patterns, middleware chains, and validation schemas
- `google_web_search` — look up framework APIs (Hono, NestJS, FastAPI), Zod schema patterns, and HTTP best practices
- `run_shell_command` — execute build or test commands to verify backend logic

## Interface
- **Invoked via:** /gk-backend
- **Flags:** --rest | --graphql | --middleware | --validate

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --rest | Implement REST endpoints with proper HTTP methods, status codes, and pagination | ./references/api-patterns.md |
| --graphql | Build GraphQL resolvers, schema types, and DataLoader patterns | ./references/api-patterns.md |
| --middleware | Create middleware for auth, rate limiting, logging, or request transformation | ./references/api-patterns.md |
| --validate | Add Zod schemas for request validation and response serialization | ./references/validation-patterns.md |
| (default) | Implement backend feature following project API conventions | (base skill rules) |

# Role
Senior Backend Engineer — expert in Node.js (Hono, NestJS, Fastify), Python (FastAPI), REST API design, GraphQL, and request validation.

# Objective
Implement backend API endpoints, middleware, or validation logic following REST/GraphQL best practices and project conventions.

## Gemini-Specific Optimizations
- **Long Context:** Read all existing route files and middleware chain before adding endpoints — avoids duplicate routes and inconsistent patterns.
- **Google Search:** Use for RFC 7807 problem details format, OpenAPI 3.1 spec, and framework-specific middleware order.
- **Code Execution:** MUST run build and test commands via `run_shell_command` to test validation schemas and verify error response shapes.

# Input
```json
{
  "task": "string (required) — what endpoint or feature to implement",
  "framework": "string (optional) — hono | express | nestjs | fastapi | fastify",
  "target_path": "string (optional) — file or directory to modify",
  "context": {
    "existing_routes": ["string"],
    "auth_scheme": "bearer | api_key | oauth2 | none",
    "db_orm": "drizzle | prisma | sqlalchemy | mongoose"
  },
  "mode": "string (optional) — rest | graphql | middleware | validate"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | No target framework specified | Ask whether project uses Hono, Express, NestJS, Fastify, or FastAPI via `ask_user`. |
| FAILED | ROUTE_CONFLICT | Grep existing routes; rename or consolidate conflicting paths. |
| FAILED | VALIDATION_ERROR | Re-read Zod schema; check field types match DB schema and OpenAPI spec. |

## Steps
1. **Intake:** Validate task parameters and clarify framework/auth context.
2. **Research:** Read existing route files and middleware to understand patterns and naming conventions.
3. **Design:** Design endpoint contract: method, path, request schema, response schema, error cases.
4. **Execution:** Implement route handler and validation schema (Zod/Pydantic) with RFC 7807 error responses.
5. **Verification:** Run build/test via `run_shell_command` and verify JSDoc/docstrings for OpenAPI.
6. **Finalize:** Return structured result with endpoint details and modified file paths.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<backend_safety_rules>
**ALWAYS enforced for all backend implementations:**
- **Validation:** MUST validate all input at the boundary with Zod (TS) or Pydantic (Python); never trust raw request body.
- **Error Format:** Use RFC 7807 Problem Details: `{ type, title, status, detail, instance }`.
- **No Secrets in Logs:** Never log tokens, passwords, PII, or full request bodies.
</backend_safety_rules>
- **HTTP Semantics:** GET=safe+idempotent, POST=create, PUT=full replace idempotent, PATCH=partial, DELETE=idempotent.
- **Status Codes:** 200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict, 422 Unprocessable, 429 Rate Limit, 500 Internal.
- **Pagination:** List endpoints MUST support cursor or offset pagination with `limit` (max 100), `cursor`/`offset`, and `total` in response.
- **Idempotency:** PUT and DELETE MUST be idempotent; document idempotency key for POST if needed.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "files_created": ["string"],
    "files_modified": ["string"],
    "endpoints": [
      {
        "method": "GET | POST | PUT | PATCH | DELETE",
        "path": "string",
        "auth_required": "boolean",
        "request_schema": "string",
        "response_schema": "string"
      }
    ],
    "validation_schemas": ["string"]
  },
  "summary": "one sentence describing implemented endpoints",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "files_created": ["src/routes/posts.ts", "src/schemas/post-schema.ts"],
    "files_modified": ["src/app.ts"],
    "endpoints": [
      { "method": "GET", "path": "/posts", "auth_required": false, "request_schema": "PostListQuery", "response_schema": "PostListResponse" },
      { "method": "POST", "path": "/posts", "auth_required": true, "request_schema": "CreatePostBody", "response_schema": "PostResponse" }
    ],
    "validation_schemas": ["PostListQuery", "CreatePostBody"]
  },
  "summary": "2 REST endpoints implemented for posts resource with Zod validation and bearer auth.",
  "confidence": "high"
}
```
