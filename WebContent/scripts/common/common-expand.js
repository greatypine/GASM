var PMSMethod = function(html, func) {
	this.html = html;
	if(func && typeof func == 'function') {
		this.func = func;	
	} else {
		this.func = function() {}
	}
}
String.prototype.trim=function(){
     return this.replace(/(^\s*)|(\s*$)/g, '');
}
$.isEmpty = function(v) {
	switch (typeof v){
	case 'undefined' : 
		return true;
		break;
	case 'string' : 
		if(v.length == 0 || v.trim().length == 0) {
			return true; 
		}
		break;
	case 'boolean' : 
		if(!v) {
			return true;
		}
		break;
	case 'number' : 
		if(0 === v) {
			return true; 
		}
		break;
	case 'object' :
		if(null === v) {
			return true;
		}
		if(undefined !== v.length && v.length==0) {
			return true;
		}
		for(var k in v) {
			return false;
		}
		return true;
		break;
	}
	return false;
}

//this is for enterring password when download files.
PMSApplication.prototype.enterPasswd = function(data) {
	var div = $("<div></div>");
	div.html("请输入密码:<input type='text' id='passwd' name='passwd'/>");
	
	div.dialog({
		bgiframe: true,
		title:"输入密码",
		width:480,
//		height:390,
		buttons : {
			"OK":function() {
				var passwd = document.getElementById("passwd").value;
				enterPasswd(passwd);
							
			}
		},
		modal:true
	});
	var enterPasswd = function(passwd) {
		if(passwd==null||passwd==""){
			$$.showMessage("输入密码","输入密码");
			return;
		}
		var url = "download.do?passwd="+passwd+"&id="+data;
		var formVar = document.getElementById("form1");
		formVar.action= url;
		formVar.submit();
		div.dialog("close");
//		$$.ajax(url, null, function(data, textStatus, XMLHttpRequest){
//			div.dialog("close");
//		});
	}
}

PMSApplication.prototype.removeObj = function(queryid, data, nTr, allColumns) {
	var div = $("<div></div>");
	div.html("${query.ui.confirm.delete}");
	var doDelete = function(data) {
		var url = $$.PMSDispatchActionUrl;
		var data = new PMSRequestObject("baseManager",
				"removeObj", [ queryid, data[0] ]);
		$$.ajax(url, "requestString="+data.toJsonString(), function(data, textStatus, XMLHttpRequest){
			div.dialog("close");
			if(data.result) {
				$(nTr).remove();
			}
		});
	}
	div.dialog({
		bgiframe: true,
		title:"${query.ui.prompt}",
		width:320,
		buttons : {
			"${query.ui.ok}":function() {
				doDelete(data);
			},
			"${query.ui.cancel}":function(){
				$(this).dialog("close");
			}
		},
		modal:true
	});
}

/**
 * 自定义搜索框的搜索方法
 * @param {Object} queryId queryid
 * @param {Object} searchDivId 自定义搜索条件所在的div的id
 * @param {Object} queryDivId queryId所在的div的id.(可选)
 */
PMSApplication.prototype.executeSearch = function(queryId, searchDivId, queryDivId) {
	var gridStr = "div";
	if(queryDivId && queryDivId.length>0) gridStr += "[id='" + queryDivId + "']";
	gridStr += "[queryid='" + queryId + "']";
	$(gridStr).each(function(i) {
		var configContainer = $(this);
		configContainer.children().remove();
		$$.fetchGridMetadata(configContainer);
		var autoload = $(this).attr("autoload") == 'false' ? false : true;
		if (autoload === false) {
			$$.executeQuery($(this),searchDivId);
		}
	});
}
/**
 * 自定义查询
 * @param {Object} queryGridId
 * @param {Object} reqObj
 */
PMSApplication.prototype.executeMutilSearch = function(queryGridId, managerName, methodName, reqObj) {
	var clientReqObj = new PMSRequestObject(managerName, methodName, reqObj);
	var grid = $("#"+queryGridId);
	var columns = grid.data("columns");
	var aoColumns = [];
	// Set Query info.
	for(var i = 0; i < columns.length; i++) {
		 var col = columns[i];
		 // Set query info.
		 var tmp = col.visible ? col.display : false;
		 grid.data("columns")[i].display = tmp;
		 columns[i].display = tmp;
		 // Set Display.
		 aoColumns[i] = {
			 "bVisible" : tmp,
			 "sColumnName" : col.key, 
			 "sWidth" : col.width, 
			 "bSortable" : col.sort,
			 "sAlign": col.align
		 };
	 }
	
	var table = grid.data("dataTable");
	var bShowDetail = grid.attr("showdetail") == "true" ? true : false;
	var bShowCheckbox = grid.attr("showcheckbox") == "true" ? true : false;
	
	var bShowOperate = false;
	
	var opArrs = eval(grid.attr("operators"));
	bShowOperate = (opArrs && (opArrs.length>0) ) ? true : false;

	var addFunc = eval(grid.attr("addFunc"));
	var bShowAdd = (typeof addFunc) == 'function' ? true : false

	var bShowTitle = grid.attr("showtitle") == "false" ? false : true;
	var bShowPrint = grid.attr("showprint") == "false" ? false : true;
	var bShowPaging = grid.attr("showpaging") == "false" ? false : true;

	var searchDiv = $("#not exists div id");

	var queryid = grid.attr("queryid"); 
	var td = table.dataTable({
		"bProcessing" : true,
		"bDestroy" : true,
		"bServerSide" : true,
		"bAutoWidth" : false,
		"sDom" : '<"H"hr>tsq<"F"lip>',
		"bLengthChange": true,
		"bInfo" : true,
		"queryid" : queryid,
		"aoColumns" : aoColumns,
		"allColumns" : columns,
		"bShowCheckbox" : bShowCheckbox,
		"bShowDetail" : bShowDetail,
		"bShowOperate" : bShowOperate,
		"bShowTitle" : bShowTitle,
		"bShowPrint" : bShowPrint,
		"bShowPaging" : bShowPaging,
		"operators" : opArrs,
		"bShowAdd" : bShowAdd,
		"addFunc" : addFunc,
		"searchDiv" : searchDiv,
		"bPaginate": true,
		"reqObj":reqObj,
		"sPaginationType": "full_numbers",
		"sPostData": "requestString=" + clientReqObj.toJsonString(),
		"sAjaxSource" : $$.PMSDispatchActionUrl
	});
	grid.data("jquery.datatable", td);
}
//Should remove this wrong named method.
PMSApplication.prototype.ajaxAsync = function(url, data, callback, failureCallback) {
	this.ajaxSync(url, data, callback, failureCallback);
};
PMSApplication.prototype.exportExcel = function(templatePath, gridContainerName){

	var manager = "queryManager";
	var method = "getCount";
	
	if(typeof(gridContainerName) == "undefined" || gridContainerName == ""){
		gridContainerName = "gridContainer";
	}

	var queryConditions = this.buildQueryConditions($('#' + gridContainerName));
		
	reqObj = new PMSRequestObject(manager, method, [$.toJSON(queryConditions)]);   
	$$.serverInvoke(reqObj, function(data, textStatus, XMLHttpRequest) {
		var result = $.fromJSON(data.data).result;
		if(result) {
    		var url = $$.PMSReportActionUrl + "?skip=true&isNew=1&templatePath=" + templatePath + "&requestType=4&param=" + $.toJSON(queryConditions);
    		var frm = $("<form></form>");
    		frm.attr("method", "post");
    		frm.attr("action", url);
    		$("body").append(frm);
    		frm.submit();		
		} else {
			$$.onServerInvokeFailed(XMLHttpRequest, "${client.exportOverLimit}");
		}
	});   
};
PMSApplication.prototype.offlineExport = function(templatePath, gridContainerName){

	var manager = "queryManager";
	var method = "getCountForOffline";
	
	if(typeof(gridContainerName) == "undefined" || gridContainerName == ""){
		gridContainerName = "gridContainer";
	}

	var queryConditions = this.buildQueryConditions($('#' + gridContainerName));
		
	var reqObj = new PMSRequestObject(manager, method, [$.toJSON(queryConditions)]);   
	$$.serverInvoke(reqObj, function(data, textStatus, XMLHttpRequest) {
		var result = $.fromJSON(data.data).result;
		var querymaxsize = $.fromJSON(data.data).querymaxsize;
		if(result) {
			this.showPromptDialog("请输入离线下载任务名称： ", '<input type="text" id="taskName" style="width:200px" /><font color="red">(&nbsp;*&nbsp;不能为空)</font>', true, '50%', function(){
				var taskName = $('#taskName').val();
				if(taskName == ""){
					$$.offlineExport(templatePath,gridContainerName);
					return;
				}
		
					
				var url = $$.PMSDispatchActionUrl;
				var businessName = document.title;
					
				var object = { 		
					id:null,	
					conditions: $.toJSON(queryConditions), 	
					status:	'0', 		
					templatePath: templatePath,
					requestType: 6,
					taskName: taskName,
					businessName: businessName
				}
				
				var data = new PMSRequestObject("reportTaskManager", "save", [ $.toJSON(object) ],null); 
				
				$$.ajax(url, "requestString=" +  data.toJsonString(), 
					function(data, textStatus, XMLHttpRequest){
						$$.showMessage('${query.ui.prompt}', "请到我的任务中查看!");
					}
				); 
		
			});
 	
		} else {
			$$.onServerInvokeFailed(XMLHttpRequest, "离线下载最多支持" + querymaxsize + "条数据，请重新选择条件再进行下载。");
		}
	}); 

};
PMSApplication.prototype.importData = function(searchDiv, obj, columns) {
	var container = $("#" + searchDiv);
	for ( var attr in obj) {
		if ((!obj[attr]) || typeof (obj[attr]) == 'function') {
			continue;
		}
		var ipt = container.find(" :input[name='" + attr + "']");
		if (ipt.length > 0) {
			var col = obj[attr];
			var type = ipt.attr("type");
			var tmp = col.value;
			if (null == tmp || 'undifined' == tmp) {
				continue;
			}
			if (type == "checkbox" || type == "radio") {
				if (typeof tmp != 'string')
					tmp = tmp + '';
				var vs = tmp.split(",");
				for ( var v = 0; v < vs.length; v++) {
					for ( var h = 0; h < ipt.length; h++) {
						if ($(ipt[h]).val() == vs[v]) {
							$(ipt[h]).attr("checked", true);
						}
					}
				}
			} else if (tmp.indexOf(',') >= 0) {
				var betV = tmp;
				if (betV.indexOf(",") == 0) {
					$(ipt[1]).val(betV.substr(1));
				} else if (betV.indexOf(",") == (betV.length - 1)) {
					$(ipt[0]).val(betV.substr(0, betV.length - 1));
				} else {
					$(ipt[0]).val(betV.substr(0, betV.indexOf(",")));
					$(ipt[1]).val(betV.substr(betV.indexOf(",") + 1));
				}
			} else {
				ipt.val(col.value);
			}
		}
	}
};

////是否需要加入cachekey？
//PMSApplication.prototype.buildQueryConditions = function(grid) {
//	var bShowPaging = grid.attr("showpaging") == "false" ? false : true;
//	var cacheable = (grid.attr("usecache") == "true");
//	function parseSort(sDefaultSort){
//		var oSortinfo = null;
//		if(sDefaultSort != undefined && sDefaultSort != ""){
//			function doParse(sSortinfo){
//				var a = sSortinfo.split(/\s/);
//				return {
//					variableName: a[0],
//					direction: a[1] == "desc" ? -1 : 1
//				};
//			};
//			var a = $.trim(sDefaultSort).split(",");
//			var oPrevSort;
//			for(var i = 0; i < a.length; i++){
//				var o = doParse($.trim(a[i]));
//				if(i == 0){
//					oSortinfo = o;
//				}
//				if(oPrevSort != null){
//					oPrevSort.next = o;
//				}
//				oPrevSort = o;
//			}
//		}
//		return oSortinfo;
//	};
//	var order = grid.data("order");
//	var oSortinfo = parseSort(order);
//	
//	var queryReqObj = {
//		conditions: [],
////		cache : usecache,
//		cacheable: cacheable,
//		pageinfo : {
//			currentPage : 1,
//			recordsPerPage: bShowPaging ? 100 : 9999
//		},
//		sortinfo: oSortinfo,
//		queryId : grid.attr("queryid")
//	};
//	
//	var divId = grid.attr("searchDiv");
////	var searchDiv = (divId && divId.length > 0) ? $("#" + divId) : null;
////	if (searchDiv === null) {
////		searchDiv = $("#" + "PMS_SEARCH_CONDITION_DIV_" + queryId);
////	}
//	var conditions = [];
//	if (searchDiv !== null && searchDiv.length > 0) {
//		var ele = searchDiv.find(':input[name]');
//		ele.each(function(i) {
//			var map = {};
//			var key = $(this).attr("name");
//			var isExist = false;
//			for (var j=0; j<conditions.length; j++) {
//				if (key == conditions[j].key) {
//					isExist = true;
//					map = conditions[j];
//					break;
//				}
//			}
//			var value = $(this).val();
//			var type = $(this).attr("type");
//			if ((type == 'checkbox' || type == 'radio')) {
//				if ($(this).attr("checked") !== true) {
//					value = "";
//				}
//			}
//			if (isExist) {
//				map.value += encodeURIComponent("," + value);
//			} else {
//				map.key = key;
//				map.value = encodeURIComponent(value);
//				conditions.push(map);
//			}
//		});
//	} else {
//		// no search conditions found.
//	}
//	queryReqObj.conditions = conditions;
//	
//	return queryReqObj;
//};


var PMSColumnModel = function(key, value, operator) {
	this.key = key;
	this.value = value;
	this.operator = operator;
}

var PMSOperator = function(name, value) {
	this.name = name;
	this.value = value;
}

PMSOperator.prototype.LIKE = function() {
	return new PMSOperator("LIKE", 1);
}

PMSOperator.prototype.EQ = function() {
	return new PMSOperator("EQ", 2);
}

PMSOperator.prototype.NOT_EQ = function() {
	return new PMSOperator("NOT_EQ", 3);
}

PMSOperator.prototype.GREATER_THAN = function() {
	return new PMSOperator("GREATER_THAN", 4);
}

PMSOperator.prototype.LESS_THEN = function() {
	return new PMSOperator("LESS_THEN", 5);
}

PMSOperator.prototype.GREATER_EQ = function() {
	return new PMSOperator("GREATER_EQ", 6);
}

PMSOperator.prototype.LESS_EQ = function() {
	return new PMSOperator("LESS_EQ", 7);
}

PMSOperator.prototype.IN = function() {
	return new PMSOperator("IN", 8);
}

PMSOperator.prototype.NOT_IN = function() {
	return new PMSOperator("NOT_IN", 9);
}

PMSOperator.prototype.AND = function() {
	return new PMSOperator("AND", 10);
}

PMSOperator.prototype.OR = function() {
	return new PMSOperator("OR", 11);
}

PMSOperator.prototype.BETWEEN = function() {
	return new PMSOperator("BETWEEN", 12);
}

//PMSApplication.prototype.buildOperatorElement = function() {
//	var operatorElement = $("<SELECT>" + "<option value=\"1\">like</option>"
//			+ "<option value=\"2\" selected>=</option>"
//			+ "<option value=\"3\">!=</option>"
//			+ "<option value=\"4\">&gt;</option>"
//			+ "<option value=\"5\">&lt;</option>"
//			+ "<option value=\"6\">&gt;=</option>"
//			+ "<option value=\"7\">&lt;=</option>"
//			+ "<option value=\"12\">between</option>"
//			+ "</SELECT");
//	return operatorElement;
//}
//
//PMSApplication.prototype.buildSimpleOperatorElement = function() {
//	var operatorElement = $("<SELECT>" + "<option value=\"1\">like</option>"
//			+ "<option value=\"2\" selected>=</option>"
//			+ "<option value=\"3\">!=</option>"
//			+ "</SELECT");
//	return operatorElement;
//}
//
//PMSApplication.prototype.buildConjuctionElement = function() {
//	var operatorElement = $("<SELECT>"
//			+ "<option value=\"10\" selected>${query.ui.and}</option>"
//			+ "<option value=\"11\">${query.ui.or}</option></SELECT");
//	return operatorElement;
//}
