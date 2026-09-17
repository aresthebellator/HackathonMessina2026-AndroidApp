# Memory: execution

## Schema Definition

Current task execution state. Cleared when the active task completes.
Updated by `pre-tool` and `post-tool` hooks and by agents on subtask completion.

```yaml
# Task block format:
# ---
# task_id: string (UUID v4)
# task_title: string
# status: pending | in_progress | completed | failed
# started_at: ISO 8601
# completed_at: ISO 8601 | null
# current_phase: string
# agent: string
#
# subtasks:
#   - id: string
#     title: string
#     agent: string
#     status: pending | in_progress | completed | failed | skipped
#     started_at: ISO 8601 | null
#     completed_at: ISO 8601 | null
#     depends_on: [subtask_id]
#
# errors:
#   - timestamp: ISO 8601
#     source: string (tool or agent name)
#     message: string
#
# tool_log: (last 20 entries, FIFO)
#   - timestamp: ISO 8601
#     tool: string
#     status: ok | error
#     duration_ms: integer
# ---
```

## Protocol

| Rule | Detail |
|------|--------|
| TTL | Task — cleared on task completion or explicit `/clear` |
| Max tool_log entries | 20 (FIFO eviction) |
| Write access | `pre-tool` hook (tool_log), `post-tool` hook (tool_log, errors), agents (subtasks, status) |
| Read access | All agents and hooks |
| Clear trigger | Task status set to `completed` or `failed` |

## Active Task

<!-- Current execution state is written here at runtime. Example below. -->

```yaml
task_id: ""
task_title: ""
status: pending
started_at: ""
completed_at: null
current_phase: ""
agent: ""

subtasks: []

errors: []

tool_log: []
```

## Example Runtime State

```yaml
task_id: "b2c3d4e5-f6a7-8901-bcde-f12345678901"
task_title: "implement phase-06 hooks and memory files"
status: in_progress
started_at: "2026-03-19T13:11:00Z"
completed_at: null
current_phase: "writing tool definitions"
agent: "developer"

subtasks:
  - id: "sub-01"
    title: "create hooks/"
    agent: "developer"
    status: completed
    started_at: "2026-03-19T13:11:05Z"
    completed_at: "2026-03-19T13:15:00Z"
    depends_on: []
  - id: "sub-02"
    title: "create memory/"
    agent: "developer"
    status: in_progress
    started_at: "2026-03-19T13:15:01Z"
    completed_at: null
    depends_on: [sub-01]

errors: []

tool_log:
  - timestamp: "2026-03-19T13:11:05Z"
    tool: write_file
    status: ok
    duration_ms: 12
```

## Notes
- Do not store file contents or large data blobs in this file.
- Errors section captures tool failures and agent-reported issues only.
- File is reset to empty template on each new task start.
