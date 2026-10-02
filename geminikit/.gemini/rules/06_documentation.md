# 06_DOCUMENTATION: Document Management Rules

## 1. Documentation Structure & Location
- **README.md:** Project overview, quick start, installation, and key concepts. Keep this high-level.
- **docs/:** Detailed guides, architecture, API references, and tutorials. Organize logically (e.g., `docs/architecture`, `docs/guides`).
- **.gemini/:** System-specific documentation (agents, rules, skills, memory).
- **Inline Comments:** Brief explanations within code for complex logic (avoid stating the obvious).
- **CHANGELOG.md:** Maintain a record of significant changes, version history, and release notes.

## 2. Documentation Standards
- **Clarity & Conciseness:** Write clearly and concisely. Avoid jargon unless necessary and defined.
- **Formatting:**
  - **Markdown:** Use standard Markdown syntax for text.
  - **Diagrams:** Use Mermaid for diagrams (flowcharts, sequence diagrams, etc.) where visual aids help.
  - **Code Blocks:** Use syntax highlighting for code snippets.
- **Consistency:** Maintain a consistent voice and style throughout documentation.
- **Up-to-Date:** Update documentation *with* code changes. Outdated docs are misleading.

## 3. API & Code Documentation
- **JSDoc/Docstrings:** Use standard formats (JSDoc for JS/TS, Docstrings for Python) for all public functions, classes, and modules.
- **Parameters & Returns:** Clearly document input parameters, return values, and possible exceptions.
- **Examples:** Provide usage examples for complex APIs.

## 4. Maintenance & Review
- **Review Process:** Documentation changes should be reviewed as part of the code review process (PRs).
- **Broken Links:** Regularly check for and fix broken links (internal and external).
- **Cleanup:** Remove obsolete documentation or archive it if no longer relevant.
- **Versioning:** If applicable, maintain documentation versions corresponding to software versions.

## 5. Security in Documentation
- **No Secrets:** NEVER include sensitive information (API keys, passwords, internal IPs) in documentation.
- **Sanitization:** Sanitize logs, screenshots, and code examples before adding them to docs.
