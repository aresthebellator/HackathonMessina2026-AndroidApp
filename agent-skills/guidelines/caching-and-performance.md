# Caching & Performance Guidelines

## Purpose

Define strict rules on caching patterns, database optimizations, and response-time improvements to keep application responsiveness premium under load.

## Required Standards

- **Never cache raw Eloquent models**: Caching model objects can lead to serialization issues, broken relationships, and memory bloating. Instead, serialize data to raw arrays, DTOs, or key-value payloads before caching.
- **Enforce TTL Limits**: Always specify an explicit Time-to-Live (TTL) on cached records. Avoid infinite caches (`rememberForever()`) unless you have automated cache eviction listeners.
- **Cache Eviction Hook-ups**: Use Model Observers or event listeners to clear cached segments immediately after a state-changing database write.
- **Avoid Cache Stampede (Dogpiling)**: For database-intensive reads, use `Cache::flexible()` or cache locks to prevent multiple concurrent requests from recalculating the same value simultaneously.
- **Prefer Tagged Cache Eviction**: Use `Cache::tags(['records'])` when grouping cached keys, enabling clean bulk eviction without sweeping the entire cache namespace.

## Allowed Patterns

```php
// ✅ GOOD: Storing primitive array structures, not the raw Eloquent model
$data = Cache::tags(['users'])->remember("user:{$id}", now()->addHours(2), function () use ($id) {
    $user = User::findOrFail($id);
    return [
        'id' => $user->id,
        'email' => $user->email,
        'display_name' => $user->name,
    ];
});
```

```php
// ✅ GOOD: Preventing dogpile cache stampedes via Cache::flexible
$value = Cache::flexible('popular_posts', [now()->addMinutes(5), now()->addMinutes(10)], function () {
    return Post::withCount('comments')->orderByDesc('comments_count')->limit(10)->get()->toArray();
});
```

## Forbidden Patterns

- Putting cache logic directly inside Eloquent model attribute getters (leads to unexpected queries and performance drift).
- Serializing database connection resources or model queries.
- Writing complex loop calculations that fetch uncached values iteratively.
