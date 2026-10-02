---
name: laravel-pulse-monitoring
description: "Use when configuring, integrating, or parsing application metrics and performance bottlenecks using Laravel Pulse."
license: MIT
metadata:
  author: Ali Harb
---

# Laravel Pulse Performance Monitoring

## When To Use

Use this skill when diagnosing slow application routes, resolving database performance issues, tracking cache metrics, or configuring recorders.

## Workflow

1. **Verify configuration**: Verify `config/pulse.php` exists.
2. **Setup slow-query threshold**: Configure limits for slow queries (e.g. queries taking > 500ms).
3. **Register Custom Recorders**: Create and bind custom performance recorders to track application-specific events.
4. **Isolate Dashboard Access**: Ensure only authorized administrators can view the `/pulse` dashboard.

## Examples

### Good Example

```php
// app/Providers/AppServiceProvider.php
namespace App\Providers;

use Illuminate\Support\Facades\Gate;
use App\Models\User;

class AppServiceProvider extends ServiceProvider
{
    public function boot(): void
    {
        // ✅ Restrict dashboard view access securely
        Gate::define('viewPulse', function (User $user) {
            return $user->is_admin;
        });
    }
}
```

### Bad Example

```php
// ❌ Leaving the pulse route open in production without gates or authentication checks.
Route::get('/pulse', function() {
    return view('pulse'); // VULNERABLE!
});
```
