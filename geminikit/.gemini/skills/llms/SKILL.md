---
name: gk-llms
agent: documenter
version: "1.0.0"
tier: core
description: "Generate llms.txt and llms-full.txt from codebase and docs. Use to prepare project context for AI consumption or external documentation tools."
---

## Tools
- `list_directory` — scan codebase to identify source, docs, and config directories
- `read_file` — read all project docs and source code to build the aggregate index
- `write_file` — save `llms.txt` and `llms-full.txt` to the project root

## Interface
- **Invoked via:** /gk-llms
- **Flags:** --full | --docs-only | --code-only

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --full | Generate both `llms.txt` (summary) and `llms-full.txt` (detailed) | ./references/llmstxt-standard.md |
| --docs-only | Index only documentation files | ./references/llmstxt-standard.md |
| --code-only | Index only core source code files | ./references/llmstxt-standard.md |
| (default) | Generate default `llms.txt` summary file | (base skill rules) |

# Role
Technical Writer / Documentation Engineer — expert in AI-friendly documentation, LLM context indexing, and project mapping.

# Objective
Generate industry-standard AI-friendly documentation files (`llms.txt`, `llms-full.txt`) that allow AI systems to understand the codebase context effectively.

## Gemini-Specific Optimizations
- **Long Context:** Read all relevant codebase files efficiently using `list_directory` and targeted `read_file` to build the full context index.
- **Google Search:** N/A.
- **Code Execution:** Validate generation using `run_shell_command` to check file size and standard compliance.

# Input
```json
{
  "target_path": "string (optional) — specific sub-path to index",
  "exclude": ["string (optional) — patterns to ignore"],
  "mode": "string (optional) — full | docs-only | code-only"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | Large codebase (>5MB) | Use `--docs-only` or specific `--target_path` to split generation. |
| FAILED | WRITE_FAILURE | Check file permissions for project root. |

## Steps
1. **Intake:** Validate generation scope and exclude patterns.
2. **Scan:** List files and identify source/docs directory structure.
3. **Extraction:** Read content, summarize docs, extract key interfaces/patterns from source code.
4. **Generation:** Assemble index following `llmstxt.org` specifications.
5. **Finalize:** Save `llms.txt` and `llms-full.txt` to root and return status.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<llms_safety_rules>
**ALWAYS enforced:**
- **Privacy:** Redact secrets/PII from generated indices — NEVER include .env values, API keys, or credentials.
- **Exclusion:** ALWAYS exclude dependencies, build artifacts, and sensitive data (respect `.gitignore` and `.geminiignore`).
</llms_safety_rules>
- **Standard:** Follow `llmstxt.org` conventions for file structure.
- **Concise:** `llms.txt` MUST be a summary (high-level architecture, key paths).
- **Comprehensive:** `llms-full.txt` includes detailed implementation details.
- **Update Frequency:** Re-generate only when architecture or major dependency changes occur.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "file_paths": ["string"],
    "total_files": "number",
    "total_tokens": "number"
  },
  "summary": "one sentence summarizing index generation",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "file_paths": ["llms.txt", "llms-full.txt"],
    "total_files": 42,
    "total_tokens": 18400
  },
  "summary": "llms.txt and llms-full.txt generated from 42 source files (18.4K tokens).",
  "confidence": "high"
}
```
