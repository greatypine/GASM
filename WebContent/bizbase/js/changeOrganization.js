/**
 * @author kangguolong
 */
function openParentDialog(callback,existNodes){
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:300px;height:200px'></ul>");
    var ztree2;
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 320,
        buttons: {
            "确定": function(){
                var refNodes = ztree2.getCheckedNodes();
				var unSelect = ztree2.getCheckedNodes(false);
                var name = "";
				var code = "";
				var deletedValue ="";
                for (var i = 0; i < refNodes.length; i++) {
                    name = name+","+refNodes[i].name;
					code = code+","+refNodes[i].code;
                }
				for (var i = 0; i < unSelect.length; i++) {
					deletedValue = deletedValue+","+unSelect[i].code;
                }
				name = name.substring(1,name.length);
				code = code.substring(1,code.length);
				deletedValue = deletedValue.substring(1,deletedValue.length);
                if (callback && typeof(callback) == 'function') {
                    callback(name,code,deletedValue);
                }
                div.dialog("close");
                div.remove();
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
    ztree2= generatTree4Choose(existNodes);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

function generatTree4Choose(existNodes){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
	if(existNodes == ''){
		existNodes = null;
	}
    myData = $.toJSON({
        managerName: "purStruOrgManager",
        methodName: "getChildForPrivilege",
        parameters: [existNodes,'false']
    });
    setting = {
        checkable: true,
        checkStyle: "radio",
        checkRadioType: "all",
        async: true,
        asyncParam: ["name", "id"],
        asyncParamOther: ["requestString", myData],
        callback: {
            beforeExpand: beforeExpand,
            click: zTreeOnClick
        }
    }

    function zTreeOnClick(event, treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            var data = new PMSRequestObject("purStruOrgManager", "getPrivilegeOrg", [treeNode.id,existNodes]);
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
            if (treeNode.flag != true) {
                var data = new PMSRequestObject("purStruOrgManager", "getPrivilegeOrg", [treeNode.id,existNodes]);
                $$.ajax($$.PMSDispatchActionUrl, "requestString=" + encodeURIComponent(data.toJsonString()), function(data, textStatus, XMLHttpRequest){
                    if (data.result) {
                        newNodes = eval("(" + data.data + ")");
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
        for (i = 0; i < nodes.length; i++) {
            var newNode = nodes[i];
            zTree1.addNodes(parNode, newNode);
        }
    }
    var url = $$.PMSDispatchActionUrl;
    setting.asyncUrl = url;
    zTree1 = $("#treeDemo").zTree(setting, zNodes);
    return zTree1;
    
}
