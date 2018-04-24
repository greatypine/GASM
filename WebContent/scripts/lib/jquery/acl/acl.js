/**
 * jsonData can also be set globally by setting a data object to $.acl.data.  This will
 * be used as data for every subsequent call to any function.  Any global data will
 * be overridden by call data with the same property name, of course.  This global data
 * will also be used for calls to $.acl.fillUrl(url) that have no data argument.
 * @copyright(c) 2011 IBM, http://www.ibm.com
 * @name acl
 * @version 1.0.0
 * @cat jquery/acl
 */
if(typeof jQuery == "undefined"){
    throw "Unable to load acl plugin, jQuery not found.";
}
//hadn't complete
var config=(config!=undefined?config:{});

(function($) {
	//core suport
	$.extend({
		version:"1.0.0-snapshot",
		keys:{
			
		},
		initACL: function(data) {
			$.each(data, function(i, n){				
			if(n.id){
				
				if(n.disabled)$("#"+n.id).attr("disabled",n.disabled);
				if(n.display=="none")$("#"+n.id).hide();
				$('#'+n.id).bind('click', function() {
				  alert($(this).val());
				});

				//$.each(n,function(i,n){
				//});
			}
			  
			});
		}
	});
	//plugins
	$.fn.extend({
		fillUrl: function(url) {
			return new function() { this.urlChecked = true; };
		}
	});
	
	
})(jQuery);