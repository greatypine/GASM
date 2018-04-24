//
////获取个位是零的数据
//var getNumberOfDigitZero1 = function(m,n){
//	var ge = n - ((Math.floor(n/10))*10);//个位
////	var shi = (n - ((Math.floor(n/100))*100) - n)/10;//十位数
////	var bai = (n - ((Math.floor(n/1000))*1000)-(n*10)-n)/100//百位上的数字
//	var back_number;
//	if(n>=10&&(n-m)>=10){
//		if(ge==0){
//			back_number = n; 
//		}else if(ge==1){
//			back_number = n-1;
//		}else if(ge==2){
//			back_number = n-2;
//		}else if(ge==3){
//			back_number = n-3;
//		}else if(ge==4){
//			back_number = n-4;
//		}else if(ge==5){
//			back_number = n+10-5;
//		}else if(ge==6){
//			back_number = n+10-6;
//		}else if(ge==7){
//			back_number = n+10-7;
//		}else if(ge==8){
//			back_number = n+10-8;
//		}else if(ge==9){
//			back_number = n+10-9;
//		}
//	}else if(n>=10&&(n-m)<10&&(n-m)>0){
//		
//			back_number = n; 
//		
//	}else if(n==m&&n<10){
//		
//			back_number = n; 
//		
//	}else if(n==m&&n>10){
//		if(ge==0){
//			back_number = n; 
//		}else if(ge==1){
//			back_number = n-1;
//		}else if(ge==2){
//			back_number = n-2;
//		}else if(ge==3){
//			back_number = n-3;
//		}else if(ge==4){
//			back_number = n-4;
//		}else if(ge==5){
//			back_number = n+10-5;
//		}else if(ge==6){
//			back_number = n+10-6;
//		}else if(ge==7){
//			back_number = n+10-7;
//		}else if(ge==8){
//			back_number = n+10-8;
//		}else if(ge==9){
//			back_number = n+10-9;
//		}
//	}else{
//		back_number = parseInt(n);
//	}
//
//	
//	return back_number;
//}
//
//
//
//
//    //获取小区热力图颜色图例
//var getHeatMapColorLegend = function(p1,p2){//p1:最小值 p2:最大值  
//	//>min;<=max
//	var colorArray =  [
//		   {"min":0,"max":20,"color":"#f6ff01","scope":""},
//		   {"min":20,"max":40,"color":"#ffc001","scope":""},
//		   {"min":40,"max":60,"color":"#ff9c01","scope":""},
//		   {"min":60,"max":80,"color":"#ff6701","scope":""},
//		   {"min":80,"max":100,"color":"#ff1901","scope":""}
//		   ];
//
//	//var colorArrar = new Array("#007979","#00FFFF","#796400","#FFD306","#009100","#5151A2","#8F4586","#F75000","#FF0000","#FF9797","#FF0080","#600000","#820041","#4B0091","#A8FF24","#63B8FF","#5E5E5E","#bf88113d","#0000FF","#7CCD7C");
//
//	var colorArray_new = new Array();
//	if(p1!=null&&p2!=null){
//		if(p2==0){
//			
//		}else if(p2>0&&p2<1){
//			colorArray_new = [{"min":0,"max":1,"color":"#ff1901"}];
//		}else if(p2==1){
//			colorArray_new = [{"min":0,"max":1,"color":"#ff1901"}];
//
//		}else if(p2>1&&p2<2){
//			colorArray_new = [{"min":0,"max":1,"color":"#ff6701"},{"min":'>1',"color":"#ff1901"}];
//		}else if(p2>=2&&p2<3){
//			colorArray_new = [{"min":0,"max":2,"color":"#ff6701"},{"min":'>2',"color":"#ff1901"}];
//		}else if(p2>=3&&p2<4){
//			colorArray_new = [{"min":0,"max":3,"color":"#ff6701"},{"min":'>3',"color":"#ff1901"}];
//		}else if(p2>=4&&p2<5){
//			colorArray_new = [{"min":0,"max":4,"color":"#ff6701"},{"min":'>4',"color":"#ff1901"}];
//		}else if(p2>=5&&p2<6){
//			colorArray_new = [{"min":0,"max":5,"color":"#ff6701"},{"min":'>5',"color":"#ff1901"}];
//		}else if(p2>=6&&p2<7){
//			colorArray_new = [{"min":0,"max":6,"color":"#ff6701"},{"min":'>6',"color":"#ff1901"}];
//		}else if(p2>=7&&p2<8){
//			colorArray_new = [{"min":0,"max":7,"color":"#ff6701"},{"min":'>7',"color":"#ff1901"}];
//		}else if(p2>=8&&p2<9){
//			colorArray_new = [{"min":0,"max":8,"color":"#ff6701"},{"min":'>8',"color":"#ff1901"}];
//		}else if(p2>=9&&p2<10){
//			colorArray_new = [{"min":0,"max":9,"color":"#ff6701"},{"min":'>9',"color":"#ff1901"}];
//		}else if(p2>=10){
//			for(var j=0;j<colorArray.length;j++){
//				var minP = colorArray[j].min;
//				var maxP = colorArray[j].max;
//				var minP_1 = parseInt(minP/100*p2);
//				var maxP_1 = parseInt(maxP/100*p2);
//				if(minP_1!=maxP_1){
//					if(j==0){
//						colorArray[j].min = 0;
//						colorArray[j].max = getNumberOfDigitZero1(minP_1,maxP_1);
//					}else if(j<colorArray.length-1&&j>0){
//						colorArray[j].min = colorArray[j-1].max;
//						colorArray[j].max = getNumberOfDigitZero1(minP_1,maxP_1);
//					}else if(j==colorArray.length-1){
//						colorArray[j].min = colorArray[j-1].max;
//						colorArray[j].scope = ">"+colorArray[j-1].max;
//					}
//					
//					
//					
//					colorArray_new.push(colorArray[j]);
//				}
//				
//			}
//		}
//	}
//	
//	return colorArray_new;
//}

//获取小区热力图颜色
var getHeatMapColorLocation = function(p1,p2){//p1:平均值;p2:当前值
	var colorArray =getHeatColorLegend(p1);
//	var num;
//	if(p2<1&&p2>=0){
//		num = Math.floor(p2);
//	}else if(p2<10&&p2>=1){
//		num = Math.floor(p2);
//	}else if(p2<100&&p2>=10){
//		num = Math.floor(p2/10)*10;//十位数
//	}else if(p2<1000&&p2>=100){
//		num = Math.floor(p2/100)*100;//百位数
//	}else if(temp_percent20<10000&&p2>=1000){//千位数
//		num = Math.floor(p2/1000)*1000;
//	}else if(p2<100000&&p2>=10000){//万位数
//		num = Math.floor(p2/10000)*10000;
//	}else if(p2<1000000&&p2>=100000){//十万位数
//		num = Math.floor(p2/100000)*100000;
//	}else if(p2<10000000&&p2>=1000000){//百万位数
//		num = Math.floor(p2/1000000)*1000000;
//	}else if(p2<100000000&&p2>=10000000){//千万位数
//		num = Math.floor(p2/10000000)*10000000;
//	}else if(p2<1000000000&&p2>=100000000){//亿位数
//		num = Math.floor(p2/100000000)*100000000;
//	}
	
	var color="#ffee0000";
	for(var i=0;i<colorArray.length;i++){
		
		var minP = colorArray[i].min;
		var maxP = colorArray[i].max;
		if(i==colorArray.length-1){
			if(p2>=parseInt(minP)){
				color = colorArray[i].color;
				break;
			}
		}else{
			if(p2>=parseInt(minP)&&p2<parseInt(maxP)){
				color = colorArray[i].color;
				break;
			}
		}
	}
	return color;
}


var getDynamicLegend = function (colorArray){
	
	 $("#heat_legend_title").empty();
	 var backgroundStr = $(".menu_list_bg").css("background");
	 if(colorArray.length==0){
		 if(backgroundStr.indexOf("-webkit-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-webkit-linear-gradient(#ffee0000)");

		 }else if(backgroundStr.indexOf("-o-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-o-linear-gradient(#ffee0000)");

		 }else if(backgroundStr.indexOf("-moz-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-moz-linear-gradient(#ffee0000)");

		 }else if(backgroundStr.indexOf("linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000)");
		 }else if(backgroundStr.indexOf("none repeat scroll")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000)");
		 }
		 $("#heat_legend_title").append("<li>"+0+"</li>");
		 $("#heat_legend_color").show();
	 }else if(colorArray.length==1){
		 if(backgroundStr.indexOf("-webkit-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-webkit-linear-gradient(#ffee0000,#ff1901)");

		 }else if(backgroundStr.indexOf("-o-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-o-linear-gradient(#ffee0000,#ff1901)");

		 }else if(backgroundStr.indexOf("-moz-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-moz-linear-gradient(#ffee0000,#ff1901)");

		 }else if(backgroundStr.indexOf("linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000,#ff1901)");
		 }else if(backgroundStr.indexOf("none repeat scroll")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000,#ff1901)");

		 }
		 $("#heat_legend_title").append("<li>"+colorArray[0].min+"-"+colorArray[0].max+"</li>");
		 $(".menu_list li").css("width","98%");
		 $(".menu_list li").css("text-align","center");
		 $("#heat_legend_color").show();
	 }else if(colorArray.length==2){
		 
		 if(backgroundStr.indexOf("-webkit-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-webkit-linear-gradient(#ffee0000,#ff6701,#ff1901)");

		 }else if(backgroundStr.indexOf("-o-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-o-linear-gradient(#ffee0000,#ff6701,#ff1901)");

		 }else if(backgroundStr.indexOf("-moz-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-moz-linear-gradient(#ffee0000,#ff6701,#ff1901)");

		 }else if(backgroundStr.indexOf("linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000,#ff6701,#ff1901)");
		 }else if(backgroundStr.indexOf("none repeat scroll")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000,#ff6701,#ff1901)");

		 }
		 $("#heat_legend_title").append("<li>"+colorArray[0].min+"-"+colorArray[0].max+"</li>");
		 $("#heat_legend_title").append("<li>"+colorArray[1].min+"</li>");
		 $(".menu_list li").css("width","45%");
		 $(".menu_list li").css("text-align","right");
		 $("#heat_legend_color").show();
	 }else if(colorArray.length>2){
		 
		 
		 if(backgroundStr.indexOf("-webkit-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-webkit-linear-gradient(#ffee0000,#f6ff01,#ffc001,#ff9c01,#ff6701,#ff1901)");

		 }else if(backgroundStr.indexOf("-o-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-o-linear-gradient(#ffee0000,#f6ff01,#ffc001,#ff9c01,#ff6701,#ff1901)");

		 }else if(backgroundStr.indexOf("-moz-linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","-moz-linear-gradient(#ffee0000,#f6ff01,#ffc001,#ff9c01,#ff6701,#ff1901)");

		 }else if(backgroundStr.indexOf("linear-gradient")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000,#f6ff01,#ffc001,#ff9c01,#ff6701,#ff1901)");
		 }else if(backgroundStr.indexOf("none repeat scroll")!=-1){
			 $(".menu_list_bg").css("background","linear-gradient(to right, #ffee0000,#f6ff01,#ffc001,#ff9c01,#ff6701,#ff1901)");

		 }
		 for(var i=0;i<colorArray.length;i++){
			 if(i==(colorArray.length-1)){
				 $("#heat_legend_title").append("<li>"+colorArray[i].scope+"</li>");

			 }else{
				 $("#heat_legend_title").append("<li>"+colorArray[i].min+"-"+colorArray[i].max+"</li>");

			 }
			 
		 }
		 $("#heat_legend_color").show();
//	 }
	
}
}

//avg:平均值    avg只保留最高位，然后作为50%（avg=210,只保留200），算出20%的数值
var getHeatColorLegend = function(avg){
	var percent20;
	var temp_percent20 = Math.round(parseInt(avg)/5*2);
	if(avg>0&&avg<1){
		var colorArray =  [{"min":0,"max":"1","color":"#ff1901","scope":""}];
		return colorArray;
	}else if(avg>=1 &&avg<2){
		var colorArray =  [{"min":0,"max":1,"color":"#ff6701","scope":""},
						   {"min":1,"max":"","color":"#ff1901","scope":">"+1}];
		return colorArray;
	}else if(avg>=2){
		if(temp_percent20<0.5&&temp_percent20>=0){
			var colorArray =  [{"min":0,"max":"1","color":"#ff1901","scope":""}];
			return colorArray;
		}else if(temp_percent20>=0.5&&temp_percent20<1){
			percent20 = Math.floor(temp_percent20);
		}else if(temp_percent20<10&&temp_percent20>=1){
			percent20 = Math.floor(temp_percent20);
		}else if(temp_percent20<100&&temp_percent20>=10){
			percent20 = Math.floor(temp_percent20/10)*10;//十位数
		}else if(temp_percent20<1000&&temp_percent20>=100){
			percent20 = Math.floor(temp_percent20/100)*100;//百位数
		}else if(temp_percent20<10000&&temp_percent20>=1000){//千位数
			percent20 = Math.floor(temp_percent20/1000)*1000;
		}else if(temp_percent20<100000&&temp_percent20>=10000){//万位数
			percent20 = Math.floor(temp_percent20/10000)*10000;
		}else if(temp_percent20<1000000&&temp_percent20>=100000){//十万位数
			percent20 = Math.floor(temp_percent20/100000)*100000;
		}else if(temp_percent20<10000000&&temp_percent20>=1000000){//百万位数
			percent20 = Math.floor(temp_percent20/1000000)*1000000;
		}else if(temp_percent20<100000000&&temp_percent20>=10000000){//千万位数
			percent20 = Math.floor(temp_percent20/10000000)*10000000;
		}else if(temp_percent20<1000000000&&temp_percent20>=100000000){//亿位数
			percent20 = Math.floor(temp_percent20/100000000)*100000000;
		}
	} 
	
	var gtScope = ">"+percent20*4;
	var colorArray =  [{"min":0,"max":percent20,"color":"#f6ff01","scope":""},
					   {"min":percent20,"max":percent20*2,"color":"#ffc001","scope":""},
					   {"min":percent20*2,"max":percent20*3,"color":"#ff9c01","scope":""},
					   {"min":percent20*3,"max":percent20*4,"color":"#ff6701","scope":""},
					   {"min":percent20*4,"max":"","color":"#ff1901","scope":gtScope}
					   ];
	
	return colorArray;
}
