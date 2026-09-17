---
name: create-inertia-page
description: "Use when creating Vue/React Inertia pages, configuring controllers to render Inertia views, and managing form submissions."
license: MIT
metadata:
  author: Ali Harb
---

# Create Inertia Page

## When To Use

Use this skill when building client-rendered UI pages, setting up forms using React or Vue within a Laravel backend, or lazy loading dashboard data tables.

## Workflow

1. **Verify setup**: Ensure `inertiajs/inertia-laravel` is installed and the root template (`app.blade.php`) is configured.
2. **Scaffold frontend component**: Create a Vue/React page inside `resources/js/Pages/` (e.g. `resources/js/Pages/Posts/Show.vue`).
3. **Draft Controller Handler**: Write a controller action returning `Inertia::render()`.
4. **Setup client-side form**: Use `useForm` to bind inputs and handle validations.

## Examples

### Good Example

```php
// app/Http/Controllers/ContactController.php
namespace App\Http\Controllers;

use Inertia\Inertia;
use Inertia\Response;
use App\Http\Requests\ContactRequest;

final class ContactController
{
    public function show(): Response
    {
        return Inertia::render('Contact/Show');
    }

    public function store(ContactRequest $request)
    {
        // Validation handles error redirections automatically
        // Action handles the business logic
        return redirect()->route('dashboard')->with('message', 'Sent!');
    }
}
```

```vue
<!-- resources/js/Pages/Contact/Show.vue -->
<script setup>
import { useForm } from '@inertiajs/vue3';

const form = useForm({
    email: '',
    message: '',
});

const submit = () => {
    form.post('/contact');
};
</script>

<template>
    <form @submit.prevent="submit">
        <input v-model="form.email" type="email" />
        <span v-if="form.errors.email">{{ form.errors.email }}</span>

        <textarea v-model="form.message"></textarea>
        
        <button :disabled="form.processing">Send</button>
    </form>
</template>
```

### Bad Example

```php
// ❌ Catching validation manually to return error JSONs instead of using standard form redirects.
public function store(Request $request) {
    $validator = Validator::make($request->all(), [...]);
    if ($validator->fails()) {
        return response()->json($validator->errors(), 422); // Incompatible with standard Inertia form bindings!
    }
}
```
---
