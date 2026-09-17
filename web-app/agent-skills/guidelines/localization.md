# Localization Guidelines

## Purpose

Keep user-facing text organized, translatable, reusable, and easy to audit.

## Required Standards

- Publish Laravel language files before adding application-specific localization.
- Keep app-owned UI text in app-owned language files, such as `lang/admin/**`.
- Localize labels, help text, tooltips, headings, notifications, validation messages, and error copy.
- Prefer shared translation keys when multiple files use the same wording.
- Keep translations close to the feature or bounded context they describe.
- Avoid hardcoded user-facing strings in source code when a translation key can express the same text.
- Use translation files rather than inline `@php` blocks for message assembly when possible.

## Allowed Patterns

- `__('group.key')`
- `trans_choice()`
- Local feature translation files.
- Shared message groups for repeated copy.

## Forbidden Patterns

- Hardcoded copy in controllers, Livewire, Filament, Blade, or validation rules when a translation key exists.
- Editing Laravel core language files unless a core override is genuinely required.
- Duplicating the same message text across many files.

## Naming Conventions

- Translation keys should be domain-first and feature-aware.
- Keep names predictable so keys can be reused across components.

## Testing Expectations

- Test important localization keys where user-facing behavior depends on them.
- Add regression coverage when copy or key structure matters.

## Review Checklist

- Are visible strings localized?
- Are validation and notification messages centralized where it helps?
- Are translation files organized by domain or feature?

## Acceptable Examples

- `__('admin.users.labels.name')`
- `__('admin.validation.required')`

## Unacceptable Examples

- `'Save changes'`
- `'You do not have permission.'`
