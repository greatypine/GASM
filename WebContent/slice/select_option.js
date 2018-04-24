var win;

function showTinyWin(callback){
    win = new checkWin(callback,"小区选择","tiny_village_list.html");
    win.show();
}

function showBuildingWin(callback){
    win = new checkWin(callback,"楼房选择","building_list.html");
    win.show();
}

function showVillageWin(callback,town_id){
    win = new checkWin(callback,"社区选择","village_list.html?town_id="+town_id);
    win.show();
}

/**
 *
 * @param {Object} callback 	回调函数
 * 健康屋门店callback
 * 返回值json
 */
var checkWHWin = function(callback,title,path,width,height){
    this.win = $("<div style='overflow-y: hidden'></div>");
    var _this = this;
    var initWin = function(){
        _this.win.html('<iframe name="selectWin" frameborder="0" width="100%" height="100%" src="'+path+'" scrolling="yes"></iframe>');
        _this.win.dialog({
            bgiframe: true,
            title:title,
            autoOpen:false,
            width:_this.width,
            height:_this.height,
            buttons : {
                "确定": function(){
                    window.frames["selectWin"].doSubmit();
                    _this.win.remove();
                },
                "取消":function(){
                    _this.hide();
                    _this.win.remove();
                }
            },
            modal:true
        });
    };
    this.width = width;
    this.height = height;
    this.callBack = function(json){
        if (callback && typeof(callback) == 'function') {
            callback(json);
        }
    };

    this.show = function(){
        _this.win.dialog("open");
    };

    this.hide = function(){
        if(_this.onSubmitHandler){
            _this.onSubmitHandler();
        }
        _this.win.dialog("close");
    };
    initWin();
}

/**
 *
 * @param {Object} callback 	回调函数
 * 健康屋门店callback
 * 返回值json
 */
var checkWin = function(callback,title,path){
    this.win = $("<div style='overflow-y: hidden'></div>");
    var _this = this;
    var initWin = function(){
        _this.win.html('<iframe name="selectWin" frameborder="0" width="100%" height="100%" src="'+path+'" scrolling="no"></iframe>');
        _this.win.dialog({
            bgiframe: true,
            title:title,
            autoOpen:false,
            width:_this.width,
            height:_this.height,
            buttons : {
                "确定": function(){
                    window.frames["selectWin"].doSubmit();
                    _this.win.remove();
                },
                "取消":function(){
                    _this.hide();
                    _this.win.remove();
                }
            },
            modal:true
        });
    };
    this.width = 900;
    this.height = 710;
    this.callBack = function(json){
        if (callback && typeof(callback) == 'function') {
            callback(json);
        }
    };

    this.show = function(){
        _this.win.dialog("open");
    };

    this.hide = function(){
        if(_this.onSubmitHandler){
            _this.onSubmitHandler();
        }
        _this.win.dialog("close");
    };
    initWin();
}
