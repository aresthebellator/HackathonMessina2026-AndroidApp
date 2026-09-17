# Agent Verification Guidelines

## Purpose

Ensure AI agents prove work before reporting completion.

## Rules

- Run the smallest relevant check first.
- Run broader checks before final delivery when practical.
- Report skipped checks and why.
- Include command outcomes, not hidden assumptions.
- Verify remote pushes, tags, and CI when release work is requested.

## Anti-Patterns

- Saying “should work” after code changes.
- Ignoring failed checks because the main task appears done.
