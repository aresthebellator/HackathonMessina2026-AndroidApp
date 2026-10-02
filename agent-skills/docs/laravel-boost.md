# Laravel Boost

Agent Skills ships Boost resources at:

```text
resources/boost/guidelines/core.blade.php
resources/boost/skills/*
```

Install with Composer and Laravel Boost:

```bash
composer require alizharb/agent-skills --dev
php artisan boost:install
```

The Boost guidance tells AI agents how to:

- Choose the right skill.
- Inspect installed versions.
- Follow Laravel-native conventions.
- Preserve dirty worktrees.
- Avoid secret exposure.
- Verify code before completion.
