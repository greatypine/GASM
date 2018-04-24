
/**
 * @Object    : DictTreeWin对象
 * @author    : ruanqingfeng
 * @desc      : 显示树窗口
 * @version   : 0.1
 * @singleton ：false
 */
var DictTreeWin = function(_setting){
	_setting.checkStyle = _setting.checkStyle == null ? "checkbox" : _setting.checkStyle;
	
	/**
	 * 树面板信息
	 */
	var treePanel = {
		setting:{
			checkable: true,
			checkStyle: _setting.checkStyle,
			async: false
		},
		zNodes:_setting.zNodes,
		treePanelDiv:"<ul class='tree'></ul>"
	};
	
	/**
	 * 显示窗口
	 */
	this.show = function (){
		var winContextDiv = $("<div></div>");
		var treePanelObj = $(treePanel.treePanelDiv);
		var treeObj = null;
		winContextDiv.dialog({
			bgiframe: true,
			title:_setting.title==null?"系统提示":_setting.title,
			width:_setting.width==null?300:_setting.width,
			height:_setting.height==null?300:_setting.height,
			checkStyle:_setting.height==null?300:_setting.checkStyle,
			close:function(){
				winContextDiv.remove();
			},
			buttons : {
				"确定":function() {
					winContextDiv.dialog("close");
					if(_setting.callback!=null)
						_setting.callback.close(true,treeObj.getCheckedNodes(true),treeObj.getCheckedNodes(false));
				},
				"取消":function(){
					winContextDiv.dialog("close");
					if(_setting.callback!=null)
						_setting.callback.close(false);
				}
			},
			modal:true
		});	
		winContextDiv.append(treePanelObj);
		treeObj = treePanelObj.zTree(treePanel.setting,treePanel.zNodes);
	}
}

/**
 * 提供显示数据字典的树窗口
 * @methodName   : showDictWin
 * @methodParam1 : String     （数据字典名称）
 * @methodParam2 : function   （点击确定或取消时的回调函数）
 * @author       : ruanqingfeng
 * @desc         : 提供显示数据字典的树窗口
 * @for example1 : showDictWin("projectType",function(btn,node){alert(btn);});
 */
function showDictWin(treeNodes,callBack,setting){
	if(setting==null)setting={};
	var treeWin = new DictTreeWin({
		zNodes:treeNodes,
		width:setting.width,
		height:setting.height,
		title:setting.title,
		checkStyle:setting.checkStyle,
		callback:{
			close:callBack
		}
	});
	treeWin.show(function(){});	
}
/**
 * 根据字典名称等参数显示选择树窗口
 * @param {Object} titleName 窗口名称
 * @param {Object} dictName 字典名称
 * @param {Object} checkStyle 选择按钮类型
 * @param {Object} checkedCodes 已选值。空串则表示没有
 * @param {Object} callback 回调函数用以赋值
 */
function selByDict(titleName,dictName,checkStyle,checkedCodes,callback){
	var dicts = "";
	var treeNodes = [];
	doManager("dictManager", "findDictByName", [dictName], function(datas, textStatus, XMLHttpRequest) {
		if(datas.result) {
			dicts = $.fromJSON(datas.data);
		}
	},false);
	if(dicts.length==0){
		$$.showMessage("${system.info}","字典不存在!");
		return false;
	}
	$(dicts).each(function(i,element){
		var flag = false;
		if(checkedCodes.indexOf(element.dictCode)!=-1){
			flag = true;
		}
		treeNodes.push({
			name:element.dictText,
			id:element.dictCode,
			checked:flag
		});
	});

	showDictWin(treeNodes, function(btn, node, uncheckedNode){
        if (btn) {
            var code = "";
			var codeName = "";
            for (var i = 0; i < node.length; i++) {
                code = code + "," + node[i].id;
				codeName = codeName + "," + node[i].name;
            }
            code = code.substring(1, code.length);
			codeName = codeName.substring(1, codeName.length);
			callback(code, codeName);
        }
    }, {
        title : titleName,
		checkStyle : checkStyle
    });
}