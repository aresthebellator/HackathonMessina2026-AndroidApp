---
name: debug-phpstan
description: "Use when PHPStan or Larastan reports type, generic, nullability, or framework inference issues."
license: MIT
metadata:
  author: Ali Harb
---

# Debug PHPStan

## Workflow

1. Read the exact PHPStan message and line.
2. Identify whether the issue is a real bug, missing type, generic mismatch, or framework inference gap.
3. Prefer code-level types, typed collections, return types, and PHPDoc generics over broad ignores.
4. Use targeted ignores only when framework magic cannot be expressed safely.
5. Re-run PHPStan after each small fix.

## Verification

- `vendor/bin/phpstan analyse` passes.

## Common Mistakes

- Adding `mixed` everywhere.
- Ignoring real nullable access bugs.
