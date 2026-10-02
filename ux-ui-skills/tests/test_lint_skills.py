import subprocess, sys, textwrap
from pathlib import Path

LINT = Path(__file__).parent.parent / "bin" / "lint_skills.py"

def _run(p):
    return subprocess.run([sys.executable, str(LINT), str(p)], capture_output=True, text=True)

def test_valid_skill_passes(tmp_path):
    d = tmp_path / "sample-skill"
    d.mkdir()
    f = d / "SKILL.md"
    f.write_text(textwrap.dedent('''\
        ---
        name: sample-skill
        description: Use when authoring a sample to prove the linter.
        ---
        <!-- type: rigid -->
        ## Doctrine
        Ground every claim.
        ## Process
        1. Do the thing.
        ## Red flags
        | Rationalisation | Reality |
        |---|---|
        | "It is fine" | It is not. |
    '''))
    r = _run(f)
    assert r.returncode == 0, r.stdout + r.stderr
    assert "PASS" in r.stdout

def test_missing_red_flags_table_fails(tmp_path):
    d = tmp_path / "bad-skill"
    d.mkdir()
    f = d / "SKILL.md"
    f.write_text(textwrap.dedent('''\
        ---
        name: bad-skill
        description: Use when proving failure.
        ---
        <!-- type: rigid -->
        ## Doctrine
        x
        ## Process
        1. y
        ## Red flags
        none here
    '''))
    r = _run(f)
    assert r.returncode == 1
    assert "Red flags" in r.stdout

def test_placeholder_token_fails(tmp_path):
    d = tmp_path / "ph-skill"
    d.mkdir()
    f = d / "SKILL.md"
    f.write_text(textwrap.dedent('''\
        ---
        name: ph-skill
        description: Use when proving placeholder detection.
        ---
        <!-- type: rigid -->
        ## Doctrine
        TODO
        ## Process
        1. y
        ## Red flags
        | Rationalisation | Reality |
        |---|---|
        | a | b |
    '''))
    r = _run(f)
    assert r.returncode == 1
    assert "placeholder" in r.stdout.lower()

def test_missing_frontmatter_fails(tmp_path):
    d = tmp_path / "no-fm-skill"
    d.mkdir()
    f = d / "SKILL.md"
    f.write_text(textwrap.dedent('''\
        ## Doctrine
        x
        ## Process
        1. y
        ## Red flags
        | Rationalisation | Reality |
        |---|---|
        | a | b |
    '''))
    r = _run(f)
    assert r.returncode == 1
    assert "Missing YAML frontmatter" in r.stdout

def test_name_dir_mismatch_fails(tmp_path):
    d = tmp_path / "real-dir-name"
    d.mkdir()
    f = d / "SKILL.md"
    f.write_text(textwrap.dedent('''\
        ---
        name: wrong-name
        description: Use when proving name mismatch.
        ---
        <!-- type: rigid -->
        ## Doctrine
        Ground every claim.
        ## Process
        1. Do the thing.
        ## Red flags
        | Rationalisation | Reality |
        |---|---|
        | "It is fine" | It is not. |
    '''))
    r = _run(f)
    assert r.returncode == 1
    assert "!=" in r.stdout

def test_sections_out_of_order_fails(tmp_path):
    d = tmp_path / "order-skill"
    d.mkdir()
    f = d / "SKILL.md"
    f.write_text(textwrap.dedent('''\
        ---
        name: order-skill
        description: Use when proving section order enforcement.
        ---
        <!-- type: rigid -->
        ## Process
        1. Do the thing.
        ## Doctrine
        Ground every claim.
        ## Red flags
        | Rationalisation | Reality |
        |---|---|
        | "It is fine" | It is not. |
    '''))
    r = _run(f)
    assert r.returncode == 1
    assert "order" in r.stdout.lower()

def test_extra_frontmatter_field_fails(tmp_path):
    d = tmp_path / "extra-field-skill"
    d.mkdir()
    f = d / "SKILL.md"
    f.write_text(textwrap.dedent('''\
        ---
        name: extra-field-skill
        description: Use when proving extra field rejection.
        author: someone
        ---
        <!-- type: rigid -->
        ## Doctrine
        Ground every claim.
        ## Process
        1. Do the thing.
        ## Red flags
        | Rationalisation | Reality |
        |---|---|
        | "It is fine" | It is not. |
    '''))
    r = _run(f)
    assert r.returncode == 1
    assert "unexpected field" in r.stdout.lower()
    assert "author" in r.stdout
