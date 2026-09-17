# Setup Guide

This guide covers the setup and installation of Gemini Kit.

## Prerequisites

- **Node.js**: Version 18 or higher.
- **Python**: Version 3.10 or higher.
- **Gemini CLI**: The latest version must be installed and authenticated.

---

## Option 1: Standard Installation (Recommended)

This method installs the `gk` command-line tool globally, allowing you to scaffold Gemini Kit into any project on your machine.

**1. Install via npm**

Open your terminal and run the following command:
```bash
npm install -g geminicli-kit
```

**2. Initialize in Your Project**

Navigate to your project's root directory and run the init command:
```bash
cd /path/to/your/project
gk init
```
This command creates a `.gemini` directory in your project, containing all the necessary agents, skills, and configuration files. It also creates a `GEMINI.md` file, which is the root entry point for the Orchestrator.

**3. Verify Installation**

To check that everything is working, you can list the available agents and skills:
```bash
gk list
```

---

## Option 2: Contributor Setup (For Development)

This method is for developers who want to contribute to the Gemini Kit framework itself.

**1. Clone the Repository**
```bash
git clone https://github.com/hieund-it/geminikit.git
cd geminikit
```

**2. Install Dependencies**

Install the project's development dependencies defined in `package.json`. This is required to run the local version of the framework and its scripts.
```bash
npm install
```

**3. Set Up Environment Variables**

Create a `.env` file in the root of the cloned repository by copying the example file:
```bash
cp .gemini/.env.example .env
```
You will need to edit this `.env` file to add your own API keys. **Important**: `GEMINI_API_KEY` is required for native hooks (SessionStart, AfterModel, PreCompress, AfterTool, SessionEnd) to function properly. Obtain your Gemini API key at https://aistudio.google.com/app/apikey. Other optional API keys (GitHub, Slack, Google Maps) are only needed if you plan to use those specific MCP integrations.

**4. Run Commands Locally**

Open the **Gemini CLI application** in the root of the `geminikit` directory. You can now use the AI commands directly within the chat interface:
```bash
/gk-ask "What is the status of the system?"
```
If you receive a response, your local development environment is set up correctly.

---

## Troubleshooting

### Node.js Version Error
**Error:** `gemini-kit requires Node.js >=18`

**Solution:**
```bash
node --version                    # Check current version
nvm install 18                    # Install Node 18+ (if using nvm)
npm install -g geminicli-kit      # Reinstall gk
```

### Python Not Found
**Error:** `python: command not found` or `python3.10+ required`

**Solution:**
```bash
python3 --version                 # Check Python version
brew install python@3.10          # macOS (Homebrew)
sudo apt install python3.10       # Ubuntu/Debian
choco install python              # Windows (Chocolatey)
```

### Gemini CLI Not Installed
**Error:** `gemini command not found`

**Solution:**
```bash
npm install -g @google/gemini-cli
gemini --version                  # Verify installation
```

### Hooks Not Triggering
**Error:** Native hooks in `.gemini/hooks/` not executing

**Solution:**
- Verify `.gemini/settings.json` has hook configuration
- Check Node.js version (18+ required for hooks)
- Verify `.gemini/hooks/*.js` files are executable
- Check `.gemini/errors.log` for silent failures (enable via `LOG_LEVEL=debug`)

### Memory Files Corrupted
**Error:** Session context lost or memory files inaccessible

**Solution:**
```bash
gk doctor --fix                   # Auto-heal memory system
rm -rf .gemini/memory/*           # Reset memory (lose context history)
```

---

## Upgrading Gemini Kit

### From npm (Standard Installation)
```bash
npm install -g geminicli-kit@latest
gk update                          # Update framework in current project
```

### From Repository (Development)
```bash
cd /path/to/geminikit
git pull origin main              # Get latest code
npm install                       # Update dependencies
```

---

## Platform-Specific Notes

### macOS
- Use Homebrew for dependencies: `brew install node python@3.10`
- Verify Node version after installation: `node --version`
- May need to accept security prompts for first hook execution

### Linux (Ubuntu/Debian)
- Install Node via `sudo apt install nodejs npm`
- Python 3.10+ available via APT: `sudo apt install python3.10`
- Ensure proper file permissions: `chmod +x .gemini/hooks/*.js`

### Windows
- Use WSL2 (Windows Subsystem for Linux) for best compatibility
- Or install Node.js and Python directly from official installers
- Python path may need to be added to PATH environment variable
- Use `npm install -g geminicli-kit` in PowerShell (Admin mode recommended)
