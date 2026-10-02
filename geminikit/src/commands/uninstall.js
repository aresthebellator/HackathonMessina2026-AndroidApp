/**
 * gk uninstall — removes .gemini/ and GEMINI.md from the current project.
 */

const fse = require('fs-extra')
const path = require('path')
const { intro, outro, confirm, log, cancel, isCancel } = require('@clack/prompts')

module.exports = async function uninstall() {
  const targetDir = process.cwd()
  const geminiDir = path.join(targetDir, '.gemini')
  const geminiMd = path.join(targetDir, 'GEMINI.md')

  intro('[>] GeminiKit CLI - Uninstall')

  const geminiExists = await fse.pathExists(geminiDir)
  const geminiMdExists = await fse.pathExists(geminiMd)

  if (!geminiExists && !geminiMdExists) {
    log.warn('No Gemini Kit project found in this directory.')
    outro('Nothing to remove.')
    return
  }

  // Require explicit confirmation before destroying project files
  const shouldRemove = await confirm({
    message: 'Remove .gemini/ and GEMINI.md from this project? This cannot be undone.',
  })

  if (isCancel(shouldRemove) || !shouldRemove) {
    cancel('Uninstall cancelled.')
    return
  }

  try {
    if (geminiExists) {
      await fse.remove(geminiDir)
      log.success('Removed .gemini/ directory (including local Python runtime).')
    }
    if (geminiMdExists) {
      await fse.remove(geminiMd)
      log.success('Removed GEMINI.md file.')
    }

    log.info('To completely remove GeminiKit from your system, run:')
    log.info('  npm uninstall -g geminicli-kit')
    outro('Project cleaned successfully.')
  } catch (err) {
    log.error('Uninstall failed: ' + err.message)
    process.exit(1)
  }
}
