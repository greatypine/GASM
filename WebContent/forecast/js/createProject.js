var win;
/**
 * 专业分类select数组
 * 需要在专业分类处定义ID是projectCategorySelectId
 */
var projectCategorySelectId=[
	"勘探","开发","地面","物探","测井","钻井","油气管道","炼油",
	"化工","新能源与可再生能源","安全环保","节能节水","软科学","信息"
];
/**
 * 项目类型根据项目来源联动
 */
var projectTypeId=[
["国家科技部","","油气重大专项","863","973","科技支撑计划","自然基金","科技专项"]
,["国土资源部","","超前储备","石油预探","技术支持"]
,["科技管理部","","重大科技专项","超前储备技术","重大技术攻关","研发设计制造一体化","集成配套与技术推广","重大现场试验","国际合作","决策支持研究与共性技术研究","信息系统"]
,["信息管理部","","建设项目","运维项目","研究项目"]
,["勘探与生产分公司","","勘探前期","石油预探","油藏评价","油田开发","标准化","工程监督","重大专项","信息系统"]
,["海外勘探开发公司","","技术支持"]
,["研究院","","院级项目"]
,["规划计划部","","其他"]
,["安全环保与节能部","","其他"]
,["质量标准与管理部","","其他"]
,["对外合作经理部","","其他"]
,["天然气与管道分公司","","其他"]

];
/**
 * 选择所属项目
 * 1.需要传递一个项目层级
 * 2.组织机构ID
 * 3.传递一个项目名称的inputId
 * 4.传递一个项目ID的inputId
 * 5.项目Id
 */
function showProjectCheckWin(projectLevel,projectOrgId,inputNameId,inputIDId,projectId,callback){
	win = new ProjectCheckWin(
		projectLevel,projectOrgId,inputNameId,inputIDId,projectId,callback
	);
	win.show();
}
/**
 * 此方法只是为了以后扩展
 */
function getProjectManager(){
	win = new ProjectCheckWin(
	);
	win.show();
}
/**
 * 项目选择界面
 * @param {Object} projectLevel
 * @param {Object} projectOrgId
 * @param {Object} inputNameId
 * @param {Object} inputIDId
 */
var ProjectCheckWin = function(projectLevel,projectOrgId,inputNameId,inputIDId,projectId,callback){
	this.win = $("<div></div>");
		var _this = this;
		this.contentUrl = baseUrl + "/forecast/projectCheckWin.html?projectLevel="+projectLevel+'&inputNameId='+inputNameId+'&inputIDId='+inputIDId+'&projectOrgId='+projectOrgId+'&projectId='+projectId;
		var initWin = function(){
		_this.win.html('<iframe name="projectCheckName" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"选择列表",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			closeOnEscape:false,	
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },		
			buttons : {
				"确定": function(){
					window.frames["projectCheckName"].onSubmit();
					_this.win.remove();//一定要加移除div
				},
				"取消":function(){
					_this.hide();
					_this.win.remove();
				}
			},
			modal:true
		});	
	}
	this.width = 700;
	this.height = 550;
	this.callBack = function(json){
		if (callback && typeof(callback) == 'function') {
                        callback(json);
                    }
	};
	
	this.show = function(){
		_this.win.dialog("open");
	}
	
	this.hide = function(){
		if(_this.onSubmitHandler){
			_this.onSubmitHandler();	
		}
		_this.win.dialog("close");
	}
	initWin();
	
}


/**
 * 选择依托项目
 * 1.承担单位ID
 * 2.项目状态 0：草稿；1：审批中；2：退回；3：已通过；4：已删除
 * 3.单选0，多选1
 * 4.回调函数
 */
function showpurchaseForecastCheckWin(orgId,state,checkType,callback,showOrgCheck){
	win = new PurchaseForecastCheckWin(
		orgId,state,checkType,callback,showOrgCheck
	);
	win.show();
}
/**
 * 项目选择界面
 * @param {Object} projectLevel
 * @param {Object} projectOrgId
 * @param {Object} inputNameId
 * @param {Object} inputIDId
 */
var PurchaseForecastCheckWin = function(orgId,state,checkType,callback,showOrgCheck){
	this.win = $("<div></div>");
		var _this = this;
		this.contentUrl = baseUrl + "/forecast/purchaseForecastCheckWin.html?orgId="+orgId+"&state="+state+"&checkType="+checkType+"&showOrgCheck="+showOrgCheck;
		var initWin = function(){
		_this.win.html('<iframe name="purchaseForecast" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"依托项目选择列表",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定": function(){
					window.frames["purchaseForecast"].onSubmit();
					_this.win.remove();
				},
				"取消":function(){
					_this.hide();
					_this.win.remove();
				}
			},
			modal:true
		});	
	}
	this.width = 700;
	this.height = 550;
	this.callBack = function(json){
		if (callback && typeof(callback) == 'function') {
                        callback(json);
                    }
	};
	
	this.show = function(){
		_this.win.dialog("open");
	}
	
	this.hide = function(){
		if(_this.onSubmitHandler){
			_this.onSubmitHandler();	
		}
		_this.win.dialog("close");
	}
	initWin();
	
}


/**
 * 选择测试租赁加工计划依托项目
 * 1.承担单位ID
 * 2.项目状态 0：草稿；1：审批中；2：退回；3：已通过；4：已删除
 * 3.单选0，多选1
 * 4.回调函数
 */
function showExperimentForecastCheckWin(orgId,state,checkType,callback,showOrgCheck){
	win = new experimentForecastCheckWin(
			orgId,state,checkType,callback,showOrgCheck
	);
	win.show();
}
/**
 * 项目选择界面
 * @param {Object} projectLevel
 * @param {Object} projectOrgId
 * @param {Object} inputNameId
 * @param {Object} inputIDId
 */
var experimentForecastCheckWin = function(orgId,state,checkType,callback,showOrgCheck){
	this.win = $("<div></div>");
	var _this = this;
	this.contentUrl = baseUrl + "/forecast/experimentForecastListCheckWin.html?orgId="+orgId+"&state="+state+"&checkType="+checkType+"&showOrgCheck="+showOrgCheck;
	var initWin = function(){
		_this.win.html('<iframe name="experimentForecast" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"测试租赁项目选择列表",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定": function(){
					window.frames["experimentForecast"].onSubmit();
					_this.win.remove();
				},
				"取消":function(){
					_this.hide();
					_this.win.remove();
				}
			},
			modal:true
		});	
	}
	this.width = 700;
	this.height = 550;
	this.callBack = function(json){
		if (callback && typeof(callback) == 'function') {
			callback(json);
		}
	};
	
	this.show = function(){
		_this.win.dialog("open");
	}
	
	this.hide = function(){
		if(_this.onSubmitHandler){
			_this.onSubmitHandler();	
		}
		_this.win.dialog("close");
	}
	initWin();
	
}






/**
 * 项目排重校验根据项目来源,年度,项目名称查重
 * @param {Object} projectFrom
 * @param {Object} annual
 * @param {Object} projectName
 */
function checkDataRepeat(projectFrom,annual,projectName){
		var result;
		doManager("projectForecastManager","checkDataRepeat",[projectFrom,annual,projectName],function(_data){
				if(_data.result){
					var obj = $.fromJSON(_data.data);
					result=obj;
				}
		});
		return result;
}
function onSelectClick(parentSelectId,selectId){
	var selectArray;
	$("#"+selectId).empty();
	if(selectId=="projectCategorySelectId"){
		selectArray=projectCategorySelectId;
		for(var i=0;i<selectArray.length;i++){
			$("#"+selectId).append("<option value=''>"+selectArray[i]+"</option>");;
		}
	}else{
		//projectFromVal 项目来源的值
		if(parentSelectId=="0"){
			return;
		}
		var projectFromVal=$("#"+parentSelectId).val();
		selectArray=projectTypeId;
		//项目来源选择了才进行联动
		if(projectFromVal!=undefined&&projectFromVal!=null&&projectFromVal!=""){
			for(var i=0;i<selectArray.length;i++){
				if(projectFromVal==selectArray[i][0]){
					for(var j=1;j<selectArray[i].length;j++){
						$("#"+selectId).append("<option value='"+selectArray[i][j]+"'>"+selectArray[i][j]+"</option>");
					}
				}
			}
		}
	}
}

function updateProjectTypeList(projectFromVal) {
  $("#projectType").empty();
  var selectArray=projectTypeId;
  for(var i=0;i<selectArray.length;i++){
    if(projectFromVal==selectArray[i][0]){
      for(var j=1;j<selectArray[i].length;j++){
        $("#projectType").append("<option value='"+selectArray[i][j]+"'>"+selectArray[i][j]+"</option>");
      }
    }
  }
}
 
function closeProjectTree(treeId,openId,closeId){
	$("#"+treeId).hide();
	$("#"+openId).show();
	$("#"+closeId).hide();
}
function openProjectTree(treeId,projectFromId,subSelectId,openId,closeId){
	$("#"+closeId).show();
	$("#"+openId).hide();
	projectFromTree(treeId,projectFromId,subSelectId,openId,closeId);
}



//获取当前组织机构下经费编号
function getExpenseCodes(orgId,seled){
	$("#expenseCode").html("");
	$("#expenseCode")[0].add(new Option("", ""));
	doManager("expenseCodeManager", "getExpenseCodeListByOrgId", [orgId], function(data,textStatus, XMLHttpRequest) {
		if (data.result) {
			var jsonData = $.fromJSON(data.data);
			for(i=0;i<jsonData.length;i++){
				var option = new Option(jsonData[i].expenseCode+"(剩余经费："+jsonData[i].expenseBalance+"万元)", jsonData[i].expenseCode);
				$("#expenseCode")[0].add(option);
			}
		}
	},false);
	if(seled!=null) {
		$("#expenseCode").val(seled);
	}
}

/**
 * 项目来源的树
 * p1,项目来源的ID
 * p2,联动的Id
 */
function showProjectTree(projectFromId,subSelectId){
	var setting = {
					isSimpleData : true,
				    showLine: true,
				    checkable: false,
				    treeNodeKey : "id",
				    treeNodeParentKey : "pId",
					checkType : {"Y":"s","N":"ps"},
					
					
					callback: {
						click:clickProjectFromTree,
          }
		};
				
				var treeNodes = {
					name: "项目来源",
					open:"block",
					ename: "0",
					status: "0003",
					inputId: "0",
					subSelectId: subSelectId,
					nodes: [{
						name: "国家级",
						open:"block",
						ename: "01",
						status: "0003",
						inputId: "0",
						subSelectId: subSelectId,
						nodes: [{
							name: "国家科技部",
							ename: "0101",
							status: "0001",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "国土资源部",
							ename: "0102",
							status: "0002",
							inputId: projectFromId,
							subSelectId: subSelectId
						}]
					},{
						name: "集团（股份）公司",
						open:"block",
						ename: "01",
						status: "0003",
						inputId: "0",
						subSelectId: subSelectId,
						nodes: [{
							name: "科技管理部",
							ename: "0101",
							status: "0001",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "信息管理部",
							ename: "0102",
							status: "0002",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "规划计划部",
							ename: "0103",
							status: "0003",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "安全环保与节能部",
							ename: "0104",
							status: "0004",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "质量标准与管理部",
							ename: "0105",
							status: "0005",
							inputId: projectFromId,
							subSelectId: subSelectId
						} ]
					}, {
						name: "专业分公司",
						open:"block",
						ename: "02",
						status: "0003",
						inputId: "0",
						nodes: [{
							name: "勘探与生产分公司",
							ename: "0201",
							status: "0001",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "对外合作经理部",
							ename: "0202",
							status: "0002",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "天然气与管道分公司",
							ename: "0203",
							status: "0003",
							inputId: projectFromId,
							subSelectId: subSelectId
						}, {
							name: "海外勘探开发公司",
							ename: "0204",
							status: "0004",
							inputId: projectFromId,
							subSelectId: subSelectId
						}]
					}, {
						name: "研究院",
						ename: "03",
						status: "0001",
						inputId: projectFromId,
						subSelectId: subSelectId
					}]
				};
	
	
	
    var div = $("<div id='projectFromDivId'></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>");
    var ztree;
    var obj =null ;
    div.dialog({
        bgiframe: true,
        title: "项目来源选择",
        width: 300,
        height: 410,
        buttons: {
            "取消": function(){
                div.dialog("close");
                div.remove();
            }
        },
        modal: true,
        closeOnEscape: false,
        open: function(event, ui){
            $(".ui-dialog-titlebar-close").hide();
        }
    });
    checkType = "1";
    ztree = $("#treeDemo").zTree(setting, treeNodes);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}
function clickProjectFromTree(event, treeId, treeNode){
	if(treeNode.inputId=='0'){
		return;
	}
	$("#"+treeNode.inputId).val(treeNode.name);
	onSelectClick(treeNode.inputId,treeNode.subSelectId);
	$("#projectFromDivId").dialog("close");
    $("#projectFromDivId").remove();
}



/**
 * 批量审批
 */
function showToDoBatchCheckWin(callback){
	win = new ToDoBatchCheckWin(callback);
	win.show();
}
var ToDoBatchCheckWin=function(callback){
	this.win = $("<div></div>");
		var _this = this;
		this.contentUrl = baseUrl + "/forecast/toDoBatchCheckWin.html";
		var initWin = function(){
		_this.win.html('<iframe name="purchaseForecast" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"批量审批",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定": function(){
					if(window.frames["purchaseForecast"].checkOnSubmit()){
						_this.win.remove();
					}
				},
				"取消":function(){
					_this.hide();
					_this.win.remove();
				}
			},
			modal:true
		});	
	}
	this.width = 700;
	this.height = 550;
	this.callBack = function(json){
		if (callback && typeof(callback) == 'function') {
                        callback(json);
                    }
	};
	
	this.show = function(){
		_this.win.dialog("open");
	}
	
	this.hide = function(){
		if(_this.onSubmitHandler){
			_this.onSubmitHandler();	
		}
		_this.win.dialog("close");
	}
	initWin();
	
}

/**
 * 选择经费编号
 */
function selExpenseCode(orgId,callback) {
	win = new ExpenseCheckWin(orgId,callback);
	win.show();
}
var ExpenseCheckWin = function(orgId,callback){
	this.win = $("<div></div>");
		var _this = this;
		this.contentUrl = baseUrl + "/forecast/expenseCheckWin.html?orgId="+orgId;
		var initWin = function(){
		_this.win.html('<iframe name="expenseCheckName" frameborder="0" width="100%" height="100%" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:"经费编号选择列表",
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			open: function(event, ui) { $(".ui-dialog-titlebar-close").hide(); },		
			buttons : {
				"确定": function(){
					window.frames["expenseCheckName"].doSubmit();
					_this.win.remove();//一定要加移除div
				},
				"取消":function(){
					_this.hide();
					_this.win.remove();
				}
			},
			modal:true
		});	
	}
	this.width = 800;
	this.height = 550;
	this.callBack = function(json){
		if (callback && typeof(callback) == 'function') {
			callback(json);
		}
	};
	
	this.show = function(){
		_this.win.dialog("open");
	}
	
	this.hide = function(){
		if(_this.onSubmitHandler){
			_this.onSubmitHandler();	
		}
		_this.win.dialog("close");
	}
	initWin();
	
}
