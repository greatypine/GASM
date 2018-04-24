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

function getFilePath(){
	var curWwwPath=window.document.location.href;
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	var localhostPaht=curWwwPath.substring(0,pos);
	var projectName="/file_manager/";
	return(localhostPaht+projectName);
}
//crm弹窗 icon_no:0-6(图标)
function crm_alert(icon_no,info){
	layer.alert(info,
			{
		  	icon:icon_no,
		  	closeBtn:0,
		  	skin:'layer-ext-crmskin',
			move:true, 
			shadeClose: true,
			 title:['提示','color:#fff'],
		    offset:['100px','550px']
			}
	); 
}

//crm弹窗 icon_no:0-6(图标)
function crm_alert_func(icon_no,info){
	layer.alert(info,
			{
		  	icon:icon_no,
		  	closeBtn:0,
		  	skin:'layer-ext-crmskin',
		  	move:true, 
			shadeClose: true,
		    title:['提示','color:#fff'],
		    offset:['100px','550px']
			},function(){
				window.close();
			}
	); 
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
	
	document.write('<script type="text/javascript" src="' + jsFilePath + '"></script>');
}

/*
* @desc : 导入通用JS文件
*/
importJs(baseUrl+"/startbootstrap/js/layer.js");
importJs(baseUrl+"/scripts/layui/layui.js");
/*
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- Morris.js charts -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/raphael/2.1.0/raphael-min.js"></script>
<script src="plugins/morris/morris.min.js"></script>
<!-- FastClick -->
<script src="plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
*/



/*importJs(baseUrl+"/scripts/lib/jquery/form/jquery.form.js");
importJs(baseUrl+"/scripts/common-facade.js");*/
importJs(baseUrl+"/scripts/lib/jquery/validate/lib/jquery.metadata.js");
importJs(baseUrl+"/scripts/lib/jquery/validate/jquery.validate.js");
/*importJs(baseUrl+"/scripts/lib/jquery/tooltip/jquery.tooltip.js");*/
/*importJs(baseUrl+"/scripts/common/common-tab.js");*/
/*importJs(baseUrl+"/scripts/lib/jquery/ui/jquery-ui-1.8.9.custom.min.js");*/
/*importJs(baseUrl+"/scripts/lib/jquery/timepicker/jquery-ui-timepicker-addon.js");*/
/*importJs(baseUrl+"/scripts/lib/jquery/ui/jquery.ui.datepicker-zh-CN.js");*/
importJs(baseUrl+"/scripts/common/common-core.js");
/*importJs(baseUrl+"/scripts/common/common-validation.js");
importJs(baseUrl+"/scripts/common/common-processingdialog.js");
importJs(baseUrl+"/scripts/common/common-messagedialog.js");*/
importJs(baseUrl+"/forecast/js/scripts/bidCommon.js");
/*importJs(baseUrl+"/forecast/js/scripts/DateExtend.js");
importJs(baseUrl+"/forecast/js/scripts/jquery.validateExtend.js");
importJs(baseUrl+"/forecast/js/scripts/common-validation-bid.js");*/
importJs(baseUrl+"/scripts/common/common-tool.js");
importJs(baseUrl+"/scripts/common/common-map.js");
importJs(baseUrl+"/scripts/common/common-dict.js");
importJs(baseUrl+"/scripts/common/common-form.js");
/*importJs(baseUrl+"/scripts/common/dictTreeWin.js");*/


importJs(baseUrl+"/forecast/js/lib/component/Bid.js");

importJs(baseUrl+"/bizbase/js/acl.js");

//importJs(baseUrl+"/scripts/auto.js");

//最后引入
importJs(baseUrl+"/scripts/default-ready.js");
var PMS_READY = [ "$pmspage.initialize", "$form.run" , "$$.run"  ];

/*importJs(baseUrl+"/scripts/common/ripedCommon.js");
importJs(baseUrl+"/scripts/indexPanel.js");*/