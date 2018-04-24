/*!
 * PMSDict.
 */
var PMSDict = function() {
	this.url = getRootPath() + "/dispatcher.action";
	this.manager = "dictManager";
	this.method = "findDictByName";
	this.dicts = new Map();
	this.listMap = new Map();
	this.reversedDicts = new Map();
};
/**
 * init cache the dicts at client.
 */
// PMSDict.prototype.init = function() {
// var putInMap = function(data) {
// for(var attr in data) {
// var dictArr = data[attr];
// var dictMap = new Map();
// for(var i=0; i<dictArr.length; i++) {
// var kv = dictArr[i];
// dictMap.put(kv.dictCode, kv.dictText);
// }
// $dict.dicts.put(attr, dictMap);
// $dict.listMap.put(attr, dictArr);
// }
// };
// var reqObj = new PMSRequestObject("dictManager", "getCachedDicts", [ ]);
// ajax(this.url, "requestString="+reqObj.toJsonString(), function(data) {
// var dicts = $.fromJSON(data.data);
// putInMap(dicts);
// });
// };
/**
 * add a dict to the dict map.
 * 
 * @param {Object}
 *            dictName
 * @param {Object}
 *            dict
 */
// PMSDict.prototype.add = function(dictName, dict) {
// this.dicts.put(dictName, dict);
// };
/**
 * get the dict as a map. usage: var dicts = $dict.getDictByName('gender');
 * alert(dicts.get('male')); if not exists, return null.
 * 
 * @param {Object}
 *            dictName
 */
PMSDict.prototype.getDictByName = function(dictName) {
	var dict = this.dicts.get(dictName);
	if (!dict || dict.size() <= 0) {
		this.cacheDict(dictName);
		dict = this.dicts.get(dictName);
		// dict = $dict.getDictAjax(dictName);
		// if(dict && dict.size() > 0) {
		// $dict.add(dictName, dict);
		// }
	}
	return dict;
};
/**
 * Get the dict as an Array of Object
 * 
 * @param dictName
 * @returns
 */
PMSDict.prototype.getDictList = function(dictName) {
	var dict = this.listMap.get(dictName);
	if (dict == null || dict.length <= 0) {
		this.cacheDict(dictName);
		dict = this.listMap.get(dictName);
		// dict = $dict.getListAjax(dictName);
	}
	return dict;
};
/**
 * get a dict item. usage: var dictItem = $dict.getDictText('gender', 'male');
 * alert(dictItem); //i18n:'${gender.male}'. if not exists, return null.
 * 
 * @param {Object}
 *            dictName
 * @param {Object}
 *            dictCode
 */
PMSDict.prototype.getDictText = function(dictName, dictCode) {
	var value = null;
	var dict = this.getDictByName(dictName);
	if (dict && dict.size() > 0) {
		value = dict.get(dictCode);
	}
	return value;
};
/**
 * get dictCode by dictText.
 * 
 * @param {Object}
 *            dictName
 * @param {Object}
 *            dictText
 */
PMSDict.prototype.getDictCode = function(dictName, dictText) {
	// var code = null;
	// var dict = this.getDictByName(dictName);
	// if(dict && dict.size() > 0) {
	// for (var i=0; i<dict.size(); i++) {
	// var entry = dict.getEntry(i);
	// if (entry.value == dictText) {
	// code = entry.key;
	// break;
	// }
	// }
	// }
	// return code;
	var dict = this.reversedDicts.get(dictName);
	if (dict==null || (!dict && dict.size() <= 0)) {
		this.cacheDict(dictName);
	}
	return this.reversedDicts.get(dictName).get(dictText);
};
/**
 * get dict objects ajax by dictName.
 * 
 * @param {Object}
 *            dictName
 * @Deprecated
 */
// PMSDict.prototype.getListAjax = function(dictName) {
// var dict = null;
// var reqObj = new PMSRequestObject("dictManager","findDictByName", [ dictName
// ]);
// $.ajax({
// dataType:"json",
// type:"post",
// timeout:300000,
// async: false,
// url: this.url,
// data: "requestString="+reqObj.toJsonString(),
// success: function(data, textStatus, XMLHttpRequest) {
// var tmp = data.data;
// dict = $.fromJSON(tmp);
// $dict.listMap.put(dictName, dict);
// }
// });
// return dict;
// };
/**
 * get the dict by ajax.
 * 
 * @param {Object}
 *            dictName
 * @Deprecated
 */
// PMSDict.prototype.getDictAjax = function(dictName) {
// var dict = null;
// var reqObj = new PMSRequestObject("dictManager","findDictByName", [ dictName
// ]);
// $.ajax({
// dataType:"json",
// type:"post",
// timeout:300000,
// async: false,
// url: this.url,
// data: "requestString="+reqObj.toJsonString(),
// success: function(data, textStatus, XMLHttpRequest) {
// var tmp = data.data;
// var list = $.fromJSON(tmp);
// if(list && list.length > 0) {
// dict = new Map();
// for(var i=0; i<list.length; i++) {
// var item = list[i];
// dict.put(item.dictCode, item.dictText);
// }
// }
// }
// });
// return dict;
// };
PMSDict.prototype.cacheDict = function(dictName) {
	var reqObj = new PMSRequestObject(this.manager, this.method, [ dictName ]);
	var outer = this;
	$.ajax({
		dataType : "json",
		type : "post",
		timeout : 300000,
		async : false,
		url : this.url,
		data : "requestString=" + reqObj.toJsonString(),
		success : function(data, textStatus, jsXHR) {
			var list = $.fromJSON(data.data);
			if (list != null) {
				outer.listMap.put(dictName, list);
				if (list && list.length > 0) {
					var dict = new Map();
					var reversedDict = new Map();
					for ( var i = 0; i < list.length; i++) {
						var item = list[i];
						dict.put(item.dictCode, item.dictText);
						reversedDict.put(item.dictText, item.dictCode);
					}
					outer.dicts.put(dictName, dict);
					outer.reversedDicts.put(dictName, reversedDict);
				}
			} else {
				// TODO: If the dict doesn't exist, we should notice the user.
				// $$.onServerInvokeFailed(jsXHR, data.message);
			}
		}
	});
};
/**
 * get dict by ajax.
 * 
 * @param {Object}
 *            dictName
 * @param {Object}
 *            callback
 */
PMSDict.prototype.getDict = function(dictName, callback) {
	if (!dictName)
		return null;
	var dict = this.getDictList(dictName);
	if (dict && dict.length > 0) {
		callback(dict);
	}
};

var $dict = top.$dict;
if (typeof $dict == 'undefined') {
	$dict = new PMSDict();
	// $dict.init();
};
