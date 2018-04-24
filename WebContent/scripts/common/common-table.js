var Col = function(key) {
	this.key = key ? key : null; //key, required, bean property.
	this.text = null; //content, the text to display.
	this.value = null; //bean value.
	this.type = null; //type, op col:a(super link)/js(javascript method).
	this.cls = null; //cls, css class.
	this.href = null; //href, op col:href value.
	this.sort = false; //sort or not.
	this.display = true; //display or not.
}
Col.prototype.fromTh = function(th) {
	th = $(th);
	this.key = th.attr('colname'); //required.
	this.text = th.attr('text');
	this.value = th.attr('value');
	this.type = th.attr('type');
	this.cls = th.attr('cls');
	this.href = th.attr('href');
	this.sort = th.attr('sort') == 'true' ? true : false;
	this.display = th.attr('display') == 'false' ? false : true;
}
Col.prototype.available = function() {
	if(!this.key) return false;
	return true;
}
/**
 * Table Component.
 * @param {Object} id
 * 			the table id.
 * @param {Object} datas
 * 			the json string contains a list of objects.
 */
var PMSTable = function(id, datas, options) {
	this.tableClass='display';
	this.table=$('#'+id);
	this.datas=datas ? datas : null;
	this.options = options ? options : new Object();
	var initCheck=function(table) {
		if(table.length <= 0 
			|| table.get(0).tagName.toUpperCase() != 'TABLE') {
			alert("The table<table> not exists: id=" + id);
			return false;
		}
		return true;
	};
	var pass = initCheck(this.table);
	this.src = this.table.attr('src') ? this.table.attr('src') : null;
	this.paging = this.table.attr('showpaging') == 'true' ? true : false;
	this.pageinfo = {
		currentPage:1,
		pageSize:10,
		totalPage:0,
		totalRecord:0
	};
	this.pk = this.table.attr('pkcol') ? this.table.attr('pkcol') : 'id';
	this.cols = [];
	this.ckhead = null; //colname='checkbox'
	this.showrownum = false;
	this.ckcol = [];
	this.opcol = null; // colname='op'
	if (pass) {
		var autobuild=this.table.attr("autobuild")=='false'?false:true;
		if(autobuild) {
			if (this.src !== null) {
				//TODO
			} else {
				this.build();
			}
		}
		this.table.data('table', this);
	}
}
/**
 * set the datas.
 * @param {Object} data
 */
PMSTable.prototype.setData = function(data) {
	if(data instanceof Array) {
		this.datas = data;
	}
}
/**
 * build this table.
 */
PMSTable.prototype.build = function() {
	if(this.table.attr("isbuilded")) return false;
	this.table.addClass(this.tableClass);
	var ths = this.table.find("th");
	this.ckhead=ths.filter(":[colname='checkbox']").find(":input[type='checkbox']");
	this.opcol=ths.filter(":[colname='op']");
	var formatters = this.options.formatters;
	for(var i=0; i<this.datas.length; i++) {
		var obj = this.datas[i];
		var idValue = obj[this.pk];
		var tr = $("<tr></tr>");
		for(var j=0; j<ths.length; j++) {
			var col = $(ths[j]);
			var name = col.attr('colname');
			var td = $("<td></td>");
			if(name == 'rownum') {
				this.showrownum = true;
				td.append(i+1);
			} else if(name == 'checkbox') {
				var ck = $("<input type='checkbox' />");
				ck.val(idValue);
				ck.data('obj', obj);
				td.append(ck);
				this.ckcol.push(ck);
			} else if(name == 'op') {
				var opstr = this.opcol.attr("operators");
				var ops = $.fromJSON(opstr);
				for(var k=0; k<ops.length; k++) {
					var col = ops[k];
					var html = buildOpHtml(col, obj);
					td.append(html);
				}
			} else {
				var val = obj[name];
				try {
					if (typeof formatters[name] == 'function') {
						val = formatters[name](val);
					}
				} catch (error) {
					//do nothing.
				}
				td.html(val);
			}
			tr.append(td);
		}
		if(Math.round(i%2) == 0) {
			tr.addClass('odd');
		} else {
			tr.addClass('even');
		}
		this.table.append(tr);
	}
	this.addEventListener();
	this.table.attr("isbuilded",true);
}
/**
 * build operator column html.
 * @param {Object} col
 * 			the column.
 * @param {Object} obj
 * 			the row data(obj).
 */
var buildOpHtml = function(col, obj) {
	var html = '<a href="' + col.href;
	var param = '';
	if(col.params && col.params.length > 0) {
		var ps = col.params.split(',');
		if(col.type=='a') {
			param += '?';
			for(var i=0; i<ps.length; i++) {
				var v = obj[ps[i]];
				param += (ps[i] + '=' + v);
				if(i < ps.length-1) param+='&';
			}
		} else if(col.type=='js') {
			param += '(';
			for(var i=0; i<ps.length; i++) {
				var v = obj[ps[i]];
				param += ('\''+v+'\'');
				if(i < ps.length-1) param+=',';
			}
			param += ')';
		}
	}
	html += (param+'"');
	if(col.cls){
		html += (' class="' + col.cls + '" ');
	}
	html += (' >' + col.text + '</a> ');
	return html;
}
/**
 * add the event listener.
 */
PMSTable.prototype.addEventListener = function() {
	if(this.ckhead && this.ckhead.length==1 && this.ckcol.length > 0) {
		var head = this.ckhead;
		var cks = this.ckcol;
		setCheckStatus(head, cks);
	}
}
/**
 * selectAll & cancelAll.
 * @param {Object} head
 * 				the check head
 * @param {Object} cks
 * 				the check children
 */
function setCheckStatus(head, cks) {
	head = $(head);
	cks = $(cks);
	head.click(function() {
		var checked = $(this).attr('checked'); 
		$(cks).each(function(i){
			$(this).attr('checked', checked);
		});
	});
	cks.each(function(i) {
		$(this).click(function(i) {
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
		});
	});
}

/**
 * rebuild the table.
 */
PMSTable.prototype.rebuild = function(){
	this.table.removeAttr("isbuilded");
	var tbody = this.table.children('tbody');
	$(tbody).remove();
	this.build();
}
/**
 * get the selected pk(s).
 */
PMSTable.prototype.getSelected = function() {
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
PMSTable.prototype.getSelectedObj = function() {
	var ckbs = $(this.ckcol);
	var objs = new Array();
	ckbs.each(function(i) {
		if($(this).attr('checked')) {
			objs.push($(this).data('obj'));
		}
	});
	return objs;
}

