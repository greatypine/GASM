// JavaScript Document 层级菜单
$(function(){
	$(".diqulist").children("li").each(function(){//一级城市弹出下一级
     $(this).hover(
	  function () {
		$(this).children('ol').show();
		$(this).addClass("hover");
		$(this).css("z-index","2");
  		},
		  function () {
			$(".diqulist ol").hide();
			$(".diqulist li").removeClass("hover");
			$(this).css("z-index","1");
          }
		);
  });
	
		$(".diqulist li.xiaji").each(function(){//二级城市弹出下一级
		$(this).hover(
		  function () {
			$(this).children('ol').show();
			$(this).addClass("hover");
			$(this).css("z-index","3");
			},
			  function () {
				$(this).children('ol').hide();
				$(this).removeClass("hover");
				$(this).css("z-index","2");
			  }
			);										  
													  
													  
		 })  
});
function openSelect(state){ //选择城市层关闭打开控制
	if(state == 1)
	{
	$("#diqu").show();
	}
	else
	{
	$("#diqu").hide();
	}
}
function selectall(obj){//全选功能
	
	if($(obj).attr("checked")==false){
		$(obj).parent().parent().find("input").attr("checked","");
		$(obj).parent().parent().find("ol").css("background","#fff")
		$(obj).parent().parent().find("input").attr("disabled","");
		}
	else{
		
		$(obj).parent().parent().find("input").attr("checked","checked");
		$(obj).parent().parent().find("input").attr("disabled","disabled");
		$(obj).parent().parent().find("ol").css("background","#D8D8D8")
		$(obj).attr("disabled","");
		
		}
	}
function radioselect(obj){//单选功能
	
		   $('#wuzi').attr("checked","checked");
		//$(obj).parent().parent().parent().parent().children().eq(0).addClass("red");

	}
function checksallelect(obj,checktop){//复选框多选功能
		if($(obj).attr("checked")==false){
			$(checktop).attr("checked","");
		   $(obj).parent().parent().parent().find("input").attr("checked","");

		  }
		  else{
		  $(checktop).attr("checked","checked");
		  $(obj).parent().parent().parent().find("input").attr("checked","checked");

			  }
		

	}