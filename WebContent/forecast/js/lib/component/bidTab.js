
/*
* @Object       : bidTab
* @author       : ruanqingfeng
* @version      : Tab页组件）
* @for example  : 将本js引入后，在需要显示组件的地方加入以下HTML代码
*  <div class="bidTabPage" defaults="{
		height:'150',
		items:[
			{
				title:'招标方位',
				active:true,
				contentEl:'table'//内容所对应的HTML标签ID
			},{
				title:'招标方位',
				contentEl:'table2'//内容所对应的HTML标签ID
			}
		]
	 }"></div>
*/
importCss(baseUrl+"/forecast/js/lib/component/bidTab.css");
importCss(baseUrl+"/scripts/css/tabpage.css");
var bidTab = function(_cfg){
	this.activeTabIndex = null; //当前激活的Tab索引号
	this.tabCount  = 0;         //tab页总数
	this.tabHeader = null;      //tab页头
	this.tabBody   = null;      //tab页主体
	this.items     = null;      //tab页集合
	this.width     = "100%";    //panel宽度
	this.height    = "100%";    //panel高度
	this.tabClickAble = true;   //tab页标签是否支持单击切换内容
	this.renderToDiv = null;   //渲染的DIV对象


	var listeners = {
		onBeforeActiveTab : "onBeforeActiveTab",
		onActiveTab : "onActiveTab"
	}//表格支持的事件类型（不可修改）

	
	$.extend(this,_cfg);
	
	var _this = this;


	/*
	* 为表格增加事件监听
	*/
	this.addListener = this.on = function (_eventName,_callBack){
		$(this).unbind(_eventName);
		$(this).bind(_eventName,function(ev,param1,param2,param3){
			$.event.returnValue = _callBack(param1,param2,param3);
			return $.event.returnValue;
		});
	}

	
	/*
	 * 创建Tab页标签头部
	 */
	this.createHeader = function(){
		_this.tabHeader = $('<div class="mainNavg clear"></div>');
		_this.tabHeader.append($("<ul></ul>"));
		_this.renderToDiv.css("width",_this.width);
		_this.renderToDiv.css("height",_this.height);
		_this.renderToDiv.append(_this.tabHeader);
	}
	
	/*
	 * 创建Tab页标内容部分
	 */
	this.createBody = function(){
		_this.tabBody = $('<div style="overflow:auto" class="containerbody tab_containerbody"></div>');
		_this.renderToDiv.append(_this.tabBody);
	}
	
	/*
	 * 新增一个标签页
	 */
	this.addTabPanel = function(_tabPanelElement){
		var tabOnClick = " style='cursor:default;' ";
		if(_this.tabClickAble==true){
			tabOnClick = " style='cursor:pointer;' onclick='bidTabs["+_this.tabIndex+"].setActiveTab("+_this.tabCount+")'";
		}
		_this.tabHeader.children().eq(0).append($('<li style="'+(_tabPanelElement.hidden==true?"display:none":"")+'"><a '+tabOnClick+'><font>'+_tabPanelElement.title+'</font></a></li>'));
		var newPanel = $('<div></div>');
		
		var contentElement = $(_tabPanelElement.contentEl);
		if((typeof _tabPanelElement.contentEl) =="string"){
			contentElement = $("#"+_tabPanelElement.contentEl);
		}
		newPanel.append(contentElement);
		_this.tabBody.append(newPanel);
		if(_tabPanelElement.active==true){
			_this.setActiveTab(_this.tabCount);
		}else{
			newPanel.addClass("tab_unActive");
		}
		_this.tabCount++;
	}
	
	/*
	 * 设置隐藏标签
	 */
	this.setHiddenTabPanel = function(i){
		_this.tabHeader.children().eq(0).children().eq(i).hide(); 
	}
	
	/*
	 * 设置显示标签
	 */
	this.setShowTabPanel = function(i){
		_this.tabHeader.children().eq(0).children().eq(i).show(); 
	}

	/*
	 * 创建标签页Panel，将头部信息，内容信息整合
	 */
	this.createTab = function(){
		this.createHeader();
		this.createBody();
		var _this = this;
		$(this.items).each(function(i,element){
			_this.addTabPanel(element);
		});
	}
	
	/*
	 * 改变Tab页内容
	 */
	this.changeTabPanel = function(_tabIndex,_tabPanelElement){
		_this.items[_tabIndex] = _tabPanelElement;
		var newPanel = $('<div></div>');
		var contentElement = $("#"+_tabPanelElement.contentEl);
		newPanel.append(contentElement);
		var currTabPanel = $(_this.tabBody).children("div:eq("+_tabIndex+")");
		currTabPanel.before(newPanel);
		currTabPanel.remove();
	}
	
	
	/*
	 * 根据索引将标签页设置为激活状态
	 */
	this.setActiveTab = function(_tabIndex){
		var ss = $(_this).trigger(listeners.onBeforeActiveTab,[contentElement]);
		if($.event.returnValue==false){
			$.event.returnValue = null;
			return;	
		}
		
		$(_this.tabHeader.eq(0)).find("li:eq("+_tabIndex+")").addClass("select");
		$(_this.tabHeader.eq(0)).find("li:not(:eq("+_tabIndex+"))").removeClass("select");
		
		$(_this.tabBody).children("div:eq("+_tabIndex+")").addClass("tab_active");
		$(_this.tabBody).children("div:eq("+_tabIndex+")").removeClass("tab_unActive");
		$(_this.tabBody).children("div:not(:eq("+_tabIndex+"))").addClass("tab_unActive");
		$(_this.tabBody).children("div:not(:eq("+_tabIndex+"))").removeClass("tab_active");
		var contentElement = $(_this.items[_tabIndex].contentEl);
		if((typeof _this.items[_tabIndex].contentEl) =="string"){
			contentElement = $("#"+_this.items[_tabIndex].contentEl);
		}
		contentElement.css("display","block");
		
		_this.activeTabIndex = _tabIndex;

		$(_this).trigger(listeners.onActiveTab,[contentElement]);
	}
	
	
	/*
	 * 将当前活动状态的TAB页的下一标签页设置为激活状态
	 */
	this.goNextTab = function(){
		var goTabIndex = _this.activeTabIndex+1;
		if(goTabIndex>_this.tabCount-1){
			goTabIndex = _this.tabCount-1;
		}
		
		_this.setActiveTab(goTabIndex);	
	}
	
	
	/*
	 * 将当前活动状态的TAB页的上一标签页设置为激活状态
	 */
	this.goBackTab = function(){
		var goTabIndex = _this.activeTabIndex-1;
		if(goTabIndex<0){
			goTabIndex = 0;
		}
		_this.setActiveTab(goTabIndex);	
	}
	
	

	this.createTab();
	
};

var bidTabs = [];
$(function(){
	$("div[class*='bidTabPage']").each(function(i,element){
		var defaults = $(this).attr("defaults");
		if(defaults==null||defaults==""){
			defaults = "{}";	
		}
		eval("var defaults ="+defaults);
		defaults.tabIndex = i;
		defaults.renderToDiv = $(element);
		var obj = new bidTab(defaults);
		bidTabs.push(obj);
	});
})

