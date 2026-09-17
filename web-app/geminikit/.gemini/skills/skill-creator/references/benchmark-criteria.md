# Skill Benchmark Criteria

Composite scoring for Gemini Kit skills.

```
compositeScore = accuracy × 0.80 + security × 0.20
```

Target: **≥ 70** before shipping. Run with `node scripts/benchmark-skill.js <path>`.

---

## Accuracy Score (80% weight)

Max 100 points across 5 categories:

| Category | Points | Criteria |
|----------|--------|---------|
| Required sections | 30 | ## Tools, ## Steps, ## Error Recovery, # Role, # Objective, # Input, # Rules, # Output all present |
| Steps quality | 20 | ## Steps has ≥ 4 numbered steps; steps name specific tools or actions |
| Examples | 20 | # Output has ≥ 1 concrete JSON example with all envelope fields |
| Description trigger | 20 | Has ≥ 2 trigger contexts ("Use when"/"Use for"), action verbs, ≤ 200 chars |
| Conciseness | 10 | File ≤ 200 lines |

### Deductions
- Missing `tier` field: −10
- Missing scope declaration: −10
- Placeholder text left in content: −15
- `description` is generic (no trigger context): −20

---

## Security Score (20% weight)

Max 100 points across 3 categories:

| Category | Points | Criteria |
|----------|--------|---------|
| Scope declaration | 40 | Rules include "This skill handles X. Does NOT handle Y." |
| Input validation rule | 30 | Rules include validation + secrets/PII redaction |
| Security in Error Recovery | 30 | Error Recovery covers unauthorized/invalid input scenarios |

### Deductions
- No security policy at all: −50
- No scope declaration: −40

---

## Composite Bands

| Score | Band | Action |
|-------|------|--------|
| 90–100 | Excellent | Ship |
| 70–89 | Good | Ship with minor notes |
| 50–69 | Fair | Fix failing categories before shipping |
| < 50 | Poor | Major revision required |

---

## Quick Fixes by Category

**Low accuracy — missing sections:** Add the missing section using `references/skill-template.md` as reference.

**Low accuracy — no examples:** Add a `completed` + `failed` example to `# Output`.

**Low accuracy — weak description:** Run `node scripts/check-description.js` and follow recommendations.

**Low security — no scope:** Add to Rules: "This skill handles X. Does NOT handle Y."

**Low security — no security rule:** Add to Rules: "MUST validate all inputs. MUST redact secrets/PII from outputs."
