/*
 * @methodName   : getRootPath
 * @methodReturn : String
 * @author       : ruanqingfeng
 * @desc         : 获取项目根路径
 * @for example  : getRootPath();
 */
function getRootPath(){
	var curWwwPath=window.document.location.href;
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	var localhostPaht=curWwwPath.substring(0,pos);
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return(localhostPaht+projectName);
}

/*
* @author       : ruanqingfeng
* @desc         : 全局变量，保存根路径
*/
var baseUrl = getRootPath();

/*
* @methodName   : importJs
* @methodParam  : String （JS路径）
* @methodReturn : void
* @author       : ruanqingfeng
* @desc         : 导入JS文件
* @for example  : importJs("commmon.js");
*/
function importJs(jsFilePath) {
	
	document.write('<script language="javascript" src="' + jsFilePath + '"></script>');
}

/*
* @methodName   : importCss
* @methodParam  : String （CSS路径）
* @methodReturn : void
* @author       : ruanqingfeng
* @desc         : 导入CSS文件
* @for example  : importCss("commmon.css");
*/
function importCss(cssFilePath) {
	document.write('<link rel="stylesheet" type="text/css" href="' + cssFilePath + '" media="all" />');
}

/*importCss(baseUrl+"/scripts/jquery-ui/jquery-ui-1.9.2.custom/css/redmond/jquery-ui-1.9.2.custom.min.css");
*/importCss(baseUrl+"/scripts/jqGrid/css/ui.jqgrid.css");

importJs(baseUrl+"/scripts/json2.js");
importJs(baseUrl+"/scripts/jqGrid/js/jquery.jqGrid.src.js");
importJs(baseUrl+"/scripts/jqGrid/js/jqgrid.extend.js");
importJs(baseUrl+"/scripts/jqGrid/js/i18n/grid.locale-cn.js");
/*importJs(baseUrl+"/scripts/jquery-ui/jquery-ui-1.9.2.custom/js/jquery-ui-1.9.2.custom.min.js");*/
importJs(baseUrl+"/scripts/common/jqGrid-extend.js");



// Add fancyBox main JS and CSS files
importJs(baseUrl+"/scripts/fancyBox/jquery.fancybox.pack.js");
importCss(baseUrl+"/scripts/fancyBox/jquery.fancybox.css");

importCss(baseUrl+"/scripts/jqGrid/css/jqgrid.custom.css");
