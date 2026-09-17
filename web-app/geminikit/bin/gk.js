#!/usr/bin/env node

const { program } = require('commander')
const { version } = require('../package.json')

// Verify Node.js version requirement
const [major] = process.versions.node.split('.').map(Number)
if (major < 18) {
  console.error(`gemini-kit requires Node.js >=18. Current: v${process.versions.node}`)
  process.exit(1)
}

program
  .name('gk')
  .description('Gemini Kit — multi-agent AI development framework for Gemini CLI')
  .version(version)

program
  .command('init')
  .description('Scaffold .gemini/ and GEMINI.md into the current project')
  .action(() => require('../src/commands/init')())

program
  .command('list')
  .description('List available agents and skills in the current project')
  .action(() => require('../src/commands/list')())

program
  .command('update')
  .description('Update gemini-kit to the latest version from GitHub')
  .action(() => require('../src/commands/update')())

program
  .command('doctor')
  .description('Diagnose and fix Gemini Kit setup issues')
  .option('--fix', 'Auto-fix addressable issues')
  .action((options) => require('../src/commands/doctor').run(options))


const { gain, discover, report } = require('../src/commands/token')
const tokenCmd = program.command('token').description('Token analytics and savings tracking')

tokenCmd
  .command('gain')
  .description('Show token savings dashboard')
  .option('--history', 'Show per-session breakdown')
  .option('--json', 'Output as JSON')
  .action((options) => gain(options))

tokenCmd
  .command('discover')
  .description('Identify token optimization opportunities')
  .action(() => discover())

tokenCmd
  .command('report')
  .description('Generate markdown report in plans/reports/')
  .action(() => report())

program.parse()
