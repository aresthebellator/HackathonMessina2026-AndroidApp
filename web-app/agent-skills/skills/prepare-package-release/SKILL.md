---
name: prepare-package-release
description: "Use when preparing a package release with changelog, semantic versioning, validation, tags, and GitHub release notes."
license: MIT
metadata:
  author: Ali Harb
---

# Prepare Package Release

## When To Use

Use this skill before tagging a package version.

## Workflow

1. Inspect the latest tags, changelog, composer metadata, and branch status.
2. Confirm the next version follows semantic versioning.
3. Run formatting, tests, static analysis, and package-specific validators.
4. Move relevant changelog entries from `Unreleased` into the release version and date.
5. Commit release metadata separately when appropriate.
6. Create an annotated or lightweight tag matching the repository convention.
7. Push `main` and the tag.
8. Check GitHub Actions for the release commit and tag.
9. Draft release notes from the changelog.

## Verification

- Confirm `git tag --points-at HEAD` includes the release tag.
- Confirm remote tags include the new tag.
- Confirm CI is green.

## Common Mistakes

- Tagging before updating the changelog.
- Tagging the wrong commit.
- Forgetting to push the tag.
