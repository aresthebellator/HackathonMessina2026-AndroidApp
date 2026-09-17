---
name: write-pest-test
description: "Use when writing or updating Pest PHP tests for controllers, Livewire components, API endpoints, actions, or console commands."
license: MIT
metadata:
  author: Ali Harb
---

# Write Pest PHP Test

## When To Use

Use this skill when introducing tests for newly written features or writing regression tests using Pest PHP.

## Workflow

1. **Verify setup**: Ensure Pest is initialized in `tests/Pest.php`.
2. **Scaffold test file**: Place files under `tests/Feature/` or `tests/Unit/` with `.php` suffix.
3. **Use descriptive test blocks**: Write tests using `it('does something', ...)` or `test('does something', ...)`.
4. **Mock Side-effects**: Stub out mail, events, notifications, and queues.
5. **Assert JSON payloads & database state**: Use Pest expectations `$this->assertDatabaseHas()` and `$response->assertJson()`.

## Examples

### Good Example

```php
// tests/Feature/Actions/CreateUserTest.php
use App\Actions\CreateUser;
use App\Models\User;
use Illuminate\Support\Facades\Event;
use App\Events\UserCreated;

it('creates a user record and dispatches creation event', function () {
    Event::fake();

    $action = new CreateUser();
    $user = $action->handle([
        'name' => 'John Doe',
        'email' => 'john@example.com',
    ]);

    expect($user)->toBeInstanceOf(User::class);
    expect($user->name)->toBe('John Doe');

    $this->assertDatabaseHas('users', [
        'email' => 'john@example.com',
    ]);

    Event::assertDispatched(UserCreated::class);
});
```

### Bad Example

```php
// ❌ Using verbose PHPUnit classes with boilerplate when Pest syntax is cleaner and simpler.
namespace Tests\Feature;

use Tests\TestCase;

class CreateUserTest extends TestCase
{
    public function testItCreatesUser()
    {
        // Boilerplate class structure is harder to read and maintain.
    }
}
```
