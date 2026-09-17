# Testing Best Practices Guidelines

## Purpose

Enforce high-quality, resilient, and fast-executing tests using Pest PHP, establishing concrete rules for integration verification, unit testing, and component isolation.

## Required Standards

- **Prefer Pest PHP**: Write all test cases using Pest PHP's fluent syntax (`it()`, `expect()`, `beforeEach()`) rather than verbose PHPUnit classes, unless the project has an existing strict PHPUnit-only convention.
- **Use Mock Fakes Strategically**: Always fake external side-effects (e.g. queues, emails, HTTP calls, event dispatchers) using native Laravel fakes before invoking actions:
  ```php
  Http::fake();
  Queue::fake();
  Mail::fake();
  Event::fake();
  ```
- **Isolate Database State**: Use the `Illuminate\Foundation\Testing\RefreshDatabase` or `LazilyRefreshDatabase` traits to keep tests isolated. Never allow test runs to alter persistent local or staging database records.
- **Test Request Boundaries**: Verify Form Request validation rules by sending invalid requests to the HTTP routes rather than instantiating the Form Request classes manually.
- **Explicit Authorization Tests**: Every protected endpoint or state-changing action must have both a successful path test (authorized user) and a denial path test (anonymous or unauthorized user) asserting `403 Forbidden` or `401 Unauthorized`.

## Allowed Patterns

```php
// ✅ GOOD: A clean, readable Pest feature test asserting route validation, status, and side-effects
it('allows administrators to publish orders', function () {
    Queue::fake();
    $admin = User::factory()->admin()->create();
    $order = Order::factory()->pending()->create();

    $this->actingAs($admin)
        ->postJson(route('orders.publish', $order))
        ->assertOk()
        ->assertJsonPath('data.status', 'published');

    Queue::assertPushed(ProcessOrderJob::class, function ($job) use ($order) {
        return $job->orderId === $order->id;
    });
});

it('denies customer access to publish orders', function () {
    $customer = User::factory()->customer()->create();
    $order = Order::factory()->pending()->create();

    $this->actingAs($customer)
        ->postJson(route('orders.publish', $order))
        ->assertForbidden();
});
```

## Forbidden Patterns

- Making real network requests (avoid third-party API calls in tests by using `Http::fake()`).
- Direct database writes in unit tests (unit tests should test pure logic, DTOs, and value objects without booting database records).
- Asserting database rows using raw SQL statements when `$this->assertDatabaseHas()` or `$this->assertDatabaseCount()` is cleaner.
