function doAddFactory(){
    if (validateOrg() && validateCode() && validateName()&&validateIsUniqueCode()) {
        var dto = {
            org: $('#orgCodeField').val(),
            code: $('#codeField').val(),
            name: $('#nameField').val()
        };
        $("#submitFactoryButton").attr("disabled", "disabled");
        doManager("factoryStorageManager", "addFactory", dto, function(data, textStatus, XMLHttpRequest){
            if (data.result) {
                window.location = "factory_storage_management.html";
            }else{
				$$.showMessage("${system.info}","系统异常,请联系管理员!");
			}
        }, true, {
            showWaiting: true
        });
        $("#submitFactoryButton").attr("disabled", "");
    }
}

function doAddStorage(){
    if (validateFactory() && validateCode() && validateName()&&validateIsUniqueCode2()) {
        var factoryCode = $("#factoryCodeField").val();
        var dto = {
			org: $('#orgCodeField').val(),
            code: $('#codeField').val() + factoryCode,
            name: $('#nameField').val()
        };
        $("#submitStorageButton").attr("disabled", "disabled");
        doManager("factoryStorageManager", "addStorage", dto, function(data, textStatus, XMLHttpRequest){
            if (data.result) {
				$("#submitStorageButton").attr("disabled", "");
                window.location = "factory_storage_management.html";
            }else{
				$$.showMessage("${system.info}","系统异常,请联系管理员!");
			}
        }, true, {
            showWaiting: true
        });
        
    }
}

function chooseFactory(){
	doManager("userManager", "getCurrentUserDTO", [], function(datas, textStatus, XMLHttpRequest) {
					var currentUser = $.fromJSON(datas.data);
					if (currentUser.usertype != 2) {
						$("#org\\.path").val(currentUser.orgPath + "%");
					}
				},false);
	$$.executeSearch('factoryQuery', 'searchDiv');
    var div = $("#searchFactoryDiv");
    div.dialog({
        bgiframe: true,
        title: "选择工厂",
        width: 640,
        buttons: {
            "确定": function(){
                var objs = $$.getSelectedObjects("factoryQuery");
                if (objs.length > 0) {
                    var obj = objs[0];
					$("#orgCodeField").val(obj['org.code']);
                    $("#factoryNameField").val(obj['name']);
                    $("#factoryCodeField").val(obj['code']);
					$("#factoryID").val(obj['id']);
                    $("#factory_factoryCodeField").remove();
                    $("#codeField").after("<label id='factory_factoryCodeField'>" + obj.code + "</label>");
                    div.dialog("close");
                }
                else {
                    $$.showMessage("${system.info}", "请选择一个工厂");
                }
            },
            "取消": function(){
                div.dialog("close");
            }
        },
        modal: true,
        closeOnEscape: false,
        open: function(event, ui){
            $(".ui-dialog-titlebar-close").hide();
        }
    });
}

function doUpdateFactoryStorage(){
    var id = $('#idField').val();
    if (id != null && validateName()) {
        var dto = {
            id: id,
            name: $("#nameField").val()
        }
        $("#submitStorageButton").attr("disabled", "disabled");
        doManager("factoryStorageManager", "updateFactoryStorage", dto, function(datas, textStatus, XMLHttpRequest){
            if (datas.result) {
                $$.showMessage("${system.info}", "修改成功!");
                setTimeout(goManagement, 1500);
            }else{
				$$.showMessage("${system.info}","系统异常,请联系管理员!");
			}
        }, false, {
            showWaiting: true
        });
        $("#submitStorageButton").attr("disabled", "");
    }
}

function goManagement(){
    window.location = "factory_storage_management.html";
}

function validateOrg(){
    clearError("orgSearchButton");
    var org = $("#orgCodeField").val();
	var clientCode='';
    if (org == '') {
        showError("orgSearchButton", "组织机构不能为空");
        return false;
    }
	doManager("purStruOrgManager","getObjectByCode",org,function(datas,textStatus,XMLHttpRequest){
		if(datas.result){
			var obj = $.fromJSON(datas.data);
			clientCode = obj.clientCode;
		}
	},false);
	if(clientCode==''||null==clientCode){
		showError("orgSearchButton", "组织机构没有client,请重新选择!");
        return false;
	}
    return true;
}

function validateIsUniqueCode(){
	clearError("codeField");
    var code = $("input#codeField").val();
	var id = $("#factoryID").val();
	var org = $("#orgCodeField").val();
	var flag = false;

	doManager("purStruOrgManager","checkFactoryIsUnique",[org,code],function(data,textStatus,XMLHttpRequest){
		if(data.result){
			var temp = $.fromJSON(data.data);
			flag = temp;
		}
	},false);
	if(!flag){
		showError("codeField", "工厂编码已存在");
        return false;
	}
	return true;
}

function validateIsUniqueCode2(){
	clearError("codeField");
    var code = $("input#codeField").val();
	 var factory = $("#factoryID").val();
	 var factoryCode = $("#factoryCodeField").val();
	 code = code+factoryCode;
	var flag = false;
	doManager("purStruOrgManager","checkStorageIsUnitque",[factory,code],function(data,textStatus,XMLHttpRequest){
		if(data.result){
			var temp = $.fromJSON(data.data);
			flag = temp;
		}
	},false);
	if(!flag){
		showError("codeField", "库存地点编码已存在");
        return false;
	}
	return true;
}
function validateCode(){
    clearError("codeField");
    var code = $("input#codeField").val();
	var regu = /^[A-Z0-9]{4}$/;
	var re = new RegExp(regu);
    if (!re.test(code)) {
        showError("codeField", "编码只能包含数字和大写字母,长度必须为4位");
        return false;
    }
    return true;
}

function validateName(){
    clearError("nameField");
    var code = $("input#nameField").val();
    if (code == '') {
        showError("nameField", "名称不能为空");
        return false;
    }
    return true;
}

function validateFactory(){
    clearError("factorySearchButton");
    var factory = $("#factoryCodeField").val();
    if (factory == '') {
        showError("factorySearchButton", "工厂不能为空");
        return false;
    }
    return true;
}


function clearError(forId){
    var errorObj = $('label[class="error"][for="' + forId + '"]');
    errorObj.remove();
}

function showError(forId, info){
    var ele = $('#' + forId);
    var errorInfo = "<label class='error' for='" + forId + "'>" + info + "</label>";
    ele.after(errorInfo);
}

function showErrorIn(forId, info){
    var container = $("#" + forId).parent();
    var errorInfo = "<label class='error' for='" + forId + "'>" + info + "</label>";
    container.append(errorInfo);
}
