<?php

$config = parse_ini_file('environment.ini');
$extension = '';

if (strtoupper(substr(PHP_OS, 0, 3)) === 'WIN') {
	$extension = '.exe';
}

define('SCRIPT_DIR', dirname(__FILE__));
define('INKSCAPE_EXECUTABLE', $config['inkscapeHome'] . 'inkscape' . $extension);
define('JAVA_EXECUTABLE', $config['javaHome'] . 'java' . $extension);
define('TEXTURE_PACKER_EXECUTABLE', $config['texturePackerHome'] . 'runnable-texturepacker.jar');
define('ASSETS_HOME', $config['assetsHome']);
define('PROJECT_HOME', $config['projectHome']);
define('MARKETING_HOME', $config['marketingHome']);

