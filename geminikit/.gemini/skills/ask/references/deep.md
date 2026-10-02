---
mode: deep
extends: ask
version: "1.0.0"
---

# Extra Rules

- Perform an in-depth technical analysis across multiple files and modules.
- Evaluate the architectural impact of the proposed question or solution.
- Identify hidden side effects, transitive dependencies, and potential regressions.
- Provide a structured report with code examples, tradeoffs, and long-term risks.
- Research alternative patterns and compare them with the existing one.

# Extra Output

```json
{
  "architectural_impact": ["string"],
  "multi_file_analysis": ["string"],
  "alternative_options": ["string"],
  "long_term_risks": ["string"]
}
```

## Steps
1. Scan across the entire relevant codebase
2. Trace architectural dependencies and impacts
3. Research alternative technical options
4. Summarize the deep technical synthesis

## Examples
**Input:** `/gk-ask --deep How does auth flow work across all microservices?`
**Expected behavior:** Detailed breakdown of auth flow across multiple services, including diagrams and sequence analysis.
