---
name: create-laravel-package
description: "Use when creating a Laravel package with Composer metadata, service provider, config publishing, commands, tests, CI, and release docs."
license: MIT
metadata:
  author: Ali Harb
---

# Create Laravel Package

## When To Use

Use this skill when starting or converting a repository into a reusable Laravel package.

## Workflow

1. Inspect existing package conventions, composer metadata, namespaces, and service providers.
2. Add or update `composer.json` with author, license, autoloading, support links, keywords, and Laravel auto-discovery.
3. Create a service provider that registers config, commands, views, translations, assets, or migrations only when needed.
4. Add config publishing and avoid forcing application files into the host app.
5. Add Pest/Testbench tests for package boot, command registration, config publishing, and core behavior.
6. Add CI for Composer validation, Pint, PHPStan, and tests.
7. Document installation, usage, configuration, testing, and release process.

## Verification

- Run `composer validate`.
- Run `vendor/bin/pint --test`.
- Run `vendor/bin/pest`.
- Run `vendor/bin/phpstan analyse`.

## Common Mistakes

- Hardcoding app paths instead of publishing configuration.
- Missing Laravel auto-discovery.
- Shipping examples that do not match supported Laravel versions.
