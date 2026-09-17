---
mode: logs
extends: monitor
version: "1.0.0"
---

# Extra Rules
- Parse log entries by severity: ERROR > WARN > INFO > DEBUG
- Group duplicate errors by pattern (strip timestamps, IDs, file paths from pattern key)
- Report top 10 error patterns ranked by frequency
- Flag any CRITICAL/FATAL entries immediately regardless of count
- Extract stack traces and correlate with source file locations if available
- Detect log volume spikes: >2× baseline in any 5-minute window = anomaly

# Extra Output
```json
{
  "severity_breakdown": { "ERROR": "number", "WARN": "number", "INFO": "number" },
  "top_errors": [
    {
      "pattern": "string",
      "count": "number",
      "first_seen": "string",
      "last_seen": "string",
      "sample": "string — one raw log line"
    }
  ],
  "volume_spikes": [{ "timestamp": "string", "rate": "string" }],
  "critical_entries": ["string"]
}
```

## Steps
1. Read log file(s) from `source` path
2. Parse entries by severity and timestamp
3. Group by pattern (deduplicated)
4. Rank by frequency; extract top 10
5. Detect volume spikes and CRITICAL entries
6. Return structured breakdown

## Examples
**Input:** `/gk-monitor --logs /var/log/app.log`
**Expected behavior:** Top 10 error patterns with count and first/last seen; flags any CRITICAL; reports spike at 14:32
