// JavaScript Document ie6 最小宽度

$(window).resize(function(){
	
  if ( $.browser.version==6){
		  var bodycontheight=document.documentElement.clientWidth; 
	 	 var bodycontheight1=document.documentElement.clientWidth-26; 
	 	 if(bodycontheight<=800){
		 $(".box").css("width","800px");
		  }
		else{		
			 $(".box").css("width",bodycontheight1);	
	
			}  
	  }
});