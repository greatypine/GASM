/*
* @methodName     : getTenderInterface
* @methodParam    : Object
* @methodReturn   : String
* @author         : ruanqingfeng
* @desc           : 获取招标人函数
* @interfaceDesc  : "基础模块"的接口函数
* @ref Javascript : bizbase/js/refDialog.js
* @for example    : getTenderInterface({inputId:"文本框Id"});
*/
function getTenderInterface(_cfg,_this){
	openDialogWithJson(function(_result){
		var result = $.fromJSON(_result)[0];
		
		if(_cfg.inputId!=null){
			$("#"+_cfg.inputId).val(result.id);
		}
		
		if(_cfg.inputName!=null){
			$("#"+_cfg.inputName).val(result.name);
		}
		if($("#"+_cfg.inputName).val()!=""){
			$(_this).next().remove();
		}
		
	},"1","radio")
}

/*
* @methodName     : getBidOrgTreeInterface
* @methodParam    : Object
* @methodReturn   : String
* @author         : ruanqingfeng
* @desc           : 获取招标人函数
* @interfaceDesc  : "基础模块"的接口函数
* @ref Javascript : bizbase/js/bidOrgTree.js
* @for example    : getBidOrgTreeInterface({inputId:"文本框Id"});
*/
function getBidOrgTreeInterface(_cfg,_this){
	var codeInput = $("#"+_cfg.inputId);
	getBidOrgTree(function(name,code){
		if(_cfg.inputId!=null){
			codeInput.val(code);
		}
		if(_cfg.inputName!=null){
			doManager("purStruOrgManager","getObjectByCode",code,function(_response){
				if(_response.result){
					var psorg = $.fromJSON(_response.data);	
					$("#"+_cfg.inputName).val(psorg.name);
				}
				
			});
		}
		if($("#"+_cfg.inputName).val()!=""){
			$(_this).next().remove();
		}
		
	},"radio",codeInput.val(),"bid")
}

/*
* @methodName     : getBidOrgTreeInterfaceForTender
* @methodParam    : Object
* @methodReturn   : String
* @author         : ruanqingfeng
* @desc           : 获取特定项目（标段/标包）下的投标人函数
* @interfaceDesc  : "基础模块"的接口函数
* @ref Javascript : bizbase/js/bidOrgTree.js
* @for example    : getBidOrgTreeInterface({inputId:"文本框Id"});
*/
function getBidOrgTreeInterfaceForTender(_cfg,_this){
	var codeInput = $("#"+_cfg.inputId);
	getBidOrgTree(function(name,code){
		if(_cfg.inputId!=null){
			codeInput.val(code);
		}
		if(_cfg.inputName!=null){
			doManager("purStruOrgManager","getObjectByCode",code,function(_response){
				if(_response.result){
					var psorg = $.fromJSON(_response.data);	
					$("#"+_cfg.inputName).val(psorg.name);
				}
				
			});
		}
		if($("#"+_cfg.inputName).val()!=""){
			$(_this).next().remove();
		}
		
	},"radio",codeInput.val(),"bid")
}


/*
* @methodName     : getBidLocalOrgTreeInterface
* @methodParam    : Object
* @methodReturn   : String
* @author         : ruanqingfeng
* @desc           : 获取招标人函数
* @interfaceDesc  : "基础模块"的接口函数
* @ref Javascript : bizbase/js/bidOrgTree.js
* @for example    : getBidLocalOrgTreeInterface({inputId:"文本框Id"});
*/
function getBidLocalOrgTreeInterface(_cfg,_this){
	var codeInput = $("#"+_cfg.inputId);
	
	getBidOrgTree(function(name,code){
		if(_cfg.inputId!=null){
			codeInput.val(code);
		}
		if(_cfg.inputName!=null){
			$("#"+_cfg.inputName).val(name);
		}
		if($("#"+_cfg.inputName).val()!=""){
			$(_this).next().remove();
		}
		
	},"radio",codeInput.val(),"bidForLocalOrg")
}
/*
 * @function : 获取招标管理单位tree
 * @author   : WangDong
 * @date     : 2012-05-23
 */
function getBidManagerTree(_cfg){
	getBidManagerOrg(1,function(code,name){
		$("#" + _cfg.inputId).val(code);
		$("#" + _cfg.inputName).val(name);
	});
}
