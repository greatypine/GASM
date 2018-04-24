/*
* @Object       : $.validator
* @author       : ruanqingfeng
* @desc1        : 扩展验证框架，让错误提示信息显示在控件所在容器的最后
* @desc2        : 扩展验证框架，支持暂存方法忽略required验证
*/
if($!=null&&$.validator!=null){
	
	$.extend($.fn, {
		/*saveValid: function() {
			if ( $(this[0]).is('form')) {
				return this.validate().form({uncheck:"required"});
			} else {
				var valid = true;
				var validator = $(this[0].form).validate();
				this.each(function() {
					valid &= validator.element(this);
				});
				return valid;
			}
		},*/
		saveValid:function(_cfg){
			return this.bidValid(_cfg,"save");
		},
		valid:function(_cfg){
			return this.bidValid(_cfg,"commit");
		},
		bidValid:function(_cfg,type){
			if(type==null){
				type = "commit";	
			}
			var validateCfg = {
				bNormalDisplay : true,
				iDisplayLength : 2	
			}
			if(_cfg==null){
				_cfg = {};	
			}
			$.extend(validateCfg,_cfg);
			$validator = new PMSValidator(
			$("#"+$(this).attr("id"))[0],validateCfg);
			$validator.clean();
			var flag = true;
			if(type=="commit"){
				if(!$validator.clientValidate()){
					flag = false;	
				}
				if(!$validator.clientRevalidate()){
					flag = false;	
				}
			}else if(type=="save"){
				if(!$validator.clientSaveValidate()){
					flag = false;	
				}
				if(!$validator.clientSaveRevalidate()){
					flag = false;	
				}
			}
			
			return flag;
			
		}
	});
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
		if('undefined' != typeof showLabelCallBack) {
			showLabelCallBack(label, element)
		}
	};
	$.validator.prototype.form = function(_cfg) {
		this.checkForm(_cfg);
		$.extend(this.submitted, this.errorMap);
		this.invalid = $.extend({}, this.errorMap);
		if (!this.valid())
			$(this.currentForm).triggerHandler("invalid-form", [this]);
		this.showErrors();
		return this.valid();
	}
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
	}
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
	}
	
	/**
	 * 客户端暂存校验
	 */
	PMSValidator.prototype.clientSaveValidate = function(){
		var passed = this.validator.form({uncheck:"required"});
	//	.validate(
	//	{
	//		errorClass: "error"
	////   		,errorElement: "em"  
	//	}
	//	).form();
		return passed;
	}
	
	/**
	 * 客户端重新校验，对revalidate="true"的元素
	 */
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
	}
}else{
	alert("validateExtend error! because of you not loading 'jquery.validate.js' file !");	
}

