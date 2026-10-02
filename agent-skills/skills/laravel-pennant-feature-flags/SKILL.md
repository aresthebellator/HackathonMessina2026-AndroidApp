---
name: laravel-pennant-feature-flags
description: "Use when creating feature toggles, checking flag status, or configuring class-based features using Laravel Pennant."
license: MIT
metadata:
  author: Ali Harb
---

# Laravel Pennant Feature Flags

## When To Use

Use this skill when implementing continuous delivery, beta-testing features for specific users, or configuring runtime flags.

## Workflow

1. **Verify setup**: Ensure `laravel/pennant` is installed and driver settings are configured (`config/pennant.php`).
2. **Define Feature Flag**: Register feature toggles inside `AppServiceProvider`.
3. **Class-Based Flags**: Use class-based features for complex flag calculations.
4. **Conditional checks**: Check flags inside controllers, middleware, or Blade templates cleanly.

## Examples

### Good Example

```php
// app/Providers/AppServiceProvider.php
namespace App\Providers;

use Laravel\Pennant\Feature;
use App\Models\User;

class AppServiceProvider extends ServiceProvider
{
    public function boot(): void
    {
        // Define simple check
        Feature::define('beta-dashboard', function (User $user) {
            return $user->inBetaGroup();
        });
    }
}
```

```blade
{{-- Blade check --}}
@feature('beta-dashboard')
    <x-beta-dashboard-widget />
@else
    <x-standard-dashboard-widget />
@endfeature
```

### Bad Example

```php
// ❌ Using hardcoded config values or database user flags manually for feature toggling.
if (config('app.features.beta_enabled') && auth()->user()->is_beta) {
    // Boilerplate checks that are difficult to manage and test.
}
```
