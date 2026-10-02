---
name: implement-permissions
description: "Use when implementing Laravel permissions, roles, gates, policies, or Filament authorization."
license: MIT
metadata:
  author: Ali Harb
---

# Implement Permissions

## Workflow

1. Identify actors, protected resources, actions, ownership rules, and admin exceptions.
2. Prefer policies for model resources and gates for standalone abilities.
3. Enforce authorization server-side in controllers, actions, jobs, APIs, and Filament resources.
4. Hide UI controls only after server-side rules exist.
5. Add tests for allowed, denied, guest, owner, and admin paths.

## Verification

- Authorization tests pass.
- No protected state change relies only on Blade or frontend visibility.
