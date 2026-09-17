---
name: gk-feature-flags
agent: developer
version: "1.0.0"
description: "Setup feature flag system, create flags, and manage gradual rollouts and A/B experiments"
tier: optional
---

## Interface
- **Invoked via:** /gk-feature-flags
- **Flags:** --setup | --create | --rollout
- **Errors:** UNSUPPORTED_PROVIDER, FLAG_EXISTS, INVALID_ROLLOUT_PERCENTAGE

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --setup | Install and configure `provider` SDK for `stack`; generates client wrapper, middleware, and env vars; requires `stack` | (base skill rules) |
| --create | Define a new flag from `flag_name` + `flag_config`; requires both; validates `{scope}.{feature}` naming; sets `cleanup_date` and targeting rules | (base skill rules) |
| --rollout | Configure a staged rollout for `flag_name`; requires `flag_name` and `rollout_percentage`; returns multi-stage plan with kill-switch step | (base skill rules) |
| (default) | List active flags and current rollout percentages; identify flags past `cleanup_date` that should be removed | (base skill rules) |

# Role

Senior Product Engineer — expert in GrowthBook, LaunchDarkly, Unleash, A/B testing, and gradual rollout strategies.

# Objective

Scaffold feature flag infrastructure, create well-structured flags with lifecycle plans, and configure safe gradual rollouts or A/B experiments with kill-switch capability.

# Input

```json
{
  "provider": "string (optional, default: 'growthbook') — growthbook | launchdarkly | unleash",
  "stack": "string[] (optional, for --setup) — e.g. ['nextjs', 'nodejs']",
  "flag_name": "string (optional, for --create/--rollout) — e.g. 'checkout.new_flow'",
  "flag_config": {
    "description": "string",
    "default_value": "boolean | string | number",
    "targeting_rules": "object"
  },
  "rollout_percentage": "number (optional, for --rollout) — 0-100"
}
```

## Steps

<mandatory_steps>
1. Validate required input fields per mode; return `blocked` with `missing_fields` if absent
2. Research provider SDK and current feature flag best practices (google_web_search)
3. Execute mode-specific task: scaffold setup / create flag / configure rollout plan
4. Validate flag naming: `{scope}.{feature}` convention required
5. Ensure kill-switch (0% instant rollback) step is included in any rollout plan
6. Return structured result with generated files, flag definitions, and rollout stages
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST use `{scope}.{feature}` naming convention for all flags (e.g., `checkout.new_flow`).
- MUST NOT expose SDK keys inline — reference env vars only (e.g., `process.env.GROWTHBOOK_KEY`).
- MUST define a cleanup plan (lifecycle/cleanup_date) for every created flag.
- MUST recommend server-side evaluation for security-sensitive or payment-related flags.
- MUST validate `rollout_percentage` is between 0 and 100; return `INVALID_ROLLOUT_PERCENTAGE` otherwise.
- MUST include a kill-switch (instant 0% rollback) step in every rollout plan output.
- MUST return `UNSUPPORTED_PROVIDER` if provider is not in the supported list.
- MUST return `FLAG_EXISTS` if `--create` is called with a flag_name that already exists.
- MUST return `blocked` with `missing_fields: ["stack"]` if `--setup` is invoked without `stack`.
- MUST return `blocked` with `missing_fields: ["flag_name", "flag_config"]` if `--create` is invoked without both.
- MUST return `blocked` with `missing_fields: ["flag_name", "rollout_percentage"]` if `--rollout` is invoked without both.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "provider": "string",
    "generated_files": [
      { "path": "string", "purpose": "string" }
    ],
    "install_command": "string",
    "env_vars_required": ["string"],
    "flags": [
      {
        "name": "string",
        "default_value": "boolean | string | number",
        "targeting_rules": "object",
        "cleanup_date": "string (ISO 8601)"
      }
    ],
    "rollout_plan": {
      "current_percentage": "number",
      "stages": [
        {
          "percentage": "number",
          "duration": "string",
          "success_criteria": "string"
        }
      ]
    }
  },
  "summary": "one sentence describing what was configured and the rollout strategy",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "provider": "growthbook",
    "generated_files": [{ "path": "src/lib/feature-flags.ts", "purpose": "GrowthBook client with TypeScript flag types" }],
    "install_command": "pnpm add @growthbook/growthbook",
    "env_vars_required": ["NEXT_PUBLIC_GROWTHBOOK_API_HOST", "NEXT_PUBLIC_GROWTHBOOK_CLIENT_KEY"],
    "flags": [
      { "name": "new_dashboard", "default_value": false, "targeting_rules": { "percent": 10 }, "cleanup_date": "2026-06-01" }
    ],
    "rollout_plan": {
      "current_percentage": 10,
      "stages": [
        { "percentage": 10, "duration": "2 weeks", "success_criteria": "Error rate < 0.5%" },
        { "percentage": 100, "duration": "after success", "success_criteria": "No regressions" }
      ]
    }
  },
  "summary": "GrowthBook configured with new_dashboard flag at 10% rollout; cleanup date 2026-06-01.",
  "confidence": "high"
}
```
