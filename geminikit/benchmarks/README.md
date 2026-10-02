# Gemini Kit Benchmark Suite

Real-world task benchmarks to validate Gemini Kit effectiveness against the 100-point scoring rubric.

## Usage

```bash
# List all tasks
node benchmarks/runner.js

# View a specific task (criteria + prompt)
node benchmarks/runner.js 01
node benchmarks/runner.js 07
```

## Tasks

| # | Task | Category | Skill | Difficulty |
|---|------|----------|-------|-----------|
| 01 | Plan a feature (JWT auth) | planning | gk-plan | medium |
| 02 | Debug a null pointer error | debugging | gk-debug | easy |
| 03 | Code review (SQL injection) | review | gk-review | medium |
| 04 | Write Vitest unit tests | testing | web-testing | medium |
| 05 | Document API (OpenAPI) | documentation | gk-document | easy |
| 06 | Refactor duplicated code | refactoring | gk-refactor | medium |
| 07 | Security audit (CVEs) | security | gk-audit | medium |
| 08 | Docker deployment setup | devops | gk-deploy | medium |
| 09 | Brainstorm architecture | architecture | gk-brainstorm | hard |
| 10 | Git commit + PR description | git | gk-git | easy |

## Scoring

Each task has weighted criteria. Run the task prompt in Gemini CLI, then evaluate the output against criteria:

- **Pass (1.0):** Criterion fully met
- **Partial (0.5):** Partially met
- **Fail (0.0):** Not met

```
Task score = Σ(criterion_weight × pass_score) / Σ(weights) × 100
Suite score = Σ(task_score × category_weight) / Σ(category_weights)
```

Core tasks (01-03) carry 2× weight. Extended tasks (04-10) carry 1× weight.

## Benchmark Grading

| Suite Score | Grade | Meaning |
|-------------|-------|---------|
| 90-100% | Excellent | Matches/exceeds Claude Kit |
| 75-89% | Good | Comparable to Claude Kit |
| 60-74% | Adequate | Functional, needs improvement |
| <60% | Needs work | Significant gaps |

## Results

Save evaluation results in `benchmarks/results/YYYY-MM-DD-run.md`.
