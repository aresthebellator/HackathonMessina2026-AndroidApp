---
name: write-release-notes
description: "Use when writing concise, copy-ready Markdown release notes from changelog entries and commits."
license: MIT
metadata:
  author: Ali Harb
---

# Write Release Notes

## Workflow

1. Inspect the changelog, commits since the previous tag, and notable docs changes.
2. Group notes into Added, Improved, Fixed, Changed, and Validation.
3. Explain user-facing value before implementation details.
4. Mention breaking changes and migration steps clearly.
5. Keep notes copy-ready for GitHub Releases.

## Verification

- Version matches the tag.
- Notes do not claim unverified CI success.

## Common Mistakes

- Listing internal file changes without explaining user value.
