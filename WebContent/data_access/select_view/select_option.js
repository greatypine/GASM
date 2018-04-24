var win;

/**
 * 选择城市,创建一个城市列表并显示
 * @param {Object} callback
 *
 */
function showStoreWin(callback){
    win = new checkWin(callback,"门店选择","select_view/select_store.html");
    win.show();
}

function showTinyWin(callback,house_type){
    win = new checkWin(callback,"选择","select_view/select_tiny_village.html?house_type="+house_type);
    win.show();
}

function showBuilding(callback,tinyvillage_id){
    win = new checkWin(callback,"选择","select_view/select_building.html?tinyvillage_id="+tinyvillage_id);
    win.show();
}

function showTownWin(callback){
    win = new checkWin(callback,"街道选择","select_view/select_town.html");
    win.show();
}

function showVillageWin(callback){
    win = new checkWin(callback,"社区选择","select_view/select_village.html");
    win.show();
}

function showSelectCustomer(callback,house_id){
    win = new checkWHWin(callback,"客户选择","select_view/select_customer.html?house_id="+house_id,400,400);
    win.show();
}

function showSelectHousePic(callback,obj){
    win = new checkWHWin(callback,"选择图片","select_view/select_house_pic.html?tv_id="+obj.tv_id+"&fname="+obj.fname,400,500);
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
