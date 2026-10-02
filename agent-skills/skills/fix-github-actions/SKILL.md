---
name: fix-github-actions
description: "Use when GitHub Actions are failing because of Composer, PHP versions, test matrices, linting, or workflow configuration."
license: MIT
metadata:
  author: Ali Harb
---

# Fix GitHub Actions

## Workflow

1. Inspect the failing workflow, latest run logs, dependency constraints, and local reproduction path.
2. Identify whether the failure is code, dependency, environment, matrix, cache, or workflow syntax.
3. Prefer fixing the real compatibility issue over weakening CI.
4. Keep supported PHP, Laravel, and package versions honest in README and Composer constraints.
5. Run the closest local command before pushing.
6. Push and verify the new workflow run.

## Verification

- Run the failing command locally when possible.
- Confirm the latest remote workflow conclusion is `success`.

## Common Mistakes

- Ignoring Composer security advisories.
- Removing a matrix lane without documenting dropped support.
- Fixing only one workflow while another still fails.
