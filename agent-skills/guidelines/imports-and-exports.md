# Imports And Exports Guidelines

## Purpose

Handle data import/export workflows safely at scale.

## Rules

- Validate imported rows and report row-level errors.
- Queue large imports and exports.
- Use chunking to avoid memory spikes.
- Authorize access to import/export actions.
- Redact sensitive export columns unless explicitly required.

## Anti-Patterns

- Loading large files entirely into memory.
- Exporting private data by default.
- Applying partial imports without clear error reporting.
