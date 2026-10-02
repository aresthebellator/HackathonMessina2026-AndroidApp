---
mode: post-test
extends: review
version: "1.0.0"
---

# Extra Rules

- **Primary question:** Do the tests actually verify the right behavior and catch real regressions?
- Check for **false positives** — tests that pass regardless of implementation (no meaningful assertion).
- Verify **test isolation** — unit tests MUST NOT make real network calls, DB writes, or file I/O without mocking.
- Flag missing coverage for: happy path, validation errors, edge cases, and error boundaries.
- Check that mock return types match actual module types — type-mismatched mocks give false confidence.
- Verify test names describe behavior (`it("returns 404 when user not found")`, not `it("test error")`).
- Flag `sleep()` usage in E2E tests — must use proper async waiting (Playwright auto-wait, `waitForSelector`).
- Security scan (mandatory baseline) MUST still run per base skill rules — check test files for hardcoded secrets, exposed credentials in fixtures, or unsafe `eval` in test helpers.
- Skip performance review — focus on test quality, not runtime efficiency.

# Extra Output

```json
{
  "false_positives_detected": ["string (file:line — description)"],
  "isolation_violations": ["string (file:line — real call without mock)"],
  "coverage_gaps": ["string (scenario or branch not covered)"],
  "mock_type_mismatches": ["string (file:line)"]
}
```

## Steps
1. Scan each test for meaningful assertions — flag tests with no `expect` or trivially-true assertions
2. Check all external calls (HTTP, DB, FS) are properly mocked in unit tests
3. Identify missing scenarios: validation errors, null inputs, boundary values, concurrency
4. Verify mock shapes match actual TypeScript types
5. Check test names are behavior-descriptive
6. Scan E2E tests for `sleep()` or fragile CSS selectors
7. Report `coverage_gaps` as advisory list

## Blocking Behavior
- `false_positives_detected` with count > 0 → **BLOCKING** — tests provide false confidence
- `isolation_violations` in unit tests → **BLOCKING**
- `coverage_gaps` → advisory only
- All style findings → advisory

## Examples
**Input:** `/gk-review --post-test tests/unit/auth.test.ts`
**Expected:** Detects test that always passes because mock returns undefined without type check; flags missing test for expired token scenario; confirms all DB calls are mocked.
