
/*
* @Object       : bidTable
* @author       : ruanqingfeng,shujun
* @version      : 表格组件1.0版本
* @desc1        : 表格组件（渲染出一个表格，对表格行进行动态"增""减"功能）
* @desc2        : 支持非单元格合并的简单表头
* @desc3        : 支持单元格合并的表头
* @desc4        : 允许页面中创建多个bidTable,被维护在bidTables数组变量中，可通过数组索引到某个bidTable
* @for example  : 将本js引入后，在需要显示组件的地方加入<div class="bidTable"></div>即可
*       参数用法 : 在div中加入属性defaults={
*	                                      headerAlign:{align:"center",valign:"middle"},
*                                         formatCfg  :{key1:"#,###.00"},
*                                         alignCfg   :{key1:"center"}
*										}
*/
importCss(baseUrl+"/bizbase/css/bidTable.css");
importJs(baseUrl+"/bizbase/script/ajaxfileupload.js");

function isArray(o) {
  return Object.prototype.toString.call(o) === '[object Array]';
} 

function in_array(e,arr){  
	for(var i=0;i<arr.length;i++){  
		if(arr[i] == e)  
		return true;  
	}  
	return false;  
} 


var BidTable = function(_cfg){ 
	this.supportColumnType = "select,search,checkboxBoolean,checkbox2,checkbox,rownum,detail,label,dateLabel,hidden,number,text,textarea,date,delBtn";//目前支持的列类型（不可修改）
	this.supportValueType = "select,search,checkboxBoolean,checkbox2,label,dateLabel,number,input,textarea,date,hidden";//目前支持的值类型（不可修改）
	this.renderTo = "bidTable";//表格渲染的容器ID
	this.renderToDiv = null;   //渲染的DIV对象
	this.rows = 1;//当前表格记录数（不可修改）
	this.isCreateHeader = false;//表头是否已经创建（不可修改）
	this.addRowBtn = null;//"增加"按钮（不可修改）
	this.rowInitCnt = 0;//初始化时需要默认构造几行
	this.showHeader = true;//初始化时是否显示表头
	this.maxRows = 10;//允许添加最大行数
	this.tableWidth = "98%";//表格宽度
	this.showAddButton = true;//是否显示"增加"按钮
	this.addRowBtnText = "添加";//"添加"按钮上的文字信息
	this.bidTable = null;//表格对象
	this.headerColumnType = "simple";//表头类型（simple,groupHeader）
	this.tableColumnsShow = null;//表格的列显示配置（设置false，将不显示该列）
	this.detailId = 0;//绑定明细列
	this.detailBtnText = "查看"//查看明细的按钮Text
	this.changeValue1="";//
	this.changeValue2="";
	this.changeValue3="";
	this.ckcol = [];
	this.store = null;
	this.createSortNum = false;
	this.tableId = "";
	this.isLastB=false;  //最后一行显示是否加粗
	this.selectObjs={a:'1111'};
	
	this.css=[];
	this.alignCfg=[];//表格对齐方式
	this.formatCfg=[];//数字格式化
	this.headerAlign=[];//表头对齐方式
	
	this.tableColumns = [
		[{rowspan:2,header:"排序"},{rowspan:2,header:"投标人"},{rowspan:2,header:"投标人名称"},{rowspan:2,header:"标段/标包"},{rowspan:2,header:"投标报价（万元）"},{colspan:3,header:"评标结果汇总"},{rowspan:2,header:"操作"}],
		[{header:"技术"},{header:"商务"},"总分"]
	];//表头
	this.tableColumnsType = [
		"rownum",["hidden","search"],"label","text","text","text","text","text","delBtn"
	];//列类型(支持一列放多个，通过[]方式配置)
	this.fieldName = [
		"id","tender","tenderName","tenderStageName","quote","technicalPoints","businessPoints","totalPoints"
	];//表格中this.supportValueType对应的组件名称
	this.tableColumnsWidth = [
		"5%","19%","27%","12%","8%","8%","8%","8%","5%"
	];//表格宽度设置
	this.tableColumnsDict = []//数据字典转换格式
	
	this.validateCfg = {
		tender:"{required:true,maxlength:50}",
		tenderStageName:"{required:true,maxlength:50}",
		quote:"{number:true,maxlength:20,range:[0.00,99999999999999999.99]}",
		technicalPoints:"{number:true,maxlength:5,range:[0.00,99.99]}",
		businessPoints:"{number:true,maxlength:5,range:[0.00,99.99]}",
		totalPoints:"{number:true,maxlength:6}"
		
	};//表格验证信息
	
	var listeners = {
		beforeDelRow     : "beforeDelRow",
		afterDelRow     : "afterDelRow",
		afterAddRow      : "afterAddRow",
		showDetailRow    : "showDetailRow",
		afterSetRowValue : "afterSetRowValue",
		changeValue1     : "changeValue1",
		changeValue2     : "changeValue2"
	}//表格支持的事件类型（不可修改）
	
	$.extend(this,_cfg);
	/*
	* 为表格初始化验证信息
	*/
	this.initValidateCfg = function (_validateCfg){
		if(_validateCfg==null){
			_validateCfg = {};	
		}
		this.validateCfg = _validateCfg;
		var _this = this;
		$(this.fieldName).each(function(i){
			var curValidate = _this.validateCfg[this];
			if(curValidate==null){
				curValidate = "";	
			}else if(typeof(curValidate)=="object"){
				curValidate = $.toJSON(curValidate);
			}
			if(curValidate==""){
				_this.bidTable.find("[name='"+this+"']").removeAttr("validate");
			}else{
				_this.bidTable.find("[name='"+this+"']").attr("validate",curValidate);
			}
		})
	}
	
	this.setInputCss=function(_css,_tr){
		var _this = this;
		if(!isArray(_css)){
			return ;
		}
		for(var i=0;i<_css.length;i++){
			var css0=_css[i];
			if(!in_array(css0.fieldName,_this.fieldName)){
				continue;
			}
			var  names= _tr.find("[name='"+css0.fieldName+"']");
			for(var j=0;j<names.length;j++){
				if(css0.className!=''){
					names[j].className=css0.className;
				}
				if(css0.readOnly){
					$(names[j]).attr("readOnly",true);
				}		
			}

		}
		
	}
	
	
	/*
	* 为表格增加事件监听
	*/
	this.addListener = this.on = function (_eventName,_callBack){
		$(this).unbind(_eventName);
		$(this).bind(_eventName,function(ev,param1,param2,param3){
			$.event.returnValue = _callBack(param1,param2,param3);
		});
	}
	
	this.addChangeValue1Event = function(filedName,filedName11,_eventName,_callBack){
	    this.changeValue1=filedName;
		this.changeValue3=filedName11;
		this.addListener(_eventName,_callBack);
	}
	
	this.addChangeValue2Event = function(filedName,filedName11,_eventName,_callBack){
	    this.changeValue3=filedName11;
		this.changeValue2=filedName;
		this.addListener(_eventName,_callBack);
	}
	
	 replaceNotNumber=function(v){
	    v=$(v);
	    var pattern =/[^0-9]/g;
	    if(pattern.test(v.val())){
	        v.val(v.val().replace(pattern,"")) ;
	     }
	   }

	/*
	* 为表格移除事件监听
	*/
	this.removeListener = this.un = function (_eventName){
		$(this).unbind(_eventName);
	}
	
	/*
	* 删除表格方法
	*/
	this.removeTable = function(){
		this.rows = 1;
		if (this.bidTable != null) {
			this.bidTable.remove();
		}
		if(this.addRowBtn!=null){
			this.addRowBtn.remove();	
		}
		this.isCreateHeader = false;	
	}
	
	/*
	* 根据行号获取行对象
	*/
	this.getTrByIndex = function(_index){
		return this.bidTable.children().eq(1).children().eq(_index);
	}
	
	/*
	* 创建表格方法
	*/
	this.createTable = function(){
		this.removeTable();
		if(this.rowInitCnt>0){
			this.showHeader = true;	
		}
		this.bidTable = $("<table class='bidTable'  style='border-collapse:collapse;width:"+this.tableWidth+"'></table>");
		//this.tbody=$("<tbody></tbody>");
		if(this.showAddButton){
			this.addRowBtn = $('<input type="button" class="uploadFileTableBtn"  value="' + this.addRowBtnText + '" />');
			var _this = this;
			this.addRowBtn.bind("click",function(){
				_this.createRow();
			});
			this.renderToDiv.append(this.addRowBtn);//增加按钮
		}
		
		
		
		if(this.showHeader){
			this.createHeader();
		}
		
		for(var i=0;i<this.rowInitCnt;i++){
			this.createRow();
		}
		this.renderToDiv.append(this.bidTable);//表格
		
		
		
	};
	
	/*
	* 创建表头方法
	*/
	this.createHeader = function(){//渲染表头
		var tableHeader = $("<thead></thead>");
		this.bidTable.append(tableHeader);
		if(!isArray(this.tableColumns[0])){//单行表头
			var headerTr = $("<tr></tr>");
			tableHeader.append(headerTr);
			for(var i=0;i<this.tableColumns.length;i++){
				var column = this.tableColumns[i];
				var columnText = "";
				var columnalign = "";
				var columnValign = "";
				if(typeof(column)=="string"){
					columnText = column;
				}else{
					columnText = column.header;
					columnalign = column.align == null ? this.headerAlign.align : column.align;
					columnValign = column.valign == null ? this.headerAlign.valign : column.valign;
				}
				headerTr.append($("<th style=\"text-align:"+columnalign+"; vertical-align:"+columnValign+"\" class='uploadFileTableHeader uploadFileTableBorder' width='"+this.tableColumnsWidth[i]+"'>"+columnText+"</th>"));
			}
			
		}else{//合并表头
			for(var i=0;i<this.tableColumns.length;i++){
				var headerTr = $("<tr></tr>");
				tableHeader.append(headerTr);
				for(var j=0;j<this.tableColumns[i].length;j++){
					var columnObj = this.tableColumns[i][j];
					if(typeof(columnObj)=="string"){
						columnObj = {rowspan:1,colspan:1,header:columnObj};
					}
					var rowspan = (columnObj.rowspan == null ? 1 : columnObj.rowspan);
					var colspan = (columnObj.colspan == null ? 1 : columnObj.colspan);
					var checkbox = (columnObj.checkbox == null ? false : columnObj.checkbox);
					var columnalign = columnObj.align == null ? this.headerAlign.align : columnObj.align;
					var columnValign = columnObj.valign == null ? this.headerAlign.valign : columnObj.valign;
					if(columnObj.header==null){
						alert("您指定的表头不正确!正确格式为：\n[[{rowspan:2,colspan:1,header:'表头1'},{rowspan:2,colspan:1,header:'表头2'}],\n[{rowspan:2,colspan:1,header:'表头1'},{rowspan:2,colspan:1,header:'表头2'}]]");	
					}
					if(checkbox){
						headerTr.append($("<th style=\"text-align:"+columnalign+"; vertical-align:"+columnValign+"\"  rowspan='"+rowspan+"' colspan='"+colspan+"' class='uploadFileTableHeader uploadFileTableBorder'><span><input class='bidCheckBox' type='checkbox' />&nbsp;"+columnObj.header+"</span></th>"));
					}else{
						headerTr.append($("<th style=\"text-align:"+columnalign+"; vertical-align:"+columnValign+"\"  rowspan='"+rowspan+"' colspan='"+colspan+"' class='uploadFileTableHeader uploadFileTableBorder'>"+columnObj.header+"</th>"));
					}
				}
			}
			this.createHiddenRow();
		}
		
		this.isCreateHeader=true;
	};
	
	/*
	* 创建隐藏行，主要是用来控制列宽度，此方法外部不建议调用
	*/
	this.createHiddenRow = function(){
		var tr = $("<tr id='bidTableHiidenRow' style='visibility:hidden'></tr>");
		for(var i=0;i<this.tableColumnsType.length;i++){
			var td = $("<td width='"+this.tableColumnsWidth[i]+"'>&nbsp;</td>");
			tr.append(td);
		}
		$(this.bidTable).append(tr);
		this.headerColumnType = "groupHeader";
		return tr;
	};
	
	/*
	* 删除隐藏行，此方法外部不建议调用
	*/
	this.removeHiddenRow = function(){
		var tr = $(this.bidTable).children().eq(1).children().eq(0);
		if(tr.attr("id")=="bidTableHiidenRow"){
			tr.remove();
		}
	};	
	
	/*
	 * 创建明细显示行
	 */
	this.createDetailRow = function(_curTr,_content){
		_curTr.attr("detailId",this.detailId);
		var detailTr = $("<tr detailId='detailId_"+(this.detailId++)+"' style='line-height: 22px;background-color:#ffffff;'></tr>");
		var detailTd = $("<td class='uploadFileTableBorder' colspan="+this.tableColumnsWidth.length+"></td>");
		detailTd.append(_content);
		detailTr.append(detailTd);
		_curTr.after(detailTr);
    }
	
	/*
	* 新增加一行方法
	*/
	this.createRow = function(_data){
		
		if(this.headerColumnType!="simple"){
			this.removeHiddenRow();	
		}
		
		if(this.rows>this.maxRows){
			bid.alert("您最多只能添加"+this.maxRows+"行！");
			return false;	
		}
		if(this.isCreateHeader==false){
			this.createHeader();
		}
		var trBg = "background-color:#ECEEF8;";
		if(this.rows%2==0){
			trBg = "background-color:#ffffff;";
		}
		var tr = $("<tr style='line-height: 22px;"+trBg+"'></tr>");
		var fieldPoint = 0;
		for(var i=0;i<this.tableColumnsType.length;i++){
			var isShowColumn = "";
			if(this.tableColumnsShow!=null&&this.tableColumnsShow[i]==false){
				isShowColumn = ";display:none";
			}
			var td = $("<td style='text-align:"+this.alignCfg[this.fieldName[fieldPoint]]+isShowColumn+"' class='uploadFileTableBorder' width='"+this.tableColumnsWidth[i]+"'></td>");
			if(typeof(this.tableColumnsType[i])=="string"){
				var obj = createColumnCom(this,this.tableColumnsType[i],fieldPoint,tr,_data);
				if(obj==null){
					return false;	
				}
				td.append(obj);
				
				if(this.supportValueType.indexOf(this.tableColumnsType[i])!=-1){
					fieldPoint++;
				}
			}else{
				for(var j=0;j<this.tableColumnsType[i].length;j++){
					var obj = createColumnCom(this,this.tableColumnsType[i][j],fieldPoint,tr);
					if(obj==null){
						return false;	
					}
					td.append(obj);	
					if (typeof(this.tableColumnsType[i][j])=='string') {
						if (this.supportValueType.indexOf(this.tableColumnsType[i][j]) != -1) {
							fieldPoint++;
						}
					}
					
					else if (typeof(this.tableColumnsType[i][j])=='object') {
						if (this.supportValueType.indexOf(this.tableColumnsType[i][j].type) != -1) {
							fieldPoint++;
						}
					}		
				}
			}
			
			//td.addClass("uploadFileTableDataCenter");
			tr.append(td);	
		}
		
		var _this=this;
		if(this.changeValue1!=""){
			var _changeValue00=$(tr).find(":input[name='" + this.changeValue3 + "']");
			_changeValue00=$(_changeValue00[0]);
			var doms = $(tr).find(":input[name='" + this.changeValue1 + "']");
			for (var j = 0; j < doms.length; j++) {
				var obj = doms[j];
				$(obj).bind("keyup", function(){
					//replaceNotNumber(obj);	
					$(_this).trigger(listeners.changeValue1,[_this.getValueByFiledName(tr),_changeValue00]);
				
					if($.event.returnValue==false){
						return;	
					}
				});
			}
		}
		
		if(this.changeValue2!=""){
			var _changeValue3=$(tr).find(":input[name='" + this.changeValue3 + "']");
			_changeValue3=$(_changeValue3[0]);
			var doms2 = $(tr).find(":input[name='" + this.changeValue2 + "']");
			for (var j = 0; j < doms2.length; j++) {
				var obj2 = doms2[j];
				$(obj2).bind("keyup", function(){
					//replaceNotNumber(obj2);	
					$(_this).trigger(listeners.changeValue2,[_this.getValueByFiledName(tr),_changeValue3]);
				
					if($.event.returnValue==false){
						return;	
					}
				});
			}
		}
		
		//添加css样式
		this.setInputCss(this.css,tr);
		
		
		$(this.bidTable).append(tr);
		var currIndex = parseInt(this.rows)-1;
		$(this).trigger(listeners.afterAddRow,[tr,currIndex]);
		
		this.addEventListener();
		createDateInput(tr);
		this.rows++;
		return tr;
	};
	
	/*
	* 创建td中的组件（此方法外部不可调用）
	*/
	var createColumnCom = function(_this,_columnType,fieldPoint,tr,_data){
		var obj = null;
		var validateStr = _this.validateCfg[_this.fieldName[fieldPoint]];
		if(validateStr!=null){
			if(typeof(validateStr)=="string"){
				validateStr = " validate=" + validateStr + " ";	
			}else{
				validateStr = " validate=" + $.toJSON(validateStr) + " ";	
			}
		}else{
			validateStr = "";
		}
			
		if(_columnType=="rownum"){
			obj = $("<span>"+_this.rows+"</span>");
			return obj;
		}else if(_columnType=="checkbox"){
			obj = $("<input type='checkbox' bidTableFlag=true />");
			_this.ckcol.push(obj);
		}else if(_columnType=="search"){
			var searchBtn = '<input bidTableFlag=true type="button" class="uploadFileTableBtn"  value="搜索" />';
			obj = $("<input bidTableFlag=true readonly "+validateStr+" elementName=' ' name='"+_this.fieldName[fieldPoint]+"' revalidate='true' id='"+_this.fieldName[fieldPoint]+ _this.tableId +_this.rows+"' class='biddingTableTextBorder' style='width:70%' type='"+_columnType+"' />"+searchBtn);	
		}else if(_columnType=="checkbox2"||_columnType=="checkboxBoolean"){
			var val = "";
			if(_data!=null){
				val = _data[_this.fieldName[fieldPoint]];
			}
			obj = $("<input bidTableFlag=true type='checkbox' value='"+val+"' />");
		}else if(_columnType=="detail"){
			obj = $("<a class='detail' href='javascript:void(0)'>"+_this.detailBtnText+"</a>");
			var _currIndex = _this.rows;
			obj.bind("click",function(){
				$(_this).trigger(listeners.showDetailRow,[tr,_this.getValueByRr(tr),_currIndex]);
			})
		}else if(_columnType=="dateLabel"){
			var val = "";
			if(_data!=null){
				val = _data[_this.fieldName[fieldPoint]];
				val = (val == null ? "" : val);
			}
			
			obj = $("<label bidType='date' class='bidTableLabel' name='"+_this.fieldName[fieldPoint]+"' id='"+_this.fieldName[fieldPoint]+"'>"+val+"</label>");
		}else if(_columnType=="label"){
			var val = "";
			if(_data!=null){
				val = _data[_this.fieldName[fieldPoint]];
				val = (val == null ? "" : val);
			}
			
			obj = $("<label class='bidTableLabel' name='"+_this.fieldName[fieldPoint]+"' id='"+_this.fieldName[fieldPoint]+"'>"+val+"</label>");
		}else if(_columnType=="date"){
			obj = $("<input bidTableFlag=true bidType='date' readonly='readonly' "+validateStr+" elementName=' ' name='"+_this.fieldName[fieldPoint]+"' revalidate='true' id='"+_this.fieldName[fieldPoint]+ _this.tableId+_this.rows+"' class='biddingTableTextBorder' style='width:80%' type='text' />");
		}else if(_columnType=="number"){
			obj = $("<input  bidTableFlag=true  "+validateStr+" elementName=' ' name='"+_this.fieldName[fieldPoint]+"' revalidate='true' id='"+_this.fieldName[fieldPoint]+ _this.tableId+_this.rows+"' class='biddingTableTextBorder' style='ime-mode:inactive;width:96%;text-align:"+_this.alignCfg[_this.fieldName[fieldPoint]]+"' type='text' />");	
			obj.bind("keydown",function(){
				return checkFloat(this);
			});
			obj.bind("keyup",function(){
				return clearNoNum(this);
			});
			
			obj.bind("blur",function(){
				if(this.value!=""){
					this.value = parseFloat(this.value);
				}
			});
			
		}else if(_columnType=="text"||_columnType=="hidden"){
			obj = $("<input bidTableFlag=true "+validateStr+" elementName=' ' name='"+_this.fieldName[fieldPoint]+"' revalidate='true' id='"+_this.fieldName[fieldPoint]+ _this.tableId+_this.rows+"' class='biddingTableTextBorder' style='width:96%' type='"+_columnType+"' />");	
		}else if(_columnType=="textarea"){
			obj = $('<textarea bidTableFlag=true elementName=" " '+validateStr+' name="'+_this.fieldName[fieldPoint]+'" revalidate="true" id="'+_this.fieldName[fieldPoint]+ _this.tableId+_this.rows+'" class="biddingTableTextBorder" name="textarea" style="width:99%" id="textarea"  rows="2"></textarea>');
		}else if(_columnType=="delBtn"){
			var value="class='uploadFileTableBtn' value='删除'";
			obj = $("<input bidTableFlag=true "+value+" type='button' />");
			var _index = (parseInt(_this.rows)-1);
			obj.bind("click",function(){
				$(_this).trigger(listeners.beforeDelRow,[_this.getValueByRr(tr),tr,_index]);
				
				if($.event.returnValue==false){
					return;	
				}
				
				_this.rows = parseInt(_this.rows)-1;
				var currRowNum = tr.find("span").html();
				tr.siblings().each(function(){
					var sibingsSpan = $(this).find("span").eq(0);
					var siblingsRowNum = sibingsSpan.html();	
					
					if(parseInt(siblingsRowNum)>parseInt(currRowNum)){
						var trBgColor = ($(this).css("background-color")).toUpperCase()=="#ECEEF8"?"#ffffff":"#ECEEF8";
						$(this).css("background-color",trBgColor);
						sibingsSpan.html(parseInt(siblingsRowNum)-1);	
					}
				})
				tr.remove();
				if(_this.rows==1){
					if(_this.headerColumnType!="simple"){
						_this.createHiddenRow();
					}
				}
				$(_this).trigger(listeners.afterDelRow);
			});
		}else if (_columnType == "select") {
			if(_this.fieldName[fieldPoint] in _this.selectObjs){
				obj=$("<select id="+_this.fieldName[fieldPoint]+" name="+_this.fieldName[fieldPoint]+"><options></options></select>");
				for(var key in _this.selectObjs[_this.fieldName[fieldPoint]]){
					obj.append($("<option value="+_this.selectObjs[_this.fieldName[fieldPoint]][key]+">"+key+"</option>"));
				}
			} else{
				alert("请配置select的名称及值.");
			}
		}else if (typeof(_columnType) == "object") {
				if (_columnType.type == "text" || _columnType.type == "hidden") {
					obj = $("<input bidTableFlag=true " + validateStr + " elementName=' ' name='" + _this.fieldName[fieldPoint] + "' revalidate='true' id='" + _this.fieldName[fieldPoint] + _this.tableId + _this.rows + "' class='biddingTableTextBorder' style='width:" + _columnType.width + "' type='" + _columnType.type + "' />");
				}
				else 
					if (_columnType.type == "label") {
						obj = $("<label name='" + _this.fieldName[fieldPoint] + "' id='" + _this.fieldName[fieldPoint] + "'></label>");
					}
					else {
						obj = $("<span>" + _columnType.text + "</span>");
					}
			}
			else {
				alert("组件目前不支持[" + _columnType + "]类型！当前版本支持的类型为：" + _this.supportColumnType);
				return null;
			}
		return obj;
	}
	
	
	this.getBidTable = function(){
	    return this.bidTable;
	}
	
	
	
	/*
	* 获取表格中某一行的值，以对象形式返回
	*/
	this.getValueByFiledName = function(_tr){
		var curTr = _tr;
		var trValue = {};
		for(var j=0;j<this.fieldName.length;j++){
			var fieldname=this.fieldName[j];
			var obj=$(curTr).find(":input[name='" + fieldname + "']");
			if(obj!=null){
				trValue[fieldname] = $(obj[0]).val();
			}
		}
		return trValue;
	}
	
	
	
	
	/*
	* 获取表格中某一行的值，以对象形式返回
	*/
	this.getValueByRr = function(_tr){
		var curTr = _tr;
		var tdArr = (curTr.children());
		var fieldPoint = 0;
		var trValue = {};
		for(var j=0;j<tdArr.length;j++){
			var inputObj = tdArr.eq(j).children();
			if(inputObj[0]==null){
				continue;	
			}
			
			for(var k=0;k<inputObj.length;k++){
				if((this.supportValueType.toUpperCase()).indexOf(inputObj[k].tagName)!=-1){
					if("INPUT" == inputObj[k].tagName ){
						if(this.tableColumnsType[fieldPoint]=="checkbox2" || $(inputObj[k]).attr("type") == "hidden" || $(inputObj[k]).attr("type") == "text"){
							trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).val();
						}else if(this.tableColumnsType[fieldPoint]=="checkboxBoolean"){
							trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).attr("checked");
						}else{
							if($(inputObj[k]).attr("type") == "checkbox" || $(inputObj[k]).attr("type") == "button" || $(inputObj[k]).attr("type") == "submit" || $(inputObj[k]).attr("type") == "reset"){
								continue;	
							}
						}
					}else if("LABEL" == inputObj[k].tagName ){
						if($(inputObj[k]).attr("class")=="bidTableLabel"){
							trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).text();
						}
					}else{
						trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).val();
					}
					
				}
			}
		}
		return trValue;
	}
	
	/*
	* 获取表格中的值，以数组形式返回
	*/
	this.getValues = function(selected){
		if(this.fieldName==null||this.fieldName==""){
			alert("请配置fieldName属性！格式如：['field1','field2','field3']");
			return ;	
		}
		var data = [];
		var tableTrs = $(this.bidTable).find("tr");	
		var headerCnt = 1;
		if(this.headerColumnType!="simple"){
			headerCnt = this.tableColumns.length;
		}
		
		for(var i=headerCnt;i<tableTrs.length;i++){
			var curTr = $(tableTrs[i]);
			if(selected!=null){
				if(curTr.find("input[type=checkbox]").eq(0).attr("checked") == !selected){
					continue;
				}
			}

			var tdArr = (curTr.children());
			var fieldPoint = 0;
			var trValue = {};
			var flag = true;
			for(var j=0;j<tdArr.length;j++){
				var inputObj = tdArr.eq(j).children();
				if(inputObj[0]==null){
					continue;	
				}
				
				for(var k=0;k<inputObj.length;k++){
					if((this.supportValueType.toUpperCase()).indexOf(inputObj[k].tagName)!=-1){
						if("INPUT" == inputObj[k].tagName ){
							
							if(this.tableColumnsType[fieldPoint]=="checkbox2" || $(inputObj[k]).attr("type") == "hidden" || $(inputObj[k]).attr("type") == "text"){
								trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).val();
							}else if(this.tableColumnsType[fieldPoint]=="checkboxBoolean"){
								trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).attr("checked");
							}else{
								if($(inputObj[k]).attr("type") == "checkbox" || $(inputObj[k]).attr("type") == "button" || $(inputObj[k]).attr("type") == "submit" || $(inputObj[k]).attr("type") == "reset"){
									continue;	
								}
							}
						}else if("LABEL" == inputObj[k].tagName ){
							if($(inputObj[k]).attr("class")=="bidTableLabel"){
								trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).text();
							}
						} else {
							trValue[this.fieldName[fieldPoint++]] = $(inputObj[k]).val();
						}
						
						flag = false;
					}
				}
				
			}
			if(flag){
				continue;	
			}
			if(this.createSortNum){
				trValue.sortNum = i;
			}
			data.push(trValue);
		}
		return data;
	};

	
	
	
	/*
	* 设置表格中的值，参数以数组形式传入
	*/
	this.setValues = function(_data){
		this.store = _data;
		this.createTable();
		for(var i=0;i<_data.length;i++){
			var obj = _data[i];
			
			for(var key in this.formatCfg){
				if(obj[key]==null||obj[key]=="")continue;
				obj[key] = formatNumber(obj[key],this.formatCfg[key]);
			}
			
			var tr = this.createRow(this.bidTable);
			var fieldPoint = 0;
			var tdArr = (tr.children());

			for(var j=0;j<tdArr.length;j++){
				var inputObj = tdArr.eq(j).children();
				for(var k=0;k<inputObj.length;k++){
					if((this.supportValueType.toUpperCase()).indexOf(inputObj[k].tagName)!=-1){
						if("INPUT" == inputObj[k].tagName ){
							
							if(this.tableColumnsType[fieldPoint]!="checkbox2"&&this.tableColumnsType[fieldPoint]!="checkboxBoolean"){
								if($(inputObj[k]).attr("type") == "checkbox" ||$(inputObj[k]).attr("type") == "button" || $(inputObj[k]).attr("type") == "submit" || $(inputObj[k]).attr("type") == "reset"){
									continue;	
								}
							}else if(this.tableColumnsType[fieldPoint]=="checkboxBoolean"){
								
								$(inputObj[k]).attr("checked",(obj[this.fieldName[fieldPoint]]) == "true" ? true : false);
							}
							if($(inputObj[k]).attr("bidType")=="date"&&obj[this.fieldName[fieldPoint]]!=null){
								$(inputObj[k]).val(new Date(obj[this.fieldName[fieldPoint++]]).format("yyyy-MM-dd"));
							}else{
								$(inputObj[k]).val(obj[this.fieldName[fieldPoint++]]);
							}
						}else if("SELECT" == inputObj[k].tagName ){
							$(inputObj[k]).val(obj[this.fieldName[fieldPoint++]]);
						}else if("LABEL" == inputObj[k].tagName ){
							if (this.fieldName[fieldPoint] in this.selectObjs) {
								for (var key in this.selectObjs[this.fieldName[fieldPoint]]) {
									if (this.selectObjs[this.fieldName[fieldPoint]][key] == obj[this.fieldName[fieldPoint]]) {
										$(inputObj[k]).text(key);
									}
								}
								fieldPoint++;
							}
							else {
								var text = "";
								if (this.tableColumnsDict.length != 0 && this.tableColumnsDict[fieldPoint] != null && this.tableColumnsDict[fieldPoint] != "") {
									var dict = new PMSDict();
									text = (dict.getDictText(this.tableColumnsDict[fieldPoint], obj[this.fieldName[fieldPoint++]]));
								}
								else {
									text = obj[this.fieldName[fieldPoint++]];
								}
								
								if ($(inputObj[k]).attr("bidType") == "date" && text != null) {
									$(inputObj[k]).text(new Date(text).format("yyyy-MM-dd"));
								}
								else {
									if (this.isLastB) {
										if (i == _data.length - 1) {
											$(inputObj[k]).html("<b>" + text + "</b>");
										}
										else {
											$(inputObj[k]).text(text);
										}
									}
									else {
										$(inputObj[k]).text(text);
									}
								}
							}
						}else if("TEXTAREA" == inputObj[k].tagName ){
							$(inputObj[k]).text(obj[this.fieldName[fieldPoint++]]);
						}else if(this.tableColumnsType[fieldPoint] == "detail"  ){
							fieldPoint++;
						}
						
					}
				}
			}
			$(this).trigger(listeners.afterSetRowValue,[tr,this.getValueByRr(tr)]);
		}
		
	}
	this.createTable();
	
	
};
var bidTables = [];
BidTable.items = {};
$(function(){
	$("div[class*='bidTable']").each(function(i,element){
		var defaults = $(this).attr("defaults");
		if(defaults==null||defaults==""){
			defaults = "{}";	
		}
		eval("var defaults ="+defaults);
		defaults.tableIndex = i;
		defaults.renderToDiv= $(element);
		defaults.tableId = $(this).attr("id");
		var obj = new BidTable(defaults);
		bidTables.push(obj);
		BidTable.items[defaults.tableId] = obj;
	});
})


/**
 * selectAll & cancelAll.
 * @param {Object} head
 * 				the check head
 * @param {Object} cks
 * 				the check children
 */
function setCheckStatus(head, cks) {
	head = $(head);
	cks = $(cks);
	head.click(function() {
		var checked = $(this).attr('checked'); 
		$(cks).each(function(i){
			$(this).attr('checked', checked);
		});
	});
	cks.each(function(i) {
		$(this).click(function(i) {
			if(head.attr('checked')) {
				for(var j=0; j<cks.length; j++) {
					if(!$(cks[j]).attr('checked')) {
						head.attr('checked', false);
						break;
					}
				}
			} else {
				var flag = true;
				for(var j=0; j<cks.length; j++) {
					if(!$(cks[j]).attr('checked')) {
						flag = false;
						break;
					}
				}
				if(flag) head.attr('checked', true);
			}
		});
	});
}

BidTable.prototype.addEventListener = function() {
	var ths = this.bidTable.find("th");
	var ckhead=ths.find(":input[type='checkbox']");
	if(ckhead && ckhead.length==1 && this.ckcol.length > 0) {
		var head = ckhead;
		var cks = this.ckcol;
		setCheckStatus(head, cks);
	}
}

BidTable.prototype.getSelectedObj = function() {
	var ckbs = $(this.ckcol);
	var objs = new Array();
	var _this=this;
	ckbs.each(function(i) {
		if($(this).attr('checked')) {
			var _tr=$(this).parent().parent();
			objs.push(_this.getValueByRr(_tr));
		}
	});
	return objs;
}


BidTable.prototype.setValuesBydatas = function(_data) {
	
		for(var i=0;i<_data.length;i++){
			var obj = _data[i];

			var tr = this.createRow(this.bidTable);
			var fieldPoint = 0;
			var tdArr = (tr.children());
			//alert(tdArr.length);
			for(var j=0;j<tdArr.length;j++){
				var inputObj = tdArr.eq(j).children();
				for(var k=0;k<inputObj.length;k++){
					if((this.supportValueType.toUpperCase()).indexOf(inputObj[k].tagName)!=-1){
						if("INPUT" == inputObj[k].tagName ){
							if($(inputObj[k]).attr("type") == "checkbox" ||$(inputObj[k]).attr("type") == "button" || $(inputObj[k]).attr("type") == "submit" || $(inputObj[k]).attr("type") == "reset"){
								continue;	
							}
							$(inputObj[k]).val(obj[this.fieldName[fieldPoint++]]);
						}else if("LABEL" == inputObj[k].tagName ){
							var text = "";
							if(this.tableColumnsDict[fieldPoint]!=null&&this.tableColumnsDict[fieldPoint]!=""){
								var dict = new PMSDict();
								text = (dict.getDictText(this.tableColumnsDict[fieldPoint],obj[this.fieldName[fieldPoint++]]));
							}else{
								text = obj[this.fieldName[fieldPoint++]];
							}
							$(inputObj[k]).text(text);
						}else if("TEXTAREA" == inputObj[k].tagName ){
							$(inputObj[k]).text(obj[this.fieldName[fieldPoint++]]);
						}
						
					}
				}
			}
		}
		
}


function createDateInput(tr){
	var elements = $(tr).find("input[bidType='date']");
	var rootpath = getRootPath();
	var iconpath = rootpath + "/scripts/common/images/calendar.gif";
	$(elements).each(function(i) {
		var format = $(this).attr("format") ? 
				$(this).attr("format") : "yy-mm-dd";
		var changeMonth = $(this).attr("changeMonth") == 'false' ? 
				 false : true;
		var changeYear = $(this).attr("changeYear") == 'false' ? 
				 false : true;
		var yearRange = $(this).attr("yearRange") ? 
				$(this).attr("yearRange") : "-10:+10";
		$(this).datepicker({
			showOn: "button",
			buttonImage: iconpath,
			buttonImageOnly: true,
			changeMonth: changeMonth,
			changeYear: changeYear,
			yearRange:yearRange,
			dateFormat:format
		});
	});
}


/*
 * @methodName    : formatNumber
 * @author        : ruanqingfeng
 * @desc          : 格式化数字
 * @for example   : formatNumber(222.223344,"#，###.00")
 */
function formatNumber(number,formatter){
	var splitArr = formatter.split(".");
	if(splitArr.length==1){
		number = roundNumber(number,0);	
	}else{
		number = roundNumber(number,splitArr[1].length);		
	}
	var splitArr = splitArr[0].split(",");
	if(splitArr.length!=1){
		var len = splitArr[1].length;
		var numbers = number.split(".");
		var f = numbers[0].indexOf("-")!=-1;
		numbers[0] = numbers[0].replace("-","");
		number = (f?"-":"")+formatMoney(numbers[0],len,",") + "." + numbers[1];
	}
	return number;
}
	
/*
 * @methodName    : roundNumber
 * @author        : ruanqingfeng
 * @desc          : 四舍五入
 * @for example   : roundNumber(222.223344,2)
 */
function roundNumber(thisNumber,n){
	return parseFloat(thisNumber).toFixed(n);
}


/*
 * @methodName    : formatMoney
 * @author        : ruanqingfeng
 * @desc          : 格式化金额
 * @for example   : formatMoney(222.223344,2,",")
 */
function formatMoney(str, step, splitor) { 
	str = str.toString(); 
	var len = str.length; 
	
	if(len > step) { 
		var l1 = len%step, 
		l2 = parseInt(len/step), 
		arr = [], 
		first = str.substr(0, l1); 
		if(first != '') { 
			arr.push(first); 
		}; 
		for(var i=0; i<l2 ; i++) { 
			arr.push(str.substr(l1 + i*step, step)); 
		}; 
		str = arr.join(splitor); 
	}; 
	return str; 
} 

/*
 * @methodName    : replaceMoney
 * @author        : ruanqingfeng
 * @desc          : 将带有“，”千位分隔符的金额过滤成数字类型的金额
 * @for example   : replaceMoney("11,222.223344")
 */
function replaceMoney(money){
	money = money.replace(/,/ig,"");
	
	return money;
}


