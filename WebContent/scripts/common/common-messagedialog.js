var $msg = {};

(function(){

var defaultOpt = {
	title: "系统提示",
    width: 320,
	height: 220,
    modal: true,
    autoOpen: false
};
var alertButtons = {
	buttons: {
		"确定": function(){
            $(this).dialog("close");
            if (typeof this.callback == "function") 
                this.callback();
            $(this).remove();
        }
	}
};
var confirmButtons = {
	buttons: {
		"确定": function(){
			$(this).dialog("close");
			if (typeof this.callbackOk == "function") 
				this.callbackOk();
			$(this).remove();
		},
		"取消": function(){
			$(this).dialog("close");
			if (typeof this.callbackCancel == "function") 
				this.callbackCancel();
			$(this).remove();
		}
	}
};

$msg.alert = function(msg, callback){
    var div = $("<div></div>").html(msg);
	div[0].callback = callback;
	var opt = $.extend({}, defaultOpt, alertButtons);
    return div.dialog(opt).dialog("open");
};

$msg.alertAt = function(msg, position, callback){
    var div = $("<div></div>").html(msg);
	div[0].callback = callback;
	var opt = $.extend({}, defaultOpt, alertButtons);
    return div.dialog(opt)
		.dialog('option', 'position', position || 'center')
		.dialog("open");
};

$msg.confirm = function(msg, callbackOk, callbackCancel){
    var div = $("<div></div>").html(msg);
	div[0].callbackOk = callbackOk;
	div[0].callbackCancel = callbackCancel;
	var opt = $.extend({}, defaultOpt, confirmButtons);
    return div.dialog(opt).dialog("open");
};

$msg.confirmAt = function(msg, position, callbackOk, callbackCancel){
	var div = $("<div></div>").html(msg);
	div[0].callbackOk = callbackOk;
	div[0].callbackCancel = callbackCancel;
	var opt = $.extend({}, defaultOpt, confirmButtons);
    return div.dialog(opt)
		.dialog('option', 'position', position || 'center')
		.dialog("open");
};

})();

/**
 * 遗留代码。
 * @param {Object} title
 * @param {Object} text
 * @param {Object} urlOnOkButton
 * @param {Object} okText
 * @param {Object} cancelText
 * @param {Object} fnOk
 * @param {Object} fnCancel
 */
var MessageDialog = function(title, text, urlOnOkButton, okText, cancelText, fnOk, fnCancel){
    this.title = title;
    this.text = text;
    this.urlOnOkButton = urlOnOkButton;
    this.okText = okText || "ok";
    this.cancelText = cancelText || "cancel";
    this.fnOk = fnOk;
    this.fnCancel = fnCancel;
};

MessageDialog.prototype.open = function(){
    var MessageDialog = this;
    var div = $('<div></div>').html(MessageDialog.text);
    var $dialog = div.dialog({
        modal: true,
        autoOpen: false,
        title: MessageDialog.title,
        buttons: [{
            text: MessageDialog.okText,
            click: function(){
                $(this).dialog("close");
                div.remove();
                if (MessageDialog.urlOnOkButton) {
                    window.open(MessageDialog.urlOnOkButton);
                }
                if (MessageDialog.fnOk) {
                    MessageDialog.fnOk();
                }
            }
        }, {
            text: MessageDialog.cancelText,
            click: function(){
                $(this).dialog("close");
                div.remove();
                if (MessageDialog.fnCancel) {
                    MessageDialog.fnCancel();
                }
            }
        }]
    });
    $dialog.dialog("open");
};
