# How to Create a New Skill

Step-by-step workflow for adding a skill to Gemini Kit. Follow in order — do not skip steps.

---

## Step 1: Verify need

- Does a skill already exist for this task? Check `.gemini/skills/`
- Does this violate SCR-B1 (single responsibility)? If "analyze AND fix" → split into two skills
- If both pass → proceed

## Step 2: Create directory and file

```bash
mkdir -p .gemini/skills/<skill-name>
cp .gemini/template/skill-template.md .gemini/skills/<skill-name>/SKILL.md
```

Directory name and file name MUST be kebab-case and match the `name` field you will set in frontmatter.

## Step 3: Fill frontmatter

```yaml
---
name: gk-<skill-name>       # kebab-case, unique across skills/
agent: <primary-agent>      # primary agent owner (e.g., developer, security)
version: "1.0.0"            # semver
tier: optional              # core | optional | internal
description: "Verb + what + outcome. Use when... One sentence ≤200 chars."
---
```

## Step 4: Define each section (in order)

| Section | Rule |
|---------|------|
| `## Tools` | List all Gemini CLI tools this skill uses (read_file, google_web_search, etc.) |
| `## Interface` | Invoked via (command or agent-only), Flags accepted, Error codes returned |
| `## Mode Mapping` | Table mapping flags to `./references/` files (MANDATORY if flags exist) |
| `## Initialization` | OPTIONAL: only add if skill writes to `.gemini/.skill-state.json` for hook injection |
| `# Role` | One sentence — who this skill is (expert persona) |
| `# Objective` | One sentence — what it produces. Start with a verb. |
| `## Gemini-Specific Optimizations` | 3 bullets: Long Context, Google Search, Code Execution — specify when/how to use |
| `# Input` | JSON schema: all required fields typed, all optional fields with defaults |
| `## Error Recovery` | Table: Error code/Scenario → Cause → Recovery action |
| `## Steps` | 4–6 numbered execution steps with tool hints |
| `# Rules` | MUST/MUST NOT statements. Skill-specific. Min 5 rules. |
| `# Output` | JSON schema inside `result`, then response envelope + examples |

## Step 5: Write the output section

Always use this structure:

```json
// Skill-specific schema (content of result):
{ "field_1": "type", "field_2": "type" }

// Response envelope (required):
{
  "status": "completed | failed | blocked",
  "result": { /* skill-specific fields */ },
  "summary": "one sentence",
  "confidence": "high | medium | low"
}
```

## Step 6: Validate against checklist

```
[ ] YAML frontmatter present (name, agent, version, tier, description)
[ ] tier field in frontmatter (core | optional | internal)
[ ] description includes "Use when..." trigger context (≤200 chars)
[ ] ## Tools section present (listing Gemini CLI tools used)
[ ] ## Interface section present (Invoked via, Flags, Errors)
[ ] ## Mode Mapping table present (if flags exist)
[ ] ## Initialization block present (if skill tracks state)
[ ] # Role section present (expert persona)
[ ] # Objective section present (verb-led action)
[ ] ## Gemini-Specific Optimizations present (Long Context, Search, Code Execution)
[ ] # Input section present with typed JSON schema
[ ] ## Error Recovery table present (BLOCKED + FAILED rows minimum)
[ ] ## Steps section present with 4-6 numbered steps
[ ] # Rules section present (min 5 rules)
[ ] # Output section present with status, result, summary, confidence
[ ] File name matches frontmatter name (kebab-case)
[ ] Total lines ≤ 200
[ ] Single responsibility (SCR-B1)
[ ] No assumption of missing data — return blocked if required fields absent
[ ] Artifact Management rule (Rule 05_6) present if generating reports
[ ] Plan Management rule (Rule 02_5.1) present if generating plans
[ ] Stateless (no session state)
```

## Step 7: Register in REGISTRY.md

Add the skill to `.gemini/REGISTRY.md` following the established format. The AfterTool hook will handle synchronization.

---

## Line budget

| Section | Target lines |
|---------|-------------|
| Frontmatter | 5 |
| Tools | 5–8 |
| Gemini Optimizations | 5–8 |
| Error Recovery | 5–8 |
| Steps | 6–10 |
| Role + Objective | 4 |
| Input schema | 10–20 |
| Rules | 8–15 |
| Output (schema + envelope + examples) | 25–35 |
| **Total** | **≤ 200** |
