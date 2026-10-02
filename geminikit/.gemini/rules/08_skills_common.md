# Skill Common Rules (08)

These rules apply to all atomic skills within the Gemini Kit framework and must be followed to ensure consistency, security, and efficiency.

<no_assumptions>
**HARD RULE — No Assumptions:**
MUST NOT assume missing data. Return `status: blocked` immediately if required input fields are absent.
Never infer, guess, or hallucinate missing values.
</no_assumptions>

<evidence_over_claims>
**HARD RULE — Evidence over Claims:**
Do NOT report success unless verification step has passed with zero errors.
`status: completed` requires proof — test pass, build success, or explicit confirmation.
</evidence_over_claims>

## Core Mandates

<security_audit_rule>
- **Security Audit** — ALWAYS check for sensitive data (secrets, keys, PII) in inputs/outputs and redact before returning.
</security_audit_rule>
- **Context Economy** — minimize the number of files read and tokens used while maintaining analysis quality.
- **Output Routing** — The handoff file (`.agent-handoff-{session}.json`) is the ONLY structured data channel between agents. User-facing responses MUST be human-readable per `04_output.md` — never raw JSON. See `agent-handoff-schema.json` for the handoff contract.
- **Shell Compatibility:** Use POSIX-compatible shell syntax by default. On Windows environments, use PowerShell-compatible syntax. Quote paths containing spaces on all platforms.
- **Artifact Management (Rule 05_6):** ALL generated reports, specs, or logs MUST be stored in the appropriate `reports/{skill}/` directory with a timestamped filename (e.g., `{date}-{slug}.md`).

## Execution Excellence

- **Micro-tasking**: Break complex operations into atomic units verifiable with a single deliverable.
- **Validation-Driven**: No subtask is complete without an automated verification (test case or shell check).
- **Structured Output**: For agent-to-agent handoffs, use the handoff file contract. For user-facing responses, always use human-readable format.
- **Confidence Rating**: Include a `confidence` field (`high | medium | low`) in every output to indicate the certainty of the result.

---
> **Reminder:** No assumptions → ask. No evidence → blocked. No secrets in output. Artifacts in `reports/{skill}/`.
