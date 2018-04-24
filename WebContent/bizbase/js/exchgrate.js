function doAddExchgRate(){
    if (validateCurrency() && validateRate() && validateStartTime()) {
        var dto = {
            fromCurrency: $('#fromCurrency').val(),
            toCurrency: $('#toCurrency').val(),
            fromRate: $('#fromRate').val(),
            startTime: $('#startTime').val()
        };
        $("#submitExchgRateButton").attr("disabled", "disabled");
        doManager("exchangeRateManager", "addExchangeRate", dto, function(data, textStatus, XMLHttpRequest){
                if (data.result) {
                    window.location = "exchgrate_management.html";
                }else{
					bid.alert(data.message);
				}
            }, false, {
                showWaiting: true
         });
		 $("#submitExchgRateButton").attr("disabled", "");
    }
}

function validateCurrency(){
    clearError("fromCurrency");
    clearError("toCurrency");
    var from = $("#fromCurrency").val();
    if (from == '') {
        showError('fromCurrency', '请选择币种(从)');
        return false;
    }
    var to = $("#toCurrency").val();
    if (to == '') {
        showError('toCurrency', '请选择币种(到)');
        return false;
    }
    if (from == to) {
        showError('toCurrency', '币种(从)不能与币种(到)相同');
        return false;
    }
    return true;
}

function validateRate(){
    var fieldId = "fromRate";
    var rateFrom = $("#" + fieldId).val();
    clearError(fieldId);
    var info = '';
    var regu = /^(\d+)(\.?)(\d{5,5})$/;
    var re = new RegExp(regu);
    v = parseFloat(rateFrom);
    if (!re.test(rateFrom) || v == 0) {
        info = '请输入大于0的数字(精确到小数点后5位)';
        showError(fieldId, info);
        return false;
    }
    return true;
}

function validateStartTime(){
    clearError("startTime");
    var startTime = $("#startTime").val();
    if (startTime == '') {
        showErrorIn("startTime", "有效期开始不能为空");
        return false;
    }
    var now = new Date().format("yyyy-MM-dd hh:mm:ss");
    //now = now.substring(0, 18) + "00";
    var flag = comptime(startTime, now);
    if (flag == 1) {
        showErrorIn("startTime", "有效期开始必须晚于当前时间");
        return false;
    }
    return true;
}

function clearError(forId){
    var errorObj = $('label[class="error"][for="' + forId + '"]');
    errorObj.remove();
}

function showError(forId, info){
    var ele = $('#' + forId);
    var errorInfo = "<label class='error' for='" + forId + "'>" + info + "</label>";
    ele.after(errorInfo);
}

function showErrorIn(forId, info){
    var container = $("#" + forId).parent();
    var errorInfo = "<label class='error' for='" + forId + "'>" + info + "</label>";
    container.append(errorInfo);
}

/**
 * 时间对象的格式化;
 * eg:format="YYYY-MM-dd hh:mm:ss";
 * @param {Object} format
 */
Date.prototype.format = function(format){
    var o = {
        "M+": this.getMonth() + 1, // month
        "d+": this.getDate(), // day
        "h+": this.getHours(), // hour
        "m+": this.getMinutes(), // minute
        "s+": this.getSeconds(), // second
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
        "S": this.getMilliseconds()
        // millisecond
    }
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

/**
 * 比较时间，时间格式"yyyy-MM-dd HH:mm:ss"
 * @param {Object} beginTime
 * @param {Object} endTime
 */
function comptime(beginTime, endTime){
    var beginTimes = beginTime.substring(0, 10).split('-');
    var endTimes = endTime.substring(0, 10).split('-');
    
    beginTime = beginTimes[1] + '-' + beginTimes[2] + '-' + beginTimes[0] + ' ' + beginTime.substring(10, 19);
    endTime = endTimes[1] + '-' + endTimes[2] + '-' + endTimes[0] + ' ' + endTime.substring(10, 19);
    
    var a = (Date.parse(endTime) - Date.parse(beginTime)) / 3600 / 1000;
    
    if (a < 0) {
        return -1;
    }
    else 
        if (a > 0) {
            return 1;
        }
        else 
            if (a == 0) {
                return 0;
            }
            else {
                return 'exception'
            }
}

$.fn.numeral = function(){
    $(this).css("ime-mode", "disabled");
    this.bind("keypress", function(){
        if (event.keyCode == 46) {
            if (this.value.indexOf(".") != -1) {
                return false;
            }
        }
        else {
            return event.keyCode >= 46 && event.keyCode <= 57;
        }
    });
    this.bind("blur", function(){
        if (this.value.lastIndexOf(".") == (this.value.length - 1)) {
            this.value = this.value.substr(0, this.value.length - 1);
        }
        else 
            if (isNaN(this.value)) {
                this.value = "";
            }
    });
    this.bind("paste", function(){
        var s = clipboardData.getData('text');
        if (!/\D/.test(s)) 
            ;
        value = s.replace(/^0*/, '');
        return false;
    });
    this.bind("dragenter", function(){
        return false;
    });
    this.bind("keyup", function(){
        if (/(^0+)/.test(this.value)) {
            this.value = this.value.replace(/^0*/, '');
        }
    });
};
