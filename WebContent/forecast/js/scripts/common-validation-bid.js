$.extend(jQuery.validator.messages, {
    precision: "输入值小数位不能超过{1}位",
    required: "请输入",
    remote: "请修正该字段",
    email: "请输入正确格式的电子邮件",
    url: "请输入合法的网址",
    date: "请输入合法的日期",
    dateISO: "请输入合法的日期 (ISO).",
    number: "请输入合法的数字",
    digits: "只能输入整数",
    creditcard: "请输入合法的信用卡号",
    equalTo: "请再次输入相同的值",
    accept: "请输入拥有合法后缀名的字符串",
    maxlength: jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
    minlength: jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
    rangelength: jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
    range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
    max: jQuery.validator.format("请输入一个最大为 {0} 的值"),
    min: jQuery.validator.format("请输入一个最小为 {0} 的值"),
	unique:"{0}已存在",
	tableUnique:"已存在", //判断列表输入值的唯一性
	identifier:"请输入合法的编号",
	exists:"{0}不存在"
});
$.validator.setDefaults({
	
		submitHandler: function(form){
//			// do other stuff for a valid form   
//			alert('ok11');
//			form.submit();
		},
		invalidHandler: function(form, validator){
//			alert('invali22');
//			var errors = validator.numberOfInvalids();
//			if (errors) {
//				var message = errors == 1 ? 'You missed 1 field. It has been highlighted' : 'You missed ' + errors + ' fields. They have been highlighted';
//				$("div.error span").html(message);
//				$("div.error").show();
//			}
//			else {
//				$("div.error").hide();
//			}
		},
	highlight: function(input) {
		//alert('hid');
		//$(input).addClass("ui-state-highlight");
	},
	unhighlight: function(input) {
		//alert('unhid');
		//$(input).removeClass("ui-state-highlight");
	}
});

//value：检测的对象的值
//element：检测的对象
$.validator.addMethod("telphoneValid", function(value, element) { 
//alert('add');
    var tel = /^(130|131|132|133|134|135|136|137|138|139|150|152|153|157|158|159|180|186|187|188|189)\d{8}$/; 
    return tel.test(value) || this.optional(element); 
}, "请输入正确的手机号码");

$.validator.addMethod("telphoneValid2", function(value, element) { 
    var tel = /^\d{11}$/; 
    return tel.test(value) || this.optional(element); 
}, "请输入正确的手机号码");

$.validator.addMethod("isPhone", function(value, element) {
	var length = value.length;
	var mobile = /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/;
	var tel = /^\d{3,4}-?\d{7,9}$/;
	return this.optional(element) || (tel.test(value) || mobile.test(value));
}, "请正确填写您的联系电话 ");

//邮政编码验证        
jQuery.validator.addMethod("isZipCode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写您的邮政编码 ");

jQuery.validator.addMethod("isNum", function(value, element) {
	if (isNaN(value)) {
		return false;
	}
	return true;
}, "只能输入数字！");

jQuery.validator.addMethod("eqToAnother", function(value, element) {
	if ($(element).val() == $("#"+$(element).attr("eqValidateId")).val()) {
		return true;
	}
	return false;
}, "两次密码不一致！");


jQuery.validator.addMethod("checkRegistMoney", function(value, element) {
	var arr = value.split(".");
	if(arr[1].length>2){
		return false;	
	}
	return true;
}, "注册资金必须保留2位小数！");

jQuery.validator.addMethod("technicalPoints", function(value, element) {
	var arr = value.split(".");
	if(arr[0].length>2){
		return false;	
	}
	if(arr.length>1){
		if(arr[1].length>2){
			return false;	
		}
	}
	return true;
}, "请输入一个长度最多是5的字符串");

jQuery.validator.addMethod("quantity", function(value, element) {
	var arr = value.split(".");
	if(arr.length<=1){
		return true;	
	}
	if(arr[1].length>3){
		return false;	
	}
	return true;
}, "必须保留3位小数！");

jQuery.validator.addMethod("money", function(value, element) {
	var arr = value.split(".");
	if(arr.length<=1){
		return true;	
	}
	if(arr[1].length>2){
		return false;	
	}
	return true;
}, "必须保留2位小数！");

$.validator.addMethod("required", function(value, element,param) {
	if($(element).parent()!=null && $(element).parent().prev()!=null){
		var elementName = $(element).parent().prev().text().replace("*","").replace(":","").replace("：","");
		elementName = elementName.replace(/\040/,"");
		if($(element).attr("elementName")!=""&&$(element).attr("elementName")!=null){
			elementName = $(element).attr("elementName");
		}
		if($(element).attr("id")=="starttime"){
			jQuery.validator.messages.required = "请输入"+elementName+"起始值";
		}else if($(element).attr("id")=="endtime"){
			jQuery.validator.messages.required = "请输入"+elementName+"结束值";
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

//招标文件编号 唯一性验证
$.validator.addMethod("projectFileCodeValid", function(value, element) { 
	var flag;
    if(checkProjectFileCode()) {
		flag = false;
	}  else {
		flag = true;
	}
    return flag; 
}, "招标文件编号已存在");


$.validator.addMethod("projectNameValid", function(value, element) { 
	var flag;
    if(checkProjectName()) {
		flag = false;
	}  else {
		flag = true;
	}
    return flag; 
}, "项目名称已存在");

$.validator.addMethod("projectCodeValid", function(value, element) { 
	var nowProjectCode = ($("#projectCode").val() == "" ? null : $("#projectCode").val());
	var nowBidInfoNoticeId = ($("#id").val() == "" ? null : $("#id").val());
	var flag = null;
	doManager("bidInfoNoticeManager","validateProjectCode",[nowProjectCode,nowBidInfoNoticeId],function(_response){
		if(_response.result){
			flag = $.fromJSON(_response.data);	
		}
	},false);
	return flag;
	
}, "项目编号已存在");

$.validator.addMethod("identifier", function(value, element) { 
	var regs = new RegExp(/[^(a-zA-Z\d\-)]/);
    return !regs.test(value); 
});

$.validator.addMethod("projectTenderValid", function(value, element) { 
	var flag;
    if(checkProjectTender()) {
		flag = true;
	}  
	else {
		flag = false;
	}
    return flag; 
}, "招标人不存在");

$.validator.addMethod("tenderAgencyNameValid", function(value, element) { 
	var flag;
	
	if(getRadioVal("tenderOrganization")=="2"){
		
		if($("#tenderAgencyName").val()==""){
			
			flag = false;
		}else{
			flag = true;
		}
	}  
	else {
		flag = true;
	}
    return flag; 
}, "招标代理机构名称不能为空");

$.validator.addMethod("oddValid", function(value, element) { 
	var flag;
	var val = $(element).val();
	if(val==""){
		return true;
	}
	if(parseInt(val)%2==0){
		flag = false;
	}else{
		flag = true;
	}

    return flag; 
}, "必须为奇数");

//此方法仅仅限于表格验证
$.validator.addMethod("tableUnique", function(value, element) { 
	var flag=true;
	var name = $(element).attr("name");
	var inputs=$(bidTables[1].getBidTable()).find("[name='"+name+"']");
	if(inputs.length<=1){
		return flag;
	}
	var j=-1;
	for(var i=0;i<inputs.length;i++){
		var input=inputs[i];
		if(value==input.value){
			j++;
			if(j==1){
				flag=false;
				break;
			}
		}
	}

    return flag; 
}, "不能重复");

//此方法仅仅限于表格验证
$.validator.addMethod("selExpertNumValid", function(value, element) { 
	var flag=true;
	var projectLevelObj = $("#projectLevel");
	if(projectLevelObj.val()=="0003"){
		flag = true;	
	}else{
		if(parseInt(fixMath(parseInt(value),3,"*"))<parseInt(fixMath(parseInt($("#assessBidTotalNumber").val()) , 2 ,"*" ))){
			flag = false;	
		}	
	}

    return flag; 
}, "必须大于评标委员会总人数2/3以上");



$.validator.addMethod("unique", function(value, element) { 
	var flag = false;
	var id = "";
	var tableKey = $(element).attr("tableKey");
	if(tableKey!=null&&tableKey!=""){
		id = $("#"+tableKey).val();
	}
	if(id==""){
		id="-1";	
	}
	if($(element).val()==""){
		return true;	
	}
	doManager($(element).attr("managerName"), $(element).attr("managerMethod"),[id,$(element).attr("name"),$(element).val()],function(data, textStatus, XMLHttpRequest){
		if (data.data == "true") {
			flag = false;
		}
		if (data.data == "false") {
			flag = true;
		}	
	 },false);
	
	

    return flag; 
});

$.validator.addMethod("precision", function(value, element,params) {
	var arr = value.split(".");
	if(arr.length>1){
		if(arr[1].length>parseInt(params[1])){
			return false;
		}	
	}
	return true;
});

$.validator.addMethod("exists", function(value, element) {
    var flag = false;
	var data = [$(element).attr("existsValidClass"), $(element).attr("existsValidName")==undefined?$(element).attr("name"):$(element).attr("existsValidName"), $(element).val(), (($("#"+$(element).attr("existsValidId")).length==0)||($("#"+$(element).attr("existsValidId")).val()==""))?null:$("#"+$(element).attr("existsValidId")).val()];
    var reqObj = new PMSRequestObject("infoManager", "isExistsByFieldValue", data);
    $.ajax({
        type: "POST",
        url: $$.PMSDispatchActionUrl,
        async: false, // Ajax同步
        dataType: "json",
        data: "requestString=" + encodeURIComponent(reqObj.toJsonString()),
        success: function(data, textStatus, XMLHttpRequest){
            if (data.data == "true") {
					flag = false;
			}
            if (data.data == "false") {
				flag = true;
			}	
         }
    });
    return flag;
},"已存在");

/*
 *新建招标代理机构的时候， 判断招标代理机构是否已存在
 */
$.validator.addMethod("isOrgExists", function(value, element) {
    var flag = false;
	var bidderAgentDTO = {
		id:$("#bidderAgentId").val(),
		agentCode:$("#agentCode").val(),
		orgCode:value
	}
	doManager("bidderAgentManager","isExistsByOrgCode",bidderAgentDTO,function(data){
		if (data.result) {
			if (data.data == "true") {
				flag = false;
			}
			if (data.data == "false") {
				flag = true;
			}
		}
	},false);
    
    return flag;
},"该组织机构的代理机构已存在");





/*
 * 判断估算金额后者输入框大于前者输入框
 */
$.validator.addMethod("numberRelation", function(value, element) {
	var selfValue = parseFloat($("#estimateAmount1").val()==""?"0":$("#estimateAmount1").val());
	var relationValue = parseFloat($("#estimateAmount2").val()==""?"0":$("#estimateAmount2").val());
	($("#estimateAmount").find("label").remove());
	if(selfValue > relationValue){
		return false;
	}
	return true;
}, "后者金额要大于前者金额！ ");

$.validator.addMethod("country", function(value, element) {
	if(value == '国家'){
		return false;
	}
	return true;
}, "请选择 ");
$.validator.addMethod("province", function(value, element) {
	if(value == '省份、州'){
		return false;
	}
	return true;
}, "请选择 ");
$.validator.addMethod("city", function(value, element) {
	if(value == '地级市、县'){
		return false;
	}
	return true;
}, "请选择 ");
$(function(){
	$.metadata.setType("attr", "validate");	
});
