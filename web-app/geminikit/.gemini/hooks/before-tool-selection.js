// BeforeToolSelection hook — blocks expensive tool patterns
// API: { session_id, tool_name, tool_input, cwd }
// Output: { hookSpecificOutput: { decision: "allow"|"block", reason: "string" } }
const fs = require('fs');
const path = require('path');
const { readStdin } = require('./lib/read-stdin');
const { logError, logInfo } = require('./lib/logger');

const LARGE_DIR_PATTERNS = [/^\/?$/, /node_modules/, /^\.git$/, /^dist$/, /^build$/, /^\.next$/];
const BINARY_EXTENSIONS = /\.(png|jpg|jpeg|gif|webp|svg|wasm|zip|tar|gz|bz2|pdf|exe|bin|dll|so)$/i;
const HISTORY_FILE_PREFIX = 'gk-tool-history-';
const CONSECUTIVE_READ_LIMIT = 3;

function getHistoryPath(sessionId) {
  const safeId = String(sessionId).replace(/[^a-zA-Z0-9-_]/g, '_');
  return path.join('/tmp', `${HISTORY_FILE_PREFIX}${safeId}.json`);
}

function readHistory(sessionId) {
  try {
    return JSON.parse(fs.readFileSync(getHistoryPath(sessionId), 'utf8'));
  } catch { return []; }
}

function writeHistory(sessionId, history) {
  try {
    fs.writeFileSync(getHistoryPath(sessionId), JSON.stringify(history.slice(-20)));
  } catch { /* ignore */ }
}

function checkConsecutiveReads(sessionId, currentTool) {
  const history = readHistory(sessionId);
  history.push(currentTool);
  writeHistory(sessionId, history);

  if (currentTool !== 'read_file') return;
  const recent = history.slice(-CONSECUTIVE_READ_LIMIT);
  if (recent.length === CONSECUTIVE_READ_LIMIT && recent.every(t => t === 'read_file')) {
    logInfo('before-tool-selection', `WARNING: ${CONSECUTIVE_READ_LIMIT}+ consecutive read_file calls — consider using grep_search`);
  }
}

async function main() {
  try {
    const input = readStdin();
    const { session_id = 'default', tool_name = '', tool_input = {} } = input;

    // Block list_directory on large dirs
    if (tool_name === 'list_directory') {
      const dirPath = tool_input?.path || tool_input?.directory || '';
      const isLargeDir = LARGE_DIR_PATTERNS.some(p => p.test(dirPath));
      if (isLargeDir) {
        logInfo('before-tool-selection', `Blocked list_directory on large dir: ${dirPath}`);
        process.stdout.write(JSON.stringify({
          hookSpecificOutput: { decision: 'block', reason: `list_directory blocked on '${dirPath}' — use grep_search or glob patterns instead` }
        }));
        process.exit(0);
      }
    }

    // Block read_file on binary files
    if (tool_name === 'read_file') {
      const filePath = tool_input?.path || tool_input?.file_path || '';
      if (BINARY_EXTENSIONS.test(filePath)) {
        logInfo('before-tool-selection', `Blocked read_file on binary: ${filePath}`);
        process.stdout.write(JSON.stringify({
          hookSpecificOutput: { decision: 'block', reason: `read_file blocked on binary file '${filePath}' — binary files cannot be read as text` }
        }));
        process.exit(0);
      }
      checkConsecutiveReads(session_id, 'read_file');
    } else {
      checkConsecutiveReads(session_id, tool_name);
    }

    process.stdout.write(JSON.stringify({ hookSpecificOutput: { decision: 'allow' } }));
  } catch (err) {
    logError('before-tool-selection', err);
    process.stdout.write(JSON.stringify({ hookSpecificOutput: { decision: 'allow' } }));
  }
  process.exit(0);
}

main();
