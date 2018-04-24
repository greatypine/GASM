/*
* @Object       : CommonTable
* @author       : ruanqingfeng
* @version      : 表格组件1.0版本
* @desc1        : 表格组件（渲染出一个表格，对表格行进行动态"增""减"功能）
* @desc2        : 支持非单元格合并的简单表头
* @desc3        : 支持单元格合并的表头
* @desc4        : 允许页面中创建多个CommonEditTable,被维护在CommonEditTable.items数组变量中，可通过Id获取到某个CommonEditTable
* @for example  : 将本js引入后，在需要显示组件的地方加入
	<div id="myT" class="CommonEditTable" defaults='{
    	isTreePanel:true,
        selectMode:"checkbox",
    	columns:[
       		{
            	headerText:"姓名",
                headerIndex:"name",
                xtype:"label"
           	},{
            	headerText:"班级",
                headerIndex:"classname",
                xtype:"text"
           	},{
            	headerText:"数据字典",
                headerIndex:"isk",
                xtype:"comboBox",
                datas:[
                	{
                    	key:"0001",
                        value:"是"
                   	},{
                    	key:"0002",
                        value:"否"
                   	}
                ]
           	}
       	]
    }'></div>
*/
importCss(baseUrl+"/common/js/lib/component/CommonEditTable.css");
var CommonEditTable = function(_cfg){
	this.store   = [];
	this.columns = [];
	this.renderToDiv = null;   //渲染的DIV对象
	this.width = "100%";//表格宽度
	this.height = "100%";//表格高度
	this.commonTable = null;//表格对象
	this.commonTableContainer = null;//表格对象DIV容器
	this.isTreePanel = false;//是否是树形结构数据
	this.selectMode = "";//选择模式：【"checkbox","radio"】
	this.animate = true;//动画模式
	this.expand = false;//树形结构是否默认展开
	this.tableId = null;//表格唯一标识符
	this.cacheDictMap = {};//缓存数据字段
	var currentCreateRowIndex = 0;//当前创建的行号
	var currentRowNumber = 0;//当前创建的表格行序号
	$.extend(this,_cfg);
	var _this = this;
	
	var createTable = function(){
		if(_this.title != null){
			createTitle();
		}
		_this.commonTable = $("<table class='commonTable'  style='border-collapse:collapse;width:" + (_this.width.indexOf("%") == -1 ? _this.width : "100%") + ";'></table>");
		if(_this.headerColumns == null){
			createSimpleHeader();
		}else{
			createHeader();
		}
		
		_this.commonTableContainer = $('<div class="commonTableContainer" style="width:' + _this.width +';height:' + _this.height +';overflow:auto;"></div>');
		_this.renderToDiv.append(_this.commonTableContainer);
		_this.commonTableContainer.append(_this.commonTable);//表格
	}
	
	var createTitle = function(){
		var tableTitle = $('<div class="commonTable_title" style="width:' + _this.width +';"></div>');
		var toggleElement = $('<a href="#" ><img isOpen=true src="' + baseUrl + '/scripts/images/22.png" align="absmiddle" id="pic" /></a>');
		toggleElement.bind("click",function(){
			tableTitle.toggleClass("commonTable_title");
			tableTitle.toggleClass("commonTable_title_all_border");
			 $(_this.commonTableContainer).slideToggle("fast",function(_tbn){
				var _img = $(toggleElement).children().eq(0);
				if(_img.attr("isOpen")){
					$(_img).attr("isOpen",false);
					$(_img).attr("src",baseUrl + "/scripts/images/33.png");
				}else{
					$(_img).attr("isOpen",true);
					$(_img).attr("src",baseUrl + "/scripts/images/22.png");
				}
			});
		});
		
		var titleElement = $('<span>&nbsp;' + _this.title + '</span>');
		tableTitle.append(toggleElement);
		tableTitle.append(titleElement);
		_this.renderToDiv.append(tableTitle);//表格标题
	}
	
	var createSimpleHeader = function(){
		var tableHeader = $("<thead></thead>");
		_this.commonTable.append(tableHeader);
		var tableHeaderTr = $("<tr></tr>");
		tableHeader.append(tableHeaderTr);
		if(_this.selectMode == "checkbox"){
			var _selectHeaderTr = $("<th width='10px' class='uploadFileTableHeader '></th>");
			var _selectHeaderTrCheckBox = $("<input type='checkbox' name='commonTable_selectHeader'/>");
			_selectHeaderTrCheckBox.bind("change",function(){
				_this.commonTable.find("input[type='checkbox'][name='commonTable_selectDetail']").attr("checked",$(this).attr("checked"));;	
			});
			_selectHeaderTr.append(_selectHeaderTrCheckBox);
			tableHeaderTr.append(_selectHeaderTr);
		}
		if(_this.isTreePanel){
			tableHeaderTr.append($("<th width='10px'  class='uploadFileTableHeader '></th>"));
		}
		
		for(var i=0;i<_this.columns.length;i++){
			var curr_column = _this.columns[i];
			if(curr_column.xtype == "hidden"){
				continue;	
			}
			curr_column.width = (curr_column.width == null ? "" : ("width='" + curr_column.width) + "'");
			tableHeaderTr.append($("<th " + curr_column.width + " class='uploadFileTableHeader '>" + curr_column.headerText + "</th>"));
		}
		
	}
	
	var isArray = function(o) {      
	  return Object.prototype.toString.call(o) === '[object Array]';       
	} 
	
	
	var createHeader = function(){
		var tableHeader = $("<thead></thead>");
		_this.commonTable.append(tableHeader);
		
		var _tmpHeaderColumns = [];
		if(!isArray(_this.headerColumns[0])){
			_tmpHeaderColumns.push(_this.headerColumns);
		}else{
			_tmpHeaderColumns = _this.headerColumns;
		}
		for(var i=0;i<_tmpHeaderColumns.length;i++){
			var tableHeaderTr = $("<tr></tr>");
			tableHeader.append(tableHeaderTr);
			if(_this.selectMode == "checkbox" && (i == 0)){
				var _selectHeaderTr = $("<th rowspan='" + _tmpHeaderColumns.length + "' width='10px' class='uploadFileTableHeader '></th>");
				var _selectHeaderTrCheckBox = $("<input type='checkbox' name='commonTable_selectHeader'/>");
				_selectHeaderTrCheckBox.bind("change",function(){
					_this.commonTable.find("input[type='checkbox'][name='commonTable_selectDetail']").attr("checked",$(this).attr("checked"));;	
				});
				_selectHeaderTr.append(_selectHeaderTrCheckBox);
				tableHeaderTr.append(_selectHeaderTr);
			}
			if(_this.isTreePanel && (i == 0)){
				tableHeaderTr.append($("<th rowspan='" + _tmpHeaderColumns.length + "' width='10px'  class='uploadFileTableHeader '></th>"));
			}
			
			for(var j=0;j<_tmpHeaderColumns[i].length;j++){
				var curr_column = _tmpHeaderColumns[i][j];
				curr_column.rowspan = (curr_column.rowspan == null ? "1" : curr_column.rowspan);
				curr_column.colspan = (curr_column.colspan == null ? "1" : curr_column.colspan);
				curr_column.width = (curr_column.width == null ? "" : ("width='" + curr_column.width) + "'");
				tableHeaderTr.append($("<th " + curr_column.width + " rowspan='" + curr_column.rowspan + "' colspan='" + curr_column.colspan + "'  class='uploadFileTableHeader '>" + curr_column.headerText + "</th>"));
			}
		}
	}
	
	var columnEllipsis = function(){
		var ellipsisElements = _this.commonTable.find("div[isEllipsis=false][class*='text_ellipsis']");
		
		for (var i=0;i<ellipsisElements.length;i++){
			ellipsisElements.eq(i).hide();
			ellipsisElements.eq(i).attr("style","width:" + ellipsisElements.eq(i).parent()[0].clientWidth);
			ellipsisElements.eq(i).attr("isEllipsis","true");
			ellipsisElements.eq(i).show();
		}
	}
	
	var createTdContent = function(column,rowData,tableTr,_isCreateChildRow){
		column.xtype = getColumnXtype(column);
		var columnValue = rowData[column.headerIndex];
		columnValue = (columnValue == null ? "" : columnValue);
		var _tmpValidate = (column.validate == null ? "" : column.validate);
		var columnContent = null;
		if(column.xtype == "label"){
			if(column.dictName != null){
				var _dictData = getDictListByName(column.dictName);
				for(var i=0;i<_dictData.length;i++){
					if(columnValue == _dictData[i].dictCode){
						columnValue = _dictData[i].dictText;
						break;
					}
				}	
			}
			
			var labelClassName = "normal";
			if(column.listeners != null && column.listeners.onClick != null){
				labelClassName = "label_click_link";
			}
			columnContent = $("<div isEllipsis=false class='text_ellipsis' name='commonTableColumn_" + column.headerIndex + "'><nobr class='" + labelClassName + "' title='" + columnValue + "'>" + columnValue + "</nobr></div>");
			if(column.listeners != null && column.listeners.onClick != null){
				columnContent.find("nobr").bind("click",function(){
					var _tmpRowData = _getRowValueByTr($(tableTr));	
					column.listeners.onClick(_tmpRowData);
				}).bind("mouseover",function(){
					columnContent.find("nobr").removeClass().addClass("label_click_hover");
				}).bind("mouseout",function(){
					columnContent.find("nobr").removeClass().addClass("label_click_link");
				});
			}
		}if(column.xtype == "rowNum"){
			
			if(_isCreateChildRow == false){
				currentRowNumber++;
			}
			columnContent = $("<div divRowNum='" + (currentRowNumber) + "' isRowNum=true isEllipsis=false class='text_ellipsis' name='commonTableColumn_" + column.headerIndex + "'><nobr title='" + (currentRowNumber) + "'>" + (currentRowNumber) + "</nobr></div>");
		}else if(column.xtype == "text"){
			columnContent = $("<input commonTableFlag='true' style='width:100%' validate='" + _tmpValidate + "' revalidate='true' class='commonTable_inputText' id='commonTableColumnId_" + (createElementId()) + "_" + column.headerIndex + "' name='commonTableColumn_" + column.headerIndex + "' value='" + columnValue + "'/>");
			columnContent.bind("blur",function(){
				if(column.listeners && column.listeners.onBlur){
					var _tmpRowData = _getRowValueByTr($(tableTr));	
					column.listeners.onBlur(_tmpRowData);
				}
			});
		}else if(column.xtype == "textarea"){
			columnContent = $("<textarea commonTableFlag='true'  style='width:100%' validate='" + _tmpValidate + "' revalidate='true' class='commonTable_inputText' id='commonTableColumnId_" + (createElementId()) + "_" + column.headerIndex + "' name='commonTableColumn_" + column.headerIndex + "' value='" + columnValue + "'/>");
		}else if(column.xtype == "hidden"){
			columnContent = $("<input commonTableFlag='true'  type='hidden'  name='commonTableColumn_" + column.headerIndex + "' value='" + columnValue + "'/>");
		}else if(column.xtype == "checkBox"){
			var _checked = "";
			if((columnValue+"") != "0"){
				columnValue = "1";	
				_checked = "checked";
			}
			columnContent = $("<input commonTableFlag='true' " + _checked + " type='checkbox'  name='commonTableColumn_" + column.headerIndex + "' value='" + columnValue + "'/>");
		}else if(column.xtype == "delButton"){
			columnContent = $("<input commonTableFlag='true'  type='button' class='buttonu'  name='commonTableColumn_del_" + column.headerIndex + "' hiddenValue='" + columnValue + "' value='删除'/>");
			columnContent.bind("click",function(){
				if(_isCreateChildRow == false && tableTr.find("div[isRowNum=true]").length != 0 ){
					var curr_del_rowNum = tableTr.find("div[isRowNum=true]").eq(0).attr("divRowNum");
					$.each(_this.commonTable.find("div[isRowNum=true]"),function(_index,_element){
						if(parseInt($(_element).attr("divRowNum"))>parseInt(curr_del_rowNum)){
							var new_rowNum = parseInt($(_element).attr("divRowNum"))-1;
							$(_element).attr("divRowNum",new_rowNum);
							$(_element).find("nobr").attr("title",new_rowNum);
							$(_element).find("nobr").text(new_rowNum);
						}
					});
					currentRowNumber--;
					
				}
				tableTr.remove();
			});
		}else if(column.xtype == "comboBox"){
			columnContent = $("<select commonTableFlag='true'  name='commonTableColumn_" + column.headerIndex + "' />");
			if(column.datas != null){
				for(var i=0;i<column.datas.length;i++){
					var _isSelected = "";
					if(columnValue == column.datas[i].key){
						_isSelected = "selected";
					}
					var _tmpOption = $("<option value='" + column.datas[i].key + "' " + _isSelected + ">" + column.datas[i].value + "</option>");	
					columnContent.append(_tmpOption);
				}
			}else{
				var _dictData = getDictListByName(column.dictName);
				for(var i=0;i<_dictData.length;i++){
					var _isSelected = "";
					if(columnValue == _dictData[i].dictCode){
						_isSelected = "selected";
					}
					var _tmpOption = $("<option value='" + _dictData[i].dictCode + "' " + _isSelected + ">" + _dictData[i].dictText + "</option>");	
					columnContent.append(_tmpOption);
				}
			}
			columnContent.bind("change",function(){
				if(column.listeners && column.listeners.onChange){
					var _tmpRowData = _getRowValueByTr($(tableTr));	
					column.listeners.onChange(_tmpRowData);
				}
			});
		}else if(column.xtype == "search"){
			columnContent = $("<div style='white-space:nowrap;' name='commonTableColumn_div_" + column.headerIndex + "'></div>");
			var columnContent_input = $("<input commonTableFlag='true'  validate='" + _tmpValidate + "' revalidate='true'  class='commonTable_inputText_dis' readonly name='commonTableColumn_input_" + column.headerIndex + "' id='commonTableColumnId_" + (createElementId()) + "_" + column.headerIndex + "' value='" + columnValue + "'/>");
			if(column.valueField != null){
				rowData[column.valueField] = (rowData[column.valueField] == null ? "" : rowData[column.valueField]);
				var columnContent_value_input = $("<input commonTableFlag='true'  type='hidden' class='commonTable_inputText_dis' readonly name='columnContent_value_input_" + column.valueField + "' value='" + rowData[column.valueField] + "'/>");
				columnContent.append(columnContent_value_input);
			}
			
			//var columnContent_btn = $('<input commonTableFlag="true"  style="cursor:hand;" class="buttonu" type="button" value="搜索" />');
			var columnContent_btn = $('<img style="cursor:hand"  src="' + baseUrl + '/common/images/pop_up_window.gif" width="16" height="16" align="absmiddle" />');
			columnContent.append(columnContent_input);
			columnContent.append(columnContent_btn);
			
			columnContent_btn.bind("click",function(){
				var _tmpRowData = _getRowValueByTr($(columnContent_btn).parent().parent().parent());	
				
				var _onSearchCallBack = function(_searchResult){
					var _search_obj = {};
					if(typeof(_searchResult) != "object"){
						_search_obj[column.headerIndex] = _searchResult;
						_search_obj[column.valueField] = "";
					}else{
						_search_obj = _searchResult;	
					}
					columnContent_input.val(_search_obj[column.headerIndex]);
					if(column.valueField != null){
						columnContent_value_input.val(_search_obj[column.valueField]);
					}
				}
				column.listeners.onSearch(_tmpRowData,_onSearchCallBack);
				
			});
		}
		
		column.isParentColumn = (column.isParentColumn == null ? true : column.isParentColumn);
		column.isChildColumn = (column.isChildColumn == null ? true : column.isChildColumn);
		if(_isCreateChildRow){
			if(!column.isChildColumn){
				columnContent.addClass("commonTableHiddenColumnContent");
			}
		}else{
			if(!column.isParentColumn){
				columnContent.addClass("commonTableHiddenColumnContent");
			}
		}
		
		return columnContent;
	}
	
	var removeTableStore = function(){
		_this.commonTable.find("tr[isDataRow=true]").remove();	
		currentCreateRowIndex = 0;
	}
	var commonTableAppendTr = function(tableTr){
		_this.commonTable.append(tableTr);
	}
	
	var createElementId = function(){
		return (new Date()-0) + Math.random();	
	}
	
	var getDictListByName = function(dictName) {
		if(_this.cacheDictMap[dictName] == null){
				doManager("dictManager","findDictByName",dictName,function(_response){
				if(_response.result){
					var result = $.fromJSON(_response.data);
					_this.cacheDictMap[dictName] = result;
				}
			},false);
		}
		return _this.cacheDictMap[dictName];
		
	};
	
	var getColumnValue = function(_dataRow,_column){
		var dataElement = $(_dataRow).find("[name='commonTableColumn_" + _column.headerIndex + "']");
		if(getColumnXtype(_column) == "label" || getColumnXtype(_column) == "rowNum"){
			return dataElement.text();
		}else if(getColumnXtype(_column) == "text"){
			return dataElement.val();
		}else if(getColumnXtype(_column) == "textarea"){
			return dataElement.text();
		}else if(getColumnXtype(_column) == "hidden"){
			return dataElement.val();
		}else if(getColumnXtype(_column) == "checkBox"){
			return dataElement.attr("checked")?"1":"0";
		}else if(getColumnXtype(_column) == "comboBox"){
			return dataElement.val();
		}else if(getColumnXtype(_column) == "search"){
			dataElement = $(_dataRow).find("[name='commonTableColumn_input_" + _column.headerIndex + "']");
			return dataElement.val();
		}else if(getColumnXtype(_column) == "delButton"){
			dataElement = $(_dataRow).find("[name='commonTableColumn_del_" + _column.headerIndex + "']");
			return dataElement.attr("hiddenValue");
		}
	}
	
	var getColumnXtype = function(column){
		if(column.xtype == null){
			column.xtype = "label";
		}
		return column.xtype;
	}
	
	
	var _getSelectedValues = function(selected,isChildRow){
		var selectValues = [];
		selected = (selected ? ":checked" : ":not(:checked)");
		var selectedCheckboxs = _this.commonTable.find("input[isChildRow=" + isChildRow + "][type='checkbox'][name='commonTable_selectDetail']"+selected);	
		for(var i=0;i<selectedCheckboxs.length;i++){
			var curr_tr = selectedCheckboxs.eq(i).parent().parent();
			if(isChildRow){
				var rowMap = {};
				for(var j=0;j<_this.columns.length;j++){
					rowMap = putRowMapValue(rowMap,_this.columns[j],curr_tr);
					
				}
				selectValues.push(rowMap);
			}else{
				selectValues.push(_this.getRowValue(curr_tr.attr("rowIndex")));
			}
		}
		return selectValues;
	}
	
	var _getRowValueByTr = function(_tr){
		var rowMap = {};
		for(var j=0;j<_this.columns.length;j++){
			rowMap = putRowMapValue(rowMap,_this.columns[j],_tr);
			
		}
		if(_this.isTreePanel){
			rowMap.childrens = _this.getChildValues(_tr.attr("rowIndex"));
			
		}
		return rowMap;
	}
	
	var putRowMapValue = function(rowMap,column,dataRow){
		if(getColumnXtype(column) != "delButton"){
			rowMap[column.headerIndex] = getColumnValue(dataRow,column);
			if(column.dictName != null){
				var _dictData = getDictListByName(column.dictName);
				for(var i=0;i<_dictData.length;i++){
					if(rowMap[column.headerIndex] == _dictData[i].dictText){
						rowMap[column.headerIndex] = _dictData[i].dictCode;
					}
				}	
			};
		}
		
		if(getColumnXtype(column)){
			if(column.valueField != null){
				var dataElement = $(dataRow).find("[name='columnContent_value_input_" + column.valueField + "']");
				rowMap[column.valueField] = dataElement.val();
			}
		}
		
		return rowMap;
	}
	
	var createRow = function(_rowData,isCreateChildRow){
		if(isCreateChildRow == null){
			isCreateChildRow = false;	
		}
		var trBg = "commonTableTrEvenBg";
		if(isCreateChildRow == true){
			trBg = "commonTableTrOddBg";
		}
		var tableTr = $("<tr class='" + trBg + "' isDataRow=true isChildRow=" + isCreateChildRow + " rowIndex='"+currentCreateRowIndex+"' style='line-height: 22px;'></tr>");
		if(_this.animate){
			tableTr.hover(function(){
				tableTr.addClass("commonTableTrHoverBg");
			},function(){
				tableTr.removeClass("commonTableTrHoverBg");
			});
		}
		
		if(_this.selectMode == "checkbox"){
			var _selectTd = $("<td class='uploadFileTableHeader commonTableBorder'></td>");
			var _selectTdCheckBox = $("<input isChildRow=" + isCreateChildRow + " type='checkbox' name='commonTable_selectDetail'/>");
			_selectTd.append(_selectTdCheckBox);
			tableTr.append(_selectTd);
		}
		if(_this.isTreePanel){
			var treeTd = $('<td class="commonTableBorder"></td>');
			tableTr.append(treeTd);
			var treeTd_a = $('<a href="#"></a>');
			treeTd.append(treeTd_a);
			var treeTd_a_img = $('<img treeImg=true rowIndex="' + currentCreateRowIndex + '" src="' + baseUrl + '/scripts/lib/jquery/ztree/zTreeStyle/img/plus_noLine.gif" width="18" height="18" align="absmiddle"  />');
			treeTd_a.append(treeTd_a_img);
			if(_rowData.childrens != null){
				treeTd_a_img.bind("click",function(){
					if($(this).attr("isOpen") == null){
						$(this).attr("isOpen","true");
						if(_this.store[$(tableTr).attr("rowIndex")].childrens != null){
							_this.createChildrenRows(tableTr,_this.store[$(tableTr).attr("rowIndex")].childrens);
						}
					}
				
					if($(this).attr("isOpen") == "true"){
						$(this).attr("src",baseUrl + "/scripts/lib/jquery/ztree/zTreeStyle/img/minus_noLine.gif");	
						$(this).attr("isOpen","false");
						_this.commonTable.find("tr[isDataRow=true][isChildRow=true][rowIndex="+$(tableTr).attr("rowIndex")+"]").show();
					}else if($(this).attr("isOpen") == "false"){
						$(this).attr("src",baseUrl + "/scripts/lib/jquery/ztree/zTreeStyle/img/plus_noLine.gif");	
						$(this).attr("isOpen","true");
						_this.commonTable.find("tr[isDataRow=true][isChildRow=true][rowIndex="+$(tableTr).attr("rowIndex")+"]").hide();
					}
					
				});
				
			}else{
				treeTd_a_img.attr("src",baseUrl + "/scripts/lib/jquery/ztree/zTreeStyle/img/page.gif");		
			}
			
			
			
		}
		
		for(var j=0;j<_this.columns.length;j++){
			if(_this.columns[j].xtype == "hidden"){
				tableTr.append(createTdContent(_this.columns[j],_rowData,tableTr,isCreateChildRow));
			}else{
				_this.columns[j].align = (_this.columns[j].align == null ? "left" : _this.columns[j].align);
				var tableTd = $("<td align='" + _this.columns[j].align + "' class='commonTableBorder commonTableColumn_td_style'></td>");
				tableTd.append(createTdContent(_this.columns[j],_rowData,tableTr,isCreateChildRow));
				tableTr.append(tableTd);
			}
		}
		if(isCreateChildRow != true){
			currentCreateRowIndex = currentCreateRowIndex + 1;
		}
		return tableTr;
	}
	
	this.createChildrenRows = function(parentTr,childrenDatas){
		for(var i=0;i<childrenDatas.length;i++){
			afterElement = ($(parentTr).parent().find("tr[isDataRow=true][rowIndex='" + $(parentTr).attr("rowIndex") + "']").last());
			var childTr = createRow(childrenDatas[i],true);
			childTr.attr("rowIndex",$(parentTr).attr("rowIndex"));
			var rowNum = $(parentTr).find("div[isRowNum=true]").eq(0).attr("divRowNum");
			childTr.find("div[isRowNum=true]").children().eq(0).attr("title",rowNum);
			childTr.find("div[isRowNum=true]").children().eq(0).html(rowNum);
			childTr.attr("childRowIndex",i);
			$(afterElement).after(childTr);
		}
		columnEllipsis();
	}
	
	this.createChildrenBySelectParent = function(childrenDatas){
		var selectedCheckboxs = this.commonTable.find("input[isChildRow=false][type='checkbox'][name='commonTable_selectDetail']:checked");	
		if(selectedCheckboxs.length == 0){
			base.alert("请选择父节点！");
		}else if(selectedCheckboxs.length > 1){
			base.alert("不能选择多行来创建子节点！");
		}else{
			var curr_tr = selectedCheckboxs.eq(0).parent().parent();
			this.createChildrenRows($(curr_tr),childrenDatas);
		}
		
	}
	
	this.createRows = function(rowDatas){
		this.store = this.store.concat(rowDatas);
		for(var i=0;i<rowDatas.length;i++){
			var tableTr = createRow(rowDatas[i]);
			commonTableAppendTr(tableTr);
		}
		columnEllipsis();
		if(this.expand){
			this.expendAll();
		}
	}
	
	
	this.setValues = function(data){
		removeTableStore();
		this.createRows(data);
	}
	
	this.showTable = function(data){
		for(var i=0;i<this.columns.length;i++){
			if(this.columns[i].xtype != "hidden"){
				this.columns[i].xtype = "label";
			}
		}
		this.setValues(data);
	}
	
	
	this.getValues = function(){
		var resultData = [];
		var _dataRows = this.commonTable.find("tr[isDataRow=true][isChildRow=false]");
		for(var i=0;i<_dataRows.length;i++){
			resultData.push(this.getRowValue(i));
		}
		return resultData;
	}
	
	
	
	this.getSelectedValues = function(selected){
		return _getSelectedValues(selected,false);
	}
	
	this.getSelectedChildValues = function(selected){
		return _getSelectedValues(selected,true);
	}
	
	
	
	this.getRowValue = function(rowIndex){
		var _dataRow = this.commonTable.find("tr[isDataRow=true][isChildRow=false]:eq(" + rowIndex + ")");
		var rowMap = {};
		for(var j=0;j<this.columns.length;j++){
			rowMap = putRowMapValue(rowMap,this.columns[j],_dataRow);
		}
		
		if(this.isTreePanel){
			rowMap.childrens = this.getChildValues(rowIndex);
			
		}
		return rowMap;
	}
	
	this.getChildRowValue = function(rowIndex,childRowIndex){
		var _childDataRow = this.commonTable.find("tr[isDataRow=true][isChildRow=true][rowIndex='" + rowIndex + "']:eq(" + childRowIndex + ")");
		var rowMap = {};
		for(var j=0;j<this.columns.length;j++){	
			rowMap = putRowMapValue(rowMap,this.columns[j],_childDataRow);
			
			
			
		}
		return rowMap;
	}
	
	this.getChildValues = function(rowIndex){
		var resultData = [];
		var _childDataRows = this.commonTable.find("tr[isDataRow=true][isChildRow=true][rowIndex='" + rowIndex + "']");
		for(var i=0;i<_childDataRows.length;i++){
			resultData.push(this.getChildRowValue(rowIndex,i));
		}
		return resultData;
	}
	
	this.deleteSelectedRows = function(){
		var selectedCheckboxs = _this.commonTable.find("input[type='checkbox'][name='commonTable_selectDetail']:checked");
		for(var i=0;i<selectedCheckboxs.length;i++){
			var curr_tr = selectedCheckboxs.eq(i).parent().parent();
			if(eval(curr_tr.attr("isChildRow")) == false){
				var _childDataRows = this.commonTable.find("tr[isDataRow=true][isChildRow=true][rowIndex='" + curr_tr.attr("rowIndex") + "']");
				for(var j=0;j<_childDataRows.length;j++){
					
					_childDataRows.eq(j).remove();
				}
				
				if( curr_tr.find("div[isRowNum=true]").length != 0 ){
					var curr_del_rowNum = curr_tr.find("div[isRowNum=true]").eq(0).attr("divRowNum");
					$.each(_this.commonTable.find("div[isRowNum=true]"),function(_index,_element){
						if(parseInt($(_element).attr("divRowNum"))>parseInt(curr_del_rowNum)){
							var new_rowNum = parseInt($(_element).attr("divRowNum"))-1;
							$(_element).attr("divRowNum",new_rowNum);
							$(_element).find("nobr").attr("title",new_rowNum);
							$(_element).find("nobr").text(new_rowNum);
						}
					});
					currentRowNumber--;
				}
				
			}
			curr_tr.remove();
		}	
	}
	
	this.expendAll = function(){
		this.commonTable.find("img[treeImg=true][isOpen!=false]").trigger("click");
	}
	
	createTable();
	
	CommonEditTable.items[this.tableId] = this;
}


CommonEditTable.items = {};
$(function(){
	$("div[class*='CommonEditTable']").each(function(i,element){
		var defaults = $(this).attr("defaults");
		if(defaults==null||defaults==""){
			defaults = "{}";
		}
		eval("var defaults ="+defaults);
		defaults.tableIndex = i;
		defaults.renderToDiv= $(element);
		defaults.tableId = $(this).attr("id");
		var obj = new CommonEditTable(defaults);
	});
})
