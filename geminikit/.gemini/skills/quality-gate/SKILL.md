---
name: gk-quality-gate
agent: developer
version: "1.0.0"
tier: core
format: "json"
description: "Enforce quality gate before git commit: run tests and code review, block if score < 7 or tests fail."
---

## Tools
- `run_shell_command` — execute test commands and capture results
- `read_file` — read test output and review findings

## Interface
- **Invoked via:** agent-only (developer, git workflow)
- **Flags:** --strict | --quick

## Mode Mapping

| Flag | Description |
|------|-------------|
| --strict | Raise threshold to score ≥ 9, zero warnings allowed |
| --quick | Skip test execution, run review only (faster gate) |
| (default) | Tests must pass + review score ≥ 7 |

# Role
Quality Gate Engineer — enforces minimum quality standards before allowing code commits.

# Objective
Block git commit if code does not meet minimum quality thresholds: all tests pass AND review score ≥ 7 AND no critical security findings.

<quality_gate_contract>
**THIS IS A HARD GATE. Non-negotiable rules:**
1. Tests MUST pass (0 failures allowed)
2. Review score MUST be ≥ 7 (default threshold)
3. NO critical security findings allowed — blocks regardless of score
4. Return `status: blocked` with specific reason if ANY check fails
5. NEVER return `gate_passed: true` when any check failed
</quality_gate_contract>

# Input
```json
{
  "files": ["string"],
  "test_command": "string (optional, default: npm test)",
  "threshold": "number (optional, default: 7)"
}
```

## Steps

<mandatory_steps>
1. **Run tests** — execute `test_command` via `run_shell_command`; capture pass/fail/skip counts
2. **Review code** — invoke `/gk-review --quick` on `files`; extract `score` and `critical_findings`
3. **Evaluate gate** — check ALL conditions: tests_failed == 0 AND score >= threshold AND critical_findings.length == 0
4. **Report result** — return `gate_passed: true` only if ALL conditions met; otherwise `status: blocked` with `block_reason`
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Artifact Management (Rule 05_6):** Save gate reports to `reports/quality-gate/{YYMMDD-HHmm}-gate.md`.
- NEVER skip any check without explicit `--quick` flag from user
- MUST include specific `block_reason` when returning `status: blocked`
- Score threshold default is 7; overridable via `threshold` input field
- `--strict` mode: threshold = 9, zero `medium` or higher findings

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | Tests failed | Report failing test names and errors; commit is blocked |
| BLOCKED | Review score < threshold | Report score, threshold, and top findings; commit is blocked |
| BLOCKED | Critical security finding | Report finding location; HARD BLOCK regardless of score |
| FAILED | Test command not found | Ask user to provide correct test command |
| FAILED | Review skill unavailable | Report degraded mode; do NOT auto-pass gate |

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "gate_passed": "boolean",
    "tests": {
      "passed": "number",
      "failed": "number",
      "skipped": "number",
      "command": "string",
      "duration_ms": "number"
    },
    "review": {
      "score": "number (1-10)",
      "verdict": "string",
      "critical_findings": ["string"],
      "high_findings_count": "number"
    },
    "threshold": "number",
    "block_reason": "string (present when gate_passed: false)"
  },
  "report_path": "string",
  "summary": "one sentence: PASSED or BLOCKED with key metrics",
  "confidence": "high | medium | low"
}
```

**Example — Gate Passed:**
```json
{
  "status": "completed",
  "result": {
    "gate_passed": true,
    "tests": { "passed": 42, "failed": 0, "skipped": 2, "command": "npm test" },
    "review": { "score": 8, "verdict": "approve_with_suggestions", "critical_findings": [] },
    "threshold": 7
  },
  "summary": "Gate PASSED: 42/42 tests pass, review score 8/10.",
  "confidence": "high"
}
```

**Example — Gate Blocked:**
```json
{
  "status": "blocked",
  "result": {
    "gate_passed": false,
    "tests": { "passed": 38, "failed": 4, "command": "npm test" },
    "review": { "score": 6, "verdict": "request_changes", "critical_findings": [] },
    "threshold": 7,
    "block_reason": "4 tests failed; review score 6 is below threshold 7"
  },
  "summary": "Gate BLOCKED: 4 test failures, score 6/10 below threshold.",
  "confidence": "high"
}
```
