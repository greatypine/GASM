function getRequestParams() {
	var url = window.location.search;
	var theRequest = new Object();
	if (url.indexOf('?') != -1) {
		var str = url.substr(1);
		var strs = str.split('&');
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split('=')[0]] = unescape(strs[i].split('=')[1]);
		}
	}
	return theRequest;
}

var $validator;
var request = getRequestParams();
var userId = request['userId'];
var userObj;
var oldPasswordFlag = false;
var newPasswordFlag = false;
/**
 * 初始用户信息
 * isShow:是否显示
 */
function initUserInfo(isShow){
	$validator = new PMSValidator($("#userEditForm")[0],{
		bNormalDisplay: false,
		iDisplayLength: 999
	});
	if (null != userId || "" == userId) {
		doManager("userManager", "getUserDTO", userId, function(data, textStatus, XMLHttpRequest) {
			userObj = $.fromJSON(data.data);
			if(isShow){
				$("#userName").val(userObj.name);
				$("#mobilephone").val(userObj.mobilephone);
				$("#email").val(userObj.email);
				$("#phone").val(userObj.phone);
				$("#fax").val(userObj.fax);
				//投保人,临时供应商,供应商不允许修改名称
				if(userObj.bidderType==2||userObj.bidderType==3){
					$("#userName").attr("disabled",true);
				}
				if (userObj.sex == 0||userObj.sex == '男') {
	                $("input[name=sex]").eq(0).attr("checked", true);
	            }else {
	                $("input[name=sex]").eq(1).attr("checked", true);
	            }
			}
		});
	}
}
/**
 * 修改用户基本信息
 */
function editUserInfo() {
	var passed = $validator.clientValidate();
	if (!passed) {
		return false;
	}
	//password:null不修改密码
	var modifyObj = {
		name: $("#userName").val(),
		mobilephone: $("#mobilephone").val(),
		email: $("#email").val(),
		phone: $("#phone").val(),
		fax: $("#fax").val(),
		sex: getRadioVal("sex"),
		password:null
	}
	editDialog(modifyObj);
}
/**
 * 修改用户密码
 */
function editPassword() {
	$validator.clean();
	var passed = $validator.clientValidate();
	if (!passed) {
		return;
	}
	if (!oldPasswordFlag) {
		return;
	}
	if (!newPasswordFlag) {
		return;
	}
	
	//增加密码强度的限定条件

    var regu = /\w*[0-9]+\w*$/;
    var regu2 = /\w*[a-zA-Z]+\w*$/;
    var newPassword = $("#newPassword").val().replace(/\s+/g,',');
    if (!regu.test(newPassword) || !regu2.test(newPassword)) {
        alert("密码必须包含字母和数字！");
        $("#newPassword").focus();
        return false;
    }

    if($("#newPassword").val().length<8){
        alert("密码必须要8个字符！");
        return false;
    }

    var modifyObj = {
		password: $("#newPassword").val()
	}
	editDialog(modifyObj);
}
/**
 * 修改提示框
 */
function editDialog(modifyObj){
	var div = $("<div></div>");
	div.html("确定修改?");
	div.dialog(
	{
		bgiframe: true,
		title: "提示",
		width: 320,
		buttons: 
		{
			"确定": function() {
				//修改参数
				$.extend(userObj,modifyObj);
				var methodName ="saveModifyUser";
				if(userObj.doctype==1){
					methodName="modifyExternalUser";
				}
				doManager("userManager", methodName, userObj, function(data, textStatus, XMLHttpRequest) {
					if (data.result) {
						returnPage();
					}else{
						$$.showMessage('${query.ui.prompt}', data.message);
					}
				},true,{showWaiting:true});
				$(this).dialog("close");
			},
			"取消": function() {
				$(this).dialog("close");
			}
		},
		modal: true
	});
}
/**
 * 验证旧密码
 */
function checkPassword(obj) {
	var str = obj.value;
	if (str == "") {
		$("#validatePassword").text("");
		return;
	}
	doManager("userManager", "checkPassword", [userId, str], function(data, textStatus, XMLHttpRequest) {
		if (data.result) {
			if (data.data == "false") {
				$("#validatePassword").text("密码输入不正确");
				oldPasswordFlag = false;
			}
			else {
				$("#validatePassword").text("");
				oldPasswordFlag = true;
			}
		}
	}, false);
}
/**
 * 验证新密码
 */
function isSamePassword() {
	var str1 = $("#newPassword").val();
	var str2 = $("#newPassword2").val();
	if (str2 != str1) {
		$("#newSamePassword").text("密码不一致");
		newPasswordFlag = false;
	}
	else {
		$("#newSamePassword").text("");
		newPasswordFlag = true;
	}
}
/**
 * 返回
 */
function returnPage() {
    //http://localhost:8080/JavaCRM/indexs/szIndex_yadea.html
    // window.location = "../index.html";
	window.parent.location.reload();
}