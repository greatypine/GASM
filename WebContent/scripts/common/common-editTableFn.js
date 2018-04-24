function EditTableFn(){
	this.staticStr=''; //固定值字符串填写
	this.formulaDefined=''; //公式定义
	this.fixedItemOutput=''; //固定项输出
}
EditTableFn.prototype = {
	importStaticStr:function(o,validation,isreadonly){
		var tableDiv = $("<div></div>");
		var table = $("<table></table>");
		tableDiv.append(table);
		//table.id = "table2";
		table.attr("id","table2");
		table.attr("type","0001");
		if(isreadonly != true){
			var tr = $("<tr style='display: none;'></tr>");
			var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' onBlur='"+validation+"(this)' ><span style='color:red;'></span> = </td>");
			var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px'><span style='color:red;'></span></td>");
			var td3 = $("<td></td>");
			tr.append(td1);
			tr.append(td2);
			tr.append(td3);
			table.append(tr);
		}
		if(typeof(o) != 'undefined' && o != null && o.length != 0){
			for(var i=0;i<o.length;i++){
				var tr = $("<tr></tr>");
				if(isreadonly == true){
					var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' value='"+o[i].key+"' readonly><span style='color:red;'></span> = </td>");
					var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px' value='"+o[i].value+"' readonly><span style='color:red;'></span></td>");
					tr.append(td1);
					tr.append(td2);
				}else{
					if(o[i].status == '1'){
						var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px;background-color:#EEFFFF' onchange='"+validation+"(this)'  value='"+o[i].key+"'><span style='color:red;'></span> = </td>");
						var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px;background-color:#EEFFFF'  value='"+o[i].value+"'><span style='color:red;'></span></td>");
						tr.append(td1);
						tr.append(td2);
					}else{
						var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' onchange='"+validation+"(this)'  value='"+o[i].key+"'><span style='color:red;'></span> = </td>");
						var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px'  value='"+o[i].value+"'><span style='color:red;'></span></td>");
						tr.append(td1);
						tr.append(td2);
					}
					
				}
				var td3 = $("<td></td>");
				tr.append(td3);
				table.append(tr);
			}
		}
		if(isreadonly != true){
			var addInput = $("<input type='button' value='新增' onclick=fnTools.createTr('table2')>");
		    table.append(addInput);
		}
		this.staticStr = tableDiv.html();
	},
	importFormulaDefined:function(o,validation,isreadonly){
		var tableDiv = $("<div></div>");
		var table = $("<table></table>");
		tableDiv.append(table);
		table.attr("id","table3");
		table.attr("type","0002");
		if(isreadonly != true){
			var tr = $("<tr style='display: none;'></tr>");
			var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' onBlur='"+validation+"(this)' valid='false'><span style='color:red;'></span> = </td>");
			var td2 = $("<td><select id='fxLable' name='oper' ><option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()'>最小值</option><option value='arithmetic()' >四则运算</option></select></td>");
			var td3 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px' onBlur='"+validation+"(this)' valid='false'><span id='msg' style='color:red;'></span></td>");
			var td4 = $("<td><input type='hidden' name='num' ><input type='hidden' name='status' value='0'></td>");
			var td5 = $("<td><input type='button' onClick='saveSingleFunc(this)' name='save' value='保存'>&nbsp;<input type='button' onClick='editSingleFunc(this)' name='edit' value='编辑'>&nbsp;</td>");
			var td6 = $("<td></td>");
			var td7 = $("<td ><span id='msg1' style='color:red;'></span>&nbsp;&nbsp;&nbsp;<span id='msg2' style='color:red;'></span></td>");
			tr.append(td1);
			tr.append(td2);
			tr.append(td3);
			tr.append(td4);
			tr.append(td5);
			tr.append(td6);
			tr.append(td7);
			table.append(tr);
		}
		if(typeof(o) != 'undefined' && o != null && o.length != 0){
			for(var i=0;i<o.length;i++){
				var tr = $("<tr></tr>");
				if(isreadonly == true){//只读，用于模板编辑页面显示？
					var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' value='"+o[i].key+"' readonly><span style='color:red;'></span> = </td>");
					var td2 = $("<td></td>");
					if(o[i].oper == 'sum()'){
						td2.html("求和<input type='hidden' id='fxLable' name='oper' value='"+o[i].oper+"'>");
					}else if(o[i].oper == 'evg()'){
						td2.html("求平均数<input type='hidden' id='fxLable' name='oper' value='"+o[i].oper+"'>");
					}else if(o[i].oper == 'max()'){
						td2.html("求最大值<input type='hidden' id='fxLable' name='oper' value='"+o[i].oper+"'>");
					}else if(o[i].oper == 'min()'){
						td2.html("求最小值<input type='hidden' id='fxLable' name='oper' value='"+o[i].oper+"'>");
					}
					var td3 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px' value='"+o[i].value+"' readonly><span style='color:red;'></span></td>");
					tr.append(td1);
					tr.append(td2);
					tr.append(td3);
				}else{
					if(o[i].status == '1'){//公式处于不可用、待删除状态？
						var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px;background-color:#EEFFFF' onBlur='"+validation+"(this)' value='"+o[i].key+"' ><span style='color:red;'></span> = </td>");
						//var td2 = $("<td><select id='fxLable' name='oper' ><option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()'>最小值</option></select></td>");
						var td2 = $("<td></td>");
						var selecttd2 = $("<select id='fxLable' name='oper' ></select>");
						if(o[i].oper == 'sum()'){
							selecttd2.html("<option value=''></option><option value='sum()' selected>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()'>最小值</option>");
						}else if(o[i].oper == 'evg()'){
							selecttd2.html("<option value=''></option><option value='sum()'>求和</option><option value='evg()' selected>求平均数</option><option value='max()'>最大值</option><option value='min()'>最小值</option>");
						}else if(o[i].oper == 'max()'){
							selecttd2.html("<option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()' selected>最大值</option><option value='min()'>最小值</option>");
						}else if(o[i].oper == 'min()'){
							selecttd2.html("<option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()' selected>最小值</option>");
						}
						td2.append(selecttd2);
						var td3 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px;background-color:#EEFFFF' onBlur='"+validation+"(this)' value='"+o[i].value+"' ><span style='color:red;'></span></td>");
						tr.append(td1);
						tr.append(td2);
						tr.append(td3);
					}else{//回显要编辑的公式
						var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' onBlur='"+validation+"(this)' valid='true' value='"+o[i].key+"' ><span style='color:red;'></span> = </td>");
						//var td2 = $("<td><select id='fxLable' name='oper' ><option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()'>最小值</option></select></td>");
						var td2 = $("<td></td>");
						var selecttd2 = $("<select id='fxLable' name='oper' ></select>");
						if(o[i].oper == 'sum()'){
							selecttd2.html("<option value=''></option><option value='sum()' selected>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()'>最小值</option><option value='arithmetic()' >四则运算</option>");
						}else if(o[i].oper == 'evg()'){
							selecttd2.html("<option value=''></option><option value='sum()'>求和</option><option value='evg()' selected>求平均数</option><option value='max()'>最大值</option><option value='min()'>最小值</option><option value='arithmetic()' >四则运算</option>");
						}else if(o[i].oper == 'max()'){
							selecttd2.html("<option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()' selected>最大值</option><option value='min()'>最小值</option><option value='arithmetic()' >四则运算</option>");
						}else if(o[i].oper == 'min()'){
							selecttd2.html("<option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()' selected>最小值</option><option value='arithmetic()' >四则运算</option>");
						}else if(o[i].oper == 'arithmetic()'){
							selecttd2.html("<option value=''></option><option value='sum()'>求和</option><option value='evg()'>求平均数</option><option value='max()'>最大值</option><option value='min()' >最小值</option><option value='arithmetic()' selected>四则运算</option>");
						}
						td2.append(selecttd2);
						var td3 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px' valid='true' onBlur='"+validation+"(this)' value='"+o[i].value+"' ><span id='msg' style='color:red;'></span></td>");
						tr.append(td1);
						tr.append(td2);
						tr.append(td3);
					}
				}
				var td4 = $("<td><input type='hidden' name='num' value='"+o[i].num+"'><input type='hidden' name='status' value='"+o[i].status+"'></td>");
				var td5 = $("<td><input type='button' onClick='saveSingleFunc(this)' name='save' value='保存'>&nbsp;<input type='button' onClick='editSingleFunc(this)' name='edit' value='编辑'>&nbsp;<input type='button' onClick='deleteSingleFunc(this)' name='delete' value='删除'></td>");
				var td6 = $("<td></td>");
				var td7 = $("<td valign='bottom'><span id='msg1' style='color:red;'></span>&nbsp;&nbsp;&nbsp;<span id='msg2' style='color:red;'></span></td>");
				tr.append(td4);
				tr.append(td5);
				tr.append(td6);
				tr.append(td7);
				//初始化时，公式不可编辑
				tr.find("input[name!='edit'][name!='delete']").attr("disabled","disabled");
				tr.find("select").attr("disabled","disabled");
				table.append(tr);
			}
		}
		if(isreadonly != true){
			var addInput = $("<input type='button' value='新增' onclick=fnTools.createTr('table3')>");
			table.append(addInput);
		}
		this.formulaDefined = tableDiv.html();
	},
	importFixedItemOutput:function(o,validation,isreadonly){
		var tableDiv = $("<div></div>");
		var table = $("<table></table>");
		tableDiv.append(table);
		table.attr("id","table4");
		table.attr("type","0003");
		if(isreadonly != true){
			var tr = $("<tr style='display: none;'></tr>");
			var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' onBlur='"+validation+"(this)' ><span style='color:red;'></span> = </td>");
			var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px' onBlur='"+validation+"(this)' ><span style='color:red;'></span></td>");
			var td3 = $("<td></td>");
			tr.append(td1);
			tr.append(td2);
			tr.append(td3);
			table.append(tr);
		}
		if(typeof(o) != 'undefined' && o != null && o.length != 0){
			for(var i=0;i<o.length;i++){
				var tr = $("<tr></tr>");
				if(isreadonly == true){
					var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' value='"+o[i].key+"' readonly><span style='color:red;'></span> = </td>");
					var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px' value='"+o[i].value+"' readonly><span style='color:red;'></span></td>");
					tr.append(td1);
					tr.append(td2);
				}else{
					if(o[i].status == '1'){
						var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px;background-color:#EEFFFF' onchange='"+validation+"(this)' value='"+o[i].key+"' ><span style='color:red;'></span> = </td>");
						var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px;background-color:#EEFFFF' onchange='"+validation+"(this)' value='"+o[i].value+"' ><span style='color:red;'></span></td>");
						tr.append(td1);
						tr.append(td2);
					}else{
						var td1 = $("<td><input type='text' name='key' id='fxvalue' style='width:50px' onBlur='"+validation+"(this)' value='"+o[i].key+"' ><span style='color:red;'></span> = </td>");
						var td2 = $("<td><input type='text' name='value' id='fxvalue' style='width:260px' onBlur='"+validation+"(this)' value='"+o[i].value+"' ><span style='color:red;'></span></td>");
						tr.append(td1);
						tr.append(td2);
					}
				}
				var td3 = $("<td></td>");
				tr.append(td3);
				table.append(tr);
			}
		}
		if(isreadonly != true){
			var addInput = $("<input type='button' value='新增' onclick=fnTools.createTr('table4')>");
		    table.append(addInput);
		}
		this.fixedItemOutput = tableDiv.html();
	},
	init:function(o,validation,isreadonly){
		if(typeof(o) != 'undefined' && o != null && o.length != 0){
			var i1 = 0;var i2 = 0;var i3 = 0;
			var staticStrs = [];
			var formulaDefineds = [];
			var fixedItemOutput = [];
			for(var i=0;i<o.length;i++){
				if(o[i].type == '0001'){
					staticStrs[i1] = o[i];
					i1++;
				}else if(o[i].type == '0002'){
					formulaDefineds[i2] = o[i];
					i2++;
				}else if(o[i].type == '0003'){
					fixedItemOutput[i3] = o[i];
					i3++;
				}
			}
			this.importStaticStr(staticStrs,validation,isreadonly);
			this.importFormulaDefined(formulaDefineds,validation,isreadonly);
			this.importFixedItemOutput(fixedItemOutput,validation,isreadonly);
		}
	}
	
}

function EditTableFnTools(){
	
}

EditTableFnTools.prototype = {
	createTr:function(tableId){		
		var tableHtml = document.getElementById(tableId);
		var rows = tableHtml.rows;
        var rowIndex = rows.length;//默认在末尾插入一行
        var row = tableHtml.insertRow(rowIndex);//在表格的指定插入一行
		var cells = rows[0].cells;
		//获取总行数（含“保存”的一行）
		var rownum=$("#"+tableId+" tr").length;	
		var lastTr=$("#"+tableId+" tr:eq("+(rownum-2)+")");
		//获取最后一行的num（排序）值
		var lastNumValue=lastTr.find("input[name='num']").val();
		
		for(var i=0;i<cells.length;i++){
			var td = row.insertCell(i);
			//alert(cells[i].innerHTML);
			if(i == cells.length-2){		
				td.innerHTML = "<input type='button' name='cancel' value='撤销' onclick=fnTools.delRow(this,'"+tableId+"')>";
				td.setAttribute("operator","del");		
			}else{
				var tdHtml=cells[i].innerHTML;
				
				/*if(tdHtml && tdHtml.indexOf('name=num')!=-1){
					var temp=eval(tdHtml);
					var inputObj=$(temp).find("input[name='num']");
					alert(inputObj);
				}*/
				td.innerHTML = cells[i].innerHTML;
			}
		}
		//将新加的一行隐含域num的值赋值
		$(row).find("input[name='num']").val(1+parseFloat(lastNumValue));
	},
	delRow:function(obj,tableId) {
        var rowIndex = obj.parentNode.parentNode.rowIndex;//获得行下标
        var tb = document.getElementById(tableId);
        tb.deleteRow(rowIndex);//删除当前行
    },
	getValueBySaveFn:function(){//添加公式时提交得到公式的value值
		var result = [];
		var tables = $("#fnDiv").find("table");
		for (var i = 0; i < tables.length; i++) {
			var table = $(tables[i]);
			if (table.attr("type") != null) {
				var trs = table.find("tr");
				for (var j = 0; j < trs.length; j++) {
					if (trs[j].style.display != 'none') {
						var objs = $(trs[j]).find("input,select");
						var trData = {};
						for (var k = 0; k < objs.length; k++) {
							var obj = $(objs[k]);
							if ($(obj).attr("type") == "button") {
								continue;
							}
							trData[obj.attr("name")] = obj.val();
						}
						/*if (j == 0) {
							trData.num = String(j);
						}
						else {
							trData.num = j;
						}*/
						//trData.status = '0';
						trData.type = table.attr("type");
						trData.editorTableTemplateId = $("#templateid").val();
						result.push(trData);
					}
				}
			}
		}
		return result;
	},
	getValueByTempleteUpate:function(editTable){//修改模板时得到工时更新的值
		var result = [];
		var tables = $("#fnDiv").find("table");
		for (var i = 0; i < tables.length; i++) {
			var table = $(tables[i]);
			    if(table.attr("type") != null){
					var trs = table.find("tr");
					for (var j = 0; j < trs.length; j++) {
						var objs = $(trs[j]).find("input,select");
						var trData = {};
						trData.status = 0;
						for (var k = 0; k < objs.length; k++) {
							var obj = $(objs[k]);
							if ($(obj).attr("type") == "button") {
								continue;
							}
							if(obj.attr("name") == 'key'){
								trData[obj.attr("name")] = editTable.getNewKeyByOldKey(obj.val());
								if(editTable.getNewKeyByOldKey(obj.val()) !== obj.val()){trData.status = 1}
							}else if(obj.attr("name") == 'value'){
								var valuestr = obj.val();
								if(valuestr.indexOf(':') != -1){
									trData[obj.attr("name")] = this.formatCommaToColon(editTable.getNewKeysValueStr(this.formatColonToCommaStr(valuestr)));
								}else{
									trData[obj.attr("name")] = editTable.getNewKeysValueStr(this.formatColonToCommaStr(valuestr));
								}
								if(editTable.getNewKeyByOldKey(obj.val()) !== obj.val()){trData.status = 1}
							}else{
								trData[obj.attr("name")] = obj.val();
							}
						}
						if (j == 0) {
							trData.num = String(j);
						}
						else {
							trData.num = j;
						}
						trData.type = table.attr("type");
						trData.editorTableTemplateId = $("#templateid").val();
						result.push(trData);
					}
				}
		}
		return result;
	},
	formatColonToCommaStr:function(str){//将公式里的冒号转化成逗号
	   var arrt = '';
	   var arrts = str.split(":");
	   if(arrts.length === 2){
	   	 var arrt0 = this.resolutionStr(arrts[0]);
		 var arrt1 = this.resolutionStr(arrts[1]);
		 var start = parseInt(arrt0[1]);
		 var end = parseInt(arrt1[1]);
         if(arrt0[0] === arrt1[0] && start < end){
			 for(var i=start;i<=end;i++){
                arrt = arrt + arrt0[0] + i + ',';
			 }
			 arrt = arrt.substr(0,arrt.length-1);
		 }else if(arrt0[0] !== arrt1[0] && arrt0[1] === arrt1[1]){
            //alert("1公式格式不对！");
			return false;
		 }else{
            //alert("2公式格式不对！");
			return false;
		 }
	   }
	   if(arrt == '' || arrt == null)
	   arrt = str;
	   return arrt;
	 },
	 formatCommaToColon:function(str){//将公式里的逗号转化成冒号
	   var arrt = '';
	   var strs = str.split(',');
	   arrt = strs[0]+':'+strs[strs.length-1];
	   return arrt;
	 },
	 resolutionStr:function(str){
	 	var arrts = [];
		if(str.search(/[a-zA-Z]+/)==0){
			var i = str.search(/[0-9]+/);
            arrts[0] = str.substr(0,i);
			arrts[1] = str.substr(i,str.length);
		}else{
           alert("字符串格式不对！");
		   return false;
		}
		return arrts;
	 },
}
var fnTools = new EditTableFnTools();
