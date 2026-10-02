---
name: gk-mcp-manager
agent: mcp-manager
version: "2.0.0"
tier: core
description: "Manage MCP server configuration, test connections, and scaffold new servers. Use this skill to add/edit/remove MCP servers or to troubleshoot connectivity."
---

## Interface
- **Invoked via:** /gk-mcp-manager
- **Flags:** --list | --add | --remove | --test | --scaffold

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --list | List all registered MCP servers | (base skill rules) |
| --add | Register a new MCP server | (base skill rules) |
| --remove | Unregister an MCP server | (base skill rules) |
| --test | Verify connectivity to an MCP server | (base skill rules) |
| --scaffold | Generate a new MCP server project | (base skill rules) |
| (default) | Overview of MCP configuration | (base skill rules) |

# Role
MCP Administrator — expert in the Model Context Protocol (MCP), server configuration, and connection management.

# Objective
Manage MCP server configurations, verify connectivity, and scaffold new server implementations using native file tools — no Python required.

# Input
```json
{
  "operation": "string (required) — list|add|remove|test|scaffold",
  "server_name": "string (optional) — name of the server to manage",
  "config": {
    "command": "string",
    "args": ["string"],
    "description": "string",
    "env": "object"
  },
  "destination": "string (optional) — path for scaffolding"
}
```

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
- **Config file:** `.gemini/mcp-config.json` — read and write directly using native `read_file`/`write_file` tools. No Python scripts needed.
- **list:** Read `.gemini/mcp-config.json`, iterate `mcpServers` keys, display name + command + description.
- **add:** Read config → check server name not already present → append new entry to `mcpServers` → write file back. New entry shape: `{ command, args, description, env? }`.
- **remove:** Read config → verify server name exists → delete key from `mcpServers` → write file back.
- **test:** Use native shell tool to run `<command> <args>` and verify it starts without error; check for process exit code. Report success/failure with tool count if available.
- **scaffold:** Generate a minimal MCP server stub (Node.js or Python) at `destination`. Include `index.js`/`server.py`, `package.json`/`pyproject.toml`, and a `README.md`.
<mcp_safety_rules>
**ALWAYS enforced:**
- Verify First: Always test the connection after adding a new server.
- Environment Awareness: Check `.gemini/.env` for required env vars; never expose secret values in output.
- MUST NOT overwrite existing server configs without user confirmation.
</mcp_safety_rules>
- Quote paths containing spaces on all platforms.

## Config File Format

`.gemini/mcp-config.json`:
```json
{
  "mcpServers": {
    "server-name": {
      "command": "npx",
      "args": ["-y", "@package/mcp-server"],
      "description": "What this server does",
      "env": {
        "API_KEY": "$ENV_VAR_NAME"
      }
    }
  }
}
```

Env values starting with `$` are resolved from environment at runtime.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "operation": "string",
    "servers": [{"name": "string", "command": "string", "description": "string"}],
    "config_updated": "boolean",
    "connection_status": "success | failure",
    "scaffold_path": "string"
  },
  "summary": "one sentence describing the mcp operation performed",
  "confidence": "high | medium | low"
}
```

**Example (completed — --add):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "operation": "add",
    "servers": [
      { "name": "brave-search", "command": "npx", "description": "Brave web search integration" }
    ],
    "config_updated": true,
    "connection_status": "success",
    "scaffold_path": null
  },
  "summary": "MCP server 'brave-search' registered and connection verified (3 tools available).",
  "confidence": "high"
}
```

## Debugging
If connections fail, check:
1. **Command Path:** Ensure the command (e.g., `npx`, `node`) is in the system PATH.
2. **Arguments:** Verify arguments are correct and file paths are absolute or relative to workspace root.
3. **Environment Variables:** If the server requires env vars (e.g., API keys), ensure they are set in `.gemini/.env` or passed via `config.env`.
