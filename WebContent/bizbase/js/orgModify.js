function showClientQuery(){
//  client号不允许修改
//	doEditCssType();
//	var tempValue = $("#client").val();
//    $$.executeSearch('erpClientQuery', 'searchConditionDiv');
//    $("#erpClientInfo").dialog({
//        bgiframe: true,
//        title: 'client选择',
//        width: 600,
//        height: 400,
//        modal: true,
//        buttons: {
//            "${form.ui.ok}": function(){
//                var selects = $$.getSelectedObj("erpClientQuery");
//                if (selects.length > 0) {
//					var codes = "";
//                    for(var i=0;i<selects.length;i++){
//						codes=codes+selects[i][2]+",";
//					}
//                    $("#client").val(codes.substring(0,codes.length-1));
//					if(tempValue!=(codes.substring(0,codes.length-1))){
//						$("#ERPC").val('');
//						$("#ERPO").val('');
//						$("#ERPG").val('');
//					}
//					$("#erpClientInfo").dialog('close');
//                }else{
//					$("#erpClientInfo").dialog('close');
//				}
//            },
//            "${query.ui.cancel}": function(){
//                $("#erpClientInfo").dialog('close');
//            }
//        }
//    });
}

function searchCompany(){
	doEditCssType();
	var client = $("#client").val();
	if(''==client||null==client){
		$$.showMessage('系统信息','请先选择client');
		return false;
	}else{
		$("#searchConditionDiv2").find('[name=erpClientCode]').val(client);
		$$.executeSearch('erpCompanyQuery', 'searchConditionDiv2');
	}
    $("#erpCompanyQuery").dialog({
        bgiframe: true,
        title: '公司代码选择',
        width: 600,
        height: 400,
        modal: true,
        buttons: {
			"${query.ui.reset}": function(){
				$("#ERPC").val("");
                $("#erpCompanyQuery").dialog('close');
            },
            "${form.ui.ok}": function(){
                var selects = $$.getSelectedObj("erpCompanyQuery");
                if (selects.length > 0) {
					var codes="";
                    for(var i=0;i<selects.length;i++){
						codes=codes+selects[i][1]+",";
					}
                    $("#ERPC").val(codes.substring(0,codes.length-1));
					$("#erpCompanyQuery").dialog('close');
                }else{
					$("#erpCompanyQuery").dialog('close');
				}
            },
            "${query.ui.cancel}": function(){
                $("#erpCompanyQuery").dialog('close');
            }
        }
    });
}

function searchPurchaseOrg(){
	doEditCssType();
	var client = $("#client").val();
	if(''==client||null==client){
		$$.showMessage('系统信息','请先选择client');
		return false;
	}else{
		$("#ConditonMainDiv4").find('[name=erpClientCode]').val(client);
		$$.executeSearch('erpPurchaseOrgQuery', 'searchConditionDiv4');
	}
    $("#erpPurchaseOrgQuery").dialog({
        bgiframe: true,
        title: 'ERP采购组织选择',
        width: 600,
        height: 400,
        modal: true,
        buttons: {
			"${query.ui.reset}": function(){
				$("#ERPO").val("");
                $("#erpPurchaseOrgQuery").dialog('close');
            },
            "${form.ui.ok}": function(){
                var selects = $$.getAllSelectedObjects("erpPurchaseOrgQuery");
                if (selects.length > 0) {
					var codes="";
                    for(var i=0;i<selects.length;i++){
						codes=codes+selects[i].purchaseOrgCode+",";
					}
                    $("#ERPO").val(codes.substring(0,codes.length-1));
					$("#erpPurchaseOrgQuery").dialog('close');
                }else{
					$("#erpPurchaseOrgQuery").dialog('close');
				}
            },
            "${query.ui.cancel}": function(){
                $("#erpPurchaseOrgQuery").dialog('close');
            }
        }
    });
}
function serachPurchaseGroup(){
	doEditCssType();
	var client = $("#client").val();
	if(''==client||null==client){
		$$.showMessage('系统信息','请先选择client');
		return false;
	}else{
		$("#ConditonMainDiv3").find('[name=erpClientCode]').val(client);
		$$.executeSearch('erpPurchaseQuery', 'searchConditionDiv3');
	}
    $("#erpPurchaseQuery").dialog({
        bgiframe: true,
        title: 'ERP采购组选择',
        width: 600,
        height: 400,
        modal: true,
        buttons: {
			"${query.ui.reset}": function(){
				$("#ERPG").val("");
                $("#erpPurchaseQuery").dialog('close');
            },
            "${form.ui.ok}": function(){
                var selects = $$.getAllSelectedObjects("erpPurchaseQuery");
                if (selects.length > 0) {
					var codes="";
                    for(var i=0;i<selects.length;i++){
						codes=codes+selects[i].purchaseGroupCode+",";
					}
                    $("#ERPG").val(codes.substring(0,codes.length-1));
					$("#erpPurchaseQuery").dialog('close');
                }else{
					$("#erpPurchaseQuery").dialog('close');
				}
            },
            "${query.ui.cancel}": function(){
                $("#erpPurchaseQuery").dialog('close');
            }
        }
    });
}

function doEditCssType(){
    var table = $('.display');
    table.each(function(){
        var th1 = $(this).find('th:first');
        if (th1.find("input[type=checkbox]").length > 0) {
            th1.css('width', '1%');
        }
        $(this).find('th').each(function(){
            $(this).css('border', '1px solid #999999').css('background', '#D1D6F0');
            ;
        });
    });
}
//为地区公司选择进口,地区集中采购组织机构
function forLocalCompanyAttr(orgId,type,inputName,inputId){
	var div = $("<div></div>");
	var ztree;
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:295px;width:auto;height:200px' ></ul>");
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 440,
        buttons: {
            "确定": function(){
				var node = ztree.getCheckedNodes(true);
				if(node.length==0){
					div.dialog("close");
        			div.remove();
					return;	
				}
				if(type=='1'){
					doManager("purStruOrgManager", "checkAttrForLocal", [node[0].id, 'true'], function(data, textStatus, XMLHttpRequest){
						if(data.result){
							var flag = $.fromJSON(data.data);
							if(flag!=''){
								$("#"+inputId).val(node[0].code);
								$("#"+inputName).val(flag);
								div.dialog("close");
        						div.remove();
							}else{
								$$.showMessage("${system.info}","该组织机构不是采购组织!")
							}
						}
					});
				}else{
					doManager("purStruOrgManager", "checkAttrForLocal", [node[0].id, 'false'], function(data, textStatus, XMLHttpRequest){
						if(data.result){
							var flag = $.fromJSON(data.data);
							if(flag!=''){
								$("#"+inputId).val(node[0].code);
								$("#"+inputName).val(flag);
								div.dialog("close");
        						div.remove();
							}else{
								$$.showMessage("${system.info}","该组织机构不是组长单位!")
							}
						}
					});
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
    ztree = forLocalcomAttr(id);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}
function forLocalcomAttr(id){
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
    myData = $.toJSON({
        managerName: "purStruOrgManager",
        methodName: "getOrgnaztionForLocalAttr",
        parameters: [id]
    });
        setting = {
            checkable: true,
            checkStyle: "radio",
            checkRadioType: "all",
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
            var data = new PMSRequestObject("purStruOrgManager", "getAuthChildsByParentId", [treeNode.id]);
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
            var data = new PMSRequestObject("purStruOrgManager", "getAuthChildsByParentId", [treeNode.id]);
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
    zTree1 = $("#treeDemo").zTree(setting, zNodes);
    
    return zTree1;
    
}