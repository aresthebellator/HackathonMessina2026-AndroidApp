---
mode: set | get | explain
extends: coding-level
version: "1.0.0"
---

# Level Profiles Reference

| Level | Label | Description | Code Style | Vocabulary |
|-------|-------|-------------|------------|------------|
| 0 | Beginner | Non-programmer; no code concepts | Analogies only; no syntax | Plain English, everyday analogies |
| 1 | Novice | Learning basics; knows variables/loops | Simple, heavily commented | Basic terms: function, variable, loop |
| 2 | Junior | 1-2 years exp; works on guided tasks | Annotated patterns, common idioms | Common patterns: callback, async/await |
| 3 | Mid | 3-5 years; writes features independently | Clean code, standard patterns | Architecture basics: MVC, REST, DRY |
| 4 | Senior | 5+ years; reviews, designs systems | Production-quality, trade-offs | Systems: scaling, observability, coupling |
| 5 | Architect | 10+ years; cross-system thinking | Terse, pattern-reference heavy | Strategic: CAP, distributed consensus, SLO |

---

# Mode: --set

Save user's coding level to memory.

## Extra Rules
- Level MUST be integer 0–5; reject values outside this range
- Write to `.gemini/memory/pinned.md` using format: `**Coding Level:** {N} ({Label})`
- Confirm the change with a brief message explaining what will change in responses
- If level increases, note which concepts will now be assumed as known

## Steps
1. Validate level is 0–5 integer
2. Read current `pinned.md` to check existing level
3. Write new level to `pinned.md`
4. Confirm change; describe what adjusts (vocabulary, code examples, assumed knowledge)

## Examples
**Input:** `/gk-coding-level --set 3`
**Expected behavior:** Saves "Mid" level; confirms that responses will now include architecture trade-offs and assume REST/async knowledge

---

# Mode: --get

Retrieve and display current coding level profile.

## Extra Rules
- Read `pinned.md` to extract current level
- If no level set, default to 2 (Junior) and inform user
- Show full profile: label, description, assumed knowledge, code style

## Steps
1. Read `.gemini/memory/pinned.md`
2. Extract level value
3. Display full profile for that level
4. Suggest `--set` to change

## Examples
**Input:** `/gk-coding-level --get`
**Expected behavior:** Shows "Level 3 – Mid: Responses assume REST, async/await, basic design patterns"

---

# Mode: --explain

Explain a concept tuned to the current coding level.

## Extra Rules
- Read current level from `pinned.md` before generating explanation
- Level 0–1: Use real-world analogies; avoid technical terms
- Level 2–3: Include working code snippets with brief comments
- Level 4–5: Focus on trade-offs, edge cases, and system implications
- Adjust vocabulary, depth, and example complexity to match level exactly

## Steps
1. Read current level from `pinned.md`
2. Select vocabulary and depth appropriate for level
3. Draft explanation: analogy (0-1) → pattern (2-3) → trade-offs (4-5)
4. Include code example scaled to level complexity
5. Offer to go deeper or simplify

## Examples
**Input:** `/gk-coding-level --explain "what is a database index"`
**Expected:** Level 1 → "Like a book's table of contents"; Level 4 → "B-tree vs hash index: selectivity, write overhead, composite index ordering"
