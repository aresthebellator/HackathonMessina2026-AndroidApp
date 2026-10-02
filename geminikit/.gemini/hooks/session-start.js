// SessionStart hook — loads pinned + recent long-term memory into short-term context
const path = require('path');
const fs = require('fs');
const { readMemory, writeMemory } = require('./lib/memory-manager');
const { readStdin } = require('./lib/read-stdin');
const { logError } = require('./lib/logger');
const { clearStaleHandoffs } = require('./lib/agent-handoff-manager');

function isDir(base, name) {
  try { return fs.statSync(path.join(base, name)).isDirectory(); } catch { return false; }
}

function extractProjectType(content) {
  const m = content.match(/^-\s+Project type:\s+(mobile|web|backend|tool)\s*$/m);
  return m ? m[1] : null;
}

function detectProjectSignals(cwd) {
  const signals = { mobile: [], web: [], backend: [], tool: [] };
  try {
    let pkg = {};
    try { pkg = JSON.parse(fs.readFileSync(path.join(cwd, 'package.json'), 'utf8')); } catch {}
    const deps = { ...pkg.dependencies, ...pkg.devDependencies };

    // Mobile signals
    if (fs.existsSync(path.join(cwd, 'pubspec.yaml'))) signals.mobile.push('pubspec.yaml');
    if (fs.existsSync(path.join(cwd, 'metro.config.js'))) signals.mobile.push('metro.config.js');
    if (fs.existsSync(path.join(cwd, 'react-native.config.js'))) signals.mobile.push('react-native.config.js');
    if (isDir(cwd, 'android') && isDir(cwd, 'ios')) signals.mobile.push('android/+ios/');

    // Shared path checks (reused across web + backend)
    const hasSrcRoutes = isDir(cwd, 'src/routes');

    // Web signals — gated behind Electron/Tauri exclusion
    const isElectron = 'electron' in deps;
    const isTauri = '@tauri-apps/cli' in deps;
    if (!isElectron && !isTauri) {
      const webConfigs = ['next.config.js', 'next.config.ts', 'next.config.mjs',
                          'nuxt.config.js', 'nuxt.config.ts',
                          'astro.config.js', 'astro.config.ts', 'astro.config.mjs'];
      for (const cfg of webConfigs) {
        if (fs.existsSync(path.join(cwd, cfg))) { signals.web.push(cfg); break; }
      }
      const hasViteConfig = ['vite.config.js','vite.config.ts','vite.config.mjs']
        .some(f => fs.existsSync(path.join(cwd, f)));
      const hasReactOrVue = 'react' in deps || 'vue' in deps;
      if (hasViteConfig && hasReactOrVue && !hasSrcRoutes) signals.web.push('vite.config.*+react/vue');
    }

    // Backend signals
    if (hasSrcRoutes) signals.backend.push('src/routes/');
    if (fs.existsSync(path.join(cwd, 'src', 'controllers'))) signals.backend.push('src/controllers/');
    const backendDeps = ['express', 'fastify', 'hono'];
    for (const d of backendDeps) { if (d in deps) signals.backend.push(d); }
    if (fs.existsSync(path.join(cwd, 'main.py'))) {
      let fastapiFound = false;
      try { fastapiFound = /fastapi/i.test(fs.readFileSync(path.join(cwd, 'requirements.txt'), 'utf8')); } catch {}
      if (!fastapiFound) {
        try { fastapiFound = /fastapi/i.test(fs.readFileSync(path.join(cwd, 'pyproject.toml'), 'utf8')); } catch {}
      }
      if (fastapiFound) signals.backend.push('main.py+fastapi');
    }
    const hasCmd = isDir(cwd, 'cmd');
    if (fs.existsSync(path.join(cwd, 'main.go')) && !hasCmd) signals.backend.push('main.go');

    // Tool signals
    if (isDir(cwd, 'bin')) signals.tool.push('bin/');
    if (hasCmd) signals.tool.push('cmd/');
    const toolDeps = ['commander', 'yargs'];
    for (const d of toolDeps) { if (d in deps) signals.tool.push(d); }
  } catch {}
  return signals;
}

function buildDetectionBlock(signals) {
  const counts = Object.entries(signals).map(([type, hits]) => ({ type, count: hits.length, hits }));
  const matched = counts.filter(c => c.count > 0).sort((a, b) => b.count - a.count);

  if (matched.length === 0) {
    return [
      '## ⚠️ PROJECT TYPE UNKNOWN',
      'No codebase signals detected.',
      'ACTION REQUIRED — at the start of this session you MUST ask:',
      '  "I couldn\'t detect the project type. Is this mobile, web, backend, or tool?"',
      'Write answer to .gemini/memory/pinned.md under ## Project Context: - Project type: <type>',
      'Apply matching section from 09_product-rules.md after confirmation.',
    ].join('\n');
  }

  if (matched.length === 1 || (matched.length >= 2 && matched[0].count > matched[1].count)) {
    const winner = matched[0];
    return [
      '## ⚠️ PROJECT TYPE CONFIRMATION NEEDED',
      `Detected: **${winner.type}** (signals: ${winner.hits.join(', ')})`,
      'ACTION REQUIRED — at the start of this session you MUST ask the user:',
      `  "I detected this as a **${winner.type}** project (${winner.hits.join(', ')}). Is that correct?"`,
      `On confirm → write to .gemini/memory/pinned.md under ## Project Context: - Project type: ${winner.type}`,
      'On reject  → ask "What type? (mobile/web/backend/tool)" → write corrected type.',
      `Apply [${winner.type}] section from 09_product-rules.md after confirmation.`,
    ].join('\n');
  }

  const candidates = matched.map(c => `${c.type} (${c.hits.join(', ')})`).join(' | ');
  return [
    '## ⚠️ PROJECT TYPE AMBIGUOUS',
    `Multiple types detected: ${candidates}`,
    'ACTION REQUIRED — at the start of this session you MUST ask the user:',
    '  "I detected multiple project types. Which is the primary type? (mobile/web/backend/tool)"',
    'Write answer to .gemini/memory/pinned.md under ## Project Context: - Project type: <type>',
    'Apply matching section from 09_product-rules.md after confirmation.',
  ].join('\n');
}

function main() {
  try {
    const input = readStdin(); // { session_id: '...', hook_event_name: 'SessionStart', ... }
    const cwd = input.cwd || process.cwd();
    // Only clear stale handoffs (older than 1h) — clearing all would nuke sibling agents in multi-agent mode
    clearStaleHandoffs(cwd);

    // Load pinned context (immutable rules/instructions)
    const pinned = readMemory('pinned.md');

    // Auto-create pinned.md if missing
    if (!pinned) {
      writeMemory('pinned.md',
        '# Pinned Knowledge (Immutable Context)\n\n' +
        '## Project Context\n\n' +
        '## Critical Business Logic\n\n' +
        '## Global Architectural Rules\n'
      );
    }

    // Detect or confirm project type
    const projectType = pinned ? extractProjectType(pinned) : null;
    let typeContext = '';
    if (projectType) {
      typeContext = `## Active Project Rules\nType: **${projectType}** — apply [${projectType}] section from 09_product-rules.md.\n`;
    } else {
      const signals = detectProjectSignals(cwd);
      typeContext = buildDetectionBlock(signals);
    }

    // Load last 3 entries from long-term memory
    const longTerm = readMemory('long-term.md');
    const entries = longTerm.split(/(?=^## )/m).filter(s => s.trim());
    const recent = entries.slice(-3).join('\n');

    // Write initialized short-term context for this session
    // Support both field names: session_id (official API) and sessionId (legacy)
    const sessionContext = [
      `# Session Context`,
      `Loaded: ${new Date().toISOString()}`,
      `Session: ${input.session_id || input.sessionId || 'unknown'}`,
      '',
      pinned ? `## Pinned Context\n${pinned}` : '',
      typeContext,
      recent ? `## Recent History\n${recent}` : '',
    ].filter(Boolean).join('\n') + '\n';

    writeMemory('short-term.md', sessionContext);

    process.stdout.write(JSON.stringify({
      hookSpecificOutput: { additionalContext: sessionContext }
    }));
  } catch (err) {
    logError('session-start', err);
    process.stdout.write(JSON.stringify({ hookSpecificOutput: { additionalContext: '' } }));
  }
  process.exit(0);
}

main();
