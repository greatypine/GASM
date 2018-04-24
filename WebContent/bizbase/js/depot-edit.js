/**
 * 判断编辑时仓库名是否重复
 */
function checkEditDepotName() {
	var nowDepotName = $("#depotName").val();
	var idVal = $("#id").val();
	if (validateExistsByEdit("com.cnpc.pms.bizbase.basedata.syscode.entity.Depot", idVal, "depotName", nowDepotName)) {
		return true;
	}
	return false;
}

/**
 * 页面加载时加载数据
 *
 */
function loadDepotDate() {
	var id = getUrlParamByKey('id');
	$(function() {
		if (id || "" == id) {
			var url = $$.PMSDispatchActionUrl;
			var data = new PMSRequestObject("depotManager", "getDepotDTO", [id]);
			$$.ajax(url, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest) {
				var str = data.data;
				var depot = $.fromJSON(str);
				$("#id").val(depot.id);
				$("#depotName").val(depot.depotName);
				$("#depotCode").val(depot.depotCode);
				$("#depot_address").val(depot.depot_address);
				$("#agency").val(depot.agency);
				$("#contact_phone").val(depot.contact_phone);
				$("#contact").val(depot.contact);
				$("#memo").val(depot.memo);
				$("#createUserID").val(depot.createUserID);
				$("#createDate").val(depot.createDate);
				$("#lastModifyUserID").val(depot.lastModifyUserID);
				$("#lastModifyDate").val(depot.lastModifyDate);
			});
		}
	})
}


function editDepot() {
	$validator = new PMSValidator($("#editDepotForm")[0], 
		{
			bNormalDisplay: false,
			iDisplayLength: 1
		});
	var passed = $validator.clientValidate();
	if (!passed) {
		return false;
	}
	var div = $("<div></div>");
	div.html("确定修改？");
	var depotObj = 
		{
			id: $("#id").val(),
			depotCode: $("#depotCode").val(),
			depotName: $("#depotName").val(),
			depot_address: $("#depot_address").val(),
			contact_phone: $("#contact_phone").val(),
			agency: $("#agency").val(),
			contact: $("#contact").val(),
			memo: $("#memo").val(),
			createUserID: $("#createUserID").val(),
			createDate: $("#createDate").val(),
			lastModifyUserID: $("#lastModifyUserID").val(),
			lastModifyDate: $("#lastModifyDate").val()
		};
	div.dialog(
		{
			bgiframe: true,
			title: "提示",
			width: 320,
			buttons: 
				{
					"确定": function() {
						doManager("depotManager", "saveModifyDepot", depotObj, function(data, textStatus, XMLHttpRequest) {
							div.dialog("close");
						});
					},
					"取消": function() {
						$(this).dialog("close");
					}
				},
			modal: true
		});
}

function returnPage() {
	history.go(-1);
}



