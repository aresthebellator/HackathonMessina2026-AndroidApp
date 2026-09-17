---
name: debug-composer-conflicts
description: "Use when Composer cannot install or update because of dependency conflicts, platform requirements, or security advisories."
license: MIT
metadata:
  author: Ali Harb
---

# Debug Composer Conflicts

## Workflow

1. Run `composer why-not` or inspect Composer's solver output.
2. Check PHP version, Laravel version, package constraints, minimum stability, and plugin allowances.
3. Confirm whether security advisories are blocking a version.
4. Prefer updating constraints honestly over forcing ignored advisories.
5. Update docs and CI matrix when support changes.
6. Re-run `composer update --dry-run` or the intended install/update command.

## Verification

- `composer validate` passes.
- The intended `composer require` or `composer update` succeeds.

## Common Mistakes

- Pinning an old vulnerable framework version.
- Adding broad constraints without testing each supported lane.
