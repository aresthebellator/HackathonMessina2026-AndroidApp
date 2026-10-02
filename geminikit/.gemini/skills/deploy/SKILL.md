---
name: gk-deploy
agent: devops
version: "2.0.0"
tier: optional
description: "Execute build and deployment pipelines to various environments"
---

## Tools
- `run_shell_command` — execute build commands, health checks, and deployment scripts
- `read_file` — read deployment configs, environment files, and Dockerfiles before deploying
- `google_web_search` — look up platform-specific deployment docs or troubleshoot errors

## Interface
- **Invoked via:** /gk-deploy
- **Flags:** --staging | --production | --dry-run
- **Errors:** BUILD_FAILED, DEPLOY_TARGET_UNREACHABLE, AUTH_ERROR

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --staging | Deploy to staging environment | ./references/staging.md |
| --production | Deploy to production environment | ./references/production.md |
| --dry-run | Simulation deployment without changes | ./references/dry-run.md |
| (default) | Standard deployment pipeline | (base skill rules) |

# Role

Senior Release Engineer

# Objective

Automate the build, packaging, and deployment process to ensure consistent and reliable releases.

# Input

```json
{
  "target": "string (required) — staging | production",
  "version": "string (required) — semantic version or git commit hash",
  "context": {
    "build_command": "string",
    "artifacts_path": "string",
    "secrets": ["string — names of secrets required"]
  }
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read all environment configs, Dockerfiles, and CI/CD configs before deploying — prevents config drift errors
- **Google Search:** Use for platform-specific deployment guides (Vercel, GCP, AWS, Fly.io) and troubleshooting
- **Code Execution:** N/A — use `run_shell_command` for actual deployment steps

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `target` or `version` missing | Ask user to specify environment and version |
| FAILED | BUILD_FAILED | Report build error; do NOT deploy; suggest fix |
| FAILED | DEPLOY_TARGET_UNREACHABLE | Verify credentials and network; provide rollback command |
| FAILED | AUTH_ERROR | Report auth failure; do NOT expose secrets in output |

# Rules

- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)

<deploy_safety_rules>
**HARD GATES — deployment is BLOCKED if any fail:**
1. Build MUST succeed before deployment attempt
2. Dry-run or staging verification MUST pass before production deploy
3. Rollback strategy MUST be defined in deployment plan
4. Secrets MUST NOT appear in logs, output, or deployment artifacts
</deploy_safety_rules>

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "deployment_url": "string (optional)",
    "logs": "string — summarized build/deploy logs",
    "rollback_command": "string"
  },
  "summary": "one sentence describing the deployment status",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "deployment_url": "https://staging.myapp.com",
    "logs": "Build succeeded in 42s. 3 containers started. Health check passed (/healthz → 200).",
    "rollback_command": "fly deploy --image registry.fly.io/myapp:v1.8.2"
  },
  "summary": "v1.8.3 deployed to staging successfully; rollback command provided.",
  "confidence": "high"
}
```

**Example (failed):**
```json
{
  "status": "failed",
  "result": {
    "deployment_url": null,
    "logs": "Build failed: TypeScript error in src/routes/posts.ts:42 — Type 'string' not assignable to 'number'.",
    "rollback_command": null
  },
  "summary": "Deployment aborted: build failed due to TypeScript type error; fix before retrying.",
  "confidence": "high"
}
```
