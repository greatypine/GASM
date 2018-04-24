$(document).ready(function() { 

	// Navigation menu

	$('ul#navigation').superfish({ 
		delay:       1000,
		animation:   {opacity:'show',height:'show'},
		speed:       'fast',
		autoArrows:  true,
		dropShadows: false
	});
	
	$('ul#navigation li').hover(function(){
		$(this).addClass('sfHover2');
	},
	function(){
		$(this).removeClass('sfHover2');
	});

	// Accordion
	$("#accordion, #accordion2").accordion({ header: "h3" });

	// Tabs
	$('#tabs, #tabs2, #tabs5').tabs();

	// Dialog			
	$('#dialog').dialog({
		autoOpen: false,
		width: 600,
		bgiframe: false,
		modal: false,
		buttons: {
			"Ok": function() { 
				$(this).dialog("close"); 
			}, 
			"Cancel": function() { 
				$(this).dialog("close"); 
			} 
		}
	});
	
	// Login Dialog			
	$('#login').dialog({
		autoOpen: false,
		width: 300,
		height: 130,
		bgiframe: true,
		modal: true,
		buttons: {
			"确定": function() { 
				urlStr = "../logout.do";
				window.location.href = urlStr;
			}, 
			"取消": function() { 
				$(this).dialog("close"); 
			} 
		}
	});
	
	// Dialog Link
	$('#dialog_link').click(function(){
		$('#dialog').dialog('open');
		return false;
	});

	// Dialog auto open			
	$('#welcome').dialog({
		autoOpen: true,
		width: 470,
		height: 180,
		bgiframe: true,
		modal: true,
		buttons: {
			"View Admintasia V1.0": function() { 
				$(this).dialog("close"); 
			}			
		}
	});

	// Dialog auto open			
	$('#welcome_login').dialog({
		autoOpen: true,
		width: 370,
		height: 430,
		bgiframe: true,
		modal: true,
		buttons: {
			"Proceed to demo !": function() {
				window.location = "index.php";
			}
		}
	});

	// Datepicker
	$('#datepicker').datepicker({
		inline: true
	});
	
	//Hover states on the static widgets
	$('#dialog_link, ul#icons li').hover(
		function() { $(this).addClass('ui-state-hover'); }, 
		function() { $(this).removeClass('ui-state-hover'); }
	);
	
	//Sortable

	$(".column").sortable({
		connectWith: '.column'
	});

	//Sidebar only sortable boxes
	$(".side-col").sortable({
		axis: 'y',
		connectWith: '.side-col'
	});

	$(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
		.find(".portlet-header")
			.addClass("ui-widget-header")
			.prepend('<span class="ui-icon ui-icon-circle-arrow-s"></span>')
			.end()
		.find(".portlet-content");

	$(".portlet-header .ui-icon").click(function() {
		$(this).toggleClass("ui-icon-circle-arrow-n");
		$(this).parents(".portlet:first").find(".portlet-content").slideToggle();
	});

	//$(".column").disableSelection();


	/* Table Sorter */
	$("#sort-table")
	.tablesorter({
		widgets: ['zebra'],
		headers: { 
		            // assign the secound column (we start counting zero) 
		            0: { 
		                // disable it by setting the property sorter to false 
		                sorter: false 
		            }, 
		            // assign the third column (we start counting zero) 
		            6: { 
		                // disable it by setting the property sorter to false 
		                sorter: false 
		            } 
		        } 
	})
	
	.tablesorterPager({container: $("#pager")}); 

	$(".header").append('<span class="ui-icon ui-icon-carat-2-n-s"></span>');

	
});

	/* Tooltip */

	$(function() {
		$('.tooltip').tooltip({
			track: true,
			delay: 0,
			showURL: false,
			showBody: " - ",
			fade: 250
			});
		});
		
	/* Theme changer - set cookie */

    $(function() {
       
		$("link[title='style']").attr("href","css/styles/default/ui.css");
        $('a.set_theme').click(function() {
           	var theme_name = $(this).attr("id");
			$("link[title='style']").attr("href","css/styles/" + theme_name + "/ui.css");
			$.cookie('theme', theme_name );
			$('a.set_theme').css("fontWeight","normal");
			$(this).css("fontWeight","bold");
        });
		
		var theme = $.cookie('theme');
	    
		if (theme == 'default') {
	        $("link[title='style']").attr("href","css/styles/default/ui.css");
	    };
	    
		if (theme == 'light_blue') {
	        $("link[title='style']").attr("href","css/styles/light_blue/ui.css");
	    };
	

	/* Layout option - Change layout from fluid to fixed with set cookie */
       
       $("#fluid_layout a").click (function(){
			$("#fluid_layout").hide();
			$("#fixed_layout").show();
			$("#page-wrapper").removeClass('fixed');
			$.cookie('layout', 'fluid' );
       });

       $("#fixed_layout a").click (function(){
			$("#fixed_layout").hide();
			$("#fluid_layout").show();
			$("#page-wrapper").addClass('fixed');
			$.cookie('layout', 'fixed' );
       });

	    var layout = $.cookie('layout');
	    
		if (layout == 'fixed') {
			$("#fixed_layout").hide();
			$("#fluid_layout").show();
	        $("#page-wrapper").addClass('fixed');
	    };

		if (layout == 'fluid') {
			$("#fixed_layout").show();
			$("#fluid_layout").hide();
	        $("#page-wrapper").addClass('fluid');
	    };
	
    });
    
    
 	/* Check all table rows */
	
var checkflag = "false";
function check(field) {
if (checkflag == "false") {
for (i = 0; i < field.length; i++) {
field[i].checked = true;}
checkflag = "true";
return "check_all"; }
else {
for (i = 0; i < field.length; i++) {
field[i].checked = false; }
checkflag = "false";
return "check_none"; }
}