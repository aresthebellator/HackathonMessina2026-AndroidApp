---
mode: deep
extends: analyze
version: "1.0.0"
---

# Extra Rules

- Set depth to "deep" automatically — override surface defaults
- Trace all call chains to their origin, not just direct callers
- Map all transitive dependencies, not just direct imports
- Identify hidden coupling between modules that share no explicit import
- Flag all circular dependency chains as critical-severity structural risks
- Count unique call paths and report the longest chain length
- Detect dead code reachable only through deprecated or removed entry points

# Extra Output

```json
{
  "call_graph": [
    {
      "from": "string",
      "to": "string",
      "path_length": "number"
    }
  ],
  "transitive_deps": ["string"],
  "longest_call_chain": "number",
  "circular_chains": [["string"]],
  "hidden_coupling": ["string"]
}
```

## Steps
1. Set analysis depth to deep
2. Trace all call chains from entry points
3. Map transitive dependencies (2+ levels)
4. Detect circular dependency chains
5. Report longest chain and hidden coupling

## Examples
**Input:** `/gk-analyze --deep src/api/`
**Expected behavior:** Full call graph, transitive deps map, circular chain detection, coupling report
