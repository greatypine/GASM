/*
* @Object       : FileUploadTable
* @author       : ruanqingfeng
* @version      : 上传组件2.0版本
* @desc         : （新增功能：1:一个页面允许多个上传组件table，多个组件被维护在fileUploadTables数组变量中，可通过数组索引到某个uploadTable）
* @desc         : （新增功能：2:支持表格改变属性后重新渲染）
* @for example  : 将本js引入后，在需要显示组件的地方加入<div class="uploadTable"></div>即可
*/

var FileUploadTableForApproval = function(_cfg){
	this.simpleUploadTable = false;
	this.businessType = "document";
	this.uploadActionUrl   = getRootPath() + '/uploaderAction.action?businessType=';//上传附件的后台处理URL
	this.downloadActionUrl = getRootPath() + '/DownloaderAction.action';//下载附件的后台处理URL
	this.delFileManager = {//删除已经上传后的表格行（需要删除后台数据）
		managerName : "attachmentManager",//对应的后台Manager
		managerMethod : "deleteByAttachmentId"//对应的后台Manager方法
	};
	this.loadDataManager= {  //载入页面数据的manager
		managerName:"",  //manager名
		managerMethod:"", //manager的方法名
		params:[]        //参数
	};
	this.dataFieldName= {fileName:"fileName",createDate:"createDate",createUserName:"createUserName",attachmentComment:"attachmentComment",id:"id", attachmentId:"attachmentId"};
	this.autoShow = true;//是否自动渲染
	this.renderTo = "approvalUploadTable";//表格渲染的容器Class
	this.rowInitCnt = 0;//初始化时需要默认构造几行
	this.showHeader = false;//初始化时是否显示表头
	this.maxRows    = 10;//允许添加最大行数
	this.tableWidth = "98%";//表格宽度
	this.showAddButton = true;//是否显示"增加"按钮
	this.tableColumnsWidth = ["5%","13%","7%","50%","20%","5%"];//表格宽度设置
	this.tableColumnsShow  = [true,true,true,true,false,false,false];//表格的列显示配置（设置false，将不显示该列）
	this.tableColumnsType  = ["rownum","label","label","label","label","label","label"];//列类型
	this.tableColumns      = ["序号","附件名","上载日期","上载用户","备注","id","attachmentId"];//表头（不可修改）
	this.isCreateHeader  = false;//表头是否已经创建（不可修改）
	this.addFileBtn      = null;//"增加"按钮（不可修改）
	this.fileUploadTable = null;//上传表格对象（不可修改）
	this.rows = 1;//当前表格记录数（不可修改）
	this.delFileBtnIndex = 1;//删除按钮索引（不可修改）
	$.extend(this,_cfg);
	this.render = function(_cfg){
		this.rows = 1;
		if(_cfg == null){
			_cfg = {};
		}
		$.extend(this,_cfg);
		if(this.addFileBtn!=null){
			this.addFileBtn.remove();
		}
		if (this.fileUploadTable != null) {
			this.fileUploadTable.remove();
		}
		this.isCreateHeader = false;
		this.createTable();
	};
	this.uploadActionUrl = this.uploadActionUrl + this.businessType;	
	
	var listeners = {
		beforeDownload : "beforeDownload"
	}//表格支持的事件类型（不可修改
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
	* @Method       : readerTable
	* @desc         : 根据后台数组数据，呈现一个只读的上传组件表格，该表格所有内容均不允许动态修改
	* @for example  : var data = [{value1:key1},{value2:key2}]
	*               : fileUploadTables[0].readerTable(data)
	*/
	this.readerTable = function(_data){
		this.showAddButton = false;
		this.tableColumnsShow = [true,true,true,true,false,false];
	  	this.tableColumnsType = ["rownum","label","label","label","file","button"];
		this.setValues(_data);
	};
	
	/*
	 * 创建增加按钮(如果showAddButton为true，则创建，否则将不创建)----此方法外部不允许调用
	 */
	var createAddBtn = function(_this){
		if (_this.showAddButton) {
			_this.addFileBtn = $('<input type="button" class="uploadFileTableBtn"  value="添加附件" />');
		
			_this.addFileBtn.bind("click", function(){
				_this.createRow();
			});
			$("div[class*='"+_this.renderTo+"']").eq(_this.tableIndex).append(_this.addFileBtn);//增加按钮
		}
	}
	
	/*
	* 创建表格方法
	*/
	this.createTable = function(){
		if(this.rowInitCnt>0){
			this.showHeader = true;	
		}
		this.fileUploadTable = $("<table class='uploadFileTable' width="+this.tableWidth+"></table>");
		
		var renderToDiv = $("div[class*='"+this.renderTo+"']").eq(this.tableIndex);
		createAddBtn(this);
		if(this.showHeader){
			this.createHeader();
		}
		
		for(var i=0;i<this.rowInitCnt;i++){
			this.createRow();
		}
		renderToDiv.append(this.fileUploadTable);//表格
		
	};
	
	/*
	* 创建表头方法
	*/
	this.createHeader = function(){//渲染表头
		if(this.isCreateHeader){
			return;
		}
		var tableHeader = $("<thead></thead>");
		this.fileUploadTable.append(tableHeader);
		
		var headerTr = $("<tr></tr>");
		tableHeader.append(headerTr);
		for(var i=0;i<this.tableColumns.length;i++){
			var isShowColumn = "";
			if(this.tableColumnsShow[i]==false){
				isShowColumn = "displayNone";
			}
			headerTr.append($("<th class='uploadFileTableHeader uploadFileTableBorder "+isShowColumn+"' width='"+this.tableColumnsWidth[i]+"'>"+this.tableColumns[i]+"</th>"));	
		}
		this.isCreateHeader=true;
	};
	
	/*
	* 新增加一行方法
	*/
	this.createRow = function(){
		
		if(this.rows>this.maxRows){
			$$.showMessage("警告","您最多只能添加"+this.maxRows+"行附件！");
			return false;	
		}
		if(this.isCreateHeader==false){
			this.createHeader();
		}

		
		
		var tr = $("<tr class='uploadTableRowLineHeight'></tr>");
		for(var i=0;i<this.tableColumns.length;i++){
			var isShowColumn = "";
			if(this.tableColumnsShow[i]==false){
				isShowColumn = "displayNone";
			}
			var td = $("<td  class='uploadFileTableBorder "+isShowColumn+"'></td>");
			if(this.tableColumnsType[i]=="rownum"){
				td.append($("<span>"+this.rows+"</span>"));
				td.append($("<input type='hidden' name='attachmentId' id='attachmentId'/>"));
				td.addClass("uploadFileTableDataCenter");
				this.rows++;
			}else if(this.tableColumnsType[i]=="label"){
				td.append($(""));	
				td.addClass("uploadFileTableDataCenter");
			}else if(this.tableColumnsType[i]=="text"){
				td.append($("<input disabled validate='maxlength:200' id='uploadTableNotes"+(this.rows-2)+"' class='fileInputWidth inputtext_dis' type='"+this.tableColumnsType[i]+"' />"));	
			}else if(this.tableColumnsType[i]=="file"){
				var fileBtn = $("<input type='"+this.tableColumnsType[i]+"' class='fileInputWidth inputtext_dis' name='attach"+(this.delFileBtnIndex)+"'id='attach"+(this.delFileBtnIndex++)+"'/>");
				var _this = this;
				fileBtn.bind("change",function(){
					_this.upload(this,tr);
				});
				td.append(fileBtn);	
			}else if(this.tableColumnsType[i]=="button"){
				var value = "";
				value="class='uploadFileTableBtn' value='删除'";
				td.addClass("uploadFileTableDataCenter");
				td.append($("<input type='hidden' name='uploadFileId' />"));
				var delBtn = $("<input "+value+" type='"+this.tableColumnsType[i]+"'/>");
				var _this = this;
				delBtn.bind("click",function(){
					var fileId = ($(tr).children().eq(0).find("input").eq(0).val());
					var delFun = function(){
						_this.rows = parseInt(_this.rows)-1;;
						var currRowNum = tr.find("span").html();
						tr.siblings().each(function(){
							var sibingsSpan = $(this).find("span").eq(0);
							var siblingsRowNum = sibingsSpan.html();	
							
							if(parseInt(siblingsRowNum)>parseInt(currRowNum)){
								sibingsSpan.html(parseInt(siblingsRowNum)-1);	
							}
						})
						tr.remove();
						_this.setRowBgClass();
					}
					if(fileId==""){
						delFun();
					}else{
						var delParam = new PMSRequestObject(_this.delFileManager.managerName,_this.delFileManager.managerMethod,[fileId],null); 
						$$.ajax($$.PMSDispatchActionUrl, "requestString=" + encodeURIComponent(delParam.toJsonString()),function(data){
							if(data.result){
								datas=$.fromJSON(data.data);
								if(datas=="success") {
									delFun();
								}else{
								  alert(datas);
								}
							}
						});
						
					}
					
				});
				td.append(delBtn);	
			}
			tr.append(td);	
		}
		$(this.fileUploadTable).append(tr);	
		this.setRowBgClass();
		return tr;
	};
	
	/*
	 * 设置奇偶数行样式
	 */
	this.setRowBgClass = function(){
		
		$(this.fileUploadTable).find("tbody tr").removeClass();
		$(this.fileUploadTable).find("tbody tr:even").addClass("uploadTableRow_evenBg");
		$(this.fileUploadTable).find("tbody tr:odd").addClass("uploadTableRow_oddBg");
	}
	
	/*
	* 上传附件方法
	*/
	this.upload = function(_file,_tr){
		var rowNumTd = _tr.children().eq(0);
		var fileNameTd = _tr.children().eq(1);
		var fileTypeTd = _tr.children().eq(2);
		var fileComTd = _tr.children().eq(3);
		var attach = _file.id;
		$.handleError = function(){};
		$PD.display();
		var _this = this;
		$.ajaxFileUpload
		(
			{
				url:this.uploadActionUrl,
				secureuri:false,
				fileElementId:attach,
				dataType: 'json',
				success: function (data, status)
				{
					if(data.message == "File suffix is not allowed.") {
						$$.showMessage('${query.ui.prompt}','对不起！不支持该类型附件！');
					}
					if(data.message == "File size is too large.") {
						$$.showMessage('${query.ui.prompt}','对不起！您上传的文件过大！');
					}
					var datas = $.fromJSON(data.data);	
					
					
					var htmlObj = $("<a class='downloadFontClass' href='"+_this.downloadActionUrl+"?id="+datas.id+"&skip=true'>" + datas.name + "</a>");
					var attId = datas.id;
					htmlObj.bind("click",function(){
						return _this.downloadFile(attId);
					})
					
					fileNameTd.html("");
					fileNameTd.append(htmlObj);
					rowNumTd.children().eq(1).val(datas.id);
					fileTypeTd.html(datas.fileSuffix);
					
					var f_c_0 = fileComTd.children().eq(0);
					f_c_0.removeClass("inputtext_dis");
					f_c_0.addClass("inputtextshort");
					f_c_0.attr("disabled",false);
					
				},
				error: function (data, status, e)
				{
					$$.showMessage('${query.ui.prompt}',e);
				}
			}
		)
	};
	
	/*
	 * 下载表格中指定行的附件
	 */
	this.downloadFile = function(fileId){
	 	$(this).trigger(listeners.beforeDownload,[fileId]);
		if($.event.returnValue==false){
			return false;	
		}
	}
	
	/*
	* 获取表格中的值，以数组形式返回
	*/
	this.getValues = function(){
		var data = [];
		var tableTrs = $(this.fileUploadTable).find("tr");	
		for(var i=1;i<tableTrs.length;i++){
			var curTr = $(tableTrs[i]);
			var aId = $(curTr.children().eq(0)).find("input").val();
			if(aId==""||aId==null){
				continue;	
			}
			var trValue = {
				attachmentId:aId,
				fileName:$(curTr.children().eq(1).children().eq(0)).html(),
				fileType:$(curTr.children().eq(2)).html(),
				attachmentComment:$(curTr.children().eq(3)).find("input").val(),
				id:($(curTr).children().eq(5).find("input").eq(0).val())
				
			};
			data.push(trValue);
		}
		return data;
	};
	
	/*
	* 设置表格中的值，参数以数组形式传入
	*/
	this.setValues = function(_data){
		this.render();
		for(var i=0;i<_data.length;i++){
			var obj = _data[i];
			var tr = this.createRow(this.fileUploadTable);
			$(tr.children().eq(0)).find("input").val(eval("obj."+this.dataFieldName.attachmentId));
			
			var htmlObj = $("<a class='downloadFontClass' href='"+this.downloadActionUrl+"?id=" + eval("obj."+this.dataFieldName.attachmentId) + "&skip=true'>" + eval("obj."+this.dataFieldName.fileName) + "</a>");
			var attId = eval("obj."+this.dataFieldName.attachmentId);
			var _this = this;
			htmlObj.bind("click",function(){
				return _this.downloadFile(attId);
			})
			$(tr.children().eq(1)).append(htmlObj);
			if (this.simpleUploadTable) {
				$(tr.children().eq(2)).html(eval("obj." + this.dataFieldName.fileType));
				var t_c_0 = $(tr.children().eq(3));
				if (t_c_0.find("input").length != 0) {
					t_c_0.find("input").val(eval("obj." + this.dataFieldName.attachmentComment));
					t_c_0.find("input").removeClass("inputtext_dis");
					t_c_0.find("input").addClass("inputtextshort");
					t_c_0.find("input").attr("disabled", false);
				}
				else {
					t_c_0.html(eval("obj." + this.dataFieldName.attachmentComment));
				}
				if (tr.children().length >= 5) {
					var id = (obj.id == null ? "" : obj.id);
					($(tr).children().eq(5).find("input").eq(0).val(id));
				}
			} else {
				var n = 1;
				for(var key in this.dataFieldName) {
					if (key == "fileName") {
						continue;
					}
					else {
						if (key == "createDate") {
							$(tr.children().eq(n + 1)).html(new Date(parseInt(eval("obj." + this.dataFieldName[key]))).format("yyyy-MM-dd"));
							
						}
						else {
							$(tr.children().eq(n + 1)).html(eval("obj." + this.dataFieldName[key]));
						}
						n++;
					}
				}
			}
			
		}
	}
	
	/*
	* 初始化表格数据
	*/
	this.initTableData = function(){
		var datas=[];
		var url = $$.PMSDispatchActionUrl;
	  var data = new PMSRequestObject(this.loadDataManager.managerName, this.loadDataManager.managerMethod, this.loadDataManager.params);
		var _this = this;
		$$.ajax(url, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest){
	        datas = $.fromJSON(data.data);//把得到的JSON串转换成数组形式
			_this.setValues(datas);
	    });
	}
	
	if(this.autoShow){
		this.createTable();
		if(this.loadDataManager.managerName!=""&&this.loadDataManager.managerMethod!=""){
			this.initTableData();
		}
	}
};

FileUploadTableForApproval.items = {};
$(function(){
	$("div[class*='approvalUploadTable']").each(function(i){
		var defaults = $(this).attr("defaults");
		if(defaults==null||defaults==""){
			defaults = "{}";	
		}
		eval("var defaults ="+defaults);	
		defaults.tableIndex = i;
		var obj = new FileUploadTableForApproval(defaults);
		FileUploadTableForApproval.items[$(this).attr("id")] = obj;
	});
})
