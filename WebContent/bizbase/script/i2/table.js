  /* Preparing Sorters for multiple tables on the page*/
  /* Each Page will simply add tables to the table_Ids Array*/
  var table_sorters = new Object();
  function table_createSorter(tableId, sortBy, sortOrder)
  {
    table_sorters[tableId] = new TableSorter(tableId);
    if (sortBy != null && sortBy!='') table_doSort( sortOrder,sortBy, tableId, false);
  }

  function table_getSorter(tableId)
  {
    return table_sorters[tableId];
  }

  /* Return the table given id*/
  function table_getObjectFromID(id)
  {
    var scrollable = true;
    if (document.getElementById(id+'_header') == null) scrollable = false;
    if (scrollable == true)
      return document.getElementById(id+'_data')
    else
      return document.getElementById(id)
  }

 /* Return the table given id*/
  function table_getHeaderFromID(id)
  {
    if (document.getElementById(id+'_header') != null) return document.getElementById(id+'_header');
    else
      return document.getElementById(id)
  }

  /* Return the alt of image inside cell*/
  function table_cell_getImageAlt(tdObj)
  {
    var childLen = tdObj.childNodes.length;
    var k =0;
    for(k = 0; k < childLen; k++)
    {
      var child = null;
      var imgchild = null;
      child =  tdObj.childNodes[k];

      if (child.nodeName == 'IMG')
      {
        return child.alt;
      }
      else if (child.nodeName == 'A')
      {
        return child.title;
      }

    }
    return "";
  }

  /*
   Prepares table for sort
   TableSorter = ts = new TableSorter('tableId');
   ts.sort(columnNumber, AscendingOrDescending);
   ex ts.sort(2,0) ; ts.sort(3,1);
  */
  function TableSorter(id)
  {
    // Fields
    this.id = id;
    this.uiTable  =  table_getObjectFromID(id);
    this.uiTableHeader = table_getHeaderFromID(id);

    if (this.uiTable== null) return ;
    if (this.uiTableHeader== null) return ;
    //if (this.uiTable == this.uiTableHeader) return;
    this.sortedBy="";

    this.sortOrder=null;

    this.rows = new Array();
    for (var i=0; i<this.uiTable.rows.length; i++)
    {
       this.rows[i] = this.uiTable.rows[i];
    }
    // Methods
    this.sort= table_sort;
    this.refreshTable= table_sort_refresh;
    this.reverseTable= table_sort_reverse;

    this.dataTypes = new Array();
    for (var iCell=0; iCell < this.uiTable.rows[0].cells.length; iCell++)
    {
       var dataType= this.uiTable.rows[0].cells[iCell].dataType;
       var val  =  this.uiTable.rows[0].cells[iCell].innerText
       try
       {  // try to get the first non empty value
          for (var iRow=0; iRow < this.uiTable.rows.length; iRow++)
          {
            var val =  this.uiTable.rows[iRow].cells[iCell].innerText;
            if (trimString(val) != '') break;
          }
       }catch (e){}
       this.dataTypes[iCell] = table_getCellDataType(val, dataType);

    }
  }

  function table_getCellDataType(val,dataType)
  {
    if (dataType != null && trimString(dataType) != '') return dataType;
    if (trimString(val) == '') return "Text"
    if (dateIsValid(val,page_dateTimeFormat)) return  "Date";
    if (dateIsValid(val,page_dateFormat)) return  "Date";
    if (dateIsValid(val,page_timeFormat)) return  "Date";
    if (isNaN(val)) return  "Text" ; else return "Number";
    return "Text";

  }
  function table_sort(column, order, resize)
  {
    if (resize == null) resize = true;

    // Already in correct order
    if (this.sortedBy == column && this.sortedOrder == order)
    {
      return;
    }
    // Already sorted just reverse table
    else  if (this.sortedBy == column )
    {
      this.sortedOrder = 1-this.sortedOrder;
      this.reverseTable(resize);
      return;
    }
    // else sort and refresh

    // Remembering for efficency
    if (order == null) order = 0;
    this.sortedOrder = order;
    this.sortedBy = column;
    // Facilitatiing reverse ordering
    if (order == 1) order = -1;   else order = 1;
    // Number

    if (this.dataTypes[column] == "Number")
    {
      this.rows.sort(
            function compare(a,b)
            {
              var value = a.cells[column].innerText;
              var  val1 = value.replace(/[,]+/g, "");
              value = b.cells[column].innerText
              var  val2 = value.replace(/[,]+/g, "");

              if (val1 == null ||  trimString(val1) == '')  { return -1*order; }
              if (val2 == null ||  trimString(val2) == '') { return 1*order; }

              if (parseFloat(val1) < parseFloat(val2) ) { return -1*order; }
              if (parseFloat(val1) > parseFloat(val2) ) { return 1*order; }
              return 0;
             });
    }
    // Date
    else if (this.dataTypes[column] == "Date")
    {
//    alert("date");
      this.rows.sort(
            function compare(a,b)
            {
              var  val1 = (a.cells[column].innerText);
              var  val2 = (b.cells[column].innerText);

              if (val1 == null ||  trimString(val1) == '')  { return -1*order; }
              if (val2 == null ||  trimString(val2) == '') { return 1*order; }

              return (datecompare(val1, dateGetFormat(val1),val2,dateGetFormat(val2)))*order;
            });
    }
    // Image
    else if (this.dataTypes[column] == "Image")
    {
      this.rows.sort(
            function compare(a,b)
            {
              var  val1 = table_cell_getImageAlt(a.cells[column]);
              var  val2 = table_cell_getImageAlt(b.cells[column]);

              if (val1 == null ||  trimString(val1) == '')
              {
               return -1*order;
              }
              if (val2 == null ||  val2 == '')
              {
                return 1*order;
              }

              if (val1+"" < val2+"" ) { return -1*order; }
              if (val1+"" > val2+"" ) { return 1*order; }

              return 0;
            });
    }
    // AlphaNumeric
    else
    {
      this.rows.sort(
             function compare(a,b)
             {
                var  val1 = (a.cells[column].innerText);
                var  val2 = (b.cells[column].innerText);

                if ((val1 == null ||  trimString(val1) == ''))
                {
                  return -1*order;
                }
                if ((val2 == null ||  trimString(val2) == ''))
                {
                  return 1*order;
                }
                // If Values are just numbers or  alphanumeric
                if (isNaN(val1) || isNaN(val2)) type = "Text"; else type = "Number";

                if (type == "Text")
                {
                  if (val1+"" < val2+"") { return -1*order; }
                  if (val1+"" > val2+"") { return 1*order; }
                  return 0;
                }
                else
                {
                  if (parseFloat(val1) < parseFloat(val2)) {return -1*order; }
                  if (parseFloat(val1) > parseFloat(val2)) {return 1*order; }
                  return 0;
                }
              });
    }
    // reorder the html table
   this.refreshTable(resize)
  }

  /* Reversers the rows in the table*/
  function table_sort_reverse(resize)
  {
    obj = this;
    noOfRows =  obj.uiTable.rows.length;
    for (var i=0; i < (noOfRows/2); i++)
    {
      obj.uiTable.rows[i].swapNode(obj.uiTable.rows[noOfRows-1-i]);
    }
    if (resize == true || resize == null) onResize();
  }

  /* reorders the html table based on the results of TableSorter*/
  function table_sort_refresh(resize)
  {
    var obj = this;
    for (var i=0; i < obj.rows.length; i++)
    {
      var from = -1;
      for (var j=0; j < obj.uiTable.rows.length; j++)
      {
        if ( obj.rows[i] ==  obj.uiTable.rows[j])
        {
          from = j;break;
        }
      }
      var to = i;
      obj.uiTable.moveRow(from,to);
      var mod = to%2 ;
      if ( mod == 0 ) obj.uiTable.rows[to].className="tableRow1";
      else obj.uiTable.rows[to].className="tableRow0";
    }
   // if (obj.sortedOrder == 1)  obj.reverseTable();
     if (resize == true || resize == null) onResize();
  }


  var gSortTableId;
  // Called from user Action to show popup menu
  function sort(value , column, obj, tableId)
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
    
    docObj.sortByClient = 'false';
    docObj.form.reset();

    var sortByElementName = "SORT_BY";
    
    if ( tableId != null && tableId != "" ) 
    {
      sortByElementName = tableId + "_SORT_BY";
      gSortOrderElemName = tableId + "_SORT_ORDER";
      gStartCountElemName = tableId + "_START_COUNT";
    }
 
    var sortByElement = docObj.getElementById(sortByElementName);
    if (sortByElement != null)  
    {
      sortByElement.value = value;
    }
    else
    {
      docObj.form.SORT_BY.value = value;
    }

    var sortOrderPopUpObj = docObj.getElementById('sortOrder');
    gSortTableId = tableId;

    i2uiShowMenu('sortOrder',sortOrderPopUpObj);
  }

  // called when user choose order on the popup menu
  function sortOrder(order)
  {
    if (document.sortByClient == true)
    {
      table_onClientSort_doSort(order,document.sortColumnNumber);
    }
    else
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

      document.form.submit();
    }
  }

  function table_disable_multi_sort(tableId)
  {
    //multi sort table :: start
    var p = "";
    if(tableId != null && tableId != "")
    {
      p = tableId + "_";
    }
    var sortBy2 = document.getElementById(p + "SORT_BY2");
    var sortOrder2 = document.getElementById(p + "SORT_ORDER2");
    var sortBy3 = document.getElementById(p + "SORT_BY3");
    var sortOrder3 = document.getElementById(p + "SORT_ORDER3");
    if(sortBy2 != null)
    {
      sortBy2.disabled = "true";
    }
    if(sortOrder2 != null)
    {
      sortOrder2.disabled = "true";
    }
    if(sortBy3 != null)
    {
      sortBy3.disabled = "true";
    }
    if(sortOrder3 != null)
    {
      sortOrder3.disabled = "true";
    }
    //multi sort table :: end
  }
  
  function table_sort_changeIcon( cell, sortOrder)
  {
    var childLen = cell.childNodes.length;
    var k;

    if ( childLen >= 1)
    {
      for(k = 0; k < childLen; k++)
      {
        var child =  cell.childNodes[k];
        if (child.nodeName == 'SPAN')
        {
          if (sortOrder == 0 )
          {
            if (child.id == 'asc')
              toggleItemVisibility(child,'show');
            else if (child.id == 'desc')  toggleItemVisibility(child,'hide');
            else if (child.id == 'unsorted')  toggleItemVisibility(child,'hide');
            
          }
          else if (sortOrder == 1)
          {
            if (child.id == 'asc')
              toggleItemVisibility(child,'hide');
            else if (child.id == 'desc')  toggleItemVisibility(child,'show');
            else if (child.id == 'unsorted')  toggleItemVisibility(child,'hide');
          }
          else if (sortOrder == -1)
          {
            if (child.id == 'asc' || child.id =='ascdesc' || child.id=='desc')
              toggleItemVisibility(child,'hide');
            else if (child.id == 'unsorted')  toggleItemVisibility(child,'show');
          }
        }
      }
    }
  }

  /* This is called from user action */
  function table_onClientSort_getOrder(id, column)
  {
    document.sortByClient = true;
    document.sortTableId = id;
    document.sortColumnNumber = column;;
    i2uiShowMenu('sortOrder');
    onResize();
    return;
  }
  
  /* This is called from user action */
  function table_doSort(order,columnName, tableId, resize)
  {
     var sorter = table_getSorter(tableId);
     var table = sorter.uiTableHeader;
     if (table == null) return;
     var posOfSortingHeaderRow = table.rows.length-1;
     if (posOfSortingHeaderRow < 0 )return;
     var column = -1;
     for(var i =0; i< table.rows[posOfSortingHeaderRow].cells.length;i++)
      {
        if (table.rows[posOfSortingHeaderRow].cells[i].name == columnName)
        {
          column = i; break
        }
      }
     if (column == -1) return;

     var cell = table.rows[posOfSortingHeaderRow].cells[column];
     if (cell.sortable="client")
     {
      table_onClientSort_doSort(order,column,tableId,resize);
     }
     else
     {
      // NotImplemented
     }
  }

  /* This is called from user action */
  function table_onClientSort_doSort(order,column, tableId,resize)
  {
     if (tableId == null) tableId = document.sortTableId;
      if (tableId == null) return;
      if (column == null) return;
      if (resize == null) resize = true;

      var sorter = table_getSorter(tableId);
      var table = sorter.uiTableHeader;
      var posOfSortingHeaderRow = table.rows.length-1;
      var cell = table.rows[posOfSortingHeaderRow].cells[column];
      for(var i =0; i< table.rows[posOfSortingHeaderRow].cells.length;i++)
      {
        table_sort_changeIcon(table.rows[posOfSortingHeaderRow].cells[i],-1);
      }

      if (order == 'Ascending' )
      {
        sorter.sort(column,0, resize);
        table_sort_changeIcon(cell,0);
      }
      else if (order == 'Descending')
      {
        order = 'Descending';
        sorter.sort(column,1,resize);
        table_sort_changeIcon(cell,1);
     }
     else
    {
      sorter.sort(column,null,resize);
      table_sort_changeIcon(cell,sorter.sortedOrder);
    }
    return;
  }


  function ui_configureTable(tableId)
  {
    var pageId = document.form.PAGE_NAME.value;
    if( pageId == "" )
      pageId = document.form.PAGE.value;
    var sysId = document.form.SYS_ID;//for pages built using studio
    var s = omxContextPath + '/core/configure.jsp?TABLE_ID=' + tableId+"&PAGE_ID="+pageId;
    if( sysId )
      s += "&SYS_ID=" + sysId.value;
    popUpWindow(s,'configurePopup');
  }

  function table_export(tableId,horizontal, where)
  {

     if (where == "true" || where == "client" || where == "")
     {
       ui_extractTable(tableId, horizontal);
     }
     else if (where == "ui")
     {
       exportTablePGL(tableId);
     }
     else if (where == "server")
     {
        // not implemented in server yet.
     }

  }

  function table_export_old(tableId, where, action)
  {
     if (where == "true" || where == "client" || where == "")
     {
       ui_extractTable(tableId);
     }
     else if (where == 'x2')
     {
       exportTableX2(action);
     }
  }

  // private
   function exportTableX2(action)
  {

    var prevAction = document.search_form.action;
    var prevTarget = document.search_form.target;
    var prevMaxRows = document.search_form.MAX_ROWS.value;
    var max = page_maxRowsExportable;
    if (document.result_form.RECORD_COUNT.value > max)
    {
       core_alert("Your search criteria resulted in {0} rows. Only {1} rows can be exported to excel. Please change the search critieria and try again.",document.result_form.RECORD_COUNT.value, max);
       return;
    }
    document.search_form.MAX_ROWS.value = document.result_form.RECORD_COUNT.value;

    document.search_form.target = "i2ui_shell_bottom";
    document.search_form.action=omxContextPath + action;
    document.search_form.submit();

    document.search_form.action= prevAction;
    document.search_form.target= prevTarget;
    document.search_form.MAX_ROWS.value= prevMaxRows;
  }
  //private
  function exportTablePGL(tableId)
  {
    var pageId = document.form.PAGE_NAME.value;

    var maxRowsElem;
    var startAtRowElem;
    var recordCountElem;
    
    if(form[tableId + "_MAX_ROWS"] != null)
    {
      maxRowsElem = eval("document.form." + tableId + "_MAX_ROWS");
      recordCountElem = eval("document.form." + tableId + "_RECORD_COUNT");
    }
    else
    {
      maxRowsElem = document.form.MAX_ROWS;
      recordCountElem = document.form.RECORD_COUNT;
    }
    
    if(form[tableId + "_START_COUNT"] != null)
    {
      startAtRowElem = eval("document.form." + tableId + "_START_COUNT");
    }
    else
    {
      startAtRowElem = document.form.START_COUNT;
    }
    
    var prevAction = document.form.action;
    var prevTarget = document.form.target;
    if(maxRowsElem != null)
      var prevMaxRows = maxRowsElem.value;
    if(startAtRowElem != null)
      var prevStartAtRow = startAtRowElem.value;

    var max = page_maxRowsExportable;
    if (recordCountElem != null && recordCountElem.value > max)
    {
       var result = core_confirm("Your table has {0} rows of data. Only {1} rows can be exported to excel. Do you wish to continue?", recordCountElem.value, max);
       if(result == "no")
       {
         return;
       }
    }
    
    if(maxRowsElem != null)
      maxRowsElem.value = max;

    if (startAtRowElem != null)
    {
      startAtRowElem.value = 0;
    }
    
    document.form.BUTTON_ID.value = "SYS_RELOAD";
    document.form.target = "i2ui_shell_bottom";    
    document.form.action = omxContextPath+ "/CSVServlet?LAF_FILTER_ID="+ tableId+"&PAGE_ID="+pageId;
    document.form.submit();

    document.form.action= prevAction;
    document.form.target= prevTarget;
    if(maxRowsElem != null)
      maxRowsElem.value= prevMaxRows;
    if (startAtRowElem != null)
    {
      startAtRowElem.value = prevStartAtRow;
    }
  }

/* Returns the selected rowid's as an array given the tableid */
function table_getSelectedRows(tableid)
{
  var selected = new Array();
  // NS4 browser not supported
 // if (document.layers != null)
 ////   return selected;

  var tableobj = document.getElementById(tableid);
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
      var j = 0;
      for(var i=0; i<len; i++)
      {
        if (checkboxes[i].id != null &&checkboxes[i].id== tableid+"_rowselector")
        {
          j++;
          if (checkboxes[i].checked)
          {
              selected[selected.length] = checkboxes[i].value;
          }
        }
      }
    }
  }
  return selected;
}

/* Select a rows in the table give the id's*/
function table_selectRows(tableid, rows)
{
  var selected = new Array();
  var tableobj = document.getElementById(tableid);
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
      var j = 0;
      for(var i=0; i<len; i++)
      {
        if (checkboxes[i].id != null &&checkboxes[i].id== tableid+"_rowselector")
        {
          j++;
          var checked = false;
          for(var k=0; k<rows.length; k++)
          {
            if (checkboxes[i].value == rows[k])
            {
              checked = true;break;
            }
          }
          if (checked)
          {
              checkboxes[i].checked = true;
              table_highlightSelectedRowForChild(checkboxes[i])
          }
          else table_highlightSelectedRowForChild(checkboxes[i], false)
        }
      }
    }
  }
  return selected;
}

/* Give a table make the sub rows unselectable */
/* Currently assumes all subrows have same color */
/* Later need to have a subtree="true" attribute at the TR level*/
/* public */
function table_makeSubTreeUnSelectable(tableid)
{
  var tableobj = document.getElementById(tableid);
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
      var j = 0;
      for(var i=0; i<len; i++)
      {
        if (checkboxes[i].id == tableid+"_rowselector")
        {
          j++;
//          if (checkboxes[i].checked)
//          {
            // get owning row
            var rowobj = checkboxes[i];
            while (rowobj != null && rowobj.tagName != "TR")
            {
              if (rowobj.parentElement)
              {
                rowobj = rowobj.parentElement;
              }
              else
              {
                rowobj = rowobj.parentNode;
              }
            }
            if (rowobj.className == "tableRow0")
            {
              checkboxes[i].style.display="none";
              checkboxes[i].disabled = true;
              }
  //        }
        }
      }
    }
  }
  return null;
}

/* Given a child(checkbox) toggle the yellow highlight of the owning row as desired*/
/* private */
function table_highlightSelectedRowForChild(obj, state)
{
  if (state == null) state = true;

  var rowobj = obj;
  while (rowobj != null && rowobj.tagName != "TR")
  {
    if (rowobj.parentElement)
    {
      rowobj = rowobj.parentElement;
    }
    else
    {
      rowobj = rowobj.parentNode;
    }
  }
  if (rowobj != null)
  {
    if (state == true)
    {
      rowobj.originalClassName =  rowobj.className;
      rowobj.className = "rowHighlight";
      rowobj.cells[0].childNodes[0].style.backgroundColor = "";
    }
    else
    {
      if (rowobj.originalClassName != null)
      rowobj.className =  rowobj.originalClassName;
    }
  }


}


// Fields.js
 function dropDown_sort(obj, notused, selectFirst)
 {
 if (obj == null) return;
  var first = 0;
  var o = new Array();
  if (obj.options==null) { return; }
  for (var i = first,j=0; i<obj.options.length; i++,j++)
  {
    o[j] = new Option( obj.options[i].text, obj.options[i].value, obj.options[i].defaultSelected, obj.options[i].selected) ;
  }
  if (o.length==0) { return; }
    o = o.sort(
              function(a,b) {
                if (a.value == "") {return -1}
                if ((a.text+"") < (b.text+"")) { return -1; }
                if ((a.text+"") > (b.text+"")) { return 1; }
                return 0;
                }
              );

  for (var i=first,j=0; j<o.length; i++,j++)
  {
    obj.options[i] = new Option(o[j].text, o[j].value, o[j].defaultSelected, o[j].selected);
  }

  if (selectFirst == true )
  obj.options[0].selected = true;
}

function mass_update()
{
  document.form.BUTTON_ID.value = "mass_update";
  document.form.submit();
}


/* 
  Assumption:: This will work for multiple tables inside a step as long as the
  field names (including the checkbox names) are different between the tables. 
  The mass entry is applied only on those rows that are checked.
*/
function mass_entry_update(fieldName, checkboxName)
{  
  if ( !ifAnyChecked("form", checkboxName) )
  {    
    core_alert("Atleast one checkbox should be selected");
    return;
  }

  var massEntryPrefix = "MASS_ENTRY_";
  var massEntryPrefixLength = massEntryPrefix.length;
  var fieldNameToBeUpdated = fieldName.substring(massEntryPrefixLength);

  var massValue = eval("document.form." + fieldName + ".value");

  var checkboxElem = document.getElementsByName(checkboxName);
  var fieldElem = document.getElementsByName(fieldNameToBeUpdated);

  var index = 0;
  for( i=0; i<checkboxElem.length; i++ )
  {
    if (checkboxElem[i].checked == true)
    {
      if (fieldElem[i].type.toLowerCase() != 'hidden' && !fieldElem[i].disabled && !fieldElem[i].readOnly)
      {
        fieldElem[i].value = massValue;
      }
    }
  }
}

function apply_filters(tableId, targetId)
{
  var bValidFilters = true;
 
  if(!eval('isValid_'+targetId+'()'))
  {
    return;
  }

  if (bValidFilters)
  {
    if ( tableId != null && tableId != "" )
    {
      document.form[ tableId + '_START_COUNT'].value = 0;
    }
    else
    {
      document.form.START_COUNT.value = 0;
    }
    update(targetId, 'SYS_RELOAD', undefined, undefined, "view.x2ps?innerAction=Filter");
  }
}

function clear_filters_and_refresh()
{
  for (var i = 0; i<document.form.elements.length; i++) 
  {
    var selectedElem = document.form.elements[i];
    if ((selectedElem.name.indexOf('FILTER_') > -1)) 
    {
      selectedElem.value = ""; 
    }
  }
  ui_submit('SYS_RELOAD');
}