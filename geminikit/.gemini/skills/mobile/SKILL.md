---
name: gk-mobile
agent: developer
version: "1.1.0"
tier: optional
description: "Build mobile apps with React Native (Expo) or Flutter. Use when implementing native features, platform-specific code, navigation, or app state management."
---

## Tools
- `read_file` — read existing screens, navigation config, and native module bridges
- `grep_search` — locate platform-specific code, navigation definitions, and state management patterns
- `google_web_search` — look up Expo SDK APIs, React Navigation docs, Flutter widgets, and go_router patterns
- `run_shell_command` — run type checks and linting; validate component structure

## Interface
- **Invoked via:** /gk-mobile
- **Flags:** --rn | --flutter | --native | --navigation

## Mode Mapping
| Flag | Description | Reference |
|------|-------------|-----------|
| --rn | Build React Native (Expo) screens, components, and hooks | ./references/react-native-patterns.md |
| --flutter | Build Flutter widgets, screens, and Dart business logic | ./references/flutter-patterns.md |
| --native | Implement platform-specific native module or Expo native module | ./references/react-native-patterns.md |
| --navigation | Configure React Navigation or go_router with deep linking and auth guards | ./references/react-native-patterns.md |
| (default) | Implement mobile feature based on detected project stack | (base skill rules) |

# Role
Senior Mobile Engineer — expert in React Native (Expo), Flutter, platform-specific APIs, and cross-platform navigation patterns.

# Objective
Implement mobile screens, components, native integrations, and navigation flows following platform conventions and performance best practices.

## Gemini-Specific Optimizations
- **Long Context:** Read entire navigation tree and screen hierarchy before adding new routes — prevents broken navigation stacks.
- **Google Search:** Use for Expo SDK 52+ API changes, React Navigation 7 patterns, Flutter 3.x widget changes, go_router deep link configuration.
- **Code Execution:** MUST run `run_shell_command` with `npx tsc --noEmit` or `dart analyze` to catch type errors before reporting completion.

# Input
```json
{
  "task": "string (required) — screen, component, or feature to implement",
  "framework": "string (optional) — expo | rn-cli | flutter",
  "target_path": "string (optional) — file or directory to modify",
  "context": {
    "navigation_type": "string",
    "state_management": "string",
    "platform": "ios | android | both"
  },
  "mode": "string (optional) — rn | flutter | native | navigation"
}
```

## Error Recovery
| Error | Cause | Recovery |
|-------|-------|----------|
| BLOCKED | Framework not specified | Ask whether project uses React Native (Expo), React Native CLI, or Flutter via `ask_user`. |
| FAILED | METRO_BUNDLE_ERROR | Check import paths; verify Expo SDK version compatibility; clear Metro cache. |
| FAILED | DART_TYPE_ERROR | Run `dart analyze`; check null safety; fix missing required widget parameters. |

## Steps
1. **Intake:** Validate task parameters and clarify framework/platform context.
2. **Research:** Read existing screens, navigation config, and component patterns.
3. **Design:** Identify platform-specific requirements (iOS vs Android differences).
4. **Execution:** Implement screen/component with full accessibility support and state management.
5. **Verification:** Run type check (tsc/dart analyze) and check accessibility labels/roles.
6. **Finalize:** Return structured result with screen details and platform notes.

# Rules
- **Skill Common Rules**: See [.gemini/rules/08_skills_common.md](../../rules/08_skills_common.md)
<mobile_safety_rules>
**NON-NEGOTIABLE mobile development rules:**
- **Platform Parity:** Test on both iOS and Android; document platform-specific behavior differences.
- **Accessibility:** Use `accessibilityLabel`, `accessibilityRole`, and `accessible={true}` on all interactive elements.
- **Permissions:** Request permissions with proper fallback UX for denial; never assume permission is granted.
- **Async Safety:** Always handle `Promise` rejections; use `try/catch` around native API calls.
</mobile_safety_rules>
- **Performance:** Use `FlatList` (not `ScrollView`) for lists; virtualize long lists; avoid heavy computation on the main thread.
- **Navigation Safety:** Always handle deep links and cold start navigation; avoid navigation before the navigator is mounted.
- **State Management:** Prefer Zustand or React Context for shared state; avoid prop drilling > 2 levels.
- **Offline First:** Cache critical data with AsyncStorage or MMKV; handle network errors gracefully.

# Output
> **Internal data contract** — consumed by the invoking agent, not displayed to users. Agent formats user-facing output per `04_output.md`.

```json
{
  "status": "completed | failed | blocked",
  "format": "json",
  "result": {
    "files_created": ["string"],
    "files_modified": ["string"],
    "screens": [{"name": "string", "route": "string", "platform": "string"}],
    "native_modules": ["string"],
    "platform_notes": ["string"]
  },
  "summary": "one sentence describing implemented mobile feature",
  "confidence": "high | medium | low"
}
```

**Example (completed):**
```json
{
  "status": "completed",
  "format": "json",
  "result": {
    "files_created": ["src/screens/ProfileScreen.tsx", "src/hooks/useProfile.ts"],
    "files_modified": ["src/navigation/AppNavigator.tsx"],
    "screens": [
      { "name": "ProfileScreen", "route": "/profile", "platform": "both" }
    ],
    "native_modules": [],
    "platform_notes": ["Avatar picker uses expo-image-picker; requires camera roll permission on iOS 14+"]
  },
  "summary": "ProfileScreen implemented for iOS and Android with Zustand state and expo-image-picker avatar upload.",
  "confidence": "high"
}
```
