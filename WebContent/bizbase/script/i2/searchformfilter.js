// Enter Key Tapping - Start::
browserName = navigator.appName;
var formName = 'result_form';

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

// checks if all check box is selected
function checkifAllSelected(form)
{
    var count;
    var numOfChecked = 0;
    var temp =  "document." + form + ".elements.length" ;
    var elementsLen = eval(temp);
    var foundChecked = false;
    var numOfCheckBoxes = 0;

    for(count = 0; count < elementsLen; count++)
        {
            var type = "document."+ form + ".elements[" + count + "].type";
            type = eval(type);
            var checked = "document."+ form + ".elements[" + count + "].checked";
            checked = eval(checked);
            //alert("type = " + type + " checked = " + checked);
            if(

                 type == "checkbox" &&
                 checked == true
               )
               {
                 numOfChecked++;

               }
            if(type == "checkbox" )
               {
                 numOfCheckBoxes++;

               }
        }
    if(numOfChecked == numOfCheckBoxes)
     foundChecked = true;
    //alert("numOfChecked" + numOfChecked);
    //alert("numOfCheckBoxes" + numOfCheckBoxes);

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
/*
function sort(value)
{
    //document.result_form.reset();
    document.result_form.SORT_BY.value=value;
    document.result_form.action="dbformfilter.jsp";
    i2uiShowMenu('sortOrder');
}*/

/*
function sort(value, sequence, isSortable, isFrozenAllowed, form_name, noOfColumns)
{
    form = document.forms[form_name];
    if (form == null)
    	form = document.result_form;
    else
    	formName = form_name;
    form.SELECTED_COLUMN.value=value;
    form.COLUMN_SEQUENCE.value=sequence;
    form.COLUMN_COUNT.value=noOfColumns;    
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

function form_sortOrder(order)
{
    form = document.forms[formName];
    if (form == null)
    	form = document.result_form;
    form.SORT_BY.value=form.SELECTED_COLUMN.value;
    form.SORT_ORDER.value=order;
    form.DO_SEARCH.value='yes';
    if (form.START_COUNT)
 	 form.START_COUNT.value=0;
    form.submit();
}
*/

function showMenu(value)
{
    //document.result_form.reset();
    document.result_form.HIDDEN_COLUMN.value=value;
    i2uiShowMenu('editorMenu'); 
}


/*
function form_freeze()
{
        form = document.forms[formName];
        if (form == null)
 	     form = document.result_form;
	
	form.FROZEN_COLUMN.value=form.SELECTED_COLUMN.value;
	form.FROZEN_SEQUENCE.value=form.COLUMN_SEQUENCE.value;
	form.DO_SEARCH.value='Yes';
	//form.START_COUNT.value=0;
	form.USE_OLD_FILTER.value='YES';
	form.submit();
	//chandru end
}
function form_unfreeze()
{
        form = document.forms[formName];
        if (form == null)
 	     form = document.result_form;
	form.FROZEN_COLUMN.value='None';
	form.FROZEN_SEQUENCE.value=-999;
	form.DO_SEARCH.value='Yes';
	form.USE_OLD_FILTER.value='YES';
	//form.START_COUNT.value=0;
	form.submit();
}

*/




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

//checking whether the page have a checkbox or radio button
  function checkType(form)
  {	
	//alert("inside checkType/queryForm");
	var count;
	var elementsLen = form.elements.length;
	var typeCheck = false;
	var type;
	for(count = 0;count < elementsLen;count++)
	{
	    if(form.elements[count].name == "SELECT_ALL" ||
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
// download csv


function onExportToExcelGlobal(fileName, fileFormat)
{
    var selRows = getSelectedRowIndices();
    var exportAll;
    prevAction = document.result_form.action;
      if (window.parent.parent.push_frame)	//scmui frames
      {
        document.result_form.target = "push_frame";
      }
      else					//BCM frames
      {
        document.result_form.target = "i2ui_shell_bottom";
      }
            
      //alert(exportToExcelThreshold);
    if ( checkifAnySelected(result_form) == true )
        {
		if(checkType(result_form))
		{
		
			if( ifAllChecked('result_form') &&  (parseInt(totalRecordCount) > parseInt(max_rows)))
			{
				if (core_confirm( exportMesg ) == 'yes' )
				{
            if(totalRecordCount > exportToExcelThreshold)
				    {				    	
				    	core_alert("Total number of selected records ({0}) are exceeding the Exception Threshold ({1})! Please narrow down your search criteria", totalRecordCount,exportToExcelThreshold);
					    return;
				    }
            
				    exportAll = 'YES';
				    document.result_form.action=omxContextPath+ "/toolkit/framework/util/exportToExcel.cmd?EXPORT_ALL="+exportAll+"&FILE_NAME="+fileName+"&FILE_FORMAT="+fileFormat;
				    document.result_form.submit();
				}
				else
				{
				    exportAll = 'NO';
				    document.result_form.action=omxContextPath+ "/toolkit/framework/util/exportToExcel.cmd?SELECTED_ROW_NUMS="+selRows+"&EXPORT_ALL="+exportAll+"&FILE_NAME="+fileName+"&FILE_FORMAT="+fileFormat;
				    document.result_form.submit();
				}
			}
			else
			{
			    exportAll = 'NO';
			    document.result_form.action=omxContextPath+ "/toolkit/framework/util/exportToExcel.cmd?SELECTED_ROW_NUMS="+selRows+"&EXPORT_ALL="+exportAll+"&FILE_NAME="+fileName+"&FILE_FORMAT="+fileFormat;
			    document.result_form.submit();
			}
		}
		else
		{
			if((parseInt(totalRecordCount) > 1))
			{
				if (core_confirm( exportMesgRadio ) == 'yes' )
				{
            if(totalRecordCount > exportToExcelThreshold)
				    {				    	
				    	core_alert("Total number of selected records ({0}) are exceeding the Exception Threshold ({1})! Please narrow down your search criteria", totalRecordCount,exportToExcelThreshold);
					    return;
				    }
				    exportAll = 'YES';
				    document.result_form.action=omxContextPath+ "/toolkit/framework/util/exportToExcel.cmd?EXPORT_ALL="+exportAll+"&FILE_NAME="+fileName+"&FILE_FORMAT="+fileFormat;
				    document.result_form.submit();
				}
				else
				{
				    exportAll = 'NO';
				    document.result_form.action=omxContextPath+ "/toolkit/framework/util/exportToExcel.cmd?SELECTED_ROW_NUMS="+selRows+"&EXPORT_ALL="+exportAll+"&FILE_NAME="+fileName+"&FILE_FORMAT="+fileFormat;
				    document.result_form.submit();
				}
			}
			else
			{
			    exportAll = 'NO';
			    document.result_form.action=omxContextPath+ "/toolkit/framework/util/exportToExcel.cmd?SELECTED_ROW_NUMS="+selRows+"&EXPORT_ALL="+exportAll+"&FILE_NAME="+fileName+"&FILE_FORMAT="+fileFormat;
			    document.result_form.submit();
			}
		}
        } 
    else
        {  
            core_alert("PLEASE_SELECT_TRANSACTION");
        }
    document.result_form.target = "appFrame";
    document.result_form.action = prevAction;          
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
    if(page_form == null)  page_form = document.result_form;
    
    if(result_form == null)  result_form = document.search_form;
    if(result_form == null)  result_form = document.result_form;
    jumpToPage(actionName, startCount, result_form, page_form);
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

	var slave_table = document.getElementById('result_form_table_slave_data');
	if (slave_table == null)
		i2uiToggleRowSelectionState(obj, originalstate ,'result_form_table', null, false);
	else
		i2uiToggleRowSelectionState(obj, originalstate ,'result_form_table_slave', null, false);
	
	var table = document.getElementById('result_form_table_data');
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
	var table = document.getElementById('result_form_table_data');
	var slavetable = document.getElementById('result_form_table_slave');
	var originalstate = className;
	if (currentposition == -1)
	{
		var slave_table = document.getElementById('result_form_table_slave_data');
		if (slave_table == null)
		 	toggleAllRowsSelectionState(obj, 'result_form_table_data');
		else
			toggleAllRowsSelectionState(obj, 'result_form_table_slave_data');
		
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
		var slave_table = document.getElementById('result_form_table_slave_data');
        
		if (slave_table != null)
		{
			i2uiToggleRowSelectionState(obj, originalstate ,'result_form_table_slave', null, true);
	     }
		if (obj.checked == true)
		{
		
			//table.rows[currentposition].className = "rowHighlight";
	        	i2uiToggleRowSelectionState(obj, originalstate ,'result_form_table_data', null, true);

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
	     var rowNum = 0;
	      if(document.getElementById("_rowselector_filter") !=null) rowNum =1;
	      for(var i=0; i<len; i++)
	      {
		if (checkboxes[i].id == "_rowselector")
		{
		  //i2uiActiveRowSelector = checkboxes[i];
		  checkboxes[i].checked = obj.checked;
		  toggleRowSelectionState(checkboxes[i], rowNum, null, true)
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
// framework supports
function massUpdateGlobal(){
      massUpdate();
}

function massUpdate()
{
	if( ifAllChecked('result_form') )
	{

		  if ( updateAll == 'false' )
		   {
			 groupEditSelected ();
			}
		   else if (parseInt(totalRecordCount) <= parseInt(max_rows))
		   {
			 groupEditSelected ();

		   }			
		   else if ( core_confirm( "Do you want to update all the records ({0}) in the table? If you select 'No', only the records displayed in the current page will be updated.",totalRecordCount) == 'yes' )
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


function groupEditAll()
{
  	document.result_form.UPDATE_MODE.value = 'ALL' ;
	document.result_form.action= omxContextPath+ "/toolkit/framework/forms/formController/goToGroupEditPage.cmd";
	document.result_form.submit();
}


function groupEditSelected()
{
  	document.result_form.UPDATE_MODE.value = 'MULTIPLE' ;
	document.result_form.action= omxContextPath+ "/toolkit/framework/forms/formController/goToGroupEditPage.cmd";
	document.result_form.submit();

}

function getSelectedRowIndices()
{
       var checkboxes;
       var selectedIds=[];
       var checkedRowIndex = 0;
       checkboxes = document.getElementsByTagName("INPUT");
       if (checkboxes != null)
       {
          var len = checkboxes.length;
          var rowNum = 1; // Fix for issue - 572762
          for(var i=0; i<len; i++)
          {
            if (checkboxes[i].id.indexOf("_rowselector") >= 0 )

                    {
                        if (checkboxes[i].checked)
                        {
                            selectedIds[checkedRowIndex] = rowNum;
                            checkedRowIndex++;
                        }
                        var rowNum = rowNum + 1;
                    }
          }
       }
    return selectedIds; 
}