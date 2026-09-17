---
name: file-output-rules
version: "1.0.0"
description: "Cross-skill rules for writing human-readable output files when output is large or skill produces a report."
---

# Tool: file-output-rules

## Purpose

Rules for writing output files to disk for user consumption. Reference from any skill
that may produce large output or report-style results.

## File Output Rules

- MUST write Markdown file when: output > 100 lines OR skill.type = "report"
- MUST NOT write file if output ≤ 100 lines AND skill.type ≠ "report"
- Location: `plans/reports/{YYYYMMDD}-{slug}.md` (use skill name as slug base)
- **Output Directory Constraint:** All output files MUST be written exclusively within the `plans` directory and must NOT be written outside the current project's root.
- Response MUST include `"output_file": "path/to/file.md"` in result object
- File format: Markdown (human-readable); JSON response envelope provides machine-readable form

## Usage in Skills
...
Add one line to skill's `# Rules` section:

```
- File output: → See .gemini/tools/file-output-rules.md
```

## Applies To

- .gemini/skills/review/SKILL.md
- .gemini/skills/debug/SKILL.md
- .gemini/skills/analyze/SKILL.md
- .gemini/skills/plan/SKILL.md
- .gemini/skills/design/SKILL.md
- .gemini/skills/brainstorm/SKILL.md
