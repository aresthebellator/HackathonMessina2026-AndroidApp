const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const PROJECT_DIR = process.cwd();

const REQUIRED_DIRS = [
  { id: 'dir_gemini', rel: '.gemini', label: '.gemini/ directory', fixable: true },
  { id: 'dir_logs', rel: path.join('.gemini', 'logs'), label: '.gemini/logs/ directory (token analytics)', fixable: true },
  { id: 'dir_memory', rel: path.join('.gemini', 'memory'), label: '.gemini/memory/ directory', fixable: true },
  { id: 'dir_hooks', rel: path.join('.gemini', 'hooks'), label: '.gemini/hooks/ directory', fixable: false },
];

const REQUIRED_FILES = [
  { id: 'file_gemini_md', rel: 'GEMINI.md', label: 'GEMINI.md', fixable: false },
  { id: 'file_settings', rel: path.join('.gemini', 'settings.json'), label: '.gemini/settings.json (hooks config)', fixable: false },
];

const MEMORY_FILES = [
  'short-term.md', 'long-term.md', 'execution.md', 'pinned.md',
];

function checkNodeVersion(issues) {
  const [major] = process.versions.node.split('.').map(Number);
  if (major < 18) {
    issues.push({ id: 'node_version', msg: `Node.js >=18 required (current: v${process.versions.node})`, fixable: false });
  } else {
    console.log(`✓ Node.js v${process.versions.node}`);
  }
}

function checkDirsAndFiles(issues) {
  for (const { id, rel, label, fixable } of REQUIRED_DIRS) {
    const abs = path.join(PROJECT_DIR, rel);
    if (!fs.existsSync(abs)) {
      issues.push({ id, msg: `Missing: ${label}`, fixable, abs });
    } else {
      console.log(`✓ ${label}`);
    }
  }

  for (const { id, rel, label, fixable } of REQUIRED_FILES) {
    const abs = path.join(PROJECT_DIR, rel);
    if (!fs.existsSync(abs)) {
      issues.push({ id, msg: `Missing: ${label}`, fixable, abs });
    } else {
      console.log(`✓ ${label}`);
    }
  }
}

function checkHooksConfig(issues) {
  const settingsPath = path.join(PROJECT_DIR, '.gemini', 'settings.json');
  if (!fs.existsSync(settingsPath)) return; // already caught above

  try {
    // settings.json may use JSONC (comments with //) — strip both inline and full-line comments
    const raw = fs.readFileSync(settingsPath, 'utf8');
    // Regex: skip matches inside quoted strings, strip // outside strings
    const stripped = raw
      .replace(/("(?:[^"\\]|\\.)*")|\/\/[^\n]*/g, (m, str) => str || '')
      .replace(/,(\s*[}\]])/g, '$1');
    const settings = JSON.parse(stripped);
    const hooks = settings.hooks || {};
    const EXPECTED = ['SessionStart', 'AfterModel', 'PreCompress', 'AfterTool', 'SessionEnd'];
    const missing = EXPECTED.filter(h => !hooks[h]);
    if (missing.length > 0) {
      issues.push({ id: 'hooks_missing', msg: `Missing hooks in settings.json: ${missing.join(', ')}`, fixable: false });
    } else {
      console.log(`✓ settings.json — ${EXPECTED.length} hooks configured`);
    }
  } catch (err) {
    issues.push({ id: 'settings_invalid', msg: `settings.json is not valid JSON: ${err.message}`, fixable: false });
  }
}

function checkMemoryFiles(issues) {
  const memDir = path.join(PROJECT_DIR, '.gemini', 'memory');
  if (!fs.existsSync(memDir)) return; // already caught above

  const missing = MEMORY_FILES.filter(f => !fs.existsSync(path.join(memDir, f)));
  if (missing.length > 0) {
    issues.push({
      id: 'memory_files',
      msg: `Missing memory files: ${missing.join(', ')}`,
      fixable: true,
      missing,
      memDir,
    });
  } else {
    console.log(`✓ Memory files (${MEMORY_FILES.join(', ')})`);
  }
}

function checkHooksNpmDeps(issues) {
  const hooksDir = path.join(PROJECT_DIR, '.gemini', 'hooks');
  if (!fs.existsSync(hooksDir)) return;

  const nodeModulesDir = path.join(hooksDir, 'node_modules');
  const packageJson = path.join(hooksDir, 'package.json');

  if (fs.existsSync(packageJson) && !fs.existsSync(nodeModulesDir)) {
    issues.push({
      id: 'hooks_npm',
      msg: '.gemini/hooks/node_modules missing — hooks cannot summarize',
      fixable: true,
      hooksDir,
    });
  } else if (fs.existsSync(nodeModulesDir)) {
    // Check for @google/generative-ai
    const genAiDir = path.join(nodeModulesDir, '@google', 'generative-ai');
    if (!fs.existsSync(genAiDir)) {
      issues.push({
        id: 'hooks_npm_genai',
        msg: '@google/generative-ai missing in hooks/node_modules — auto-summarization broken',
        fixable: true,
        hooksDir,
      });
    } else {
      console.log('✓ .gemini/hooks npm dependencies (@google/generative-ai)');
    }
  }
}

function applyFixes(issues) {
  console.log('\nApplying fixes...');
  for (const issue of issues) {
    if (!issue.fixable) continue;

    if (issue.id === 'dir_gemini' || issue.id === 'dir_logs' || issue.id === 'dir_memory') {
      fs.mkdirSync(issue.abs, { recursive: true });
      console.log(`  ✓ Created ${issue.abs}`);
    }

    if (issue.id === 'memory_files') {
      for (const f of issue.missing) {
        const filePath = path.join(issue.memDir, f);
        const header = f.replace('.md', '');
        fs.writeFileSync(filePath, `# Memory: ${header}\n\n`, 'utf8');
        console.log(`  ✓ Created ${f}`);
      }
    }

    if (issue.id === 'hooks_npm' || issue.id === 'hooks_npm_genai') {
      console.log(`  Running npm install in ${issue.hooksDir}...`);
      try {
        execSync('npm install', { cwd: issue.hooksDir, stdio: 'inherit', timeout: 120000 });
        console.log('  ✓ npm install completed');
      } catch (err) {
        console.log(`  ✗ npm install failed — run manually: cd .gemini/hooks && npm install (${err.message})`);
      }
    }
  }
  console.log('\nFixes applied. Run "gk doctor" again to verify.');
}

function run(options = {}) {
  console.log('Checking Gemini Kit setup...\n');
  const issues = [];

  checkNodeVersion(issues);
  checkDirsAndFiles(issues);
  checkHooksConfig(issues);
  checkMemoryFiles(issues);
  checkHooksNpmDeps(issues);

  console.log('\n── Results ──');
  if (issues.length === 0) {
    console.log('✓ Gemini Kit setup is healthy!');
    return;
  }

  const fixable = issues.filter(i => i.fixable);
  console.log(`${issues.length} issue(s) found (${fixable.length} auto-fixable):`);
  issues.forEach(i => {
    const fix = i.fixable ? ' (--fix)' : '';
    console.log(`  ${i.fixable ? '⚠' : '✗'} ${i.msg}${fix}`);
  });

  if (options.fix) {
    applyFixes(issues);
  } else if (fixable.length > 0) {
    console.log('\nRun "gk doctor --fix" to auto-fix addressable issues.');
  }

  // Exit with code 1 so CI scripts can detect unhealthy setups
  process.exit(1);
}

module.exports = { run };
