---
name: duolingo-design-system
description: Create product and marketing designs inspired by the publicly observable Duolingo design language: playful, friendly, high-energy, accessible, and habit-forming. Use when designing interfaces, illustrations, campaigns, components, or brand-adjacent concepts that need this aesthetic.
---

# Duolingo-inspired design system

Use this skill to create work with the recognizable *design qualities* associated with Duolingo: approachable learning, expressive personality, strong visual hierarchy, cheerful color, and clear progress. Treat this as an inspiration guide, not an official Duolingo brand manual.

## Important boundaries

- Do not claim affiliation with Duolingo or present the output as an official Duolingo product.
- Do not reproduce the Duolingo wordmark, logo, Duo character, proprietary illustrations, exact campaign artwork, or other protected assets.
- Do not trace or recreate a distinctive existing screen. Build an original system with a similar emotional direction.
- Prefer original mascots, iconography, copy, names, and illustrations.
- If a precise brand value is unknown, choose a sensible accessible value and label it as an approximation rather than inventing official specifications.

## Core design intent

Every decision should reinforce these ideas:

1. **Learning should feel inviting.** Reduce intimidation with warmth, clarity, and small achievable steps.
2. **Progress should feel tangible.** Show momentum through completion states, streak-like continuity, milestones, levels, and celebratory feedback.
3. **Personality belongs in the product.** Use expressive characters, witty microcopy, and surprising but purposeful motion.
4. **Clarity comes before decoration.** Playfulness must never obscure the next action, lesson content, status, or error.
5. **Make returning feel rewarding.** Create a friendly ritual rather than pressure, shame, or manipulative urgency.

## Visual language

### Shape

- Favor rounded rectangles, soft corners, pill controls, circular badges, and chunky friendly silhouettes.
- Use simple geometric primitives with a few memorable irregular details; avoid sterile grids and razor-sharp corporate geometry.
- Give major controls a grounded, tactile feel with a visible base, inset, or lower edge rather than relying only on a floating shadow.
- Keep illustration contours smooth and bold. Avoid photorealism for primary learning moments.

### Color

Use a bright, high-contrast palette with one confident action color, a calm neutral foundation, and semantic feedback colors. A useful starting palette is:

| Role | Approximation | Use |
|---|---|---|
| Primary action green | `#58CC02` | Main positive action, progress, success |
| Deep green | `#1F8A00` | Pressed states, outlines, emphasis |
| Warm yellow | `#FFC800` | Rewards, points, highlights |
| Sky blue | `#1CB0F6` | Information, secondary progress |
| Coral/red | `#FF4B4B` | Errors, loss, destructive states |
| Purple | `#CE82FF` | Special content or premium accent |
| Warm white | `#FFFFFF` / `#F7F7F7` | Surfaces and page background |
| Ink | `#3C3C3C` | Primary readable text |

These are directional references, not guaranteed current official tokens. Check contrast for every text/background pair. Do not use all accent colors at once: establish one dominant accent and reserve the others for meaning.

### Typography

- Choose a rounded, humanist sans-serif with open counters and a sturdy bold weight.
- Use a strong weight contrast: bold, compact headings and highly legible regular body text.
- Keep headings short and conversational. Use sentence case unless a display treatment clearly benefits from another choice.
- Use generous line height for instructions and language examples.
- Never trade readability for a playful type effect. Avoid all-caps paragraphs, thin gray text, and decorative fonts for essential content.

### Illustration

- Use expressive original characters with oversized faces, readable poses, and clear emotional states.
- Keep forms compact, colorful, and slightly dimensional through flat shading, simple highlights, or soft gradients.
- Exaggerate gesture and facial expression more than anatomy.
- Build scenes from a few large shapes and one visual joke or surprise.
- Use visual metaphors for learning and progress: paths, doors, flags, stars, hearts, trophies, maps, and celebration particles.
- Avoid generic stock photography in the hero moment when an original illustration would communicate faster.

### Iconography

- Use simple, bold, rounded icons with consistent optical weight.
- Icons should remain recognizable at small sizes and work in monochrome before color is added.
- Use color to communicate state, not merely to decorate.
- Pair unfamiliar icons with labels; do not make users decode a mascot or symbol for a critical action.

## Component rules

### Buttons

- One clear primary action per surface.
- Use a short verb: “Start lesson”, “Review”, “Continue”, or an original equivalent.
- Make the primary button visually substantial, rounded, and easy to find.
- Include default, hover, pressed, disabled, focus, loading, and success states.
- A pressed state should feel physical: slightly reduced elevation or a visible lower edge.

### Cards and lesson units

- Use cards to break learning into approachable units.
- Show status at a glance: locked, available, in progress, complete, or needs review.
- Keep the card anatomy consistent: title, concise context, progress/status, then action.
- Use decorative art as a framing device, not as a competing focal point.

### Progress and rewards

- Make progress continuous and legible: bars, steps, paths, rings, or checkmarks.
- Celebrate completion with a brief visual or motion payoff, then return focus to the next useful action.
- Reward effort and consistency without creating anxiety around missed days.
- Explain what a reward means and how it was earned.

### Feedback and errors

- Use friendly, direct language. State what happened and what the user can do next.
- Pair semantic color with iconography and text; never rely on color alone.
- Avoid sarcasm, guilt, or language that makes a learner feel unintelligent.
- Keep error illustrations expressive but subordinate to the recovery action.

### Navigation

- Keep the information architecture shallow and predictable.
- Make the current location and next recommended action obvious.
- Use a persistent progress context where it helps users build a habit.
- Avoid burying practice, review, or accessibility settings behind novelty interactions.

## Motion and interaction

- Motion should clarify state changes, reward progress, or add personality.
- Prefer short, springy transitions with a clear beginning and end over constant ambient animation.
- Use scale, bounce, confetti, and character reactions sparingly at meaningful moments.
- Respect `prefers-reduced-motion`; provide an equivalent non-animated state.
- Never use motion to delay a critical action or hide a status change.

## Voice and microcopy

Write like a supportive, witty coach:

- Warm, concise, energetic, and specific.
- Encourage the next attempt instead of judging the previous one.
- Use occasional wordplay, but never let a joke obscure meaning.
- Prefer “You’re ready for one more” over pressure-heavy language.
- Keep labels action-oriented and feedback scannable.

Example tone:

- Good: “Nice work. Try one more review to lock it in.”
- Good: “Almost there — choose the word that means ‘bonjour’.”
- Avoid: “Wrong. You failed this lesson.”
- Avoid: “Don’t break your streak!” when it creates unnecessary anxiety.

## Accessibility and inclusivity

- Meet WCAG contrast requirements and verify focus visibility.
- Provide keyboard, touch, and screen-reader-friendly interactions.
- Use text, icons, shape, and placement together for state communication.
- Support localization: allow for longer strings, different scripts, and right-to-left layouts.
- Avoid relying on animal, national, cultural, or gender stereotypes in original characters.
- Keep learning content adjustable for text size, audio, timing, and sensory needs.

## Design process

When asked to create a screen, feature, brand concept, or component:

1. Identify the learner goal and the single next action.
2. Define the state model: unavailable, available, active, completed, error, and loading as applicable.
3. Establish a neutral layout and accessible hierarchy before adding personality.
4. Apply rounded geometry, a bright controlled palette, bold type, and original expressive artwork.
5. Add progress, reward, and microcopy only where they support comprehension or motivation.
6. Check responsive behavior, localization expansion, keyboard focus, reduced motion, and color contrast.
7. Run the originality check: no Duolingo logo, Duo, wordmark, copied illustration, or near-identical screen.

## Output checklist

Before presenting the result, confirm:

- The user can identify the next action in under a few seconds.
- The visual hierarchy works without the illustration.
- The palette has a clear dominant accent and accessible contrast.
- Progress and status are understandable without color alone.
- The interface feels optimistic and tactile, not childish or chaotic.
- The concept is original and clearly described as Duolingo-inspired rather than official.

