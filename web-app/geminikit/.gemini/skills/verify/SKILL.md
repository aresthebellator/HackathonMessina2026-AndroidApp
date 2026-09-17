---
name: gk-verify
agent: developer
version: "1.0.0"
tier: core
description: "Execute Python code in sandbox to verify fixes, test logic, or validate transformations."
---

## Interface
- **Invoked via:** /gk-verify (or "agent-only")
- **Flags:** none

# Role
Verification Engineer — executes code to validate correctness claims via Python sandbox.

# Objective
Execute a code snippet in Python sandbox and report pass/fail with evidence.

# Input

```json
{
  "code":        "string (required) — code to verify (any language logic, translated to Python)",
  "description": "string (required) — what the code should do",
  "assertions":  ["string (optional) — specific conditions to verify"],
  "language":    "string (optional, default: python) — source language for translation context"
}
```

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST translate non-Python code to equivalent Python for sandbox execution
- MUST wrap code in try/except to capture all errors
- MUST include assertions that map to the described behavior
- MUST use Gemini's native code execution tool (not `run_shell_command`)
- MUST NOT attempt file I/O, network calls, or external package imports in sandbox
- MUST report execution stdout/stderr in output
- SHOULD keep test scripts under 50 lines
- Populate `display` with pass/fail verdict and key evidence

## Steps
1. Analyze input code and description
2. Generate Python test script with assertions
3. Execute via Gemini's native code execution sandbox
4. Parse execution output for pass/fail
5. Return structured result with `display` field

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "display": "## Verification: PASS/FAIL\n\n**Test:** description\n**Result:** ...",
  "result": {
    "verdict": "pass | fail | error",
    "test_script": "string — the generated Python test",
    "execution_output": "string — stdout/stderr from sandbox",
    "assertions_passed": 0,
    "assertions_total": 0
  },
  "summary": "one sentence verdict",
  "confidence": "high | medium | low"
}
```

**Example (completed — pass):**
```json
{
  "status": "completed",
  "format": "json",
  "display": "## Verification: PASS\n\n**Test:** sort([3,1,2]) returns [1,2,3]\n**Result:** ✓ All 3 assertions passed",
  "result": {
    "verdict": "pass",
    "test_script": "assert sorted([3,1,2]) == [1,2,3]",
    "execution_output": "All assertions passed.",
    "assertions_passed": 3,
    "assertions_total": 3
  },
  "summary": "Verification passed: sort function returns correct ascending order.",
  "confidence": "high"
}
```

**On blocked:**
```json
{
  "status": "blocked",
  "format": "json",
  "missing_fields": ["code", "description"],
  "summary": "Cannot proceed: code and description are required"
}
```
