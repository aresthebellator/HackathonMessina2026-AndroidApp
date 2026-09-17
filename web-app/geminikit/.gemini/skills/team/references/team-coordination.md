# Team Coordination Reference

Patterns for orchestrating multi-agent teams in Gemini Kit.

## Team Structure Design Principles

### Ownership-First Design
```yaml
# Before spawning any agent, design ownership map:
ownership_map:
  frontend-dev:
    owns:
      - "src/components/**"
      - "src/app/**"
      - "src/hooks/**"
    reads:
      - "src/types/**"        # Shared types — read-only for frontend
      - "src/db/schema.ts"   # To understand data shapes
      - "docs/api-spec.md"   # API contract from backend dev

  backend-dev:
    owns:
      - "src/routes/**"
      - "src/db/**"
      - "src/middleware/**"
    reads:
      - "src/types/**"        # Shared types — read-only

  tester:
    owns:
      - "tests/**"
    reads:
      - "src/**"              # Read all source, never edit

  lead:
    owns:
      - "src/types/**"        # Shared types — lead resolves conflicts
      - "docs/**"
      - "*.config.*"
```

### Task Board Setup
```
BEFORE spawning agents, create all tasks in the task board:

Task #1: Implement database schema [backend-dev]
  - File ownership: src/db/**
  - Blocked by: none
  - Blocks: #2, #3

Task #2: Implement API routes [backend-dev]
  - File ownership: src/routes/**
  - Blocked by: #1 (needs schema)
  - Blocks: #4

Task #3: Implement UI components [frontend-dev]
  - File ownership: src/components/**
  - Blocked by: none (can start with mock data)
  - Blocks: #4

Task #4: Integration testing [tester]
  - File ownership: tests/**
  - Blocked by: #2, #3
  - Blocks: none
```

## Agent Spawn Protocol

### Context Isolation Template
```
Task: [Specific task description — one concrete deliverable]
Files to create/modify (your ownership):
  - src/routes/users.ts
  - src/routes/posts.ts
Files to read for context (read-only):
  - src/db/schema.ts — understand data model
  - docs/api-spec.md — API contract
  - src/types/index.ts — shared TypeScript types

Acceptance criteria:
  1. POST /api/users returns 201 with user object
  2. GET /api/users/:id returns 404 if user not found
  3. All inputs validated with Zod
  4. RFC 7807 error format used

Constraints:
  - Do NOT modify src/db/** (owned by backend-dev task #1)
  - Do NOT create new files outside ownership list
  - Use Hono framework (project standard)

Work context: /Users/hieund/Sources/myproject
Reports: /Users/hieund/Sources/myproject/plans/reports/
```

### Anti-Pattern: Context Dumping
```
BAD: "Continue from where we left off on the authentication system we discussed earlier..."
GOOD: "Implement JWT middleware in src/middleware/auth.ts with the spec in docs/auth-api.md"

BAD: "Fix the issues we found in the code review"
GOOD: "Fix null check missing at src/routes/users.ts:47 (root cause: user lookup returns null when not found)"

BAD: "Look at the codebase and figure out what needs to be done"
GOOD: "Read src/routes/users.ts and add POST /users endpoint following the pattern in src/routes/posts.ts"
```

## Communication Patterns

### Lead ↔ Agent Communication
```
Lead monitors task board — agents update tasks to communicate:
  - "in_progress" = working
  - "completed" = done, review my output
  - "blocked" = need help, lead should read my last message

Agents send messages for:
  - DONE: Summary of what was implemented + file paths
  - BLOCKED: What's blocking + what's needed to unblock
  - NEEDS_CONTEXT: Specific question to unblock

Lead NEVER broadcasts unless critical blocker affecting all agents.
```

### Message Format (Agents to Lead)
```
# Completion Report

**Status:** DONE

**Implemented:**
- POST /api/users — Zod validated, 201 response
- GET /api/users/:id — 404 handling with RFC 7807
- src/routes/users.ts (modified)
- src/middleware/auth.ts (created)

**Next:** Task #3 (frontend) unblocked — API contract documented in docs/api-spec.md

**Docs impact:** minor — updated docs/api-spec.md with new endpoints
```

## Conflict Resolution

### Shared File Conflict Prevention
```
If two agents need the same file:

Option 1: Lead handles the file
  - Assign shared file to lead
  - Both agents receive file as read-only
  - Lead merges changes after both complete

Option 2: Sequential dependency
  - Agent A owns the file, completes first
  - Agent B reads Agent A's output, makes incremental additions
  - No concurrent writes

Option 3: Split the file
  - Create agent-a-feature.ts and agent-b-feature.ts
  - Create index.ts as aggregator (lead-owned)

Anti-pattern to avoid:
  - Two agents owning "src/types/**" — always one owner
  - Agent modifying config files during parallel phase
  - Agent reading another agent's in-progress work
```

## Git Worktree Setup
```bash
# Setup worktrees for parallel implementation
git worktree add ../project-backend feature/backend-api
git worktree add ../project-frontend feature/frontend-ui

# Each agent works in their worktree:
# backend-dev: /path/to/project-backend (feature/backend-api branch)
# frontend-dev: /path/to/project-frontend (feature/frontend-ui branch)

# After completion, lead merges worktrees:
cd /path/to/project
git merge feature/backend-api --no-ff -m "feat: implement backend API"
git merge feature/frontend-ui --no-ff -m "feat: implement frontend UI"

# Cleanup worktrees after merge:
git worktree remove ../project-backend
git worktree remove ../project-frontend
```

## Shutdown Protocol
```
When lead wants to shut down team:
1. Lead sends shutdown_request to each agent
2. Agent finishes current atomic operation (don't stop mid-file-write)
3. Agent marks current task as completed or paused with note
4. Agent sends shutdown_response: { requestId, approved: true }
5. Lead waits for all approvals before terminating session

Agent rejecting shutdown (mid-critical-operation):
  shutdown_response: { requestId, approved: false, reason: "Writing migration file, done in ~30s" }
  After completing: send shutdown_response: { requestId, approved: true }
```

## Team Size Recommendations

| Task Type | Team Size | Rationale |
|---|---|---|
| Single feature | 1 agent | Parallelization overhead not worth it |
| Feature with tests | 2 (implementer + tester) | Clean separation, tester verifies finished code |
| Full-stack feature | 3 (backend + frontend + tester) | True parallelism possible with API contract |
| Complex system | 4-5 max | Coordination cost grows quadratically |
| Any team | +1 lead | Lead coordinates, resolves conflicts, owns shared files |
