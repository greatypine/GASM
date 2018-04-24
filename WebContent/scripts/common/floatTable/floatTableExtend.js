/*
为页面添加一个左侧的表格，用于显示数据，支持链接跳转和点击回调函数。
用法：
1.将原页面元素用一个div进行包装
2.导入 js文件 和 floatTableExt.css
3.调用 $$.addFloatTable(para);

para格式：{width:200px(可选),wrapperId(包含页面所有元素的div ID):,data:[
	{rowId:"",title:"",value:"",href:""(可选),fn:function(){}(可选，如果有，会覆盖href参数)},
	{rowId:"",title:"",value:"",href:""(可选),fn:function(){}(可选，如果有，会覆盖href参数)}
]}

方法：
draw() setData后可以重绘表格
setValue(rowId, value)
setData(data) 修改第一次传入para中的data数组，需要用draw()方法重绘表格
setUrl(rowId, url)
setFn(rowId, fn)
*/

PMSApplication.prototype.addFloatTable = function(para){
	this.fTable = new floatTable(para);
	return this.fTable;
};

var showOrHide = false;
var tableOrigHeight;

function getWrapperDivWidth(flyDiv){
	var w = $("body").width() - $(flyDiv).width() - 10;
	return w;
}

function toggleFloatTable(elm){
	var div = $(elm).closest("div")
	var floatTableOrigWidth = div.data("para").width;
	var wrapperId = div.data("para").wrapperId;
	
	if(!showOrHide){
		tableOrigHeight = div.height();
		div.animate({height:30,width:30},"normal","swing",function(){
			div.find("table").hide();
			$("#" + wrapperId).css("width",getWrapperDivWidth(div) + "px");
		});
		$(elm).html("展开");
	}else{
		$("#" + wrapperId).css("width",($("body").width() - floatTableOrigWidth.replace("px","")-10) + "px");
		div.animate({height:tableOrigHeight,width:floatTableOrigWidth},"normal");
		div.find("table").show();
		$(elm).html("收起");
	}
	showOrHide = !showOrHide;
}

var floatTable = function(para){
	var _defPara = {width:"200px"};
	var _para = $.extend(true,{},_defPara,para);
	var _div = $('<div id="dFloatTable"></div>');
	var _tb;
	
	function doLayout(){
		var wrapperDiv = $("#" + _para.wrapperId);
		
		_div.css("width",_para.width);
		wrapperDiv.css("width",getWrapperDivWidth(_div) + "px");
		
		wrapperDiv.css({display:"inline-block","float":"right"});
		_div.insertBefore(wrapperDiv);
		_tb.css("width","100%").css("");
		floatTableOrigWidth = _para.width;
	}
	
	this.draw = function(){
		_div.empty();
		_div.append('<a href="javascript:void(0);" onclick="toggleFloatTable(this);">收起</a>');
		_tb = $('<table id="tbFloat"></table>');
		$.each(_para.data,function(i,val){
			if(val.value != null)
				var tr = $('<tr><td name="title"></td><td name="value"></td></tr>').attr("id",val.rowId);
			else
				var tr = $('<tr><td name="title"></td></tr>').attr("id",val.rowId);
			if(val.fn != null){
				var a = $("<a/>").attr("href","javascript:void(0);").html(val.title).bind("click",val.fn);
				tr.find("td[name='title']").append(a);
			}else if(val.href != null){
				var a = $("<a/>").attr("href",val.href).html(val.title);
				tr.find("td[name='title']").append(a);
			}else{
				tr.find("td[name='title']").html(val.title);
			}
			if(val.value != null)
				tr.find("td[name='value']").html(val.value);
			
			_tb.append(tr);
		});
		_div.data("para",_para);
		_div.append(_tb);
		doLayout();
	};
	
	this.setValue = function(rowId, value){
		_div.find("tr#" + rowId).find("td[name='value']").html(value);
	};
	
	this.setData = function(data){
		_para.data = data;
		this.draw();
	};
	
	this.setUrl = function(rowId, url){
		var td = _div.find("tr#" + rowId).find("td[name='title']:first");
		if(td.length >0){
			if(td.find("a").length > 0){
				td.find("a:first").attr("href",url);
			}else{
				var a = $("a").attr("href",url).html(td.text());
				td.append(a);
			}
		}
	};
	
	this.setFn = function(rowId, fn){
		this.setUrl(rowId,"javascript:void(0);");
		_div.find("tr#" + rowId).find("td[name='title']:first>a:first").unbind("click").bind("click",fn);
	};
	
	this.getWidth = function(){
		return _para.width;
	};
	$(window).bind("resize",function(){
		doLayout();
	});
	this.draw();
};


