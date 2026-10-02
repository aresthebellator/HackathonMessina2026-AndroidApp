---
mode: auto
extends: ui
version: "1.0.0"
---

# Extra Rules
- **Autonomy**: Execute end-to-end UI generation (Tokens -> Code -> Audit) without intermediate steps.
- **Code Generation**: Produce production-ready UI code (Tailwind/CSS) directly in the session.
- **Self-Audit**: Automatically run a "Virtual Review" against WCAG 2.2 after code generation.
- **Verification**: Must provide a "Summary of Correctness" (Contrast scores, touch target sizes).
- **Tech Stack**: Default to React + Tailwind unless specified otherwise.

# Extra Output
```json
{
  "auto_pilot": {
    "generated_files": [{"path": "string", "content": "string"}],
    "tokens": "object",
    "audit_results": {
      "contrast_score": "string",
      "accessibility_passed": "boolean",
      "issues_fixed": ["string"]
    }
  }
}
```

## Steps
1. Parse requirement and generate Design Tokens
2. Generate full UI component/page code
3. Execute internal Audit (Contrast, Spacing, Accessibility)
4. Fix any detected high-severity issues automatically
5. Present final code and validation report to user

## Examples
**Input:** `/gk-design --auto Create a responsive glassmorphism login form`
**Expected behavior:** Generates code, verifies accessibility, and reports success.
