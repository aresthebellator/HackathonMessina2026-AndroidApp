# Validation

Validate the package inventory during repository development:

```bash
php scripts/sync-boost-skills.php
php scripts/generate-manifest.php
php scripts/validate-skills.php
```

The validator checks:

- Manifest shape.
- Skill metadata.
- Guideline metadata.
- Skill paths.
- Guideline paths.
- Skill-to-guideline references.
- Preset-to-skill references.

