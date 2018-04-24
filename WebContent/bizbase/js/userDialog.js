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
 * @param {Object} orgCode:组织结构编码
 * @param {Object} positionCodes岗位编码，如果传递多个用","分割
 * @param {object} selectOrgAble组织机构是否可以选择,"false"不可以选择
 */
function checkUser(checkType,callback,orgCode,positionCodes,selectOrgAble){
	alert(3);
	win = new UserCheckWin(checkType,callback,orgCode,positionCodes,selectOrgAble
	);
	win.show();
}
/**
 * 
 * @param {Object} callback
 * 人员选择回调函数callback
 * 返回值json
 */
var UserCheckWin = function(checkType,callback,orgCode,positionCodes,selectOrgAble){
	alert(4);
	this.win = $("<div></div>");
		var _this = this;
		this.contentUrl = baseUrl + "/bizbase/userCheckWin.html?checkType="+checkType+"&orgCode="+orgCode+"&positionCodes="+positionCodes+"&selectOrgAble="+selectOrgAble;
		var initWin = function(){
		_this.win.html('<iframe name="userCheckName" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"人员选择列表",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定": function(){
					window.frames["userCheckName"].doSubmit();
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



function checkExpertUser(checkType,callback){
	win = new ExpertUserCheckWin(checkType,callback);
	win.show();
}
/**
 * 
 * @param {Object} callback
 * 专家选择回调函数callback
 * 返回值json
 */
var ExpertUserCheckWin = function(checkType,callback){
	this.win = $("<div></div>");
		var _this = this;
		this.contentUrl = baseUrl + "/bizbase/expertUserCheckWin.html?checkType="+checkType;
		var initWin = function(){
		_this.win.html('<iframe name="expertUserCheckName" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"人员选择列表",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定": function(){
					window.frames["expertUserCheckName"].doSubmit();
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
