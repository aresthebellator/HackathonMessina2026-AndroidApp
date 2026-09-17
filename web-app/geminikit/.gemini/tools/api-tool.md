# Tool: api-tool

## Purpose
HTTP API request wrapper. Executes outbound HTTP calls with auth header injection,
response parsing, and credential protection. Read requests need no approval;
write requests require confirmation.

## Requires Approval
- `GET`, `HEAD`, `OPTIONS`: no approval required
- `POST`, `PUT`, `PATCH`, `DELETE`: yes — explicit `confirmed: true` flag required

## Input

```json
{
  "method": "string (required) — GET | POST | PUT | PATCH | DELETE | HEAD",
  "url": "string (required) — full URL including protocol",
  "headers": "object (optional) — request headers { key: value }",
  "body": "object | string (optional) — request body (auto-serialized to JSON if object)",
  "auth_alias": "string (optional) — named auth config from settings.json tools.api.auth",
  "timeout_ms": "integer (optional, default: 10000) — max 30000",
  "confirmed": "boolean (optional, default: false) — required true for write methods"
}
```

## Output

```json
{
  "status": "ok | error",
  "http_status": "integer — HTTP response status code",
  "body": "object | string — parsed response body",
  "headers": "object — response headers",
  "duration_ms": "integer — total request time including DNS",
  "error": "string | null — error message if status is error"
}
```

## Safety Constraints

| Rule | Detail |
|------|--------|
| Domain allowlist | Requests only to domains listed in `settings.json tools.api.allowed_domains` |
| No credential logging | Auth headers and tokens redacted to `[REDACTED]` in all logs |
| Write confirmation | POST/PUT/PATCH/DELETE require `confirmed: true` |
| Timeout hard cap | 30 000 ms maximum; requests exceeding limit are aborted |
| No localhost by default | `localhost`, `127.0.0.1`, `0.0.0.0` blocked unless `allow_local: true` in settings |
| Redirect limit | Follow max 5 redirects; abort on redirect loop |

## Auth Aliases
Defined in `.gemini/settings.json` under `tools.api.auth`. Example aliases:
- `github` → injects `Authorization: Bearer <GITHUB_TOKEN>` from env
- `internal` → injects service account header from env

Never put raw tokens in `headers` field — use `auth_alias` instead.

## Allowed Domains (example)
```json
["api.github.com", "api.openai.com", "hooks.slack.com"]
```
Configure in `.gemini/settings.json`. Wildcard subdomains supported: `*.example.com`.

## Example Calls

```json
{
  "method": "GET",
  "url": "https://api.github.com/repos/owner/repo/issues",
  "auth_alias": "github",
  "timeout_ms": 5000
}
```

```json
{
  "method": "POST",
  "url": "https://hooks.slack.com/services/T00/B00/xxx",
  "body": { "text": "Build passed" },
  "confirmed": true,
  "timeout_ms": 5000
}
```

## Error Handling
- Network timeout → `{ status: "error", error: "Request timed out after <n>ms" }`
- Domain not in allowlist → `{ status: "error", error: "Domain not allowed: <domain>" }`
- HTTP 4xx/5xx → returned as `ok` with `http_status` set; agent interprets code
