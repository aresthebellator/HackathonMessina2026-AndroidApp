# Skill Design Patterns

5 proven patterns for structuring Gemini Kit skills. Pick one based on what the skill does.

---

## Choosing a Pattern

| Pattern | Use when |
|---------|----------|
| 1. Sequential Workflow | Multi-step task with strict order and validation gates |
| 2. Tool Orchestration | Skill coordinates multiple Gemini tools across phases |
| 3. Iterative Refinement | Output quality improves with loops (reports, docs, plans) |
| 4. Context-Aware Routing | Same goal, different tools/steps depending on input |
| 5. Expert Advisor | Skill adds domain knowledge, not just tool execution |

---

## Pattern 1: Sequential Workflow

**Use when:** Steps have strict dependencies — step N needs output of step N-1.

**Structure:**
```markdown
## Steps
1. Read input + validate required fields
2. Execute phase A → verify output before proceeding
3. Execute phase B using output from step 2
4. Save artifact to `reports/` or `plans/`
5. Return structured result with file path
```

**Key rules:**
- Validate at each gate — return `blocked` early if a step fails
- Save intermediate outputs for recovery
- Each step names the tool it uses

**Example:** `gk-plan` — reads codebase → decomposes → writes phase files → saves plan.md

---

## Pattern 2: Tool Orchestration

**Use when:** Skill coordinates multiple Gemini tools in a workflow (search + read + execute + write).

**Structure:**
```markdown
## Tools
- `google_web_search` — Phase 1: gather evidence
- `web_fetch` — Phase 1: extract from top URLs
- `read_file` — Phase 2: understand project context
- `run_code` — Phase 3: verify logic
- `write_file` — Phase 4: save output

## Steps
1. Search: run ≥2 queries with `google_web_search`
2. Fetch: `web_fetch` top 2-3 URLs for evidence
3. Read context: `read_file` key project files
4. Process + verify with `run_code` if applicable
5. Write output with `write_file`
```

**Key rules:**
- List ALL tools in `## Tools` — don't use unlisted tools
- `google_web_search` goes first for any external information
- Cite URLs in output

**Example:** `gk-research` — searches → fetches evidence → compares → recommends

---

## Pattern 3: Iterative Refinement

**Use when:** Quality improves with multiple passes — draft → validate → refine → re-validate.

**Structure:**
```markdown
## Steps
1. Generate initial draft
2. Self-review against quality criteria in Rules
3. Identify gaps or violations
4. Refine draft to address gaps
5. Re-validate (max 3 iterations — return best effort if still failing)
6. Save final output
```

**Key rules:**
- Define explicit "done" criteria in Rules (e.g., "MUST have ≥5 items per section")
- Set iteration limit (3) to prevent infinite loops
- Report final quality score or confidence level in output

**Example:** `gk-document` — generate docs → check completeness → refine → save

---

## Pattern 4: Context-Aware Routing

**Use when:** Same goal but different execution path based on input flags, file types, or detected context.

**Structure:**
```markdown
## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --mode-a | Path A for context X | ./references/mode-a.md |
| --mode-b | Path B for context Y | ./references/mode-b.md |
| (default) | Auto-detect context | (base skill rules) |

## Steps
1. Detect context: read input flags or inspect `source` field
2. Route to appropriate mode/path
3. Execute mode-specific steps (see mode files)
4. Return unified output schema regardless of path taken
```

**Key rules:**
- Output schema MUST be identical for all paths — caller shouldn't know which path ran
- Each mode file has its own `## Steps` section
- Default path handles the most common case

**Example:** `gk-review` — `--security` path vs `--perf` path vs default code review

---

## Pattern 5: Expert Advisor

**Use when:** Skill adds domain expertise on top of basic tool execution — compliance checks, architectural guidance, security policies.

**Structure:**
```markdown
# Role
[Specific domain expert — e.g., "Senior Security Auditor with OWASP expertise"]

## Steps
1. Gather context (read files, search for CVEs)
2. Apply domain rules from Rules section to findings
3. Classify severity using domain taxonomy
4. Generate actionable recommendations (not just findings)
5. Produce expert-level report with specific fix guidance
```

**Key rules:**
- Role must be specific — "Senior X Expert" not "AI assistant"
- Rules encode domain knowledge (OWASP categories, SOLID principles, etc.)
- Output must be actionable — not "there may be issues" but "line 42: SQL injection via unsanitized `userId`; fix: parameterized query"
- `confidence` field reflects certainty — use `low` when evidence is partial

**Example:** `gk-audit` — applies OWASP taxonomy → checks CVE database → provides severity-classified report

---

## Writing Effective Descriptions (Pushy Descriptions)

The `description` field determines when the AI agent activates this skill. Generic descriptions cause under-triggering.

```yaml
# ❌ Under-triggers — too generic
description: "Analyze code and report findings."

# ✅ Triggers reliably — specific contexts + trigger phrases
description: "Analyze code quality, security, and performance. Use when asked to review
  a file, check for bugs, audit security, detect N+1 queries, or assess maintainability."
```

**Rules for pushy descriptions:**
- Include 3-5 specific trigger contexts ("Use when...", "Use for...")
- Name the output ("...and return a structured findings report")
- Keep ≤ 200 characters
- Use action verbs that match how users will ask ("review", "audit", "check", "analyze")

---

## Scope Declaration (Required)

Every skill MUST declare what it handles AND what it doesn't:

```markdown
# Rules
- This skill handles code analysis and quality reporting. Does NOT fix code — use `gk-bug-fixer` for fixes.
- This skill handles SQL query optimization. Does NOT handle NoSQL (MongoDB, Redis).
```

**Why required:** Without negative scope, AI may expand skill behavior to adjacent tasks, causing side effects or duplicating other skills.
