function openItemTreeDialog(callback, checkType){
    var div = $("<div></div>");
	var ztree;
    var json = '[';
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:295px;width:auto;height:200px' ></ul>");
    id = "treeDemo";
    div.dialog({
        bgiframe: true,
        title: "物料选择",
        width: 320,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
                if (refNodes.length > 0) {
                    for (var i = 0; i < refNodes.length; i++) {
                        json += '{"name":"' + refNodes[i].name + '","code":"' + refNodes[i].code + '"},';
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
        modal: true
    });
    checkType = (checkType == "radio" ? "1" : "2");
    ztree = generatItemTree(checkType, id);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

function showItemTree(id, checkType){
	var ztree;
    var json = '[';
	checkType = (checkType == "radio" ? "1" : "2");
    ztree = generatItemTree(checkType, id);
    return ztree;
}

function generatItemTree(checkType, id){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
    myData = $.toJSON({
        managerName: "itemManager",
        methodName: "getDefaultItemNodes",
        parameters: ""
    });
    if (checkType == '1') {//单选
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
                beforeExpand: beforeExpand,
                click: zTreeOnClick
            }
        }
    }
    else { //多选
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
                click: zTreeOnClick,
                beforeExpand: beforeExpand
            }
        }
    }
    function zTreeOnClick(event, treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            var data = new PMSRequestObject("itemManager", "getChildrenByParentId", [treeNode.id]);
            $$.ajax($$.PMSDispatchActionUrl, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest){
                if (data.result) {
                    newNodes = eval("(" + data.data + ")");
                    addNode(newNodes, treeNode);
                }
            });
        }
    }
    function beforeExpand(treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            var data = new PMSRequestObject("itemManager", "getChildrenByParentId", [treeNode.id]);
            $$.ajax($$.PMSDispatchActionUrl, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest){
                if (data.result) {
                    newNodes = eval("(" + data.data + ")");
                    addNode(newNodes, treeNode);
                }
            });
        }
        else {
            return true;
        }
        return false;
    }
    function addNode(nodes, parNode){
        for (i = 0; i < nodes.length; i++) {
            var newNode = nodes[i];
            zTree1.addNodes(parNode, newNode);
        }
    }
    var url = $$.PMSDispatchActionUrl;
    setting.asyncUrl = url;
    zTree1 = $("#" + id).zTree(setting, zNodes);
    
    return zTree1;
    
}
