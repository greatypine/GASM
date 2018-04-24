
/*
* @methodName   : doLoadFormFromJson
* @methodParam1  : String （form表单的名字）
* @methodParam2  : String （JSON字符串）
* @methodReturn : boolean
* @author       : zhulei
* @desc         : 加载form数据
* @for example3  : doLoadFormFromJson(""formName1",{key1:'11',key2:'22'}");（用JSON字符串对某个form赋值)
*/
function doLoadFormFromJson(formId,jsonData){
	if(jsonData==null||jsonData==""){
		jsonData = {};	
	}
	 for(var attr in jsonData){ 
         
         if(typeof(jsonData[attr])=='function'){                     
           continue; 
         } 
         var $input = $("#"+formId+"    :input[name='"+attr+"']"); 
         var type = $input.attr("type");                
         if(type=="checkbox" ||type=="radio"){ 
               
             var avalues = jsonData[attr].split(","); 
               
             for(var v=0; v<avalues.length;v++){ 
               $input.each(function(i,n){ 
                   var value = $(n).val();                         
                   if(value == avalues[v]){                        
                     $(n).attr("checked","checked"); 
                   } 
               }); 
           } 
         }else{ 
           $input.val(jsonData[attr]); 
         } 
           
       }   
 }