# Parallel Execution Patterns Reference

Strategies for maximizing parallelism in multi-agent development workflows.

## Dependency Analysis Framework

### Task Dependency Graph
```
# Analyze plan phases for dependency chains:

Sequential (cannot parallelize):
  Phase 1 (DB Schema) → Phase 2 (API Routes) → Phase 3 (API Tests)
  Reason: Each depends on outputs of previous

Parallel (safe to run concurrently):
  Phase 2 (API Routes) ∥ Phase 4 (UI Components)
  Reason: UI can use mock data; API contract defined upfront

Mixed:
  Phase 1 → [Phase 2 ∥ Phase 4] → Phase 5 (Integration Tests)
```

### Parallelization Checklist
```
For each task pair (A, B), check:
  □ A does not write files that B reads?
  □ B does not write files that A reads?
  □ A and B do not share any mutable state?
  □ Neither depends on the other's runtime output?
  □ Both have clearly defined, non-overlapping file ownership?

If all boxes checked → safe to parallelize
If any box unchecked → sequential or restructure
```

## API Contract Pattern (Enables Frontend/Backend Parallelism)

### Define Contract Before Spawning
```ts
// docs/api-contract.ts — define BEFORE spawning parallel agents
// Both backend (implements) and frontend (consumes) get this as read-only input

export interface UserResponse {
  id: string;
  name: string;
  email: string;
  role: "user" | "admin";
  createdAt: string;  // ISO 8601
}

export interface CreateUserRequest {
  name: string;
  email: string;
}

export interface PaginatedResponse<T> {
  data: T[];
  nextCursor: string | null;
  total: number;
}

// Endpoints:
// GET  /api/users          → PaginatedResponse<UserResponse>
// POST /api/users          → UserResponse (201)
// GET  /api/users/:id      → UserResponse (404 if not found)
// PATCH /api/users/:id     → UserResponse
// DELETE /api/users/:id    → 204 No Content
```

### Frontend Mock During Parallel Development
```ts
// src/lib/api-mock.ts — frontend uses this while backend is being built
import type { UserResponse } from "docs/api-contract";

const MOCK_USERS: UserResponse[] = [
  { id: "1", name: "Alice", email: "alice@example.com", role: "user", createdAt: "2024-01-01T00:00:00Z" },
  { id: "2", name: "Bob", email: "bob@example.com", role: "admin", createdAt: "2024-01-02T00:00:00Z" },
];

// Toggle via env var — dev uses mock, production uses real API
export const apiClient = {
  getUsers: async () =>
    process.env.NEXT_PUBLIC_USE_MOCK === "true"
      ? { data: MOCK_USERS, nextCursor: null, total: 2 }
      : realApiClient.getUsers(),
};
```

## Parallel Phase Execution Example

### Plan Structure (3-Agent Parallel)
```
Phase 1: Database Setup (SEQUENTIAL - prerequisite)
  Owner: backend-dev
  Output: src/db/schema.ts (exported types)

Phase 2: Backend API (PARALLEL with Phase 3)
  Owner: backend-dev
  Input: src/db/schema.ts
  Output: src/routes/**, docs/api-endpoints.md

Phase 3: Frontend Components (PARALLEL with Phase 2)
  Owner: frontend-dev
  Input: docs/api-contract.ts (defined before phase starts)
  Output: src/components/**, src/app/**

Phase 4: Integration Tests (SEQUENTIAL - after 2 & 3)
  Owner: tester
  Input: Phase 2 + Phase 3 outputs
  Output: tests/**
```

### Lead Orchestration Script
```ts
// Pseudo-code for lead agent orchestration
async function orchestrateParallelPhases() {
  // Phase 1: Sequential setup
  await createTask("Setup database schema", { ownership: "src/db/**" });
  await waitForTask(1);  // Block until DB schema done

  // Phase 2 + 3: Parallel
  const [backendTask, frontendTask] = await Promise.all([
    createTask("Implement API routes", {
      ownership: "src/routes/**",
      context: { reads: ["src/db/schema.ts", "docs/api-contract.ts"] }
    }),
    createTask("Build UI components", {
      ownership: "src/components/**, src/app/**",
      context: { reads: ["docs/api-contract.ts"] }
    }),
  ]);

  await Promise.all([waitForTask(backendTask.id), waitForTask(frontendTask.id)]);

  // Phase 4: Sequential tests after parallel work
  await createTask("Write integration tests", {
    ownership: "tests/**",
    context: { reads: ["src/**"] }
  });
}
```

## Merge Strategy

### After Parallel Work Completes
```bash
# Scenario: backend in feature/backend-api, frontend in feature/frontend-ui

# Step 1: Review and merge backend
git checkout main
git merge feature/backend-api --no-ff
# Run tests: npm test

# Step 2: Review and merge frontend
git merge feature/frontend-ui --no-ff
# Run tests: npm test -- --coverage

# Step 3: Integration test
npm run test:e2e

# Step 4: Cleanup
git branch -d feature/backend-api feature/frontend-ui
git worktree remove ../project-backend
git worktree remove ../project-frontend
```

### Conflict Resolution Decision Tree
```
Merge conflict detected in shared file?
  ├── Yes, in src/types/** →
  │     Lead owns types; lead picks correct version; update both agents
  ├── Yes, in package.json →
  │     Merge both dependency lists; run npm install; verify no conflicts
  ├── Yes, in config file →
  │     Lead reviews both configs; manually merge settings; test
  └── Yes, in source code →
        Two agents wrote same file? → Ownership violation, escalate immediately
        Same file, different sections? → Use git merge with careful review
```

## Performance Analysis

### Speedup Calculation
```
Sequential baseline:
  Phase 1: 30min
  Phase 2: 2hr
  Phase 3: 2hr
  Phase 4: 1hr
  Total: 5.5hr

Parallel execution:
  Phase 1: 30min (must run first)
  Phase 2 ∥ Phase 3: max(2hr, 2hr) = 2hr
  Phase 4: 1hr
  Total: 3.5hr

Speedup: 5.5h / 3.5h = 1.57x

Note: Coordination overhead ~15min; net speedup still significant for tasks > 1hr
Rule of thumb: parallelization worthwhile when independent tasks each take > 45min
```

## Anti-Patterns

| Anti-pattern | Impact | Fix |
|---|---|---|
| Two agents writing the same file | Merge conflicts, data loss | One owner per file, always |
| Agent A reading Agent B's in-progress output | Race condition, partial data | Sequential dependency or handoff point |
| Spawning > 5 agents | Coordination overhead exceeds speedup | Max 3-4 implementation agents |
| No API contract before parallel spawn | Frontend/backend incompatible | Define contract doc before any spawn |
| Agent checking other agent's file during work | Tight coupling, defeats parallelism | Trust task board status, not file state |
| Merging partial work before agent completes | Integration failures | Wait for task COMPLETED status |
