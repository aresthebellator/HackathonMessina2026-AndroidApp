---
name: configure-local-environment
description: "Use when setting up site domain mapping, configuring Mailpit SMTP credentials, and switching PHP versions locally using Laravel Herd."
license: MIT
metadata:
  author: Ali Harb
---

# Configure Local Environment

## When To Use

Use this skill when installing a new project locally, setting up isolated local test databases, or configuring mail traps.

## Workflow

1. **Herd Link**: Run `herd link` inside the project root to map it to a `.test` domain.
2. **Mail Traps**: Configure `.env` to redirect outgoing emails to local port `1025` using SMTP.
3. **Database Scaffolding**: Setup an empty local SQLite or Postgres database and run `php artisan migrate --seed`.
4. **Secure Local SSL**: Run `herd secure` to enable HTTPS locally.

## Examples

### Good Example

```bash
# ✅ Set up HTTPS and test domains locally in seconds
cd /Users/user/Sites/my-project
herd link my-app
herd secure my-app
# Now accessible securely at https://my-app.test
```

### Bad Example

```bash
# ❌ Hardcoding server blocks manually inside /etc/hosts and /etc/nginx/nginx.conf
# This is brittle, non-standardized, and difficult for other team members to reproduce.
```
---
