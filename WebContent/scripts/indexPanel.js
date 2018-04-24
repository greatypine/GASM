// 个性化首页设置方法
function showIndexPanel(url) {
	var nowPath = getRootPath() + "/indexs/model_config.html";
	var nowDiv = $('<div id="configTable"><iframe id="wfIframe" frameborder="0" width="680px" height="330px" scrolling="no" src=""></iframe></div>');
	nowDiv.children().attr("src", nowPath);
	nowDiv.dialog({
		bgiframe : true,
		title : '自定义模板',
		autoOpen : true,
		width : "auto",
		height : "auto",
		closeOnEscape : false,
		open : function(event, ui) {
			$(".ui-dialog-titlebar-close").hide();
		},
		buttons : {
			"关闭" : function() {
				nowDiv.dialog("close");
				window.location = url;
			}
		},
		modal : true
	});
}