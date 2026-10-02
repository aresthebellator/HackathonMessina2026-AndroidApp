<?php

declare(strict_types=1);

$root = dirname(__DIR__);
$source = $root.'/skills';
$destination = $root.'/resources/boost/skills';

if (! is_dir($source)) {
    fwrite(STDERR, "Missing skills directory.\n");
    exit(1);
}

if (is_dir($destination)) {
    $iterator = new RecursiveIteratorIterator(
        new RecursiveDirectoryIterator($destination, FilesystemIterator::SKIP_DOTS),
        RecursiveIteratorIterator::CHILD_FIRST
    );

    foreach ($iterator as $file) {
        $file->isDir() ? rmdir($file->getPathname()) : unlink($file->getPathname());
    }
}

if (! is_dir($destination)) {
    mkdir($destination, 0755, true);
}

$iterator = new RecursiveIteratorIterator(
    new RecursiveDirectoryIterator($source, FilesystemIterator::SKIP_DOTS),
    RecursiveIteratorIterator::SELF_FIRST
);

foreach ($iterator as $file) {
    $target = $destination.'/'.substr($file->getPathname(), strlen($source) + 1);

    if ($file->isDir()) {
        if (! is_dir($target)) {
            mkdir($target, 0755, true);
        }

        continue;
    }

    copy($file->getPathname(), $target);
}

echo "Boost skills synced.\n";
