<?php

declare(strict_types=1);

it('ships a boost core guideline', function () {
    expect(file_exists(__DIR__.'/../../resources/boost/guidelines/core.blade.php'))->toBeTrue();
});

it('ships boost discoverable skills for every manifest skill', function () {
    $manifest = json_decode(file_get_contents(__DIR__.'/../../manifest.json') ?: '', true);

    foreach ($manifest['skills'] as $skill) {
        expect(file_exists(__DIR__.'/../../resources/boost/skills/'.$skill['name'].'/SKILL.md'))
            ->toBeTrue("Missing Boost skill [{$skill['name']}].");
    }
});
