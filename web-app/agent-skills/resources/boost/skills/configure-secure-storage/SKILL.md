---
name: configure-secure-storage
description: "Use when setting up file uploads, configuring private/public disks, and serving sensitive document URLs."
license: MIT
metadata:
  author: Ali Harb
---

# Configure Secure Storage

## When To Use

Use this skill when implementing avatar uploads, invoice attachments, document storage, or configuring AWS S3/minio disks.

## Workflow

1. **Verify disk rules**: Ensure disk driver config is defined in `config/filesystems.php`.
2. **Access Control check**: Never use the `public` disk for documents that require authentication/authorization policies.
3. **Generate temporary URL**: Serve private documents using temporary URLs (`Storage::temporaryUrl()`).
4. **Clean files**: Clean up orphaned or failed file uploads via event-based disk cleanup jobs.

## Examples

### Good Example

```php
// app/Actions/GenerateInvoiceDownloadUrl.php
namespace App\Actions;

use Illuminate\Support\Facades\Storage;
use App\Models\Invoice;

final readonly class GenerateInvoiceDownloadUrl
{
    public function handle(Invoice $invoice): string
    {
        // Serve secure link valid for 10 minutes only
        return Storage::disk('s3-private')->temporaryUrl(
            $invoice->file_path,
            now()->addMinutes(10)
        );
    }
}
```

### Bad Example

```php
// ❌ Storing private client documents in public directory where anyone can guess the URL.
$file->move(public_path('invoices'), $file->getClientOriginalName());
```
