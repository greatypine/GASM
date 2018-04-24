/**
 *treeType 1物采组织机构,2为专家推荐单位
 */
function openPrivilegeDialog(orgTreeType, callback,existNodes){
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%'></ul>");
    var ztree;
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 500,
		height:320,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
				var unSelect = ztree.getCheckedNodes(false);
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
    if (orgTreeType ==1) {
        ztree = generatTree(1,existNodes);
    }
    else {
        ztree = generatTree(2,existNodes);
    }
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}
/**
 * 
 * treeType 1为物采组织机构,2为专家推荐单位
 * @param {Object} type
 */
function generatTree(treeType,existNodes){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
	if(existNodes == ''){
		existNodes = null;
	}
    if (treeType == 1) {
        myData = $.toJSON({
                    managerName: "purStruOrgManager",
                    methodName: "getChildForPrivilege",
                    parameters: [existNodes,'false']
                });
    }
    else {
		myData = $.toJSON({
                    managerName: "purStruOrgManager",
                    methodName: "getChildForPrivilege",
                    parameters: [existNodes,true]
                });
	}
   //多选
    setting = {
        checkable: true,
        checkStyle: "checkbox",
        async: true,
        asyncParam: ["name", "id"],
        asyncParamOther: ["requestString", myData],
        checkType: {
             Y:"s",
			 N:"s"
        },
        callback: {
            click: zTreeOnClick,
            beforeExpand: beforeExpand
        }
    }
    
    function zTreeOnClick(event, treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
			if(treeNode.flag!=true){
	            var data = new PMSRequestObject("purStruOrgManager", "getPrivilegeOrg", [treeNode.id,existNodes]);
	            $$.ajax($$.PMSDispatchActionUrl, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest){
	                if (data.result) {
	                    newNodes = eval("(" + data.data + ")");
	                    addNode(newNodes, treeNode);
	                }
	            });
				treeNode.flag = true;
			}
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

