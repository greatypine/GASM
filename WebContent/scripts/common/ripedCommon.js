/**
 * 赵彬彬添加对象是否为空的判断,如果是数字0，js会将0当成false等同因此加了一个“”
 */
function isEmptyValue( s ){
	if("undefined"==s){
		return true;
	}
	if(typeof(s)=="undefined"){
		return true;
	}
	s=s+"";
  	return s == null || typeof( s ) == 'string' && (s == ''||s=='null') || s == '&nbsp;' || typeof( s ) == 'number' && isNaN( s );
}
/**
 * 赵彬彬添加用来统一所有返回按钮摆放问题
 */
$(function(){
	$(".ico2").each(function(){
		if('undefined' != typeof pmsDetailView) {
			$(this).append('<button class="buttonu" style="height:26px;text-align:right" resourceId="general_search_detail" id="DetailView" onclick="pmsDetailView()">综合查询</button> |');
		}
		if ('undefined' != typeof pmsTaskView) {
			$(this).append('<button class="buttonu" style="height:26px;text-align:right" id="TaskView" onclick="pmsTaskView()">查看任务</button> |');
		}
		$(this).append('<button class="buttonu" style="height:26px;text-align:right" id="Back" onclick="goToBack()">返回</button>');
	});
});
/**
 * 检查所有以pmsGoBack命名的js进行主动调用
 */
 function goToBack(){
		if ('undefined' != typeof pmsGoBack) {
		pmsGoBack();
	}else{
		history.go(-1);
	}
}
