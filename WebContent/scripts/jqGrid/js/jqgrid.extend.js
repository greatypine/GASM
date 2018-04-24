/**
 * 扩展jqgrid方法
 */

var multiEditTablePara = {};
var multiEditJq;

$.jgrid.extend({
	isEmptyRow : function(rowId) {//因为在cell edit模式下，dirty cell才可能是被编辑过。只有有值的dirty cell才不是空行
		var valMap = this.getRowData(rowId);
		var rs = true;
		var table = this;
		$.each(valMap,function(key,val){     
			if(table.isDirtyCell(rowId,key)){
				if("&nbsp;" == val)
					val = "";
				
				if($.trim(val) != "")
					rs = false;
			}
		 });
		return rs;
	},
	isDirtyCell:function(rolId, colName){
		var cellAria = $(this).getTableId() + "_" + colName;
		return $(this).find("tr#"+rolId).find("td[aria-describedby='" + cellAria + "']").hasClass("dirty-cell");
	},
	getTableId:function(){
		return $(this).attr("id");
	},
	getEditableRowIds:function(){//用于inline 模式下获取所有可编辑的行编号
		var rs = [];
		this.find("tr[editable='1']").each(function(i){
			rs.push($(this).attr("id"));
		});
		return rs;
	}
});

function getObj(objOrID){
	if(typeof(objOrID) == "string" ){
		return $("#" + objOrID);
	}
	return $(objOrID);
}

function setSelectedRowEditable(table){
	table = getObj(table);
	var sltRowsNums = getSltedRows(table);
	if (sltRowsNums.length) {
        for (var i = 0; i < sltRowsNums.length; i++) {
        	table.jqGrid('editRow',sltRowsNums[i]);
        }
    }
}

function getSltedRows(table){
	table = getObj(table);
	return table.jqGrid("getGridParam", "selarrrow");
}

//获取指定表格中选中复选框的数据
function getMultiSltLineData(tableId) {
    var selectedRowIds = getSltedRows(tableId);
    if (selectedRowIds.length) {
        var result = [];
        for (var i = 0; i < selectedRowIds.length; i++) {
            result.push($("#" + tableId).jqGrid('getRowData', selectedRowIds[i]));
        }
        return result;
    }
    return [];
}

//获取已选择的行中可编辑的数据
function getSltedEditableData(table){
	table = getObj(table);
	var sltRowsIds = table.jqGrid("getEditableRowIds");
	var result = [];
	if (sltRowsIds.length) {
        for (var i = 0; i < sltRowsIds.length; i++) {
        	var tr = $("#" + sltRowsIds[i]);
        	var tds = tr.children("td[title]").has(".editable");//复选框列没有title属性
        	var editableCells = [];
        	$.each(tds,function(j,td){
        		editableCells.push($($(this).children(".editable")[0]).attr("name"));//记录下所有可编辑的框的列名
    		});
        	table.jqGrid('saveRow',sltRowsIds[i],false, 'clientArray');//clientArray 不保存到服务器
        	var vals = {};
        	for(var j=0;j<editableCells.length;j++){
        		vals[editableCells[j]] = table.jqGrid("getCell",sltRowsIds[i],editableCells[j]);
        	}
        	if(!$.isEmptyObject(vals)){
        		result.push(vals);
        	}
        }
    }
	return result;
}

function getMultiEditDiv(){
	if($("#divMultiEdit").length <= 0){
		$("body").append($("<div name='divMultiEdit' id='divMultiEdit'></div>"));
	}else{
		$("#divMultiEdit").empty();
	}

	    div = $("#divMultiEdit");
	    div.append($("<table name='tableMultiEdit' id='tableMultiEdit'></table>"));
	    div.append($("<div name='divMultiEditFoot' id='divMultiEditFoot'></div>"));
	    div.append($("<input type='button' value='保存' name='btnSave' id='btnSave' onclick='saveAdd()'></input>"));
	    div.append($("<input type='button' value='取消' name='btnCancel' id='btnCancel' onclick='closeDiv()'></input>"));

	    div.hide();
	    return div;
}


/**
 * 新增多行时保存数据
 */
function saveAdd(){
	var ids = $("#tableMultiEdit").jqGrid("getDataIDs");
	var rs = [];
	$.each(ids,function(i){
		if(!$("#tableMultiEdit").jqGrid("isEmptyRow", this)){
			rs.push($("#tableMultiEdit").jqGrid("getRowData",this));
		}
	});
	
	if(rs.length == 0){
		alert("请填写数据");
		return;
	}
	doManager(multiEditTablePara.svrClsName, multiEditTablePara.svrMethodName, rs, function(datas, textStatus, XMLHttpRequest){
		alert("回调要处理什么呢？");
		closeDiv();
	}, true, {
		showWaiting: true
	});
}

//完全复制表格到div层中，但是所有单元格可编辑
function doAdd(tableName,svrClsName,svrMethodName,rowsCount){
	var tableObj = getObj(tableName);
	var tableInitPara = tableObj.getGridParam("tableInitPara");
	getMultiEditDiv();//初始化div
	tableInitPara = $.extend(true, {}, tableInitPara);  //克隆一份参数
	$.extend(tableInitPara, {
        cellEdit: true, //按单元格编辑
        cellsubmit: 'clientArray',//单元格保存时不提交
        multiselect: false,
        height:"auto",
        loadComplete: function() {}
    }); 
	setTableAllEditable(tableInitPara);
	multiEditJq = $("#tableMultiEdit").jqGrid(tableInitPara);
	showDiv();
	
    if(isNaN(rowsCount))
    	rowsCount = 10;
    	
    setTimeout("addLine(" + rowsCount + ")" , 200);//jquery1.5和插件有冲突，马上添加行不行，必需延时添加
    
    multiEditTablePara.svrClsName = svrClsName;
    multiEditTablePara.svrMethodName = svrMethodName;
    setCellLostFocus($("#tableMultiEdit")[0]);
    
}

//jquery1.5和插件有冲突，马上添加行不行，必需延时添加
function addLine(rowsCount){
	for(var i=0; i<rowsCount;i++){
    	multiEditJq.jqGrid("addRowData", "id", [{"id": "newRow" + i}]);
    }
}

function doEdit(tableName) {
	var rowIds = getSltedRows(tableName);
    if (rowIds.length == 0) {
        alert("请选择需要编辑的行");
        return;
    }
    setSelectedRowEditable(tableName);
    $("#btn_multiSave").removeClass('ui-state-disabled');
    $("#btn_cancleEdit").removeClass('ui-state-disabled');
    $("#btn_multiEdit").addClass('ui-state-disabled');
}

function doSaveEdit(tableName,svrClsName,svrMethodName){
	$("#btn_multiSave").addClass('ui-state-disabled');
    $("#btn_cancleEdit").addClass('ui-state-disabled');
    $("#btn_multiEdit").removeClass('ui-state-disabled');
    
    var data = getSltedEditableData(tableName);
    doManager(svrClsName, svrMethodName, data, function(datas, textStatus, XMLHttpRequest){
		alert("回调要处理什么呢？");
	}, true, {
		showWaiting: true
	});
}

function doCancleEdit(tableName){
	var table = getObj(tableName);
	var rowIds = table.jqGrid("getEditableRowIds");
	for(var i=0;i<rowIds.length;i++){
		table.jqGrid("restoreRow",rowIds[i]);
	}
	$("#btn_multiSave").addClass('ui-state-disabled');
    $("#btn_cancleEdit").addClass('ui-state-disabled');
    $("#btn_multiEdit").removeClass('ui-state-disabled');
}

function doDel(tableName, svrClsName, svrMethodName) {
    souceTableID = tableName;
    var delData = getSltedRows(tableName);
    if (delData.length == 0) {
        alert("请选择需要删除的行");
        return;
    }
    if (!confirm('确实要删除选中的' + delData.length + '条记录吗?'))
        return;
    doManager(svrClsName, svrMethodName, delData, function(datas, textStatus, XMLHttpRequest){
    	if (datas.result) {
    		getObj(tableName).trigger('reloadGrid');;
    	}
    	else {
    		$$.showMessage("系统提示", datas.message);
    	}
    }, true, {
    	showWaiting: true
    });
}

//表格中所有可见列可编辑
function setTableAllEditable(tableInitPara){
	var cms = tableInitPara.colModel;
	for(var i=0; i<cms.length; i++){
		var cm = cms[i];
		if(cm.hidden != true){
			cm.editable = true;
		}
	}
}

function showDiv() {
	$.fancybox([$("#divMultiEdit")], {
        type: 'html',
        autoSize: false,
        width: 800,
        height: 600,
        modal: true
    });
    multiEditJq.jqGrid("setGridWidth", 750);
}

function closeDiv() {
    multiEditJq.jqGrid("clearGridData", true); //清除数据和表格脚
    $.fancybox.close();
}

//单元格编辑时失去焦点，自动保存单元格数据，减少出错。自定义单元格进入编辑时，默认获取焦点
function setCellLostFocus(table) {
    $(table).setGridParam({
        afterEditCell: function (id, name, val, iRow, iCol) {
            if (!$("#" + iRow + "_" + name).is(":focus")) {
                $("#" + iRow + "_" + name).focus();
            }
            $("#" + iRow + "_" + name).bind('blur', function () {
                $(table).saveCell(iRow, iCol);
            });
        }
    });
}

/**
 * 产生指定的随机数
 * @param start
 * @param end
 * @returns
 */
function rnd(start, end){
    return Math.floor(Math.random() * (end - start) + start);
}

var jqGridToolbar = function(tableName, pageDivName){
	this.tableObj = getObj(tableName);
	this.addBtnOpt = $.extend(true, {},{
            caption: "添加",
            title: "title_Add",
            buttonicon: "ui-icon-plusthick",
            id: "btn_multiAdd",
            onClickButton: function(){}
        });
	this.delBtnOpt = $.extend(true, {},{
	        caption: "删除",
            title: "title_Del",
            buttonicon: "ui-icon-minusthick",
            id: "btn_multiDel",
            onClickButton: function () {}
        });
	this.cancleBtnOpt = $.extend(true, {},{
        	caption: "放弃",
        	title: "title_cancle",
        	buttonicon: "ui-icon-cancle",
        	id: "btn_cancleEdit",
        	onClickButton: function () {
        		doCancleEdit(tableName);
        	}
        });
	this.saveBtnOpt =$.extend(true, {},{caption: "保存",
        	title: "title_Save",
        	buttonicon: "ui-icon-disk",
        	id: "btn_multiSave",
        	onClickButton: function () {}
        });
	this.editBtnOpt = $.extend(true, {},{
            caption: "编辑",
            title: "title_Edit",
            buttonicon: "ui-icon-pencil",
            id: "btn_multiEdit",
            onClickButton: function () {}
            });
	
	//为表格增加按钮，btnClkFn为点击按钮的事件
	this.addBtnToTable = function (btnOpt,btnClkFn){
		if(typeof(btnClkFn) != "undefined")
			btnOpt.onClickButton = btnClkFn;
		var id = btnOpt.id;
		if($("#"+id).length > 0){//id不能重复
			id = id + rnd(1,1000);
			btnOpt.id = id;
		}
		this.tableObj.jqGrid('navGrid', "#" + pageDivName).jqGrid('navButtonAdd', "#" + pageDivName,btnOpt);
	};
	
	this.addDefaultAddBtn = function(svrClsName, svrMethodName, rowsCount){
		this.addBtnToTable(this.addBtnOpt,function(){doAdd(tableName,svrClsName,svrMethodName,rowsCount);});
	};
	
	this.addDefaultDelBtn = function(svrClsName, svrMethodName){
        this.addBtnToTable(this.delBtnOpt,function(){doDel(tableName,svrClsName,svrMethodName);});
    };
    this.addDefaultEditBtn = function(svrClsName, svrMethodName){
        this.addBtnToTable(this.editBtnOpt,function(){doEdit(tableName);});
        this.addBtnToTable(this.saveBtnOpt,function(){doSaveEdit(tableName,svrClsName,svrMethodName);});
        this.addBtnToTable(this.cancleBtnOpt,function(){doCancleEdit(tableName);});
        $("#btn_multiSave").addClass('ui-state-disabled');
        $("#btn_cancleEdit").addClass('ui-state-disabled');
    };

    //各个Para为false，或如下格式{svrClsName:"svrClsName",svrMethodName:"svrMethodName"},add按钮可以配置rowsCount参数
    this.addDefaultEditBar = function(addPara, editPara, delPara){
        if(addPara != false)
            this.addDefaultAddBtn(addPara.svrClsName,addPara.svrMethodName,addPara.rowsCount);
        if(editPara != false)
            this.addDefaultEditBtn(addPara.svrClsName,addPara.svrMethodName);
        if(delPara != false)
            this.addDefaultDelBtn(addPara.svrClsName,addPara.svrMethodName);
    };
};