importCss(baseUrl + "/scripts/lib/jquery/ztree/zTreeStyle/zTreeStyle.css");
importJs(baseUrl + "/scripts/lib/jquery/ztree/jquery-ztree-2.4.js");
importJs(baseUrl + "/bizbase/js/refDialog.js");
importJs(baseUrl + "/bizbase/js/bidOrgTree.js");
importJs(baseUrl + "/forecast/js/lib/interface/bizbaseInterface.js");
/*
 * @function : 初始化查询树形数据所需要的页面元素
 * @author   : WangDong
 * @date     : 2012-05-21
 */
var Tree = {
    initSearchTreeElement: function(_cfg){
        var valid = _cfg.valid;
        var validate = '';
        if (valid == true) {
            validate = 'validate="{required:true}"';
        }
        eval('var fun=' + _cfg.fun);
		if (_cfg.parent) {
			_cfg.parent.append('<input type="hidden" class="inputtext" name="' + _cfg.code + '" id="' + _cfg.code + '"/>').append('<input type="text" class="inputtext_dis" name="' + _cfg.name + '" ' + validate + ' id="' + _cfg.name + '" readonly="readonly"/>').append('<input type="button" id="' + _cfg.btnId + '" class="buttonu" value="搜索"/>');
			$('#' + _cfg.btnId).bind('click', function(){
				fun(_cfg.params);
			});
		}else{
			alert('The _cfg.parent can not be null!');
		}
    },
	initShowElement:function(_cfg){
		if (_cfg.parent) {
			_cfg.parent.append('<input type="text" class="inputtext_dis" name="' + _cfg.name + '" id="' + _cfg.name + '"/>');
		}else{
			alert('The _cfg.parent can not be null!');
		}
	}
};
/*
 *@function      : 初始化树形元素所需要的参数
 *@param  btnId  : 搜索按钮id用于对按钮的操作
 *@param  code   : 用于填充返回信息code的元素id
 *@param  name   : 用于填充返回信息name的元素id
 *@param  parent : 树形元素所要添加的父元素
 *@param  valid  : 该填充字段是否是必须的
 *@param  fun    : 调用通用接口中用于显示树形div的方法名称
 *@param  params : 要传给上接口方法的参数
 *@author        : WangDong
 *@date          : 2012-05-21
 */
//$(function(){
//    Tree.initSearchTreeElement({
//        btnId: 'btnId',
//        code: 'code',
//        name: 'name',
//        parent: $('#tdtree'),
//        valid: true,
//        fun: 'getBidOrgTreeInterface',
//        params: {
//            inputId: 'code',
//            inputName: 'name'
//        }
//    });
//})
