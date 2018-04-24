// Enter Key Tapping - Start::
browserName = navigator.appName;

if (browserName == "Netscape")
{
    document.captureEvents(Event.KEYPRESS);
    document.onkeypress=NetEnterKey;
}
else
{
    if (browserName.indexOf("Explorer") >= 0)
        {
            document.onkeypress=IEEnterKey;
        }
}


function IEEnterKey()
{
    if(window.event.keyCode == 13)
        {
            SetfocusSubmit(window.event.srcElement)
                event.returnValue=false;
        }
}

function NetEnterKey(e)
{
    key = e.which;
    if(key == 13){
        SetfocusSubmit( e.target )
            return false;
    }
}
//issue 468436
function SetfocusSubmit( target )
{
  if(target.name == 'pagenum')
  {
    getRecords('jump');
  }
  else
  {
    dispatchSearch();
  }
}
/*
function SetfocusSubmit( target )
{
    if ( target.form == document.search_form )
        {
            onSearch();
        }
    else if (target.form == document.search_footer_form)
        {

            getRecords('jump');
        }
    else if (target.form == document.result_form)
        {


            dispatchSearch();
        }
    else
    {
      var js = target.toString();
      eval(js);
    }
}
*/
//end issue 468436
// Enter Key Tapping - End.


// Scrollable table
function resizeScrollableTables()
{
  if (!document.layers)
  {
      var table_id = 'result_table';
      var slave_id = 'result_table_slave';
      var width = document.body.offsetWidth - 50;
      var height = document.body.offsetHeight/2 - 30;
      var slave_width = 200;
      // resize table   approx
      
      
      if (document.result_form.FROZEN_SEQUENCE == null || document.result_form.FROZEN_SEQUENCE.value == '' || document.result_form.FROZEN_SEQUENCE.value == '-999')
      {
      	i2uiResizeScrollableArea(table_id, height, width, null, null, null,null, null);
      }
      else
      {
        i2uiResizeScrollableArea(table_id,height,30,slave_id,20,56,null, null);
      }
      i2uiResizeScrollableContainer('button_container',document.body.offsetHeight - 50, null, width + 15, true, 'yes');
      i2uiResizeScrollableContainer('result_container',document.body.offsetHeight - 150, null, width + 25, true, 'yes');
      var rowselectorobj = document.getElementById("_rowselector_header");
      if (rowselectorobj != null)
      	toggleRowSelectionState(rowselectorobj, -1, null, true);

  }
}

// On Load
function onLoad()
{
  resizeScrollableTables();
  setFocus();
}

// On Resize
function onResize()
{
  resizeScrollableTables();
  setFocus();
}


// Setting Focus
function setFocus()
{
  var elementsLen = document.result_form.elements.length;

  for(count = 0; count < elementsLen; count++)
  {
    if(
      document.result_form.elements[count].type != "hidden"
      )
      {
        document.result_form.elements[count].focus();
        return;
      }
  }
}


// Select All CheckBox
function checkAll(field)
{
    for (i = 0; i < field.length; i++)
        field[i].checked = true ;
}

function uncheckAll(field)
{
    for (i = 0; i < field.length; i++)
        field[i].checked = false ;
}

function toggleCheckboxes(_form, _field, _all)
{
    if(!_field) return;

    var numOfAlerts = _field.length;
    var select = _all.checked;
    var selectedAlerts = 0;

    if ( numOfAlerts == 1 )
        {
            eval("_field.checked = " + select);
        }

    if ( numOfAlerts > 1 )
        {
            for(i=0; i<numOfAlerts; i++)
                {
                    eval("_field[" + i + "].checked = " + select);
                }
        }
    else
        {
            _field.checked = select;
        }
    return;

}

// other than SELECT_ALL
function checkifAnySelected(form)
{
    var count;
    var elementsLen = form.elements.length;
    var foundChecked = false;

    for(count = 0; count < elementsLen; count++)
        {
            if(
               form.elements[count].type == "checkbox" &&
               form.elements[count].checked == true  &&
               form.elements[count].name != "SELECT_ALL"
               )
                {
                    foundChecked = true;break;
                }
            else if (
							 form.elements[count].type == "radio" &&
							 form.elements[count].checked == true
						)
            {
                    foundChecked = true;
                    break;

            }
        }
    return foundChecked;
}

function onSelectRow(pId)
{
}

//  Calendar
function showCalendar(fieldName)
{
    setDateField(fieldName);
}


// Sorting
function sort(value, sequence, isSortable, isFrozenAllowed, noOfColumns)
{
    //alert("value is "+ isSortable);
    document.result_form.SELECTED_COLUMN.value=value;
    document.result_form.COLUMN_SEQUENCE.value=sequence;
    document.result_form.COLUMN_COUNT.value=noOfColumns;
    if (isSortable == 'yes')
    {
    	if (isFrozenAllowed == 'yes')
    	{
    	    i2uiShowMenu('sortOrder');
    	}
    	else
    	{
            i2uiShowMenu('sortOrder_noFreeze');    	
    	}
    
    }
    else
    {
    	if (isFrozenAllowed == 'yes')
    	{
            i2uiShowMenu('editorMenu');    	
    	
    	}
    	else
    	{
            i2uiShowMenu('editorMenu_noFreeze');    	
    	
    	}
    
    }
}

function sortOrder(order)
{
    // fixed for issue 453596
    document.result_form.SORT_BY.value=document.result_form.SELECTED_COLUMN.value;
    document.result_form.HIDDEN_COLUMN.value='';
    //alert(document.result_form.HIDDEN_COLUMN.value);
    document.result_form.SORT_ORDER.value=order;
    document.result_form.DO_SEARCH.value='yes';
    document.result_form.START_COUNT.value=0;
    document.result_form.target="appFrame";
    //document.result_form.submit();
    //issue 470910
    dispatchSearch();
}
// anish for hiding columns
// Sorting
function showMenu(value)
{
    //alert("show javascript menu");
    //document.result_form.reset();
    document.result_form.HIDDEN_COLUMN.value=value;
    i2uiShowMenu('editorMenu');
}

// Advanced Search
function onAdvancedSearch()
{
    document.search_form.SEARCH_TYPE.value='Advanced';
    document.search_form.submit();
}

// Advanced Search
function onSimpleSearch()
{
    document.search_form.SEARCH_TYPE.value='Simple';
    document.search_form.submit();
}

// Searching
function onSearch()
{
    if ( validate() == true )
        {
            document.search_form.START_COUNT.value=0;
            document.search_form.DO_SEARCH.value='Yes';
            document.search_form.submit();
        }
}

// Saving Search
function onSaveSearch()
{
    if ( validateSaveSearch() == true )
        {
            document.search_form.action='../search/save_search/saveSearch.cmd';
            document.search_form.submit();
        }
}

// downloading xml
function downloadDocument()
{
    prevAction = document.result_form.action;

    if ( checkifAnySelected(result_form) == true )
        {
            document.result_form.action='../report/report/downloadDocument.cmd';
            document.result_form.submit();
        }
    else
        {
            core_alert("PLEASE_SELECT_TRANSACTION");
        }

    document.result_form.action= prevAction;
}


// Form Validations
function validateSaveSearch()
{
    var count;
    var elementsLen = document.search_form.elements.length;
    var foundFilledField = false;
    var msg = "";

    for(count = 0; count < elementsLen; count++)
        {
            if(
               document.search_form.elements[count].type !="hidden" &&
               document.search_form.elements[count].value != "" )
                {
                    foundFilledField = true;break;
                }
        }
    if (foundFilledField == false)
        {
            msg += "Please specify search criteria";

            return false;
        }
    return validate();
}


function validate()
{
    var count;
    var elementsLen = document.search_form.elements.length;
    var success = true;
    var foundFilledField = false;
    var msg = "";

    //Validate fields
    for(count = 0; count < elementsLen; count++)
        {
            var name = document.search_form.elements[count].name;



            if( document.search_form.elements[count].fieldtype == "Number" &&
                document.search_form.elements[count].value != "" )
                {
                    if ( isNaN(document.search_form.elements[count].value) )
                        {
                            msg += name + " should be a valid number";
                            success = false;
                            break;
                        }
                }

            if( document.search_form.elements[count].fieldtype == "Date" &&
                document.search_form.elements[count].value != "" )
                {
                    /*  removed date validation for internationalization and time issues - uwe      if ( IsDate(document.search_form.elements[count].value) == false )
                        {
                        msg += name + " should be a valid date";
                        success = false;
                        break;
                        }    */
                }
        }

    return success;
}


// Pagination
function getRecords(actionName, startCount, result_form, page_form)
{
    if(page_form == null)  page_form = document.search_footer_form;
    if(page_form == null)  page_form = document.result_form;

    if(result_form == null || result_form == 'null')  result_form = document.result_form;

    jumpToPage(actionName, startCount, result_form, page_form);
}

  function jumpToPage(actionName, startCount, result_form, page_form)
{
	//alert("hello");
	//alert("actionName="+actionName);
	//alert("startCount="+startCount);
	//alert("result_form="+result_form);
	//alert("page_form="+page_form);
	//alert("maxRows=" + maxRows);
	var nextCount = parseInt(startCount) + maxRows;
	var prevCount = 0;

	if ( parseInt(startCount) > 0 )
		prevCount = parseInt(startCount) - maxRows;

	if (startCount == null)
	  startCount=page_form.pagenum.value;

	var pagenum= parseInt(startCount);  pagenum--;

	if (actionName == "jump")
	{
	  if(( page_form.RECORD_COUNT.value == 0 || page_form.RECORD_COUNT.value > pagenum*maxRows)  && (pagenum+1>0) && (page_form.START_COUNT.value != pagenum*maxRows))
	  {
		  result_form.reset();
		  //by anish
		  disableUnUsedFilterElements();
		  result_form.DO_SEARCH.value='Yes';
		  result_form.START_COUNT.value=pagenum*maxRows;
		  result_form.target="appFrame";
		  //issue 456064
		  document.result_form.action="tableeditor.jsp";
		  // end 456064
 			displayBusyBox();
		  result_form.submit();
		}
	else
	{
	  core_alert("PAGINATION_ALERT");
	}
	 }
}

///////////////////////////////////////////////////   SEARCH FUNCTIONS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	function hideIt()
	{
		var noOfColumns = document.result_form.COLUMN_COUNT.value;
		if (parseInt(noOfColumns) < 2)
		{
			core_alert("CANNOT_HIDE_ONLY_COLUMN");
			return;
		}
			
			
		document.result_form.HIDDEN_COLUMN.value=document.result_form.SELECTED_COLUMN.value
		//alert("hide=" + document.result_form.HIDDEN_COLUMN.value);
		//document.result_form.DO_SEARCH.value='Yes';
		//alert("hide=" + document.result_form.DO_SEARCH.value);
		//document.result_form.START_COUNT.value=0;
		document.result_form.action="tableeditor.jsp";
		document.result_form.target="appFrame";		
		document.result_form.submit();
	}
	function showAll()
	{
		document.result_form.HIDDEN_COLUMN.value='None';
		//alert("hide=" + document.result_form.HIDDEN_COLUMN.value);
		//document.result_form.DO_SEARCH.value='Yes';
		//alert("hide=" + document.result_form.DO_SEARCH.value);
		//document.result_form.START_COUNT.value=0;
		document.result_form.action="tableeditor.jsp";
		document.result_form.target="appFrame";		
		document.result_form.submit();
	}
	function freeze()
	{
		//chandru start
		var i = "1";
		document.result_form.FROZEN_COLUMN.value=document.result_form.SELECTED_COLUMN.value;
		document.result_form.FROZEN_SEQUENCE.value=parseInt(document.result_form.COLUMN_SEQUENCE.value) + parseInt(i);
		//document.result_form.DO_SEARCH.value='Yes';
		//document.result_form.START_COUNT.value=0;
		document.result_form.action="tableeditor.jsp";
		document.result_form.target="appFrame";		
		document.result_form.submit();
		//chandru end
	}
	function unfreeze()
	{
		//chandru start
		document.result_form.FROZEN_COLUMN.value='None';
		document.result_form.FROZEN_SEQUENCE.value=-999;
		//document.result_form.DO_SEARCH.value='Yes';
		//document.result_form.START_COUNT.value=0;
		document.result_form.action="tableeditor.jsp";
		document.result_form.target="appFrame";		
		document.result_form.submit();
		//chandru end
	}
	function dispatchSearch()
	{
		//alert("anish filter it");
		if(validateSearchParam())
		{
			disableUnUsedFilterElements();
			document.result_form.target="appFrame";
			document.result_form.DO_SEARCH.value='Yes';
			document.result_form.START_COUNT.value=0;
			document.result_form.action="tableeditor.jsp";
			displayBusyBox();
			document.result_form.submit();
		}
  	}


///////////////////////////////////////////////////   ACTION FUNCTIONS \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	function disableUnUsedFilterElements() {
	  var elements = document.result_form.elements;
	  var elementCount = elements.length;
	  var element;	
	  for (var i = 0; i < elementCount; i++) {
	  	element = elements[i];	
	  	if ((element.tagName=='INPUT' || element.tagName=='SELECT') && (element.name.indexOf('F_') != -1 ))
	  	{
			element.value = trimString(element.value);
			if (element.value == '') {
		  		element.disabled = true;
			}
		}
	  }
	}

	  function findWhereUsed() {
		disableUnUsedFilterElements();
	  if ( checkifAnySelected(result_form) == true )
	   {
		 if ( checkifOneSelected(result_form) == true )
		   {
			document.result_form.target="appFrame";
			document.result_form.action="TableEditorController/findWhereUsed.cmd";
			document.result_form.submit();
		   }
		   else
			{
			  core_alert("PLEASE_SELECT_ONLY_ONE_TRANSACTION");
			}
		}
	  else
		{
		  core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
		}
	  }


	  function onGroupEdit()
	  	  {
		if( !ifAllChecked('result_form') && checkifAnySelected(result_form))
		{
		  var count = countSelectedRecords();
		  core_alert("You are going to update ({0}) records",count);
		}

		disableUnUsedFilterElements();


		if( ifAllChecked('result_form') && checkifAnySelected(result_form))
		{
			   if ( updateAll == 'false' )
			   {
				 groupEditSelected ();
				}
			   else if (totalRecordCount <= maxRows )
			   {
				 groupEditSelected ();

			   }
			   else if ( core_confirm("Do you want to update all the records ({0}) in the table? If you select 'No', only the records displayed in the current page will be updated.",totalRecordCount) == 'yes' )
			   {
				   if   ( totalRecordCount >= exceptionThreshold )
					{
						core_alert("EXCEPTION_THRESHOLD_WARNING");
						return;
					}
				   else if (  (totalRecordCount < exceptionThreshold) && (totalRecordCount >= warnThreshold) )
					{
						   core_alert("WARNING_THRESHOLD_ALERT");
						   groupEditAll ();
					}
				   else
					{
						  groupEditAll ();
					}


			   }
			   else
				   groupEditSelected ();

		}
		else if (! ifAllChecked('result_form')  && checkifAnySelected(result_form) == true  )
		{
			groupEditSelected ();
		}
		else
		{
		  core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
		}
	  
	  
	  }

	  function groupEditSelected ()
	  {
				disableUnUsedFilterElements();
				document.result_form.target="appFrame";
				document.result_form.UPDATE_ALL.value="NO";
				document.result_form.TABLE_ROW_OPERATION.value = "GROUP_EDIT";
				document.result_form.action="tablerow.jsp";
				document.result_form.submit();
	  }
	 function groupEditAll ()
	  {
				disableUnUsedFilterElements();
				document.result_form.target="appFrame";
				document.result_form.UPDATE_ALL.value="YES";
				document.result_form.TABLE_ROW_OPERATION.value = "GROUP_EDIT";
				document.result_form.action="tablerow.jsp";
				document.result_form.submit();
	  }
	  function onSelectForeignKey()
	  {
					disableUnUsedFilterElements();
					document.result_form.target="appFrame";
					document.result_form.action="TableEditorController/selectForeignKey.cmd";
					if(checkifAnySelected(result_form) == false)
					{
						core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");	
						return;
					}
					document.result_form.submit();
	  }
	  
	  function onCancel()
	  {
					disableUnUsedFilterElements();
					document.result_form.target="appFrame";
					document.result_form.action="TableEditorController/cancel.cmd";
					document.result_form.submit();
	  }
	  

	  function onCopy()
	  {
		  disableUnUsedFilterElements();
		  if ( checkifAnySelected(result_form) == true )
		  {
			var selectedRowCount = 0;
			for(i = 0; i < result_form.elements.length; i++) {
			  if(result_form.elements[i].name == 'SELECTED_ID' && result_form.elements[i].checked ) {
				selectedRowCount++;
			  }
			}
			if(selectedRowCount > 1) {
			  core_alert("PLEASE_SELECT_ONLY_ONE_TRANSACTION");
			  return;
			}

			  document.result_form.target="appFrame";
			  document.result_form.TABLE_ROW_OPERATION.value = "COPY";
			  document.result_form.action="tablerow.jsp";
			  document.result_form.submit();
		  }
		  else
		  {
			core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
		  }
	  }

	  function onEdit()
	  {
	  	//added for 3M
		  var selectedRowCount = 0;
		  for(i = 0; i < result_form.elements.length; i++)
		  {
			if(result_form.elements[i].name == 'SELECTED_ID' && result_form.elements[i].checked )
			{
				selectedRowCount++;
			}
		  }
		//alert(selectedRowCount);
		if(selectedRowCount > 1)
		{
			core_alert("You are going to update ({0}) records",selectedRowCount);			
		}	
		//added for 3M
		
		disableUnUsedFilterElements();
		if ( checkifAnySelected(result_form) == true )
		{
			document.result_form.target="appFrame";
			document.result_form.TABLE_ROW_OPERATION.value = "EDIT";
		    document.result_form.action="tablerow.jsp";
			document.result_form.submit();
		}
		else
		{
		  core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
		}
	  }

   function checkifOneSelected(form)
   {
		var count;
		var numOfChecks = 0;
		var elementsLen = form.elements.length;
		var oneCheck = true;

		for(count = 0; count < elementsLen; count++)
		{
		  if( form.elements[count].type == "checkbox" && form.elements[count].checked == true  &&
			 form.elements[count].name != "SELECT_ALL"  )
		   {
			 numOfChecks++;
		   }
		}
		if (numOfChecks > 1)
		{
		  oneCheck = false;
		}
		return oneCheck;
   }

	  function onDelete()
	  {
	   var confirmMesg = "CONFIRM_ON_DELETE";

		if ( checkifAnySelected(result_form) == true )
		{
		  if( core_confirm( confirmMesg ) == 'yes' )
		   {
		    disableUnUsedFilterElements();
			document.result_form.target="appFrame";
			document.result_form.DO_SEARCH.value = 'Yes';
			document.result_form.START_COUNT.value = 0;
			document.result_form.action="TableEditorController/deleteDBRows.cmd";
			displayBusyBox();
			document.result_form.submit();
		   }
			//core_success("Rows Deleted...");
		}
		else
		{
		  core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
		}
	  }
	  function onDetails()
	  {

		if ( checkifAnySelected(result_form) == true )
		{
			disableUnUsedFilterElements();
			document.result_form.target="appFrame";
			document.result_form.action="../dbrow/view/dbrow.jsp";
			document.result_form.submit();
		}
		else
		{
		  core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
		}
	  }
	  function onAdd()
	  {
		   disableUnUsedFilterElements();
		   document.result_form.action="tablerow.jsp";
		   document.result_form.TABLE_ROW_OPERATION.value = "ADD";
		   document.result_form.target="appFrame";
		   document.result_form.submit();
	  }
	  
	  function doSomething(f)
	  {
			dispatchSearch();
	  }
	  function clearDateFields(f)
	  {
			dispatchSearch();
	  }
	  function onClearFilter()
	  {
		  disableUnUsedFilterElements();
		  document.result_form.target="appFrame";
		  document.result_form.action="TableEditorController/clearFilter.cmd";
		  displayBusyBox( );
		  document.result_form.submit();
	  }
	  
	  //checking whether the page have a checkbox or radio button
	  function checkType(form)
	  {	
	  	//alert("inside checkType");
	  	var count;
		var elementsLen = form.elements.length;
		var typeCheck = false;
		var type;
		for(count = 0;count < elementsLen;count++)
		{
		    if(form.elements[count].name == "SELECT_ALL" &&
		    	form.elements[count].type =="checkbox")
			{
			    typeCheck = true;
			    var type  = "checkBox";
			    break;
			}
			else
			{
			    typeCheck = false;
			    var type  = "radio";
			}
       		}
       		//alert(type);
       		//alert(typeCheck);
       		
       		return typeCheck;
	  }
	  function saveReport()
	  {
	  if(checkifAnySelected(result_form) == true)
	  {
	        	//alert(exportToExcelThreshold);
		if(checkType(result_form))
		{
			if( ifAllChecked('result_form') && (totalRecordCount > maxRows))
			{
				if (core_confirm(exportMesg)== 'yes')
				{
				    if(totalRecordCount > exportToExcelThreshold)
				    {				    	
				    	core_alert("Total number of selected records ({0}) are exceeding the Exception Threshold ({1})! Please narrow down your search criteria", totalRecordCount,exportToExcelThreshold);
					    return;
				    }
			            document.result_form.EXPORT_ALL.value = 'YES';
			  }
				else
				    document.result_form.EXPORT_ALL.value = 'NO';
			}
			else
			    document.result_form.EXPORT_ALL.value = 'NO';
			 
		}
		else
		{
			if(totalRecordCount > maxRows)
			{
				if(core_confirm(exportMesg) == 'yes' )
				{
					if(totalRecordCount > exportToExcelThreshold)
					{
						core_alert("Total number of selected records ({0}) are exceeding the Exception Threshold ({1})! Please narrow down your search criteria", totalRecordCount,exportToExcelThreshold);
						return;
				  }
					document.result_form.EXPORT_ALL.value = 'YES';
				}
				else
					document.result_form.EXPORT_ALL.value = 'NO';
				
			}
			else
			    document.result_form.EXPORT_ALL.value = 'NO';
		}

		  document.result_form.DIRECTORY.value = 'toolkit.framework.tableeditor';
		  document.result_form.FILE.value = 'TableEditorView';
		  
		  var prevTarget = document.result_form.target;
			var prevAction = document.result_form.action;

		  document.result_form.target = "i2ui_shell_bottom";
		  document.result_form.action='reportdb/controller/saveReport.cmd';
		  displayBusyBox( );
		  document.result_form.submit();
		  
		  document.result_form.target = prevTarget;
			document.result_form.action= prevAction;
			document.result_form.submit(); 
      	  }
      	  else
        	core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
	  }
	  
	 function onFilter()
	  {
			disableUnUsedFilterElements();
			document.result_form.target="appFrame";
			document.result_form.PAGE.disabled = 'true';
			document.result_form.action="../filter/controller/display.cmd";
			document.result_form.submit();
	  }

	function onSaveFilter()
	  {

			if( document.filter_form.FILTER_NAME.value == "" )
			{
			  core_alert("PROMPT_FILTER_NAME");
			  return;
			}

			document.result_form.FILTER_NAME.value =  document.filter_form.FILTER_NAME.value;
			if (document.filter_form.FILTER_SCOPE  && document.filter_form.FILTER_SCOPE.checked)
			  {
				document.result_form.FILTER_SCOPE.value =  'GLOBAL';
			   }
			else
				document.result_form.FILTER_SCOPE.value = 'LOCAL';
		    	disableUnUsedFilterElements();
			document.result_form.PAGE.disabled = 'true';
			document.result_form.target="appFrame";
			document.result_form.action="../filter/controller/saveFilter.cmd";
			document.result_form.submit();

	  }
	  
	   function dataUpload()
	   {			   		
			document.result_form.target="appFrame";
			document.result_form.PAGE.disabled = 'true';
			document.result_form.action=omxContextPath+ "/upload_kit/uploads/local_trans_upload_details.jsp";
			document.result_form.submit();
	   }


          function onCustomize()
	  {
	  			//alert('sdsdfsd');
	  			document.result_form.target="appFrame";
	  			document.result_form.PAGE.disabled = 'true';
	  			document.result_form.action="../customize/customize.jsp";
	  			document.result_form.submit();
	  }

          function onCreateFavorite()
	  {
	  			document.result_form.target="appFrame";
      document.result_form.action=omxContextPath+ "/core/search/controller/saveInSession.x2c";
	  			document.result_form.submit();
	  }

          function onManageFavorites()
	  {
	  			document.result_form.target="appFrame";
      document.result_form.action=omxContextPath+ "/core/search/controller/saveInSession.x2c";
	  			document.result_form.submit();
	  }
	  
	  
function toggleSingleRowSelectionState(obj, currentposition, className, retainHeaderState)
{
	var originalstate = className;
	if (className == null)
	{
		if (currentposition % 2 == true)
		    originalstate = "tableRow0";
		else
		    originalstate = "tableRow1";
	}		

	var slave_table = document.getElementById('result_table_slave_data');
	if (slave_table == null)
		i2uiToggleRowSelectionState(obj, originalstate ,'result_table', null, false);
	else
		i2uiToggleRowSelectionState(obj, originalstate ,'result_table_slave', null, false);
	
	var table = document.getElementById('result_table_data');
	if (obj.checked == true)
	{

		table.rows[currentposition].className = "rowHighlight";
	}
	else
	{
		table.rows[currentposition].className = originalstate;

	}
	var len = table.rows.length;
	var rowstate;
	for(var i=1; i<len; i++)
	{
		if (i== currentposition)
			continue;
		if (i % 2 == true)
		    rowstate = "tableRow0";
		else
		    rowstate = "tableRow1";
		table.rows[i].className = rowstate;
	}
	
}


function toggleRowSelectionState(obj, currentposition, className, retainHeaderState)
{
	
	
	var table = document.getElementById('result_table_data');
	var originalstate = className;
	if (currentposition == -1)
	{
		var slave_table = document.getElementById('result_table_slave_data');
		if (slave_table == null)
		 	toggleAllRowsSelectionState(obj, 'result_table_data');
		else
			toggleAllRowsSelectionState(obj, 'result_table_slave_data');
		
	}
	else
	{
		if (className == null)
		{
			if (currentposition % 2 == true)
			    originalstate = "tableRow0";
			else
			    originalstate = "tableRow1";
		}		
		var slave_table = document.getElementById('result_table_slave_data');
		if (slave_table != null)
	          {
                        i2uiToggleRowSelectionState(obj, originalstate ,'result_table_slave', null, true);
                   }
		if (obj.checked == true)
		{
		
			table.rows[currentposition].className = "rowHighlight";
		}
		else
		{
			table.rows[currentposition].className = originalstate;
			
		}
		if (retainHeaderState != true )
		{
			var rowselectorobj = document.getElementById("_rowselector_header");
			rowselectorobj.checked = false;	
		}
		
	}
	
}


function toggleAllRowsSelectionState(obj, tableId)
{
	  var tableobj = document.getElementById(tableId);
	  if (tableobj != null)
	  {
	    var checkboxes;

	    // IE has a document object per element
	    if(tableobj.document)
	      checkboxes = tableobj.document.getElementsByTagName("INPUT");
	    else
	      checkboxes = document.getElementsByTagName("INPUT");

	    if (checkboxes != null)
	    {
	      var len = checkboxes.length;
	      var rowNum = 1;
	      for(var i=0; i<len; i++)
	      {
		if (checkboxes[i].id == "_rowselector")
		{
		  //i2uiActiveRowSelector = checkboxes[i];
		  checkboxes[i].checked = obj.checked;
		  toggleRowSelectionState(checkboxes[i], rowNum, null, true);
		  /*
		  var onclickhandler = checkboxes[i].onclick+"!!!";
		  var from = onclickhandler.indexOf("{");
		  onclickhandler = onclickhandler.substring(from+1);
		  var to = onclickhandler.lastIndexOf("}");
		  onclickhandler = onclickhandler.substring(0,to);
		  //alert(onclickhandler);
		  eval(onclickhandler);
		  */
		  var rowNum = rowNum + 1;
		}
	      }
	      //i2uiActiveRowSelector = null;
	    }
	  }

}

function back()
{
  document.result_form.target="appFrame";
  document.result_form.action=omxContextPath+ "/core/history/back.cmd";
  document.result_form.submit();
}
//for 3M requirement 
function onGenerate()
{
	var selectedRowCount = 0;
	for(i = 0; i < result_form.elements.length; i++)
	{
		if(result_form.elements[i].name == 'SELECTED_ID' && result_form.elements[i].checked )
		{
			selectedRowCount++;
		}
	}
	
	if(selectedRowCount > 1)
	{
		core_alert("Please select only one record");
	}	
	//added for 3M

	disableUnUsedFilterElements();
	if ( checkifAnySelected(result_form) == true )
	{
		document.result_form.target="appFrame";
		document.result_form.action=omxContextPath+ "/toolkit/framework/hierarchy/timeHierarchy/HorizonRefDetails/getHorizonDetails.cmd";
		displayBusyBox();
		document.result_form.submit();
	}
	else
	{
	  core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
	}
		
	
}

function onDetails()
{
	
	var selectedRowCount = 0;
	for(i = 0; i < result_form.elements.length; i++)
	{
		if(result_form.elements[i].name == 'SELECTED_ID' && result_form.elements[i].checked )
		{
			selectedRowCount++;
		}
	}
	
	if(selectedRowCount > 1)
	{
		core_alert("Please select only one record");
	}	
	disableUnUsedFilterElements();
	if ( checkifAnySelected(result_form) == true )
	{
			document.result_form.target="appFrame";
			document.result_form.action=omxContextPath+ "/toolkit/framework/hierarchy/timeHierarchy/HorizonRefDetails/getHorizonReferenceDetails.cmd";
			document.result_form.submit();
	}
	else
	{
		  core_alert("PLEASE_SELECT_ATLEAST_ONE_TRANSACTION");
	}
}
