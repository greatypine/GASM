/**
 * Tree
 * Copyright (c) 2010 Hewang 
 * Date: 2010-11-09 
 * 瑞飞自己的树插件，基于Jquery 1.4.3
 * 
 * @author Howard
 * @version 2.5
 * 
 * 版本新特性：
 * 		该版本增加配置项：disabledMode:all/parent/children
 * 		在静态树中，根据节点的禁用状态设置整棵树禁用状态
 * 
 * 注：采用动态加载的形式，node.id必须保证唯一，
 *    如果有重复，则可能会显示出错。
 *    如在使用过程中发现任何问题，请及时提醒，多谢，多谢！
 *    
 *	可配置项:
 * 			url:null,
 *          lazy:true, //true/false
 *          expandAll:false, //true/false
 *			display:true, //true/false
 *		    animation:false, //和下一个的配置一起使用
 * 		    speed:'normal',//'slow'/'normal'/'fast'/number(eg:1000)
 *			checkbox:false, //true/false
 *			checkMode:'single',//single(默认单选)/related(级联)/free(多选)
 *			//节点状态刷新方式：默认'update'，针对于异步方式
 *			//可选：'update'/'never'
 *			//此属性跟controller配合使用
 *			refreshMode:'update',
 *			//parent/children：默认由父节点控制子节点的状态
 *			//此属性跟refreshMode配合使用
 *			controller:'parent',
 *			grid:true, //是否带边框（仿表格）
 *			formatter:function(node){},
 *			onClick:function(node){}
 *
 *	方法:
 *		* 使用checkbox时返回已选中的节点id总数
 *			$.fn.tree.getCheckedSum()
 *		* 选中的id用‘,’连成的字符串
 *			$.fn.tree.getCheckedIdStr()
 *		* 选中的id数组
 *			$.fn.tree.getCheckedIdArr()
 *		* 按照最大限度的父节点返回ID
 *		*（如某节点下所有子节点为选中状态，则只返回该节点的id）
 *			$.fn.tree.getCheckedIdArrByParent()
 *
 *	demo:
 *		在页面中定义：<div id="rftree"> </div>
 *		在其后写:
 *				<script type="text/javascript">
 *					$(function() {
 *						$('#rftree').tree({
 *							url:'yourTreeUrl',
 *							//配置项,详见“可配置项”
 *							checkbox:true
 *						});
 *					});
 *					//数据获取方法：(具体方法见“方法”)
 *						eg: function getSelected() {
 *								return $('rftree').tree.getCheckedIdArr();
 *							} //即可得到当前选中的节点的id数组
 *				</script>
 *
 */

(function($){
	
	//document.write('<div id="rftree" style="background-color: #E4E6F5;border: 1px solid #000;width:180px;text-align:left;"></div>');
	
	$.equalsIgnoreCase = function(src, dist) {
		if(src.toLowerCase()==dist.toLowerCase()) return true;
		return false;
	}
	$.isEmpty = function(v) {
		switch (typeof v){
		case 'undefined' : 
			return true;
			break;
		case 'string' : 
			if(v.trim().length == 0) {
				return true; 
			}
			break;
		case 'boolean' : 
			if(!v) {
				return true;
			}
			break;
		case 'number' : 
			if(0 === v) {
				return true; 
			}
			break;
		case 'object' :
			if(null === v) {
				return true;
			}
			if(undefined !== v.length && v.length==0) {
				return true;
			}
			for(var k in v) {
				return false;
			}
			return true;
			break;
		}
		return false;
	}
	$.findInArr = function(key, array) {      /*JS暴虐查找*/
		re = new RegExp(key,[""])
		return (array.toString().replace(re,"┢").replace(/[^,┢]/g,"")).indexOf("┢");
	}
	
	/**
	 * 树形插件
	 * 容器应为div标签
	 * checkbox使用input checkbox的方式
	 */
	$.fn.tree = function(options){
	
		//alert(mouseOverClass);
		var defaults = { //各种属性、参数(默认)
			url:null,
			remote:true,
			lazy:false, //false/true
			root:null,
			expandAll:false, //true/false
			display:true, //true/false
			animation:false,
			speed:'normal',//'slow'/'normal'/'fast'/number(eg:1000)
			checkbox:false, //true/false
			checkMode:'single',//single(默认单选)/related(级联)/free(多选)
			//节点状态刷新方式：默认'update'，针对于异步方式
			//可选：'update'/'never'
			//此属性跟controller配合使用
			refreshMode:'update', 
			//parent/children：默认由父节点控制子节点的状态
			//此属性跟refreshMode配合使用
			controller:'parent',
			disabledMode:'none', //none/all/parent/children:仅限于静态树
			grid:false, //false/true:是否带边框
			mouseOverClass:true, //鼠标移到节点上的效果
			formatter:function(node){
				return node.text;
			},
			onChange:function(){},
			onClick:function(){}
		};
		var options = $.extend(defaults, options);
		//checkbox 样式
		var status = [false,'half',true];
		
		
		/**
		 * 定义一个Node结构体（类）
		 */
		var Node = function(id, text) {
			this.id = id;
			this.text = text;
			this.open = false;   //是否展开,后台传值
			this.checked = false;  //false/'half'/true
			this.disabled = false; //checkbox是否被禁用,false/true
			this.isLeaf = false; //是否叶子节点
			this.attributes = '';
			this.target = '';
			this.expanded = false; //是否展开过
			this.level = -1;
			this.parent = null;
			this.children = new Array();
			
			/**
			 * 改变checkbox的状态为statu
			 */
			this.setStatu = function(statu) {
				if(this.checked != statu) {
					var checkbox = this.target.find("input[type='checkbox']")[0];
					if(statu == 'half') {
						$(checkbox).attr('indeterminate', true);
					} else {
						$(checkbox).attr('indeterminate', false);
						checkbox.checked = statu;
					}
					this.checked = statu;
				}
			};
			/**
			 * 改变子节点的状态
			 */
			this.setChildrenStatu = function(statu) {
				if(this.children.length > 0) {
					for(var i=0; i<this.children.length; i++) {
						this.children[i].setStatu(statu);
						this.children[i].setChildrenStatu(statu);
					}
				}
			};
			/**
			 * 改变父节点的状态
			 */
			this.setParentStatu = function() {
				var tempParent = this.parent;
				if(tempParent != options.root) {
					var result = tempParent.checkChildrenStatus();
					if(result != -1) {
						tempParent.setStatu(status[result]);
						tempParent.setParentStatu();
					}
				}
			}
			/**
			 * 在所有子节点中：
			 * 0.全不选状态        -> 返回0
			 * 1.全半选/部分选中    -> 返回1
			 * 2.全选状态          -> 返回2
			 * 即：在status中的下标值
			 * 注：禁用状态下的选择算true，其他同理。
			 */
			this.checkChildrenStatus = function() {
				if(this.children.length <= 0) return -1;
				var first = this.children[0].checked;
				for(var i=0; i<this.children.length; i++) {
					var temp = this.children[i].checked;
					if(first != temp) {
						return 1;
						break;
					}
				}
				return $.findInArr(first,status);
			}
			/**
			 * 改变checkbox的状态
			 * 根据目前状态改变
			 */
			this.changeCheck = function() {
				//默认方式为单选（single）
				if(options.checkMode == 'related') { //级联
					this.setChildrenStatu(this.checked);
					this.setParentStatu();
				} else if(options.checkMode == 'free') { //无限制
					return false;
				} else { //默认（单选）
					if(this.checked == true) {
						this.setSingleChecked();
					}
				}
			};
			/**
			 * 单选
			 */
			this.setSingleChecked = function() {
				var root = options.root;
				root.target.find("input[type='checkbox']")
					.attr('checked',false)
					.attr('indeterminate',false);
//				var ck = this.target.find("input[type='checkbox'][id='ck"+this.id+"']")[0];
				var ck = this.target.find("input[type='checkbox']")[0];
				$(ck).attr('checked',true);
				this.checked = true;
			};
			
			/**
			 * 禁用所有父节点（不包括本身）
			 */
			this.setAllParentsDisabled = function(disabled) {
				var tempParent = this.parent;
				if(tempParent != options.root) {
					var p_disabled = tempParent.disabled;
					if(p_disabled != disabled) {
						var ck = tempParent.target.find("input[type='checkbox']")[0];
						$(ck).attr('disabled',disabled);
						tempParent.setAllParentsDisabled(disabled);
					}
				}
			};
			/**
			 * 禁用所有子节点（不包括本身）
			 */
			this.setAllChildrenDisabled = function(disabled) {
				if(this.children.length > 0) {
					for(var i=0; i<this.children.length; i++) {
						this.children[i].disabled = disabled;
						this.children[i].setAllChildrenDisabled(disabled);
					}
				}
			};
		}

/*** private (私有方法) **********************************************/
		/**
		 * 动态加载时，由后台json数据构建Node对象
		 */
		function buildNode(data_i) {
			var node = new Node();
			
			//判断是否为叶子节点
			if(data_i.children==null){
				data_i.isLeaf = true;
			}else{
				data_i.isLeaf = false;	
			}
			//节点id
			node.id = $.isEmpty(data_i.id) ? '' : data_i.id;
			
			//显示文字
			node.text = data_i.text;
		
			//如有复选框，是否选中 false/'half'/true
			node.checked = $.isEmpty(data_i.checked) ? false : data_i.checked;
			//如有复选框，是否被禁用
			node.disabled = $.isEmpty(data_i.disabled) ? false : data_i.disabled; //checkbox是否被禁用,false/true
			
		
			//是否是叶子节点
			node.isLeaf = $.isEmpty(data_i.isLeaf) ? false : data_i.isLeaf;
			//其他属性
			
			node.attributes = $.isEmpty(data_i.attributes) ? '' : data_i.attributes;
			
			return node;
		};
		//被禁用的节点
		var disabledNodes = new Array();
		
		/**
		 * 一次性加载时，需要一次性处理所有节点及其子节点
		 * 此为构建Node对象数组的方法
		 */
		function buildAllNodes(_p_, datas) {
			$(datas).each(function(i) {
				var child = buildNode(datas[i]);
				child.level = _p_.level + 1;
				if(!child.isLeaf) child.expanded=true;
				child.open = options.expandAll;
				if( ! $.isEmpty(datas[i].children)) {
					buildAllNodes(child, datas[i].children);
				}
				child.parent = _p_;
				_p_.children.push(child);
				if(child.disabled) {
					disabledNodes.push(child);
				}
			});
		}
		
		/**
		 * 仅用于静态树
		 * 根据禁用的节点，禁用子节点
		 */
		function setChildrenDisabledStatus() {
			for(var i=0; i<disabledNodes.length; i++) {
				disabledNodes[i].setAllChildrenDisabled(disabledNodes[i].disabled);
			}
		}
		/**
		 * 仅用于静态树
		 * 根据禁用的节点，禁用父节点
		 */
		function setParentDisabledStatus() {
			for(var i=0; i<disabledNodes.length; i++) {
				disabledNodes[i].setAllParentsDisabled(disabledNodes[i].disabled);
			}
		}
		
		//此句在懒加载和非懒加载中都有用
		var isGrid = options.grid ? ' tree-node-border' : '';
		/**
		 * 得到节点的缩进
		 */
		function appendIndent(node) {
			var str = '';
			for(var i=0; i<node.level; i++) {
				str += '<span class="tree-indent"> </span>';
			}
			return str;
		}
		/**
		 * 得到节点前的展开图标
		 * 1.没有图标（叶子节点）
		 * 2.有图标，且图标是收缩状态的
		 * 3.有图标，且图标是展开的
		 */
		function appendNodeHit(node) {
			var str = '';
			if(!node.isLeaf) {
				str += '<span class="tree-hit';
				str += (node.open==true ? ' tree-expanded' : ' tree-collapsed');
				str += '"> </span>';
			} else {
				str += '<span class="tree-indent"> </span>';
			}
			return str;
		}
		/**
		 * 得到节点前的图标
		 * 默认为文件夹图标
		 * 当是叶子节点时为文件图标
		 */
		function appendNodeIcon(node) {
			var str = '';
			str += '<span class="';
			str += (node.isLeaf==true ? 'tree-icon tree-file' : 'tree-icon tree-folder');
			str += '"> </span>';
			return str;
		}
		/**
		 * 添加复选框
		 */
		function appendCheckbox(node) {
			var str = '';
			if(options.checkbox) {
				str += '<input type="checkbox"';
				str += (node.checked==true ? ' checked' : ' ');
				str += (node.disabled==true ? ' disabled' : ' ');
				str += '/>';
			}
			return str;
		}
		/**
		 * 为节点添加文字
		 */
		function appendNodeText(node) {
			var str = '';
			str += '<span class="node-title">';
			var formatMethod = options.formatter;
			if(!$.isFunction(formatMethod)) {
				throw new Error('formatter不是有效的函数!');
			}	
			str += formatMethod(node);
			str += '</span>';
			return str;
		}

		/**
		 * 构建所有子节点的html
		 */
		function buildAllHtmls(_children) {
			var html = '';
			if(!$.isEmpty(_children)) {
				html += '<ul' 
						+ (options.expandAll ? '' : ' style="display:none;"') 
						+ '>';
				for(var i=0; i<_children.length; i++) {
					html += ('<li><div class="tree-node' 
						+ isGrid 
						+ '" node-id="'
						+ _children[i].id + '" style="cursor:default;">'
						+ appendIndent(_children[i])
						+ appendNodeHit(_children[i])
						+ appendNodeIcon(_children[i])
						+ appendCheckbox(_children[i])
						+ appendNodeText(_children[i])
						+ '</div>'
						+ buildAllHtmls(_children[i].children)
						+ '</li>'
					);
				}
				html += '</ul>';
			}
			
			return html;
		}
		
		/**
		 * 为节点绑定相关事件
		 * 1.节点展开图标的单击事件
		 * 2.如果options.checkbox为true,绑定checkbox事件
		 * 3.绑定node.text的单击事件
		 */
		function bindEvents(node_e) {
			var div = $("div.tree-node[node-id='"+node_e.id+"']")[0]; 
			var li = $(div).parent();
			node_e.target = li; //绑定target元素（<li>）到节点（node）
			var span = node_e.target.find("div span.tree-hit")[0];
			$(span).bind('click',function(_node) { //展开与收缩
				_node = node_e;
//此处可以想办法改进（如：在node类中定义状态变量，保存此节点当前是展开还是收缩的）
//				if(this.className.indexOf('tree-expanded') >= 0) {
//					collapse(_node);
//				} else {
//					expand(_node);
//				}
				if(_node.open) {
					collapse(_node);
				} else {
					expand(_node);
				}
			});
			if(options.checkbox) { //checkbox
				var checkbox = node_e.target.find("input[type='checkbox']")[0];
				$(checkbox).bind('click', function(_node) {
					_node = node_e;
					$(_node).trigger('change');
				});
				$(node_e).bind('change', function() {
					if(this.disabled) {
						return false;
					} else {
						this.checked = checkbox.checked;
						this.changeCheck();
					}
					if($.isFunction(options.onChange)) {
						options.onChange(node_e);
					}
				});
			}
			var text = node_e.target.find("div span.node-title").eq(0);
			if(options.mouseOverClass) {
				text.bind('mouseover', function(node_mo) {
					text.addClass('tree-node-hover');
				});
				text.bind('mouseout', function(node_mo) {
					text.removeClass('tree-node-hover');
				});
			}
			text.bind('click', function(node_c) { //node.text单击事件
				node_c = node_e;
				if($.isFunction(options.onClick)) {
					options.onClick(node_c);			 //在options中配置
				
					$("div span.node-title").removeClass('tree-node-selected');
					text.addClass('tree-node-selected');
				}
			});
		}
		
		function bindAllEvents(_children) {
			if(!$.isEmpty(_children)) {
				$(_children).each(function(i) {
					bindEvents(this);
					if(!$.isEmpty(this.children)){
						bindAllEvents(this.children);
					}
				});
			}
		}
		
		/**
		 * 加载数据（非懒加载）
		 */
		function load(node) {
			
			if(options.managerName==null||options.managerName==""){
				alert("远程获取数据必须提供managerName参数配置！");
				return ;	
			}
			if(options.methodName==null||options.methodName==""){
				alert("远程获取数据必须提供methodName参数配置！");
				return ;	
			}
			
			var data = new PMSRequestObject(options.managerName,options.methodName,options.methodParams);
		
			
			$$.ajax($$.PMSDispatchActionUrl, "requestString="+data.toJsonString(), function(data, textStatus, XMLHttpRequest){
				
					
					
				if(data.result) {
					eval("var resultData = "+data.data);
					var resultData = $.fromJSON(resultData);
					loadData(resultData,node);
					if(options.disabledMode=='all') {
						setParentDisabledStatus();
					} else if(options.disabledMode=='parent') {
						setParentDisabledStatus();
					}
				}
			});	
			/*
			$.ajax({
				type : "POST",
				url : options.url,
				data : 'id='+node.id,
				success : function(data) {
					//对于直接从文件中读取的，需要将data(string)转成json对象
//					if(typeof data != 'object'){ // typeof data == 'string'
//						data = eval('(' + data + ')');
//					}
					loadData(data,node);
					if(options.disabledMode=='all') {
						setParentDisabledStatus();
					} else if(options.disabledMode=='parent') {
						setParentDisabledStatus();
					}
				}
			});*/
		}
		
		function loadData(data,node){
			
			if(data.length > 0) {
				var html = '<ul>';
				$(data).each(function(i){
						
					var child = buildNode(data[i]);
				
					child.parent = node;
					child.level = node.level + 1;
					child.open = options.expandAll;
					if(!$.isEmpty(data[i].children)) {
						buildAllNodes(child,data[i].children);
					}
					
					if(options.disabledMode=='all') {
						setChildrenDisabledStatus();
					} else if(options.disabledMode=='children') {
						setChildrenDisabledStatus();
					}
					
					html += ('<li><div class="tree-node' 
						+ isGrid 
						+ '" node-id="'
						+ child.id + '" style="cursor:default">'
						+ appendIndent(child)
						+ appendNodeHit(child)
						+ appendNodeIcon(child)
						+ appendCheckbox(child)
						+ appendNodeText(child)
						+ '</div>'
						+ buildAllHtmls(child.children)
						+'</li>');
					child.expanded = true;
					node.children.push(child);
				});
				html += '</ul>';
				
				node.target.append(html);
				node.expanded = true; //标记已被展开过
				
				bindAllEvents(node.children);
				
//						for(var i=0; i<node.children.length; i++) {
//							bindEvents(node.children[i]); //为每个节点绑定事件
//						}
				//alert('该版本暂不提供此功能,请关注后续版本');
//						buildAllNodes(node,data);
//						var html = buildAllHtmls(node.children);
//						node.target.append(html);
			}	
		}
		
		/**
		 * 懒加载
		 */
		function lazyLoad(node) {
			var folderSpan = node.target.find("div span.tree-folder")[0];
			$(folderSpan).addClass('tree-loading');
			$.ajax({
				type : "POST",
				url : options.url,
				data : 'id='+node.id,
				success : function(data) {
					if(data.length > 0) {
						var children = new Array();
						var html = '<ul>';
						$(data).each(function(i){
							var child = buildNode(data[i]);
							child.level = node.level + 1;
							if(options.refreshMode == 'update') { //有更新就检查子节点的状态
								//contoller is 'parent'
								if(options.controller == 'parent') {
									
									if(options.disabledMode=='all') {
										child.disabled = node.disabled;
									} else if(options.disabledMode=='children') {
										child.disabled = node.disabled;
									}
									
									//如果该节点禁用，则所有子节点禁用
//									if(node.disabled) child.disabled = node.disabled;
									
									//如果该节点选中，则所有子节点选中,仅对于checkMode为级联时有效
									if(options.checkMode=='related' && node.checked == true) {
										child.checked = node.checked;
									}
								} else { //controller为children
									//controller为children的情况下，不检查子节点的状态
								}
							} else { //refreshMode为nerver
								//刷新模式为nerver的情况下，不检查子节点的状态
							}
							html += ('<li><div class="tree-node ' 
								+ isGrid 
								+ '" node-id="'
								+ child.id + '" style="cursor:default;">'
								+ appendIndent(child)
								+ appendNodeHit(child)
								+ appendNodeIcon(child)
								+ appendCheckbox(child)
								+ appendNodeText(child)
								+ '</div>'
								+'</li>');
							child.parent = node;
							children.push(child);
						});
						html += '</ul>';
						node.target.append(html);
						node.expanded = true; //标记已被展开过
						node.children = children;
						if(options.refreshMode == 'update') { //有更新就检查父节点的状态
							//更新父节点的状态
							if(options.checkMode=='related') {
								node.children[0].setParentStatu();
							}
							//如果该节点禁用，则所有父节点禁用
							for(var i=0; i<children.length; i++) {
								//如果该节点是禁用状态，则将其所有父节点禁用
								if(options.disabledMode=='all') {
									children[i].setAllParentsDisabled(children[i].disabled);
								} else if(options.disabledMode=='parent') {
									children[i].setAllParentsDisabled(children[i].disabled);
								}
								
//								if(children[i].disabled) children[i].setAllParentsDisabled(children[i].disabled);
								
								bindEvents(children[i]); //为每个节点绑定事件
							}
						} else { //refreshMode为nerver
							//refreshMode为nerver的情况下，什么也不做
						}
					}
					$(folderSpan).removeClass('tree-loading');
				}
			}); // ajax结束
		}
		/**
		 * 展开节点(懒加载)
		 */
		function expand(node) {
			if(node.isLeaf) return false;
			if(node.expanded) {
				var children = node.target.find('ul')[0];
				if(options.animation) {
					$(children).slideDown(options.speed);
				} else {
					$(children).show();
				}
			} else {
				lazyLoad(node);
			}
			var span = node.target.find("div span.tree-hit")[0];
			$(span).removeClass('tree-collapsed')
				   .addClass('tree-expanded');
			node.open = true;
		};
		
		/**
		 * 收缩节点
		 */
		function collapse(node) {
			var span = node.target.find("div span.tree-hit")[0];
			$(span).removeClass('tree-expanded')
				   .addClass('tree-collapsed');
			var children = node.target.find('ul')[0];
			if(options.animation) {
				$(children).slideUp(options.speed);
			} else {
				$(children).hide();
			}
			node.open = false;
		}
		
		var _tree = this;
		
		/**
		 * 有两种获取checked个数的方法：
		 * 经测试，第一种效率明显高于第二种，
		 * 第二种参见：getChecked2()
		 * 大约耗时：getChecked耗时约为getChecked2的1/4
		 */
		var _sum = 0;
		var _ids = '';
		var _idArr = new Array();
		var _idArr_p = new Array();
		var _textArr_p = new Array();
		var _nodes = new Array();
		var _nodes_p = new Array();
		//选中的总数
		function getCheckedSumFree(node) {
			if((!$.isEmpty(node.id)) && node.checked == false) {
				return;
			} else {
				if(node.checked==true) {
					_sum++;
				}
				if(node.children.length>0) {
					$(node.children).each(function(i) {
						getCheckedSumFree(this);
					});
				}
			}
		}
		//选中的id用','连成的字符串
		function getCheckedIdsRelated(node) {
			if((!$.isEmpty(node.id)) && node.checked == false) {
				return;
			} else {
				if(node.checked==true) {
					_ids += (node.id + ',');
				}
				if(node.children.length>0) {
					$(node.children).each(function(i) {
						getCheckedIdsRelated(this);
					});
				}
			}
		}
		//free时获取选中
		function getCheckedIdsFree(node) {
			if(node.checked==true) {
				_ids += (node.id + ',');
			}
			if(node.children.length>0) {
				$(node.children).each(function(i) {
					getCheckedIdsFree(this);
				});
			}
		} 
		//选中的id数组
		function getCheckedIdArrFree(node) {
			if((!$.isEmpty(node.id)) && node.checked == false) {
				return null;
			} else {
				if(node.checked==true) {
					_idArr.push(node.id);
				}
				if(node.children.length>0) {
					$(node.children).each(function(i) {
						getCheckedIdArrFree(this);
					});
				}
			}
		}
		//选中的id数组(按最大限度的父节点)
		function getCheckedIdArrFreeByParent(node) {
			if((!$.isEmpty(node.id)) && node.checked == false) {
				return;
			} else {
				if(node.checked==true) {
					_idArr_p.push(node.id);
				} else {
					if(node.children.length>0) {
						$(node.children).each(function(i) {
							getCheckedIdArrFreeByParent(this);
						});
					}
				}
			}
		}
		
		
		
		
		//选中的text数组(按最大限度的父节点)
		function getCheckedTextArrFreeByParent(node) {
			if((!$.isEmpty(node.id)) && node.checked == false) {
				return;
			} else {
				if(node.checked==true) {
					_textArr_p.push(node.text);
				} else {
					if(node.children.length>0) {
						$(node.children).each(function(i) {
							getCheckedTextArrFreeByParent(this);
						});
					}
				}
			}
		}
		
		//选中的Node数组
		function getCheckedNodeArrFree(node) {
			if((!$.isEmpty(node.id)) && node.checked == false) {
				return null;
			} else {
				if(node.checked==true) {
					_nodes.push(node);
				}
				if(node.children.length>0) {
					$(node.children).each(function(i) {
						getCheckedNodeArrFree(this);
					});
				}
			}
		}
		//选中的Node数组(按最大限度的父节点)
		function getCheckedNodeArrFreeByParent(node) {
			if((!$.isEmpty(node.id)) && node.checked == false) {
				return;
			} else {
				if(node.checked==true) {
					_nodes_p.push(node);
				} else {
					if(node.children.length>0) {
						$(node.children).each(function(i) {
							getCheckedNodeArrFreeByParent(this);
						});
					}
				}
			}
		}
		
/************************************************************/
/***********       核心               ************************/
/***********       方法               ************************/
/************************************************************/
		this.each(function(){
			//插件实现代码
			if(!options.display){
				alert('不显示');
				return false;
			}
			/*if($.isEmpty(options.url)) {
				//alert('无法加载树，因缺失数据源，请配置url!');
				//return false;
				options.remote = false;
			}*/
			//树的容器必须为div
			if(!$.equalsIgnoreCase(this.tagName,'div')) {
				alert('rftree应该存在于div中');
				return false;
			}
			
			/**** 将dom元素绑定到root上 ****/
			var root = new Node('',this.id);
			root.target = $(this);
			root.target.addClass('tree');
			if(options.grid) {
				root.target.addClass('tree-border');
			}
			options.root = root;
			
			//初始化//展开根节点
			if(options.lazy) {
				expand(options.root);
			} else {
				
				if(options.remote){
					load(options.root);
				}else{
					loadData(options.data,options.root);
				}
				
			}
		}); //this.each(fn) //end
		

/************ 暂时不用的方法 ****************/
		/**
		 * 通过jquery的选择器及过滤器在dom中查找
		 * 这种方法效率较低，暂不使用，留作备用
		 */
		function getChecked2(node) {
			var ckbox = $(node.target)
					.find("input[type='checkbox']")
					.filter(function(index){
						//不知什么原因，浏览器默认半选状态也为checked;
						if(this.checked==true 
						&& this.indeterminate==false) {
							return true;
						} else return false;
					})
					;
			return ckbox.length;
		}
		
		
/*** public  对外公开的方法  **********************************************************/		
		/**
		 * 使用checkbox时返回已选中的节点id总数
		 */
		$.fn.tree.getCheckedSum = function(_tree){
			if(!options.checkbox) {
				alert('找不到checkbox控件!');
				return null;
			}
			_tree = options.root;
			_sum = 0;
			getCheckedSumFree(_tree);
			return _sum;
		};
		/**
		 * 选中的id用‘,’连成的字符串
		 */
		$.fn.tree.getCheckedIdStr = function(_tree){
			if(!options.checkbox) {
				alert('找不到checkbox控件!');
				return null;
			}
			_tree = options.root;
			_ids = '';
			if(options.checkMode=='related') {
				getCheckedIdsRelated(_tree);
			} else if(options.checkMode=='free') {
				getCheckedIdsFree(_tree);
			}
			_ids = _ids.substring(0, _ids.length-1);
			return _ids;
		};
		/**
		 * 选中的id数组
		 */
		$.fn.tree.getCheckedIdArr = function(_tree){
			if(!options.checkbox) {
				alert('找不到checkbox控件!');
				return null;
			}
			_tree = options.root;
			_idArr = new Array();
			getCheckedIdArrFree(_tree);
			return _idArr;
		};
		
		/**
		 * 按照最大限度的父节点返回ID
		 * （如某节点下所有子节点为选中状态，则只返回该节点的id）
		 */
		$.fn.tree.getCheckedIdArrByParent = function(_tree) {
			if(!options.checkbox) {
				alert('找不到checkbox控件!');
				return null;
			}
			_tree = options.root;
			_idArr_p = new Array();
			getCheckedIdArrFreeByParent(_tree);
			return _idArr_p;
		}
		
		/**
		 * 按照最大限度的父节点返回Text
		 * （如某节点下所有子节点为选中状态，则只返回该节点的id）
		 */
		$.fn.tree.getCheckedTextArrByParent = function(_tree) {
			if(!options.checkbox) {
				alert('找不到checkbox控件!');
				return null;
			}
			_tree = options.root;
			_textArr_p = new Array();
			getCheckedTextArrFreeByParent(_tree);
			return _textArr_p;
		}
		
		/**
		 * 选中的Node数组
		 */
		$.fn.tree.getCheckedNodeArr = function(_tree){
			if(!options.checkbox) {
				alert('找不到checkbox控件!');
				return null;
			}
			_tree = options.root;
			_nodes = new Array();
			getCheckedNodeArrFree(_tree);
			return _nodes;
		};
		
		/**
		 * 按照最大限度的父节点返回Node
		 * （如某节点下所有子节点为选中状态，则只返回该节点）
		 */
		$.fn.tree.getCheckedNodeArrByParent = function(_tree) {
			if(!options.checkbox) {
				alert('找不到checkbox控件!');
				return null;
			}
			_tree = options.root;
			_nodes_p = new Array();
			getCheckedNodeArrFreeByParent(_tree);
			return _nodes_p;
		}
		
		/**
		 * 得到改变的节点
		 */
		$.fn.tree.getChangedIds = function(_tree){
			alert('该功能目前尚在开发中');
		};
		
		/**
		 * 重新刷新树
		 */
		$.fn.tree.reload = function(_tree){
			document.getElementById("rftree").innerHTML = "";
			currNode = null;
			//初始化//展开根节点
			if(options.lazy) {
				expand(options.root);
			} else {
				
				if(options.remote){
					load(options.root);
				}else{
					loadData(options.data,options.root);
				}
				
			}
		};
		
		
		
	}; //$.fn.tree(options) //end
	
})(jQuery); 