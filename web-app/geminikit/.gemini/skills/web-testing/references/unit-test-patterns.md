# Unit Test & Performance Test Patterns Reference

Canonical patterns for Vitest unit tests and k6 performance tests.

## Vitest Configuration
```ts
// vitest.config.ts
import { defineConfig } from "vitest/config";
import react from "@vitejs/plugin-react";
import tsconfigPaths from "vite-tsconfig-paths";

export default defineConfig({
  plugins: [react(), tsconfigPaths()],
  test: {
    environment: "jsdom",          // Use "node" for pure backend tests
    globals: true,                 // No need to import describe/it/expect
    setupFiles: ["./tests/setup.ts"],
    coverage: {
      provider: "v8",
      reporter: ["text", "lcov", "html"],
      thresholds: {
        statements: 80,
        branches: 80,
        functions: 80,
        lines: 80,
      },
      exclude: ["node_modules", "*.config.*", "tests/**"],
    },
  },
});
```

## Test Setup File
```ts
// tests/setup.ts
import "@testing-library/jest-dom";  // adds toBeInTheDocument(), toHaveClass(), etc.
import { cleanup } from "@testing-library/react";
import { afterEach, vi } from "vitest";

afterEach(() => {
  cleanup();      // unmount React trees after each test
  vi.clearAllMocks(); // reset mock state
});

// Global mocks for browser APIs not available in jsdom
Object.defineProperty(window, "ResizeObserver", {
  value: vi.fn(() => ({ observe: vi.fn(), unobserve: vi.fn(), disconnect: vi.fn() })),
});

Object.defineProperty(window, "IntersectionObserver", {
  value: vi.fn(() => ({ observe: vi.fn(), unobserve: vi.fn(), disconnect: vi.fn() })),
});
```

## Unit Test: Pure Function (AAA Pattern)
```ts
// src/utils/format-currency.test.ts
import { describe, it, expect } from "vitest";
import { formatCurrency } from "./format-currency";

describe("formatCurrency", () => {
  // Happy path
  it("formats USD amounts with dollar sign and 2 decimal places", () => {
    // Arrange
    const amount = 1234.5;
    // Act
    const result = formatCurrency(amount, "USD");
    // Assert
    expect(result).toBe("$1,234.50");
  });

  // Edge cases
  it("formats zero correctly", () => {
    expect(formatCurrency(0, "USD")).toBe("$0.00");
  });

  it("handles negative amounts", () => {
    expect(formatCurrency(-99.9, "USD")).toBe("-$99.90");
  });

  // Error cases
  it("throws for non-finite values", () => {
    expect(() => formatCurrency(Infinity, "USD")).toThrow("Invalid amount");
    expect(() => formatCurrency(NaN, "USD")).toThrow("Invalid amount");
  });
});
```

## Unit Test: React Component (Testing Library)
```tsx
// src/components/UserCard/UserCard.test.tsx
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, it, expect, vi } from "vitest";
import { UserCard } from "./UserCard";

const mockUser = { id: "1", name: "Alice", email: "alice@example.com", role: "user" as const };

describe("UserCard", () => {
  it("renders user name and email", () => {
    render(<UserCard user={mockUser} />);
    expect(screen.getByText("Alice")).toBeInTheDocument();
    expect(screen.getByText("alice@example.com")).toBeInTheDocument();
  });

  it("calls onDelete when delete button is clicked", async () => {
    const user = userEvent.setup();
    const onDelete = vi.fn();
    render(<UserCard user={mockUser} onDelete={onDelete} />);
    await user.click(screen.getByRole("button", { name: /delete/i }));
    expect(onDelete).toHaveBeenCalledWith("1");
    expect(onDelete).toHaveBeenCalledTimes(1);
  });

  it("shows loading spinner when deleting", async () => {
    const user = userEvent.setup();
    const onDelete = vi.fn(() => new Promise((r) => setTimeout(r, 1000))); // slow operation
    render(<UserCard user={mockUser} onDelete={onDelete} />);
    await user.click(screen.getByRole("button", { name: /delete/i }));
    expect(screen.getByRole("status", { name: /loading/i })).toBeInTheDocument();
  });
});
```

## Mocking Modules
```ts
// Mock a module factory — runs at module level
vi.mock("@/lib/db", () => ({
  db: {
    users: {
      findMany: vi.fn(),
      findUnique: vi.fn(),
      create: vi.fn(),
      update: vi.fn(),
      delete: vi.fn(),
    },
  },
}));

// In test — configure mock return values
import { db } from "@/lib/db";

it("returns users from database", async () => {
  vi.mocked(db.users.findMany).mockResolvedValueOnce([mockUser]);
  const result = await getUserList();
  expect(result).toHaveLength(1);
  expect(result[0].name).toBe("Alice");
});

// Spy on a method without replacing module
it("calls fetch with correct URL", async () => {
  const fetchSpy = vi.spyOn(globalThis, "fetch").mockResolvedValueOnce(
    new Response(JSON.stringify({ data: [] }), { status: 200 })
  );
  await fetchProducts("electronics");
  expect(fetchSpy).toHaveBeenCalledWith(expect.stringContaining("category=electronics"), expect.any(Object));
});
```

## Integration Test: API Route
```ts
// src/routes/users.test.ts
import { describe, it, expect, beforeAll, afterAll } from "vitest";
import { app } from "@/app";
import { createTestDb, cleanupTestDb } from "@/tests/db-helpers";

describe("POST /api/users", () => {
  beforeAll(async () => { await createTestDb(); });
  afterAll(async () => { await cleanupTestDb(); });

  it("creates user and returns 201", async () => {
    const res = await app.request("/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: "Bob", email: "bob@example.com" }),
    });
    expect(res.status).toBe(201);
    const body = await res.json();
    expect(body).toMatchObject({ name: "Bob", email: "bob@example.com" });
    expect(body.id).toBeDefined();
  });

  it("returns 422 for invalid email", async () => {
    const res = await app.request("/api/users", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: "Bob", email: "not-an-email" }),
    });
    expect(res.status).toBe(422);
    const body = await res.json();
    expect(body.type).toContain("validation");
  });
});
```

## k6 Performance Test
```js
// tests/perf/api-load.js
import http from "k6/http";
import { check, sleep } from "k6";
import { Rate, Trend } from "k6/metrics";

const errorRate = new Rate("errors");
const responseTime = new Trend("response_time_ms");

export const options = {
  stages: [
    { duration: "30s", target: 10 },   // Ramp up to 10 users
    { duration: "1m", target: 50 },    // Stay at 50 users
    { duration: "30s", target: 100 },  // Peak load
    { duration: "30s", target: 0 },    // Ramp down
  ],
  thresholds: {
    http_req_duration: ["p(95)<500"],  // 95% of requests under 500ms
    http_req_failed: ["rate<0.01"],    // Error rate below 1%
    errors: ["rate<0.01"],
  },
};

const BASE_URL = __ENV.BASE_URL || "http://localhost:3000";

export default function () {
  const res = http.get(`${BASE_URL}/api/products`, {
    headers: { Authorization: `Bearer ${__ENV.API_TOKEN}` },
  });

  const success = check(res, {
    "status is 200": (r) => r.status === 200,
    "response has data": (r) => JSON.parse(r.body).data?.length > 0,
    "response time < 500ms": (r) => r.timings.duration < 500,
  });

  errorRate.add(!success);
  responseTime.add(res.timings.duration);
  sleep(1);  // Think time between requests
}
```

## Anti-Patterns

| Anti-pattern | Fix |
|---|---|
| Testing implementation details (internal state) | Test observable behavior and output |
| `vi.mock` with wrong module path | Use exact import path matching source file |
| Real API calls in unit tests | Mock with `vi.mock` or MSW |
| `expect(x).toBeTruthy()` for all assertions | Use specific matchers: `toBe`, `toEqual`, `toHaveBeenCalledWith` |
| No `beforeEach` reset for shared mocks | Add `vi.clearAllMocks()` in `afterEach` via setup file |
| k6 test without thresholds | Always define `thresholds` for p95 and error rate |
