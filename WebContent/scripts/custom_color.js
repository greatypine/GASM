var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/; 
String.prototype.colorHex = function(){  
    var that = this;  
    if(/^(rgb|RGB)/.test(that)){  
        var aColor = that.replace(/(?:||rgb|RGB)*/g,"").split(",");  
        var strHex = "#";  
        for(var i=0; i<aColor.length; i++){  
            var hex = Number(aColor[i]).toString(16);  
            if(hex === "0"){  
                hex += hex;   
            }  
            strHex += hex;  
        }  
        if(strHex.length !== 7){  
            strHex = that;    
        }  
        return strHex;  
    }else if(reg.test(that)){  
        var aNum = that.replace(/#/,"").split("");  
        if(aNum.length === 6){  
            return that;      
        }else if(aNum.length === 3){  
            var numHex = "#";  
            for(var i=0; i<aNum.length; i+=1){  
                numHex += (aNum[i]+aNum[i]);  
            }  
            return numHex;  
        }  
    }else{  
        return that;      
    }  
};
var curColor = '613030';
var generateColor = function() {
	
	var customColor=(parseInt(curColor, 16) + parseInt('20000', 10)).toString(16);
	curColor = customColor;
	return customColor;
};

function getColor(i){
	var a,b,c;
	var initColor=0.9;

	
	a=Math.floor((initColor/parseInt(i))*100+(parseInt(i)*11.5));
	b=Math.floor((initColor/parseInt(i))*150+(parseInt(i)*12.5+100));
	c=Math.floor((initColor/parseInt(i))*200+(parseInt(i)*13.5+150));
		
	var sRgb = "RGB("+a+", "+b+", "+c+")";
	var sHexColor = sRgb.colorHex();
	return sHexColor;
			//定义字符串变量colorValue存放可以构成十六进制颜色值的值
			//var colorValue="0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f";
			//以","为分隔符，将colorValue字符串分割为字符数组["0","1",...,"f"]
			//var colorArray = colorValue.split(",");
			//var color="#";//定义一个存放十六进制颜色值的字符串变量，先将#存放进去
			//使用for循环语句生成剩余的六位十六进制值
			//for(var i=0;i<6;i++){
				//colorArray[Math.floor(Math.random()*16)]随机取出
				// 由16个元素组成的colorArray的某一个值，然后将其加在color中，
				//字符串相加后，得出的仍是字符串
			//	color+=colorArray[Math.floor(Math.random()*16)];
			//}
			//return color;
		}