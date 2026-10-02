# Security & Data Protection Guidelines

## Purpose

Secure personal identifiable information (PII), restrict administrative and file permissions, enforce token validations, and protect sensitive data from leak vectors.

## Required Standards

- **PII Encryption**: Store sensitive user attributes (e.g., social security numbers, API tokens) using Eloquent's encrypted casting:
  ```php
  protected $casts = [
      'secret_token' => 'encrypted',
  ];
  ```
- **Failsafe Environment Files**: Never commit `.env` variables or private security keys to the source repository. Maintain strict `.gitignore` patterns.
- **Secure File Storage**: Avoid storing user uploads directly in the `public/` directory. Store sensitive documents in private filesystems, serving them via signed, short-lived URLs (`Storage::temporaryUrl()`).
- **Protect State Mutations**: State-changing endpoints (POST/PUT/DELETE) must enforce CSRF validation middleware.
- **Strict Query Bindings**: Never concatenate raw input strings into database queries (e.g., `whereRaw("email = '{$email}'")`). Always use parameter binding or standard Eloquent builders.

## Allowed Patterns

```php
use Illuminate\Support\Facades\Storage;

// ✅ GOOD: Exposing sensitive files securely using short-lived signed URLs
final readonly class ServingController
{
    public function download(Document $document)
    {
        $this->authorize('view', $document);

        return redirect()->away(
            Storage::disk('s3')->temporaryUrl($document->path, now()->addMinutes(15))
        );
    }
}
```

```php
// ✅ GOOD: Parameterized raw query queries
$users = User::whereRaw('active = ? AND role = ?', [true, 'admin'])->get();
```

## Forbidden Patterns

- Storing API tokens, keys, passwords, or PII as plain text in the database.
- Concatenating raw variables inside `DB::raw()` or `whereRaw()` statements (causes SQL injection vulnerabilities).
- Exposing file uploads on public URLs without authorization policy checks.
