#!/usr/bin/env node
/**
 * benchmark-skill.js — Computes a composite quality score for a Gemini Kit SKILL.md.
 *
 * compositeScore = accuracy × 0.80 + security × 0.20
 *
 * Usage:
 *   node benchmark-skill.js <path/to/SKILL.md>
 *
 * Exit codes:
 *   0 — score ≥ 70 (passes bar)
 *   1 — score < 70 (fails bar)
 */

const fs = require('fs')
const path = require('path')

const RED    = '\x1b[31m'
const GREEN  = '\x1b[32m'
const YELLOW = '\x1b[33m'
const CYAN   = '\x1b[36m'
const RESET  = '\x1b[0m'

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

// ── Accuracy scoring (max 100) ──────────────────────────────────────────────

function scoreAccuracy(content, fm) {
  let score = 0
  const deductions = []
  const passes = []

  // Required sections (30 pts)
  const reqSections = ['## Tools', '## Steps', '## Error Recovery', '# Role', '# Objective', '# Input', '# Rules', '# Output']
  const presentSections = reqSections.filter(s => new RegExp('^' + s.replace(/[#]/g, '\\$&'), 'm').test(content))
  const sectionScore = Math.round((presentSections.length / reqSections.length) * 30)
  score += sectionScore
  if (sectionScore === 30) passes.push(`All ${reqSections.length} required sections present (+30)`)
  else deductions.push(`Missing sections: ${reqSections.filter(s => !presentSections.includes(s)).join(', ')} (-${30 - sectionScore})`)

  // Steps quality (20 pts)
  const stepsMatch = content.match(/^## Steps\n([\s\S]*?)(?=\n#)/m)
  if (stepsMatch) {
    const numberedSteps = (stepsMatch[1].match(/^\d+\./gm) || []).length
    if (numberedSteps >= 4) {
      score += 20
      passes.push(`Steps: ${numberedSteps} numbered steps (+20)`)
    } else if (numberedSteps >= 2) {
      score += 10
      deductions.push(`Only ${numberedSteps} steps — aim for ≥ 4 (-10)`)
    } else {
      deductions.push(`## Steps has no numbered steps (-20)`)
    }
  } else {
    deductions.push('## Steps section missing (-20)')
  }

  // Examples in output (20 pts)
  const hasExample = /"status".*"completed"/s.test(content) || /Example \(completed\)/i.test(content)
  if (hasExample) {
    score += 20
    passes.push('Output has completed example (+20)')
  } else {
    deductions.push('No completed output example (-20)')
  }

  // Description trigger quality (20 pts)
  const desc = fm && fm['description'] ? fm['description'] : ''
  const triggerCount = (desc.match(/use when|use for|whenever|for when|when user|when asked/gi) || []).length
  const hasActionVerb = /\b(create|generate|build|analyze|review|audit|check|scan|fix|debug|refactor|document|deploy|test|validate|run|execute)\b/i.test(desc)
  const descLen = desc.length
  if (triggerCount >= 2 && hasActionVerb && descLen <= 200) {
    score += 20
    passes.push(`Description: ${triggerCount} triggers, action verb, ${descLen} chars (+20)`)
  } else if (triggerCount >= 1 || hasActionVerb) {
    score += 10
    deductions.push(`Description partially effective (triggers:${triggerCount}, verb:${hasActionVerb}, len:${descLen}) (-10)`)
  } else {
    deductions.push(`Description has no trigger contexts or action verbs (-20)`)
  }

  // Conciseness (10 pts)
  const lines = content.split('\n').length
  if (lines <= 200) {
    score += 10
    passes.push(`Conciseness: ${lines} lines ≤ 200 (+10)`)
  } else {
    deductions.push(`File too long: ${lines} lines (aim ≤ 200) (-10)`)
  }

  // Deductions
  if (fm && !fm['tier']) {
    score = Math.max(0, score - 10)
    deductions.push('Missing tier field (-10)')
  }
  const hasScopeDecl = /does not handle|does NOT handle/i.test(content)
  if (!hasScopeDecl) {
    score = Math.max(0, score - 10)
    deductions.push('No scope declaration (-10)')
  }
  const hasPlaceholder = /\[skill-name\]|\[placeholder\]|one sentence description/i.test(content)
  if (hasPlaceholder) {
    score = Math.max(0, score - 15)
    deductions.push('Placeholder text still present (-15)')
  }

  return { score: Math.min(100, Math.max(0, score)), passes, deductions }
}

// ── Security scoring (max 100) ───────────────────────────────────────────────

function scoreSecurity(content) {
  let score = 0
  const deductions = []
  const passes = []

  // Scope declaration (40 pts)
  const hasScopeDecl = /does not handle|does NOT handle/i.test(content)
  if (hasScopeDecl) {
    score += 40
    passes.push('Scope declaration present (+40)')
  } else {
    deductions.push('No scope declaration ("This skill handles X. Does NOT handle Y.") (-40)')
  }

  // Input validation + secrets rule (30 pts)
  const hasValidation = /validate.*input|redact.*secret|secret.*redact|pii.*redact|redact.*pii/i.test(content)
  if (hasValidation) {
    score += 30
    passes.push('Input validation / secrets redaction rule present (+30)')
  } else {
    deductions.push('No input validation or secrets/PII redaction rule (-30)')
  }

  // Error Recovery covers security-relevant scenarios (30 pts)
  const hasErrorRecovery = /## Error Recovery/m.test(content)
  const hasBlockedRow = /BLOCKED/i.test(content)
  const hasFailedRow = /FAILED/i.test(content)
  if (hasErrorRecovery && hasBlockedRow && hasFailedRow) {
    score += 30
    passes.push('Error Recovery has BLOCKED + FAILED rows (+30)')
  } else if (hasErrorRecovery) {
    score += 15
    deductions.push('Error Recovery missing BLOCKED or FAILED row (-15)')
  } else {
    deductions.push('No ## Error Recovery section (-30)')
  }

  return { score: Math.min(100, Math.max(0, score)), passes, deductions }
}

// ── Main ─────────────────────────────────────────────────────────────────────

function run(filePath) {
  const absPath = path.resolve(filePath)

  if (!fs.existsSync(absPath)) {
    console.error(`${RED}ERROR${RESET}: File not found: ${absPath}`)
    process.exit(1)
  }

  const content = fs.readFileSync(absPath, 'utf8')
  const fm = parseFrontmatter(content)

  console.log(`\nBenchmarking: ${absPath}\n`)

  const acc = scoreAccuracy(content, fm)
  const sec = scoreSecurity(content)
  const composite = Math.round(acc.score * 0.80 + sec.score * 0.20)

  // Accuracy breakdown
  console.log('── Accuracy (80% weight) ──')
  acc.passes.forEach(p => console.log(`  ${GREEN}✓${RESET} ${p}`))
  acc.deductions.forEach(d => console.log(`  ${RED}✗${RESET} ${d}`))
  console.log(`  Score: ${acc.score}/100\n`)

  // Security breakdown
  console.log('── Security (20% weight) ──')
  sec.passes.forEach(p => console.log(`  ${GREEN}✓${RESET} ${p}`))
  sec.deductions.forEach(d => console.log(`  ${RED}✗${RESET} ${d}`))
  console.log(`  Score: ${sec.score}/100\n`)

  // Composite
  const band = composite >= 90 ? `${GREEN}Excellent${RESET}` :
               composite >= 70 ? `${GREEN}Good${RESET}` :
               composite >= 50 ? `${YELLOW}Fair${RESET}` :
                                 `${RED}Poor${RESET}`

  console.log(`── Composite Score ──`)
  console.log(`  ${acc.score} × 0.80 + ${sec.score} × 0.20 = ${CYAN}${composite}/100${RESET} — ${band}`)

  if (composite >= 70) {
    console.log(`\n${GREEN}PASSED${RESET} — composite ≥ 70, skill is ready to ship\n`)
    process.exit(0)
  } else {
    console.log(`\n${RED}FAILED${RESET} — composite < 70, fix issues above before shipping\n`)
    process.exit(1)
  }
}

const arg = process.argv[2]
if (!arg) {
  console.error('Usage: node benchmark-skill.js <path/to/SKILL.md>')
  process.exit(1)
}
run(arg)
