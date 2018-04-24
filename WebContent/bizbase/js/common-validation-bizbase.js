/**
 * 根据列名和值验证数据库中是否存在相同值的记录
 * @param String className:实体类名
 * 		  String fieldName:属性名称
 * 		  String value:属性值
 * @return boolean 是否存在（true/false）
 */
function validateExists(className, fieldName, value) {
	var flag = false;
	var reqObj = new PMSRequestObject("bizValidateManager", "isExistsByFieldValue", [className, fieldName, value]);
	$.ajax(
		{
			type: "POST",
			url: $$.PMSDispatchActionUrl,
			async: false, //Ajax同步 
			dataType: "json",
			data: "requestString=" + reqObj.toJsonString(),
			success: function(data, textStatus, XMLHttpRequest) {
				if (data.data == "true") {
					flag = true;
				}
			}
		});
	return flag;
}

/**
 * 修改时判断修改的值是否存在重复
 * @return boolean 是否存在（true/false）
 */
function validateExistsByEdit(className, idVal, fieldName, value) {
	var flag = false;
	var reqObj = new PMSRequestObject("bizValidateManager", "getObjByFieldValue", [className, fieldName, value]);
	$.ajax(
		{
			type: "POST",
			url: $$.PMSDispatchActionUrl,
			async: false, //Ajax同步 
			dataType: "json",
			data: "requestString=" + reqObj.toJsonString(),
			success: function(data, textStatus, XMLHttpRequest) {
				var objArr = eval("(" + data.data + ")");
				if (objArr.length > 0) {
					if (objArr[0].id != idVal) {
						flag = true;
					}
				}
			}
		});
	return flag;
}

//value：检测的对象的值
//element：检测的对象
$.validator.addMethod("telphoneValid", function(value, element) {
	//alert('add');
	var tel = /^(130|131|132|133|134|135|136|137|138|139|147|150|151|152|153|157|158|159|180|182|187|188|189|177)\d{8}$/;
	return tel.test(value) || this.optional(element);
}, "请输入正确的手机号码");
/**
 * 电话号码验证(包括手机、固定电话)
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("isPhone", function(value, element) {
	var length = value.length;
	var mobile = /^(((18[0-9]{1})|(17[0-9]{1})|(13[0-9]{1})|(15[0-9]{1}))+\d{8})$/;
	var tel = /^\d{3,4}-?\d{7,9}$/;
	return this.optional(element) || (tel.test(value) || mobile.test(value));
}, "请正确填写您的联系电话 ");

//手机号码验证
jQuery.validator.addMethod("isMobile", function(value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(17[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写您的手机号码 ");

//APP登录手机号码验证
jQuery.validator.addMethod("appMobile", function(value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
	return this.optional(element) || (length == 11 && mobile.test(value));
}, "请正确填写APP登录手机号码 ");


//电话号码验证
jQuery.validator.addMethod("isTel", function(value, element) {
	var tel=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/; // 电话号码格式010-12345678
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的号码");

//邮政编码验证        
jQuery.validator.addMethod("isZipCode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码 ");

/**
 * 身份证号码验证
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("isCardNo", function(value, element) {
	var cardNo = /^([1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|))$/;
	var isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/; 
	return this.optional(element) || (cardNo.test(value)) ||(isIDCard1.test(value));
}, "请正确填写您的身份证号码");

/**
 * 电子邮件验证
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("isEmail", function(value, element) {
	var email = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/ ;  
	return this.optional(element) || (email.test(value));
}, "请正确填写您的电子邮件 ");

/**
 * qq号码验证
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("isQQ", function(value, element) {
	var qq = /^[1-9][0-9]{4,9}$/;  
	return this.optional(element) || (qq.test(value));
}, "请正确填写您的qq号码 ");

//基础部分编码验证        
jQuery.validator.addMethod("isCorrectCode", function(value, element) {
	var code =  new RegExp(/^[a-zA-Z]+/);
	if(!code.test(value)){
		return false;
	}
//	code = new RegExp(/[^(a-zA-Z\d\-~!@#\$%\^&\*\(\)_=+\[\]\{\}\\\|;',.\/:\"<>?`)]/);
	code = new RegExp(/[^a-zA-Z\d\-@_.]/);
	return this.optional(element) || (!code.test(value));
}, "编码格式错误:必须以字母开头且不能包含中文和全角");

/**
 * 验证仓库名是否重复
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("depotNameValid", function(value, element) {
	var flag;
	if (checkDepotName()) {
		flag = false;
	}
	else {
		flag = true;
	}
	return flag;
	
}, "库房名称已存在");

/**
 * 验证仓库编号是否重复
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("depotCodeValid", function(value, element) {
	var flag;
	if (checkDepotCode()) {
		flag = false;
	}
	else {
		flag = true;
	}
	return flag;
}, "库房编号已存在");

/**
 * 编辑时，验证仓库名是否重复
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("depotNameEditValid", function(value, element) {
	var flag;
	if (checkEditDepotName()) {
		flag = false;
	}
	else {
		flag = true;
	}
	return flag;
}, "库房名称已存在");

/**
 * 验证单位编码是否唯一
 * @param {Object} value
 * @param {Object} element
 */
$.validator.addMethod("unitCodeValid", function(value, element) {
	var flag;
	if (checkUnitCode()) {
		flag = false;
	}
	else {
		flag = true;
	}
	return flag;
}, "单位编码已存在");

/**
* add roleName validator
* @param {Object} value
* @param {Object} element
*/
$.validator.addMethod("roleNameValid", function(value, element) {
	var flag;
	if (checkRoleName()) {
		flag = false;
	}else {
		flag = true;
	}
		return flag;
	}, "角色名称已存在");

/**
* 角色编码唯一性校验
* @param {Object} value
* @param {Object} element
*/
$.validator.addMethod("roleCodeValid", function(value, element) {
	var flag;
	if (checkRoleCode()) {
		flag = false;
	}
	else {
		flag = true;
	}
	return flag;
}, "角色编码已存在");

$.validator.addMethod("required", function(value, element,param) {
	if($(element).parent()!=null && $(element).parent().prev()!=null){
		var elementName = $(element).parent().prev().text().replace("*","").replace(":","").replace("：","");
		elementName = elementName.replace(/\040/,"");
		if($(element).attr("elementName")!=""&&$(element).attr("elementName")!=null){
			elementName = $(element).attr("elementName");
		}
		if(element.inputType=="select"||element.type=="select"){
			jQuery.validator.messages.required = "请选择"+elementName
		}else{
			jQuery.validator.messages.required = "请输入"+elementName;
		}
	}
	// check if dependency is met
	if ( !this.depend(param, element) )
		return "dependency-mismatch";
	switch( element.nodeName.toLowerCase() ) {
	case 'select':
		// could be an array for select-multiple or a string, both are fine this way
		var val = $(element).val();
		return val && val.length > 0;
	case 'input':
		if ( this.checkable(element) )
			return this.getLength(value, element) > 0;
	default:
		return $.trim(value).length > 0;
	}
});
 
 
function validateFunction(){
    if (validateExists("com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function", "activityCode", $("#newCodeField").val())) {
        return true;
    }
    return false;
}

function validateParentFunction(){
    if (validateExists("com.cnpc.pms.bizbase.rbac.resourcemanage.entity.Function", "path", $("#newParentCodeField").val()+',')) {
        return true;
    }
    return false;
}


//验证目录code
$.validator.addMethod("uniqueFunction", function(value, element) { 
		var flag;
	if (validateFunction()) {
		flag = false;
	}
	else {
		flag = true;
	}
	return flag;
}, "该内部码已存在");

//验证目录父节点code
$.validator.addMethod("validateParentFunction", function(value, element) { 
		var flag;
	if (validateParentFunction()) {
		flag = true;
	}
	else {
		flag = false;
	}
	return flag;
}, "该父节点内部号不存在,请重新输入");