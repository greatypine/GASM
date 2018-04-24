/**
 * jsonData can also be set globally by setting a data object to $.acl.data.  This will
 * be used as data for every subsequent call to any function.  Any global data will
 * be overridden by call data with the same property name, of course.  This global data
 * will also be used for calls to $.acl.fillUrl(url) that have no data argument.
 * @copyright(c) 2011 IBM, http://www.ibm.com
 * @name acl
 * @version 1.0.0
 * @cat jquery/acl
 * @author zhangming03@cnpc.com.cn
 */
if(typeof jQuery == "undefined"){
    throw "Unable to load acl plugin, jQuery not found.";
}
//hadn't complete
var config=(config!=undefined?config:{});

var pmsappACLData = null;

(function($) {
	//core suport
	$.extend({
		version:"1.0.0-snapshot",
		keys:{
			
		},
		initACL: function(data) {
			var curOpArr = new Array();
			pmsappACLData = data;
			$.each(data, function(i, n){
					if(n.resourceId!=null){
						if (n.disabled=='true') {
							$("[resourceId='"+n.resourceId+"']").attr("disabled",n.disabled);
						}
					} 
			});
			if($pmspage.opArr!=null){
				$.each($pmspage.opArr, function(i, m){
				var flag = false; //判断是否应该disabled
				$.each(data, function(i, n){
					if($("[id*=gridContainer]").length>0||$("[id*=GridContainer]").length>0) {
						if(m.resourceId == n.resourceId) {
								flag = true;
							}
						}
					});	
					if(flag == false){
						curOpArr.push(m);
					}				
				});
			if(data.length>0){
				$pmspage.opArr  = curOpArr;
			}
			}
			
		}

	});
	//plugins
	$.fn.extend({
		fillUrl: function(url) {
			return new function() { this.urlChecked = true; };
		}
	});
	
})(jQuery);

/**
 * Page Initialize Handler.
 * @copyright(c) 2011 IBM, http://www.ibm.com
 * @author He fei
 * @since 2011/05/17
 */
var PMSPage = function() {
	this.basePath = "";
	this.opArr = null;
}

PMSPage.prototype.setBasePath = function(){
	var href = window.location.pathname;
	href = href.substr(0, href.indexOf("/", 1));
	this.basePath = href + "/";
};

PMSPage.prototype.initialize = function() {
	this.setBasePath();
	var data = new PMSRequestObject("functionManager", "getStatus", "");
	var callback = function(data, textStatus, XMLHttpRequest) {
		var returnObj = $.fromJSON(data.data);
		$.initACL(returnObj);
	};
	
	$.ajax({
		url : this.basePath + "dispatcher.action",
		dataType : "json",
		type : "post",
		async : false,
		data : "requestString=" + data.toJsonString(),
		timeout : $$.requestTimeout,
		success : function(data, textStatus, XMLHttpRequest) {
			if (data.result) {
				callback(data, textStatus, XMLHttpRequest);
			} else {
				$$.onServerInvokeFailed(XMLHttpRequest, data.message)
			}
		},
		error : $$.onServerInvokeFailed
	});	
};

var $pmspage = pmspage = new PMSPage();