// AfterTool hook — logs tool invocations + triggers post-write processing for write operations
// Official AfterTool API: { session_id, hook_event_name, tool_name, tool_input, tool_response, cwd, timestamp }
const path = require('path');
const { appendMemory } = require('./lib/memory-manager');
const { readStdin } = require('./lib/read-stdin');
const { logError, logInfo } = require('./lib/logger');
const { isWriteOperation, getFilePath, createPhaseTemplates, syncSkillRegistry, indexReport } = require('./lib/post-write-processor');
const { truncateResponse } = require('./lib/response-truncator');

const SENSITIVE_TOKENS = /^(key|token|secret|password|credential|auth(orization)?)$/i;

// Redact sensitive field values before logging.
// Splits field name by _ or - to catch compound names like api_key, auth_token.
function isSensitiveKey(name) {
  return String(name).toLowerCase().split(/[_-]/).some(p => SENSITIVE_TOKENS.test(p));
}

function redactSensitive(obj) {
  if (!obj || typeof obj !== 'object') return obj;
  return Object.fromEntries(
    Object.entries(obj).map(([k, v]) =>
      isSensitiveKey(k) ? [k, '[REDACTED]'] : [k, v]
    )
  );
}

async function main() {
  try {
    const input = readStdin();
    // Official AfterTool API uses snake_case: tool_name, tool_input, tool_response, cwd
    const { tool_name = 'unknown', tool_input, tool_response, cwd = process.cwd() } = input;

    const redactedInput = redactSensitive(tool_input);
    const hasError = tool_response && (tool_response.error || tool_response.status === 'error');
    const status = hasError ? 'error' : 'ok';

    const ts = new Date().toISOString();
    const inputSummary = redactedInput ? JSON.stringify(redactedInput).slice(0, 120) : '';
    const logLine = `[${ts}] TOOL ${tool_name} → ${status}${inputSummary ? ` | ${inputSummary}` : ''}\n`;
    appendMemory('execution.md', logLine);

    // Post-write processing: detect write operations and trigger auto-actions
    if (isWriteOperation(tool_name)) {
      const filePath = getFilePath(tool_input);
      if (filePath) {
        const absPath = path.isAbsolute(filePath) ? filePath : path.join(cwd, filePath);
        // Guard against path traversal via malicious cwd or filePath
        const safeCwd = path.resolve(cwd);
        if (!path.resolve(absPath).startsWith(safeCwd + path.sep) && path.resolve(absPath) !== safeCwd) {
          logError('after-tool', `Path escape attempt blocked: ${absPath}`);
          process.stdout.write(JSON.stringify({ status: 'ok' }));
          process.exit(0);
        }
        const relPath = path.relative(cwd, absPath);

        if (/^plans\/[^/]+\/plan\.md$/.test(relPath)) {
          const created = createPhaseTemplates(absPath, cwd);
          if (created.length > 0) {
            process.stdout.write(JSON.stringify({
              hookSpecificOutput: {
                additionalContext: `Phase stubs auto-created: ${created.join(', ')} — fill in content for each phase.`
              }
            }));
            process.exit(0);
          }
        } else if (/^\.gemini\/skills\/[^/]+\/SKILL\.md$/.test(relPath)) {
          syncSkillRegistry(cwd);
        } else if (/^reports\//.test(relPath)) {
          indexReport(relPath, cwd);
        }
      }
    }

    // Truncate large tool responses to save context tokens
    const truncated = truncateResponse(tool_name, tool_input, tool_response);
    if (truncated) {
      logInfo('after-tool', `Truncated ${tool_name} response`);
      process.stdout.write(JSON.stringify({
        hookSpecificOutput: { toolResponse: truncated }
      }));
      process.exit(0);
    }

  } catch (err) {
    logError('after-tool', err);
  }

  process.stdout.write(JSON.stringify({ status: 'ok' }));
  process.exit(0);
}

main();
