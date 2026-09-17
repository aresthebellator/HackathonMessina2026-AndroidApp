# Inertia.js & Single Page Application Guidelines

## Purpose

Define structural rules for building Single Page Applications (SPAs) using Inertia.js, ensuring optimized state sharing, efficient data passing, and client-agnostic Vue/React rendering boundaries.

## Required Standards

- **Use Inertia for SPA rendering**: Always return `Inertia::render()` from controller actions instead of Blade views when building client-rendered views.
- **Enforce State Optimization via Lazy Props**: Avoid bloated initial page payloads. Use lazy properties (`Inertia::lazy()`) for heavy calculations or data tables that are not immediately visible on page load.
- **Sanitize Shared Context**: Only share global state (e.g. auth user, global configuration, flash messages) inside the `HandleInertiaRequests` middleware. Never bloat the global shared payload with route-specific data.
- **Handle Form Requests via `useForm`**: Instruct client-side scripts to use Inertia's native `useForm` helper to handle validation error states, loading spinners, and request submissions.
- **Server-Side Validation Redirects**: When validation fails in an Inertia form request, Laravel automatically redirects back, sharing validation errors via the session. Do not attempt to catch validation exceptions manually inside controllers to return JSON responses.

## Allowed Patterns

```php
use Inertia\Inertia;
use Inertia\Response;

// ✅ GOOD: Clean controller returning scoped Inertia views with lazy-loaded data
final readonly class PostController
{
    public function show(Post $post): Response
    {
        return Inertia::render('Posts/Show', [
            'post' => [
                'id' => $post->id,
                'title' => $post->title,
                'content' => $post->content,
            ],
            // Lazy load comments only if the client requests them explicitly
            'comments' => Inertia::lazy(fn () => $post->comments()->latest()->get()->toArray()),
        ]);
    }
}
```

## Forbidden Patterns

- Mixing Inertia and Livewire rendering within the same application dashboard.
- Passing raw, unmapped Eloquent models into `Inertia::render` (causes serialization leaks).
- Manually parsing validation rules on the frontend instead of relying on server-side Form Request validation.
