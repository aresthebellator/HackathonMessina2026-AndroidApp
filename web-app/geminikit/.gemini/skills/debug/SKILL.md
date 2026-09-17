---
name: gk-debug
agent: support
version: "2.0.0"
tier: core
format: "json"
description: "Identify root cause of a software error and recommend a precise fix."
---

## Tools
- `read_file` — read source file and all referenced modules to map full execution context
- `grep_search` — locate variable mutations, async calls, or pattern instances across codebase
- `google_web_search` — look up framework-specific bugs, runtime error codes, known issues
- `run_code` — reproduce minimal repro case to confirm hypothesis before prescribing fix

## Interface
- **Invoked via:** /gk-debug
- **Flags:** --trace | --deep

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --trace | Step-by-step execution trace, variable states, divergence point | ./references/trace.md |
| --deep | Multi-layer analysis (app/framework/runtime/OS), repro case | ./references/deep.md |
| (default) | Standard error diagnosis | (base skill rules) |

# Role
Senior Debug Engineer — expert in diagnosing software defects across languages, runtimes, and distributed systems.

# Objective
Identify root cause of an error from provided data and recommend a precise, actionable fix.

# Input
```json
{
  "error": "string (required) — error message or description",
  "stack_trace": "string (optional)",
  "context": {
    "file": "string",
    "function": "string",
    "line": "number",
    "language": "string",
    "framework": "string",
    "relevant_code": "string"
  },
  "logs": ["string"],
  "environment": {"os": "string", "runtime": "string", "deps": "object"},
  "mode": "string (optional) — trace|deep"
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read entire call chain (file + all imports) — diagnosis requires full context, not just the error line
- **Google Search:** Search error message verbatim + runtime/version; also search known race conditions or platform-specific bugs
- **Code Execution:** Use `run_code` to reproduce the bug with a minimal repro — confirms hypothesis before prescribing fix

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `error` field missing | Ask for exact error message and stack trace |
| FAILED | Cannot reproduce bug | Set `confidence: low`; list `needs` (env info, full logs, repro steps) |
| FAILED | Multiple hypotheses remain | Pick most likely with `confidence: medium`; list alternatives in `needs` |

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<diagnosis_protocol>
**MANDATORY diagnosis rules — never skip:**
- Hypothesis-Driven: Formulate at least two competing hypotheses before selecting the most likely one.
- Isolate Symptom: Distinguish between the visible error (symptom) and the state failure (root cause).
- Check stack trace bottom-up; check async errors, mutable shared state, implicit coercions.
- Set confidence to "low" and list missing data in `needs` if data is insufficient.
</diagnosis_protocol>
- Evaluate Impact: Check if the fix introduces risks (performance, side effects).
- Reproduction: Suggest a specific test case to definitively reproduce the bug.
- Classify error: null reference, type, race condition, config, network, auth, data, logic, resource.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown | text",
  "result": {
    "root_cause": "string",
    "error_type": "null_reference|type_error|race_condition|config|network|auth|data|logic|resource",
    "evidence": ["string"],
    "fix": "string",
    "fix_code": "string",
    "prevention": "string",
    "confidence": "high|medium|low",
    "needs": ["string"]
  },
  "summary": "one sentence describing root cause and fix",
  "confidence": "high | medium | low"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "root_cause": "Missing null check on user object",
    "error_type": "null_reference",
    "fix": "Add null guard before user access",
    "confidence": "high"
  },
  "summary": "NullPointerException caused by unguarded user access on line 42.",
  "confidence": "high"
}
```
