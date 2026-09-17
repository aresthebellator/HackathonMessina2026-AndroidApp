---
name: implement-api-rate-limiting
description: "Use when adding API rate limits, abuse controls, throttles, and tests for Laravel routes or integrations."
license: MIT
metadata:
  author: Ali Harb
---

# Implement API Rate Limiting

## Workflow

1. Identify actors, endpoints, authentication mode, abuse risk, and expected traffic.
2. Configure named Laravel rate limiters with meaningful keys.
3. Apply throttles at the route or middleware boundary.
4. Return safe error responses without leaking sensitive details.
5. Add tests for allowed requests, exceeded limits, guests, and authenticated users.

## Verification

- Rate limit tests pass.
- Limits are documented for API consumers when public.
