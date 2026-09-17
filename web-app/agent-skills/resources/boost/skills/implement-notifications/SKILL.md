---
name: implement-notifications
description: "Use when adding Laravel notifications across mail, database, broadcast, Slack, or custom channels."
license: MIT
metadata:
  author: Ali Harb
---

# Implement Notifications

## Workflow

1. Identify recipient, trigger event, channels, queue needs, and unsubscribe/privacy rules.
2. Create notification classes with localized copy and typed data.
3. Queue notifications when delivery may be slow.
4. Avoid including secrets or excessive personal data.
5. Add tests using notification fakes.

## Verification

- Notification tests assert recipient, channel, and payload.
