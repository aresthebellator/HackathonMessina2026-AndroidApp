# Gemini Kit Hooks System

This directory contains the native lifecycle hooks for Gemini CLI. These hooks are Node.js scripts that automate state management, token optimization, and skill coordination without requiring manual human intervention.

## Architecture

Hooks are executed as isolated Node.js processes by the Gemini CLI. They communicate via:
- **Input**: JSON received through `stdin`.
- **Output**: JSON written to `stdout`.
- **Memory**: Persistent state is shared via `.gemini/memory/` and `.gemini/.skill-state.json`.

## Hook Lifecycle Events

| Hook Script | Gemini CLI Event | Responsibility |
|:---|:---|:---|
| `session-start.js` | `SessionStart` | Initializes short-term context by loading pinned rules and recent long-term history. |
| `before-model.js` | `BeforeModel` | Trims stale tool responses from history and logs token usage estimates. |
| `pre-compress.js` | `PreCompress` | Summarizes conversation before context compression to preserve key facts. |
| `before-agent-rules-inject.js` | `BeforeAgent` | Injects structured context and skill-specific templates into the agent's instructions. |
| `before-tool-selection.js` | `BeforeToolSelection` | Validates or enforces specific tool selection based on active skill constraints. |
| `before-tool-privacy-block.js` | `BeforeTool` | Prevents tools from accessing sensitive files or credentials. |
| `after-tool.js` | `AfterTool` | Logs tool usage, redacts sensitive info, truncates large outputs, and triggers post-write automation. |
| `after-model.js` | `AfterModel` | Processes model responses before they are displayed to the user. |
| `session-end.js` | `SessionEnd` | Cleans up session state and finalizes long-term memory updates. |

## Library Modules (`lib/`)

The hook system relies on shared logic located in the `lib/` directory:

- **`memory-manager.js`**: Low-level read/write access to `.gemini/memory/` files.
- **`skill-state-manager.js`**: Manages the `.gemini/.skill-state.json` file for skill-hook coordination.
- **`skill-context-builder.js`**: Builds complex injection strings for different skills (e.g., active plan paths).
- **`post-write-processor.js`**: Detects file writes and triggers automatic actions (e.g., syncing the registry or indexing reports).
- **`response-truncator.js`**: Intelligently truncates large tool responses (like `read_file`) to save context tokens.
- **`agent-handoff-manager.js`**: Manages the flow of state between different specialized agents.
- **`gemini-summarizer.js`**: Integration with Gemini models for autonomous summarization.

## Token Optimization Strategies

- **Redaction**: `after-tool.js` automatically masks fields matching sensitive patterns (key, secret, token, etc.).
- **Truncation**: Large tool responses are truncated to a safe limit, preserving enough context for the agent while preventing token overflow.
- **Trimming**: `before-model.js` removes old tool response bodies from the conversation history while keeping the turn structure intact.
- **Context Injection**: `before-agent-rules-inject.js` ensures only necessary, high-signal data is injected into the agent's prompt.

## Setup

Registration of these hooks is managed in `.gemini/settings.json`. Ensure `GEMINI_API_KEY` is set in your environment for hooks requiring model access.
