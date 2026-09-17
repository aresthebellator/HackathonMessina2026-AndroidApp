# Skill Eval Guide

How to test a skill's triggering accuracy and output quality after creation.

---

## Why Eval?

A skill that isn't triggered is useless. A skill that triggers on wrong inputs causes noise.
Eval answers: **Does this skill activate when it should? Does it stay quiet when it shouldn't?**

---

## Step 1: Define Test Cases

Create `evals/evals.json` in the skill directory:

```json
{
  "skill": "gk-<name>",
  "cases": [
    {
      "id": "tp-01",
      "type": "true_positive",
      "prompt": "Create a new skill for analyzing logs",
      "expected": "triggered"
    },
    {
      "id": "tp-02",
      "type": "true_positive",
      "prompt": "Add a /gk-audit command",
      "expected": "triggered"
    },
    {
      "id": "tn-01",
      "type": "true_negative",
      "prompt": "Fix a bug in the auth module",
      "expected": "not_triggered"
    },
    {
      "id": "tn-02",
      "type": "true_negative",
      "prompt": "Write a unit test for the parser",
      "expected": "not_triggered"
    }
  ]
}
```

**Rule:** Min 4 cases — 2 true positives, 2 true negatives.

---

## Step 2: Run Triggering Eval

Manually verify each test case by checking whether the skill's `description` field
contains keywords that match the prompt's intent:

- True positive prompts should have clear keyword overlap with the description
- True negative prompts should have no matching action verbs or trigger contexts

Run the description quality checker as a proxy:
```bash
node .gemini/skills/skill-creator/scripts/check-description.js .gemini/skills/<name>/SKILL.md
```

A passing description (≥2 trigger contexts, clear action verbs) correlates strongly with
correct triggering behavior.

---

## Step 3: Output Quality Checklist

Manually verify generated output against these criteria:

```
[ ] status field present (completed | failed | blocked)
[ ] summary is one sentence describing outcome
[ ] result contains skill-specific fields (not empty object)
[ ] No raw JSON rendered to user (display field used for human output)
[ ] Handoff context ≤ 200 words
[ ] confidence field reflects actual certainty
```

---

## Step 4: Iterate on Description

If true positive rate < 80%:
- Run `node scripts/check-description.js <path>` for specific fix hints
- Add more trigger contexts ("Use when...", "Use for...")
- Add synonyms for the action verb
- Re-run eval

---

## Step 5: Benchmark Score

Run `node scripts/benchmark-skill.js <path>` for composite score.
Target: **≥ 70** before shipping.

```
compositeScore = accuracy × 0.80 + security × 0.20
```

Full criteria: `references/benchmark-criteria.md`

---

## Passing Bar

| Metric | Threshold |
|--------|-----------|
| Validate | 0 errors |
| True positive rate | ≥ 80% |
| True negative rate | ≥ 80% |
| Benchmark composite | ≥ 70 |
