/**
 * 要使用下面的方法，须设置ie的安全级别。
 * 具体方法：
 * 1.将当前站点添加到可信站点。
 * 2.ie工具->internet选项->安全->可信站点->自定义级别->对未标记为可安全执行脚本的ActiveX空间初始化并执行脚本 启用
 */

var hkey_path = "HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
var oPrintBak = {};
var errorMsg = "，请手动设置打印机。";

/**
 * 保存注册表的打印配置信息。
 */
function backupPrintConfig(){
	try {
        var RegWsh = new ActiveXObject("WScript.Shell");
        oPrintBak.header = RegWsh.RegRead(hkey_path + "header");
        oPrintBak.footer = RegWsh.RegRead(hkey_path + "footer");
        oPrintBak.marginLeft = RegWsh.RegRead(hkey_path + "margin_left");
        oPrintBak.marginTop = RegWsh.RegRead(hkey_path + "margin_top");
        oPrintBak.marginRight = RegWsh.RegRead(hkey_path + "margin_right");
        oPrintBak.marginBottom = RegWsh.RegRead(hkey_path + "margin_bottom");
    }catch (e) {}
}
/**
 * 恢复注册表的打印配置信息。
 */
function restorePrintConfig(){
	try {
        var RegWsh = new ActiveXObject("WScript.Shell");
		if(oPrintBak.header)	 RegWsh.RegWrite(hkey_path + "header", oPrintBak.header);
		if(oPrintBak.footer)	 RegWsh.RegWrite(hkey_path + "footer", oPrintBak.footer);
		if(oPrintBak.marginLeft)	RegWsh.RegWrite(hkey_path + "margin_left", oPrintBak.marginLeft);
		if(oPrintBak.marginTop)		RegWsh.RegWrite(hkey_path + "margin_top", oPrintBak.marginTop);
		if(oPrintBak.marginRight)	RegWsh.RegWrite(hkey_path + "margin_right", oPrintBak.marginTop);
		if(oPrintBak.marginBottom)	RegWsh.RegWrite(hkey_path + "margin_bottom", oPrintBak.marginBottom);
    }catch (e) {}
}
/**
 * 设置纸张方向为 横向
 */
function setDirection(){
	try{
		var wsShell= new ActiveXObject("WScript.Shell");
		//打印页面的Menubar必须可见，此操作类似按键盘上的Alt+F+U也就是 调出页面设置对话框
		wsShell.sendKeys("%fu");
		setTimeout(function(){
			//此操作类似按键盘上的Alt+A也就是 设置横向打印
			wsShell.sendKeys("%a");
			//此操作类似按键盘上的回车 页面设置对话框的默认焦点在 确定上 所以直接确定
			wsShell.sendKeys('{ENTER}');
		}, 1000);
	}catch(e){}
}
/**
 * 设置打印注册表。
 * @param {Object} sHeader
 * @param {Object} sFooter
 * @param {Object} sMarginLeft
 * @param {Object} sMarginTop
 * @param {Object} sMarginRight
 * @param {Object} sMarginBottom
 */
function setPrintConfig(sHeader, sFooter, sMarginLeft, sMarginTop, sMarginRight, sMarginBottom){
	try {
        var RegWsh = new ActiveXObject("WScript.Shell");
		
        RegWsh.RegWrite(hkey_path + "header", sHeader);
        RegWsh.RegWrite(hkey_path + "footer", sFooter);
        RegWsh.RegWrite(hkey_path + "margin_left", sMarginLeft);
        RegWsh.RegWrite(hkey_path + "margin_top", sMarginTop);
        RegWsh.RegWrite(hkey_path + "margin_right", sMarginRight);
        RegWsh.RegWrite(hkey_path + "margin_bottom", sMarginBottom);
    }catch (e) {
		alert(e + errorMsg);
    }
}
/**
 * 去页眉 页脚 边距。
 */
function pagesetup_null(){
	setPrintConfig("", "", "0", "0", "0", "0");
}