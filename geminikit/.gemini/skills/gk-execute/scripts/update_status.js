const fs = require('fs');
const path = require('path');

const VALID_STATUS_CHARS = new Set([' ', 'x', 'X']);
const MAX_DESC_LENGTH = 500;
const PROJECT_ROOT = path.resolve(__dirname, '../../../../');

function updateStatus(filePath, taskDescription, newStatusChar) {
    // Validate status char
    if (!VALID_STATUS_CHARS.has(newStatusChar)) {
        console.error(`Error: invalid status char '${newStatusChar}' — must be ' ', 'x', or 'X'`);
        process.exit(1);
    }

    // Validate path stays within project root
    const resolved = path.resolve(filePath);
    if (!resolved.startsWith(PROJECT_ROOT + path.sep) && resolved !== PROJECT_ROOT) {
        console.error(`Error: path escapes project root: ${filePath}`);
        process.exit(1);
    }

    if (!fs.existsSync(resolved)) {
        console.error(`Error: File not found: ${filePath}`);
        process.exit(1);
    }

    const content = fs.readFileSync(resolved, 'utf-8');
    const lines = content.split(/\r?\n/);

    // Cap description length before regex construction to prevent ReDoS
    const desc = taskDescription.slice(0, MAX_DESC_LENGTH);
    let updated = false;
    const escapedDesc = desc.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    const taskRegex = new RegExp(`^(\\s*-\\s*\\[)([xX ]?)(\\]\\s+)(${escapedDesc})(\\s*.*)$`, 'i');

    for (let i = 0; i < lines.length; i++) {
        const line = lines[i];
        const match = line.match(taskRegex);

        if (match) {
            lines[i] = `${match[1]}${newStatusChar}${match[3]}${match[4]}${match[5]}`;
            updated = true;
            break;
        }
    }

    if (updated) {
        fs.writeFileSync(resolved, lines.join('\n'), 'utf-8');
        console.log(`Successfully updated task: "${desc}" to [${newStatusChar}]`);
    } else {
        console.error(`Error: Task not found: "${desc}"`);
        process.exit(1);
    }
}

const planFilePath = process.argv[2];
const taskDescription = process.argv[3];
const newStatusChar = process.argv[4] || 'x';

if (!planFilePath || !taskDescription) {
    console.error('Usage: node update_status.js <path_to_plan_file> <task_description> [status_char]');
    process.exit(1);
}

updateStatus(planFilePath, taskDescription, newStatusChar);
