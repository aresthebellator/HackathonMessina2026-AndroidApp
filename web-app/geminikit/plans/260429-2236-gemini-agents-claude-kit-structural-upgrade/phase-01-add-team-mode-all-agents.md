# Phase 01 — Thêm Team Mode vào tất cả 14 agents

## Overview

- **Priority:** P1
- **Status:** pending
- **Effort:** 45m

## Context

Claude Kit agents có standardized "Team Mode" section cho multi-agent coordination.  
Gemini Kit hoàn toàn thiếu section này — không hỗ trợ TaskList/SendMessage/shutdown workflow.

## Team Mode Template (Standard)

Thêm section này vào cuối mỗi agent file:

```markdown
---

# Team Mode (when spawned as teammate)

When operating as a team member:
1. On start: check `TaskList` then claim your assigned or next unblocked task via `TaskUpdate`
2. Read full task description via `TaskGet` before starting work
3. {ROLE_SPECIFIC_RULE} — see per-agent customization below
4. When done: `TaskUpdate(status: "completed")` then `SendMessage` {DELIVERABLE} to lead
5. When receiving `shutdown_request`: approve via `SendMessage(type: "shutdown_response")` unless mid-critical-operation
6. Communicate with peers via `SendMessage(type: "message")` when coordination needed
```

## Per-Agent Customization

| Agent | Rule #3 (role-specific) | Deliverable (rule #4) |
|-------|------------------------|-----------------------|
| architect | Do NOT make code changes — report ADR and recommendations only | ADR or architecture report to lead |
| comparator | Do NOT modify source code in either system — read-only analysis | comparison report to lead |
| designer | Respect file ownership — only edit design/spec files assigned to you | design deliverables summary to lead |
| developer | Respect file ownership boundaries stated in task description — never edit files outside your boundary | implementation report to lead |
| devops | Respect file ownership — only edit infra/config files assigned to you | infra change summary to lead |
| documenter | Never modify code files — only documentation files assigned to you | doc updates summary to lead |
| maintenance | Respect file ownership — only refactor/migrate files explicitly assigned | maintenance report to lead |
| mcp-manager | Only execute MCP operations specified in task — do not modify project code files | MCP execution results to lead |
| planner | Do NOT implement code — create plans and coordinate task dependencies only | plan summary to lead |
| researcher | Do NOT make code changes — report findings and research results only | research report to lead |
| reviewer | Do NOT make code changes — report findings and recommendations only | review report to lead |
| security | Do NOT make code changes — report security findings only | security report to lead |
| support | Do NOT make code changes — report incident analysis and recommendations only | incident report to lead |
| tester | Wait for blocked tasks (implementation) to complete before testing; only create/edit test files assigned to you | test results to lead |

## Files to Modify

All 14 files:
- `.gemini/agents/architect.md`
- `.gemini/agents/comparator.md`
- `.gemini/agents/designer.md`
- `.gemini/agents/developer.md`
- `.gemini/agents/devops.md`
- `.gemini/agents/documenter.md`
- `.gemini/agents/maintenance.md`
- `.gemini/agents/mcp-manager.md`
- `.gemini/agents/planner.md`
- `.gemini/agents/researcher.md`
- `.gemini/agents/reviewer.md`
- `.gemini/agents/security.md`
- `.gemini/agents/support.md`
- `.gemini/agents/tester.md`

## Implementation Steps

1. Đọc từng file agent
2. Append Team Mode section vào cuối file (sau Error Handling nếu có)
3. Customize rule #3 và deliverable theo bảng trên
4. Verify section không conflict với Output/Handoff contract hiện có

## Success Criteria

- [ ] 14/14 agents có Team Mode section
- [ ] Mỗi agent có role-specific rule #3 phù hợp
- [ ] Format nhất quán với Claude Kit conventions
