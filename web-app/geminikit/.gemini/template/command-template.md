---
# Command Template
# Replace [command-name], [skill-name], [flags], [target] with actual values.
# Remove ## Available Modes section if skill has no modes.
---

description = "Usage: /gk-[command-name] [flags] [target]"

prompt = """
## Task
{{args}}

## Base Skill
@{../skills/[skill-name]/SKILL.md}

## Available Modes (apply the mode matching any flag in the task above)
@{../skills/[skill-name]/references/[mode1].md}
@{../skills/[skill-name]/references/[mode2].md}
"""
