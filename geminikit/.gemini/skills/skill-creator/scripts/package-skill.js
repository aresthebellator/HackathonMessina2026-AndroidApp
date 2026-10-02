#!/usr/bin/env node
/**
 * package-skill.js — Bundles a Gemini Kit skill directory into a distributable .zip.
 *
 * Validates the skill passes all checks before packaging.
 *
 * Usage:
 *   node package-skill.js <path/to/skill-dir> [output-dir]
 *
 * Example:
 *   node package-skill.js .gemini/skills/my-skill
 *   node package-skill.js .gemini/skills/my-skill ./dist
 *
 * Exit codes:
 *   0 — packaged successfully
 *   1 — validation failed or packaging error
 */

const fs = require('fs')
const path = require('path')
const { execSync } = require('child_process')

const RED   = '\x1b[31m'
const GREEN = '\x1b[32m'
const CYAN  = '\x1b[36m'
const RESET = '\x1b[0m'

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

function run(skillDir, outputDir) {
  const absSkillDir = path.resolve(skillDir)

  if (!fs.existsSync(absSkillDir)) {
    console.error(`${RED}ERROR${RESET}: Directory not found: ${absSkillDir}`)
    process.exit(1)
  }

  const skillMdPath = path.join(absSkillDir, 'SKILL.md')
  if (!fs.existsSync(skillMdPath)) {
    console.error(`${RED}ERROR${RESET}: No SKILL.md found in: ${absSkillDir}`)
    process.exit(1)
  }

  const content = fs.readFileSync(skillMdPath, 'utf8')
  const fm = parseFrontmatter(content)

  if (!fm || !fm['name'] || !fm['version']) {
    console.error(`${RED}ERROR${RESET}: SKILL.md missing name or version in frontmatter`)
    process.exit(1)
  }

  const skillName = fm['name']
  const skillVersion = fm['version']

  console.log(`\nPackaging: ${skillName} v${skillVersion}\n`)

  // Step 1: Run validate-skill.js
  const validateScript = path.join(__dirname, 'validate-skill.js')
  if (fs.existsSync(validateScript)) {
    console.log(`${CYAN}Running validate-skill.js...${RESET}`)
    try {
      execSync(`node "${validateScript}" "${skillMdPath}"`, { stdio: 'inherit' })
    } catch {
      console.error(`\n${RED}FAILED${RESET} — validation errors must be fixed before packaging\n`)
      process.exit(1)
    }
  }

  // Step 2: Run benchmark-skill.js
  const benchmarkScript = path.join(__dirname, 'benchmark-skill.js')
  if (fs.existsSync(benchmarkScript)) {
    console.log(`${CYAN}Running benchmark-skill.js...${RESET}`)
    try {
      execSync(`node "${benchmarkScript}" "${skillMdPath}"`, { stdio: 'inherit' })
    } catch {
      console.error(`\n${RED}FAILED${RESET} — benchmark score < 70, fix quality issues before packaging\n`)
      process.exit(1)
    }
  }

  // Step 3: Create output directory
  const outDir = outputDir ? path.resolve(outputDir) : process.cwd()
  if (!fs.existsSync(outDir)) {
    fs.mkdirSync(outDir, { recursive: true })
  }

  // Step 4: Build zip filename
  const zipName = `${skillName}-v${skillVersion}.zip`
  const zipPath = path.join(outDir, zipName)

  // Step 5: Create zip using system zip command
  const skillDirName = path.basename(absSkillDir)
  const parentDir = path.dirname(absSkillDir)

  console.log(`\n${CYAN}Creating ${zipName}...${RESET}`)
  try {
    execSync(`cd "${parentDir}" && zip -r "${zipPath}" "${skillDirName}" --exclude "*/evals/*" --exclude "*/.DS_Store" --exclude "*/node_modules/*"`, {
      stdio: 'inherit'
    })
  } catch (err) {
    // Fallback: try without --exclude (older zip versions)
    try {
      execSync(`cd "${parentDir}" && zip -r "${zipPath}" "${skillDirName}"`, { stdio: 'inherit' })
    } catch {
      console.error(`${RED}ERROR${RESET}: Failed to create zip. Ensure 'zip' is installed.`)
      process.exit(1)
    }
  }

  // Step 6: Report result
  const stats = fs.statSync(zipPath)
  const kb = (stats.size / 1024).toFixed(1)

  console.log(`\n${GREEN}PACKAGED${RESET} — ${zipPath} (${kb} KB)`)
  console.log(`\nContents: ${skillName}/SKILL.md + references/ + scripts/\n`)
}

const [,, skillDir, outputDir] = process.argv
if (!skillDir) {
  console.error('Usage: node package-skill.js <skill-dir> [output-dir]')
  console.error('Example: node package-skill.js .gemini/skills/my-skill ./dist')
  process.exit(1)
}
run(skillDir, outputDir)
