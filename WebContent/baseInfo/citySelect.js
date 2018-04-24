
/**
 * Jquery省市区联动代码重构
 * @version 0.0.2
 */

 function CitySelect(options) {
	this.config = {
		 data             :      '',             // 数据
		 provId           :      '',             // 省下拉框id容器 格式是 #XXX
		 cityId           :      '',             // 市下拉框id容器 格式是 #XXX
		 areaId           :      '',             // 区下拉框id容器 格式是 #XXX
		 streetId         :      '',             // 街道下拉框id容器 格式是 #XXX
		 isSelect         :      true,           // 默认为true 选择第一项 如果页面有指定省市区项时，就显示指定项。
		                                         // 如果false，就选中 "请选择"
		 provCallBack     :      null,           // 省份渲染完成后回调
		 cityCallBack     :      null,           // 市渲染完成后的回调
		 areaCallBack     :      null,           // 区渲染完成后的回调
		 streetCallBack   :      null            // 街道渲染完成后的回调
	};
	this.config = $.extend(this.config, options || {});
	this.init();
 }

 CitySelect.prototype = {

	constructor: CitySelect,
	
	/*
	 * 初始化方法init
	 */
	init: function() {
		var self = this,
			_config = self.config;

		if(!/^#/.test(_config.provId)) {
			alert("省份ID传的不正确,格式是 #XXX");
			return;
		}
		if(!/^#/.test(_config.cityId)) {
			alert("城市ID传的不正确,格式是 #XXX");
			return;
		}
		if(!/^#/.test(_config.areaId)) {
			alert("区ID传的不正确,格式是 #XXX");
			return;
		}
		if(!/^#/.test(_config.streetId)) {
			alert("街道ID传的不正确,格式是 #XXX");
			return;
		}
		this.provCode = $(_config.provId).attr("data-code");
		this.cityCode = $(_config.cityId).attr("data-code");
		this.areaCode = $(_config.areaId).attr("data-code");
		this.streetCode = $(_config.streetId).attr("data-code");
		
		// 使省市区id (#XX --> XX)
		this.provid =  _config.provId.replace(/^#/g,'');
		this.cityid = _config.cityId.replace(/^#/g,'');
		this.areaid = _config.areaId.replace(/^#/g,'');
		this.streetid = _config.streetId.replace(/^#/g,'');

		// 渲染省份
		self._provFunc();

		//渲染省对应的市
		self._cityFunc();
		
		// 渲染省市 对应的区
		self._areaFunc();
		
		// 渲染省市 对应的街道
		self._streetFunc();
		
		// 如果省份的值是-1的话 那么设置省市区select都为-1
		var provFirstOpt = $(_config.provId).val();
		if(provFirstOpt == -1) {
			$(_config.provId).attr("data-code","-1");
			$(_config.cityId).attr("data-code","-1");
			$(_config.areaId).attr("data-code","-1");
			$(_config.streetId).attr("data-code","-1");
		}
		// change事件 省切换 自动切换市 and 区 and 街道
		$(_config.provId).change(function(e){
			self._cityFunc();
			self._areaFunc();
			self._streetFunc();
		});

		// 市 change事件 自动切换区 and 街道
		$(_config.cityId).change(function(e){
			self._areaFunc();
		});
		// 区change事件 
		$(_config.areaId).change(function(e){
			$(_config.areaId).attr("data-code",$(this).val());
			self._streetFunc();
		});
		// 街道change事件 
		$(_config.streetId).change(function(){
			$(_config.streetId).attr("data-code",$(this).val());
		});
	},
	/*
	 * 渲染省数据
	 * @method _provFunc {private}
	 */
	_provFunc: function(){
		var self = this,
			_config = self.config;

		var elemHtml = '';
		elemHtml+= '<option value="-1" data-name="请选择" selected>请选择</option>';

		$(_config.data).each(function(i,curItem){
			elemHtml += "<option data-name='"+curItem[1]+"' value='"+curItem[0]+"'>"+curItem[1]+"</option>";
		});
		$(_config.provId).html(elemHtml);
		
		if(_config.isSelect) {
			$($(_config.provId + " option")[1]).attr("selected",'true');
			if(this.provCode) {
				self._setSelectCode(_config.provId,this.provCode);
			}
		}
		
		_config.provCallBack && $.isFunction(_config.provCallBack) && _config.provCallBack();
	},
	/*
	 * 渲染市对应的市数据
	 * @method _cityFunc {private}
	 */
	_cityFunc: function(){
		var self = this,
			_config = self.config;

		var provIndex = $(_config.provId).get(0).selectedIndex,   // 获取省份的索引index
			elemHtml = '',
			prov,
			citys,
			provOpt;
		
		provOpt = $($(_config.provId + ' option')[0]).attr("value");
		var opthtml = '<option value="-1" data-name="请选择" selected>请选择</option>';

		// 如果省有请选择的话 那么相应的省要减一
		if(provOpt == -1) {
			provIndex--;
		}
		$(_config.cityId).empty();
		$(_config.areaId).empty();
		/*
		 * 如果选择的是 第一项 "请选择" 的话 那么省市区的value都为-1
		 * 并且默认选中市 区的第一项 请选择option
		 */
		if(provIndex < 0){
			$(_config.provId).attr("data-code",'-1');
			$(_config.cityId).attr("data-code",'-1');
			$(_config.areaId).attr("data-code",'-1');

			$(_config.cityId).html(opthtml);
			$(_config.areaId).html(opthtml);
			return;
		}
		prov = _config.data[provIndex];
		citys = prov[2];
		elemHtml += '<option value="-1" data-name="请选择">请选择</option>';
		for(var m = 0; m < citys.length; m++) {
			var oneCity = citys[m];
			elemHtml += "<option value='"+oneCity[0]+"' data-name='"+oneCity[1]+"'>"+oneCity[1]+"</option>";
		}
		$(_config.cityId).html(elemHtml);

		if(_config.isSelect) {
			$($(_config.cityId + " option")[1]).attr("selected",'true');
			if(this.cityCode) {
				self._setSelectCode(_config.cityId,this.cityCode);
			}
		}
		self._selectChangeCode();

		_config.cityCallBack && $.isFunction(_config.cityCallBack) && _config.cityCallBack();
	},
	/*
	 * 渲染区对应的区数据
	 * @method _areaFunc {private}
	 */
	_areaFunc: function(){
		var self = this,
			_config = self.config;

		var	provOpt = $($(_config.provId + ' option')[0]).attr("value"),
			cityOpt = $($(_config.cityId + ' option')[0]).attr("value");

		var provIndex = $(_config.provId).get(0).selectedIndex,
			cityIndex = $(_config.cityId).get(0).selectedIndex,
			elemHtml = "",
			prov,
			citys,
			areas,
			oneCity;
		var opthtml = '<option value="-1" data-name="请选择" selected>请选择</option>';
		
		/*
		 * 如果省选择了 "请选择了" 那么省和市自动切换到请选择 那么相应的索引index-1
		 * 如果省没有选择 "请选择"，市选择了 "请选择" 那么市的索引index - 1
		 */
		if(provOpt == -1) {
			provIndex--;
			cityIndex--;

		}else if(provOpt != -1 && cityOpt == -1) {
			cityIndex--;
		}
		
		if(provIndex < 0 || cityIndex < 0){
			$(_config.cityId).attr("data-code",'-1');
			$(_config.areaId).attr("data-code",'-1');
			$(_config.areaId).html(opthtml);
			return;
		};
		prov = _config.data[provIndex];
		citys = prov[2];
		oneCity = citys[cityIndex];
		areas = oneCity[2];

		elemHtml += '<option value="-1" data-name="请选择">请选择</option>';
		if(areas != null) {
			for(var n = 0; n < areas.length; n++) {
				var oneArea = areas[n];
				elemHtml += "<option value='"+oneArea[0]+"' data-name='"+oneArea[1]+"'>"+oneArea[1]+"</option>";
			}
			
		}
		$(_config.areaId).html(elemHtml);
		
		if(_config.isSelect) {
			$($(_config.areaId + " option")[1]).attr("selected",'true');
			if(this.areaCode) {
				self._setSelectCode(_config.areaId,this.areaCode);
			}
		}
		self._selectChangeCode();

		_config.areaCallBack && $.isFunction(_config.areaCallBack) && _config.areaCallBack();
	},
	/*
	 * 渲染街道对应的区数据(目前只有北京街道的数据)
	 * @method _areaFunc {private}
	 */
	_streetFunc : function(){
		var self = this,
			_config = self.config;

		var	provOpt = $($(_config.provId + ' option')[0]).attr("value"),
			cityOpt = $($(_config.cityId + ' option')[0]).attr("value"),
			areaOpt = $($(_config.areaId + ' option')[0]).attr("value");

		var provIndex = $(_config.provId).get(0).selectedIndex,
			cityIndex = $(_config.cityId).get(0).selectedIndex,
			areaIndex = $(_config.areaId).get(0).selectedIndex,
			elemHtml = "",
			prov,
			citys,
			areas,
			oneCity,
			oneArea,//根据index从areas里面找到需要的area信息
			streets;
		var opthtml = '<option value="-1" data-name="请选择" selected>请选择</option>';
		
		/*
		 * 如果省选择了 "请选择" 那么省和市和街道自动切换到请选择 那么相应的索引index-1
		 * 如果省没有选择 "请选择"，市选择了 "请选择" 那么市的索引index - 1
		 * 如果
		 */
		if(provOpt == -1) {
			provIndex--;
			cityIndex--;
			areaIndex--;

		}else if(provOpt != -1 && cityOpt == -1) {
			cityIndex--;
			areaIndex--;
		}
		
		if(provIndex < 0 || cityIndex < 0){
			$(_config.cityId).attr("data-code",'-1');
			$(_config.areaId).attr("data-code",'-1');
			$(_config.streetId).attr("data-code",'-1');
			$(_config.streetId).html(opthtml);
			return;
		};
		prov = _config.data[provIndex];
		citys = prov[2];
		oneCity = citys[cityIndex];
		areas = oneCity[2];
		//这里显示街道信息
		oneArea = areas[areaIndex];
		streets = oneArea[2];

		elemHtml += '<option value="-1" data-name="请选择">请选择</option>';
		if(streets != null) {
			for(var n = 0; n < streets.length; n++) {
				var oneStreet = streets[n];
				elemHtml += "<option value='"+oneStreet[0]+"' data-name='"+oneStreet[1]+"'>"+oneStreet[1]+"</option>";
			}
			
		}
		$(_config.streetId).html(elemHtml);
		
		if(_config.isSelect) {
			$($(_config.areaId + " option")[1]).attr("selected",'true');
			if(this.areaCode) {
				self._setSelectCode(_config.areaId,this.areaCode);
			}
		}
		self._selectChangeCode();

		_config.areaCallBack && $.isFunction(_config.areaCallBack) && _config.areaCallBack();
	},
	
	
	 /*
     * 根据change事件来改变省市区select下拉框的data-code的值 
     * @method _changeSetCode private
     * @param selectId 省市区select dom节点
     */
    _changeSetCode: function(selectId){
    	var self = this,
    		_config = self.config;
    	var idx = selectId.selectedIndex, //获取选中的option的索引
            option,
            value,
			dataValue = $(_config.provId).val();
		var html = "<option data-name='请选择' value='-1' selected>请选择</option>",
			provOpt = $($(_config.provId + ' option')[0]).attr("value");

        if(idx > -1) {
			if(dataValue == -1) {
				if(provOpt != "-1") {
					$(_config.provId).prepend(html);
					$(_config.cityId).prepend(html);
					$(_config.areaId).prepend(html);
				}
				$(_config.provId).attr("data-code",'');
				$(_config.cityId).attr("data-code",'');
				$(_config.areaId).attr("data-code",'');
			}else {
				option = selectId.options[idx];  //获取选中的option元素
				value = $(option).val();
				$(selectId).attr("data-code",value);
			}
        }
    },
	_selectChangeCode: function(){
		var self = this,
			_config = self.config;

		// 省change事件时候 省select下拉框data-code也要变化
		var provId = document.getElementById(this.provid);
		self._changeSetCode(provId);

		// 市change事件时候 市select下拉框data-code也要变化
		var cityId = document.getElementById(this.cityid);
		self._changeSetCode(cityId);
		
		// 区change事件时候 区select下拉框data-code也要变化
		var areaId = document.getElementById(this.areaid);
		self._changeSetCode(areaId);
		
		// 街道change事件时候 街道select下拉框data-code也要变化
		var streetId = document.getElementById(this.streetid);
		self._changeSetCode(streetId);
	},
	 /*
     * 根据下拉框select属性data-code值 来初始化省下拉框的值
     * @method _setSelectCode {private}
     * @param selectId 下拉框的Id
     */
    _setSelectCode: function(selectId,code){
    	var self = this,
    		_config = self.config;
    		
    	var optionElems = $(selectId + " option");
		
    	$(optionElems).each(function(index,item){
    		var curCode = $(item).val();
    		if(curCode == code) {
    			$(item).attr("selected",'true');
				$(selectId).attr("data-code",curCode);
    			return;
    		}
    	});
    }
 };