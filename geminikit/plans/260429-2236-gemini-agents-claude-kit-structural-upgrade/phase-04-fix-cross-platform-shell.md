# Phase 04 — Replace PowerShell rule → Cross-platform shell

## Overview

- **Priority:** P2
- **Status:** pending
- **Effort:** 15m

## Context

Tất cả Gemini Kit agents hiện có 2 rules:
```
- **PowerShell Mandatory:** MUST use PowerShell-compatible syntax for all shell commands (PowerShell 7+ preferred).
- **Windows Pathing:** MUST use backslashes `\` for paths or properly quote paths containing spaces.
```

Project chạy macOS/Linux. Rules này gây noise và sai context.

## Replacement Rule

Thay cả 2 rules bằng 1 rule:

```markdown
- **Shell Syntax:** Use platform-appropriate shell syntax (bash/zsh on Unix/macOS, PowerShell on Windows). For cross-platform scripts, prefer POSIX-compatible syntax.
```

## Files to Modify

Tất cả 14 agents có chứa "PowerShell Mandatory" hoặc "Windows Pathing":

```
.gemini/agents/architect.md
.gemini/agents/comparator.md
.gemini/agents/designer.md
.gemini/agents/developer.md
.gemini/agents/devops.md
.gemini/agents/documenter.md
.gemini/agents/maintenance.md
.gemini/agents/mcp-manager.md
.gemini/agents/planner.md
.gemini/agents/researcher.md
.gemini/agents/reviewer.md
.gemini/agents/security.md
.gemini/agents/support.md
.gemini/agents/tester.md
```

## Implementation Steps

1. Grep để confirm pattern chính xác trong mỗi file
2. Replace 2 lines PowerShell + Windows Pathing bằng 1 line Shell Syntax
3. Verify không còn "PowerShell" mention nào trong Rules sections

## Grep Pattern

```bash
grep -n "PowerShell\|Windows Pathing" .gemini/agents/*.md
```

## Success Criteria

- [ ] 0 occurrences of "PowerShell Mandatory" trong tất cả agents
- [ ] 0 occurrences of "Windows Pathing" trong tất cả agents
- [ ] Cross-platform Shell Syntax rule có mặt trong agents có shell access
