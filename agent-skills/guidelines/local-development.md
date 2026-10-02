# Local Development Guidelines

## Purpose

Define the standards for setting up, running, and isolating local development environments using Laravel Herd, Mailpit, and local databases to prevent local configurations from leaking into production.

## Required Standards

- **Prefer Laravel Herd**: Use Laravel Herd as the default development environment on macOS/Windows. Ensure that PHP versions are locked locally to match the target production engine.
- **Enforce Local Site Isolation**: Avoid running multiple sites on the same port or server block. Use Herd's `herd link` or Valet paths to map clean `.test` local domains (e.g. `http://my-app.test`).
- **Never Send Real Mail in Local**: Intercept all outbound application mail using local Mailpit servers (`mailpit` driver on port `1025`). Ensure `.env` is configured to prevent real mail from leaking to clients during development tests.
- **Local DB Segregation**: Use isolated databases for local testing and development. Databases must have matching structures but must never import raw production passwords or user PII without sanitization.

## Allowed Patterns

```
# Example local .env setup
DB_CONNECTION=sqlite # or mysql/pgsql with local credentials
DB_DATABASE=/absolute/path/to/database.sqlite

MAIL_MAILER=smtp
MAIL_HOST=127.0.0.1
MAIL_PORT=1025
MAIL_USERNAME=null
MAIL_PASSWORD=null
```

## Forbidden Patterns

- Inputting production databases or credentials directly inside the local `.env` configuration.
- Sending transactional marketing emails to real customer addresses while running the application locally.
- Relying on manual local Nginx configuration blocks when Herd or Valet is already supported.
