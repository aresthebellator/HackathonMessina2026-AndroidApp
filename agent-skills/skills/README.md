# Skills

This directory contains the publishable Agent Skills library.

## Inventory

- 62 skills total.
- Every skill lives in its own folder.
- Every skill uses `SKILL.md`.
- The generated inventory is stored in `manifest.json`.

## Categories

### Architecture And Refactoring

- `create-action`
- `create-service`
- `create-dto`
- `create-request`
- `create-policy`
- `create-architecture-test`
- `decompose-model`
- `refactor-business-logic`
- `create-eloquent-builder`
- `create-database-migration`
- `design-multi-tenant-feature`

### Laravel Feature Work

- `create-api-endpoint`
- `create-domain-feature`
- `create-event-listener`
- `create-job`
- `create-console-command`
- `create-php-attribute`
- `create-api-integration`
- `implement-webhooks`
- `implement-import-export`
- `implement-notifications`
- `implement-permissions`
- `implement-api-rate-limiting`
- `implement-observability`
- `schedule-artisan-task`

### UI And Admin

- `create-livewire-component`
- `design-blade-component`
- `manage-frontend-styling`
- `review-tailwind-flux`
- `review-accessibility`
- `filament-development`
- `reuse-filament-resources`
- `filament-shield`
- `create-inertia-page`

### Package And Release Work

- `create-laravel-package`
- `create-laravel-boost-guidelines`
- `prepare-package-release`
- `write-release-notes`
- `review-package-readme`
- `review-open-source-package`
- `fix-github-actions`
- `debug-composer-conflicts`
- `audit-package-version`

### Quality, Debugging, And Review

- `write-tests`
- `write-pest-test`
- `review-feature`
- `review-security`
- `review-performance`
- `review-static-analysis`
- `review-ai-generated-code`
- `debug-failing-tests`
- `debug-phpstan`
- `format-codebase-pint`

### Operations And Platform

- `deploying-laravel-cloud`
- `configure-secure-storage`
- `configure-local-environment`
- `implement-subscription-billing`
- `laravel-pulse-monitoring`
- `laravel-pennant-feature-flags`
- `laravel-reverb-websockets`
- `manage-localization`
- `audit-ai-agent-instructions`

## Maintenance

After adding or renaming skills, regenerate and validate the manifest:

```bash
php scripts/generate-manifest.php
php scripts/validate-skills.php
```
