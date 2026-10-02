# Billing & Subscription Guidelines

## Purpose

Standardize SaaS monetization integrations using Laravel Cashier (Stripe/Paddle), ensuring robust user tier validation, secure webhook ingestion, and grace period compliance.

## Required Standards

- **Use Cashier for Subscriptions**: Always use Laravel Cashier's native methods and helpers for managing user billing plans, avoiding direct raw database modifications of subscription tables.
- **Check Subscription Access via Gates/Middleware**: Restrict feature access using clean authorization checks:
  ```php
  if ($user->subscribed('default')) { ... }
  ```
- **Failsafe Webhook Signatures**: Never process incoming webhook payloads (e.g. `customer.subscription.deleted`) without enforcing signature verification middleware.
- **Grace Period Verification**: Always respect grace periods (`onGracePeriod()`). Allow users access to their plans until the subscription cycle has fully expired, even if they have requested a cancellation.
- **Isolate Payment Logic**: Payment processing, card updates, and billing portal redirects must be routed via Cashier's pre-built controller routes or isolated billing portal classes.

## Allowed Patterns

```php
// ✅ GOOD: Clean middleware verification for subscription tiers
namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;

final class EnsureUserIsSubscribed
{
    public function handle(Request $request, Closure $next, string $plan = 'default')
    {
        if ($request->user() && ! $request->user()->subscribed($plan)) {
            return redirect()->route('billing.portal');
        }

        return $next($request);
    }
}
```

## Forbidden Patterns

- Manually writing raw database queries to update the `subscriptions` table.
- Disabling signature validation on webhook routes in production environments.
- Suspending user access immediately upon subscription cancellation without checking for grace periods.
