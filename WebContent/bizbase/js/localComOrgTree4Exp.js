/**
 * type 1 radio 否则 checkBox
 * 组装json并调用回调函数
 * @param {Object} callback
 */
function getComOrgTreePrivilege(callback,exsitNode,type,usergroupId,showIsCom,isCompatibalValue){
    var div = $("<div></div>");
	if(showIsCom=='show'){
		if(isCompatibalValue==1){
			div.append($("<label>向下兼容 </label><input type='checkBox' checked=true value='1' name='isCompatibleCom' id='isCompatibleCom'><div style='padding:2px'></div>"
			+"<ul id='treeDemo' class='tree' style='overflow:auto;width:auto;height:200px' ></ul>"));
		}else{
			div.append($("<label>向下兼容 </label><input type='checkBox' value='1' name='isCompatibleCom' id='isCompatibleCom'><div style='padding:2px'></div>"
			+"<ul id='treeDemo' class='tree' style='overflow:auto;width:auto;height:200px' ></ul>"));
		}
	}else{
		div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:auto;height:200px' ></ul>");
	}
    var ztree;
    var json = '[';
    div.dialog({
        bgiframe: true,
        title: "专家推荐单位",
        width: 500,
        height: 300,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
                var name = "";
				var code = "";
				var deletedValue =""; 
				var unSelect = ztree.getCheckedNodes(false);
                for (var i = 0; i < refNodes.length; i++) {
                    name = name+","+refNodes[i].name;
					code = code+","+refNodes[i].code;
                }
				for (var i = 0; i < unSelect.length; i++) {
					deletedValue = deletedValue+","+ unSelect[i].code;
                }
				name = name.substring(1,name.length);
				code = code.substring(1,code.length);
				deletedValue = deletedValue.substring(1,deletedValue.length);
				var isCompatible2 =0;
				if($("input[name='isCompatibleCom']").attr('checked')){
					isCompatible2 = $("input[name='isCompatibleCom']").val();
				}
                if (callback && typeof(callback) == 'function') {
                    callback(name,code,deletedValue,isCompatible2);
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
    ztree = generatLocalComOrgTree(exsitNode,type,usergroupId);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}
/**
 * type 1 radio 否则 checkBox
 * 组装json并调用回调函数
 * @param {Object} callback
 */
function getLocalComOrgTree(callback,exsitNode,type){
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>");
    var ztree;
    var json = '[';
    div.dialog({
        bgiframe: true,
        title: "专家推荐单位",
        width: 500,
        height: 300,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
                if (refNodes.length > 0) {
                    for (var i = 0; i < refNodes.length; i++) {
                        json += '{"name":"' + refNodes[i].name + '","id":"' + refNodes[i].id + '","code":"' + refNodes[i].code + '"},';
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
    ztree = generatLocalComOrgTree(exsitNode,type);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

/**
 * 
 *
 * @param {Object} type
 */
function generatLocalComOrgTree(exsitNode,type,usergroupId){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
	if(exsitNode == ''){
		exsitNode = null;
	}
	if(undefined==usergroupId||null==usergroupId||''==usergroupId){
		myData = $.toJSON({
	        managerName: "purStruOrgManager",
	        methodName: "getChildForPrivilege",
	        parameters: [exsitNode,true]
    	});
	}else{
		myData = $.toJSON({
        managerName: "purStruOrgManager",
        methodName: "getLocOrgForExpPirvilege",
        parameters: [exsitNode,true,usergroupId,'false']
    });
	}
	var checkType = "radio";
	if(type!=1){
		checkType="checkbox"	
	}
    setting = {
        checkable: true,
        checkStyle: checkType,
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
//            click: zTreeOnClick
        }
    }
    
//    function zTreeOnClick(event, treeId, treeNode){
//        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
//            var data = new PMSRequestObject("purStruOrgManager", "getLocalCom4Exp", [treeNode.id,exsitNode]);
//            $$.ajax($$.PMSDispatchActionUrl, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest){
//                if (data.result) {
//                    newNodes = eval("(" + data.data + ")");
//                    addNode(newNodes, treeNode);
//                }
//            });
//        }
//    }
    
    function beforeExpand(treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            if (treeNode.flag != true) {
                var data = new PMSRequestObject("purStruOrgManager", "getLocalCom4Exp", [treeNode.id,exsitNode]);
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
