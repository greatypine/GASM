// JavaScript Document
/*!
 * Author:xuanliwei@163.com
 * 2010.5.20
 */
 /*
$(function(){
    $("#panle").find("dd").each(function(){
        var height_dt = $("#panle").find("dt").height();
        var dd_body = $("body").height() - height_dt * $("#panle").find("dt").size();
        $(this).css("height", dd_body);
    });
});
*/
$(function(){
    $('#panle').find('dt').click(function(){
        $('#panle').find('dt').removeClass("select_panle");//以后是否考虑子递归和无限节点
       $(this).next().siblings("dd").hide("fast");
        $(this).addClass("select_panle");
        $(this).next().show();
    });
    //$("#panle dd:first").css("display","block");//初始化第一个展开

});
$(function(){
    $('#panle').find('h1').click(function(){
        var tree_list = $(this).next();
        if (tree_list.is(':visible')) {
            tree_list.slideUp();
            $(this).removeClass();
        }
        else {
            tree_list.slideDown();
            $(this).addClass("open");
        }
        
    });
});
$(function(){
    $('#panle').find('a').click(function(){
        $('#panle').find('a').removeClass("hover");
        $(this).addClass("hover");
    });
});
