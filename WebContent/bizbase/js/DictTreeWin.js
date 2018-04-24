

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
 * 
 */
 /*
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