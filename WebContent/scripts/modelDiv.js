// 对象容器
var _modelManager = new Array();

// 寻找对象，并调用对象内部方法
function changeUrl(_id_, _idx_, _this) {
	for (var i=0; i<_modelManager.length; i++) {
		var _divObj_ = _modelManager[i];
		var _idStr = _divObj_._id;
		if (_id_ == _idStr) {
			_divObj_.changeModelTabContent(_idx_, _this);
		}
	}
}

var modelDiv = function(mid) {
	var A=new Object();// 新建一个对象
	this.modelWidth = 500;
	this.modelHeight = 300;
	
	this._id = rnd();
	_modelManager.push(this);
	// div对象
	this.divObj = $("#" + mid);
	// 是否是选项卡
	this.modelTab = false;
	// 标题名称
	this.modelTitleName = new Array();
	// 内容
	this.modelTabContent = new Array();
	// 标题图片地址
	this.modelTitleIcon = new Array();
	// 标题栏右侧操作选项名称
	this.modelMoreName = new Array();
	// 标题栏右侧操作选项地址
	this.modelMoreAddress = new Array();
	// 标题栏右侧操作选项图片地址
	this.modelMoreIcon = new Array();
	
	// 内容样式 0:文字 1:图片 2:div(flash)
	this.modelContentStyle = 0;
	// 内容的链接地址
	this.modelContentUrl = new Array();
	
	this.changeModelTabContent = function(_idx, _this) {
		if (_this) {
			if (A.Current) {
				A.Current.style.color = 'blue';
			}
			// 恢复链接颜色
			_this.style.color = '#CC3352';
			// 单击链接颜色
			A.Current = _this;// 设置当前连接
		}
		
		if (this.modelContentStyle == 0) {
			this.divObj.find("ul").html(this.modelTabContent[_idx]);
		} else {
			this.divObj.find(".content").html(this.modelTabContent[_idx]);
		}		
	};
	
	this.getModelTitle = function() {
		this._hrefStr = "";
		if (this.modelTab) {
			for (var i=0; i<this.modelTitleName.length; i++) {
				var _modelTTName = this.modelTitleName[i];
				var _modelTbCnt = this.modelTabContent[i];
				var _modelTTIcon = this.modelTitleIcon[i];
				
				if (_modelTTIcon != null && _modelTTIcon != "" && _modelTTIcon != "undefined") {
					if (i == this.modelTitleName.length - 1) {
						this._hrefStr += "<a href='#' onClick=\"changeUrl("+ this._id +"," + i + ",this)\"><img src=\""+_modelTTIcon+"\" />"+ _modelTTName +"</a>";	
					} else {
						this._hrefStr += "<a href='#' onClick=\"changeUrl("+ this._id +"," + i + ",this)\"><img src=\""+_modelTTIcon+"\" />"+ _modelTTName +"</a> | ";	
					}
				} else {
					if (i == this.modelTitleName.length - 1) {
						this._hrefStr += "<a href='#' onClick=\"changeUrl("+ this._id +"," + i + ",this)\">"+ _modelTTName +"</a>";	
					} else {
						this._hrefStr += "<a href='#' onClick=\"changeUrl("+ this._id +"," + i + ",this)\">"+ _modelTTName +"</a> | ";	
					}
				}
			}
		} else {
			for (var i=0; i<this.modelTitleName.length; i++) {
				var _modelTTName = this.modelTitleName[i];
				var _modelTbCnt = this.modelTabContent[i];
				var _modelTTIcon = this.modelTitleIcon[i];
				if (_modelTTIcon != null && _modelTTIcon != "" && _modelTTIcon != "undefined") {
					if (i == this.modelTitleName.length - 1) {
						this._hrefStr += "<img src=\""+_modelTTIcon+"\" />"+ _modelTTName;	
					} else {
						this._hrefStr += "<img src=\""+_modelTTIcon+"\" />"+ _modelTTName + " | ";	
					}
				} else {
					if (i == this.modelTitleName.length - 1) {
						this._hrefStr += _modelTTName;	
					} else {
						this._hrefStr += _modelTTName + " | ";	
					}
					
				}
			}
		}
		return this._hrefStr;
	}
	
	this.getModelMore = function() {
		this._moreStr = "";
		
		if (this.modelMoreAddress != null && this.modelMoreAddress != "" && this.modelMoreAddress.length > 0) {
			for (var i=0; i<this.modelMoreAddress.length; i++) {
				var _modelMName = this.modelMoreName[i];
				var _modelMAdd = this.modelMoreAddress[i];
				var _modelMIcon = this.modelMoreIcon[i];
				if (_modelMName == "undefined") {
					_modelMName = "";		
				}
				if (_modelMIcon != null && _modelMIcon != "" && _modelMIcon != "undefined") {
					this._moreStr += "<a href=\"" + _modelMAdd + "\"><img src=\""+_modelMIcon+"\" />"+ _modelMName +"</a>";
				} else {
					this._moreStr += "<a href=\"" + _modelMAdd + "\">"+ _modelMName +"</a>";
				}
			}
		}
		return this._moreStr;
	}
	
	this.idxDiv = "<div class=\"head\"><table class=\"_title\"><tr><td style=\"text-align: left;\"></td><td style=\"text-align: right;\"></td></tr></table></div>"
			+ "<div class=\"content\">"
			+"</div>";
	this.getPanelContent = function() {
		var ulStr = "";
		// 文字
		if (this.modelContentStyle == 0) {
			ulStr = "<ul></ul>";
			var cntStr = "";
			for (var i=0; i<this.modelTabContent.length; i++) {
				var cntText = this.modelTabContent[i];
				var cntUrl = this.modelContentUrl[i];
				if (cntUrl != "" && cntUrl != null && cntUrl != "undefined" && cntUrl.length > 0) {
					for (var j=0; j<cntText.length; j++) {
						cntStr += "<li><a href=\"" + cntUrl[j] + "\"><nobr>&nbsp;&#8226; "+cntText[j]+"</nobr></a></li>";						
					}
				} else {
					for (var j=0; j<cntText.length; j++) {
						cntStr += "<li><nobr>&nbsp;&#8226; "+cntText[j]+"</nobr></li>";	
					}
				}
				this.modelTabContent[i] = cntStr;
				cntStr = "";
			}
		// 图片
		} else if (this.modelContentStyle == 1) {
			var _mwidth = this.modelWidth - 2;
			var _mheight = this.modelHeight - 30;
			var cntStr = "";
			for (var i=0; i<this.modelTabContent.length; i++) {
				var cntImg = this.modelTabContent[i];
				var cntUrl = this.modelContentUrl[i];
				if (cntUrl != "" && cntUrl != null && cntUrl != "undefined" && cntUrl.length > 0) {
					
					for (var j=0; j<cntImg.length; j++) {
						cntStr += "<a href=\"" + cntUrl[j] + "\"><img style=\"width:"+_mwidth+"px; height:"+_mheight+"px\" src=\""+cntImg[j]+"\" /></a>";
					}
				} else {
					for (var j=0; j<cntImg.length; j++) {
						cntStr += "<img width=\""+_mwidth+"\" height=\"100%\" src=\"100%\" />";
					}
				}
				this.modelTabContent[i] = cntStr;
				cntStr = "";
			}
		} else {
			var cntStr = "";
			for (var i=0; i<this.modelTabContent.length; i++) {
				var cntImg = this.modelTabContent[i];
				var cntUrl = this.modelContentUrl[i];
				if (cntUrl != "" && cntUrl != null && cntUrl != "undefined" && cntUrl.length > 0) {
					for (var j=0; j<cntImg.length; j++) {
						cntStr += "<a href=\"" + cntUrl[j] + "\">"+cntImg[j]+"</a>";
					}
				} else {
					for (var j=0; j<cntImg.length; j++) {
						cntStr += cntImg[j];
					}
				}
				this.modelTabContent[i] = cntStr;
				cntStr = "";
			}
		}
		return ulStr;
	}
	
	this.init = function() {
		var hrefStr = this.getModelTitle();
		var moreStr = this.getModelMore();
		this.divObj.append(this.idxDiv);
		this.divObj.find("._title td").eq(0).append(hrefStr);
		if (moreStr != "") {
			this.divObj.find("._title td").eq(1).append(moreStr);			
		}
		var cntStr = this.getPanelContent();
		this.divObj.find(".content").append(cntStr);
		this.changeModelTabContent(0);
		this.divObj.find("._title a").eq(0).click();
		
		this.divObj.css({"width": this.modelWidth + "px", "min-height": this.modelHeight + "px"});
	}	
	return this;
};


rnd.today=new Date();
rnd.seed=rnd.today.getTime();
function rnd() {
　　　　rnd.seed = (rnd.seed*9301+49297) % 233280;
　　　　return rnd.seed/(233280.0);
}
