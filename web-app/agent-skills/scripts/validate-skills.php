<?php

declare(strict_types=1);

$root = dirname(__DIR__);
$manifestPath = $root.'/manifest.json';
$presetsPath = $root.'/presets.json';
$errors = [];

foreach ([$manifestPath, $presetsPath] as $path) {
    if (! is_file($path)) {
        $errors[] = basename($path).' is missing.';
    }
}

$manifest = is_file($manifestPath) ? json_decode(file_get_contents($manifestPath) ?: '', true) : [];
$presets = is_file($presetsPath) ? json_decode(file_get_contents($presetsPath) ?: '', true) : [];

if (! is_array($manifest)) {
    $errors[] = 'manifest.json is invalid JSON.';
    $manifest = [];
}

if (! is_array($presets)) {
    $errors[] = 'presets.json is invalid JSON.';
    $presets = [];
}

$skillNames = [];
$guidelineNames = [];

foreach (($manifest['guidelines'] ?? []) as $guideline) {
    $name = $guideline['name'] ?? '';
    $guidelineNames[] = $name;

    if ($name === '' || ! is_file($root.'/'.($guideline['path'] ?? ''))) {
        $errors[] = "Guideline [{$name}] has an invalid path.";
    }
}

foreach (($manifest['skills'] ?? []) as $skill) {
    $name = $skill['name'] ?? '';
    $skillNames[] = $name;

    foreach (['name', 'category', 'description', 'path', 'guidelines'] as $field) {
        if (! array_key_exists($field, $skill)) {
            $errors[] = "Skill [{$name}] is missing [{$field}].";
        }
    }

    if (! is_file($root.'/'.($skill['path'] ?? ''))) {
        $errors[] = "Skill [{$name}] has an invalid path.";
    }

    foreach (($skill['guidelines'] ?? []) as $guideline) {
        if (! in_array($guideline, $guidelineNames, true)) {
            $errors[] = "Skill [{$name}] references missing guideline [{$guideline}].";
        }
    }
}

foreach (($presets['presets'] ?? []) as $preset => $definition) {
    foreach (($definition['skills'] ?? []) as $skill) {
        if (! in_array($skill, $skillNames, true)) {
            $errors[] = "Preset [{$preset}] references missing skill [{$skill}].";
        }
    }
}

foreach (($manifest['skills'] ?? []) as $skill) {
    $skillName = $skill['name'] ?? '';
    $boostPath = $root.'/resources/boost/skills/'.$skillName.'/SKILL.md';

    if (! is_file($boostPath)) {
        $errors[] = "Boost skill [{$skillName}] is missing at resources/boost/skills.";
    }
}

if ($errors !== []) {
    echo "Agent Skills validation failed:\n";
    foreach ($errors as $error) {
        echo "- {$error}\n";
    }
    exit(1);
}

echo "Agent Skills validation passed.\n";
