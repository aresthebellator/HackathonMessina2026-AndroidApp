---
name: schedule-artisan-task
description: "Use when scheduling daily jobs, cleanups, statistics generation, or configuring Laravel scheduler options."
license: MIT
metadata:
  author: Ali Harb
---

# Schedule Artisan Task

## When To Use

Use this skill when introducing cron-like tasks, scheduling cleanups, syncing data on intervals, or testing scheduler rules.

## Workflow

1. **Locate schedule entrypoint**: Open `routes/console.php`.
2. **Define task schedule**: Use `Schedule::command()` or `Schedule::job()`.
3. **Prevent concurrency**: Chain safety methods (`withoutOverlapping()`, `onOneServer()`).
4. **Log output**: Enforce logging of failures using `onFailure()`.

## Examples

### Good Example

```php
// routes/console.php
use Illuminate\Support\Facades\Schedule;
use App\Jobs\SyncInventoryWithExternalApi;

Schedule::job(new SyncInventoryWithExternalApi)
    ->hourly()
    ->onOneServer()
    ->withoutOverlapping()
    ->onFailure(function (Throwable $e) {
        Log::error('Inventory sync failed: ' . $e->getMessage());
    });
```

### Bad Example

```php
// ❌ Inline heavy database loop, no overlapping prevention, no multi-server safety
Schedule::call(function () {
    foreach (User::all() as $user) {
        $user->calculateStats(); // Blocks the scheduler, overlaps if execution takes > 1 min, duplicates on multi-server!
    }
})->everyMinute();
```
---
