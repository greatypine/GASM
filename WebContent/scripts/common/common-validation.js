var PMSValidator;

(function($) {
	$.extend(jQuery.validator.messages, {
		required : "请输入",
		remote : "请修正该字段",
		email : "请输入正确格式的电子邮件",
		url : "请输入合法的网址",
		date : "请输入合法的日期",
		dateISO : "请输入合法的日期 (ISO).",
		number : "请输入合法的数字",
		digits : "只能输入整数",
		creditcard : "请输入合法的信用卡号",
		equalTo : "请再次输入相同的值",
		accept : "请输入拥有合法后缀名的字符串",
		maxlength : jQuery.validator.format("请输入一个长度最多是 {0} 的字符串"),
		minlength : jQuery.validator.format("请输入一个长度最少是 {0} 的字符串"),
		rangelength : jQuery.validator.format("请输入一个长度介于 {0} 和 {1} 之间的字符串"),
		range : jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		max : jQuery.validator.format("请输入一个最大为 {0} 的值"),
		min : jQuery.validator.format("请输入一个最小为 {0} 的值")
	});
	$.validator.setDefaults({
		errorPlacement : function(label, element) {
			switch ($(element).attr("type").toLowerCase()) {
			case "checkbox":
			case "radio":
				var an = $("[name=" + element.attr('name') + "]", this.nForm);
				label.insertAfter(an[an.length - 1].nextSibling);
				break;
			default:
				var nAppendTo = element[0];
				nAppendTo = nAppendTo.parentNode.lastChild;
				label.insertAfter(nAppendTo);
				break;
			}
		},
		submitHandler : function(form) {
			// // do other stuff for a valid form
			// alert('ok11');
			// form.submit();
		},
		invalidHandler : function(form, validator) {
			// var errors = validator.numberOfInvalids();
			// alert(errors);
			// if (errors) {
			// var message = errors == 1 ? 'You missed 1 field. It has been highlighted' : 'You missed ' + errors + '
			// fields. They have been highlighted';
			// $("div.error span").html(message);
			// $("div.error").show();
			// }
			// else {
			// $("div.error").hide();
			// }
		},
		highlight : function(input) {
			// alert('hid');
			// $(input).addClass("ui-state-highlight");
		},
		unhighlight : function(input) {
			// alert('unhid');
			// $(input).removeClass("ui-state-highlight");
		}
	});

	$.validator.addMethod("fullName", function(value, element) {
		return value == "" || value == undefined || (!/\s/.test(value) && value.length <= 11);
	}, "姓名中不能有空格，长度不能大于11");

	$.validator.addMethod("mobile", function(value, element) {
		// var tel = /^(130|131|132|133|134|135|136|137|138|139|150|153|157|158|159|180|187|188|189)\d{8}$/;
		var tel = /^\d{11}$/;
		return this.optional(element) || tel.test(value);
	}, "请输入正确的手机号码（11位）");

	// $.validator.addMethod("nation", function(value, element) {
	// return true;
	// }, "");

	$.validator.addMethod("newIdCard", function(value, element) {
		return this.optional(element) || isIdCard(value);
	}, "请正确输入您的身份证号码 ");

	$.validator.addMethod("formatDate", function(value, element) {
		return value == "" || value == undefined || /^\d{4}-\d{2}-\d{2}$/.test(value);
	}, "请输入正确格式的日期（YYYY-MM-DD）");

	function isIdCard(sId) {
		var aCity = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		};
		var iSum = 0;
		// var info = "";
		if (sId == "")
			return true;
		if (!/^\d{17}(\d|x)$/i.test(sId))
			return false;
		sId = sId.replace(/x$/i, "a");
		if (aCity[parseInt(sId.substr(0, 2))] == null)
			return false;
		sBirthday = sId.substr(6, 4) + "-" + Number(sId.substr(10, 2)) + "-" + Number(sId.substr(12, 2));
		var d = new Date(sBirthday.replace(/-/g, "/"));
		if (sBirthday != (d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate()))
			return false;
		for ( var i = 17; i >= 0; i--)
			iSum += (Math.pow(2, i) % 11) * parseInt(sId.charAt(17 - i), 11);
		if (iSum % 11 != 1)
			return false;
		return true;
	}

	/**
	 * PMSValidator
	 */
	PMSValidator = function(nForm, oSettings) {
		this.MESSAGE_PREFIX = "_validation:";

		this.clientErrSel = "label.error";

		this.oSettings = $.extend({
			bNormalDisplay : false,
			iDisplayLength : 5
		}, oSettings || {});
		this.oSettings.bNormalDisplay = !!this.oSettings.bNormalDisplay;
		if ((typeof this.oSettings.iDisplayLength != "number" || this.oSettings.iDisplayLength <= 0)) {
			this.oSettings.iDisplayLength = 0;
		}

		var oSettings = this.oSettings;
		var displayModel = oSettings.bNormalDisplay ? {} : {
			showErrors : function(errorMap, errorList) {
				var validator = this;
				var bNormalDisplay = oSettings.bNormalDisplay;
				var iDisplayLength = oSettings.iDisplayLength;

				for ( var i = 0; i < errorList.length; i++) {
					var error = errorList[i];
					var element = error.element;
					var message = error.message;
					if (validator.settings.highlight) {
						validator.settings.highlight.call(validator, error.element, validator.settings.errorClass,
								validator.settings.validClass);
					}

					if (_isInited(element)) {
						_cleanTooltip(element);
					}

					var msg;
					if (bNormalDisplay == false && iDisplayLength < message.length) {
						msg = message.substr(0, iDisplayLength) + "..";
						if (!_isInited(element)) {
							_initTooltip(element, message);
						} else {
							_changeTooltipMsg(element, message);
						}
					} else {
						msg = message;
					}
					validator.showLabel(error.element, msg);
				}

				if (validator.errorList.length) {
					validator.toShow = validator.toShow.add(validator.containers);
				}

				if (this.settings.success) {
					for ( var i = 0; this.successList[i]; i++) {
						this.showLabel(this.successList[i]);
					}
				}
				if (this.settings.unhighlight) {
					for ( var i = 0, elements = this.validElements(); elements[i]; i++) {
						this.settings.unhighlight.call(this, elements[i], this.settings.errorClass,
								this.settings.validClass);
					}
				}
				this.toHide = this.toHide.not(this.toShow);
				this.hideErrors();
				this.addWrapper(this.toShow).show();

				for ( var i = 0; i < validator.successList.length; i++) {
					var element = validator.successList[i];
					if (_isInited(element)) {
						_cleanTooltip(element);
					}
				}
				this.toHide.each(function() {
					var name = $(this).attr("for");
					$("[name=" + name + "]").each(function() {
						if (_isInited(this)) {
							_cleanTooltip(this);
						}
					});
				});
			}
		};
		this.nForm = nForm;

		$.data(this.nForm, 'validator', null);
		this.validator = $(nForm).validate($.extend({}, displayModel));

		$(this.nForm).data("PMSValidator", this);
	};

	function _delegateNode(nNode) {
		return $(nNode).filter("input").length > 0 ? nNode.parentNode : nNode;
	}
	function _isInited(nNode) {
		nNode = _delegateNode(nNode);
		return nNode._initTooltip == undefined ? false : true;
	}
	function _initTooltip(nNode, message) {
		nNode = _delegateNode(nNode);
		$(nNode).attr("title", message);
		$(nNode).tooltip({
			track : true
		});
		nNode._initTooltip = true;
	}
	function _changeTooltipMsg(nNode, message) {
		nNode = _delegateNode(nNode);
		nNode.tooltipText = message;
	}
	function _cleanTooltip(nNode) {
		$("#tooltip").hide();
		// $(element).unbind('mouseover');
		nNode = _delegateNode(nNode);
		_changeTooltipMsg(nNode, "");
	}

	/**
	 * 日期比较验证
	 */
	PMSValidator.prototype.addDateCompareRule = function(options) {
		var opt = $.extend({
			msg : "开始时间应小于结束时间"
		}, options);
		if (opt.dateFrom != null && opt.dateTo != null) {
			if ($.validator._dateCompareIndex == undefined) {
				$.validator._dateCompareIndex = 0;
			}
			var rule = "dateCompare" + $.validator._dateCompareIndex++;
			$.validator.addMethod(rule, function(value, element) {
				var rt = true;
				if ($(opt.dateTo).val() != null && $(opt.dateTo).val().length > 0) {
					rt = $(opt.dateFrom).val() <= $(opt.dateTo).val();
				}
				return rt;
			}, opt.msg);

			var validateRule = {};
			validateRule[rule] = true;
			$.addValidate($(opt.dateTo), validateRule);
			// _appendValidateRule(opt.dateTo, rule);
		}
	};

	$.validator.prototype.showLabel = function(element, message) {
		var label = this.errorsFor( element );
		if ( label.length ) {
			// refresh error/success class
			label.removeClass().addClass( this.settings.errorClass );
		
			// check if we have a generated label, replace the message then
			label.attr("generated") && label.html(message);
		} else {
			// create label
			label = $("<" + this.settings.errorElement + "/>")
				.attr({"for":  this.idOrName(element), generated: true})
				.addClass(this.settings.errorClass)
				.html(message || "");
			if ( this.settings.wrapper ) {
				// make sure the element is visible, even in IE
				// actually showing the wrapped element is handled elsewhere
				label = label.hide().show().wrap("<" + this.settings.wrapper + "/>").parent();
			}
			var sibElement = ($(element).nextAll());
			var sibElementTemp = element;
			
			if(sibElement.length>0){
				sibElementTemp = sibElement[sibElement.length-1];
			}
			if ( !this.labelContainer.append(label).length )
				this.settings.errorPlacement
					? this.settings.errorPlacement(label, $(element) )
					: label.insertAfter(sibElementTemp);
			
			
		}
		if ( !message && this.settings.success ) {
			label.text("");
			typeof this.settings.success == "string"
				? label.addClass( this.settings.success )
				: this.settings.success( label );
		}
		this.toShow = this.toShow.add(label);
	};
	$.validator.prototype.form = function(_cfg) {
		this.checkForm(_cfg);
		$.extend(this.submitted, this.errorMap);
		this.invalid = $.extend({}, this.errorMap);
		if (!this.valid())
			$(this.currentForm).triggerHandler("invalid-form", [this]);
		this.showErrors();
		return this.valid();
	};
	$.validator.prototype.checkForm = function(_cfg) {
		this.prepareForm();
		for ( var i = 0, elements = (this.currentElements = this.elements()); elements[i]; i++ ) {
			this.check( elements[i],_cfg);
		}
		return this.valid(); 
	};
	$.validator.prototype.element = function( element,_cfg) {
		element = this.clean( element );
		this.lastElement = element;
		this.prepareElement( element );
		this.currentElements = $(element);
		var result = this.check( element,_cfg);
		if ( result ) {
			delete this.invalid[element.name];
		} else {
			this.invalid[element.name] = true;
		}
		if ( !this.numberOfInvalids() ) {
			// Hide error containers on last error
			this.toHide = this.toHide.add( this.containers );
		}
		this.showErrors();
		return result;
	};
	$.validator.prototype.check = function( element,_cfg) {
		element = this.clean( element );
		
		if (this.checkable(element)) {
			
			element = this.findByName( element.name )[0];
		}
		
		var rules = $(element).rules();
		var dependencyMismatch = false;
		for( method in rules ) {
			if(_cfg!=null && _cfg.uncheck!=null){
				if((_cfg.uncheck).indexOf(method)!=-1){
					continue;	
				}	
			}
			var rule = { method: method, parameters: rules[method] };
			if(method=="number"&&element.value==""){
				continue;
			}
			if(method=="digits"&&element.value==""){
				continue;
			}
			if(method=="maxlength"&&element.value==""){
				continue;
			}
			if(method=="max"&&element.value==""){
				continue;
			}
			if(method=="number"&&element.value==""){
				continue;
			}
			if(method=="range"&&element.value==""){
				continue;
			}
			try {
				var result = $.validator.methods[method].call( this, element.value.replace(/\r/g, ""), element, rule.parameters );
				
				
				if ( result == "dependency-mismatch" ) {
					dependencyMismatch = true;
					continue;
				}
				dependencyMismatch = false;
				
				if ( result == "pending" ) {
					this.toHide = this.toHide.not( this.errorsFor(element) );
					return;
				}
				
				if( !result ) {
					this.formatAndAdd( element, rule );
					return false;
				}
			} catch(e) {
				this.settings.debug && window.console && console.log("exception occured when checking element " + element.id
					 + ", check the '" + rule.method + "' method", e);
				throw e;
			}
		}
		
		if (dependencyMismatch)
			return;
		if ( this.objectLength(rules) )
			this.successList.push(element);
		return true;
	};
	
	
	// function _appendValidateRule(nNode, sRule){
	// if($(nNode).attr("validate") == undefined){
	// $(nNode).attr("validate", sRule+":true");
	// }else if($(nNode).attr("validate").indexOf(sRule) == -1){
	// $(nNode).attr("validate", $(nNode).attr("validate")+", "+sRule+":true");
	// }
	// }

	/**
	 * init
	 */
	// PMSValidator.prototype.init = function(){
	// };
	/**
	 * 清空表单校验的错误信息
	 */
	PMSValidator.prototype.clean = function() {
		$(this.clientErrSel).hide();
		var first = $(this.nForm).find(":first");
		if (first.attr("_err_") == "true") {
			first.remove();
		}

		if (this.oSettings.bNormalDisplay == false && this.oSettings.iDisplayLength > 0) {
			$(this.nForm).find("*[validate], *[revalidate]").each(function() {
				$(this).attr("title", "");
				$(this).tooltip();
			});
		}
	};
	
	
	
	/**
	 * 客户端校验
	 */
	PMSValidator.prototype.clientValidate = function() {
		var passed = this.validator.form();
		return passed;
	};

	PMSValidator.prototype.clientSaveValidate = function(){
		var passed = this.validator.form({uncheck:"required"});
		return passed;
	};
	
	/**
	 * 客户端重新校验，对revalidate="true"的元素
	 */
	PMSValidator.prototype.clientRevalidate = function() {
		var passed = true;
		var validator = this.validator;
		$(this.nForm).find("[revalidate]").each(function() {
			var elementPassed = true;
			if ($(this).attr("revalidate") == "true") {
				elementPassed = validator.element(this);
			}
			if (elementPassed != true) {
				passed = false;
			}
		});
		return passed;
	};
	
	PMSValidator.prototype.clientSaveRevalidate = function(){
		var passed = true;
		var validator = this.validator;
		$(this.nForm).find("[revalidate]").each(function(){
			var elementPassed = true;
			if($(this).attr("revalidate") == "true"){
				 elementPassed = validator.element(this,{uncheck:"required"});
			}
		
			if(elementPassed != true){
				passed = false;
			}
		});
		return passed;
	};
	
	/**
	 * 服务端校验不通过，返回true
	 */
	PMSValidator.prototype.isServerInvalid = function(message) {
		if (message == null) {
			return false;
		}
		return message.indexOf(this.MESSAGE_PREFIX) == 0;
	};
	/**
	 * 将服务端错误信息显示在form的上方
	 */
	PMSValidator.prototype.showServerInvalidError = function(errorMsg) {
		var first = $(this.nForm).find(":first");
		var msg = errorMsg.substr(this.MESSAGE_PREFIX.length);
		var newDiv = $("<label _err_='true' >" + msg + "</label>").addClass("error");// .css("color", "red");//
		// .addClass("ui-state-highlight");//.insertBefore(first);
		newDiv.insertBefore(first);
	};

	/**
	 * 特殊显示错误模式
	 */
	PMSValidator.prototype.showLabel = function(element, message) {
		var oSettings = $(this.currentForm).data("PMSValidator").oSettings;
		var bNormalDisplay = oSettings.bNormalDisplay;
		var iDisplayLength = oSettings.iDisplayLength;
		if (!bNormalDisplay) {
			var allMessage = message;
			message = allMessage.substr(0, iDisplayLength);
			var sOtherHalfMessage = allMessage.substr(iDisplayLength);

			if ($("#_tipTable").length <= 0) {
				$('body')
						.append(
								$(
										'<table id="_tipTable" class="tableTip" style="z-index:10">'
												+ '<tr><td class="leftImage"></td><td class="contenImage" align="left"></td><td class="rightImage"></td></tr></table>')
										.addClass("error").hide());
			}
			// $(element).addClass('tooltipinputerr');
			$(element).hover(function(event) {
				$('.contenImage').html(sOtherHalfMessage);
				var position = $(this).position();
				$('#_tipTable').css({
					position : "absolute",
					left : position.left,
					top : position.top,
					zIndex : 1010
				}).show();
				for ( var index in this.successList) {
					if (element == this.successList[index]) {
						$('#_tipTable').hide();
						break;
					}
				}
			}, function(event) {
				var position = $('#_tipTable').offset();
				var width = $('#_tipTable').width();
				var height = $('#_tipTable').height();
				var minX = position.left;
				var maxX = position.left + width;
				var minY = position.top - height;
				var maxY = position.top;
				var x = event.pageX;
				var y = event.pageY - 20;
				if (x < minX || x > maxX || y < minY || y > maxY) {
					$('#_tipTable').hide();
				}
			});
		}

		var label = this.errorsFor(element);
		if (label.length) {
			// refresh error/success class
			label.removeClass().addClass(this.settings.errorClass);

			// check if we have a generated label, replace the message then
			label.attr("generated") && label.html(message);
		} else {
			// create label
			label = $("<" + this.settings.errorElement + "/>").attr({
				"for" : this.idOrName(element),
				generated : true
			}).addClass(this.settings.errorClass).html(message || "");
			if (this.settings.wrapper) {
				// make sure the element is visible, even in IE
				// actually showing the wrapped element is handled elsewhere
				label = label.hide().show().wrap("<" + this.settings.wrapper + "/>").parent();
			}
			if (!this.labelContainer.append(label).length)
				this.settings.errorPlacement ? this.settings.errorPlacement(label, $(element)) : label
						.insertAfter(element);
		}
		if (!message && this.settings.success) {
			label.text("");
			typeof this.settings.success == "string" ? label.addClass(this.settings.success) : this.settings
					.success(label);
		}
		this.toShow = this.toShow.add(label);
	};

	/**
	 * 可以用来清除对元素的校验设置
	 */
	$.extend({
		/**
		 * Add new validation rules to the given element
		 * 
		 * @param element -
		 *            Validate target element, e.g. input
		 * @param rule -
		 *            the object contains rules should be added,e.g. {requried:true}
		 */
		addValidate : function(element, rules) {
			$.extend($.metadata.get($(element)[0]), rules);
		},
		/**
		 * Set new validation rules to the given element
		 * 
		 * @param element -
		 *            Validate target element, e.g. input
		 * @param rule -
		 *            the object contains rules should be set
		 */
		setValidate : function(element, rules) {
			$.data($(element)[0], "metadata", rules);
		},
		/**
		 * Deprecated methods
		 */
		setValidateMetadata : function(element, sAttr) {
			if (sAttr.indexOf('{') < 0) {
				sAttr = "{" + sAttr + "}";
			}
			var oAttr = eval("(" + sAttr + ")");
			$.data($(element)[0], "metadata", oAttr);
		}
	});

	$(function() {
		$.metadata.setType("attr", "validate");
	});

	//From pmsapp, to integrate with PMSForm
//	$.extend($.fn, {
//		saveValid:function(_cfg){
//			return this.bidValid(_cfg,"save");
//		},
//		valid:function(_cfg){
//			return this.bidValid(_cfg,"commit");
//		},
//		bidValid:function(_cfg,type){
//			if(type==null){
//				type = "commit";	
//			}
//			var validateCfg = {
//				bNormalDisplay : true,
//				iDisplayLength : 2	
//			};
//			if(_cfg==null){
//				_cfg = {};	
//			}
//			$.extend(validateCfg,_cfg);
//			$validator = new PMSValidator($("#"+$(this).attr("id"))[0],validateCfg);
//			$validator.clean();
//			var flag = true;
//			if(type=="commit"){
//				if(!$validator.clientValidate()){
//					flag = false;	
//				}
//				if(!$validator.clientRevalidate()){
//					flag = false;	
//				}
//			}else if(type=="save"){
//				if(!$validator.clientSaveValidate()){
//					flag = false;	
//				}
//				if(!$validator.clientSaveRevalidate()){
//					flag = false;	
//				}
//			}
//			
//			return flag;
//			
//		}
//	});
})(jQuery);