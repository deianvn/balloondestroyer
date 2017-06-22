<?php

include 'lib.php';

chdir(MARKETING_HOME);
$iconsSvgPath = MARKETING_HOME . 'icons.svg';
$exportables = array('xxhdpi', 'xhdpi', 'hdpi', 'mdpi');
$targetName = 'icon.png';

function createTargetPath($name) {
	global $targetName;
	return PROJECT_HOME . 'res/drawable-' . $name . '/' . $targetName;
}

foreach (getObjects($iconsSvgPath) as $object) {

	if (in_array($object, $exportables)) {
		export($iconsSvgPath, $object, createTargetPath($object));
	} else if ($object === 'icon') {
		export($iconsSvgPath, $object, PROJECT_HOME . $targetName);
	}

}

