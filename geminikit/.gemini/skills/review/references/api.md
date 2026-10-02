---
mode: api
extends: review
version: "1.0.0"
---

# Extra Rules

- Focus exclusively on REST/GraphQL API design patterns and specs.
- Enforce standard status codes (2xx, 4xx, 5xx) for all possible outcomes.
- Validate pagination, filtering, and sorting for all collection endpoints.
- Check for idempotency in PUT and DELETE operations.
- Verify security headers (e.g., CORS, Rate-Limiting, Content-Type).
- Flag missing schema validation for all incoming payloads.
- Ensure GraphQL queries have depth limits and appropriate error handling.

# Extra Output

```json
{
  "api_standard_compliance": ["string"],
  "missing_endpoints": ["string"],
  "breaking_changes": ["string"],
  "security_headers_status": ["string"]
}
```

## Steps
1. Filter focus for API design
2. Validate REST/GraphQL best practices
3. Check status code compliance
4. Verify idempotency and schema validation

## Examples
**Input:** `/gk-review --api src/swagger.yaml`
**Expected behavior:** Detailed review of API specs, status codes, and security headers.
