/**
 * 该方法用于各个业务模块在点击保存按钮时来将不为空检验去掉
 * 以为现在框架中提供的表单校验都需要以pmsForm为class，因此知道样式为pmsForm的form
 * 该方法暂时未考虑一个界面有多个form需要校验的情况
 */
function ignoreNullAttrFunction(){
    $(".pmsForm").find("[validate]").each(function(i){
        var validateStr = $(this).attr("validate");
        if (validateStr.indexOf("required:true") > -1) {
            removeValidateMetaData($(this).attr("id"));
            validateStr = validateStr.replace("required:true", "required:false");
            $(this).attr("validate", validateStr);
        }
    });
}

/**
 *
 * 该方法对应着ignoreNullAttrFunction方法，在点击保存按钮之后需要将form的校验信息还原
 */
function initValidateForm(){
    $(".pmsForm").find("[validate]").each(function(i){
        var validateStr = $(this).attr("validate");
        if (validateStr.indexOf("required:false") > -1) {
            removeValidateMetaData($(this).attr("id"));
            validateStr = validateStr.replace("required:false", "required:true");
            $(this).attr("validate", validateStr);
        }
    });
}
