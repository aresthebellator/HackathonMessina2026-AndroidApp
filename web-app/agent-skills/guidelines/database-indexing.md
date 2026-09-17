# Database Indexing Guidelines

## Purpose

Keep growing Laravel databases fast and consistent.

## Rules

- Add indexes for common filters, joins, foreign keys, and unique business constraints.
- Use composite indexes that match query order.
- Avoid indexing every column blindly.
- Document large-table migration risks.
- Test query behavior when performance is critical.

## Anti-Patterns

- Adding filters without indexes.
- Relying only on application validation for uniqueness.
- Running locking migrations on large tables without a plan.
