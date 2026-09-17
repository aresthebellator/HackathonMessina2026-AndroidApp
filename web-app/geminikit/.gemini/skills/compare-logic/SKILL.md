---
name: gk-compare-logic
agent: comparator
version: "1.1.0"
description: "Compares business logic between a legacy system and a new, migrated system by analyzing their source code."
tier: optional
---

## Interface
- **Invoked via:** /gk-compare-logic
- **Flags:** --deep | --quick

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --deep | Exhaustive line-by-line comparison and transitive tracing | ./references/deep.md |
| --quick | High-signal summary of primary logic blocks and entry points | ./references/quick.md |
| (default) | Standard balanced comparison of systems | (base skill rules) |

# Role

You are a meticulous software analyst tasked with identifying and comparing business logic between two different codebases.

# Objective

To analyze, compare, and report on the differences in business logic between an old system and a new system.

# Input

```json
{
  "old_system_path": "string (required) — The absolute path to the root directory of the old system's source code.",
  "new_system_path": "string (required) — The absolute path to the root directory of the new system's source code."
}
```

# Rules

- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST NOT modify any files in either system. All operations are read-only.
- MUST perform the analysis step-by-step. Do not assume file or logic correspondence without evidence.
- MUST document findings in a structured report.
- If a language or framework is unfamiliar, use `google_web_search` to find common patterns for that technology.

## Steps

## Phase 1: High-Level Analysis

1.  **Map Directory Structures:**
    - Use `list_directory` on both `old_system_path` and `new_system_path`.
    - Visually compare the two structures. Identify potential corresponding folders (e.g., `src/` vs `app/`, `includes/` vs `utils/`).

2.  **Identify Core Technologies:**
    - Look for key files that indicate the frameworks and languages used (e.g., `pom.xml` for Java/Maven, `package.json` for Node.js, `composer.json` for PHP/Composer, `requirements.txt` for Python).
    - Read these files to understand the core dependencies.

## Phase 2: Logic Discovery & Mapping

1.  **Identify Entry Points:**
    - Start by searching for common entry points or controllers. Use `grep_search` for keywords like `route`, `controller`, `handler`, `main`, or API endpoint definitions.
    - Create a list of potential business logic entry points for both systems.

2.  **Find Corresponding Files (Iterative):**
    - Pick a high-level entry point from the old system.
    - Based on its name and functionality, form a hypothesis about the corresponding file in the new system.
    - Use `grep_search` with function names, unique strings, or variable names from the old file to find its counterpart in the new system.
    - If a match is found, add the pair to your "files_to_compare" list.
    - Repeat for all significant files.

## Phase 3: Detailed Comparison

1.  **Compare File Pairs:**
    - For each pair of files in your "files_to_compare" list:
    - Read the contents of both files using `read_file`.
    - Go through function by function or method by method.
    - Describe the logic of the old function in plain English.
    - Describe the logic of the new function in plain English.
    - Compare the two descriptions.

2.  **Identify Discrepancies:**
    - **Match:** The logic is identical or functionally equivalent.
    - **Partial Match:** The core logic is similar, but with additions or modifications. Note the changes.
    - **Mismatch/Gap:** The logic is completely different, or a function from the old system is missing in the new one (or vice-versa).
    - **New Feature:** A new function or logic exists only in the new system.

## Phase 4: Reporting

1.  **Structure the Report:**
    - Save report to `reports/compare-logic/{YYMMDD-HHmm}-comparison.md` (Rule 05_6).
    - Use a structured format. For each pair of files compared, create a section.
    - For each function/logic block within those files, detail your findings using the "Match", "Partial Match", "Mismatch/Gap", "New Feature" categories.

2.  **Write a Summary:**
    - At the top of the report, write a high-level summary.
    - Mention the overall similarity score (e.g., "Approximately 70% of the business logic has been migrated with high fidelity.").
    - List the most significant discrepancies or areas that require manual review.

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "report_path": "string — path to the full comparison report",
    "similarity_score": "number (0-100) — estimated % of logic migrated with high fidelity",
    "files_compared": "number — count of file pairs analyzed",
    "discrepancies": [
      {
        "severity": "critical | high | medium | low",
        "type": "mismatch | gap | new_feature | partial_match",
        "old_location": "string (file:function)",
        "new_location": "string (file:function) | null",
        "description": "string — what differs and why it matters"
      }
    ],
    "matched_files": [
      {
        "old_path": "string",
        "new_path": "string",
        "match_quality": "exact | functional | partial"
      }
    ],
    "unmapped_old_files": ["string — old system files with no counterpart in new system"],
    "unmapped_new_files": ["string — new system files with no counterpart in old system"],
    "manual_review_required": ["string — areas too complex for automated comparison"]
  },
  "summary": "one sentence describing fidelity score and most critical discrepancy",
  "confidence": "medium"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "report_path": "reports/compare-logic/260427-1430-comparison.md",
    "similarity_score": 68,
    "files_compared": 14,
    "discrepancies": [
      {
        "severity": "critical",
        "type": "mismatch",
        "old_location": "includes/payment.php:processRefund",
        "new_location": "src/services/payment.ts:processRefund",
        "description": "Old system applies 5% restocking fee for partial refunds; new system refunds full amount — revenue impact."
      },
      {
        "severity": "high",
        "type": "gap",
        "old_location": "includes/inventory.php:adjustStock",
        "new_location": null,
        "description": "Stock adjustment on order cancellation not implemented in new system."
      }
    ],
    "matched_files": [
      { "old_path": "includes/auth.php", "new_path": "src/auth/auth.ts", "match_quality": "functional" }
    ],
    "unmapped_old_files": ["includes/legacy-export.php"],
    "unmapped_new_files": ["src/webhooks/stripe-handler.ts"],
    "manual_review_required": ["Payment reconciliation logic — complex conditional tax rules"]
  },
  "summary": "68% fidelity: 2 critical gaps in refund and stock logic require immediate attention before go-live.",
  "confidence": "medium"
}
```

**Note:** Confidence is 'medium' by default — automated comparison may miss runtime behavior nuances. Report always recommends final manual review.
