var multiEditJq;
var souceTableID;
var operation; //edit、add、del
var multiEditTableInitPara = {};
$(document).ready(function () {
    $("body").append($("<div name='divMultiEdit' id='divMultiEdit'></div>"));
});

function setSltRowEditable(table) {
    var $t = $(table);
    var selectedIds = $t.jqGrid("getGridParam", "selarrrow");
    selectedIds = $t.jqGrid("getGridParam", "selarrrow");
}

//获取指定表格中选中复选框的行id
function getMultiSltLineIds(tableId) {
    var $t = $("#" + tableId);
    return $t.jqGrid("getGridParam", "selarrrow");
}

//获取指定表格中选中复选框的数据
function getMultiSltLineData(tableId) {
    var selectedRowIds = getMultiSltLineIds(tableId);
    if (selectedRowIds.length) {
        var result = [];
        for (var i = 0; i < selectedRowIds.length; i++) {
            result.push($("#" + tableId).jqGrid('getRowData', selectedRowIds[i]));
        }
        return result;
    }
    return [];
}

/*
废弃不用
*/
function delSltLines(tableId) {
    var selectedRowIds = getMultiSltLineIds(tableId);
    if (selectedRowIds.length) {
        /*selectRowIds是一个指向选中行数组的一个引用，在用 jqgrid删除数据时会动态的变化;
         将数组的长度赋给一个变量，则循环的条件就不会变化了 ;
         每次都取第 0个数据*/
        var len = selectedRowIds.length;
        for (var i = 0; i < len; i++) {
            $("#" + tableId).jqGrid("delRowData", selectedRowIds[0]);
        }
    }
}

//获取div层,如果没有则创建
function getMultiEditDiv() {
    $("#divMultiEdit").empty();

    div = $("#divMultiEdit");
    div.append($("<table name='tableMultiEdit' id='tableMultiEdit'></table>"));
    div.append($("<div name='divMultiEditFoot' id='divMultiEditFoot'></div>"));
    div.append($("<input type='button' value='保存' name='btnSave' id='btnSave' onclick='doSave()'></input>"));
    div.append($("<input type='button' value='取消' name='btnCancel' id='btnCancel' onclick='closeDiv()'></input>"));

    div.hide();
    return div;
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

function doEdit(tableName, tableInitPara) {
    souceTableID = tableName;
    var div = getMultiEditDiv();
    $.extend(tableInitPara, {
        cellEdit: true,
        cellsubmit: 'clientArray',
        multiselect: false,
		ondblClickRow:function(rowid,iRow, iCol, e){
			showRichTxtEditDialog(rowid,iRow, iCol, e);
		}
    }); //按单元格编辑
    var data = getMultiSltLineData(tableName);
    if (data.length == 0) {
        alert("请选择需要编辑的行");
        return;
    }
    multiEditJq = jQuery("#tableMultiEdit").jqGrid(tableInitPara);
    multiEditJq.jqGrid("clearGridData", true); //清除数据和表格脚
    var JsonData = {
        "records": data.length,
        "rows": data
    };
    $("#tableMultiEdit")[0].addJSONData(JsonData);
    setCellLostFocus($("#tableMultiEdit")[0]);
    showDiv();
}

function doDel(tableName) {
    souceTableID = tableName;
    var delData = getMultiSltLineData(tableName);
    if (delData.length == 0) {
        alert("请选择需要删除的行");
        return;
    }
    if (!confirm('确实要删除选中的' + delData.length + '条记录吗?'))
        return;
    var url = $("#" + tableName).jqGrid("getGridParam", "editurl");
    var postDataExtra = $("#" + tableName).jqGrid("getGridParam", "postData");
    var postData = $.extend({
        data: JSON.stringify(delData)
    }, postDataExtra);
    postData = $.extend(postData, {
        "oper": operation
    });
    $.ajax({
        url: url,
        type: 'POST',
        async: false,
        data: postData,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            refreshSaveData(data.data, operation);
            showErr(data.err);
        },
        error: function (data, textStatus, jqXHR) {
            alert(textStatus);
        }
    });
}

function doAdd(tableName, tableInitPara) {
    souceTableID = tableName;
    var div = getMultiEditDiv();
    $.extend(tableInitPara, {
        cellEdit: true,
        cellsubmit: 'clientArray',
        multiselect: false,
		ondblClickRow:function(rowid,iRow, iCol, e){
			showRichTxtEditDialog(rowid,iRow, iCol, e);
		}
    }); //按单元格编辑
    multiEditJq = jQuery("#tableMultiEdit").jqGrid(tableInitPara);
    multiEditJq.jqGrid("clearGridData", true); //清除数据和表格脚
    multiEditJq.jqGrid("addRowData", "keyId", [
        { "keyId": "newRow1"},
        { "keyId": "newRow2"},
        { "keyId": "newRow3"},
        { "keyId": "newRow4"},
        { "keyId": "newRow5"}
    ]);
    setCellLostFocus($("#tableMultiEdit")[0]);
    showDiv();
}

function showDiv() {
    jQuery.fancybox([$("#divMultiEdit")], {
        type: 'html',
        autoSize: false,
        width: 800,
        height: 600,
        modal: true
    });
    multiEditJq.jqGrid("setGridWidth", 750);
}

function doSave() {
//    var changeLineData = multiEditJq.jqGrid("getChangedCells"); //获取修改的行的数据，不可编辑字段也能拿到
    var changeLineData = getChangeLinesData("tableMultiEdit"); //获取修改的行的数据，不可编辑字段也能拿到
    if (changeLineData.length == 0) {
        closeDiv();
        return;
    }

    var editurl = multiEditJq.jqGrid("getGridParam", "editurl");
    var postDataExtra = multiEditJq.jqGrid("getGridParam", "postData");
    var postData = $.extend({
        data: JSON.stringify(changeLineData),oper: operation
    }, postDataExtra);
    $.ajax({
        url: editurl,
        type: 'POST',
        async: false,
        data: postData,
        dataType: "json",
        success: function (data, textStatus, jqXHR) {
            refreshSaveData(data.data, operation);
			var err = data.err;
			if(err.length == 0){
	            closeDiv();
			}else{
	            showErr(data.err);
			}
        },
        error: function (data, textStatus, jqXHR) {
            alert(textStatus);
        }
    });
}

function closeDiv() {
    multiEditJq.jqGrid("clearGridData", true); //清除数据和表格脚
    jQuery.fancybox.close();
}

function buildMultiToolBar(tableName, pageDivName, tableInitPara, addFlag, editFlag, delFlag) {
    add = $.type(arguments[3]) === 'undefined' ? true : arguments[3];
    edit = $.type(arguments[4]) === 'undefined' ? true : arguments[4];
    del = $.type(arguments[5]) === 'undefined' ? true : arguments[5];
	multiEditTableInitPara[tableName] = tableInitPara;
    if (add) {
        $("#" + tableName).jqGrid('navGrid', "#" + pageDivName).jqGrid('navButtonAdd', "#" + pageDivName, {
            caption: "添加",
            title: "title_Add",
            buttonicon: "ui-icon-plusthick",
            id: "btn_multiAdd",
            onClickButton: function () {
                operation = 'add';
                doAdd(tableName, tableInitPara);
            }
        });
    }

    if (edit) {
        $("#" + tableName).jqGrid('navGrid', "#" + pageDivName).jqGrid('navButtonAdd', "#" + pageDivName, {
            caption: "编辑",
            title: "title_Edit",
            buttonicon: "ui-icon-pencil",
            id: "btn_multiEdit",
            onClickButton: function () {
                doEdit(tableName, tableInitPara);
                operation = 'edit';
            }
        });
    }

    if (del) {
        $("#" + tableName).jqGrid('navGrid', "#" + pageDivName).jqGrid('navButtonAdd', "#" + pageDivName, {
            caption: "删除",
            title: "title_Del",
            buttonicon: "ui-icon-minusthick",
            id: "btn_multiDel",
            onClickButton: function () {
                operation = 'del';
                doDel(tableName);
            }
        });
    }
}

/*
将服务器回传的数据更新到原始表格对应的行列中
*/
function refreshSaveData(dataList, opType) {
    $.each(dataList, function (n, value) {
        var keyId = value.keyId;
        if ("edit" == opType) {
			var colModel = multiEditTableInitPara[souceTableID].colModel;
			//由于jqgrid对td内部嵌套表格支持不好，用setRowData会导致数据错乱，因此自己实现设置单元格的值，只设置编辑过的列
			for(var i=0;i<colModel.length;i++){
				 var colName = colModel[i].name;
				 setCellValue(souceTableID,keyId,colName,value[colName]);
			}
			multiEditJq.jqGrid('delRowData', keyId);//从多行编辑表中移除已经成功的数据
        }
        else if ("del" == opType) {
            $("#" + souceTableID).jqGrid('delRowData', keyId);
        }
        else if ("add" == opType) {
            $("#" + souceTableID).jqGrid('addRowData', keyId, value);
			multiEditJq.jqGrid('delRowData', value.addOpOrgKeyID);//从多行编辑表中移除已经成功的数据
        }
    });
}

function showErr(errList) {
    var result = "";
    $.each(errList, function (n, value) {
        result += value + "\n";
    });
    if (result != "")
        alert(result);
}

/*
列名是不是在预设的colModel中，废弃不用
*/
function isColNameInInitPara(colName){
	var colModel = multiEditTableInitPara[souceTableID].colModel;
	for(var i=0;i<colModel.length;i++){
		if(colModel[i].name == colName)
			return true;
	}
	return false;
}

/*
设定指定表指定单元格的值
*/
function setCellValue(tableID, rowID, colName, value){
	var table = $("#" + tableID);
	var tr = $("#" + tableID + ">tbody").children("tr").filter("#" + rowID);
	var sltColName = 'aria-describedby=' + tableID+'_'+colName
	var td = tr.children("td").filter("[" + sltColName + "]");
	td.html(value);
}

/*
对表格的某行做标记，表明已被更改，保存时将向服务器提交
*/
function setRowEdited(tableId, rowID){
	var tr = $("#" + tableId + ">tbody").children("tr").filter("#" + rowID);
	tr.addClass("edited");
}

/*
由于jqgrid对单元格内嵌表格支持的不好，getChangedCells不能用
*/
function getChangeLinesData(tableId){
	var trs = $("#" + tableId + ">tbody").children("tr").filter(".edited");
	var result = [];
	$.each(trs,function(n, tr){
		var tds = $(tr).children("td");
		var trMap = {};
		$.each(tds,function(j,td){
			var aria = $(td).attr("aria-describedby");
			var colName = aria.substr(aria.lastIndexOf("_") + 1);
			var content = $(td).html();
			if("&nbsp;" == content)
				content = "";
			trMap[colName] = content;
		});
		result.push(trMap);
	});
	return result;
}
