/**
 * 检测库房名称是否已存在
 */
function checkDepotName() {
	var nowDepotName = $("#depotName").val();
	if (validateExists("com.cnpc.pms.bizbase.basedata.syscode.entity.Depot", "depotName", nowDepotName)) {
		return true;
	}
	return false;
}

/**
 * 检测库房标号是否已存在
 */
function checkDepotCode() {
	var nowDepotCode = $("#depotCode").val();
	if (validateExists("com.cnpc.pms.bizbase.basedata.syscode.entity.Depot", "depotCode", nowDepotCode)) {
		return true;
	}
	return false;
}

/**
 * 提交库房数据
 */
function doSubmit() {

	//$validator.clean();
	var passed = $validator.clientValidate($("#storeRoomForm")[0]);
	if (!passed) {
		//alert('test');
		return false;
	}
	doSave('depotManager', 'addNewDepot');
	
}

function doSave(Manager, Method) {
	var url = $$.PMSDispatchActionUrl;
	var depotObject = 
		{
			depotName: $("#depotName").val(),
			depotCode: $("#depotCode").val(),
			agency: $("#agency").val(),
			depot_address: $("#depot_address").val(),
			contact: $("#contact").val(),
			contact_phone: $("#contact_phone").val(),
			memo: $("#memo").val()
		};
	var data = new PMSRequestObject(Manager, Method, [$.toJSON(depotObject)], null);
	$$.ajax(url, "requestString=" + data.toJsonString(), function(datas, textStatus, XMLHttpRequest) {
		$$.showMessage("${system.info}","添加库房成功!");
		location="storeroom_manager.html"
	});
}

/**
 * 重置Form表单
 */
//			(function($) {
//				$(function() {
//					$("form button[type=reset]").click(function(event) {
//						var msgDialog = new MessageDialog("提示", "清除所有输入的信息？", null, "确定", "取消", function() {
//							event.target.form.reset();	
//							//$validator.clean();					
//							MessageDialog("close");
//						}, null).open();
//						return false;
//					});
//				});
//				
//			})(jQuery);

function doReset() {
	$validator.clean();
	$form.reset("storeRoomForm");
	
}

var $validator;
$(document).ready(function(){
		$validator = new PMSValidator($("#storeRoomForm")[0], 
		{
			bNormalDisplay: false,
			iDisplayLength: 111
		});
	
});
