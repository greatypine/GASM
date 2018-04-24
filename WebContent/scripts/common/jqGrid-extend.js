/**
 * 框架集成jqGrid所需的扩展，沿用框架原有的查询前后交互体系。
 * 需要调用后端指定的类和方法，将原有的返回值修改为jqgrid的格式
 * 前端该用jqgrid展示
 * 2014年5月19日 chenwei
 */

/**
 * 自定义搜索框的搜索方法
 * @param {Object} queryId queryid
 * @param {Object} searchDivId 自定义搜索条件所在的div的id
 * @param {Object} queryDivId queryId所在的div的id.(可选)
 */
PMSApplication.prototype.executeSearchForJqgrid = function(jgPara){
	var gridStr = "div";
	gridStr += "[queryid='" + jgPara.queryId + "']";
	$(gridStr).each(function(i) {
		var configContainer = $(this);
		configContainer.children().remove();
		$$.fetchGridMetadata(configContainer);//在configContainer添加许多属性
		var autoload = $(this).attr("autoload") == 'false' ? false : true;
		if (autoload === false) {
			$$.executeQueryForJqgrid($(this),jgPara);
		}
	});
};

/**
 * 执行搜索，并将结果展示在jqGrid中
 * @author ChenWei
 *
 */
PMSApplication.prototype.executeQueryForJqgrid = function(grid,jgPara) {
	var managerName = grid.attr("managername") ? grid.attr("managername") : this.queryManagerName;
	var methodName = grid.attr("methodname") ? grid.attr("methodname") : this.queryMethod;
	var queryId = grid.attr("queryid");
	if(grid.attr("metadatamethod") == 'true'){
		queryId = grid.attr("metaqueryid");
	}

	var queryReqObj = this.buildQueryConditions(grid);
	
	var reqObj = new PMSRequestObject(managerName, methodName, [ $.toJSON(queryReqObj) ]);
			
	var defaultPara = {
			url:$$.PMSDispatchActionUrl,
			datatype: 'json',
	        mtype: 'post',
    		pager: '#' + jgPara.pageDivId,
    		postData: "requestString=" + reqObj.toJsonString(),
    		autowidth: true,  
    		altRows: true,
            rownumbers: true,
            loadonce: false,
           	pagerpos:"center",
           	multiselect:true,
            jsonReader: {
                    root: "rows",
                    repeatitems : false,
                    page: "page",
                    total: "total",
                    records: "records",
                    id:"id"
                    },
            rowList:[10,20,30],//行数选择框
            rowNum:10,//默认行数
            viewrecords:true,  //总数是否可见
    		loadComplete: function(xhr){ 
    			$(this).clearGridData(true);//IBM框架会将返回的数据就行包装，需要解析
    			this.addJSONData(JSON.parse(xhr.data));
    	   },
    	   onPaging:function(pgButton){
    		   var queryPage = 1;
    		   var recPerpage = $("#"+jgPara.pageDivId).find("select.ui-pg-selbox").val();
    		   if(pgButton =="records "){
    			   queryPage = 1;
    		   }else{
    			   var op = pgButton.substring(0,4);
    			   if(op == "firs"){//首页
    				   queryPage = 1;
    			   }else if(op == "last"){
    				   queryPage = $(this).lastpage;
    			   }else if(op == "prev"){
    				   queryPage = $(this).getGridParam('page') -1;
    			   }else if(op == "next"){
    				   queryPage = $(this).getGridParam('page') + 1;
    			   }
    		   }
    		   var reqObj = $(this).getGridParam("queryReqObj");
    		   reqObj.pageinfo.currentPage = queryPage;
    		   reqObj.pageinfo.recordsPerPage = recPerpage;
    		   
    		   var reqObj = new PMSRequestObject(managerName, methodName, [ $.toJSON(reqObj) ]);
    		   $(this).setGridParam({ postData: "requestString=" + reqObj.toJsonString() });
    		   $(this).trigger('reloadGrid');
    	   },
    	   onSortCol:function(index,iCol,sortorder){//排序事件
    	       
    	   }
    };
	var newPara = $.extend(defaultPara,jgPara.tablePara);
 	$("#" + jgPara.tableDivId).jqGrid("GridUnload");  
	$("#" + jgPara.tableDivId).jqGrid(newPara);
	$("#" + jgPara.tableDivId).jqGrid('navGrid','#'+jgPara.pageDivId,{del:false,edit:false,add:false,add:false,search:false,refresh:false});//编辑按钮采用自定义的多行编辑
	
	$("#" + jgPara.tableDivId).setGridParam({"tablePara": jgPara });
	$("#" + jgPara.tableDivId).setGridParam({"tableInitPara": jgPara.tablePara });
	$("#" + jgPara.tableDivId).setGridParam({"queryReqObj":queryReqObj});//将查询条件保存下来，用于翻页时查询
	
	//去除框架生成的标题栏和表头
	var tableId = "Querytable_" + jgPara.queryId;
	$("#" + tableId + ".Querytable").each(function(i){
		   var nextElm = $(this).next();
		   if(nextElm.is("table") && nextElm.hasClass("display")){
			   nextElm.remove();
		   }
		   $(this).remove();
	 });
};

/**
 * jqgrid表格参数 2014年5月20日 chenwei
 */
var JqGridPara = function(queryId,tableDivId, pageDivId, tablePara,toolbarPara) {
	this.queryId = queryId;
	this.tableDivId = tableDivId;
	this.pageDivId = pageDivId;
	this.tablePara = tablePara;
	this.toolbarPara = toolbarPara;
};
