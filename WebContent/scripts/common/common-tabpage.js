/*
 * 注：更新后的功能（注释的代码块，用JQuery原生的标签页禁用功能），对现有程序影响较大，暂时回复原版本。
 */
/*
$(function(){
    $("div.tabs").each(function(index){
		var cache = $(this).attr('cache') === 'false' ? false : true;
		var disableindex=[];
		var lilist = $(this).find("ul").first().children("li");
		
		$(lilist).each(function(inner_index) {
			if($(this).attr("disabled")==='' || $(this).attr("disabled") ==='true' || $(this).attr("disabled") ==true) {
				disableindex.push(inner_index);
			}
		})

        $(this).tabs({
			cache: cache,
            ajaxOptions: {
                error: function(xhr, status, index, anchor){
                    $(anchor.hash).html("Couldn't load this tab.");
                }
            },
			disabled:disableindex
        });
    });
});
*/
$(function(){
    $("div.tabs").each(function(index){
		var cache = $(this).attr('cache') === 'false' ? false : true;
        $(this).tabs({
			cache: cache,
            ajaxOptions: {
                error: function(xhr, status, index, anchor){
                    $(anchor.hash).html("Couldn't load this tab.");
                }
            }
        });
    });
});
