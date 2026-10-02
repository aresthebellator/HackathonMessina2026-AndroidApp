---
name: create-console-command
description: "Use when creating or changing a Laravel console command and its prompts, output, signature, or supporting tests."
license: MIT
metadata:
  author: Ali Harb
---

# Create Console Command

## When To Use

Use this skill for Laravel Artisan commands, especially when the command should be discoverable, generator-backed, and friendly to interactive users.

## Inputs Needed

- Command purpose, signature, options, prompts, output, side effects, and tests.

## Workflow

1. Check whether an Artisan generator already exists before creating the file manually.
2. Use the appropriate `php artisan make:*` command when one exists.
3. Put reusable behavior in Actions, Services, Jobs, or Support classes.
4. Use `laravel/prompts` for interactive input, choice, and confirmation instead of ad hoc console IO.
5. Keep the command thin and typed.
6. Add tests for signature, output, and behavior.

## Files That May Be Created

- `app/Console/Commands/*`
- Supporting Actions, Services, DTOs, and tests.

## Files That May Be Modified

- Console commands, providers, support classes, config, translation files, and tests.

## Architecture Rules

- Commands orchestrate; reusable classes own business logic.
- Prefer dependency injection over `app()` in commands.
- Use Laravel Prompts for user interaction.
- Localize command output when it is user-facing and reusable.

## Testing Requirements

- Test command behavior, prompts, output, and side effects.

## Security Requirements

- Validate interactive input and avoid leaking secrets into output.

## Review Checklist

- Was a generator used if available?
- Is `laravel/prompts` used for interactive input?
- Is business logic extracted?
- Are outputs localized where appropriate?

## Common Mistakes

- Writing everything directly inside the command class.
- Using plain `ask()`/`choice()` patterns when Laravel Prompts gives better UX.
- Creating commands manually when an Artisan generator exists.
