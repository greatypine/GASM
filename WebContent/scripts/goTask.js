/**
 * 跳转到任务界面
 */
var topInterval = null;// 菜单树的定时器
var leftInterval = null;// 登陆iframe的定时器
var stopInterval = null;// 停止定时器
var leftChangeSize = null;// 左侧frame的宽度
var topChangeSize = null;// 上面frame的宽度
var isTopEnd = false;// top隐藏完毕
var isLeftEnd = false;// left隐藏完毕
function changeTopAndLeftFrame(url) {
	leftChangeSize = 172;
	topChangeSize = 46;
	topInterval = setInterval("topChange()", 50);
	leftInterval = setInterval("leftChange()", 50);
	stopInterval = setInterval("stopChange('" + url + "')", 50);
}

var stopChange = function(url) {
	if (isTopEnd && isLeftEnd) {
		clearInterval(stopInterval);
		$(window.top.document).find("frame[name='appFrame']").attr("src", url);
	}
}
var topChange = function() {
	topChangeSize = topChangeSize - 5;
	eval("$(window.top.document).find('frameset').first().attr('rows','" + topChangeSize + ",*,12')");
	if (topChangeSize <= 0) {
		clearInterval(topInterval);
		isTopEnd = true;
	}
}
var leftChange = function() {
	leftChangeSize = leftChangeSize - 20;
	eval("$(window.top.document).find('frameset[name=\"i2ui_shell_content\"]').attr('cols','" + leftChangeSize + ",*')");
	if (leftChangeSize <= 0) {
		clearInterval(leftInterval);
		isLeftEnd = true;

	}
}