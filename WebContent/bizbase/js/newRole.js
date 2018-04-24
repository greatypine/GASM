function searchRoleQuery(){
    $("#roleConditionDiv").find("input[name=code]").val($("#oleRoleCode").val());
    $("#roleConditionDiv").find("input[name=name]").val($("#oldRoleName2").val());
	$("#roleConditionDiv").find("input[name=type]").val($("#roleTypeStr").val()+"%");
	$("#roleConditionDiv").find("input[name=roleAttribute]").val($("#roleTypeStr2").val());
	
    $$.executeSearch('roleQuery', 'roleConditionDiv');
}

function showOldRoles(){
	$("#roleConditionDiv").find("input[name=type]").val($("#roleTypeStr").val()+"%");
	$("#roleConditionDiv").find("input[name=roleAttribute]").val($("#roleTypeStr2").val());
    $$.executeSearch('roleQuery', 'roleConditionDiv');
    doEditCssType();
    $("#oldRoleInfo").dialog({
        bgiframe: true,
        title: '角色选择',
        width: 805,
        height: 400,
        modal: true,
        buttons: {
            "${form.ui.ok}": function(){
                var selects = $$.getSelectedObj("roleQuery");
                if (selects.length == 0) {
                    $$.showMessage("${system.info}", "未选择要复制的角色");
                    return false;
                }
                if (selects.length > 0) {
                    var roleInfo = {
                        roleId: selects[0][0],
                        oldRole: selects[0][1],
                    }
                    $("#oldRoleName").val(roleInfo.oldRole);
                    $("#oldRoleId").val(roleInfo.roleId);
                    $("#oldRoleInfo").dialog('close');
                    $("#oldRoleName").focus();
                }
            },
            "${query.ui.cancel}": function(){
                $("#oldRoleInfo").dialog('close');
            }
        }
    });
}

/**
 * 解决列表Css样式不一致的问题.
 */
function doEditCssType(){
    var table = $('.display');
    table.each(function(){
        var th1 = $(this).find('th:first');
        if (th1.find("input[type=checkbox]").length > 0) {
            th1.css('width', '1%');
        }
        $(this).find('th').each(function(){
            $(this).css('border', '1px solid #999999').css('background', '#D1D6F0');
            ;
        });
    });
}

function checkRoleName(){
    var nowRoleName = $("#roleName").val();
    if (validateExists("com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role", "name", nowRoleName)) {
        return true;
    }
    return false;
}

function checkRoleCode(){
    if (validateExists("com.cnpc.pms.bizbase.rbac.rolemanage.entity.Role", "code", $("#roleCode").val())) {
        return true;
    }
    return false;
}

function doSsave(){
    var passed = $validator.clientValidate($("#roleAddForm")[0]);
    if (!passed) {
        return false;
    }
    var roleObject = {
        name: $("#roleName").val(),
        code: $("#roleCode").val(),
		copyRoleId:$("#oldRoleId").val(),
        disabledFlag: getRadioVal("radioA"),
        note: $("#memo").val(),
		type:$("#roleType").val(),
		roleAttribute:$("#roleAttribute").val()
    };
    doManager("roleManager", "addNewRoleDTO", roleObject, function(data, textStatus, XMLHttpRequest){
        if (data.result) {
            var str = $.fromJSON(data.data);
            window.location.href = "roleAuth.html?id=" + str.id;
        }
    },true,{showWaiting:true});
}

function backPage(){
    window.location = "role_management.html";
}
