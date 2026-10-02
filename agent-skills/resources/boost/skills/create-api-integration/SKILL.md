---
name: create-api-integration
description: "Use when building custom external integrations, Stripe webhooks, CRM integrations, or external HTTP clients."
license: MIT
metadata:
  author: Ali Harb
---

# Create API Integration

## When To Use

Use this skill when introducing third-party API clients, syncing database entities to external software, or building custom integration drivers.

## Workflow

1. **Verify configuration**: Declare credentials in `.env` and map them inside `config/services.php`.
2. **Build Client class**: Place client class under `app/Services/` or `app/Integrations/`.
3. **Resilience check**: Enforce timeout, retry count, and throw validations.
4. **Define fakes**: Implement test mocks using `Http::fake()`.

## Examples

### Good Example

```php
// app/Services/WeatherClient.php
namespace App\Services;

use Illuminate\Support\Facades\Http;

final readonly class WeatherClient
{
    public function __construct(
        private string $key,
        private string $url = 'https://api.weather.com'
    ) {}

    public function getForecast(string $city): array
    {
        return Http::baseUrl($this->url)
            ->timeout(5)
            ->retry(2)
            ->get('/forecast', ['q' => $city, 'key' => $this->key])
            ->throw()
            ->json();
    }
}
```

```php
// tests/Feature/WeatherTest.php
it('fetches weather data safely', function () {
    Http::fake([
        'api.weather.com/*' => Http::response(['temp' => 72], 200),
    ]);

    $client = new WeatherClient('secret-key');
    $data = $client->getForecast('New York');

    expect($data['temp'])->toBe(72);
});
```

### Bad Example

```php
// ❌ Hardcoded API URL, no timeout, and no test mock support.
class SyncJob implements ShouldQueue {
    public function handle() {
        $res = file_get_contents('https://external-api.com/sync?key=123');
        // If external-api.com hangs, this job blocks indefinitely!
    }
}
```
