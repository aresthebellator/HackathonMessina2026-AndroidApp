// sync-scaffold.js — copies .gemini/ → scaffold/ for npm publish.
// Run before `npm publish` only. Do NOT run on every SKILL.md edit.
// For live registry sync, use: node .gemini/scripts/sync-registry.js

const fs = require('fs');
const path = require('path');

const src = path.join(__dirname, '../../.gemini');
const dest = path.join(__dirname, '../../scaffold');

function sync() {
  try {
    console.log('âš™  Generating scaffold from .gemini...');
    
    // 1. Ensure source exists
    if (!fs.existsSync(src)) {
      console.warn('âš   Warning: .gemini directory not found. Skipping scaffold generation.');
      return;
    }

    // 2. Clean up old scaffold (Using Node 18+ rmSync)
    if (fs.existsSync(dest)) {
      fs.rmSync(dest, { recursive: true, force: true });
    }
    
    // 3. Create fresh scaffold directory
    fs.mkdirSync(dest, { recursive: true });
    
    // 4. Copy content with native filter (Using Node 18+ cpSync)
    fs.cpSync(src, dest, {
      recursive: true,
      filter: (srcPath) => {
        if (srcPath === src) return true;
        
        const relative = path.relative(src, srcPath);
        const parts = relative.split(path.sep);
        const isTrash = relative.startsWith('memory') ||
                        relative.startsWith('runtime') ||
                        relative.startsWith('logs') ||
                        parts.includes('node_modules') ||
                        parts.includes('.venv') ||
                        path.basename(srcPath) === '.env' ||
                        path.basename(srcPath) === '.skill-state.json';

        return !isTrash;
      }
    });
    
    console.log('âœ“ Scaffold generated successfully.');
  } catch (err) {
    console.warn('âš   Scaffold generation warning:', err.message);
    console.warn('    Run "npm run build" manually if scaffold is missing.');
  }
}

sync();
