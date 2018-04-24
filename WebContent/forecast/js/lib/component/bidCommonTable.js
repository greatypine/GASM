
importCss(baseUrl+"/forecast/js/lib/component/bidTable.css");
importJs(baseUrl+"/forecast/js/scripts/ajaxfileupload.js");
importJs(baseUrl+"/forecast/js/scripts/DateExtend.js");

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


var BidCommonTable = function(_cfg){ 
	this.spieclSupportColumn="rownum,checkbox,delBtn,detail";  
	this.supportColumnType = "select,search,checkbox2,checkbox,rownum,detail,label,dateLabel,hidden,number,text,textarea,date,delBtn";//目前支持的列类型（不可修改）
	this.supportValueType = "select,search,checkbox2,label,dateLabel,number,input,textarea,date,hidden";//目前支持的值类型（不可修改）
	this.renderTo = "bidCommonTable";//表格渲染的容器ID
	this.renderToDiv = null;   //渲染的DIV对象
	this.rows = 1;//当前表格记录数（不可修改）
	this.isCreateHeader = false;//表头是否已经创建（不可修改）
	this.addRowBtn = null;//"增加"按钮（不可修改）
	this.rowInitCnt = 0;//初始化时需要默认构造几行
	this.showHeader = true;//初始化时是否显示表头
	this.maxRows = 1000000000;//允许添加最大行数
	this.tableWidth = "98%";//表格宽度
	this.showAddButton = true;//是否显示"增加"按钮
	this.addRowBtnText = "添加";//“添加”按钮上的文字信息
	this.bidCommonTable = null;//表格对象
	this.headerColumnType = "simple";//表头类型（simple,groupHeader）
	this.tableColumnsShow = null;//表格的列显示配置（设置false，将不显示该列）
	this.vlidateEmable=false;
	this.isFileRecord = false;
	this.detailTxt = "报名内容";
	this.detailFun;
	
	this.ckcol = [];
	this.store = null;
	this.createSortNum = false;
	//this.columns=[];
	
	 this.columns = [
    ],

	this.tableHeaders = [];//表头
	
	$.extend(this,_cfg);
	
	/*
	* 为表格增加事件监听
	*/
	this.addListener = this.on = function (_eventName,_callBack){
		$(this).unbind(_eventName);
		$(this).bind(_eventName,function(ev,param1,param2,param3){
			$.event.returnValue = _callBack(param1,param2,param3);
		});
	}
	
	/*
	* 删除表格方法
	*/
	this.removeTable = function(){
		this.rows = 1;
		if (this.bidCommonTable != null) {
			this.bidCommonTable.remove();
		}
		if(this.addRowBtn!=null){
			this.addRowBtn.remove();	
		}
		this.isCreateHeader = false;
		this.ckcol=[];
	}
	
	/*
	* 创建表格方法
	*/
	this.createTable = function(){
		this.removeTable();
		if(this.rowInitCnt>0){
			this.showHeader = true;	
		}
		this.bidCommonTable = $("<table class='bidCommonTable'  style='border-collapse:collapse;width:"+this.tableWidth+"'></table>");
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
		this.renderToDiv.append(this.bidCommonTable);//表格
		
		//this.addEventListener();
		
	};
	
	/*
	* 创建表头方法
	*/
	this.createHeader = function(){//渲染表头
		var tableHeader = $("<thead></thead>");
		this.bidCommonTable.append(tableHeader);
		var headerTr = $("<tr></tr>");
		tableHeader.append(headerTr);
		//暂时只支持单行表头
		for(var i=0;i<this.columns.length;i++){
			var obj=this.columns[i];
			if(obj.xtype =='checkbox'){
				headerTr.append($("<th class='uploadFileTableHeader uploadFileTableBorder' width='"+obj.width+"'><span><input type='checkbox' />&nbsp;"+obj.header+"</span></th>"));
			}else{
				headerTr.append($("<th class='uploadFileTableHeader uploadFileTableBorder' width='"+obj.width+"'>"+obj.header+"</th>"));
			}		
		}
		this.isCreateHeader=true;
	};
	
	/*
	* 创建隐藏行，主要是用来控制列宽度，此方法外部不建议调用
	*/
//	this.createHiddenRow = function(){
//		var tr = $("<tr id='bidTableHiidenRow' style='visibility:hidden'></tr>");
//		for(var i=0;i<this.tableColumnsType.length;i++){
//			var td = $("<td width='"+this.tableColumnsWidth[i]+"'>&nbsp;</td>");
//			tr.append(td);
//		}
//		$(this.bidTable).append(tr);
//		this.headerColumnType = "groupHeader";
//		return tr;
//	};
	
	/*
	* 删除隐藏行，此方法外部不建议调用
	*/
//	this.removeHiddenRow = function(){
//		var tr = $(this.bidTable).children().eq(1).children().eq(0);
//		if(tr.attr("id")=="bidTableHiidenRow"){
//			tr.remove();
//		}
//	};	
	
	/*
	 * 创建明细显示行
	 */
//	this.createDetailRow = function(_curTr,_content){
//		_curTr.attr("detailId",this.detailId);
//		var detailTr = $("<tr detailId='detailId_"+(this.detailId++)+"' style='line-height: 22px;background-color:#ffffff;'></tr>");
//		var detailTd = $("<td class='uploadFileTableBorder' colspan="+this.tableColumnsWidth.length+"></td>");
//		detailTd.append(_content);
//		detailTr.append(detailTd);
//		_curTr.after(detailTr);
//    }
	var _this = this;
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
		for(var i=0;i<this.columns.length;i++){
			var isShowColumn = "";
			if(this.tableColumnsShow!=null&&this.tableColumnsShow[i]==false){
				isShowColumn = "style='display:none'";
			}
			var td = $("<td "+isShowColumn+" class='uploadFileTableBorder' width='"+this.columns[i].width+"'></td>");
			
			//扩展隐藏域的情形
			if(this.columns[i].hiddenBoxs!=null){
				if(isArray(this.columns[i].hiddenBoxs)){
					var objHidd=this.columns[i].hiddenBoxs;
					for(var j=0;j<objHidd.length;j++){
						var val="";
						if(_data!=null){
							val=_data[objHidd[j].name];
						}
						var obj = $("<input  name='"+objHidd[j].name+"' type='hidden' value='"+val+"' />");	
						td.append(obj);
					}
				}	
			}
			
		
		 var validateStr = "";
	    if(this.columns[i].validateCfg !=null){
	    	if(typeof(this.columns[i].validateCfg[this.columns[i].name])=="string"){
				validateStr = " validate=" + this.columns[i].validateCfg[this.columns[i].name] + " ";	
			}else{
				validateStr = " validate=" + $.toJSON(this.columns[i].validateCfg[this.columns[i].name]) + " ";	
			}
	    }
	   
	    	if(validateStr!=""){
	    		$(td).css("white-space","normal");
	    	}
	
	       if(this.columns[i].xtype =='checkbox'){
	       		var obj = $("<input  type='checkbox'  />");	
	       		this.ckcol.push(obj);
	       		$(td).css("text-align","center");
	       		$(td).css("vertical-align","middle");
				td.append(obj);
	       } 
	       else if(this.columns[i].xtype =='rownum'){
	       		var obj  = $("<span>"+this.rows+"</span>");
				this.rows++;
				$(td).css("text-align","center");
				td.append(obj);
	       }else if(this.columns[i].xtype =='delBtn'){
			   
			   var value="class='uploadFileTableBtn' value='删除'";
				var obj = $("<input bidTableFlag=true "+value+" type='button' />");
				var _this = this;
				var _index = (parseInt(_this.rows)-1);
				obj.bind("click",function(){
					
					_this.rows = parseInt(_this.rows)-1;
					var currRowNum = tr.find("span").html();
					_this.ckcol.splice(parseInt(currRowNum)-1,1);
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
				});
				td.append(obj);
	       }else if(this.columns[i].xtype == 'label'){
	       	   var val = "";
			   if(_data!=null){
				val = _data[this.columns[i].name];
				val = (val == null ? "" : val);
				}
			    var obj = $("<label class='bidTableLabel' name='"+this.columns[i].name+"' >"+val+"</label>");
				td.append(obj);
	       }else if(this.columns[i].xtype == "detail"){
	    	   var val = "";
				var obj = $("<a class='detail' href='javascript:void(0)'>"+this.detailTxt+"</a>");
				td.append(obj);
				var _this = this;
				var id = tr.find("input").eq(2).val();
				obj.bind("click",function(){
					_this.detailFun(id);
				})
		   }else if(this.columns[i].xtype == 'text'){
	       	   var val = "";
			   if(_data!=null){
				val = _data[this.columns[i].name];
				val = (val == null ? "" : val);
				}
				var obj = $("<input "+validateStr+" elementName='"+this.columns[i].header+"' id='"+this.columns[i].name+this.rows+"' name='"+this.columns[i].name+"' revalidate='true' class='biddingTableTextBorder' style='width:96%' type='text' value='"+val+"' />");	
				if(this.vlidateEmable){
					obj.attr("validate","");
				}
				td.append(obj);
	       }
			td.addClass("uploadFileTableDataCenter");
			tr.append(td);	
		}

		$(this.bidCommonTable).append(tr);
		$(this).trigger("afterAddRow",[tr]);
		this.addEventListener();
		return tr;
	};
	
	
	
	
	this.getBidCommonTable = function(){
	    return this.bidCommonTable;
	}
	
	
	
	/*
	* 获取表格中某一行的值，以对象形式返回
	*/

	
	
	/*
	* 获取表格中某一行的值，以对象形式返回
	*/
	this.getValueByRr = function(_tr){
		
	}
	
	/*
	* 获取表格中的值，以数组形式返回
	*/
	this.getValues = function(){
		
		var data = [];
		var tableTrs = $(this.bidCommonTable).find("tr");	
		var headerCnt = 1;
		if(this.headerColumnType!="simple"){
			//headerCnt = this.tableColumns.length;
		}
		for(var i=headerCnt;i<tableTrs.length;i++){
			var curTr = $(tableTrs[i]);
			var trValue = {};
			for(var j=0;j<this.columns.length;j++){
				var column=this.columns[j];
				
				//扩展隐藏域的情形
				if(column.hiddenBoxs!=null){
					if(isArray(column.hiddenBoxs)){
						var objHidd=column.hiddenBoxs;
						for(var p=0;p<objHidd.length;p++){
							var hdObj=curTr.find(":input[name='"+objHidd[p].name+"']")[0];
							trValue[objHidd[p].name]=$(hdObj).val();
						}
					}	
				}
				
			 	if(column.xtype == 'checkbox' || column.xtype == 'rownum' ){
	       			continue;
	         	}else if(column.xtype =='label'){
	       			var hdObj=curTr.find(":[name='"+column.name+"']")[0];
					trValue[column.name]=$(hdObj).text();
	         	}else if(column.xtype == 'text'){
	       			var hdObj=curTr.find(":input[name='"+column.name+"']")[0];
					trValue[column.name]=$(hdObj).val();
	         	}   
		
			}
			data.push(trValue);
		}
		return data;
	};
	
	
	/*
	* 获取表格中某一行的值，以对象形式返回
	*/
	this.getValueByRr = function(_tr){
		var curTr = _tr;
		var trValue = {};
			for(var j=0;j<this.columns.length;j++){
				
				var column=this.columns[j];
				
				//扩展隐藏域的情形
				if(column.hiddenBoxs!=null){
					if(isArray(column.hiddenBoxs)){
						var objHidd=column.hiddenBoxs;
						for(var p=0;p<objHidd.length;p++){
							var hdObj=curTr.find(":input[name='"+objHidd[p].name+"']")[0];
							trValue[objHidd[p].name]=$(hdObj).val();
						}
					}	
				}
				
			 	if(column.xtype == 'checkbox' || column.xtype == 'rownum' ){
	       			continue;
	         	}else if(column.xtype =='label'){
	       			var hdObj=curTr.find(":[name='"+column.name+"']")[0];
					trValue[column.name]=$(hdObj).text();
	         	}else if(column.xtype == 'text'){
	       			var hdObj=curTr.find(":input[name='"+column.name+"']")[0];
					trValue[column.name]=$(hdObj).val();
	         	}
			} 	
		return trValue;
	}
	
	
	/*
	* 设置表格中的值，参数以数组形式传入
	*/
	this.setValues = function(_data){
		this.createTable();
		for(var i=0;i<_data.length;i++){
			var obj = _data[i];
			var tr = this.createRow(obj);
		}
		
	}
	
	this.createTable();
	
	
};
var bidCommonTables = [];
$(function(){
	$("div[class*='bidCommonTable']").each(function(i,element){
		var defaults = $(this).attr("defaults");
		if(defaults==null||defaults==""){
			defaults = "{}";	
		}
		eval("var defaults ="+defaults);	
		defaults.tableIndex = i;
		defaults.renderToDiv= $(element);
		var obj = new BidCommonTable(defaults);
		bidCommonTables.push(obj);
	});
})


/**
 * selectAll & cancelAll.
 * @param {Object} head
 * 				the check head
 * @param {Object} cks
 * 				the check children
 */
function setCheckStatus(head, cks,_this) {
	head = $(head);
	cks = $(cks);
	head.click(function() {
		var checked = $(this).attr('checked'); 
		$(cks).each(function(i){
			$(this).attr('checked', checked);
		});
	});
	
	var filedNames=[];
	var validateCfgs=[];
	if(_this.vlidateEmable){
	    for(var i=0;i<_this.columns.length;i++){
			if(_this.columns[i].validateCfg != null){
				filedNames.push(_this.columns[i].name);
				if(typeof(_this.columns[i].validateCfg[_this.columns[i].name])=="string"){
					validateCfgs.push(_this.columns[i].validateCfg[_this.columns[i].name]);	
				}else{
					validateCfgs.push($.toJSON(_this.columns[i].validateCfg[_this.columns[i].name]));
				}		
			}
	  	}
	}
	cks.each(function(i) {
		$(this).click(function(i) {
			if(_this.vlidateEmable){
				for(var k=0; k<cks.length; k++) {
				if($(cks[k]).attr('checked')){
					var _tr=$(cks[k]).parent().parent();
					for(var j=0;j<filedNames.length;j++){
						var obj=_tr.find(":[name='"+filedNames[j]+ "']");
						$(obj[0]).removeData();
						$(obj[0]).attr("validate",validateCfgs[j]);   
					}
				}else{
					var _tr=$(cks[k]).parent().parent();
					for(var j=0;j<filedNames.length;j++){
						var obj=_tr.find(":[name='"+filedNames[j]+ "']");
						//$(obj[0]).attr("validate",validateCfgs[j]);
						$(obj[0]).removeData();
						$(obj[0]).removeAttr("validate");       
					}	
				}
				}
			}

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

BidCommonTable.prototype.addEventListener = function() {
	var ths = this.bidCommonTable.find("th");
	var ckhead=ths.find(":input[type='checkbox']");
	var _this=this;
	if(ckhead && ckhead.length==1 && this.ckcol.length > 0) {
		var head = ckhead;
		var cks = this.ckcol;
		setCheckStatus(head, cks,_this);
	}
}

//BidCommonTable.prototype.setSelectedValidate = function() {
//	var filedNames=[];
//	var validateCfgs=[];
//	for(var i=0;i<this.columns.length;i++){
//		if(this.columns[i].validateCfg != null){
//			filedNames.push(this.columns[i].name);
//			if(typeof(this.columns[i].validateCfg[this.columns[i].name])=="string"){
//				validateCfgs.push(this.columns[i].validateCfg[this.columns[i].name]);	
//			}else{
//				validateCfgs.push($.toJSON(this.columns[i].validateCfg[this.columns[i].name]));
//			}		
//		}
//	}
//	var ckbs = $(this.ckcol);
//	var _this=this;
//	ckbs.each(function(i) {
//		if(!$(this).attr('checked')) {
//			var _tr=$(this).parent().parent();
//			for(var j=0;j<filedNames.length;j++){
//				var obj=_tr.find(":[name='"+filedNames[j]+ "']");
//				$(obj[0]).removeAttr("validate");       
//			}
//		}else{
//			var _tr=$(this).parent().parent();
//			for(var j=0;j<filedNames.length;j++){
//				var obj=_tr.find(":[name='"+filedNames[j]+ "']");
//				alert(validateCfgs[j]);
//				$(obj[0]).attr("validate",validateCfgs[j]);       
//			}
//		}
//	});
//}

BidCommonTable.prototype.getSelectedObj = function() {
	var ckbs = $(this.ckcol);
	var objs = new Array();
	var _this=this;
	ckbs.each(function(i) {
		if($(this).attr('checked')) {
			var _tr=$(this).parent().parent();
			var val = _this.getValueByRr(_tr);
			if(_this.isFileRecord){
				val.packageId = $(_tr).find("input[type=hidden][name='tenderStageId']").eq(0).val();	
				val.packageName = $(_tr).find("input[type=text][name='tenderStageName']").eq(0).val();	
			}
			val.myCommonTableTrIndex = i;
			objs.push(val);
		}
	});
	return objs;
}


BidCommonTable.prototype.getdisSelectedObj = function() {
	var ckbs = $(this.ckcol);
	var objs = new Array();
	var _this=this;
	ckbs.each(function(i) {
		if(!$(this).attr('checked')) {
			var _tr=$(this).parent().parent();
			objs.push(_this.getValueByRr(_tr));
		}
	});
	return objs;
}


BidCommonTable.prototype.setValuesBydatas = function(_data) {
		for(var i=0;i<_data.length;i++){
			var obj = _data[i];
			var tr = this.createRow(obj);	
		}	
}


BidCommonTable.prototype.removeSelectedTr = function() {
	var datas= this.getdisSelectedObj();
	this.setValues(datas);	
}






//function createDateInput(tr){
//	var elements = $(tr).find("input[bidType='date']");
//	var rootpath = getRootPath();
//	var iconpath = rootpath + "/scripts/common/images/calendar.gif";
//	$(elements).each(function(i) {
//		var format = $(this).attr("format") ? 
//				$(this).attr("format") : "yy-mm-dd";
//		var changeMonth = $(this).attr("changeMonth") == 'false' ? 
//				 false : true;
//		var changeYear = $(this).attr("changeYear") == 'false' ? 
//				 false : true;
//		var yearRange = $(this).attr("yearRange") ? 
//				$(this).attr("yearRange") : "-10:+10";
//		$(this).datepicker({
//			showOn: "button",
//			buttonImage: iconpath,
//			buttonImageOnly: true,
//			changeMonth: changeMonth,
//			changeYear: changeYear,
//			yearRange:yearRange,
//			dateFormat:format
//		});
//	});
//}



