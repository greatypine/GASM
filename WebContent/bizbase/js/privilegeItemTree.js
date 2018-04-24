function pirvilegeItemTreeDialog(callback,exsitNode){
    var div = $("<div></div>");
	var ztree;
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:395px;width:auto;height:400px' ></ul>");
    id = "treeDemo";
    div.dialog({
        bgiframe: true,
        title: "物料选择",
        width: 440,
        buttons: {
			"分配全部权限":function(){
				ztree.checkAllNodes(true);
			},
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
				var deleteNodes = ztree.getCheckedNodes(false);
					var name = "";
					var code = "";
					var deletedValue="";
					if(refNodes.length>0){
						for (var i = 0; i < refNodes.length; i++) {
                        name = name+","+refNodes[i].name;
						code = code+","+refNodes[i].code;
                    }
					}
                    if(deleteNodes.length>0){
						for (var i = 0; i < deleteNodes.length; i++) {
						deletedValue = deletedValue+","+deleteNodes[i].code;
                    }
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
    ztree = generatItemTree(id,exsitNode);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}
function generatItemTree(id,exsitNode){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
	if(exsitNode == ''){
		exsitNode = null;
	}
	
    myData = $.toJSON({
        managerName: "itemManager",
        methodName: "getPrivilegeItemNodes",
        parameters: [exsitNode]
    });
        setting = {
            checkable: true,
            checkStyle: "checkbox",
            async: true,
            asyncParam: ["name", "id"],
            asyncParamOther: ["requestString", myData],
            checkType: {
               Y:"",
			   N:""
            },
            callback: {
                click: zTreeOnClick,
                beforeExpand: beforeExpand,
            }
        }
    function zTreeOnClick(event, treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            var data = new PMSRequestObject("itemManager", "getChildrenPrivilegeItemNodes", [treeNode.id,exsitNode]);
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
            var data = new PMSRequestObject("itemManager", "getChildrenPrivilegeItemNodes", [treeNode.id,exsitNode]);
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
