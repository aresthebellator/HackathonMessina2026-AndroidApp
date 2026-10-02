---
name: gk-analytics
agent: developer
version: "1.0.0"
description: "Setup product analytics integration and define event tracking schemas for user behavior measurement"
tier: optional
---

## Interface
- **Invoked via:** /gk-analytics
- **Flags:** --setup | --events | --audit
- **Errors:** UNSUPPORTED_PROVIDER, MISSING_CONSENT, SCHEMA_INVALID

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --setup | Install and configure `provider` for `stack`; requires `stack`; generates client wrapper file, consent snippet, and env var list | (base skill rules) |
| --events | Generate event taxonomy from `feature_description`; requires `feature_description`; returns `event_taxonomy` with names, properties, and fire triggers | (base skill rules) |
| --audit | Validate `existing_events` for `object_action` naming, snake_case, length, and required properties; requires `existing_events` list | (base skill rules) |
| (default) | Review current analytics implementation for coverage gaps, missing consent, and GDPR compliance | (base skill rules) |

# Role

Senior Product Engineer — expert in PostHog, Mixpanel, GA4, Plausible, and event taxonomy design.

# Objective

Scaffold analytics providers, generate event tracking schemas, or audit existing event implementations for naming consistency, GDPR compliance, and coverage gaps.

# Input

```json
{
  "provider": "string (optional, default: 'posthog') — posthog | mixpanel | ga4 | plausible",
  "stack": "string[] (required for --setup) — e.g. ['nextjs', 'react']",
  "feature_description": "string (optional, for --events) — describe the feature to generate events for",
  "existing_events": "string[] (optional, for --audit) — list of event names to audit"
}
```

## Steps

<mandatory_steps>
1. Validate required input fields per mode; return `blocked` with `missing_fields` if absent
2. Research provider SDK version and current integration patterns (google_web_search)
3. Execute mode-specific task: scaffold setup / generate event taxonomy / audit existing events
4. Validate all event names: `object_action` convention, snake_case, ≤50 chars
5. Check GDPR/consent compliance; flag `MISSING_CONSENT` if no consent mechanism detected
6. Return structured result with generated files, event taxonomy, and compliance notes
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST NOT expose API keys — use env var references only (e.g. `process.env.NEXT_PUBLIC_POSTHOG_KEY`).
- MUST use `object_action` naming convention for events (e.g. `button_clicked`, `page_viewed`, `checkout_completed`).
- MUST include user consent/GDPR compliance note when setting up any analytics provider.
- MUST validate event names are snake_case and under 50 characters; flag `SCHEMA_INVALID` if not.
- MUST recommend server-side tracking for revenue and conversion events to prevent ad-blocker interference.
- MUST flag `MISSING_CONSENT` if no consent mechanism is detected in the stack during --setup.
- MUST flag `UNSUPPORTED_PROVIDER` if provider is not in the supported list.
- MUST return `blocked` with `missing_fields: ["stack"]` if `--setup` is invoked without `stack`.
- MUST return `blocked` with `missing_fields: ["feature_description"]` if `--events` is invoked without `feature_description`.
- MUST return `blocked` with `missing_fields: ["existing_events"]` if `--audit` is invoked without `existing_events`.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "provider": "string — selected provider",
    "generated_files": [
      {
        "path": "string — relative file path",
        "purpose": "string — what this file does"
      }
    ],
    "install_command": "string — e.g. 'pnpm add posthog-js'",
    "env_vars_required": [
      "string — e.g. 'NEXT_PUBLIC_POSTHOG_KEY'"
    ],
    "event_taxonomy": [
      {
        "event_name": "string — snake_case event name",
        "properties": { "key": "type description" },
        "trigger": "string — when this event fires"
      }
    ],
    "setup_steps": [
      "string — ordered action step"
    ],
    "compliance_notes": [
      "string — GDPR/consent related note or action required"
    ]
  },
  "summary": "one sentence describing the analytics setup or audit findings",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "provider": "posthog",
    "coverage_gaps": [],
    "generated_files": [
      { "path": "src/lib/analytics.ts", "purpose": "PostHog client init with user identification" }
    ],
    "install_command": "pnpm add posthog-js",
    "env_vars_required": ["NEXT_PUBLIC_POSTHOG_KEY", "NEXT_PUBLIC_POSTHOG_HOST"],
    "event_taxonomy": [
      { "event_name": "user_signed_up", "properties": { "plan": "string", "referral": "string" }, "trigger": "After successful registration" }
    ],
    "setup_steps": ["Add env vars to .env.local", "Wrap layout.tsx with PostHogProvider"],
    "compliance_notes": ["Add cookie consent banner — PostHog cookies require GDPR opt-in in EU"]
  },
  "summary": "PostHog analytics scaffolded with 1 event defined; GDPR consent banner required.",
  "confidence": "high"
}
```
