<?php

declare(strict_types=1);

namespace AlizHarb\AgentSkills\Tests;

use AlizHarb\AgentSkills\AgentSkillsServiceProvider;
use Illuminate\Foundation\Application;
use Orchestra\Testbench\TestCase as Orchestra;

class TestCase extends Orchestra
{
    /**
     * @param  Application  $app
     * @return array<int, class-string>
     */
    protected function getPackageProviders($app): array
    {
        return [
            AgentSkillsServiceProvider::class,
        ];
    }
}
