---
mode: alerts
extends: monitor
version: "1.0.0"
---

# Extra Rules
- Check active alerts from: log ERROR spike, metric threshold breach, health check failure
- Classify alert severity: P1 (service down) > P2 (degraded) > P3 (warning)
- For each active alert: state trigger, duration, affected component, recommended action
- Correlate alerts fired within the same 5-minute window as likely related
- MUST NOT silence or acknowledge alerts — read-only analysis only
- If no alert system configured, derive alerts from current log + metric anomalies

# Alert Classification

| P Level | Condition | Example |
|---------|-----------|---------|
| P1 | Service unavailable; error rate >10% | DB connection failed, OOM kill |
| P2 | Degraded performance; error rate 2-10% | p99 >2s, memory >90% |
| P3 | Anomaly detected; proactive warning | p95 >500ms, slow query detected |

# Extra Output
```json
{
  "active_alerts": [
    {
      "id": "string",
      "severity": "P1 | P2 | P3",
      "title": "string",
      "triggered_at": "string",
      "duration": "string",
      "component": "string",
      "recommended_action": "string"
    }
  ],
  "correlated_groups": [["string — alert IDs likely from same root cause"]],
  "alert_count": { "P1": "number", "P2": "number", "P3": "number" }
}
```

## Steps
1. Read active alerts from system or derive from log/metric anomalies
2. Classify each by severity (P1/P2/P3)
3. Correlate alerts fired within same time window
4. Recommend action for each active alert
5. Return prioritized alert list

## Examples
**Input:** `/gk-monitor --alerts`
**Expected behavior:** P1: "DB connection pool exhausted (triggered 4m ago)", P3: "Memory usage trending up (78%)" with recommended actions
