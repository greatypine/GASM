function openActivityTree(callback){
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:295px;width:auto;height:200px' ></ul>");
    var ztree;
    div.dialog({
        bgiframe: true,
        title: "节点选择",
		width: 500,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
				if(refNodes.length==0){
					div.dialog("close");
               		 div.remove()
				}
                var name =  refNodes[0].name;
				var code =  refNodes[0].activityCode;
                if (callback && typeof(callback) == 'function') {
                    callback(name,code);
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
    ztree = generatActivityTree();
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}
function generatActivityTree(){
 		var zTree1;
        var setting;
        var zNodes = [];
        var myData = $.toJSON({
            managerName: "functionManager",
            methodName: "getLevelOneNode",
            parameters: []
        });
        
        setting = {
            checkable: true,
			checkStyle:'radio',
			checkRadioType: "all",
            async: true,
            asyncParam: ["name", "id"], //获取节点数据时，必须的数据名称，例如：id、name
            asyncParamOther: ["requestString", myData], //其它参数 ( key, value 键值对格式)
		    callback: {
				beforeExpand:beforeExpand,
                click: zTreeOnClick
            }
        }
      
		function addNode(nodes, parNode) {
			for (i = 0; i < nodes.length; i++) {
				var newNode = nodes[i];
				zTree1.addNodes(parNode, newNode);
			}
		}
		function zTreeOnClick(event, treeId, treeNode) {
			if (treeNode.nodes == null || treeNode.nodes.length == 0) {
			var data = new PMSRequestObject("functionManager", "getNextLevelNodes", [treeNode.id]);
				$$.ajax($$.PMSDispatchActionUrl, "requestString=" + encodeURIComponent(data.toJsonString()), function(data, textStatus, XMLHttpRequest) {
					if (data.result) {
						newNodes = eval("(" + data.data + ")");
						addNode(newNodes, treeNode);
					}
				});
			}
		}
		function beforeExpand(treeId, treeNode) {
			if (treeNode.nodes == null || treeNode.nodes.length == 0) {
				if(treeNode.flag!=true)
				{
					var data = new PMSRequestObject("functionManager", "getNextLevelNodes", [treeNode.id]);
					$$.ajax($$.PMSDispatchActionUrl, "requestString=" + encodeURIComponent(data.toJsonString()), function(data, textStatus, XMLHttpRequest) {
					if (data.result) {
						newNodes = eval("(" + data.data + ")");
						addNode(newNodes, treeNode);
					}
					});
					treeNode.flag=true;
				}
			
			} else {
				return true;
			}
			return false;
		}
	var url = $$.PMSDispatchActionUrl;
		setting.asyncUrl = url;
	zTree1 = $("#treeDemo").zTree(setting, zNodes);

		return zTree1;
}
