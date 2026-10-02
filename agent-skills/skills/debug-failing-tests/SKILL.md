---
name: debug-failing-tests
description: "Use when Pest, PHPUnit, Livewire, browser, or package tests fail and need systematic diagnosis."
license: MIT
metadata:
  author: Ali Harb
---

# Debug Failing Tests

## Workflow

1. Run the smallest failing test first.
2. Read the failure message, stack trace, and assertion intent.
3. Determine whether the test, fixture, environment, or product code is wrong.
4. Fix the root cause instead of weakening assertions.
5. Add a regression test when the failure exposed an untested behavior.
6. Re-run the targeted test and then the relevant suite.

## Verification

- Targeted test passes.
- Relevant suite passes.

## Common Mistakes

- Re-running the full suite repeatedly before isolating the failure.
- Deleting assertions that describe real behavior.
