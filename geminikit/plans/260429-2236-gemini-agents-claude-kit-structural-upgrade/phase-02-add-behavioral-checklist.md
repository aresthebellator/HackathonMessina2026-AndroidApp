# Phase 02 — Thêm Behavioral Checklist vào 9 agents

## Overview

- **Priority:** P1
- **Status:** pending
- **Effort:** 30m

## Context

Claude Kit dùng behavioral checklists để agent tự verify trước khi kết thúc task.  
Gemini Kit không có — agent có thể return "completed" mà không kiểm tra các điều kiện quan trọng.

## Agents cần Checklist (9 agents)

Không thêm vào: `developer` (đã có autonomous loop + retry), `planner` (đã phức tạp), `reviewer` (đã có scoring formula), `tester` (đã có rules chặt), `mcp-manager` (simple executor).

### architect.md

Thêm sau `# Objective`, trước `# Permissions`:

```markdown
## Behavioral Checklist

Before producing any ADR or architectural recommendation, verify:

- [ ] First Principles applied: stripped to fundamental truths, not "industry standard" alone
- [ ] 2-3 competing approaches evaluated: not variations on the same idea
- [ ] Second-order effects named: downstream consequences stated for each approach
- [ ] Trade-off matrix complete: every recommendation has a named "cost of admission"
- [ ] Inversion applied: asked "what would make this catastrophically fail?" for each option
- [ ] Decision documented: ADR written before session ends, not just verbal recommendation
```

### comparator.md

```markdown
## Behavioral Checklist

Before producing comparison report, verify:

- [ ] Both system paths readable and verified
- [ ] Entry points identified in both systems
- [ ] Every finding has category: Match | Partial Match | Mismatch | Gap | New Feature
- [ ] No code modified in either system (read-only enforced)
- [ ] Confidence stated: if low, status=blocked with gaps listed
```

### designer.md

```markdown
## Behavioral Checklist

Before delivering spec or review output, verify:

- [ ] Requirements read fully before producing any spec
- [ ] Accessibility checked: contrast ≥ 4.5:1, focus states present, ARIA documented
- [ ] All component states specified: default, hover, focus, disabled, error
- [ ] spec mode: precise enough for developer to implement without ambiguity
- [ ] review mode: every finding has severity + concrete fix suggestion
- [ ] approved: false if contrast < 4.5:1 or missing focus states (no exceptions)
```

### devops.md

```markdown
## Behavioral Checklist

Before reporting infra changes complete, verify:

- [ ] No hardcoded secrets: all credentials via env vars or secret managers
- [ ] Idempotency: scripts/configs can run multiple times without side effects
- [ ] Infrastructure as Code: declarative configs preferred over imperative scripts
- [ ] Read existing infra before modifying: no blind overwrites
- [ ] Security First: least privilege applied to all new roles/permissions
```

### documenter.md

```markdown
## Behavioral Checklist

Before delivering documentation, verify:

- [ ] Read actual code before documenting — never described assumed behavior
- [ ] Every code example compiles/runs (or is marked as pseudocode)
- [ ] Referenced file paths, function names, and CLI flags verified to exist
- [ ] Code contradictions flagged: if code differs from existing docs, both versions presented
- [ ] scope=update: only changed sections from new code, not full rewrite
- [ ] ADR/changelog format enforced where applicable
```

### maintenance.md

```markdown
## Behavioral Checklist

Before reporting maintenance task complete, verify:

- [ ] Analysis ran first: complexity/debt identified before any changes
- [ ] No functionality broken: existing tests pass after refactor
- [ ] Incremental approach: no "big bang" rewrite without justification
- [ ] Verification step completed: test suite ran and passed
- [ ] Debt assessment documented: quantitative or qualitative improvement stated
```

### researcher.md

```markdown
## Behavioral Checklist

Before delivering research report, verify:

- [ ] Multiple sources consulted: no single-source conclusions
- [ ] Interview questions asked first (Interview-First rule): no proposal in first turn
- [ ] Max 5 options evaluated: filtered to most relevant
- [ ] Every option has both pros AND cons: no one-sided analysis
- [ ] Constraint violations disqualified: hard constraints enforced before soft
- [ ] Single recommendation made: research ends with ranked choice, not a list
- [ ] Confidence stated: if low, status=blocked with gaps listed
```

### security.md

```markdown
## Behavioral Checklist

Before delivering security assessment, verify:

- [ ] Attack surface mapped: all external inputs and trust boundaries identified
- [ ] Dependency audit run: vulnerable packages checked
- [ ] Static analysis run: hardcoded secrets and unsafe patterns scanned
- [ ] No secrets exposed in report: found secrets masked before writing
- [ ] Every finding has remediation step: not just identification
- [ ] Zero Trust assumed: every external input treated as malicious
```

### support.md

```markdown
## Behavioral Checklist

Before delivering incident report, verify:

- [ ] Incident report read fully before starting analysis
- [ ] Log analysis completed: error patterns identified
- [ ] Root cause traced: not just symptoms
- [ ] PII masked in all report output
- [ ] Workaround provided: immediate mitigation if possible
- [ ] Next step actionable: specific code or config change recommended
```

## Implementation Steps

1. Thêm `## Behavioral Checklist` section vào mỗi agent ngay sau `# Objective` block
2. Dùng checklist phù hợp với vai trò của agent (không copy-paste generic)
3. Ensure format: `- [ ] Item: concrete, verifiable condition`

## Success Criteria

- [ ] 9/9 agents có Behavioral Checklist
- [ ] Mỗi checklist có 5-7 items phù hợp với vai trò
- [ ] Không có items chung chung, mỗi item là verifiable condition
