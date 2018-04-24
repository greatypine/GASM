var userSelectDiv = function(rentenderToDiv){
	this.renderToDiv =rentenderToDiv;
	this.srcClickObj = "";
	this.desClickObj = "";
	this.srcDiv  = 	$("<div class='srcDivClass' style='width:100px;height:123px;float:left;border:1px solid #C5C5C5'><div class='wfSelectDivHeaderClass'>待选用户姓名</div><ul ></ul></div>");
	this.desDiv = 	$("<div  class='desData srcDivClass' style='width:100px;height:123px;float:left;border:1px solid #C5C5C5'><div class='wfSelectDivHeaderClass'>已选用户姓名</div><ul ></ul></div>");
	this.modelBtn = $("<div  style='width:50px;height:100px;float:left;padding-top:30px' align='center'><input type='button'  value='>>'/><br/><input type='button' value='<<'/><br/></div>");
	this.sortBtn = 	$("<div  style='width:50px;height:100px;float:left;padding-top:30px' align='center'><input type='button' value='上移'/><br/><input type='button' value='下移'/><br/></div>");
	this.renderToDiv.append(this.srcDiv).append(this.modelBtn).append(this.desDiv);
	var _this = this;
	this.setValues = function(_data) {
		for (var i = 0; i < _data.length; i++) {
			for (var j = 0; j < _data[i].length; j++) {
				if (_data[i][j] != null) {
					

							this.srcDiv.find("ul").append("<li><label style='display:none'>" + _data[i][j].code + "</label>" + _data[i][j].name + "</li>");

				}
				
			}
		}
		this.modelBtn.find("input[type='button']").eq(0).bind("click",_this.getName);
		this.modelBtn.find("input[type='button']").eq(1).bind("click",_this.rollbackName);
		this.sortBtn.find("input[type='button']").eq(0).bind("click",_this.upSort);
		this.sortBtn.find("input[type='button']").eq(1).bind("click",_this.downSort);
		this.srcDiv.find("li").each(function(i,element){
			$(element).bind("click",function(){
				if(_this.srcClickObj.length > 0) {
					_this.srcClickObj.removeAttr("style");
				}
				
				_this.srcClickObj = $(element);
				$(element).attr("style","background-color:#BEC5E7")
			});
		});
		this.setLiBgColor("src");
		
	}
	
	this.setLiBgColor = function(type){
		var targetDiv = type=="src"?this.srcDiv:this.desDiv;
		targetDiv.find("li").removeClass("srcLiEven");
		targetDiv.find("li").removeClass("srcLiOdd");
		targetDiv.find("li:even").addClass("srcLiEven");
		targetDiv.find("li:odd").addClass("srcLiOdd");
	}
	
	this.getName = function() {
		if (_this.srcClickObj.length > 0) {
			_this.desDiv.find("ul").append("<li>" + _this.srcClickObj.html() + "</li>");
			_this.desDiv.find("ul").children().last().bind("click", function(){
				_this.changeBgColorDes(this);
			});
			_this.srcClickObj.remove();
			_this.srcClickObj = "";
			_this.setLiBgColor("src");
			_this.setLiBgColor("des");
		}
	}
	this.rollbackName = function() {
		if (_this.desClickObj.length > 0) {
			_this.srcDiv.find("ul").append("<li>" + _this.desClickObj.html() + "</li>");
			_this.srcDiv.find("ul").children().last().bind("click", function(){
				_this.changeBgColorRes(this);
			});
			_this.desClickObj.remove();
			_this.desClickObj = "";
			_this.setLiBgColor("src");
			_this.setLiBgColor("des");
		}
	}
	
	this.changeBgColorDes =	function (_this2) {
		if(_this.desClickObj.length > 0) {
			_this.desClickObj.removeAttr("style");
		}
		$(_this2).attr("style","background-color:#BEC5E7");
		_this.desClickObj = $(_this2);
	}
	
	this.changeBgColorRes =function (_this3) {
		if(_this.srcClickObj.length > 0) {
			_this.srcClickObj.removeAttr("style");
		}
		$(_this3).attr("style","background-color:#BEC5E7");
		_this.srcClickObj = $(_this3);
	}
	
	this.upSort = function() {
		if(_this.desDiv.find('ul').children(_this.desClickObj).index() != 0) {
			alert(_this.desDiv.find('ul').children(_this.desClickObj).index());
			alert(_this.desDiv.find('ul').children().eq(_this.desDiv.find('ul').children(_this.desClickObj).index()-1).length);
		} else {
			return false;
		}
	}
	this.downSort = function() {
		alert('down');
	}
}

	function doSubmitwf(approvalMsg2,desUrl,doActionManager,args) {
		var Object = createObject();
		if(Object == "false") {
			wf.alert("必须为每个传阅节点选择审批人！");
			return false;
		}
		var nowTrainferData = new Array(); 
		nowTrainferData.push(args);
		wfDoManager(doActionManager,"startWorkflowForSelect",[approvalMsg2,Object,nowTrainferData],function(data){
			if($.fromJSON(data.data).msg.result.value == "packageLocked") {
				wf.alert("您选择的采购方案包已被锁定，请更换包信息!");
			} else {
				if (desUrl == "#") {
					wf.alert("创建成功");
				}
				else {
					window.location = baseUrl + desUrl;
				}
			}
			

		});
	}
	function  createObject() {
		var iframeJquery = ($("#wfIframe")[0].contentWindow.window.$);
		var myData = new Array();
		var flag = "";
		iframeJquery("div[class='desData srcDivClass']").each(function(i,element1){
			
			myData[i] = new Array();
			$(element1).children("ul").each(function(j,element2){
				if($(element2).find("label").length <= 0) {
					flag = "false";
					return false
				}
				$(element2).find("label").each(function(k,element3){
					if( $(element3).html() == null) {
						flag = "false";
					} 
					myData[i][k] = $(element3).html();
					if(myData[i].length <= 0) {
						flag = "false";
					}
				});


			});

		});
		if (flag == "false") {
			return flag;
		}
		else {
			if (myData[0].length <= 0) {
				return "false";
			} else {
				return myData;
			}
		}
	}
	

var userSelectAlert = function(desUrl,approvalMsg1,doActionManager,args){
	this.win = $("<div></div>");
	this.contentUrl = wfBaseUrl + "/commonsource/doselect.html";
	this.width = 650;
	this.height = 400;
	this.callBack = function(){};
	var _this = this;
	var initWin = function(){
		_this.win.html('<iframe id="wfIframe" frameborder="0" width="' + (_this.width - 20) + '" height="' + (_this.height - 90) + '" src="' + _this.contentUrl + '"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"请选择审批人",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定":function(){
					if(doSubmitwf(approvalMsg1,desUrl,doActionManager,args) != false){
						_this.win.remove();
					}


				},
				"取消":function(){
					_this.win.remove();
				}
			},
			modal:true
		});	
	}
	
	this.show = function(){
		_this.win.dialog("open");
	}
	
	this.hide = function(){
		_this.win.dialog("close");
	}
	initWin();
	
}
