#!/usr/bin/env node
/**
 * Gemini Kit Benchmark Runner
 * Reads task definition files and displays scoring criteria.
 * Usage: node benchmarks/runner.js [task-id]
 */

const fs = require('fs');
const path = require('path');

const TASKS_DIR = path.join(__dirname, 'tasks');

function parseFrontmatter(content) {
  const match = content.match(/^---\n([\s\S]*?)\n---/);
  if (!match) return {};
  const fm = {};
  for (const line of match[1].split('\n')) {
    const [key, ...rest] = line.split(':');
    if (key && rest.length) fm[key.trim()] = rest.join(':').trim().replace(/^"|"$/g, '');
  }
  return fm;
}

function loadTask(filePath) {
  const content = fs.readFileSync(filePath, 'utf8');
  const fm = parseFrontmatter(content);
  const body = content.replace(/^---[\s\S]*?---\n/, '');
  return { ...fm, body, file: path.basename(filePath) };
}

function loadAllTasks() {
  return fs.readdirSync(TASKS_DIR)
    .filter(f => f.endsWith('.md'))
    .sort()
    .map(f => loadTask(path.join(TASKS_DIR, f)));
}

function printTask(task) {
  console.log(`\n${'═'.repeat(60)}`);
  console.log(`Task ${task.id}: ${task.name}`);
  console.log(`${'═'.repeat(60)}`);
  console.log(`Category:   ${task.category}`);
  console.log(`Skill:      ${task.skill}`);
  console.log(`Difficulty: ${task.difficulty}`);
  console.log(`Max tokens: ${task.max_tokens}`);
  console.log(`Timeout:    ${task.timeout_seconds}s`);
  console.log('\n' + task.body.trim());
}

function printSummary(tasks) {
  console.log('\nGemini Kit Benchmark Suite');
  console.log('═'.repeat(60));
  console.log(`Total tasks: ${tasks.length}\n`);
  tasks.forEach(t => {
    console.log(`  [${t.id}] ${(t.name || '').padEnd(35)} ${(t.category || '').padEnd(15)} ${t.difficulty || ''}`);
  });
  console.log('\nRun: node benchmarks/runner.js <task-id>  (e.g. 01, 02)');
}

// Main
const arg = process.argv[2];
const tasks = loadAllTasks();

if (!arg) {
  printSummary(tasks);
} else {
  const task = tasks.find(t => t.id === arg || t.file.startsWith(arg));
  if (!task) {
    console.error(`Task "${arg}" not found. Available: ${tasks.map(t => t.id).join(', ')}`);
    process.exit(1);
  }
  printTask(task);
}
