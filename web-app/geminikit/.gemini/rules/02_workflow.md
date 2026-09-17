# 02_WORKFLOW: Orchestration & Execution

## 1. Orchestration Hierarchy
- **Orchestrator:** Routes ONLY; does NOT execute. Decomposes tasks into ≤5 steps.
- **Agents:** Execute assigned tasks; MUST NOT call other Agents directly.
- **Routing:**
  - Simple (1-agent): Direct routing.
  - Complex (Multi-step): Pass through `planner` to generate a roadmap before execution.

## 2. Execution Contract

<mandatory_sequence>
**REQUIRED execution order — NEVER skip or reorder:**
Research → Strategy → Execution → Validation → Quality Gate → Done
</mandatory_sequence>

- MUST follow the workflow: **Research → Strategy → Execution → Validation**.
- **2.1 Intake & Interview Protocol (CRITICAL):**
  - **Context Gathering**: For tasks involving `refactor`, `migrate`, or architectural changes, the Agent MUST interview the user using `gk-intake` or specific questions.
  - **Implicit History**: Ask about the history of the code being changed: "Why was it written this way?", "What are the hidden side effects?".
  - **Goal Verification**: Confirm the final expected state before generating any plan.
- **Strategy Phase:**
  1. Determine if the task requires external integrations (DB, API, Repo).
  2. If yes, execute MCP Bridge discovery (`python .gemini/scripts/mcp_bridge.py`).
  3. Incorporate discovered tools from `temp_mcp_manifest.json` into the implementation plan.
<confirmation_required>
- **Confirmation Gate:** Upon completion of the Strategy Phase (Planning), the Orchestrator MUST present the plan to the user and **wait for explicit confirmation** (e.g., "Plan approved", "Proceed") before proceeding to the Execution Phase.
</confirmation_required>
- A task is considered complete ONLY after the result has been verified (Validation is the final mandatory step).

## 3. Priority & Conflict Resolution
- Priority order: `blocked_resolution` > `test` > `debug` > `review` > `develop` > `plan`.
- File Conflict: The first agent to claim a file has priority. The second agent MUST wait or redirect.

## 4. Shell & Environment Protocol (Windows/PowerShell)
- **PowerShell Priority:** Since the environment uses `powershell.exe`, Agent MUST use PowerShell-compatible syntax.
- **Command Chaining:** DO NOT use `&&` (Bash/CMD logic). Use `;` (PowerShell separator).
- **Cmdlet Preference:** Prefer official PowerShell Cmdlets (e.g., `Move-Item`, `Copy-Item`, `Remove-Item`) over aliases.
- **Error Handling:** On non-zero exit code, return `status: "failed"`. On timeout (30s), return `error.code: "COMMAND_TIMEOUT"`.
- **Safety Guards:**
  - MUST NOT run destructive commands (rm, format, mkfs) without `confirmed: true`.
  - Hard-blocked patterns: `rm -rf /`, `format C:`, `dd if=`.
- **Output Capture:** Capture stdout and stderr separately; truncate streams at 10KB if exceeded.
- **Path Handling:** Use backslashes `\` for Windows paths or properly quote paths containing spaces.

## 5. Persistence & Documentation
- **5.1 Plan Storage (CRITICAL):** ALL plans generated during Research/Strategy (manually or via `enter_plan_mode`) MUST be persisted to the project's `plans/{date}-{slug}/plan.md` directory immediately. NEVER rely on system temporary (tmp) directories for plan storage. This ensures transparency, traceability, and consistency across sessions.
- **5.2 Memory Updates:** After each completed phase or task, update `.gemini/memory/execution.md` with the current state.
- **5.3 Auto-Persistence (NEW):**
  - **Auto-Sync:** At the end of every interaction turn (before the final response to the user), the Orchestrator MUST automatically synchronize the current session state to `.gemini/memory/`.
  - **Auto-Summarize:** If the current `execution.md` or session context exceeds 2000 tokens, or upon completion of a major Directive, the Orchestrator MUST silently invoke the `summarize` skill to update `long-term.md` and `short-term.md`.
  - **Implicit Export:** Treat the final response as an implicit `export-session` trigger, ensuring all artifacts and decisions are captured in the project's memory folder.

- **5.4 Concurrency & Locking:**
  - **Lock File:** Before any write operation to `.gemini/memory/`, the Orchestrator MUST check for `.gemini/memory/session.lock`.
  - **Race Prevention:** If a lock exists, retry up to 3 times (500ms intervals). If it persists, log a warning and append to a temporary buffer.
  - **Atomic Writes:** Always release the lock immediately after a successful write operation.

## 6. Error Recovery Protocol
- **Mid-execution Failure:** If a phase fails after partially modifying files, the Orchestrator MUST:
  1. Log the failure state to `.gemini/memory/execution.md` with affected file list.
  2. Do NOT attempt further writes to partially-modified files.
  3. Report `status: "blocked"` with `recovery_action: "manual_review"` to the user.
- **Checkpoint Restore:** If `execution.md` contains a prior checkpoint, the Orchestrator MAY offer to resume from it — but MUST confirm with the user before proceeding.
<no_auto_fix>
- **No Silent Forward-Fix:** Never attempt to auto-fix a failed write without user confirmation.
</no_auto_fix>

## 7. API Rate Limit & Retry
- On HTTP 429 / rate limit error: wait using exponential backoff — 2s, 4s, 8s — max 3 retries.
- After 3 retries with no success: return `status: "failed"`, `error.code: "RATE_LIMITED"`, and surface to user.
- On HTTP 5xx from external APIs: treat as transient, apply same backoff. Log each retry to `execution.md`.
- **Graceful Degradation:** If a skill/agent is unavailable (timeout or load error), fall back to reporting the task as `blocked` — do not silently skip or hallucinate output.

## 9. Definition of Done

<definition_of_done>
A task is ONLY considered complete when ALL of the following are true:
1. **Tests pass** — 0 failures in test suite
2. **Review score ≥ 7** — via `/gk-review`; no critical security findings
3. **Quality gate passed** — `/gk-quality-gate` returns `gate_passed: true`
4. **Artifacts saved** — reports stored in `reports/{skill}/` per Rule 05_6
5. **Memory updated** — `.gemini/memory/execution.md` reflects current state

**A task that skips any of these steps is INCOMPLETE — regardless of user approval.**
</definition_of_done>

## 8. High-Impact Skill Validation
- **gk-refactor / gk-migrate**: MUST provide a "Before/After" comparison report and wait for user approval before applying changes to the main branch.
- **gk-plan**: Every plan must have a "Risks & Mitigations" section.
- **gk-research**: Reports must end with a "Recommendation for Decision" and wait for the user to pick an option.
- **gk-skill-creator**: Skills are generated in a `.gemini/skills/drafts/` folder and require a manual move to the active registry by the user or an explicit "Confirm & Move" command.

