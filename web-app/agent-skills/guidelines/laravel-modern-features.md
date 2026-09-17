# Laravel 13 Native Attributes & Modern Features Guidelines

## Purpose

Establish standards for using Laravel 13's native PHP 8+ attributes and modern features, eliminating legacy boilerplate properties and methods in favor of declarative, type-safe metadata.

## Required Standards

- **Declarative Controller Middleware & Auth**: Prefer `#[Middleware]` and `#[Authorize]` attributes on controller classes or methods to enforce security boundaries directly at the controller definition.
- **Contextual Injection in Constructors**: Use parameter-level container attributes (`#[Config]`, `#[Storage]`, `#[DB]`, `#[CurrentUser]`) for dependency injection to inject config keys, storage disks, connection pools, and authenticated users directly into services without manual service container binding.
- **Declarative Job Configuration**: Configure queued jobs using Queue attributes (`#[Queue]`, `#[Tries]`, `#[Backoff]`, `#[Timeout]`) rather than defining public properties or class methods.
- **Eloquent Model Metadata**: Use model-level attributes (`#[Table]`, `#[Connection]`, `#[UsePolicy]`, `#[ObservedBy]`) to declare table names, databases, authorization policies, and observers.
- **Modern Blade `@use` Directive**: Use the `@use` directive at the top of Blade templates to import class namespaces cleanly instead of writing fully-qualified class names repeatedly in templates.
- **Defer Functionality**: Use the `defer()` helper for non-critical post-response operations (e.g. tracking telemetry) instead of dispatching persistent queued jobs.

---

## Allowed Patterns

### 1. Attribute-Based Controller Routing & Security
```php
namespace App\Http\Controllers;

use App\Models\Document;
use Illuminate\Routing\Attributes\Controllers\Authorize;
use Illuminate\Routing\Attributes\Controllers\Middleware;

#[Middleware('auth:sanctum')]
final class DocumentController
{
    #[Authorize('view', 'document')]
    public function show(Document $document): DocumentResource
    {
        return new DocumentResource($document);
    }
}
```

### 2. Contextual Container Injection
```php
namespace App\Services;

use Illuminate\Container\Attributes\Config;
use Illuminate\Container\Attributes\Storage;
use Illuminate\Contracts\Filesystem\Filesystem;

final readonly class FileStorageService
{
    public function __construct(
        #[Storage('s3-private')] private Filesystem $privateDisk,
        #[Config('filesystems.disks.s3-private.region')] private string $region
    ) {}
}
```

### 3. Declarative Queue Jobs
```php
namespace App\Jobs;

use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Queue\Attributes\Backoff;
use Illuminate\Queue\Attributes\Queue;
use Illuminate\Queue\Attributes\Tries;

#[Queue('notifications')]
#[Tries(5)]
#[Backoff([10, 30, 60])]
final class SendWebhookNotificationJob implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;
}
```

### 4. Eloquent Attribute Mapping
```php
namespace App\Models;

use App\Policies\PostPolicy;
use App\Observers\PostObserver;
use Illuminate\Database\Eloquent\Attributes\Table;
use Illuminate\Database\Eloquent\Attributes\UsePolicy;
use Illuminate\Database\Eloquent\Attributes\ObservedBy;

#[Table('blog_posts')]
#[UsePolicy(PostPolicy::class)]
#[ObservedBy(PostObserver::class)]
final class Post extends Model
{
    //
}
```

---

## Forbidden Patterns

- Mixing old class properties (like `$tries`, `$backoff`, `$connection` in jobs or models) with new attributes in the same class (pick one standard, preferring attributes in new code).
- Utilizing custom PHP attributes to hold core business workflows or database access code.
- Hardcoding config keys directly inside services instead of using `#[Config('services.name.key')]`.
