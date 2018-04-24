
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
		var pos_start=str.indexOf(key+"=")+key.length+1; //避免单词中包含key
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

/**
 * 显示/隐藏 页面某一元素
 * @param {Object} targetid 页面标签id
 */
function toggle(targetid, imgId){
    var target = $("#" + targetid);
    if (target.is(":visible")) {
        target.hide();
        $("#" + imgId).attr("src", "../../scripts/images/33.png");//变成加号
    }
    else {
        target.show();
        $("#" + imgId).attr("src", "../../scripts/images/22.png");//变成减号
    }
}

/**
 * 隐藏右边菜单树
 */
function hideShowNavi(){
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

/**
 * 变换左右箭头
 */
function switchArrow(){
    var objLeft = $(".arrowleft");
    var objRight = $(".arrowright");
    var leftClass = $(".arrowleft").attr('class');
    var rightClass = $(".arrowright").attr('class');
    if (leftClass == "arrowleft") {
        objLeft.removeClass();
        objLeft.addClass("arrowright");
    }
    else {
        objRight.removeClass();
        objRight.addClass("arrowleft");
    }
}

/**
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

/**
 * @Object       : Date
 * @methodName   : format
 * @methodParam  : String （format字符串））
 * @methodReturn : Date
 * @author       : ruanqingfeng
 * @desc         : 扩展时间对象的格式化函数
 * @for example1  : var now = new Date(942278400000).format("yyyy-MM-dd hh:mm:ss");
 */
Date.prototype.format = function(format){
    /*  
     * eg:format="YYYY-MM-dd hh:mm:ss";
     */
    var o = {
        "M+": this.getMonth() + 1, // month   
        "d+": this.getDate(), // day   
        "h+": this.getHours(), // hour   
        "m+": this.getMinutes(), // minute   
        "s+": this.getSeconds(), // second   
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter   
        "S": this.getMilliseconds()
        // millisecond   
    }
    
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

/**
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
/**
 * 格式化金额
 * @param {Object} srcStr
 * @param {Object} nAfterDot
 */
function FormatNumber(srcStr,nAfterDot)        //nAfterDot小数位数
{
	if(srcStr==null||srcStr=="null"||srcStr==""){
		srcStr =  "0.00";
	}
	var srcStr,nAfterDot;
	var resultStr,nTen;
	srcStr = ""+srcStr+"";
	strLen = srcStr.length;
	dotPos = srcStr.indexOf(".",0);
	if (dotPos == -1){
		resultStr = srcStr+".";
		for (i=0;i<nAfterDot;i++){
			resultStr = resultStr+"0";
		}
		return resultStr;
	}
	else{
		if ((strLen - dotPos - 1) >= nAfterDot){
			nAfter = dotPos + nAfterDot + 1;
			nTen =1;
			for(j=0;j<nAfterDot;j++){
				nTen = nTen*10;
			}
			resultStr = Math.round(parseFloat(srcStr)*nTen)/nTen;
			return resultStr;
		}
		else{
			resultStr = srcStr;
			for (i=0;i<(nAfterDot - strLen + dotPos + 1);i++){
				resultStr = resultStr+"0";
			}
			return resultStr;
		}
	}
}
		
/**
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

/**
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

/**
 * 列表中将data数组数据转换成json对象
 * @param {Object} data
 * @param {Object} allColumns
 */
function convertDataToObj(data, allColumns){
    var json = "{";
    for (var i = 0; i < data.length; i++) {
        json += "'" + allColumns[i].key + "'";
        json += ":"
        json += "'" + data[i] + "',";
    }
    json = json.substr(0, json.length - 1) + "}";
    var jsonObj = $.fromJSON(json);
	return jsonObj;
}

function getRadioVal(radioName) {
	var radioArr = $("input[name="+radioName+"]");
	for(var i=0;i<radioArr.length;i++){
		if(radioArr[i].checked){
			return radioArr[i].value;	
		}	
	}
	return "";
}

/**
 * @methodName    : copyObject
 * @methodParam1  : Object （第一个对象）
 * @methodParam2  : Object （第二个对象）
 * @methodReturn  : Object
 * @author        : ruanqingfeng
 * @desc          : 用第二个对象的值覆盖第一个对象，返回覆盖后的结果
 * @for example1  : copyObject(o1,o2);)
 */
function copyObject(obj1,obj2) {
	for(var key in obj2){
		obj1[key] = obj2[key]
	}
	return obj1;
}

function formatIframeJSON(data) {
	var result = new Array();
	for(var i=0; i<data.length; i++) {
		result.push(data[i]);
	}
	return result;
}
