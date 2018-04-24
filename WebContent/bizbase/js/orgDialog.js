var entityOrgFlag = "";
function startWith(a, b){
    if (a.length < b.length) {
        return -1;
    }
    for (var i = 0; i < a.length; i++) {
        if (b == a.charAt(i)) {
            return i;
        }
    }
    return -1;
}

/**
 *
 * @param {Object} callback 回调函数
 * @param {Object} checkType 单选("radio")多选：随意
 * @param {Object} scope 组织机构范围:如果要找到所有的则”all“，否则传递"other"
 * @param {Object} orgCode 指定组织机构code
 * @param {Object} entityOrgFlag 子节点显示到的层级 0(机关单位)、1(分院)、2(科研单位,所) 3、室
 * @param {Object} checkCodeStr 以选择的组织机构的code
 */
function orgDialog(callback, checkType, scope, orgCode, entityOrgFlag, checkCodeStr, entityOrgFlagNotShow){
    if (!checkCodeStr) {
        checkCodeStr = "";
    }
    orgDialogWithJson(callback, checkType, scope, orgCode, entityOrgFlag, checkCodeStr, entityOrgFlagNotShow);
}

function orgDialogWithJson(callback, checkType, scope, orgCode, entityOrgFlag, checkCodeStr, entityOrgFlagNotShow){
    if (!checkCodeStr) {
        checkCodeStr = "";
    }
    if (!entityOrgFlagNotShow) {
        entityOrgFlagNotShow = "";
    }
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow-y:scroll;width:100%;height:100%' ></ul>");
    var ztree;
    var json = '[';
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 500,
        height: 300,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
                ztree.getnode
                if (refNodes.length > 0) {
                    for (var i = 0; i < refNodes.length; i++) {
                        var temp = refNodes[i].name;
                        var p = startWith(temp, ')');
                        temp = temp.substring(p + 1, temp.length);
                        json += '{"name":"' + temp + '","id":"' + refNodes[i].id + '","code":"' + refNodes[i].code + '"},';
                    }
                    json = json.substr(0, json.length - 1) + "]";
                    if (callback && typeof(callback) == 'function') {
                        callback(json);
                    }
                    div.dialog("close");
                    div.remove();
                }
            },
            "取消": function(){
                div.dialog("close");
                div.remove();
            }
        },
        modal: true,
        closeOnEscape: false,
        open: function(event, ui){
            $(".ui-dialog-titlebar-close").hide();
        }
    });
    checkType = (checkType == "radio" ? "1" : "2");
    ztree = myGeneratTree(checkType, scope, orgCode, entityOrgFlag, checkCodeStr, entityOrgFlagNotShow);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

function myGeneratTree(type, scope, orgCode, entityOrgFlag, checkCodeStr, entityOrgFlagNotShow){
    if (!checkCodeStr) {
        checkCodeStr = "";
    }
    entityOrgFlag = entityOrgFlag;
    var zcheckCodeStr = checkCodeStr;
    if (zcheckCodeStr != "") {
        if (zcheckCodeStr.charAt(0) != ",") {
            zcheckCodeStr = "," + zcheckCodeStr;
        }
        if (zcheckCodeStr.charAt(zcheckCodeStr.length - 1) != ",") {
            zcheckCodeStr = zcheckCodeStr + ",";
        }
    }
    var zNotShowStr = entityOrgFlagNotShow;
    if (zNotShowStr != "") {
        if (zNotShowStr.charAt(0) != ",") {
            zNotShowStr = "," + zNotShowStr;
        }
        if (zNotShowStr.charAt(zNotShowStr.length - 1) != ",") {
            zNotShowStr = zNotShowStr + ",";
        }
    }
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
    
    myData = $.toJSON({
        managerName: "purStruOrgManager",
        methodName: "getOrgByParam",
        parameters: [scope, orgCode, entityOrgFlag]
    });
    
    //单选
    if (type == '1') {
        setting = {
            checkable: true,
            checkStyle: "radio",
            checkRadioType: "all",
            async: true,
            asyncParam: ["name", "id"],
            asyncParamOther: ["requestString", myData],
            checkType: {
                "Y": "",
                "N": ""
            },
            callback: {
                beforeExpand: beforeExpand
            }
        }
        
        //多选
    }
    else {
        setting = {
            checkable: true,
            checkStyle: "checkbox",
            async: true,
            asyncParam: ["name", "id"],
            asyncParamOther: ["requestString", myData],
            checkType: {
                "Y": "",
                "N": ""
            },
            callback: {
                beforeExpand: beforeExpand
            }
        }
    }
    function beforeExpand(treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            if (treeNode.flag != true) {
                var data = new PMSRequestObject("purStruOrgManager", "getOrgByParam", ["other", treeNode.code, entityOrgFlag]);
                $$.ajax($$.PMSDispatchActionUrl, "requestString=" + encodeURIComponent(data.toJsonString()), function(data, textStatus, XMLHttpRequest){
                    if (data.result) {
                        newNodes = eval("(" + data.data + ")");
                        //根据传入的选择ID自动选择
                        $.each(newNodes, function(i, tmpObj){
                            if (zcheckCodeStr.indexOf("," + tmpObj.id + ",") != -1) {
                                tmpObj.checked = true;
                            }
                        });
                        addNode(newNodes, treeNode);
                    }
                });
                treeNode.flag = true;
            }
        }
        else {
            return true;
        }
        return false;
    }
    function addNode(nodes, parNode){
        var isAddNode = true;
        for (i = 0; i < nodes.length; i++) {
            if (zNotShowStr.indexOf("," + nodes[i].entityOrgFlag + ",") != -1) {
                isAddNode = false;
            }
            if (isAddNode) {
                var newNode = nodes[i];
                zTree1.addNodes(parNode, newNode);
            }
            isAddNode = true;
        }
    }
    
    var url = $$.PMSDispatchActionUrl;
    setting.asyncUrl = url;
    zTree1 = $("#treeDemo").zTree(setting, zNodes);
    setTimeout(function(){
        //处理根结点选中状态
        var tmpAllNodes = zTree1.getNodes();
        $.each(tmpAllNodes, function(i, tmpObj){
            if (zcheckCodeStr.indexOf("," + tmpObj.id + ",") != -1) {
                tmpObj.checked = true;
                zTree1.updateNode(tmpObj, true);
            }
        });
        
    }, 300);
    return zTree1;
    
}
