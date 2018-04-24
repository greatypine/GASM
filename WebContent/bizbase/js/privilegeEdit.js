var orgId = getUrlParamByKey('orgId');
var priId = getUrlParamByKey('priId');
var type = getUrlParamByKey('type');
var userGroupId = getUrlParamByKey('userGroupId');
var priCode, priShowName, priBusinessId, conditionName;
var clientCode=null;
var parentID;
var psorgCode;
var psorgParentCode;
function returnPage(){
    history.go(-1);
}

function doSave(id){
    if (id == "conSubmitId") {
        var fileValueArrCheck = [];
        var fileNameArrCheck = [];
        var conditionsValue = getConditons();
        for (var i = 0; i < conditionsValue.length; i++) {
            if (conditionsValue[i].fieldValue != "") {
                fileValueArrCheck.push(conditionsValue[i].fieldValue);
            }
            if (conditionsValue[i].fieldName != "") {
                fileNameArrCheck.push(conditionsValue[i].fieldName);
            }
        }
        if (fileNameArrCheck.length != conditionsValue.length) {
            $$.showMessage("${system.info}", "条件名称不能为空!");
            return;
        }
        if (fileValueArrCheck.length != conditionsValue.length) {
            $$.showMessage("${system.info}", "条件取值不能为空!");
            return;
        }
    	var isRepeat = false;
    	var fm = new Array();
   		$.each(conditionsValue, function(i, n){
        fm[i] = n.fieldName;
    	});
    	for (var i = 0; i < fm.length; i++) {
        	for (var j = i + 1; j < fm.length; j++) {
            if (fm[j] == fm[i]) {
                isRepeat = true;
                break;
            }
        }
    }
    if (isRepeat) {
        $$.showMessage("${system.info}", "条件名称不能重复!");
        return;
    }
    for (var i = 0; i < fm.length; i++) {
        if (conditionName.indexOf(fm[i]) != -1) {
            isRepeat = true;
            break;
        }
    }
    if (isRepeat) {
        $$.showMessage("${system.info}", "条件已存在,请检查后重新添加!");
        return;
    }
    }
    var div = $("<div></div>");
    div.html("确定提交？");
    div.dialog({
        bgiframe: true,
        title: "提示",
        width: 320,
        buttons: {
            "确定": function(){
                if (id == "priSubmitId") {
                    doSubmit(this);
                    $(this).dialog("close");
                }
                if (id == "conSubmitId") {
                    doAddConditon(this);
                }
                
            },
            "取消": function(){
                $(this).dialog("close");
            }
        },
        modal: true
    });
}

function doSubmit(obj){
    $validator = new PMSValidator($("#privilegeEditForm")[0], {
        bNormalDisplay: false,
        iDisplayLength: 999
    });
    $validator.clean();
    var passed = $validator.clientValidate();
    if (!passed) {
        $(obj).dialog("close");
        return false;
    }
    var privilegeObj = {
        id: priId,
        code: priCode,
        showName: priShowName,
        disabledFlag: 1,
        businessId: $("#businessType").val()
    }
    doManager("privilegeManager", "modifyPrivilege", privilegeObj, function(data, textStatus, XMLHttpRequest){
		if(type=='userGroupQuery'){
			window.location = "userGroupQuery.html"
		}else{
			window.location = "userGroupManagement.html?orgId=" + orgId;
		}
    });
}

function doEdit(obj){
    var conditonsObj;
    conditonsObj = {
        id: $("#id").val(),
        fieldName: $("#fieldName2").val(),
        operation: $("#operationshow2").val(),
        fieldValue: $("#fieldValue2").val(),
        deletedValue: $("#deletedValue").val(),
		isCompatible: $("#isCompatibalDB").val()
    };
    if ($("#fileValueForShowEdit").val() == "") {
        $$.showMessage("${system.info}", "条件取值不可为空!");
        return;
    }
    doManager("privilegeManager", "modifyConditionDTO", conditonsObj, function(data, textStatus, XMLHttpRequest){
        $$.executeSearch('conditionQuery', 'searchConDiv');
        $(obj).dialog("close");
    }, true, {
        showWaiting: true
    });
}
function assignAll(obj){
	$(obj).dialog("close");
	var div = $("<div></div>");
			div.html("此操作将添加所有大类,确定提交？");
			div.dialog(
				{
					bgiframe: true,
					title: "提示",
					width: 320,
					buttons: 
						{
							"确定": function() {
								doAssignAll(this);
							},
							"取消": function() {
								$(this).dialog("close");
							}
						},
					modal: true
				});
}
function doAssignAll(obj){
	var conditonsObj;
	$(obj).dialog("close");
    conditonsObj = {
        id: $("#id").val(),
        fieldName: $("#fieldName2").val(),
        operation: $("#operationshow2").val(),
    };
	 doManager("privilegeManager", "addAllItems", conditonsObj, function(data, textStatus, XMLHttpRequest){
        $$.executeSearch('conditionQuery', 'searchConDiv');
        $(obj).dialog("close");
    }, true, {
        showWaiting: true
    });
}
var treeEditFlag = true;
var editObj = {
    html: '<a href="#" class="blue">编辑</a>',
    func: function(queryid, data, nTr, allColumns,allColumnsDataMap){
        var conditon;
        var div = $("<div></div>");
        $("#id").val(allColumnsDataMap.id);
        $("#operationshow2").val(allColumnsDataMap.operation);
        $("#fileValueForShowEdit").val(allColumnsDataMap.fieldValue);
        $("#fieldValue2").val(allColumnsDataMap.fieldValue);
		$("#valueInDB").val(allColumnsDataMap.fieldValue);
		$("#isCompatibalDB").val(allColumnsDataMap.isCompatible);
        var filed = $("#fieldName2")[0];
		var assign = document.getElementById("assignAll");
		var bs = "";
		
		if(allColumnsDataMap.fieldName=="物料类别"){
			bs={
				"分配全部权限":
					function(){
						assignAll(this);
					}
				,
                "确定": function(){
                    doEdit(this);
                },
                "取消": function(){
                    treeEditFlag = true;
                    $(this).dialog("close");
                }
            }
		}else{
			bs={
                "确定": function(){
                    doEdit(this);
                },
                "取消": function(){
                    treeEditFlag = true;
                    $(this).dialog("close");
                }
            }
		}
        for (var i = 0; i < filed.options.length; i++) {
            if (allColumnsDataMap.fieldName == filed.options[i].value) {
                filed.options[i].selected = true;
            }
        }
        var opera = $("#operationshow2")[0];
        for (var i = 0; i < opera.options.length; i++) {
            if (allColumnsDataMap.operation == opera.options[i].text) {
                opera.options[i].selected = true;
            }
        }
        conditon = $("#form2");
        div.html(conditon);
        div.dialog({
            bgiframe: true,
            title: "编辑",
            width: 400,
            buttons: bs,
            modal: true
        });
    }
}

var delObj = {
    html: '<a href="#" class="blue">删除</a>',
    func: function(queryid, data, nTr, allColumns,allColumnsDataMap){
        var div = $("<div></div>");
        div.html("确定删除?");
        var doDelete = function(data){
            var url = $$.PMSDispatchActionUrl;
            var data = new PMSRequestObject("privilegeManager", "removeCondition", [queryid, allColumnsDataMap.id]);
            $$.ajax(url, "requestString=" + encodeURIComponent(data.toJsonString()), function(data, textStatus, XMLHttpRequest){
                div.dialog("close");
                if (data.result) {
                    $(nTr).remove();
                    window.location.reload();
                }
            });
        }
        div.dialog({
            bgiframe: true,
            title: "提示",
            width: 320,
            buttons: {
                "确定": function(){
                    doDelete(data);
                },
                "取消": function(){
                    $(this).dialog("close");
                }
            },
            modal: true
        });
    }
}
var opArr = [editObj, delObj];
var currentStep = 0;
var maxLineNum = 0;
var treeFlag = true;
var obj;
var fileName;

/** 添加一列条件. */
function addLine(){
    var condition = $("#conditionDiv");
    condition.removeClass("hidden");
    maxLineNum = $("#addtable tr:last-child").children("td").html();
    var tableId = $("#addtable");
    if (maxLineNum == null) {
        maxLineNum = 1;
    }else {
        maxLineNum = parseInt(maxLineNum);
        maxLineNum += 1;
    }
    obj = $("#operationsHiddenForpur");
    fileName = $("#fileNameHiddenForpur");
    var _tr = tableId[0].insertRow(maxLineNum - 1);
    $(_tr).attr("id", "line" + maxLineNum);
    var td0 = _tr.insertCell(0);
    td0.innerHTML = maxLineNum;
    var td1 = _tr.insertCell(1);
    td1.innerHTML = "<select  onchange='changeFlag(this," + maxLineNum + ")' name='fileName' id='fileName" + maxLineNum + "'>" + (fileName.html()) + "</select>";
    var td2 = _tr.insertCell(2);
    td2.innerHTML = "<select name='operations' id='operations'>" + (obj.html()) + "</select>";
    var td3 = _tr.insertCell(3);
    td3.innerHTML = "<input id='fileValueForShow" + maxLineNum + "' name='fileValueForShow' type='text' readonly='readonly' class='inputtext_dis'/>" +
	    "<input id='fileValuePurTran" + maxLineNum + "' name='fileValuePurTran' type='hidden'/>" +
		"<input id='fileType" + maxLineNum + "' name='fileType' type='hidden'/>" +
	    "<input value='选择' class='buttonu' type='button' onclick='showDifTree(" + maxLineNum + ")'>"+
		"<input id='isCompatibal"+maxLineNum+"'name='isCompatibal'"+maxLineNum+" type='hidden'/>";
    var td4 = _tr.insertCell(4);
    td4.innerHTML = "<input type='button' id='operate" + maxLineNum + "' class='buttonu' value='删除' onclick='doDelete(this)' />";
}

/** 删除一列条件. */
function doDelete(button){
    maxLineNum -= 1;
    var seqtemp = $(button).parent().parent().children("td").html();
    currentStep = seqtemp;
    $("#addtable tr").each(function(){
        var seq = parseInt($(this).children("td").html());
        if (seq == currentStep) 
            $(this).remove();
        if (seq > currentStep) 
            $(this).children("td").each(function(i){
                if (i == 0) 
                    $(this).html(seq - 1);
            });
        if (maxLineNum == 0) {
            var condition = $("#conditionDiv");
            condition.addClass("hidden");
        }
    });
    currentStep = 0;
}

/** 获取组装条件. */
function getConditons(){
    var arr = [];
    var fileNameArr = $("select[name=fileName]");
    var operationArr = $("select[name=operations]");
    var fileValueArr = $("input[name*=fileValuePurTran]");
	var fileType = $("input[name*=fileType]");
	var isCompatibal = $("input[name*=isCompatibal]");
    for (var i = 0; i < fileNameArr.length; i++) {
        arr.push({
            fieldName: fileNameArr.eq(i).val(),
            operation: operationArr.eq(i).val(),
            fieldValue: fileValueArr.eq(i).val(),
			fieldType: fileType.eq(i).val(),
            privilege: priId,
			isCompatible:isCompatibal.eq(i).val()
        });
    }
    return arr;
}

/** 隐藏条件添加的table. */
function doDeleteTable(){
    var addTableTr = $("#addtable").children("tr");
    $("#addtable tr").each(function(){
        $(this).remove();
    });
    var condition = $("#conditionDiv");
    condition.addClass("hidden");
    maxLineNum = 0;
}

function doAddConditon(obj){
	var conditions = getConditons();
    doManager("privilegeManager", "addCondition", $.toJSON(conditions), function(data, textStatus, XMLHttpRequest){
        doDeleteTable();
        $$.executeSearch('conditionQuery', 'searchConDiv');
    }, true, {
        showWaiting: true
    });
    $(obj).dialog("close");
}

//改变类别时候清空原有的数值
function changeFlag(obj, number){
    $("#fileValueForShow" + number).val("");
    $("#fileValuePurTran" + number).val("");
    $("#isCompatibal" + number).val("");
	$("#fileType" + number).val("");
}


//添加条件时显示不同的树
function showDifTree(number){
    var obj = $("#fileName" + number);
    if (obj.val() == "") {
        $$.showMessage("${system.info}", "请先选择条件名称!");
    }
	//物采组织机构,需求组织机构
    if (obj.val().indexOf('orgCode')>0) {
        var codes = $("#fileValuePurTran" + number).val();
        var callback = function(name, code){
            $("#fileValueForShow" + number).val(code);
            $("#fileValuePurTran" + number).val(code);
			$("#fileType" + number).val("character");
        }
        openPrivilegeDialog(1, callback, codes);
    }else if(obj.val().indexOf('statisticsType')>0) {
		var privilegeType = obj.val();
		var privilegeName = "统计类型";
		var dictName = "project_stat_type";
		commonShowAdd(privilegeType,privilegeName,dictName,'checkbox',number);
		$("#fileType" + number).val("number");
	}
	
	/*
	//招标人,招标代理机构
    if (obj.val() == 'projectTenderPath' || obj.val() == 'tenderAgencyPath') {
        var codes = $("#fileValuePurTran" + number).val();
        var callback = function(name, code, deletevalue,compatible){
            $("#fileValueForShow" + number).val(code);
            $("#fileValuePurTran" + number).val(code);
			$("#isCompatibal"+number).val(compatible)
        }
        getBidOrgTree(callback, 'checkbox', codes,'bizbase','show');
    }
	//专家推荐单位
	if (obj.val() == 'recommandOrgPath') {
        var codes = $("#fileValuePurTran" + number).val();
        var callback = function(name, code,deletedValue,compatible){
            $("#fileValueForShow" + number).val(code);
            $("#fileValuePurTran" + number).val(code);
			$("#isCompatibal"+number).val(compatible)
        }
        getComOrgTreePrivilege(callback, codes, 2,userGroupId,'show');
    }
	//管理员组织机构
    if (obj.val() == 'managerOrg') {
        var codes = $("#fileValuePurTran" + number).val();
        var callback = function(name, code){
            $("#fileValueForShow" + number).val(code);
            $("#fileValuePurTran" + number).val(code);
        }
        openParentDialog(callback, codes);
    }
	//erp采购订单类型
	if(obj.val()=='erpProcType'){
		if(clientCode==''||clientCode==null){
			$$.showMessage("${system.info}", "Client Code为空!请检查该角色组所属组织结构数据!");
			return;
		}
		var codes = $("#fileValuePurTran"+number).val();
		var callback = function(name,code){
			$("#fileValueForShow"+number).val(code);
			$("#fileValuePurTran"+number).val(code);
		}
		erpProcTypeeDialog(callback,codes,clientCode);
	}
	//物料类别
    if (obj.val() == 'item') {
        var codes = $("#fileValuePurTran" + number).val();
        var callback = function(name, code){
            $("#fileValueForShow" + number).val(code);
            $("#fileValuePurTran" + number).val(code);
        }
        pirvilegeItemTreeDialog(callback, codes);
    }
	//项目类别
	if (obj.val() == 'projectType') {
		var privilegeType = obj.val();
		var privilegeName = "项目类别";
		var dictName = "projectType";
		commonShowAdd(privilegeType,privilegeName,dictName,'checkbox',number);
    }
	//供应商总部二级产品审批级别
	if(obj.val()=='approvalLevel'){
		var privilegeType = obj.val();
		var privilegeName = "供应商总部二级产品审批级别";
		var dictName = "sup_approvallevel";
		commonShowAdd(privilegeType,privilegeName,dictName,'checkbox',number);
	}
	//供应商总部一级级产品审批级别
	if(obj.val()=='materialLevel'){
		var privilegeType = obj.val();
		var privilegeName = "供应商总部一级级产品审批级别";
		var dictName = "sup_supprivilege_app";
		commonShowAdd(privilegeType,privilegeName,dictName,'checkbox',number);
	}
	//专家等级
    if (obj.val() == 'expertLevel') {
		var privilegeType = obj.val();
		var privilegeName = "专家等级";
		var dictName = "expertLevel";
		commonShowAdd(privilegeType,privilegeName,dictName,'checkbox',number);
    }
	//供应商管理级别
	if(obj.val()=='supplierLevel'){
		var privilegeType = obj.val();
		var privilegeName = "供应商管理级别";
		var dictName = "sup_supPrivilege";
		commonShowAdd(privilegeType,privilegeName,dictName,'radio',number);
	}
	//供应商准入方案级别
	if(obj.val()=='accessSchemeLevel'){
		var privilegeType = obj.val();
		var privilegeName = "供应商准入方案级别";
		var dictName = "sup_supPrivilege";
		commonShowAdd(privilegeType,privilegeName,dictName,'checkbox',number);
	}
	*/
}

//编辑条件时，显示物料树或者组织机构树
function showDifTreeEdit(){
    var objEdit = $("#fieldName2");
	//获取条件值用户标记已经选定的
    var codes = $("#fileValueForShowEdit").val();
	var dbValue = $("#valueInDB").val();
	//物采组织机构,需求组织机构
    if (objEdit.val().indexOf('orgCode')>0) {      
        var callback = function(name, code, deletedValue){
			var db = getAllCheckedNodeCode(code,deletedValue,dbValue);
            $("#fileValueForShowEdit").val(db);
            $("#fieldValue2").val(code);
            $("#deletedValue").val(deletedValue);
        }
        openPrivilegeDialog(1, callback, codes);
    }else if(objEdit.val().indexOf('statisticsType')>0) {
		var privilegeType = objEdit.val();
		var privilegeName = "统计类型";
		var dictName = "project_stat_type";
		commonShowEdit(privilegeType,privilegeName,dictName,'checkbox',codes);
	}
	
	/*
	//招标人,招标代理机构
    if (objEdit.val() == 'projectTenderPath' || objEdit.val() == 'tenderAgencyPath') {     
        var callback = function(name, code, deletedValue,isCompatibalDBValue){
			var db = getAllCheckedNodeCode(code,deletedValue,dbValue);
            $("#fileValueForShowEdit").val(db);
            $("#fieldValue2").val(code);
            $("#deletedValue").val(deletedValue);
			$("#isCompatibalDB").val(isCompatibalDBValue);
        }
		var isCompatibalValue = $("#isCompatibalDB").val();
        getBidOrgTree(callback, 'checkbox', codes,'bizbase','show',isCompatibalValue);
    }
	//专家推荐单位
	if (objEdit.val() == 'recommandOrgPath') {
        var callback = function(name, code, deletedValue,isCompatibalDBValue){
            var db = getAllCheckedNodeCode(code,deletedValue,dbValue,isCompatibalDBValue);
            $("#fileValueForShowEdit").val(db);
            $("#fieldValue2").val(code);
            $("#deletedValue").val(deletedValue);
			$("#isCompatibalDB").val(isCompatibalDBValue);
        }
		var isCompatibalValue = $("#isCompatibalDB").val();
        getComOrgTreePrivilege(callback, codes, 2,userGroupId,'show',isCompatibalValue);
    }
	//管理员组织机构
    if (objEdit.val() == 'managerOrg') {
        var callback = function(name, code){
            $("#fileValueForShowEdit").val(code);
            $("#fieldValue2").val(code);
        }
        openParentDialog(callback, codes);
    }
	//erp采购订单类型
	if (objEdit.val() == 'erpProcType') {
		if(clientCode==''||clientCode==null){
			$$.showMessage("${system.info}", "Client Code为空!请检查该角色组所属组织结构数据!");
			return;
		}
        var callback = function(name, code, deletedValue){
			var db = getAllCheckedNodeCode(code,deletedValue,dbValue);
            $("#fileValueForShowEdit").val(db);
            $("#fieldValue2").val(code);
            $("#deletedValue").val(deletedValue);
        }
       erpProcTypeeDialog(callback,codes,clientCode);
    }
	//物料类别
	if (objEdit.val() == 'item') { 
        var callback = function(name, code, deletedValue){
			var db = getAllCheckedNodeCode(code,deletedValue,dbValue);
            $("#fileValueForShowEdit").val(db);
            $("#fieldValue2").val(code);
            $("#deletedValue").val(deletedValue);
        }
        pirvilegeItemTreeDialog(callback, codes);
    }
	//项目类别
	if (objEdit.val() == 'projectType') {
		var privilegeType = objEdit.val();
		var privilegeName = "项目类别";
		var dictName = "projectType";
		commonShowEdit(privilegeType,privilegeName,dictName,'checkbox',codes);
    }
	//供应商总部二级产品审批级别
	if(objEdit.val()=='approvalLevel'){
		var privilegeType = objEdit.val();
		var privilegeName = "供应商总部二级产品审批级别";
		var dictName = "sup_approvallevel";
		commonShowEdit(privilegeType,privilegeName,dictName,'checkbox',codes);			
	}
	//供应商总部一级产品审批级别
	if(objEdit.val()=='materialLevel'){
		var privilegeType = objEdit.val();
		var privilegeName = "供应商总部一级产品审批级别";
		var dictName = "sup_supprivilege_app";
		commonShowEdit(privilegeType,privilegeName,dictName,'checkbox',codes);
	}
	//专家等级
    if (objEdit.val() == 'expertLevel') {
		var privilegeType = objEdit.val();
		var privilegeName = "专家等级";
		var dictName = "expertLevel";
		commonShowEdit(privilegeType,privilegeName,dictName,'checkbox',codes);
    }
	//供应商管理级别
    if(objEdit.val()=='supplierLevel'){
		var privilegeType = objEdit.val();
		var privilegeName = "供应商管理级别";
		var dictName = "sup_supPrivilege";
		commonShowEdit(privilegeType,privilegeName,dictName,'radio',codes);
	}
	//供应商准入方案级别
	if(objEdit.val()=='accessSchemeLevel'){
		var privilegeType = objEdit.val();
		var privilegeName = "供应商准入方案级别";
		var dictName = "sup_supPrivilege";
		commonShowEdit(privilegeType,privilegeName,dictName,'checkbox',codes);
	}
	*/
}

//编辑时改变条件则清空原有的页面上显示条件的文本框的数据
function changEditFlag(obj){
    $("#fileValueForShowEdit").val("");
    $("#fieldValue2").val("");
	$("isCompatibalDB").val("");
}
/**
 * 通用添加权限显示对话框
 * ZhaoQingdong 2012-9-28
 * @param {Object} privilegeType 权限类型
 * @param {Object} privilegeName 权限名称
 * @param {Object} dictName 字典表名
 * @param {Object} checkStyle 选择类型
 * @param {Object} number 添加权限行数
 */
function commonShowAdd(privilegeType,privilegeName,dictName,checkStyle,number){
	var dicts = "";
	var treeNodes = [];
	doManager("privilegeManager", "getListByUserPrivilege", [privilegeType,dictName], function(datas, textStatus, XMLHttpRequest) {
		if (datas.result) {
			dicts = $.fromJSON(datas.data);
		}
	},false);
	if(dicts.length==0){
		$$.showMessage("${system.info}","你没有分配"+privilegeName+"的权限!");
		return false;
	}
	var codes = $("#fileValuePurTran"+number).val();
	$(dicts).each(function(i,element){
		var flag = false;
		if(codes.indexOf(element.dictCode)!=-1){
			flag = true;
		}
		treeNodes.push({
			name:element.dictText,
			id:element.dictCode,
			checked:flag,
		});
	});
	showDictWin(treeNodes,function(btn,node){
		if(btn){
			var code = "";
			for(var i=0;i<node.length;i++){
				code=code+","+node[i].id;
			}
			code = code.substring(1,code.length);
			$("#fileValueForShow"+number).val(code);
			$("#fileValuePurTran"+number).val(code);
		}},{
		title:privilegeName,
		checkStyle: checkStyle
	});
}
/**
 * 通通用编辑权限显示对话框
 * ZhaoQingdong 2012-9-28
 * @param {Object} privilegeType 权限类型
 * @param {Object} privilegeName 权限名称
 * @param {Object} dictName 字典表名
 * @param {Object} checkStyle 选择类型
 * @param {Object} codes 已分配权限字符串
 */
function commonShowEdit(privilegeType,privilegeName,dictName,checkStyle,codes){
	var dbValue = $("#valueInDB").val();
	var dicts = "";
	var treeNodes = [];
	doManager("privilegeManager", "getListByUserPrivilege", [privilegeType,dictName], function(datas, textStatus, XMLHttpRequest) {
		if (datas.result) {
			dicts = $.fromJSON(datas.data);
		}
	},false);
	if(dicts.length==0){
		$$.showMessage("${system.info}","你没有分配"+privilegeName+"的权限!");
		return false;
	}
	$(dicts).each(function(i,element){
		var flag = false;
		if(codes.indexOf(element.dictCode)!=-1){
			flag = true;
		}
		treeNodes.push({
			name:element.dictText,
			id:element.dictCode,
			checked:flag,
		});
	});

	showDictWin(treeNodes, function(btn, node, uncheckedNode){
        if (btn) {
            var code = "";
            var deletevalue = "";
            for (var i = 0; i < node.length; i++) {
                code = code + "," + node[i].id ;
            }
            for (var i = 0; i < uncheckedNode.length; i++) {
                deletevalue = deletevalue+","+uncheckedNode[i].id;
            }
            code = code.substring(1, code.length);
            deletevalue = deletevalue.substring(1, deletevalue.length);
			var db = getAllCheckedNodeCode(code,deletevalue,dbValue);
            $("#fileValueForShowEdit").val(db);
            $("#fieldValue2").val(code);
            $("#deletedValue").val(deletevalue);
        }
    }, {
        title:privilegeName,
		checkStyle: checkStyle
    });
}
