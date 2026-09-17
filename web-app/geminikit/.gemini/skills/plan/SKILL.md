---
name: gk-plan
agent: planner
version: "2.0.0"
tier: core
format: "json"
description: "Break down a complex task into structured, executable subtasks with dependencies and effort estimates."
---

## Tools
- `read_file` — read ALL relevant source files before planning (leverage 1M context window)
- `list_directory` — explore project structure to identify scope and affected modules
- `write_file` — save `plan.md` and phase files to `plans/{date}-{slug}/`
- `google_web_search` — research unfamiliar frameworks, architecture patterns, or library trade-offs

## Interface
- **Invoked via:** /gk-plan
- **Flags:** --fast | --deep | --parallel | --from <path> | --dry-run | --phase <id>

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --fast | Rapid planning with minimal detail | ./references/fast.md |
| --deep | Comprehensive analysis and detailed decomposition | ./references/deep.md |
| --parallel | Identify maximum concurrent task opportunities | ./references/parallel.md |
| --from | Load base plan from existing file path | ./references/from.md |
| --dry-run | Validate plan logic without committing/saving | ./references/dry-run.md |
| --phase | Focus on a specific phase by ID | ./references/phase.md |
| (default) | Standard task decomposition | (base skill rules) |

# Role
Technical Planning Specialist — expert in decomposing complex engineering tasks into structured, executable phases.

# Objective
Break down a complex task into a structured plan of subtasks with deliverables, effort estimates, dependencies, and blockers.

# Input
```json
{
  "task": "string (required) — task to decompose",
  "constraints": [{"type": "tech|biz|time|res", "description": "string"}],
  "context": {
    "codebase": "string",
    "tech_stack": ["string"],
    "patterns": "string"
  },
  "max_subtasks": "number (default: 7)"
}
```

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- File output: → See .gemini/tools/file-output-rules.md
- **Plan Storage (Rule 02_5.1):** ALL generated plans MUST be stored in `plans/{date}-{slug}/plan.md`. The `{slug}` should be a kebab-case version of the task name.
- Max 7 subtasks per plan — group related work into phases if needed.
- Atomic Subtasks: Each subtask must be independent and verifiable with a single deliverable.
- Dependencies: Mark explicitly; list IDs of subtasks it depends on.
- Effort: Use T-shirt sizes: XS (<2h), S (2-4h), M (half day), L (1 day), XL (2-3 days).
- Contingency: For L or XL tasks, explicitly mention risks/buffers.
- Critical Path: Identify the sequence that determines total delivery time.
- Establish Foundation: First subtask should set up environment, schemas, or interfaces.
- Flag Blockers: Decisions or external dependencies required before work starts.
- Parallelism: Identify tasks that can run concurrently versus those that share resources.

## Initialization (Required)
Before starting, write skill state to enable hook context injection:
```json
{
  "skill": "gk-plan",
  "session_id": "<current-session-id>",
  "timestamp": "<ISO-timestamp>",
  "slug": "<task-slug>"
}
```
Write to: `.gemini/.skill-state.json`
The BeforeAgent hook will inject `plan-template.md` and `phase-template.md` automatically.
**Note:** AfterTool hook auto-creates phase stub files after you write `plan.md`.

## Gemini-Specific Optimizations
- **Long Context:** MUST read all relevant source files (src/, tests/, configs) BEFORE decomposing — use Gemini's 1M token window to understand the full codebase instead of guessing structure
- **Google Search:** Use for unfamiliar frameworks, library selection, architecture patterns, or version compatibility checks
- **Code Execution:** N/A — planning is analytical, not runtime verification

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `task` field missing | Ask user to provide the task description |
| BLOCKED | Insufficient codebase context | Use `list_directory` then `read_file` on key entry points |
| FAILED | Plan exceeds 7 subtasks | Group related work into phases; keep max 7 phases per plan |
| FAILED | Unknown tech stack | Run `google_web_search` to research before decomposing |

<mandatory_steps>
## Steps — ALL steps are required, in order
0. **Read codebase first** — `list_directory` + `read_file` on key files. DO NOT plan without this step.
1. Analyze task and extract core requirements
2. Group related work into high-level phases
3. Decompose each phase into atomic, verifiable subtasks
4. Map dependencies and identify the critical path
5. Estimate effort (XS-XL) and identify risks
6. Define success criteria and parallel execution lanes
**Plan is INVALID without a Risks & Mitigations section.**
</mandatory_steps>

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown | text",
  "result": {
    "subtasks": [
      {
        "id": "string",
        "name": "string",
        "description": "string",
        "deliverable": "string",
        "effort": "XS|S|M|L|XL",
        "depends_on": ["string"],
        "files_affected": ["string"]
      }
    ],
    "dependencies": {"critical_path": ["string"], "parallel_groups": [["string"]]},
    "total_effort": "string",
    "blockers": [{"description": "string", "affects": ["string"], "resolution": "string"}],
    "risks": [{"risk": "string", "likelihood": "high|medium|low", "mitigation": "string"}],
    "clarifications": ["string"],
    "success_criteria": ["string"]
  },
  "output_file": "string",
  "summary": "one sentence describing the plan",
  "confidence": "high | medium | low"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "subtasks": [{"id": "T1", "name": "Setup DB", "effort": "S", "deliverable": "schema.sql"}],
    "dependencies": {"critical_path": ["T1"], "parallel_groups": []},
    "total_effort": "S",
    "blockers": [],
    "risks": [],
    "clarifications": [],
    "success_criteria": []
  },
  "output_file": "plans/2026-03-22-setup-db/setup-db.md",
  "summary": "Plan produced: 1 subtask, total effort S.",
  "confidence": "high"
}
```
