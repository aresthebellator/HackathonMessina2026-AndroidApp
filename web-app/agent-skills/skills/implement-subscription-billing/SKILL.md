---
name: implement-subscription-billing
description: "Use when configuring Laravel Cashier, setting up billing portals, handling S3/Stripe webhooks, or verifying user tiers."
license: MIT
metadata:
  author: Ali Harb
---

# Implement Subscription Billing

## When To Use

Use this skill when installing Cashier, managing customer plans, redirecting users to the Stripe portal, or subscribing users to a plan.

## Workflow

1. **Verify Billable Trait**: Ensure the user model uses the `Laravel\Cashier\Billable` trait.
2. **Setup Billing Portal**: Register routes to redirect users to Stripe's Customer Portal.
3. **Configure Webhooks**: Expose a webhook endpoint and register the Stripe webhook key in `.env`.
4. **Enforce Access**: Create middleware to check for active plans.

## Examples

### Good Example

```php
// app/Http/Controllers/BillingController.php
namespace App\Http\Controllers;

use Illuminate\Http\Request;

final class BillingController
{
    public function portal(Request $request)
    {
        // Redirect the user to Stripe's customer billing dashboard
        return $request->user()->redirectToBillingPortal(
            route('dashboard')
        );
    }
}
```

```php
// app/Models/User.php
namespace App\Models;

use Illuminate\Foundation\Auth\User as Authenticatable;
use Laravel\Cashier\Billable;

final class User extends Authenticatable
{
    use Billable;
}
```

### Bad Example

```php
// ❌ Creating direct payments using custom Stripe API Curl calls
// Cashier handles invoice generation, tokens, and customers automatically.
```
---
