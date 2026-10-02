# Filament Guidelines

## Purpose

Use Filament v5 as a declarative admin and operations layer for Laravel applications without moving business logic out of reusable application classes.

## Required Standards

- Filament should be used for panels, resources, relation managers, pages, widgets, forms, tables, infolists, actions, notifications, and query-builder-style UI when the project needs an admin experience.
- Panel providers, resources, pages, widgets, actions, and table/query builders should stay thin and orchestrate reusable application code.
- Filament components should call Actions, Services, DTOs, Policies, Events, Jobs, or query builders rather than containing business workflows directly.
- Use Filament localization helpers and Laravel translation keys for all visible labels, messages, empty states, and notifications.
- Before writing application translations for Filament, run `php artisan lang:publish` and keep admin-facing copy in `lang/admin/**`.
- Keep business rules in reusable layers; Filament is the presentation and orchestration layer.
- Prefer PHP 8 attributes and Filament’s own declarative APIs where they reduce boilerplate and improve readability.
- Check the Filament v5 upgrade guide and current docs before using any API; avoid deprecated methods, deprecated route/component patterns, and removed conveniences.
- Use policies, authorization callbacks, and resource access rules consistently for protected records and actions.
- For authorization on Filament panels, prefer Filament Shield as the default policy/permission generator and keep policies synchronized with generated permissions.
- Use custom Eloquent builders or query objects for reusable filtering and ordering; do not move reusable query logic into model scopes in new code.
- Prefer Spatie Laravel Data for Filament form and action data contracts when the project is already using it.
- Avoid inline imports in Blade and example snippets; prefer fully qualified class names.
- Use shared resource form/table methods for relation managers when the same layout or behavior already exists.
- Prefer Filament resource reuse for related managers and shared tables/forms when the same structure already exists.
- Prefer the smallest shared schema fragment when only part of a resource can be reused cleanly.
- Keep panel providers opinionated but thin; enable panel-wide features such as SPA mode, database notifications, and error notifications when the app benefits from them.
- Publish and use the Laravel notifications table when Filament notifications or database notifications are part of the admin experience.
- Follow Filament v5 API names exactly; do not use removed `Form $form` or `Table $table` conventions if the installed package expects schema-based signatures instead.

## Filament Areas To Cover

- Panels and panel providers.
- Resources, relation managers, clusters, and custom pages.
- Forms, actions, schemas, wizards, repeaters, sections, tabs, and file upload flows.
- Tables, filters, summaries, bulk actions, query builder filters, custom data sources, and row actions.
- Infolists for read-only display and action modals.
- Widgets for dashboards and metrics.
- Notifications and background or queued admin interactions.
- Imports and exports when the package or plugin is installed.
- Custom components, view overrides, and published stubs when necessary.
- Premium or third-party extensions only after the package is installed and the feature exists in this app.

## Allowed Patterns

- `php artisan make:filament-resource`
- `php artisan make:filament-page`
- `php artisan make:filament-widget`
- `php artisan make:filament-resource relation-manager`
- `php artisan make:filament-user`
- `php artisan make:notifications-table`
- `php artisan filament:optimize`
- Resource `form()`, `table()`, `infolist()`, and `getPages()` methods that delegate to reusable classes or helper methods.
- Filament actions that call Actions or Services.
- Translation keys such as `__('filament.users.labels.name')` or package translation keys.
- Custom data sources for tables when the source is not Eloquent.

## Forbidden Patterns

- Business workflows directly inside Filament closures when the same logic should be reused elsewhere.
- Repeating the same validation or business logic in resources, pages, widgets, and controllers.
- Using Filament as a reason to skip policies, DTOs, or reusable Actions.
- Hardcoding user-facing labels that should be localizable.
- Putting reusable query logic in model scopes in new code.
- Using outdated Filament signatures or examples from older versions.

## Naming Conventions

- Resource names match the domain model and `Resource` suffix.
- Page classes use descriptive operation names or panel-specific names.
- Relation managers name the relationship they manage.
- Widgets use outcome or metric names.
- Form and table component names should describe intent, not implementation.

## Testing Expectations

- Test resource actions, authorization, validation, query behavior, and persistence changes.
- Add Livewire or Filament tests for admin interactions that affect behavior.
- Add unit tests for Actions, DTOs, query builders, and services called by Filament.
- Add regression tests for custom table data, filters, and action flows.

## Review Checklist

- Does the Filament component delegate business behavior?
- Is authorization explicit and tested?
- Are labels and messages localized?
- Are related managers reusing resource form/table definitions where appropriate?
- Are shared schema fragments extracted instead of copying entire forms or tables?
- Are reusable queries in builders or query objects?
- Are forms and tables thin wrappers around reusable code?
- Are premium or plugin features only used when actually installed?

## Acceptable Examples

```php
public static function form(Schema $schema): Schema
{
    return $schema
        ->schema([
            Forms\Components\TextInput::make('name')
                ->label(__('filament.users.fields.name'))
                ->required(),
        ]);
}
```

```php
public static function table(Table $table): Table
{
    return $table
        ->query(User::query()->active())
        ->actions([
            Tables\Actions\EditAction::make(),
        ]);
}
```

## Unacceptable Examples

```php
Forms\Components\Actions\Action::make('approve')
    ->action(function (Model $record): void {
        $record->status = 'approved';
        $record->save();
    });
```

```php
TextColumn::make('status')->label('Approved?');
```
