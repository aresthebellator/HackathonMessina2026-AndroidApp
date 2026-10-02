---
name: manage-localization
description: "Use when adding or restructuring Laravel translation files and localization keys."
license: MIT
metadata:
  author: Ali Harb
---

# Manage Localization

## When To Use

Use this skill for new translation keys, language file organization, admin copy, validation messages, and repeated user-facing text.

## Inputs Needed

- Feature area, language scope, reuse needs, and message ownership.

## Workflow

1. Inspect existing language file structure and naming.
2. Run `php artisan lang:publish` before adding app-owned language files if they do not already exist.
3. Place app-owned translation files under the project’s chosen app namespace, such as `lang/admin/**`.
4. Centralize repeated strings into shared keys where practical.
5. Keep validation messages close to the feature or contract they support.
6. Use localized labels in Blade, Filament, Livewire, Requests, and notifications.
7. Add tests when a translation key is part of a behavior contract.

## Files That May Be Created

- Translation files under `lang/`

## Files That May Be Modified

- Feature code that references translation keys, localization files, and tests.

## Architecture Rules

- Localization should support reusable, client-agnostic behavior.
- App copy belongs in app-owned language files, not Laravel core files.

## Testing Requirements

- Test the important keys and any behavior that depends on translated copy.

## Review Checklist

- Are strings centralized?
- Is the translation path feature-oriented?
- Are core language files left alone unless a real override is needed?

## Common Mistakes to Avoid

- Duplicating validation text in many files.
- Editing Laravel core language files for app copy.
