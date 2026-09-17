---
name: review-performance
description: "Use when reviewing Laravel code for database, queue, cache, API, Livewire, or rendering performance."
license: MIT
metadata:
  author: Ali Harb
---

# Review Performance

## When To Use

Use this skill when code touches growing datasets, loops, queries, rendering, APIs, queues, caches, Livewire, or external calls.

## Inputs Needed

- Expected data size, access pattern, query paths, response needs, queues, caches, and test or profiling output.

## Workflow

1. Identify hot paths and growth risks.
2. Check query count, eager loading, selected columns, pagination, indexes, and aggregate strategy.
3. Check loops for repeated database, cache, network, or filesystem calls.
4. Check queues for slow or retryable work.
5. Check caches for correctness, invalidation, and stampede risks.
6. Check Livewire components for excessive state, missing keys, and unnecessary requests.
7. Recommend focused fixes with measurable value.

## Files That May Be Created

- Performance regression tests where stable and valuable.

## Files That May Be Modified

- Queries, models, migrations, Actions, jobs, caches, resources, Livewire components, and tests if fixing issues is requested.

## Architecture Rules

- Performance fixes should preserve boundaries and avoid moving logic into presentation layers.

## Testing Requirements

- Prefer behavior tests plus targeted query-count or regression tests only when they are stable.

## Security Requirements

- Do not cache private data in shared scopes.
- Avoid leaking data through broad eager loads or resources.

## Review Checklist

- N+1 queries, pagination, indexes, selected columns, aggregates, cache invalidation, queue boundaries, external timeouts, payload size, Livewire request volume, and memory use.

## Common Mistakes

- Caching before fixing query shape.
- Adding brittle query-count tests.
- Doing external calls inside loops or transactions.
