# Background Queues & Horizon Guidelines

## Purpose

Establish rules for running asynchronous background jobs safely, maintaining partition performance, and managing failure states using Laravel Horizon.

## Required Standards

- **Enforce Job Idempotency**: Queue jobs must be designed to run safely multiple times without causing duplicate transactions (e.g. check status before charging a payment).
- **Prevent Duplicate Dispatches**: Use the `ShouldBeUnique` interface for jobs where duplicate concurrent executions are forbidden. Specify the unique identifier explicitly.
- **Commit-Sensitive Dispatches**: Never dispatch background jobs within an active database transaction before the commit is confirmed. Use `dispatch()->afterCommit()` or implement the `ShouldQueueAfterCommit` interface.
- **Explicit Timeout & Tries**: Always configure explicit job limits in queue attributes:
  ```php
  #[Tries(3)]
  #[Timeout(60)]
  ```
- **Failsafe Failure Handlers**: Implement a public `failed(Throwable $exception)` method on critical jobs to handle notifications, logging, or state rollbacks on final exhaust.

## Allowed Patterns

```php
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Queue\Attributes\Queue;
use Illuminate\Queue\Attributes\Tries;

// ✅ GOOD: Commit-sensitive, unique queue partitions
#[Queue('default')]
#[Tries(3)]
final class ProcessOrderJob implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    public function __construct(public int $orderId) {}

    public function handle(OrderService $service): void
    {
        // Double-check condition for idempotency
        $order = Order::findOrFail($this->orderId);
        if ($order->isProcessed()) {
            return;
        }

        $service->process($order);
    }
}
```

## Forbidden Patterns

- Storing massive binary data or complete Eloquent structures in the job constructor (causes Redis payload limit failures). Only pass model IDs.
- Running long-running API requests or file exports without explicit timeouts.
- Manually invoking `app()` inside queue handlers when dependency injection can resolve constructor helpers.
