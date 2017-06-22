<?php

include 'environment.php';

function printResponse($response) {
    foreach ($response as $str) {
        echo $str . '\n';
    }
}

function startsWith($haystack, $needle) {
    return $needle === "" || strrpos($haystack, $needle, -strlen($haystack)) !== false;
}

function endsWith($haystack, $needle) {
    return $needle === "" || (($temp = strlen($haystack) - strlen($needle)) >= 0 && strpos($haystack, $needle, $temp) !== false);
}

function getObjects($path) {
    $objects = array();
    $response = inkscape('--file=' . $path . ' --query-all');
    
    foreach ($response as $object) {
        $elements = explode(",", $object);
        
        if (count($elements) > 0 && strlen($elements[0]) > 0) {
			$exportable = exportable($elements[0]);

			if ($exportable !== false) {
            	$objects[] = $exportable;
			}
        }
    }
    
    return $objects;
}

function layer($object) {
    if (startsWith($object, "layer-") == false) {
        return false;
    }
    
    return substr($object, 6);
}

function exportable($object) {
    if (startsWith($object, "export-") == false) {
        return false;
    }
    
    return substr($object, 7);
}

function inkscape($command) {
    if (strlen($command) > 0) {
        $command = ' ' . $command;
    }
    
    exec(INKSCAPE_EXECUTABLE . $command, $response);
    
    return $response;
}

function export($path, $object, $dest) {
    $response = inkscape('--file=' . $path . ' --export-id=export-' . $object . ' --export-png ' . $object);
    rename($object, $dest);
}

function tpack($inputDir, $outputDir, $name) {
	$configFile = '';

	if (file_exists(SCRIPT_DIR . '/assets-packer.properties')) {
		$configFile = ' ' . SCRIPT_DIR . '/assets-packer.properties';
	}

	$command = JAVA_EXECUTABLE . ' -jar ' . TEXTURE_PACKER_EXECUTABLE . ' ' . $inputDir . ' ' . $outputDir . ' ' . $name . $configFile;
	exec($command);
}



