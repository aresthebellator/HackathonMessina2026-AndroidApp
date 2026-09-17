# Versioning

Agent Skills follows semantic versioning.

- Patch releases fix existing behavior or docs.
- Minor releases add backwards-compatible skills, guidelines, metadata, or presets.
- Major releases may rename skills, remove presets, or change Boost/skills.sh structure.

Before tagging:

```bash
composer validate
vendor/bin/pint --test
vendor/bin/pest
vendor/bin/phpstan analyse
php scripts/validate-skills.php
```
