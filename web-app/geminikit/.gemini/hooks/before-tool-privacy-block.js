// BeforeTool hook — blocks access to sensitive files/commands before tool executes
// Fail-open: allow on error to avoid false blocks
const path = require('path');
const { readStdin } = require('./lib/read-stdin');
const { logError, logInfo } = require('./lib/logger');

const SENSITIVE_FILE_PATTERNS = [
  /^\.env(\.|$)/i,      // .env, .env.local, .env.production
  /\.key$/i,            // *.key
  /\.pem$/i,            // *.pem
  /\.cert$/i,           // *.cert
  /\.p12$/i,            // *.p12
  /secret/i,            // *secret*
  /credential/i,        // *credential*
  /password/i,          // *password*
  /\.token$/i,          // *.token
];

const SENSITIVE_SHELL_PATTERNS = [
  /cat\s+.*\.env/i,
  /echo\s+\$[A-Za-z_]*(KEY|SECRET|TOKEN|PASSWORD|CREDENTIAL)/i,
  /printenv\s+[A-Za-z_]*(KEY|SECRET|TOKEN|PASSWORD|CREDENTIAL)/i,
];

// File-access tool names supported by Gemini CLI
const FILE_TOOLS = ['read_file', 'write_file', 'edit_file', 'create_file', 'replace_file_content'];

function isSensitivePath(filePath) {
  const basename = path.basename(filePath);
  return SENSITIVE_FILE_PATTERNS.some(p => p.test(basename));
}

function isSensitiveCommand(command) {
  return SENSITIVE_SHELL_PATTERNS.some(p => p.test(command));
}

async function main() {
  try {
    const input = readStdin();
    // Official BeforeTool API: { session_id, tool_name, tool_input, cwd, timestamp }
    const { tool_name, tool_input = {} } = input;

    let blocked = false;
    let reason = '';

    const filePath = tool_input.path || tool_input.file_path || tool_input.filename;
    if (FILE_TOOLS.includes(tool_name) && filePath) {
      if (isSensitivePath(filePath)) {
        blocked = true;
        reason = `Blocked: "${path.basename(filePath)}" may contain sensitive data (keys, secrets, credentials). To access this file, ask the user for explicit approval first.`;
      }
    } else if (tool_name === 'run_shell_command' && tool_input.command) {
      if (isSensitiveCommand(tool_input.command)) {
        blocked = true;
        reason = `Blocked: command may expose sensitive environment variables or credentials. Ask the user for explicit approval before running.`;
      }
    }

    if (blocked) {
      logInfo('before-tool-privacy-block', `Blocked ${tool_name}: ${reason}`);
      process.stdout.write(JSON.stringify({ decision: 'deny', reason }));
    } else {
      process.stdout.write(JSON.stringify({ decision: 'allow' }));
    }
  } catch (err) {
    logError('before-tool-privacy-block', err);
    // Fail-open: allow on error to avoid false blocks
    process.stdout.write(JSON.stringify({ decision: 'allow' }));
  }
  process.exit(0);
}

main();
