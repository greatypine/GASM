

function isArray(o) {      
  return Object.prototype.toString.call(o) === '[object Array]';       
} 

function in_array(e,arr){  
	for(var i=0;i<arr.length;i++){  
		if(arr[i] == e)  
		return true;  
	}  
	return false;  
} 


var BidSelectMonth = function(_cfg){ 
	
	this.renderTo = "bidSelectMonth";//表格渲染的容器ID
	this.renderToDiv = null;   //渲染的DIV对象
	this.yearInitMin = 30;
	this.yearInitMax = 30;
	this.BidSelectMonth = null;
	this.yearSelect=null;
	this.monthSelect=null;

	
	
	$.extend(this,_cfg);
	
	
	this.getMonthSelect = function(){
	    return this.monthSelect;
	}
	
	this.getYearSelect = function(){
	    return this.yearSelect;
	}
	
	
	
	/*
	* 创建表格方法
	*/
	this.createBidSelectMonth = function(){
		
		var date=new Date();

		var m0=[];
		m0.push('01');
		m0.push('02');
		m0.push('03');
		m0.push('04');
		m0.push('05');
		m0.push('06');
		m0.push('07');
		m0.push('08');
		m0.push('09');
		m0.push('10');
		m0.push('11');
		m0.push('12');
		
		var m1=[];
		m1.push('一');
		m1.push('二');
		m1.push('三');
		m1.push('四');
		m1.push('五');
		m1.push('六');
		m1.push('七');
		m1.push('八');
		m1.push('九');
		m1.push('十');
		m1.push('十一');
		m1.push('十二');
		
		
		
		
		this.BidSelectMonth = $("<span class='bidSelectMonth' ></span>");
		this.yearSelect=$("<select></select>");
		for(var i=date.getFullYear()-this.yearInitMin;i<=date.getFullYear()+this.yearInitMax;i++){
			var option=$("<option value='"+i+"'>"+i+"</option>");
			if(i==date.getFullYear()){
				option.attr("selected","selected");
			}
			this.yearSelect.append(option);
		}
		
		this.monthSelect=$("<select></select>");
		for(var i=0;i<=11;i++){
			var option=$("<option value='"+m0[i]+"'>"+m1[i]+"</option>");
			if(i==(date.getMonth())){
				option.attr("selected","selected");
			}
			this.monthSelect.append(option);
		}
		this.BidSelectMonth.append(this.yearSelect);
		this.BidSelectMonth.append($("<label>年</label>"));
		this.BidSelectMonth.append(this.monthSelect);
		this.BidSelectMonth.append($("<label>月</label>"));	
		this.renderToDiv.append(this.BidSelectMonth);//表格
		
	};


	

	
	
	
	this.getBidSelectMonth = function(){
	    return this.BidSelectMonth;
	}
	
	/*
	* 获取表格中的值，以数组形式返回
	*/
	this.getValue = function(){
		var year=$(this.yearSelect).val();
		var month=$(this.monthSelect).val();
		return year+"-"+month;
		
	};
	
	
	
	
	
	
	this.createBidSelectMonth();
	
	
};
var bidSelectMonths = [];
$(function(){
	$("span[class*='bidSelectMonth']").each(function(i,element){
		var defaults = $(this).attr("defaults");
		if(defaults==null||defaults==""){
			defaults = "{}";	
		}
		eval("var defaults ="+defaults);	
		defaults.tableIndex = i;
		defaults.renderToDiv= $(element);
		var obj = new BidSelectMonth(defaults);
		bidSelectMonths.push(obj);
	});
	

})








