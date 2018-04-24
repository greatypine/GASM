/*
 * 使用SSF组提供的上传列表组件引用JS包
 **/
importJs("../scripts/common/ajaxfileupload.js");
importJs("../scripts/common/uploadTable2.0.js");
importJs("../scripts/common/uploadTableDIY.js");
importCss("../scripts/css/uploadTable.css");//

$(function(){
	setTimeout("initUploadTable();",1000);
})


function initUploadTable(){
	$(fileUploadTables).each(function(i){
		this.businessType = "bidAttachment";
		this.uploadActionUrl = getRootPath() + '/uploaderAction.action?businessType=' + this.businessType;//上传附件的后台处理URL	
	})
	$(fileUploadTablesDIY).each(function(i){
		this.businessType = "bidAttachment";
		this.uploadActionUrl = getRootPath() + '/uploaderAction.action?businessType=' + this.businessType;//上传附件的后台处理URL	
	})	
}