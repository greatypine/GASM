$(function(){
	doFunFilter();
})

var roleCode = "";

function doFunFilter() {
	var userSettingDTOFun = {
			hallId:getUrlParamByKey("roomId"),
			userCode:getUrlParamByKey("userCode")
	}
	
	doManager("bidRoleSettingManager","getHallUserFun",userSettingDTOFun,function(data){
		if(data.result) {
			var permissionStr = $.fromJSON(data.data).userPermission;
			roleCode = $.fromJSON(data.data).roleCode;
			//处理主持人等的特殊权限
			if(roleCode == "0001") {
				//这里主要处理开标大厅 一个更改发言权限的按钮
				$("#specialSpeakFun").show();
			}
			if(permissionStr.indexOf("speakFun,") < 0) {
				if($("#chatroom1").length > 0) {
					$("#chatroom1").contents().find("form").hide();
				}				
			}  else {
					if($("#chatroom1").length > 0) {
					$("#chatroom1").contents().find("form").show();
				}				
			}
			$("[hallResourceId]").each(function(){
				if(permissionStr.indexOf(($(this).attr("hallResourceId")+",")) < 0) {
					$(this).hide();
				} else {
					$(this).show();
				}
			})
		}
	})

}