// JavaScript Document弹窗

//creat by superleng 2010.12.9 V1
var pageWidth,pageHeight,dialogContent,myContent;
$(function(){
pageWidth=$("body").width()+document.documentElement.scrollTop;
pageHeight=$("body").height()+document.documentElement.scrollTop; 
//myContent=$("#dialogURL").html();

//dialogContent=myContent.split("|");
creatLayer();
})

function creatLayer(){
//创建遮挡背景层和窗口层，添加iframe层遮挡select控件（IE6BUG）
$("body").append('<div id="blackBg"><iframe style="opacity:0;filter:alpha(opacity=0);"></iframe></div>');
$("#blackBg").attr("style",'display:none; position:absolute; left:0px; top:0px;z-index:10; background:#000;width:'+pageWidth+'px;height:'+pageHeight+'px;opacity:0.3;filter:alpha(opacity=30);');
}


//通用显示Dialog层函数
function showDialog(showIndex){
	var _content=dialogContent[showIndex];
	//var _width=$("#dialogURL").children().eq(showIndex).width();
	//var _height=$("#dialogURL").children().eq(showIndex).height();
	$("#blackBg").show("slow");
	//$("#dialogBox").html(_content);
	//var X=(pageWidth-_width)/2;

	var Y=(document.documentElement.clientHeight-_height)/2+document.documentElement.scrollTop;
	//$("#dialogBox").css("left",X+"px");
	//$("#dialogBox").css("top",Y+"px");
	//$("#dialogURL").empty();//清空dialogURL容器，防止重ID
}

function closeDialog(obj){
	$("#blackBg").fadeOut("slow");
	$(obj).fadeOut("slow");
	//$("#dialogBox").html("");
	//$("#dialogURL").html(myContent);//还原dialogURL容器内容
	}
$(function(){
	$("#diqu,#zhuanye").click( function () { //绑定多个弹窗		
		var tanchuang='#'+$(this).attr("id")+'1';
		//alert(tanchuang)
		$(tanchuang).fadeIn(); 
		$("#blackBg").fadeIn();
		 var X1=$(tanchuang).width()/2;
		 var Y1=$(tanchuang).height()/2;
		 var X=(document.documentElement.clientWidth)/2-X1;
		 var Y=(document.documentElement.clientHeight)/2+document.documentElement.scrollTop-Y1;
		$(tanchuang).css("position","absolute")
		$(tanchuang).css("z-index","11")
		$(tanchuang).css("top",Y+"px");
		$(tanchuang).css("right",X+"px");
	});


})