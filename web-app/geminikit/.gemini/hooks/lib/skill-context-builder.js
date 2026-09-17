// skill-context-builder.js
// Builds skill-specific context strings for BeforeAgent hook injection
// Each builder reads templates directly (no cache) to stay current with template changes

const fs = require('fs');
const path = require('path');

const MAX_TEMPLATE_LINES = 80;

// Read a template file, truncated to MAX_TEMPLATE_LINES
function readTemplate(cwd, filename) {
  try {
    const filePath = path.join(cwd, '.gemini', 'template', filename);
    const content = fs.readFileSync(filePath, 'utf8');
    return content.split('\n').slice(0, MAX_TEMPLATE_LINES).join('\n');
  } catch { return ''; }
}

// Format timestamp as YYMMDD-HHMM
function fmtTimestamp() {
  const now = new Date();
  const pad = n => String(n).padStart(2, '0');
  return `${String(now.getFullYear()).slice(2)}${pad(now.getMonth() + 1)}${pad(now.getDate())}-${pad(now.getHours())}${pad(now.getMinutes())}`;
}

function buildPlanContext(state, cwd) {
  const planTpl = readTemplate(cwd, 'plan-template.md');
  const phaseTpl = readTemplate(cwd, 'phase-template.md');
  const outputDir = state.outputDir || `plans/${fmtTimestamp()}-${state.slug || 'task'}/`;
  return [
    `## Skill Context: gk-plan`,
    `Output dir: ${outputDir}`,
    `Naming pattern: YYMMDD-HHMM-{slug}`,
    planTpl  ? `\n### plan-template.md (first ${MAX_TEMPLATE_LINES} lines):\n${planTpl}` : '',
    phaseTpl ? `\n### phase-template.md (first ${MAX_TEMPLATE_LINES} lines):\n${phaseTpl}` : '',
    `\n→ Create plan.md using the template above. Fill in title, description, phases.`,
    `→ Phase files will be auto-created by AfterTool hook after you write plan.md.`,
  ].filter(Boolean).join('\n');
}

function buildExecuteContext(state, cwd) {
  try {
    const plansDir = path.join(cwd, 'plans');
    if (!fs.existsSync(plansDir)) return `## Skill Context: gk-execute\nNo plans/ directory found.`;
    const dirs = fs.readdirSync(plansDir)
      .filter(d => fs.statSync(path.join(plansDir, d)).isDirectory())
      .sort().reverse();
    for (const dir of dirs) {
      const planFile = path.join(plansDir, dir, 'plan.md');
      if (!fs.existsSync(planFile)) continue;
      const content = fs.readFileSync(planFile, 'utf8');
      const statusMatch = content.match(/^status:\s*(.+)$/m);
      if (!statusMatch || !['in_progress', 'pending'].includes(statusMatch[1].trim())) continue;
      // Extract pending phases from phases table (rows with | pending |)
      const pendingPhases = [...content.matchAll(/\|[^|]+\|\s*pending\s*\|/g)]
        .map(m => m[0].trim()).slice(0, 5);
      return [
        `## Skill Context: gk-execute`,
        `Active plan: plans/${dir}/plan.md`,
        pendingPhases.length > 0 ? `Pending phases:\n${pendingPhases.join('\n')}` : 'All phases complete.',
        `→ Use the active plan path above. Do NOT scan plans/ directory.`,
      ].join('\n');
    }
  } catch { /* fall through */ }
  return `## Skill Context: gk-execute\nNo active plan found.`;
}

function buildSkillCreatorContext(cwd) {
  // Read skill-template from skill-creator/references/ (moved from template/)
  let tpl = '';
  try {
    const tplPath = path.join(cwd, '.gemini', 'skills', 'skill-creator', 'references', 'skill-template.md');
    tpl = fs.readFileSync(tplPath, 'utf8').split('\n').slice(0, 60).join('\n');
  } catch { /* template not found — skip injection */ }
  return [
    `## Skill Context: gk-skill-creator`,
    `Output path: .gemini/skills/{name}/SKILL.md`,
    tpl ? `\n### skill-template.md (first 60 lines):\n${tpl}` : '',
    `Registry will be auto-synced by AfterTool hook after you write SKILL.md.`,
  ].filter(Boolean).join('\n');
}

function buildBrainstormContext(state, cwd) {
  const reportTpl = readTemplate(cwd, 'summary-template.md');
  const slug = state.slug || 'session';
  const reportPath = `reports/brainstorm/${fmtTimestamp()}-${slug}.md`;
  return [
    `## Skill Context: gk-brainstorm`,
    `Report output: ${reportPath}`,
    reportTpl ? `\n### summary-template.md:\n${reportTpl}` : '',
    `→ Write your brainstorm artifact to the report path above.`,
  ].filter(Boolean).join('\n');
}

// Main dispatcher — returns empty string if state is null or skill unknown
function buildSkillContext(state, cwd) {
  if (!state || !state.skill) return '';
  try {
    switch (state.skill) {
      case 'gk-plan':         return buildPlanContext(state, cwd);
      case 'gk-execute':      return buildExecuteContext(state, cwd);
      case 'gk-skill-creator': return buildSkillCreatorContext(cwd);
      case 'gk-brainstorm':   return buildBrainstormContext(state, cwd);
      default:                return '';
    }
  } catch { return ''; }
}

module.exports = { buildSkillContext };
