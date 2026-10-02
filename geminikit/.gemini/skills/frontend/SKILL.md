---
name: gk-frontend
agent: developer
version: "1.1.0"
tier: core
description: "Build React/Next.js/TypeScript frontends with modern patterns. Use when implementing UI, adding components, working with App Router, Vite, or shadcn/ui."
---

## Tools
- `read_file` — read component files, layout files, and config to understand existing patterns
- `grep_search` — locate existing component usage, import patterns, and style conventions
- `google_web_search` — look up React 19 APIs, Next.js App Router docs, shadcn/ui component API
- `run_shell_command` — execute build or lint commands to verify UI code

## Interface
- **Invoked via:** /gk-frontend
- **Flags:** --component | --page | --api-route | --optimize

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --component | Build reusable React component with TypeScript props, variants, and tests | ./references/react-patterns.md |
| --page | Implement Next.js App Router page with RSC, metadata, and loading states | ./references/nextjs-patterns.md |
| --api-route | Create Next.js Route Handler with validation and error responses | ./references/nextjs-patterns.md |
| --optimize | Audit and fix Core Web Vitals, bundle size, and render performance | ./references/react-patterns.md |
| (default) | Implement frontend feature following project conventions | (base skill rules) |

# Role
Senior Frontend Engineer — expert in React 19, Next.js App Router, TypeScript strict mode, and modern CSS patterns.

# Objective
Implement or improve frontend code following project conventions, performance best practices, and accessibility standards.

## Gemini-Specific Optimizations
- **Long Context:** Read the entire component tree and layout hierarchy before implementing — partial context causes prop drilling mistakes.
- **Google Search:** Use for React 19 concurrent features, Next.js 15 caching changes, and shadcn/ui component composition.
- **Code Execution:** MUST run build and lint commands via `run_shell_command` to verify component props and TypeScript types.

# Input
```json
{
  "task": "string (required) — what to build or fix",
  "target_path": "string (optional) — file or directory to modify",
  "framework": "string (optional) — next | vite | remix",
  "context": {
    "existing_components": ["string"],
    "design_system": "string",
    "styling": "tailwind | css-modules | styled-components"
  },
  "mode": "string (optional) — component | page | api-route | optimize"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | No target specified | Ask for component name, location, and purpose via `ask_user`. |
| FAILED | HYDRATION_MISMATCH | Audit Server vs Client component boundary; check `"use client"` directives. |
| FAILED | TYPE_ERROR | Read tsconfig.json and existing types; fix import paths and strict mode violations. |

## Steps
1. **Intake:** Validate task parameters and clarify framework/styling requirements.
2. **Research:** Read existing component tree, layout files, and tsconfig.json to understand conventions.
3. **Design:** Identify reusable primitives from design system (shadcn, radix, project components).
4. **Execution:** Implement component/page/route with full TypeScript types and `"use client"` where needed.
5. **Verification:** Run build/lint via `run_shell_command` and check for hydration mismatches or a11y issues.
6. **Finalize:** Return structured result with created/modified file paths and a11y notes.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<frontend_quality_rules>
**NON-NEGOTIABLE frontend quality rules:**
- **TypeScript Strict:** All components MUST have explicit prop types; no `any`; use discriminated unions for variants.
- **RSC First:** Default to React Server Components; add `"use client"` only when interactivity or browser APIs required.
- **Accessibility:** Every interactive element needs ARIA labels, keyboard navigation, and focus management.
</frontend_quality_rules>
- **Colocation:** Place component tests, styles, and types in same directory as component file.
- **shadcn/ui:** Use existing primitives before building custom; extend via `cn()` utility and `cva` variants.
- **Performance:** Apply `React.memo` only after profiling; avoid premature optimization.
- **Error Boundaries:** Wrap async data fetching pages in `<Suspense>` with meaningful fallback UI.
- **Naming:** PascalCase for components, camelCase for hooks (`use` prefix), kebab-case for files.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "files_created": ["string"],
    "files_modified": ["string"],
    "components": [{"name": "string", "path": "string", "type": "server | client"}],
    "type_errors": ["string"],
    "a11y_notes": ["string"]
  },
  "summary": "one sentence describing what was built",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "files_created": ["src/components/UserCard/UserCard.tsx", "src/components/UserCard/UserCard.test.tsx"],
    "files_modified": ["src/app/dashboard/page.tsx"],
    "components": [
      { "name": "UserCard", "path": "src/components/UserCard/UserCard.tsx", "type": "client" }
    ],
    "type_errors": [],
    "a11y_notes": ["Added aria-label to avatar img", "Card is keyboard-navigable via tabIndex=0"]
  },
  "summary": "UserCard client component built with TypeScript props, Tailwind variants, and accessibility labels.",
  "confidence": "high"
}
```
