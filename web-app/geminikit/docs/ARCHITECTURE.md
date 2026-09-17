# System Architecture - Gemini Kit

## Overview
Gemini Kit is a multi-agent AI development framework designed to run inside the Gemini CLI. It optimizes token usage and standardizes software engineering workflows through the use of specialized agents, atomic skills, and strict rule enforcement.

## System Architecture Diagram

```
[Layer 1] ENTRY POINT
User Input (/gk-...) ──► .gemini/GEMINI.md
                           │
                           ▼
                  Parse & Complexity Check
                           │
                           ▼
[Layer 2]      ┌──────────────────────────────┐
AGENTS         │         ORCHESTRATOR         │
(Role)         └───────────┬──────────────────┘
                           │ Route
           ┌──────┬────────┼──────────┬──────────┬─────────┐
           ▼      ▼        ▼          ▼          ▼         ▼
      Planner  Developer  Reviewer   Researcher  Designer  ... (etc.)
          │        │         │          │           │
          │        │         │          │           │
[Layer 3] ▼        ▼         ▼          ▼           ▼
SKILLS  plan     debug     review     onboard       ui
        research git       analyze    brainstorm
                 sql
                 ...
          │        │         │          │           │
          └────────┴─────────┴──────────┴───────────┘
                           │
                           ▼
[Layer 4] TOOLS   Files • Shell • Search • Database • API
                           │
                           ▼
[Layer 5] MEMORY  Short-Term • Execution State • Long-Term
```

The system is built on a 5-layer architecture, ensuring separation of concerns and scalability:
1.  **Entry Point:** `GEMINI.md` - Responsible for parsing commands, checking complexity, and routing to the appropriate agent.
2.  **Agents:** Specialized executors with defined roles (e.g., `planner`, `developer`). See the [Agent Registry](AGENTS_REGISTRY.md) for a complete list.
3.  **Skills:** Atomic, stateless processors that perform specific tasks (e.g., `gk-debug`, `gk-plan`).
4.  **Tools:** External I/O interfaces for interacting with the environment.
5.  **Memory System**: State management for short-term and long-term context, featuring auto-persistence, silent summarization, and self-healing.

## Core Components

### 1. Orchestrator
The central brain of the system. Its primary responsibilities are:
- **Parsing**: Detecting commands and user intent from `/gk-...` inputs.
- **Decomposition**: Breaking down complex requests into manageable subtasks.
- **Delegation**: Selecting the right Agent for each task based on the [Agent Registry](AGENTS_REGISTRY.md).
- **Aggregation**: Summarizing results from various agents into a cohesive response.
- **Security Control**: Enforcing the Agent Permission Matrix and tool access limits.

### 2. Agents
Specialized entities with defined roles and rules (located in `.gemini/agents/`). They are the "who" in the system, responsible for executing tasks within their domain. Key agents include:
- **Planner**: Creates execution roadmaps for complex tasks.
- **Developer**: Implements code, fixes bugs, and manages Git.
- **Reviewer**: Analyzes code quality, security, and performance.
- **Researcher**: Gathers information and performs brainstorming.
- **Documenter**: Generates technical documentation.
- **Designer**: Produces and validates UI/UX specifications.

A complete list of agents and their skills is available in the [Agent Registry](AGENTS_REGISTRY.md).

### 3. Skills (v2.0.0)
Atomic, reusable units of functionality (located in `.gemini/skills/`). Skills are the "what" — they follow a strict Input/Output schema and perform one specific job (e.g., `gk-summarize`, `gk-analyze`, `gk-git`). They are the building blocks that agents use to accomplish their goals.

**Skill Inventory (55 total):**
- **Core Skills (15)**: plan, research, debug, review, git, sql, deploy, infra, audit, analyze, brainstorm, document, export-session, summarize, skill-creator
- **Specialized Skills (40)**: frontend, backend, mobile, auth, database, cms, i18n, email, analytics, media, feature-flags, a11y, performance, coding-level, compare-logic, context-engineering, docs-seeker, health-check, llms, mcp-manager, migrate, monitor, observability, preview, quality-gate, refactor, security-scan, sequential-thinking, team, watzup, web-testing, and others

All skills as of Phase 05 (2026-04-26) include:
- **Tools section**: Explicit Gemini CLI tools with when-to-use guidance
- **Output sections**: Plain-text handoff contract (status, artifacts, summary, confidence) per `04_output.md`
- **Error Recovery**: Tabular error handling strategies
- **Gemini-Specific Optimizations**: Long context reading, google_search integration, code execution verification

## Memory System

The Gemini Kit Memory System provides robust state management through:
- **Auto-Persistence**: Automatic state saving across sessions to ensure continuity.
- **Silent Summarization**: Background compression of long context windows to maintain model performance without user interruption.
- **Implicit Export**: Automated backup of critical session data for recovery.
- **File Locking**: Prevents concurrent modification conflicts during multi-agent execution.
- **Memory Rotation/Archiving**: Intelligently manages long-term storage and retrieval of past session insights.
- **Self-Healing**: Automatic detection and recovery from corrupted state files.
- **Pinned Knowledge**: Persistent facts and user preferences that bypass rotation logic.

## Native Hooks System

Gemini CLI v0.26.0+ supports native hooks (`.gemini/settings.json` hook registration) that enable automatic state management without human intervention. Gemini Kit implements five lifecycle hooks via Node.js scripts in `.gemini/hooks/`:

### Hook Events & Responsibilities

| Event | Script | Trigger | Responsibility |
|-------|--------|---------|-----------------|
| **SessionStart** | `session-start.js` | Session initialization | Load pinned context and last 3 long-term memory entries into short-term context |
| **AfterModel** | `after-model.js` | Model response received | Check token threshold; auto-summarize short-term to long-term if >25K tokens or every 10 turns; compress long-term if >15 entries |
| **PreCompress** | `pre-compress.js` | Before Gemini CLI prunes history | Snapshot short-term context to long-term to prevent loss during context compression |
| **AfterTool** | `after-tool.js` | After tool execution | Log tool name, status (success/error), and duration to execution.md; redact sensitive fields; detect write operations and trigger post-write processing (phase scaffolding, skill registry sync, report indexing) |
| **SessionEnd** | `session-end.js` | Session termination | Final long-term memory compression if overloaded; reset short-term memory |

**Note:** BeforeAgent hook (`before-agent-rules-inject.js`) is injected by the orchestrator layer, not registered in settings.json. It detects active skill via `.gemini/.skill-state.json` and injects skill-specific context (templates, paths, registry state) before standard rules injection, reducing boilerplate and token usage.

### Hook Infrastructure

**Library Modules** (`.gemini/hooks/lib/`):
- `memory-manager.js`: CRUD operations for `.gemini/memory/*.md` files (read, write, append, trim entries)
- `gemini-summarizer.js`: Gemini API wrapper with token threshold logic and conversation summarization
- `logger.js`: Silent error logger writing to `.gemini/errors.log` (debug mode via `LOG_LEVEL=debug`)
- `skill-state-manager.js`: Read/write `.gemini/.skill-state.json` for hook-skill communication (session tracking, context injection)
- `post-write-processor.js`: Post-write operations including phase file scaffolding, skill registry sync, and report indexing

**Configuration** (`.gemini/settings.json`):
```json
"hooks": {
  "SessionStart": [{ "hooks": [{ "type": "command", "command": "node .gemini/hooks/session-start.js", "timeout": 5000 }] }],
  "AfterModel": [{ "hooks": [{ "type": "command", "command": "node .gemini/hooks/after-model.js", "timeout": 30000 }] }],
  "PreCompress": [{ "hooks": [{ "type": "command", "command": "node .gemini/hooks/pre-compress.js", "timeout": 10000 }] }],
  "AfterTool": [{ "hooks": [{ "type": "command", "command": "node .gemini/hooks/after-tool.js", "timeout": 5000 }] }],
  "SessionEnd": [{ "hooks": [{ "type": "command", "command": "node .gemini/hooks/session-end.js", "timeout": 10000 }] }]
}
```

### Memory Files

Three persistent memory files in `.gemini/memory/`:
- `short-term.md`: Session-specific context, populated at SessionStart, appended during AfterModel, cleared at SessionEnd
- `long-term.md`: Cross-session knowledge, auto-populated by AfterModel summaries and compressed when >15 entries
- `execution.md`: Tool invocation audit trail logged by AfterTool (name, status, duration, timestamps)
- `pinned.md`: Immutable system instructions and context that persist across all sessions

### Token Thresholds & Auto-Summarization

- **TOKENS_THRESHOLD**: 25,000 — trigger summarization when cumulative tokens reach this limit
- **TURNS_INTERVAL**: 10 — also summarize every 10 model turns regardless of token count
- **MAX_LONG_TERM_ENTRIES**: 15 — compress long-term memory entries when count exceeds this threshold

All summarization uses `gemini-2.0-flash` model via `@google/generative-ai` package for fast, cost-effective compression.

## Security Framework

Security is integrated at the core through:
- **Agent Permission Matrix**: A granular access control table defining which agents can access specific tools and file paths.
- **Forbidden Paths (Blacklist)**: Built-in protection for sensitive directories and files (e.g., `.env`, `.git`, `.ssh`).
- **Tool Access Control**: Strict limits on shell command execution, file system modifications, and network requests.
- **Pre-emptive Secret Masking**: Automated identification and redaction of credentials and secrets before they are processed by the LLM.

## Context Economy

Context Economy is a core system principle focused on token optimization:
- **Token Efficiency**: Every tool and skill is optimized to minimize input/output tokens.
- **Context Rotation**: Older or irrelevant context is automatically moved to long-term memory or archived to keep the active window focused.
- **Surgical Reads**: Tools prefer reading specific line ranges or symbols over entire files to reduce overhead.

## Operational Workflow


Gemini Kit follows a mandatory four-phase lifecycle for every task:

1.  **Research**: Mapping the codebase, validating assumptions, and reproducing issues.
2.  **Strategy**: Formulating a plan. For complex tasks, this involves the **Planner** agent.
3.  **Execution**: Performing the actual work (Plan -> Act -> Validate).
4.  **Validation**: Mandatory final step to ensure correctness and prevent regressions.

### Complex Task Workflow
For multi-step or ambiguous tasks (e.g., "build a login system"), the framework follows a specific planning and execution cycle:

```
    User Request (Complex/Ambiguous)
                 │
                 ▼
          [ 1. PLAN ] ───► Planner Agent
                 │            │ Decompose Task
                 │            ▼
                 │       Execution Plan (DAG)
                 │       [Phase 1] → [Phase 2] → [Phase 3]
                 │
                 ▼
          [ 2. EXECUTE ] ◄── Loop through Phases ──┐
                 │                                 │
      ┌──────────┴──────────┐                      │
      ▼                     ▼                      │
  Developer             Reviewer                   │
(Implement)             (Audit)                    │
      │                     │                      │
      └──────────┬──────────┘                      │
                 ▼                                 │
          Update Memory/Context ───────────────────┘
                 │
                 ▼
          [ 3. VALIDATE ]
                 │
                 ▼
            Final Output
```

## Communication Protocol
- **Structured I/O**: Agents and Skills communicate using JSON schemas defined in `.gemini/schemas/`.
- **Handoffs**: Data is passed between agents using a standard handoff envelope (from_agent, to_agent, artifacts, context).
- **Memory**: System state is persisted in `.gemini/memory/` (execution.md, long-term.md).

## Safety & Governance
- **Confirmation Gate**: Any plan modifying the system requires explicit user approval.
- **Global Rules**: Core principles like **YAGNI**, **KISS**, and **DRY** are enforced via `.gemini/system.md` and `.gemini/rules/`.

## Rules & Standards
The framework operates under strict global rules:
-   **Core:** Foundational principles and safety (`01_core.md`).
-   **Workflow:** Orchestration and execution logic (`02_workflow.md`).
-   **Resource:** Token and context optimization (`03_resource.md`).
-   **Output:** Communication protocols (`04_output.md`).
-   **Development:** Coding standards and task execution (`05_development.md`).
-   **Documentation:** Document management rules (`06_documentation.md`).
