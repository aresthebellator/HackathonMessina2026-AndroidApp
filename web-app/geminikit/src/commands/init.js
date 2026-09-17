/**
 * gk init — scaffolds .gemini/ and GEMINI.md into the current project
 * from the installed package's scaffold/ directory.
 */

const fse = require('fs-extra')
const path = require('path')
const pc = require('picocolors')
const { spawnSync } = require('child_process')
const { intro, outro, confirm, log, cancel, isCancel, text } = require('@clack/prompts')
const { createSpinner } = require('../utils/ui')

/**
 * Recursively collects all file paths from dir, skipping entries where filterFn returns false.
 * The filter receives the full path and applies to both files and directories.
 */
async function walkFiles(dir, filterFn) {
  const files = []
  const walk = async (cur) => {
    for (const e of await fse.readdir(cur, { withFileTypes: true })) {
      const full = path.join(cur, e.name)
      if (filterFn && !filterFn(full)) continue
      e.isDirectory() ? await walk(full) : files.push(full)
    }
  }
  await walk(dir)
  return files
}

/**
 * Copies files one by one with verbose output (→ filename per line).
 * Uses plain console.log for per-file lines — clack decorators are too noisy for 20+ lines.
 */
async function copyFilesVerbose(files, srcBase, destBase, overwrite) {
  for (const src of files) {
    const rel = path.relative(srcBase, src)
    const dest = path.join(destBase, rel)
    await fse.ensureDir(path.dirname(dest))
    await fse.copy(src, dest, { overwrite })
  }
}

/**
 * Runs 'npm install' in the specified directory.
 */
async function installNodePackages(targetDir) {
  if (!(await fse.pathExists(path.join(targetDir, 'package.json')))) return

  const s = createSpinner()
  s.start('Installing Node packages...')
  const isWin = process.platform === 'win32'
  const result = spawnSync('npm', ['install', '--quiet'], {
    cwd: targetDir,
    encoding: 'utf8',
    timeout: 300000,
    shell: isWin,
  })
  if (result.error) throw new Error(`npm spawn error: ${result.error.message}`)
  if (result.status !== 0) throw new Error(result.stderr || 'npm install failed')
  s.stop('Node packages installed')
}

/**
 * Interactive .env setup: copies .env.example and prompts for values.
 */
async function setupEnvInteractively(targetDir) {
  const envPath = path.join(targetDir, '.gemini', '.env')
  const examplePath = path.join(targetDir, '.gemini', '.env.example')

  if (!(await fse.pathExists(examplePath))) return

  if (!(await fse.pathExists(envPath))) {
    await fse.copy(examplePath, envPath)
    log.info('.env file created from .env.example')
  }

  const content = await fse.readFile(envPath, 'utf8')
  const entries = content.split('\n')
    .map(l => l.trim())
    .filter(l => l && !l.startsWith('#') && l.includes('='))
    .map(l => {
      const idx = l.indexOf('=')
      return { key: l.slice(0, idx).trim(), currentValue: l.slice(idx + 1).trim() }
    })

  if (entries.length === 0) return

  log.info('Interactive environment setup. Press Enter to keep current value.')

  let newContent = content
  for (const { key, currentValue } of entries) {
    const value = await text({
      message: `Enter value for ${pc.cyan(key)}:`,
      initialValue: currentValue,
      placeholder: 'leave empty to clear'
    })

    if (isCancel(value)) {
      if (newContent !== content) log.warn('.env partially updated — cancelled mid-way')
      break
    }
    let regex
    try {
      const escapedKey = key.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
      regex = new RegExp(`^${escapedKey}=.*`, 'gm')
    } catch (e) {
      log.warn(`Skipping key '${key}': invalid regex — ${e.message}`)
      continue
    }
    const sanitizedValue = (value ?? currentValue).replace(/[\r\n]/g, '')
    newContent = newContent.replace(regex, `${key}=${sanitizedValue}`)
  }

  await fse.writeFile(envPath, newContent)
  log.success('.env configuration updated')
}

/**
 * The main initialization logic, exported for reuse by 'update' command.
 * Runs 3 steps: copy files → install Node packages → setup env.
 */
async function performInit(geminiSource, geminiTarget, targetDir, geminiMdSource, overwrite = false) {
  const startTime = Date.now()

  try {
    // Step 1: Copy framework files
    const filterFn = (src) => {
      const rel = path.relative(geminiSource, src)
      if (rel.startsWith('memory') || rel.startsWith('runtime') || path.basename(src) === '.env') return false
      return true
    }
    log.info('[1/3] Copying framework files...')
    const files = await walkFiles(geminiSource, filterFn)
    await copyFilesVerbose(files, geminiSource, geminiTarget, overwrite)
    if (await fse.pathExists(geminiMdSource)) {
      await fse.copy(geminiMdSource, path.join(targetDir, 'GEMINI.md'), { overwrite })
    }
    log.success(`[1/3] Copied ${files.length} files`)

    // Step 2: Install Node packages for hooks
    log.info('[2/3] Installing Node packages for hooks...')
    await installNodePackages(path.join(geminiTarget, 'hooks'))
    log.success('[2/3] Node packages ready')

    // Step 3: Setup environment variables
    log.info('[3/3] Setting up environment variables...')
    await setupEnvInteractively(targetDir)
    log.success('[3/3] Environment ready')

    const elapsed = ((Date.now() - startTime) / 1000).toFixed(1)
    log.success(`Gemini Kit initialized successfully! (${elapsed}s)`)
    log.info(`Config file: ${pc.cyan('GEMINI.md')}`)
  } catch (err) {
    log.error('Error during initialization: ' + err.message)
    throw err
  }
}

module.exports = async function init() {
  const pkgRoot = path.join(__dirname, '../..')
  const geminiSource = path.join(pkgRoot, 'scaffold')
  const geminiMdSource = path.join(pkgRoot, 'GEMINI.md')
  const targetDir = process.cwd()
  const geminiTarget = path.join(targetDir, '.gemini')

  intro('[>] GeminiKit CLI - Init')

  // Validation: Ensure source directory exists (now using 'scaffold/' for stability)
  if (!(await fse.pathExists(geminiSource))) {
    log.error(`Source error: Could not find framework files in ${geminiSource}`)
    log.warn('Try reinstalling with: npm install -g geminicli-kit --force')
    process.exit(1)
  }

  if (await fse.pathExists(geminiTarget)) {
    log.warn('.gemini/ already exists in this project.')
    log.warn('WARNING: Initializing will OVERWRITE your current configuration and custom settings!')
    log.info('Please back up your changes (e.g. settings.json, custom agents) before proceeding.')

    const shouldContinue = await confirm({
      message: 'Are you sure you want to continue?',
    })

    if (isCancel(shouldContinue) || !shouldContinue) {
      cancel('Initialization cancelled. Your files were not changed.')
      process.exit(0)
    }

    // Guard against symlink attacks before removal
    const lstat = fse.lstatSync(geminiTarget)
    if (lstat.isSymbolicLink()) {
      log.error('.gemini/ is a symlink — cannot safely remove. Remove it manually first.')
      process.exit(1)
    }
    const realCwd = fse.realpathSync(process.cwd())
    // Use realpathSync on the target too so symlinked CWDs don't cause false positives
    const realTarget = fse.realpathSync(geminiTarget)
    if (!realTarget.startsWith(realCwd + path.sep)) {
      log.error('.gemini/ resolves outside the project directory — cannot safely remove.')
      process.exit(1)
    }

    log.info('Removing existing .gemini/ to reinitialize...')
    await fse.remove(geminiTarget)
  }

  try {
    await performInit(geminiSource, geminiTarget, targetDir, geminiMdSource)
    outro('Initialization complete')
  } catch (err) {
    log.error('Initialization failed: ' + err.message)
    process.exit(1)
  }
}

module.exports.performInit = performInit
module.exports.walkFiles = walkFiles
