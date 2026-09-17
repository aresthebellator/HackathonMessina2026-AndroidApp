#!/usr/bin/env node
/**
 * check-description.js — Analyzes a skill's description field for trigger effectiveness.
 *
 * Usage:
 *   node check-description.js <path/to/SKILL.md>
 *
 * Exit codes:
 *   0 — description passes quality bar
 *   1 — description has issues that may cause undertriggering
 */

const fs = require('fs')
const path = require('path')

const RED    = '\x1b[31m'
const GREEN  = '\x1b[32m'
const YELLOW = '\x1b[33m'
const CYAN   = '\x1b[36m'
const RESET  = '\x1b[0m'

function pass(msg)  { console.log(`${GREEN}✓${RESET} ${msg}`) }
function fail(msg)  { console.log(`${RED}✗${RESET} ${msg}`) }
function warn(msg)  { console.log(`${YELLOW}⚠${RESET} ${msg}`) }
function info(msg)  { console.log(`${CYAN}ℹ${RESET} ${msg}`) }

const ACTION_VERBS = [
  'create', 'generate', 'build', 'analyze', 'review', 'audit', 'check',
  'scan', 'fix', 'debug', 'refactor', 'document', 'deploy', 'test',
  'search', 'fetch', 'compare', 'plan', 'run', 'execute', 'validate',
]

// Use non-overlapping patterns to avoid double-counting a single trigger phrase
// e.g. "Use when user asks" should count as 1 trigger, not 2
const TRIGGER_PATTERNS = [
  /use when(?! you need)/i,
  /use for\b/i,
  /whenever\b/i,
  /for when\b/i,
  /when you need/i,
]

const GENERIC_PHRASES = [
  'one sentence', 'action-oriented', 'what this skill does',
  '[skill-name]', 'placeholder', 'description here',
]

function parseFrontmatter(content) {
  const match = content.match(/^---\n([\s\S]*?)\n---/)
  if (!match) return null
  const fm = {}
  // Handle multi-line description (indented continuation)
  const raw = match[1]
  const lines = raw.split('\n')
  let currentKey = null
  let currentValue = []
  for (const line of lines) {
    const colonIdx = line.indexOf(':')
    // New key
    if (colonIdx !== -1 && !line.startsWith(' ') && !line.startsWith('\t')) {
      if (currentKey) fm[currentKey] = currentValue.join(' ').trim().replace(/^["']|["']$/g, '')
      currentKey = line.slice(0, colonIdx).trim()
      currentValue = [line.slice(colonIdx + 1).trim()]
    } else if (currentKey && (line.startsWith(' ') || line.startsWith('\t'))) {
      currentValue.push(line.trim())
    }
  }
  if (currentKey) fm[currentKey] = currentValue.join(' ').trim().replace(/^["']|["']$/g, '')
  return fm
}

function analyzeDescription(desc) {
  const errors = []
  const warnings = []
  let score = 0

  // Length check
  const len = desc.length
  if (len > 200) {
    errors.push(`Too long: ${len} chars (max 200). Remove ${len - 200} chars.`)
  } else if (len < 30) {
    errors.push(`Too short: ${len} chars. Add specific trigger contexts.`)
  } else {
    score += 20
    pass(`Length: ${len} chars (≤ 200)`)
  }

  // Generic placeholder check
  const genericFound = GENERIC_PHRASES.find(p => desc.toLowerCase().includes(p.toLowerCase()))
  if (genericFound) {
    errors.push(`Contains placeholder text: "${genericFound}" — replace with real description`)
  }

  // Action verb check
  const foundVerbs = ACTION_VERBS.filter(v => new RegExp(`\\b${v}`, 'i').test(desc))
  if (foundVerbs.length === 0) {
    errors.push(`No action verbs found. Add one of: ${ACTION_VERBS.slice(0, 8).join(', ')}, ...`)
  } else {
    score += 20
    pass(`Action verbs: ${foundVerbs.join(', ')}`)
  }

  // Trigger context check
  const foundTriggers = TRIGGER_PATTERNS.filter(p => p.test(desc))
  if (foundTriggers.length === 0) {
    errors.push('No trigger contexts found. Add "Use when..." or "Use for..." phrases.')
  } else if (foundTriggers.length === 1) {
    warnings.push('Only 1 trigger context. Aim for 2+ for better coverage.')
    score += 10
  } else {
    score += 30
    pass(`Trigger contexts: ${foundTriggers.length} found`)
  }

  // Specificity check — does it name the output type?
  const mentionsOutput = /report|file|plan|list|result|summary|output|schema|struct/i.test(desc)
  if (!mentionsOutput) {
    warnings.push('Description does not mention output type. Add what it produces (e.g., "...and returns a findings report").')
  } else {
    score += 15
    pass('Mentions output type')
  }

  // Uniqueness check — is this specific enough?
  const wordCount = desc.trim().split(/\s+/).length
  if (wordCount < 8) {
    warnings.push(`Only ${wordCount} words — likely too vague. Expand with specific contexts.`)
  } else {
    score += 15
    pass(`Word count: ${wordCount}`)
  }

  return { score, errors, warnings }
}

function run(filePath) {
  const absPath = path.resolve(filePath)

  if (!fs.existsSync(absPath)) {
    console.error(`${RED}ERROR${RESET}: File not found: ${absPath}`)
    process.exit(1)
  }

  const content = fs.readFileSync(absPath, 'utf8')
  const fm = parseFrontmatter(content)

  console.log(`\nChecking description: ${absPath}\n`)

  if (!fm) {
    fail('No YAML frontmatter found')
    process.exit(1)
  }

  if (!fm['description']) {
    fail('No description field in frontmatter')
    process.exit(1)
  }

  const desc = fm['description']
  info(`Current description:\n  "${desc}"\n`)

  const { score, errors, warnings } = analyzeDescription(desc)

  if (warnings.length > 0) {
    console.log(`\n── Warnings ──`)
    warnings.forEach(w => warn(w))
  }

  if (errors.length > 0) {
    console.log(`\n── Errors ──`)
    errors.forEach(e => fail(e))
  }

  console.log(`\n── Score: ${score}/100 ──`)

  if (errors.length > 0) {
    console.log(`\n${RED}UNDERTRIGGER RISK${RESET} — fix errors above before shipping\n`)

    // Suggest improved description
    console.log(`${CYAN}Suggested structure:${RESET}`)
    console.log(`  "${fm['name'] || 'Skill'} description. Use when [trigger1]. Use for [trigger2]. Returns [output type]."`)
    process.exit(1)
  } else if (score < 70) {
    console.log(`\n${YELLOW}FAIR${RESET} — address warnings to improve trigger accuracy\n`)
    process.exit(1)
  } else {
    console.log(`\n${GREEN}PASSED${RESET} — description quality is acceptable\n`)
    process.exit(0)
  }
}

const arg = process.argv[2]
if (!arg) {
  console.error('Usage: node check-description.js <path/to/SKILL.md>')
  process.exit(1)
}
run(arg)
