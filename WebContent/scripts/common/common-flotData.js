function FlotData(){}
FlotData.prototype = {
	formatData:function(flotChartModel,flotChartData){
		var datasets = new object();
		
		return datasets;
	},
	bildBarChoiceContainer:function(choseId,datasets){
		var choiceContainer = $("#"+choseId);
		choiceContainer.append("<table><tr>");
	    $.each(datasets, function(key, val) {
	        choiceContainer.append('<td><input type="checkbox" name="' + key +
	                               '" checked="checked" id="id' + key + '">' +
	                               '<label for="id' + key + '">'
	                                + val.label + '</label></td>');
	    });
		choiceContainer.append("</table></tr>");
	},
	plotBarAccordingToChoices:function(choseId,placeholder,datasets){
		var data = [];
		var choiceContainer = $("#"+choseId);
        choiceContainer.find("input:checked").each(function () {
            var key = $(this).attr("name");
            if (key && datasets[key])
                data.push(datasets[key]);
        });
		//变换数据格式开始
		var map = new Map();
		var mapdata = new Map();
		var valuemap = 1;
		for(var i=0;i<data.length;i++){
			var datachilds = data[i].data;
			for(var j=0;j<datachilds.length;j++){
				var datachild = datachilds[j];
				if(map.put(datachild[0], valuemap)){
					mapdata.put(valuemap, datachild[0]);
					valuemap++;
				}
			}
		}
		for(var i=0;i<data.length;i++){
			var datachilds = data[i].data;
			for(var j=0;j<datachilds.length;j++){
				var datachild = datachilds[j];
				data[i].data[j][0] = map.get(datachild[0]);
			}
		}
		//变换数据格式结束
        if (data.length > 0)
            $.plot($("#placeholder"), data, {
                xaxis: { tickFormatter: function (v) { 
					if(parseInt(v) != valuemap){
					    return mapdata.get(v); 
					}else{
		                return '';
					}
					} ,
					tickSize:1,min:1,max:valuemap},
	            yaxis: { tickFormatter: function (v) { return v; }}   
			}); 
	},
	initBar:function(choseId,placeholder,datasets){
		var choseIdObj = $("#"+choseId);
		if(choseIdObj != undefined){
			var choseIdObjs = choseIdObj.children();
			for(var i=0;i<choseIdObjs.length;i++){
				$(choseIdObjs[i]).remove();
			}
		}
		var placeholderObj = $("#"+placeholder);
		if(placeholderObj != undefined){
			var placeholderObjs = placeholderObj.children();
			for(var i=0;i<placeholderObjs.length;i++){
				$(placeholderObjs[i]).remove();
			}
		}
		this.bildBarChoiceContainer(choseId,datasets);
	    this.plotBarAccordingToChoices(choseId,placeholder,datasets);
		var choiceContainer = $("#"+choseId);
		choiceContainer.find("input").bind('click',{choseId:choseId,placeholder:placeholder,datasets:datasets},function(event){
			var flotData = new FlotData();
			flotData.plotBarAccordingToChoices(event.data.choseId,event.data.placeholder,event.data.datasets);
		});
	},
	bildPieChoiceContainer:function(choseId,datasets){
		var choiceContainer = $("#"+choseId);
		choiceContainer.append("<table><tr>");
		var i=0;
		$.each(datasets, function(key, val) {
	        if(i == 0){
	          choiceContainer.append('<td><input type="radio" name="aaa" checked="checked" id="' + key + '">' +
	                               '<label for="id' + key + '">'
	                                + val.label + '</label></td>');
		   }else{
	          choiceContainer.append('<td><input type="radio" name="aaa" id="' + key + '">' +
	                               '<label for="id' + key + '">'
	                                + val.label + '</label></td>');
		   }
		   i++;
	    });
		choiceContainer.append("</table></tr>");
	},
	plotPieAccordingToChoices:function(choseId,placeholder,datasets){
		var choiceContainer = $("#"+choseId);
        choiceContainer.find("input:checked").each(function () {
            var key = $(this).attr("id");
			var datas = datasets[key];
            if (key && datas){
			    var data = datas.data;
			    $.plot($("#"+placeholder), data, 
				{
					series: {
						pie: { 
							show: true,
							radius: 1,
							label: {
								show: true,
								radius: 1,
								formatter: function(label, series){
									return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'+label+'<br/>'+Math.round(series.percent)+'%</div>';
								},
								background: { 
									opacity: 0.5,
									color: '#000'
								}
							}
						}
					},
					legend: {
						show: false
					}
				});
			}
        });
	},
	initPie:function(choseId,placeholder,datasets){
		this.bildPieChoiceContainer(choseId,datasets);
	    this.plotPieAccordingToChoices(choseId,placeholder,datasets);
		var choiceContainer = $("#"+choseId);
		choiceContainer.find("input").bind('click',{choseId:choseId,placeholder:placeholder,datasets:datasets},function(event){
			var flotData = new FlotData();
			flotData.plotPieAccordingToChoices(event.data.choseId,event.data.placeholder,event.data.datasets);
		});
	},
}

function containsArray(array,element) {
  
    for (var i = 0; i < array.length; i++) {
        if (array[i] == element) {
            return true;
			break;
        }
    }
    return false;
}

function FlotDataModel(label,data){
	this.label = label;
	this.data = data;
}

function FlotDataTools(){}

FlotDataTools.prototype = {
	formatColumnsToBarData: function(columnsData, allColumns, barDataModel){
		var datasets = {};
		var columnData = columnsData.data[0];
		var width = 1/(barDataModel.length+1);
		for(var i=0;i<barDataModel.length;i++){
			var modelData = barDataModel[i].data;
			var keysData = modelData.data;
			var chartData = [];
			for(var j=0;j<keysData.length;j++){
				var datas = [];
				var keyData = columnData[keysData[j]];
				datas[0] = j+1+(width*i);
				datas[1] = keyData;
				chartData[j] = datas;
			}
			var modelChartData = {};
			modelChartData.label = modelData.label;
			modelChartData.data = chartData;
			datasets[barDataModel[i].id] = modelChartData;
		}
		return datasets;
	},
	formatColumnsToPieData: function(columnsData, allColumns, pieDataModel){
		var datasets = {};
		var columnData = columnsData.data[0];
		for(var i=0;i<pieDataModel.length;i++){
			var modelData = pieDataModel[i].data;
			var keysData = modelData.data;
			var chartData = [];
			for(var j=0;j<keysData.length;j++){
				var datas = {};
				var keyData = columnData[keysData[j]];
				var headData = this.getHeaderByKey(keysData[j],allColumns);
				datas.label = headData;
				datas.data = keyData;
				chartData[j] = datas;
			}
			var modelChartData = {};
			modelChartData.label = modelData.label;
			modelChartData.data = chartData;
			datasets[pieDataModel[i].id] = modelChartData;
		}
		return datasets;
	},
	getHeaderByKey: function(key, allColumns){
		var keyheader = '';
		for(var i=0;i<allColumns.length;i++){
			if(allColumns[i].key == key){
				keyheader = allColumns[i].header;
				break;
			}
		}
		return keyheader;
	},

}



