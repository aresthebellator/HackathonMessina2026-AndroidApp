# Contributing to Gemini Kit

We welcome contributions! Please follow these guidelines to ensure consistency and quality.

## Development Workflow

1.  **Fork** the repository and clone your fork.
2.  **Branch** off `main` for your feature or fix: `git checkout -b feature/your-feature-name`.
3.  **Implement** your changes.
    - If adding a new agent/skill, follow the template in `.gemini/template/`.
    - If modifying core logic, update relevant tests.
    - If modifying hooks, ensure backward compatibility with `.gemini/settings.json` registration.
4.  **Validate** your changes locally.
    - Run existing tests: `npm test`
    - Check Python scripts in `bridge/` for syntax: `python3 -m py_compile bridge/*.py`
    - Ensure hooks execute without errors via `gk doctor`
5.  **Commit** with clear messages (Conventional Commits preferred): `feat: add new skill for database migration`.
6.  **Push** to your fork and submit a **Pull Request**.
    - Include a description of changes and testing approach
    - Reference any related issues or PRs
    - Ensure all CI checks pass

## Coding Standards

### Languages

| Language | Usage |
|----------|-------|
| **TypeScript / JavaScript** | CLI (`bin/`, `src/`), Node.js logic, skill scripts |
| **Python** | Automation scripts (`.gemini/scripts/`) |
| **Markdown** | Agent definitions, skill prompts, documentation |

### Style
- Variables: `camelCase` (JS/TS), `snake_case` (Python)
- Classes: `PascalCase` (all languages)
- Files: `kebab-case` for JS/TS/Python; respect existing conventions
- **Documentation**: All new features must include docs in `docs/` and docstrings in code.
- **Testing**: Add unit tests for logic-heavy components.

## Understanding the Native Hooks System

Gemini Kit v1.2.1+ uses five lifecycle hooks for automatic state management:

| Hook | File | Purpose |
|------|------|---------|
| SessionStart | `session-start.js` | Load context from long-term memory into short-term |
| AfterModel | `after-model.js` | Auto-summarize when token threshold (25K) or turn interval (10) reached |
| PreCompress | `pre-compress.js` | Snapshot short-term to long-term before Gemini CLI prunes history |
| AfterTool | `after-tool.js` | Log tool calls and trigger post-write processing (phase scaffolding, registry sync) |
| SessionEnd | `session-end.js` | Compress long-term memory and reset short-term |

**Memory Files** (`.gemini/memory/`):
- `short-term.md`: Session-specific context (reset on SessionEnd)
- `long-term.md`: Cross-session knowledge (max 15 entries, auto-compressed)
- `execution.md`: Tool invocation audit trail
- `pinned.md`: Immutable system instructions

When modifying hooks or memory-related features:
- Test with `gk doctor --fix` to validate hook execution
- Ensure backward compatibility with existing memory files
- Document changes in CHANGELOG.md under the appropriate version

## Adding New Skills

1.  Create a folder in `.gemini/skills/<skill-name>`.
2.  Create `SKILL.md` following the v2.0.0 schema (see [SKILLS_GUIDE.md](../docs/SKILLS_GUIDE.md)).
3.  Include all required sections: Tools, Role, Objective, Input/Output Schema, Error Recovery, Gemini-Specific Optimizations.
4.  Validate skill with: `node .claude/scripts/validate-skill.js .gemini/skills/<skill-name>/SKILL.md`
5.  Update `.gemini/REGISTRY.md` to register the new skill (auto-synced by `after-tool.js` hook).
6.  Add a corresponding command if applicable.
7.  Add skill to appropriate category in [SKILLS_GUIDE.md](../docs/SKILLS_GUIDE.md).

## License

By contributing to Gemini Kit, you agree that your contributions will be licensed under the [MIT License](../LICENSE) that covers this project.

You retain copyright of your own contributions. By submitting a pull request, you confirm that:
- You have the right to submit the work under the MIT License.
- You understand your contribution may be distributed under the MIT License.

## Reporting Bugs

Please use the issue tracker to report bugs. Include:
- Steps to reproduce.
- Expected behavior.
- Actual behavior.
- Environment details (OS, version).
