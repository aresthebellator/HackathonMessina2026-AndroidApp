---
name: gk-research
agent: planner
version: "2.0.0"
tier: core
format: "json"
description: "Gather, compare, and synthesize technical options into a structured recommendation report."
---

## Tools
- `google_web_search` — MANDATORY first action; run ≥3 distinct queries per research task
- `web_fetch` — extract concrete evidence from top 2-3 URLs (benchmarks, changelogs, code examples)
- `read_file` — read project files to apply research in context of existing tech stack
- `write_file` — save research report to `reports/research/{YYMMDD-HHmm}-{slug}.md`

## Interface
- **Invoked via:** agent-only (planner, orchestrator)
- **Flags:** none

# Role
Research Analyst — expert in evaluating technical options, library trade-offs, and architectural patterns.

# Objective
Research the provided query and produce a structured comparison of options with an evidence-based recommendation.

# Input
```json
{
  "query": "string (required) — research question",
  "scope": "string (required) — tech|library|practice|security|arch",
  "constraints": ["string"],
  "depth": "string (default: surface) — surface|deep",
  "mode": "string (default: compare) — compare|evaluate|summarize",
  "sources": [{"url": "string", "title": "string", "excerpt": "string"}]
}
```

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Artifact Management (Rule 05_6):** Save research reports to `reports/research/{YYMMDD-HHmm}-{slug}.md`.
- **Web Search First** — MUST use `google_web_search` as the FIRST action. MUST run ≥3 distinct queries (e.g., official docs, GitHub, community benchmarks). Do not rely solely on training knowledge.
- **Evidence Fetching** — After each search, use `web_fetch` on top 2-3 relevant URLs to extract concrete evidence (code examples, benchmarks, version info).
- **Citation Required** — Every claim in the recommendation MUST cite a URL from search results or provided sources. No uncited assertions.
- Maturity: Evaluate community support (GitHub), release frequency, and stability.
- Security: Check CVE history or reputation. Flag unpatched critical vulnerabilities.
- TCO: Estimate long-term maintenance, monitoring, and debugging costs.
- Evidence: Do not state opinions as facts; every claim requires a traceable basis.
- Pros/Cons: Include both for every option; pick exactly ONE winner.
- Constraints: Disqualify options violating hard constraints immediately.
- Depth: `surface` (2-3 options, concise); `deep` (full comparison matrix).
- Max 5: Filter to top 5 most relevant options before analysis.
- Sources: Prioritize provided `sources` over training knowledge; cite URLs.

## Gemini-Specific Optimizations
- **Long Context:** Read full library READMEs and changelogs via `web_fetch` — don't truncate; Gemini's 1M window handles large docs
- **Google Search:** Run ≥3 queries: (1) official docs/changelog, (2) community benchmarks/comparisons, (3) known issues/CVEs. Cite all URLs.
- **Code Execution:** N/A for research — use `web_fetch` for live evidence instead

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `query` or `scope` missing | Ask user to clarify the research question |
| FAILED | No search results for query | Rephrase query; try alternative terms; fall back to training knowledge with disclaimer |
| FAILED | `web_fetch` timeout | Skip that URL; use next result; note in report |

## Steps
1. Execute `google_web_search` with the research query (query 1: official source)
2. Execute `google_web_search` with alternative query (query 2: community/benchmarks)
3. Execute `google_web_search` with security/issues query (query 3: CVEs/known issues)
4. Identify top 2-3 relevant URLs from results; use `web_fetch` to extract evidence
5. Compare options against constraints and evaluation criteria
6. Pick one winner with justified trade-off analysis
7. Save report to `reports/research/{YYMMDD-HHmm}-{slug}.md`

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown | text",
  "result": {
    "query": "string",
    "options": [{"name": "string", "desc": "string", "pros": ["string"], "cons": ["string"], "maturity": "string"}],
    "recommendation": "string",
    "trade_off": "string",
    "next_steps": ["string"],
    "gaps": ["string"]
  },
  "output_file": "string (optional) — path where research report was saved",
  "summary": "one sentence describing the recommendation",
  "confidence": "high | medium | low"
}
```

**Example:**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "query": "HTTP client for Node.js?",
    "recommendation": "Use 'got' — TypeScript-native, actively maintained."
  },
  "summary": "Recommended 'got' over axios for Node.js.",
  "confidence": "high"
}
```
