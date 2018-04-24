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
	this.PMSDispatchActionUrl = getRootPath() + "/dispatcher.action";
	this.formElement = "form[class*='pmsForm']";
	this.selectElement = "input[class*='pmsSelect']";
	this.radioElement = "input[class*='pmsRadio']";
	this.checkboxElement = "input[class*='pmsCheckbox']";
	this.dateElement = "input[class*='pmsDateField']";
	this.timeElement = "input[class*='pmsTimeField']";
};

/**
 * 初始化所有表单
 */
PMSForm.prototype.initForm = function() {
	//初始化表单元素
	$form.initFormStyle();
	//初始化表单
	var forms = $(this.formElement);
	forms.each(function(i) {
		var display = $(this).attr("display");
		var isDialog = $(this).attr("dialog");
		var show = (display && display=='false') ? false : true;
		$(this).css('display', show ? 'block' : 'none');
		if(show && isDialog && isDialog=='true') {
			$form.showDialog(this);
		} else {
			var form = $(this);
			$(this).find(":input[type='submit']").click(function() {
				var beforeSubmit = form.attr("beforeSubmit");
				var flag = false;
				try {
					var temp = beforeSubmit+'(form)';
					flag = eval(temp);
				} catch(e){
				}
				if( ! flag ) {
					return false;
				}
				$form.submit($(forms[i]));
				return false;
			});
		}
		var bValidate = $(this).attr("validate") == "true" ? true : false;
		this.bValidate = bValidate;
		$(this).removeAttr("validate");
		if(bValidate){
			
			var bClientValidate = $(this).attr("clientvalidate") == "true" ? true : false;
			this.bClientValidate = bClientValidate;
			
			var bServerValidate = $(this).attr("servervalidate") == "true" ? true : false;
			this.bServerValidate = bServerValidate;
			
			var oSettings = {};
			var iDisplayLength = Number($(this).attr("displaynumber"));
			if(iDisplayLength >= 0){
				oSettings = {
					bNormalDisplay : false,
					iDisplayLength : iDisplayLength
				};
			}else{
				oSettings = {
					bNormalDisplay : true
				};
			}
			this.$validator = new PMSValidator(
				this, oSettings);
		}
	});
};
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
};
/**
 * 将配置有dialog="false"的表单以弹出框的形式呈现
 * @param {Object} form
 */
PMSForm.prototype.showDialog = function(form) {
	var width = $(form).attr("width");
	var pmsTitle = $(form).attr("pmsTitle");
	var callback = $(form).attr("callback");
	var beforeSubmit = $(form).attr("beforeSubmit");
	var modal = $(form).attr("modal");
	var buttons = $(form).attr("buttons");
	if(!buttons) buttons = 'ok,tempsave,cancel';
	if( ! width) {
		width = $(form).css("width");
	}
	$form.dialog(form, {
		width:width,
		title:pmsTitle,
		beforeSubmit:beforeSubmit,
		buttons:buttons,
		callback:callback,
		modal:modal
	});
};
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
		buttons:'ok,tempsave,cancel',
		reset:false,
		beforeSubmit:function(form) {
			//default do nothing.
			return true;
		},
		modal:true
	};
	var options = $.extend(defaults, options);
	var btns = options.buttons.split(',');
	options.buttons = new Object();
	for(var i=0; i<btns.length; i++) {
		if(btns[i]=='ok') {
			options.buttons["${form.ui.ok}"] = function(){
				var flag = false;
				try {
					flag = options.beforeSubmit(form);
				} 
				catch (e) {
					var temp = options.beforeSubmit + '(form)';
					flag = eval(temp);
				}
				if (!flag) {
					return false;
				}
				$form.submit(form, function(data, textStatus, XMLHttpRequest){
					$(form).dialog("close");
					try {
						options.callback(data, textStatus, XMLHttpRequest);
					} 
					catch (e) {
						eval(options.callback + '(data, textStatus, XMLHttpRequest)');
					}
				}, true, false);
			};
		} 
		else if(btns[i]=='tempsave') {
			options.buttons["${form.ui.tempsave}"]=function(){
				$form.cleanValidate(form);
				var flag = false;
				try {
					flag = options.beforeSubmit(form);
				} catch(e){
					var temp = options.beforeSubmit+'(form)';
					flag = eval(temp);
				}
				if( ! flag ) {
					return false;
				}
				$form.submit(form, function(data, textStatus, XMLHttpRequest) {
					$(form).dialog("close");
					try{
						options.callback(data, textStatus, XMLHttpRequest);
					}catch(e) {
						eval(options.callback+'(data, textStatus, XMLHttpRequest)');
					}
				}, true, true);
			};
		} else if(btns[i]=='reset') {
			options.buttons["${form.ui.reset}"]=function() {
				$(form)[0].reset();
			};
		} else if(btns[i]=='cancel') {
			options.buttons["${form.ui.cancel}"]=function() {
				$(form)[0].reset();
				$(form).dialog("close");
			};
		} else {
			
		}
	}
	if(options.reset) {
		$(form)[0].reset();
	}
	$(form).dialog({
		width:options.width,
		title:options.title,
		buttons : options.buttons,
		modal:options.modal
	});
};

/**
 * 重置表单
 * @param {Object} formId
 */
PMSForm.prototype.reset = function(formId) {
	var form = $("#" + formId);
	form[0].reset();
};

/**
 * 清空表单校验的错误信息
 */
PMSForm.prototype.cleanValidate = function(form) {
	if(form.bValidate){
		var validator = form.$validator;
		validator.clean(form);
	}
};

/**
 * 客户端验证表单 
 * @param {Object} form
 */
PMSForm.prototype.clientValidate = function(form){
	var passed = true;
	if(form.bValidate){
		this.cleanValidate(form);
		if(form.bClientValidate){
			var validator = form.$validator;
			passed = validator.clientValidate();
			
			var bRevalidate = $(form).attr("revalidate") == "true" ? true : false;
			if(bRevalidate){
				passed = validator.clientRevalidate();
			}
		}
	}else{
		passed = true;
	}
	return passed;
};

/**
 * 提交表单 
 * @param {Object} form
 */
PMSForm.prototype.submit = function(form, callback, bServerValidate, isTempSave) {
	var bServerValidateFinal = false;
	
	if(form.bValidate){
		if(form.bClientValidate){
			var pass = this.clientValidate(form);
			if(pass == false){
				return false;
			}
		}
		if(form.bServerValidate){
			bServerValidateFinal = bServerValidate;
		}else{
			bServerValidateFinal = false;
		}
	}
	
	var ajax = $(form).attr("ajax");
	if(ajax && ajax == 'false') {
		$(form)[0].submit();
	} else {
		$form.ajaxSubmit(form, callback, bServerValidateFinal, isTempSave);
	}
};
/**
 * gather the form data(inputs).
 * @param {Object} form
 */
PMSForm.prototype.gatherData = function(form) {
	var inputs = $(form).find(":input[name]");
	var obj = new Object();
	var process = function(key, value, obj, flag) {
		var keys = key.split('.');
		var len = keys.length;
		switch(len) {
			case 2:
				if(typeof obj[keys[0]] != 'object')
					obj[keys[0]] = new Object();
				if(flag && obj[keys[0]][keys[1]] && obj[keys[0]][keys[1]].length > 0)
					obj[keys[0]][keys[1]] += (',' + value);
				else 
					obj[keys[0]][keys[1]]=value;
				break;
			case 3:
				if(typeof obj[keys[0]] != 'object')
					obj[keys[0]] = new Object();
				if(typeof obj[keys[0][keys[1]]] != 'object')
					obj[keys[0][keys[1]]] = new Object();
				if(flag && obj[keys[0]][keys[1]][keys[2]] && obj[keys[0]][keys[1]][keys[2]].length > 0)
					obj[keys[0]][keys[1]][keys[2]] += (',' + value);
				else 
					obj[keys[0]][keys[1]][keys[2]]=value;
				break;
			case 4:
				if(typeof obj[keys[0]] != 'object')
					obj[keys[0]] = new Object();
				if(typeof obj[keys[0][keys[1]]] != 'object')
					obj[keys[0][keys[1]]] = new Object();
				if(typeof obj[keys[0][keys[1]][keys[2]]] != 'object')
					obj[keys[0][keys[1]][keys[2]]] = new Object();
				if(flag && obj[keys[0]][keys[1]][keys[2]][keys[3]] && obj[keys[0]][keys[1]][keys[2]][keys[3]].length > 0)
					obj[keys[0]][keys[1]][keys[2]][keys[3]] += (',' + value);
				else 
					obj[keys[0]][keys[1]][keys[2]][keys[3]]=value;
				break;
			case 5:
				if(typeof obj[keys[0]] != 'object')
					obj[keys[0]] = new Object();
				if(typeof obj[keys[0][keys[1]]] != 'object')
					obj[keys[0][keys[1]]] = new Object();
				if(typeof obj[keys[0][keys[1]][keys[2]]] != 'object')
					obj[keys[0][keys[1]][keys[2]]] = new Object();
				if(typeof obj[keys[0][keys[1]][keys[2]][keys[3]]] != 'object')
					obj[keys[0][keys[1]][keys[2]][keys[3]]] = new Object();
				if(flag && obj[keys[0]][keys[1]][keys[2]][keys[3]][keys[4]] && obj[keys[0]][keys[1]][keys[2]][keys[3]][keys[4]].length > 0)
					obj[keys[0]][keys[1]][keys[2]][keys[3]][keys[4]] += (',' + value);
				else 
					obj[keys[0]][keys[1]][keys[2]][keys[3]][keys[4]]=value;
				break;
			default:
				obj[key]=value;
				break;
		}
	};
	inputs.each(function(i) {
		var type = $(this).attr('type');
		var flag = false;
		if(type == 'checkbox' || type == 'radio') flag = true;
		var key = $(this).attr('name');
		var value = $(this).val();
		value = value.replace(/(\\)/g, "\\\\\\\\").replace(/(\")/g, "\\\\\\\"").replace(/(\r)/g, "\\\\r").replace(/(\n)/g, "\\\\n").replace(/(\t)/g, "\\\\t");
		value = encodeURIComponent(value);
		if(key.indexOf('.') <= 0) {
			if(flag === true) {
				if($(this).attr('checked') === true) {
					if(obj[key] && obj[key].length > 0) {
						obj[key] += (',' + value);
					} else {
						obj[key] = value;
					}
				}
			} else {
				obj[key] = value;
			}
		} else {
			process(key, value, obj, flag);
		}
	});
	return $.toJSON(obj);
//	var objson = $.toJSON(obj);
//	objson = encodeURIComponent(objson);
//	return objson;
};


/**
 * ajax 提交表单
 * @param {Object} form
 */
PMSForm.prototype.ajaxSubmit = function(form, callback, bServerValidateFinal, isTempSave) {
	var url = $(form).attr("action");
	var managerName = $(form).attr("pmsmanager");
	var methodName = $(form).attr("pmsmethod");
	var queryId = $(form).attr("queryId");
	var token = $(form).attr("token");
	if( !url ) url = this.PMSDispatchActionUrl;
	if( !managerName ) managerName = "baseManager";
	if( !methodName ) methodName = "saveObj";
	if( !token ) token = null;
	var params = $form.gatherData(form);
	var parameters = [];
	if(queryId && queryId.length > 0) {
		parameters = [ queryId, params ];
	} else {
		if(!methodName) methodName = "saveObject";
		parameters = [ params ];
	}
	var reqObj = new PMSRequestObject(managerName, methodName, parameters, token);
	$$.ajax(url,
		"requestString=" + reqObj.toJsonString(), 
		function(data, textStatus, XMLHttpRequest){
			//alert(data.message);
			if(data.result) {
//				if(isTempSave !== true) {
					$form.checkRepeatable(form);
//				}
				if (bServerValidateFinal) {
					var $validator = form.$validator;
					if($validator.isServerInvalid(data.message)){
                		$validator.showServerInvalidError(data.message);
					}
				}else{
					if(callback && typeof callback == 'function') {
						callback(data,textStatus,XMLHttpRequest);
					}
				}
			} else {
				var errDiv = $(form).attr("errDiv");
				if(errDiv){
					$(form).find("div[name="+errDiv+"]").html(textStatus);
				}else{
					$$.showMessage('${query.ui.prompt}','${query.ui.failure}');
				}
			}
		}
	);
};

/**
 * 初始化所有表单中的元素（包括select,checkbox,radio）
 * 只要是:input[class='pmsSelect/pmsRadio/pmsCheckbox']，均会被该方法初始化
 */
PMSForm.prototype.initFormStyle = function() {
	var form = $(".pmsForm");
	form.each(function(index) {
		$form.initSelectElement(this);
		$form.initCheckboxElement(this);
		$form.initRadioElement(this);
		$form.initDateElement(this);
		$form.initTimeElement(this);
		$form.checkRepeatable(this);
	});
};
/**
 * check repeatable.
 * @param {Object} form
 */
PMSForm.prototype.checkRepeatable = function(form) {
	var repeatable = $(form).attr('repeatable') == 'false' ?
			false : true;
	if(repeatable === false) {
		//同步发送
		$$.ajaxAsync(this.PMSDispatchActionUrl, "repeatable=false", function(data, textStatus, XMLHttpRequest) {
			if(data.result) {
				$(form).attr('token', data.token);
			} else {
			}
		});
	}
};
/**
 * 初始化select元素
 * @param {Object} form
 */
PMSForm.prototype.initSelectElement = function(form) {
	var elements = $(form).find(":" + this.selectElement);
	$(elements).each(function(i) {
		var data = $(this).attr("data");
		data = eval(data);
		if($(this).children().length <= 0 && data) {
			for(var i=0; i<data.length; i++) {
				var option = $("<option value='" + data[i][0] + "'>" + data[i][1] + "</option>");
				if(data[i][2] && data[i][2]=='selected') {
					option.attr("selected", "selected");
				}
				$(this).append(option);
			}
		} else {
			var src = $(this).attr("src");
			var autoLoad = $(this).attr("autoload") == 'false' ? false : true;
			if(src && src.length > 0) {
				if(autoLoad) {
					$form.buildAjaxSelect(this, src);
				} else {
					var _that = this;
					$(this).click(function(){
						$form.buildAjaxSelect(_that, src);
					});
				}
			} else {
				$(this).replaceWith("<select/>");
			}
		}
	});
};
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
			var onClick = $(this).attr('clicked');
			for(var i=0; i<data.length; i++) {
				var adata = data[i];
				var radio;
				if(eName && eName != '') {
					var sb = "<input type='radio' name='" + eName + "'";
					if(adata[2] && adata[2] == 'checked') {
						sb += " checked='true' ";
					}
					sb += " value='"+adata[0]+"'>" + adata[1] + "</input>";
					radio = $(sb);
				}
				$(this).before(radio);
				if(onClick) {
					radio.click(function() {
						return eval(onClick + '(this)');
					});
				}
			}
			$(this).remove();
		} else {
			var src = $(this).attr("src");
			if (src && src.length > 0) {
				$form.buildAjaxRadio(this, src);
			} else {
				$(this).replaceWith("<input type='radio'");
			}
		}
	});
};
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
			var onClick = $(this).attr('clicked');
			var validations = $(this).attr('validate');
			for(var i=0; i<data.length; i++) {
				var adata = data[i];
				var sb = "<input type='checkbox' name='" + adata[0] + "'";
				if(adata[3] && adata[3] == 'checked') {
					sb += " checked='true' ";
				}
				sb += " value='"+adata[1]+"'>" + adata[2] + "</input>";
				var checkbox = $(sb);
				checkbox.attr('validate', validations);
				
				$(this).before(checkbox);
				if(onClick) {
					checkbox.click(function() {
						return eval(onClick + '(this)');
					});
				}
			}
			$(this).remove();
		} else {
			var src = $(this).attr("src");
			if (src && src.length > 0) {
				$form.buildAjaxCheckbox(this, src);
			} else {
				$(this).replaceWith("<input type='checkbox'");
			}
		}
	});
};
/**
 * 初始化date选择器元素
 * @param {Object} form
 */
PMSForm.prototype.initDateElement = function(form) {
	var elements = $(form).find(":" + this.dateElement);
	var rootpath = getRootPath();
	var iconpath = rootpath + "/scripts/common/images/calendar.gif";
	$(elements).each(function(i) {
		var format = $(this).attr("format") ? 
				$(this).attr("format") : "yy-mm-dd";
		var changeMonth = $(this).attr("changeMonth") == 'false' ? 
				 false : true;
		var changeYear = $(this).attr("changeYear") == 'false' ? 
				 false : true;
		var yearRange = $(this).attr("yearRange") ? 
				$(this).attr("yearRange") : "-10:+10";
		var onSelect = $(this).attr("selected") ? 
				$(this).attr("selected") : null;
		var defaultDateStr = $(this).attr("defaultDate") ? 
				$(this).attr("defaultDate") : null;
		var defaultDate = new Date();
		if (defaultDateStr != null && defaultDateStr.length > 0) {
			//defaultDate="y=2012,m-4,d+9"
			var year = defaultDate.getYear();
			var month = defaultDate.getMonth();
			var day = defaultDate.getDate();
			
			var arr = defaultDateStr.split(",");
			var year = defaultDate.getYear();
			var month = defaultDate.getMonth();
			var day = defaultDate.getDate();
			for (var ai=0; ai < arr.length; ai++) {
				var op = arr[ai];
				var attr = op.substring(0,1);
				var ope = op.substring(1,2);
				var v = op.substring(2);
				if (attr == 'y') {
					if (ope == '-') {
						year = year - parseInt(v);
					} else if (ope == '+') {
						year = year + parseInt(v);
					} else {
						year = parseInt(v);
					}
				} else if (attr == 'm') {
					if (ope == '-') {
						month = month - parseInt(v);
					} else if (ope == '+') {
						month = month + parseInt(v);
					} else {
						month = parseInt(v);
					}
				} else if (attr == 'd') {
					if (ope == '-') {
						day = day - parseInt(v);
					} else if (ope == '+') {
						day = day + parseInt(v);
					} else {
						day = parseInt(v);
					}
				}
			}
			defaultDate = new Date(year, month, day);
		}
		$(this).datepicker({
			showOn: "button",
			buttonImage: iconpath,
			buttonImageOnly: true,
			changeMonth: changeMonth,
			changeYear: changeYear,
			yearRange:yearRange,
			defaultDate:defaultDate,
			onSelect : function(inputText, instance) {
				var $validator = $(form).data("PMSValidator");
				if($validator){
					$validator.validator.element(this);
				}
				if (onSelect === null) {
					return true;
				} else {
					return eval(onSelect + '("' + inputText + '")');
				}
			},
			dateFormat:format
		});
	});
};

/**
 * 初始化time选择器元素
 * @param {Object} form
 */
PMSForm.prototype.initTimeElement = function(form) {
	var elements = $(form).find(":" + this.timeElement);
	var rootpath = getRootPath();
	var iconpath = rootpath + "/scripts/common/images/calendar.gif";
	$(elements).each(function(i) {
		var format = $(this).attr("format") ? 
				$(this).attr("format") : "yy-mm-dd";
		var changeMonth = $(this).attr("changeMonth") == 'false' ? 
				 false : true;
		var changeYear = $(this).attr("changeYear") == 'false' ? 
				 false : true;
		var yearRange = $(this).attr("yearRange") ? 
				$(this).attr("yearRange") : "-10:+10";
		var timeFormat = $(this).attr("timeFormat") ? 
				$(this).attr("timeFormat") : "hh:mm tt";
		var showTime = $(this).attr("showTime") == 'false' ? 
				false : true;
		var showSecond = $(this).attr("showSecond") == 'true' ? 
				true : false;
		var onSelect = $(this).attr("selected") ? 
				$(this).attr("selected") : null;
		$(this).datetimepicker({
			showOn: "button",
			buttonImage: iconpath,
			buttonImageOnly: true,
			changeMonth: changeMonth,
			changeYear: changeYear,
			yearRange:yearRange,
			showTime:showTime,
			showSecond:showSecond,
			timeFormat:timeFormat,
			onSelect : function(inputText, instance) {
				var $validator = $(form).data("PMSValidator");
				if($validator){
					$validator.validator.element(this);
				}
				if (onSelect === null) {
					return true;
				} else {
					return eval(onSelect + '("' + inputText + '")');
				}
			},
			dateFormat:format
		});
	});
};
/**
 * 构建ajax方式的select
 * @param {Object} selector
 * @param {Object} dictName
 */
PMSForm.prototype.buildAjaxSelect = function(selector, dictName) {
	var sel = $(selector);
	if(sel.children().length > 0) {
		return false;
	}
	var cked = sel.attr("checkeds");
	var generateBlankOption = sel.attr('showblank') == 'true' ? true : false;
	var build = function(data) {
		if(generateBlankOption === true) sel.append($('<option></option>'));
		var dicts = data;
		if(dicts && dicts.length > 0) {
			for(var i=0; i<dicts.length; i++) {
				var option = $('<option value="' + dicts[i].dictCode + '">' + dicts[i].dictText +'</option>');
				if(cked && cked == dicts[i].dictCode) {
					option.attr("selected", "selected");
				}
				//2012-10-12 郝成杰修改，加入在页面是否显示
				if(dicts[i].disabledFlag == 1) {
					option.attr("disabled", true);
				}
				sel.append(option);
			}
		}
	};
	$dict.getDict(dictName, build);
};
/**
 * 构建ajax方式的radio
 * @param {Object} radio
 * @param {Object} dictName
 */
PMSForm.prototype.buildAjaxRadio = function(radio, dictName) {
	var rd = $(radio);
	var cked = rd.attr("checkeds");
	var onClick = rd.attr('clicked');
	var build = function(data) {
		var dicts = data;
		if (dicts && dicts.length > 0) {
			for(var i=0; i<dicts.length; i++) {
				var sb = "<input type='radio' name='" + rd.attr("name") + "'";
				if(cked && cked == dicts[i].dictCode) {
					sb += " checked='true'";
				}
				sb += " value='"+dicts[i].dictCode+"'>" + dicts[i].dictText + "</input>";
				var radio = $(sb);
				rd.before(radio);
				if(onClick) {
					radio.click(function() {
						return eval(onClick + '(this)');
					});
				}
			}
			rd.remove();
		}
	};
	$dict.getDict(dictName, build);
};
/**
 * 构建ajax方式的checkbox
 * @param {Object} checkbox
 * @param {Object} dictName
 */
PMSForm.prototype.buildAjaxCheckbox = function(checkbox, dictName) {
	var ck = $(checkbox);
	var cked = ck.attr("checkeds");
	var ckeds = [];
	if (cked && cked.length > 0) {
		ckeds = cked.split(",");
	}
	var onClick = ck.attr('clicked');
	var validations = ck.attr('validate');
	var build = function(data) {
		var dicts = data;
		for(var i=0; i<dicts.length; i++) {
			var checkbox = $("<input type='checkbox' name='" + ck.attr("name") + "' value='"+dicts[i].dictCode+"'>" + dicts[i].dictText + "</input>");
			$(checkbox).attr('validate', validations);
			if(ckeds.length > 0) {
				for(var vs=0; vs<ckeds.length; vs++) {
					if(ckeds[vs] == dicts[i].dictCode) {
						$(checkbox).attr("checked", true);
						break;
					}
				}
			}
			ck.before(checkbox);
			if(onClick) {
				checkbox.click(function() {
					return eval(onClick + '(this)');
				});
			}
		}
		ck.remove();
	};
	$dict.getDict(dictName, build);
};

/**
 * load data for form
 * @param {Object} formId
 * @param {Object} url (default "dispatcher.action")
 * @param {Object} reqObj
 */
PMSForm.prototype.loadFormData = function(formId, url, param, callback){
	$$.ajax(url, param, function(data, textStatus, XMLHttpRequest){
		if(data.result) {
			$form.loadData(formId, eval('('+data.data+')'), callback);
		}
	});
};
PMSForm.prototype.loadData = function(formId, obj, callback){
	this.loadDataMethod(formId, obj, false, callback);
};

PMSForm.prototype.importData = function(formId, obj, callback){
	this.loadDataMethod(formId, obj, true, callback);
};


/**
 * put values into form fields.
 * @param {Object} formId
 * @param {Object} obj
 */
PMSForm.prototype.loadDataMethod = function(formId, obj, ignore, callback) {
	var tmpForm = $("#"+formId);
	if(tmpForm.get(0).tagName.toUpperCase() == 'FORM') {
		tmpForm[0].reset();
	}
	for (var attr in obj) {
		if ((obj[attr] == 'undefined') || typeof(obj[attr]) == 'function') {
			continue;
		}
		var ipt = tmpForm.find(" :input[name='" + attr + "']");
		if( ipt.length == 0 && (ignore === false)) {
			tmpForm.append($('<input type="hidden" name="' + attr + '" value="' + obj[attr] + '" />'));
		} else {
			var type = ipt.attr("type");
			if (type == "checkbox" || type == "radio") {
				var tmp = obj[attr];
				if(typeof tmp != 'string') tmp = tmp + '';
				var vs = tmp.split(",");
				for (var v = 0; v < vs.length; v++) {
					ipt.each(function(i, n){
						if ($(this).val() == vs[v]) {
							$(this).attr("checked", true);
						}
//						var value = $(n).val();
//						if (value == vs[v]) {
//							$(n).attr("checked", true);
//						}
					});
				}
			} else {
				ipt.val(obj[attr]);
			}
		}
	}
	if(callback && typeof callback == 'function') {
		callback($("#"+formId)[0], obj);
	}
};

PMSForm.prototype.run = function() {
	$form.initForm();
};

// PMSForm global instance.
var $form = pmsform = new PMSForm();
