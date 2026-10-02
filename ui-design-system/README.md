# UI Design System

Comprehensive UI/UX design intelligence skill for Claude Code, Codex, Gemini CLI, and other AI coding agents. 1421 lines covering 21 sections of actionable design guidance, from aesthetic anchoring to automated audit pipelines.

> Downloads · docs · updates: <https://www.imagentx.top/en/products/ui-design-system>

## What's Inside

| Section | Content | Scale |
|---------|---------|-------|
| **When to Apply** | Must Use / Recommended / Skip decision criteria | — |
| **Rule Categories by Priority** | 10-level priority table for rule triage | — |
| **§1-5: Eight Anchors** | Aesthetic territory system — each locking palette, type, texture to CSS tokens | 8 anchors |
| **§6: Extended Style References** | 73 design styles with key moves, signature colors, and best-fit scenarios | 73 styles |
| **§7: Industry Color Palettes** | Ready-to-use palettes for SaaS, healthcare, fintech, education, etc. | 30+ systems |
| **§8: Compatibility Matrix** | Style × context compatibility + decision examples | — |
| **§9: Typography Pairings** | 73 font combinations with Google Fonts URLs (Western + CJK + Mobile) | 73 pairings |
| **§10: UX Guidelines** | 199 actionable rules across 14 subcategories | 199 rules |
| **§11: Industry Design Reasoning** | Design decisions by product type (SaaS, marketplace, health, etc.) | 14 types |
| **§12: Chart Selection Guide** | Match data type → chart type with color guidance | 25 data types |
| **§13: Professional UI Rules** | Icons, interaction, light/dark mode, layout | 4 tables |
| **§14: Pre-Delivery Checklist** | 5 categories of ship-readiness checks | 20+ items |
| **§15: Next.js Best Practices** | App Router, Server Components, streaming, ISR, image optimization | 52 rules |
| **§16: React Best Practices** | Hooks, memoization, error boundaries, TypeScript patterns | 53 rules |
| **§17: Tailwind CSS Best Practices** | v4 syntax, responsive, dark mode, animation, accessibility | 55 rules |
| **§18: UI Styling** | shadcn/ui + Tailwind patterns, form validation, dark mode toggle | — |
| **§19: Design Token Architecture** | Three-layer system: Primitive → Semantic → Component | — |
| **§20: Brand Identity Framework** | Voice, visual identity, messaging, color management | — |
| **§21: UI/UX Audit Pipeline** | Playwright walkthrough + axe-core WCAG + Lighthouse perf audit | Full pipeline |

## Install

### Claude Code

```bash
git clone https://github.com/Ilm-Alan/ui-design-system.git ~/.claude/skills/ui-design-system
```

### Codex

```bash
git clone https://github.com/Ilm-Alan/ui-design-system.git ~/.codex/skills/ui-design-system
```

### Gemini CLI

Enable experimental skills in `~/.gemini/settings.json`:

```json
{ "experimental": { "skills": true } }
```

Then:

```bash
git clone https://github.com/Ilm-Alan/ui-design-system.git ~/.gemini/skills/ui-design-system
```

Verify with `/skills list`.

## The Eight Anchors

Each anchor locks specific CSS tokens. Picking an anchor commits to those tokens — not to a vibe.

| Anchor | Surface | Typography | Accent | Texture |
|--------|---------|------------|--------|---------|
| **Swiss** | Pure white `#FFF` or neutral `#F7F7F8` | Akzidenz-Grotesk / Helvetica Neue / Söhne | Swiss Red `#E4002B` or Yves Klein Blue `#002FA7` | Visible grid lines, 1px hairlines |
| **Industrial** | Pitch black `#000` or warm-black `#0B0C0A` | IBM Plex Mono / JetBrains Mono | Green `#00E676`, Red `#FF3B30`, or Acid Lime `#C6FF4A` | Flat, 1px borders, tabular numerics |
| **Brutalist** | Pure primaries — `#FF0000` `#0000FF` `#FFFF00` `#000` `#FFF` | System fonts — Times, Helvetica, Courier | Pure primaries competing equally | Hard offset shadows `8px 8px 0 #000`, native controls |
| **Aurora Maximalism** | Dark saturated gradient: violet → magenta → cyan | Inter Variable / PP Neue Machina / Sharp Grotesk | Neon `text-shadow` glow | Mesh gradient, spring-physics motion |
| **Chaotic Maximalism** | Clashing pastels + neons | 3+ faces from different registers | Hot pink `#FF71CE` + acid `#DFFF00` + cyan `#00FFFF` | Patterns on every surface |
| **Retro-Futuristic** | Pitch black `#0A0014` | VT323 / Orbitron / Space Mono / Monoton | Magenta `#FF006E` + cyan `#00FFFF` | CRT scanlines, chromatic aberration |
| **Organic** | Earth tones — sage `#8B9D83`, clay `#B08B6E`, terracotta `#C66B3D` | Humanist serif (Freight, Caslon) or warm geometric sans | Earth tones | Grain 1-3%, rounded corners 16-32px |
| **Lo-Fi** | Paper-yellow `#E8E0C0` (not cream) | Mixed system fonts — Times + Helvetica + Courier | Mixed system | Halftone dots, rotated 2-8°, Risograph offset |

## Quick Start

Once installed, the skill activates automatically when your task involves UI/UX decisions. You can also invoke it explicitly:

```
/ui-design-system design a landing page for an AI SaaS product
```

### Example: Anchor Selection

```
Context: AI SaaS landing page, technical audience, need to stand out
Anchor: Industrial — pitch black surface with monospace type signals "developer tool"
Differentiator: Terminal-style live demo section with real CLI output
Tokens: #000000 bg, IBM Plex Mono, #00E676 accent, 1px borders, no shadows
```

### Example: Audit Pipeline

```
/ui-design-system audit ui/ux http://localhost:3000
```

Runs the three-stage audit:
1. Playwright walkthrough at 1440×900, 768×1024, 375×812
2. axe-core WCAG 2.1 AA scan
3. Lighthouse mobile performance audit

Produces `./audit/report.md` with P0-P3 findings, screenshots, and a Playwright smoke test.

## Style References

§6 provides 73 design styles organized into four categories:

- **General Visual** (19 styles): Minimalism, Neumorphism, Glassmorphism, Brutalism, Claymorphism, Dark Mode OLED, etc.
- **Landing Page** (8 styles): Hero-Centric, Conversion-Optimized, Storytelling-Driven, etc.
- **BI/Analytics Dashboard** (10 styles): Data-Dense, Real-Time Monitoring, Executive, etc.
- **Mobile-First** (6 styles): App-Like, Gesture-Driven, Progressive, etc.

Each style includes: key move, signature colors, and best-fit use cases.

For 67 additional design system skills (Corporate, Agentic, Dashboard, Dithered, etc.), see [awesome-design-skills](https://github.com/bergside/awesome-design-skills) — pull with `npx typeui.sh pull <slug>`.

## Data Sources

| Source | Content |
|--------|---------|
| [nextlevelbuilder/ui-ux-pro-max-skill](https://github.com/nextlevelbuilder/ui-ux-pro-max-skill) | Core UX rules, styles, palettes, fonts, product patterns |
| [bergside/awesome-design-skills](https://github.com/bergside/awesome-design-skills) | 67 design system style references |
| [brumley-agents/claude-ui-ux-audit-skill](https://github.com/brumley-agents/claude-ui-ux-audit-skill) | Playwright + axe-core + Lighthouse audit pipeline |
| Next.js / React / Tailwind CSV data | Stack-specific best practices with code examples |
| shadcn/ui patterns | Component library integration, form validation, dark mode |
| Design Token Architecture | Three-layer token system (Primitive → Semantic → Component) |

## Content Discipline

The skill enforces strict content rules — every string on screen must name real information or be authored content that knows what it is:

- No fabricated data (fake emails, invented telemetry)
- No filler labels (`// INTELLIGENCE LAYER` under headings)
- No themed replacement of standard UI copy (`Authenticate Session` instead of `Next`)
- No unicode glyphs as icon substitutes (`▣ Dashboard`)
- No AI-slop register (`Ask the grid.`)

## Repository Structure

```
ui-design-system/
├── SKILL.md       # 1421 lines — the full skill definition
├── README.md      # This file
└── LICENSE.txt    # MIT
```

## License

MIT.
