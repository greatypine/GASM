/**
 * @author zhongyouyuan
 */
/**
 * 搜索投标人信息.
 */
var onSearchBidders = function() {
	$$.executeSearch('biddersSearchQuery', 'conditionsBidDiv'); //queryid, the params div id.			
}

/**
 * 搜索供应商信息.
 */
var onSearchSupplier = function() {
	$$.executeSearch('suppliersQuery', 'conditionsSupDiv'); //queryid, the params div id.			
}

/**
 * 显示投标人信息列表.
 */
function searchBidders() {
	setNullSupBidValue("formBid");
	onSearchBidders();
	doEditCssType();
	$("#biddersInfoId").dialog(
		{
			bgiframe: true,
			title: '投标人查询',
			width: 805,
			height: 400,
			modal: true,
			buttons: 
				{
					"${form.ui.ok}": function() {
						var selects = $$.getSelectedObj("biddersSearchQuery");
						if (selects.length > 0) {
							var ugInfo = 
								{
									applyId: selects[0][0],
									applyCode: selects[0][1],
									applyName: selects[0][2],
								};
							$("#bidName").val(ugInfo.applyName);
							$("#bidCode").val(ugInfo.applyId);
							$("#biddersInfoId").dialog('close');
						}
						
					},
					"${query.ui.cancel}": function() {
						$("#biddersInfoId").dialog('close');
					}
				}
		});
}

/**
 * 解决列表Css样式不一致的问题.
 */
function doEditCssType() {
	var table = $('.display');
	table.each(function() {
		var th1 = $(this).find('th:first');
		if (th1.find("input[type=checkbox]").length > 0) {
			th1.css('width', '1%');
		}
		$(this).find('th').each(function() {
			$(this).css('border', '1px solid #999999').css('background', '#D1D6F0');
			;
		});
	});
}

/**
 * 供应商信息列表显示.
 */
function searchSupplier() {
	setNullSupBidValue("formSup");
	onSearchSupplier();
	doEditCssType();
	$("#supplierInfoId").dialog(
		{
			bgiframe: true,
			title: '供应商查询',
			width: 805,
			height: 400,
			modal: true,
			buttons: 
				{
					"${form.ui.ok}": function() {
						var selects = $$.getSelectedObj("suppliersQuery");
						if (selects.length > 0) {
							var ugInfo = 
								{
									applyId: selects[0][0],
									applyCode: selects[0][1],
									applyName: selects[0][2],
								};
							$("#supName").val(ugInfo.applyName);
							$("#supCode").val(ugInfo.applyId);
							$("#supplierInfoId").dialog('close');
						}
						
					},
					"${query.ui.cancel}": function() {
						$("#supplierInfoId").dialog('close');
					}
				}
		});
}

/**
 * 清空搜索条件的数据.
 *
 * @param {Object} form
 */
var setNullSupBidValue = function(form) {
	var formObj = $("#" + form);
	if (form == "formBid") {
		var bidderNameObj = $(formObj).find(":input[name='bidderName']");
		var bidderCodeObj = $(formObj).find(":input[name='bidderCode']");
		bidderNameObj.val("");
		bidderCodeObj.val("");
	}
	else {
		var supplierNameObj = $(formObj).find(":input[name='supplierName']");
		var supplierCodeObj = $(formObj).find(":input[name='supplierCode']");
		supplierNameObj.val("");
		supplierCodeObj.val("");
	}
}
