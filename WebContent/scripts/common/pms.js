

/**
 * @Object    : Bid对象
 * @author    : ruanqingfeng
 * @desc      : 招标模块通用的方法集合对象
 * @version   : 0.1版本
 * @singleton
 */
(function(){
	if (typeof this.Bid === 'undefined') {
       this.bid = this.Bid = {};
    }
	Bid.token = null;
	$(function(){
		Bid.token = getToken();	
	})
	/**
	 * @desc       : 弹出提示信息
	 * @param1     : msg(需要显示的信息)
	 * @param2     : callback(回调函数)
	 * @for example: 将本js引入后，直接在页面上调用Bid.alert("显示内容",function(_btn){});
	 */
	Bid.alert = function (msg,callback){
		
		$$.showMessage('系统提示',msg, function(data){
			if(callback!=null)
				callback(true);
		});
	};

	/**
	 * @desc       : 弹出确认信息
	 * @param1     : msg(需要显示的信息)
	 * @param2     : callback(回调函数)
	 * @for example: 将本js引入后，直接在页面上调用Bid.alert("显示内容",function(_btn){});
	 */
	Bid.confirm = function (msg,callback,position){
		var div = $("<div></div>");
		div.html(msg);
		var obj = div.dialog({
			bgiframe: true,
			title:"系统提示",
			width:320,
    		autoOpen: false,
			buttons : {
				"确定":function() {
					div.dialog("close");
					if(callback!=null)
						callback(true);
				},
				"取消":function(){
					div.dialog("close");
					if(callback!=null)
						callback(false);
				}
			},
			modal:true
		});	
		if(position!=null){
			obj = obj.dialog('option', 'position', position || 'center');
		}
		obj.dialog("open");
		$("div").queue(function () {
			  $(this).addClass("newcolor");
			  $(this).dequeue();
		  });
	
	};
	
})();
