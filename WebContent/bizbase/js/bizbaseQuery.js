/*
 * @methodName     : getTenderInterface
 * @methodParam    : Object
 * @methodReturn   : String
 * @author         : kangguolong
 * @desc           : 获取所选节点下的所有id
 * @interfaceDesc  : "基础模块"的接口函数
 * @ref Javascript : bizbase/js/refDialog.js
 * @for example    : getTenderInterface({inputId:"文本框Id"});
 */
function getTenderInterface(_cfg, _this){
    openDialogWithJson(function(_result){
        var result = $.fromJSON(_result)[0];
        if (_cfg.inputId != null) {
            $("#" + _cfg.inputId).val("");
            doManager("purStruOrgManager", "getPurOrgDTOById", result.id, function(datas, textStatus, XMLHttpRequest){
                var orgObj = $.fromJSON(datas.data);
//                $("#" + _cfg.inputId).val(orgObj.id);
				$("#" + _cfg.inputId).val(orgObj.path+"%");
            });
        }
        
        if (_cfg.inputName != null) {
            $("#" + _cfg.inputName).val(result.name);
        }
        
        if ($("#" + _cfg.inputName).val() != "") {
            $(_this).next().remove();
        }
        
    }, "1", _cfg.dataPrivilege)
}

/**
 * 组装json并调用回调函数
 * @comments : 该方法中orgTreeType,checkType参数已失效.
 * @comments : 重新定义checkType为数据权限标识,有该标识则组织机构树全部加载
 * @editBy   : kangguolong (2013-04-09) 有需要改此方法时请通知
 * @param {Object} callback
 * @param {String} checkType: 可选类型"check","radio"
 */
function openDialogWithJson(callback, orgTreeType, checkType){
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>");
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
    //checkType = (checkType == "radio" ? "1" : "2");
    ztree = generatTree(checkType);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

/**
 * @comments : type为true时加载所有组织机构,其它时加载所属单位以及下级组织机构.
 * @param {Object} type
 */
function generatTree(type){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
	if(type == 'true'){
		myData = $.toJSON({
	        managerName: "purStruOrgManager",
	        methodName: "getChild",
	        parameters: ['true']
	    });
	}else{
		myData = $.toJSON({
	        managerName: "purStruOrgManager",
	        methodName: "getChild",
	        parameters: ['false']
	    });
	}
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
    
    function zTreeOnClick(event, treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            var data = new PMSRequestObject("purStruOrgManager", "getChildsByParentId", [treeNode.id]);
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
                var data = new PMSRequestObject("purStruOrgManager", "getChildsByParentId", [treeNode.id]);
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
