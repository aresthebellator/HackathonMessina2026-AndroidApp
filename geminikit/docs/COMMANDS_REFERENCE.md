# Commands Reference

This guide details the commands available in Gemini Kit, including both the core CLI tools (`gk`) and the AI-driven agent commands (`/gk-...`).

## CLI Commands

These commands manage the Gemini Kit environment and are executed directly in your terminal.

| Command | Description |
|---------|-------------|
| `gk init` | Scaffolds the `.gemini/` directory and `GEMINI.md` configuration into the current project. Includes an interactive setup for Python environment and packages. |
| `gk list` | Lists all available agents and skills in the current project. |
| `gk update` | Updates the Gemini Kit framework to the latest version from GitHub. |
| `gk uninstall` | Removes the `.gemini/` directory and uninstalls the local runtime from the current project. |
| `gk doctor [--fix]` | Diagnose Gemini Kit setup issues (Node version, directories, hooks, memory files). Use `--fix` to auto-correct addressable problems. |
| `gk bridge init [--plan <path>]` | Create `.bridge/` directory structure and generate task queue from plan.md. Auto-detects plan if path omitted. |
| `gk bridge start` | Launch orchestrator and stream pipeline progress (pending → executing → gemini_done → reviewing → done). |
| `gk bridge status` | Display pipeline state summary and per-task status table. |
| `gk bridge reset [--failed-only]` | Reset tasks to pending for retry. Use `--failed-only` to reset only failed tasks (default: resets all). |
| `gk token gain [--history] [--json]` | Show token savings dashboard. `--history` shows per-session breakdown; `--json` outputs machine-readable format. |
| `gk token discover` | Analyze token usage patterns to identify missed optimization opportunities. |
| `gk token report` | Generate markdown token analytics report saved to `plans/reports/token-report-YYYYMMDD.md`. |

## AI Agent Commands

These commands invoke specialized AI agents to perform complex tasks.

**Syntax:**
```bash
/gk-<command> [--mode <value>] [arguments]
```

| Command | Agent | Description |
|---------|-------|-------------|
| `/gk-analyze` | `reviewer` | Analyze code or system structure for complexity, dependencies, and risks. |
| `/gk-ask` | `(self)` | Ask technical and general questions with grounded context. |
| `/gk-audit` | `security` | Audit dependencies and static code for security vulnerabilities and license compliance. |
| `/gk-brainstorm` | `researcher` | Brainstorm software solutions and architectural options. |
| `/gk-compare-logic` | `comparator` | Compare business logic between legacy and new systems. |
| `/gk-skill-creator` | `developer` | Generate new skill and agent files from templates. |
| `/gk-debug` | `developer` | Identify the root cause of a software error and recommend a fix. |
| `/gk-deploy` | `devops` | Execute build and deployment pipelines to various environments. |
| `/gk-design` | `designer` | Generate or review UI specifications. |
| `/gk-export-session` | `developer` | Exports the current session state and conversation summary for continuation. |
| `/gk-fix-bug` | `developer` | Automatically find and fix bugs from error logs. |
| `/gk-health-check` | `maintenance` | Validate framework compliance across all agents and skills. |
| `/gk-infra` | `devops` | Manage infrastructure as code (Docker, K8s, Terraform). |
| `/gk-intake` | `researcher` | Capture, structure, and refine initial user requirements or project ideas. |
| `/gk-mcp-manager` | `mcp-manager` | Manage MCP server configurations and connections. |
| `/gk-migrate` | `maintenance` | Manage database schema changes and data migrations. |
| `/gk-monitor` | `support` | Analyze system logs and monitor performance metrics. |
| `/gk-onboard` | `researcher` | Quickly grasp a new project's architecture and workflow. |
| `/gk-plan` | `planner` | Break down a complex task into an executable plan. |
| `/gk-refactor` | `maintenance` | Improve code structure and maintainability. |
| `/gk-review` | `reviewer` | Perform a comprehensive review of code quality, security, and performance. |

## Internal/Agent-Only Skills

These skills are typically invoked automatically by agents during execution, but can be called manually if needed.

- **`gk-document`** (`documenter`): Generate technical documentation.
- **`gk-git`** (`developer`): Execute git operations.
- **`gk-research`** (`planner`): Gather technical information.
- **`gk-sql`** (`developer`): Optimize SQL queries.
- **`gk-summarize`** (`orchestrator`): Compress conversation history.
