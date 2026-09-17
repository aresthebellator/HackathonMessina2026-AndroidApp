---
name: ui-design-system
description: "Comprehensive UI/UX design intelligence: 8 aesthetic anchors, 73 styles, 199 UX rules, 73 font pairings, 30+ industry palettes, 25 chart types, Next.js/React/Tailwind best practices, shadcn/ui patterns, design tokens, brand identity framework, and UI/UX audit pipeline. Use for all frontend design, styling, UX decisions, and audit tasks."
---

# UI Design System

Eight anchors. Each is a distinct aesthetic territory locked to specific CSS tokens. Pick one per brief. Match its tokens.

> **Reach for the unexpected. Fidelity to the anchor. Discipline on the content. Nothing left to default.**

---

## When to Apply

此 Skill 在任务涉及 **UI 结构、视觉设计决策、交互模式或用户体验质量控制** 时使用。

### Must Use

- 设计新页面（Landing Page、Dashboard、Admin、SaaS、Mobile App）
- 创建或重构 UI 组件（buttons、modals、forms、tables、charts 等）
- 选择配色方案、字体系统、间距标准或布局系统
- 审查 UI 代码的用户体验、可访问性或视觉一致性
- 实现导航结构、动画或响应式行为
- 做产品级设计决策（风格、信息层级、品牌表达）
- 提升界面的感知质量、清晰度或可用性

### Recommended

- UI 看起来"不够专业"但原因不明时
- 收到可用性或体验反馈时
- 上线前 UI 质量优化
- 跨平台设计对齐（Web / iOS / Android）
- 构建设计系统或可复用组件库

### Skip

- 纯后端逻辑开发
- 仅涉及 API 或数据库设计
- 与界面无关的性能优化
- 基础设施或 DevOps 工作
- 非视觉脚本或自动化任务

**判断标准**：如果任务会改变功能的**外观、手感、运动方式或交互方式**，就应该使用此 Skill。

---

## Rule Categories by Priority

*按优先级 1→10 决定先关注哪个规则类别。*

| Priority | Category | Impact | Key Checks (Must Have) | Anti-Patterns (Avoid) |
|----------|----------|--------|------------------------|------------------------|
| 1 | Accessibility | CRITICAL | Contrast 4.5:1, Alt text, Keyboard nav, Aria-labels | Removing focus rings, Icon-only buttons without labels |
| 2 | Touch & Interaction | CRITICAL | Min size 44×44px, 8px+ spacing, Loading feedback | Reliance on hover only, Instant state changes (0ms) |
| 3 | Performance | HIGH | WebP/AVIF, Lazy loading, Reserve space (CLS < 0.1) | Layout thrashing, Cumulative Layout Shift |
| 4 | Style Selection | HIGH | Match product type, Consistency, SVG icons (no emoji) | Mixing flat & skeuomorphic randomly, Emoji as icons |
| 5 | Layout & Responsive | HIGH | Mobile-first breakpoints, Viewport meta, No horizontal scroll | Horizontal scroll, Fixed px container widths, Disable zoom |
| 6 | Typography & Color | MEDIUM | Base 16px, Line-height 1.5, Semantic color tokens | Text < 12px body, Gray-on-gray, Raw hex in components |
| 7 | Animation | MEDIUM | Duration 150–300ms, Motion conveys meaning, Spatial continuity | Decorative-only animation, Animating width/height, No reduced-motion |
| 8 | Forms & Feedback | MEDIUM | Visible labels, Error near field, Helper text, Progressive disclosure | Placeholder-only label, Errors only at top, Overwhelm upfront |
| 9 | Navigation Patterns | HIGH | Predictable back, Bottom nav ≤5, Deep linking | Overloaded nav, Broken back behavior, No deep links |
| 10 | Charts & Data | LOW | Legends, Tooltips, Accessible colors | Relying on color alone to convey meaning |

---

## 1. How to work

Before writing code, run this sequence:

1. **Context** — Identify purpose, audience, domain, content density. State the problem in one sentence.
2. **Anchor** — Pick one. Lean unexpected. A Swiss punk record label, an Industrial florist, a Brutalist luxury watchmaker, an Aurora tax app, a Chaotic law firm, a Retro-Futuristic wedding photographer, an Organic trading terminal, a Lo-Fi luxury hotel — each more distinctive than its safe counterpart. Safe pairings produce generic work. Unexpectedness is surface-level — what a first-time viewer sees. Don't let "users are technical" or "it's really a data tool" route every brief to Industrial. State the choice and the reason in one line.
3. **Differentiator** — Define one memorable anchor-internal move: a signature interaction, a typographic gesture, a layout motif, or a material treatment. One sentence. Describable. Visible in the rendered output.
4. **System** — Match the chosen anchor's tokens exactly. Picking Swiss means white + sans + grid, not "some flavor of clean."
5. **Implementation** — Outline structure, then build. Content on screen is authored to the discipline in §2 — no fabrication, no filler, no themed replacement for standard UI copy.

Commit fully to one anchor. Hybridising ("Swiss with Brutalist edge") is a category error — each signature excludes the others by construction.

---

## 2. Content is not design

Design is visuals — palette, typography, structure, texture. **Content — every string, number, and label on screen — is authored separately and has its own discipline.** Token fidelity is not a defence against content slop.

The rule: every string on screen must either name real information from the product, or be authored content that knows what it is — headline, button label, legal body, form field name, sample data that reads as sample. What's forbidden is content pretending to be something it isn't.

**Forbidden:**

- **Fabrication posing as real data.** Invented session personas (`a.chen@grid.co`), fake telemetry (`GRID.FREQ 59.998 Hz`, `BUILD 8.2.0-rc3`). If a slot has no real content, leave it empty — do not fabricate to make the screen look alive.
- **Filler labels.** Mono-caps subtitles nobody asked for (`SECURE OPERATOR AUTHENTICATION` under a login masthead), `//`-prefixed kickers pretending to be code comments (`// INTELLIGENCE LAYER`). If removing the string removes no information, it was filler.
- **Themed replacement of standard UI copy.** `Authenticate Session` instead of `Next`; `Remember this operator` instead of `Remember me`. Standard copy for standard actions. Themed copy is a tax that must be paid with actual utility.
- **Unicode glyphs as icon substitutes.** `▣ Dashboard`, `◊ Market Navigator`. Either use a real icon set or use nothing. ASCII art is not iconography.
- **AI-slop register.** The model recognises this. Twee subcopy on serious surfaces (`Ask the grid.`), synth-sci-fi status strips on mundane B2B, ornamental "seam"/"joinery" flourishes pretending to be structural. Recognise it in your own output; cut it before the reviewer does.

---

## 3. The eight anchors

Each anchor locks specific CSS tokens. Picking the anchor commits to those tokens. If the rendered output drifts outside them, the anchor didn't hold.

### 1. Swiss

**Surface:** Pure white `#FFFFFF` or neutral `#F7F7F8`. **Typography:** Akzidenz-Grotesk, Helvetica Neue, or Söhne — sans display and body, one family. **Accent:** Swiss Red `#E4002B`, International Orange `#FF4F00`, or Yves Klein Blue `#002FA7` — one, used deliberately. **Structure:** visible grid lines or 1 px hairline rules. Left-aligned typography; asymmetric balance. Numerals as composition elements (dates, folio numbers, page markers set in condensed sans).

**Breaks if:** warm paper, serif display, grain texture, or centered typography appears.

### 2. Industrial

**Surface:** Pitch black `#000000` or warm-black `#0B0C0A`. **Typography:** IBM Plex Mono, JetBrains Mono, or Berkeley Mono — mono for display and body. **Signal color:** one semantic — green `#00E676`, red `#FF3B30`, amber `#FFB800`, or acid lime `#C6FF4A`. **Structure:** flat; 1 px borders instead of shadows. Tabular numerics via `font-variant-numeric: tabular-nums`.

**Breaks if:** serif typography, proportional fonts, warm paper, any grain, decorative shadows, or rounded corners appear.

### 3. Brutalist

**Surface:** Pure primary or anti-primary — `#FF0000`, `#0000FF`, `#FFFF00`, `#000000`, `#FFFFFF`. Pick 2–3, compete equally. **Typography:** system fonts only — Times New Roman, Helvetica, Courier, Arial, system-ui. Mix faces deliberately. **Shadows:** hard offset, no blur — `box-shadow: 8px 8px 0 #000`. **Controls:** native browser — unstyled `<button>`, default `<select>`, underlined blue links that stay blue. Margins crushed; type runs edge-to-edge.

**Breaks if:** webfonts, tuned hex beyond pure primaries, soft shadows, rounded corners, or centered layout appear.

### 4. Aurora Maximalism

**Surface:** Dark saturated gradient — `linear-gradient` or `conic-gradient` through violet `#5D34D0` → magenta `#FF006E` → cyan `#00F0FF`, or `#3B82F6 → #A855F7 → #EC4899`. **Typography:** Inter Variable, PP Neue Machina, or Sharp Grotesk for oversized display (15–25 vw). **Texture:** mesh gradient as primary surface feature; neon `text-shadow` glow on accents (`0 0 20px <accent>`). **Motion:** spring-physics orchestration, scroll-linked parallax.

**Breaks if:** flat backgrounds, warm paper, restraint, or hairline rules as primary structure appear.

### 5. Chaotic Maximalism

**Surface:** Clashing palette — pastels *and* neons in the same composition. Hot pink `#FF71CE` + acid yellow `#DFFF00` + cyan `#00FFFF` + any third. **Typography:** mixed faces deliberately colliding — 3+ faces from different registers on the same page. **Texture:** patterns on every surface (squiggles, dots, zigzags, checker — SVG or `repeating-linear-gradient`). Oversized display crashing against busy ground.

**Breaks if:** coherent palette, single typeface, whitespace as structural element, or 60/30/10 dominance appears.

### 6. Retro-Futuristic

**Surface:** Pitch black `#0A0014` or deep navy-black. **Typography:** period-specific — VT323 (CRT), Orbitron (synthwave), Space Mono (cyberpunk), Monoton (Miami-neon), Press Start 2P (arcade), IBM Plex Mono (terminal). **Accent:** neon pair — magenta `#FF006E` + cyan `#00FFFF` (synthwave) or phosphor green `#00FF41` + amber `#FFB000` (terminal). **Texture:** CRT scanlines via `::before` `repeating-linear-gradient` overlay, or chromatic aberration (`text-shadow: 2px 0 #FF0000, -2px 0 #00FFFF`), or both. Glow committed.

**Breaks if:** flatness, modern sans-serifs (Inter, Söhne), paper surfaces, or absence of texture appears.

### 7. Organic

**Surface:** Earth tones — sage `#8B9D83`, clay `#B08B6E`, terracotta `#C66B3D`, ochre `#C08E3A`, moss `#606C38`. When a light surface is needed: sand `#E8DCC7` or oat `#D4B895`. **Never cream `#F0-F8` warm-paper range.** **Typography:** humanist serif (Freight, Caslon, Fraunces — Fraunces is restricted to this anchor only) or warm geometric sans (Greycliff, Epilogue, Recoleta). **Structure:** rounded corners 16–32 px. **Texture:** grain at 1–3 % via SVG feTurbulence. **Motion:** gentle ease 300–500 ms, breathing animations on hero elements.

**Breaks if:** cream backgrounds (warm-tinted `#F0+`), cold greys, pure whites, pure blacks, or hard rectangles appear.

### 8. Lo-Fi

**Surface:** Paper-yellow `#E8E0C0` or `#EDE4CF` — more saturated than cream. **Typography:** mixed system fonts on the same page (Times + Helvetica + Courier colliding deliberately). **Structure:** rotated elements (2–8° off-grid via `transform: rotate`). **Texture:** halftone dot transitions (SVG pattern or `radial-gradient` tile) on imagery; Risograph misregistration (2–4 px RGB channel offset via `text-shadow: 3px 0 #FF006E, -3px 0 #00FFCC`). SVG staple, tape, torn-edge elements.

**Breaks if:** precision, single typeface, smooth motion, rectangles squared to the grid, or cream (the surface is specifically paper-yellow, more saturated) appears.

---

## 4. Output

Every implementation delivers:

- **Stated direction** — A short preamble in a designer's prose before the code, naming: the chosen anchor, why this pairing over the safe one, the differentiator, and the key palette / typefaces / texture choices pulled from the anchor. Written with conviction, not as a checklist.
- **Token fidelity** — The rendered CSS matches the anchor's tokens exactly. If Swiss is chosen, the CSS contains no warm paper, no Fraunces, no grain. If Industrial is chosen, every typeface declared is monospace. Token drift means the anchor didn't hold.
- **Content discipline** — Every string, number, and label on screen names real information or is authored content that knows what it is. No fabricated data, no filler labels, no themed replacement for standard UI copy, no unicode-glyph icons. Token fidelity alone is not sufficient; content ships too.
- **Differentiator visible** — The one memorable move is implemented in the rendered output, not merely described.

---

## 5. Before shipping

- **Unexpected pairing** — Did the choice reach for creative tension, or default to the safe pairing?
- **Token fidelity** — Does every rendered token live inside the anchor's allowed range? If tokens appear that the anchor doesn't allow, the anchor didn't hold.
- **Content discipline** — Every label names real information; standard UI copy for standard actions; no fabrication, filler mono-caps, `//` kickers, unicode-glyph icons, twee subcopy, or AI-slop register. If any appeared, cut.
- **Differentiator visible** — Is the memorable anchor-internal move actually rendered?
- **Hybrid resistance** — Was one anchor held, or did the execution drift into "Swiss with Brutalist edge"?

---

## 6. Extended Style References

All 70+ visual styles beyond the 8 core anchors. Use these for **inspiration and detail decisions** within an anchor — not as anchor replacements. Each reference is a technique, not a territory.

### 6.1 General Visual Styles

| # | Style | Key Move | Signature Colors | Best For |
|---|-------|----------|------------------|----------|
| 1 | **Minimalism & Swiss** | Grid-based, sans-serif, high contrast | `#000000` / `#FFFFFF` + single accent | Enterprise, docs, SaaS |
| 2 | **Neumorphism** | Soft extruded/debossed shapes | Pastel: `#C8E0F4` / `#e0e5ec` bg | Wellness, fintech, apps |
| 3 | **Glassmorphism** | Frosted glass, backdrop blur | `rgba(255,255,255,0.15)` on vibrant bg | Dashboards, creative |
| 4 | **Brutalism** | Raw, unpolished, default fonts | Primary: `#FF0000` `#0000FF` `#FFFF00` | Art, music, portfolios |
| 5 | **3D & Hyperrealism** | Depth, realistic textures, 3D models | Deep: `#001F3F` / `#228B22` | Product showcases, gaming |
| 6 | **Vibrant & Block-based** | Bold blocks, geometric, high energy | Neon: `#39FF14` `#BF00FF` `#FF1493` | Youth brands, events |
| 7 | **Dark Mode (OLED)** | True black, high contrast, eye-friendly | `#000000` / `#121212` / `#0A0E27` | Media, dev tools, night |
| 8 | **Accessible & Ethical** | WCAG AAA, large text, keyboard nav | `4.5:1+` contrast, simple palette | Gov, healthcare, education |
| 9 | **Claymorphism** | Puffy 3D clay-like elements | Pastel: `#FDBCB4` / `#ADD8E6` | Kids, playful brands |
| 10 | **Aurora UI** | Vibrant mesh gradients, luminous | Blue↔Orange, Purple↔Yellow gradients | Creative, AI, SaaS |
| 11 | **Retro-Futurism** | 80s neon, CRT scanlines, geometric | `#0080FF` `#FF006E` `#00FFFF` on `#0A0014` | Gaming, synthwave |
| 12 | **Flat Design** | 2D, bold colors, no shadows | Solid bright: 4-6 color palette | Mobile, clean UI |
| 13 | **Skeuomorphism** | Realistic textures, 3D, real-world metaphors | Wood, leather, metal textures | Utility apps, nostalgia |
| 14 | **Liquid Glass** | Flowing glass, morphing, fluid effects | Iridescent rainbow, translucent | Apple-style, premium |
| 15 | **Motion-Driven** | Animation-heavy, scroll effects | Bold high-contrast animated colors | Storytelling, launches |
| 16 | **Micro-interactions** | Small gesture-based animations | Subtle 10-20% color shifts | Apps, SaaS, refined UI |
| 17 | **Inclusive Design** | Color-blind friendly, haptic, voice | WCAG AAA, avoid red-green only | Healthcare, gov, public |
| 18 | **Zero Interface** | Minimal visible UI, voice/AI-driven | Neutral: `#FAFAFA` / `#F0F0F0` | Ambient, smart devices |
| 19 | **Soft UI Evolution** | Refined neumorphism, better contrast | Tinted pastels: `#87CEEB` / `#FFB6C1` | Modern apps, dashboards |
| 38 | **Neubrutalism** | Bold borders, black outlines, thick shadows | `#FFEB3B` `#FF5252` `#2196F3` + `#000` | Gen Z brands, bold UI |
| 39 | **Bento Box Grid** | Asymmetric modular cards, Apple-style | Neutral `#F5F5F7` + brand accent | Dashboards, portfolios |
| 40 | **Y2K Aesthetic** | Chrome, metallic, bubblegum, iridescent | `#FF69B4` `#00FFFF` `#C0C0C0` | Fashion, retro brands |
| 41 | **Cyberpunk UI** | Neon, terminal, HUD, glitch, dystopian | `#00FF00` `#FF00FF` `#00FFFF` on dark | Gaming, sci-fi, tech |
| 42 | **Organic Biophilic** | Nature, organic shapes, sustainable | `#228B22` `#8B4513` `#87CEEB` | Wellness, eco brands |
| 43 | **AI-Native UI** | Conversational, ambient, minimal chrome | `#6366F1` (AI Purple) + neutral | AI tools, chatbots |
| 44 | **Memphis Design** | 80s geometric, playful, postmodern | `#FF71CE` `#FFCE5C` `#86CCCA` | Creative, art, events |
| 45 | **Vaporwave** | Synthwave, sunset gradient, nostalgic | `#FF71CE` `#01CDFE` `#B967FF` | Music, retro-futurism |
| 46 | **Dimensional Layering** | Overlapping cards, z-depth, floating | Neutral `#FFFFFF` `#F5F5F5` + accent | Product displays, portfolios |
| 47 | **Exaggerated Minimalism** | Oversized type, loud negative space | `#000000` / `#FFFFFF` + single accent | Editorial, brand statements |
| 48 | **Kinetic Typography** | Text as animation, morphing, typing | Flexible, high contrast recommended | Launches, storytelling |
| 49 | **Parallax Storytelling** | Scroll-driven narrative, layered depth | Gradients, natural colors, section hues | Marketing, brand stories |
| 50 | **Swiss Modernism 2.0** | Updated grid, modular, asymmetric | `#000000` `#FFFFFF` `#F5F5F5` + accent | Enterprise, editorial |
| 51 | **HUD / Sci-Fi FUI** | Futuristic wireframe, data transparency | `#00FFFF` `#0080FF` `#FF006E` on dark | Gaming, sci-fi, defense |
| 52 | **Pixel Art** | Retro 8-bit, blocky, nostalgic | NES palette, bright limited colors | Gaming, retro brands |
| 55 | **Spatial UI (VisionOS)** | Glass, depth, gaze/gesture, translucent | `rgba(255,255,255,0.15-0.3)` | Vision Pro, spatial apps |
| 56 | **E-Ink / Paper** | Matte, high-contrast, calm, monochrome | `#FDFBF7` / `#F5F5F5` / `#1A1A1A` | Reading, slow tech |
| 57 | **Gen Z Chaos / Maximalism** | Stickers, collage, mixed media, loud | `#FF00FF` `#00FF00` `#FFFF00` | Youth, social, events |
| 58 | **Biomimetic / Organic 2.0** | Cellular, fluid, breathing, generative | `#FF9999` `#00FF41` bioluminescent | Health, nature, science |
| 59 | **Anti-Polish / Raw Aesthetic** | Hand-drawn, scanned textures, imperfect | `#FAFAF8` / `#4A4A4A` / `#1A1A1A` | Art, indie, authentic |
| 60 | **Tactile Digital** | Jelly buttons, chrome, squishy, bouncy | Chrome `#C0C0C0` / Jelly `#FF9EC6` | Playful apps, kids |
| 61 | **Nature Distilled** | Muted earthy, skin tones, wood, soil | `#C67B5C` `#D4C4A8` `#B5651D` | Wellness, organic brands |
| 62 | **Interactive Cursor Design** | Custom cursor, hover effects, pointer transform | Brand-dependent accent | Portfolios, creative |
| 63 | **Voice-First Multimodal** | Voice UI, audio feedback, ambient | Calm: `#FAFAFA` / `#6B8FAF` | Smart devices, accessibility |
| 64 | **3D Product Preview** | 360° view, rotatable, zoomable, AR | Neutral: `#E8E8E8` / `#FFFFFF` | E-commerce, product |
| 65 | **Gradient Mesh / Aurora Evolved** | Complex multi-stop gradients, flowing | `#00FFFF` `#FF00FF` `#FFFF00` blend | Creative, AI, premium |
| 66 | **Editorial Grid / Magazine** | Asymmetric columns, pull quotes, drop caps | `#000000` / `#FFFFFF` + brand accent | Publishing, news, blogs |
| 67 | **Chromatic Aberration** | RGB split, color fringing, glitch, VHS | Offset: `#FF0000` `#00FF00` `#0000FF` | Retro tech, analog error |
| 68 | **Vintage Analog / Retro Film** | Film grain, VHS, faded colors, light leaks | `#F5E6C8` `#D4A574` `#4A7B7C` | Photography, nostalgia |

> **More styles:** `bergside/awesome-design-skills` — 67 additional design system skills (Corporate, Agentic, Dashboard, Dithered, Doodle, Dramatic, etc.) with per-skill SKILL.md tokens and component rules. Pull with `npx typeui.sh pull <slug>` or browse at https://typeui.sh/design-skills

### 6.2 Landing Page Styles

| # | Style | Key Move | Best For |
|---|-------|----------|----------|
| 20 | **Hero-Centric** | Large hero, compelling headline, high-contrast CTA | Product launches, SaaS |
| 21 | **Conversion-Optimized** | Form-focused, single CTA, urgency elements | Lead gen, signups |
| 22 | **Feature-Rich Showcase** | Multiple feature sections, grid layout | Feature-heavy products |
| 23 | **Minimal & Direct** | Minimal text, white space, single column | Clean brands, luxury |
| 24 | **Social Proof-Focused** | Testimonials, client logos, case studies | B2B, enterprise |
| 25 | **Interactive Product Demo** | Embedded mockup/video, walkthrough | Complex products |
| 26 | **Trust & Authority** | Certifications, credentials, metrics | Professional services |
| 27 | **Storytelling-Driven** | Narrative flow, visual story progression | Brand storytelling |

### 6.3 BI/Analytics Dashboard Styles

| # | Style | Key Move | Best For |
|---|-------|----------|----------|
| 28 | **Data-Dense** | Multiple charts, KPI cards, minimal padding | Analytics platforms |
| 29 | **Heat Map** | Color-coded grid/matrix, intensity viz | Geographic, usage data |
| 30 | **Executive** | High-level KPIs, large metrics, minimal detail | C-suite, summaries |
| 31 | **Real-Time Monitoring** | Live updates, status indicators, alerts | DevOps, trading |
| 32 | **Drill-Down Analytics** | Hierarchical exploration, expandable | Deep data analysis |
| 33 | **Comparative Analysis** | Side-by-side, period-over-period, A/B | Performance comparison |
| 34 | **Predictive Analytics** | Forecast lines, confidence intervals | AI/ML products |
| 35 | **User Behavior** | Funnel viz, user flow, conversion tracking | Product analytics |
| 36 | **Financial** | Revenue, P&L, budget tracking, ratios | Fintech, accounting |
| 37 | **Sales Intelligence** | Deal pipeline, territory, leaderboard | CRM, sales tools |

### 6.4 Mobile-First Styles

| # | Style | Key Move | Signature Colors | Best For |
|---|-------|----------|------------------|----------|
| 69 | **Bauhaus (包豪斯)** | Geometric, constructivist, hard shadow | `#D02020` `#1040C0` `#F0C800` | Design-forward mobile |
| 70 | **Minimalist Monochrome** | Black+white, editorial, austere | `#000000` / `#FFFFFF` | Editorial, luxury |
| 71 | **Modern Dark (Cinema)** | Cinematic, ambient light, glassmorphism | `#020203` / `#5E6AD2` accent | Media, premium apps |
| 72 | **SaaS Mobile (High-Tech)** | Electric blue, gradient, fintech | `#0052FF` → `#4D7CFF` | SaaS, fintech mobile |
| 73 | **Terminal CLI** | Matrix green, monospace, hacker | `#33FF00` on `#050505` | Dev tools, CLI apps |
| 74 | **Kinetic Brutalism** | Motion, marquee, acid yellow, aggressive | `#DFE104` on `#09090B` | Bold brands, events |
| 75 | **Flat Design Mobile** | 2D, no shadow, color blocking, touch-first | `#3B82F6` `#10B981` | Clean mobile apps |
| 76 | **Material You (MD3)** | Tonal surfaces, pills, soft curves | `#6750A4` / `#E8DEF8` | Android, Google-style |
| 77 | **Neo Brutalism Mobile** | Pop art, stickers, thick borders | `#FFFDF5` / `#FF6B6B` / `#FFD93D` | Gen Z, playful apps |
| 78 | **Bold Typography Mobile** | Poster, broadsheet, vermillion, negative space | `#0A0A0A` / `#FAFAFA` | Editorial, news |
| 79 | **Academia (Scholarly)** | Library, mahogany, parchment, brass, serif | `#1C1714` / `#251E19` | Education, books |
| 80 | **Cyberpunk Mobile HUD** | Neon, glitch, chamfered, scanlines, CRT | `#0A0A0F` / `#12121A` | Gaming, sci-fi |
| 81 | **Bitcoin DeFi** | Digital gold, orange, glassmorphism, gradient | `#F7931A` / `#EA580C` | Crypto, DeFi |
| 82 | **Claymorphism Mobile** | Clay, 3D, soft, bubbly, candy, tactile | `#7C3AED` / `#DB2777` | Kids, playful |
| 83 | **Enterprise SaaS Mobile** | Professional, indigo, violet, polished | `#4F46E5` / `#7C3AED` | B2B, enterprise |
| 84 | **Sketch Hand-Drawn** | Wobbly, imperfect, paper, kalam, collage | `#FF4D4D` / `#2D2D2D` | Creative, indie |
| 85 | **Neumorphism Mobile** | Dual shadow, extruded, inset, monochromatic | `#6C63FF` on `#E0E5EC` | Wellness, fintech |

---

## 7. Industry Color Palettes

Pre-built, WCAG-compliant color systems for common product types. Use these to **quickly establish brand colors** when the anchor doesn't specify exact hues.

### SaaS & Tech

| Product Type | Primary | On Primary | Accent | Background | Notes |
|--------------|---------|------------|--------|------------|-------|
| **General SaaS** | `#2563EB` | `#FFFFFF` | `#EA580C` | `#F8FAFC` | Trust blue + orange CTA |
| **Micro SaaS** | `#6366F1` | `#FFFFFF` | `#059669` | `#F5F3FF` | Indigo + emerald |
| **AI/Chatbot** | `#7C3AED` | `#FFFFFF` | `#0891B2` | `#FAF5FF` | AI purple + cyan |
| **Fintech** | `#F59E0B` | `#0F172A` | `#8B5CF6` | `#0F172A` | Gold trust + purple tech |
| **Productivity** | `#0D9488` | `#FFFFFF` | `#EA580C` | `#F0FDFA` | Teal focus + orange |
| **Design System** | `#4F46E5` | `#FFFFFF` | `#EA580C` | `#EEF2FF` | Indigo + doc hierarchy |
| **Remote Work** | `#6366F1` | `#FFFFFF` | `#059669` | `#F5F3FF` | Calm indigo + success |

### Service & Professional

| Product Type | Primary | On Primary | Accent | Background | Notes |
|--------------|---------|------------|--------|------------|-------|
| **B2B Service** | `#0F172A` | `#FFFFFF` | `#0369A1` | `#F8FAFC` | Professional navy |
| **Healthcare** | `#0891B2` | `#FFFFFF` | `#059669` | `#ECFEFF` | Calm cyan + health |
| **Mental Health** | `#8B5CF6` | `#FFFFFF` | `#059669` | `#FAF5FF` | Lavender + wellness |
| **Education** | `#4F46E5` | `#FFFFFF` | `#EA580C` | `#EEF2FF` | Playful indigo + orange |
| **Government** | `#0F172A` | `#FFFFFF` | `#0369A1` | `#F8FAFC` | High contrast navy |
| **Micro-Credentials** | `#0369A1` | `#FFFFFF` | `#A16207` | `#F0F9FF` | Trust blue + gold |

### Consumer & Creative

| Product Type | Primary | On Primary | Accent | Background | Notes |
|--------------|---------|------------|--------|------------|-------|
| **E-commerce** | `#059669` | `#FFFFFF` | `#EA580C` | `#ECDF5F` | Success green + urgency |
| **Luxury** | `#1C1917` | `#FFFFFF` | `#A16207` | `#FAFAF9` | Premium dark + gold |
| **Creative Agency** | `#EC4899` | `#FFFFFF` | `#0891B2` | `#FDF2F8` | Bold pink + cyan |
| **Gaming** | `#7C3AED` | `#FFFFFF` | `#F43F5E` | `#0F0F23` | Neon purple + rose |
| **NFT/Web3** | `#8B5CF6` | `#FFFFFF` | `#FBBF24` | `#0F0F23` | Purple tech + gold |
| **Podcast** | `#1E1B4B` | `#FFFFFF` | `#F97316` | `#0F0F23` | Dark audio + warm |
| **Subscription Box** | `#D946EF` | `#FFFFFF` | `#EA580C` | `#FDF4FF` | Purple + urgency orange |

### Mobile-First

| Product Type | Primary | On Primary | Accent | Background | Notes |
|--------------|---------|------------|--------|------------|-------|
| **Social Media** | `#E11D48` | `#FFFFFF` | `#2563EB` | `#FFF1F2` | Rose + engagement blue |
| **Dating** | `#E11D48` | `#FFFFFF` | `#EA580C` | `#FFF1F2` | Romantic rose + warm |
| **Pet Tech** | `#F97316` | `#0F172A` | `#2563EB` | `#FFF7ED` | Playful orange + trust |
| **Smart Home** | `#1E293B` | `#FFFFFF` | `#22C55E` | `#0F172A` | Dark tech + status green |
| **EV/Charging** | `#0891B2` | `#FFFFFF` | `#16A34A` | `#ECFEFF` | Electric cyan + eco |
| **Creator Economy** | `#EC4899` | `#FFFFFF` | `#EA580C` | `#FDF2F8` | Pink + engagement orange |
| **Portfolio/Personal** | `#18181B` | `#FFFFFF` | `#2563EB` | `#FAFAFA` | Monochrome + blue |

### Dashboard & Analytics

| Product Type | Primary | On Primary | Accent | Background | Notes |
|--------------|---------|------------|--------|------------|-------|
| **Financial** | `#0F172A` | `#FFFFFF` | `#22C55E` | `#020617` | Dark + green positive |
| **Analytics** | `#1E40AF` | `#FFFFFF` | `#D97706` | `#F8FAFC` | Blue data + amber |
| **Sales** | `#0F172A` | `#FFFFFF` | `#0369A1` | `#F8FAFC` | Navy + blue deals |
| **IoT/Smart Home** | `#1E293B` | `#FFFFFF` | `#22C55E` | `#0F172A` | Dark + status green |

---

## 8. Using Extended References

**Rule: Extended references serve the anchor, not the other way around.**

### How to apply

1. **Pick your anchor first** — the 8 core anchors define the territory
2. **Use extended references for details** — choose a surface treatment, layout pattern, or texture that fits *within* the anchor's rules
3. **Use industry palettes for brand colors** — when the anchor allows accent flexibility, pick from §7
4. **Match page type to style** — use §6.2 for landing pages, §6.3 for dashboards, §6.4 for mobile

### Compatibility quick reference

| Technique | Works with | Breaks |
|-----------|------------|--------|
| Glassmorphism | Aurora, Lo-Fi (dark) | Swiss, Industrial, Brutalist |
| Neumorphism | Organic (light) | Industrial, Brutalist, Retro-Futuristic |
| Claymorphism | Organic, Chaotic | Swiss, Industrial, Brutalist |
| Bento Grid | Swiss, Industrial, Organic (light) | Brutalist, Chaotic |
| Editorial Grid | Swiss, Organic, Lo-Fi | Industrial, Aurora |
| Kinetic Typography | Aurora, Chaotic, Retro-Futuristic | Swiss, Industrial, Organic |
| E-Ink/Paper | Organic, Lo-Fi | Aurora, Chaotic, Retro-Futuristic |
| Pixel Art | Retro-Futuristic, Lo-Fi | Swiss, Industrial, Organic |
| Parallax Storytelling | Aurora, Organic | Swiss, Brutalist, Industrial |
| Memphis | Chaotic, Lo-Fi | Swiss, Industrial, Organic |
| Cyberpunk | Retro-Futuristic, Industrial (adjacent) | Swiss, Organic, Lo-Fi |
| Bauhaus | Swiss (adjacent), Brutalist (adjacent) | Aurora, Chaotic, Retro-Futuristic |
| Material You | Swiss (soft variant), Organic | Brutalist, Retro-Futuristic |
| Terminal CLI | Industrial, Retro-Futuristic | Organic, Lo-Fi, Chaotic |

### Example decisions

- **Swiss + Bento Grid**: Clean grid layout with Swiss typography → valid
- **Organic + Claymorphism**: Puffy organic shapes with earth tones → valid
- **Industrial + Neumorphism**: Soft shadows on black terminal → breaks Industrial
- **Aurora + Glassmorphism**: Frosted glass panels with gradient glow → valid
- **Retro-Futuristic + Pixel Art**: CRT + pixel aesthetic → valid
- **Chaotic + Memphis**: Geometric patterns with clashing colors → valid
- **Lo-Fi + Editorial Grid**: Magazine layout with hand-drawn feel → valid
- **Brutalist + Bauhaus**: Geometric primary colors with hard shadows → works

---

## 9. Typography Pairings

73 font pairings with Google Fonts URLs, Tailwind config, and use cases. Pick one pairing per project.

### 9.1 Western Pairings

| # | Name | Type | Heading | Body | Best For |
|---|------|------|---------|------|----------|
| 1 | Classic Elegant | Serif+Sans | Playfair Display | Inter | Luxury, fashion, spa, editorial |
| 2 | Modern Professional | Sans+Sans | Poppins | Open Sans | SaaS, corporate, startups |
| 3 | Tech Startup | Sans+Sans | Space Grotesk | DM Sans | Tech, AI, developer tools |
| 4 | Editorial Classic | Serif+Serif | Cormorant Garamond | Libre Baskerville | Publishing, blogs, news |
| 5 | Minimal Swiss | Sans+Sans | Inter | Inter | Dashboards, admin, docs |
| 6 | Playful Creative | Display+Sans | Fredoka | Nunito | Children, education, gaming |
| 7 | Bold Statement | Display+Sans | Bebas Neue | Source Sans 3 | Marketing, portfolios, events |
| 8 | Wellness Calm | Serif+Sans | Lora | Raleway | Health, spa, yoga, organic |
| 9 | Developer Mono | Mono+Sans | JetBrains Mono | IBM Plex Sans | Dev tools, docs, code editors |
| 10 | Retro Vintage | Display+Serif | Abril Fatface | Merriweather | Vintage brands, breweries |
| 11 | Geometric Modern | Sans+Sans | Outfit | Work Sans | General, portfolios, agencies |
| 12 | Luxury Serif | Serif+Sans | Cormorant | Montserrat | Fashion, jewelry, premium |
| 13 | Friendly SaaS | Sans+Sans | Plus Jakarta Sans | Plus Jakarta Sans | SaaS, web apps, dashboards |
| 14 | News Editorial | Serif+Sans | Newsreader | Roboto | News, blogs, journalism |
| 15 | Handwritten Charm | Script+Sans | Caveat | Quicksand | Personal blogs, invitations |
| 16 | Corporate Trust | Sans+Sans | Lexend | Source Sans 3 | Enterprise, government, finance |
| 17 | Brutalist Raw | Mono+Mono | Space Mono | Space Mono | Brutalist, dev portfolios |
| 18 | Fashion Forward | Sans+Sans | Syne | Manrope | Fashion, creative agencies |
| 19 | Soft Rounded | Sans+Sans | Varela Round | Nunito Sans | Children, pets, wellness |
| 20 | Premium Sans | Sans+Sans | Satoshi | General Sans | Premium brands, agencies |
| 29 | Legal Professional | Serif+Sans | EB Garamond | Lato | Law firms, legal services |
| 30 | Medical Clean | Sans+Sans | Figtree | Noto Sans | Healthcare, clinics, pharma |
| 31 | Financial Trust | Sans+Sans | IBM Plex Sans | IBM Plex Sans | Banks, finance, insurance |
| 32 | Real Estate Luxury | Serif+Sans | Cinzel | Josefin Sans | Real estate, luxury property |
| 33 | Restaurant Menu | Serif+Sans | Playfair Display SC | Karla | Restaurants, cafes, food |
| 34 | Art Deco | Display+Sans | Poiret One | Didact Gothic | Vintage events, luxury hotels |
| 35 | Magazine Style | Serif+Sans | Libre Bodoni | Public Sans | Magazines, publications |
| 36 | Crypto/Web3 | Sans+Sans | Orbitron | Exo 2 | Crypto, NFT, blockchain |
| 37 | Gaming Bold | Display+Sans | Russo One | Chakra Petch | Gaming, esports, action |
| 38 | Indie/Craft | Display+Sans | Amatic SC | Cabin | Craft brands, artisan, indie |
| 39 | Startup Bold | Sans+Sans | Clash Display | Satoshi | Startups, pitch decks |
| 40 | E-commerce Clean | Sans+Sans | Rubik | Nunito Sans | E-commerce, retail |
| 41 | Academic/Research | Serif+Sans | Crimson Pro | Atkinson Hyperlegible | Universities, research |
| 42 | Dashboard Data | Mono+Sans | Fira Code | Fira Sans | Dashboards, analytics |
| 43 | Music/Entertainment | Display+Sans | Righteous | Poppins | Music, entertainment, events |
| 44 | Minimalist Portfolio | Sans+Sans | Archivo | Space Grotesk | Design portfolios |
| 45 | Kids/Education | Display+Sans | Baloo 2 | Comic Neue | Children's apps, education |
| 46 | Wedding/Romance | Script+Serif | Great Vibes | Cormorant Infant | Wedding, invitations |
| 47 | Science/Tech | Sans+Sans | Exo | Roboto Mono | Science, research, tech docs |
| 48 | Accessibility First | Sans+Sans | Atkinson Hyperlegible | Atkinson Hyperlegible | Gov, healthcare, a11y-critical |
| 49 | Sports/Fitness | Sans+Sans | Barlow Condensed | Barlow | Sports, fitness, athletic |
| 50 | Luxury Minimalist | Serif+Sans | Bodoni Moda | Jost | Luxury minimalist, high-end |

### 9.2 CJK & International Pairings

| # | Name | Heading | Body | Language |
|---|------|---------|------|----------|
| 21 | Vietnamese Friendly | Be Vietnam Pro | Noto Sans | Vietnamese |
| 22 | Japanese Elegant | Noto Serif JP | Noto Sans JP | Japanese |
| 23 | Korean Modern | Noto Sans KR | Noto Sans KR | Korean |
| 24 | Chinese Traditional | Noto Serif TC | Noto Sans TC | Traditional Chinese |
| 25 | Chinese Simplified | Noto Sans SC | Noto Sans SC | Simplified Chinese |
| 26 | Arabic Elegant | Noto Naskh Arabic | Noto Sans Arabic | Arabic (RTL) |
| 27 | Thai Modern | Noto Sans Thai | Noto Sans Thai | Thai |
| 28 | Hebrew Modern | Noto Sans Hebrew | Noto Sans Hebrew | Hebrew (RTL) |

### 9.3 Mobile-Specific Pairings

| # | Name | Heading | Body | Best For |
|---|------|---------|------|----------|
| 58 | Minimalist Monochrome Editorial | Playfair Display | Source Serif 4 | Luxury fashion mobile |
| 59 | Modern Dark Cinema | Inter | Inter | Dev tools, fintech, AI |
| 60 | SaaS Mobile Boutique | Calistoga | Inter | B2B SaaS, fintech |
| 61 | Terminal CLI | JetBrains Mono | JetBrains Mono | Dev tools, Web3 |
| 62 | Kinetic Brutalism | Space Grotesk | Space Grotesk | Music, sports, brand |
| 63 | Flat Design Mobile | Inter | Inter | Cross-platform, dashboards |
| 64 | Material You MD3 | Roboto | Roboto | Android, Google-style |
| 65 | Neo Brutalism Mobile | Space Grotesk | Space Grotesk | Gen Z, creative tools |
| 66 | Bold Typography Mobile | Inter | Playfair Display | Editorial, events |
| 67 | Academia Mobile | Cormorant Garamond | Crimson Pro | Knowledge, scholarly |
| 68 | Cyberpunk Mobile | Orbitron | JetBrains Mono | Gaming, crypto, sci-fi |
| 69 | Web3 Bitcoin DeFi | Space Grotesk | Inter | DeFi, NFT, metaverse |
| 70 | Claymorphism Mobile | Nunito | DM Sans | Children, teen social |
| 71 | Enterprise SaaS Mobile | Plus Jakarta Sans | Plus Jakarta Sans | B2B, productivity |
| 72 | Sketch Hand-Drawn Mobile | Kalam | Patrick Hand | Journaling, prototypes |
| 73 | Neumorphism Mobile | Plus Jakarta Sans | Plus Jakarta Sans | Smart home, aesthetic |

### 9.4 Import Pattern

```html
<!-- Google Fonts import (add to <head>) -->
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=HEADING:wght@400;600;700&family=BODY:wght@400;500;700&display=swap" rel="stylesheet">
```

```css
/* CSS custom properties */
:root {
  --font-heading: 'HEADING', serif;
  --font-body: 'BODY', sans-serif;
}
```

---

## 10. UX Guidelines

99 rules with severity levels. **High** = must fix, **Medium** = should fix, **Low** = nice to have.

### 10.1 Navigation

| Rule | Platform | Do | Don't | Severity |
|------|----------|-----|-------|----------|
| Smooth Scroll | Web | `scroll-behavior: smooth` on html | Jump without transition | High |
| Sticky Navigation | Web | Add `pt-{nav-height}` to body | Let nav overlap content | Medium |
| Active State | All | Highlight current page/section | No visual feedback | Medium |
| Back Button | Mobile | Preserve navigation history | Break browser back | High |
| Deep Linking | All | URLs reflect current state | Static URLs for dynamic content | Medium |
| Breadcrumbs | Web | Use for 3+ level depth | Use for flat sites | Low |
| Bottom Nav Limit | Mobile | Max 5 items with labels + icons | More than 5 items, icon-only | High |
| Drawer Usage | Mobile | Use for secondary navigation | Use for primary actions | Medium |
| Back Behavior | All | Predictable, consistent back; preserve scroll/state | Broken back, lost state | High |
| Nav Label + Icon | All | Both icon and text label | Icon-only navigation | High |
| Nav State Active | All | Visually highlight current location | No active indicator | Medium |
| Nav Hierarchy | All | Primary vs secondary nav clearly separated | Everything at same level | Medium |
| Modal Escape | All | Clear close/dismiss affordance; swipe-down on mobile | No way to close modal | High |
| Search Accessible | Web | Top bar or tab; provide recent/suggested queries | Buried search | Medium |
| State Preservation | All | Back restores scroll, filters, inputs | Lost state on back | High |
| Overflow Menu | All | Use overflow/more when actions exceed space | Cram everything in | Medium |
| Adaptive Navigation | All | ≥1024px sidebar; small screens bottom/top nav | Same nav on all sizes | Medium |
| Back Stack Integrity | All | Never silently reset navigation stack | Unexpected jump to home | High |
| Navigation Consistency | All | Same placement across all pages | Change nav by page type | Medium |
| Avoid Mixed Patterns | All | Don't mix Tab + Sidebar + Bottom Nav at same level | Mixed nav patterns | Medium |
| Modal vs Navigation | All | Don't use modals for primary navigation flows | Modal as navigation | Medium |
| Focus on Route Change | All | Move focus to main content after page transition | No focus management | Medium |
| Persistent Nav | All | Core nav reachable from deep pages | Hidden nav in sub-flows | Medium |
| Destructive Nav Separation | All | Delete/logout visually separated from normal nav | Destructive actions in nav | Medium |
| Empty Nav State | All | Explain why destination unavailable | Silently hide unavailable nav | Low |

### 10.2 Animation

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Excessive Motion | Animate 1-2 elements per view max | Animate everything | High |
| Duration Timing | 150-300ms for micro-interactions; complex ≤400ms | >500ms for UI transitions | Medium |
| Reduced Motion | Check `prefers-reduced-motion` | Ignore motion settings | High |
| Loading States | Skeleton screens or spinners | Leave UI frozen | High |
| Hover vs Tap | Use click/tap for primary actions | Rely only on hover | High |
| Continuous Animation | Infinite only for loading indicators | Infinite decorative animations | Medium |
| Transform Performance | Animate `transform` and `opacity` | Animate `width`/`height`/`top`/`left` | Medium |
| Easing Functions | `ease-out` for entering, `ease-in` for exiting | `linear` for UI transitions | Low |
| Motion Meaning | Every animation expresses cause-effect relationship | Decorative-only animation | Medium |
| State Transition | State changes animate smoothly, not snap | Instant state changes | Medium |
| Spatial Continuity | Page transitions maintain spatial continuity | Disorienting jumps | Medium |
| Spring Physics | Prefer spring/physics-based curves for natural feel | Linear or rigid cubic-bezier | Low |
| Exit Faster Than Enter | Exit animations ~60-70% of enter duration | Same duration for enter/exit | Low |
| Stagger Sequence | Stagger list/grid items by 30-50ms per item | All-at-once or too-slow reveals | Low |
| Shared Element Transition | Use shared element/hero transitions between screens | No visual continuity between views | Low |
| Interruptible | Animations must be interruptible by user tap/gesture | Blocking animations | Medium |
| No Blocking Animation | Never block user input during animation | UI frozen during animation | High |
| Scale Feedback | Subtle scale (0.95-1.05) on press for cards/buttons | No press feedback | Low |
| Gesture Feedback | Drag/swipe/pinch provide real-time visual response | Delayed or no feedback during gesture | Medium |
| Hierarchy Motion | Enter from below = deeper, exit upward = back | Random motion directions | Low |
| Motion Consistency | Unify duration/easing tokens globally | Different rhythm per component | Low |
| Modal Motion | Modals animate from trigger source (scale+fade or slide-in) | Generic fade-in for all modals | Low |
| Navigation Direction | Forward = left/up; backward = right/down | Inconsistent navigation motion | Low |
| Layout Shift Avoid | Animations use transform, not layout properties | Animation causes CLS | Medium |

### 10.3 Layout

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Z-Index Management | Use z-index scale (e.g., 10, 20, 30) | Random z-index values | High |
| Overflow Hidden | Check for clipped content | Blindly apply `overflow: hidden` | Medium |
| Fixed Positioning | Account for fixed element height | Ignore overlap with content | Medium |
| Stacking Context | Be aware of new contexts | Assume z-index is global | Medium |
| Content Jumping | Reserve space for async content | Let layout shift on load | High |
| Viewport Units | Use `dvh`/`svh` instead of `100vh` on mobile | Use `100vh` (broken on mobile) | Medium |
| Container Width | Max-width 65-75ch for body text | Full-width text lines | Medium |

### 10.4 Touch

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Touch Target Size | Min 44x44px (iOS) / 48x48dp (Android) | Smaller than 44px | High |
| Touch Spacing | Min 8px between targets | Adjacent interactive elements | Medium |
| Gesture Conflicts | Use standard gestures | Custom gestures that override system | Medium |
| Tap Delay | Remove 300ms delay (`touch-action: manipulation`) | Leave 300ms tap delay | Medium |
| Pull to Refresh | Only where expected | Allow accidental refresh | Low |
| Haptic Feedback | Use for important confirmations | Overuse for every interaction | Low |

### 10.5 Interaction

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Focus States | Visible focus ring for keyboard users | No focus indicator | High |
| Hover States | Visual feedback on interactive elements | No hover feedback | Medium |
| Active States | Show press/click feedback | No immediate feedback | Medium |
| Disabled States | Visual distinction + `aria-disabled` | Remove from DOM | Medium |
| Loading Buttons | Disable + show spinner during async | Allow double submission | High |
| Error Feedback | Clear error message near the problem | Silent failures | High |
| Success Feedback | Confirm successful actions | No confirmation | Medium |
| Confirmation Dialogs | For destructive actions (delete, remove) | No confirmation for destructive actions | High |

### 10.6 Accessibility

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Color Contrast | WCAG AA minimum (4.5:1 body, 3:1 large) | Low contrast text | High |
| Color Only | Use icons/labels + color | Convey info by color alone | High |
| Alt Text | Meaningful alt for informative images | Empty alt on informative images | High |
| Heading Hierarchy | Sequential H1→H2→H3 | Skip heading levels | Medium |
| ARIA Labels | Label all interactive elements | Unlabeled buttons/links | High |
| Keyboard Navigation | All functionality via keyboard | Keyboard-inaccessible features | High |
| Screen Reader | Content makes sense read aloud | Visual-only information | Medium |
| Form Labels | Visible labels for all inputs | Placeholder-only labels | High |
| Error Messages | Announced via `aria-live` | Errors not announced | High |
| Skip Links | "Skip to main content" link | No skip navigation | Medium |
| Motion Sensitivity | Respect `prefers-reduced-motion` | Parallax/scroll-jacking for everyone | High |

### 10.7 Forms

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Input Labels | Visible label above or beside input | Placeholder as only label | High |
| Error Placement | Error message below the input | Error far from the field | Medium |
| Inline Validation | Validate on blur (not every keystroke) | Validate only on submit | Medium |
| Input Types | `type="email"`, `type="tel"`, etc. | Use `type="text"` for everything | Medium |
| Autofill Support | Use `autocomplete` attributes | Ignore browser autofill | Medium |
| Required Indicators | `*` or "(required)" label | No indication of required fields | Medium |
| Password Visibility | Toggle show/hide password | No way to see password while typing | Medium |
| Submit Feedback | Show loading state + success/error | No feedback after submit | High |
| Input Affordance | Inputs look interactive (border, bg) | Inputs blend with background | Medium |
| Mobile Keyboards | `inputmode="numeric"` for numbers | Default keyboard for phone/zip | Medium |
| Input Helper Text | Persistent helper text below complex inputs | Placeholder-only guidance | Medium |
| Disabled States | Reduced opacity (0.38-0.5) + cursor change + semantic attr | Controls that look tappable but do nothing | Medium |
| Progressive Disclosure | Reveal complex options progressively | Overwhelm users upfront | Medium |
| Error Recovery | Error messages include clear recovery path (retry, edit, help) | Dead-end error messages | High |
| Multi-Step Progress | Show step indicator or progress bar; allow back | No progress feedback | Medium |
| Form Autosave | Long forms auto-save drafts to prevent data loss | No auto-save on long forms | Low |
| Sheet Dismiss Confirm | Confirm before dismissing modal with unsaved changes | Unsaved changes lost on dismiss | Medium |
| Error Clarity | State cause + how to fix (not just "Invalid input") | Vague error messages | High |
| Field Grouping | Group related fields logically (fieldset/visual grouping) | Random field order | Medium |
| Focus Management | After submit error, auto-focus first invalid field | No focus management on error | Medium |
| Error Summary | Multiple errors: summary at top with anchor links | Errors only inline | Low |
| Touch Friendly Input | Mobile input height ≥44px | Tiny inputs on mobile | Medium |
| Destructive Emphasis | Destructive actions use danger color, visually separated | Destructive actions same as primary | Medium |
| Toast Accessibility | Toasts use `aria-live="polite"`, don't steal focus | Toasts that steal focus | Medium |
| ARIA Live Errors | Form errors use `aria-live` region or `role="alert"` | Errors not announced to screen readers | High |
| Contrast Feedback | Error/success colors meet 4.5:1 contrast ratio | Low-contrast error messages | Medium |
| Timeout Feedback | Request timeout shows clear feedback with retry option | Silent timeout | Medium |
| Undo Support | Allow undo for destructive/bulk actions (e.g. "Undo delete" toast) | No undo for destructive actions | Low |
| Success Feedback | Brief visual feedback for completed actions (checkmark, toast) | No confirmation after success | Medium |
| Read-Only Distinction | Read-only visually and semantically different from disabled | Read-only looks disabled | Low |

### 10.8 Responsive

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Mobile First | Design mobile, enhance for larger | Desktop-first with breakpoints | Medium |
| Breakpoint Testing | Test at 320px, 375px, 768px, 1024px, 1440px | Only test one size | Medium |
| Touch Friendly | Touch-sized targets on mobile layouts | Desktop-sized buttons on mobile | High |
| Readable Font Size | Min 16px body text on mobile | <14px body text | High |
| Viewport Meta | `<meta name="viewport" content="width=device-width, initial-scale=1">` | No viewport meta | High |
| Horizontal Scroll | Never cause horizontal scroll | Content wider than viewport | High |
| Image Scaling | `max-width: 100%` on all images | Fixed-width images | Medium |
| Table Handling | Horizontal scroll or card layout for tables | Tables that overflow on mobile | Medium |

### 10.9 Typography

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Line Height | 1.5-1.75 for body, 1.2-1.3 for headings | Tight line-height for body | Medium |
| Line Length | 45-75 characters per line | >80 characters per line | Medium |
| Font Size Scale | Consistent type scale (1.25 ratio) | Random font sizes | Medium |
| Font Loading | `font-display: swap` + preload | FOUT/FOIT without fallback | Medium |
| Contrast Readability | Body text 4.5:1+ contrast | Low contrast body text | High |
| Heading Clarity | Headings visually distinct from body | Headings blend with body text | Medium |
| Text Styles System | Use platform type system (display/headline/title/body/label) | Ad-hoc font sizes per component | Medium |
| Weight Hierarchy | Bold headings (600-700), Regular body (400), Medium labels (500) | Same weight everywhere | Medium |
| Color Semantic | Define semantic color tokens (primary, secondary, error, surface) | Raw hex in components | Medium |
| Color Dark Mode | Desaturated/lighter tonal variants, not inverted; test contrast separately | Inverted colors for dark mode | Medium |
| Color Accessible Pairs | Foreground/background pairs meet 4.5:1 (AA) or 7:1 (AAA) | Low-contrast color pairs | High |
| Color Not Decorative Only | Functional color (error red, success green) must include icon/text | Color-only meaning | High |
| Truncation Strategy | Prefer wrapping; when truncating use ellipsis + tooltip/expand | Truncation without way to see full text | Low |
| Letter-spacing | Respect default per platform; avoid tight tracking on body | Tight letter-spacing on body text | Low |
| Number Tabular | Tabular/monospaced figures for data columns, prices, timers | Proportional figures in data tables | Low |
| Whitespace Balance | Use whitespace to group related items, separate sections | Visual clutter, no breathing room | Low |

### 10.10 Performance

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Image Optimization | WebP/AVIF, responsive `srcset` | Uncompressed PNG/JPEG | High |
| Lazy Loading | `loading="lazy"` for below-fold images | Load all images immediately | Medium |
| Code Splitting | Dynamic imports for non-critical code | Single large bundle | Medium |
| Caching | Cache static assets with long max-age | No cache headers | Medium |
| Font Loading | Preconnect to font origins | No preconnect | Medium |
| Third Party Scripts | Defer non-critical scripts | Blocking third-party scripts | Medium |
| Bundle Size | Monitor and limit JS bundle size | Unchecked bundle growth | Medium |
| Render Blocking | Inline critical CSS, defer rest | All CSS render-blocking | Medium |
| Image Dimension | Declare width/height or aspect-ratio to prevent CLS | Images without dimensions causing layout shift | High |
| Font Preload | Preload only critical fonts | Overusing preload on every variant | Low |
| Reduce Reflows | Avoid frequent layout reads/writes; batch DOM reads then writes | Layout thrashing | Medium |
| Content Jumping | Reserve space for async content to avoid layout jumps | CLS from unsized elements | High |
| Virtualize Lists | Virtualize lists with 50+ items | Rendering all items in long lists | Medium |
| Main Thread Budget | Keep per-frame work under ~16ms for 60fps | Heavy tasks blocking main thread | Medium |
| Progressive Loading | Skeleton screens/shimmer for >1s operations | Long blocking spinners | Medium |
| Input Latency | Keep input latency under ~100ms for taps/scrolls | Laggy input response | Medium |
| Debounce Throttle | Debounce/throttle high-frequency events (scroll, resize, input) | Unthrottled scroll/resize handlers | Medium |
| Offline Support | Provide offline state messaging and basic fallback | No offline handling | Low |
| Network Fallback | Degraded modes for slow networks (lower-res, fewer animations) | Full experience on slow networks | Low |
| Tap Feedback Speed | Visual feedback within 100ms of tap | Delayed tap response | Medium |

### 10.11 Feedback

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Loading Indicators | Show system status during waits | Silent loading | High |
| Empty States | Guide users when no content exists | Blank empty states | Medium |
| Error Recovery | Help users recover from errors | Dead-end error messages | Medium |
| Progress Indicators | Show progress for multi-step processes | No progress feedback | Medium |
| Toast Notifications | Transient messages for non-critical info | Modal for every notification | Medium |
| Confirmation Messages | Confirm successful actions | No success confirmation | Medium |

### 10.12 Content

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Truncation | Handle long content gracefully (ellipsis, expand) | Overflow or cut off | Medium |
| Date Formatting | Use locale-appropriate formats | Hardcoded date format | Low |
| Number Formatting | Format large numbers (1,000 / 10K) | Raw numbers (10000) | Low |
| Placeholder Content | Show realistic placeholders during dev | Lorem ipsum in production | Low |

### 10.13 AI & Spatial

| Rule | Platform | Do | Don't | Severity |
|------|----------|-----|-------|----------|
| AI Disclaimer | All | Tell users they're talking to AI | Hide AI nature | High |
| AI Streaming | All | Stream responses for perceived speed | Wait for full response | Medium |
| AI Feedback | All | Let users rate/correct AI output | No feedback mechanism | Low |
| Gaze Hover | VisionOS | Respond to eye tracking before pinch | Only gesture-based input | High |
| Depth Layering | VisionOS | Z-depth to separate content layers | Flat UI in spatial context | Medium |

### 10.14 Sustainability

| Rule | Do | Don't | Severity |
|------|-----|-------|----------|
| Auto-Play Video | Never auto-play video with sound | Auto-play heavy video | Medium |
| Asset Weight | Optimize 3D/image assets for web | Ship uncompressed heavy assets | Medium |

---

## 11. Industry Design Reasoning

Decision rules by product type. Use when starting a new project to quickly establish design direction.

| Product | Recommended Pattern | Style Priority | Color Mood | Typography | Key Effects | Avoid |
|---------|---------------------|----------------|------------|------------|-------------|-------|
| **SaaS (General)** | Hero+Features+CTA | Glassmorphism+Flat | Trust blue+Accent | Professional+Hierarchy | Subtle hover 200-250ms | Excessive animation, Dark by default |
| **Micro SaaS** | Hero-Centric+Trust | Motion-Driven+Vibrant | Bold primaries+Accent | Modern+Energetic | Scroll-triggered, Parallax | Static, No video, Poor mobile |
| **E-commerce** | Feature-Rich Showcase | Vibrant&Block | Brand+Success green | Engaging+Clear hierarchy | Card hover lift 200ms | Flat without depth, Text-heavy |
| **E-commerce Luxury** | Feature-Rich Showcase | Liquid Glass+Glassmorphism | Premium+Minimal accent | Elegant+Refined | Chromatic aberration, Fluid 400-600ms | Vibrant blocks, Playful colors |
| **B2B Service** | Feature-Rich+Trust | Trust&Authority+Minimalism | Professional blue+Neutral | Formal+Clear | Section transitions, Feature reveals | Playful, Hidden credentials |
| **Financial Dashboard** | Data-Dense Dashboard | Dark OLED+Data-Dense | Dark bg+Red/Green alerts | Clear+Readable | Real-time number animations | Light mode, Slow rendering |
| **Analytics Dashboard** | Data-Dense+Drill-Down | Data-Dense+Heat Map | Cool→Hot gradients | Clear+Functional | Hover tooltips, Chart zoom | Ornate design, No filtering |
| **Healthcare App** | Social Proof-Focused | Neumorphism+Accessible | Calm blue+Health green | Readable+Large 16px+ | Soft shadow, Smooth press 150ms | Neon, Motion-heavy, AI purple |
| **Educational App** | Feature-Rich Showcase | Claymorphism+Micro-interactions | Playful+Clear hierarchy | Friendly+Engaging | Soft press 200ms, Fluffy | Dark modes, Complex jargon |
| **Creative Agency** | Hero-Centric+Portfolio | Motion-Driven+Aurora | Bold gradient+Accent | Creative+Display | Scroll animations, Parallax | Corporate blue, Generic layout |
| **Gaming** | Hero-Centric+Showcase | Cyberpunk+Vibrant | Neon+Dark bg | Bold+Display | Glitch effects, Particle animations | Pastel colors, Soft shadows |
| **Real Estate** | Feature-Rich+Gallery | Minimal&Direct+Trust | Professional+Trustworthy | Clean+Serif accent | Image hover, Virtual tour | Overly decorative, Slow loading |
| **Restaurant** | Hero-Centric+Menu | Organic+Vintage Analog | Warm earth tones | Elegant+Readable | Image gallery, Menu animations | Neon colors, Corporate feel |
| **Non-Profit** | Storytelling-Driven | Accessible&Ethical | Trust blue+Success green | Readable+Large | Impact counters, Donation flow | Dark modes, Complex navigation |

---

## 12. Chart Selection Guide

Match data type to chart type. Each rule includes when to use, when not to use, and accessibility notes.

| Data Type | Best Chart | When to Use | When NOT to Use | A11y Grade |
|-----------|------------|-------------|-----------------|------------|
| **Trend Over Time** | Line Chart | Time axis, observe rise/fall | <4 data points, >6 series | AA |
| **Compare Categories** | Bar Chart | Discrete categories, ranking | >15 categories, time dimension | AAA |
| **Part-to-Whole** | Pie/Donut | ≤5 categories, visual proportion | >5 categories, need precise values | C |
| **Correlation** | Scatter/Bubble | Two continuous variables, clusters | Categorical variables, <20 points | B |
| **Heatmap/Intensity** | Heat Map | 2D grid, time patterns | <20 cells, need exact values | B |
| **Geographic** | Choropleth/Bubble Map | Regional/location dimension | Different-sized regions misleading | B |
| **Funnel/Flow** | Funnel/Sankey | Sequential stages, conversion | Non-sequential, non-decreasing | AA |
| **Performance vs Target** | Gauge/Bullet | Single KPI vs target | No target, multiple KPIs | AA |
| **Forecast** | Line + Confidence Band | Historical + predictions | No historical baseline | AA |
| **Anomaly Detection** | Line + Highlights | Time-series outliers | Predefined categories | AA |
| **Hierarchical** | Treemap | Size in hierarchy, overview | >3 levels deep | B |
| **Flow/Process** | Sankey | Quantities between nodes | Loop directions | AA |
| **Cumulative Changes** | Waterfall | Additive pos/neg components | Non-additive, >12 bars | AA |
| **Multi-Variable** | Radar/Spider | Compare entities across axes | >8 axes, need precise values | B |
| **Stock/OHLC** | Candlestick | Financial OHLC data | Non-financial audience | B |
| **Network** | Network Graph | Connections between entities | >500 nodes without clustering | C |
| **Distribution** | Box Plot | Spread, median, outliers | <20 points per group | B |
| **KPI Dashboard** | Bullet Chart | Multiple KPIs side by side | Single KPI emphasis | AA |
| **Proportion/Fraction** | Waffle Chart | What fraction of whole is filled | >5 categories, exact values | A |
| **Nested Proportion** | Sunburst | Nested proportions + hierarchy | >3 levels, mobile context | C |
| **Root Cause** | Decomposition Tree | Metric → contributing factors | No causal relationship | AA |
| **3D Spatial** | 3D Scatter/Surface | Z-axis carries meaning | 2D conveys same insight | C |
| **Real-Time** | Streaming Area | Live monitoring, IoT/ops | Update <1/min | AA |
| **Sentiment** | Word Cloud + Sentiment | NLP output, exploratory | Precise values matter | C |
| **Process Mining** | Process Map | Event logs, actual process flow | No event log data | B |

### Chart Color Guidance

| Pattern | Color Strategy |
|---------|---------------|
| Single series | Primary brand color |
| Multiple series | Distinct colors + distinct line styles (solid/dashed/dotted) |
| Positive/Negative | Green `#22C55E` / Red `#EF4444` |
| Heatmap gradient | Cool (blue) → Warm (red) |
| Confidence band | 15% opacity fill of forecast color |
| Foreground vs Background | High contrast for foreground, muted for background |

### Chart Accessibility Rules

1. **Never rely on color alone** — add patterns, labels, or shapes
2. **Provide data table fallback** — every chart should have a table alternative
3. **Keyboard navigation** — allow tabbing through data points
4. **ARIA labels** — describe chart purpose and key insights
5. **Colorblind safe** — use tools like Coblis to verify palettes

---

## 13. Common Rules for Professional UI

Frequently overlooked issues that make UI look unprofessional.

### Icons & Visual Elements

| Rule | Standard | Avoid | Why It Matters |
|------|----------|--------|----------------|
| No Emoji as Structural Icons | Use SVG icons (Lucide, Heroicons) | Emojis for navigation/settings/controls | Font-dependent, inconsistent across platforms |
| Vector-Only Assets | SVG or platform vector icons | Raster PNG icons | Scalable, crisp, dark/light mode adaptable |
| Stable Interaction States | Color/opacity/elevation transitions without layout shift | Layout-shifting transforms | Prevents jitter, preserves smooth motion |
| Consistent Icon Sizing | Design tokens (icon-sm/md/lg) | Mixing arbitrary values (20/24/28px) | Maintains rhythm and visual hierarchy |
| Stroke Consistency | Consistent stroke width per layer (1.5px or 2px) | Mixing thick and thin arbitrarily | Reduces perceived polish issues |
| Filled vs Outline Discipline | One icon style per hierarchy level | Mixing filled/outline at same level | Semantic clarity and stylistic coherence |
| Touch Target Minimum | 44×44pt interactive area (use hitSlop if smaller) | Small icons without expanded tap area | Accessibility and usability standards |
| Icon Alignment | Align to text baseline, consistent padding | Misaligned icons | Prevents subtle visual imbalance |
| Icon Contrast | WCAG 4.5:1 for small, 3:1 for larger UI glyphs | Low-contrast icons | Accessibility in light and dark modes |

### Interaction

| Rule | Do | Don't |
|------|----|-----|
| Tap Feedback | Clear pressed feedback (ripple/opacity/elevation) within 80-150ms | No visual response on tap |
| Animation Timing | Micro-interactions 150-300ms with platform-native easing | Instant transitions or >500ms |
| Accessibility Focus | Screen reader focus order matches visual order; descriptive labels | Unlabeled controls, confusing focus traversal |
| Disabled State Clarity | Disabled semantics + reduced emphasis + no tap action | Controls that look tappable but do nothing |
| Touch Target Minimum | ≥44×44pt (iOS) / ≥48×48dp (Android); expand hit area for small icons | Tiny tap targets without padding |
| Gesture Conflict Prevention | One primary gesture per region; avoid nested tap/drag conflicts | Overlapping gestures causing accidental actions |
| Semantic Native Controls | Prefer native primitives (Button, Pressable) with proper a11y roles | Generic containers as primary controls |

### Light/Dark Mode Contrast

| Rule | Do | Don't |
|------|----|-----|
| Surface Readability (Light) | Cards/surfaces clearly separated from background with opacity/elevation | Overly transparent surfaces |
| Text Contrast (Light) | Body text ≥4.5:1 against light surfaces | Low-contrast gray body text |
| Text Contrast (Dark) | Primary ≥4.5:1, secondary ≥3:1 on dark surfaces | Dark mode text blends into background |
| Border Visibility | Separators visible in both themes | Theme-specific borders disappearing |
| State Contrast Parity | Pressed/focused/disabled equally distinguishable in both themes | Interaction states for one theme only |
| Token-Driven Theming | Semantic color tokens mapped per theme | Hardcoded per-screen hex values |
| Scrim & Modal Legibility | Modal scrim 40-60% black to isolate foreground | Weak scrim leaving background competing |

### Layout & Spacing

| Rule | Do | Don't |
|------|----|-----|
| Safe-Area Compliance | Respect top/bottom safe areas for fixed headers, tab bars, CTA bars | Fixed UI under notch/status bar/gesture area |
| System Bar Clearance | Spacing for status/navigation bars and gesture home indicator | Tappable content colliding with OS chrome |
| Consistent Content Width | Predictable width per device class (phone/tablet) | Mixing arbitrary widths between screens |
| 8dp Spacing Rhythm | Consistent 4/8dp spacing for padding/gaps/sections | Random spacing increments |
| Readable Text Measure | Long-form text readable on large devices | Full-width long text on tablets |
| Section Spacing Hierarchy | Clear vertical rhythm tiers (16/24/32/48) by hierarchy | Inconsistent spacing for similar levels |
| Adaptive Gutters | Increase horizontal insets on larger widths and landscape | Same narrow gutter on all sizes |
| Scroll + Fixed Coexistence | Bottom/top insets so lists aren't hidden behind fixed bars | Scroll content obscured by sticky bars |

---

## 14. Pre-Delivery Checklist

### Visual Quality
- [ ] No emojis used as icons (use SVG instead)
- [ ] All icons from a consistent icon family and style
- [ ] Pressed-state visuals don't shift layout bounds
- [ ] Semantic theme tokens used consistently (no ad-hoc hardcoded colors)

### Interaction
- [ ] All tappable elements provide clear pressed feedback
- [ ] Touch targets meet minimum size (≥44×44pt iOS, ≥48×48dp Android)
- [ ] Micro-interaction timing 150-300ms with native easing
- [ ] Disabled states visually clear and non-interactive
- [ ] Screen reader focus order matches visual order
- [ ] Gesture regions avoid nested/conflicting interactions

### Light/Dark Mode
- [ ] Primary text contrast ≥4.5:1 in both modes
- [ ] Secondary text contrast ≥3:1 in both modes
- [ ] Dividers/borders and interaction states distinguishable in both modes
- [ ] Modal/drawer scrim opacity preserves foreground legibility (40-60% black)
- [ ] Both themes tested before delivery

### Layout
- [ ] Safe areas respected for headers, tab bars, bottom CTA bars
- [ ] Scroll content not hidden behind fixed/sticky bars
- [ ] Verified on small phone, large phone, and tablet (portrait + landscape)
- [ ] Horizontal insets adapt correctly by device size
- [ ] 4/8dp spacing rhythm maintained
- [ ] Long-form text readable on larger devices

### Accessibility
- [ ] All meaningful images/icons have accessibility labels
- [ ] Form fields have labels, hints, and clear error messages
- [ ] Color is not the only indicator
- [ ] Reduced motion and dynamic text size supported
- [ ] Accessibility traits/roles/states announced correctly

---

## 15. Next.js Best Practices

52 rules for Next.js App Router. Code examples included.

### Routing

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| App Router | `app/` directory with `page.tsx` | `pages/` for new projects | `app/dashboard/page.tsx` | `pages/dashboard.tsx` | Medium |
| File-based routing | Create routes by adding files in `app/` | Manual route configuration | `app/blog/[slug]/page.tsx` | Custom router setup | Medium |
| Route groups | Parentheses for grouping without URL change | Nested folders affecting URL | `(marketing)/about/page.tsx` | `marketing/about/page.tsx` | Low |
| Loading states | `loading.tsx` alongside `page.tsx` | Manual loading state management | `app/dashboard/loading.tsx` | `useState` for loading in page | Medium |
| Error handling | `error.tsx` with reset function | `try/catch` in every component | `app/dashboard/error.tsx` | `try/catch` in page component | High |

### Rendering

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Server Components default | Keep components server by default | Add `'use client'` unnecessarily | `export default function Page()` | `('use client')` for static content | High |
| Client Components | `'use client'` only when needed | Server Component with hooks/events | `('use client')` for onClick/useState | No directive with useState | High |
| Push Client down | Client wrapper for interactive parts only | Mark page as Client Component | `<InteractiveButton/>` in Server Page | `('use client')` on page.tsx | High |
| Streaming | `Suspense` for slow data fetches | Wait for all data before render | `<Suspense><SlowComponent/></Suspense>` | `await allData` then render | Medium |
| Rendering strategy | SSG for static, SSR for dynamic, ISR for semi-static | SSR for static content | `export const revalidate = 3600` | `fetch` without cache config | Medium |

### Data Fetching

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Fetch in Server Components | `async function Page() { await fetch() }` | `useEffect` for initial data | `const data = await fetch(url)` | `useEffect(() => fetch(url))` | High |
| Cache config (Next.js 15+) | Explicitly set `cache: 'force-cache'` for static | Assume default is cached | `fetch(url, { cache: 'force-cache' })` | `fetch(url)` // Uncached in v15 | High |
| Server Actions | `action={serverAction}` in forms | API route for every mutation | `<form action={createPost}>` | `<form onSubmit={callApiRoute}>` | Medium |
| Revalidate | `revalidatePath`/`revalidateTag` after mutations | `'use client'` with manual refetch | `revalidatePath('/posts')` | `router.refresh()` everywhere | Medium |

### Images

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Use next/image | `<Image>` for all images | `<img>` tags directly | `<Image src={} alt={} width={} height={}/>` | `<img src={}/>` | High |
| Provide dimensions | `width` and `height` props or `fill` | Missing dimensions | `<Image width={400} height={300}/>` | `<Image src={url}/>` | High |
| Remote domains | `remotePatterns` in `next.config.js` | Allow all domains | `remotePatterns: [{ hostname: 'cdn.example.com' }]` | `domains: ['*']` | High |
| Priority for LCP | `priority` prop on hero images | All images with priority | `<Image priority src={hero}/>` | `<Image priority/>` on every image | Medium |

### Fonts & Metadata

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| next/font | Self-hosted fonts with zero layout shift | External font links | `import { Inter } from 'next/font/google'` | `<link href="fonts.googleapis.com"/>` | Medium |
| generateMetadata | Dynamic metadata based on params | Hardcoded metadata everywhere | `generateMetadata({ params })` | `export const metadata = {}` | Medium |
| OpenGraph | Add OG images for social sharing | Missing social preview images | `opengraph: { images: ['/og.png'] }` | No OG configuration | Medium |

### API, Middleware, Security

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Route Handlers | `app/api/` routes for API endpoints | `pages/api` for new projects | `export async function GET(request)` | `export default function handler` | Medium |
| Validate input | Zod or similar for validation | Trust client input | `const body = schema.parse(await req.json())` | `const body = await req.json()` | High |
| Middleware auth | `middleware.ts` at root | Auth check in every page | `export function middleware(request)` | `if (!session) redirect in page` | Medium |
| Middleware matcher | `config.matcher` for specific routes | Run middleware on all routes | `matcher: ['/dashboard/:path*']` | No matcher config | Medium |
| Env vars | `NEXT_PUBLIC_` prefix for client vars | Server vars exposed to client | `NEXT_PUBLIC_API_URL` | `API_SECRET` in client code | High |
| Input sanitize | Escape, sanitize, validate all input | Direct interpolation of user data | `DOMPurify.sanitize(userInput)` | `dangerouslySetInnerHTML={{ __html: userInput }}` | High |
| CSP headers | Configure CSP in `next.config.js` | No security headers | `headers()` with CSP | No CSP configuration | High |

### Performance & Config

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Dynamic imports | `dynamic()` for heavy components | Import everything statically | `const Chart = dynamic(() => import('./Chart'))` | `import Chart from './Chart'` | Medium |
| Avoid CLS | Skeleton loaders, aspect ratios | Content popping in | `<Skeleton className="h-48"/>` | No placeholder for async content | High |
| Link component | `<Link href="">` for internal links | `<a>` for internal navigation | `<Link href="/about">About</Link>` | `<a href="/about">About</a>` | High |
| Strict mode | `reactStrictMode: true` | Strict mode disabled | `reactStrictMode: true` | `reactStrictMode: false` | Medium |
| Standalone output | `output: 'standalone'` for Docker | Default output for containers | `output: 'standalone'` | No output config for Docker | Medium |

---

## 16. React Best Practices

53 rules for React patterns and performance.

### State Management

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| useState for local state | Simple component state | Class components this.state | `const [count, setCount] = useState(0)` | `this.state = { count: 0 }` | Medium |
| Lift state up | Share state by lifting to common ancestor | Prop drilling through many levels | Parent holds state, passes down | Deep prop chains | Medium |
| useReducer for complex | Complex state with multiple sub-values | Multiple useState for related values | `useReducer` with action types | 5+ `useState` calls that update together | Medium |
| Avoid unnecessary state | Derive values from existing state | Store derivable values in state | `const total = items.reduce(...)` | `const [total, setTotal] = useState(0)` | High |
| Lazy initialization | `useState(() => computeExpensive())` | `useState(computeExpensive())` | `useState(() => JSON.parse(data))` | `useState(JSON.parse(data))` | Medium |

### Effects & Hooks

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Cleanup effects | Return cleanup function in `useEffect` | No cleanup for subscriptions | `useEffect(() => { sub(); return unsub; })` | `useEffect(() => { subscribe(); })` | High |
| Correct dependencies | All referenced values in dependency array | Empty deps with external references | `[value]` when using value in effect | `[]` when using props/state in effect | High |
| Avoid unnecessary effects | Transform data during render, handle events directly | `useEffect` for derived state or event handling | `const filtered = items.filter(...)` | `useEffect(() => setFiltered(items.filter(...)))` | High |
| Refs for non-reactive | `useRef` for interval IDs, DOM elements | `useState` for values that don't need render | `const intervalRef = useRef(null)` | `const [intervalId, setIntervalId] = useState()` | Medium |
| Rules of hooks | Only call hooks at top level in React functions | Hooks in conditions, loops, or callbacks | `const [x, setX] = useState()` | `if (cond) { const [x, setX] = useState() }` | High |
| Custom hooks | Extract shared stateful logic to custom hooks | Duplicate hook logic across components | `const { data } = useFetch(url)` | Duplicate `useEffect`/`useState` in components | Medium |
| use prefix | Custom hooks must start with `use` | `fetchData` or `getData` for hook | `function useFetch(url)` | `function fetchData(url)` | High |

### Rendering & Components

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Stable keys | Use stable IDs as keys | Array index as key for dynamic lists | `key={item.id}` | `key={index}` | High |
| useMemo | `useMemo` for expensive filtering/sorting | Recalculate every render | `useMemo(() => expensive(), [deps])` | `const result = expensiveCalc()` | Medium |
| useCallback | `useCallback` for handlers passed to memoized children | New function reference every render | `useCallback(() => {}, [deps])` | `const handler = () => {}` | Medium |
| React.memo | `memo` for pure components with stable props | `memo` everything or nothing | `memo(ExpensiveList)` | `memo(SimpleButton)` | Low |
| Avoid inline objects | Define style objects outside component | Inline objects in props | `<div style={styles.container}>` | `<div style={{ margin: 10 }}>` | Medium |
| Small components | Single responsibility per component | Large multi-purpose components | `<UserAvatar /><UserName />` | `<UserCard />` with 500 lines | Medium |
| Composition | Use `children` prop for flexibility | Inheritance hierarchies | `<Card>{content}</Card>` | `class SpecialCard extends Card` | Medium |
| Fragments | `<>` for grouping without DOM node | Extra div wrappers | `<>{items.map(...)}</>` | `<div>{items.map(...)}</div>` | Low |

### Props, Events, Forms

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Destructure props | Destructure in function signature | `props.name` throughout | `function User({ name, age })` | `function User(props)` | Low |
| Default values | Default values in destructuring | Undefined checks throughout | `function Button({ size = 'md' })` | `if (size === undefined) size = 'md'` | Low |
| Avoid prop drilling | Context or composition for deeply nested | Passing props through 5+ levels | `<UserContext.Provider>` | `<A user={u}><B user={u}><C user={u}>` | Medium |
| TypeScript props | `interface Props { name: string }` | PropTypes or no validation | `interface ButtonProps { onClick: () => void }` | `Button.propTypes = {}` | Medium |
| Pass handlers | Pass function reference, not invocation | `onClick={handleClick()}` causing immediate call | `onClick={handleClick}` | `onClick={handleClick()}` | High |
| Controlled forms | `value` + `onChange` for inputs | Uncontrolled inputs with refs | `<input value={val} onChange={setVal}>` | `<input ref={inputRef}>` | Medium |
| Debounce input | `useDeferredValue` or debounce for search | Filter on every keystroke | `useDeferredValue(searchTerm)` | `useEffect` filtering on every change | Medium |

### Performance & Error Handling

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| DevTools Profiler | Profile before optimizing | Optimize without measuring | React DevTools Profiler | Guessing at bottlenecks | Medium |
| Lazy load routes | `lazy()` for routes and heavy components | Import everything upfront | `const Page = lazy(() => import('./Page'))` | `import Page from './Page'` | Medium |
| Virtualize lists | `react-window` or `react-virtual` for 100+ items | Render thousands of DOM nodes | `<VirtualizedList items={items}/>` | `{items.map(i => <Item />)}` | High |
| Error boundaries | `ErrorBoundary` wrapping sections | Let errors crash entire app | `<ErrorBoundary><App/></ErrorBoundary>` | No error handling | High |
| Async error handling | `try/catch` in async handlers | Unhandled promise rejections | `try { await fetch() } catch(e) {}` | `await fetch() // no catch` | High |

### Accessibility & TypeScript

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Semantic HTML | `button` for clicks, `nav` for navigation | `div` with onClick for buttons | `<button onClick={...}>` | `<div onClick={...}>` | High |
| Focus management | Focus trap in modals, return focus on close | No focus management | `useEffect` to focus input | Modal without focus trap | High |
| ARIA live regions | `aria-live` for dynamic updates | Silent updates to screen readers | `<div aria-live="polite">{msg}</div>` | `<div>{msg}</div>` | Medium |
| Label controls | `htmlFor` matching input id | Placeholder as only label | `<label htmlFor="email">Email</label>` | `<input placeholder="Email"/>` | High |
| Type props | `interface Props` with all prop types | `any` or missing types | `interface Props { name: string }` | `function Component(props: any)` | High |

---

## 17. Tailwind CSS Best Practices

55 rules for Tailwind CSS v4.

### Animation & Z-Index

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Animate utilities | Built-in animations respect reduced-motion | Custom `@keyframes` for simple effects | `animate-pulse` | `@keyframes pulse {...}` | Medium |
| Limit bounce | Single CTA with `animate-bounce` only | 5+ elements with `animate-bounce` | Single CTA with `animate-bounce` | Multiple bounce animations | High |
| Transition duration | `duration-150` to `duration-300` for UI | `duration-1000` or longer for UI | `transition-all duration-200` | `transition-all duration-1000` | Medium |
| Z-index scale | `z-0 z-10 z-20 z-30 z-40 z-50` | Arbitrary z-index values | `z-50` for modals | `z-[9999]` | Medium |
| Fixed z-index | `z-50` for nav, `z-40` for dropdowns | Relying on DOM order for stacking | `fixed top-0 z-50` | `fixed top-0` (no z-index) | High |

### Layout & Images

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Container | `max-w-7xl mx-auto` for main content | Full-width content on large screens | `max-w-7xl mx-auto px-4` | `w-full` (no max-width) | Medium |
| Responsive padding | `px-4 sm:px-6 lg:px-8` | Same padding all sizes | `px-4 sm:px-6 lg:px-8` | `px-8` (same all sizes) | Medium |
| Grid gaps | `gap-4 gap-6 gap-8` | Margins on individual items | `grid gap-6` | `grid` with `mb-4` on each item | Medium |
| Aspect ratio | `aspect-video aspect-square` | No aspect ratio on containers | `aspect-video rounded-lg` | No aspect control | Medium |
| Object fit | `object-cover object-contain` | Stretched distorted images | `object-cover w-full h-full` | No object-fit | Medium |
| Lazy loading | `loading='lazy'` on images | All images eager load | `<img loading='lazy'>` | `<img>` without lazy | High |
| Responsive images | `srcset` and `sizes` attributes | Same large image all devices | `srcset` with multiple sizes | 4000px image everywhere | High |
| SVG dimensions | Add `width`/`height` attributes to SVGs | SVG without explicit dimensions | `<svg class='size-6' width='24' height='24'>` | `<svg class='size-6'>` | High |

### Typography & Colors

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Prose plugin | `prose prose-lg` for article content | Custom styles for markdown | `prose prose-lg max-w-none` | Custom text styling | Medium |
| Line height | `leading-relaxed` for body text | Default tight line height | `leading-relaxed` (1.625) | `leading-none` or `leading-tight` | Medium |
| Text truncation | `truncate` or `line-clamp-*` | Overflow breaking layout | `line-clamp-2` | No overflow handling | Medium |
| Dark mode | `dark:bg-gray-900 dark:text-white` | No dark mode support | `dark:bg-gray-900` | Only light theme | Medium |
| Semantic colors | `bg-primary text-success border-cta` | Generic color names in components | `bg-primary` | `bg-blue-500` everywhere | Medium |
| Theme variables | Define colors in Tailwind theme | `bg-[var(--color-primary)]` | `bg-primary` | `bg-[var(--color-primary)]` | Medium |
| Gradients v4 | `bg-linear-to-r` (v4 syntax) | `bg-gradient-to-*` (deprecated in v4) | `bg-linear-to-r from-blue-500 to-purple-500` | `bg-gradient-to-r` | Medium |

### Forms & Accessibility

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Focus states | `focus:ring-2 focus:ring-blue-500` | Remove focus outline | `focus:ring-2 focus:ring-offset-2` | `focus:outline-none` (no replacement) | High |
| Disabled states | `disabled:opacity-50 disabled:cursor-not-allowed` | No disabled indication | `disabled:opacity-50` | Same style as enabled | Medium |
| Touch targets | `min-h-[44px] min-w-[44px]` on mobile | Small buttons on mobile | `min-h-[44px] min-w-[44px]` | `h-8 w-8` on mobile | High |
| Loading buttons | `disabled` + spinner icon | Clickable during loading | `<Button disabled><Spinner/></Button>` | Button without loading state | High |
| Icon buttons | `aria-label` on icon buttons | Icon button without label | `<button aria-label='Close'><XIcon/></button>` | `<button><XIcon/></button>` | High |
| Screen reader text | `sr-only` for hidden labels | Missing context for icons | `<span class='sr-only'>Close menu</span>` | No label for icon button | High |
| Focus visible | `focus-visible:ring-2` (keyboard only) | Focus on all interactions | `focus-visible:ring-2` | `focus:ring-2` (shows on click too) | Medium |
| Reduced motion | `motion-reduce:animate-none` | Ignore motion preferences | `motion-reduce:transition-none` | No reduced motion support | High |

### Responsive & Performance

| Rule | Do | Don't | Code Good | Code Bad | Severity |
|------|-----|-------|-----------|----------|----------|
| Mobile-first | Default mobile + `md:` + `lg:` | Desktop-first approach | `text-sm md:text-base` | `text-base max-md:text-sm` | Medium |
| Breakpoint testing | Test at 320, 375, 768, 1024, 1280, 1536 | Only test on dev device | Test all breakpoints | Single device testing | High |
| Content paths | `content` array in config | Deprecated `purge` option (v2) | `content: ['./src/**/*.{js,ts,jsx,tsx}']` | `purge: [...]` | High |
| Avoid @apply bloat | Direct utilities in HTML | Heavy `@apply` usage | `class='px-4 py-2 rounded'` | `@apply px-4 py-2 rounded;` | Low |
| Size utilities | `size-4 size-8 size-12` for squares | Separate `h-* w-*` for squares | `size-6` | `h-6 w-6` | Low |
| Shrink shorthand | `shrink-0` | `flex-shrink-0` | `shrink-0` | `flex-shrink-0` | Low |

---

## 18. UI Styling (shadcn/ui + Tailwind)

### Core Stack

| Layer | Tool | Purpose |
|-------|------|---------|
| Component | shadcn/ui (Radix UI + Tailwind) | Pre-built accessible components, copy-paste distribution |
| Styling | Tailwind CSS | Utility-first, build-time, zero runtime |
| Design | Tokens | Consistent spacing, colors, typography |

### Setup

```bash
npx shadcn@latest init          # 初始化
npx shadcn@latest add button card dialog form  # 添加组件
```

### Component Patterns

**Form with validation:**
```tsx
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"

const schema = z.object({
  email: z.string().email(),
  password: z.string().min(8)
})

export function LoginForm() {
  const form = useForm({ resolver: zodResolver(schema), defaultValues: { email: "", password: "" } })
  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(console.log)} className="space-y-6">
        <FormField control={form.control} name="email" render={({ field }) => (
          <FormItem>
            <FormLabel>Email</FormLabel>
            <FormControl><Input type="email" {...field} /></FormControl>
            <FormMessage />
          </FormItem>
        )} />
        <Button type="submit" className="w-full">Sign In</Button>
      </form>
    </Form>
  )
}
```

**Responsive layout with dark mode:**
```tsx
<div className="min-h-screen bg-white dark:bg-gray-900">
  <div className="container mx-auto px-4 py-8">
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <Card className="bg-white dark:bg-gray-800 border-gray-200 dark:border-gray-700">
        <CardContent className="p-6">
          <h3 className="text-xl font-semibold text-gray-900 dark:text-white">Content</h3>
        </CardContent>
      </Card>
    </div>
  </div>
</div>
```

### Best Practices

1. **Component Composition**: Build complex UIs from simple, composable primitives
2. **Utility-First**: Use Tailwind classes directly; extract components only for true repetition
3. **Mobile-First**: Start with mobile styles, layer responsive variants
4. **Accessibility-First**: Leverage Radix UI primitives, add focus states, use semantic HTML
5. **Design Tokens**: Use consistent spacing scale, color palettes, typography system
6. **Dark Mode**: Apply dark variants to all themed elements
7. **Performance**: Leverage automatic CSS purging, avoid dynamic class names

---

## 19. Design Token Architecture

Three-layer token system: Primitive → Semantic → Component.

### Token Layers

```css
/* Primitive — raw values */
--color-blue-600: #2563EB;

/* Semantic — purpose aliases */
--color-primary: var(--color-blue-600);

/* Component — component-specific */
--button-bg: var(--color-primary);
```

### Benefits

- **Theme switching**: Semantic layer enables light/dark mode
- **Component customization**: Component tokens enable per-component override
- **HSL format**: Use HSL for opacity control
- **No raw hex**: Never use raw hex in components — always reference tokens

### Tailwind Integration

Define colors in Tailwind theme and use directly:
```css
/* Tailwind config */
--color-primary: #2563EB;
--color-secondary: #7C3AED;

/* Usage */
bg-primary text-secondary
```

---

## 20. Brand Identity Framework

Brand voice, visual identity, messaging, and consistency.

### Brand Elements

| Element | Description |
|---------|-------------|
| Voice Framework | Tone, personality, writing style |
| Visual Identity | Colors, typography, logo usage |
| Messaging | Value proposition, key messages |
| Consistency | Checklists, approval workflows |

### Brand Sync Workflow

1. Edit `docs/brand-guidelines.md` (source of truth)
2. Sync to design tokens
3. Verify token output

### Key References

| Topic | Focus |
|-------|-------|
| Voice Framework | Tone of voice, content guidelines |
| Visual Identity | Color palette, typography specs, logo rules |
| Messaging Framework | Value prop, key messages, elevator pitch |
| Color Management | Palette organization, usage rules |
| Typography Specs | Font choices, scale, usage guidelines |
| Logo Usage Rules | Sizing, spacing, clear space, misuse examples |
| Approval Checklist | Pre-publish brand compliance check |

---

## §21 — UI/UX Audit Pipeline

A three-stage audit for running web apps: Playwright walkthrough, axe-core WCAG scan, Lighthouse perf audit. Produces structured P0-P3 reports with screenshots and smoke tests.

### Prerequisites

| MCP Server | Package | Purpose |
|---|---|---|
| `playwright` | `@playwright/mcp` | Browser automation — navigate, click, screenshot |
| `a11y` | `a11y-mcp` | axe-core WCAG 2.1 AA scan |
| `lighthouse` | `lighthouse-mcp` | Performance / a11y / best-practices / SEO scores |

### Trigger Phrases

`audit ui/ux`, `ui audit`, `ux audit`, `ux review`, `a11y audit`, `accessibility audit`, `lighthouse audit`, or any request to audit a localhost/staging URL.

### Inputs

- **URL** (required) — running app URL. If not given, ask.
- **Primary user flow** — e.g. "sign up → create project → invite teammate". If unspecified, auto-discover from landing page CTA.
- **Auth strategy** — prefer email signup with dummy `audit-{ISO-timestamp}@example.com`. Only attempt OAuth if no email option.
- **Output directory** — default `./audit/` for report + screenshots, `./tests/` for smoke test.

### Pipeline

#### Stage 0 — Pre-flight

1. Confirm URL reachable (`curl -s -o /dev/null -w "%{http_code}"`). Fail fast if dev server not running.
2. Check `./audit/screenshots/` and `./tests/` are writable.
3. If cwd is read-only (Windows EPERM), reconfigure Playwright MCP output dir.

#### Stage 1 — Playwright Walkthrough

For each viewport in `[1440x900, 768x1024, 375x812]`:

1. `browser_resize` → `browser_navigate` → wait for hydration
2. `browser_snapshot` → read accessibility tree, note target refs
3. `browser_take_screenshot` (fullPage) → `./audit/screenshots/{NN}-{slug}-{viewport}.png`
4. Walk primary flow. For each state: snapshot + screenshot.
5. `browser_console_messages` — note errors, silent failures.

**Look for:**
- Layout: content full-width on desktop, off-screen drawers, broken responsive
- Dead controls: buttons that do nothing, console errors after click
- Copy bugs: pluralization errors, platform-wrong verbs, unfilled placeholders
- Missing affordances: tabs without active state, required fields with no announcement
- Empty/first-run UX: huge whitespace, no marketing, full-width CTAs

#### Stage 2 — Axe-core A11y Scan

For each unique URL:
```
mcp__a11y__audit_webpage with tags: ["wcag2a","wcag2aa","wcag21a","wcag21aa","best-practice"], includeHtml: true
```

Group by impact (critical/serious/moderate/minor). For each violation:
- Rule ID (e.g. `color-contrast`)
- Failing target selector
- One-line failure summary
- **Specific fix** (not "improve contrast" — give actual hex: "darken `#2a9d5f` to `~#218754` to clear 4.5:1")

Document auth limitations explicitly.

#### Stage 3 — Lighthouse Mobile Audit

```
mcp__lighthouse__run_audit with device: "mobile", categories: ["performance","accessibility","best-practices","seo"], throttling: true
```

**Dev server numbers are fiction.** Build production output first:

| Framework | Build | Serve |
|---|---|---|
| Next.js | `npm run build && npm start` | different port |
| Vite | `npm run build` | `npx serve dist -l 8090` |
| Expo | `npx expo export -p web` | `npx serve dist -l 8090` |
| CRA | `npm run build` | `npx serve build -l 8090` |

Re-run Lighthouse against prod build. Save JSON to `./audit/lighthouse-prod.json`.

### Output Artifacts

#### 1. `./audit/report.md`

```markdown
# {AppName} — UI/UX Audit

**Audited:** {date} · **URL:** {url} · **Build:** {dev|prod}
**Tooling:** @playwright/mcp · a11y-mcp (axe-core) · lighthouse-mcp
**Primary flow:** {description}
**Viewports:** 1440×900, 768×1024, 375×812
**Caveats:** {auth limitations, dev-vs-prod}

## 1. UX findings (ranked by impact)

### P0 — {title}
- **What I saw:** {observation + screenshot ref}
- **Why it matters:** {user impact}
- **Fix:** {specific, actionable — code path or API}

### P1 — ...
### P2 — ...
### P3 — ...

## 2. Accessibility violations (axe-core WCAG 2.1 A/AA)

| # | Rule ID | Severity | Element | Failing detail | Suggested fix |
|---|---------|----------|---------|----------------|---------------|

## 3. Lighthouse — mobile, throttled

| Category | Dev server | Prod build | Δ |
|----------|-----------|------------|---|
| Performance | ... | ... | ... |
```

**Severity definitions:**
- **P0** = ship-blocker, broken feature, dead control, critical a11y
- **P1** = serious UX issue affecting most users
- **P2** = noticeable but non-blocking (copy bugs, inconsistent affordances)
- **P3** = polish / nice-to-have

#### 2. `./audit/screenshots/`

PNG screenshots named `{NN}-{slug}-{viewport}.png`. Leading number for flow ordering. Same NN across viewports.

#### 3. `./tests/smoke.spec.ts`

Playwright test (`@playwright/test`) that re-runs the primary flow:
- `BASE_URL = process.env.BASE_URL ?? '<url>'`
- Unique account per run: `audit-${new Date().toISOString().replace(/[:.]/g,'-')}@example.com`
- Role-based selectors (`getByRole`), not CSS classes
- `test.describe` with viewport sanity checks (1440 / 768 / 375)

### Report-Writing Rules

- Don't sandbag. If home page is a wall of nothing, say so.
- Always pair findings with a specific fix. "Improve contrast" is useless; "darken `#2a9d5f` to `~#218754` (clears 4.5:1)" is useful.
- Cite screenshots by filename when describing visual issues.
- Note what you couldn't audit (auth-gated pages without session).
- Top 3 Lighthouse fixes sequenced by **payoff**, not severity.

### When NOT to Use

- Code review → use `code-review` skill
- Security review → use `security-review` skill
- "Is my design good?" without a running URL → ask for URL or Figma link first
