#!/usr/bin/env bash
# Gemini Kit Setup Script
# Validates and creates the .gemini/ directory structure.
# Safe to run multiple times — idempotent, no destructive operations.
# Usage: bash .gemini/skills/install.sh
set -e

GEMINI_DIR=".gemini"
PASS=0
FAIL=0

# Required subdirectories
DIRS=(
  "agents"
  "skills"
  "commands"
  "hooks"
  "memory"
  "prompts"
  "rules"
  "schemas"
  "tools"
  "template"
)

# Core files that must exist for the framework to function
CORE_FILES=(
  "settings.json"
)

echo "Gemini Kit — Setup & Validation"
echo "================================"
echo ""

# Check .gemini/ root exists
if [ ! -d "$GEMINI_DIR" ]; then
  echo "  [MISSING] $GEMINI_DIR/ — creating..."
  mkdir -p "$GEMINI_DIR"
  FAIL=$((FAIL + 1))
else
  echo "  [OK]      $GEMINI_DIR/"
  PASS=$((PASS + 1))
fi

echo ""
echo "Subdirectories:"

for dir in "${DIRS[@]}"; do
  target="$GEMINI_DIR/$dir"
  if [ -d "$target" ]; then
    echo "  [OK]      $target/"
    PASS=$((PASS + 1))
  else
    mkdir -p "$target"
    echo "  [CREATED] $target/"
    FAIL=$((FAIL + 1))
  fi
done

echo ""
echo "Core files:"

for file in "${CORE_FILES[@]}"; do
  target="$GEMINI_DIR/$file"
  if [ -f "$target" ]; then
    echo "  [OK]      $target"
    PASS=$((PASS + 1))
  else
    echo "  [MISSING] $target"
    FAIL=$((FAIL + 1))
  fi
done

echo ""
echo "================================"
echo "Results: $PASS OK, $FAIL issue(s)"
echo ""

if [ "$FAIL" -gt 0 ]; then
  echo "Action required:"
  echo "  - Directories were created automatically."
  echo "  - Missing core files must be added manually."
  echo "  - See docs/codebase-summary.md for the full file list."
  echo ""
  echo "Run /gk-help once core files are in place."
  exit 1
else
  echo "Gemini Kit is ready."
  echo "Run /gk-help to get started."
  exit 0
fi
