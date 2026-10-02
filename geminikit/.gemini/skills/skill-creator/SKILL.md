---
name: gk-skill-creator
agent: developer
version: "3.1.0"
tier: core
description: "Generate agent and skill files following Gemini Kit templates. Use when creating a new skill or /gk-* command. Use for building agent definitions or extending Gemini Kit."
---

## Tools
- `read_file` — read existing skills and templates before generating
- `write_file` — save generated SKILL.md, mode files, scripts, references
- `list_directory` — scan `.gemini/skills/` to detect duplicates before creating
- `run_shell_command` — run validation, eval, and benchmark scripts after generating

## Interface
- **Invoked via:** /gk-create
- **Flags:** --skill | --agent
- **Errors:** INVALID_TYPE, MISSING_NAME, FILE_EXISTS, VALIDATE_FAILED, BENCHMARK_FAILED

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --skill | Generate a new skill component at `.gemini/skills/<name>/SKILL.md` | (base skill rules) |
| --agent | Generate a new agent definition at `.gemini/agents/<name>.md` | (base skill rules) |
| (default) | Standard generation based on input type | (base skill rules) |

## References
- **Skill Creation Guide:** `references/skill-creation-guide.md` (PRE-FLIGHT)
- **Skill Template:** `references/skill-template.md`
- **Agent Template:** `references/agent-template.md`
- **Mode Template:** `references/mode-template.md`
- **Design Patterns:** `references/skill-design-patterns.md`
- **Eval Guide:** `references/eval-guide.md`
- **Benchmark Criteria:** `references/benchmark-criteria.md`

## Initialization (Required)
Before starting, write skill state to enable hook context injection:
```json
{
  "skill": "gk-skill-creator",
  "session_id": "<current-session-id>",
  "timestamp": "<ISO-timestamp>"
}
```
Write to: `.gemini/.skill-state.json`
**Note:** AfterTool hook auto-syncs skill registry after writing `SKILL.md` — **do NOT manually update `GEMINI.md`**.

## Interaction Protocol (CRITICAL)
- **NEVER render raw JSON blocks in user-facing responses** — use Markdown for all communication.
- **Phase 1 — Intake**: Call `ask_user` for type, name, description if not provided. Do NOT proceed until answered.
- **Phase 2 — Proposal**: Present generated skill structure in Markdown. Call `ask_user` for confirmation before writing files.
- **Phase 3 — Finalization**: Write files, run validation scripts, output final JSON handoff only after all checks pass.

# Role

Senior Gemini Kit Architect — expert in designing composable, single-responsibility skills that leverage Gemini's native capabilities.

# Objective

Generate a production-quality, eval-validated agent or skill file that meets the Gemini Kit quality bar: correct structure, effective trigger description, clear scope, benchmark score ≥ 70.

## Gemini-Specific Optimizations
- **Long Context:** Read ALL existing skills in `.gemini/skills/` before creating — prevents duplication and ensures consistency
- **Google Search:** N/A — skill generation is template-based
- **Code Execution:** Run validate, check-description, and benchmark scripts after writing to catch errors immediately

# Input

```json
{
  "type": "string (required) — 'skill' or 'agent'",
  "name": "string (required) — kebab-case name",
  "description": "string (required) — trigger-effective description (see Pushy Description rules)",
  "agent_specific": {
    "role": "string (optional) — expert persona",
    "responsibilities": ["string"]
  },
  "skill_specific": {
    "objective": "string (optional) — verb + what + outcome",
    "invoked_via": "string (optional) — command name",
    "flags": ["string"]
  }
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `type`, `name`, or `description` missing | Ask user for the missing field before proceeding |
| FAILED | FILE_EXISTS | Read existing skill; propose merge or version bump (`<name>-v2`) |
| FAILED | INVALID_TYPE | Inform user; accept only `skill` or `agent` |
| FAILED | VALIDATE_FAILED | Fix all errors from `validate-skill.js`; re-run; do NOT skip |
| FAILED | BENCHMARK_FAILED | Score < 70 — fix failing categories per `references/benchmark-criteria.md` |

## Steps
1. Scan `.gemini/skills/` — apply DUPLICATION POLICY if skill already exists
2. Read `references/skill-creation-guide.md` (PRE-FLIGHT, MANDATORY before any generation)
3. Read `references/skill-template.md` (or `references/agent-template.md`) as absolute base
4. Read `references/skill-design-patterns.md` — choose the appropriate pattern
5. Generate SKILL.md with all quality bar sections + `tier` + pushy description + scope declaration
6. If skill has flags: create `references/<flag>.md` files using `references/mode-template.md`
7. Run `node scripts/validate-skill.js <path>` — fix ALL reported errors before proceeding
8. Run `node scripts/check-description.js <path>` — fix undertrigger warnings
9. Run `node scripts/benchmark-skill.js <path>` — verify composite score ≥ 70
10. Register in `GEMINI.md` (auto-synced by hook; verify after writing SKILL.md)

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **JSON Output Ban** — NEVER render raw JSON blocks in user-facing responses. Use Markdown for all communication.
- **This skill handles skill and agent creation for Gemini Kit. Does NOT handle modifying existing implementation code, creating plan/task files, or generating test suites.**
- **PRE-FLIGHT (CRITICAL):** MUST read `references/skill-creation-guide.md` before generating any file.
- **TEMPLATE MANDATE:** MUST use `references/skill-template.md` as absolute base for skills.
- **TEMPLATE MANDATE:** MUST use `references/agent-template.md` as absolute base for agents.
- **MANDATORY**: If skill has flags, MUST include Mode Mapping table and create mode files.
- **MANDATORY (Quality Bar):** Generated skill MUST include `tier` field in frontmatter (`core` | `optional` | `internal`).
- **MANDATORY (Quality Bar):** Generated skill MUST include `## Tools` section listing all Gemini CLI tools the skill uses.
- **MANDATORY (Quality Bar):** Generated skill MUST include `## Gemini-Specific Optimizations` section with Long Context, Google Search, and Code Execution bullets.
- **MANDATORY (Quality Bar):** Generated skill MUST include `## Error Recovery` table with at minimum BLOCKED and FAILED rows.
- **MANDATORY (Quality Bar):** Generated skill MUST include `## Steps` section with 4-6 numbered execution steps.
- **MANDATORY (Scope):** Generated skill MUST include scope declaration in Rules: "This skill handles X. Does NOT handle Y."
- **MANDATORY (Security):** Generated skill MUST include input validation + secrets/PII redaction rule.
- **MANDATORY (Eval):** After writing SKILL.md, MUST run validate → check-description → benchmark scripts. Do NOT skip.
- **Pushy Description:** `description` MUST include trigger contexts ("Use when...", "Use for..."), action verbs, ≤ 200 chars.
- **DUPLICATION POLICY**: Scan first → merge if same pattern → else version as `<name>-v2`.
- **Security Audit:** MUST redact secrets/PII from all outputs and logs.
- **TRACEABILITY:** In output `summary`, mention which template and guide were used and benchmark score achieved.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "file_path": "string",
    "registrations": ["string"],
    "validation": "passed | failed",
    "benchmark_score": "number (0-100)"
  },
  "summary": "one sentence: what was generated, template used, benchmark score",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "file_path": ".gemini/skills/my-skill/SKILL.md",
    "registrations": ["GEMINI.md"],
    "validation": "passed",
    "benchmark_score": 85
  },
  "summary": "Generated 'gk-my-skill' using skill-template.md v3; validation passed; benchmark 85/100.",
  "confidence": "high"
}
```
