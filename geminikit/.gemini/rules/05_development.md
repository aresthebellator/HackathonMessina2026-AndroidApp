# 05_DEVELOPMENT: Coding & Task Execution Rules

## 1. Code Quality & Standards
- **Consistency:** Follow existing project coding style and conventions.
- **Linting/Formatting:** Ensure code passes all linting and formatting checks (e.g., ESLint, Prettier, Black, flake8) before committing.
- **Clean Code:**
  - **Naming:** Use meaningful and descriptive variable/function names.
  - **Functions:** Keep functions small and focused on a single responsibility.
  - **Complexity:** Avoid deep nesting and complex logic; refactor if necessary.
- **Comments:**
  - Explain *why*, not *what* (code should be self-explanatory).
  - Document public APIs, complex algorithms, and workarounds.

<mandatory_quality_gate>
## 2. Testing Strategy — MANDATORY
- **New Features:** MUST include unit tests (positive + negative cases). No exceptions.
- **Bug Fixes:** MUST include regression test. No exceptions.
- **Before ANY commit:** Run `/gk-quality-gate` — commit is BLOCKED if gate fails.
- **Coverage:** Aim for high test coverage for critical paths.
</mandatory_quality_gate>

## 3. Git & Version Control
- **Branching:** Use descriptive branch names (e.g., `feature/add-login`, `fix/nav-bug`).
- **Commits:**
  - **Atomic:** Keep commits small and focused on a single logical change.
  - **Messages:** Follow the conventional commits format (e.g., `feat: add login page`, `fix: resolve nav issue`).
  - **Description:** Provide context in the commit body if the change is complex.
<no_secrets>- **No Secrets:** NEVER commit API keys, passwords, or sensitive data. Use environment variables.</no_secrets>

## 4. Error Handling & Logging
- **Graceful Failure:** Handle errors gracefully; do not let the application crash unexpectedly.
- **Logging:** Use appropriate logging levels (debug, info, warn, error). Avoid console logs in production code.
- **User Feedback:** Provide clear error messages to the user when something goes wrong.

## 5. Dependency Management
- **Minimalism:** Avoid adding unnecessary dependencies.
- **Security:** Regularly check for and update vulnerable dependencies.
- **Lock Files:** Always commit lock files (`package-lock.json`, `yarn.lock`, `Pipfile.lock`, etc.) to ensure consistent installs.

## 6. Artifact & Report Management
- **6.1 Project-Centric Storage (CRITICAL):** ALL generated artifacts, including but not limited to: security audits, code reviews, architectural analyses, research reports, and session exports, MUST be stored WITHIN the project's workspace.
- **6.2 Directory Convention:** 
  - **Reports:** Use `reports/{skill-name}/{date}-{type}.md` (e.g., `reports/audit/260323-security.md`).
  - **Plans:** Use `plans/{date}-{slug}/` as defined in Rule 02_5.1.
  - **Documentation:** Use the standard `docs/` or project-specific documentation paths.
- **6.3 No External Output:** Agents MUST NOT output files to system temporary directories, user home directories, or any path outside the defined workspace directories.

---
> **Reminder:** Every commit MUST pass `/gk-quality-gate` (tests pass + review score ≥ 7). No secrets. No console.log in production.

