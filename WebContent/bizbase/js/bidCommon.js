
/*
* @methodName   : getUrlParamByKey
* @methodParam  : String
* @methodReturn : String
* @author       : mahaihang
* @desc         : 获取url中的参数，通过key获取value
* @for example  : getUrlParamByKey("userName");
*/
function getUrlParamByKey(key,url)
{
	var str=window.location.search;
	if(url!="" && url!=null){
		str = new String(url);	
	}
	if (str.indexOf(key)!=-1)
	{
		var pos_start=str.indexOf(key)+key.length+1;
		var pos_end=str.indexOf("&",pos_start);
		if (pos_end==-1)
		{
			return str.substring(pos_start);
		}
		else
		{
			return str.substring(pos_start,pos_end)
		}
	}
	else
	{
		return null;
	}
}


/*
* @methodName   : doManager
* @methodParam1 : String   （manager在appContext.xml中bean的ID）
* @methodParam2 : String   （manager的方法）
* @methodParam3 : String   （manager的方法）
* @methodParam4 : function （回调函数）
* @methodParam4 : boolean  （是否异步（false：同步，true:异步）默认为true）
* @methodReturn : String
* @author       : ruanqingfeng
* @desc         : 获取url中的参数，通过key获取value
* @for example1 :后台manager方法对应一个String参数---->  doManager("managerName","managerMethod","testValue",function(_data){});
* @for example2 :后台manager方法对应多个参数      ---->  doManager("managerName","managerMethod",["t1","t2"],function(_data){});
* @for example3 :后台manager方法对应一个DTO对象   ---->  doManager("managerName","managerMethod",{key1:"value1",key2:"value2"},function(_data){});
* @for example4 :后台manager方法对应一个List      ---->  doManager("managerName","managerMethod",'["t1","t2"]',function(_data){});
*/
function doManager(managerName,managerMethod,params,callBack,async,settings){
	
	var url  = $$.PMSDispatchActionUrl;
	var data = null;
	
	if(settings==null)settings = {};
	
	if(settings.showWaiting==true){
		$PD.display();	
	}
	var token = null ;
	var doManagerVersion = "doManagerVersion";
	if(settings.version!=null){
		doManagerVersion = doManagerVersion + settings.version;
	}
	if(typeof(params)=="string"){//单个字符串
		data = new PMSRequestObject(managerName,managerMethod,[params],null); 
	}else if(typeof(params)=="object"){
		if(params.length==null){//对象
			var doManagerVersionCom = $("#"+doManagerVersion);
			if(doManagerVersionCom.length!=0){
				params.version = doManagerVersionCom.val();
			}
			data = new PMSRequestObject(managerName,managerMethod,[$.toJSON(params)],null); 
		}else{//数组
			var arr = [];
			for(var i=0;i<params.length;i++){
				var param = params[i];
				
				if(typeof(param)=="string"){//数组中是字符串
					arr.push(params[i]);	
				}else if(param==null){//空
					arr.push(params[i]);
				}else{//数组中是对象
					arr.push($.toJSON(params[i]));	
				}
			}	
			data = new PMSRequestObject(managerName,managerMethod,arr,null); 
		}
	}
	var doManagerCallBack = function(_data, textStatus, XMLHttpRequest){
		if($.fromJSON(_data.data)!=null&&$.fromJSON(_data.data).version!=null){
			var doManagerVersionCom = $("#"+doManagerVersion);
			if(doManagerVersionCom.length==0){
				doManagerVersionCom = $("<input type='hidden' name='"+doManagerVersion+"' id='"+doManagerVersion+"' >");
				$(document.body).append(doManagerVersionCom);
			}
			doManagerVersionCom.val($.fromJSON(_data.data).version);
		}
		if(_data.result==true){
			//setToken();
		}
		callBack(_data, textStatus, XMLHttpRequest);
	}
	if(async==false){
		$$.asyncAjax(url, "requestString=" + encodeURIComponent(data.toJsonString()),doManagerCallBack);
	}else{
		$$.ajax(url, "requestString=" + encodeURIComponent(data.toJsonString()),doManagerCallBack); 	
	}
}


/*
* @methodName   : getToken
* @methodReturn : String
* @author       : ruanqingfeng
* @desc         : 获取一个新的Token
* @for example1 : getToken();
*/
function getToken(){
	var token = null;
	$$.asyncAjax(getRootPath() + "/dispatcher.action", "repeatable=false", function(data, textStatus, XMLHttpRequest) {
		
		if(data.result) {
			token = data.token;
		}
	});
	
	return null;
} 


/*
* @methodName   : setToken
* @author       : ruanqingfeng
* @desc         : 设置一个新的Token
* @for example1 : setToken();
*/
function setToken(){
	Bid.token = getToken();
} 



/*
* @methodName   : asyncAjax
* @methodParam1 : String  （提交后台的url路径）
* @methodParam2 : String  （提交的数据）
* @methodParam3 : funtion （回调函数）
* @methodParam4 : funtion （请求失败回调函数）
* @author       : ruanqingfeng
* @desc         : 扩展框架的异步提交
*/
PMSApplication.prototype.asyncAjax = function(url, data, callback, failureCallback) {
	$.ajax({
		url : url,
		dataType : "json",
		type : "post",
		async: false, // Ajax同步
		data : data,
		timeout : $$.requestTimeout,
		success : function(data, textStatus, XMLHttpRequest) {
			if (data.result) {
				callback(data, textStatus, XMLHttpRequest);
			} else {
				if (failureCallback) {
					failureCallback(data, textStatus, XMLHttpRequest);
				} else {
					$$.onServerInvokeFailed(XMLHttpRequest, data.message);
				}
			}
		},
		error : $$.onServerInvokeFailed
	});
}




/*
* @methodName   : getRadioVal
* @methodParam  : String （radio组名称）
* @methodReturn : String
* @author       : ruanqingfeng
* @desc         : 获取选中的radio值
* @for example  : getRadioVal("radioName");
*/
function getRadioVal(radioName) {
	var radioArr = $("input[name="+radioName+"]");
	for(var i=0;i<radioArr.length;i++){
		if(radioArr[i].checked){
			return radioArr[i].value;	
		}	
	}
	return "";
}

/*
* @methodName   : getCheckBoxVal
* @methodParam  : String （checkBox组名称）
* @methodReturn : String
* @author       : ruanqingfeng
* @desc         : 获取选中的checkBox值
* @for example  : getCheckBoxVal("radioName");
*/
function getCheckBoxVal(checkBoxName) {
	var checkBoxArr = $("input[name="+checkBoxName+"]");
	var result = "";
	for(var i=0;i<checkBoxArr.length;i++){
		if(checkBoxArr[i].checked){
			result =  result + (result == ""?"":",") + checkBoxArr[i].value ;	
		}	
	}
	return result;
}


/*
* @methodName   : checkSelectAll
* @methodParam1  : String （全选的checkbox组件ID
* @methodParam2  : String （需要被全选的checkbox组件ID）
* @methodReturn : boolean
* @author       : ruanqingfeng
* @desc         : 设置全选
* @for example  : checkSelectAll("selectAllName","selectName");
*/
function checkSelectAll(selectAllName,selectName) {
	if(selectName==null){
		selectName = selectAllName.substr(0,selectAllName.length-3);
	}
	$("input[name="+selectName+"]").attr("checked",$("#"+selectAllName).attr("checked"));
}

/*
* @methodName   : showDialog
* @methodParam1  : String （需要打开的URL地址）
* @methodParam2  : String （窗口宽度）
* @methodParam3  : String （窗口高度）
* @methodParam4  : String （是否需要滚动条）
* @methodReturn : Object
* @author       : ruanqingfeng
* @desc         : 打开一个模态窗口
* @for example  : showDialog("http://www.cnpc.com","20","20","true");
*/
function showDialog(urlStr, dialogWidth, dialogHeight, Scroll) {
	var varReturn = "";
	var randStr = new Date().getTime();
	if (urlStr.lastIndexOf("?") == -1) {
		varReturn = showModalDialog(urlStr + "?randStr=" + randStr, window, "dialogWidth:" + dialogWidth
						+ "px;dialogHeight:" + dialogHeight + "px;status:no;help:no;resizable:yes;scroll:" + Scroll);
	} else {
		varReturn = showModalDialog(urlStr + "&randStr=" + randStr, window, "dialogWidth:" + dialogWidth
						+ "px;dialogHeight:" + dialogHeight + "px;status:no;help:no;resizable:yes;scroll:" + Scroll);
	}
	return varReturn;
}


/*
* @methodName   : loadFormData
* @methodParam1  : String （JSON字符串）
* @methodParam2  : String （form表单的名字）
* @methodReturn : boolean
* @author       : ruanqingfeng
* @desc         : 加载form数据
* @for example1  : loadFormData("{key1:'11',key2:'22'}");(用JSON字符串对全局范围赋值)
* @for example2  : loadFormData({key1:'11',key2:'22'});（用JSON对象对全局范围赋值)
* @for example3  : loadFormData("{key1:'11',key2:'22'}","formName1");（用JSON字符串对某个form赋值)
* @for example4  : loadFormData({key1:'11',key2:'22'},"formName1");（用JSON对象对某个form赋值)
*/
function loadFormData(json,formName) {
	if(json==null||json==""){
		json = {};	
	}
	if(typeof(json)=="string"){//传过来的是JSON字符串
		json = $.fromJSON(json);
	}
	
	for(var key in json){
		var obj = null;
		var value = json[key];
		if(formName!=null&&formName!=""){
			obj = $("form[name=" + formName + "] [name=" + key + "]");	
		}else{
			obj = $("[name=" + key + "]");
		}
		obj.each(function(i){
			if(($(this).attr("type")).toUpperCase()=="RADIO"){
				if($(this).val()==value){
					this.checked = true;
				}
			}else if(($(this).attr("type")).toUpperCase()=="CHECKBOX"){
				if(("," + value + ",").indexOf("," + $(this).val() + ",")!=-1){
					this.checked = true;
				}
			}else if((this.tagName).toUpperCase()=="SELECT"){
				$(this).attr("value",value);
			}else{
				if(obj.attr("format")=="date"){
					if(value!=null&&value!=""){
						value = new Date(parseInt(value)).format("yyyy-MM-dd");	
					}
				}
				if(obj.attr("bidFormat")=="date"){
					if(value!=null&&value!=""){
						value = new Date(parseInt(value)).format("yyyy-MM-dd hh:mm");	
					}
				}
				obj.val(value);
			}
		})
		
	}
	return true;
}

/*
* @methodName   : getFormData
* @methodParam2  : String （form表单的名字）
* @methodReturn : []
* @author       : ruanqingfeng
* @desc         : 获取一组规则的form数据，返回[{key1:value1},{key1:value1},{key1:value1}]
* @for example1  : getFormData("formName");(用JSON字符串对全局范围赋值)
*/
function getFormData(comName) {
	var arr = $("#"+comName + " input[name]");
	var firstInputName = ($("#"+comName + ">input:first-child").attr("name"));
	var rowCnt = $("#" + comName + ">[name=" + firstInputName + "]").length;
	var colCnt = arr.length/rowCnt;
	
	var result = [];
	for(var i=0;i<rowCnt;i++){
		var obj = {};
		for(var j=0;j<colCnt;j++){
			var com = $(arr[(i*colCnt)+j]);
			obj[com.attr("name")] = com.val();	
		}
		result.push(obj);
	}
	var returnVal = {};
	returnVal[comName] = result;
	return returnVal;
}


/*
* @methodName    : checkFloat
* @methodReturn  : boolean
* @author        : ruanqingfeng
* @desc          : 限制输入的值必须为数字，在input的onkeypress中调用
* @for example  : onkeypress="return checkFloat()"
*/
function checkFloat() {
	var keycode = event.keyCode;
	
	if ((event.srcElement.value.indexOf(".") != -1) && (keycode == 46)) {
		return false;
	}
	if ((event.srcElement.value == "") && (keycode == 46)) {
		return false;
	}
	if (keycode > 57 || keycode < 46 || keycode == 47) {
		return false;
	}

}


/*
* @methodName    : doCancel
* @author        : ruanqingfeng
* @desc          : 所有页面的取消统一操作
* @for example  : onclick="doCancel()"
*/
function doCancel(url) {
	bid.confirm("确定要取消当前编辑操作吗？",function(_btn){
		if(_btn==true){
			window.location=url;
		}	
	})
}


/*
* @methodName    : fixMath
* @author        : ruanqingfeng
* @desc          : 四则运算精度修正函数 
* @for example1  : fixMath(1,2,'+')
* @for example2  : fixMath(10,1.12,'-')
* @for example3  : fixMath(1,2,'*')
* @for example4  : fixMath(1,2,'/')
*/
function  fixMath(m,n,op){ 
    var  a  =  (m+" "); 
    var  b  =  (n+" "); 
    var  x  =  1;
    var  y  =  1;
    var  c  =  1;
    if(a.indexOf( ".") >0){
        x = Math.pow(10,a.length - a.indexOf( ".") - 1); 
    } 
    if(b.indexOf( ".") >0) { 
        y = Math.pow(10,b.length - b.indexOf( ".") - 1); 
    } 
    switch(op) 
    { 
        case  '+':
        case  '-':
            c = Math.max(x,y);
            m = Math.round(m*c);
            n = Math.round(n*c);
            break;
        case  '*': 
            c = x*y;
            m = Math.round(m*x);
            n = Math.round(n*y);
            break; 
        case '/':
            c = Math.max(x,y);
            m = Math.round(m*c);
            n = Math.round(n*c);
            c = 1;
            break;
    }
    return   eval( "( "+m+op+n+ ")/ "+c); 
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
* @methodName    : refreshTaskTree
* @author        : zhaocaipeng
* @desc          : 刷新任务树节点状态
* @for example   : refreshTaskTree()
*/
function refreshTaskTree() {
	$(document.body).html("");
	var url = $$.PMSDispatchActionUrl;
	parent.refreshTree(url);
}


/*
* @methodName    : goTaskTree
* @author        : zhaocaipeng
* @desc          : 返回到任务树
* @for example   : goTaskTree()
*/
function goTaskTree() {
	$(document.body).html("");
}




/*
* @methodName    : toggleNavi
* @author        : ruanqingfeng
* @desc          : 隐藏或显示右边菜单树
* @for example   : toggleNavi()
*/
function toggleNavi(){
    var obj = top.frames['i2ui_shell_content'];//得到左侧导航栏的frameset对象
    var colNum = parseInt(obj.cols.split(",")[0]);//得到左侧导航栏的frameset对象的col值的第一个属性
    if (colNum > 1) {
        top.frames['i2ui_shell_content'].cols = '1,*'
    }
    else {
        top.frames['i2ui_shell_content'].cols = '172,*'
    }
	switchArrow();
}


/*
* @methodName    : switchArrow
* @author        : ruanqingfeng
* @desc          : 变换左右箭头
* @for example   : switchArrow()
*/
function switchArrow(){
	var objLeft = $(".arrowleft");
	var objRight = $(".arrowright");
	var leftClass = $(".arrowleft").attr('class');
	var rightClass = $(".arrowright").attr('class');
	if(leftClass=="arrowleft"){
		objLeft.removeClass();
		objLeft.addClass("arrowright");
	}else{
		objRight.removeClass();
		objRight.addClass("arrowleft");
	}
}
