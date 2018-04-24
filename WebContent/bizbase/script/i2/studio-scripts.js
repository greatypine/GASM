var studioPage = true;

function getRecords(actionName, startCount,tableElement, tableId)
{
    var page_form = null;
    var search_form = null;

    if(page_form == null)  page_form = document.form;

    if(search_form == null)  search_form = document.form;
 
    jumpTo(actionName, startCount, search_form, page_form,tableElement, tableId);
}

function jumpTo(actionName, startCount, search_form, page_form,tableElement, tableId)
{
  var maxRows = 10;
  if ( tableElement != null && tableElement != "" )
  {
    var maxRowElement = tableElement + "_MAX_ROWS";
    if (page_form[maxRowElement] != null)  
      maxRows = page_form[maxRowElement].value;
      
    var recCountElement =  tableElement + "_RECORD_COUNT";
    var recCount = page_form[recCountElement].value;
    var startCountElement = tableElement + "_pagenum";
    var bstartCount= page_form[startCountElement].value;
    var pageStartCountValue = page_form[tableElement + "_START_COUNT"].value;
  }
  else 
  {
    if (page_form.MAX_ROWS != null)  
      maxRows = page_form.MAX_ROWS.value;
    
    var startCountElement = "pagenum";
    var bstartCount= page_form[startCountElement].value;
    var recCount = page_form.RECORD_COUNT.value;
    var pageStartCountValue = page_form.START_COUNT.value;
  }
  
  var nextCount = parseInt(startCount) + maxRows;
  var prevCount = 0;
  if ( parseInt(startCount) > 0 )
    prevCount = parseInt(startCount) - maxRows;
  
  if (startCount == null)
    startCount= bstartCount;

  var pagenum = parseInt(startCount);  pagenum--;
  
  if (actionName == "jump")
  {
    if( (  recCount  == 0 || recCount  > pagenum*maxRows)  &&  (pagenum+1>0) )
    // Commenting this condition because it doesnt work if we click the pagination button
    // quickly twice or more times - eQ :: 533549
    // the START_COUNT value in the form is already set to a different value which
    // makes the subsequent clicks not to work
    // && (pageStartCountValue != pagenum*maxRows) )
    {
      //search_form.reset();
      if ( tableElement == null || tableElement == "" )
        search_form.START_COUNT.value = pagenum*maxRows;
      else 
      {
        var sfStartCount = tableElement + "_START_COUNT";
        search_form[sfStartCount].value = pagenum*maxRows;
      }
      update(tableId, 'SYS_RELOAD');
    }
    else
    {
      core_alert("Please enter a valid page number.");
      page_form[startCountElement].focus();
      page_form[startCountElement].select();
    }
  }
}

function sortOrder(order)
{
  var sortOrderElement = document.getElementById(gSortOrderElemName);
  var startCountElement = document.getElementById(gStartCountElemName);
  if (sortOrderElement != null && startCountElement != null)  
  {
    sortOrderElement.value = order;
    startCountElement.value = 0;
    table_disable_multi_sort(gSortTableId);
  }
  else
  {
    document.form.SORT_ORDER.value = order;
    document.form.START_COUNT.value = 0;
    table_disable_multi_sort();
  }

  // Fix for eQ 600275
  suffixIndex = gSortTableId.lastIndexOf('_syncmaster');
  if (suffixIndex > 0)
  {
    gSortTableId = gSortTableId.substr(0, suffixIndex);
  }
  
  update(gSortTableId, 'SYS_RELOAD');
}


function onRefresh()
{
  ui_submit('SYS_REFRESH');
}

function onTabUpdate(containerId, action)
{
  update(containerId, action);
}
