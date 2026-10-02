# Onboarding to Gemini Kit

Welcome to the Gemini Kit project! This guide provides a rapid introduction to the framework.

## 1. What is Gemini Kit?

Gemini Kit is a multi-agent AI framework that orchestrates specialized AI "Agents" (like Developers, Planners, and Reviewers) to autonomously yet safely complete complex software engineering tasks.

## 2. Core Concepts

- **Orchestrator**: The central brain that parses user commands, decomposes tasks, and delegates work to the appropriate agent.
- **Agents**: Specialized roles defined in `.gemini/agents/`. Each agent has a specific persona, set of skills, and responsibility. See the [Agent Registry](AGENTS_REGISTRY.md) for a full list.
- **Skills**: Atomic capabilities (e.g., `gk-git`, `gk-debug`, `gk-plan`) that agents use to perform actions. These are the building blocks of the system.
- **Registry**: The single source of truth for all commands and skills, located at `.gemini/REGISTRY.md`.

## 3. How to Get Started

1.  **Read the Architecture**: Start with the [System Architecture](ARCHITECTURE.md) document to understand how the components fit together.
2.  **Review the Commands**: Check the [Commands Reference](COMMANDS_REFERENCE.md) to learn how to interact with the system using `/gk-...` commands.
3.  **Try a Command**:
    - Use `/gk-ask "What is the purpose of the researcher agent?"` to query the system about itself.
    - Use `/gk-plan "Refactor the login logic to use a new authentication service"` to see how the Planner agent breaks down a task.

## 4. Key Files & Directories

### Core Framework
- **`.gemini/`**: The heart of the framework containing:
  - **`agents/`**: Agent role definitions (architect, developer, reviewer, etc.)
  - **`skills/`**: Atomic capability modules (gk-plan, gk-debug, gk-review, etc.)
  - **`rules/`**: Execution rules and standards (core, workflow, development, documentation)
  - **`hooks/`**: Lifecycle event handlers (session-start, after-model, pre-compress, after-tool, session-end)
  - **`memory/`**: State persistence files (short-term, long-term, execution, pinned)
  - **`settings.json`**: Hook registration and configuration
  - **`.skill-state.json`**: Active skill tracking for context injection

### Project Structure
- **`src/commands/`**: CLI command implementations (init, list, update, doctor, bridge, token)
- **`bridge/`**: Python orchestrator pipeline (task-schema, bridge-queue, bridge-dispatch, orchestrator, task-generator)
- **`docs/`**: Project documentation including this guide
- **`GEMINI.md`**: Root configuration and entry point for the system

## 5. Prerequisites

- **Node.js 18+**: Required for CLI commands and hooks
- **Python 3.10+**: Required for bridge orchestration scripts
- **Gemini CLI**: Must be installed and authenticated (latest version recommended)
- **Claude CLI**: Required for bridge review stage (optional for standard usage)

## 6. Hook System & Memory

The framework uses five lifecycle hooks for automatic state management:

| Event | Trigger | Function |
|-------|---------|----------|
| **SessionStart** | Session initialization | Load pinned context + last 3 long-term entries |
| **AfterModel** | Model response received | Auto-summarize if >25K tokens or 10 turns |
| **PreCompress** | Before context pruning | Snapshot short-term to long-term |
| **AfterTool** | After tool execution | Log tool calls and trigger post-write processing |
| **SessionEnd** | Session termination | Compress long-term memory and reset short-term |

Three persistent memory files manage state:
- **`short-term.md`**: Session-specific context (reset on SessionEnd)
- **`long-term.md`**: Cross-session knowledge (max 15 entries, auto-compressed)
- **`execution.md`**: Tool invocation audit trail with timestamps and status
- **`pinned.md`**: Immutable system instructions (persist across all sessions)

## 7. Security & Access Control

The Agent Permission Matrix defines tool access by role:
- **File Read**: Read source code and config
- **File Write**: Modify code, create tests, manage infrastructure
- **Shell Execute**: Run terminal commands
- **Network Access**: Call external APIs and search services
- **Secrets Access**: Read sensitive credentials
- **Bypass Blacklist**: Access protected paths (.env, .git, .ssh, etc.)

See [AGENTS_REGISTRY.md](AGENTS_REGISTRY.md) for complete permission matrix.
