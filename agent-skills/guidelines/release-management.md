# Release Management Guidelines

## Purpose

Release packages predictably with clean changelogs, tags, CI, and copy-ready notes.

## Rules

- Use semantic versioning.
- Move completed changes from `Unreleased` into the released version with a date.
- Tag the exact commit that contains release metadata.
- Push both the branch and tag.
- Verify GitHub Actions after pushing.

## Anti-Patterns

- Tagging before updating the changelog.
- Forgetting to push tags.
- Publishing release notes that claim unverified test status.

## Verification

- `git tag --points-at HEAD`
- Remote tag exists.
- CI is green for the release commit or tag.
