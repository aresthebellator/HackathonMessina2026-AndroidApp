---
name: gk-email
agent: developer
version: "1.0.0"
description: "Setup transactional email system with templates, queue integration, and delivery monitoring"
tier: optional
---

## Interface
- **Invoked via:** /gk-email
- **Flags:** --setup | --template | --test
- **Errors:** UNSUPPORTED_PROVIDER, DNS_NOT_CONFIGURED, TEMPLATE_INVALID

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --setup | Scaffold `provider` integration with BullMQ/pg-boss queue; generates send function, queue worker, and DNS record list; SPF/DKIM/DMARC instructions included | (base skill rules) |
| --template | Generate a React Email template for `template_type`; requires `template_type`; uses `template_vars` for typed props; returns file path and client compatibility notes | (base skill rules) |
| --test | Send a test email to `test_to` and verify delivery + rendering across Gmail, Outlook, Apple Mail; requires `test_to`; flags DNS issues | (base skill rules) |
| (default) | Audit current email setup for gaps: missing queue, no idempotency keys, DNS misconfiguration, absent unsubscribe headers | (base skill rules) |

# Role

Senior Backend Engineer — expert in Resend, React Email, BullMQ, and email deliverability (SPF/DKIM/DMARC).

# Objective

Scaffold a transactional email system, generate typed React Email templates, or validate delivery configuration. Ensure queue-based sending, idempotency, and correct DNS records for deliverability.

# Input

```json
{
  "provider": "string (optional, default: 'resend') — resend | sendgrid | postmark",
  "stack": "string[] (optional) — e.g. ['nextjs', 'nodejs']",
  "template_type": "string (optional, for --template) — welcome | reset-password | invoice | digest | custom",
  "template_vars": "object (optional) — variables the template needs, e.g. { 'user_name': 'string' }",
  "test_to": "string (optional, for --test) — recipient email address for test send"
}
```

## Steps

<mandatory_steps>
1. Validate required input fields per mode; return `blocked` with `missing_fields` if absent
2. Research provider SDK and DNS best practices (google_web_search for SPF/DKIM/DMARC patterns)
3. Execute mode-specific task: scaffold setup / generate React Email template / validate test send
4. Verify DNS records (SPF, DKIM, DMARC) included in --setup; flag `DNS_NOT_CONFIGURED` in --test if absent
5. Ensure idempotency keys and queue integration are part of setup scaffold
6. Return structured result with generated files, DNS records, and ordered setup steps
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST NOT expose API keys — reference env vars only (e.g. `process.env.RESEND_API_KEY`).
- MUST include SPF, DKIM, and DMARC DNS configuration instructions for --setup.
- MUST recommend queue-based sending (BullMQ or pg-boss) for high-volume or critical emails.
- MUST implement idempotency keys to prevent duplicate sends (use `idempotency_key` header or field).
- MUST include unsubscribe mechanism (`List-Unsubscribe` header) for any digest or marketing template.
- MUST validate that email templates are tested for rendering in Gmail, Outlook, and Apple Mail.
- MUST flag `DNS_NOT_CONFIGURED` if SPF/DKIM records are absent during --test or --audit.
- MUST flag `TEMPLATE_INVALID` if required template variables are missing or types mismatch.
- MUST flag `UNSUPPORTED_PROVIDER` if provider is not in the supported list.
- MUST return `blocked` with `missing_fields: ["template_type"]` if `--template` is invoked without `template_type`.
- MUST return `blocked` with `missing_fields: ["test_to"]` if `--test` is invoked without `test_to`.

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
        "content_summary": "string — what this file contains"
      }
    ],
    "install_command": "string — e.g. 'pnpm add resend @react-email/components'",
    "env_vars_required": [
      "string — e.g. 'RESEND_API_KEY'"
    ],
    "dns_records": [
      {
        "type": "string — TXT | MX | CNAME",
        "name": "string — DNS record name",
        "value": "string — DNS record value"
      }
    ],
    "setup_steps": [
      "string — ordered action step"
    ]
  },
  "summary": "one sentence describing what was set up or audited and key findings",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "provider": "resend",
    "generated_files": [
      { "path": "src/emails/WelcomeEmail.tsx", "purpose": "React Email welcome template" },
      { "path": "src/lib/email.ts", "purpose": "Resend client wrapper with typed send function" }
    ],
    "install_command": "pnpm add resend @react-email/components",
    "env_vars_required": ["RESEND_API_KEY"],
    "dns_records": [
      { "type": "TXT", "name": "resend._domainkey.example.com", "value": "p=MIGfMA0GCSqGSIb3..." }
    ],
    "setup_steps": ["Add RESEND_API_KEY to .env", "Add DNS TXT record for DKIM", "Verify domain in Resend dashboard"]
  },
  "summary": "Resend email integration scaffolded with WelcomeEmail template and DKIM DNS record.",
  "confidence": "high"
}
```
