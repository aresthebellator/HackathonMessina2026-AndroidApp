# Gemini Kit

**Gemini Kit** is a multi-agent AI development framework that runs inside the **Gemini CLI**. It orchestrates specialized AI agents and atomic skills to deliver high-quality software engineering outcomes with a focus on architectural integrity and token efficiency.

## 🚀 Quick Start

### Prerequisites
- **Gemini CLI** installed.
- **Node.js** (v18+) and **Python** (v3.10+) available.

### Installation

**1. Install CLI (Recommended)**
```bash
npm install -g geminicli-kit
```
This installs the `gk` command globally. You can then scaffold Gemini Kit into any project:
```bash
cd my-project
gk init      # Scaffold .gemini/ structure, agents, and skills
gk list      # List available agents and skills
gk update    # Pull the latest framework updates
gk uninstall # Remove Gemini Kit from the current project
gk bridge    # Manage Claude-Gemini bridge pipeline
gk version   # Show installed Gemini Kit version
```

**2. Clone for Contribution**
```bash
git clone https://github.com/hieund-it/geminikit.git
cd geminikit
npm install
```
For more details, see the [Setup Guide](docs/SETUP_GUIDE.md).

## 📚 Core Documentation

All documentation is located in the `docs/` directory:

- **[Onboarding Guide](docs/ONBOARDING.md)**: The best place to start. A rapid introduction to the framework.
- **[System Architecture](docs/ARCHITECTURE.md)**: Explains how the Orchestrator, Agents, and Skills work together.
- **[Agent Registry](docs/AGENTS_REGISTRY.md)**: A complete list of all available agents and their specializations.
- **[Commands Reference](docs/COMMANDS_REFERENCE.md)**: A guide to using the `/gk-` commands to interact with agents.
- **[Skills Guide](docs/SKILLS_GUIDE.md)**: A reference for all atomic skills available to agents.

## 🧩 Core Concepts

The framework is built around a few key ideas:

- **Orchestrator**: The central brain that parses commands and delegates tasks.
- **Agents**: Specialized, role-based AIs (e.g., `Developer`, `Reviewer`) that own specific domains.
- **Skills**: Atomic, reusable tools (e.g., `gk-debug`, `gk-git`) that agents use to execute tasks.

## ✨ Key Features

- **🧠 Advanced Memory System**: Features auto-persistence, silent summarization, and self-healing state management to ensure seamless long-running sessions.
- **🛡️ Robust Security Framework**: Implements a strict Agent Permission Matrix, forbidden path blacklisting, and pre-emptive secret masking for safe execution.
- **📉 Context Economy**: Built-in token optimization and context rotation to minimize costs and maximize model performance.

## 🤝 Contributing

We welcome contributions! Please see [CONTRIBUTING.md](docs/CONTRIBUTING.md) for details on how to submit pull requests, report issues, and propose new features.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.
