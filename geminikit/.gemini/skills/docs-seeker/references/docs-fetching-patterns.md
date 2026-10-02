---
mode: latest | example | compare
extends: docs-seeker
version: "1.0.0"
---

# Mode: --latest

Retrieve the most recent version docs and breaking changes.

## Extra Rules
- MUST search for: `{library} changelog`, `{library} v{latest} migration`, `{library} breaking changes`
- Check npm/PyPI/pub.dev for latest published version number before searching docs
- Prioritize official CHANGELOG.md and migration guides over third-party blog posts
- If breaking changes found, list them in a dedicated `breaking_changes` array
- Include minimum required runtime/peer versions

## Extra Output
```json
{
  "latest_version": "string",
  "previous_version": "string",
  "breaking_changes": ["string — describe what broke and migration path"],
  "new_features": ["string"],
  "migration_guide_url": "string"
}
```

## Steps
1. Fetch latest version from package registry
2. Search official changelog for breaking changes since installed version
3. Extract migration steps
4. Identify deprecated APIs and replacements
5. Return structured version diff

## Examples
**Input:** `/gk-docs-seeker --latest next.js`
**Expected behavior:** Reports Next.js 15 vs 14 breaking changes (App Router changes, caching defaults, etc.)

---

# Mode: --example

Fetch working code examples for a specific API or pattern.

## Extra Rules
- Search: `{library} {api_name} example`, `{library} {pattern} code sample`, official docs examples
- Prioritize: official docs > official repo examples > high-quality OSS usage
- Examples MUST be runnable — verify they use current API (not deprecated signatures)
- Adapt example to project's detected language/framework if identifiable
- Include import/install snippet with every example

## Extra Output
```json
{
  "examples": [
    {
      "title": "string",
      "code": "string",
      "language": "string",
      "source_url": "string",
      "notes": "string (optional)"
    }
  ]
}
```

## Steps
1. Search for official code examples in docs
2. Validate examples use current non-deprecated API
3. Adapt to project stack if known
4. Include import and install commands
5. Return 2-3 most relevant examples

## Examples
**Input:** `/gk-docs-seeker --example "react-query useInfiniteQuery"`
**Expected behavior:** 2-3 working examples showing `useInfiniteQuery` with `getNextPageParam`, typed with TypeScript

---

# Mode: --compare

Compare two libraries or frameworks for best fit.

## Extra Rules
- Evaluate both options across: performance, bundle size, DX, maintenance status, ecosystem
- MUST check GitHub stars, last commit date, open issues for maintenance signal
- Use a comparison matrix — never recommend without quantitative backing
- State which is better FOR THE USER's specific use case (not generically)
- Flag if one option is deprecated or unmaintained

## Extra Output
```json
{
  "option_a": { "name": "string", "version": "string", "stars": "number", "last_release": "string" },
  "option_b": { "name": "string", "version": "string", "stars": "number", "last_release": "string" },
  "comparison_matrix": {
    "performance": "string — which wins and why",
    "bundle_size": "string",
    "dx": "string",
    "maintenance": "string",
    "ecosystem": "string"
  },
  "recommendation": "string — for user's specific use case"
}
```

## Steps
1. Search for both libraries' latest versions and maintenance status
2. Gather benchmark data (bundle size, performance) from official sources
3. Build comparison matrix across 5 dimensions
4. Identify user's specific requirements from context
5. Recommend best fit with explicit rationale

## Examples
**Input:** `/gk-docs-seeker --compare "zustand vs jotai"`
**Expected behavior:** Matrix comparing bundle size, boilerplate, React 18 compat, DevTools; recommends based on project scale
