---
name: design-multi-tenant-feature
description: "Use when designing Laravel features that must be tenant-aware, isolated, authorized, and testable."
license: MIT
metadata:
  author: Ali Harb
---

# Design Multi Tenant Feature

## Workflow

1. Identify tenant model, tenant resolution, data ownership, users, roles, and cross-tenant admin behavior.
2. Add tenant keys, indexes, and constraints where data must be isolated.
3. Scope queries through policies, builders, middleware, or tenancy package conventions.
4. Prevent cross-tenant access in jobs, events, exports, imports, and APIs.
5. Add tests that prove tenant isolation and admin exceptions.

## Verification

- Cross-tenant access tests fail safely.
- Database constraints support tenant boundaries.
