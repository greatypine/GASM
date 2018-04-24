function EditTable(){
	this.rowSize=0;
	this.colSize=0;
	this.templateHtml='';
	this.templateHtmlDataMap='';
	this.templateHtmlData='';
	this.templateHtmlWithKey='';
	this.templateHtmlWithInput='';
	this.templateHtmlRemoveKey='';
	this.biddersTemplateHtml='';
	this.cellArray=[];
	this.captionTitle;
	this.datakeyattrs = [];
	this.dataoldkeyattrs = [];
	this.calculateUnitAttrs = [];
	this.cellDataTypeMap = new Map();
	this.src;
}
EditTable.prototype = {
	eachTableHtml:function(o){// columns size
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.firstChild;
	    this.captionTitle = objHtml.caption.innerHTML;
		var rows = objHtml.rows;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			for(var j=0;j<cells.length;j++){
				if(j+1>this.colSize){
					this.colSize = j+1;
				}
			}
		}
		this.rowSize = rows.length;
	},
	formatTableHtml:function(o){ //give td datetype and datekey attribute
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.firstChild;
	    this.captionTitle = objHtml.caption.innerHTML;
		var rows = objHtml.rows;
		var k = 0;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			var rowsHtml = [];
			for(var j=cells.length-1;j>=0;j--){
				var jj = this.colSize + 1;
				var ii = i+1;
				var colspan = cells[j].getAttribute("colspan");
				var jj1 = jj;
				if(j != cells.length-1){
					jj1 = cells[j+1].getAttribute("jj");
				}
			    if(typeof(colspan) != 'undefined' && colspan != null){
					jj = jj1-parseInt(colspan);
				}else{
					jj = jj1-1;
				}
				var datakey = this.formatNumberToLetter(jj)+String(ii);
				var dataoldkey = cells[j].getAttribute("dataoldkey");
				if(dataoldkey == null || typeof(dataoldkey) == 'undefined' || dataoldkey == ''){
					dataoldkey = datakey;
				}
				var datatype = cells[j].getAttribute("datatype");
				if(datatype != 'label'){
					this.datakeyattrs[k] = datakey;
				    this.dataoldkeyattrs[k] = dataoldkey;
					k++;
				}
				this.cellDataTypeMap.put(datakey,datatype);
				cells[j].removeAttribute("dataoldkey");
				cells[j].removeAttribute("datakey");
				cells[j].setAttribute("dataoldkey",dataoldkey);
				cells[j].setAttribute("datakey",datakey);
				cells[j].setAttribute("jj",jj);
				var cellsHtml = cells[j].innerHTML.replace(/&nbsp;/g,"").replace(/<br>/g,"");
				rowsHtml[j] = cells[j].outerHTML;
				if(datatype == null || typeof(datatype) == 'undefined' || datatype == ''){
					if(typeof(cellsHtml) == 'undefined' || cellsHtml == null || (cellsHtml).trim() == ''){
						cells[j].setAttribute("datatype","data");
						cells[j].innerHTML = '';
					}else{
						cells[j].setAttribute("datatype","label");
					}
				}else{
					cells[j].removeAttribute("dataoldkey");
					cells[j].removeAttribute("datakey");
					cells[j].setAttribute("dataoldkey",dataoldkey);
					cells[j].setAttribute("datakey",datakey);
					if(datatype != 'label'){cells[j].innerHTML = ''}
				}
			}
			this.cellArray[i] = rowsHtml;
		}
		this.rowSize = rows.length;
		this.templateHtml = objHtml.outerHTML;
	},
	formatTableHtmlAddKey:function(o){ //give td datetype and datekey attribute
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.firstChild;
	    this.captionTitle = objHtml.caption.innerHTML;
		var rows = objHtml.rows;
		var k = 0;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			var rowsHtml = [];
			for(var j=cells.length-1;j>=0;j--){
				var jj = this.colSize + 1;
				var ii = i+1;
				var colspan = cells[j].getAttribute("colspan");
				var jj1 = jj;
				if(j != cells.length-1){
					jj1 = cells[j+1].getAttribute("jj");
				}
			    if(typeof(colspan) != 'undefined' && colspan != null){
					jj = jj1-parseInt(colspan);
				}else{
					jj = jj1-1;
				}
				var datakey = this.formatNumberToLetter(jj)+String(ii);
				var dataoldkey = cells[j].getAttribute("dataoldkey");
				if(dataoldkey == null || typeof(dataoldkey) == 'undefined' || dataoldkey == ''){
					dataoldkey = datakey;
				}
				var datatype = cells[j].getAttribute("datatype");
				if(datatype != 'label'){
					this.datakeyattrs[k] = datakey;
				    this.dataoldkeyattrs[k] = dataoldkey;
					k++;
				}
				this.cellDataTypeMap.put(datakey,datatype);
				//cells[j].removeAttribute("dataoldkey");
				cells[j].removeAttribute("datakey");
				//cells[j].setAttribute("dataoldkey",dataoldkey);
				cells[j].setAttribute("datakey",datakey);
				cells[j].setAttribute("jj",jj);
				var cellsHtml = cells[j].innerHTML.replace(/&nbsp;/g,"").replace(/<br>/g,"");
				rowsHtml[j] = cells[j].outerHTML;
				if(datatype == null || typeof(datatype) == 'undefined' || datatype == ''){
					if(typeof(cellsHtml) == 'undefined' || cellsHtml == null || (cellsHtml).trim() == ''){
						cells[j].setAttribute("datatype","data");
						cells[j].innerHTML = '';
					}else{
						cells[j].setAttribute("datatype","label");
					}
				}else{
					//cells[j].removeAttribute("dataoldkey");
					cells[j].removeAttribute("datakey");
					//cells[j].setAttribute("dataoldkey",dataoldkey);
					cells[j].setAttribute("datakey",datakey);
					if(datatype != 'label'){cells[j].innerHTML = ''}
				}
			}
			this.cellArray[i] = rowsHtml;
		}
		this.rowSize = rows.length;
		this.templateHtml = objHtml.outerHTML;
	},
	init:function (o){//init
		this.src = o;
		this.eachTableHtml(o);
		this.formatTableHtml(this.src);
	},
	
	initAddKey:function (o){//initAddKey
		this.src = o;
		this.eachTableHtml(o);
		this.formatTableHtmlAddKey(this.src);
		this.formatTemplateHtmlWithKey(this.templateHtml);
		return this.templateHtmlWithKey;
	},
	
	formatTemplateHtmlWithKey:function(o){ //to template add key
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.lastChild;
		var rows = objHtml.rows;
		var k = 0;
		var h = 0;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			for(var j=0;j<cells.length;j++){
				var datakey = cells[j].getAttribute("datakey");
				var dataoldkey = cells[j].getAttribute("dataoldkey");
				var datatype = cells[j].getAttribute("datatype");
				if(datatype != 'label'){
					this.datakeyattrs[k] = datakey;
				    this.dataoldkeyattrs[k] = dataoldkey;
					k++;
				}
				this.cellDataTypeMap.put(datakey,datatype);
				if(datatype == null || typeof(datatype) == 'undefined' || datatype == '' || datatype == 'label'){
					
				}else if(datatype == 'calculateUnit'){
					cells[j].innerHTML = '<font color=#COCOCO>'+cells[j].getAttribute("datakey")+'</font>';
					cells[j].style.cssText = "background-color:#F0FFF0";
					this.calculateUnitAttrs[h] = cells[j].getAttribute("datakey");
					h++
				}else{
					cells[j].innerHTML = '<font color=#COCOCO>'+cells[j].getAttribute("datakey")+'</font>';
				}
				
			}
		}
		this.templateHtmlWithKey = objHtml.outerHTML;
	},
	formatTemplateHtmlToOldKey:function(o){ //去掉oldkey
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.lastChild;
		var rows = objHtml.rows;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			for(var j=0;j<cells.length;j++){
				var datakey = cells[j].getAttribute("datakey");
				cells[j].removeAttribute("dataoldkey");
				cells[j].setAttribute("dataoldkey",datakey);
				cells[j].removeAttribute("jj");
			}
		}
		return objHtml.outerHTML;
	},
	
	formatTemplateHtmlRemoveKey:function(o){  //update template
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.lastChild;
		var rows = objHtml.rows;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			for(var j=0;j<cells.length;j++){
				var datatype = cells[j].getAttribute("datatype");
				if(datatype == null || typeof(datatype) == 'undefined' || datatype == ''){
					
				}else if(datatype == 'data'){
					cells[j].innerHTML = '';
				}else{
					
				}
			}
		}
		this.templateHtmlRemoveKey = objHtml.outerHTML;
	},
	getNewKeysValueStr:function(key){//get new key by old key result more
	    var keys = [];
		if(key != ""){keys = key.split(',')}
		var arr1 = this.dataoldkeyattrs;
		var arr2 = this.datakeyattrs;
	    var keyvalue = '';
	    for(var i=0;i<keys.length;i++){
           var key1 = this.getNewKeysByOldKeys(arr1,arr2,keys[i]);
           if(key1 != '')
		   keyvalue = keyvalue + key1 +',';
	    }
	    keyvalue = keyvalue.substr(0,keyvalue.length-1);
		if(keyvalue == ''){keyvalue = key}
	    return keyvalue;
	},
	getNewKeysByOldKeys:function(arr1,arr2,key){//get new key by old key result more
		var keyvalue = '';
		for(var i=0;i<arr1.length;i++){
			if(arr1[i] === key){
				keyvalue = keyvalue + arr2[i]+',';
			}
		}
		keyvalue = keyvalue.substr(0,keyvalue.length-1);
		return keyvalue;
	},
	getNewKeyByOldKey:function(key){//get new key by old key  result one
		var arr1 = this.dataoldkeyattrs;
		var arr2 = this.datakeyattrs;
		var keyvalue = '';
		for(var i=0;i<arr1.length;i++){
			if(arr1[i] === key){
				keyvalue = arr2[i];
			}
		}
		if(keyvalue == ''){keyvalue = key}
		return keyvalue;
	},
	
	formatTemplateHtmlWithInput:function(o){
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.lastChild;
		var rows = objHtml.rows;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			for(var j=0;j<cells.length;j++){
				var datatype = cells[j].getAttribute("datatype");
				if(datatype == null || typeof(datatype) == 'undefined' || datatype == ''){
					
				}else if(datatype == 'data'){
					var cellHtml = cells[j].innerHTML.replace(/<br>/g,"").replace(/&nbsp;/g,"");
					var datakey = cells[j].getAttribute("datakey");
					var fn = cells[j].getAttribute("fn");
					if(cellHtml != null && cellHtml.trim() != "" ){
						if (fn == null || typeof(fn) == 'undefined' || fn == '') {
						    cells[j].innerHTML = "<input type='text' name='"+datakey+"' id='"+datakey+"' style='width:90%;border:0;height:20px'>"+cellHtml;
						}else{
							fn = fn.substr(0,fn.indexOf('(')+1)+"'"+fn.substring(fn.indexOf('(')+1,fn.indexOf(')'))+"','"+datakey+"')"
							cells[j].innerHTML = "<input type='text' name='"+datakey+"' id='"+datakey+"' style='width:90%;border:0;height:20px' onfocus="+fn+" onclick="+fn+" readonly>"+cellHtml;
						}
					}else{
						if (fn == null || typeof(fn) == 'undefined' || fn == '') {
						    cells[j].innerHTML = "<input type='text' name='"+datakey+"' id='"+datakey+"' style='width:98%;border:0;height:20px'>";
						}else{
							fn = fn.substr(0,fn.indexOf('(')+1)+"'"+fn.substring(fn.indexOf('(')+1,fn.indexOf(')'))+"','"+datakey+"')"
							cells[j].innerHTML = "<input type='text' name='"+datakey+"' id='"+datakey+"' style='width:98%;border:0;height:20px' onfocus="+fn+" onclick="+fn+" readonly>";
						}
						
					}
					
				}else if(datatype == 'label'){
					cells[j].setAttribute("height","21px");
					cells[j].innerHTML = "<B>" +cells[j].innerHTML+ "</B>"
				}else{
					
				}
			}
		}
		this.templateHtmlWithInput = objHtml.outerHTML;
	},
	importTableHtmlData:function(o,map){
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.firstChild;
		var rows = objHtml.rows;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			for(var j=0;j<cells.length;j++){
				var datatype = cells[j].getAttribute("datatype");
				if(datatype == null || typeof(datatype) == 'undefined' || datatype == ''){
					
				}else if(datatype == 'data'){
					var key = cells[j].getAttribute("datakey");
					var mapvalue = map.trim().split(",");
					for(var k=0;k<mapvalue.length;k++){
						var mapkeys = mapvalue[k].trim().split(":");
						if(key == mapkeys[0].replace(/"/g,"")){
							cells[j].innerHTML = mapkeys[1];
						}
					}
					
				}else{
					
				}
				
			}
		}
		this.templateHtmlData = objHtml.outerHTML;
	},
	exportTableHtmlData:function(o){
		var objdiv = document.createElement("div");
		objdiv.innerHTML = o;
		var objHtml = objdiv.firstChild;
		var rows = objHtml.rows;
		for(var i=0;i<rows.length;i++){
			var cells = rows[i].cells;
			for(var j=0;j<cells.length;j++){
				var datatype = cells[j].getAttribute("datatype");
				if(datatype == null || typeof(datatype) == 'undefined' || datatype == ''){
					
				}else if(datatype == 'data'){
					var key = cells[j].getAttribute("datakey");
					var keyvalue = cells[j].innerHTML.replace(/&nbsp;/g,"").replace(/<br>/g,"");
					this.templateHtmlDataMap = this.templateHtmlDataMap + key +":" +keyvalue+",";
				}else{
					
				}
				
			}
		}
		
	},
	
	formatNumberToLetter:function(o){
		var letters = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
		return letters[o-1];
	},
	
}

function EditTableTools(){
	
}

EditTableTools.prototype = {
	 formatColonToComma:function(str){//将公式里的冒号转化成逗号，验证
	   var arrt = '';
	   if(str.indexOf(':')>0 && str.indexOf(',')>0){
	      alert("公式里面不能同时存在冒号和逗号！");
		  return 'false';
	   }else if(str.indexOf(':')>0 && str.indexOf(',') == -1){
	   	  //alert(str.indexOf(':'));
		  //alert(str.indexOf(','));
	      var arrts = str.split(':');
	      if(arrts.length == 2){
	         var arrt0 = this.resolutionStr(arrts[0]);
			 var arrt1 = this.resolutionStr(arrts[1]);
			 var start = parseInt(arrt0[1]);
			 var end = parseInt(arrt1[1]);
	         if(arrt0[0] === arrt1[0] && start < end){
				 for(var i=start;i<=end;i++){
	                arrt = arrt + arrt0[0] + i + ',';
				 }
				 arrt = arrt.substr(0,arrt.length-1);
			 }else if(arrt0[0] !== arrt1[0] && arrt0[1] === arrt1[1]){
	            //alert("1公式格式不对！");
				return arrt;
			 }else{
	            //alert("2公式格式不对！");
				return str;
			 }
		  }else{
	         //alert("3公式格式不对！");
			 return str;
		  }
	   }else if(str.indexOf(':')==-1 && str.indexOf(',') > 0){
	      arrt = str;
	   }else{
	      //alert("4公式格式不对！");
		  arrt = str;
		  return arrt;
	   }
	   if(arrt == '' || arrt == null)
	   arrt = str;
	   return arrt;
	 },
	 formatColonToCommaStr:function(str){//将公式里的冒号转化成逗号
	   var arrt = '';
	   var arrts = str.split(":");
	   if(arrts.length === 2){
	   	 var arrt0 = this.resolutionStr(arrts[0]);
		 var arrt1 = this.resolutionStr(arrts[1]);
		 var start = parseInt(arrt0[1]);
		 var end = parseInt(arrt1[1]);
         if(arrt0[0] === arrt1[0] && start < end){
			 for(var i=start;i<=end;i++){
                arrt = arrt + arrt0[0] + i + ',';
			 }
			 arrt = arrt.substr(0,arrt.length-1);
		 }else if(arrt0[0] !== arrt1[0] && arrt0[1] === arrt1[1]){
            //alert("1公式格式不对！");
			return false;
		 }else{
            //alert("2公式格式不对！");
			return false;
		 }
	   }
	   if(arrt == '' || arrt == null)
	   arrt = str;
	   return arrt;
	 },
	 formatCommaToColon:function(str){//将公式里的逗号转化成冒号
	   var arrt = '';
	   var strs = str.split(',');
	   arrt = strs[0]+':'+strs[strs.length-1];
	   return arrt;
	 },
	 getDatatypeByDatekey:function(map,str){//未完成
	 	var arrts = [];
		if(str.search(/[a-zA-Z]+/)==0){
			var i = str.search(/[0-9]+/);
            arrts[0] = str.substr(0,i);
			arrts[1] = str.substr(i,str.length);
		}else{
           alert("字符串格式不对！");
		   return false;
		}
		return arrts;
	 },
	 resolutionStr:function(str){
	 	var arrts = [];
		if(str.search(/[a-zA-Z]+/)==0){
			var i = str.search(/[0-9]+/);
            arrts[0] = str.substr(0,i);
			arrts[1] = str.substr(i,str.length);
		}else{
           alert("字符串格式不对！");
		   return false;
		}
		return arrts;
	 },
	 isExitWithArrayByArr:function(firstArrs,arrs){ //key arrs contain keys
	 	var b='true';
	    for(var i=0;i<arrs.length;i++){
	       if(!this.isExitWithArray(firstArrs,arrs[i])){ 
		     b=arrs[i];
			 break;
		   }
	    }
	    return b;
	 },
	 isExitWithArrayByStr:function(firstArrs,str){ //key arrs contain key
	 	var b="true";
	    if(!this.isExitWithArray(firstArrs,str)){
			b = str;
		}
	    return b;
	 },
	 isExitWithArray:function(firstArrs,str){
	 	var b=false;
	    for(var i=0;i<firstArrs.length;i++){
	       if(firstArrs[i]==str){
		     b=true;
			 break;
		   }
	    }
	    return b;
	 },
	 isCalculateUnit:function(cellDataTypes,str){//验证公式左端key的类型必须为计算单元（新加）
		 	var b=true;		 	
		 	var typeValue=cellDataTypes.get(str);
		 	if(typeValue!='calculateUnit'){b=false;}		 	
		    return b;
	},
	getCalculateCellsByArrs:function(cellDataTypes,arrs){//获取数组单元格中type为计算单元的单元格数组
	 	var calculateCells=[];
	 	for(index in arrs){
	 		var typeValue=cellDataTypes.get(arrs[index]);
	 		if(typeValue=='calculateUnit'){
	 			calculateCells.push(arrs[index]);
	 		}
	 	}
	 	return calculateCells;	 	
    },
	isExitInPreviousKeys:function(objTr,arrs){//验证公式里面的计算单元格必须在前面的公式的key值里面出现过
	 	var b="true";
	 	//前面没有tr则全部返回
	 	if(objTr.prev("tr:visible").html()==null){
	 		return arrs.toString();
	 	}
	 	var keys=[];
	 	objTr.prevAll("tr:visible").each(function(i){
	 		keys.push($(this).find("input[name='key']").val());
	 	});
	 	if(keys.lenght==0){return arrs.toString();}	 	
	 	var errorCells=[];
	 	for(index in arrs){	 		
	 		var i=$.inArray(arrs[index], keys);
	 		
	 		if(i==-1){
	 			errorCells.push(arrs[index]);
	 		}
	 	}	 	
	 	if(errorCells.length>0){
	 		return errorCells.toString();
	 	}
	    return b;
   },
   hasSavedOfPreviousFuncs:function(objTr){//验证前面公式均处于保存状态。
	 	var b=true;
	 	//前面没有tr则全部返回
	 	if(objTr.prev("tr:visible").html()==null){
	 		return true;
	 	}	 	
	 	objTr.prevAll("tr:visible").each(function(i){
	 		var disabled=$(this).find("input[name='save']").attr("disabled");	 
	 		//alert("i:"+i+"attr:"+disabled);
	 		if(disabled!=true){	 			
	 			b=false;
	 			return false;
	 		}	 		
	 	});	 
	 	
	    return b;
   },
   
}