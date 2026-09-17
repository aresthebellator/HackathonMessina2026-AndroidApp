<?php

declare(strict_types=1);

$root = dirname(__DIR__);

function title_from_name(string $name): string
{
    return ucwords(str_replace('-', ' ', $name));
}

function frontmatter_value(string $contents, string $key): ?string
{
    if (preg_match('/^'.preg_quote($key, '/').':\s*"?([^"\n]+)"?/m', $contents, $matches) === 1) {
        return trim($matches[1]);
    }

    return null;
}

$categoryMap = [
    'architecture' => ['action', 'dto', 'service', 'domain', 'model', 'builder', 'architecture'],
    'laravel-core' => ['api', 'command', 'migration', 'event', 'job', 'policy', 'request', 'attribute', 'schedule'],
    'frontend' => ['blade', 'livewire', 'inertia', 'frontend', 'tailwind', 'localization'],
    'filament' => ['filament'],
    'quality' => ['test', 'pest', 'review', 'phpstan', 'pint', 'performance', 'security'],
    'operations' => ['cloud', 'pulse', 'pennant', 'reverb', 'storage', 'environment', 'billing', 'package', 'release', 'github', 'composer', 'debug'],
    'ai-agent' => ['agent', 'boost', 'cursor', 'copilot'],
];

$guidelineMap = [
    'architecture' => 'application-architecture',
    'laravel-core' => 'modern-php-laravel',
    'frontend' => 'frontend-styling',
    'filament' => 'filament',
    'quality' => 'testing-best-practices',
    'operations' => 'package-development',
    'ai-agent' => 'ai-agent-safety',
];

$skills = [];
foreach (glob($root.'/skills/*/SKILL.md') ?: [] as $path) {
    $contents = file_get_contents($path) ?: '';
    $name = basename(dirname($path));
    $category = 'laravel-core';

    foreach ($categoryMap as $candidate => $needles) {
        foreach ($needles as $needle) {
            if (str_contains($name, $needle)) {
                $category = $candidate;
                break 2;
            }
        }
    }

    $guidelines = array_values(array_unique(array_filter([
        $guidelineMap[$category] ?? 'ai-development',
        'ai-development',
        str_contains($name, 'security') ? 'security-and-data-protection' : null,
        str_contains($name, 'test') || str_contains($name, 'phpstan') ? 'testing-best-practices' : null,
        str_contains($name, 'filament') ? 'filament' : null,
        str_contains($name, 'package') || str_contains($name, 'release') ? 'package-development' : null,
        str_contains($name, 'github') ? 'github-actions' : null,
    ])));

    $skills[] = [
        'name' => $name,
        'category' => $category,
        'description' => frontmatter_value($contents, 'description') ?: 'Use this skill for '.strtolower(title_from_name($name)).'.',
        'path' => 'skills/'.$name.'/SKILL.md',
        'guidelines' => $guidelines,
        'tags' => array_values(array_unique([$category, ...explode('-', $name)])),
    ];
}

$guidelines = [];
foreach (glob($root.'/guidelines/*.md') ?: [] as $path) {
    $name = basename($path, '.md');
    if ($name === 'README') {
        continue;
    }

    $category = 'general';
    foreach ($categoryMap as $candidate => $needles) {
        foreach ($needles as $needle) {
            if (str_contains($name, $needle)) {
                $category = $candidate;
                break 2;
            }
        }
    }

    $guidelines[] = [
        'name' => $name,
        'category' => $category,
        'description' => title_from_name($name).' guidelines.',
        'path' => 'guidelines/'.$name.'.md',
    ];
}

usort($skills, fn (array $a, array $b): int => $a['name'] <=> $b['name']);
usort($guidelines, fn (array $a, array $b): int => $a['name'] <=> $b['name']);

$manifest = [
    'name' => 'agent-skills',
    'version' => '1.0.0',
    'description' => 'Advanced Laravel AI agent skills and guidelines for Laravel Boost, skills.sh, and coding assistants.',
    'author' => 'Ali Harb',
    'skills' => $skills,
    'guidelines' => $guidelines,
];

file_put_contents($root.'/manifest.json', json_encode($manifest, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES).PHP_EOL);
