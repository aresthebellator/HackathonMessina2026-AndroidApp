<?php

declare(strict_types=1);

namespace AlizHarb\AgentSkills;

use Illuminate\Support\ServiceProvider;

class AgentSkillsServiceProvider extends ServiceProvider
{
    public function boot(): void
    {
        // Laravel Boost auto-discovers the resources/boost directory.
    }
}
