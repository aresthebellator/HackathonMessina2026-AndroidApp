---
name: review-security
description: "Use when reviewing Laravel code for authorization, validation, data exposure, authentication, secrets, file uploads, queues, events, and unsafe input handling."
license: MIT
metadata:
  author: Ali Harb
---

# Review Security

## When To Use

Use this skill for security-focused review or when code touches protected data, authentication, authorization, files, external calls, queues, or APIs.

## Inputs Needed

- Threat model, changed files, actors, assets, trust boundaries, routes, jobs, events, and tests.

## Workflow

1. Identify actors, assets, and trust boundaries.
2. Check authentication and authorization.
3. Check validation and mass assignment.
4. Check output exposure and serialization.
5. Check raw queries, file uploads, redirects, SSRF, secrets, logs, queues, and events.
6. Check rate limits and abuse paths.
7. Verify tests cover denial and malicious input paths.

## Files That May Be Created

- Security tests if fixing issues is requested.

## Files That May Be Modified

- Policies, requests, Actions, Resources, controllers, routes, jobs, events, config, and tests if fixing issues is requested.

## Architecture Rules

- Security checks should be enforced server-side in reusable layers.
- Views may communicate authorization but must not be the only enforcement.

## Testing Requirements

- Test denied users, invalid input, private field exposure, unsafe state transitions, and abuse controls.

## Security Requirements

- Default deny.
- Validate all boundary input.
- Avoid logging secrets.
- Escape output.
- Use safe queries and file handling.

## Review Checklist

- Authorization, validation, mass assignment, serialization, rate limiting, CSRF, escaping, raw SQL, uploads, secrets, logs, queues, events, and external calls.

## Common Mistakes

- Checking only happy-path authorization.
- Returning raw models from APIs.
- Trusting delayed job payloads indefinitely.
