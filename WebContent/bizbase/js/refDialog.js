function startWith(a,b){
	if(a.length<b.length){
		return -1;
	}
	for(var i=0;i<a.length;i++){
		if(b==a.charAt(i)){
			return i;
		}
	}
	return -1;
}

function openDialog(id){
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:300px;height:200px'></ul>");
    var ztree;
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 500,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
                var refId = refNodes[0].code;
                var refValue = refNodes[0].name;
                $("#" + id).val(refId);
                $("#" + id).attr("orgId", refId);
                div.dialog("close");
                div.remove();
            },
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
    ztree = generatTree("1");
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

/**
 * 页面上自己定义显示域(name)和隐藏域(id)
 * @param {Object} id 显示域(name)的ID
 * @param {Object} hiddenId 隐藏域(id)的ID
 * @param {Object} orgTreeType '1'专家推荐单位 2 专家工作单位 3 回避单位  4
 * @param (Object) callback 用于信息填充后所要进行的操作比如校验，可以根据需要添加。
 */
function openDialogWithHidden(id, hiddenId, orgTreeType, callback){
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%'></ul>");
    var ztree;
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 500,
        height: 320,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
                if (refNodes.length > 0) {
                    var refId = refNodes[0].code;
                    var refValue = refNodes[0].name;
					var p = startWith(refValue,')');
					refValue = refValue.substring(p+1,refValue.length);
                    $("#" + id).val(refValue);
                    $("#" + hiddenId).val(refId);
                    if (callback && (typeof callback == 'function')) {
                        callback();
                    }
                    div.dialog("close");
                    div.remove();
                }
            },
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
    if (orgTreeType == '1') {
        ztree = generatTree('1', 3);
    }else if(orgTreeType=='5'){
		 ztree = generatTree('1', 6);
	}
    else {
        ztree = generatTree('1');
    }
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

/**
 * 打开单位选择对话框
 * @param callback  回调函数
 * @param checkTypet  选择类型 radio:单选 check:多选
 * @param orgFlag 需要过滤的单位属性
 * @param checkCodeStr 已选择的编号串格式如下：A,B,C  ，用于自动勾选已选择的记录
 */
function myOpenOrgSelDialog(callback,checkType,orgFlag,checkCodeStr) {
  if (!checkCodeStr) {
    checkCodeStr="";
  }
  myOpenDialogWithJson(callback, checkType, orgFlag,checkCodeStr);
}

/**
 * 组装json并调用回调函数
 * @param {Object} callback
 * @param {String} checkType: 可选类型"check","radio"
 * @param  orgFlag 单位属性
 * @param  checkCodeStr 已选择的单位编号
 */
function myOpenDialogWithJson(callback,checkType,orgFlag,checkCodeStr){
  if (!checkCodeStr) {
    checkCodeStr="";
  }
  var div = $("<div></div>");
  div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>");
  var ztree;
  var json = '[';
  div.dialog({
    bgiframe: true,
    title: "组织机构选择",
    width: 500,
    height: 300,
    buttons: {
      "确定": function(){
        var refNodes = ztree.getCheckedNodes();
        ztree.getnode
        if (refNodes.length > 0) {
          for (var i = 0; i < refNodes.length; i++) {
            var temp = refNodes[i].name;
            var p = startWith(temp,')');
            temp = temp.substring(p+1,temp.length);
            json += '{"name":"' + temp + '","id":"' + refNodes[i].id + '","code":"' + refNodes[i].code + '"},';
          }
          json = json.substr(0, json.length - 1) + "]";
          if (callback && typeof(callback) == 'function') {
            callback(json);
          }
          div.dialog("close");
          div.remove();
        }
      },
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
  checkType = (checkType == "radio" ? "1" : "2");
  ztree = myGeneratTree(checkType, orgFlag,checkCodeStr);
  $("div").queue(function(){
    $(this).addClass("newcolor");
    $(this).dequeue();
  });
}

/**
 * 通过type组件 1单选  否则 多选,
 * orgFlag 帮助到的单位层级
 * @param {Object} type
 * 真是看不明白这是写的写什么东东，括号套的都逆天了。FK
 */
function myGeneratTree(type,orgFlag,checkCodeStr){
  if (!checkCodeStr) {
    checkCodeStr="";
  }
  var zcheckCodeStr=checkCodeStr;
  if (zcheckCodeStr!="") {
    if (zcheckCodeStr.charAt(0)!=",") {
      zcheckCodeStr=","+zcheckCodeStr;
    }
    if (zcheckCodeStr.charAt(zcheckCodeStr.length-1)!=",") {
      zcheckCodeStr=zcheckCodeStr+",";
    }
  }
  var zTree1;
  var setting;
  var myData;
  var zNodes = [];

  myData = $.toJSON({
    managerName: "purStruOrgManager",
    methodName: "getOrgByFlag",
    parameters: [orgFlag]
  });

  //单选
  if (type == '1') {
    setting = {
      checkable: true,
      checkStyle: "radio",
      checkRadioType: "all",
      async: true,
      asyncParam: ["name", "id"],
      asyncParamOther: ["requestString", myData],
      checkType: {
        "Y": "",
        "N": ""
      }
    }

  //多选
  } else {
    setting = {
      checkable: true,
      checkStyle: "checkbox",
      async: true,
      asyncParam: ["name", "id"],
      asyncParamOther: ["requestString", myData],
      checkType: {
        "Y": "",
        "N": ""
      }
    }
  }

  var url = $$.PMSDispatchActionUrl;
  setting.asyncUrl = url;
  zTree1 = $("#treeDemo").zTree(setting, zNodes);
  setTimeout(function() {
    //处理根结点选中状态
    var tmpAllNodes=zTree1.getNodes();
    $.each(tmpAllNodes,function(i,tmpObj) {
      if (zcheckCodeStr.indexOf(","+tmpObj.id+",")!=-1) {
        tmpObj.checked=true;
        zTree1.updateNode(tmpObj,true);
      }
    });

  },300);
  return zTree1;

}




/**
 * 打开单位选择对话框
 * @param callback  回调函数
 * @param checkTypet  选择类型 radio:单选 check:多选
 * @param isadmin 是否管理员 管理员从根单位开始帮助 ，非管理员从自己有权的最高级单位帮助
 */
function openOrgSelDialog(callback,checkType,isadmin,checkCodeStr) {
  var adminOption="6";
  if (isadmin) adminOption="20";
  if (!checkCodeStr) {
    checkCodeStr="";
  }
  openDialogWithJson(callback, "", checkType, adminOption,checkCodeStr);
}

/**
 * 组装json并调用回调函数
 * @param {Object} callback
 * @param {String} checkType: 可选类型"check","radio"
 */
function openDialogWithJson(callback, orgTreeType, checkType, treeType,checkCodeStr){
    if (!checkCodeStr) {
      checkCodeStr="";
    }
    var div = $("<div></div>");
    div.html("<ul id='treeDemo' class='tree' style='overflow:auto;width:100%;height:100%' ></ul>");
    var ztree;
    var json = '[';
    div.dialog({
        bgiframe: true,
        title: "组织机构选择",
        width: 500,
        height: 300,
        buttons: {
            "确定": function(){
                var refNodes = ztree.getCheckedNodes();
              ztree.getnode
                if (refNodes.length > 0) {
                    for (var i = 0; i < refNodes.length; i++) {
						var temp = refNodes[i].name;
						var p = startWith(temp,')');
						temp = temp.substring(p+1,temp.length);
                        json += '{"name":"' + temp + '","id":"' + refNodes[i].id + '","code":"' + refNodes[i].code + '"},';
                    }
                    json = json.substr(0, json.length - 1) + "]";
                    if (callback && typeof(callback) == 'function') {
                        callback(json);
                    }
                    div.dialog("close");
                    div.remove();
                }
            },
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
    checkType = (checkType == "radio" ? "1" : "2");
    ztree = generatTree(checkType, treeType,checkCodeStr);
    $("div").queue(function(){
        $(this).addClass("newcolor");
        $(this).dequeue();
    });
}

/**
 * 通过type组件tree type==1单选  否则 多选,
 * treeType 1为招标树,2为单位树,3为地区公司树,5为地区公司,4为总部+地区公司,否则为其他所有
 * @param {Object} type
 * 真是看不明白这是写的写什么东东，括号套的都逆天了。FK
 */
function generatTree(type, treeType,checkCodeStr){
    if (!checkCodeStr) {
      checkCodeStr="";
    }
    var zcheckCodeStr=checkCodeStr;
    if (zcheckCodeStr!="") {
      if (zcheckCodeStr.charAt(0)!=",") {
        zcheckCodeStr=","+zcheckCodeStr;
      }
      if (zcheckCodeStr.charAt(zcheckCodeStr.length-1)!=",") {
        zcheckCodeStr=zcheckCodeStr+",";
      }
    }
    var zTree1;
    var setting;
    var myData;
    var zNodes = [];
    if (treeType == 1) {
      myData = $.toJSON({
          managerName: "purStruOrgManager",
          methodName: "getBidChild",
          parameters: ""
      });
    } else if (treeType == 2) {
      myData = $.toJSON({
          managerName: "purStruOrgManager",
          methodName: "getEntityOrgChild",
          parameters: ""
      });
    } else if (treeType == 3) {
       myData = $.toJSON({
            managerName: "purStruOrgManager",
            methodName: "getlocalCompanyOrg",
            parameters: ""
       });
    } else if (treeType == 4) {
        myData = $.toJSON({
            managerName: "purStruOrgManager",
            methodName: "getlocalCompanyOrgForSup",
            parameters: ""
        });
    } else if (treeType == 5) {
        myData = $.toJSON({
            managerName: "purStruOrgManager",
            methodName: "getOnlyLocalCompanyOrgForSup",
            parameters: ""
        });
    } else if (treeType == 6) {
        myData = $.toJSON({
            managerName: "purStruOrgManager",
            methodName: "getChild",
            parameters: ['false']
        });
     }  else {
        myData = $.toJSON({
            managerName: "purStruOrgManager",
            methodName: "getChild",
            parameters: ['true']
        });
    }

    if (type == '1') {
        if (treeType != 3 && treeType != 4) {
            if (treeType == 5) {
                setting = {
                    checkable: true,
                    checkStyle: "radio",
                    checkRadioType: "all",
                    async: true,
                    asyncParam: ["name", "id"],
                    asyncParamOther: ["requestString", myData],
                    checkType: {
                        "Y": "",
                        "N": ""
                    }
                }
            }
            else {
                setting = {
                    checkable: true,
                    checkStyle: "radio",
                    checkRadioType: "all",
                    async: true,
                    asyncParam: ["name", "id"],
                    asyncParamOther: ["requestString", myData],
                    checkType: {
                        "Y": "",
                        "N": ""
                    },
                    callback: {
                        beforeExpand: beforeExpand,
                        click: zTreeOnClick
                    }
                }
            }
        }
        else {
            setting = {
                checkable: true,
                checkStyle: "radio",
                checkRadioType: "all",
                async: true,
                asyncParam: ["name", "id"],
                asyncParamOther: ["requestString", myData],
                checkType: {
                    "Y": "",
                    "N": ""
                }
            }
        }
    }
    else { //多选
        setting = {
            checkable: true,
            checkStyle: "checkbox",
            async: true,
            asyncParam: ["name", "id"],
            asyncParamOther: ["requestString", myData],
            checkType: {
                "Y": "",
                "N": ""
            },
            callback: {
                click: zTreeOnClick,
                beforeExpand: beforeExpand
            }
        }
    }
    
    function zTreeOnClick(event, treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            var data = new PMSRequestObject("purStruOrgManager", "getChildsByParentId", [treeNode.id]);
            $$.ajax($$.PMSDispatchActionUrl, "requestString=" + data.toJsonString(), function(data, textStatus, XMLHttpRequest){
                if (data.result) {
                    newNodes = eval("(" + data.data + ")");
                  //根据传入的选择ID自动选择
                  $.each(newNodes,function(i,tmpObj) {
                    if (zcheckCodeStr.indexOf(","+tmpObj.id+",")!=-1) {
                      tmpObj.checked=true;
                    }
                  });
                    addNode(newNodes, treeNode);
                }
            });
        }
    }
    
    function beforeExpand(treeId, treeNode){
        if (treeNode.nodes == null || treeNode.nodes.length == 0) {
            if (treeNode.flag != true) {
                var data = new PMSRequestObject("purStruOrgManager", "getChildsByParentId", [treeNode.id]);
                $$.ajax($$.PMSDispatchActionUrl, "requestString=" + encodeURIComponent(data.toJsonString()), function(data, textStatus, XMLHttpRequest){
                    if (data.result) {
                        newNodes = eval("(" + data.data + ")");
                      //根据传入的选择ID自动选择
                      $.each(newNodes,function(i,tmpObj) {
                        if (zcheckCodeStr.indexOf(","+tmpObj.id+",")!=-1) {
                          tmpObj.checked=true;
                        }
                      });
                        addNode(newNodes, treeNode);
                    }
                });
                treeNode.flag = true;
            }
        }
        else {
            return true;
        }
        return false;
    }
    function addNode(nodes, parNode){
        for (i = 0; i < nodes.length; i++) {
            var newNode = nodes[i];
            zTree1.addNodes(parNode, newNode);
        }
    }
    var url = $$.PMSDispatchActionUrl;
    setting.asyncUrl = url;
    zTree1 = $("#treeDemo").zTree(setting, zNodes);
    setTimeout(function() {
      //处理根结点选中状态
      var tmpAllNodes=zTree1.getNodes();
      $.each(tmpAllNodes,function(i,tmpObj) {
        if (zcheckCodeStr.indexOf(","+tmpObj.id+",")!=-1) {
          tmpObj.checked=true;
          zTree1.updateNode(tmpObj,true);
        }
      });

    },300);
    return zTree1;
    
}

/*
 * @function                   : 查询组织机构方法
 * @param {Object} id          : 显示name的输入框id
 * @param {Object} hiddenId    : 显示id的隐藏输入框id
 * @param {Object} orgTreeType : '1'专家推荐单位 2 专家工作单位 3 回避单位
 * @author                     : WangDong
 */
function searchUnitExp(id, hiddenId, orgTreeType, callback){
    if (orgTreeType == 3) {
        openDialogWithJson(callback, orgTreeType);
    }
    else 
        if (orgTreeType == 1) {
            getLocalComOrgTree(function(json){
                var obj = $.fromJSON(json);
                //$("input[name=recommendOrgId]").val(obj[0].code);
                $("#" + hiddenId).val(obj[0].code);
                var temp = obj[0].name;
                var p = startWith(temp, ')');
                temp = temp.substring(p + 1, temp.length);
                //$("input[name=recommendOrgName]").val(temp);
                $("#" + id).val(temp);
            }, null, 1);
        }
        else {
            openDialogWithHidden(id, hiddenId, orgTreeType, function(){
                if (callback && (typeof callback == 'function')) {
                    callback();
                }
            });
        }
}

/**
 * 打开岗位帮助对话框
 * @param callBack 回调函数
 * 函数返回值为JSON字符串
 * 格式如下：
 * [{"id":240,"name":"室主任","code":"PTRY"},{"id":241,"name":"室副主任","code":"SFZR"}]
 */
function openPostDialog(callBack) {
    var div = $("<div></div>");
    div.html("<iframe name='postselframe' style='width:100%;height:100%' id='postframe'></iframe>");
    div.dialog({
      bgiframe: true,
      title: "组织机构选择",
      width: 800,
      height: 630,
      buttons: {
        "确定": function(){
        	var selVal=window.frames["postselframe"].getSelVals();
        	callBack(selVal);
            div.dialog("close");
            div.remove();
          },
        "取消": function(){
          div.dialog("close");
          div.remove();
        }
      },
      modal: true,
      closeOnEscape: false,
      open: function(event, ui){
        $(".ui-dialog-titlebar-close").hide();
        $("#postframe").attr("src","../bizbase/selectPostQuery.html");
      }
    });
    $("div").queue(function(){
      $(this).addClass("newcolor");
      $(this).dequeue();
    });
  }

/**
 * 通用帮助方法
 * @param helpTitle  帮助标题
 * @param helpWidth  帮助宽度
 * @param helpHeight 帮助高度
 * @param helpURL  帮助URL地址
 * @param selType  帮助类型  0：单选  1：复选
 * @param callBack 回调函数
 * 补充说明：
 * 调用的URL页面中必须包含     getSelVals() 用于返回选择的数据
 * 不论单选还是复选，返回值必须为JSON数组，注意不是串
 */
function openGeneralHelp(helpTitle,helpWidth,helpHeight,helpURL,selType,callBack) {
  if (helpURL.indexOf("?")>-1) {
    helpURL+="&selType="+selType;
  } else {
    helpURL+="?selType="+selType;
  }
  var div = $("<div></div>");
  div.html("<iframe name='generalHelpFrame' style='width:100%;height:100%' id='generalHelpFrame'></iframe>");
  div.dialog({
    bgiframe: true,
    title: helpTitle,
    width: helpWidth,
    height: helpHeight,
    buttons: {
      "确定": function(){
        var selVal=window.frames["generalHelpFrame"].getSelVals();
        callBack(selVal);
        div.dialog("close");
        div.remove();
      },
      "取消": function(){
        div.dialog("close");
        div.remove();
      }
    },
    modal: true,
    closeOnEscape: false,
    open: function(event, ui){
      $(".ui-dialog-titlebar-close").hide();
      $("#generalHelpFrame").attr("src",helpURL);
    }
  });
  $("div").queue(function(){
    $(this).addClass("newcolor");
    $(this).dequeue();
  });
}
