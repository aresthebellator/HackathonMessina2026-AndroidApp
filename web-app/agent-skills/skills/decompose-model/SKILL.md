---
name: decompose-model
description: "Use when a model is getting too large and needs concerns, casts, builders, or other extractions."
license: MIT
metadata:
  author: Ali Harb
---

# Decompose Model

## When To Use

Use this skill when an Eloquent model is becoming too large or starts mixing persistence with workflow code.

## Inputs Needed

- Model name, current responsibilities, reusable behaviors, query needs, and extraction target.

## Workflow

1. Inspect the model and its related classes.
2. Identify what belongs in relations, casts, accessors, mutators, builders, policies, or support classes.
3. Extract reusable query logic into a builder or query object.
4. Extract reusable behavior into a concern when it fits.
5. Keep only small model-level invariants in the model.
6. Add or update tests for the extracted behavior.

## Files That May Be Created

- Builders, concerns, casts, support classes, policies, DTOs, and tests.

## Files That May Be Modified

- The model, related builders, and related tests.

## Architecture Rules

- The model should stay readable and persistence-focused.
- Reusable query logic should not become model scopes in new code.
- The model should not orchestrate workflows.

## Testing Requirements

- Test the extracted behavior where it now lives.

## Review Checklist

- Did the extraction reduce model size?
- Is the logic in the right layer?
- Did behavior stay the same?

## Common Mistakes to Avoid

- Extracting too many tiny files without reducing complexity.
- Leaving reusable query behavior in model scopes.
