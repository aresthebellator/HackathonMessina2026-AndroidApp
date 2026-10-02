---
name: review-static-analysis
description: "Use when reviewing code for PHPStan, typing, and maintainability issues before they become noisy failures."
license: MIT
metadata:
  author: Ali Harb
---

# Review Static Analysis

## When To Use

Use this skill when reviewing code for typing problems, generic misuse, nullability issues, or likely PHPStan failures.

## Inputs Needed

- Files changed, intended types, collections, generics, and known analysis baseline.

## Workflow

1. Inspect the changed code with types in mind.
2. Look for missing type hints, weak PHPDoc, and mixed arrays that should be typed.
3. Prefer explicit return types and parameter types.
4. Prefer small value objects, DTOs, and builders over ambiguous array structures.
5. Check collection generics and array-shape documentation where helpful.
6. Check for dead code, unreachable branches, and unsafe null assumptions.
7. Keep the fix narrow and testable.

## Files That May Be Created

- Tests, DTOs, value objects, casts, builders, or small support classes.

## Files That May Be Modified

- PHP classes, PHPDoc blocks, tests, and helper code.

## Architecture Rules

- Types should clarify the design, not obscure it.
- Prefer code that helps PHPStan and human readers at the same time.

## Testing Requirements

- Add or update tests when the typing change affects runtime behavior.

## Review Checklist

- Are return types explicit?
- Are array shapes or generics documented where they matter?
- Is any dynamic behavior being left too loose?

## Common Mistakes to Avoid

- Overusing `mixed`.
- Leaving collection or array types undocumented.
- Fixing analysis by suppressing issues instead of clarifying the code.
