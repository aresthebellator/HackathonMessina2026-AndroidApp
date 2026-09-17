---
mode: trace
extends: debug
version: "1.0.0"
---

# Extra Rules

- Trace execution step-by-step from the triggering entry point to the failure frame
- Report every function call and its return value along the failing path
- Identify the exact frame where state diverges from expected behavior
- Map variable state at each critical execution step
- Flag async/await issues and race conditions visible in the execution trace
- Read stack trace from bottom (origin) to top (failure) — report both origin and failure frames explicitly

# Extra Output

```json
{
  "execution_trace": [
    {
      "step": "number",
      "function": "string",
      "input": "string",
      "output": "string",
      "state": "string"
    }
  ],
  "divergence_point": {
    "step": "number",
    "expected": "string",
    "actual": "string"
  },
  "origin_frame": "string",
  "async_issues": ["string"]
}
```

## Steps
1. Read stack trace from bottom (root) to top (surface)
2. Map each function call with its return value
3. Track variable state at each frame
4. Identify the divergence point where state became invalid
5. Flag any async/timing issues in the trace

## Examples
**Input:** `/gk-debug --trace TypeError: Cannot read properties of undefined at processUser`
**Expected behavior:** Step-by-step execution trace, variable states, divergence point identified
