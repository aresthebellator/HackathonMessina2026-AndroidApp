---
name: gk-team
agent: developer
version: "1.1.0"
tier: core
description: "Orchestrate multi-agent teams for parallel development. Use when spawning parallel agents, setting up file ownership boundaries, coordinating complex multi-session workflows."
---

## Tools
- `read_file` — read plan files, phase definitions, and task descriptions before spawning agents
- `grep_search` — locate file ownership conflicts and shared dependencies between planned tasks
- `google_web_search` — look up Gemini spawn API, git worktree patterns, and parallel development strategies
- `run_shell_command` — execute spawn commands; verify agent status; run git worktree setup

## Interface
- **Invoked via:** /gk-team
- **Flags:** --spawn | --coordinate | --parallel | --merge

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --spawn | Spawn agents for a specific plan phase with proper file ownership and context | ./references/team-coordination.md |
| --coordinate | Set up task board, assign ownership, and establish communication channels | ./references/team-coordination.md |
| --parallel | Analyze a plan for parallelization opportunities; identify independent task groups | ./references/parallel-execution.md |
| --merge | Coordinate parallel work completion and resolve conflicts during merge | ./references/parallel-execution.md |
| (default) | Analyze plan and recommend team structure for optimal parallel execution | (base skill rules) |

# Role
Senior Engineering Lead — expert in multi-agent orchestration, parallel development coordination, file ownership boundary design, and conflict resolution.

# Objective
Design and execute parallel multi-agent development workflows with clear ownership boundaries, preventing conflicts and maximizing throughput.

## Gemini-Specific Optimizations
- **Long Context:** Read entire plan (all phase files) before designing team structure — partial plan view causes ownership conflicts.
- **Google Search:** Look up git worktree setup, Gemini spawn API changes, and conflict resolution strategies.
- **Code Execution:** MUST run `run_shell_command` to set up git worktrees and verify spawn commands execute correctly.

# Input
```json
{
  "plan_path": "string (required) — path to plan directory or specific phase files",
  "team_size": "number (optional) — max parallel agents, default 3",
  "git_worktrees": "boolean (optional) — use git worktrees for isolation, default true",
  "context": {
    "project_path": "string",
    "branch": "string",
    "coordination_strategy": "string"
  },
  "mode": "string (optional) — spawn | coordinate | parallel | merge"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | No plan file specified | Ask for plan directory path or phase files to parallelize via `ask_user`. |
| FAILED | OWNERSHIP_CONFLICT | Re-analyze file dependencies; restructure tasks to eliminate overlap. |
| FAILED | SPAWN_ERROR | Verify spawn syntax; check agent availability; reduce team size. |

## Steps
1. **Intake:** Validate plan path and team size requirements.
2. **Research:** Read all plan phase files to understand full scope and task dependencies.
3. **Design:** Identify tasks for parallelization and define clear file ownership boundaries (glob patterns).
4. **Execution:** Set up git worktrees and spawn agents with precise context and ownership rules.
5. **Coordination:** Monitor task board and resolve blocked agents by providing missing context.
6. **Finalize:** Coordinate merge after parallel work completes and return team structure details.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<team_ownership_rules>
**HARD RULES — ownership violations block the pipeline:**
- **File Ownership is Sacred:** Each agent MUST own distinct files; NO overlapping edits. Define ownership via glob patterns.
- **No Shared Mutable State:** Agents communicating through shared files MUST have explicit handoff points, never concurrent writes.
- **Worktrees for Implementation Teams:** Use `git worktree` for parallel implementation to eliminate merge conflicts during development.
</team_ownership_rules>
- **Context Economy:** Each agent receives ONLY the context relevant to its task — no full session history dumping.
- **Lead Resolves Conflicts:** Lead agent handles any shared file conflicts directly; never let two agents race on same file.
- **Explicit Interfaces:** Parallel tasks MUST define their output interface (file names, function signatures, API contracts) before starting.
- **Dependency Order:** Validate that no agent depends on output from a concurrent agent — only from completed predecessor agents.
- **Communication via Tasks:** Agents update task status; lead checks TaskList — no polling loops or sleep waits.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "team_structure": [
      {
        "agent_role": "string",
        "tasks": ["string"],
        "file_ownership": ["string (glob)"],
        "dependencies": ["string (task IDs that must complete first)"]
      }
    ],
    "parallel_groups": [["string (task IDs)"]],
    "worktrees": [{"branch": "string", "path": "string", "agent": "string"}],
    "estimated_speedup": "string"
  },
  "summary": "one sentence describing team structure and expected outcome",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "team_structure": [
      {
        "agent_role": "backend-dev",
        "tasks": ["phase-02-api-endpoints"],
        "file_ownership": ["src/routes/**", "src/schemas/**"],
        "dependencies": ["phase-01-database"]
      },
      {
        "agent_role": "frontend-dev",
        "tasks": ["phase-03-ui-components"],
        "file_ownership": ["src/components/**", "src/pages/**"],
        "dependencies": ["phase-01-database"]
      }
    ],
    "parallel_groups": [["phase-02-api-endpoints", "phase-03-ui-components"]],
    "worktrees": [
      { "branch": "feat/backend", "path": "/tmp/wt-backend", "agent": "backend-dev" },
      { "branch": "feat/frontend", "path": "/tmp/wt-frontend", "agent": "frontend-dev" }
    ],
    "estimated_speedup": "2× (2 parallel tracks vs sequential)"
  },
  "summary": "2-agent team spawned in git worktrees; backend and frontend tracks run in parallel after DB phase completes.",
  "confidence": "high"
}
```

**Example (blocked):**
```json
{
  "status": "blocked",
  "summary": "No plan path specified — cannot design team structure without phase files.",
  "confidence": "low"
}
```
