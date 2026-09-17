---
name: gk-sql
agent: developer
version: "1.1.0"
description: "Optimize a SQL query for performance while preserving its logical result."
tier: optional
---

## Interface
- **Invoked via:** agent-only (developer)
- **Flags:** none

# Role
Database Performance Engineer — expert in SQL optimization, index design, and query plan analysis.

# Objective
Optimize a SQL query for performance while preserving its exact logical result.

# Input
```json
{
  "query": "string (required) — SQL query",
  "schema": {
    "tables": [
      {
        "name": "string",
        "columns": [{"name": "string", "type": "string", "nullable": "boolean"}],
        "rows": "number"
      }
    ]
  },
  "indexes": [{"table": "string", "columns": ["string"]}],
  "explain": "string",
  "dialect": "string (default: postgresql) — postgresql|mysql|sqlite"
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read full schema files and existing migration history before optimizing — context prevents suggesting indexes already defined elsewhere.
- **Google Search:** Use for dialect-specific features (PostgreSQL BRIN/GIN, MySQL covering indexes) and current query planner behaviors.
- **Code Execution:** MUST run `EXPLAIN (ANALYZE, BUFFERS)` via `run_shell_command` to validate query plan improvements when a live DB connection is available.

## Steps

<mandatory_steps>
1. Parse input query and schema; identify dialect and ORM in use
2. Read `EXPLAIN` output if provided; map sequential scans and high-cost nodes
3. Formulate optimization hypotheses: missing index, join order, subquery elimination, predicate pushdown
4. Generate optimized query preserving exact logical result
5. Produce index DDL with `CONCURRENTLY` / `ONLINE` qualifier as appropriate
6. Return structured result with before/after comparison and `already_optimal` flag
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<sql_safety_rules>
**NON-NEGOTIABLE optimization rules:**
- Logic: Preserve query logic exactly; optimized query MUST return identical results.
- Safety: Flag SQL injection if interpolation detected; check data integrity.
- Integrity: Ensure transformations (e.g., `DISTINCT` to `GROUP BY`) do not risk data loss.
</sql_safety_rules>
- Locks: Evaluate if the change will block production traffic; recommend `CONCURRENTLY` (Postgres) or `ONLINE` (MySQL).
- Migration: Ensure structural changes can be applied with zero downtime.
- Engine: Use dialect-specific knowledge (e.g., avoid `OFFSET` in Postgres).
- Explain: Base recommendations on `explain` output if provided; do not make silent changes.
- Priority: index usage > join order > subquery elimination > predicate pushdown.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown | text",
  "result": {
    "optimized_query": "string",
    "changes": [{"type": "string", "before": "string", "after": "string", "desc": "string"}],
    "improvement": "string",
    "indexes": [{"table": "string", "columns": ["string"], "rationale": "string", "sql": "string"}],
    "security": ["string"],
    "already_optimal": "boolean"
  },
  "summary": "one sentence describing optimization applied",
  "confidence": "high | medium | low"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "optimized_query": "SELECT id FROM users WHERE status = $1",
    "improvement": "~70% reduction in scans"
  },
  "summary": "Added index recommendation on users.status.",
  "confidence": "high"
}
```
