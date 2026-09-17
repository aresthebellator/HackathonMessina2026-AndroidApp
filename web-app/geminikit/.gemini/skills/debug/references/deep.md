---
mode: deep
extends: debug
version: "1.0.0"
---

# Extra Rules

- Analyze across multiple layers: application, framework, runtime, OS — report which layers were examined
- Investigate transient causes: race conditions, memory corruption, timing dependencies, flaky state
- Check for interaction effects between concurrent or async operations
- Analyze system state context at failure time: memory pressure, file descriptors, thread count
- Provide a minimal reproduction case that isolates the bug to its smallest reproducible form
- Rank all possible causes by likelihood with supporting evidence for each

# Extra Output

```json
{
  "layers_analyzed": ["application|framework|runtime|os"],
  "transient_cause": "boolean",
  "reproduction_case": "string",
  "system_state": {
    "memory": "string",
    "concurrency": "string",
    "file_descriptors": "string"
  },
  "interaction_effects": ["string"],
  "ranked_causes": [
    {
      "cause": "string",
      "likelihood": "high|medium|low",
      "evidence": "string"
    }
  ]
}
```

## Steps
1. Analyze issue across all layers (app → framework → runtime → OS)
2. Check for transient causes (race conditions, memory pressure)
3. Inspect system state at time of failure
4. Find interaction effects between components
5. Produce minimal reproducible case
6. Rank root causes by likelihood

## Examples
**Input:** `/gk-debug --deep memory usage grows continuously in background worker`
**Expected behavior:** Multi-layer analysis, interaction effects, minimal repro case, ranked cause list
