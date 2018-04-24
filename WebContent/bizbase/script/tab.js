// JavaScript Document tab切换
function get_firstchild(n)//或得子元素
{
	var x=n.firstChild;
	while (x.nodeType!=1)
	{
	x=x.nextSibling;
	}
	return x;
}

function nav_set(obj,tag_N){		
	var nav_list= document.getElementById("tabdocument").getElementsByTagName("li");
	var nav_cont=document.getElementById("nav_cont").getElementsByTagName("h1");
	
	for(var i=0;i<nav_cont.length;i++){
		nav_list[i].className="";	
		//alert(get_firstchild(nav_cont[i]).nodeName)
		get_firstchild(nav_cont[i]).className="displaynone";
	}		
	obj.className="select";
	document.getElementById(tag_N).className="displayblock";
}

function nextbtn(num,tag_N){
	var nav_list= document.getElementById("tabdocument").getElementsByTagName("li");
	var nav_cont=document.getElementById("nav_cont").getElementsByTagName("h1");
	
	//alert(nav_cont.length)
	for(var i=0;i<nav_cont.length;i++){
		nav_list[i].className="";	
		//alert(get_firstchild(nav_cont[i]).nodeName)
		get_firstchild(nav_cont[i]).className="displaynone";
	}
			
	nav_list[num].className="select";
	document.getElementById(tag_N).className="displayblock";
}