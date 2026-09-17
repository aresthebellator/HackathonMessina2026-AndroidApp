/**
 * gk list — prints a table of available agents and skills
 * found in the .gemini/ directory of the current project.
 * Skills are grouped by tier: core (default), optional, internal (hidden).
 */

const fs = require('fs')
const path = require('path')
const pc = require('picocolors')
const { intro, outro, log } = require('@clack/prompts')

/**
 * Reads the SKILL.md frontmatter and returns the tier value.
 * Defaults to 'core' when not specified or file is unreadable.
 */
function parseSkillTier(skillDir) {
  try {
    const content = fs.readFileSync(path.join(skillDir, 'SKILL.md'), 'utf8')
    const match = content.match(/^tier:\s*(\w+)/m)
    return match ? match[1] : 'core'
  } catch {
    return 'core'
  }
}

module.exports = function list() {
  const geminiDir = path.join(process.cwd(), '.gemini')

  intro('[>] GeminiKit CLI - List')

  if (!fs.existsSync(geminiDir)) {
    log.warn('No .gemini/ found in this directory. Run gk init first.')
    outro('Done')
    return
  }

  let found = false

  // List agents (each is a .md file in .gemini/agents/)
  const agentsDir = path.join(geminiDir, 'agents')
  if (fs.existsSync(agentsDir)) {
    const agents = fs.readdirSync(agentsDir)
      .filter(f => f.endsWith('.md'))
      .map(f => f.replace('.md', ''))
      .sort()

    if (agents.length > 0) {
      console.log(pc.bold('\nAgents:'))
      agents.forEach(a => console.log(`  ${pc.cyan('•')} ${a}`))
      found = true
    }
  }

  // List skills grouped by tier (internal skills are hidden)
  const skillsDir = path.join(geminiDir, 'skills')
  if (fs.existsSync(skillsDir)) {
    const allSkills = fs.readdirSync(skillsDir)
      .filter(f => {
        try { return fs.lstatSync(path.join(skillsDir, f)).isDirectory() }
        catch { return false }
      })
      .sort()

    const core = []
    const optional = []

    for (const s of allSkills) {
      const tier = parseSkillTier(path.join(skillsDir, s))
      if (tier === 'internal') continue
      if (tier === 'optional') optional.push(s)
      else core.push(s)
    }

    if (core.length > 0) {
      console.log(pc.bold('\nSkills:'))
      core.forEach(s => console.log(`  ${pc.cyan('•')} ${s}`))
      found = true
    }

    if (optional.length > 0) {
      console.log(pc.bold('\nOptional Skills:'))
      optional.forEach(s => console.log(`  ${pc.dim('•')} ${s}`))
      found = true
    }
  }

  if (!found) {
    log.warn('No agents or skills found in .gemini/')
    outro('Done')
    return
  }

  outro('Done')
}
