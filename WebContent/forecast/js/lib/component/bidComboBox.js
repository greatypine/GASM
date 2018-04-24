/*
* @component    : BidComboBox
* @author       : ruanqingfeng
* @version      : 1.0
* @desc         : ����js���������Ҫ��ʾ�ɿ����ѡ����4�б�ؼ��м���class="bidComboBox"
* @for example  : ����JS��ҳ����д�룺<select class="bidComboBox"><option>ֵ1</option><option>ֵ2</option></select>
*/


importCss(baseUrl+"/forecast/css/jquery.comboBox.css");
    
(function( $ ) {
	$.widget( "ui.combobox", {
		_create: function() {
			
			var self = this,
				select = this.element.hide(),
				selected = select.children( ":selected" ),
				value = selected.val() ? selected.text() : "";
				var validateStr = select.attr("bidValidate");
				if(validateStr!=null&&validateStr!=""){
					this.element[0].validate = "";
					this.element.removeAttr("bidValidate");	
					validateStr = "validate='"+validateStr+"'";
				}else{
					validateStr = "";
				}
			var style = select.attr("style");
			if(style==null||style==""){
				style = "";
			}
			var disabled = select.attr("disabled");
			if(disabled==null||disabled==""){
				disabled = "";
			}else{
				disabled = "readonly='readonly'"
			}
			var width = select.attr("width");
			if(width==null||width==""){
				width = "";
			}else{
				width = "width:"+width+""
			}
			var inputObject = $("<input  type='text' "+disabled+" "+validateStr+" />");
			if(disabled!="readonly='readonly'"){
				inputObject.attr("style","background-color: #ffffff;height:18px;"+width+"");
			}else{
				inputObject.attr("style","background-color: #cccccc;height:18px;"+width+"");
			}
			
			var input = this.input = inputObject
				.insertAfter( select )
				.val( value )
				.autocomplete({
					delay: 0,
					minLength: 0,
					source: function( request, response ) {
						var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
						response( select.children( "option" ).map(function() {
							var text = $( this ).text();
							if ( this.value && ( !request.term || matcher.test(text) ) )
								return {
									label: text.replace(
										new RegExp(
											"(?![^&;]+;)(?!<[^<>]*)(" +
											$.ui.autocomplete.escapeRegex(request.term) +
											")(?![^<>]*>)(?![^&;]+;)", "gi"
										), "<strong>$1</strong>" ),
									value: text,
									option: this
								};
						}) );
					},
					select: function( event, ui ) {
					
					},
					change: function( event, ui ) {
						
					}
				})
				.addClass( "ui-widget-content" );

			input.data( "autocomplete" )._renderItem = function( ul, item ) {
				return $( "<li></li>" )
					.data( "item.autocomplete", item )
					.append( "<a>" + item.label + "</a>" )
					.appendTo( ul );
			};
			
//			if (undefined != input.data("autocomplete")) {
//				input.data("autocomplete")._renderItem = function(ul, item){
//					return $("<li></li>").data("item.autocomplete", item).append("<a>" + item.label + "</a>").appendTo(ul);
//				};
//			}
			this.button = $( "<button style='width:20px;height:20px' type='button'>&nbsp;</button>" )
				.attr( "tabIndex", -1 )
				.attr( "title", "Show All Items" )
				.insertAfter( input )
				.button({
					icons: {
						primary: "ui-icon-triangle-1-s"
					},
					text: false
				})
				.removeClass( "ui-corner-all" )
				.addClass( "ui-corner-right ui-button-icon ui-widget-top" )
				.click(function() {
					if(disabled!="readonly='readonly'"){
							// close if already visible
						if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
							input.autocomplete( "close" );
							return;
						}
	
						// pass empty string as value to search for, displaying all results
						input.autocomplete( "search", "" );
						input.focus();
					}
					
				});
		},

		destroy: function() {
			this.input.remove();
			this.button.remove();
			this.element.show();
			$.Widget.prototype.destroy.call( this );
		}
	});
})( jQuery );

$(function() {
		
	$("select[class*=bidComboBox]").each(function(i){
		$(this).combobox();
	});
});

var BidComboBox = {
	setValue:function(comboId,value){
		$("#"+comboId).next().val(value);
	},
	setOptionSelected:function(comboId,key){
		
		var select = $("#"+comboId);
		selected = select.children( ":selected" ),
		value = selected.val() ? selected.text() : "";
		$("#"+comboId).next().val(value);
	},
	getValue:function(comboId){
		return $("#"+comboId).next().val();
	},
	setDisabled:function(comboId){
		$("#"+comboId).next().attr('disabled',true);
		$("#"+comboId).next().next().attr('disabled',true);
	}
};