(function($){
	
$(function(){
    $("form button[type=reset]").click(function(event){
        new MessageDialog("提示", "清除所有输入的信息？", null, "确定", "取消", function(){
            event.target.form.reset();
        }, null).open();
        return false;
    });
});

})(jQuery);