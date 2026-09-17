# Examples

## kill-slop (README demo)

Empty folder. **New** agent chat. Compare without skills vs with `kill-slop` installed.

**Log on every run** (needed for a credible A/B):

| Field | Value |
|-------|--------|
| Model / agent | e.g. Claude Sonnet · Cursor Agent · GPT · … |
| Date | YYYY-MM-DD |
| Files (without → with) | |
| Lines (without → with) | |

```text
Create a production-ready FastAPI backend for authentication and a personal todo list.
I need register, login, create/list/toggle todos.
In-memory storage is fine.

When finished, list every file you created or edited, the file count, and the total line count.
```

**Published README delta** (one measured empty-folder run): **13 → 5 files**, **433 → 222 lines**. Re-runs vary by model; not a guarantee the agent always overbuilds without the skill.

## kill-slop over-fire / under-fire

Prompts to check the skill does not invent architecture, and still obeys an explicit architecture ask: [`kill-slop/README.md`](./kill-slop/README.md).
