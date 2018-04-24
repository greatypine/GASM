function RefWebList(refManagerName,refMethodName)
{
	this.refManagerName = refManagerName;
	this.refMethodName = refMethodName;
}
RefInputText.prototype.initialize = function(divRefWebList)
{
	var generateTree="<script src='../../js/lib.js' type='text/javascript'></script>"+
					 "<SCRIPT LANGUAGE='JavaScript'>var zTree1;var setting;var myData ;var zNodes = [];"
					 +"myData = $.toJSON({managerName:"+this.refManagerName+",methodName:"+this.refMethodName
					 +",parameters: ['0']});setting = { checkable: true,checkStyle:'radio',async: true,asyncParam: ['name', 'id'],"+
					 "asyncParamOther: ['requestString', myData],"
					 ;
}