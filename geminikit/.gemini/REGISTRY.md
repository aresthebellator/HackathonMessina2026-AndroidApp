# Registry

This file is automatically managed by `sync-registry.js`. ALWAYS reference this for command structures and skill paths.

---

## Command Recognition

<!-- GK_COMMAND_TABLE_START -->
| Command | Agent | Skills | Description |
|---------|-------|--------|-------------|
| `/gk-a11y [--audit \| --fix \| --report] <args>` | reviewer | a11y | Audit UI components for WCAG 2.2 AA compliance and generate actionable accessibility fixes |
| `/gk-analytics [--setup \| --events \| --audit] <args>` | developer | analytics | Setup product analytics integration and define event tracking schemas for user behavior measurement |
| `/gk-analyze [--deep \| --security \| --perf \| --onboard] <args>` | reviewer | analyze | Analyze code or system structure and report findings on complexity, dependencies, and risks. |
| `/gk-ask [--deep \| --quick] <args>` | (self) | ask | Expert assistant for answering technical and general questions with grounded context. |
| `/gk-audit [--deps \| --static \| --license] <args>` | security | audit | Audit dependencies and static code for security vulnerabilities and license compliance |
| `/gk-auth [--jwt \| --oauth \| --session \| --rbac] <args>` | developer | auth | Implement authentication and authorization with JWT, OAuth2, or session management. Use when adding login, OAuth providers, session handling, or Better Auth integration. |
| `/gk-backend [--rest \| --graphql \| --middleware \| --validate] <args>` | developer | backend | Build Node.js/Python backend APIs with REST or GraphQL. Use when implementing API endpoints, middleware, authentication, or server-side logic. |
| `/gk-brainstorm` | researcher | brainstorm | Software solution brainstorming, architectural evaluation, and technical decision debating. |
| `/gk-fix-bug (or "agent-only") [--verify \| --deep] <args>` | developer | bug-fixer | Identify root cause from error logs and generate a verified code fix with regression tests. |
| `/gk-cms [--setup \| --schema \| --query] <args>` | developer | cms | Integrate headless CMS (Sanity, Strapi, Payload), define content schemas, and generate typed data fetching queries |
| `/gk-coding-level [--set \| --get \| --explain] <args>` | developer | coding-level | Adapt response depth and style to user's coding experience level. Persists in memory to ensure consistent interaction quality. |
| `/gk-compare-logic [--deep \| --quick] <args>` | comparator | compare-logic | Compares business logic between a legacy system and a new, migrated system by analyzing their source code. |
| `/gk-context-engineering [--audit \| --compress \| --archive] <args>` | developer | context-engineering | Monitor and optimize context usage. Use to audit token count, implement compression strategies, and manage long-running session context. |
| `/gk-database [--design \| --query \| --optimize \| --index] <args>` | developer | database | Design schemas, write queries, and optimize performance for PostgreSQL and MongoDB. Use when creating database models, writing complex queries, or improving DB performance. |
| `/gk-debug [--trace \| --deep] <args>` | support | debug | Identify root cause of a software error and recommend a precise fix. |
| `/gk-deploy [--staging \| --production \| --dry-run] <args>` | devops | deploy | Execute build and deployment pipelines to various environments |
| `/gk-docs-seeker [--latest \| --example \| --compare] <args>` | researcher | docs-seeker | Fetch and summarize external documentation for any library, API, or framework. Use to keep project knowledge updated without leaving the terminal. |
| `agent-only (documenter)` | documenter | document | Generate accurate technical documentation from provided code content and context. |
| `/gk-email [--setup \| --template \| --test] <args>` | developer | email | Setup transactional email system with templates, queue integration, and delivery monitoring |
| `/gk-export-session` | developer | export-session | Exports the current session state and conversation summary for continuation in a new session. |
| `/gk-feature-flags [--setup \| --create \| --rollout] <args>` | developer | feature-flags | Setup feature flag system, create flags, and manage gradual rollouts and A/B experiments |
| `/gk-frontend [--component \| --page \| --api-route \| --optimize] <args>` | developer | frontend | Build React/Next.js/TypeScript frontends with modern patterns. Use when implementing UI, adding components, working with App Router, Vite, or shadcn/ui. |
| `agent-only (developer) [--dry-run] <args>` | developer | git | Execute git operations: commit, branch, status, PR prep, and conflict detection. |
| `` | developer | gk-execute | Execute Markdown-based implementation plans by parsing, executing tasks, and updating status. |
| `/gk-health-check` | maintenance | health-check | Validate framework compliance across all agents and skills. |
| `/gk-i18n [--setup \| --extract \| --validate] <args>` | developer | i18n | Setup internationalization, extract hardcoded strings, and validate translation completeness |
| `/gk-infra [--docker \| --k8s \| --terraform] <args>` | devops | infra | Manage infrastructure as code (Docker, K8s, Terraform configurations) |
| `/gk-intake [--refine \| --spec \| --interview] <args>` | researcher | intake | Capture, structure, and refine initial user requirements or project ideas. |
| `/gk-journal [--today \| --feature \| --bug \| --lesson] <args>` | developer | journal | Write technical journal entries about session work, changes made, and lessons learned. Use after completing features, fixing bugs, or finishing implementation sessions. |
| `/gk-llms [--full \| --docs-only \| --code-only] <args>` | documenter | llms | Generate llms.txt and llms-full.txt from codebase and docs. Use to prepare project context for AI consumption or external documentation tools. |
| `/gk-mcp-manager [--list \| --add \| --remove \| --test \| --scaffold] <args>` | mcp-manager | mcp-manager | Manage MCP server configuration, test connections, and scaffold new servers. Use this skill to add/edit/remove MCP servers or to troubleshoot connectivity. |
| `/gk-media [--video \| --audio \| --image \| --thumbnail] <args>` | developer | media | Process video, audio, and images with FFmpeg and ImageMagick. Use when converting media formats, applying filters, generating thumbnails, or processing uploads. |
| `/gk-migrate [--generate \| --apply \| --rollback] <args>` | maintenance | migrate | Manage database schema changes and data migrations |
| `/gk-mobile [--rn \| --flutter \| --native \| --navigation] <args>` | developer | mobile | Build mobile apps with React Native (Expo) or Flutter. Use when implementing native features, platform-specific code, navigation, or app state management. |
| `/gk-monitor [--logs \| --metrics \| --alerts] <args>` | support | monitor | Analyze system logs and monitor performance metrics to detect anomalies |
| `/gk-observability [--setup \| --audit \| --configure] <args>` | devops | observability | Setup and audit observability infrastructure — error tracking, APM, and structured logging |
| `/gk-performance [--audit \| --bundle \| --vitals] <args>` | reviewer | performance | Audit Core Web Vitals, bundle size, and runtime performance — then recommend actionable optimizations |
| `/gk-plan [--fast \| --deep \| --parallel \| --from <path> \| --dry-run \| --phase <id>] <args>` | planner | plan | Break down a complex task into structured, executable subtasks with dependencies and effort estimates. |
| `/gk-preview [--explain \| --diagram \| --ascii \| --slides] <args>` | documenter | preview | Generate visual explanations of code, architecture, or logic. Use to visualize complex systems, generate diagrams, or create slide-style presentations. |
| `agent-only (developer, git workflow) [--strict \| --quick] <args>` | developer | quality-gate | Enforce quality gate before git commit: run tests and code review, block if score < 7 or tests fail. |
| `/gk-refactor [--pattern \| --modernize \| --cleanup] <args>` | maintenance | refactor | Improve code structure and maintainability without changing external behavior |
| `agent-only (planner, orchestrator)` | planner | research | Gather, compare, and synthesize technical options into a structured recommendation report. |
| `/gk-retro [--sprint \| --weekly \| --project] <args>` | planner | retro | Run sprint retrospectives with git metrics analysis. Use when reviewing sprint/week performance, identifying patterns, or generating retrospective reports. |
| `/gk-review [--strict \| --quick \| --api \| --api-generate \| --api-validate \| --api-serve \| --security \| --perf \| --post-fix \| --post-implement \| --post-test \| --post-refactor] <args>` | reviewer | review | Comprehensive review of code quality, API design, security, and performance. Includes OpenAPI 3.1 spec generation, validation, and UI serving. |
| `/gk-security-scan [--owasp \| --secrets \| --sast \| --report] <args>` | security | security-scan | Scan code for OWASP Top 10 vulnerabilities, secrets exposure, and SAST issues. Use when auditing code security, checking for injection flaws, or detecting leaked credentials. |
| `/gk-sequential-thinking [--depth \| --assumption \| --refine] <args>` | architect | sequential-thinking | Structured step-by-step analysis for complex problems. Use when thinking through logic, analyzing interdependencies, or solving ambiguous technical challenges. |
| `/gk-create [--skill \| --agent] <args>` | developer | skill-creator | Generate agent and skill files following Gemini Kit templates. Use when creating a new skill or /gk-* command. Use for building agent definitions or extending Gemini Kit. |
| `agent-only (developer)` | developer | sql | Optimize a SQL query for performance while preserving its logical result. |
| `agent-only (orchestrator)` | (self) | summarize | Compress context into structured memory using project-specific templates (execution, long-term, short-term). |
| `/gk-team [--spawn \| --coordinate \| --parallel \| --merge] <args>` | developer | team | Orchestrate multi-agent teams for parallel development. Use when spawning parallel agents, setting up file ownership boundaries, coordinating complex multi-session workflows. |
| `/gk-design [--spec \| --review \| --gen \| --auto] <args>` | designer | design | Generate precise visual component specs or review implemented UI for design quality and accessibility compliance. |
| `/gk-verify (or "agent-only")` | developer | verify | Execute Python code in sandbox to verify fixes, test logic, or validate transformations. |
| `/gk-watzup [--summary \| --plan] <args>` | developer | watzup | Review recent changes and wrap up the current work session. Use when finishing a coding session, summarizing work done, or planning next steps. |
| `/gk-web-testing [--e2e \| --unit \| --perf \| --coverage] <args>` | tester | web-testing | Write and run Playwright E2E, Vitest unit, and k6 performance tests. Use when adding test coverage, writing E2E scenarios, or load testing APIs. |

<!-- GK_COMMAND_TABLE_END -->

---

## Skill Registry

<!-- GK_SKILL_REGISTRY_START -->
| Skill | File | Modes | Use for |
|-------|------|-------|---------|
| a11y | `.gemini/skills/a11y/SKILL.md` | — | Audit UI components for WCAG 2.2 AA compliance and generate actionable accessibility fixes |
| analytics | `.gemini/skills/analytics/SKILL.md` | — | Setup product analytics integration and define event tracking schemas for user behavior measurement |
| analyze | `.gemini/skills/analyze/SKILL.md` | — | Analyze code or system structure and report findings on complexity, dependencies, and risks. |
| ask | `.gemini/skills/ask/SKILL.md` | — | Expert assistant for answering technical and general questions with grounded context. |
| audit | `.gemini/skills/audit/SKILL.md` | — | Audit dependencies and static code for security vulnerabilities and license compliance |
| auth | `.gemini/skills/auth/SKILL.md` | — | Implement authentication and authorization with JWT, OAuth2, or session management. Use when adding login, OAuth providers, session handling, or Better Auth integration. |
| backend | `.gemini/skills/backend/SKILL.md` | — | Build Node.js/Python backend APIs with REST or GraphQL. Use when implementing API endpoints, middleware, authentication, or server-side logic. |
| brainstorm | `.gemini/skills/brainstorm/SKILL.md` | — | Software solution brainstorming, architectural evaluation, and technical decision debating. |
| bug-fixer | `.gemini/skills/bug-fixer/SKILL.md` | — | Identify root cause from error logs and generate a verified code fix with regression tests. |
| cms | `.gemini/skills/cms/SKILL.md` | — | Integrate headless CMS (Sanity, Strapi, Payload), define content schemas, and generate typed data fetching queries |
| coding-level | `.gemini/skills/coding-level/SKILL.md` | — | Adapt response depth and style to user's coding experience level. Persists in memory to ensure consistent interaction quality. |
| compare-logic | `.gemini/skills/compare-logic/SKILL.md` | — | Compares business logic between a legacy system and a new, migrated system by analyzing their source code. |
| context-engineering | `.gemini/skills/context-engineering/SKILL.md` | — | Monitor and optimize context usage. Use to audit token count, implement compression strategies, and manage long-running session context. |
| database | `.gemini/skills/database/SKILL.md` | — | Design schemas, write queries, and optimize performance for PostgreSQL and MongoDB. Use when creating database models, writing complex queries, or improving DB performance. |
| debug | `.gemini/skills/debug/SKILL.md` | — | Identify root cause of a software error and recommend a precise fix. |
| deploy | `.gemini/skills/deploy/SKILL.md` | — | Execute build and deployment pipelines to various environments |
| docs-seeker | `.gemini/skills/docs-seeker/SKILL.md` | — | Fetch and summarize external documentation for any library, API, or framework. Use to keep project knowledge updated without leaving the terminal. |
| document | `.gemini/skills/document/SKILL.md` | — | Generate accurate technical documentation from provided code content and context. |
| email | `.gemini/skills/email/SKILL.md` | — | Setup transactional email system with templates, queue integration, and delivery monitoring |
| export-session | `.gemini/skills/export-session/SKILL.md` | — | Exports the current session state and conversation summary for continuation in a new session. |
| feature-flags | `.gemini/skills/feature-flags/SKILL.md` | — | Setup feature flag system, create flags, and manage gradual rollouts and A/B experiments |
| frontend | `.gemini/skills/frontend/SKILL.md` | — | Build React/Next.js/TypeScript frontends with modern patterns. Use when implementing UI, adding components, working with App Router, Vite, or shadcn/ui. |
| git | `.gemini/skills/git/SKILL.md` | — | Execute git operations: commit, branch, status, PR prep, and conflict detection. |
| gk-execute | `.gemini/skills/gk-execute/SKILL.md` | — | Execute Markdown-based implementation plans by parsing, executing tasks, and updating status. |
| health-check | `.gemini/skills/health-check/SKILL.md` | — | Validate framework compliance across all agents and skills. |
| i18n | `.gemini/skills/i18n/SKILL.md` | — | Setup internationalization, extract hardcoded strings, and validate translation completeness |
| infra | `.gemini/skills/infra/SKILL.md` | — | Manage infrastructure as code (Docker, K8s, Terraform configurations) |
| intake | `.gemini/skills/intake/SKILL.md` | — | Capture, structure, and refine initial user requirements or project ideas. |
| journal | `.gemini/skills/journal/SKILL.md` | — | Write technical journal entries about session work, changes made, and lessons learned. Use after completing features, fixing bugs, or finishing implementation sessions. |
| llms | `.gemini/skills/llms/SKILL.md` | — | Generate llms.txt and llms-full.txt from codebase and docs. Use to prepare project context for AI consumption or external documentation tools. |
| mcp-manager | `.gemini/skills/mcp-manager/SKILL.md` | — | Manage MCP server configuration, test connections, and scaffold new servers. Use this skill to add/edit/remove MCP servers or to troubleshoot connectivity. |
| media | `.gemini/skills/media/SKILL.md` | — | Process video, audio, and images with FFmpeg and ImageMagick. Use when converting media formats, applying filters, generating thumbnails, or processing uploads. |
| migrate | `.gemini/skills/migrate/SKILL.md` | — | Manage database schema changes and data migrations |
| mobile | `.gemini/skills/mobile/SKILL.md` | — | Build mobile apps with React Native (Expo) or Flutter. Use when implementing native features, platform-specific code, navigation, or app state management. |
| monitor | `.gemini/skills/monitor/SKILL.md` | — | Analyze system logs and monitor performance metrics to detect anomalies |
| observability | `.gemini/skills/observability/SKILL.md` | — | Setup and audit observability infrastructure — error tracking, APM, and structured logging |
| performance | `.gemini/skills/performance/SKILL.md` | — | Audit Core Web Vitals, bundle size, and runtime performance — then recommend actionable optimizations |
| plan | `.gemini/skills/plan/SKILL.md` | — | Break down a complex task into structured, executable subtasks with dependencies and effort estimates. |
| preview | `.gemini/skills/preview/SKILL.md` | — | Generate visual explanations of code, architecture, or logic. Use to visualize complex systems, generate diagrams, or create slide-style presentations. |
| quality-gate | `.gemini/skills/quality-gate/SKILL.md` | — | Enforce quality gate before git commit: run tests and code review, block if score < 7 or tests fail. |
| refactor | `.gemini/skills/refactor/SKILL.md` | — | Improve code structure and maintainability without changing external behavior |
| research | `.gemini/skills/research/SKILL.md` | — | Gather, compare, and synthesize technical options into a structured recommendation report. |
| retro | `.gemini/skills/retro/SKILL.md` | — | Run sprint retrospectives with git metrics analysis. Use when reviewing sprint/week performance, identifying patterns, or generating retrospective reports. |
| review | `.gemini/skills/review/SKILL.md` | — | Comprehensive review of code quality, API design, security, and performance. Includes OpenAPI 3.1 spec generation, validation, and UI serving. |
| security-scan | `.gemini/skills/security-scan/SKILL.md` | — | Scan code for OWASP Top 10 vulnerabilities, secrets exposure, and SAST issues. Use when auditing code security, checking for injection flaws, or detecting leaked credentials. |
| sequential-thinking | `.gemini/skills/sequential-thinking/SKILL.md` | — | Structured step-by-step analysis for complex problems. Use when thinking through logic, analyzing interdependencies, or solving ambiguous technical challenges. |
| skill-creator | `.gemini/skills/skill-creator/SKILL.md` | — | Generate agent and skill files following Gemini Kit templates. Use when creating a new skill or /gk-* command. Use for building agent definitions or extending Gemini Kit. |
| sql | `.gemini/skills/sql/SKILL.md` | — | Optimize a SQL query for performance while preserving its logical result. |
| summarize | `.gemini/skills/summarize/SKILL.md` | — | Compress context into structured memory using project-specific templates (execution, long-term, short-term). |
| team | `.gemini/skills/team/SKILL.md` | — | Orchestrate multi-agent teams for parallel development. Use when spawning parallel agents, setting up file ownership boundaries, coordinating complex multi-session workflows. |
| design | `.gemini/skills/design/SKILL.md` | — | Generate precise visual component specs or review implemented UI for design quality and accessibility compliance. |
| verify | `.gemini/skills/verify/SKILL.md` | — | Execute Python code in sandbox to verify fixes, test logic, or validate transformations. |
| watzup | `.gemini/skills/watzup/SKILL.md` | — | Review recent changes and wrap up the current work session. Use when finishing a coding session, summarizing work done, or planning next steps. |
| web-testing | `.gemini/skills/web-testing/SKILL.md` | — | Write and run Playwright E2E, Vitest unit, and k6 performance tests. Use when adding test coverage, writing E2E scenarios, or load testing APIs. |

<!-- GK_SKILL_REGISTRY_END -->
