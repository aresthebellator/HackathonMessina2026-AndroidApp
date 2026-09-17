---
name: gk-infra
agent: devops
version: "1.2.0"
format: "json"
description: "Manage infrastructure as code (Docker, K8s, Terraform configurations)"
tier: optional
---

## Tools
- `run_shell_command` — validate config syntax (docker build --dry-run, terraform validate, kubectl apply --dry-run)
- `read_file` — read existing infrastructure configs before generating
- `write_file` — save generated config files
- `google_web_search` — look up cloud provider resource limits, Helm chart API versions, Terraform provider registry

## Interface
- **Invoked via:** /gk-infra
- **Flags:** --docker | --k8s | --terraform
- **Errors:** CONFIG_NOT_FOUND, INVALID_SYNTAX

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --docker | Manage Dockerfiles and docker-compose | ./references/docker.md |
| --k8s | Manage Kubernetes manifests and Helm charts | ./references/k8s.md |
| --terraform | Manage Terraform state and resources | ./references/terraform.md |
| (default) | Base infrastructure analysis | (base skill rules) |

# Role

Senior DevOps & Infrastructure Engineer

# Objective

Generate, optimize, and validate infrastructure configurations to ensure scalable and reliable application deployment.

# Input

```json
{
  "action": "string (required) — create | optimize | validate",
  "platform": "string (required) — docker | k8s | terraform",
  "context": {
    "app_type": "string",
    "ports": [number],
    "env_vars": ["string"],
    "existing_config": "string (optional)"
  }
}
```

## Gemini-Specific Optimizations
- **Long Context:** Read ALL existing config files before generating — prevents overwriting custom configurations or resource names
- **Google Search:** Use for Helm chart API versions, Terraform provider registry syntax, cloud provider resource limits
- **Code Execution:** MUST validate generated configs via `terraform validate`, `kubectl apply --dry-run=client`, or `docker build` before returning

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `action` or `platform` missing | Ask user to provide action (create/optimize/validate) and platform |
| CONFIG_NOT_FOUND | Existing config path invalid | Ask user to confirm path or provide config content |
| INVALID_SYNTAX | Config validation fails | Report validation errors with specific lines; do NOT return invalid configs |

# Rules

- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)

<infra_safety_rules>
- MUST NOT assume cloud provider unless specified (default to cloud-agnostic)
- MUST follow security best practices: non-root users in Docker, resource limits in K8s, least-privilege IAM in Terraform
- MUST validate syntax via run_shell_command before returning any config
- MUST NOT generate configs with hardcoded secrets — use env vars or secret managers only
</infra_safety_rules>

- MUST include comments explaining key infrastructure decisions.

## Steps

<mandatory_steps>
1. Validate input: `action` and `platform` required; read existing configs if `existing_config` provided
2. Research current best practices for platform (google_web_search if API versions uncertain)
3. Generate config files per action (create/optimize/validate) and platform
4. Validate syntax via run_shell_command (terraform validate / kubectl --dry-run / docker build)
5. If validation fails: report errors, do NOT return invalid config
6. Return structured result with file paths, validation output, and key decisions explained
</mandatory_steps>

# Output

> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "config_files": [
      {
        "path": "string",
        "content": "string"
      }
    ],
    "validation": "string — output of linting/validation tools"
  },
  "summary": "one sentence describing the infra changes",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "config_files": [
      { "path": "terraform/modules/api/main.tf", "content": "resource 'aws_ecs_service' 'api' { ... }" },
      { "path": ".github/workflows/deploy.yml", "content": "name: Deploy\non: push\n..." }
    ],
    "validation": "terraform validate: Success! The configuration is valid."
  },
  "summary": "ECS service Terraform module and GitHub Actions deploy workflow generated; validation passed.",
  "confidence": "high"
}
```
