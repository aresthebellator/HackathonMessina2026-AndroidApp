# kill-slop: over-fire vs under-fire

Use these after installing `kill-slop`. Goal: confirm the skill stays sharp without ignoring explicit user intent.

## Over-fire (should stay minimal)

Empty folder. User did **not** ask for architecture layers.

```text
Create a production-ready FastAPI backend for authentication and a personal todo list.
I need register, login, create/list/toggle todos.
In-memory storage is fine.
```

**Expect:** flat layout (e.g. `main` + `auth` + store). No invented `domain/` / `application/` / `infrastructure/` trees, no unrequested deps, no drive-by extras.

**Fail if:** Clean Architecture folders, schema/router/config theater the prompt did not need, or deps nobody asked for.

## Under-fire (explicit ask must win)

Empty folder. User **explicitly** asks for a heavier shape.

```text
Create a FastAPI backend for authentication and a personal todo list
(register, login, create/list/toggle todos). In-memory storage is fine.

Use a Clean Architecture layout with domain/, application/, infrastructure/, and interface adapters. Keep layers explicit even if the app is small.
```

**Expect:** the requested layer folders (or an equivalent explicit layered layout). kill-slop must **not** flatten this away.

**Fail if:** the agent “simplifies” back to a flat app and ignores the Clean Architecture ask.

## Notes

- Over-fire protects the product promise (less slop).
- Under-fire protects trust (user intent wins). See `User intent wins` in [`skills/kill-slop/SKILL.md`](../../skills/kill-slop/SKILL.md).
- Log model + date when you run either case.
