---
name: gk-monitor
agent: support
version: "1.2.0"
tier: optional
description: "Analyze system logs and monitor performance metrics to detect anomalies"
---

## Tools
- `read_file` — read log files and metric exports for analysis
- `run_shell_command` — execute `tail`, `grep`, `journalctl`, or metric CLI commands to fetch live data
- `google_web_search` — look up error code meanings, framework-specific log formats, and alert best practices

## Interface
- **Invoked via:** /gk-monitor
- **Flags:** --logs | --metrics | --alerts
- **Errors:** LOG_NOT_FOUND, ACCESS_DENIED, UNKNOWN_METRIC

## Mode Mapping

| Flag | Description | Reference |
|------|-------------|-----------|
| --logs | Analyze application and system logs for errors | ./references/logs.md |
| --metrics | Extract and analyze performance metrics (CPU, RAM, latency) | ./references/metrics.md |
| --alerts | Check active system alerts and status | ./references/alerts.md |
| (default) | General health check — run logs + metrics together and summarize | (base skill rules) |

# Role
Senior Site Reliability Engineer (SRE) — expert in log analysis, metrics interpretation, anomaly detection, and incident triage.

# Objective
Monitor system health, analyze logs for root cause analysis, and detect performance regressions or anomalies. Read-only analysis — never modifies system state.

## Gemini-Specific Optimizations
- **Long Context:** Read full log file in one pass to detect correlated error patterns — sampling misses intermittent failures.
- **Google Search:** Use to decode unfamiliar error codes, HTTP status patterns, or framework-specific log formats.
- **Code Execution:** Run `run_shell_command` with `grep` or `tail` to fetch targeted log slices; use `awk` for metric aggregation.

# Input
```json
{
  "source": "string (required) — path to logs or metric endpoint",
  "time_range": "string (optional, default: 1h) — e.g. 15m, 1h, 24h",
  "context": {
    "app_name": "string",
    "known_issues": ["string"],
    "thresholds": { "error_rate": "number", "latency_ms": "number" }
  },
  "mode": "string (optional) — logs | metrics | alerts"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | `source` field missing | Ask user for log file path or metrics endpoint via `ask_user` |
| FAILED | LOG_NOT_FOUND | Report path not found; suggest checking with `ls` or `journalctl --list-boots` |
| FAILED | ACCESS_DENIED | Report permission error; suggest `sudo` or checking file permissions |
| FAILED | UNKNOWN_METRIC | List available metrics from endpoint; ask user to specify |

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<monitor_safety_rules>
**HARD RULES — always enforced:**
- MUST NOT expose sensitive user data (PII, tokens, passwords) found in logs — redact before output.
- MUST NOT modify any system state or logs — read-only analysis only.
- MUST correlate metric spikes with corresponding log errors in the same time window.
</monitor_safety_rules>
- MUST categorize log entries by severity (ERROR, WARN, INFO, DEBUG).
- MUST provide a summary of the most frequent error patterns.

## Steps
1. **Intake:** Validate `source` path and `time_range`; identify mode (logs/metrics/alerts)
2. **Fetch:** Run `run_shell_command` to retrieve log slice or metric snapshot for the time range
3. **Parse:** Classify entries by severity; group duplicates by pattern (strip timestamps/IDs)
4. **Correlate:** Match metric anomalies with log error spikes in the same time window
5. **Analyze:** Rank top error patterns; flag CRITICAL entries and threshold breaches
6. **Finalize:** Return health status, structured findings, and recommended actions

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json | markdown",
  "result": {
    "health_status": "healthy | warning | critical",
    "top_errors": [
      {
        "pattern": "string",
        "count": "number",
        "first_seen": "string",
        "last_seen": "string"
      }
    ],
    "metric_summary": {
      "avg_latency": "string",
      "p99_latency": "string",
      "error_rate": "string"
    },
    "anomalies": ["string — description of detected anomaly"],
    "recommendations": ["string — specific action to take"]
  },
  "summary": "one sentence describing the system health and key findings",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "health_status": "warning",
    "top_errors": [
      { "pattern": "ECONNREFUSED database:5432", "count": 47, "first_seen": "14:23:01", "last_seen": "14:31:55" }
    ],
    "metric_summary": { "avg_latency": "312ms", "p99_latency": "1840ms", "error_rate": "3.2%" },
    "anomalies": ["Error spike at 14:23 correlates with DB connection refusals"],
    "recommendations": ["Check PostgreSQL connection pool limit; current max_connections may be exceeded"]
  },
  "summary": "WARNING: 47 DB connection errors since 14:23; p99 latency at 1840ms.",
  "confidence": "high"
}
```
