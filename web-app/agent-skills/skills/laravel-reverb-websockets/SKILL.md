---
name: laravel-reverb-websockets
description: "Use when setting up, configuring, or debugging real-time broadcasting and websockets using Laravel Reverb and Echo."
license: MIT
metadata:
  author: Ali Harb
---

# Laravel Reverb Websockets

## When To Use

Use this skill when implementing real-time updates, chat systems, live notifications, or dashboard stat updates using Laravel Reverb and Laravel Echo.

## Workflow

1. **Verify installation**: Ensure Reverb configuration exists (`config/reverb.php`).
2. **Channel definition**: Specify broadcast channels and authorized access rules inside `routes/channels.php`.
3. **Configure the Event**: Ensure event implements `ShouldBroadcast` or `ShouldBroadcastNow`.
4. **Trigger Event**: Broadcast the event safely via standard event dispatches.
5. **Echo Client Integration**: Setup frontend Echo client listener bindings for private/presence channels.

## Examples

### Good Example

```php
// app/Events/MessageSent.php
namespace App\Events;

use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Queue\SerializesModels;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Broadcasting\PrivateChannel;

final class MessageSent implements ShouldBroadcast
{
    use Dispatchable, SerializesModels;

    public function __construct(public string $message, public int $userId) {}

    public function broadcastOn(): array
    {
        return [
            new PrivateChannel("chat.user.{$this->userId}"),
        ];
    }
}
```

```php
// routes/channels.php
Broadcast::channel('chat.user.{id}', function (User $user, int $id) {
    return $user->id === $id;
});
```

### Bad Example

```php
// ❌ Using public broadcast channels for sensitive data, or using custom HTTP calls for real-time updates.
public function triggerUpdate(Request $request) {
    Http::post('http://localhost:3000/trigger', $request->all());
}
```
