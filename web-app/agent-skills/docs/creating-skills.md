# Creating Skills

Each skill should be focused on one workflow.

Required frontmatter:

```yaml
---
name: create-action
description: "Use when creating or changing a Laravel Action class."
license: MIT
metadata:
  author: Ali Harb
---
```

Recommended sections:

- When To Use
- Workflow
- Verification
- Common Mistakes

After creating a skill:

```bash
php scripts/generate-manifest.php
php scripts/validate-skills.php
```
