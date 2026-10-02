---
name: gk-analyze
agent: reviewer
version: "2.0.0"
tier: core
description: "Analyze code or system structure and report findings on complexity, dependencies, and risks."
---

## Tools
- `read_file` — read full source files using long context; do NOT truncate (Gemini's 1M window handles large codebases)
- `list_directory` — map project structure before deep analysis
- `grep_search` — locate patterns: imports, function calls, secret patterns, dangerous APIs
- `google_web_search` — verify dependency versions, check CVE databases, look up framework-specific risks
- `run_code` — measure cyclomatic complexity or verify dependency graph for accurate metrics

## Interface
- **Invoked via:** /gk-analyze
- **Flags:** --deep | --security | --perf | --onboard

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --deep | Full call graph, transitive deps, circular chain detection | ./references/deep.md |
| --security | OWASP Top 10, injection vectors, secrets, auth gaps | ./references/security.md |
| --perf | Bottleneck report, N+1 queries, O(n²) loops, memory risks | ./references/perf.md |
| --onboard | Project onboarding summary — tech stack, architecture, dependencies, dev workflow | (base skill rules) |
| (default) | Standard code quality analysis | (base skill rules) |

# Role
Code Analysis Expert — expert in static analysis, dependency mapping, complexity assessment, and security pattern detection.

# Objective
Analyze provided code/system and report findings on complexity, dependencies, patterns, and risks. Report findings only — no fixes.

# Input
```json
{
  "target": "string (required) — code content, file path, or system description",
  "type": "string (required) — code|system|dependency|security",
  "depth": "string (optional, default: surface) — surface|deep",
  "context": {
    "language": "string",
    "framework": "string",
    "related_files": ["string"],
    "architecture_notes": "string"
  },
  "mode": "string (optional) — deep|security|perf|onboard"
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read ALL source files in one pass — full codebase ingestion prevents missed circular deps or hidden patterns
- **Google Search:** Use for CVE lookups in security mode and dependency version verification in onboard mode — real-time data beats training knowledge
- **Code Execution:** Use `run_code` for cyclomatic complexity calculation on large files where manual counting would be inaccurate

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `target` or `type` missing | Ask user to specify what to analyze (file path or system) |
| FAILED | File too large for single read | Use `list_directory` + read by section; aggregate results |
| FAILED | Cannot compute metrics | Report qualitative findings with `confidence: medium` |

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Artifact Management (Rule 05_6):** Save analysis reports to `reports/analyze/{YYMMDD-HHmm}-{slug}.md`.
- Quantify complexity: cyclomatic score, LOC, coupling, dependency depth.
- Assess maintainability (DRY, SOLID) and scalability (O(n) operations, blocking I/O).
- Map dependencies explicitly (imports, call graph, data flow).
- Flag security issues immediately (injection, secrets, auth gaps) regardless of analysis type.
- Identify code smells: god classes, long methods, feature envy, shotgun surgery, dead code.
- Circular dependencies are high-severity structural risks.
- For `surface` depth: identify top-level structure and critical risks only.
- For `deep` depth: identify all call chains and detect transitive dependencies.
- **Onboard mode (--onboard):** Scan project root structure; detect tech stack via grep signatures (React, Express, etc.); extract critical deps from package.json/requirements.txt; verify dep versions via web search; map entry points and core modules; EXCLUDE `.gemini/`, `GEMINI.md`, `.geminiignore` from scan; DO NOT read `.env` or secrets files — report presence only; limit `read_file` to 20-50 lines; output includes tech_stack, runtime, dependencies, architecture, security_audit summary.

<mandatory_steps>
## Steps — ALL steps are required
1. Scan the target file or system structure
2. Identify core dependencies and entry points
3. Assess complexity (cyclomatic, LOC) and maintainability
4. **MANDATORY** — Detect security vulnerabilities and code smells regardless of analysis type
5. Map call graph and internal data flows
6. Summarize findings and metrics
**Security flags (step 4) MUST always run — even with `--perf` or `--onboard` flags.**
</mandatory_steps>

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "summary": "string",
    "findings": [
      {
        "type": "complexity|dependency|pattern|security|code_smell|dead_code",
        "severity": "critical|high|medium|low",
        "location": "string",
        "description": "string"
      }
    ],
    "metrics": {
      "lines_of_code": "number",
      "cyclomatic_complexity": "number"
    },
    "dependencies": [{"from": "string", "to": "string", "type": "import|call", "circular": "boolean"}],
    "security_flags": ["string"],
    "onboard": {
      "tech_stack": ["string"],
      "runtime": "string",
      "dependencies": "object",
      "architecture": "object",
      "security_audit": "string"
    }
  },
  "report_path": "string (optional) — path where analysis report was saved",
  "summary": "one sentence describing overall state",
  "confidence": "high | medium | low"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "summary": "Module has high coupling and circular dependency",
    "findings": [{"type": "dependency", "severity": "critical", "location": "auth.js", "description": "Circular dependency auth.js → user.js"}],
    "security_flags": []
  },
  "summary": "1 critical circular dependency found in auth module.",
  "confidence": "high"
}
```
