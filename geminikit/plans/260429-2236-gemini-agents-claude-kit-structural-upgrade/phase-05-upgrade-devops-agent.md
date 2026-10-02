# Phase 05 — Nâng cấp devops.md

## Overview

- **Priority:** P1
- **Status:** pending
- **Effort:** 30m

## Context

devops.md hiện tại score **6.2/10** — yếu nhất trong Gemini Kit.  
Thiếu: input schema, explicit process steps, insufficient output format.

## Current State Issues

1. Không có Input section (chỉ có Rules + Output)
2. Không có Process section (không có numbered steps)
3. Output section quá tối giản
4. Skills section không đầy đủ (thiếu monitoring/logging skills)

## Changes Required

### 1. Thêm Input Schema (sau # Skills)

```markdown
# Input

```json
{
  "task": "string (required) — infra or deployment task description",
  "context": {
    "tech_stack": ["string — e.g. Docker, K8s, Terraform, GitHub Actions"],
    "environment": "string — dev | staging | production",
    "target_service": "string — service or component being deployed/configured",
    "existing_configs": ["string — paths to existing infra files for reference"]
  },
  "mode": "string — provision | deploy | configure | monitor | rollback (default: deploy)"
}
```

**Field rules:**
- `task`: required, non-empty — describe ONE operation, not a multi-step sequence
- `mode=rollback`: requires previous deployment reference or config backup path
- `environment=production`: triggers additional safety checks before execution
```

### 2. Thêm Process Section (sau Input)

```markdown
# Process

1. **Read existing infra** — load all files in `context.existing_configs`; never modify what you haven't read
2. **Identify scope** — list all files to create/modify/delete and services affected
3. **Safety check** — if `environment=production`: enumerate risks, ask for confirmation before proceeding
4. **Execute changes** — apply changes idempotently; log every action
5. **Verify result** — test connectivity, service health, or deployment status after changes
6. **Document** — record what changed, why, and how to revert

**Rollback rule:** Every change MUST have a documented revert path before execution.
```

### 3. Update Rules Section

Thêm sau Access Control:
```markdown
- **Production gate** — `environment=production` operations require risk enumeration before execution; never silent-deploy to prod.
- **Rollback first** — document revert path before applying any change.
- **Verify after** — always test service health post-deployment; do not report complete without verification.
```

### 4. Expand Output Section

```markdown
# Output

> **Handoff contract** — structured data passes via handoff file only. User-facing responses use human-readable format per `04_output.md`.

- **Status:** completed | failed | blocked | rolled_back
- **Artifacts:** infra/config files created/modified/deleted with summaries
- **Environment:** dev | staging | production
- **Mode:** provision | deploy | configure | monitor | rollback
- **Verification:** health check result or deployment confirmation
- **Revert path:** how to undo the changes applied
- **Blockers:** issues that prevented completion
- **Next steps:** recommended follow-up actions

# Error Handling

| Situation | Action |
|-----------|--------|
| `environment=production` without risk review | Stop — enumerate risks, await explicit confirmation |
| Missing `existing_configs` for complex change | Request paths before proceeding |
| Idempotency cannot be guaranteed | Flag as risk, propose alternative approach |
| Rollback needed | Execute revert path, report as `status: "rolled_back"` |
| Secret detected in config | Flag as critical, remove before writing to file |
```

## Files to Modify

- `.gemini/agents/devops.md`

## Implementation Steps

1. Read current devops.md fully
2. Insert Input section after Skills section
3. Insert Process section after Input section
4. Update Rules section (add 3 new rules)
5. Replace minimal Output section with expanded version including Error Handling table

## Success Criteria

- [ ] devops.md có Input JSON schema
- [ ] devops.md có Process section với numbered steps
- [ ] devops.md có Error Handling table
- [ ] devops.md có production gate rule
- [ ] devops.md score estimate: 8.5+/10
