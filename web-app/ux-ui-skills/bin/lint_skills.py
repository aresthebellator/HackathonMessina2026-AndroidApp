#!/usr/bin/env python3
"""Structural linter for SKILL.md files. Enforces CONVENTIONS.md. stdlib only."""
import re, sys
from pathlib import Path

def lint(path: Path) -> list[str]:
    text = path.read_text(encoding="utf-8")
    errs = []
    fm = re.match(r"^---\n(.*?)\n---\n", text, re.DOTALL)
    if not fm:
        errs.append("Missing YAML frontmatter fenced by ---")
    else:
        if len(fm.group(0)) > 1024:
            errs.append("Frontmatter exceeds 1024 characters")
        body_after_fm = text[fm.end():]
        name_m = re.search(r"^name:\s*([a-z0-9-]+)\s*$", fm.group(1), re.MULTILINE)
        desc_m = re.search(r"^description:\s*(.+?)\s*$", fm.group(1), re.MULTILINE)
        if not name_m:
            errs.append("Frontmatter: missing kebab-case `name`")
        elif name_m.group(1) != path.parent.name:
            errs.append(f"Frontmatter: name '{name_m.group(1)}' != dir '{path.parent.name}'")
        if not desc_m:
            errs.append("Frontmatter: missing `description`")
        elif not re.match(r"Use (when|before|after) ", desc_m.group(1)):
            errs.append("Frontmatter: description must start with 'Use when/before/after '")
        # Fix 4: enforce no additional frontmatter fields
        for line in fm.group(1).splitlines():
            if not line.strip():
                continue
            if not re.match(r"^(name|description):", line):
                key = line.split(":")[0].strip()
                errs.append(f"Frontmatter: unexpected field '{key}'")
        # Fix 1: type-comment must appear immediately after closing fence (at most one newline)
        if not re.match(r"[ \t]*\n?<!-- type: (rigid|flexible) -->", body_after_fm):
            errs.append("Missing `<!-- type: rigid|flexible -->` after frontmatter")
    for h in ("## Doctrine", "## Process", "## Red flags"):
        if h not in text:
            errs.append(f"Missing required section: {h}")
    # Fix 2: enforce section order
    positions = [text.find(h) for h in ("## Doctrine", "## Process", "## Red flags")]
    if all(p != -1 for p in positions) and positions != sorted(positions):
        errs.append("Sections must appear in order: ## Doctrine, ## Process, ## Red flags")
    if "## Red flags" in text:
        tail = text.split("## Red flags", 1)[1]
        if "| Rationalisation | Reality |" not in tail:
            errs.append("Red flags: missing '| Rationalisation | Reality |' table header")
    # Fix 3: case-insensitive placeholder detection
    text_lower = text.lower()
    for tok in ("todo", "tbd", "fixme", "placeholder"):
        if tok in text_lower:
            errs.append(f"Contains placeholder token: {tok}")
    if not text.endswith("\n") or text.endswith("\n\n"):
        errs.append("File must end with exactly one trailing newline")
    return errs

def main(argv: list[str]) -> int:
    if len(argv) < 2:
        print("usage: lint_skills.py <SKILL.md> [<SKILL.md> ...]")
        return 2
    failed = False
    for arg in argv[1:]:
        p = Path(arg)
        errs = lint(p)
        if errs:
            failed = True
            print(f"FAIL {p}")
            for e in errs:
                print(f"  - {e}")
        else:
            print(f"PASS {p}")
    return 1 if failed else 0

if __name__ == "__main__":
    raise SystemExit(main(sys.argv))
