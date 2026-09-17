# Gemini CLI Skill: Expertise in Test-Driven Development (TDD) and Extreme Programming (XP)

[Original File](https://github.com/h3xar0n/tdd-xp-demo/blob/main/GEMINI.md)

This repository contains a **Gemini CLI Skill** designed to enforce the discipline of **Test-Driven Development (TDD)** and **Extreme Programming (XP)**. Once installed, it guides Gemini to act as a pair programmer that prioritizes the Red-Green-Refactor cycle.

## ✨ Features

* **Red-Green-Refactor Enforcement**: Prevents writing implementation code before a failing test exists.
* **XP Philosophies**: Encourages Small Commits, YAGNI (You Ain't Gonna Need It), and Simple Design.
* **Optimized for Python/Pytest**: Familiar with the `tdd-xp-demo` architecture.

## 📦 Installation

Ensure you have the [Gemini CLI](https://geminicli.com) installed, then run:

```bash
gemini skills install https://github.com/YOUR_USERNAME/tdd-xp-skill

```

## 🛠️ Usage

Once installed, the skill is automatically available. You can trigger it by asking Gemini to help you with a TDD task:

> "Gemini, let's build a new authentication module using the TDD skill."

### Available Commands

Inside the Gemini CLI, you can manage this skill using:

* `/skills list` - Verify the skill is active.
* `/skills reload` - Update the skill if you've made local changes.

## 📜 Skill definition

The core logic resides in `SKILL.md`. It instructs the LLM to:

1. Wait for a failing test (**Red**).
2. Write minimal code to pass (**Green**).
3. Clean up the mess (**Refactor**).

---


User Prompt:

"I want to start a new Python feature for a calculator using TDD."

Gemini Response:

"I've detected you want to use TDD. I'm activating the tdd-xp skill. Let's start by writing our first failing test in tests/test_calculator.py..."

Optional: Adding Scripts/Tools
If you want the skill to automatically run tests (like the demo does), you can add a script to the skill folder:

Create ~/.gemini/skills/tdd-xp/scripts/run_tests.sh.

Reference it in your SKILL.md instructions: "When the user asks to 'check progress', execute scripts/run_tests.sh using the shell tool."

