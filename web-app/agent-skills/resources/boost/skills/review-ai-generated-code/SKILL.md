---
name: review-ai-generated-code
description: "Use when reviewing code written by an AI assistant for correctness, safety, maintainability, and project fit."
license: MIT
metadata:
  author: Ali Harb
---

# Review AI Generated Code

## Workflow

1. Inspect the diff, user request, tests, and surrounding conventions.
2. Look first for behavioral bugs, security issues, data loss, and hidden regressions.
3. Check whether generated code invented APIs or ignored installed versions.
4. Verify architecture boundaries and naming fit the project.
5. Confirm tests cover meaningful behavior and failure paths.
6. Report findings by severity with file and line references.

## Verification

- Run relevant tests and static analysis when fixing findings.

## Common Mistakes

- Praising broad generated changes without checking behavior.
