---
title: "Gemini Agents — Claude Kit Structural Upgrade"
description: "Add behavioral checklists, Team Mode, Memory Maintenance to all 14 agents; upgrade devops/designer; replace PowerShell rule with cross-platform"
status: completed
priority: P1
effort: 3h
branch: main
tags: [agents, gemini-kit, structure, quality]
created: 2026-04-29
blockedBy: []
blocks: []
---

# Gemini Agents — Claude Kit Structural Upgrade

## Context

Brainstorm phân tích 2026-04-29 so sánh Claude Kit vs Gemini Kit agents.  
Gemini Kit đạt **8.2/10** — tốt về Input/Output contracts nhưng thiếu 3 structural features của Claude Kit.

**Brainstorm session:** `/brainstorm` command 2026-04-29

## Scope

HOLD — chỉnh sửa đúng những gì phân tích, không mở rộng.

## What's Being Added

| Feature | From Claude Kit | Applied to |
|---------|----------------|-----------|
| Behavioral Checklist | Pre-completion self-verification | 9 agents (complex ones) |
| Team Mode section | Multi-agent coordination protocol | All 14 agents |
| Memory Maintenance | Structured memory management | 6 agents (write access) |
| devops.md upgrade | Input schema + process steps | devops only |
| designer.md upgrade | Research workflow + capabilities | designer only |
| Cross-platform shell | Replace PowerShell Mandatory | All 14 agents |

## Agents List

| Agent | Checklist | Team Mode | Memory | Shell Fix | Content Upgrade |
|-------|-----------|-----------|--------|-----------|----------------|
| architect | ✅ | ✅ | ✅ | ✅ | — |
| comparator | ✅ | ✅ | — | ✅ | — |
| designer | ✅ | ✅ | — | ✅ | ✅ Major |
| developer | — (already has loop) | ✅ | ✅ | ✅ | — |
| devops | ✅ | ✅ | ✅ | ✅ | ✅ Major |
| documenter | ✅ | ✅ | — | ✅ | — |
| maintenance | ✅ | ✅ | ✅ | ✅ | — |
| mcp-manager | — | ✅ | — | ✅ | — |
| planner | — (already complex) | ✅ | ✅ | ✅ | — |
| researcher | ✅ | ✅ | ✅ | ✅ | — |
| reviewer | — (already has scoring) | ✅ | — | ✅ | — |
| security | ✅ | ✅ | — | ✅ | — |
| support | ✅ | ✅ | — | ✅ | — |
| tester | — (already has rules) | ✅ | ✅ | ✅ | — |

## Phases

| Phase | Description | Effort | Status |
|-------|-------------|--------|--------|
| [01](./phase-01-add-team-mode-all-agents.md) | Thêm Team Mode vào tất cả 14 agents | 45m | completed |
| [02](./phase-02-add-behavioral-checklist.md) | Thêm Behavioral Checklist vào 9 agents | 30m | completed |
| [03](./phase-03-add-memory-maintenance.md) | Thêm Memory Maintenance vào 6 agents | 20m | completed |
| [04](./phase-04-fix-cross-platform-shell.md) | Replace PowerShell rule → cross-platform | 15m | completed |
| [05](./phase-05-upgrade-devops-agent.md) | Nâng cấp devops.md (input schema + process) | 30m | completed |
| [06](./phase-06-upgrade-designer-agent.md) | Nâng cấp designer.md (research workflow) | 30m | completed |

## Success Criteria

- [x] Tất cả 14 agents có Team Mode section
- [x] 9 agents có Behavioral Checklist phù hợp với vai trò
- [x] 6 agents có Memory Maintenance section
- [x] Không còn "PowerShell Mandatory" trong bất kỳ agent nào
- [x] devops.md có Input schema, Process, Error Handling (score 8.5+)
- [x] designer.md có research workflow, audit mode, Design Principles
- [x] Gemini Kit agent average score từ 8.2 → 8.8+
