function serverAutoComplete (element, className, properties) {
	var serverURL = "/autocomplete.action?requestString=";
	var reqObj = {
		className : className,
		keyword : element.val(),
		properties : properties.split(",")
	};
	var url = $$.baseURL + serverURL + $.toJSON(reqObj);
	element.autocomplete(url, {
		matchContains : true,
		formatItem : function(row) {
			return "<strong>" + row[0] + "</strong>";
		},
		formatResult : function(row) {
			return row[0];
		},
		minChars : 0,
		dataType : "text"
	});
};

$(document).ready(function() {
	var autoelements = $("input[clsname]");
	$.each(autoelements, function(i, element) {
		serverAutoComplete($(element), $(element).attr("clsname"), $(element).attr("propsname"));
	});
});
