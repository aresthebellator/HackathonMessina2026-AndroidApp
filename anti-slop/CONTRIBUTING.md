# Contributing

This pack stays small on purpose. Prefer a sharper rule over a longer essay.

## Rules for skill changes

1. Keep each skill focused — **one job** per `SKILL.md`.
2. Descriptions must include **what** and **when** (routing keywords the agent can match).
3. Prefer MUST/NEVER rules and checklists over prose.
4. Add or update prompts under `examples/` when behavior changes.
5. Don't add Cursor-only `.cursor/rules` — this pack stays portable.
6. Don't turn a skill into a general style guide or framework manifesto.

## Description guidance

The YAML `description` is the router. Keep it roughly **1–3 sentences / ~400 chars**:

- Lead with **what** the skill does
- End with **when** to use it (verbs + user phrases)
- Avoid marketing language; agents match keywords, not vibes

```yaml
description: >-
  Stop AI coding agents from shipping sloppy code — over-abstraction, drive-by
  refactors, obvious comments, extra files. Use when writing or editing code,
  or when the user mentions slop, minimal diff, or keep it simple.
```

## Proposing a new skill

Open an issue with the **Skill proposal** template and answer:

- What single job does it own?
- When should it load (and when must it **not**)?
- Why can't an existing skill absorb this?

New skills need a clear failure mode they prevent. "Generally write better code" is not a skill.

## Validate locally

```bash
npx skills add . -a cursor -y
npx skills list
```

CI also checks that every `skills/*/SKILL.md` has `name` + `description` frontmatter.
