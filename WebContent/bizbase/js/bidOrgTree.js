
/**
 * 组装json并调用回调函数
 * @param {Object} callback
 * @param {String} checkType: 可选类型"check","radio"
 */
function getBidOrgTree(callback, checkType,existNodes,forWhere,showIsCom,isCompatibalValue){
    var div = $("<div></div>");
	if(showIsCom=='show'){
		if(isCompatibalValue==1){
			div.append($("<label>向下兼容 </label><input type='checkBox' checked=true value='1' name='isCompatible' id='isCompatible'><div style='padding:2px'></div>"
			+"<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>"));
		}else{
			div.append($("<label>向下兼容 </label><input type='checkBox' value='1' name='isCompatible' id='isCompatible'><div style='padding:2px'></div>"
			+"<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>"));
		}
	}else{
		div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>");
	}
    var ztree;
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 500,
        height: 330,
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
					var isCompatible2 =0;
					if($("input[name='isCompatible']").attr('checked')){
						isCompatible2 = $("input[name='isCompatible']").val();
					}
					deletedValue = deletedValue.substring(1,deletedValue.length);
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
    checkType = (checkType == "radio" ? "1" : "2");
    ztree = generatBidOrgTree(checkType,existNodes,forWhere);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

/**
 * 通过type组件tree type==1单选  否则 多选,
 * 
 * @param {Object} type
 */
function generatBidOrgTree(type,existNodes,forWhere){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
	if(existNodes == ''){
		existNodes = null;
	}
	if(forWhere=='bid'){
		myData = $.toJSON({
        managerName: "purStruOrgManager",
        methodName: "getChildForPrivilege",
        parameters: [existNodes,true]
    	});
	}else if(forWhere=='bidForLocalOrg'){
		myData = $.toJSON({
	        managerName: "purStruOrgManager",
	        methodName: "getLocOrgForExpPirvilege",
	        parameters: [null,true,bidModuleCurrUsergroupId,true]
	    });
	}else{
		myData = $.toJSON({
        managerName: "purStruOrgManager",
        methodName: "getChildForPrivilege",
        parameters: [existNodes,'false']
    	});
	}
    
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
            var data = new PMSRequestObject("purStruOrgManager", "getBidChild", [treeNode.id,existNodes]);
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
                var data = new PMSRequestObject("purStruOrgManager", "getBidChild", [treeNode.id,existNodes]);
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
/*
     * @function : 获取招标管理单位树
     * @author   ：WangDong
     * @since    : 2011-11-25
     */
function getBidManagerOrg(type,callback){
	var zTreeObj;
    var setting;
    var myData;
    var zNodes = [];
    /*
     * @function : 初始化ztree相关参数
     * @author   ：WangDong
     * @since    : 2011-11-25
     */
    switch (type) {
        case 1:{//获取招标管理单位树
            myData = {
				managerName: "purStruOrgManager",
				methodName: "getBidManagerParentNode",
				parameters: []
			}
            break;
        }
    }
    switch (type) {
		case 1:{
            setting = {
                checkable: true,
                checkStyle: "radio",
                checkRadioType: "all",
                async: true,
                asyncParam: ["id","name"],
                asyncParamOther: ["requestString", $.toJSON(myData)],
                callback: {
                    beforeExpand: beforeExpand,
                    nodeCreated: zTreeOnNodeCreated
                }
            }
            break;
        }
    }
	/*
     * @funciton       : 节点创建后触发
     * @param event    : event
     * @param treeId   : tree id
     * @param treeNode : treeNode
     * @author         : WangDong
     * @since          : 2011-11-28
     */
	function zTreeOnNodeCreated(event, treeId, treeNode) {
        if (treeNode.isBidManageOrg != 1) {
            $('#' + treeNode.tId).find('button:eq(1)').remove();
        }
	}
    /*
     * @function : 若为父节点则展开其下的子节点
     * @author   : WangDong
     * @since    : 2011-11-25
     */
    function beforeExpand(treeId, treeNode){
        if (treeNode.isParent) {
            switch (type) {
                case 1:{
                    myData.methodName="getBidManagerChildNode";
                    myData.parameters=[treeNode.id];
                    break;
                }
            }
            setting.asyncParamOther = ["requestString", $.toJSON(myData)];
            zTreeObj.updateSetting(setting);
            return true;
        }
        return false;
    }
	var div = $("<div></div>");
	div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>");
    div.dialog({
        bgiframe: true,
        title: "招标管理单位组织机构选择",
        width: 500,
        height: 330,
        buttons: {
            "确定": function(){
                var refNodes = zTreeObj.getCheckedNodes();
					if(refNodes.length > 0){
						 callback(refNodes[0].code,refNodes[0].name);
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
	var url = $$.PMSDispatchActionUrl;
    setting.asyncUrl = url;
    zTreeObj = $("#treeDemo").zTree(setting, []);
}
