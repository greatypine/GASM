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
	this.requestTimeout = 600000; // unit: milliseconds.
	this.defaultSearchDivPrefix = "PMS_SEARCH_CONDITION_DIV_";
	this.gridContainerTag = "div[queryid]";
	this.dialog = null;
	this.queryManagerName = "queryManager";
	this.customQueryManager = "queryConfigManager";
	//lushu 2013.11.6: change the API to remove cache args
	//change the invoke in fetchGridMetadata, search 2013.11.6
	this.queryMetadataMethod = "getMetaInfo"; //"getMetadata";
	this.queryMethod = "executeQuery";
	this.queryCongifMethod = "saveConfig";
	this.queryByIdMethod = "executeQueryById";
	this.queryStatisticsMethod = "executeStatistics";
	this.printing = {
		printContainer : null,
		printUrl : null
	};
	this.baseURL = "";
	this.pageURL = "";
	this.rootPath = "";
	this.PMSDispatchActionUrl = "/dispatcher.action";
	this.PMSReportActionUrl = "/report.do";
};

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
	this.pageURL = window.document.location.pathname; // "/projectname/dirname/file.html"
	this.baseURL = this.pageURL.substr(0, this.pageURL.indexOf("/", 1)); // "/projectname"
	var url = window.document.location.href; // "http://localhost:8088/projectname/dirname/file.html"
	this.rootPath = url.substring(0, url.indexOf(this.pageURL)) + this.baseURL;// "http://localhost:8088/projectname"
	this.PMSDispatchActionUrl = this.baseURL + "/dispatcher.action";
	this.PMSReportActionUrl = this.baseURL + "/report.do";
};
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
		$$.loadCache(gridContainer);
		$$.fetchGridMetadata(gridContainer);
	});
}

/**
 * The handler of server sider success invocation.
 * 
 * @author Zhou zaiqing
 * @since 2010/11/22
 */
PMSApplication.prototype.onServerInvokeSuccess = function(data, textStatus, XMLHttpRequest) {
	this.showMessage("Fetch OK", $.toJSON(data));
};

/**
 * The handler of server sider fail invocation.
 * 
 * @author Zhou zaiqing
 * @since 2010/11/22
 */
PMSApplication.prototype.onServerInvokeFailed = function(XMLHttpRequest, textStatus, errorThrown) {
	//错误回调函数由具体业务实现，暂未用到abort
	//"timeout", "error", "abort", and "parsererror"."notmodified", "success"
	var msg = textStatus;
	if (textStatus == 'parsererror') {
		msg = '${login.timeout}';
	}
	try{
		this.showMessage("${client.httperrortitle}", "Reason: " + msg);
	}catch(err){
		msg="error";
		window.location.href='https://loginjs.guoanshuju.com/login/login?service=https%3A%2F%2Fstorejs.guoanshuju.com%2FGASM';
		//window.location.href="https://login.guoanshuju.com/login/login?service=http%3A%2F%2F10.16.21.60/GASM";
	}
};

/**
 * Server side services invocation interface with callback function.
 * Remove the third parameter context by lushu 2013/10/31
 * @author Zhou zaiqing
 * @since 2010/11/22
 */
PMSApplication.prototype.serverInvoke = function(reqObj, callback) {
	var sendString = "requestString=" + reqObj.toJsonString();
	//	var sendCtx = document.body;
	//	if (context != null)
	//		sendCtx = context;
	if (callback == null) {
		callback = this.onServerInvokeSuccess;
	}
	this.ajaxSync(this.PMSDispatchActionUrl, sendString, callback);
};

PMSApplication.prototype._ajax = function(url, data, callback, failureCallback, async) {
	$.ajax({
		url : url,
		dataType : "json",
		async: async,
		type : "post",
		data : data,
		timeout : $$.requestTimeout,
		success : function(data, textStatus, XMLHttpRequest) {
			if (data.result) {
				callback(data, textStatus, XMLHttpRequest);
			} else {
				if (data.message == 'permission_denied') {
					//$$.showMessage("Permission Denied","${base.permissionDenied}");
					window.location = data.data;
				} else if (failureCallback) {
					failureCallback(data, textStatus, XMLHttpRequest);
				} else {
					$$.onServerInvokeFailed(XMLHttpRequest, data.message);
				}
			}
		},
		error : $$.onServerInvokeFailed
	});
};
PMSApplication.prototype.ajax = function(url, data, callback, failureCallback) {
	this._ajax(url, data, callback, failureCallback, true);
};
PMSApplication.prototype.ajaxSync = function(url, data, callback, failureCallback) {
	this._ajax(url, data, callback, failureCallback, false);
};

//cache key of query conditions
PMSApplication.prototype._cacheKey = function(queryId){
	var key = queryId+"@"+this.pageURL;
	return key;
};
PMSApplication.prototype.loadCache = function(grid){
	var queryId = grid.attr("queryid");
	var key = this._cacheKey(queryId);
	var request = new PMSRequestObject(this.queryManagerName, "getCachedCondition", [key]); 
	this.serverInvoke(request, function(data, textStatus, XMLHttpRequest){
		grid.data("cachedConditions", data);
	});
};
PMSApplication.prototype.adoptCache = function(grid){
	var data = grid.data("cachedConditions");
	if(data!=null){
		data.data = data.data.replace(/\%/g, "");
		var conditions = eval("("+data.data+")");
		if(conditions!=null){
			if(typeof setupFormWithCache == "function"){
				setupFormWithCache(conditions.conditions);
			}
			var searchDiv = grid.attr('searchDiv') ? grid.attr('searchDiv') : ("PMS_SEARCH_CONDITION_DIV_" + queryId);
			this._setCache(searchDiv, conditions.conditions);
		}
		grid.removeData("cachedConditions");
	}
};
PMSApplication.prototype._setCache = function(searchDiv, conditions) {
	var container = $("#"+searchDiv);
	for (var x in conditions) {
		if ((!conditions[x]) || typeof(conditions[x]) == 'function') {
			continue;
		}
		var column = conditions[x];
		var value = column.value;
		if (null == value || 'undifined' == value) {
			continue;
		}
		var name = column.key;
		var inputs = container.find(" :input[name='" + name + "']");
		if( inputs.length > 0 ) {
			var type = inputs.attr("type");
			if (type == "checkbox" || type == "radio") {
				if(typeof value != 'string'){
					value = value + '';
				}
				var vs = value.split(",");
				for (var v = 0; v < vs.length; v++) {
					for(var h=0; h<inputs.length; h++){
						if ($(inputs[h]).val() == vs[v]) {
							$(inputs[h]).attr("checked", true);
						}
					}
				}
			} else if(value.indexOf(',') >= 0 && inputs.length ==2) {
				var betV = value;
				if(betV.indexOf(",") == 0) {
					$(inputs[1]).val(betV.substr(1));
				} else if(betV.indexOf(",") == (betV.length-1)) {
					$(inputs[0]).val(betV.substr(0, betV.length-1));
				} else {
					$(inputs[0]).val(betV.substr(0, betV.indexOf(",")));
					$(inputs[1]).val(betV.substr(betV.indexOf(",")+1));
				}
			} else {
				inputs.val(value);
			}
		}
	}
}


//在自定义查询列表后重新渲染表头，及更新数据
PMSApplication.prototype.reconfig = function(queryId) {
	var configContainerTag = "div[queryid='" + queryId + "']";
	$(configContainerTag).each(function(i) {
		var configContainer = $(this);
		configContainer.children().remove();
		$$.fetchGridMetadata(configContainer);
		var autoload = $(this).attr("autoload") == 'false' ? false : true;
		if (autoload === false) {
			$$.executeQuery($(this));
		}
	});
}

PMSApplication.prototype.buildGridSearchContainer = function(gridContainer) {
	var showsearch = gridContainer.attr("showsearch") == 'false' ? false : true;
	if (showsearch === true) {
		//2013.11.6 change from this.defaultSearchDivPrefix + gridContainer.attr("queryId");
		var searchDiv = gridContainer.attr("searchDiv");
		if ($("#"+searchDiv)[0] == undefined) {//prevent duplicate of creation search div
			var columns = gridContainer.data("columns");
			var titleTable = $("<table></table>");
			titleTable.addClass("Querytable");
			var tr = $("<tr></tr>");
			var td = $("<td></td>");
			tr.addClass("Colorbar1");
			td.attr('align', 'center');
			td.attr('width', '20');
			var a = $("<a href='#'></a>");
			var img = $("<img />");
			img.attr("src", this.rootPath + "/scripts/common/images/22.png");
			a.append(img);
			td.append(a);
			var td2 = $("<td></td>");
			td2.html("<strong>${query.search.searchtitle}</strong>");
			tr.append(td);
			tr.append(td2);
			titleTable.append(tr);
			var conditionDiv = $("<div></div>");
			conditionDiv.attr("id", searchDiv);			
			a.click(function() {
				conditionDiv.toggle();
				if (img.attr("src") == ( $$.rootPath+ "/scripts/common/images/22.png")) {
					img.attr("src",  $$.rootPath + "/scripts/common/images/33.png");
				} else {
					img.attr("src",  $$.rootPath + "/scripts/common/images/22.png");
				}
			});
			var conditionForm = $("<form></form>");
			conditionForm.addClass("pmsForm");
			var condTable = $("<table></table>");
			condTable.addClass("bigteble");
			
			var colarr = [];
			for (var i = 0; i < columns.length; i++) {
				var col = columns[i];
				if (col.allowSearch) {
					if(colarr.length > 0) {
						if (colarr[colarr.length-1].length > 1) {
							colarr.push([col]);
						} else {
							colarr[colarr.length-1].push(col);
						}
					} else {
						colarr.push([col]);
					}
				}
			}
			for (var i=0; i<colarr.length; i++) {
				var col1 = colarr[i][0];
				var col2 = null;
				if (colarr[i].length > 1) {
					col2 = colarr[i][1];
				}
				var tr = $('<tr></tr>');
				var td1 = $('<td></td>');
				var td2 = $('<td></td>');
				td1.addClass('Querytext');
				td2.addClass('Queryform');
				td1.html(col1.header + ": ");
				td2.html("<input type='text' name='" + col1.key + "' />")
				tr.append(td1);
				tr.append(td2);
				if (col2 != null) {
					var td3 = $('<td></td>');
					var td4 = $('<td></td>');
					td3.addClass('Querytext');
					td4.addClass('Queryform');
					td3.html(col2.header + ": ");
					td4.html("<input type='text' name='" + col2.key + "' />")
					tr.append(td3);
					tr.append(td4);
				}
				condTable.append(tr);
			}
			var lasttr = $('<tr></tr>');
			lasttr.addClass("Colorbar3");
			var ltd = $("<td colspan=4></td>");
			ltd.css('text-align', 'right');
			var queryBtn = $("<input/>");
			queryBtn.val("${query.ui.search}");
			queryBtn.attr("type", "button");
			queryBtn.button();
			queryBtn.click(function() {
				$$.executeQuery(gridContainer);
			})
			var resetbutton = $("<input type='button' value='${query.ui.reset}' />");
			resetbutton.button().click(function() {
				conditionForm[0].reset();
			});
			ltd.append(queryBtn);
			ltd.append(resetbutton);
			lasttr.append(ltd);
			condTable.append(lasttr); 
			
			conditionForm.append(condTable);
			conditionDiv.append(conditionForm);
			
			var bigdiv = $('<div></div>');
			bigdiv.append(titleTable);
			bigdiv.append(conditionDiv);
			bigdiv.css('padding', '5px');
			gridContainer.before(bigdiv);
		}
	}
}

PMSApplication.prototype.buildGridContainer = function(gridContainer) {
	var queryId = gridContainer.attr('queryid');
	if(gridContainer.attr('metadatamethod') == 'true'){
		queryId = gridContainer.attr('metaqueryid')
	};
	var columns = gridContainer.data('columns');
	var queryConfigId = gridContainer.data('queryConfigId');//2013-08-06 haochengjie
	var selectedColumns = gridContainer.data('selectedColumns');
	var unselectedColumns = gridContainer.data('unselectedColumns');
	var operatorShow = gridContainer.data('operatorShow');
	var queryDataId = gridContainer.data('queryDataId');
	if( operatorShow == undefined || operatorShow == null || operatorShow == ''){
		operatorShow = gridContainer.attr("operatorshow");
	}
	if( gridContainer.attr("showtitle") != 'false' ) {
		var titlediv = $("<div></div>");
		titlediv.addClass("panel-heading");
		var titleTable = $("<table></table>");
		titleTable.addClass("Querytable");
		titleTable.attr("id","Querytable_"+queryId);
		var tr = $("<tr></tr>");
		var td = $("<td></td>");
		//tr.addClass("Colorbar1");
		td.attr('align', 'center');
		td.attr('width', '20');
		var a = $("<a href='#'></a>");
		var img = $("<img />");
		img.attr("src", this.rootPath + "/scripts/common/images/22.png");
		a.append(img);
		td.append(a);
		a.click(function() {
			var tablediv = titleTable.parent().next("div[class*=dataTables_wrapper]");
			tablediv.toggle();
			if (img.attr("src") == (getRootPath() + "/scripts/common/images/22.png")) {
				img.attr("src", getRootPath() + "/scripts/common/images/33.png");
			} else {
				img.attr("src", getRootPath() + "/scripts/common/images/22.png");
			}
		});

		var td2 = $("<td></td>");
		td2.html("<strong>" + gridContainer.data("header") + "</strong>");
		tr.append(td);
		tr.append(td2);
		
		
		//haochengjie start
//		alert(operatorShow);
		//TODO: queryDataId在后台赋值为queryID，这里进行比对的目的是什么？
		if(operatorShow != undefined && operatorShow == "independent" ){// && queryDataId != null && queryDataId == queryId
			var td4 = $("<td></td>");
			td4.css("align","right");
			var operators = eval(gridContainer.attr("operators"));
			for (var i in operators) {
				var operator = operators[i];
				var thtml = operator.html;
				if (operator.renderer && typeof operator.renderer == 'function') {
					//thtml = op.renderer(aData, op.html);
				}
				var btn = $("<button></button>");
				btn.html(thtml);
				btn.attr("class","buttonu");
				btn.data("func", operator.func);
				//btn.click = op.func(oSettings.queryid, aData, btn.parentNode.parentNode, oSettings.allColumns);
				if (operator.func && typeof operator.func == 'function') {
					btn.click(function(){
						var aDatas = document.getElementsByName("checkboxid");
						var checkObjs = $("input[name='checkboxid']:checked");
						var checkObj = checkObjs.length;
						alert(checkObj);
						if(checkObj == 0){
							//alert("您还没有选择，请选择一条记录！");
							$$.showMessage("提示","您还没有选择，请选择一条记录！",null);
							return false;
						}else if(checkObj == 1){
							var aData = "";
						    var nTr = null;
							for(var i=0;i<aDatas.length;i++){
								if(aDatas[i].checked){
									aData = $(aDatas[i]).attr("idvalue");
									nTr = aDatas[i].parentNode.parentNode;
								}
							}
							var funct = $(this).data("func");
							funct(queryId, aData.split(","), nTr, columns);
							return false;
						}else{
							//alert("只能选择一条记录！");
							$$.showMessage("提示","操作时只能选择一条记录！",null);
							return false;
						}
					});
				}
				td4.append(btn);
				td4.append($("<font>&nbsp;</font>"));
			}

			tr.append(td4);
		}
		//haochengjie end
				
		var td_but = $("<td></td>");
		td_but.attr("align","right");
		var addFunc = eval(gridContainer.attr("addFunc"));
		if ((typeof addFunc) == 'function') {
			var addBut = $("<input type='button' style='vertical-align:middle'/>");
			addBut.val("${query.ui.add}");
			td_but.append(addBut);
			addBut.button().click(function(){
				addFunc(columns, columns);
			});
		}
		tr.append(td_but);
		
		/*Add by dhx start0*/	
		if(gridContainer.attr('configbutton') != 'false'){
			var td_config = $("<td></td>");
			td_config.attr("align","right");
			td_config.append("&nbsp;");
			var a_config = $("<a href='#'></a>");
			var img_config = $("<img id='cstmz_actv' style='vertical-align:middle'/>");
			img_config.attr("src", getRootPath() + "/scripts/common/images/cstmz_actv.gif");
			a_config.append(img_config);
			td_config.append(a_config);
			tr.append(td_config);
			var pathName=window.document.location.pathname;
			var config_obj = new Object();
			config_obj['pathName'] = pathName;
			config_obj['queryId'] = queryId;
			config_obj['selectedColumns'] = selectedColumns;
			config_obj['unselectedColumns'] = unselectedColumns;
			config_obj['operatorShow'] = operatorShow;	
			config_obj['queryConfigId'] = queryConfigId;//2013-08-06 haochengjie
			a_config.click(function(){
				var nowPath = getRootPath() + "/scripts/common/table_config.html?config_obj="+encodeURIComponent($.toJSON(config_obj));
				var nowDiv = $('<div id="configTable"><iframe id="wfIframe" frameborder="0" width="680px" height="330px" scrolling="no" src=""></iframe></div>');
				nowDiv.children().attr("src",nowPath);
				nowDiv.data("config_obj",config_obj);
				nowDiv.dialog({
					bgiframe: true,
					title:'自定义表格',
					autoOpen:true,
					width:"auto",
					height:"auto",
					closeOnEscape: false,
	   				open: function(event, ui) { 
	   					$(".ui-dialog-titlebar-close").hide();
					},
					buttons : {
					"关闭":function(){
	//					$("#wfIframe")[0].contentWindow.confirm();
						nowDiv.dialog("close");
						$$.reconfig(queryId);
					}
				},
	//				close: function(event, ui) {
	//					$("#wfIframe")[0].contentWindow.confirm();
	//					nowDiv.dialog("close");
	//					$$.reconfig(queryId);
	//				},
					modal:true
				});	
			});
		}	
		
		/*Add by dhx end1*/
		
		titleTable.append(tr);

		titlediv.append(titleTable);
		gridContainer.append(titlediv);
		if (!gridContainer.attr("searchDiv")) {
			gridContainer.css('padding', '5px');
		}
	}
	
	var table = $("<table></table>");
	var thead = $("<thead></thead>");
	var tr = $("<tr></tr>");
	thead.append(tr);
	table.append(thead);
	table.toggleClass("display");
	table.id = gridContainer.id + "_datatable";
	
	if (gridContainer.attr("showcheckbox") == "true") {
		var th = $("<th></th>");
		th.html("<input type='checkbox' />");
		th.addClass("checkboxHeader");
		th.css("width", "3%");
		tr.append(th);
	}
	
	function getcolumn(columns,key){
		for(var i=0;i<columns.length;i++){
			if(columns[i].key == key){
				return columns[i];
			}
		}
	}
	var titleAlign = gridContainer.attr("titleAlign");
	var reporttitle = gridContainer.attr("reporttitle");
	if(typeof(reporttitle) != 'undefined' && reporttitle != 'false'){//多表格表头
		//var titleMap = eval(reporttitle);
		var multiTableHeads = new MultiTableHeads();
		if(reporttitle == "true"){
		var columnsList = gridContainer.data("columnsList");
			multiTableHeads.init(eval(columnsList));
		}else{
			multiTableHeads.init(eval(reporttitle));
		}
	    //multiTableHeads.init(titleMap);
		var titleMap = multiTableHeads.result;
		var columns = gridContainer.data("columns");
		for(var i=0;i<multiTableHeads.rowMax;i++){
			var trtitle = $("<tr></tr>");
			var mcolumns = titleMap[i];
			for(var j=0;j<mcolumns.length;j++){
				var column = mcolumns[j];
				if(column != null && column != ''){
					var th = $("<th></th>");
					if(column.indexOf('@') >0 && column.indexOf('#') >0 ){
						var columnth = column.split('@');
						th.html(columnth[0]);
						th.attr("colspan",columnth[1].split('#')[0]);
						th.attr("rowspan",columnth[1].split('#')[1]);
					}else if(column.indexOf('@') >0 && column.indexOf('#') <0 ){
						var columnth = column.split('@');
						th.html(columnth[0]);
						th.attr("colspan",columnth[1]);
					}else if(column.indexOf('#') >0 && column.indexOf('@') <0 ){
						var columnth = column.split('#');
						th.html(columnth[0]);
						th.css("width",getcolumn(columns,multiTableHeads.columns[j]).width);
						th.attr("keyfiexd",multiTableHeads.columns[j]);
						th.attr("rowspan",columnth[1]);
					}else if(column.indexOf('#') <0 && column.indexOf('@') <0 ){
						th.html(column);
						th.css("width",getcolumn(columns,multiTableHeads.columns[j]).width);
						th.attr("keyfiexd",multiTableHeads.columns[j]);
					}
					th.css('vertical-align', 'middle');
					if(typeof(titleAlign) == 'undefined' || titleAlign == null){
						th.css('textAlign', column.align);
					}else{
						th.css('textAlign', titleAlign);
					}
					trtitle.append(th);
				}
			}
			if(i == 0){
				if (gridContainer.attr("operators")) {
					var th = $("<th></th>");
					th.html("${query.ui.operate}");
					th.addClass("operateHeader");
					th.attr("rowspan",multiTableHeads.rowMax);
					trtitle.append(th);
				}
			}
			thead.append(trtitle);
		}
		
		var rcolumns = multiTableHeads.columns;
		var sortcolumns = [];
		for(var i=0;i<rcolumns.length;i++){
			for(var j=0;j<columns.length;j++){
				if(rcolumns[i] == columns[j].key){
					sortcolumns[i] = columns[j];
				}
			}
		}
		for(var j=0;j<columns.length;j++){
			var bb = true;
			for(var i=0;i<rcolumns.length;i++){
				if(rcolumns[i] == columns[j].key){
					bb = false;
				}
			}
			if(bb == true){
				sortcolumns.push(columns[j]);
			}
		}
		gridContainer.data("columns", sortcolumns);
	}else{
		var columns = gridContainer.data("columns");
		for ( var i = 0; i < columns.length; i++) {
			var column = columns[i];
			var th = $("<th></th>");
			th.html(renderTitle(column));
			th.css("width", "80px");
			if(typeof(titleAlign) == 'undefined' || titleAlign == null){
				th.css('textAlign', column.align);
			}else{
				th.css('textAlign', titleAlign);
			}
			if(!column.display) {
				th.hide();
			}
			tr.append(th);
		}
	}
	
//	var columns = gridContainer.data("columns");
//	for ( var i = 0; i < columns.length; i++) {
//		var column = columns[i];
//		var th = $("<th></th>");
//		th.html(column.header);
//		th.css("width", column.width);
//		th.css("textAlign", column.align);
//		if(!column.display) {
//			th.hide();
//		}
//		tr.append(th);
//	}
	
	if (gridContainer.attr("operators")) {
		if(titleMap == null || titleMap == '' || typeof(titleMap) == 'undefined'){
			var th = $("<th></th>");
			th.html("${query.ui.operate}");
			th.addClass("operateHeader");
			tr.append(th);
		}
	}
	
	table.data("grid", gridContainer);
	table.grid = gridContainer;
	gridContainer.append(table);
	gridContainer.data("dataTable", table);
	if(gridContainer.attr("metadatamethod") == 'true'){
		$$.executeQuery(gridContainer);
	}else{
		var autoload = gridContainer.attr("autoload") == 'false' ? false : true;
		if (autoload === true) {
			$$.executeQuery(gridContainer);
		}
	}
};

PMSApplication.prototype.fetchGridMetadata = function(grid,columnpageinfo,columnsortinfo,autoload) {
	grid.id = grid.attr("id");
	var queryId = grid.attr("queryid");
	var usecache = grid.attr("usecache") == 'true' ? true : false;
	var configurable = grid.attr('configbutton') == 'false' ? false : true;
	var manager = configurable ? this.customQueryManager:this.queryManagerName;//当显示列不需要配置的时候不走configquerymanager
	var method = this.queryMetadataMethod;
	var reqObj = null;
	if(grid.attr("metadatamethod") == 'true'){
		queryId = grid.attr("metaqueryid");
		manager = this.queryManagerName;
		method = "getMetadataByArgs";
		var list = [];
		var searchDiv = grid.attr('searchDiv') ? grid.attr('searchDiv') : ("PMS_SEARCH_CONDITION_DIV_" + queryId);
		var searchGrid = $("#"+searchDiv);
		if (searchGrid !== null && searchGrid.length > 0){
			var ele = searchGrid.find(":input[name]");
			if(ele.length != 0) {
				for(var i=0;i<ele.length;i++){
					var args = new Object();
					var eleObj = $(ele[i]);
					args["key"] = eleObj.attr('name');
					args["value"] = eleObj.val();
					list[i] = args;
				}
			}
		}
		var currentPage = 1;
		var recordsCountPage = 0;
		var recordsPerPage = 3;
		var showheaderpaging = grid.attr("showheaderpaging");
		if(typeof(showheaderpaging) == 'undefined' || showheaderpaging == 'false'){
			recordsPerPage = 99999;
		}
        if(typeof(columnpageinfo) != 'undefined' && columnpageinfo != null){
			currentPage = columnpageinfo.currentPage;
			recordsCountPage = columnpageinfo.recordsCountPage;
			recordsPerPage = columnpageinfo.recordsPerPage;
		}
		var columnpageinfostr = currentPage + "," +recordsCountPage + "," + recordsPerPage;
		
		var columnsSort = new Object();
		if(typeof(columnsortinfo) != 'undefined' && columnsortinfo != null){
			if(typeof(columnsortinfo) == 'object'){
				columnsSort = $.toJSON(columnsortinfo);
			}else{
				var columnsortinfos = columnsortinfo.split(":");
				columnsSort["variableName"] = "id";
				columnsSort["variableValue"] = columnsortinfos[0];
				columnsSort["sortType"] = columnsortinfos[1];
				columnsSort = $.toJSON(columnsSort);
			}
		}
		
		reqObj = new PMSRequestObject(manager,
			method, [ queryId, columnpageinfostr, columnsSort, list, ""+usecache ]);
	}else{
		var searchDiv = grid.attr('searchDiv');
		if(searchDiv == undefined || searchDiv == ""){
			searchDiv = this.defaultSearchDivPrefix + queryId;
			grid.attr('searchDiv',searchDiv);
		}
		if (configurable == false) {// 当显示列不需要配置
			// reqObj = new PMSRequestObject(manager, method, [ queryId, ""+usecache ]);//2013.11.6
			reqObj = new PMSRequestObject(manager, method, [ queryId ]);// 2013.11.6
		} else {
			reqObj = new PMSRequestObject(manager, method, [ queryId, this.pageURL]);//, "" + usecache 
		}
	}
	
	this.serverInvoke(reqObj, function(data, textStatus, XMLHttpRequest) {
		var metadata = $.fromJSON(data.data);
		var customized =  metadata.selected != undefined;
		if(customized){
			var columnsMap = {};
			for(var i in metadata.columns){
				var column = metadata.columns[i];
				columnsMap[column.key] = column;
			}
			var customizedColumn = [];
			function _nxServiceAdapter(source, attr, display){
				if(source != undefined){
					var array = [];
					for(var i in source){
						var key = source[i];
						var element = {
								key:key,
								header:columnsMap[key].header
						};
						array.push(element);
						columnsMap[key].display = display;
						customizedColumn.push(columnsMap[key]);
						var f = delete columnsMap[key];
					}
					grid.data(attr, array);
				}
			};
			_nxServiceAdapter(metadata.selected,"selectedColumns",true);
			_nxServiceAdapter(metadata.unSelected,"unselectedColumns",false);
			for(var key in columnsMap){
				columnsMap[key].display = false;
				if(key=="id"){//为了兼容目前通过data[0]取数据的方式
					customizedColumn.unshift(columnsMap[key]);
				}else{
					customizedColumn.push(columnsMap[key]);
				}
			}
			grid.data("columns", customizedColumn);
		}else{
			grid.data("columns", metadata.columns);
			grid.data("selectedColumns", metadata.selectedColumns);
			grid.data("unselectedColumns", metadata.unselectedColumns);
		}
		
		grid.data("header", metadata.header);
		grid.data("order", metadata.order);
//		grid.data("columns", metadata.columns);
		grid.data("columnsList", metadata.columnsList);
		grid.data("columnsSort", metadata.columnsSort);
		grid.data("columnpageinfo", metadata.columnpageinfo);
//		grid.data("selectedColumns", metadata.selectedColumns);
//		grid.data("unselectedColumns", metadata.unselectedColumns);
		grid.data("operatorShow", metadata.operatorShow);
		grid.data("queryDataId", metadata.queryDataId);
		grid.data("columnpageinfo", metadata.columnpageinfo);
		grid.data("uniondatakey", metadata.uniondatakey);
		grid.data('queryConfigId', metadata.queryConfigId);//2013-08-06 haochengjie
		
		if(grid.attr("metadatamethod") == 'true'){
			if(showheaderpaging == 'true'){
				var columnsPageInfo = new ColumnsPageInfo();
				if(typeof(autoload) == 'undefined'){
					autoload = grid.attr('autoload');
					if(autoload == 'true'){autoload = true;}
				}
				if(autoload === true){
					columnsPageInfo.createPageInfo(queryId,metadata.columnpageinfo,metadata.columnsSort);
				}
				if(grid.attr("showtitle") == 'true'){
					var titleDiv = $("#Querytable_"+queryId);
					if(titleDiv.length != 0){
						$(titleDiv).remove();
					}
				}
                var tableDiv = $("#"+queryId+"_wrapper");
				if(tableDiv.length != 0){
					$(tableDiv).remove();
				}
				var tableDiv1 = $("#"+queryId+"_id");
				if(tableDiv1.length != 0){
					$(tableDiv1).remove();
				}
				var dataTables_wrapper = grid.find("div[class=dataTables_wrapper]");
				if(dataTables_wrapper.length != 0){
					$(dataTables_wrapper).remove();
				}
				if(autoload === true){
					$$.buildGridContainer(grid);
				}
			}else{
				//$$.buildGridSearchContainer(grid);
				if(autoload === true){
					$$.buildGridContainer(grid);
				}
			}
		}else{
			$$.buildGridSearchContainer(grid);

			$$.adoptCache(grid);
			
			$$.buildGridContainer(grid);
		}

		var metaCallback = eval(grid.attr("metaCallback"));
		if (typeof metaCallback == 'function') {
			metaCallback(metadata);
		}
	});

}


PMSApplication.prototype._fnGetConditions = function(searchDiv) {
	var conditions = [];
	if (searchDiv !== null && searchDiv.length > 0) {
		var ele = searchDiv.find(':input[name]');
		ele.each(function(i) {
			var map = {};
			var key = $(this).attr("name");
			var isExist = false;
			for ( var j = 0; j < conditions.length; j++) {
				if (key == conditions[j].key) {
					isExist = true;
					map = conditions[j];
					break;
				}
			}
			var value = $(this).val();
			var type = $(this).attr("type");
			var likeOption = $(this).attr("likeOption"); //是否进行模糊查询，默认值为true
			if (likeOption == undefined || likeOption == null || likeOption != "false") 
				likeOption = "true";
			if ((type == 'checkbox' || type == 'radio')) {
				if ($(this).attr("checked") !== true) {
					value = "";
				}
			}
			if ((type == 'select-one')) {
				if (value == undefined || value == null) {
					value = "";
				}
			}
			if ((type == "text") && likeOption == "true") { //如果是用户手动输入项，需要在前后各加一个百分号
				if (value != undefined && value != null && value != "") {
					value = "%" + value + "%";
				}
			}
			if (isExist) {
				map.value += encodeURIComponent("," + value);
			} else {
				map.key = key;
				map.value = encodeURIComponent(value);
				conditions.push(map);
			}
		});
	} else {
		// no search conditions found.
	}
	return conditions;
};
PMSApplication.prototype.buildQueryConditions = function(grid) {
	var queryId = grid.attr("queryid");
	if (grid.attr("metadatamethod") == 'true') {
		queryId = grid.attr("metaqueryid");
	}
	var cacheable = (grid.attr("usecache") == "true");
	var cacheKey = this._cacheKey(queryId);
	var bShowPaging = grid.attr("showpaging") == "false" ? false : true;
	var columnsSort = grid.data("columnsSort");
	var columnpageinfo = grid.data("columnpageinfo");
	function parseSort(sDefaultSort) {
		var oSortinfo = null;
		if (sDefaultSort != undefined && sDefaultSort != "") {
			function doParse(sSortinfo) {
				var a = sSortinfo.split(/\s/);
				return {
					variableName : a[0],
					direction : a[1] == "desc" ? -1 : 1
				};
			}
			var a = $.trim(sDefaultSort).split(",");
			var oPrevSort;
			for ( var i = 0; i < a.length; i++) {
				var o = doParse($.trim(a[i]));
				if (i == 0) {
					oSortinfo = o;
				}
				if (oPrevSort != null) {
					oPrevSort.next = o;
				}
				oPrevSort = o;
			}
		}
		return oSortinfo;
	}
	var order = grid.data("order");
	var oSortinfo = parseSort(order);
	var searchDiv = $("#" + grid.attr("searchDiv"));
	var conditions = this._fnGetConditions(searchDiv);

	var queryReqObj = {
		conditions : conditions,
		cacheable : cacheable,
		cacheKey : cacheKey,
		pageinfo : {
			currentPage : 1,
			recordsPerPage : bShowPaging ? 10 : 9999
		},
		sortinfo : oSortinfo,
		columnpageinfo : columnpageinfo,
		columnsSort : columnsSort,
		queryId : queryId
	};
	return queryReqObj;
};

PMSApplication.prototype.executeQuery = function(grid) {
	var managerName = grid.attr("managername") ? grid.attr("managername") : this.queryManagerName;
	var methodName = grid.attr("methodname") ? grid.attr("methodname") : this.queryMethod;
	var bShowTitle = grid.attr("showtitle") == "false" ? false : true;
	var bShowPrint = grid.attr("showprint") == "false" ? false : true;
	var bShowPaging = grid.attr("showpaging") == "false" ? false : true;
	var bShowExport = grid.attr("showExport") == "true" ? true : false;
	var queryId = grid.attr("queryid");
	if(grid.attr("metadatamethod") == 'true'){
		queryId = grid.attr("metaqueryid");
	}

	var searchDiv = $("#" + grid.attr("searchDiv"));
	
	//added by xiang zhongming on 2012/06/27 for data render
	var fnRender = function(obj) {
		var fnRender = grid.attr("fnRender");
		var treetable = grid.attr("treetable");
		if(obj){
			if(typeof treetable != "undefined" && treetable == "true"){//haochnegjie, treetable return
				if (fnRender) {
					//return eval(fnRender)(obj.aData,obj.iDataColumn,obj.sColumnName);
					return eval(fnRender)(obj,obj.sColumnName);
				} else {
					return obj.aData[obj.iDataColumn];
				}
			}else{//datatable return
				if (fnRender) {
					return eval(fnRender)(obj.aData,obj.iDataColumn,obj.sColumnName,$$.aDataAllColumnsToMap(obj.aData,obj.oSettings.allColumns));
				} else {
					return obj.aData[obj.iDataColumn];
				}
			}
		}
	};
	
	var columns = grid.data("columns");
	var aoColumns = [];
	// Set Query info.
	for (var i = 0; i < columns.length; i++) {
		var col = columns[i];
		// Set Display.
		aoColumns[i] = 
			{
				"bVisible": col.display,
				"sColumnName": col.key,
				"sWidth": col.width,
				"bSortable": col.allowSort,
				"sAlign": col.align,
				"fnRender": fnRender,
				"sColumnSort": col.columnSort,
				"sTips": col.tips  //haochengjie
			};
	}

	var queryReqObj = this.buildQueryConditions(grid);
	
	var reqObj = new PMSRequestObject(managerName, methodName, [ $.toJSON(queryReqObj) ]);
			
	var table = grid.data("dataTable");
	
	//haochnegjie start
	var operatorShow = grid.data('operatorShow');
	if(typeof operatorShow == 'undefined' || operatorShow == null || operatorShow == ''){
		operatorShow = grid.attr("operatorshow");
	}
	//haochengjie end
	
	var bShowCheckbox = grid.attr("showcheckbox") == "true" ? true : false;
	
	//haochnegjie start
	if(operatorShow == "independent"){
		bShowCheckbox = true;
	}
	//haochengjie end
	
	var checkMode = grid.attr("checkmode") == 'single' ? 'single' : 'multi';
	var checkRenderer = null;
	var checkClick = null;
	if (bShowCheckbox === true) {
		checkRenderer = eval(grid.attr("checkRenderer"));
		checkClick = eval(grid.attr("checkClick"));
	}
	
	var bShowOperate = false;
	
	var opArrs = eval(grid.attr("operators"));
	bShowOperate = (opArrs && (opArrs.length>0) ) ? true : false;

	var addFunc = eval(grid.attr("addFunc"));
	var bShowAdd = (typeof addFunc) == 'function' ? true : false;
	
	/**
	 * 解决表格无数据宽度不足100%的问题
	 * @param {Object} oInstance
	 * @author hewang
	 */
	var setTableWidth = function (oInstance) {
		var table = oInstance.nTable;
		if($(table).find('td').length <= 1) {
			$(table).css("width", "100%");
			$(table).css("table-layout", "auto");
		}
//		var parentWidth = $(table).parents("div.dataTables_wrapper")[0].offsetWidth;
//		var tableWidth = table.offsetWidth;
//		if (parentWidth > tableWidth) {
//			$(table).css("width", "100%");
//		}
	}
	
	//begin added by yangtao 
	var fnSaveCheck = function(oInstance, oSettings, json){
		var allData = $.parseJSON(oSettings.data);
		var pageinfo = allData['pageinfo'];
		if (null != pageinfo && "undefined" != pageinfo && parseInt(pageinfo['totalRecords']) > 0) {
			var nTable = oInstance.nTable;
			//添加序号
			var showNO = grid.attr("showNO");
			if(showNO != "false"){
				var noThead= $('thead',nTable);
				var noTbody = $('tbody',nTable);
				var theadRows = $(noThead).children();
				var tbodyRows = $(noTbody).children();
				
				var thh = $("#xuhaoid");
				if(thh.length == 0){
					var cells = $(theadRows[0]).children();
					if(cells.length == 0){
						cells = $(theadRows[1]).children();
					}
					var th = $("<th></th>");
					th.html("序号");
					th.css("vertical-align","middle");
					th.css("text-align","center");
					th.css("padding-right", "10px");
					th.css("width","4%");
					th.attr("id","xuhaoid");
					th.attr("rowspan",theadRows.length);
					$(cells[0]).before(th);
				}
				
				for(var i=0;i<tbodyRows.length;i++){
					var cells = $(tbodyRows[i]).children();
					var td = $("<td></td>");
					td.attr("id","xuhaoid"+i);
					td.css("text-align","center");
					td.css("width","4%");
					td.html(pageinfo['startRowPosition']+i+1);
					var tdd = $("#xuhaoid"+i);
					if(tdd.length == 0){
						$(cells[0]).before(td);
					}
				}
			}
		}
		
		if(oInstance.bSaveCheck){
			var aCheckedBox = oInstance.aCheckedBox;
			if(aCheckedBox.length > 0){
				
				$(':checkbox',nTable).each(function(index){
					for (var i=0; i < aCheckedBox.length; i++) {
						if(aCheckedBox[i].val() == $(this).val()){
							$(this).attr('checked', true);
						}				
					}
				});			
			}
		}
		//重新设置表格的宽度
		setTableWidth(oInstance); 
		
		//query回调函数
		var queryCallBack = grid.attr("queryCallBack");
		if(queryCallBack) {
			eval(queryCallBack)(json);
		}
        //alert(document.getElementById("idTableFixed"));
        var treetable = grid.attr("treetable");
		if(typeof treetable != "undefined" && treetable != null){
			var map = [];
			var redata = $.parseJSON(oSettings.data).data;
			for(var i=0;i<redata.length;i++){
				//map[i] = redata[i].parentId;
				redata[i].index = i+1;
			}
			for(var i=0;i<redata.length;i++){
				if(redata[i].parentId == '0'){
					map[i] = 0;
				}
				for (var j = 0; j < redata.length; j++) {
				    if(redata[i].parentId == redata[j].id){
						if(redata[i].parentId != '0'){
							map[i] = redata[j].index;
						}
					}
				}
			}
			var isleaf1 = grid.attr("isleaf");
			if(typeof isleaf1 == "undefined" && isleaf1 == "true"){
				isleaf1 = true;
			}else{
				islesf1 = false;
			}
			var options = {openImg: "images/TreeTable/tv-collapsable.gif", shutImg: "images/TreeTable/tv-expandable.gif", leafImg: "images/TreeTable/tv-item.gif", lastOpenImg: "images/TreeTable/tv-collapsable-last.gif", lastShutImg: "images/TreeTable/tv-expandable-last.gif", lastLeafImg: "images/TreeTable/tv-item-last.gif", vertLineImg: "images/TreeTable/vertline.gif", blankImg: "images/TreeTable/blank.gif", collapse: false, column: 0, striped: false, highlight: true,  state:false, isleaf:isleaf1};
			var tbody = $('tbody',nTable);
			$(tbody).attr("id","treet1");
			//$(tbody).jqTreeTable(map, options);
			$(tbody).jqTreeTable(map, options,grid.attr("queryid")+"_id");
		}
		
        //多表头对齐方式
		var reporttitle = grid.attr("reporttitle");
		if (typeof(reporttitle) != 'undefined') {
			var queryid = grid.attr("queryid");
			var div = $("#"+queryid+"_id");
			//div.css("height","400px");
		}
		
	    
		//将表格添加一行合计
		var reporttotle = eval(grid.attr("reporttotle"));
		$("#totalfiexdid").remove();
		if(typeof(reporttotle) != 'undefined' ){
			var totleMap = eval(reporttotle);
			var columns = oInstance.allColumns;
			var totletr = $("<tr></tr>");
			if(showNO == "true"){
				var tdno = $("<td>&nbsp;</td>");
				tdno.css("background-color","#BEC5E7");
				totletr.append(tdno);
			}
			for(var i=0;i<columns.length;i++){
				if(columns[i].display === true){
					var b = true;
					for(var j=0;j<totleMap.length;j++){
						var mapcolumn = totleMap[j].split(":");
						if(mapcolumn[0] == columns[i].key){
							var totalHeader = columns[i].header+"${query.statistisc.sum}";
							b = false;
							var columnvalue = mapcolumn[1];
							var td = $("<td></td>");
							td.css("background-color","#BEC5E7");
							td.css("text-align", "right");
							if(typeof(columnvalue) == 'undefined' || columnvalue == 'null' || columnvalue == 'false'){
								td.html("");
							}else if(columnvalue == 'true'){
								var totalData = $.parseJSON(oSettings.data);//total
				                var totalDatas = [];
								if(typeof(totalData["StatisticsInfo"]) != 'undefined' && totalData["StatisticsInfo"] != null){
                                    totalDatas = totalData["StatisticsInfo"].split(",");//total
								}
								var totalHeaderSum = columns[i].key+"${query.statistisc.sum}";
								var totalHeaderCount = columns[i].key+"${query.statistisc.count}";
								var totalHeaderAvg = columns[i].key+"${query.statistisc.avg}";
								var totalHeaderMin = columns[i].key+"${query.statistisc.min}";
								var totalHeaderMax = columns[i].key+"${query.statistisc.max}";
								for(var k=0;k<totalDatas.length;k++){
									var totalDatass = totalDatas[k].split(":");
									if(totalHeaderSum.trim() == totalDatass[0].trim()){
										td.html(totalDatass[1]);
									}else if(totalHeaderCount.trim() == totalDatass[0].trim()){
										td.html(totalDatass[1]);
									}else if(totalHeaderAvg.trim() == totalDatass[0].trim()){
										td.html(Math.round(parseFloat(totalDatass[1])*100)/100);
									}else if(totalHeaderMin.trim() == totalDatass[0].trim()){
										td.html(totalDatass[1]);
									}else if(totalHeaderMax.trim() == totalDatass[0].trim()){
										td.html(totalDatass[1]);
									}
								}
							}else{
								if(columnvalue.indexOf('合计') > 0){td.css("text-align", "center");}
								td.html(columnvalue);
							}
							totletr.append(td);
						}
					}
					if(b){
						var td = $("<td></td>");
						td.css("background-color","#BEC5E7");
						td.css("width",columns[i].width);
						td.html("");
						totletr.append(td);
					}
				}
				
			}
			if(grid.attr("operators")){
				var td = $("<td></td>");
				td.css("background-color","#BEC5E7");
				totletr.append(td);
			}
			$(nTable).append(totletr);

		}



        if (typeof ajax_AfterLoad != 'undefined' && ajax_AfterLoad instanceof Function) {
            ajax_AfterLoad(grid[0].id);
        }

		//add flot start
//		var datasets = [];
//		var obj = $.parseJSON(oSettings.data);
//		var flotData = new FlotData();
//		
//		//add bar
//		var flotBarFnRender = grid.attr("flotBarFnRender");
//		if(typeof(flotBarFnRender) != 'undefined'){
//			datasets = eval(flotBarFnRender)(obj);
//			flotData.initBar('choices','placeholder',datasets,null);
//		}
//		//add pie
//		var flotPieFnRender = grid.attr("flotPieFnRender");
//		if(typeof(flotPieFnRender) != 'undefined'){
//			datasets = eval(flotPieFnRender)(obj);
//			flotData.initPie('choices1','placeholder1',datasets);
//		}
		//add flot end  ----haochnegjie---
		
	};
	//end added by yangtao
	
	var metadatamethod = grid.attr("metadatamethod");
	var unionqueryid = grid.attr("unionqueryid") == undefined ? null : grid.attr("unionqueryid");
	var wSearchDiv = grid.attr('searchDiv') ? grid.attr('searchDiv') : ("PMS_SEARCH_CONDITION_DIV_" + queryId);
	var columnscurrentpage = grid.data("columnscurrentpage");
	
	var bSaveCheck = grid.attr("savecheck") == 'true' ? true : false;
	var checkval = grid.attr("checkval");
	var td = table.dataTable({
			"bProcessing": true,
			"bDestroy": true,
			"bServerSide": true,
			"managerName": managerName,
			"methodName": methodName,
			"bAutoWidth": false,
			"sDom": '<"H"hr>tsqe<"F"lip>',
			"bLengthChange": true,
			"bInfo": true,
			"queryid": queryId,
			"aoColumns": aoColumns,
			"allColumns": columns,
			"bShowCheckbox": bShowCheckbox,
			"checkMode": checkMode,
			"checkRenderer": checkRenderer,
			"checkClick": checkClick,
			"checkval": checkval,
			"bShowOperate": bShowOperate,
			"bShowTitle": bShowTitle,
			"bShowPrint": bShowPrint,
			"bShowPaging": bShowPaging,
			"bShowExport": bShowExport,
			"operators": opArrs,
			"bShowAdd": bShowAdd,
			"addFunc": addFunc,
			"searchDiv": searchDiv,
			"fnGetConditions": this._fnGetConditions,//fnGetConditions,
			"bPaginate": true,
			"sPaginationType": "full_numbers",
			"reqObj": queryReqObj,
			"sPostData": "requestString=" + reqObj.toJsonString(),
			"sAjaxSource": $$.PMSDispatchActionUrl,
			"bJQueryUI": true, //显示jquery ui 图标 add by lvpeng
			fnDrawCallback: fnSaveCheck,
			"bSaveCheck": bSaveCheck,
			"operatorshow": operatorShow,
			"metadatamethod": metadatamethod,
			"unionqueryid": unionqueryid,
			"wSearchDiv": wSearchDiv,
			"columnscurrentpage": columnscurrentpage //haochengjie end
		});
		
	grid.data("jquery.datatable", td);
	var displaydiv = $("<div class='table-container' id='"+queryId+"_id'></div>");
	var table = grid.find("table[class*=display]");
	table.css("width", "");
	table.wrap(displaydiv);
	
}
/**
 * get the checked ids
 * @param {Object} gridQueryId
 */
PMSApplication.prototype.getSelected = function(gridQueryId) {
	var grid = $('div[queryId="' + gridQueryId + '"]');
	var chks = grid.find(' :input[class="pms_dt_checkbox"][checked]');
	var ids = new Array();
	chks.each(function(i) {
		ids.push($(this).val());
	});
	return ids;
}

/**
 * get all the checked ids, include the previous checked
 * @param {String} gridQueryId
 */
PMSApplication.prototype.getAllSelected = function(gridQueryId) {
	var grid = $('div[queryId="' + gridQueryId + '"]');
	var aCheckedBox = grid.data("allCheckedBox");
	var aCheckedId = [];
	for(var i = 0, length = aCheckedBox.length; i < length; i++){
		aCheckedId.push(aCheckedBox[i].val());
	}
	var aSplicePos = [];
	$('tbody tr', grid).each(function(index){
		var chbox = $(this).find('input[type=checkbox]:first');
		if (chbox.length < 1) {
			return;
		}
		var checkboxId = chbox.val();
		var isChecked = chbox.attr('checked')?true:false;

		var pos = -1;
		for(var i = 0; i < aCheckedBox.length; i++){
			if(checkboxId == aCheckedBox[i].val()){
				pos = i;
				break;
			}
		}
		if(pos == -1){
			if(isChecked){
				aCheckedId.push(checkboxId);
			}
		}else{
			if(!isChecked){
				aSplicePos.push(pos);
			}
		}	
	});
	
	aSplicePos.sort();
	for(var i = aSplicePos.length - 1; i >= 0; i--){
		aCheckedId.splice(aSplicePos[i], 1);
	}
	
	return aCheckedId;
}


/**
 * get the checked objs.
 * @param {Object} gridQueryId
 */
PMSApplication.prototype.getSelectedObj = function(gridQueryId) {
	var grid = $('div[queryId="' + gridQueryId + '"]');
	var chks = grid.find(' :input[class="pms_dt_checkbox"][checked]');
	var objs = new Array();
	chks.each(function(i) {
		objs.push($(this).data('obj'));
	});
	return objs;
}

/**
 * get the checked objects.
 * @param {Object} gridQueryId
 */
PMSApplication.prototype.getSelectedObjects = function(gridQueryId) {
	var grid = $('div[queryId="' + gridQueryId + '"]');
	var chks = grid.find(' :input[class="pms_dt_checkbox"][checked]');
	var objs = new Array();
	var cols = grid.data("columns");
	chks.each(function(i) {
		var obj = $(this).data('obj');
		var object = new Object();
		for (var j=0; j<cols.length; j++) {
			object[cols[j].key] = obj[j];
		}
		objs.push(object);
	});
	return objs;
}

/**
 * get the checked objectss.
 * @param {String} gridQueryId
 */
PMSApplication.prototype.getAllSelectedObjects = function(gridQueryId) {
	var grid = $('div[queryId="' + gridQueryId + '"]');
	var aCheckedBox = grid.data("allCheckedBox");
	var cols = grid.data("columns");
	var aCheckedObj = [];
	for(var i = 0, length = aCheckedBox.length; i < length; i++){
		var objFr = aCheckedBox[i].data('obj');
		var objTo = {};
		for (var j = 0; j < cols.length; j++) {
			objTo[cols[j].key] = objFr[j];
		}
		aCheckedObj.push(objTo);
	}
	var aSplicePos = [];
	$('table.display tbody tr', grid).each(function(index){
		var chbox = $(this).find('input[type=checkbox]:first');
		if (chbox.length < 1) {
			return;
		}
		var checkboxId = chbox.val();
		var obj = chbox.data('obj');
		
		var checkboxObj = new Object();
		for (var j=0; j<cols.length; j++) {
			checkboxObj[cols[j].key] = obj[j];
		}
		var isChecked = chbox.attr('checked')?true:false;
		var pos = -1;
		for(var i = 0; i < aCheckedBox.length; i++){
			if(checkboxId == aCheckedBox[i].val()){
				pos = i;
				break;
			}
		}
		if(pos == -1){
			if(isChecked){
				aCheckedObj.push(checkboxObj);
			}
		}else{
			if(!isChecked){
				aSplicePos.push(pos);
			}
		}	
	});
	aSplicePos.sort();
	for(var i = aSplicePos.length - 1; i >= 0; i--){
		aCheckedObj.splice(aSplicePos[i], 1);
	}
	return aCheckedObj;
}

PMSApplication.prototype.preparePrint = function(printContainer, printPageUrl) {
	this.printing.printObject = $("<object id=\"printObject\" style=\"display:none\" classid=\"clsid:1663ed61-23eb-11d2-b92f-008048fdd814\" codebase=\"smsx.cab#Version=6,5,439,72\"></object>");
	this.printing.printUrl = printPageUrl;
}

/**
 * 显示确认对话框
 * @param {Object} title 标题
 * @param {Object} content 内容
 * @param {Object} isHtml 内容是否含有html,true是,false否
 * @param {Object} width
 * @param {Object} callback function
 */
PMSApplication.prototype.showPromptDialog = function(title, content, isHtml, width, callback){
    var div = $('<div></div>');
    if (isHtml) {
        div.html(content);
    }
    else {
        div.text(content);
    }
    div.dialog({
        bgiframe: true,
        title: title,
        width: width,
        buttons: {
            "${form.ui.ok}": function(){
                if (callback && typeof callback == 'function') {
                     callback();
                }
				div.dialog('close');
				div.remove();
            },
            "${query.ui.cancel}": function(){
                div.dialog('close');
                div.remove();
            }
        },
        modal: true
    });
};


/**
 * Model Window for PMS UI
 * 
 * @author Zhou zaiqing
 * @since 2010/11/21
 */
PMSApplication.prototype.showMessage = function(title, message, callback) {
	if (this.dialog == null){
		this.dialog = new PMSDialog(title, message);
	}
	this.dialog.show(title, message, callback);
}

PMSApplication.prototype.showConfirm = function(title, message, callback) {
	if (this.dialog == null){
		this.dialog = new PMSDialog(title, message);
	}
	this.dialog.confirm(title, message, callback);
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

//treetable 返回表格属性 haochengjie
PMSApplication.prototype.treeTableFormProperties = function(map,obj) {
	var formProperties = '';
	var data = [];
	var datavalue = obj.aData[obj.iDataColumn];
	var propertiesType,size;
	if(typeof map[obj.sColumnName] != 'undefined'){
		propertiesType = map[obj.sColumnName][0];
		data = map[obj.sColumnName][1];
	    size = map[obj.sColumnName][2];
	}
	if(typeof data == 'undefined' || data == null){
		var dict = obj.oSettings.allColumns[obj.iDataColumn].dict;
        if(typeof(dict) != 'undefined' && dict != ''){
			data = $dict.getDictList(dict);//Modified by lushu@2013.4.23 $dict.getListAjax(dict);
		}else{
			data = [];
		}
	}
	if(propertiesType == "text"){
		formProperties = "<input type='text' class='"+obj.oSettings.queryid+"_"+obj.sColumnName+"' name='"+obj.oSettings.queryid+"_"+obj.sColumnName+"' value='"+obj.aData[obj.iDataColumn]+"' size='"+size+"' onchange=\"$$.treeTableFormPropertiesInputOnchange('"+obj.oSettings.queryid+"','"+obj.iDataRow+"')\">";
	} else if (propertiesType == "radio"){
		for(var i=0;i<data.length;i++){
			var sb = "<input type='radio' class='"+obj.oSettings.queryid+"_"+obj.sColumnName+"' name='"+obj.oSettings.queryid+"_"+obj.sColumnName +"_"+obj.iDataRow+"'";
			   if (data[i].dictCode && data[i].dictText&&datavalue) {
			   	if (data[i].dictCode == datavalue || data[i].dictText == datavalue) {
			   		sb += " checked='true'";
			   	}
			   }
			   sb += " value='"+data[i].dictCode+"' onclick=\"$$.treeTableFormPropertiesInputOnchange('"+obj.oSettings.queryid+"','"+obj.iDataRow+"')\">" + data[i].dictText + "</input>";
			formProperties = formProperties + sb;	
		}
	} else if (propertiesType == "checkbox"){
		for(var i=0;i<data.length;i++){
			var sb = "<input type='checkbox' class='"+obj.oSettings.queryid+"_"+obj.sColumnName+"' name='"+obj.oSettings.queryid+"_"+obj.sColumnName +"_"+obj.iDataRow+"'";
			  if (data[i].dictCode && data[i].dictText&&datavalue) {
			  	if (data[i].dictCode == datavalue || data[i].dictText == datavalue) {
			  		sb += " checked='true'";
			  	}
			  }
			  sb += " value='"+data[i].dictCode+"' onclick=\"$$.treeTableFormPropertiesInputOnchange('"+obj.oSettings.queryid+"','"+obj.iDataRow+"')\">" + data[i].dictText + "</input>";
			formProperties = formProperties + sb;	
		}
	} else if (propertiesType == "select"){
		var sb1 = "<select class='"+obj.oSettings.queryid+"_"+obj.sColumnName+"' name='"+obj.oSettings.queryid+"_"+obj.sColumnName +"_"+obj.iDataRow+"' width='"+size+"' onchange=\"$$.treeTableFormPropertiesInputOnchange('"+obj.oSettings.queryid+"','"+obj.iDataRow+"')\">";
		//var datavalues = datavalue.split('.');
		for(var i=0;i<data.length;i++){
	   	   var sb = "<option ";
		   if(data[i].dictCode && data[i].dictText){
		   	    if(data[i].dictCode == datavalue || data[i].dictText == datavalue) {
					sb += " selected='true'";
				}
		   }
		   sb += " value='"+data[i].dictCode+"'>" + data[i].dictText + "</option>";
			formProperties = formProperties + sb;	
		}
		formProperties = sb1 + formProperties + "</select>";
	} else if (propertiesType == "hidden"){
		formProperties = "<input type='hidden' class='"+obj.oSettings.queryid+"_"+obj.sColumnName+"' name='"+obj.oSettings.queryid+"_"+obj.sColumnName+"' value='"+obj.aData[obj.iDataColumn]+"'>";
	} else {
		formProperties = obj.aData[obj.iDataColumn];
	}
	return formProperties;
}

//treetable 返回表格属性,当属性value改变时调用此函数 haochengjie
PMSApplication.prototype.treeTableFormPropertiesInputOnchange = function(queryId,iDataRow){
	//alert($("#"+queryId+"_flag_"+iDataRow).val());
	var flagObj = $("#"+queryId+"_flag_"+iDataRow);
	flagObj.val("1");
	flagObj.parent().parent().css("background-color","#96CDCD");
	//alert($("#"+queryId+"_flag_"+iDataRow).val());
}

//treetable 批量添加时获取批量属性值，","号分开
PMSApplication.prototype.getPropersValuesByPropersName = function(queryId,attributeName){
	var flags = document.getElementsByName(queryId+"_flag");
	var attributeNames = document.getElementsByName(queryId+"_"+attributeName);
	var attributeNameValue = '';
	for(var i=0;i<flags.length;i++){
		if(flags[i] == 1){
			attributeNameValue = attributeNameValue + attributeNames[i] + ",";
		}
	}
	return attributeNameValue.substring(0,attributeNameValue.length-1);
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
			'确认' : function() {
				$(this).dialog("close");
				if(callback && typeof callback == 'function') {
					callback();
				}
				$(this).dialog("destroy");
			}
		}
	});

}

PMSDialog.prototype.confirm = function(title, content, callback) {
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
			'确认' : function() {
				$(this).dialog("close");
				$(this).dialog("destroy");
				if(callback && typeof callback == 'function') {
					callback();
				}
			},
			'取消' : function() {
				$(this).dialog("close");
				$(this).dialog("destroy");
			}
		}
	});

}

/**
 * The aData and allColumns values are combined into one map
 * 
 * @author hao chnegjie
 * @since 2012/10/30
 */
PMSApplication.prototype.aDataAllColumnsToMap = function(aData, allColumns) {
	var map = new Object();
	for(var i=0;i<allColumns.length;i++){
		var col = allColumns[i];
		map[col.key]=aData[i];
	}
	return map;
}

/**
 * 自定义搜索框的搜索方法
 * @param {Object} queryId queryid
 */
PMSApplication.prototype.executeSearch = function(queryId) {
	var gridStr = "div";
	gridStr += "[queryid='" + queryId + "']";
	$(gridStr).each(function(i) {
		var configContainer = $(this);
		configContainer.children().remove();
		$$.fetchGridMetadata(configContainer);
		var autoload = $(this).attr("autoload") == 'false' ? false : true;
		if (autoload === false) {
			$$.executeQuery($(this));
		}
	});
}

PMSApplication.prototype.executeMetadataSearch = function(queryId,columnpageinfo,columnsortinfo) {
	var gridStr = "div";
	gridStr += "[metaqueryid='" + queryId + "']";
	$(gridStr).each(function(i) {
		$$.fetchGridMetadata($(this),columnpageinfo,columnsortinfo,true);
	});
}

PMSApplication.prototype.executeSearchByMeta = function(queryId,columnscurrentpage) {
	var gridStr = $("div[metaqueryid='" + queryId + "']");
	var columnsortinfo = null;
	if(typeof(columnscurrentpage) == 'undefined'){columnscurrentpage = '1'}
	var pageinfo = {
		currentPage: parseInt(columnscurrentpage),
	    endRowPosition: 2,
		lastPage: false,
		recordsCountPage: 0,
		recordsPerPage: 3,
		startRowPosition: 0,
		totalPages: 0,
		totalRecords: 0
	}
	$$.fetchGridMetadata(gridStr,pageinfo,columnsortinfo,true);
}

//表头自定义搜索框
PMSApplication.prototype.executeMetadataAndData = function(queryId,sortinfo) {
	if(typeof(sortinfo) == 'undefined'){sortinfo = null}
	var pageinfo = {
		currentPage: 1,
	    endRowPosition: 2,
		lastPage: false,
		recordsCountPage: 0,
		recordsPerPage: 3,
		startRowPosition: 0,
		totalPages: 0,
		totalRecords: 0
	}
	$$.executeMetadataSearch(queryId,pageinfo,sortinfo,"true");
    //$$.executeSearch(queryId);
}


//added by lushu 2013.09.16
function renderColumns(aData,iColumn,sColumnName,map){
	if(typeof COLUMNS != "undefined"){
		var func = COLUMNS[sColumnName];
		if(typeof func == "function"){
			return func(aData,iColumn,sColumnName,map);
		}
	}
	return map[sColumnName];
};
function renderTitle(column){
	if(typeof TABLE_HEADS != "undefined"){
		var func = TABLE_HEADS[column.key];
		if(typeof func == "function"){
			return func(column);
		}
	}
	return column.header;
};
function exportDataTable (gridContainer){
	var operation = "online.export.max";
	var method = "operationAllowed";
	
	if(typeof(gridContainer) == "undefined" || gridContainer == ""){
		gridContainer = "gridContainer";
	}
	var queryConditions = $$.buildQueryConditions($('#' + gridContainer));
	var reqObj = new PMSRequestObject($$.queryManagerName, method, [$.toJSON(queryConditions),operation]);   
	$$.serverInvoke(reqObj, function(data, textStatus, XMLHttpRequest) {
		if(eval(data.data)){
			var data = decodeURIComponent($.toJSON(queryConditions));
			var exportFrm = $("<form></form>");
			exportFrm.attr("target", "_blank");
			exportFrm.attr("method", "post");
			exportFrm.attr("action", $$.PMSReportActionUrl);
			var requestStringElement = $("<input type='hidden' name='requestString' />");
			requestStringElement.val(data);
			exportFrm.append(requestStringElement);
			var requestTypeElement = $("<input type='hidden' name='requestType' value='4' />");
			exportFrm.append(requestTypeElement);
			var dynamic = $("<input type='hidden' name='dynamic' value='true'/>");
			exportFrm.append(dynamic);
			$("body").append(exportFrm);
			exportFrm.submit();
		}else{
			$$.onServerInvokeFailed(XMLHttpRequest, "${client.exportOverLimit}");
		};
	});
};

/**
 * 自定义搜索框的搜索方法
 * @param {Object} queryId queryid
 * @param {Object} searchDivId 自定义搜索条件所在的div的id
 * @param {Object} queryDivId queryId所在的div的id.(可选)
 */
//PMSApplication.prototype.executeSearch = function(queryId, searchDivId, queryDivId) {
//	var gridStr = "div";
//	if(queryDivId && queryDivId.length>0) gridStr += "[id='" + queryDivId + "']";
//	gridStr += "[queryid='" + queryId + "']";
//	$(gridStr).each(function(i) {
//		$$.executeQuery($(this), searchDivId);
//	});
//}

PMSApplication.prototype.run = function() {
	$$.initialize();
	$$.dataBinding();
}

// PMSApplication global instance.
var $$ = pmsApp = new PMSApplication();
