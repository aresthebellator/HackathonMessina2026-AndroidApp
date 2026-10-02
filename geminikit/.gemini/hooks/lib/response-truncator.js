// response-truncator.js — truncates large tool responses to save context tokens
// Returns truncated response or null (no truncation needed)

const TRUNCATION_RULES = {
  read_file:         { maxLines: 200, keepHead: 150, keepTail: 10 },
  run_shell_command: { maxLines: 100, keepHead: 80,  keepTail: 10 },
  shell:             { maxLines: 100, keepHead: 80,  keepTail: 10 },
  execute_command:   { maxLines: 100, keepHead: 80,  keepTail: 10 },
};

function truncateResponse(toolName, toolInput, toolResponse) {
  // Opt-out check
  if (toolInput?.__no_truncate) return null;

  const rule = TRUNCATION_RULES[toolName];
  if (!rule) return null;

  // Extract text content from response
  const text = typeof toolResponse === 'string'
    ? toolResponse
    : toolResponse?.output || toolResponse?.content || toolResponse?.text || null;
  if (!text || typeof text !== 'string') return null;

  const lines = text.split('\n');
  if (lines.length <= rule.maxLines) return null;

  const truncatedCount = lines.length - rule.keepHead - rule.keepTail;
  const head = lines.slice(0, rule.keepHead);
  const tail = lines.slice(-rule.keepTail);
  const marker = `\n[... truncated ${truncatedCount} lines — use grep_search for targeted access ...]\n`;

  const truncated = [...head, marker, ...tail].join('\n');

  // Return in same shape as input
  if (typeof toolResponse === 'string') return truncated;
  const key = toolResponse?.output !== undefined ? 'output'
    : toolResponse?.content !== undefined ? 'content' : 'text';
  return { ...toolResponse, [key]: truncated };
}

module.exports = { truncateResponse };
