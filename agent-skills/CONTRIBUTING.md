# Contributing

Thank you for improving Laravel Agent Skills.

## Workflow

1. Keep every skill focused on one workflow.
2. Prefer shared guidelines over duplicated rules.
3. Add or update tests when package tooling changes.
4. Regenerate the manifest after adding skills or guidelines.
5. Run validation before opening a PR.

```bash
php scripts/generate-manifest.php
php scripts/validate-skills.php
composer validate
vendor/bin/pint --test
vendor/bin/pest
vendor/bin/phpstan analyse
```

## Skill Requirements

Each skill should include:

- Frontmatter with `name`, `description`, `license`, and `metadata.author`.
- A clear workflow.
- Verification guidance.
- Common mistakes when useful.

## Guideline Requirements

Guidelines should be reusable, short, and enforceable.
