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

var wcmFileRoot = "temp/";

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

/*
* @desc : 导入通用CSS文件
*/
importCss(baseUrl+"/forecast/css/datepicter/jquery.ui.all.css");
importCss(baseUrl+"/scripts/lib/jquery/tooltip/jquery.tooltip.css");

importCss(baseUrl+"/scripts/css/style_sheet_core.css");//框架全局
importCss(baseUrl+"/scripts/css/common-app.css");//页面全局
importCss(baseUrl+"/scripts/css/datetime.css");//时间
importCss(baseUrl+"/scripts/css/tabpage.css");//TAB
importCss(baseUrl+"/scripts/css/datatables.css");//列表
importCss(baseUrl+"/forecast/css/bidCommon.css");
importCss(baseUrl+"/scripts/css/validation.css");//错误提示





/*
* @desc : 导入通用JS文件
*/
importJs(baseUrl+"/scripts/lib/jquery/jquery-1.5.js");
importJs(baseUrl+"/scripts/common-facade.js");
importJs(baseUrl+"/scripts/lib/jquery/validate/lib/jquery.metadata.js");
importJs(baseUrl+"/scripts/lib/jquery/validate/jquery.validate.js");
importJs(baseUrl+"/scripts/lib/jquery/tooltip/jquery.tooltip.js");
importJs(baseUrl+"/scripts/common/common-tab.js");
importJs(baseUrl+"/scripts/lib/jquery/ui/jquery-ui-1.8.9.custom.min.js");
importJs(baseUrl+"/scripts/lib/jquery/timepicker/jquery-ui-timepicker-addon.js");
importJs(baseUrl+"/scripts/lib/jquery/ui/jquery.ui.datepicker-zh-CN.js");
importJs(baseUrl+"/scripts/common/common-core.js");
importJs(baseUrl+"/scripts/common/common-validation.js");
importJs(baseUrl+"/scripts/common/common-processingdialog.js");
importJs(baseUrl+"/scripts/common/common-messagedialog.js");
//importJs(baseUrl+"/script/tree/jquery.cookie.js");
//importJs(baseUrl+"/script/dialog/dialog.js");
importJs(baseUrl+"/forecast/js/scripts/bidCommon.js");
importJs(baseUrl+"/forecast/js/scripts/DateExtend.js");
importJs(baseUrl+"/forecast/js/scripts/jquery.validateExtend.js");
importJs(baseUrl+"/forecast/js/scripts/common-validation-bid.js");
importJs(baseUrl+"/scripts/common/common-tool.js");
importJs(baseUrl+"/scripts/common/common-map.js");
importJs(baseUrl+"/scripts/common/common-dict.js");
importJs(baseUrl+"/scripts/common/common-form.js");
//---------------------------------------------------
importJs(baseUrl+"/scripts/common/common-columnsPageInfo.js");
//---------------------------------------------------
importJs(baseUrl+"/scripts/common/common-multiTableHeads.js");
importJs(baseUrl+"/forecast/js/lib/component/Bid.js");
importJs(baseUrl+"/bizbase/js/acl.js");

//最后引入
importJs(baseUrl+"/scripts/default-ready.js");
var PMS_READY = [ "$pmspage.initialize", "$$.run", "$form.run" ];

