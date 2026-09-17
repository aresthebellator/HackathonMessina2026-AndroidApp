# Phase 06 — Nâng cấp designer.md

## Overview

- **Priority:** P2
- **Status:** pending
- **Effort:** 30m

## Context

designer.md hiện tại score **7.6/10** — kém xa Claude Kit's ui-ux-designer.md.  
Thiếu: research workflow, asset generation capabilities, comprehensive skill list.

## Current State

designer.md hiện có:
- spec mode: basic layout + component specs
- review mode: scoring + issue list
- Thiếu: research phase, design system reference, accessibility tools, image generation

## Changes Required

### 1. Expand Skills Section

Thay:
```markdown
# Skills

- `gk-ui` — visual spec generation and UI quality review
```

Bằng:
```markdown
# Skills

- `gk-ui` — visual spec generation and UI quality review
- `gk-research` — design research and trend analysis (delegate to `researcher` agent)
- `gk-analyze` — design system audit and consistency analysis

# Tools

- File read/write: design specs and asset files
- Shell: run image processing scripts (ImageMagick, etc.)
- Web search: design references, accessibility guidelines
- File output: → See `.gemini/tools/file-output-rules.md`
```

### 2. Expand Input Schema

Thêm `design_system` và `research_required` fields:

```json
{
  "task": "string — describe UI to design or validate",
  "mode": "string — spec | review | audit (required)",
  "context": {
    "tech_stack": ["string"],
    "design_system": {
      "ref": "string — path to design-guidelines.md or Figma URL",
      "tokens": ["string — color/spacing/typography tokens if known"]
    },
    "requirements": ["string — functional/visual requirements"],
    "files": ["string — implemented files to review (review mode only)"],
    "research_required": "boolean — if true, delegate trend research to researcher agent"
  }
}
```

### 3. Add Research Phase to Process

**spec mode** — thêm Research step trước Analysis:

```markdown
**spec mode:**
0. Research phase (if `research_required: true`) — delegate to `researcher` agent:
   - Query: design trends for {context}, accessibility best practices, similar product UI patterns
   - Wait for research report before proceeding
1. Analyze requirements — extract visual intent and component boundaries
2. Read design system — load tokens, existing components from `context.design_system.ref`
3. Define layout structure — grid, spacing, breakpoints (mobile-first)
4. Specify each component — states, variants, design tokens
5. Output accessibility requirements (contrast, focus, ARIA)
6. If assets needed — invoke `gk-ui` for generation or provide asset specifications

**review mode:**
1. Load implemented files from `context.files`
2. Load design system reference if available
3. Compare against design standards: spacing, color, typography, consistency
4. Check accessibility: contrast ratio ≥ 4.5:1, focus states, ARIA attributes
5. Score and classify issues by severity (0-100, deduct: critical=20, high=10, medium=5, low=1)

**audit mode (new):**
1. Scan all UI files in codebase
2. Extract all colors, spacing values, typography declarations
3. Identify deviations from design system tokens
4. Report inconsistencies grouped by: color/spacing/typography/component
```

### 4. Add Design Principles Section

```markdown
# Design Principles

- **Mobile-First:** Designs start at 320px and scale up
- **Accessibility:** WCAG 2.1 AA minimum (4.5:1 contrast, focus states, ARIA)
- **Consistency:** All specs reference existing design tokens before introducing new ones
- **Evidence-Based:** spec mode uses research findings; review mode uses code as truth
- **Spec Precision:** Specs must be precise enough for developer to implement without questions
```

### 5. Add audit mode to Output section

```markdown
- **If audit:** design_system_coverage (%), token violations list — each with file:line, property, current_value, expected_token; consistency_score (0-100)
```

## Files to Modify

- `.gemini/agents/designer.md`

## Implementation Steps

1. Read current designer.md fully
2. Expand Skills section + add Tools section
3. Update Input JSON schema
4. Rewrite Process section (3 modes: spec, review, audit)
5. Add Design Principles section
6. Update Output section with audit mode

## Success Criteria

- [ ] designer.md có Tools section
- [ ] designer.md có research_required option trong input
- [ ] designer.md có research phase trong spec mode
- [ ] designer.md có audit mode
- [ ] designer.md có Design Principles section
- [ ] designer.md score estimate: 8.5+/10
