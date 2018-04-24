/*!
 * import js files and css files.
 */
function importJs(jsFilePath) {
	document.write('<script language="javascript" src="' + jsFilePath + '"></script>');
}
function importCss(cssFilePath) {
	document.write('<link rel="stylesheet" type="text/css" href="' + cssFilePath + '" media="all" />');
}

importJs("../scripts/lib/jquery/jquery-1.5.js");
importJs("../scripts/common/common-tool.js");
importJs("../scripts/common/common-dict.js");
