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
//jquery ui
importJs("../scripts/lib/jquery/ui/jquery-ui-1.8.9.custom.min.js");
//日期时间组件国际化文件
importJs("../scripts/lib/jquery/ui/jquery.ui.datepicker-zh-CN.js");
importJs("../scripts/lib/jquery/timepicker/jquery-ui-timepicker-addon.js");
//jquery datatables
importJs("../scripts/lib/jquery/datatables/jquery.dataTables.js");
//jquery.metadata.js 验证用，使validate="required:true"等等有效
importJs("../scripts/lib/jquery/validate/lib/jquery.metadata.js");
//jquery.validate.js 验证用
importJs("../scripts/lib/jquery/validate/jquery.validate.js");
//jquery.tooltip.js 验证用，鼠标悬浮框效果
importJs("../scripts/lib/jquery/tooltip/jquery.tooltip.js");

//importJs("../scripts/lib/jquery/acl/acl.js");
importJs("../scripts/lib/jquery/ztree/jquery-ztree-2.4.js");

//common tools, required.
importJs("../scripts/common/common-tool.js");

importJs("../scripts/common/common-core.js");
importJs("../scripts/common/common-dict.js");
importJs("../scripts/common/common-form.js");
importJs("../scripts/common/common-table.js");

importJs("../scripts/common/common-divadaptor.js");
importJs("../scripts/common/common-messagedialog.js");
importJs("../scripts/common/common-processingdialog.js");
importJs("../scripts/common/common-resetbutton.js");
importJs("../scripts/common/common-tab.js");
importJs("../scripts/common/common-tabpage.js");
importJs("../scripts/common/common-validation.js");



//common-validation.js 验证用
importJs("../scripts/common/common-validation.js");
//common-messagedialog.js 对话框
importJs("../scripts/common/common-messagedialog.js");
//common-processingdialog.js 提交后显示“处理中。。。”的div
importJs("../scripts/common/common-processingdialog.js");

//importJs("../scripts/lib/jquery/tableEditor/jquery.tableEditor.js");
//importJs("../scripts/lib/jquery/tableEditor/jquery.tablesorter.js");

//不在使用importCss("../scripts/css/common.css");
importCss("../scripts/css/style_sheet_core.css");//框架全局
importCss("../scripts/css/common-app.css");//页面全局
importCss("../scripts/css/datatables.css");//列表
importCss("../scripts/css/datetime.css");//时间
importCss("../scripts/css/tabpage.css");//TAB
//importCss("../scripts/css/uploadtable.css");//
//importCss("../scripts/css/validation.css");//错误提示


//其它控件引用
importCss("../scripts/lib/jquery/autocomplete/jquery.autocomplete.css");
//jquery.tooltip.css 验证用，鼠标悬浮框效果
importCss("../scripts/lib/jquery/tooltip/jquery.tooltip.css");
importCss("../scripts/lib/jquery/ztree/zTreeStyle/zTreeStyle.css");

importJs(baseUrl + "/bizbase/js/acl.js");
//最后引入ready.js
importJs("../scripts/default-ready.js");
var PMS_READY = [ "$pmspage.initialize", "$form.run" , "$$.run" ];
/**
 * 赵彬彬添加的统一返回按钮的js
 */
importJs(baseUrl+"/scripts/common/ripedCommon.js");
/**
 * 王硕加入跳转到任务树界面的js
 */
importJs(baseUrl+"/scripts/goTask.js");
