<?php

declare(strict_types=1);

it('contains the expected package identity', function () {
    $manifest = json_decode(file_get_contents(__DIR__.'/../../manifest.json') ?: '', true);

    expect($manifest)
        ->toBeArray()
        ->and($manifest['name'])->toBe('agent-skills')
        ->and($manifest['version'])->toBe('1.0.0')
        ->and($manifest['author'])->toBe('Ali Harb')
        ->and($manifest['skills'])->toHaveCount(62)
        ->and($manifest['guidelines'])->toHaveCount(44);
});

it('keeps preset metadata valid', function () {
    $presets = json_decode(file_get_contents(__DIR__.'/../../presets.json') ?: '', true);

    expect($presets['name'])->toBe('agent-skills-presets')
        ->and($presets['presets'])->toHaveCount(9);
});
