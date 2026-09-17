# Dirty Worktree Safety Guidelines

## Purpose

Protect user work when repositories already contain changes.

## Rules

- Check status before editing, committing, or tagging.
- Do not revert changes you did not make unless explicitly asked.
- If user changes conflict with the task, pause and ask.
- Commit only related files.
- Avoid destructive Git commands.

## Anti-Patterns

- `git reset --hard` without explicit approval.
- Broad commits that include unrelated work.
- Amending commits without being asked.
