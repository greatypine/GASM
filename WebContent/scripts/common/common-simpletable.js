/**
 * jQuery extends.
 */
jQuery.extend({
	/**
	 * Extend jQuery to serialize a object to json string.
	 * 
	 * @param Object,
	 *            support type:
	 *            object,array,string,function,number,boolean,regexp *
	 * @return json string
	 */
	toJSON : function(object) {
		if (object === null) return "null";
		if (!object)
			return null;
		var type = typeof object;
		if ('object' == type) {
			if (Array == object.constructor)
				type = 'array';
			else if (RegExp == object.constructor)
				type = 'regexp';
			else
				type = 'object';
		}
		switch (type) {
		case 'undefined':
		case 'unknown':
			return;
			break;
		case 'function':
		case 'boolean':
		case 'regexp':
			return object.toString();
			break;
		case 'number':
			return isFinite(object) ? object.toString() : 'null';
			break;
		case 'string':
			return '"'
					+ object.replace(/(\\|\")/g, "\\$1").replace(
							/\n|\r|\t/g,
							function() {
								var a = arguments[0];
								return (a == '\n') ? '\\n'
										: (a == '\r') ? '\\r'
												: (a == '\t') ? '\\t' : ""
							}) + '"';
			break;
		case 'object':
			if (object === null)
				return 'null';
			var results = [];
			for ( var property in object) {
				var value = jQuery.toJSON(object[property]);
				if (value !== undefined)
					results.push(jQuery.toJSON(property) + ':' + value);
			}
			return '{' + results.join(',') + '}';
			break;
		case 'array':
			var results = [];
			for ( var i = 0; i < object.length; i++) {
				var value = jQuery.toJSON(object[i]);
				if (value !== undefined)
					results.push(value);
			}
			return '[' + results.join(',') + ']';
			break;
		}
	},
	fromJSON : function(jStr) {
		return (new Function('return ' + jStr))();
	}
});

/*!
 * Simple Table.
 * @author : hymer
 * createTime : 2011-08-12
 */

/**
 * SimpleTable Component.
 * @param {Object} tableId
 * @param {Object} options
 */
var SimpleTable = function(tableId, options) {
	var instance = this;
	instance.table=$('#'+tableId);
	var imagePath = getRootPath()+'/scripts/images/';
	var _default = {
		//some default options.
		scrollX : false,
		adapter : new PMSTableAdapter(),
		url : getRootPath() + "/dispatcher.action",
		param : function() {
			return "";
		},
		autoLoad : true,
		formatters : {
			//default rownum formatter.
			rownum : function(v, obj, instance) {
				return instance.table.find('tbody tr').length+1;
			},
			//default checkbox formatter.
			checkbox : function(v, obj, instance) {
				var ck = $("<input type='checkbox' />");
				ck.val(obj[instance.options.pkcol]);
				ck.data('obj', obj);
				instance.ckcol.push(ck);
				return ck;
			}
		},
		checkMode : 'not-single', //checkbox run mode, single check or not.
		checkboxRenderer : function(cks, ckhead) {},
		listeners : { //all listeners must return a boolean value, determine whether continue.
			oncheck : function(_self) {return true;},
			beforeload : function() {return true;},
			afterload : function(data) {return true;}
		},
		pageSize : 10,
		MAX_PAGESIZE : 999,
		pkcol : 'id',
		paging : true,
		pagingOptions : {
			first : true,
			end : true,
			go : true,
			firstHtml : '<img src="'+imagePath+'sy.png" />',
			lastHtml : '<img src="'+imagePath+'syy.png" />',
			nextHtml : '<img src="'+imagePath+'xyy.png" />',
			endHtml : '<img src="'+imagePath+'wy.png" />'
		},
		sortables : [],
		css : {
			tableClass : 'display',
			odd : 'odd',
			even : 'even',
			pagingDiv : 'simple-table-paging-div',
			pagingSizeDiv : 'simple-table-paging-size-div',
			pagingInfoDiv : 'simple-table-paging-info-div',
			pagingButtonsDiv : 'simple-table-paging-buttons-div',
			pagingTable : 'simple-table-paging-table',
			pagingDisable : 'paing-disabled',
			go : 'go',
			goHover : 'go-hover',
			sortable : 'simple-table-sortable',
			asc : 'simple-table-sort-asc',
			desc : 'simple-table-sort-desc',
			sorting : 'sorting'
		},
		info : {
			pageSizeSelect:'显示{0}条记录',
			pagingInfo : '正显示{0}到{1}条记录 共计:{2}',
			first : '首页',
			last : '上一页',
			next : '下一页',
			end : '尾页',
			go : 'GO',
			emptyTable : '当前列表为空'
		}
	};
	if(instance.table.length <= 0 
		|| instance.table.get(0).tagName.toUpperCase() != 'TABLE') {
		alert("Table not exists: id=" + id);
		return false;
	}
	instance.options = jQuery.extend(true, _default, options); //深度继承
	
	/////////////////////////////////////////////////
	//           Function Definition
	////////////////////////////////////////////////
	
	/**
	 * 获取根路径(eg:http://localhost:8080/bbs)
	 */
	function getRootPath(){
		var cwp=window.document.location.href;
		var pn=window.document.location.pathname;
		var pos=cwp.indexOf(pn);
		var lp=cwp.substring(0,pos);
		var pon=pn.substring(0,pn.substr(1).indexOf('/')+1);
		return(lp+pon);
	}
	
	/**
	 * 初始化：
	 * 1.列表样式
	 * 2.初始分页信息
	 * 3.收集列头信息
	 */
	var init = function(instance) {
		instance.table.addClass(instance.options.css.tableClass);
		instance.table.attr('width', '100%');
		instance.cols = new Array();
		instance.ckhead = null; //colname='checkbox'
		instance.ckcol = new Array();
		if(instance.options.paging) {
			instance.pageinfo = {
				currentPage:1,
				pageSize:instance.options.pageSize
			};
		} else {
			instance.pageinfo = {
				currentPage:1,
				pageSize:instance.options.MAX_PAGESIZE
			};
		}
		gatherHeadInfo(instance); //table header(th)
	}
	/**
	 * 收集头信息
	 */
	var gatherHeadInfo = function(instance) {
		var ths = instance.table.find("th");
		if (ths.length > 0) {
			instance.ckhead = ths.filter(":[colname='checkbox']").find(":input[type='checkbox']");
			for(var i=0; i<ths.length; i++) {
				var col = new Object();
				var th = $(ths[i]);
				col.key = th.attr('colname');
				col.display = th.attr('display') == 'false' ? false : true;
				if(col.display === false) {
					th.hide();
				}
				col.element = th;
				instance.cols.push(col);
			}
		} else {
			buildTableHead(instance);
		}
	}
	
	var buildTableHead = function(instance) {
		var columns_def = instance.options.columns;
		var thead = $("<thead></thead>");
		var tr = $("<tr></tr>");
		for (var name in columns_def) {
			var head = columns_def[name];
			var default_col = {
				header : ' ',
				align : 'left',
				display : true,
				width : '10%'
			};
			var col = jQuery.extend(default_col, head);
			col.key = name;
			var th = $("<th></th>");
			th.attr('colname', name);
			th.attr('width', head.width);
			th.attr('align', head.align);
			if(col.display === false) {
				th.hide();
			}
			th.append(head.header);
			if (name == 'checkbox') {
				if (!head.header) {
					var chh = $('<input type="checkbox" />');
					th.append(chh);
					instance.ckhead = chh;
				} else {
					instance.ckhead = th;
				}
			}
			tr.append(th);
			col.element = th;
			instance.cols.push(col);
		}
		thead.append(tr);
		instance.table.append(thead);
	}
	
	var initParam = function(instance) {
		var param = instance.options.adapter.initParam(instance);
		instance.parameters = param;
	}
	
	var load = function(instance, callback) {
		if (instance.options.listeners.beforeload()) {
			initParam(instance);
			$.ajax({
				dataType:"json",
				type:"post",
				timeout:30000,
				url: instance.options.url,
				data: instance.parameters,
				success: function(data, textStatus, XMLHttpRequest) {
					refresh(instance, data);
					if (typeof instance.options.checkboxRenderer == 'function') {
						instance.options.checkboxRenderer(instance.ckcol, instance.ckhead);
					}
					if(typeof callback == 'function') {
						callback(data, textStatus, XMLHttpRequest);
					}
					instance.options.listeners.afterload(data);
		        }
			});
		}
	}
	
	var refresh = function(instance, response) {
		if(response.result) {
			var respObj = $.fromJSON(response.data);
			instance.respObj = respObj;
			instance.datas = respObj.data;
			resetPageInfo(instance, respObj.pageinfo);
			rebuild(instance);
		} else {
			// do some thing, eg: alert error info.
			alert(response.message);
		}
	}
	
	var rebuild = function(instance){
		empty(instance);
		build(instance);
	}
	
	var resetPageInfo = function(instance, pageinfo) {
		instance.pageinfo.currentPage = pageinfo.currentPage;
		instance.pageinfo.pageSize = pageinfo.recordsPerPage;
		instance.pageinfo.totalPages = Math.ceil(pageinfo.totalRecords / pageinfo.recordsPerPage);
		instance.pageinfo.totalRecords = pageinfo.totalRecords;
	}
	
	/**
	 * build table with available data.
	 * @param {Object} instance
	 */
	var build = function(instance) {
		if(instance.table.attr("isbuilded")) return ;
		instance.ckcol = [];
		if(instance.datas.length > 0) {
			instance.isEmpty = false;
			instance.table.attr('width', '');
			var formatters = instance.options.formatters;
			var columns_def = instance.options.columns;
			for(var i=0; i<instance.datas.length; i++) {
				var obj = instance.datas[i];
				var tr = $("<tr></tr>");
				for(var j=0; j<instance.cols.length; j++) {
					var col = instance.cols[j];
					if (col.display === false) {
						continue;
					}
					var name = col.key;
					var td = $("<td></td>");
					var td_align = columns_def[name].align;
					if(td_align) {
						td.css('text-align', td_align);
					}
					if (instance.sortinfo && col.key == instance.sortinfo.key) {
						td.addClass(instance.options.css.sorting);
					}
					var val = obj[name];
					try {
						if (typeof formatters[name] == 'function') {
							val = formatters[name](val, obj, instance);
						}
					} catch (error) {
						val = obj[name];
						//do nothing.
					}
					td.append(val);
					tr.append(td);
				}
				if(Math.round(i%2) == 0) {
					tr.addClass(instance.options.css.odd);
				} else {
					tr.addClass(instance.options.css.even);
				}
				instance.table.append(tr);
			}
		} else {
			instance.isEmpty = true;
			instance.table.attr('width', '100%');
			buildEmptyTable(instance);
		}
		addEventListener(instance);
		buildPaging(instance);
		if (instance.options.scrollX && instance.isScrollX !== true) {
			var displaydiv = $("<div class='table-container'></div>");
			instance.table.wrap(displaydiv);
			instance.isScrollX = true;
		}
		instance.table.attr("isbuilded",true);
	}
	//数据为空时显示
	var buildEmptyTable = function(instance) {
		var tr = $('<tr/>');
		var td = $('<td/>');
		td.attr('colspan', instance.cols.length);
		td.css('text-align', 'center');
		td.html(instance.options.info.emptyTable);
		tr.append(td);
		instance.table.append(tr);
	}
	//动态设置pageSize
	var buildSelectHtml = function(instance, pageSizeDiv) {
		var currPageSize = instance.options.pageSize;
		var pageSizeHtml = instance.options.info.pageSizeSelect;
		if (pageSizeHtml.indexOf('{0}') >= 0) {
			var words1 = pageSizeHtml.substring(0,pageSizeHtml.indexOf('{0}'));
			var words2 = pageSizeHtml.substring(pageSizeHtml.indexOf('{0}')+3);
			var pageSize1 = $('<font>'+words1+'</font>');
			var pageSize2 = $('<select></select>');
			var option1 = $('<option></option>');
			var option2 = $('<option></option>');
			var option3 = $('<option></option>');
			var option4 = $('<option></option>');
			var option5 = $('<option></option>');
			if (currPageSize <= 5) {
				option1.val(currPageSize).html(currPageSize).attr("selected","selected");
				option2.val(currPageSize * 2).html(currPageSize *2);
			} else {
				option1.val(currPageSize / 2).html(currPageSize / 2);
				option2.val(currPageSize).html(currPageSize).attr("selected","selected");
			}
			option3.val(currPageSize * 3).html(currPageSize * 3);
			option4.val(currPageSize * 5).html(currPageSize * 5);
			option5.val(currPageSize * 10).html(currPageSize * 10);
			pageSize2.append(option1);
			pageSize2.append(option2);
			pageSize2.append(option3);
			pageSize2.append(option4);
			pageSize2.append(option5);
			pageSize2.change(function(v) {
				var newPageSize = $(this).children('option:selected').val();
				instance.options.pageSize = newPageSize;
				instance.doSearch();
			});
			var pageSize3 = $('<font>'+words2+'</font>');
			pageSizeDiv.append(pageSize1);
			pageSizeDiv.append(pageSize2);
			pageSizeDiv.append(pageSize3);
		}
	}
	//显示当前分页信息
	var buildPageHtml = function(instance, div) {
		var pagingInfo = instance.options.info.pagingInfo;
		if (pagingInfo.length > 0) {
			if (instance.pageinfo.totalRecords == 0) {
				pagingInfo = pagingInfo.replace('{0}', 0);
				pagingInfo = pagingInfo.replace('{1}', 0);
				pagingInfo = pagingInfo.replace('{2}', 0);
			} else {
				pagingInfo = pagingInfo.replace('{0}', 1+(instance.pageinfo.currentPage-1)*instance.options.pageSize);
				pagingInfo = pagingInfo.replace('{1}', (instance.pageinfo.currentPage-1)*instance.options.pageSize + instance.datas.length);
				pagingInfo = pagingInfo.replace('{2}', instance.pageinfo.totalRecords);
			}
			div.html(pagingInfo);
		}
	}
	
	var buildPaging = function(instance) {
		if(instance.options.paging) {
			var div, infoDiv, first, last, next, end, input, go;
			if(instance.paging == null) {
				div = $('<div></div>');
				var pageSizeDiv = $('<div></div>');
				infoDiv = $('<div></div>');
				var pagingDiv = $('<div></div>');
				div.addClass(instance.options.css.pagingDiv);
				pageSizeDiv.addClass(instance.options.css.pagingSizeDiv);
				infoDiv.addClass(instance.options.css.pagingInfoDiv);
				pagingDiv.addClass(instance.options.css.pagingButtonsDiv);
				
				buildSelectHtml(instance, pageSizeDiv); //select the pageSize.
				buildPageHtml(instance, infoDiv); //display info.
				
				var paging_table = $('<table></table>');
				var paging_tr = $('<tr></tr>');
				var first_td = $("<td></td>");
				var last_td = $("<td></td>");
				var next_td = $("<td></td>");
				var end_td = $("<td></td>");
				var input_td = $("<td></td>");
				var go_td = $("<td></td>");
				first = $(instance.options.pagingOptions.firstHtml);
				last = $(instance.options.pagingOptions.lastHtml);
				next = $(instance.options.pagingOptions.nextHtml);
				end = $(instance.options.pagingOptions.endHtml);
				input = $('<input type="text" />')
				go = $('<span/>');
				
//				first.html(instance.options.info.first);
//				last.html(instance.options.info.last);
//				next.html(instance.options.info.next);
//				end.html(instance.options.info.end);
				input.val(instance.pageinfo.currentPage);
				input.attr('size', 2);
//				input.attr('maxlength', 2);
				go.html(instance.options.info.go);
				
				first.addClass('first');
				last.addClass('last');
				next.addClass('next');
				end.addClass('end');
				input.addClass('input');
				go.addClass('go');
				
				first_td.append(first);
				last_td.append(last);
				next_td.append(next);
				end_td.append(end);
				input_td.append(input);
				go_td.append(go);
				
				paging_tr.append(first_td);
				paging_tr.append(last_td);
				paging_tr.append(next_td);
				paging_tr.append(end_td);
				paging_tr.append(input_td);
				paging_tr.append(go_td);
				paging_table.append(paging_tr);
				paging_table.addClass(instance.options.css.pagingTable);
				pagingDiv.append(paging_table);
				
				div.append(pageSizeDiv);
				div.append(infoDiv);
				div.append(pagingDiv);
				instance.table.after(div);
				
				first.click(function() {
					if(instance.pageinfo.currentPage!=1) {
						instance.pageinfo.currentPage=1;
						load(instance);
					}
				});
				last.click(function() {
					if(instance.pageinfo.currentPage > 1) {
						instance.pageinfo.currentPage --;
						load(instance);
					}
				});
				next.click(function() {
					if(instance.pageinfo.totalPages > instance.pageinfo.currentPage) {
						instance.pageinfo.currentPage ++;
						load(instance);
					}
				});
				end.click(function() {
					if(instance.pageinfo.totalPages > instance.pageinfo.currentPage) {
						instance.pageinfo.currentPage = instance.pageinfo.totalPages;
						load(instance);
					}
				});
				go.click(function() {
					var goPage = input.val();
					if((goPage != instance.pageinfo.currentPage) 
							&& (1 <= goPage) 
							&& (goPage <= instance.pageinfo.totalPages)) {
						instance.pageinfo.currentPage = goPage;
						load(instance);
					}
				});
				go.hover(function() {
					$(this).addClass(instance.options.css.goHover);
				},function() {
					$(this).removeClass(instance.options.css.goHover);
				});
				instance.paging = {
					info:infoDiv,
					first:first,
					last:last,
					next:next,
					end:end,
					input:input,
					go:go
				};
			} else {
				first = instance.paging.first;
				last = instance.paging.last;
				next = instance.paging.next;
				end = instance.paging.end;
				input = instance.paging.input;
				go = instance.paging.go;
				infoDiv = instance.paging.info;
				
				input.val(instance.pageinfo.currentPage);
				buildPageHtml(instance, infoDiv);
			}
			
			var disabledCss = instance.options.css.pagingDisable;
			if (instance.pageinfo.totalPages <= 1) {
				first.addClass(disabledCss);
				last.addClass(disabledCss);
				next.addClass(disabledCss);
				end.addClass(disabledCss);
			}
			else {
				if (instance.pageinfo.currentPage === 1) {
					first.addClass(disabledCss);
					last.addClass(disabledCss);
					next.removeClass(disabledCss);
					end.removeClass(disabledCss);
				} else {
					first.removeClass(disabledCss);
					last.removeClass(disabledCss);
					if(instance.pageinfo.currentPage === instance.pageinfo.totalPages) {
						next.addClass(disabledCss);
						end.addClass(disabledCss);
					} else {
						next.removeClass(disabledCss);
						end.removeClass(disabledCss);
					}
				}
			}
			if (instance.options.pagingOptions.first == false) {
				first.hide();
			}
			if (instance.options.pagingOptions.end == false) {
				end.hide();
			}
		}
	}
	
	/**
	 * add the event listener.
	 */
	var addEventListener = function(instance) {
		if(instance.ckhead && instance.ckhead.length==1 && instance.ckcol.length > 0) {
			var head = instance.ckhead;
			var cks = instance.ckcol;
			setCheckStatus(instance, head, cks);
		}
		if (instance.isAlreadyAddSortListener !== true 
				&& instance.options.sortables instanceof Array 
				&& instance.options.sortables.length > 0) {
			var keys = instance.options.sortables;
			for (var i=0; i<keys.length; i++) {
				addSortListener(instance, keys[i]);
			}
			instance.isAlreadyAddSortListener = true;
		}
	}
	
	/**
	 * selectAll & cancelAll.
	 * @param {Object} head
	 * 				the check head
	 * @param {Object} cks
	 * 				the check children
	 */
	var setCheckStatus = function (instance, head, cks) {
		head = $(head);
		cks = $(cks);
		var isSingle = (instance.options.checkMode == 'single');
		if (isSingle) { //single check, disable ckhead.
			head.attr('disabled', 'disabled');
		} else {
			head.click(function() {
				var checked = $(this).attr('checked'); 
				$(cks).each(function(i){
					$(this).attr('checked', checked);
				});
			});
		}
		cks.each(function(i) {
			$(this).click(function(i) {
				if (instance.options.listeners.oncheck(this)) {
					if (isSingle) { //single check.
						var isCheck = $(this).attr('checked');
						if (isCheck) {
							for(var j=0; j<cks.length; j++) {
								$(cks[j]).attr('checked',false);
							}
							$(this).attr('checked',true);
						}
					} else {
						if(head.attr('checked')) {
							for(var j=0; j<cks.length; j++) {
								if(!$(cks[j]).attr('checked')) {
									head.attr('checked', false);
									break;
								}
							}
						} else {
							var flag = true;
							for(var j=0; j<cks.length; j++) {
								if(!$(cks[j]).attr('checked')) {
									flag = false;
									break;
								}
							}
							if(flag) head.attr('checked', true);
						}
					}
				} else {
					$(this).attr('checked', !($(this).attr('checked')));
				}
			});
		});
	}
	/**
	 * 添加表头点击事件
	 * @param {Object} instance
	 * @param {Object} key
	 */
	var addSortListener = function(instance, key) {
		for (var i=0; i<instance.cols.length; i++) {
			var col = instance.cols[i];
			if (key == col.key) {
				col.element.addClass(instance.options.css.sortable)
				.attr('sortable', true)
				.click(function() {
					if (instance.sortinfo && instance.sortinfo.key == key) {
						instance.sortinfo.dir = instance.sortinfo.dir * (-1);
					} else {
						instance.sortinfo = {
							key: key,
							dir: 1
						};
					}
					var th = $(this);
					load(instance, function() {
						//TODO add classes to sorted th&td.
						instance.table.find('tr th').each(function() {
							if ($(this).attr('sortable')) {
								$(this).removeClass(instance.options.css.desc);
								$(this).removeClass(instance.options.css.asc);
								$(this).addClass(instance.options.css.sortable);
							}
						});
						th.removeClass(instance.options.css.sortable);
						if (instance.sortinfo.dir === 1) {
							th.removeClass(instance.options.css.desc);
							th.addClass(instance.options.css.asc);
						} else {
							th.removeClass(instance.options.css.asc);
							th.addClass(instance.options.css.desc);
						}
					});
				});
				break;
			}
		}
		
//		instance.table.find('tr th[colname="'+key+'"]')
//		.addClass('simple-table-sortable')
//		.attr('sortable', true)
//		.click(function() {
//			if (instance.sortinfo && instance.sortinfo.key == key) {
//				instance.sortinfo.dir = instance.sortinfo.dir * (-1);
//			} else {
//				instance.sortinfo = {
//					key: key,
//					dir: 1
//				};
//			}
//			var th = $(this);
//			load(instance, function() {
//				//TODO add classes to sorted th&td.
//				instance.table.find('tr th').each(function() {
//					if ($(this).attr('sortable')) {
//						$(this).removeClass('simple-table-sort-desc');
//						$(this).removeClass('simple-table-sort-asc');
//						$(this).addClass('simple-table-sortable');
//					}
//				});
//				th.removeClass('simple-table-sortable');
//				if (instance.sortinfo.dir === 1) {
//					th.removeClass('simple-table-sort-desc');
//					th.addClass('simple-table-sort-asc');
//				} else {
//					th.removeClass('simple-table-sort-asc');
//					th.addClass('simple-table-sort-desc');
//				}
//			});
//		});
	}
	/**
	 * 置空列表
	 */
	var empty = function(instance) {
		instance.table.removeAttr("isbuilded");
		if (instance.ckhead) {
			instance.ckhead.attr("checked", false);
		}
		var tbody = instance.table.children('tbody');
		$(tbody).remove();
	}
	
	/////////////////////////////////////////////////////////////
	///////     以下为对外提供的方法的定义.
	/////////////////////////////////////////////////////////////
	/**
	 * 对外提供的查询方法
	 */
	this.doSearch = function(callback) {
		this.isDoSearch = true;
		empty(this);
		init(this);
		load(this, callback);
		this.isDoSearch = false;
	}
	
	/**
	 * 设置某列显示
	 * @param {Object} colname
	 */
	this.showColumn = function(colname) {
		for (var i=0; i<this.cols.length; i++) {
			if (this.cols[i].key == colname) {
				this.cols[i].display = true;
				this.table.find('th[colname="'+colname+'"]').attr('display',true).show();
				break;
			}
		}
	}
	/**
	 * 设置某列隐藏
	 * @param {Object} colname
	 */
	this.hideColumn = function(colname) {
		for (var i=0; i<this.cols.length; i++) {
			if (this.cols[i].key == colname) {
				this.cols[i].display = false;
				this.table.find('th[colname="'+colname+'"]').attr('display',false).hide();
				break;
			}
		}
	}
	
	/**
	 * 取得表格里所有的数据。
	 */
	this.getData = function() {
		return this.datas;
	}
	
	/**
	 * get the selected pkcol(s).
	 */
	this.getSelected = function() {
		var ckbs = $(this.ckcol);
		var cked = new Array();
		ckbs.each(function(i) {
			if($(this).attr('checked')) {
				cked.push($(this).val());	
			}
		});
		return cked;
	}
	/**
	 * get the selected object(s).
	 */
	this.getSelectedObj = function() {
		var ckbs = $(this.ckcol);
		var objs = new Array();
		ckbs.each(function(i) {
			if($(this).attr('checked')) {
				objs.push($(this).data('obj'));
			}
		});
		return objs;
	}
	
	///////////////////////////////////////////////
	//           Executes
	///////////////////////////////////////////////
	init(instance);
	if (instance.options.datas) { //接收静态数据
		instance.datas = instance.options.datas;
		build(instance);
	} else if(instance.options.autoLoad) {
		instance.isDoSearch = true;
		load(instance);
		instance.isDoSearch = false;
	}
	instance.table.data('table', instance);
	//返回该实例
	return instance;
}

/**************  parameter adapters   ******************/
var DefaultAdapter = function() {
	var buildParam = function(searchDiv) {
		var param = '';
		var conditions = [];
		var ele = $('#'+searchDiv).find(':input[name]');
		ele.each(function(i) {
			var map = {};
			var key = $(this).attr("name");
			var isExist = false;
			for (var j=0; j<conditions.length; j++) {
				if (key == conditions[j].key) {
					isExist = true;
					map = conditions[j];
					break;
				}
			}
			var value = $(this).val();
			var type = $(this).attr("type");
			if ((type == 'checkbox' || type == 'radio')) {
				if ($(this).attr("checked") !== true) {
					value = "";
				}
			}
			if (isExist) {
				map.value += encodeURIComponent("," + value);
			} else {
				map.key = key;
				map.value = encodeURIComponent(value);
				conditions.push(map);
			}
		});
		if (conditions.length > 0) {
			for (var attr=0; attr<conditions.length; attr++) {
				var p = conditions[attr];
				param += '&' + p.key + '=' + p.value;
			}
		}
		return param;
	}
	this.initParam = function(tableInstance) {
		var param = '';
		param += ('currentPage=' + tableInstance.pageinfo.currentPage);
		param += ('&recordsPerPage=' + tableInstance.pageinfo.pageSize);
		if (tableInstance.isDoSearch) {
			var paramString = buildParam(tableInstance.options.param);
			param += paramString;
			tableInstance.queryObject = paramString;
		} else {
			param += tableInstance.queryObject;			
		}
		return param;
	}
}

var PMSTableAdapter = function() {
	/**
	 * Request Object is the data model used to communicate with server side.
	 */
	var PMSRequestObject = function(managerName, methodName, parameters, token) {
		this.managerName = managerName;
		this.methodName = methodName;
		this.parameters = parameters;
		this.token = token;
		this.toJsonString = function() {
			return $.toJSON({
				managerName : this.managerName,
				methodName : this.methodName,
				parameters : this.parameters,
				token : this.token
			});
		};
	};
	var buildQueryObj = function(tableInstance) {
		var searchConId = tableInstance.options.param;
		tableInstance.searchContainer = $('#'+searchConId);
		var conditions = gatherData(tableInstance.searchContainer);
		var queryObj = {
			conditions:conditions
		};
		return queryObj;
	}
	
	var gatherData = function(container) {
		var conditions = [];
		var ele = container.find(':input[name]');
		ele.each(function(i) {
			var map = {};
			var key = $(this).attr("name");
			var isExist = false;
			for (var j=0; j<conditions.length; j++) {
				if (key == conditions[j].key) {
					isExist = true;
					map = conditions[j];
					break;
				}
			}
			var value = $(this).val();
			var type = $(this).attr("type");
			if ((type == 'checkbox' || type == 'radio')) {
				if ($(this).attr("checked") !== true) {
					value = "";
				}
			}
			if (isExist) {
				map.value += encodeURIComponent("," + value);
			} else {
				map.key = key;
				map.value = encodeURIComponent(value);
				conditions.push(map);
			}
		});
		return conditions;
	}
	//adapter must implement this method.
	this.initParam = function(tableInstance) {
		var param = '';
		param += "requestString=";
		var manager = tableInstance.options.managerName ? tableInstance.options.managerName : "queryManager";
		var method = tableInstance.options.methodName ? tableInstance.options.methodName : "executeQuery";
		var queryId = tableInstance.table.attr('queryid');
		var queryObj = null;
		if (tableInstance.isDoSearch) {
			queryObj = buildQueryObj(tableInstance);
			queryObj.queryId = queryId;
		} else {
			queryObj = tableInstance.queryObject;
		}
		if (tableInstance.sortinfo) {
			queryObj.sortinfo = {
				variableName:tableInstance.sortinfo.key,
				direction:tableInstance.sortinfo.dir
			};
		}
		queryObj.pageinfo = {
			currentPage : tableInstance.pageinfo.currentPage,
			recordsPerPage : tableInstance.pageinfo.pageSize
		};
		tableInstance.queryObject = queryObj;
		var reqObj = new PMSRequestObject(manager, method, [ $.toJSON(queryObj) ]);
		param += reqObj.toJsonString();
		return param;
	}
}
