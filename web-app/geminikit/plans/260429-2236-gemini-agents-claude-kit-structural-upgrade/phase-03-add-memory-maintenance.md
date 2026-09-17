# Phase 03 — Thêm Memory Maintenance vào 6 agents

## Overview

- **Priority:** P2
- **Status:** pending
- **Effort:** 20m

## Context

Claude Kit agents có Memory Maintenance section hướng dẫn agent update memory khi phát hiện thông tin quan trọng.  
Gemini Kit có `Memory Access: READ/WRITE` trong permissions nhưng không có hướng dẫn cụ thể khi nào/gì cần save.

## Agents cần Memory Maintenance (6 agents)

Chỉ agents có `Memory Access: READ/WRITE` trong permissions:

| Agent | Memory Access |
|-------|--------------|
| architect | READ/WRITE |
| developer | READ/WRITE |
| devops | READ/WRITE |
| maintenance | READ/WRITE |
| mcp-manager | READ/WRITE |
| planner | READ/WRITE |

## Memory Maintenance Template

Thêm section này vào cuối file, sau Output section, trước Team Mode:

```markdown
---

## Memory Maintenance

Update agent memory when you discover:
- {AGENT_SPECIFIC_1}
- {AGENT_SPECIFIC_2}
- {AGENT_SPECIFIC_3}

Keep memory files concise. Use topic-specific files for overflow.
```

## Per-Agent Customization

### architect.md
```markdown
## Memory Maintenance

Update agent memory when you discover:
- Architectural decisions and their rationale (ADRs worth preserving)
- Project-specific constraints that affect design choices
- Technology stack decisions and why alternatives were rejected

Keep memory files concise. Use topic-specific files for overflow.
```

### developer.md
```markdown
## Memory Maintenance

Update agent memory when you discover:
- Project conventions and patterns not documented elsewhere
- Recurring bugs and their root causes/fixes
- Build/test/run commands specific to this project

Keep memory files concise. Use topic-specific files for overflow.
```

### devops.md
```markdown
## Memory Maintenance

Update agent memory when you discover:
- Infrastructure topology and deployment patterns
- Environment-specific configurations and their purpose
- CI/CD pipeline quirks and known issues

Keep memory files concise. Use topic-specific files for overflow.
```

### maintenance.md
```markdown
## Memory Maintenance

Update agent memory when you discover:
- High-debt areas identified during analysis
- Refactoring patterns that proved effective for this codebase
- Migration strategies and lessons learned

Keep memory files concise. Use topic-specific files for overflow.
```

### mcp-manager.md
```markdown
## Memory Maintenance

Update agent memory when you discover:
- MCP server connection states and known issues
- Tool availability per server and their quirks
- Configuration patterns that work for this project

Keep memory files concise. Use topic-specific files for overflow.
```

### planner.md
```markdown
## Memory Maintenance

Update agent memory when you discover:
- Project conventions and architectural patterns
- Recurring planning decisions and their outcomes
- Phase structures that worked well for similar features

Keep memory files concise. Use topic-specific files for overflow.
```

## Implementation Steps

1. Thêm `## Memory Maintenance` section vào 6 agents
2. Position: sau Output section, trước Team Mode section (Phase 01 đã thêm)
3. Customize theo domain knowledge của từng agent

## Success Criteria

- [ ] 6/6 agents có Memory Maintenance section
- [ ] Nội dung phù hợp với domain của agent (không generic)
- [ ] Format nhất quán
