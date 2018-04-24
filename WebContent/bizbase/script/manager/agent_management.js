
var currNode = null;
$(function(){
	var treeObj = $('#rftree').tree({
		onClick:function (node){
			
			currNode = node;
			loadOrgInfoByNode(node);
			document.getElementById("newTableId").value = node.attributes.id;
			document.getElementById("newSuperiorOrgCode").value = node.id;
			//top.document.getElementById("main").src = (node.url);
		},
		expandAll:true,
		managerName:"com.cnpc.pms.bid.manager.OrganizationManager",
		methodName:"excuteOrganizationTree",
		methodParams:["0"],
		remote:true,
		id:"rftree",
		refreshMode:'never',
		disabledMode:'none',
		animation:false, //false/true
		speed:'normal',//'slow'/'normal'/'fast'/number(eg:1000)
		checkbox:false, //false/true
		checkMode:'related',//single(默认单选)/related(级联)/free(多选)
		display:true
	});
});	


function new_agent(){
	var showtr1=document.getElementById("newagent");
	var showtr2=document.getElementById("editagent");
	if(currNode==null){
		alert("请选择上级组织！");	
		return;
	}
   showtr2.style.display="none";
   showtr1.style.display="block";

}

function edit_agent(){
	if(currNode==null){
		alert("请选择组织机构！");	
		return;
	}
	var showtr1=document.getElementById("newagent");
	var showtr2=document.getElementById("editagent");
   showtr1.style.display="none";
   showtr2.style.display="block";

}


/*
* @methodName   : doSubmit
* @methodParam  : none
* @methodReturn : none
* @author       : ruanqingfeng
* @desc         : 新增组织机构时，点击提交方法触发的函数
* @for example  : doSubmit();
*/
function doSubmit(){
	if(currNode==null){
		alert("请选择上级组织！");	
		return;
	}
	var url = $$.PMSDispatchActionUrl;
	var newOrgNameObj = $("#newOrgName");
	var orgType = $.getRadioChecked("newOrgType");
	var newSuperiorOrgCodeObj = $("#newSuperiorOrgCode");
	var newFunBidRequireFlag = $.getCheckBoxValues("newFunBidRequireFlag");

	var funBidRequireFlag = "";
	var param = {
		orgName : newOrgNameObj.val(),
		orgType : orgType,
		superiorOrgCode : newSuperiorOrgCodeObj.val(),
		funBidRequireFlag : newFunBidRequireFlag
	};
	
	var data = new PMSRequestObject("organizationManager",
			"addNewOrganization", [$.toJSON(param) ]);
	
	$$.ajax(url, "requestString="+data.toJsonString(), function(data, textStatus, XMLHttpRequest){
		if(data.result) {
			$('#rftree').tree.reload();
		}
	});	
}


/*
* @methodName    : doDelete
* @methodParam1  : orgCode
* @methodReturn  : none
* @author        : ruanqingfeng
* @desc          : 删除组织机构时，点击方法触发的函数
* @for example   : doDelete();
*/
function doDelete(){
	var newTableIdObj = document.getElementById("newTableId");
	
	if(currNode==null){
		alert("请选择上级组织！");	
		return;
	}
	if(currNode.isLeaf==false){
		alert("您选择的组织机构存在下级机构，无法删除！");
		return;
	}
	
	
	var url = $$.PMSDispatchActionUrl;
	
	var data = new PMSRequestObject("organizationManager",
			"deleteOrganization", [newTableIdObj.value]);
	
	$$.ajax(url, "requestString="+data.toJsonString(), function(data, textStatus, XMLHttpRequest){
		if(data.result) {
			document.getElementById("newSuperiorOrgCode").value = "0";
			$('#rftree').tree.reload();
		}
	});	
}

/*
* @methodName   : doEditSubmit
* @methodParam  : none
* @methodReturn : none
* @author       : ruanqingfeng
* @desc         : 编辑组织机构时，点击提交方法触发的函数
* @for example  : doEditSubmit();
*/
function doEditSubmit(){
	if(currNode==null){
		alert("请选择上级组织！");	
		return;
	}
	var url = $$.PMSDispatchActionUrl;
	var tableIdObj = $("#tableId");
	var orgNameObj = $("#orgName");
	var orgCodeObj = $("#orgCode");
	var orgType = $.getRadioChecked("orgType");
	var validFlag = $.getRadioChecked("validFlag");
	var superiorOrgCodeObj = $("#superiorOrgCode");
	var funBidRequireFlag = $.getCheckBoxValues("funBidRequireFlag");
	var funBidRequireFlag = "";
	var param = {
		id : tableIdObj.val(),
		orgName : orgNameObj.val(),
		orgCode : orgCodeObj.val(),
		orgType : orgType,
		validFlag : validFlag,
		superiorOrgCode : superiorOrgCodeObj.val(),
		funBidRequireFlag : funBidRequireFlag
	};
	
	var data = new PMSRequestObject("rganizationManager",
			"modifyOrganization", [$.toJSON(param) ]);
	
	$$.ajax(url, "requestString="+data.toJsonString(), function(data, textStatus, XMLHttpRequest){
		if(data.result) {
			$('#rftree').tree.reload();
		}
	});	
}


/*
* @methodName    : loadOrgInfoByNode
* @methodParam1  : node
* @methodReturn  : none
* @author        : ruanqingfeng
* @desc          : 编辑组织机构时，需要选中节点，点击节点触发函数，实现赋值
* @for example   : loadOrgInfoByNode(node);
*/
function loadOrgInfoByNode(node){
	var attribute = node.attributes;
	$("#tableId").val(attribute.id);
	$("#orgCode").val(attribute.orgCode);
	$("#orgName").val(attribute.orgName);
	$("#superiorOrgCode").val(attribute.superiorOrgCode);
	$("#createDate").val(attribute.createDate);
	$("#createUserID").val(attribute.createUserID);
	$("#lastModifyDate").val(attribute.lastModifyDate);
	$("#orgCode").val(attribute.orgCode);
	$.setRadioChecked("orgType",attribute.orgType);
	$.setRadioChecked("validFlag",attribute.validFlag);
	$.setCheckBoxValues("funBidRequireFlag",attribute.funBidRequireFlag);
}

