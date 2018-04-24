
/*
* @Object       : Date
* @methodName   : format
* @methodParam  : String （format字符串））
* @methodReturn : Date
* @author       : ruanqingfeng
* @desc         : 扩展时间对象的格式化函数
* @for example1  : var now = new Date(942278400000).format("yyyy-MM-dd hh:mm:ss"); 
*/
Date.prototype.format = function(format) {   
   /*  
     * eg:format="YYYY-MM-dd hh:mm:ss";  
     */  
    var o = {   
       "M+" :this.getMonth() + 1, // month   
       "d+" :this.getDate(), // day   
       "h+" :this.getHours(), // hour   
       "m+" :this.getMinutes(), // minute   
       "s+" :this.getSeconds(), // second   
       "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter   
       "S"  :this.getMilliseconds()   
    // millisecond   
    }   
 
   if (/(y+)/.test(format)) {   
       format = format.replace(RegExp.$1, (this.getFullYear() + "")   
               .substr(4 - RegExp.$1.length));   
   }   

  for ( var k in o) {   
       if (new RegExp("(" + k + ")").test(format)) {   
           format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]   
                   : ("00" + o[k]).substr(("" + o[k]).length));   
      }   
   }
   if(format == "1970-01-01"){
	   format = "";
   }   
   return format;   
}



/* Chinese initialisation for the jQuery UI date picker plugin. */
/* Written by Cloudream (cloudream@gmail.com). */
jQuery(function($){
	$.datepicker._selectDay = function(a,b,c,e){d = $;var f=d(a);if(!(d(e).hasClass(this._unselectableClass)||this._isDisabledDatepicker(f[0]))){f=this._getInst(f[0]);f.selectedDay=f.currentDay=
d("a",e).html();f.selectedMonth=f.currentMonth=b;f.selectedYear=f.currentYear=c;this._selectDate(a,this._formatDate(f,f.currentDay,f.currentMonth,f.currentYear))};$(a).trigger("change");}
});
