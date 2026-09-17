/**
 * gk update — reinstalls gemini-kit globally from the npm registry.
 */

const { spawnSync } = require('child_process')
const path = require('path')
const fse = require('fs-extra')
const pc = require('picocolors')
const { intro, outro, select, log, cancel, isCancel } = require('@clack/prompts')
// Moved to top-level to surface module-load errors early
const init = require('./init')
const { walkFiles } = init

/**
 * Compares scaffold source vs existing .gemini/ dir.
 * Returns arrays of relative paths: files that changed content, and files that are new.
 * Skips memory/, runtime/, .env (user-owned paths).
 */
async function getChangedFiles(srcDir, destDir) {
  const filterFn = (p) => {
    const rel = path.relative(srcDir, p)
    return !rel.startsWith('memory') && !rel.startsWith('runtime') && !p.endsWith('.env')
  }
  const srcFiles = await walkFiles(srcDir, filterFn)
  const changed = [], newFiles = []
  for (const srcFile of srcFiles) {
    const rel = path.relative(srcDir, srcFile)
    const destFile = path.join(destDir, rel)
    if (!(await fse.pathExists(destFile))) {
      newFiles.push(rel)
    } else {
      const [a, b] = await Promise.all([fse.readFile(srcFile), fse.readFile(destFile)])
      if (!a.equals(b)) changed.push(rel)
    }
  }
  return { changed, newFiles }
}

// Safe package name pattern — prevents command injection if package.json is tampered
const PKG_NAME_PATTERN = /^[@a-z0-9/_.-]+$/i

module.exports = async function update() {
  const pkgRoot = path.join(__dirname, '../..')
  const pkg = require(path.join(pkgRoot, 'package.json'))

  const pkgName = pkg.name

  // Validate package name before passing to shell
  if (!pkgName || !PKG_NAME_PATTERN.test(pkgName)) {
    log.error(`Invalid package name in package.json: "${pkgName}"`)
    process.exit(1)
  }

  intro('[>] GeminiKit CLI - Update')

  try {
    log.step(`Running: npm install -g ${pkgName}`)
    // On Windows, .cmd files require shell:true to execute via spawnSync
    const isWin = process.platform === 'win32'
    const result = spawnSync('npm', ['install', '-g', pkgName], { stdio: 'inherit', shell: isWin })
    if (result.error) throw new Error(`Failed to spawn npm: ${result.error.message}`)
    if (result.status !== 0) throw new Error(`npm exited with code ${result.status ?? `signal ${result.signal}`}`)
    log.success('gemini-kit updated successfully!')

    // Check if current directory is a Gemini Kit project
    const targetDir = process.cwd()
    const geminiTarget = path.join(targetDir, '.gemini')

    const geminiSource = path.join(pkgRoot, 'scaffold')
    const geminiMdSource = path.join(pkgRoot, 'GEMINI.md')

    if (await fse.pathExists(geminiTarget)) {
      log.info('Detected an existing .gemini/ folder. Scanning for changes...')

      const { changed, newFiles } = await getChangedFiles(geminiSource, geminiTarget)

      if (changed.length === 0 && newFiles.length === 0) {
        log.success('Project files are already up to date.')
      } else {
        // Show changed files (cap display at 15 to avoid wall-of-text)
        if (changed.length > 0) {
          log.warn(`${changed.length} file(s) changed:`)
          changed.slice(0, 15).forEach(f => console.log(`  ${pc.yellow('~')} ${f}`))
          if (changed.length > 15) console.log(`  ${pc.dim(`... and ${changed.length - 15} more`)}`)
        }
        if (newFiles.length > 0) {
          log.info(`${newFiles.length} new file(s):`)
          newFiles.slice(0, 10).forEach(f => console.log(`  ${pc.green('+')} ${f}`))
          if (newFiles.length > 10) console.log(`  ${pc.dim(`... and ${newFiles.length - 10} more`)}`)
        }

        const choice = await select({
          message: 'How to handle these changes?',
          options: [
            { value: 'all',    label: 'Overwrite all',       hint: 'apply all updates including changed files' },
            { value: 'new',    label: 'Add new files only',  hint: 'skip changed files, preserve your edits' },
            { value: 'cancel', label: 'Cancel',              hint: 'keep current files unchanged' },
          ],
        })

        if (isCancel(choice) || choice === 'cancel') {
          cancel('Project update cancelled.')
          return
        }

        if (choice === 'all') {
          // Full backup → restore user data → overwrite all scaffold files
          const { randomBytes } = require('crypto')
          const tempDir = path.join(targetDir, '.gemini_backup_' + randomBytes(8).toString('hex'))
          await fse.move(geminiTarget, tempDir)
          let restored = false
          try {
            await fse.ensureDir(geminiTarget)
            const memoryDir = path.join(tempDir, 'memory')
            const runtimeDir = path.join(tempDir, 'runtime')
            const envFile = path.join(tempDir, '.env')
            if (await fse.pathExists(memoryDir)) await fse.move(memoryDir, path.join(geminiTarget, 'memory'))
            if (await fse.pathExists(runtimeDir)) await fse.move(runtimeDir, path.join(geminiTarget, 'runtime'))
            if (await fse.pathExists(envFile)) await fse.copy(envFile, path.join(geminiTarget, '.env'), { overwrite: true })
            await init.performInit(geminiSource, geminiTarget, targetDir, geminiMdSource, true)
            log.success('Project updated. Existing libraries and data preserved.')
          } catch (initErr) {
            log.error('Update failed: ' + initErr.message)
            log.warn('Restoring from backup...')
            // Mark restored BEFORE moving so finally never deletes the backup
            restored = true
            if (await fse.pathExists(geminiTarget)) await fse.remove(geminiTarget)
            await fse.move(tempDir, geminiTarget)
            log.success('Project restored to its previous state.')
            return
          } finally {
            // Always clean up the backup dir unless it was moved back during restore
            if (!restored && await fse.pathExists(tempDir)) {
              try { await fse.remove(tempDir) } catch { /* ignore cleanup errors */ }
            }
          }
        } else {
          // 'new' — only copy files that don't exist yet, leave changed files untouched
          if (newFiles.length === 0) {
            log.info('No new files to add. Existing files unchanged.')
          } else {
            for (const rel of newFiles) {
              const dest = path.join(geminiTarget, rel)
              await fse.ensureDir(path.dirname(dest))
              await fse.copy(path.join(geminiSource, rel), dest)
            }
            // Copy GEMINI.md only if it doesn't exist yet
            const geminiMdDest = path.join(targetDir, 'GEMINI.md')
            if (!(await fse.pathExists(geminiMdDest)) && await fse.pathExists(geminiMdSource)) {
              await fse.copy(geminiMdSource, geminiMdDest)
            }
            log.success(`${newFiles.length} new file(s) added. Existing files unchanged.`)
          }
        }
      }
    }

    outro('Update complete')
  } catch (err) {
    log.error('Update failed: ' + err.message)
    log.info(`  Try manually: npm install -g ${pkgName}`)
    process.exit(1)
  }
}
