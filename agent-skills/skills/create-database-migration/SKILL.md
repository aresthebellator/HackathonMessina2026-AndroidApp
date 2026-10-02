---
name: create-database-migration
description: "Use when creating, altering, or adding new database tables, indexes, or constraints in Laravel migrations."
license: MIT
metadata:
  author: Ali Harb
---

# Create Database Migration

## When To Use

Use this skill when designing schemas, creating new Eloquent model backing tables, adding indexes, or modifying existing database columns.

## Workflow

1. **Verify conventions**: Inspect nearby migration files to ensure naming compatibility.
2. **Scaffold migration**: Run `php artisan make:migration create_xxx_table`.
3. **Define constraints**: Declare explicit relationships, types, indexes, and unique attributes.
4. **Define down operation**: Ensure the migration is reversible (implement `down()` properly dropping added columns or tables).
5. **Dry run checks**: Run local migrations and rollbacks to verify structural success.

## Examples

### Good Example

```php
// database/migrations/2026_07_07_000000_create_profiles_table.php
use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('profiles', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->unique()->constrained()->cascadeOnDelete();
            $table->string('bio', 500)->nullable();
            $table->string('avatar_url')->nullable();
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('profiles');
    }
};
```

### Bad Example

```php
// ❌ Creating relations without constraints, missing indices, missing down method.
Schema::create('profiles', function (Blueprint $table) {
    $table->id();
    $table->integer('user_id'); // Vulnerable and loose relation!
    $table->text('bio');
});
```
