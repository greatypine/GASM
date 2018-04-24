function getRootPath(){
	var curWwwPath=window.document.location.href;
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	var localhostPaht=curWwwPath.substring(0,pos);
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return(localhostPaht+projectName);
}
var baseUrl = getRootPath();

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
importJs("../scripts/common/common-map.js");
importJs("../scripts/lib/jquery/ui/jquery-ui-1.8.9.custom.min.js");
importJs("../scripts/lib/jquery/datatables/jquery.dataTables.js");
importJs("../scripts/lib/jquery/timepicker/jquery-ui-timepicker-addon.js");
//输入框自动完成
importJs("../scripts/lib/jquery/autocomplete/jquery.autocomplete.js");
importJs("../scripts/lib/jquery/thickbox/thickbox-compressed.js");
//jquery.metadata.js 验证用，使validate="required:true"等等有效
importJs("../scripts/lib/jquery/validate/lib/jquery.metadata.js");
//jquery.validate.js 验证用
importJs("../scripts/lib/jquery/validate/jquery.validate.js");
//jquery.tooltip.js 验证用，鼠标悬浮框效果
importJs("../scripts/lib/jquery/tooltip/jquery.tooltip.js");
importJs("../scripts/lib/jquery/ztree/jquery-ztree-2.4.js");
importJs("../scripts/common/common-core.js");
importJs("../scripts/common/common-tool.js");
importJs("../scripts/common/common-expand.js");
importJs("../scripts/common/common-dict.js");
importJs("../scripts/common/common-tool.js");
importJs("../scripts/common/common-form.js");
importJs("../scripts/common/common-table.js");
importJs("js/common-validation-bizbase.js");
//common-validation.js 验证用
importJs("../scripts/common/common-validation.js");
//common-messagedialog.js 对话框
importJs("../scripts/common/common-messagedialog.js");
//common-processingdialog.js 提交后显示“处理中。。。”的div
importJs("../scripts/common/common-processingdialog.js");
importJs("js/bidCommon.js");
importJs("js/acl.js");
importCss(baseUrl+"/scripts/css/deliCommon.css");//bal通用css
importJs(baseUrl+"/scripts/common/qualcommon.js");
importJs("../scripts/lib/jquery/ui/jquery.ui.datepicker-zh-CN.js")
//importCss(baseUrl+"/bizbase/script/datepicter/jquery.ui.all.css");
importCss(baseUrl+"/scripts/lib/jquery/tooltip/jquery.tooltip.css");
//jquery.tooltip.css 验证用，鼠标悬浮框效果
importCss("../scripts/lib/jquery/tooltip/jquery.tooltip.css");
importCss("../scripts/css/datetime.css");
importCss("../scripts/css/datatables.css");
importCss("../scripts/lib/jquery/thickbox/thickbox.css");
importCss("../scripts/lib/jquery/ztree/zTreeStyle/zTreeStyle.css");
importCss("../scripts/css/validation.css");//错误提示
importCss("css/style.css");
//最后引入
importJs("../scripts/default-ready.js");
var PMS_READY = [ "$pmspage.initialize", "$$.run", "$form.run" ];

function toggleBiz(targetid,imgId) {
	var target = $("#"+targetid);
	if(target.is(":visible")){
		target.hide();
		$("#"+imgId).attr("src","../scripts/images/33.png");//变成加号
	}else{
		target.show();
		$("#"+imgId).attr("src","../scripts/images/22.png");//变成减号
	}
}