// post-write-processor.js
// Handles post-write actions triggered by AfterTool hook:
//   plans/*/plan.md       → scaffold phase stub files from template
//   .gemini/skills/*/SKILL.md → sync skill registry
//   reports/**/*          → append to execution.md index
// All functions are fail-open (errors caught, return false/[])

const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');

// Gemini CLI write operation tool names (best-effort — expand if new names discovered)
const WRITE_TOOL_NAMES = new Set([
  'write_file', 'edit_file', 'replace_file', 'create_file',
  'replace_in_file', 'overwrite_file', 'patch_file'
]);

function isWriteOperation(toolName) {
  return WRITE_TOOL_NAMES.has(String(toolName).toLowerCase());
}

// Gemini CLI may use 'path', 'file_path', or 'filename' for the file path field
function getFilePath(toolInput) {
  return toolInput?.path || toolInput?.file_path || toolInput?.filename || null;
}

// plans/*/plan.md → create phase-NN.md stubs from phase-template.md
// Counts phase rows in the Phases table (| N | ... |), caps at 7
function createPhaseTemplates(planFilePath, cwd) {
  try {
    const planDir = path.dirname(planFilePath);
    const templatePath = path.join(cwd, '.gemini', 'template', 'phase-template.md');
    if (!fs.existsSync(templatePath)) return [];
    const planContent = fs.readFileSync(planFilePath, 'utf8');
    const template = fs.readFileSync(templatePath, 'utf8');

    // Match table rows like "| 01 | ..." or "| 1 |..." in the Phases section
    const phaseMatches = [...planContent.matchAll(/^\|\s*(\d{1,2})\s*\|/gm)];
    const phaseCount = Math.min(phaseMatches.length || 3, 7); // fallback 3 stubs

    const created = [];
    for (let i = 1; i <= phaseCount; i++) {
      const num = String(i).padStart(2, '0');
      const phaseFile = path.join(planDir, `phase-${num}.md`);
      if (!fs.existsSync(phaseFile)) {
        const content = template.replace(/\{phase_number\}/g, num);
        fs.writeFileSync(phaseFile, content);
        created.push(`phase-${num}.md`);
      }
    }
    return created;
  } catch { return []; }
}

// .gemini/skills/*/SKILL.md → run sync-registry.js if it exists
function syncSkillRegistry(cwd) {
  try {
    const syncScript = path.join(cwd, '.gemini', 'scripts', 'sync-registry.js');
    if (!fs.existsSync(syncScript)) return false;
    spawnSync(process.execPath, [syncScript], { cwd, stdio: 'pipe', timeout: 10000 });
    return true;
  } catch { return false; }
}

// reports/**/* → append entry to execution.md
function indexReport(reportPath, cwd) {
  try {
    const { appendMemory } = require('./memory-manager');
    const ts = new Date().toISOString();
    appendMemory('execution.md', `[${ts}] REPORT created: ${reportPath}\n`);
    return true;
  } catch { return false; }
}

module.exports = { isWriteOperation, getFilePath, createPhaseTemplates, syncSkillRegistry, indexReport };
