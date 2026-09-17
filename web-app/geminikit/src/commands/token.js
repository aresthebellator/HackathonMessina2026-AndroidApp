const fs = require('fs');
const path = require('path');

const PROJECT_DIR = process.cwd();
const TOKEN_LOG_FILE = path.join(PROJECT_DIR, '.gemini', 'logs', 'token-usage.jsonl');

// Box drawing helpers — content width = 38 chars (between │ │)
const BOX_WIDTH = 38;
function row(label, value) {
  const inner = `  ${label}${value}`;
  return `│${inner.padEnd(BOX_WIDTH)}│`;
}

const MAX_LOG_READ_BYTES = 50 * 1024 * 1024; // 50MB guard

function parseTokenLog() {
  if (!fs.existsSync(TOKEN_LOG_FILE)) return [];
  try {
    const stat = fs.statSync(TOKEN_LOG_FILE);
    if (stat.size > MAX_LOG_READ_BYTES) {
      console.warn(`Warning: token log exceeds 50MB (${Math.round(stat.size / 1024 / 1024)}MB) — reading last 10,000 lines only`);
      // Read only last chunk to avoid OOM
      const buf = Buffer.alloc(MAX_LOG_READ_BYTES);
      const fd = fs.openSync(TOKEN_LOG_FILE, 'r');
      let bytesRead;
      try {
        bytesRead = fs.readSync(fd, buf, 0, MAX_LOG_READ_BYTES, Math.max(0, stat.size - MAX_LOG_READ_BYTES));
      } finally {
        fs.closeSync(fd);
      }
      return buf.slice(0, bytesRead).toString('utf8')
        .split('\n').filter(l => l.trim())
        .slice(-10000)
        .map(l => { try { return JSON.parse(l); } catch { return null; } })
        .filter(Boolean);
    }
  } catch { /* fall through to normal read */ }
  return fs.readFileSync(TOKEN_LOG_FILE, 'utf8')
    .split('\n')
    .filter(l => l.trim())
    .map(l => { try { return JSON.parse(l); } catch { return null; } })
    .filter(Boolean);
}

function groupBySessions(logs) {
  return logs.reduce((acc, entry) => {
    const key = entry.session_id || 'unknown';
    if (!acc[key]) acc[key] = [];
    acc[key].push(entry);
    return acc;
  }, {});
}

/**
 * Savings calculation: for each compression event, savings = tokens_at_compression - tokens_after_reset.
 * savingsRate = totalSaved / (totalActual + totalSaved)
 */
function calcSavings(sessionGroups) {
  let totalSaved = 0;
  let totalActual = 0;

  for (const turns of Object.values(sessionGroups)) {
    turns.sort((a, b) => a.turn - b.turn);
    for (let i = 0; i < turns.length; i++) {
      totalActual += turns[i].tokens || 0;
      if (turns[i].compressed && i + 1 < turns.length) {
        const saved = Math.max(0, (turns[i].tokens || 0) - (turns[i + 1].tokens || 0));
        totalSaved += saved;
      }
    }
  }

  const totalWithout = totalActual + totalSaved;
  if (totalWithout === 0) return 0;
  return Math.round((totalSaved / totalWithout) * 100);
}

function calcSkillBreakdown(logs) {
  const skillTotals = {};
  for (const entry of logs) {
    const skill = entry.skill || 'unknown';
    skillTotals[skill] = (skillTotals[skill] || 0) + (entry.tokens || 0);
  }
  return Object.entries(skillTotals)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 3);
}

function gain(options = {}) {
  const logs = parseTokenLog();
  if (logs.length === 0) {
    console.log('No token usage data found. Run a Gemini Kit session first.');
    return;
  }

  const sessionGroups = groupBySessions(logs);
  const sessions = Object.values(sessionGroups);

  if (sessions.length === 0) {
    console.log('No sessions found in token log.');
    return;
  }

  let totalTokens = 0;
  let compressionEvents = 0;
  sessions.forEach(sess => sess.forEach(e => {
    totalTokens += e.tokens || 0;
    if (e.compressed) compressionEvents++;
  }));

  const avgTokens = Math.round(totalTokens / sessions.length);
  const savingsPct = calcSavings(sessionGroups);
  const topSkills = calcSkillBreakdown(logs);

  console.log('╔══════════════════════════════════════╗');
  console.log('│  Gemini Kit — Token Analytics        │');
  console.log('├──────────────────────────────────────┤');
  console.log(row('Sessions analyzed:    ', String(sessions.length)));
  console.log(row('Avg tokens/session:   ', avgTokens.toLocaleString()));
  console.log(row('Compression events:   ', String(compressionEvents)));
  console.log(row('Estimated savings:    ~', `${savingsPct}%`));
  console.log('├──────────────────────────────────────┤');
  console.log('│  Top skills by usage:                │');
  topSkills.forEach(([skill, tokens], i) => {
    const truncated = skill.length > 18 ? skill.slice(0, 17) + '…' : skill.padEnd(18);
    const label = `  ${i + 1}. ${truncated}`;
    const val = `${tokens.toLocaleString()} tkn`;
    console.log(`│${(label + val).padEnd(BOX_WIDTH)}│`);
  });
  console.log('╚══════════════════════════════════════╝');

  if (options.history) {
    console.log('\n── Per-Session Breakdown ──');
    sessions.forEach((turns, i) => {
      const sid = turns[0]?.session_id || 'unknown';
      const total = turns.reduce((s, t) => s + (t.tokens || 0), 0);
      const comps = turns.filter(t => t.compressed).length;
      console.log(`  [${i + 1}] ${sid.slice(0, 16)}  turns=${turns.length}  tokens=${total.toLocaleString()}  compressions=${comps}`);
    });
  }

  if (options.json) {
    console.log(JSON.stringify({ sessions: sessions.length, avgTokens, compressionEvents, savingsPct, topSkills }));
  }
}

function discover() {
  const logs = parseTokenLog();
  if (logs.length === 0) {
    console.log('No token usage data found. Run a Gemini Kit session first.');
    return;
  }

  const sessionGroups = groupBySessions(logs);
  const sessions = Object.values(sessionGroups);
  let foundIssues = false;

  console.log('Gemini Kit — Token Optimization Analysis\n');

  // Pattern 1: Sessions without any compression (missed savings opportunity)
  const uncompressedSessions = sessions.filter(s => !s.some(t => t.compressed));
  const uncompressedLong = uncompressedSessions.filter(s => {
    const maxTokens = Math.max(...s.map(t => t.tokens || 0));
    return maxTokens > 20000;
  });
  if (uncompressedLong.length > 0) {
    console.log(`⚠  ${uncompressedLong.length} session(s) exceeded 20K tokens without compression.`);
    console.log('   → Check TOKENS_THRESHOLD in .gemini/hooks/after-model.js (default: 25000).');
    foundIssues = true;
  }

  // Pattern 2: High per-turn token count (surgical read opportunities)
  const highTurns = logs.filter(l => (l.tokens || 0) > 10000);
  const topSkills = calcSkillBreakdown(logs);
  if (highTurns.length > 0 && topSkills.length > 0) {
    const [topSkill, topTokens] = topSkills[0];
    const avgHighTurn = Math.round(highTurns.reduce((s, t) => s + t.tokens, 0) / highTurns.length);
    console.log(`ℹ  ${highTurns.length} turn(s) exceeded 10K tokens (avg: ${avgHighTurn.toLocaleString()}).`);
    console.log(`   Top consumer: ${topSkill} (${topTokens.toLocaleString()} total tokens).`);
    console.log('   → Surgical reads (line ranges, symbols) can reduce per-turn cost by 20-40%.');
    foundIssues = true;
  }

  // Pattern 3: Sessions with no active skill tracked (missing skill-state context)
  const unknownSkillLogs = logs.filter(l => !l.skill || l.skill === 'unknown');
  if (unknownSkillLogs.length > 0) {
    const pct = Math.round((unknownSkillLogs.length / logs.length) * 100);
    console.log(`ℹ  ${pct}% of turns have no skill tracked (${unknownSkillLogs.length}/${logs.length}).`);
    console.log('   → Initialize .gemini/.skill-state.json in skills to improve attribution.');
    foundIssues = true;
  }

  // Pattern 4: Overall savings summary
  const savingsPct = calcSavings(sessionGroups);
  if (savingsPct > 0) {
    console.log(`✓  Context Economy active: ~${savingsPct}% token savings vs uncompressed baseline.`);
  } else if (!foundIssues) {
    console.log('✓  No significant optimization opportunities detected.');
    console.log('   Run more Gemini Kit sessions to build a meaningful dataset.');
  }
}

function report() {
  const logs = parseTokenLog();
  const date = new Date().toISOString().split('T')[0];
  const reportPath = path.join(PROJECT_DIR, 'plans', 'reports', `token-report-${date}.md`);

  const sessionGroups = groupBySessions(logs);
  const sessions = Object.values(sessionGroups);
  const compressionEvents = logs.filter(l => l.compressed).length;
  const totalTokens = logs.reduce((s, l) => s + (l.tokens || 0), 0);
  const avgTokens = sessions.length > 0 ? Math.round(totalTokens / sessions.length) : 0;
  const savingsPct = calcSavings(sessionGroups);
  const topSkills = calcSkillBreakdown(logs);

  let md = `# Token Analytics Report\n\n`;
  md += `**Generated:** ${new Date().toISOString()}  \n`;
  md += `**Data range:** ${logs[0]?.ts || 'N/A'} → ${logs[logs.length - 1]?.ts || 'N/A'}\n\n`;

  md += `## Dashboard\n\n`;
  md += `| Metric | Value |\n|--------|-------|\n`;
  md += `| Sessions analyzed | ${sessions.length} |\n`;
  md += `| Total turns | ${logs.length} |\n`;
  md += `| Avg tokens/session | ${avgTokens.toLocaleString()} |\n`;
  md += `| Compression events | ${compressionEvents} |\n`;
  md += `| Estimated savings | ~${savingsPct}% |\n\n`;

  md += `## Top Skills by Token Usage\n\n`;
  md += `| Rank | Skill | Total Tokens |\n|------|-------|-------------|\n`;
  topSkills.forEach(([skill, tokens], i) => {
    md += `| ${i + 1} | ${skill} | ${tokens.toLocaleString()} |\n`;
  });

  md += `\n## Optimization Findings\n\n`;
  const uncompressedLong = sessions.filter(s =>
    !s.some(t => t.compressed) && Math.max(...s.map(t => t.tokens || 0)) > 20000
  );
  if (uncompressedLong.length > 0) {
    md += `- ⚠ ${uncompressedLong.length} session(s) exceeded 20K tokens without compression\n`;
  }
  const highTurns = logs.filter(l => (l.tokens || 0) > 10000);
  if (highTurns.length > 0) {
    md += `- ℹ ${highTurns.length} turn(s) exceeded 10K tokens — consider surgical reads\n`;
  }
  if (savingsPct > 0) {
    md += `- ✓ Context Economy active: ~${savingsPct}% savings vs uncompressed baseline\n`;
  }

  md += `\n## Per-Session Breakdown\n\n`;
  md += `| Session | Turns | Total Tokens | Compressions |\n|---------|-------|-------------|-------------|\n`;
  sessions.forEach((turns, i) => {
    const sid = (turns[0]?.session_id || 'unknown').slice(0, 16);
    const total = turns.reduce((s, t) => s + (t.tokens || 0), 0);
    const comps = turns.filter(t => t.compressed).length;
    md += `| ${sid} | ${turns.length} | ${total.toLocaleString()} | ${comps} |\n`;
  });

  try {
    fs.mkdirSync(path.join(PROJECT_DIR, 'plans', 'reports'), { recursive: true });
    fs.writeFileSync(reportPath, md, 'utf8');
    console.log(`Report generated: ${reportPath}`);
  } catch (err) {
    console.error(`Failed to write report: ${err.message}`);
  }
}

module.exports = { gain, discover, report };
