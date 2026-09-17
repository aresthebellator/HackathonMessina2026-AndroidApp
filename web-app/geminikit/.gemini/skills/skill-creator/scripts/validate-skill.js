#!/usr/bin/env node
/**
 * validate-skill.js — Validates a Gemini Kit SKILL.md against the quality bar.
 *
 * Usage:
 *   node validate-skill.js <path/to/SKILL.md>
 *   node validate-skill.js .gemini/skills/my-skill/SKILL.md
 *
 * Exit codes:
 *   0 — all checks passed
 *   1 — one or more checks failed
 */

const fs = require('fs')
const path = require('path')

const RED = '\x1b[31m'
const GREEN = '\x1b[32m'
const YELLOW = '\x1b[33m'
const RESET = '\x1b[0m'

function pass(msg) { console.log(`${GREEN}✓${RESET} ${msg}`) }
function fail(msg) { console.log(`${RED}✗${RESET} ${msg}`) }
function warn(msg) { console.log(`${YELLOW}⚠${RESET} ${msg}`) }

function parseFrontmatter(content) {
  const match = content.match(/^---\n([\s\S]*?)\n---/)
  if (!match) return null
  const fm = {}
  for (const line of match[1].split('\n')) {
    const idx = line.indexOf(':')
    if (idx === -1) continue
    const key = line.slice(0, idx).trim()
    const value = line.slice(idx + 1).trim().replace(/^["']|["']$/g, '')
    fm[key] = value
  }
  return fm
}

function validateSkill(filePath) {
  const absPath = path.resolve(filePath)

  if (!fs.existsSync(absPath)) {
    console.error(`${RED}ERROR${RESET}: File not found: ${absPath}`)
    process.exit(1)
  }

  const content = fs.readFileSync(absPath, 'utf8')
  const lines = content.split('\n')
  const errors = []
  const warnings = []

  console.log(`\nValidating: ${absPath}\n`)

  // ── Frontmatter ───────────────────────────────────────────────────────────

  const fm = parseFrontmatter(content)
  if (!fm) {
    errors.push('No YAML frontmatter found (must start with ---)')
  } else {
    // Required fields
    for (const field of ['name', 'agent', 'version', 'description']) {
      if (!fm[field]) errors.push(`Frontmatter missing required field: ${field}`)
    }

    // tier field (quality bar)
    if (!fm['tier']) {
      errors.push('Frontmatter missing `tier` field (core | optional | internal)')
    } else if (!['core', 'optional', 'internal'].includes(fm['tier'])) {
      errors.push(`Invalid tier value: "${fm['tier']}" — must be core | optional | internal`)
    }

    // name must start with gk-
    if (fm['name'] && !fm['name'].startsWith('gk-')) {
      errors.push(`name must start with "gk-" prefix (got: "${fm['name']}")`)
    }

    // kebab-case name
    if (fm['name'] && !/^gk-[a-z0-9-]+$/.test(fm['name'])) {
      errors.push(`name must be kebab-case: gk-[a-z0-9-]+ (got: "${fm['name']}")`)
    }

    // description length
    if (fm['description']) {
      const len = fm['description'].length
      if (len > 200) {
        errors.push(`description too long: ${len} chars (max 200)`)
      }
      // Check for generic placeholder
      if (fm['description'].toLowerCase().includes('one sentence') ||
          fm['description'] === 'What this skill does (action-oriented)') {
        errors.push('description appears to be a placeholder — write a pushy, trigger-effective description')
      }
      // Warn if no "use when" trigger context
      const hasTrigger = /use when|use for|whenever|for when/i.test(fm['description'])
      if (!hasTrigger) {
        warnings.push('description has no trigger context ("Use when..." or "Use for...") — may undertrigger')
      }
    }

    // version semver
    if (fm['version'] && !/^\d+\.\d+\.\d+$/.test(fm['version'])) {
      errors.push(`version must be semver (e.g. "1.0.0"), got: "${fm['version']}"`)
    }
  }

  // ── Required sections (quality bar) ──────────────────────────────────────

  const requiredSections = [
    { pattern: /^## Tools/m, name: '## Tools' },
    { pattern: /^## Interface/m, name: '## Interface' },
    { pattern: /^## Initialization/m, name: '## Initialization (Required)' },
    { pattern: /^## Interaction Protocol/m, name: '## Interaction Protocol (CRITICAL)' },
    { pattern: /^## Gemini-Specific Optimizations/m, name: '## Gemini-Specific Optimizations' },
    { pattern: /^## Error Recovery/m, name: '## Error Recovery' },
    { pattern: /^## Steps/m, name: '## Steps' },
    { pattern: /^# (Role|Objective|Input|Rules|Output)/m, name: '# Role / Objective / Input / Rules / Output' },
  ]

  for (const { pattern, name } of requiredSections) {
    if (!pattern.test(content)) {
      errors.push(`Missing required section: ${name}`)
    }
  }

  // ── Error Recovery table ──────────────────────────────────────────────────

  if (/^## Error Recovery/m.test(content)) {
    const hasBlocked = /BLOCKED/i.test(content)
    const hasFailed = /FAILED/i.test(content)
    if (!hasBlocked) errors.push('Error Recovery table missing BLOCKED row')
    if (!hasFailed) errors.push('Error Recovery table missing FAILED row')
  }

  // ── Scope declaration ─────────────────────────────────────────────────────

  const hasScopeDecl = /does not handle|does NOT handle|not handle|MUST NOT.*(?:handle|process)/i.test(content)
  if (!hasScopeDecl) {
    warnings.push('No scope declaration found — add "This skill handles X. Does NOT handle Y." in Rules')
  }

  // ── Security policy ───────────────────────────────────────────────────────

  const hasSecurityRule = /validate.*input|redact.*secret|secret.*redact/i.test(content)
  if (!hasSecurityRule) {
    warnings.push('No security rule found — add input validation + secrets redaction rule')
  }

  // ── JSON Output Ban rule ──────────────────────────────────────────────────

  if (/^# Rules/m.test(content)) {
    const hasJsonBan = /JSON Output Ban|NEVER.*render.*raw JSON|NEVER.*embed.*JSON/i.test(content)
    if (!hasJsonBan) {
      warnings.push('Rules section missing JSON Output Ban rule — add: "NEVER render raw JSON blocks in user-facing responses"')
    }
  }

  // ── Mode files existence ──────────────────────────────────────────────────
  // Use string-slice to avoid m-flag $ matching end-of-line prematurely

  const modeMappingIdx = content.search(/^## Mode Mapping/m)
  if (modeMappingIdx !== -1) {
    const rest = content.slice(modeMappingIdx)
    const sectionEnd = rest.search(/\n##|\n# /)
    const mappingContent = sectionEnd !== -1 ? rest.slice(0, sectionEnd) : rest
    const skillDir = path.dirname(absPath)
    // Only require reference file when Reference column explicitly points to ./references/<flag>.md
    // Rows with "(base skill rules)" or similar do not require a file
    const flagWithFileRefs = [...mappingContent.matchAll(/\|\s*--(\w[\w-]*)\s*\|[^|]*\|\s*\.\/references\/([\w-]+\.md)\s*\|/g)]
    for (const m of flagWithFileRefs) {
      const flag = m[1]
      const refFile = m[2]
      const modeFile = path.join(skillDir, 'references', refFile)
      if (!fs.existsSync(modeFile)) {
        errors.push(`Mode Mapping declares --${flag} with reference references/${refFile} but file not found`)
      }
    }
  }

  // ── Output schema ─────────────────────────────────────────────────────────

  if (!/# Output/m.test(content)) {
    errors.push('Missing # Output section')
  } else {
    if (!/"status"/.test(content)) errors.push('Output schema missing "status" field')
    if (!/"summary"/.test(content)) errors.push('Output schema missing "summary" field')
    if (!/blocked/i.test(content)) warnings.push('Output section missing blocked example')
    if (!/failed/i.test(content)) warnings.push('Output section missing failed example')
  }

  // ── Line count ────────────────────────────────────────────────────────────

  if (lines.length > 200) {
    warnings.push(`File is ${lines.length} lines — aim for ≤ 200`)
  }

  // ── Results ───────────────────────────────────────────────────────────────

  console.log('── Frontmatter ──')
  if (fm) {
    fm['name']        ? pass(`name: ${fm['name']}`)        : fail('name: missing')
    fm['agent']       ? pass(`agent: ${fm['agent']}`)      : fail('agent: missing')
    fm['version']     ? pass(`version: ${fm['version']}`)  : fail('version: missing')
    fm['tier']        ? pass(`tier: ${fm['tier']}`)        : fail('tier: missing')
    fm['description'] ? pass(`description: ${fm['description'].slice(0, 60)}…`) : fail('description: missing')
  } else {
    fail('Frontmatter: not found')
  }

  console.log('\n── Required Sections ──')
  for (const { pattern, name } of requiredSections) {
    pattern.test(content) ? pass(name) : fail(name)
  }

  console.log('\n── Content Checks ──')
  pass(`Line count: ${lines.length}${lines.length > 200 ? ' ⚠ exceeds 200' : ''}`)

  if (warnings.length > 0) {
    console.log(`\n── Warnings (${warnings.length}) ──`)
    warnings.forEach(w => warn(w))
  }

  if (errors.length > 0) {
    console.log(`\n── Errors (${errors.length}) ──`)
    errors.forEach(e => fail(e))
    console.log(`\n${RED}FAILED${RESET} — ${errors.length} error(s), ${warnings.length} warning(s)\n`)
    process.exit(1)
  } else {
    console.log(`\n${GREEN}PASSED${RESET} — 0 errors, ${warnings.length} warning(s)\n`)
    process.exit(0)
  }
}

const arg = process.argv[2]
if (!arg) {
  console.error(`Usage: node validate-skill.js <path/to/SKILL.md>`)
  process.exit(1)
}
validateSkill(arg)
