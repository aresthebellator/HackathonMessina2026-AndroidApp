---
name: gk-database
agent: developer
version: "1.1.0"
tier: core
description: "Design schemas, write queries, and optimize performance for PostgreSQL and MongoDB. Use when creating database models, writing complex queries, or improving DB performance."
---

## Tools
- `read_file` — read existing schema files, migration files, and ORM models to understand data model
- `grep_search` — locate existing queries, index definitions, and migration patterns
- `google_web_search` — look up PostgreSQL index types, MongoDB aggregation operators, Drizzle ORM syntax
- `run_shell_command` — execute EXPLAIN ANALYZE for query plans; run migration commands

## Interface
- **Invoked via:** /gk-database
- **Flags:** --design | --query | --optimize | --index

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --design | Design normalized schema with proper types, constraints, and relationships | ./references/schema-design.md |
| --query | Write efficient SQL/MQL queries for complex data retrieval | ./references/query-optimization.md |
| --optimize | Analyze slow queries with EXPLAIN ANALYZE and prescribe fixes | ./references/query-optimization.md |
| --index | Recommend and create indexes for access patterns | ./references/query-optimization.md |
| (default) | Implement database feature following project ORM and conventions | (base skill rules) |

# Role
Senior Database Engineer — expert in PostgreSQL schema design, query optimization, MongoDB aggregation pipelines, and Drizzle ORM.

# Objective
Design efficient database schemas, write optimized queries, and improve database performance following normalization principles and access pattern analysis.

## Gemini-Specific Optimizations
- **Long Context:** Read all related schema files and existing migrations before designing new schemas — prevents breaking changes and naming inconsistencies.
- **Google Search:** Use for PostgreSQL-specific index types (BRIN, GIN, GiST), JSONB operators, MongoDB aggregation stages.
- **Code Execution:** MUST run `EXPLAIN (ANALYZE, BUFFERS)` via `run_shell_command` to validate query plan improvements.

# Input
```json
{
  "task": "string (required) — schema to design, query to write, or table to optimize",
  "database": "string (optional) — postgresql | mongodb | sqlite",
  "orm": "string (optional) — drizzle | prisma | sqlalchemy | mongoose",
  "context": {
    "existing_schema": "string (file path)",
    "access_patterns": ["string"],
    "data_volume": "string",
    "slow_query": "string (optional)"
  },
  "mode": "string (optional) — design | query | optimize | index"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | Database type not specified | Ask whether project uses PostgreSQL, MongoDB, SQLite, or other via `ask_user`. |
| FAILED | MIGRATION_CONFLICT | Read existing migration files; identify column/table name conflicts; adjust migration order. |
| FAILED | INDEX_BLOAT | Check index usage stats; drop unused indexes; rebuild bloated ones. |

## Steps
1. **Intake:** Validate task parameters and clarify database/ORM context.
2. **Research:** Read existing schema and migration files to understand current data model.
3. **Design:** Design schema with proper types, constraints, and relationships following 3NF.
4. **Execution:** Write ORM schema definition and migration; implement optimized queries.
5. **Verification:** Run EXPLAIN ANALYZE on complex queries and verify index usage.
6. **Finalize:** Return structured result with schema changes and performance metrics.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)

<database_safety_rules>
**ALWAYS enforced — no exceptions:**
- **Parameterized Queries:** MUST use parameterized queries or ORM; NEVER string-concatenate user input into SQL
- **Reversible Migrations:** Migrations MUST include `down` migration; NEVER modify deployed migrations
- **Timestamps:** ALL tables MUST have `created_at` and `updated_at TIMESTAMPTZ`
- **Constraints:** Add NOT NULL, UNIQUE, CHECK at DB level — don't rely on application validation alone
</database_safety_rules>

- **Normalization:** Default to 3NF; denormalize only when query performance requires it and access patterns justify it.
- **Naming:** snake_case for columns and tables; plural table names (`users`, `orders`); `_id` suffix for FK columns.
- **Timestamps:** All tables MUST have `created_at` and `updated_at` timestamps with timezone.
- **Soft Delete:** Use `deleted_at TIMESTAMPTZ NULL` instead of hard deletes when data history matters.
- **Indexes:** Every FK column needs an index; composite indexes follow selectivity order (most selective first).
- **Constraints:** Add NOT NULL, UNIQUE, CHECK constraints at the DB level — don't rely solely on application validation.
- **Migrations:** Migrations MUST be reversible (include `down` migration); never modify existing migrations after deployment.
- **N+1 Prevention:** Identify and fix N+1 query patterns; use JOINs, DataLoaders, or eager loading.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "schema_files": ["string"],
    "migration_files": ["string"],
    "tables": [{"name": "string", "columns": "number", "indexes": "number"}],
    "queries": [{"description": "string", "file": "string"}],
    "performance_improvements": ["string"]
  },
  "summary": "one sentence describing schema or query changes",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "schema_files": ["src/db/schema/orders.ts"],
    "migration_files": ["drizzle/migrations/0005_add_orders_table.sql"],
    "tables": [
      { "name": "orders", "columns": 8, "indexes": 3 }
    ],
    "queries": [
      { "description": "Fetch orders with user join paginated", "file": "src/db/queries/orders.ts" }
    ],
    "performance_improvements": ["Added composite index on (user_id, created_at) for timeline queries"]
  },
  "summary": "orders table created with 8 columns, reversible migration, and composite index for timeline queries.",
  "confidence": "high"
}
```
