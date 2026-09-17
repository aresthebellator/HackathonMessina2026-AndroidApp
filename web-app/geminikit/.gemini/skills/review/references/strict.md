---
mode: strict
extends: review
version: "1.0.0"
---

# Extra Rules

- Set threshold to "zero-tolerance" — flag all minor style issues, naming inconsistencies, and optional improvements.
- Mandatory check for unit test coverage on all new or modified functions.
- Enforce strict adherence to architectural patterns (e.g., Clean Architecture, SOLID, DRY) without exception.
- API Review: All endpoints MUST have schema validation, explicit status codes for ALL error conditions, and comprehensive documentation.
- Flag any use of `any` (TypeScript), `dynamic` (other languages), or missing type definitions as critical errors.
- Reject any code with even minor complexity risks or potential for over-engineering.

# Extra Output

```json
{
  "strict_compliance_score": "number (0-100)",
  "violations": {
    "architectural": ["string"],
    "style_minor": ["string"],
    "test_coverage_gap": ["string"]
  }
}
```

## Steps
1. Set review threshold to strict
2. Scan for architectural pattern violations
3. Evaluate minor style and naming issues
4. Check test coverage for all modified units
5. Validate full API schema compliance

## Examples
**Input:** `/gk-review --strict src/auth.ts`
**Expected behavior:** Comprehensive list of all issues, including minor style and naming; strict architectural validation.
