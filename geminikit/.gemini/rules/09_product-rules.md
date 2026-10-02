# 09_PRODUCT: Product-Specific Development Rules

> **Usage:** At session start, read `.gemini/memory/pinned.md` for a `Project type: <type>` line under `## Project Context`.
> Apply ONLY the matching `[type]` section below. If no entry found, follow the detection
> protocol in `01_core.md § 6` to identify the type, confirm with user, and save to `pinned.md`.

---

## [mobile] Mobile Development Rules

**Applies to:** Flutter, React Native

### Stack Conventions
- **Flutter:** Riverpod (state), go_router (navigation), freezed (models), dio (HTTP)
- **React Native:** React Navigation, Zustand or Redux Toolkit, axios or fetch
- Use `const` constructors in Flutter wherever possible to reduce rebuilds
- Separate UI widgets from business logic — no API calls inside `build()`

### Architecture
- Feature-first folder structure: `lib/features/{feature}/`
- Repository pattern for data access; inject via providers/hooks
- Platform-specific code: isolate behind abstract interfaces
- **Secrets:** Never embed API keys in source — use build-time env injection (`--dart-define` for Flutter, `react-native-config` for RN) or a secrets vault

### Testing
- Flutter: widget tests + unit tests; use `flutter_test`, `mocktail`; golden file tests for critical UI components (`matchesGoldenFile`)
- RN: React Native Testing Library; mock native modules explicitly; snapshot tests or Storybook/Chromatic for visual regression

### Build & CI
- Define build flavors/schemes: `dev`, `staging`, `prod` with separate env configs
- Never hardcode API base URLs — use flavor-specific constants
- CI: run `flutter analyze` / `eslint` + tests before merge

### Performance
- Flutter: profile with DevTools; avoid `setState` on large trees; use `RepaintBoundary`
- RN: use `FlatList` over `ScrollView` for large lists; enable Hermes engine
- Lazy-load heavy screens; defer non-critical imports

---

## [web] Web Development Rules

**Applies to:** Next.js (App Router) — primary. Nuxt/Astro: apply general rules; consult framework docs for framework-specific patterns.

### Stack Conventions
- **Next.js:** App Router; prefer React Server Components (RSC); `use client` only when needed
- Styling: Tailwind CSS utility-first; shadcn/ui for UI components
- Data fetching: RSC for server data; SWR/React Query for client-side mutations
- State: Zustand for global state; avoid prop drilling beyond 2 levels

### Rendering Strategy (Next.js)
- Static pages → `generateStaticParams` + ISR (`revalidate`)
- Dynamic, personalized → SSR (`dynamic = 'force-dynamic'`)
- Real-time → client component + SWR/socket

### SEO & Metadata
- Use Next.js `metadata` export on every page; include `title`, `description`, `openGraph`
- Add `sitemap.ts` and `robots.ts` in `app/`
- Use semantic HTML: `<main>`, `<nav>`, `<article>`, `<section>`

### Performance Targets
- LCP < 2.5s | CLS < 0.1 | INP < 200ms
- Use `next/image` for all images; `next/font` for fonts
- Bundle budget: < 200KB initial JS (gzipped); audit with `@next/bundle-analyzer`
- Avoid importing entire libraries — tree-shake aggressively

### Testing
- Unit: Vitest + React Testing Library for components
- E2E: Playwright for critical user flows
- Accessibility: axe-core in CI

---

## [backend] Backend API Rules

**Applies to:** Node.js (Express/Fastify/Hono), Go (Gin/Chi), Python (FastAPI) — REST-first rules.  
**GraphQL/tRPC note:** Versioned routes and REST verbs do not apply; follow schema-first design, resolver patterns, and type-safe client generation instead.

### API Design
- Versioned routes: `/api/v1/resource`
- RESTful verbs: `GET` (read), `POST` (create), `PUT/PATCH` (update), `DELETE` (remove)
- Consistent error envelope: `{ "error": { "code": "RESOURCE_NOT_FOUND", "message": "...", "details": {} } }`
- Paginate list endpoints: `{ "data": [], "pagination": { "page", "limit", "total" } }`

### Validation & Security
- Validate ALL input at entry point: zod (TS), pydantic (Python), go-playground/validator (Go)
- Never trust raw request body — strip unknown fields
- Auth middleware on all protected routes; verify JWT signature + expiry
- Rate-limit public endpoints; stricter limits on auth routes
- Configure CORS explicitly — never use wildcard `*` in production for credentialed requests

### Database
- Use transactions for multi-step writes
- Index all foreign keys and frequently-filtered columns
- Never run migrations in app startup — use dedicated migration tool
- Soft-delete sensitive records; hard-delete only after retention period

### Reliability
- `GET /health` endpoint: returns `{ "status": "ok" }` (200 OK); keep minimal — no `uptime`/version on public route (information disclosure risk)
- Optional internal `/health/details` with `uptime`, `version` — protect behind internal network or auth
- Structured logging: include `requestId`, `userId`, `duration` on every log line
- Graceful shutdown: drain in-flight requests before exit
- Set timeouts on all outbound HTTP calls

### Testing
- Unit test business logic in isolation (mock DB/external services)
- Integration tests against real DB (use test containers or in-memory DB)
- Test auth flows: valid token, expired token, missing token, wrong scope

---

## [tool] CLI Tool / Script Rules

**Applies to:** Node.js CLI, Python scripts, Go CLI (Cobra), Bash scripts

### Arg Parsing
- Node.js: `commander` or `yargs`; Python: `click` or `argparse`; Go: `cobra`
- Always implement `--help` / `-h` (auto-handled by most frameworks)
- Provide `--version` flag returning semver string
- Detect CLI entry: `bin` field in `package.json` (Node), `console_scripts` in `pyproject.toml` (Python), `cmd/` dir (Go) — not shebang detection

### Standard Flags (implement consistently)
| Flag | Behavior |
|------|----------|
| `--dry-run` | Show what would happen, make no changes |
| `--verbose` / `-v` | Increase log verbosity |
| `--json` | Output machine-readable JSON to stdout |
| `--quiet` / `-q` | Suppress non-error output |

### Output Etiquette
- **stdout**: data output only (pipe-friendly)
- **stderr**: logs, progress, warnings, errors
- Never mix human text with JSON on stdout when `--json` is set
- Use spinner/progress only when stdout is a TTY (`process.stdout.isTTY`)

### Exit Codes
| Code | Meaning |
|------|---------|
| 0 | Success |
| 1 | General runtime error |
| 2 | Misuse / bad arguments |
| 130 | Interrupted (SIGINT) — must be set explicitly in Node.js via `process.on('SIGINT', () => process.exit(130))` |

### Cross-Platform
- Use `path.join()` / `filepath.Join()` / `os.path.join()` — never hardcode `/` or `\`
- Rules above apply to **generated code**; when running shell commands as a Gemini agent, follow `02_workflow.md § 4` shell rules
- Test on at least 2 platforms (Linux CI + macOS or Windows)

### Testing
- Unit test command handlers with mocked I/O
- Test `--dry-run` produces correct output with no side effects
- Test bad arg combinations produce exit code 2 + helpful message
