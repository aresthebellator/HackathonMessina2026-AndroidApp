const fs = require('fs');
const path = require('path');

function parsePlan(filePath) {
    let content;
    try {
        content = fs.readFileSync(filePath, 'utf-8');
    } catch (err) {
        console.error(`Error: Cannot read file: ${filePath} — ${err.message}`);
        process.exit(1);
    }
    const lines = content.split(/\r?\n/); // Support both \n and \r\n
    
    let currentTask = null;
    let tasks = [];
    let completedCount = 0;
    let totalCount = 0;

    // Fixed regex to be more robust
    const taskRegex = /^\s*-\s*\[\s*([xX ]?)\s*\]\s*(.*)$/;

    for (let i = 0; i < lines.length; i++) {
        const line = lines[i].trim();
        if (!line) continue;
        
        const match = line.match(taskRegex);
        
        if (match) {
            const status = match[1].toLowerCase();
            const description = match[2].trim();
            
            totalCount++;
            if (status === 'x') {
                completedCount++;
            } else {
                if (!currentTask) {
                    currentTask = {
                        description,
                        lineIndex: i,
                        originalLine: lines[i]
                    };
                }
            }
            
            tasks.push({
                description,
                status: status === 'x' ? 'completed' : 'pending',
                lineIndex: i
            });
        }
    }

    const result = {
        currentTask,
        progress: {
            total: totalCount,
            completed: completedCount,
            percentage: totalCount > 0 ? Math.round((completedCount / totalCount) * 100) : 0
        },
        status: completedCount === totalCount && totalCount > 0 ? 'completed' : (completedCount > 0 ? 'ongoing' : 'not_started')
    };

    const lines = [
        `Current task: ${result.currentTask ? `"${result.currentTask.description}"` : 'none'}`,
        `Progress: ${result.progress.completed}/${result.progress.total} tasks (${result.progress.percentage}%)`,
        `Status: ${result.status}`,
    ];
    if (result.currentTask) {
        lines.push(`Line index: ${result.currentTask.lineIndex}`);
    }
    console.log(lines.join('\n'));
}

const planFilePath = process.argv[2];
if (!planFilePath) {
    console.error('Usage: node parse_plan.js <path_to_plan_file>');
    process.exit(1);
}

parsePlan(planFilePath);
