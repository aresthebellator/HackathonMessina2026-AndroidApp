---
name: review-open-source-package
description: "Use when reviewing a Laravel or PHP package for public release readiness."
license: MIT
metadata:
  author: Ali Harb
---

# Review Open Source Package

## Workflow

1. Inspect Composer metadata, namespace, service provider, README, license, changelog, tests, and CI.
2. Check installability in a fresh Laravel app.
3. Verify examples, configuration, and release notes.
4. Review security posture and data handling.
5. Report blocking issues before release.

## Verification

- Package tests, static analysis, and Composer validation pass.
