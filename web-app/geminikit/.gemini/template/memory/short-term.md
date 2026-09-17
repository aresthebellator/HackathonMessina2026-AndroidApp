# Memory: short-term

## Schema Definition

Runtime session state. Auto-cleared on session end. Written by `session-init` hook.

```yaml
# --- Session Identity ---
session_id: ""            # string (UUID v4) — unique per session
project_name: ""          # string — dirname of working_dir
project_type: ""          # enum: node | python | go | rust | unknown
working_dir: ""           # string — absolute path
session_start_time: ""    # string — ISO 8601

# --- Model Config ---
model_default: ""         # string — e.g. gemini-2.5-pro
debug_mode: false         # boolean
token_budget: 1000000     # integer — remaining token budget for session

# --- Current Task ---
current_task: ""          # string — active task description
current_agent: ""         # string — agent currently running
current_skill: ""         # string — skill currently active (if any)

# --- Recent Commands (FIFO, max 10) ---
recent_commands: []
# Each entry:
#   - command: string
#     timestamp: ISO 8601
#     status: completed | failed

# --- Active Agents (max 5) ---
active_agents: []
# Each entry:
#   - name: string
#     started_at: ISO 8601
#     task: string

# --- Temp Variables (max 20 keys) ---
vars: {}
# Free-form key-value pairs for inter-agent passing within session
```

## Protocol

| Rule | Detail |
|------|--------|
| TTL | Session — cleared automatically on `session_end` |
| Max entries | 50 total keys across all sections |
| Eviction | FIFO — oldest `recent_commands` and `active_agents` removed first |
| Write access | `session-init` hook (init), any agent (vars section only) |
| Read access | All agents and hooks |
| Persistence | None — do not copy sensitive values to `long-term.md` |

## Example Runtime State

```yaml
session_id: "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
project_name: "geminikit"
project_type: "node"
working_dir: "/Users/dev/geminikit"
session_start_time: "2026-03-19T13:11:00Z"
model_default: "gemini-2.5-pro"
debug_mode: false
token_budget: 847320
current_task: "implement phase-06 hooks and memory"
current_agent: "developer"
recent_commands:
  - command: "/plan implement hooks"
    timestamp: "2026-03-19T13:12:00Z"
    status: completed
vars:
  last_plan_path: "plans/260319-1311-hooks/plan.md"
```

## Notes
- Never store API keys, tokens, or passwords in this file.
- `vars` section is the only section agents may freely write to.
- File is recreated fresh each session by `session-init` hook.
