# 01_CORE: Foundations & Safety

<critical_rules>
## 1. Supreme Principles
- **YAGNI & KISS:** Implement only what is necessary; prioritize the simplest working solution.
- **No Assumptions:** If information is missing, you MUST ask for clarification; never infer or guess.
- **Single Responsibility:** Each Agent/Skill MUST perform exactly ONE task.

## 2. Safety & Security
- **Non-Destructive:** DO NOT delete or overwrite critical files without explicit user confirmation.
- **Security:** Never store or display API Keys, passwords, or PII (Personally Identifiable Information).
- **Scope:** DO NOT write files outside the current project directory.
</critical_rules>

## 3. Anti-Hallucination
<confidence_gate>
- Derive all conclusions ONLY from actual data collected during execution.
- Set `confidence: "low"` if evidence is incomplete. If `low` confidence is reached at a critical junction, you MUST halt and report `blocked`.
</confidence_gate>

## 4. Component Construction (Integrity)
- **Skill Creation:** MUST include a Registry entry, Input/Output Schema (JSON), and isolated logic. Max 200 lines per skill.
- **Language Standard:** All Skills MUST be written in **English**. This is mandatory to minimize token usage and ensure cross-agent compatibility.
- **Agent Creation:** MUST define a specific Role, Rule-set, and I/O contract. MUST NOT overlap with existing agent responsibilities.

## 5. Localization & Language
- **Input Processing:** All user inputs MUST be translated to English internally before starting the reasoning or execution phase.
- **Internal Logic:** All reasoning, planning, and intermediate steps MUST be conducted in English.

## 6. Project Type Detection

**When to run:** Start of first session on a project (no `project-type` line found in `.gemini/memory/pinned.md`).  
**Purpose:** Determine which section of `09_product-rules.md` to apply. Run once; stored in `pinned.md` (never pruned).

### Step 1 — Check Pinned Memory
Read `.gemini/memory/pinned.md` → look for a line matching `Project type: <type>` under `## Project Context`.
- **Found** → extract type, apply matching `[<type>]` section from `09_product-rules.md`. **Stop.**
- **Not found** → proceed to Step 2.

### Step 2 — Scan Codebase Signals (project root only)

| Type | Detection signals — check at **project root** |
|------|------------------------------------------------|
| `mobile` | `pubspec.yaml` (Flutter) OR `metro.config.js`/`react-native.config.js` OR both `android/` and `ios/` dirs |
| `web` | `next.config.*` OR `nuxt.config.*` OR `astro.config.*` OR (`vite.config.*` AND react/vue in `package.json` deps AND no `src/routes/`) |
| `backend` | `src/routes/` or `src/controllers/` OR express/fastify/hono/gin/chi dep OR (`main.py` AND fastapi in `requirements.txt`/`pyproject.toml`) OR `main.go` with no `cmd/` |
| `tool` | `bin/` dir at project root OR `commander`/`yargs`/`cobra`/`click` dep OR `cmd/` dir (Go) |

- **Exclusions for `web`:** Electron (`electron` dep), Tauri (`tauri` dep), Storybook-only (`storybook` dep + no page routes) → do NOT classify as `web`; treat as 0-match and ask user.
- 2+ signals → high confidence; still confirm with user.
- Multiple types match → list all; ask user to pick **primary** (e.g. fullstack monorepo — one type per session context).
- 0 signals → ask user directly; no guessing.

### Step 3 — Confirm & Save
Ask user: *"I detected this as a **[type]** project (signals: X, Y). Is that correct?"*

- **User confirms** → write to `.gemini/memory/pinned.md` under `## Project Context`:
  ```
  - Project type: <type>
  ```
  Apply `[<type>]` section from `09_product-rules.md`. Done.

- **User rejects** → ask: *"What is the correct project type? (mobile / web / backend / tool)"*  
  On answer, write the corrected type to `pinned.md` and apply matching section. Done.

---
> **Reminder:** YAGNI + KISS + No Assumptions + Single Responsibility. If missing data → ask. If outside scope → refuse.
