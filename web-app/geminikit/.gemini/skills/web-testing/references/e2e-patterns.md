# Playwright E2E Test Patterns Reference

Canonical patterns for writing reliable Playwright end-to-end tests.

## Project Structure
```
tests/
├── e2e/
│   ├── auth.spec.ts         # Auth flows: login, logout, register
│   ├── products.spec.ts     # Product browsing and search
│   ├── checkout.spec.ts     # Purchase flow
│   └── admin.spec.ts        # Admin-specific flows
├── fixtures/
│   ├── index.ts             # Custom test fixtures
│   └── users.ts             # User factory for tests
└── playwright.config.ts
```

## Playwright Config
```ts
// playwright.config.ts
import { defineConfig, devices } from "@playwright/test";

export default defineConfig({
  testDir: "./tests/e2e",
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 1 : undefined,
  reporter: "html",
  use: {
    baseURL: process.env.BASE_URL ?? "http://localhost:3000",
    trace: "on-first-retry",
    screenshot: "only-on-failure",
  },
  projects: [
    { name: "chromium", use: { ...devices["Desktop Chrome"] } },
    { name: "Mobile Chrome", use: { ...devices["Pixel 5"] } },
  ],
  webServer: {
    command: "npm run dev",
    url: "http://localhost:3000",
    reuseExistingServer: !process.env.CI,
  },
});
```

## Custom Fixtures (Page Object + Auth)
```ts
// tests/fixtures/index.ts
import { test as base, expect } from "@playwright/test";

interface UserFixture {
  loggedInPage: Page;
  adminPage: Page;
}

export const test = base.extend<UserFixture>({
  loggedInPage: async ({ page }, use) => {
    // Inject auth cookie — much faster than UI login flow
    await page.context().addCookies([{
      name: "session",
      value: process.env.TEST_SESSION_TOKEN!,
      domain: "localhost",
      path: "/",
    }]);
    await use(page);
  },

  adminPage: async ({ browser }, use) => {
    const context = await browser.newContext();
    await context.addCookies([{
      name: "session",
      value: process.env.TEST_ADMIN_SESSION_TOKEN!,
      domain: "localhost",
      path: "/",
    }]);
    const page = await context.newPage();
    await use(page);
    await context.close();
  },
});

export { expect };
```

## Page Object Model
```ts
// tests/pages/LoginPage.ts
import { type Page, type Locator } from "@playwright/test";

export class LoginPage {
  private readonly emailInput: Locator;
  private readonly passwordInput: Locator;
  private readonly submitButton: Locator;
  private readonly errorMessage: Locator;

  constructor(private page: Page) {
    // Prefer: ARIA roles and labels > data-testid > visible text > CSS selectors
    this.emailInput = page.getByRole("textbox", { name: /email/i });
    this.passwordInput = page.getByLabel(/password/i);
    this.submitButton = page.getByRole("button", { name: /sign in/i });
    this.errorMessage = page.getByRole("alert");
  }

  async goto() {
    await this.page.goto("/login");
  }

  async login(email: string, password: string) {
    await this.emailInput.fill(email);
    await this.passwordInput.fill(password);
    await this.submitButton.click();
  }

  async getErrorText() {
    return this.errorMessage.textContent();
  }
}
```

## E2E Test File Pattern
```ts
// tests/e2e/auth.spec.ts
import { test, expect } from "../fixtures";
import { LoginPage } from "../pages/LoginPage";

test.describe("Authentication", () => {
  test.describe("Login", () => {
    test("redirects to dashboard on successful login", async ({ page }) => {
      const loginPage = new LoginPage(page);
      await loginPage.goto();
      await loginPage.login("user@example.com", "ValidPass123!");
      await expect(page).toHaveURL("/dashboard");
      await expect(page.getByRole("heading", { name: "Dashboard" })).toBeVisible();
    });

    test("shows error message for invalid credentials", async ({ page }) => {
      const loginPage = new LoginPage(page);
      await loginPage.goto();
      await loginPage.login("user@example.com", "wrongpassword");
      // Do NOT use sleep() — Playwright auto-waits for the element
      const errorText = await loginPage.getErrorText();
      expect(errorText).toContain("Invalid credentials");
    });

    test("blocks access to protected pages when unauthenticated", async ({ page }) => {
      await page.goto("/dashboard");
      await expect(page).toHaveURL("/login");
    });
  });

  test.describe("Logout", () => {
    test("clears session and redirects to home", async ({ loggedInPage }) => {
      await loggedInPage.goto("/dashboard");
      await loggedInPage.getByRole("button", { name: /logout/i }).click();
      await expect(loggedInPage).toHaveURL("/");
    });
  });
});
```

## API Mocking in E2E
```ts
test("shows error state when API fails", async ({ page }) => {
  // Mock API response before navigating
  await page.route("**/api/products", (route) =>
    route.fulfill({ status: 500, body: JSON.stringify({ error: "Server error" }) })
  );
  await page.goto("/products");
  await expect(page.getByRole("alert")).toContainText("Something went wrong");
});

test("intercepts API call and verifies payload", async ({ page }) => {
  const [request] = await Promise.all([
    page.waitForRequest("**/api/checkout"),
    page.getByRole("button", { name: /buy now/i }).click(),
  ]);
  const body = request.postDataJSON();
  expect(body).toMatchObject({ productId: "prod_123", quantity: 1 });
});
```

## Visual Regression Testing
```ts
test("product card matches design", async ({ page }) => {
  await page.goto("/products/1");
  // Take screenshot of specific component only
  const card = page.getByTestId("product-card");
  await expect(card).toHaveScreenshot("product-card.png", {
    maxDiffPixels: 100, // allow minor anti-aliasing differences
  });
});
```

## Anti-Patterns

| Anti-pattern | Fix |
|---|---|
| `page.waitForTimeout(2000)` | Use `waitForSelector`, `waitForResponse`, or rely on Playwright's auto-waiting |
| CSS class selectors: `.btn-primary` | Use `getByRole`, `getByLabel`, `getByTestId` |
| Shared mutable state between tests | Use `test.beforeEach` or fixtures to reset state |
| Real login flow for every test | Inject auth cookies or use storage state for speed |
| `test.only` committed to repo | CI must fail with `forbidOnly: true` |
| Hardcoded `localhost:3000` URLs | Use `baseURL` from config + relative paths |
