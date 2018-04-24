/*!
 * CNPC PMS Web Application JavaScript Common Library v0.0.1
 * http://www.cnpc.com.cn/
 * Author: Zhou Zaiqing
 *
 * Copyright 2010 IBM Corporation.
 * 
 */

/**
 * PMSApplication Class Definition.
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/21
 */
var PMSApplication = function() {
	this.version = "v0.1";
	this.className = "PMSApplication";
	this.i18nElementTag = "span";
	this.i18nElementAttribute = "MessageKey";
	this.requestTimeout = 30000; // unit: milliseconds.
	this.gridContainerTag = "div[queryid]";
	this.autoCompleteTag = "input[clsname]";
	this.dialog = null;
	this.queryManagerName = "queryManager";
	this.queryMetadataMethod = "getQueryColumns";
	this.queryMethod = "executeQuery";
	this.queryByIdMethod = "executeQueryById";
	this.queryStatisticsMethod = "executeStatistics";
	this.printing = {
		printContainer : null,
		printUrl : null
	}
	/**
	 * PMS Dispatcher Action URL. The common way to communicate with server
	 * side.
	 */
	this.PMSDispatchActionUrl = "/dispatcher.action";
	this.PMSReportActionUrl = "/report.do";
	this.PMSAutoCompleteActionUrl = "/autocomplete.action?requestString=";
};

var PMSMethod = function(html, func) {
	this.html = html;
	if(func && typeof func == 'function') {
		this.func = func;	
	} else {
		this.func = function() {}
	}
}

/**
 * Application Initialize Handler.
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/21
 */
PMSApplication.prototype.initialize = function() {
	this.initProcessUrl();
	this.initUISet();
	this.initI18nResource();
};

PMSApplication.prototype.initProcessUrl = function() {
	var href = window.location.href.substr(0, window.location.href
			.lastIndexOf("/"));
	this.PMSDispatchActionUrl = href + "/dispatcher.action";
	this.PMSReportActionUrl = href + "/report.do";
	this.PMSAutoCompleteActionUrl = href
			+ "/autocomplete.action?requestString=";
};

/**
 * Process all i18n bundles.
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/21
 */
PMSApplication.prototype.initI18nResource = function() {
	var attrName = this.i18nElementAttribute;
	$(this.i18nElementTag).each(function() {
		var resourceElement = $(this);
		var message = resourceElement.attr(attrName);
		if (message) {
			resourceElement.html(message);
		}
	});
};

/**
 * The handler of server sider success invocation.
 * 
 * @author Zhou zaiqing
 * @since 2010/11/22
 */
PMSApplication.prototype.onServerInvokeSuccess = function(data, textStatus,
		XMLHttpRequest) {
	$$.showMessage("Fetch OK", $.toJSON(data));
}

/**
 * The handler of server sider fail invocation.
 * 
 * @author Zhou zaiqing
 * @since 2010/11/22
 */


PMSApplication.prototype.onServerInvokeFailed = function(XMLHttpRequest,
		textStatus, errorThrown) {
			//alert("5");
			
	$$.showMessage("${client.httperrortitle5}",
			"${client.httperrorcontent}\r\nReason: " + textStatus);
}


/**
 * Server side services invocation interface with callback function.
 * 
 * @author Zhou zaiqing
 * @since 2010/11/22
 */
PMSApplication.prototype.serverInvoke = function(reqObj, callback, context) {
	var sendString = "requestString=" + reqObj.toJsonString();
	var sendCtx = document.body;
	if (context != null)
		sendCtx = context;
	if (callback == null) {
		callback = $$.onServerInvokeSuccess;
	}
	this.ajax($$.PMSDispatchActionUrl, sendString, callback);
};

PMSApplication.prototype.ajax = function(url, data, callback) {
	$.ajax({
		url : url,
		dataType : "json",
		type : "post",
		data : data,
		timeout : $$.requestTimeout,
		success : function(data, textStatus, XMLHttpRequest) {
			alert("4");
			if (data.result) {
				//alert("1");
				callback(data, textStatus, XMLHttpRequest);
			} else {
				//alert("2");
				$$.onServerInvokeFailed(XMLHttpRequest, data.message)
			}
			//alert("3");
		},
		error : $$.onServerInvokeFailed
	});
}

PMSApplication.prototype.dataBinding = function() {
	// Printing support
	if (this.printing.printUrl != null) {
		$("body").prepend(this.printing.printObj);
		this.printing.printObj.printing.PrintUrl(this.printing.printUrl, true);
	}
	this.prepareComponents();
}

PMSApplication.prototype.prepareComponents = function() {
	// Initialize Container of Grids.
	$(this.gridContainerTag).each(function(index) {
		var gridContainer = $(this);
		$$.fetchGridMetadata(gridContainer);
	});

	// Initialize AutoComplete Components.
	$(this.autoCompleteTag).each(
			function(index) {
				this.autoCompleteContainer = $(this);
				var reqObj = {
					className : this.autoCompleteContainer.attr("clsName"),
					keyword : this.autoCompleteContainer.val(),
					properties : this.autoCompleteContainer.attr("propsname")
							.split(",")
				};
				var url = $$.PMSAutoCompleteActionUrl + $.toJSON(reqObj);

				this.autoCompleteContainer.autocomplete(url, {
					matchContains : true,
					formatItem : this.formatAutoCompleteItem,
					formatResult : this.formatAutoCompleteResult
				});
				// $$.processAutoCompleteElement($(this),
				// $(this).attr("clsName"), $(this).attr("propsname"));
			});
}

PMSApplication.prototype.processAutoCompleteElement = function(element,
		className, properties) {
	var reqObj = {
		className : className,
		keyword : element.val(),
		properties : properties.split(",")
	};
	var url = $$.PMSAutoCompleteActionUrl + $.toJSON(reqObj);
	element.autocomplete(url, {
		matchContains : true,
		formatItem : $$.formatAutoCompleteItem,
		formatResult : $$.formatAutoCompleteResult,
		minChars : 0
	});
}

PMSApplication.prototype.formatAutoCompleteItem = function(row) {
	return "<strong>" + row[0] + "</strong>";
}

PMSApplication.prototype.formatAutoCompleteResult = function(row) {
	return row[0];
}

PMSApplication.prototype.buildGridSearchContainer = function(gridContainer) {
	/**
	 * <h3><a href="#">First header</a></h3>
	 * <div>First content</div>
	 * <h3><a href="#">Second header</a></h3>
	 * <div>Second content</div>
	 */
	var isSimpleQuery = gridContainer.attr("queryformat") == "simple";

	var queryControlPanel = $("<div></div>");
	queryControlPanel
			.toggleClass("ui-accordion ui-widget ui-helper-reset ui-accodion-icons");
	var searchTitle = $("<h3><a href=\"#\">${query.search.searchtitle}</a></h3>");
	searchTitle
			.toggleClass("ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-top");

	var searchDiv = $("<div></div>");
	searchTitle.click(function() {
		$(this).next().toggle('fast');
		return false;
	});
	queryControlPanel.append(searchTitle);
	queryControlPanel.append(searchDiv);

	var conditionTitle = $("<h3><a href=\"#\">${query.search.conditiontitle}</a></h3>");
	conditionTitle
			.toggleClass("ui-accordion-header ui-helper-reset ui-state-default ui-state-active ui-corner-all");
	var conditionDiv = $("<div></div>");
	conditionTitle.click(function() {
		$(this).next().toggle("fast");
	}).next().hide();
	queryControlPanel.append(conditionTitle);
	queryControlPanel.append(conditionDiv);

	searchDiv.toggleClass("searchCondition");
	conditionDiv.toggleClass("searchCondition");

	searchDiv.id = gridContainer.id + "_search";
	var columns = gridContainer.data("columns");
	var operators = [];
	
	var searchColumns = new Array();
	for ( var i = 0; i < columns.length; i++) {
		var column = columns[i];
		if(!column.visible) {
			continue;
		}
		if (column.allowSearch) {
//			searchColumns.push(column);
			
			
			if (searchDiv.children().length > 0) {
				// TODO: Add 'and' and 'or' conjuction;
				var conjunction = $$.buildConjuctionElement();
				conjunction.attr("id", gridContainer.id + "_conjunction_"
						+ column.key);
				conjunction[0].value = column.conjunction;
				operators.push(conjunction);
				searchDiv.append(conjunction);
			}
			var label = $("<label></label>");
			label.html(column.header);
			var operator = null;
//			if(column.type != "java.lang.Long" && column.type != "java.lang.Double" && column.type != "java.sql.Date") {
//				operator = $$.buildSimpleOperatorElement();
//			} else {
//				operator = $$.buildOperatorElement();
//			}
			operator = $$.buildOperatorElement();
			operator.attr("id", gridContainer.id + "_operator_" + column.key);
			operator[0].value = column.operator;
			
			operator.change(function(){
				var _that = $(this);
				var temp = _that.attr("id");
				var _inputId = temp.replace("_operator_", "_input_");
				if(this.value == 12) {
					var _input = $(document.getElementById(_inputId));
//					var _input = $("#" + _inputId);
					var input2 = $("<input type='text' />");
					input2.attr("id", _inputId + "2");
					input2.name = input2.id;
						input2.addClass(_input.attr("class"));
						input2.attr("errormsg", _input.attr("errormsg"));
					if(_input.data("col")) {
						var coco = _input.data("col");
						$$.processAutoCompleteElement(input2, coco.type, coco.key.substr(coco.key.indexOf(".") + 1));
					}
					_input.after(input2);
				} else {
					var input2 = $(document.getElementById(_inputId + "2"));
					input2.remove();
				}
			});
			
			operators.push(operator);
			label.attr("id", gridContainer.id + "_label_" + column.key);
			var input = $("<input/>");
			input.attr("id", gridContainer.id + "_input_" + column.key);
			input.name = input.id;
			input.val(column.value);
			
			if(column.type == "java.lang.Long" || column.type == "java.lang.Double") {
				input.addClass("number");
				input.attr("errormsg", column.header+"${client.msg.mustbnum}");
//			} else {
//				input.addClass("required");
//				input.attr("errormsg", column.header+"${client.msg.required}");
			}
				
			var periodIndex = column.key.indexOf(".");
			if (periodIndex > 0 && periodIndex < column.key.length - 1) {
				input.data("col",column);
				$$.processAutoCompleteElement(input, column.type, column.key.substr(periodIndex + 1));
			} else {
				input.data("col",column);
				$$.processAutoCompleteElement(input, "com.cnpc.pms.poc.entity.SupplierInfo", column.key);
			}
			label.attr("for", input.id);
			searchDiv.append(label);
			searchDiv.append(operator);
			searchDiv.append(input);
		}

		// Insert Condition Panel
		var condition = $("<div></div>");
		var header = $("<strong></strong>");
		header.css("margin-right", "8px");
		header.html(column.header);
		condition.append(header);

		var displayCtl = $("<span>${query.ui.display}</span>");
		displayCtl.data("valueControl", $("<input type=\"checkbox\" />"));
		displayCtl.data("valueControl").data("column", column);
		displayCtl.data("valueControl").attr("checked", column.display);
		displayCtl.data("valueControl").click(function() {
			var valCtl = $(this);
			valCtl.data("column").display = valCtl.attr("checked");
		});
		displayCtl.append(displayCtl.data("valueControl"));
		condition.append(displayCtl);

		if (column.allowAvg) {
			var avgCtl = $("<span>${query.statistisc.avg}</span>");
			avgCtl.data("valueControl", $("<input type=\"checkbox\" />"));
			avgCtl.data("valueControl").data("column", column);
			avgCtl.data("valueControl").attr("checked", column.avg);
			avgCtl.data("valueControl").click(function() {
				var valCtl = $(this);
				valCtl.data("column").avg = valCtl.attr("checked");
			});
			avgCtl.append(avgCtl.data("valueControl"));
			condition.append(avgCtl);
		}

		if (column.allowCount) {
			var countCtl = $("<span>${query.statistisc.count}</span>");
			countCtl.data("valueControl", $("<input type=\"checkbox\" />"));
			countCtl.data("valueControl").data("column", column);
			countCtl.data("valueControl").attr("checked", column.count);
			countCtl.data("valueControl").click(function() {
				var valCtl = $(this);
				valCtl.data("column").count = valCtl.attr("checked");
			});
			countCtl.append(countCtl.data("valueControl"));
			condition.append(countCtl);
		}

		if (column.allowMin) {
			var minCtl = $("<span>${query.statistisc.min}</span>");
			minCtl.data("valueControl", $("<input type=\"checkbox\" />"));
			minCtl.data("valueControl").data("column", column);
			minCtl.data("valueControl").attr("checked", column.min);
			minCtl.data("valueControl").click(function() {
				var valCtl = $(this);
				valCtl.data("column").min = valCtl.attr("checked");
			});
			minCtl.append(minCtl.data("valueControl"));
			condition.append(minCtl);
		}

		if (column.allowMin) {
			var maxCtl = $("<span>${query.statistisc.max}</span>");
			maxCtl.data("valueControl", $("<input type=\"checkbox\" />"));
			maxCtl.data("valueControl").data("column", column);
			maxCtl.data("valueControl").attr("checked", column.max);
			maxCtl.data("valueControl").click(function() {
				var valCtl = $(this);
				valCtl.data("column").max = valCtl.attr("checked");
			});
			maxCtl.append(maxCtl.data("valueControl"));
			condition.append(maxCtl);
		}

		if (column.allowSum) {
			var sumCtl = $("<span>${query.statistisc.sum}</span>");
			sumCtl.data("valueControl", $("<input type=\"checkbox\" />"));
			sumCtl.data("valueControl").data("column", column);
			sumCtl.data("valueControl").attr("checked", column.sum);
			sumCtl.data("valueControl").click(function() {
				var valCtl = $(this);
				valCtl.data("column").sum = valCtl.attr("checked");
			});
			sumCtl.append(sumCtl.data("valueControl"));
			condition.append(sumCtl);
		}

		conditionDiv.append(condition);
		conditionDiv.hide();
	}
	
	var queryBtn = $("<input/>");
	queryBtn.val("${query.ui.search}");
	queryBtn.attr("type", "button");
	queryBtn.button();
	searchDiv.append(queryBtn);

	gridContainer.append(queryControlPanel);
	gridContainer.data("searchElement", searchDiv);
	gridContainer.data("conditionElement", conditionDiv);

	queryBtn.click(function() {
		if($$.validate(gridContainer)) {
			$$.executeQuery(gridContainer);
		}
	})

	if (isSimpleQuery) {
		for ( var i = 0; i < operators.length; i++) {
			operators[i].hide();
		}
	}
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
PMSApplication.prototype.validate = function(grid) {
	var requireds = $(".required",grid);
	for(var i=0; i<requireds.length; i++) {
		var field = $(requireds[i]);
		var v = field.val();
		if($.isEmpty(v)) {
			var msg = $("<span><font color='red'>" + field.attr("errormsg") + "</font></span>");
			field.after(msg);
			field.focus();
			setTimeout(function(){
				msg.remove();},2000);
			return false;
		}
	}

	var numbers = $(".number",grid);
	for(var j=0; j<numbers.length; j++) {
		var field = $(numbers[j]);
		var v = Number(field.val());
		if(v.toString() == 'NaN') {
			var msg = $("<span><font color='red'>" + field.attr("errormsg") + "</font></span>");
			field.after(msg);
			field.focus();
			setTimeout(function(){
				msg.remove();},2000);
			return false;
		}
	}
	return true;
}

PMSApplication.prototype.buildOperatorElement = function() {
	var operatorElement = $("<SELECT>" + "<option value=\"1\">like</option>"
			+ "<option value=\"2\" selected>=</option>"
			+ "<option value=\"3\">!=</option>"
			+ "<option value=\"4\">&gt;</option>"
			+ "<option value=\"5\">&lt;</option>"
			+ "<option value=\"6\">&gt;=</option>"
			+ "<option value=\"7\">&lt;=</option>"
			+ "<option value=\"12\">between</option>"
			+ "</SELECT");
	return operatorElement;
}

PMSApplication.prototype.buildSimpleOperatorElement = function() {
	var operatorElement = $("<SELECT>" + "<option value=\"1\">like</option>"
			+ "<option value=\"2\" selected>=</option>"
			+ "<option value=\"3\">!=</option>"
			+ "</SELECT");
	return operatorElement;
}

PMSApplication.prototype.buildConjuctionElement = function() {
	var operatorElement = $("<SELECT>"
			+ "<option value=\"10\" selected>${query.ui.and}</option>"
			+ "<option value=\"11\">${query.ui.or}</option></SELECT");
	return operatorElement;
}

PMSApplication.prototype.buildGridContainer = function(gridContainer) {
	var table = $("<table></table>");
	var thead = $("<thead></thead>");
	var tr = $("<tr></tr>");
	thead.append(tr);
	table.append(thead);
	table.toggleClass("display");
	table.id = gridContainer.id + "_datatable";
	var columns = gridContainer.data("columns");
	
	for ( var i = 0; i < columns.length; i++) {
		var column = columns[i];
		if(!column.visible) {
			continue;
		}
		var th = $("<th></th>");
		th.html(column.header);
		th.css("width", column.width);
		th.css("textAlign", column.align);
		tr.append(th);
	}
	table.data("grid", gridContainer);
	table.grid = gridContainer;
	gridContainer.append(table);
	gridContainer.data("dataTable", table);
	$$.executeQuery(gridContainer);
}

PMSApplication.prototype.fetchGridMetadata = function(grid) {
	// TODO: Query from Server with Data Grid's metadatas.
	var queryId = grid.attr("queryid");
	grid.id = grid.attr("id");
	var reqObj = new PMSRequestObject(this.queryManagerName,
			this.queryMetadataMethod, [ queryId ]);
	this.serverInvoke(reqObj, function(data, textStatus, XMLHttpRequest) {
		var cols = eval(data.data);
		grid.data("columns", cols);
		$$.buildGridSearchContainer(grid);
		$$.buildGridContainer(grid);
	});

}

PMSApplication.prototype.executeQuery = function(grid) {
	// Binding Datagrid.
	var queryReqObj = {
		columns: [],
		pageinfo : {
			currentPage : 1,
			recordsPerPage: 10
		},
		queryId : grid.attr("queryid")
	};	
	var columns = grid.data("columns");
	var aoColumns = [];
	// Set Query info.
	for(var i = 0; i < columns.length; i++) {
		 var col = columns[i];
		 // Set query info.
		 var value = null;
		 var operator = 2;
		 var conjunction = 10;
		 
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
		 
		 if (col.allowSearch) {
			 operator = $(document.getElementById(grid.id + "_operator_" + col.key)).val();
			 if(operator == 12) {
				 var value1 = $(document.getElementById(grid.id + "_input_" + col.key)).val();
				 var value2 = $(document.getElementById(grid.id + "_input_" + col.key + "2")).val();
				 if(value1.length > 0 && value2.length > 0)
				 	value = encodeURIComponent(value1+"___"+value2);
			 } else {
				 value = encodeURIComponent($(document.getElementById(grid.id + "_input_" + col.key)).val());
			 }
			 conjunction = $(document.getElementById(grid.id + "_conjunction_" + col.key)).val();
			 if (conjunction == null) {
				 conjunction = 10;
			 }
		 } else {
		 	value = col.value;
		 	operator = col.operator;
		 	conjunction = col.conjunction;
		 }
		 
		 queryReqObj.columns[i] = {
			key: col.key,
			value: value,
			header: encodeURIComponent(col.header),
			operator: operator,
			conjunction: conjunction,
			min: col.min,
			max: col.max,
			avg: col.avg,
			count: col.count,
			sum: col.sum,
			display: tmp
		 }
	 }
	var reqObj = new PMSRequestObject(this.queryManagerName,
			this.queryMethod, [ $.toJSON(queryReqObj) ]);
	var table = grid.data("dataTable");
	var bShowDetail = grid.attr("showdetail") == "true" ? true : false;
	var bShowCheckbox = grid.attr("showcheckbox") == "true" ? true : false;
	
	var editable = grid.attr("editable") == "true" ? true : false;
	var deleteable = grid.attr("deleteable") == "true" ? true : false;
	var bShowOperate = false;
	
	var opArrs = eval(grid.attr("operators"));
	bShowOperate = (opArrs && opArrs.length) > 0 ? true : false;

	var addFunc = eval(grid.attr("addFunc"));
	var bShowAdd = (typeof addFunc) == 'function' ? true : false

	var queryid = grid.attr("queryid"); 
	var td = table.dataTable({
		"bProcessing" : true,
		"bDestroy" : true,
		"bServerSide" : true,
		"bAutoWidth" : false,
		"sDom" : '<"H"hr>tsq<"F"ipl>',
		"bLengthChange": true,
		"bInfo" : true,
		"queryid" : queryid,
		"aoColumns" : aoColumns,
		"allColumns" : columns,
		"bShowCheckbox" : bShowCheckbox,
		"bShowDetail" : bShowDetail,
		"bShowOperate" : bShowOperate,
		"operators" : opArrs,
		"bShowAdd" : bShowAdd,
		"addFunc" : addFunc,
		"bPaginate": true,
		"sPaginationType": "full_numbers",
		"sPostData": "requestString=" + reqObj.toJsonString(),
		"sAjaxSource" : $$.PMSDispatchActionUrl
	});
	grid.data("jquery.datatable", td);
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
PMSApplication.prototype.preparePrint = function(printContainer, printPageUrl) {
	this.printing.printObject = $("<object id=\"printObject\" style=\"display:none\" classid=\"clsid:1663ed61-23eb-11d2-b92f-008048fdd814\" codebase=\"smsx.cab#Version=6,5,439,72\"></object>");
	this.printing.printUrl = printPageUrl;
}

/**
 * Initialize PMS Web UI.
 * 
 * @author Zhou zaiqing
 * @since 2010/11/21
 */
PMSApplication.prototype.initUISet = function() {
	// Initialize Dialog Container.
	if (this.dialogContainer == null) {
		this.dialogContainer = $("<div></div>");
	}
}
/**
 * Model Window for PMS UI
 * 
 * @author Zhou zaiqing
 * @since 2010/11/21
 */
PMSApplication.prototype.showMessage = function(title, message, callback) {
	if (this.dialog == null)
		this.dialog = new PMSDialog(title, message);
	this.dialog.show(title, message, callback);
}

PMSApplication.prototype.store = function(key, value) {
	var _STOREKEY_ = "_pms_root_data_";
	var topWin = window.top;
	var currWin = window;
	// Restore data from cookie;
	if ($.cookie(_STOREKEY_) != null) {
		topWin[_STOREKEY_] = jQuery.parseJSON($.cookie(_STOREKEY_));
	}
	if (topWin == currWin) {
		this.showMessage("PMS Error", "Store data error, no top window has been found.");
		return null;
	}
	var dataMap = topWin[_STOREKEY_];
	if (!jQuery.isArray(dataMap)) {
		dataMap = [];
	}
	var resultValue = null;
	
	for (var i = 0; i < dataMap.length; i++) {
		var innerObj = dataMap[i];
		if (jQuery.isPlainObject(innerObj)) {
			if (innerObj.key !== undefined && innerObj.key == key) {
				if (value !== undefined) {
					dataMap[i] = {
						key: key,
						val: value
					};
					resultValue = value;
				} else {
					return innerObj.val;
				}
			}
		}
	}
	if (resultValue == null) {
		dataMap.push({
			key: key,
			val: resultValue = value
		})
	}
	
	topWin[_STOREKEY_] = dataMap;
	$.cookie(_STOREKEY_, $.toJSON(topWin[_STOREKEY_]));
	return resultValue;
}

// Create a cookie with the specified name and value.
// The cookie expires at the end of the 20th century.
function SetCookie(sName, sValue) {
	var date = new Date();
	document.cookie = sName + "=" + escape(sValue) + "; expires="
			+ date.toGMTString();
}

// Retrieve the value of the cookie with the specified name.
function GetCookie(sName) {
	// cookies are separated by semicolons
	var aCookie = document.cookie.split("; ");
	for ( var i = 0; i < aCookie.length; i++) {
		// a name/value pair (a crumb) is separated by an equal sign
		var aCrumb = aCookie[i].split("=");
		if (sName == aCrumb[0])
			return unescape(aCrumb[1]);
	}
	// a cookie with the requested name does not exist
	return null;
}

/**
 * Request Object is the data model used to communicate with server side.
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/21
 */
var PMSRequestObject = function(managerName, methodName, parameters, token) {
	this.managerName = managerName;
	this.methodName = methodName;
	this.parameters = parameters;
	this.token = token;
};

/**
 * Add object parameter to invoke Server side service.
 * 
 * @param parameter
 * @author Zhou zaiqing
 * @since 2010/11/21
 */
PMSRequestObject.prototype.addParameter = function(parameter) {
	if (this.parameters == null || typeof (this.parameters) != "Array")
		this.parameters = [];
	this.parameters.push(parameter);
};

/**
 * Serial Request Object to Json String.
 * 
 * @author Zhou zaiqing
 * @since 2010/11/22
 */
PMSRequestObject.prototype.toJsonString = function() {
	return $.toJSON({
		managerName : this.managerName,
		methodName : this.methodName,
		parameters : this.parameters,
		token : this.token
	});
};

/**
 * PMS Dialog Class.
 * 
 * @param title
 * @param content
 * @returns PMS Dialog Instance.
 * @author Zhou zaiqing
 * @since 2010/11/21
 */
var PMSDialog = function(title, content) {
	this.title = title;
	this.content = content;
	this.initialize();
}
PMSDialog.PMSDialogId = "_PMSDIALOG_";

PMSDialog.prototype.setTitle = function(title) {
	this.title = title;
	this.uiElement.attr("title", this.title);
}

PMSDialog.prototype.setContent = function(content) {
	this.content = content;
	this.dialogContentElement
			.html("<span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 50px 0;\"></span>"
					+ content);
}

PMSDialog.prototype.initialize = function() {
	this.uiElement = $("<div></div>").attr("id", PMSDialog.PMSDialogId).hide();
	this.dialogContentElement = $("<p></p>");
	this.uiElement.append(this.dialogContentElement);
	if (this.title != null) {
		this.setTitle(this.title);
	}
	if (this.content != null) {
		this.setContent(this.content);
	}
	document.body.appendChild(this.uiElement[0]);
}

PMSDialog.prototype.show = function(title, content, callback) {
	if (title != null && this.title != title)
		this.setTitle(title);
	if (content != null && this.content != content)
		this.setContent(content);
	this.uiElement.dialog("destroy");
	this.uiElement.dialog({
		modal : true,
		resizable : false,
		title : this.title,
		buttons : {
			Ok : function() {
				$(this).dialog("close");
				if(callback && typeof callback == 'function') {
					callback();
				}
				$(this).dialog("destroy");
			}
		}
	});

}

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

PMSOperator.prototype.AND = function() {
	return new PMSOperator("AND", 10);
}

PMSOperator.prototype.OR = function() {
	return new PMSOperator("OR", 11);
}

PMSOperator.prototype.BETWEEN = function() {
	return new PMSOperator("BETWEEN", 12);
}

jQuery.extend({
	/**
	 * Extend jQuery to serialize a object to json string.
	 * 
	 * @param Object,
	 *            support type:
	 *            object,array,string,function,number,boolean,regexp *
	 * @return json string
	 */
	toJSON : function(object) {
		if (!object)
			return null;
		var type = typeof object;
		if ('object' == type) {
			if (Array == object.constructor)
				type = 'array';
			else if (RegExp == object.constructor)
				type = 'regexp';
			else
				type = 'object';
		}
		switch (type) {
		case 'undefined':
		case 'unknown':
			return;
			break;
		case 'function':
		case 'boolean':
		case 'regexp':
			return object.toString();
			break;
		case 'number':
			return isFinite(object) ? object.toString() : 'null';
			break;
		case 'string':
			return '"'
					+ object.replace(/(\\|\")/g, "\\$1").replace(
							/\n|\r|\t/g,
							function() {
								var a = arguments[0];
								return (a == '\n') ? '\\n'
										: (a == '\r') ? '\\r'
												: (a == '\t') ? '\\t' : ""
							}) + '"';
			break;
		case 'object':
			if (object === null)
				return 'null';
			var results = [];
			for ( var property in object) {
				var value = jQuery.toJSON(object[property]);
				if (value !== undefined)
					results.push(jQuery.toJSON(property) + ':' + value);
			}
			return '{' + results.join(',') + '}';
			break;
		case 'array':
			var results = [];
			for ( var i = 0; i < object.length; i++) {
				var value = jQuery.toJSON(object[i]);
				if (value !== undefined)
					results.push(value);
			}
			return '[' + results.join(',') + ']';
			break;
		}
	}
});

jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};

/**
 * 缓存数据字典在客户端($DICT_MAP)
 */
PMSApplication.prototype.getDictMap = function() {
	var reqObj = new PMSRequestObject("dictManager", "cacheAllDicts", [ ]);
	$$.ajax($$.PMSDispatchActionUrl, "requestString="+reqObj.toJsonString(), 
		function(data, textStatus, XMLHttpRequest) {
			var map = eval("("+data.data+")");
			$DICT_MAP.clear();
			for(var attr in map) {
				$DICT_MAP.put(attr, map[attr]);
			}
	});
}



/*!
 * CNPC PMS Web Application JavaScript Library of Form.
 * http://www.cnpc.com.cn/
 * Author:Hewang
 * 
 * usage:
 * ----------------------------------------
 * 第一种方式：(dialog/非dialog)
 * html中声明如下：
 * <script>
 * 		function doSuccess(data) {
 * 			alert("success");
 * 		}
 * </script>
 * <form
 * 		id="exampleForm"
 * 		class="pmsForm" //必须
 * 		dialog="true" //可选，如不为true，则不弹出框
 * 		display="false" //显示与否，默认(不配置)为true
 * 		modal="true" //是否模态，默认为true
 * 		ajax="false" //是否ajax提交，默认(不配置)为true
 * 		pmsTitle="添加供应商信息"
 *		action="dispatcher.action" //form原有配置项
 *		method="post" //form中原有的配置项
 *		queryId="supplierQuery" //queryId
 *		pmsmanager="baseManager"  //在Spring中注册的manager name
 *		pmsmethod="saveObj"  //manager中对应的方法
 *		callback="doSuccess" //表单提交成功后的回调函数
 *		width="500px" //弹出框宽度
 * >  
 * 	...  (表单域)
 * </form>
 * 如未配置dialog，可以通过调用 $form.showDialog($("#exampleForm"));来显示弹出框。
 * 如配置dialog=true,display=false，则可调用 $form.show($("#exampleForm"));来显示弹出框。
 * 
 * -------------------------------------------
 * 第二种方式：(only dialog)
 * html中声明如下：
 * <form
 * 		id="exampleForm2"
 * 		class="pmsForm" //必须
 * 		display="false" //显示与否，默认(不配置)为true
 * 		ajax="false" //是否ajax提交，默认(不配置)为true
 *		action="dispatcher.action" //form原有配置项
 *		method="post" //form中原有的配置项
 *		queryId="supplierQuery" //queryId
 *		pmsmanager="baseManager"  //在Spring中注册的manager name
 *		pmsmethod="saveObj"  //manager中对应的方法
 * >  
 * ...  (表单域)
 * </form>
 * 可通过
 * 	$form.dialog($("#exampleForm2"), {
 *					width:'500px',
 *					title:'添加供应商信息',
 *					reset:true,//重置表单,默认为false
 *					callback:function(data){
 *						//$$.showMessage('提示','添加成功!');
 *						$$.showMessage('提示','添加成功!', function(data){//do some thing..});
 *					},
 *					modal:true
 *				});
 * ----------------------------------------
 */

/**
 * 表单类
 * 只要标注了class="pmsForm"的表单均可被此类截获.
 */
var PMSForm = function() {
	this.PMSDispatchActionUrl = "/dispatcher.action";
	this.formElement = "form[class='pmsForm']";
	this.selectElement = "input[class='pmsSelect']";
	this.radioElement = "input[class='pmsRadio']";
	this.checkboxElement = "input[class='pmsCheckbox']";
	this.dateElement = "input[class='pmsDateField']";
}

/**
 * 初始化所有表单
 */
PMSForm.prototype.initForm = function() {
	//初始化表单元素
	$form.initFormStyle();
	
	//初始化表单
	var myform = $(this.formElement);
	myform.each(function(i) {
		var display = $(this).attr("display");
		var isDialog = $(this).attr("dialog");
		var show = (display && display=='false') ? false : true;
		$(this).css('display', show ? 'block' : 'none');
		if(show && isDialog && isDialog=='true') {
			$form.showDialog(this);
		} else {
			$(this).find(":input[type='submit']").click(function() {
				$form.submit($(myform[i]));
				return false;
			});
		}
	});
}
/**
 * 将配置有dialog="true"并且display=false的表单显示
 * @param {Object} form
 */
PMSForm.prototype.show = function(form) {
	var isDialog = $(form).attr("dialog");
	if(isDialog && isDialog=='true') {
		$form.showDialog(form);
	} else {
		$(form).css('display','block');
	}
}
/**
 * 将配置有dialog="false"的表单以弹出框的形式呈现
 * @param {Object} form
 */
PMSForm.prototype.showDialog = function(form) {
	var width = $(form).attr("width");
	var pmsTitle = $(form).attr("pmsTitle");
	var callback = $(form).attr("callback");
	var modal = $(form).attr("modal");
	if( ! width) {
		width = $(form).css("width");
	}
	$form.dialog(form, {
		width:width,
		title:pmsTitle,
		callback:callback,
		modal:modal
	});
}
/**
 * 将表单转换成弹出框的形式呈现
 * @param {Object} form
 */
PMSForm.prototype.dialog = function(form, options) {
	var defaults = {
		title:'',
		width:'480px',
		callback:function(data){
			$$.showMessage('${query.ui.prompt}','${query.ui.success}', function(data){
				$(form).dialog("close");
			});
		},
		reset:false,
		buttons:{
			"${query.ui.ok}":function(){
				$form.submit(form, function(data) {
					$(form).dialog("close");
					try{
						options.callback(data);
					}catch(e) {
						eval(options.callback+'(data)');
					}
				});
			},
			"${query.ui.cancel}":function(){
				$(form)[0].reset();
				$(form).dialog("close");
			}
		},
		modal:true
	};
	var options = $.extend(defaults, options);
	
	if(options.reset) {
		$(form)[0].reset();
	}
	$(form).dialog({
		width:options.width,
		title:options.title,
		buttons : options.buttons,
		modal:options.modal
	});
}

/**
 * 重置表单
 * @param {Object} formId
 */
PMSForm.prototype.reset = function(formId) {
	var form = $("#" + formId);
	form[0].reset();
}

/**
 * 提交表单 
 * @param {Object} form
 */
PMSForm.prototype.submit = function(form, callback) {
	var ajax = $(form).attr("ajax");
	if(ajax && ajax == 'false') {
		$(form)[0].submit();
	} else {
		$form.ajaxSubmit(form, callback);
	}
}

/**
 * ajax 提交表单
 * @param {Object} form
 */
PMSForm.prototype.ajaxSubmit = function(form, callback) {
	var url = $(form).attr("action");
	var managerName = $(form).attr("pmsmanager");
	var methodName = $(form).attr("pmsmethod");
	var queryId = $(form).attr("queryId");
	var inputs = $(form).find(":input[name]");
	var params = "{";
	for(var i=0; i<inputs.length; i++) {
		var input = $(inputs[i]);
		var ty = input.attr('type');
		var name = input.attr("name");
		if(ty == 'checkbox' || ty == 'radio') {
			continue;	
		}
		var val = input.val();
		params += '"' + name + '":"';
		params += val + '",';
	}
	var chkInputs = $(form).find(" :input[name][checked]");
	var chkMap = new Map();
	chkInputs.each(function(i) {
		var name = $(this).attr("name");
		var val = $(this).val();
		if(chkMap.indexOf(name) != -1) {
			chkMap.setValue(name, chkMap.get(name) + ',' + val);			
		} else {
			chkMap.put(name, val);
		}
	});
	for(var i=0; i<chkMap.size(); i++) {
		var entry = chkMap.getEntry(i);
		params += '"' + entry.key + '":"';
		params += entry.value + '",';
	}
	params = params.substr(0, params.length-1) + "}";
	var saveObj = {
		queryId:queryId,
		presaveJsonData:params
	}
	var reqObj = new PMSRequestObject(managerName, methodName,
		 [ queryId, params ]);
	$$.ajax(url,
		"requestString=" + reqObj.toJsonString(), 
		function(data, textStatus, XMLHttpRequest){
			if(data.result) {
				callback(data,textStatus,XMLHttpRequest);
			} else {
				$$.showMessage('${query.ui.prompt}','${query.ui.failure}');
			}
		}
	);
}

/*!
 * Map:
 * 	方法：
 * 	1.put(key,value)
 * 	2.get(key) --> value
 * 	3.size() -->the size of map
 *  4.remove(key)
 *  5.setValue(key, value)
 *  6.getEntry(index) -->{key:xx,value:xx}
 *  7.indexOf(key) -->the index of key
 *  8.isEmpty() --> true/false.
 *  9.clear()
 */
var Map = function(){
    var Entry = function(key, value){
        this.key = key;
        this.value = value;
    };
	this.entries = new Array();
    this.put = function(key, value){
        for (var i = 0; i < this.entries.length; i++) {
            if (this.entries[i].key === key) {
                return false;
            }
        }
        this.entries.push(new Entry(key, value));
		return true;
    };
    this.get = function(key){
        for (var i = 0; i < this.entries.length; i++) {
            if (this.entries[i].key === key) {
                return this.entries[i].value;
            }
        }
        return null;
    };
	this.remove = function(key) {
		var index = this.indexOf(key);
		if(index != -1) {
			this.entries.splice(index,1);
		}
	};
	this.setValue = function(key, value) {
		var index = this.indexOf(key);
		if(index != -1) {
			this.entries[index].value = value;
		}
	};
	this.getEntry = function(index) {
		if(index >= 0 && index < this.size()) {
			return this.entries[index];
		}
		return null;
	};
    this.size = function(){
        return this.entries.length;
    };
    this.isEmpty = function(){
        return this.entries.length <= 0;
    };
	this.clear = function() {
		this.entries = [];
	};
	this.indexOf = function(key) {
		var index = -1;
		for(var i=0; i<this.size(); i++) {
			if(key == this.entries[i].key) {
				index = i;
				break;
			}
		}
		return index;
	};
	this.toString = function() {
		var str = '[';
		for(var i=0; i<this.size(); i++) {
			str += (this.entries[i].key+'='+this.entries[i].value+',')
		}
		str = str.substr(0, str.length-1);
		str += ']';
		return str;
	};
}

/**
 * 初始化所有表单中的元素（包括select,checkbox,radio）
 * 只要是:input[class='pmsSelect/pmsRadio/pmsCheckbox']，均会被该方法初始化
 */
PMSForm.prototype.initFormStyle = function() {
	var form = $("form");
	form.each(function(index) {
		$form.initSelectElement(form);
		$form.initCheckboxElement(form);
		$form.initRadioElement(form);
		$form.initDateElement(form);
	});
}
/**
 * 初始化select元素
 * @param {Object} form
 */
PMSForm.prototype.initSelectElement = function(form) {
	var elements = $(form).find(":" + this.selectElement);
//	alert("select=" + elements.length);
	$(elements).each(function(i) {
		var data = $(this).attr("data");
		data = eval(data);
		if(data) {
			for(var i=0; i<data.length; i++) {
				var option = $("<option value='" + data[i][0] + "'>" + data[i][1] + "</option>");
				if(data[i][2] && data[i][2]=='selected') {
					option.attr("selected","selected");
				}
				$(this).append(option);
			}
		} else {
			$(this).replaceWith("<select/>");
		}
	});
}
/**
 * 初始化radio元素
 * @param {Object} form
 */
PMSForm.prototype.initRadioElement = function(form) {
	var elements = $(form).find(":" + this.radioElement);
	$(elements).each(function(i) {
		var data = $(this).attr("data");
		var eName = $(this).attr("name");
		data = eval(data);
		if(data) {
			for(var i=0; i<data.length; i++) {
				var adata = data[i];
				var radio;
				if(eName && eName != '') {
					radio = $("<input type='radio' name='" + eName + "' value='"+adata[0]+"'>" + adata[1] + "</input>");
					if(adata[2] && adata[2] == 'checked') {
						radio.attr("checked",true);
					}
				}
				$(this).before(radio);
			}
			$(this).remove();
		} else {
			$(this).replaceWith("<input type='radio'");
		}
	});
}
/**
 * 初始化checkbox元素
 * @param {Object} form
 */
PMSForm.prototype.initCheckboxElement = function(form) {
	var elements = $(form).find(":" + this.checkboxElement);
	$(elements).each(function(i) {
		var data = $(this).attr("data");
		data = eval(data);
		if(data) {
			for(var i=0; i<data.length; i++) {
				var adata = data[i];
				var checkbox = $("<input type='checkbox' name='" + adata[0] + "' value='"+adata[1]+"'>" + adata[2] + "</input>");
				if(adata[3] && adata[3] == 'checked') {
					checkbox.attr("checked","checked");
				}
				$(this).before(checkbox);
			}
			$(this).remove();
		} else {
			$(this).replaceWith("<input type='checkbox'");
		}
	});
}
/**
 * 初始化date选择器元素
 * @param {Object} form
 */
PMSForm.prototype.initDateElement = function(form) {
	var elements = $(form).find(":" + this.dateElement);
	$(elements).each(function(i) {
		$(this).datepicker({
			dateFormat:"yy-mm-dd",
			changeMonth: true,
			changeYear: true,
			yearRange:'-100:+100',
			onSelect: function(dateText, inst) {
					$("#valid-date-label3").css('display',"none");
			}
		});
	});
}

/**
 * load data for form
 * @param {Object} formId
 * @param {Object} url (default "dispatcher.action")
 * @param {Object} reqObj
 */
PMSForm.prototype.loadFormData = function(formId, url, param){
	$$.ajax(url, param, function(data, textStatus, XMLHttpRequest){
		if(data.result) {
			$form.loadData(formId, eval('('+data.data+')'));
		}
	});
}
/**
 * put values into form fields.
 * @param {Object} formId
 * @param {Object} obj
 */
PMSForm.prototype.loadData = function(formId, obj) {
	var tmpForm = $("#"+formId);
	tmpForm[0].reset();
	for (var attr in obj) {
		if ((!obj[attr]) || typeof(obj[attr]) == 'function') {
			continue;
		}
		var ipt = tmpForm.find(" :input[name='" + attr + "']");
		if( ipt.length == 0) {
			tmpForm.append($('<input type="hidden" name="' + attr + '" value="' + obj[attr] + '" />'));
		} else {
			var type = ipt.attr("type");
			if (type == "checkbox" || type == "radio") {
				var vs = obj[attr].split(",");
				for (var v = 0; v < vs.length; v++) {
					ipt.each(function(i, n){
						var value = $(n).val();
						if (value == vs[v]) {
							$(n).attr("checked", true);
						}
					});
				}
			} else {
				ipt.val(obj[attr]);
			}
		}
	}
	$form.show($("#"+formId)[0]);
}

var $DICT_MAP = new Map();

// PMSApplication global instance.
var $$ = pmsApp = new PMSApplication();
var $form = pmsform = new PMSForm();

/**
 * Invoke jQuery to load PMSApplication instance.
 * 
 * @author Zhou Zaiqing
 * @since 2010/11/21
 */
$(document).ready(function() {
	$$.initialize();
	$$.getDictMap();
	$$.dataBinding();
	$form.initForm();
});