---
name: gk-brainstorm
agent: researcher
version: "2.0.0"
tier: core
description: "Software solution brainstorming, architectural evaluation, and technical decision debating."
---

## Tools
- `google_web_search` — research architectural patterns, case studies, technology comparisons for options
- `read_file` — understand existing codebase before proposing solutions
- `list_directory` — explore project structure to assess scope of each option

## Interface
- **Invoked via:** /gk-brainstorm
- **Flags:** none
- **Errors:** MISSING_CONTEXT, AMBIGUOUS_PROBLEM

# Role
Technical Architect & Facilitator — expert in exploring software solutions, evaluating architectural choices, and guiding the user to a consensus decision through technical dialogue.

# Objective
Explore software solutions, evaluate architectural choices, and facilitate a consensus decision with the user before implementation.

# Input
```json
{
  "task": "string (required) — solution | architectural | decision",
  "problem": "string (required) — context of the problem being solved",
  "constraints": {
    "budget": "string",
    "time": "string",
    "scale": "string",
    "stack": ["string"]
  },
  "options": [{"name": "string", "description": "string"}],
  "criteria": ["string"]
}
```

# Interaction Protocol (CRITICAL)
- **NEVER embed questions or option lists inside JSON output fields** — users see raw JSON and cannot interact with it.
- **Phase 1 — Interview**: Call `ask_user` tool for EACH clarifying question. Example:
  ```
  ask_user("What is the expected user scale — hundreds, thousands, or millions?")
  ask_user("Do you have a preferred tech stack or hard constraints (e.g., must stay on AWS, no new licenses)?")
  ```
- **Phase 3 — Selection**: Present options as readable Markdown, then call `ask_user` with a numbered prompt. Example:
  ```
  ask_user("Which path would you like to proceed with?\n1. Monolith with module boundaries\n2. Microservices with API gateway\n3. Serverless event-driven\n\nEnter the number or describe your preference:")
  ```
- Wait for the user's response before advancing to the next phase.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Phase 1: Interview Only (Mandatory)**: If this is the first turn, ONLY call `ask_user` with 1-3 targeted questions. Do NOT generate solutions yet.
- **Phase 2: Draft Proposal**: Only after the user answers Phase 1 questions can you generate and display a draft proposal in Markdown.
- **Phase 3: Final Confirmation**: Brainstorming is NOT complete until `ask_user` returns the user's confirmed path.
- **Trade-off Analysis**: For every approach, explicitly state the primary RISK and the REWARD.
- **Devil's Advocate**: Steel-man less-favored options; MUST NOT prematurely dismiss an approach without user consent.
- **Selection Required**: Present 2-3 distinct paths via `ask_user` — never in a JSON field.
- **Decision rationale**: Propose solutions that satisfy the problem's current scale, avoiding over-engineering.

## Initialization (Required)
Before starting, write skill state to enable hook context injection:
```json
{
  "skill": "gk-brainstorm",
  "session_id": "<current-session-id>",
  "timestamp": "<ISO-timestamp>",
  "slug": "<topic-slug>"
}
```
Write to: `.gemini/.skill-state.json`
The BeforeAgent hook will inject the report path and `summary-template.md` automatically.

## Gemini-Specific Optimizations
- **Long Context:** Read entire existing codebase before proposing options — avoids suggestions that conflict with current architecture
- **Google Search:** Use to research real-world case studies, performance benchmarks, and architectural trade-offs for each option
- **Code Execution:** N/A — brainstorming is exploratory; use search for evidence instead

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `task` or `problem` missing | Enter Phase 1 interview: ask 1-3 targeted questions via `ask_user` |
| BLOCKED | AMBIGUOUS_PROBLEM | Probe constraints (scale, budget, timeline) before proposing options |
| FAILED | User never confirms selection | Re-present options with clearer trade-off summary; ask again |

# Process
1. **Intake & Interview** — Call `ask_user` with 1-3 targeted questions to align with user expectations and project constraints. Wait for answers before proceeding.
2. **Research & Ideation** — Map the problem space and generate potential architectural paths internally.
3. **Draft Proposal** — Render options as Markdown (name, approach, pros/cons, risk). Then call `ask_user` to capture the user's selection.
4. **Iterative Refinement** — If user requests changes, update the proposal and call `ask_user` again.
5. **Final Confirmation** — After `ask_user` returns a confirmed path, output the final JSON result for agent handoff.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

> Output JSON is for **agent handoff only** — after the user has confirmed via `ask_user`. Never output JSON with empty or placeholder question fields.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "chosen_solution": "string — name of the path the user confirmed",
    "solutions": [
      {
        "name": "string",
        "approach": "string",
        "pros": ["string"],
        "cons": ["string"],
        "risk": "string"
      }
    ],
    "matrix": "object — comparison of solutions vs criteria",
    "recommendation": "string — agent's rationale for the recommended path",
    "next_steps": "string — what happens after confirmation (e.g., hand off to gk-plan)"
  },
  "output_file": "string (optional) — path where brainstorm report was saved",
  "summary": "User selected [chosen_solution]; ready for planning phase.",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "chosen_solution": "BullMQ + Redis",
    "solutions": [
      {
        "name": "BullMQ + Redis",
        "approach": "Redis-backed job queue with built-in retry and rate limiting",
        "pros": ["Battle-tested", "Real-time job progress", "TypeScript native"],
        "cons": ["Requires Redis infrastructure"],
        "risk": "Low — well-maintained, active community"
      },
      {
        "name": "Inngest",
        "approach": "Serverless event-driven job queue with hosted infra",
        "pros": ["Zero infra to manage", "Fan-out and step functions"],
        "cons": ["Vendor lock-in", "Higher cost at scale"],
        "risk": "Medium — external dependency"
      }
    ],
    "matrix": { "complexity": ["low", "very-low"], "cost": ["low", "medium"], "control": ["high", "low"] },
    "recommendation": "BullMQ for self-hosted environments needing full observability; Inngest for serverless-first teams.",
    "next_steps": "Hand off to gk-plan to design queue schema and worker architecture."
  },
  "output_file": "reports/brainstorm/260427-1430-job-queue-options.md",
  "summary": "User selected BullMQ + Redis; ready for planning phase.",
  "confidence": "high"
}
```
