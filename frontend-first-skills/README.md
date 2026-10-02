# Frontend Design-First Development

[![GitHub stars](https://img.shields.io/github/stars/olakunlevpn/olakunlevpn-frontend-first-skills?style=flat-square)](https://github.com/olakunlevpn/olakunlevpn-frontend-first-skills/stargazers)
[![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen.svg?style=flat-square)](LICENSE.md)

Forces your AI coding agent to always check a pre-built design folder before creating any frontend page. No more pages created from scratch. No more dummy data in production. No more broken design consistency.

This is an **agent skill** that works with Claude Code, Cursor, Cline, Gemini CLI and 40+ other AI coding agents.

## Installation

```bash
npx skills add olakunlevpn/olakunlevpn-frontend-first-skills
```

## The Problem

You spend hours designing your frontend -- creating components, establishing a design system, building pixel-perfect pages in React or Vue. Then you ask your AI agent to create a new page and it:

- Creates from scratch, ignoring your existing designs
- Invents its own layout that doesn't match your design system
- Leaves "John Doe", "Lorem ipsum", and "$99.99" everywhere
- Adds custom CSS that breaks your design consistency
- Assumes a design doesn't exist without even checking

Your carefully crafted design system falls apart page by page.

## The Solution

This skill enforces a strict design-first workflow:

1. **Check the design folder first.** Before writing any frontend code, the agent MUST list and search the design directory.
2. **Use existing designs.** Found a matching design? Copy it, integrate backend data, done.
3. **Adapt the closest match.** No exact design? Find the most similar page, copy it, make minimal changes.
4. **Never create from scratch.** Only as an absolute last resort, and only after asking you first.
5. **Zero dummy data.** Every hardcoded value gets replaced with real backend data.

## How to Use

**Start a project integration:**
```
Here is our frontend design folder: /resources/js/designs/
Integrate all pages with Inertia and backend data.
```

**Create a specific page:**
```
Create the orders list page. Check the design folder first.
```

**When a design is missing:**
```
I need a user profile page but there's no design. Find the closest match and adapt it.
```

**Verify integration:**
```
Check all pages in the orders section for dummy data and design compliance.
```

## What Happens

When you ask for a new page, the agent will:

```
Step 1: "Let me check the design folder..."
        -> Lists all files in your design directory

Step 2: "Found designs/pages/orders/OrderList.tsx"
        -> Reads the full file

Step 3: "I see 12 instances of dummy data to replace:
         - 'John Doe' -> user.name
         - '$99.99' -> order.total
         - Mock array of 5 orders -> orders prop from backend
         ..."

Step 4: Copies the design to its final location

Step 5: Integrates Inertia props, replaces ALL dummy data

Step 6: Verifies zero dummy data remains
```

If no matching design exists:

```
Step 1: "No exact design found for User Profile.
         The closest match is designs/pages/settings/AccountSettings.tsx"

Step 2: "I'll copy and adapt it with minimal changes
         to maintain design uniformity."
```

## What It Prevents

| Without This Skill | With This Skill |
|---|---|
| Agent creates pages from scratch | Agent copies from design folder |
| "Lorem ipsum" in production | Zero dummy data, all backend data |
| Inconsistent layouts | 100% uniform design across all pages |
| Custom CSS everywhere | Design system respected, no overrides |
| Agent assumes designs don't exist | Agent always checks the folder first |
| Design system falls apart over time | Design system stays intact |

## The Integration Checklist

Every page integration is verified against:

- Design layout matches original exactly
- Zero dummy data remains
- All data from backend props
- Null/empty states handled
- Forms use proper form handling
- Navigation uses proper routing
- Backend response shape matches frontend expectations
- Page looks like it belongs to the same project

## Works With

- React + Inertia.js
- Vue + Inertia.js
- Next.js
- Nuxt.js
- Any framework where designs are pre-built as components

## Files

```
SKILL.md    # Main skill file (loaded by AI agents)
```

## Contributing

Found a rule that should be added? Open an issue or submit a pull request.

## License

The MIT License (MIT). Please see [License File](LICENSE.md) for more information.
