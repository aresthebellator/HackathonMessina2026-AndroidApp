# AI Agent Safety Guidelines

## Purpose

Help AI coding assistants work safely inside real developer repositories.

## Rules

- Inspect existing conventions before editing.
- Preserve unrelated user changes.
- Never expose secrets from `.env` or local config.
- Do not run destructive commands without explicit approval.
- Do not invent framework APIs.
- Run or honestly report verification.
- Prefer small, reversible changes.

## Anti-Patterns

- Claiming tests passed without running them.
- Rewriting unrelated files.
- Installing dependencies without approval.
- Ignoring dirty worktrees.
