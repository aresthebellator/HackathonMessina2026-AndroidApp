---
mode: metrics
extends: monitor
version: "1.0.0"
---

# Extra Rules
- Collect: CPU %, memory %, p50/p95/p99 latency, error rate, request throughput (RPS)
- Flag anomalies: CPU >80%, memory >85%, p99 latency >2× baseline, error rate >1%
- Compute delta vs previous `time_range` period for trend detection
- Format latency as human-readable: `23ms` not `0.023`
- If metrics endpoint unavailable, attempt to derive from logs (e.g., latency from access log timestamps)

# Thresholds (defaults — override via `thresholds` input)

| Metric | Warning | Critical |
|--------|---------|----------|
| CPU % | 70 | 85 |
| Memory % | 75 | 90 |
| p99 latency | 500ms | 2000ms |
| Error rate | 0.5% | 2% |
| RPS drop | -20% | -50% |

# Extra Output
```json
{
  "metrics_snapshot": {
    "cpu_percent": "number",
    "memory_percent": "number",
    "p50_latency_ms": "number",
    "p95_latency_ms": "number",
    "p99_latency_ms": "number",
    "error_rate_percent": "number",
    "rps": "number"
  },
  "anomalies": [
    { "metric": "string", "value": "string", "threshold": "string", "severity": "warning | critical" }
  ],
  "trend": { "direction": "improving | degrading | stable", "delta_percent": "number" }
}
```

## Steps
1. Fetch metrics from endpoint or derive from logs
2. Compute p50/p95/p99 for latency
3. Compare each metric against thresholds
4. Compute trend vs previous period
5. Report anomalies with severity

## Examples
**Input:** `/gk-monitor --metrics`
**Expected behavior:** Snapshot with p99=1200ms (WARNING), error_rate=0.8% (WARNING), trend=degrading (-15% RPS)
