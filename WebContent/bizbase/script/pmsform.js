/*!
 * CNPC PMS Web Application JavaScript Library of Form.
 * http://www.cnpc.com.cn/
 * Author:Hewang
 * 
 * usage:
 * ----------------------------------------
 * 第一种方式：(dialog/非dialog)
 * html中声明如下：
 * <script>
 * 		function doSuccess(data) {
 * 			alert("success");
 * 		}
 * </script>
 * <form
 * 		id="exampleForm"
 * 		class="pmsForm" //必须
 * 		dialog="true" //可选，如不为true，则不弹出框
 * 		display="false" //显示与否，默认(不配置)为true
 * 		modal="true" //是否模态，默认为true
 * 		ajax="false" //是否ajax提交，默认(不配置)为true
 * 		pmsTitle="添加供应商信息"
 *		action="dispatcher.action" //form原有配置项
 *		method="post" //form中原有的配置项
 *		queryId="supplierQuery" //queryId
 *		pmsmanager="baseManager"  //在Spring中注册的manager name
 *		pmsmethod="saveObj"  //manager中对应的方法
 *		callback="doSuccess" //表单提交成功后的回调函数
 *		width="500px" //弹出框宽度
 * >  
 * 	...  (表单域)
 * </form>
 * 如未配置dialog，可以通过调用 $form.showDialog($("#exampleForm"));来显示弹出框。
 * 如配置dialog=true,display=false，则可调用 $form.show($("#exampleForm"));来显示弹出框。
 * 
 * -------------------------------------------
 * 第二种方式：(only dialog)
 * html中声明如下：
 * <form
 * 		id="exampleForm2"
 * 		class="pmsForm" //必须
 * 		display="false" //显示与否，默认(不配置)为true
 * 		ajax="false" //是否ajax提交，默认(不配置)为true
 *		action="dispatcher.action" //form原有配置项
 *		method="post" //form中原有的配置项
 *		queryId="supplierQuery" //queryId
 *		pmsmanager="baseManager"  //在Spring中注册的manager name
 *		pmsmethod="saveObj"  //manager中对应的方法
 * >  
 * ...  (表单域)
 * </form>
 * 可通过
 * 	$form.dialog($("#exampleForm2"), {
 *					width:'500px',
 *					title:'添加供应商信息',
 *					reset:true,//重置表单,默认为false
 *					callback:function(data){
 *						//$$.showMessage('提示','添加成功!');
 *						$$.showMessage('提示','添加成功!', function(data){//do some thing..});
 *					},
 *					modal:true
 *				});
 * ----------------------------------------
 */

/**
 * 表单类
 * 只要标注了class="pmsForm"的表单均可被此类截获.
 */
var PMSForm = function() {
	this.PMSDispatchActionUrl = "/dispatcher.action";
	this.formElement = "form[class='pmsForm']";
	this.selectElement = "input[class='pmsSelect']";
	this.radioElement = "input[class='pmsRadio']";
	this.checkboxElement = "input[class='pmsCheckbox']";
	this.dateElement = "input[class='pmsDateField']";
}

/**
 * 初始化所有表单
 */
PMSForm.prototype.initForm = function() {
	//初始化表单元素
	$form.initFormStyle();
	
	//初始化表单
	var myform = $(this.formElement);
	myform.each(function(i) {
		var display = $(this).attr("display");
		var isDialog = $(this).attr("dialog");
		var show = (display && display=='false') ? false : true;
		$(this).css('display', show ? 'block' : 'none');
		if(show && isDialog && isDialog=='true') {
			$form.showDialog(this);
		} else {
			$(this).find(":input[type='submit']").click(function() {
				$form.submit($(myform[i]));
				return false;
			});
		}
	});
}
/**
 * 将配置有dialog="true"并且display=false的表单显示
 * @param {Object} form
 */
PMSForm.prototype.show = function(form) {
	var isDialog = $(form).attr("dialog");
	if(isDialog && isDialog=='true') {
		$form.showDialog(form);
	} else {
		$(form).css('display','block');
	}
}
/**
 * 将配置有dialog="false"的表单以弹出框的形式呈现
 * @param {Object} form
 */
PMSForm.prototype.showDialog = function(form) {
	var width = $(form).attr("width");
	var pmsTitle = $(form).attr("pmsTitle");
	var callback = $(form).attr("callback");
	var modal = $(form).attr("modal");
	if( ! width) {
		width = $(form).css("width");
	}
	$form.dialog(form, {
		width:width,
		title:pmsTitle,
		callback:callback,
		modal:modal
	});
}
/**
 * 将表单转换成弹出框的形式呈现
 * @param {Object} form
 */
PMSForm.prototype.dialog = function(form, options) {
	var defaults = {
		title:'',
		width:'480px',
		callback:function(data){
			$$.showMessage('${query.ui.prompt}','${query.ui.success}', function(data){
				$(form).dialog("close");
			});
		},
		reset:false,
		buttons:{
			"${query.ui.ok}":function(){
				$form.submit(form, function(data) {
					$(form).dialog("close");
					try{
						options.callback(data);
					}catch(e) {
						eval(options.callback+'(data)');
					}
				});
			},
			"${query.ui.cancel}":function(){
				$(form)[0].reset();
				$(form).dialog("close");
			}
		},
		modal:true
	};
	var options = $.extend(defaults, options);
	
	if(options.reset) {
		$(form)[0].reset();
	}
	$(form).dialog({
		width:options.width,
		title:options.title,
		buttons : options.buttons,
		modal:options.modal
	});
}

/**
 * 重置表单
 * @param {Object} formId
 */
PMSForm.prototype.reset = function(formId) {
	var form = $("#" + formId);
	form[0].reset();
}

/**
 * 提交表单 
 * @param {Object} form
 */
PMSForm.prototype.submit = function(form, callback) {
	var ajax = $(form).attr("ajax");
	if(ajax && ajax == 'false') {
		$(form)[0].submit();
	} else {
		$form.ajaxSubmit(form, callback);
	}
}

/**
 * ajax 提交表单
 * @param {Object} form
 */
PMSForm.prototype.ajaxSubmit = function(form, callback) {
	var url = $(form).attr("action");
	var managerName = $(form).attr("pmsmanager");
	var methodName = $(form).attr("pmsmethod");
	var queryId = $(form).attr("queryId");
	var inputs = $(form).find(":input[name]");
	var params = "{";
	for(var i=0; i<inputs.length; i++) {
		var input = $(inputs[i]);
		var ty = input.attr('type');
		var name = input.attr("name");
		if(ty == 'checkbox' || ty == 'radio') {
			continue;	
		}
		var val = input.val();
		params += '"' + name + '":"';
		params += val + '",';
	}
	var chkInputs = $(form).find(" :input[name][checked]");
	var chkMap = new Map();
	chkInputs.each(function(i) {
		var name = $(this).attr("name");
		var val = $(this).val();
		if(chkMap.indexOf(name) != -1) {
			chkMap.setValue(name, chkMap.get(name) + ',' + val);			
		} else {
			chkMap.put(name, val);
		}
	});
	for(var i=0; i<chkMap.size(); i++) {
		var entry = chkMap.getEntry(i);
		params += '"' + entry.key + '":"';
		params += entry.value + '",';
	}
	params = params.substr(0, params.length-1) + "}";
	var saveObj = {
		queryId:queryId,
		presaveJsonData:params
	}
	var reqObj = new PMSRequestObject(managerName, methodName,
		 [ $.toJSON(saveObj) ]);
	$$.ajax(url,
		"requestString=" + reqObj.toJsonString(), 
		function(data, textStatus, XMLHttpRequest){
			if(data.result) {
				callback(data,textStatus,XMLHttpRequest);
			} else {
				$$.showMessage('${query.ui.prompt}','${query.ui.failure}');
			}
		}
	);
}

/*!
 * PMSMap:
 * 	方法：
 * 	1.put(key,value)
 * 	2.get(key) --> value
 * 	3.size() -->the size of map
 *  4.remove(key)
 *  5.setValue(key, value)
 *  6.getEntry(index) -->{key:xx,value:xx}
 *  7.indexOf(key) -->the index of key
 *  8.isEmpty --> true/false.
 */
var Map = function(){
    var Entry = function(key, value){
        this.key = key;
        this.value = value;
    }
	this.entries = new Array();
    this.put = function(key, value){
        for (var i = 0; i < this.entries.length; i++) {
            if (this.entries[i].key === key) {
                return false;
            }
        }
        this.entries.push(new Entry(key, value));
		return true;
    }
    this.get = function(key){
        for (var i = 0; i < this.entries.length; i++) {
            if (this.entries[i].key === key) {
                return this.entries[i].value;
            }
        }
        return null;
    }
	this.remove = function(key) {
		var index = this.indexOf(key);
		if(index != -1) {
			this.entries.splice(index,1);
		}
	};
	this.setValue = function(key, value) {
		var index = this.indexOf(key);
		if(index != -1) {
			this.entries[index].value = value;
		}
	};
	this.getEntry = function(index) {
		if(index >= 0 && index < this.size()) {
			return this.entries[index];
		}
		return null;
	}
    this.size = function(){
        return this.entries.length;
    }
    this.isEmpty = function(){
        return this.entries.length <= 0;
    }
	this.indexOf = function(key) {
		var index = -1;
		for(var i=0; i<this.size(); i++) {
			if(key == this.entries[i].key) {
				index = i;
				break;
			}
		}
		return index;
	};
	this.toString = function() {
		var str = '[';
		for(var i=0; i<this.size(); i++) {
			str += (this.entries[i].key+'='+this.entries[i].value+',')
		}
		str = str.substr(0, str.length-1);
		str += ']';
		return str;
	};
}

/**
 * 初始化所有表单中的元素（包括select,checkbox,radio）
 * 只要是:input[class='pmsSelect/pmsRadio/pmsCheckbox']，均会被该方法初始化
 */
PMSForm.prototype.initFormStyle = function() {
	var form = $("form");
	form.each(function(index) {
		$form.initSelectElement(form);
		$form.initCheckboxElement(form);
		$form.initRadioElement(form);
		$form.initDateElement(form);
	});
}
/**
 * 初始化select元素
 * @param {Object} form
 */
PMSForm.prototype.initSelectElement = function(form) {
	var elements = $(form).find(":" + this.selectElement);
//	alert("select=" + elements.length);
	$(elements).each(function(i) {
		var data = $(this).attr("data");
		data = eval(data);
		if(data) {
			for(var i=0; i<data.length; i++) {
				var option = $("<option value='" + data[i][0] + "'>" + data[i][1] + "</option>");
				if(data[i][2] && data[i][2]=='selected') {
					option.attr("selected","selected");
				}
				$(this).append(option);
			}
		} else {
			$(this).replaceWith("<select/>");
		}
	});
}
/**
 * 初始化radio元素
 * @param {Object} form
 */
PMSForm.prototype.initRadioElement = function(form) {
	var elements = $(form).find(":" + this.radioElement);
	$(elements).each(function(i) {
		var data = $(this).attr("data");
		var eName = $(this).attr("name");
		data = eval(data);
		if(data) {
			for(var i=0; i<data.length; i++) {
				var adata = data[i];
				var radio;
				if(eName && eName != '') {
					radio = $("<input type='radio' name='" + eName + "' value='"+adata[0]+"'>" + adata[1] + "</input>");
					if(adata[2] && adata[2] == 'checked') {
						radio.attr("checked",true);
					}
				}
				$(this).before(radio);
			}
			$(this).remove();
		} else {
			$(this).replaceWith("<input type='radio'");
		}
	});
}
/**
 * 初始化checkbox元素
 * @param {Object} form
 */
PMSForm.prototype.initCheckboxElement = function(form) {
	var elements = $(form).find(":" + this.checkboxElement);
	$(elements).each(function(i) {
		var data = $(this).attr("data");
		data = eval(data);
		if(data) {
			for(var i=0; i<data.length; i++) {
				var adata = data[i];
				var checkbox = $("<input type='checkbox' name='" + adata[0] + "' value='"+adata[1]+"'>" + adata[2] + "</input>");
				if(adata[3] && adata[3] == 'checked') {
					checkbox.attr("checked","checked");
				}
				$(this).before(checkbox);
			}
			$(this).remove();
		} else {
			$(this).replaceWith("<input type='checkbox'");
		}
	});
}
/**
 * 初始化date选择器元素
 * @param {Object} form
 */
PMSForm.prototype.initDateElement = function(form) {
	var elements = $(form).find(":" + this.dateElement);
	$(elements).each(function(i) {
		$(this).datepicker({
			dateFormat:"yy-mm-dd",
			changeMonth: true,
			changeYear: true,
			yearRange:'-100:+100',
			onSelect: function(dateText, inst) {
					$("#valid-date-label3").css('display',"none");
			}
		});
	});
}

/**
 * load data for form
 * @param {Object} formId
 * @param {Object} url (default "dispatcher.action")
 * @param {Object} reqObj
 */
PMSForm.prototype.loadFormData = function(formId, url, param){
	$$.ajax(url, param, function(data, textStatus, XMLHttpRequest){
		if(data.result) {
			$form.loadData(formId, eval('('+data.data+')'));
		}
	});
}
/**
 * put values into form fields.
 * @param {Object} formId
 * @param {Object} obj
 */
PMSForm.prototype.loadData = function(formId, obj) {
	var tmpForm = $("#"+formId);
	tmpForm[0].reset();
	for (var attr in obj) {
		if ((!obj[attr]) || typeof(obj[attr]) == 'function') {
			continue;
		}
		var ipt = tmpForm.find(" :input[name='" + attr + "']");
		if( ipt.length == 0) {
			tmpForm.append($('<input type="hidden" name="' + attr + '" value="' + obj[attr] + '" />'));
		} else {
			var type = ipt.attr("type");
			if (type == "checkbox" || type == "radio") {
				var vs = obj[attr].split(",");
				for (var v = 0; v < vs.length; v++) {
					ipt.each(function(i, n){
						var value = $(n).val();
						if (value == vs[v]) {
							$(n).attr("checked", true);
						}
					});
				}
			} else {
				ipt.val(obj[attr]);
			}
		}
	}
	$form.show($("#"+formId)[0]);
}


$(document).ready(function() {
	$form.initForm();
});

var $form = pmsform = new PMSForm();

