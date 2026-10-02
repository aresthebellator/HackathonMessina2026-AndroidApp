# Laravel Agent Skills

[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE.md)
[![Latest Version on Packagist](https://img.shields.io/packagist/v/alizharb/agent-skills.svg)](https://packagist.org/packages/alizharb/agent-skills)
[![Total Downloads](https://img.shields.io/packagist/dt/alizharb/agent-skills.svg)](https://packagist.org/packages/alizharb/agent-skills)
[![Laravel Boost](https://img.shields.io/badge/Laravel%20Boost-ready-FF2D20.svg)](resources/boost/guidelines/core.blade.php)
[![skills.sh](https://img.shields.io/badge/skills.sh-compatible-0070F3.svg)](https://www.skills.sh/alizharb/agent-skills)

Advanced Laravel AI agent skills and guidelines for Laravel Boost, skills.sh, Claude Code, Codex, Cursor, Windsurf, Copilot, and modern AI-assisted development.

This package gives coding assistants structured Laravel workflows, shared standards, safety rules, Boost-discoverable skills, and validation tooling so developers get better code with less prompting.

## Features

- 62 Laravel-focused AI skills.
- 44 reusable guidelines.
- Laravel Boost guideline and skills support.
- Boost-discoverable skills under `resources/boost/skills`.
- skills.sh-compatible `skills/` directory.
- Preset metadata for core, Filament, API, SaaS, package, security, testing, frontend, and devops workflows.
- Manifest and preset files for machine-readable discovery.
- Repository validation scripts.
- Package-ready Composer metadata, CI, docs, and release workflow.

## Installation

```bash
composer require alizharb/agent-skills --dev
```

Install through Laravel Boost:

```bash
php artisan boost:install
```

Select Agent Skills from Boost's available package list.

Install with skills.sh:

```bash
npx skills add AlizHarb/agent-skills
```

Keep the package up to date:

```bash
composer update alizharb/agent-skills
php artisan boost:update
```

## Presets

- `core`
- `full`
- `filament`
- `api`
- `saas`
- `package`
- `security`
- `testing`
- `frontend`
- `devops`

## Supported Tools

- Laravel Boost
- skills.sh
- Claude Code
- Codex
- Cursor
- Windsurf
- Copilot
- Generic `.agents/skills`

## Skill Categories

- Architecture and clean design
- Laravel core mechanics
- Filament development
- Livewire, Blade, Inertia, and frontend UI
- Security and authorization
- Testing and static analysis
- Performance and observability
- Package development and releases
- GitHub Actions and Composer debugging
- AI agent safety and instruction audits

## Documentation

- [Installation](docs/installation.md)
- [Laravel Boost](docs/laravel-boost.md)
- [skills.sh](docs/skills-sh.md)
- [Presets](docs/presets.md)
- [Validation](docs/validation.md)
- [Auditing](docs/auditing.md)
- [Security](docs/security.md)
- [Creating Skills](docs/creating-skills.md)
- [Creating Guidelines](docs/creating-guidelines.md)
- [Versioning](docs/versioning.md)
- [Codex](docs/codex.md)
- [Claude Code](docs/claude-code.md)
- [Cursor](docs/cursor.md)
- [Windsurf](docs/windsurf.md)
- [Copilot](docs/copilot.md)

## Quality Philosophy

Agent Skills is built around practical AI-agent safety:

- Inspect context before editing.
- Preserve dirty worktrees.
- Avoid destructive commands without approval.
- Never expose secrets.
- Verify changes before claiming completion.
- Prefer Laravel-native APIs and installed-version checks.

## Development

```bash
composer install
composer validate
vendor/bin/pint --test
vendor/bin/pest
vendor/bin/phpstan analyse
php scripts/sync-boost-skills.php
php scripts/generate-manifest.php
php scripts/validate-skills.php
```

## License

Laravel Agent Skills is open-source software licensed under the [MIT License](LICENSE.md).
