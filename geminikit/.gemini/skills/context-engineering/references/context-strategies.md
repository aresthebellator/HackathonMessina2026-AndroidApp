---
mode: audit | compress | archive
extends: context-engineering
version: "1.0.0"
---

# Mode: --audit

Measure current context usage and identify waste.

## Extra Rules
- Count tokens in: `execution.md`, `long-term.md`, `short-term.md`, and active conversation turns
- Report token budget remaining vs model limit (Gemini: 1M tokens)
- Flag files/sections consuming >10% of budget without recent access
- Identify duplicate information across memory files
- Report estimated turns remaining before context saturation

## Extra Output
```json
{
  "token_breakdown": {
    "execution_md": "number",
    "long_term_md": "number",
    "short_term_md": "number",
    "conversation": "number",
    "total": "number"
  },
  "budget_remaining": "number",
  "turns_remaining_estimate": "number",
  "waste_candidates": ["string — sections that can be compressed or archived"]
}
```

## Steps
1. Read all memory files and count tokens per file
2. Estimate conversation context from `execution.md` turn count
3. Compare against model limit
4. Identify waste: duplicates, stale entries, oversized sections
5. Recommend compress or archive for each waste candidate

## Examples
**Input:** `/gk-context-engineering --audit`
**Expected behavior:** Token breakdown per memory file, flagged waste sections, estimated headroom

---

# Mode: --compress

Compress memory files to reduce token usage.

## Extra Rules
- MUST read current file before compressing — never overwrite blindly
- Compression strategy by file:
  - `short-term.md`: Remove completed tasks; keep only unresolved blockers and active context
  - `execution.md`: Summarize completed phases into 1-2 sentences each
  - `long-term.md`: Deduplicate entries; merge similar patterns
- MUST preserve: active blockers, in-progress tasks, key architectural decisions
- Report bytes/tokens saved after compression

## Extra Output
```json
{
  "files_compressed": ["string"],
  "tokens_saved": "number",
  "entries_removed": "number",
  "preservation_notes": ["string — what was kept and why"]
}
```

## Steps
1. Read all target memory files
2. Identify compressible content per strategy above
3. Rewrite each file with compressed content
4. Verify critical context is preserved
5. Report savings

## Examples
**Input:** `/gk-context-engineering --compress`
**Expected behavior:** Rewrites memory files; reduces token count by 30-60%; reports what was removed

---

# Mode: --archive

Move stale memory content to archive files.

## Extra Rules
- Archive target: entries older than current sprint or unaccessed for 3+ sessions
- Archive location: `.gemini/memory/archive/{YYMMDD}-{slug}.md`
- NEVER delete archived content — always write to archive file first
- Update source memory file to reference archive: "See archive/{filename} for details"

## Extra Output
```json
{
  "archived_files": ["string"],
  "archive_paths": ["string"],
  "tokens_freed": "number"
}
```

## Steps
1. Read memory files and identify stale entries (by date or access recency)
2. Write stale entries to archive file with timestamp header
3. Replace stale entries in source with archive reference
4. Report archive path and tokens freed

## Examples
**Input:** `/gk-context-engineering --archive`
**Expected behavior:** Moves phase-01/02 entries from execution.md to archive; keeps phase-03+ active
