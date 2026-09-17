# Database Migrations & Schema Design Guidelines

## Purpose

Maintain optimal query performance, strict data integrity, and secure design patterns across all PostgreSQL/MySQL database tables.

## Required Standards

- **Enforce Foreign Key Constraints**: All relation columns must define explicit foreign key constraints using `constrained()`. Set clear deletion behaviors (e.g., `cascadeOnDelete()`, `nullOnDelete()`).
- **Define Database Indexes**: Add database indexes to foreign keys and columns frequently used in query filters, where clauses, or ordering constraints (e.g., `status`, `published_at`).
- **Public vs. Internal Identifiers**: Use auto-incrementing integer IDs internally for performance, but expose UUIDs or ULIDs to clients/public APIs to prevent ID enumeration.
- **Accurate Money/Currency Types**: Never use `float` or `double` for currency. Always use `decimal` with explicit precision (e.g., `decimal('amount', 12, 2)`).
- **JSON Column Validation**: Use `json` columns for dynamic schema data, but enforce structural checks or cast them to Spatie Data objects in code.
- **No Data Seeding in Migrations**: Migration files must only modify schema structures. Data seeding or backfilling belongs in dedicated Seeder classes or CLI commands.

## Allowed Patterns

```php
// ✅ GOOD: Proper indexes, foreign constraints, and UUID fields
Schema::create('orders', function (Blueprint $table) {
    $table->id();
    $table->uuid('uuid')->unique();
    $table->foreignId('user_id')->constrained()->cascadeOnDelete();
    $table->string('status')->index();
    $table->decimal('total_amount', 12, 2);
    $table->timestamps();
});
```

## Forbidden Patterns

- Omitting foreign key constraints or relying on application-level integrity only.
- Storing currency values as floating-point numbers.
- Executing Eloquent model calls inside migration files (leads to crash states if models are refactored later).
