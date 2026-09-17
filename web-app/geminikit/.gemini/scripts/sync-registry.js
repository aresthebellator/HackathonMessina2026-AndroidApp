'use strict';
// sync-registry.js — reads SKILL.md files and updates REGISTRY.md
// Pure Node.js, no external deps. Run from project root: node .gemini/scripts/sync-registry.js

const fs = require('fs');
const path = require('path');

const GEMINI = path.join(process.cwd(), '.gemini');
const SKILLS_DIR = path.join(GEMINI, 'skills');
const REGISTRY_FILE = path.join(GEMINI, 'REGISTRY.md');

function parseFrontmatter(content) {
  const m = content.replace(/\r\n/g, '\n').match(/^---\n([\s\S]*?)\n---/);
  if (!m) return {};
  return Object.fromEntries(
    m[1].split('\n')
      .map(l => { const i = l.indexOf(':'); return i < 0 ? null : [l.slice(0, i).trim(), l.slice(i + 1).trim().replace(/^"(.*)"$/, '$1')]; })
      .filter(Boolean)
  );
}

function extractInterface(content) {
  const invokedVia = (content.match(/\*\*Invoked via:\*\*\s*(.+)/) || [])[1]?.trim().replace(/\r/, '') || '';
  const flags = (content.match(/\*\*Flags:\*\*\s*(.+)/) || [])[1]?.trim().replace(/\r/, '') || 'none';
  return { invokedVia, flags };
}

function getModes(skillDir) {
  const modesDir = path.join(skillDir, 'modes');
  if (!fs.existsSync(modesDir)) return '—';
  const modes = fs.readdirSync(modesDir).filter(f => f.endsWith('.md')).map(f => f.replace('.md', '')).sort();
  return modes.length ? modes.map(m => `\`${m}\``).join(', ') : '—';
}

function replaceSection(content, start, end, newContent) {
  const re = new RegExp(`${start.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}[\\s\\S]*?${end.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}`);
  return content.replace(re, () => `${start}\n${newContent}\n${end}`);
}

function updateFile(filePath, sections) {
  if (!fs.existsSync(filePath)) return;
  let content = fs.readFileSync(filePath, 'utf8');
  for (const [start, end, table] of sections) content = replaceSection(content, start, end, table);
  fs.writeFileSync(filePath, content, 'utf8');
}

// Read and parse all skills
const skills = fs.readdirSync(SKILLS_DIR).sort().flatMap(dir => {
  const skillMd = path.join(SKILLS_DIR, dir, 'SKILL.md');
  if (!fs.existsSync(skillMd)) return [];
  const content = fs.readFileSync(skillMd, 'utf8');
  const fm = parseFrontmatter(content);
  if (!fm.name) return [];
  const { invokedVia, flags } = extractInterface(content);
  const modes = getModes(path.join(SKILLS_DIR, dir));
  return [{ dir, agent: fm.agent || '(self)', description: fm.description || '', invokedVia, flags, modes }];
});

// Helper: escape pipes for markdown table cells
const esc = s => s.replace(/\|/g, '\\|');

// REGISTRY.md command table
let cmdReg = '| Command | Agent | Skills | Description |\n|---------|-------|--------|-------------|\n';
for (const s of skills) {
  const cmd = s.flags !== 'none' ? `\`${s.invokedVia} [${esc(s.flags)}] <args>\`` : `\`${s.invokedVia}\``;
  cmdReg += `| ${cmd} | ${s.agent} | ${s.dir} | ${s.description} |\n`;
}

// REGISTRY.md skill registry
let skillReg = '| Skill | File | Modes | Use for |\n|-------|------|-------|---------|\n';
for (const s of skills) skillReg += `| ${s.dir} | \`.gemini/skills/${s.dir}/SKILL.md\` | ${s.modes} | ${s.description} |\n`;

updateFile(REGISTRY_FILE, [
  ['<!-- GK_COMMAND_TABLE_START -->', '<!-- GK_COMMAND_TABLE_END -->', cmdReg],
  ['<!-- GK_SKILL_REGISTRY_START -->', '<!-- GK_SKILL_REGISTRY_END -->', skillReg],
]);

console.log(`✓ Registry synced: ${skills.length} skills`);
