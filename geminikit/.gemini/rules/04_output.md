# 04_OUTPUT: Communication & Handoff

## 1. Response Format

**Two distinct output contexts — never mix them:**

| Context | Format | Audience |
|---------|--------|----------|
| Agent → Agent handoff | JSON via handoff file (`{ from, to, status, summary, artifacts?, ... }`) — see `agent-handoff-schema.json` | Next agent in pipeline |
| Agent → User response | Human-readable — adapt to content type (see below) | Human in terminal |

**User-facing format guide (choose by content type, not by habit):**
- Short answers / status: plain text — no unnecessary headers or bullets
- Multiple options / steps: numbered list (`1. 2. 3.`) — clearer than Markdown bullets
- Comparisons / trade-offs: table or labeled bullet pairs
- Long reports with sections: Markdown with headings
- Code / commands: fenced code block (always)
- **NEVER**: raw JSON block — extract human-meaningful fields and rephrase as prose/list

`# Output` sections in agent `.md` files describe the **handoff contract** — what goes into the handoff file, not what to display to the user.

Status values: `completed | failed | blocked`.

## 2. Delegation with Compression
- Agents MUST compress their output before returning to the Orchestrator.
- If raw logs exceed 3 paragraphs, you MUST use the `summarize` skill.
- DO NOT pass raw tool logs between agents; only pass compressed `artifacts`.

## 3. Handoff Contract
- `artifacts` MUST clearly list file paths and primary changes.
- `confidence: "low"` requires the next agent to re-verify artifacts before continuing.

## 4. Validation Protocol
- **Integrity Check:** All output MUST include `{ status, result, summary }`.
- **Malformed Data:** If output fails validation, MUST return `status: "failed"` — do not pass to the next component.
- **Circuit Breaker:** 3 consecutive errors from the same component → automatically report `disabled` and halt the task.

## 5. Localization
- **Reasoning:** MUST always be in English to ensure logic consistency.
- **Final Output:** MUST be in the language defined by `OUTPUT_LANGUAGE` in `.env`.
- If `OUTPUT_LANGUAGE` is missing, default to English.
