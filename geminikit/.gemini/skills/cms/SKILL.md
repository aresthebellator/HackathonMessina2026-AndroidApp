---
name: gk-cms
agent: developer
version: "1.0.0"
description: "Integrate headless CMS (Sanity, Strapi, Payload), define content schemas, and generate typed data fetching queries"
tier: optional
---

## Interface
- **Invoked via:** /gk-cms
- **Flags:** --setup | --schema | --query
- **Errors:** UNSUPPORTED_CMS, SCHEMA_INVALID, API_UNREACHABLE

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --setup | Install and configure `cms` for `stack`; generates typed client, env vars, and ISR webhook config; requires `stack` for framework-specific setup | (base skill rules) |
| --schema | Generate content type schema from `content_type` natural-language description; requires `content_type`; returns schema definition with SEO fields and draft/publish status | (base skill rules) |
| --query | Generate typed data fetching query from `query_description`; requires `query_description`; returns GROQ (Sanity) or REST/GraphQL query with TypeScript response type | (base skill rules) |
| (default) | Audit current CMS integration for gaps: missing TypeScript types, absent ISR config, no error handling, unconfigured cache tags | (base skill rules) |

# Role

Senior Full-Stack Engineer — expert in Sanity, Strapi, Payload CMS, Contentful, GROQ query language, Next.js ISR.

# Objective

Integrate headless CMS into projects with correct client configuration, typed content schemas, and optimized data fetching queries with ISR revalidation.

# Input

```json
{
  "cms": "string (optional, default: 'sanity') — sanity | strapi | payload | contentful",
  "stack": "string[] (optional) — e.g. ['nextjs', 'react']",
  "content_type": "string (optional, for --schema) — describe the content type in natural language",
  "query_description": "string (optional, for --query) — describe what data to fetch",
  "project_id": "string (optional, for Sanity) — Sanity project ID"
}
```

## Steps

<mandatory_steps>
1. Validate required input fields per mode; return `blocked` with `missing_fields` if absent
2. Research CMS SDK version and Next.js ISR/cache tag patterns (google_web_search if needed)
3. Execute mode-specific task: scaffold setup / generate schema / generate typed query
4. Validate content schema has required SEO fields (title, description, slug)
5. Configure ISR revalidation and cache tags for Next.js integrations
6. Return structured result with generated files, install command, and setup steps
</mandatory_steps>

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- MUST NOT expose CMS API tokens — use env var references only
- MUST configure cache tags and on-demand ISR revalidation for Next.js integrations
- MUST generate TypeScript types alongside queries
- MUST validate content schemas have required SEO fields (title, description, slug)
- MUST use CDN API URLs (not write tokens) for read-only client configuration
- MUST recommend draft/publish workflow for editorial content types
- MUST return `blocked` with `missing_fields: ["content_type"]` if `--schema` is invoked without `content_type`
- MUST return `blocked` with `missing_fields: ["query_description"]` if `--query` is invoked without `query_description`
- MUST return `UNSUPPORTED_CMS` if `cms` is not in the supported list
- MUST return `API_UNREACHABLE` if `project_id` is provided but the CMS API cannot be reached

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.


```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "cms": "string — selected CMS",
    "generated_files": [
      { "path": "string", "purpose": "string" }
    ],
    "install_command": "string",
    "env_vars_required": ["string"],
    "content_schema": {
      "name": "string — content type name",
      "fields": [{ "name": "string", "type": "string", "required": "boolean", "description": "string" }],
      "seo_fields": ["string — field names covering title, description, slug"]
    },
    "query": "string — data fetching query for --query mode",
    "setup_steps": ["string"],
    "audit_gaps": ["string — identified gaps for default mode"]
  },
  "summary": "one sentence describing CMS integration status and key findings",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "provider": "contentful",
    "generated_files": [{ "path": "src/lib/contentful.ts", "purpose": "Contentful client + typed query helpers" }],
    "content_types": [
      { "name": "BlogPost", "fields": [{ "name": "title", "type": "Symbol", "required": true, "description": "Post title" }], "seo_fields": ["slug", "metaDescription"] }
    ],
    "query": "const posts = await contentfulClient.getEntries({ content_type: 'blogPost' })",
    "setup_steps": ["Add CONTENTFUL_SPACE_ID and CONTENTFUL_ACCESS_TOKEN to .env"],
    "audit_gaps": []
  },
  "summary": "Contentful integration scaffolded with BlogPost content type and typed client.",
  "confidence": "high"
}
```
