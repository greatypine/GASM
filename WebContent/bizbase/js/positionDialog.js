var win;
var baseUrl=getRootPath();
function getRootPath(){
	var curWwwPath=window.document.location.href;
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	var localhostPaht=curWwwPath.substring(0,pos);
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return(localhostPaht+projectName);
}
/**
 * 
 * @param {Object} checkType:0单选，1多选
 * @param {Object} callback
 */
function checkPosition(checkType,callback){
	win = new PositionCheckWin(checkType,callback);
	win.show();
}
/**
 * 
 * @param {Object} callback
 * 岗位选择回调函数callback
 * 返回值json
 */
var PositionCheckWin = function(checkType,callback){
	this.win = $("<div></div>");
		var _this = this;
		this.contentUrl = baseUrl + "/bizbase/positionCheckWin.html?checkType="+checkType;
		var initWin = function(){
		_this.win.html('<iframe name="positionCheckName" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"岗位选择列表",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定": function(){
					window.frames["positionCheckName"].doSubmit();
					_this.win.remove();
				},
				"取消":function(){
					_this.hide();
					_this.win.remove();
				}
			},
			modal:true
		});	
	}
	this.width = 700;
	this.height = 550;
	this.callBack = function(json){
		if (callback && typeof(callback) == 'function') {
                        callback(json);
                    }
	};
	
	this.show = function(){
		_this.win.dialog("open");
	}
	
	this.hide = function(){
		if(_this.onSubmitHandler){
			_this.onSubmitHandler();	
		}
		_this.win.dialog("close");
	}
	initWin();
}
