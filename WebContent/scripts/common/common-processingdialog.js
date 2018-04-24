/**
 * $ProcessingDialog, $PD提供display(sURL)方法， 该方法在ajax提交后，显示“正在处理中，请稍候”的模态div，
 * 如果有参数，则提交成功后当前url为sURL。
 * 
 */
var $ProcessingDialog, $PD;

(function($) {
	var ProcessingDialog = function() {
	};

	ProcessingDialog.prototype.display = function(sURL) {
		var div = $('<div></div>');
		var dialog = div.dialog({
			modal : true,
			autoOpen : false,
			title : "正在处理中，请稍候...",
			minHeight : 0,
			minWidth : 0,
			width : 200,
			height : 0
		});

		var bNoHandle = false;
		div.ajaxSend(function() {
			if (!$.handleError) {
				bNoHandle = true;
				$.handleError = function() {
				};
			}
			dialog.dialog("open");
			div.removeClass().parent().find(":first").removeClass();
		});

		div.ajaxComplete(function(oInfo, xhr) {
			if (bNoHandle) {
				$.handleError = null;
				delete $.handleError;
			}
			dialog.dialog("close");
			div.remove();

			if (xhr.statusText == "success") {
				if (sURL) {
					window.location.href = sURL;
				}
			}
		});

	};

	$ProcessingDialog = $PD = new ProcessingDialog();

})(jQuery);
