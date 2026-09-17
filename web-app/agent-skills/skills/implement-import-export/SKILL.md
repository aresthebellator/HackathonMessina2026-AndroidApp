---
name: implement-import-export
description: "Use when building CSV, Excel, or structured import/export workflows in Laravel or Filament."
license: MIT
metadata:
  author: Ali Harb
---

# Implement Import Export

## Workflow

1. Define data shape, permissions, file size limits, validation, and failure reporting.
2. Prefer queued imports/exports for large datasets.
3. Validate rows before writes and report row-level errors.
4. Use transactions per safe batch, not necessarily the whole file.
5. Redact sensitive data in exports when required.
6. Add tests for permissions, malformed files, invalid rows, and successful output.

## Verification

- Small and large file paths are tested.
- Unauthorized users cannot import or export protected data.

## Common Mistakes

- Loading huge files fully into memory.
- Exporting sensitive fields by default.
