/*
 * 使用SSF组提供的上传列表组件引用JS包
 **/
importCss(baseUrl+"/scripts/css/uploadTable.css");
importJs(baseUrl+"/scripts/common/ajaxfileupload.js");
importJs(baseUrl+"/scripts/common/uploadTable2.0.js");

$(function(){
	setTimeout("initUploadTable();",1000);
})


function initUploadTable(){
	$(fileUploadTables).each(function(i){
		this.businessType = this.userBusinessType == null ? "bidAttachment" : this.userBusinessType;
		this.uploadActionUrl = getRootPath() + '/uploaderAction.action?businessType=' + this.businessType;//上传附件的后台处理URL	
	})	
}