/**
 * 导入样式文件
 */
importCss(baseUrl+"/scripts/css/userCheck.css");

/**
 * 业务界面调用，取得所有记录，以json形式返回
 */
function getSelectObjJson(){
	var userIds = $.map($("input[name='selectUserId']"), function(obj){
        return obj.value;
    });
	var userNames = $.map($("input[name='selectUserName']"), function(obj){
        return obj.value;
    });
	var userCodes = $.map($("input[name='selectUserCode']"), function(obj){
        return obj.value;
    });
	var json='{"userIds":"'+userIds+'","userNames":"'+userNames+'"'+',"userCodes":"'+userCodes+'"}';
	return json;
}
/**
 * 
 * @param {selectIds} 以前被选中的id串
 * @param {selectNames} 以前被选中的name串
 * @param {selectCodes} 以前被选中的code串
 */
var showCancelButton="";
function initUser(selectIds, selectNames,selectCodes,showCancel){
	if(!isEmptyValue(showCancel)){
		showCancelButton=showCancel;
	}
    var idArray = selectIds.split(",");
    var nameArray = selectNames.split(",");
	var codeArray = selectCodes.split(",");
    if (idArray != null && idArray.length > 0) {
        for (var i = 0; i < idArray.length; i++) {
        	if (nameArray[i] != "") {
        		addSelectUser(idArray[i], nameArray[i],codeArray[i]);        		
        	}
        }
    }
}
/**
 * 
 * @param {json} 使用人员选择器回调函数传回来的json
 * 通过拆分之后生成图片
 */
function doSelect(json){
    var userIds = $.map($("input[name='selectUserId']"), function(obj){
        return obj.value;
    });
    var userInfo = $.fromJSON(json);
    var selectIds = userInfo.userId;
    var selectNames = userInfo.userName;
	var userCodes = userInfo.userCode;
    var idArray = selectIds.split(",");
    var nameArray = selectNames.split(",");
	var codeArray = userCodes.split(",");
    if (idArray.length != 0) {
        for (var i = 0; i < idArray.length; i++) {
            if (!contains(userIds, idArray[i])) {
                addSelectUser(idArray[i], nameArray[i],codeArray[i]);
            }
        }
    }
}
/**
 * 本js内部调用的添加一条数据
 * @param {Object} selectId
 * @param {Object} selectName
 * @param {Object} selectCode
 */
function addSelectUser(selectId, selectName,selectCode){
	var selUserIds = document.getElementsByName("selectUserId");
	//var selUserCodes = document.getElementsByName("selectUserCode");
	//var selUserNames = document.getElementsByName("selectUserName");
	if (selUserIds != null && selUserIds != "undefined") {
		for (var i=0; i<selUserIds.length; i++) {
			var selUserId = selUserIds[i].value;
			if (selectId == selUserId) {
				return;
			}
		}	
	}
	
    var htmlStr = "<div class='vR'>" +
    "<div class='vN'>" +
    "<div id='userNameDiv' class='vT'>" +
    selectName +
    "<input type='hidden' name='selectUserId' value='" +
    selectId +
    "' />" +
    "<input type='hidden' name='selectUserName' value='" +
    selectName +
    "' />" +
	"<input type='hidden' name='selectUserCode' value='" +
    selectCode +
    "' />" +
    "</div>" ;
	if(isEmptyValue(showCancelButton)){
		htmlStr+="<div class='vM' onclick='deleteSelectUser(this)'></div>" +
    "</div>";
	}
    "</div>";
    $("#selectDiv").append(htmlStr);
}
/**
 * 删除方法
 * @param {Object} obj
 */
function deleteSelectUser(obj){
    $(obj).parent().parent().remove();
}
/**
 * 自己写的工具方法,判断前面参数里面是否包含后面的参数
 * @param {Object} array
 * @param {Object} element
 */
function contains(array, element){
    for (var i = 0; i < array.length; i++) {
        if (array[i] == element) {
            return true;
        }
    }
    return false;
    
}
function isEmptyValue(s){
                return s == null || typeof(s) == 'string' && (s == '' || s == 'null') || s == '&nbsp;' || typeof(s) == 'number' && isNaN(s);
}
