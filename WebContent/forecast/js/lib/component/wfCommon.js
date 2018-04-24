
function getRootPath(){
	var curWwwPath=window.document.location.href;
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	var localhostPaht=curWwwPath.substring(0,pos);
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return(localhostPaht+projectName);
}

var wfBaseUrl = getRootPath();

PMSApplication.prototype.asyncAjax = function(url, data, callback, failureCallback) {
	$.ajax({
		url : url,
		dataType : "json",
		type : "post",
		async: false, // Ajax同步
		data : data,
		timeout : $$.requestTimeout,
		success : function(data, textStatus, XMLHttpRequest) {
			if (data.result) {
				callback(data, textStatus, XMLHttpRequest);
			} else {
				if (failureCallback) {
					failureCallback(data, textStatus, XMLHttpRequest);
				} else {
					$$.onServerInvokeFailed(XMLHttpRequest, data.message);
				}
			}
		},
		error : $$.onServerInvokeFailed
	});
}

function wfDoManager(managerName,managerMethod,params,callBack,async,settings){
	
	var url  = $$.PMSDispatchActionUrl;
	var data = null;
	
	if(settings==null)settings = {};
	if(settings.showWaiting==true){
		$PD.display();	
	}
	settings.token = false;
	if(settings.token==false){
		token = null;
	}
	var doManagerVersion = "doManagerVersion";
	if(settings.version!=null){
		doManagerVersion = doManagerVersion + settings.version;
	}
	
	if(params==null){
		data = new PMSRequestObject(managerName,managerMethod,null,token); 
	}
	else if(typeof(params)=="string"){//单个字符串
		data = new PMSRequestObject(managerName,managerMethod,[params],token); 
	}else if(typeof(params)=="object"){
		if(params.length==null){//对象
			var doManagerVersionCom = $("#"+doManagerVersion);
			if(doManagerVersionCom.length!=0){
				params.version = doManagerVersionCom.val();
			}
			data = new PMSRequestObject(managerName,managerMethod,[$.toJSON(params)],token); 
		}else{//数组
			var arr = [];
			for(var i=0;i<params.length;i++){
				var param = params[i];
				if(typeof(param)=="string"){//数组中是字符串
					arr.push(params[i]);	
				}else{//数组中是对象
					arr.push($.toJSON(params[i]));	
				}
			}	
			data = new PMSRequestObject(managerName,managerMethod,arr,token);
		}
	}
	var callBackReturnVal = null;
	var doManagerCallBack = function(_data, textStatus, XMLHttpRequest){
		if($.fromJSON(_data.data)!=null&&$.fromJSON(_data.data).version!=null){
			var doManagerVersionCom = $("#"+doManagerVersion);
			if(doManagerVersionCom.length==0){
				doManagerVersionCom = $("<input type='hidden' name='"+doManagerVersion+"' id='"+doManagerVersion+"' >");
				$(document.body).append(doManagerVersionCom);
			}
			doManagerVersionCom.val($.fromJSON(_data.data).version);
		}
		callBackReturnVal = callBack(_data, textStatus, XMLHttpRequest);
	}
	
	if(async==false){
		$$.asyncAjax(url, "requestString=" + encodeURIComponent(data.toJsonString()),doManagerCallBack,doManagerCallBack);
	}else{
		$$.ajax(url, "requestString=" + encodeURIComponent(data.toJsonString()),doManagerCallBack,doManagerCallBack); 	
	}
	return callBackReturnVal;
}

var WFExceptionType = {
	TEMPLATENOTEXISTEXCEPTION:"${WFExceptionType.TEMPLATENOTEXISTEXCEPTION}",
	EXISTDOCUMENT:"${WFExceptionType.EXISTDOCUMENT}",
	NOAPPROVALNODE:"${WFExceptionType.NOAPPROVALNODE}",
	STATIONHASNOTUSER:"${WFExceptionType.STATIONHASNOTUSER}",
	CONTROLPOINTERROR:"${WFExceptionType.CONTROLPOINTERROR}",
	NULLDOCUMENTTYPE:"${WFExceptionType.NULLDOCUMENTTYPE}",
	NUllORGID:"${WFExceptionType.NUllORGID}",
	INVALIDATEDOCUMENTTYPE:"${WFExceptionType.INVALIDATEDOCUMENTTYPE}",
	INVALIDATEORGID:"${WFExceptionType.INVALIDATEORGID}",
	USERHASNOTGROUP:"${WFExceptionType.USERHASNOTGROUP}",
	WORKFLOWSERVEREXCEPTION:"${WFExceptionType.WORKFLOWSERVEREXCEPTION}",
	OTHERWFEXCEPTION:"${WFExceptionType.OTHERWFEXCEPTION}"
};

(function(){
	if (typeof this.wf === 'undefined') {
       this.wf = {};
    }
	wf.alert = function (msg,callback){
		
		$$.showMessage('工作流组件提示',msg, function(data){
			if(callback!=null)
				callback(true);
		});
	};
	
})();

function getWFTaskList(documentId, workFlowTye, workFlowTableId, callBack) {
	wfDoManager("userManager","getCurrentUserDTO",[],function(data){
		var userDTO = $.fromJSON(data.data);
		wfDoManager("workFlowManager", "getWFTaskList", [documentId, workFlowTye+"|"+userDTO.code], function(data){
            var result = $.fromJSON(data.data);
            $(workFlowTableObj).data(workFlowTableId).setValues(result);
			var userCodes = new Array();
			var nowArray = $(workFlowTableObj).data(workFlowTableId).getValues();
			for(var i = 0; i < nowArray.length; i++) {
				if((nowArray[i].approvedactionuserid != null) &&(nowArray[i].approvedactionuserid.length > 0)) {
					if ($.inArray(nowArray[i].approvedactionuserid,userCodes) == -1) {
						userCodes.push(nowArray[i].approvedactionuserid);
					}
				}
			}
			doManager("approvalAttachmentManager", "getApprovalAttachment", [documentId,workFlowTye,userCodes], function(data) {
				FileUploadTableForApproval.items["approvalTable1"].setValues($.fromJSON(data.data));
			}) 	
			if (callBack != undefined) {
				callBack();
			}
        });
		
	});
}

function handleStartWFException(msg) {
	
	if((msg==null) ||(eval("WFExceptionType."+msg) == "undefined")) {
		return false;
	} 
	wf.alert(eval("WFExceptionType."+msg))
	return true;
	
}

function doStartWorkFlowForSelect(successUrl, datas, doActionManager,args) {
	if(datas.wfUtilDTO.startWFResponse == "SELECTAPPROVAL") {
		parent.window.approvalUsers = datas.wfUtilDTO.approvalUsers;
		var mySelectAlert = new userSelectAlert(successUrl,datas.wfUtilDTO.approvalMsg,doActionManager,args);
		mySelectAlert.show();
	} else {
		if (successUrl == "#") {
			wf.alert("创建成功");
		}
		else {
			location = wfBaseUrl + successUrl;
		}
	}
}

function wfChangeBT(flag, validateForm, showLabelId, textAreaId) {
	 $validator = new PMSValidator(
		$("#"+validateForm)[0], 
		{
			bNormalDisplay : false,
			iDisplayLength : 999
	});
	$validator.clean();
	if(flag == "N") {
		$("#"+showLabelId).show();
		$("#"+textAreaId).attr("validate", "required:true,maxlength:40");
	} else {
		$("#"+showLabelId).hide();
		$("#"+textAreaId).attr("validate", "maxlength:40");
	}
}


function approvalTextAreaValidate(validateForm,nowValue,expectedValiValue) {
	if(nowValue == expectedValiValue) {
		if(!$("#"+validateForm).valid()){
			return false;
		} 
		return nowValue;
	} else {
		return nowValue;
	}
}

function getApprovalPermission(workFlowType, documentId, callBack) {
	var userDTO = "";
	wfDoManager("userManager","getCurrentUserDTO",[],function(data){
		var userDTO = $.fromJSON(data.data);		
	},false);
	var flag =  wfDoManager("workFlowManager", "getApprovalDocument",[workFlowType+"|"+userDTO.code,documentId], function(data){
		var result = $.fromJSON(data.data);
		if(result == "true") {
			callBack();
			return "true"; 
		} else if(result == "false"){
			wf.alert("${WFAPPROVALMESSAGE.NOPERMISSION}");
			return "false";
		} else if(result == "approvaled"){
			wf.alert("${WFAPPROVALMESSAGE.APPROVALED}");
			return "approvaled";
		} else if(result == "noturn") {
			wf.alert("${WFAPPROVALMESSAGE.NOTURN}");
			return "noturn";
		}
	},false);
	return flag;
}
function doMyInitDocument() {
     var retendElement  =     "$('<form id=\"wfApprovalForm\" class=\"pmsForm\" validate=\"true\" clientvalidate=\"true\">"
			                +      "<table width=\"100%\"><tr><td align=\"left\" class=\"basetext\"><strong>审批流程</strong></td></tr>"
			                +            "<tr><td align=\"left\" class=\"basetext\"><div id=\"workFlowTables_wfCommon\" class=\"workFlowTables\"></div></td></tr>"
			                +            "<tr><td align=\"right\" width=\"100%\">"
			                +                     "<table><tr><td width=\"2%\" height=\"30\" align=\"right\"class=\"basetext\">审核结果:</td>"
			                +                                "<td width=\"98%\" align=\"left\"><input type=\"radio\" name=\"approval_result\" checked=\"checked\" onclick=\"wfChangeBT('Y', 'wfApprovalForm', 'approvalSpan_wfcommon', 'approvalIdea_wfCommon');\" value=\"0003\"/>通过<input type=\"radio\" name=\"approval_result\" onclick=\"wfChangeBT('N', 'wfApprovalForm', 'approvalSpan_wfcommon', 'approvalIdea_wfCommon');\" value=\"0004\"/>拒绝</td>"
			                +                           "</tr>"
			                +                           "<tr><td width=\"2%\" align=\"right\" height=\"50\"class=\"basetext\">审核意见:<span class=\"red\" id=\"approvalSpan_wfcommon\" style=\"display:none\">*</span></td>"
			                +                                "<td width=\"98%\" align=\"left\" style=\"white-space:normal;\"><textarea name=\"approvalIdea_wfCommon\" id=\"approvalIdea_wfCommon\" style=\"width:99%\" rows=\"4\"></textarea></td>"
			                +                           "</tr>"
							+							"<tr><td height=\"4px\"></td>"
							+							"</tr>"
							+							"<tr><td colspan=\"2\" width=\"97%\" align=\"right\"  style=\"white-space:normal;\"><div class=\"uploadTable\" ></div></td>"
							+							"</tr>"
							+						    "<tr><td height=\"10px\" colspan=\"2\" align=\"right\"></td>"
							+							"</tr>"
							+							"<tr><td colspan=\"2\" width=\"97%\" align=\"right\"  style=\"white-space:normal;\"><div class=\"approvalUploadTable\" id=\"approvalTable_wfCommon\" defaults='{showAddButton:false}'></div></td>"
							+							"</tr>"
			                +                           "<tr><td height=\"10px\" colspan=\"2\" align=\"right\"></td>"
			                +                           "</tr>"
			                +                      "</table>"
			                +                    "</td>"
			                +              "</tr>"
			                +        "</table>"
			                +   "</form>')";
	
			$("#approvalFrom_wfCommon").append(retendElement);

}
