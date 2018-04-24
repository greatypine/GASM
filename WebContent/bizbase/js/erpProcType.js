function erpProcTypeeDialog(callback,exsitNode,clientCode){
    var div = $("<div></div>");
	var ztree;
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:295px;width:auto;height:200px' ></ul>");
    id = "treeDemo";
    div.dialog({
        bgiframe: true,
        title: "ERP采购订单类型",
        width: 440,
        buttons: {
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
    ztree = generatERPPROTree(id,exsitNode,clientCode);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}
function generatERPPROTree(id,exsitNode,clientCode){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
	if(exsitNode == ''){
		exsitNode = null;
	}
    myData = $.toJSON({
        managerName: "ERPProTypeManager",
        methodName: "getErpProTypeDTOList",
        parameters: [clientCode,exsitNode]
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
            }
        }
    var url = $$.PMSDispatchActionUrl;
    setting.asyncUrl = url;
    zTree1 = $("#" + id).zTree(setting, zNodes);
    
    return zTree1;
    
}
