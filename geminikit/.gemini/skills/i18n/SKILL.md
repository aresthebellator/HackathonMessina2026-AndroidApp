---
name: gk-i18n
agent: developer
version: "1.0.0"
description: "Setup internationalization, extract hardcoded strings, and validate translation completeness"
tier: optional
---

## Interface
- **Invoked via:** /gk-i18n
- **Flags:** --setup | --extract | --validate
- **Errors:** UNSUPPORTED_FRAMEWORK, MISSING_LOCALE_FILES, EXTRACTION_FAILED

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --setup | Install `library` and scaffold locale routing, middleware, and `[locale]` directory structure; generates provider wrapper and `LocaleSwitcher` component for `locales` | (base skill rules) |
| --extract | Scan `source_dir` for hardcoded UI strings; returns `extracted_strings` count, generated JSON key files by namespace, and ICU-formatted message values | (base skill rules) |
| --validate | Compare all locale translation files against `base_locale`; returns `missing_keys` per locale, `orphaned_keys` no longer used, and ICU syntax errors | (base skill rules) |
| (default) | Quick audit of i18n coverage: detect hardcoded strings, missing locale files, and RTL CSS gaps | (base skill rules) |

# Role

Senior Frontend Engineer — expert in next-intl, i18next, Lingui, ICU message format, RTL layouts.

# Objective

Setup internationalization infrastructure, extract hardcoded strings into translation keys, and validate translation completeness across all configured locales.

# Input

```json
{
  "library": "string (optional, default: 'next-intl') — next-intl | i18next | lingui",
  "locales": "string[] (optional, default: ['en']) — e.g. ['en', 'fr', 'ja', 'ar']",
  "source_dir": "string (optional, default: 'src') — directory to scan for strings",
  "base_locale": "string (optional, default: 'en') — reference locale for validation"
}
```

## Steps

<mandatory_steps>
1. Validate required input fields per mode; return `blocked` with `missing_fields` if absent
2. For --extract: scan `source_dir` for hardcoded UI strings using static analysis
3. For --validate: compare all locale files against `base_locale`; identify missing and orphaned keys
4. For --setup: scaffold locale routing, middleware, and `[locale]` directory structure
5. Validate ICU message format syntax for all extracted/generated strings
6. Check RTL support (CSS dir attribute) if Arabic/Hebrew/Farsi/Urdu locales are included
7. Return structured result with generated files, missing_keys, orphaned_keys, and setup_steps
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST use ICU message format for plurals and gender (e.g., `{count, plural, one {# item} other {# items}}`)
- MUST warn when RTL locales (ar, he, fa, ur) are added without RTL CSS support check
- MUST NOT generate keys for dynamic strings that cannot be statically analyzed
- MUST organize translation files by namespace/feature (not one giant file)
- MUST validate that extracted strings match ICU syntax before writing files
- MUST check for missing translations in all locales against base locale
- MUST return `UNSUPPORTED_FRAMEWORK` if the project stack is incompatible with the chosen `library`
- MUST return `MISSING_LOCALE_FILES` if `--validate` finds no locale JSON/TS files in the project
- MUST return `EXTRACTION_FAILED` if static analysis cannot parse `source_dir` (e.g. unsupported syntax)

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "library": "string — selected library",
    "generated_files": [
      { "path": "string", "purpose": "string" }
    ],
    "install_command": "string",
    "locales_configured": ["string"],
    "extracted_strings": "number — count for --extract mode",
    "missing_keys": [
      { "locale": "string", "key": "string" }
    ],
    "orphaned_keys": ["string"],
    "setup_steps": ["string"]
  },
  "summary": "one sentence describing i18n status and key findings",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "provider": "next-intl",
    "generated_files": [
      { "path": "messages/en.json", "purpose": "English base translations" },
      { "path": "src/lib/i18n.ts", "purpose": "next-intl provider configuration" }
    ],
    "install_command": "pnpm add next-intl",
    "locales_supported": ["en", "vi"],
    "missing_keys": [],
    "orphaned_keys": ["dashboard.oldTitle"],
    "setup_steps": ["Add next-intl plugin to next.config.js", "Wrap root layout with NextIntlClientProvider"]
  },
  "summary": "next-intl configured for en/vi; 1 orphaned key detected in EN messages.",
  "confidence": "high"
}
```
