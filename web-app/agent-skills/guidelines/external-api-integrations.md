# External API Integrations Guidelines

## Purpose

Standardize how external third-party APIs (e.g., payment gateways, CRM connections, webhooks) are integrated into Laravel applications, ensuring resilient network calls, proper error tracking, and Mock-friendly test environments.

## Required Standards

- **Never use raw Guzzle or cURL client calls**: Always use Laravel's native HTTP client wrapper (`Illuminate\Support\Facades\Http`).
- **Enforce Timeout Settings**: Every HTTP request must specify explicit connection and response timeouts (e.g. `timeout(10)->connectTimeout(3)`).
- **Graceful Retries & Backoff**: Configure automatic retries with exponential backoff for transient failures (e.g. `retry(3, 100, throw: false)`).
- **Always Throw Exceptions on Failure**: Use `.throw()` or explicitly check response statuses (`failed()`) to ensure network failures are not silently ignored.
- **Isolate Secrets**: API keys, base URLs, and client credentials must live in the `.env` file, loaded through the `config/services.php` configuration, and never hardcoded in PHP classes.
- **Write Mock-Based Feature Tests**: Ensure all external network endpoints are mocked using `Http::fake()` during test execution. Never make real HTTP calls in test suites.

## Allowed Patterns

```php
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Facades\Config;

// ✅ GOOD: A highly resilient, timeout-guarded, and testable API integration
final readonly class StripePaymentClient
{
    public function __construct(
        private string $apiKey,
        private string $baseUrl
    ) {}

    public function charge(string $token, float $amount): array
    {
        $response = Http::baseUrl($this->baseUrl)
            ->withToken($this->apiKey)
            ->timeout(15)
            ->connectTimeout(5)
            ->retry(3, 200)
            ->post('/charges', [
                'amount' => (int) ($amount * 100),
                'currency' => 'usd',
                'source' => $token,
            ]);

        // Automatically throw exception if status code is >= 400
        return $response->throw()->json();
    }
}
```

## Forbidden Patterns

- Putting HTTP calls directly inside controllers, Livewire components, or queue jobs without isolating them in dedicated integration classes.
- Skipping timeout settings (leads to blocked server worker threads).
- Failing to write `Http::fake()` triggers in test suites.
