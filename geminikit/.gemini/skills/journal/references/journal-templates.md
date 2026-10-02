# Journal Templates Reference

Structured templates for technical journal entries at different detail levels.

## Standard Journal Entry Template

```markdown
# {Topic} — {YYYY-MM-DD}

**Session:** {duration} | **Branch:** {branch} | **Commits:** {count}

## What Changed
{Brief list of files/modules touched and what was done to each}

- `{file}` — {action: added/modified/deleted} — {what it does now}
- `{file}` — {action} — {brief description}

## Why
{The decision or requirement that drove these changes}

{1-2 paragraphs explaining the context: what problem was being solved,
what alternatives were considered, why this approach was chosen}

## How It Works
{Technical explanation of the implementation}

{Include code snippets for non-obvious patterns}

\`\`\`{language}
// Key pattern or critical section
{code}
\`\`\`

## Lessons Learned
- **{concept}:** {specific lesson} — e.g. "JWT expiry: RS256 tokens with 15min TTL require refresh 
  token rotation; storing refresh in httpOnly cookie prevents XSS theft"
- **{tooling}:** {observation} — e.g. "Hono middleware order matters — auth must run before route handlers"

## Blockers Encountered
- **{problem}:** {what was tried and why it failed} — {how it was resolved or current status}

## Unresolved
- [ ] {open question or deferred item}
- [ ] {known issue or tech debt}

## Next Steps
1. {concrete action with file/location if possible}
2. {concrete action}

---
*Tags: {comma-separated tags for searchability}*
```

## Minimal Template (--brief sessions)

```markdown
# {Topic} — {YYYY-MM-DD}

**Changed:** {files}
**Summary:** {1 sentence}
**Lesson:** {1 key takeaway}
**Next:** {top priority action}
```

## Bug Fix Journal Template

```markdown
# Fix: {Bug Description} — {YYYY-MM-DD}

**Severity:** {critical/high/medium/low}
**Root Cause:** {concise root cause statement}

## Symptom
{What the user/system experienced}

## Root Cause Analysis
{Why it happened — trace through the code path}

## Fix Applied
\`\`\`{language}
// Before
{buggy code}

// After  
{fixed code}
\`\`\`

**Location:** `{file}:{line}`

## Why This Fix
{Why this specific approach vs alternatives}

## Prevention
{What would prevent this class of bug in future — test, lint rule, pattern}

## Regression Risk
{What adjacent code could be affected — tested by {test file}}

---
*Tags: bug-fix, {component}, {error-type}*
```

## Feature Implementation Template

```markdown
# Feature: {Feature Name} — {YYYY-MM-DD}

**Spec:** {link to issue/PRD if available}
**Status:** {complete/partial}

## What Was Built
{Description of the feature from user perspective}

## Architecture Decisions
| Decision | Options Considered | Chosen | Reason |
|----------|-------------------|--------|--------|
| {topic} | {opt1, opt2} | {chosen} | {why} |

## Key Implementation Details
{Technical notes on non-obvious design choices}

## Testing Approach
{How the feature was tested — unit, integration, manual}

## Known Limitations
{What the feature doesn't do yet or edge cases not handled}

---
*Tags: feature, {component}, {domain}*
```

## Append Pattern

When `--append` is used, add new section to existing entry:

```markdown
---

## Update — {HH:MM}

{New content since last write}
```

## File Naming Convention

```
plans/journal/
├── 2024-01-15-jwt-auth-implementation.md
├── 2024-01-15-token-refresh-bugfix.md    # same day, different topic
├── 2024-01-16-rate-limiting-research.md
└── 2024-01-20-auth-module-refactor.md
```

**Slug rules:**
- Derive from topic parameter if provided
- Otherwise use first commit message subject line
- Replace spaces with hyphens, lowercase, strip special chars
- Max 40 characters
- Prefix with `fix-` for bug sessions, `feat-` for features, `refactor-` for refactors

## Searchable Tags Taxonomy

Use consistent tags for cross-entry searchability:

```
# Domain
auth, api, database, ui, infra, testing, performance, security

# Action type
feature, bug-fix, refactor, research, configuration, dependency

# Outcome
blocked, resolved, deferred, shipped

# Tech
typescript, react, postgres, redis, docker, kubernetes
```

## Quality Checklist

Before finalizing a journal entry, verify:
- [ ] "What Changed" lists actual files, not vague descriptions
- [ ] "Why" explains the business/technical reason, not just "needed to"
- [ ] "Lessons Learned" entries are specific and actionable
- [ ] Blockers include what was TRIED (not just what failed)
- [ ] Next steps are concrete (include file path or component when possible)
- [ ] Tags are present for searchability

## Anti-Patterns

| Bad | Good |
|-----|------|
| "Learned TypeScript is strict" | "TypeScript: intersection types don't merge — use extends for interface composition to avoid silent property shadowing" |
| "Fixed auth bug" | "Fix: JWT validation rejected valid tokens — expiry check used server time without UTC normalization (auth.ts:89)" |
| "Next: continue implementation" | "Next: add rate limiting to POST /auth/login using redis-based sliding window (src/middleware/rateLimit.ts)" |
| No tags | Always include 3-5 tags |
| Single huge entry | Break into separate entries per distinct topic |
