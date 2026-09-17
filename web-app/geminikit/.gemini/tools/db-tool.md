# Tool: db-tool

## Purpose
Database access wrapper. Executes SQL queries against a configured database
with read-only enforcement by default. Requires explicit approval for writes.

## Requires Approval
- Read operations (`SELECT`, `EXPLAIN`, `DESCRIBE`, `SHOW`): no approval required
- Write operations (`INSERT`, `UPDATE`, `DELETE`, `DROP`, `ALTER`, `CREATE`): yes — explicit `confirmed: true` flag required

## Input

```json
{
  "query": "string (required) — SQL query to execute",
  "database": "string (required) — target database name or connection alias",
  "params": "object (optional) — parameterized query values { key: value }",
  "read_only": "boolean (optional, default: true) — enforce read-only connection",
  "confirmed": "boolean (optional, default: false) — required true for write ops",
  "timeout_ms": "integer (optional, default: 5000) — query timeout in ms, max 30000"
}
```

## Output

```json
{
  "status": "ok | error",
  "rows": "array — result rows (empty array for non-SELECT queries)",
  "row_count": "integer — number of rows returned or affected",
  "columns": "array — column names for SELECT results",
  "error": "string | null — error message if status is error",
  "duration_ms": "integer — query execution time"
}
```

## Safety Constraints

| Rule | Detail |
|------|--------|
| Read-only default | Connection opened read-only unless `read_only: false` explicitly set |
| Write confirmation | Write ops require `confirmed: true`; blocked otherwise |
| No credentials in logs | Connection string credentials redacted in `post-tool` hook |
| Input sanitization | Always use parameterized queries (`params`); never interpolate user input |
| Timeout enforcement | Hard cap at 30 000 ms; queries exceeding limit are cancelled |
| No DDL in production | `DROP`, `TRUNCATE`, `ALTER` blocked when `database` ends with `-prod` or `_production` |

## Allowed Query Patterns

```sql
-- Read (no approval needed)
SELECT * FROM users WHERE id = :id
EXPLAIN SELECT ...
SHOW TABLES
DESCRIBE orders

-- Write (confirmed: true required)
INSERT INTO logs (event, ts) VALUES (:event, :ts)
UPDATE settings SET value = :v WHERE key = :k
```

## Blocked Patterns (always)
- `DROP TABLE`, `TRUNCATE TABLE` without explicit `confirmed: true`
- `DELETE FROM <table>` without a `WHERE` clause
- Raw string interpolation (use `params` instead)

## Example Call

```json
{
  "query": "SELECT id, email FROM users WHERE created_at > :since LIMIT 10",
  "database": "app-dev",
  "params": { "since": "2026-01-01" },
  "read_only": true,
  "timeout_ms": 3000
}
```

## Connection Aliases
Defined in `.gemini/settings.json` under `tools.db.connections`.
Use alias names (e.g., `app-dev`, `app-staging`) — never raw connection strings in query calls.
