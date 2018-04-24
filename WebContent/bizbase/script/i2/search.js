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
    //document.onkeydown=IEEnterKey;
  }
}


function IEEnterKey()
{

  // if enter key
  if(window.event.keyCode == 13)
  {
    if (window.event.srcElement.onclick != null)
    {
      event.returnValue=true;
    }
    else
    {
      if ( window.event.srcElement.type != "textarea" )
      {
        SetfocusSubmit(window.event.srcElement)
      }

      event.returnValue=false;
    }
  }
  // if backspace
  if (window.event.keyCode==8)
  {
    if (window.event.srcElement.isTextEdit == false)
      event.returnValue=false;
  }
}

function NetEnterKey(e)
{
  key = e.which;
  if(key == 13)
  {
    SetfocusSubmit( e.target )
    return false;
  }
}

function SetfocusSubmit( target )
{
  if ( target.form != null && (target.form == document.search_form))
  {
    onSearch();
  }
  else if (target.form != null && (target.form == document.search_footer_form))
  {
    getRecords('jump');
  }
  else if (target.protocol == 'javascript:')
  {
    var js = target.toString();
    eval(js);
  }
  else
  {
     onSearch();
  }
}
// Enter Key Tapping - End.



// Setting Focus
function setFocus()
{
  var elementsLen = document.search_form.elements.length;

  for(count = 0; count < elementsLen; count++)
  {
    if(
      document.search_form.elements[count].type != "hidden"
      )
      {
        document.search_form.elements[count].focus();
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
        if ( _field.disabled == false )
        {
            eval("_field.checked = " + select);
        }
    }

    if ( numOfAlerts > 1 )
    {
        for(i=0; i<numOfAlerts; i++)
        {
            if ( _field[i].disabled == false )
            {
                eval("_field[" + i + "].checked = " + select);
            }
        }
    }
    else
    {
        if ( _field.disabled == false )
        {
            _field.checked = select;
        }
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
        }
    return foundChecked;
}
function checkifAnyRadioSelected(form)
{
    var count;
    var elementsLen = form.elements.length;
    var foundChecked = false;

    for(count = 0; count < elementsLen; count++)
        {
            if(
               form.elements[count].type == "radio" &&
               form.elements[count].checked == true )
                {
                    foundChecked = true;break;
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
function sort(value,position,obj)
{
  //fix for eQ :: 530156 - IE loses document after we do save on reports
  var docObj;
  
  if ( obj != null )
  {
    docObj = obj.document;
  }
  else
  {
    docObj = document;
  }

  docObj.search_form.reset();
  docObj.search_form.SORT_BY.value=value;

  var sortOrderPopUpObj = docObj.getElementById('sortOrder');
  i2uiShowMenu('sortOrder',sortOrderPopUpObj);
}

function sortOrder(order)
{
    document.search_form.SORT_ORDER.value=order;
    document.search_form.START_COUNT.value=0;
    document.search_form.submit();
}

// Advanced Search
function onAdvancedSearch()
{
    document.search_form.SEARCH_TYPE.value='Advanced';
    document.search_form.submit();
}

// Simple Search
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
            var thisPage = document.location;
            document.search_form.action= omxContextPath + '/core/search/controller/saveInSession.x2c&RETURN_ADDR=' + thisPage;
            document.search_form.submit();
        }
}

// downloading xml
function downloadDocument()
{
    prevAction = document.result_form.action;

    if ( checkifAnySelected(result_form) == true )
        {
            //quick fix. need to be revisited. see bug #405068
            //in ie 5.5 and 6.0, when we download file the current frame loses its
            //handle and it causes problem when we try to refersh that frame.
            //so we are using this i2ui hidden frame which never needs to be refreshed
            document.result_form.target = "i2ui_shell_bottom";
            document.result_form.action= omxContextPath + '/core/report/controller/downloadDocument.x2c';
            document.result_form.submit();
        }
    else
        {
            core_alert("PLEASE_SELECT_TRANSACTION");
        }

    document.result_form.action= prevAction;
}

// download csv
function saveReport()
{
    prevAction = document.result_form.action;
    if ( checkifAnySelected(result_form) == true )
        {
            //quick fix. need to be revisited. see bug #405068
            //in ie 5.5 and 6.0, when we download file the current frame loses its
            //handle and it causes problem when we try to refersh that frame.
            //so we are using this i2ui hidden frame which never needs to be refreshed
            document.result_form.target = "i2ui_shell_bottom";
            document.result_form.action= omxContextPath + '/core/report/controller/saveReport.x2c';
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


  error = "false";
  error = requiredFieldCheck();

  if ( error == 'true' )
    return false;

    var count;
    var elementsLen = document.search_form.elements.length;
  var dateFormat = document.search_form.DATE_FORMAT.value
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
                core_alert(msg);
                            success = false;
                            break;
                        }
                }

            if( ((document.search_form.elements[count].fieldtype == "DateRange") || (document.search_form.elements[count].fieldtype == "Date")) &&
                document.search_form.elements[count].value != "" && dateFormat != "")
                {
                    if ( isDate(document.search_form.elements[count].value, dateFormat) == false )
                        {
                            msg += name + " should be a valid date.  The date format must be " + dateFormat + ".";
                core_alert(msg);
                            success = false;
                            break;
                        }
                }
        }

    return success;
}

// Pagination
function getRecords(actionName, startCount, search_form, page_form)
{

    if(page_form == null)  page_form = document.search_footer_form;
    if(page_form == null)  page_form = document.result_form;

    if(search_form == null || search_form == 'null')  search_form = document.search_form;

    jumpTo(actionName, startCount, search_form, page_form);
}

function jumpTo(actionName, startCount, search_form, page_form)
{
     var maxRows = 10;
     if (search_form.MAX_ROWS != null) maxRows = search_form.MAX_ROWS.value;
     if (page_form.MAX_ROWS != null)  maxRows = page_form.MAX_ROWS.value;

    var nextCount = parseInt(startCount) + maxRows;
    var prevCount = 0;

    if ( parseInt(startCount) > 0 )
        prevCount = parseInt(startCount) - maxRows;

  if (startCount == null)
      startCount=page_form.pagenum.value;

    var pagenum= parseInt(startCount);  pagenum--;

    if (actionName == "jump")
        {
            if(
            ( page_form.RECORD_COUNT.value == 0 || page_form.RECORD_COUNT.value > pagenum*maxRows)  &&
             (pagenum+1>0) &&
               (page_form.START_COUNT.value != pagenum*maxRows)
               )
                {
                    search_form.reset();
                    search_form.START_COUNT.value=pagenum*maxRows;
                    search_form.submit();
                }
        }
}


// Resizing
// Scrollable table
function resizeTable(table_id, pWidth, pHeight,parentContainer_id, tlWidthOffset, tlHeightOffset)
{
   parentContainerScroller_id = parentContainer_id + '_scroller';
   table_id = 'result_form_table';
   table_container_id = 'result_form_container';
   search_form_container_id = 'search_form_container'

   if (tlWidthOffset == null) tlWidthOffset = 20;
   if (tlHeightOffset == null) tlHeightOffset = 105;

   // resize top level container
    i2uiResizeScrollableContainer(parentContainer_id,document.body.offsetHeight - tlHeightOffset, null, document.body.offsetWidth -tlWidthOffset, true, 'yes');

    var width = document.body.offsetWidth - pWidth;
    var height = document.body.scrollHeight - pHeight;

    // if parent container is scrolling
    var obj = document.getElementById(parentContainerScroller_id);
    if(obj)
    {
      // subtract its scrollbar
      width = width- (obj.offsetWidth -obj.clientWidth) ;
    }

   // if table is scrollable
   // subtract its scrollbar
   tablewidth = width-16; // for now

   // resize table
   i2uiResizeScrollableArea(table_id,height,tablewidth  ,null,20);
   i2uiResizeColumns(table_id);

   // resize the table container
   i2uiResizeScrollableContainer(table_container_id,document.body.offsetHeight - 20, null, width  , true, 'yes');

   // resize the search container
   i2uiResizeScrollableContainer(search_form_container_id,document.body.offsetHeight - 20, null, width  , true, 'yes');

   // resize top level container
    i2uiResizeScrollableContainer(parentContainer_id,document.body.offsetHeight - tlHeightOffset, null, document.body.offsetWidth -tlWidthOffset, true, 'yes');

}


// Deprecated
// Scrollable table
function resizeScrollableTables()
{
  if (!document.layers)
  {
    var x = document.body.scrollWidth - 42;
    i2uiResizeScrollableArea('result_table',200,x,null,20);


    i2uiResizeColumns('result_table');
  }
}

// Deprecated
function initContainer(container_id)
{
    i2uiResizeScrollableContainer(container_id,document.body.offsetHeight - 105, null, document.body.offsetWidth - 20, true, 'yes');
}

// Deprecated
function resizeContainer(container_id)
{
    i2uiResizeScrollableContainer(container_id,document.body.offsetHeight -105, null, document.body.offsetWidth - 20, true, 'yes');
}