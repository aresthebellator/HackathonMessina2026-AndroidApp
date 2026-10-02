# Bridge Testing Guide

Guide to testing the 'gk bridge' pipeline from Claude to Gemini with a simple task.

---

## Prerequisites

```bash
gk --version       # GeminiKit CLI
claude --version   # Claude CLI
gemini --version   # Gemini CLI (must be in PATH)
```

If Gemini CLI is missing:
```bash
npm install -g @google/gemini-cli
```

---

## Overview: Bridge Architecture

The bridge pipeline connects Claude (orchestrator) with Gemini (executor) and back to Claude (reviewer). It enables multi-agent complex task execution with atomic task queue management.

**Task Lifecycle:** pending → executing → gemini_done → reviewing → done (or pending for retry)

**Task JSON Schema:**
```json
{
  "task_id": "task-01-01",
  "phase": 1,
  "step": 1,
  "name": "Phase name",
  "type": "implement|fix|test|refactor",
  "status": "pending|executing|gemini_done|reviewing|done|failed",
  "assigned_to": "gemini|claude",
  "prompt": "Task description for executor",
  "gemini_summary": "Executor output summary",
  "review_result": "Reviewer feedback",
  "retry_count": 0,
  "created_at": "ISO timestamp",
  "updated_at": "ISO timestamp"
}
```

---

## Step 1 — Create a minimal plan for testing

Create the following 2 files in `plans/test-bridge/`:

**`plans/test-bridge/plan.md`**
```markdown
---
title: Bridge Test Plan
status: pending
---

# Bridge Test Plan

| Phase | Name | Effort | Status | File |
|-------|------|--------|--------|------|
| 1 | Hello World | XS | pending | [phase-01-hello.md](phase-01-hello.md) |
```

**`plans/test-bridge/phase-01-hello.md`**
```markdown
# Phase 1: Hello World

## Overview
- **Description:** Create a simple hello.txt file to verify bridge pipeline

## Related Code Files
- `hello.txt`

## Implementation Steps
1. Create file `hello.txt` with content "Hello from Gemini Bridge!"

## Success Criteria
- `hello.txt` exists with correct content
```

---

## Step 2 — Init bridge queue

```bash
gk bridge init --plan plans/test-bridge/plan.md
```

Expected result:
```
✓ Created .bridge/ directory structure
✓ Using plan: plans/test-bridge/plan.md
✓ Generated 1 task(s) in .bridge/queue/
  task-01-01: Hello World [implement]
```

Verify task file exists and is in correct format:
```bash
cat .bridge/queue/task-01-01.json
```

Expected: `"status": "pending"`, `"assigned_to": "gemini"`

---

## Step 3 — Run the pipeline

```bash
gk bridge start
```

The pipeline automatically executes in order:

```
pending → executing   (orchestrator calls Gemini)
executing → gemini_done  (Gemini update task file)
gemini_done → reviewing  (orchestrator calls Claude for review)
reviewing → done / pending (retry)
```

Ctrl+C to stop gracefully if needed.

---

## Step 4 — Check the results

```bash
# Pipeline Overview
gk bridge status

# Task Details
cat .bridge/queue/task-01-01.json

# Full logs
cat .bridge/logs/pipeline-*.log
```

**Successful Task:** "status": "done", `hello.txt` is created in the project root.

**Failed Task:** "status": "failed", read `gemini_summary` and `review_result` in task JSON to debug.

---

## Debugging

### Quick status check
```bash
python -c "
import json, glob
for f in glob.glob('.bridge/queue/*.json'):
    t = json.load(open(f))
    print(f.split('/')[-1], '->', t['status'])
    if t.get('gemini_summary'): print('  summary:', t['gemini_summary'][:100])
"
```

### Common issues

| Symptom | Cause | Fix |
|---------|-------------|-----|
| `gemini command not found` | Gemini CLI not installed | `npm install -g @google/gemini-cli` |
| Task stuck in `executing` | Gemini not updating status file | Check log — Is Gemini using the bridge-task-runner skill? |
| `BRIDGE_STATUS` missing | Claude output missing marker | Check if Claude CLI is running in `--print` mode |
| Task retries 3 times then `failed` | Review continuously fails | Read `review_result` in task JSON, fix prompt or implementation |

### Reset and Rerun

```bash
gk bridge reset        # clear state, keep queue
gk bridge init --plan plans/test-bridge/plan.md   # recreate queue
gk bridge start
```

---

## Retry & Timeout Configuration

### Retry Logic
- **Max retries:** 3 attempts per task
- **Trigger:** If review result is "FAIL" or model output is malformed
- **Behavior:** Task reverts to pending with `retry_count` incremented
- **Final state:** After 3 retries, task moves to `failed` permanently

Example retry scenario:
```
Task 01-01: pending (attempt 1) → executing → reviewing → FAIL
Task 01-01: pending (attempt 2) → executing → reviewing → FAIL
Task 01-01: pending (attempt 3) → executing → reviewing → FAIL
Task 01-01: failed (no more retries)
```

### Timeout Configuration
- **Gemini execution timeout:** 60 seconds per task (configurable in `orchestrator.py`)
- **Claude review timeout:** 30 seconds per review (configurable in `orchestrator.py`)
- **Pipeline polling interval:** 2 seconds (checks task state)

If timeout occurs:
- Task remains in `executing` or `reviewing` state
- Manual reset required: `gk bridge reset --failed-only`
- Check `.bridge/logs/pipeline-*.log` for timeout traces

---

## Flow diagram

```
Claude CLI (orchestrator.py)
    │
    ├── Read .bridge/queue/task-01-01.json (status=pending)
    │
    ├── Call: gemini -p "<task prompt>" --yolo
    │       └── Gemini execute task
    │           └── Updates task-01-01.json: status=gemini_done
    │
    ├── Call: claude --print "<review prompt>"
    │       └── Claude review output
    │           └── Output contains "BRIDGE_STATUS: PASS" or "BRIDGE_STATUS: FAIL"
    │
    └── Updates task-01-01.json: status=done (or pending if retry)
```
