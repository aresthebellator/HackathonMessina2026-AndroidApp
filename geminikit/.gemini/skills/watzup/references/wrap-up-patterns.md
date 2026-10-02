# Wrap-Up Patterns Reference

Patterns and templates for effective session wrap-up reports.

## Git Commands for Session Scoping

```bash
# Commits in last 8 hours (typical work session)
git log --oneline --since="8 hours ago"

# Commits since specific time
git log --oneline --since="2024-01-15 09:00"

# Diff stats for changed files
git diff --stat HEAD~3

# Full diff with context
git diff HEAD~3 -- "*.ts" "*.js"

# Find TODOs in changed files
git diff HEAD~3 --name-only | xargs grep -n "TODO\|FIXME\|HACK" 2>/dev/null

# Staged but uncommitted changes
git diff --cached --stat
```

## Session Scoping Heuristics

| Scenario | Git Command | Fallback |
|----------|-------------|----------|
| Active session (commits made) | `git log --oneline --since="8 hours ago"` | `git log --oneline -10` |
| Working on feature branch | `git log --oneline main..HEAD` | `git log --oneline -5` |
| No commits yet | `git status` + `git diff` | List modified files only |
| After merge | `git log --oneline --merges -3` | Diff against merge base |

## Wrap-Up Report Structure

### Standard Format

```markdown
## Session Wrap-Up — {date} {time}

**Duration:** {N} hours
**Branch:** {branch-name}
**Commits:** {count}

### What Changed
- {file-or-feature}: {brief description}
- {file-or-feature}: {brief description}

### Accomplished
{2-3 sentence narrative of the session's main achievement}

### Unresolved Items
- [ ] {TODO from code}
- [ ] {failing test or known issue}
- [ ] {deferred decision}

### Next Steps
1. [CRITICAL] {action with file/location}
2. [HIGH] {action}
3. [MEDIUM] {action}

### Notes
{any context future-self needs}
```

### Brief Format (--brief flag)

```markdown
## Quick Wrap — {date}

Changed: {files list}
Done: {1 sentence}
Blocked: {item or "none"}
Next: {single top priority}
```

## Common Patterns

### Feature Implementation Session
- Summary leads with "Implemented X feature"
- Next steps: tests → review → docs
- Flag any missing error handling as HIGH priority

### Bug Fix Session
- Summary includes root cause ("Fixed null dereference caused by...")
- Next steps: regression test → deploy verification
- Flag related areas that may have same bug pattern

### Refactor Session
- Summary includes scope ("Refactored auth module — 340 lines → 4 focused files")
- List any behavior changes even if intentional
- Flag any tests that need updating

### Investigation/Research Session
- Summary states conclusion ("Determined that X approach is not viable because...")
- List findings as unresolved if decision not made
- Next steps: implementation plan or second opinion

## Memory File Integration

When `.gemini/memory/execution.md` exists, extract:
- Tool calls made (what was searched/read)
- Error patterns encountered
- Decisions recorded during session

When `.gemini/memory/short-term.md` exists, extract:
- Active task context
- Pending items noted during session
- User preferences set this session

## Anti-Patterns to Avoid

| Anti-Pattern | Instead |
|--------------|---------|
| "Fixed some bugs" | "Fixed null check in token.ts:45 — prevented crash on expired JWT" |
| Listing every file changed | Group by feature/concern |
| Vague next steps ("continue work") | Specific action ("Write unit tests for parseToken() in token.ts") |
| Ignoring TODO comments | Always grep changed files for TODO/FIXME |
| Missing unresolved items | Check git diff for commented-out code, disabled tests |

## Priority Classification

- **CRITICAL**: Blocks deployment or causes data loss
- **HIGH**: Blocks other developers or affects correctness
- **MEDIUM**: Technical debt, missing tests, cleanup
- **LOW**: Style, docs, nice-to-have improvements

## Example: Feature Branch Wrap-Up

```
Session: 6 hours | Branch: feat/user-auth | 4 commits

Changed:
- src/auth/ (new): JWT service, middleware, route handlers
- src/types/auth.ts (new): AuthToken, UserSession types  
- src/app.ts (modified): registered /auth routes

Accomplished:
Implemented complete JWT authentication flow including token issuance, 
validation middleware, and refresh endpoint. All routes protected by 
auth middleware using Bearer token scheme.

Unresolved:
- TODO: rate limiting on /auth/login (src/auth/routes.ts:23)
- Token refresh not covered by tests
- FIXME: logout doesn't invalidate tokens server-side

Next Steps:
1. [HIGH] Write tests for token refresh and expiry edge cases
2. [HIGH] Implement server-side token blacklist for logout
3. [MEDIUM] Add rate limiting middleware (redis-based preferred)
4. [LOW] Add JSDoc to auth service public methods
```
