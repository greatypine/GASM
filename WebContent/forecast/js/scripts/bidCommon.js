	var keyStr = "ABCDEFGHIJKLMNOP" +  
    "QRSTUVWXYZabcdef" +  
    "ghijklmnopqrstuv" +  
    "wxyz0123456789+/" +  
    "=";  
	
	function encode64(input) { 
		var output = "";  
		if(input&&input!=0){
			input = escape(input);  
			var chr1, chr2, chr3 = "";  
			var enc1, enc2, enc3, enc4 = "";  
			var i = 0;  
			
			do {  
			chr1 = input.charCodeAt(i++);  
			chr2 = input.charCodeAt(i++);  
			chr3 = input.charCodeAt(i++);  
			
			enc1 = chr1 >> 2;  
			enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);  
			enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);  
			enc4 = chr3 & 63;  
			
			if (isNaN(chr2)) {  
			enc3 = enc4 = 64;  
			} else if (isNaN(chr3)) {  
			enc4 = 64;  
			}  
			
			output = output +   
			keyStr.charAt(enc1) +   
			keyStr.charAt(enc2) +   
			keyStr.charAt(enc3) +   
			keyStr.charAt(enc4);  
			chr1 = chr2 = chr3 = "";  
			enc1 = enc2 = enc3 = enc4 = "";  
			} while (i < input.length);  
			
			
		}else{
			output = input;
		}
		
		return output;  
	}  
	
	function decode64(input) { 
		var output = ""; 
		var decodeOutPut="";
		if(input&&input!=0){
			
			var chr1, chr2, chr3 = "";  
			var enc1, enc2, enc3, enc4 = "";  
			var i = 0;  
			
			// remove all characters that are not A-Z, a-z, 0-9, +, /, or =  
			var base64test = /[^A-Za-z0-9\+\/\=]/g;  
			if (base64test.exec(input)) {  
			alert("There were invalid base64 characters in the input text.\n" +  
			   "Valid base64 characters are A-Z, a-z, 0-9, '+', '/', and '='\n" +  
			   "Expect errors in decoding.");  
			}  
			input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");  
			
			do {  
			enc1 = keyStr.indexOf(input.charAt(i++));  
			enc2 = keyStr.indexOf(input.charAt(i++));  
			enc3 = keyStr.indexOf(input.charAt(i++));  
			enc4 = keyStr.indexOf(input.charAt(i++));  
			
			chr1 = (enc1 << 2) | (enc2 >> 4);  
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);  
			chr3 = ((enc3 & 3) << 6) | enc4;  
			
			output = output + String.fromCharCode(chr1);  
			
			if (enc3 != 64) {  
			output = output + String.fromCharCode(chr2);  
			}  
			if (enc4 != 64) {  
			output = output + String.fromCharCode(chr3);  
			}  
			
			chr1 = chr2 = chr3 = "";  
			enc1 = enc2 = enc3 = enc4 = "";  
			
			} while (i < input.length);  
			
			return unescape(output);  
		}
			return input;
		
		
	
	}  



function getUrlParamByKey_decode(key)
{
	var str=decodeURIComponent(window.location.search);
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
* @methodName   : getUrlParamByKey
* @methodParam  : String
* @methodReturn : String
* @author       : mahaihang
* @desc         : 获取url中的参数，通过key获取value
* @for example  : getUrlParamByKey("userName");
*/
function getUrlParamByKey(key)
{
	var str=window.location.search;
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
* @for example5 :后台manager方法无参数---->  doManager("managerName","managerMethod",null,function(_data){});
*/
function doManager(managerName,managerMethod,params,callBack,async,settings){
	
	var url  = $$.PMSDispatchActionUrl;
	var data = null;
	
	if(settings==null)settings = {};
	if(settings.showWaiting==true){
		$PD.display();
	}else if(checkShowWaitingByMethodName(managerMethod)){
		$PD.display();
	}
	var token = Bid.token ;
	settings.token = false;
	if(settings.token==false){
		token = null;
	}
	var doManagerVersion = "doManagerVersion";
	if(settings.version!=null){
		doManagerVersion = doManagerVersion + settings.version;
	}
	
	if(params==null){
		data = new PMSRequestObject(managerName,managerMethod,null,token); 
	}
	else if(typeof(params)=="string"){//单个字符串
		data = new PMSRequestObject(managerName,managerMethod,[params],token); 
	}else if(typeof(params)=="object"){
		if(params.length==null){//对象
			var doManagerVersionCom = $("#"+doManagerVersion);
			if(doManagerVersionCom.length!=0){
				params.version = doManagerVersionCom.val();
			}
			data = new PMSRequestObject(managerName,managerMethod,[$.toJSON(params)],token); 
		}else{//数组
			var arr = [];
			for(var i=0;i<params.length;i++){
				var param = params[i];
				if(param == null) {
					arr.push(null);
				}
				else if(typeof(param)=="string"){//数组中是字符串
					arr.push(params[i]);	
				}else{//数组中是对象
					arr.push($.toJSON(params[i]));	
				}
			}	
			data = new PMSRequestObject(managerName,managerMethod,arr,token);
		}
	}
	var callBackReturnVal = null;
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
			setToken();
		}
		callBackReturnVal = callBack(_data, textStatus, XMLHttpRequest);
	}
	
	if(async==false){
		$$.asyncAjax(url, "requestString=" + encodeURIComponent(data.toJsonString()),doManagerCallBack,doManagerCallBack);
	}else{
		$$.ajax(url, "requestString=" + encodeURIComponent(data.toJsonString()),doManagerCallBack,doManagerCallBack); 	
	}
	return callBackReturnVal;
}


/*
 * @methodName   : checkShowWaitingByMethodName
 * @methodParam1  : String （methodName方法名）
 * @methodReturn : boolean
 * @author       : ruanqingfeng
 * @desc         : 根据方法名判断是否需要显示正在提交的等待条
 * @for example1  : checkShowWaitingByMethodName("methodName");
 */
function checkShowWaitingByMethodName(methodName){
	if(methodName.indexOf("add") == 0){
		return true;
	}
	return false;
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
				if(obj.attr("bidFormat")=="dateYMD"){
					if(value!=null&&value!=""){
						value = new Date(parseInt(value)).format("yyyy-MM-dd");	
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
* @methodName    : clearNoNum
* @methodReturn  : void
* @author        : ruanqingfeng
* @desc          : 限制输入的值必须为数字，在input的onkeyup中调用
* @for example   : onkeyup="clearNoNum(this)"
*/
function clearNoNum(obj)   
{   
	
	
	var keycode = event.keyCode;
	if (keycode == 37|| keycode == 39) {
		return true;
	}  
    //必须保证第一个为数字而不是.   
	var temp = obj.value.replace(/^\./g,"");
	if(temp!=obj.value){
		obj.value = temp;   
	}
    //保证只有出现一个.而没有多个.   
	temp = obj.value.replace(/\.{2,}/g,".");  
	if(temp!=obj.value){
		obj.value = temp;   
	}
    //保证.只出现一次，而不能出现两次以上   
    temp = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");  
	if(temp!=obj.value){
		obj.value = temp;   
	}

}  



/*
* @methodName    : checkInteger
* @methodReturn  : boolean
* @author        : ruanqingfeng
* @desc          : 限制输入的值必须为整数，在input的onkeypress中调用
* @for example  : onkeypress="return checkInteger()"
*/
function checkInteger(_this){
	if ((event.srcElement.value.indexOf(".") != -1) || (event.keyCode == 46)) {
		return false;
	}else{
		return checkFloat(_this);
	}
}


/*
* @methodName    : checkFloat
* @methodReturn  : boolean
* @author        : ruanqingfeng
* @desc          : 限制输入的值必须为数字，在input的onkeypress中调用
* @for example  : onkeypress="return checkFloat()"
*/
function checkFloat(_this) {
	if(_this!=null){
		_this.keydownValue = _this.value;
	}
	
	var keycode = event.keyCode;
	if(event.shiftKey){
		return false;
	}
	if ((event.srcElement.value.indexOf(".") != -1) && (keycode == 46)) {
		return false;
	}
	if ((event.srcElement.value == "") && (keycode == 46)) {
		return false;
	}
	if (keycode == 190  || keycode == 8||keycode == 9||keycode == 13|| keycode == 37|| keycode == 39) {
		return true;
	}
	if(keycode == 189 && _this!=null){
		
		if(_this.value.indexOf("-")!=-1){
			return false;	
		}
		return true;
	}
	if (keycode > 57 || keycode < 46 || keycode == 47) {
		return false;
	}
	
	if (!(event.keyCode == 46) && !(event.keyCode == 8) && !(event.keyCode == 37) && !(event.keyCode == 39)) {
		if (!((event.keyCode >= 48 && event.keyCode <= 57) || (event.keyCode >= 96 && event.keyCode <= 105))) {
			event.returnValue = false;
		}
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
    m = (m == "" ? "0" : m);
    n = (n == "" ? "0" : n);
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
	if(thisNumber == null || thisNumber == ""){
		return "";	
	}
	return parseFloat(thisNumber).toFixed(n);
}

/**
 * 用途：用于验证指定条件的指定对象是否存在。 调用方法：checkProjectName()。
 * 
 * @param className-指定的类名。
 * @param fieldName-指定的字段名。
 * @param value-过滤的字段值。
 */
function validateExists(className, fieldName, value, status,id){
    var flag = false;
	if(id==null){
		id ="0";	
	}
    var reqObj = new PMSRequestObject("infoManager", "isExistsByFieldValue", [className, fieldName, value, id]);
    $.ajax({
        type: "POST",
        url: $$.PMSDispatchActionUrl,
        async: false, // Ajax同步
        dataType: "json",
        data: "requestString=" + encodeURIComponent(reqObj.toJsonString()),
        success: function(data, textStatus, XMLHttpRequest){
            if (data.data == "true") {
				if (editProjectName.length > 0) {
					if(value == editProjectName) {
						return false;
					}
				}
				flag = true;
			}
            if (data.data == "false") {
				flag = false;
			}	
         }
    });
    return flag;
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
	if (parent.refreshTree != null) {
		parent.refreshTree(url);
	}
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
* @for example   : <div class="arrowleft" onclick="toggleNavi();"></div>
*/
function toggleNavi(){
    var obj = parent.frames['i2ui_shell_content'];//得到左侧导航栏的frameset对象
    var colNum = parseInt(obj.cols.split(",")[0]);//得到左侧导航栏的frameset对象的col值的第一个属性
    if (colNum > 1) {
        parent.frames['i2ui_shell_content'].cols = '1,*'
    }
    else {
        parent.frames['i2ui_shell_content'].cols = '172,*'
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

/*
* @methodName    : toggleDiv
* @author        : ruanqingfeng
* @desc          : 隐藏或显示div，并且改变"+"，"-"号
* @for example   : <a href="#" onclick="toggleDiv('createProjectDiv');"><img src="../../scripts/images/i2/33.png" align="absmiddle" id="pic" /></a>
*/
function toggleDiv(divId,imgId){
	imgId = (imgId == null ? "pic" : imgId);
    $("#"+divId).slideToggle("fast",function(_tbn){
		if($(this).css("display")=="none"){
			$("#"+imgId).attr("src",baseUrl + "/scripts/images/33.png");
		}else{
			$("#"+imgId).attr("src",baseUrl + "/scripts/images/22.png");
		}
	});
}

function doDelete(_this, queryId, formName, before, callBack) {
	if (formName != undefined) {
		$validator = new PMSValidator($("#" + formName)[0], {
			bNormalDisplay: false,
			iDisplayLength: 1
		});
		$validator.clean();
	}
	var ids = $$.getSelected(queryId);
	var deleteBtnObj = $(_this);
	var deleteCode = deleteBtnObj.attr("deleteCode");
	var deleteName = deleteBtnObj.attr("deleteName");
	var deleteClass = deleteBtnObj.attr("deleteClass");
	var cofigArray = [deleteClass,deleteCode,deleteName];
	var searchDiv = deleteBtnObj.attr("deleteSearchDiv");
	if(before==undefined||before()) {
		doManager("infoManager", "deleteObjects", [ids,cofigArray],function(data, textStatus, XMLHttpRequest){
			$$.executeSearch(queryId, searchDiv);
			if (callBack != undefined) {
				callBack();
			}
		});	
	}
}


/*
* @methodName   : removeValidateMetaData
* @methodParam  : String 校验对象ID
* @methodReturn : String
* @author       : ruanqingfeng
* @desc         : 清除校验信息缓存
* @for example  : removeValidateMetaData("userName");
*/
function removeValidateMetaData(elementId)
{
	var elementHTMLObj = $("#"+elementId)[0];
	if(elementHTMLObj!=null){
		$.removeData(elementHTMLObj);
	}
}


/*
* @methodName   : checkExistsProjectFile
* @methodParam  : 招标项目编号
* @methodReturn : boolean
* @author       : ruanqingfeng
* @desc         : 验证是否存在标书购买记录
* @for example  : checkExistsProjectFile("11293");
*/
function checkExistsProjectFile(projectId){
	var result = "";
	doManager("bidProjectFileRecordManager","checkExistsProjectFile",projectId,function(_data){
		if(_data.result){
			result = $.fromJSON(_data.data);
		}
	},false);
	return result;
}


/*
* @methodName   : formatNullToZero
* @methodParam  : 需要转换的值
* @methodReturn : string
* @author       : ruanqingfeng
* @desc         : 将空转换成0
* @for example  : formatNullToZero("11293");
*/
function formatNullToZero(val){
	if(val == null || val == ""){
		return "0";	
	}
	return val;
}

/*
* @methodName   : tableRowToObj
* @methodParam1 : data        数组形式数据
* @methodParam2 : allColumns  列记录
* @methodReturn : Object
* @author       : ruanqingfeng
* @desc         : 将queryTable的行记录从数组性质转成对象形式
* @for example  : tableRowToObj(data,allColumns);
*/
function tableRowToObj(data,allColumns){
	var result = {};
	for(var i=0;i<data.length;i++){
		var col = allColumns[i];
		result[col.key] = data[i];
	}
	return result;
}




/*
* @methodName   : showWin
* @methodParam  : _cfg （参数配置）
* @author       : ruanqingfeng
* @desc         : 弹出一个window
* @for example  : showWin({});
*/
function showWin(_cfg) {
	var _this = {
		win:$("<div></div>"),
		contentUrl:baseUrl + "/bid/bidresult/MutiSelBidderWin.html?projectType=",
		width:850,
		height:700,
		title:'',
		callBack:function(){}
	};
	$.extend(_this,_cfg);
	var initWin = function(){
		_this.win.html('<iframe id="contentIframe" name="contentIframe" frameborder="0" width="'+(_this.width-20)+'" height="'+(_this.height-90)+'" src="'+_this.contentUrl+'"></iframe>');
		_this.win.dialog({
			bgiframe: true,
			title:_this.title,
			autoOpen:false,
			width:_this.width,
			height:_this.height,
			buttons : {
				"确定":function(){
					var value = (window.frames["contentIframe"]).getValue();
					var result = _cfg.callBack(value);
					if(result!=false){
						_this.hide();
					}
				},
				"取消":function(){
					_this.hide();
				}
			},
			modal:true
		});	
		_this.show();
	}
	
	_this.show = function(){
		_this.win.dialog("open");
	}
	
	_this.hide = function(){
		_this.win.dialog("close");
		$("#contentIframe").remove();
	}
	initWin();
}




/*
* @methodName   : getFormSimpleData
* @methodParam2  : String （form表单的名字）
* @methodReturn : []
* @author       : ruanqingfeng
* @desc         : 获取一组规则的form数据，返回[{key1:value1},{key1:value1},{key1:value1}]
* @for example1  : getFormSimpleData("formName");(用JSON字符串对全局范围赋值)
*/
function getFormSimpleData(formName) {
	var formName = formName!=""&formName!=null?"#"+formName+" ":"";
	var arr = $(formName + "input[name]");
	var returnVal = {};
	$.each(arr,function(i,obj){
		if($(obj).attr("type")=="file"){
			return;	
		}
		if($(obj).attr("bidTableFlag")=="true"){
			return;	
		}
		if($(obj).attr("type")=="radio"){
			var arr_name = $(obj).attr("name");
			returnVal[arr_name] = $('input[name="'+arr_name+'"]:checked').val();
			return;
		}
		returnVal[($(obj).attr("name"))] = $(obj).val();
		$(obj).removeAttr("readonly");
	})

	arr = $(formName + "select[name]");
	$.each(arr,function(i,obj){
		if($(obj).attr("bidTableFlag")=="true"){
			return;	
		}
		returnVal[($(obj).attr("name"))] = $(obj).val();
	})
	arr = $(formName + "textarea[name]");
	$.each(arr,function(i,obj){
		if($(obj).attr("bidTableFlag")=="true"){
			return;	
		}
		returnVal[($(obj).attr("name"))] = $(obj).text();
	})
	return returnVal;
}


/*
* @methodName   : setFormSimpleData
* @methodParam1  : String （JSON字符串）
* @methodParam2  : String （form表单的名字）
* @methodReturn : boolean
* @author       : ruanqingfeng
* @desc         : 加载form数据
* @for example1  : setFormSimpleData("{key1:'11',key2:'22'}");(用JSON字符串对全局范围赋值)
* @for example2  : setFormSimpleData({key1:'11',key2:'22'});（用JSON对象对全局范围赋值)
* @for example3  : setFormSimpleData("{key1:'11',key2:'22'}","formName1");（用JSON字符串对某个form赋值)
* @for example4  : setFormSimpleData({key1:'11',key2:'22'},"formName1");（用JSON对象对某个form赋值)
*/
function setFormSimpleData(json,formName) {
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
				if(obj.attr("bidFormat")=="dateYMD"){
					if(value!=null&&value!=""){
						value = new Date(parseInt(value)).format("yyyy-MM-dd");	
					}
				}
				obj.val(value);
			}
		})
		
	}
	return true;
}



/*
* @methodName   : parseDate
* @methodParam1  : String （时间字符串）
* @methodReturn : boolean
* @author       : ruanqingfeng
* @desc         : 将字符串转换为时间类型
* @for example4  : parseDate("2001-09-01 22:32");
*/
function parseDate(dateStr){ 
	var dTime = new Date(dateStr.replace(new RegExp("-", "g"), "/"));
	return dTime;
}


/*
* @methodName   : tableRowToObj
* @methodParam1 : data        数组形式数据
* @methodParam2 : allColumns  列记录
* @methodReturn : Object
* @author       : ruanqingfeng
* @desc         : 将queryTable的行记录从数组性质转成对象形式
* @for example  : tableRowToObj(data,allColumns);
*/
function tableRowToObj(data,allColumns){
	var result = {};
	for(var i=0;i<data.length;i++){
		var col = allColumns[i];
		result[col.key] = data[i];
	}
	return result;
}

/**
 * 将给定数字精确到小数点后两位
 * methodName formatNumber
 */
function formatNum(num) {
	if(num == null) {
		return null;
	}
	var numStr = num.toString();
	if(numStr.indexOf(".") == -1) {
		return num;
	} else {
		if(numStr.length - numStr.indexOf(".") > 3) {
			numStr = numStr.substring(0,numStr.indexOf(".")+3);
			return parseFloat(numStr);
		} else {
			return num;
		}
	}
}