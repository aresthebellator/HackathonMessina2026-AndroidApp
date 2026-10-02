# Skills Guide

Skills are atomic units of capability that Agents use to perform specific tasks. Each skill is designed to do one thing well.

## Core Skills

### Analysis & Review
- **gk-analyze**: Deep analysis of code structure, dependencies, and performance/security risks.
- **gk-audit**: Audits dependencies and static code for security vulnerabilities and license compliance.
- **gk-compare-logic**: Compares business logic between two systems (legacy vs. new).
- **gk-review**: Comprehensive code review with scoring and actionable feedback.
- **gk-design**: Generates UI specifications and validates implementation against design.

### Development & Maintenance
- **gk-bug-fixer**: Automatically analyzes error logs to identify root causes and generates verified fixes.
- **gk-debug**: Diagnoses errors and suggests fixes (supports trace mode).
- **gk-git**: Handles version control operations (commit, branch, status).
- **gk-health-check**: Validate framework compliance across all agents and skills.
- **gk-migrate**: Manages database schema changes and data migrations.
- **gk-refactor**: Improves code structure and maintainability without changing external behavior.
- **gk-skill-creator**: Scaffolds new Agent and Skill files.
- **gk-sql**: Optimizes SQL queries for performance.

### Infrastructure & Operations
- **gk-bridge-task-runner**: Execute a bridge pipeline task and signal completion.
- **gk-deploy**: Executes build and deployment pipelines to various environments.
- **gk-infra**: Manages infrastructure as code (Docker, K8s, Terraform).
- **gk-mcp-manager**: Manages MCP server configurations and connections.
- **gk-monitor**: Analyzes system logs and monitors performance metrics.

### Planning & Research
- **gk-ask**: General Q&A assistant with codebase context.
- **gk-brainstorm**: Facilitates architectural debates and decision making.
- **gk-intake**: Capture, structure, and refine initial user requirements or project ideas.
- **gk-onboard**: Summarizes project context for rapid onboarding.
- **gk-plan**: Decomposes complex tasks into execution phases.
- **gk-research**: Gathers technical information and synthesizes options.

### Documentation & Utility
- **gk-document**: Generates technical documentation from code.
- **gk-export-session**: Exports current session state.
- **gk-summarize**: Compresses conversation history and outputs.

## Skill Structure (v2.0.0)

Each skill is defined by a `SKILL.md` file containing:
- **Tools**: Explicit list of Gemini CLI tools with when-to-use guidance
- **Role**: The persona assumed by the skill
- **Objective**: The specific goal of the skill
- **Input Schema**: The JSON structure required to invoke the skill
- **Process**: Step-by-step execution logic
- **Output**: Plain-text handoff contract (status, artifacts, summary, confidence) per `04_output.md`
- **Error Recovery**: Table of error codes, causes, and recovery strategies
- **Gemini-Specific Optimizations**: How the skill leverages Gemini's native capabilities (long context, google_search, code execution)

## Skill Inventory

The framework includes 55 skills across multiple categories:
- **Core Skills (15)**: Essential agents (plan, debug, review, research, etc.)
- **Specialized Skills (40)**: Domain-specific capabilities (frontend, backend, mobile, auth, database, i18n, media, analytics, and more)

## Version 2.0.0 Quality Baseline

All skills follow v2.0.0 structure with the following enhancements:

### Mandatory Sections (All Skills)
1. **Tools** — Explicit list of Gemini CLI tools (`google_search`, `read_file`, `write_file`, `run_code`, `run_shell_command`)
2. **Output** — Plain-text handoff contract with fields: `status` (ready|blocked|partial|completed|failed), `artifacts` (files created/modified/deleted), `summary` (one sentence), `confidence` (high|medium|low), plus domain-specific content per skill role
3. **Error Recovery** — Table format with Error, Cause, and Recovery columns
4. **Gemini-Specific Optimizations** — Sections for long context (1M tokens), google_search usage, and code execution verification

### Key Improvements by Skill Category

**Research & Planning**
- `research`: Mandatory ≥3 google_search queries per task; citation requirement for all external information
- `plan`: Reads entire codebase via long context before generating execution phases
- `ask`: google_search mandatory for external/library questions; improved information grounding

**Development & Quality**
- `bug-fixer`: Code execution verification for all fixes; enhanced error log analysis
- `deploy`: Auto-detects deployment platform (Cloudflare, Vercel, Netlify, Docker, Fly.io, Railway, GCP, GitHub Actions)
- `review`: Real-time CVE checking via google_search; multiple review modes (--strict, --quick, --api, --security, --perf, --openapi)
- `git`: Content-pattern secret scanning before commit; clean output format

**Infrastructure & Auditing**
- `audit`: Real-time CVE enrichment via google_search; dependency security verification
- `debug`: Code execution for reproducing issues; trace mode support
- `refactor`: Long context analysis of entire codebase; impact assessment

**Support Skills**
- `brainstorm`, `analyze`, `document`, `intake`, `gk-execute`: All leverage Gemini-native capabilities per their domain

## Skill Lifecycle Management

Skills follow a strict lifecycle for state and discovery:

### Skill State Tracking
- Skills may write `.gemini/.skill-state.json` at startup to signal active skill and session context
- Hook system reads this file to inject skill-specific templates, paths, and registry state
- 2-hour TTL prevents stale state persistence
- Optional feature — hooks degrade gracefully if not written

### Skill Discovery & Registration
- All skills stored in `.gemini/skills/<skill-name>/SKILL.md`
- Registry auto-synced via `after-tool.js` hook on file writes
- Update `.gemini/REGISTRY.md` and `.gemini/AGENT.md` automatically
- Validation pipeline: SKILL.md → schema check → benchmark (min 70/100) → package (.zip)

### Skill Versions
- Skills use semantic versioning (major.minor.patch)
- v2.0.0 is the current baseline for all skills (as of 2026-04-26)
- Breaking changes trigger version bumps in CHANGELOG.md

## Skill Development Guidelines

All new skills must adhere to the following framework-level requirements:

### 1. Security Audit
- **Path Validation**: Skills must validate all input paths against the framework's blacklist before execution.
- **Secret Redaction**: Skills that handle sensitive output (logs, env files) must apply pre-emptive masking before returning data to the Agent.
- **Tool Scoping**: Use the most restrictive tool subset possible (e.g., prefer `read_file` over `run_shell_command` with `cat`).

### 2. Context Economy
- **Surgical I/O**: Design inputs and outputs to be as compact as possible. Return specific data points rather than large unstructured blocks.
- **Token Budgeting**: Avoid redundant summaries or verbose process descriptions in the skill's output.
- **Incremental Processing**: For large files, skills should support pagination or range-based reading to avoid context overflow.

### 3. State Management
- **Statelessness**: Skills should be stateless. Any persistent data should be handled via the framework's Memory System (Layer 5).
- **Skill State Convention** (Optional): Skills may write `.gemini/.skill-state.json` at startup to signal which skill is active. This enables hook-based context injection (templates, active plan paths, registry state) that reduces boilerplate and token usage. Schema: `{ skill: "gk-plan", session_id, timestamp, outputDir?, slug? }`. Gracefully ignored if not written — hooks degrade to standard behavior.

## Usage
Skills are typically invoked by Agents, not directly by users (except via `/gk-` commands which wrap agents).
