---
name: filament-shield
description: "Use when installing, configuring, generating, or reviewing Filament Shield authorization for Filament v5 panels."
license: MIT
metadata:
  author: Ali Harb
---

# Filament Shield

## When To Use

Use this skill when the app needs Filament panel authorization, permission generation, role seeding, super-admin handling, or policy synchronization for Filament resources, pages, and widgets.

## Inputs Needed

- Panel ID, auth provider model, tenant support, permission naming, role strategy, super-admin strategy, seeder strategy, and tests.

## Workflow

1. Inspect the current Filament panel structure and auth provider model.
2. Ensure the auth provider model uses `Spatie\Permission\Traits\HasRoles`.
3. Run Shield setup for the panel.
4. Generate permissions and policies for the panel entities.
5. Publish and customize Shield config only where needed.
6. Generate or update the Shield seeder to capture roles and permissions declaratively.
7. Generate translation files for permission labels when localization is needed.
8. Keep resource and page permissions in sync with panel entities.
9. Add tests for authorization, generated policy behavior, and seed content.

## Files That May Be Created

- `config/filament-shield.php`
- `database/seeders/*Shield*Seeder.php`
- `app/Policies/*Policy.php`
- `lang/*` or `lang/admin/*` translation files
- `app/Models/*` auth provider model updates
- Filament resource/page/widget policy or permission support classes

## Files That May Be Modified

- `app/Models/User.php`
- `app/Providers/Filament/*PanelProvider.php`
- Shield config, seeders, policies, tests, and translations

## Architecture Rules

- Shield is the default authorization layer for Filament panels in this app.
- Policies and permissions must be generated and kept synchronized.
- Panel resources, pages, and widgets should not bypass the permission model.
- Super-admin or gate interception should be centralized and explicit.
- Prefer generated permissions and seeders over hand-maintained role wiring.

## Testing Requirements

- Test role assignment, permission generation, policy enforcement, and super-admin access.
- Test the seeder output or the data it inserts.
- Test any custom permission key composition or localization behavior.

## Security Requirements

- Deny by default.
- Do not expose protected resources in Filament without Shield or equivalent policy coverage.
- Keep the auth provider model and permission sync in lockstep.

## Review Checklist

- Is Shield configured for the active Filament panel?
- Does the auth provider model use `HasRoles`?
- Are permissions generated and seeded, not hand-waved?
- Is localization in app-owned translation files?
- Are policy and permission names consistent with panel entities?

## Common Mistakes

- Leaving panel resources unprotected.
- Relying on manual policy wiring while Shield is installed.
- Forgetting to regenerate permissions after adding new resources or pages.
