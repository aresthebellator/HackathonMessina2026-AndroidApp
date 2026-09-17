# Task Scheduling & Cron Guidelines

## Purpose

Define structural rules for registering scheduled background tasks, ensuring safe multi-server execution, preventing concurrency lockups, and establishing failure logging thresholds.

## Required Standards

- **Laravel 11+ Console Routing**: Register all scheduled tasks inside `routes/console.php` using the `Schedule` facade, avoiding the legacy `app/Console/Kernel.php` file.
- **Enforce Overlap Prevention**: For tasks running at high frequencies (every minute, every five minutes), always chain `withoutOverlapping()` to prevent a slow running instance from blocking the next execution cycle.
- **Multi-Server Safety**: If the application is deployed in a multi-instance cloud environment (e.g. AWS Auto-Scaling, Laravel Cloud clusters), always chain `onOneServer()` to ensure tasks run exactly once across the cluster.
- **Background Execution**: Keep execution non-blocking. For resource-intensive operations, dispatch a queued background job (`job(new MyJob)`) rather than running raw closures or heavy logic inline in the schedule definition.
- **Log Failure Events**: Chain `onFailure()` or exception logging triggers to notify logging handlers (e.g. Bugsnag, Sentry) if a scheduled task exits with a non-zero exit code.

## Allowed Patterns

```php
// routes/console.php
use Illuminate\Support\Facades\Schedule;
use App\Jobs\CleanExpiredSessionsJob;

// ✅ GOOD: A highly resilient, cluster-safe scheduled job definition
Schedule::job(new CleanExpiredSessionsJob)
    ->daily()
    ->onOneServer()
    ->withoutOverlapping()
    ->onFailure(fn (Throwable $exception) => report($exception));
```

## Forbidden Patterns

- Executing long-running business rules, HTTP calls, or file manipulations directly inside inline schedule closures.
- Omitting `onOneServer()` for critical actions (like charging invoices) in multi-server architectures.
- Using direct server `crontab` entries to manage individual tasks instead of delegating entirely to Laravel's native scheduler (`* * * * * cd /path-to-your-project && php artisan schedule:run >> /dev/null 2>&1`).
