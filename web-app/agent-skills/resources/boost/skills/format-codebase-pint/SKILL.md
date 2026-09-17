---
name: format-codebase-pint
description: "Use when running Laravel Pint to fix styling drift, verifying import sorting, or configuring custom rules inside pint.json."
license: MIT
metadata:
  author: Ali Harb
---

# Format Codebase with Pint

## When To Use

Use this skill when staging commits, fixing PSR/formatting rule violations, or updating linting rules.

## Workflow

1. **Verify setup**: Ensure Pint is installed via composer (`composer require laravel/pint --dev`).
2. **Run linting fixer**: Run `./vendor/bin/pint` from the root of the repository to fix code styling automatically.
3. **Verify files**: Run `git diff` to ensure changes align with formatting expectations.
4. **Dry run audits**: In CI pipelines, run `./vendor/bin/pint --test` to audit without writing files.

## Examples

### Good Example

```bash
# ✅ Format only the dirty staged files before committing
./vendor/bin/pint --dirty
```

### Bad Example

```bash
# ❌ Skipping styling fixes entirely, resulting in styling conflicts and rejected PRs in CI.
```
---
