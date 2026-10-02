# Gemini skill collection

This folder contains a curated set of public skills for premium UI/UX work and disciplined application development. The repositories were downloaded from GitHub and retain their original licenses and documentation. The collection contains 333 individual `SKILL.md` files.

## Design and frontend quality

- `duolingo-design-system/` — original Duolingo-inspired visual direction created for this workspace.
- `ui-design-system/` — broad UI/UX system covering visual styles, palettes, typography, UX rules, design tokens, React, Next.js, Tailwind, accessibility, and audit workflows.
- `ux-ui-skills/` — modular anti-slop and design-grounding skills for hierarchy, interaction craft, states, copy, audits, and design-context bootstrapping.
- `frontend-first-skills/` — requires Gemini to inspect existing designs before creating frontend pages and to replace dummy data with real backend data.
- `anti-slop/` — focused quality gates for removing generic AI output, improving PR hygiene, and reviewing security-sensitive changes.
- `awesome-ai-agent-skills/` — broad practical skills for code, APIs, databases, DevOps, security, design, research, analytics, writing, and communication.

## Development quality

- `tdd-xp-skill/` — Gemini CLI skill enforcing Red-Green-Refactor, behavior-focused tests, small increments, and explicit code context.
- `gemini-dev-kit/` — downloaded public Gemini development-kit repository. Its current `main` branch contains only a README, no license file, and no additional skill files to activate yet.
- `geminikit/` — Gemini-oriented toolkit with skills, project guidance, and development workflows.
- `mantis/` — engineering workflow skills for architecture, planning, reproduction, review, threat modeling, patching, and structured reporting.
- `ultraship/` — broader software delivery workflows covering planning, implementation, testing, review, release, and project operations.
- `agent-skills/` — large engineering collection covering development, architecture, testing, security, documentation, automation, and operational practices.

## Capability coverage

| Need | Primary collections |
|---|---|
| Premium UI/UX and design systems | `ui-design-system`, `ux-ui-skills`, `frontend-first-skills` |
| Playful learning-product aesthetic | `duolingo-design-system` |
| Anti-generic / anti-AI-slop quality | `ux-ui-skills`, `anti-slop` |
| Frontend implementation | `frontend-first-skills`, `awesome-ai-agent-skills`, `agent-skills` |
| Testing and TDD | `tdd-xp-skill`, `geminikit`, `agent-skills` |
| Security and threat modeling | `anti-slop`, `awesome-ai-agent-skills`, `mantis`, `agent-skills` |
| Architecture and planning | `mantis`, `ultraship`, `geminikit`, `agent-skills` |
| Debugging and code review | `mantis`, `ultraship`, `anti-slop`, `agent-skills` |
| Documentation and communication | `awesome-ai-agent-skills`, `agent-skills` |
| DevOps and deployment | `awesome-ai-agent-skills`, `ultraship`, `agent-skills` |

## Recommended activation order

1. Use `frontend-first-skills` and `ux-ui-skills` before designing or integrating a feature.
2. Use `ui-design-system` for visual direction, tokens, responsive behavior, and accessibility.
3. Use `duolingo-design-system` only when the requested product direction calls for that particular playful learning aesthetic.
4. Use `tdd-xp-skill` or the relevant `geminikit` workflow for implementation and bug fixes.
5. Use `anti-slop` before finalizing UI or a pull request.
6. Use the relevant security, testing, architecture, and deployment skills from the broader collections as the task requires.

These are inspiration and workflow aids, not substitutes for the project’s actual requirements, existing components, backend contracts, accessibility checks, or product decisions.
