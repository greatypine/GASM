//导入当前Excel文件
var formsubmit = function(){
    if ($("#reportfile").val() == '') {
        $$.showMessage('提示', '请选择上传的Excel文件。');
        return false;
    }
	//得到基础信息
	
	//上传Url
    var url = baseUrl + '/expInfoUpload.action?businessType=excel&businessId=151515&' + 
		'requestString={"managerName":"excelImportManager","methodName":' +
		'"importExcelData","parameters":[{"executeType":"byLine"}]}';
					
	doExcelImport(url);
    //阻止表单提交
    return false;
};

//导入当前Excel文件
var importBudget = function(){
    if ($("#reportfile").val() == '') {
        $$.showMessage('提示', '请选择上传的Excel文件。');
        return false;
    }
	//得到基础信息
	
	//上传Url
    var url = baseUrl + '/expInfoUpload.action?businessType=excel&businessId=151515&' + 
		'requestString={"managerName":"excelImportManager","methodName":' +
		'"importBudgetData","parameters":[{"executeType":"byLine"}]}';
					
	doExcelImport(url);
    //阻止表单提交
    return false;
};

// 执行Excel文件导入
function doExcelImport(url){
    var options = {
        dataType: 'json',
        beforeSubmit: showRequest,
        //success: showResponse,
        url: url
	};
    $("#uploadForm").ajaxSubmit(options);
}

//显示返回信息
function showRequest(formData, jqForm, options){
    var queryString = $.param(formData);
    return true;
}

function showResponse(responseText, statusText){
	var str = "";
	var dataObj = eval("("+responseText.data+")");
	$.each(dataObj,function(idx,item){
		if (item.errors != null && item.errors.length!=0) {
			str+=item.sheetName+"(导入模板错误！)<br/>"
		}else{
			var counter = item.counter;
			var importedCounter = item.importedCounter;
			var failedRecords = item.failedRecords;
			if(checkError(item.sheetName)){
				str+=item.sheetName+"(报表模板数据： "+counter+" 条;已经上传数据： "+importedCounter+" 条;存在问题数据： "+failedRecords.length+" 条)<br/>";
			}else{
				str+=item.sheetName+"(导入成功！)<br/>"
			}
		}
		
	});
		
	repAlert("提示", str, 500, 400, true);
			
	//得到列表
	var ctable=document.getElementById("uploadReportList");
	//处理返回后的颜色
	var i=0;
	for (var identity in icheckbox) 
	{	var cinput = icheckbox[identity];
		if(cinput.checked){
			var sheetsname = isheetsname[i];
			try {
				$.each(dataObj,function(idx,item){
					if(item.sheetName==sheetsname){
						if(checkError(sheetsname)){
							if(ctable && (item.failedRecords.length>0 || (item.errors != null && item.errors.length!=0))){
								editTable(ctable,dataObj,i);
							}
							
						}else {
							if(ctable && (item.errors != null && item.errors.length!=0)){
								editTable(ctable,dataObj,i);
							}
							
						}
						
//						if(ctable)
//						{
//							var row = ctable.rows[i];
//							var cinput = row.getElementsByTagName("td")[2];
//							cinput.innerHTML = '<font style="color:red;">' + cinput.innerText + "</font>";
//							var cinput3 = row.getElementsByTagName("td")[3];
//							cinput3.innerHTML = '<font style="color:red;">' + cinput3.innerText + "</font>";
//							
//							$("#uploadReportList tr td:nth-child(4)").click(function(){
//								showDataInfo(dataObj, $(this).text());
//							});
//								
//							return false;
//						}
					}
				});
			}catch(e){}
			i++;
		}else{i++;}}
}

//显示返回信息
function showDataInfo(dataObj, sheetName){
	var strerror="";
	$.each(dataObj,function(idx,item){
		if(sheetName==item.sheetName){
			strerror+="报表《"+sheetName+"》<br/>"
			if (item.errors != null && item.errors.length != 0) {
				strerror+="导入的模板错误。"
			}else{
				strerror+="报表模板数据： "+item.counter+" 条<br/>已经上传数据： "+item.importedCounter+" 条<br/>存在问题数据： "+item.failedRecords.length+" 条";
				if(item.failedRecords!=null && item.failedRecords.length>0){
					strerror+="<br/>问题数据详细信息如下：";
					for(var i=0;i<item.failedRecords.length;i++){
						var recode = item.failedRecords[i];
						strerror+="<br/> "+(i+1)+". "+recode;
					}
				}
			}
			$("#errorTextarea").html(strerror);
			return false;
		}
	});
	
}

function editTable(ctable,dataObj,idx){
	var row = ctable.rows[idx];
	var cinput = row.getElementsByTagName("td")[2];
	cinput.innerHTML = '<font style="color:red;">' + cinput.innerText + "</font>";
	var cinput3 = row.getElementsByTagName("td")[3];
	cinput3.innerHTML = '<font style="color:red;">' + cinput3.innerText + "</font>";
								
	$("#uploadReportList tr td:nth-child(4)").click(function(){
		showDataInfo(dataObj, $(this).text());
	});
	return false;
}

function checkError(sheetName){
	var ok = false;
	var arrReport = ['供应商交易情况','供应商交货情况','可供外调一级物资情况','进口机电产品采购'];
	for(var i=0;i<arrReport.length;i++){
		if(sheetName == arrReport[i]){
			ok=true;
			break;
		}
	}
	return ok;
}