# Package Development Guidelines

## Purpose

Build Laravel packages that are installable, documented, testable, and safe to adopt.

## Rules

- Use accurate Composer constraints and support only versions that are tested.
- Register package behavior through a service provider and Laravel auto-discovery.
- Publish config, views, assets, migrations, and docs only when the host app needs control.
- Keep package code independent from a specific application namespace.
- Add Testbench coverage for service providers, commands, config, and public APIs.

## Anti-Patterns

- Hardcoding application paths.
- Shipping stale README examples.
- Claiming support for framework versions that CI does not test.

## Verification

- `composer validate`
- `vendor/bin/pint --test`
- `vendor/bin/pest`
- `vendor/bin/phpstan analyse`
