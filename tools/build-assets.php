<?php

include 'lib.php';

chdir(ASSETS_HOME);

if (count($argv) != 2) {
    echo "Usage: php assets.php <svg_file.svg>\n";
    
    return;
}

define('FILE_NAME', $argv[1]);

function build() {
	$fileName = basename(FILE_NAME);

	if (endsWith($fileName, '.svg')) {
		$fileName = substr($fileName, 0, -4);
	}

	if (!file_exists($fileName)) {
		mkdir($fileName);
	}

    foreach (getObjects(FILE_NAME) as $object) {
        
        if (!file_exists($fileName . '/' . '/packed')) {
            mkdir($fileName . '/' . '/packed');
        }
        
		export(FILE_NAME, $object, $fileName . '/' . $object . '.png');
        tpack($fileName, $fileName . '/packed', basename($fileName));
    }
}

build();

