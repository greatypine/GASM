////////////////////////////////////////////////////////////////////////////
// (c) Copyright 2000 - 2001, i2 Technologies, Inc. (formerly Intellection)//
// ALL RIGHTS RESERVED.                                                    //
//                                                                         //
// This UNPUBLISHED PROPRIETARY software is  subject to the full copyright //
// notice in the COPYRIGHT file in this directory.                         //
/////////////////////////////////////////////////////////////////////////////

/* support for editable table, spreadsheet and pivot */

// colors here vs style sheet
var PivotCellColor      = "#eceef8";
var PivotCellTotalColor = "#f7f8fd";
var PivotLabelColor     = "#d1d6f0";
var PivotFontFamily     = "verdana"; // only get 1 choice here

/* item names that are too long will cause problems in the pivot */
var maxItemNameLength = 22;
var xmlDom = null;
var XAXISDELIMITER = ":";  // was \\n

// pivot 1-sum, 2-count, 3-min, 4-max, 5-avg, 6-stddev, 7-variance
var showAsValues = new Array();
showAsValues["sum"] = 1;
showAsValues["count"] = 2;
showAsValues["min"] = 3;
showAsValues["max"] = 4;
showAsValues["avg"] = 5;
showAsValues["stddev"] = 6;
showAsValues["variance"] = 7;

function i2uiDetectEditableTable(objname)
{
  return i2uiDetectSpreadsheet(objname);
}
function i2uiDetectSpreadsheet(objname)
{
  var ssobj = document.getElementById(objname+"Spreadsheet");
  if (ssobj == null)
    return 1;
  if (ssobj.HTMLData == null || ssobj.HTMLData == "")
    return 2;
  return 0;
}
function i2uiInitEditableTable(objname, ownername, mode, col, height, width)
{                
  if (!i2uiCheckVersion(objname+"Spreadsheet"))
    return false;

  var tbl2obj = null;
  var tblobj = document.getElementById(objname+"_header");
  if (tblobj == null)
    tblobj = document.getElementById(objname);
  else
    tbl2obj = document.getElementById(objname+"_data");
  var ssobj = document.getElementById(objname+"Spreadsheet");
  if (col == null)
    col = 0;
  ssobj.setAttribute("editablecolumn",col);
  document.getElementById(objname+"EditAction").style.display="none";
  document.getElementById(objname+"CancelAction").style.display="";
  document.getElementById(objname+"SaveAction").style.display="";
  i2uiBuildSpreadsheet(objname, tblobj, tbl2obj, ssobj, col, false, 2);
  // if unlock editable area here and large number of cells to become unlocked
  // then component will render a black, unusable region on the page !!
  // delay until i2uiStartTableEdit
  // var range = i2uiBuildRange(2,col+1,tblobj.rows.length+1,tblobj.rows(0).cells.length);
  // ssobj.activeSheet.Range(range).Locked = false;
  // ssobj.activeSheet.Range(range).Interior.Color = "white";
  ssobj.activeSheet.Protection.Enabled = true;
  i2uiCreateNamedRanges(objname, tblobj, ssobj);
  if (mode == null)
    i2uiSaveTableEdit(objname);
  else
    i2uiCancelTableEdit(objname);
  i2uiResizeSpreadsheet(objname, ownername, true, height);
}
function i2uiProtectSpreadsheet(objname, flag)
{
  var ssobj = document.getElementById(objname+"Spreadsheet");
  if (ssobj != null)
    ssobj.activeSheet.Protection.Enabled = flag;
}
function i2uiInitSpreadsheet(objname, ownername, col, height, width, autofit, numExtraRows)
{
  if (!i2uiCheckVersion(objname+"Spreadsheet"))
    return false;

  var ssobj = document.getElementById(objname+"Spreadsheet");
  var tblobj = document.getElementById(objname+"Spreadsheet_xml");
  if (tblobj == null)
    tblobj = document.getElementById(objname);
  if (tblobj.tagName.toLowerCase() == "xml")
  {
    ssobj.xmlData = tblobj.xml;
    var rows = tblobj.selectNodes("//Worksheet/Table/Row").length;
    var ssheight = (ssobj.activeSheet.Cells.RowHeight * (rows + 2)) + 95;
    if (height == null)
      height = ssheight;
    else
      height = Math.min(height, ssheight);
    i2uiResizeSpreadsheet(objname, ownername, true, height);  
    return;
  }

  if (col == null)
    col = 0;
  ssobj.setAttribute("editablecolumn",col);
  i2uiBuildSpreadsheet(objname, tblobj, null, ssobj, col, true, numExtraRows);
  // if unlock editable area here and large number of cells to become unlocked
  // then component will render a black, unusable region on the page !!
  // delay until i2uiStartTableEdit
  // var range = i2uiBuildRange(2,col+1,tblobj.rows.length+1,tblobj.rows(0).cells.length);
  // ssobj.activeSheet.Range(range).Locked = false;
  // ssobj.activeSheet.Range(range).Interior.Color = "white";
  ssobj.activeSheet.Protection.Enabled = true;
  i2uiCreateNamedRanges(objname, tblobj, ssobj);
  var ssheight = (ssobj.activeSheet.Cells.RowHeight * (tblobj.rows.length + 2)) + 95;
  if (height == null)
    height = ssheight;
  else
    height = Math.min(height, ssheight);
  i2uiResizeSpreadsheet(objname, ownername, true, height);  
  // if spreadsheet wider than allowed width, invoke autofit
  if (autofit == null)
    autofit = false;
  var sum = tblobj.rows(0).cells.length * ssobj.Columns.ColumnWidth;
  if (sum > ssobj.width)
    autofit = true;
  i2uiFirstEdit(objname, tblobj, ssobj, tblobj.rows.length, autofit, numExtraRows);
}
function i2uiResizeSpreadsheet(objname, ownername, bypass, h, w)
{
  var tblobj = document.getElementById(objname+"Spreadsheet_xml");
  if (tblobj == null)
    tblobj = document.getElementById(objname);
  var ssobj = document.getElementById(objname+"Spreadsheet");
  var ownerobj = document.getElementById(ownername);
  var tabletop = i2uiComputeTop(objname);
  var ownertop = i2uiComputeTop(ownername);
  if (h == null)
    ssobj.height = Math.max(20, ownerobj.clientHeight-50-(tabletop-ownertop));
  else
    ssobj.height = h;
  if (w == null)
    ssobj.width  = Math.max(ownerobj.clientWidth-3,340);
  else
    ssobj.width  = w;
  if (bypass == null || bypass==false)
    ssobj.HTMLData = i2uiAlignSpreadsheet(objname, tblobj, ssobj);
}
function i2uiAlignSpreadsheet(objname, tblobj, ssobj, defn)
{
  if (defn == null)
    defn = ssobj.HTMLData;
  var at1 = defn.indexOf("<col ");
  var at2 = defn.indexOf("<tr");
  if (at1 == -1) 
    at1 = at2+1;
  var newcols = " ";
  var numcols = tblobj.rows(0).cells.length;
  var rowobj = tblobj.rows(0);
  for(var j=0; j<numcols; j++)
    newcols += "<col width='" + (rowobj.cells(j).clientWidth-7) + "'> ";
  var newdefn = defn.substring(0,at1-1) + newcols + defn.substring(at2);
  return(newdefn);
}
function i2uiBuildSpreadsheetColumnWidths(objname, tblobj, ssobj, owner)
{
  try
  {
    var objCol;
    var objColAttr;
    var numcols = tblobj.rows(0).cells.length;
    var rowobj = tblobj.rows(0);
    for(var j=0; j<numcols; j++)
    {
      objCol = objDom.createElement("col");
      objColAttr = objDom.createAttribute("width");
      objColAttr.text = rowobj.cells(j).clientWidth-7;
      objCol.setAttributeNode(objColAttr);
      owner.appendChild(objCol);
    }
  } catch(e){}
}
function i2uiUnlockAnalysis(objname)
{
  var numrows= 1;
  var tbl2obj = null;
  var tblobj = document.getElementById(objname+"_header");
  if (tblobj == null)
  {
    tblobj = document.getElementById(objname);
    numrows = tblobj.rows.length;
  }
  else
  {
    tbl2obj = document.getElementById(objname+"_data");
    numrows = tblobj.rows.length + tbl2obj.rows.length;
  }
  var ssobj = document.getElementById(objname+"Spreadsheet");
  var range = i2uiBuildRange(numrows+1,1,
                             ssobj.activeSheet.UsedRange.Rows.Count+1,
                             tblobj.rows(0).cells.length+10);
  ssobj.activeSheet.Range(range).Locked = false;
}
function i2uiFirstEdit(objname, tblobj, ssobj, numrows, autofit, numExtraRows)
{
  var protection = ssobj.activeSheet.Protection;
  protection.Enabled = false;
  // unlock editable area to the right of 'col'
  var col = ssobj.getAttribute("editablecolumn")/1;
  var range = i2uiBuildRange(2,col+1,numrows+1,tblobj.rows(0).cells.length);
  ssobj.activeSheet.Range(range).Locked = false;
  // unlock editable areas
  var unlockedColumns = ssobj.getAttribute("unlockedColumns");
  var len = unlockedColumns.length;
  for (var i=0; i<len; i++)
  {
    range = i2uiBuildRange(2,unlockedColumns[i]+1,numrows+1,unlockedColumns[i]+1);
    ssobj.activeSheet.Range(range).Locked = false;
    ssobj.activeSheet.Range(range).Interior.Color = "white";
  }
  // lock noneditable areas
  var lockedColumns = ssobj.getAttribute("lockedColumns");
  var len = lockedColumns.length;
  for (var i=0; i<len; i++)
  {
    range = i2uiBuildRange(2,lockedColumns[i]+1,numrows+1,lockedColumns[i]+1);
    ssobj.activeSheet.Range(range).Locked = true;
  }
  if (numExtraRows != null)
  {
    range = i2uiBuildRange(numrows+1,1,numrows+1+numExtraRows,tblobj.rows(0).cells.length);
    ssobj.activeSheet.Range(range).Locked = false;
  }
  i2uiUnlockAnalysis(objname);
  // create freeze pane
  i2uiFreezePane(objname, ssobj);
  if (autofit == true || autofit == null)
    ssobj.activeSheet.Columns.AutoFit();
  // allow user to add/remove rows and columns 
  var protection = ssobj.activeSheet.Protection;
  protection.AllowDeletingRows = true;
  protection.AllowInsertingRows = true;
  protection.AllowDeletingColumns = true;
  protection.AllowInsertingColumns = true;
  protection.Enabled = true;
  //ssobj.activeSheet.Protection.Enabled = true;
  ssobj.setAttribute("firstEdit","false");
}
function i2uiStartTableEdit(objname)
{               
  var numrows= 1;
  var tbl2obj = null;
  var tblobj = document.getElementById(objname+"_header");
  if (tblobj == null)
  {
    tblobj = document.getElementById(objname);
    numrows = tblobj.rows.length;
    tblobj.style.display="none";
  }
  else
  {
    tbl2obj = document.getElementById(objname+"_data");
    numrows = tblobj.rows.length + tbl2obj.rows.length;
    document.getElementById(objname).style.display="none";
  }
  var ssobj = document.getElementById(objname+"Spreadsheet");

  if (tbl2obj == null)
    i2uiPopulateSpreadsheet(objname, tblobj, ssobj, 1);
  else
    i2uiPopulateSpreadsheet(objname, tbl2obj, ssobj, 0);
  try{
  document.getElementById(objname+"EditAction").style.display="none";
  document.getElementById(objname+"CancelAction").style.display="";
  document.getElementById(objname+"SaveAction").style.display="";
  }catch(e){}
  ssobj.style.display="";
  if (ssobj.getAttribute("firstEdit") == null)
    i2uiFirstEdit(objname,tblobj,ssobj,numrows);
}
function i2uiFreezePane(objname, ssobj)
{
  // disabled since scrolling of frozen area is not possible
  // and if frozen area is wider than display area, we are in trouble
  return;
  var col = ssobj.getAttribute("editablecolumn")/1;
  range = i2uiBuildRange(2,col+1);
  ssobj.activeSheet.Application.ActiveWindow.FreezePanes = false;
  ssobj.activeSheet.Range(range).select();
  //ssobj.activeSheet.Range(range).activate();
  ssobj.activeSheet.Application.ActiveWindow.FreezePanes = true;
}
function i2uiCancelTableEdit(objname)
{
  var tblobj = document.getElementById(objname);
  tblobj.style.display="";
  var ssobj = document.getElementById(objname+"Spreadsheet");
  ssobj.style.display="none";
  document.getElementById(objname+"EditAction").style.display="";
  document.getElementById(objname+"CancelAction").style.display="none";
  document.getElementById(objname+"SaveAction").style.display="none";
}
function i2uiSaveTableEdit(objname)
{
  var tbl2obj = null;
  var tblobj = document.getElementById(objname+"_header");
  if (tblobj == null)
  {
    tblobj = document.getElementById(objname);
    tblobj.style.display="";
  }
  else
  {
    tbl2obj = document.getElementById(objname+"_data");
    document.getElementById(objname).style.display="";
  }
  var ssobj = document.getElementById(objname+"Spreadsheet");
  ssobj.style.display="none";
  if (tbl2obj == null)
    i2uiPopulateTable(objname, tblobj, ssobj, 1);
  else
    i2uiPopulateTable(objname, tbl2obj, ssobj, 0);
  document.getElementById(objname+"EditAction").style.display="";
  document.getElementById(objname+"CancelAction").style.display="none";
  document.getElementById(objname+"SaveAction").style.display="none";
}
var base26letters = "0123456789abcdefghijklmnopq";
var rangeletters  = " ABCDEFGHIJKLMNOPQRSTUVWXYZ";
function i2uiBuildRange(ulr, ulc, lrr, lrc, absolute)
{
  if (absolute != null)
    absolute = "$";
  else
    absolute = "";
  var result = absolute;
  var temp, len, at, offset;
  temp = ulc.toString(27);
  len = temp.length;
  offset = 0;
  for (var i=0; i<len; i++)
  {
    at = base26letters.indexOf(temp.charAt(i));
    result += rangeletters.charAt(at+offset);
    offset = 1;
  }
  result += absolute+ulr;
  if (lrr != null && lrc != null)
  {
    result += ":"+absolute;
    temp = lrc.toString(27);
    len = temp.length;
    offset = 0;
    for (var i=0; i<len; i++)
    {
      at = base26letters.indexOf(temp.charAt(i));
      result += rangeletters.charAt(at+offset);
      offset = 1;
    }
    result += absolute+lrr;
  }
  return(result);
}
function i2uiExtractTable(objname, cols, names)
{
  var tblobj = document.getElementById(objname);
  var len3 = 0;
  var ids = new Array();
  var len2 = 0;
  if (cols != null) 
  {
    ids = cols.split(",");
    len2 = ids.length;
  }
  else
  {
    len2 = tblobj.rows(0).cells.length;
    for (var i=0; i<len2; i++)
      ids[i] = i;
  }
  if (names != null)
  {
    names = names.split(",");
    len3 = names.length;
  }
  else
    names = new Array();
  for (var i=len3; i<len2; i++)
    names[i] = "col";
  var len = tblobj.rows.length;
  var result = "<"+objname+">";
  for (var i=1; i<len; i++)
  {
    result += "<row>";
    for (var j=0; j<len2; j++)
    {
      try
      {
        result += "<"+names[j]+">"+tblobj.rows[i].cells[ids[j]].innerText+"</"+names[j]+">";
      }
      catch(e){}
    }
    result += "</row>";
  }
  result += "</"+objname+">";
  return(result);
}
function i2uiExtractSpreadsheet(objname, cols, names, action)
{
  var range;
  var cellvalue;
  var tblobj = document.getElementById(objname);
  var ssobj  = document.getElementById(objname+"Spreadsheet");
  var len3 = 0;
  var ids = new Array();
  var len2 = 0;
  if (cols != null) 
  {
    ids = cols.split(",");
    len2 = ids.length;
  }
  else
  {
    len2 = tblobj.rows(0).cells.length;
    for (var i=0; i<len2; i++)
      ids[i] = i;
  }
  if (names != null)
  {
    names = names.split(",");
    len3 = names.length;
  }
  else
    names = new Array();
  for (var i=len3; i<len2; i++)
    names[i] = "col";
  var len = tblobj.rows.length;
  var result = "<"+objname+">";
  if (action == null || action == "table" || action == "all")
  {
    for (var i=1; i<len; i++)
    {
      result += "<row action=\"update\">";
      for (var j=0; j<len2; j++)
      {
        try
        {
          range = i2uiBuildRange(i+1,(ids[j]/1)+1);
          cellvalue = ssobj.ActiveSheet.Range(range).Value2
          // trouble here for date fields
        }
        catch(e)
        {
          cellvalue="";
        }
        result += "<"+names[j]+">"+cellvalue+"</"+names[j]+">";
      }
      result += "</row>";
    }
  }
  if (action == "all" || action == "new")
  {
    // process all rows here
    var last = ssobj.activesheet.UsedRange.EntireRow.Count;
    window.status = last;
    for (var i=len; i<last; i++)
    {
      var rowresult = "<row action=\"insert\">";
      var rowempty = true;
      for (var j=0; j<len2; j++)
      {
        try
        {
          range = i2uiBuildRange(i+1,(ids[j]/1)+1);
          // trouble here for date fields
          cellvalue = ssobj.ActiveSheet.Range(range).Value2;
          if (cellvalue != null && 
              cellvalue != "undefined" && 
              cellvalue != "")
            rowempty = false;
        }
        catch(e)
        {
          cellvalue="";
        }
        rowresult += "<"+names[j]+">"+cellvalue+"</"+names[j]+">";
      }
      if (!rowempty)
        result += rowresult+"</row>";
    }
  }
  if (action == "highlighted")
  {
    var first = ssobj.activesheet.application.selection.row;
    var len = first + ssobj.activesheet.application.selection.rows.count;
    for (var i=first; i<len; i++)
    {
      result += "<row action=\"delete\">";
      for (var j=0; j<len2; j++)
      {
        try
        {
          range = i2uiBuildRange(i,(ids[j]/1)+1);
          cellvalue = ssobj.ActiveSheet.Range(range).Value
        }
        catch(e)
        {
          cellvalue="";
        }
        result += "<"+names[j]+">"+cellvalue+"</"+names[j]+">";
      }
      result += "</row>";
    }
  }
  result += "</"+objname+">";
  return(result);
}
function i2uiBuildSpreadsheet(objname, tblobj, tbl2obj, ssobj, col, autofit, numExtraRows)
{
  var formula;
  var at;
  var unlockedColumns = new Array();
  var lockedColumns = new Array();
  var defn = ssobj.HTMLData;
  // add our styles
  at = defn.indexOf("</style>");
  if (at != -1)
  {
    var extraStyles = "";
    extraStyles += ".header{background-color:#d1d6f0;border-top:solid #999999 1px;border-left:solid #999999 1px;border-right:solid #999999 1px;border-bottom:solid #999999 1px;}";
    extraStyles += ".cellodd{background-color:#f7f8fd;border-top:solid #999999 1px;border-left:solid #999999 1px;border-right:solid #999999 1px;border-bottom:solid #999999 1px;}";
    extraStyles += ".celleven{background-color:#eceef8;border-top:solid #999999 1px;border-left:solid #999999 1px;border-right:solid #999999 1px;border-bottom:solid #999999 1px;}";
    extraStyles += ".readwrite {background-color:white;}";
    defn = defn.substring(0,at)+extraStyles+defn.substring(at);
  }
  objDom = new ActiveXObject("Microsoft.XMLDOM");
  objDom.preserveWhiteSpace = true;
  var objRoot = objDom.createElement("table");
  objDom.appendChild(objRoot);
  i2uiBuildSpreadsheetColumnWidths(objname, tblobj, ssobj, objRoot);
  var numrows = tblobj.rows.length;
  var numcols = tblobj.rows(0).cells.length;
  var value;
  var readonly = new Array("celleven","cellodd");
  var rowdefn;
  var objRow;
  var objRowAttr;
  var objCell;
  var objCellAttr;
  var rowobj;
  var cellobj;
  for (var i=0; i<numrows; i++)
  {
    rowobj = tblobj.rows(i);
    objRow = objDom.createElement("tr");
    objRowAttr = objDom.createAttribute("class");
    if (i == 0 && rowobj.className == "tableColumnHeadings")
      objRowAttr.text = "header";
    else
      objRowAttr.text = readonly[i%2];
    objRow.setAttributeNode(objRowAttr);
    objRoot.appendChild(objRow);
    for (var j=0; j<numcols; j++)
    {
      cellobj = rowobj.cells(j);
      objCell = objDom.createElement("td");
      objRow.appendChild(objCell);
      try
      {
        var tagName = cellobj.childNodes[0].tagName;
        if (tagName == "" || tagName == null) 
        {
          if ((formula = cellobj.getAttribute("x:fmla")) != null)
          {
            objCellAttr = objDom.createAttribute("x:fmla");
            objCellAttr.text = formula;
            objCell.setAttributeNode(objCellAttr);
            if (i == 1)
              lockedColumns[lockedColumns.length] = j;
          }
          else
          {
            if (i == 1 && j >= col)
              unlockedColumns[unlockedColumns.length] = j;
            objCell.text = cellobj.innerText;
          }
        }
        else
        if (tagName == "INPUT") 
        {
          objCellAttr = objDom.createAttribute("class");
          objCellAttr.text = "readwrite";
          objCell.setAttributeNode(objCellAttr);
          objCell.text = cellobj.childNodes[0].getAttribute("value");
          if (i == 1)
            unlockedColumns[unlockedColumns.length] = j;
        }
        else
        if (tagName == "SELECT") 
        {
          var selectobj = cellobj.childNodes[0];
          objCell.text = selectobj.options[selectobj.selectedIndex].text;
        }
        else
        {
          if (i == 1 && j >= col)
            unlockedColumns[unlockedColumns.length] = j;
          objCell.text = cellobj.innerText;
        }
      }
      catch(e)
      {
        try
        {
          if ((formula = cellobj.getAttribute("x:fmla")) != null)
          {
            objCellAttr = objDom.createAttribute("x:fmla");
            objCellAttr.text = formula;
            objCell.setAttributeNode(objCellAttr);
            if (i == 1)
              lockedColumns[lockedColumns.length] = j;
          }
          else
          {
            if (i == 1 && j >= col)
              unlockedColumns[unlockedColumns.length] = j;
            objCell.text = cellobj.innerText;
          }
        }
        catch(e){}
      }
    }
  }
  if (tbl2obj != null)
  {
    numrows = tbl2obj.rows.length;
    for (var i=0; i<numrows; i++)
    {
      rowobj = tbl2obj.rows(i);
      objRow = objDom.createElement("tr");
      objRowAttr = objDom.createAttribute("class");
      if (i == 0 && rowobj.className == "tableColumnHeadings")
        objRowAttr.text = "header";
      else
        objRowAttr.text = readonly[i%2];
      objRow.setAttributeNode(objRowAttr);
      objRoot.appendChild(objRow);
      for (var j=0; j<numcols; j++)
      {
        cellobj = rowobj.cells(j);
        objCell = objDom.createElement("td");
        objRow.appendChild(objCell);
        try
        {
          var tagName = cellobj.childNodes[0].tagName;
          if (tagName == "" || tagName == null) 
          {
            if ((formula = cellobj.getAttribute("x:fmla")) != null)
            {
              objCellAttr = objDom.createAttribute("x:fmla");
              objCellAttr.text = formula;
              objCell.setAttributeNode(objCellAttr);
              if (i == 0)
                lockedColumns[lockedColumns.length] = j;
            }
            else
            {
              if (i == 0 && j >= col)
                unlockedColumns[unlockedColumns.length] = j;
              objCell.text = cellobj.innerText;
            }
          }
          else
          if (tagName == "INPUT") 
          {
            objCellAttr = objDom.createAttribute("class");
            objCellAttr.text = "readwrite";
            objCell.setAttributeNode(objCellAttr);
            objCell.text = cellobj.childNodes[0].getAttribute("value");
            if (i == 0)
              unlockedColumns[unlockedColumns.length] = j;
          }
          else
          if (tagName == "SELECT") 
          {
            var selectobj = cellobj.childNodes[0];
            objCell.text = selectobj.options[selectobj.selectedIndex].text;
          }
          else
          {
            if (i == 0 && j >= col)
              unlockedColumns[unlockedColumns.length] = j;
            objCell.text = cellobj.innerText;
          }
        }
        catch(e)
        {
          if ((formula = cellobj.getAttribute("x:fmla")) != null)
          {
            objCellAttr = objDom.createAttribute("x:fmla");
            objCellAttr.text = formula;
            objCell.setAttributeNode(objCellAttr);
            if (i == 0)
              lockedColumns[lockedColumns.length] = j;
          }
          else
          {
            if (i == 0 && j >= col)
              unlockedColumns[unlockedColumns.length] = j;
            objCell.text = cellobj.innerText;
          }
        }
      }
    }
  }
  at = defn.indexOf("<table");
  ssobj.HTMLData = defn.substring(0,at)+objDom.xml;
  // set viewable range
  var range;
  if (numExtraRows == null)
    numExtraRows = 2;
  if (tbl2obj == null) 
    range = i2uiBuildRange(1,1,tblobj.rows.length+numExtraRows,tblobj.rows(0).cells.length);
  else
    range = i2uiBuildRange(1,1,tblobj.rows.length+tbl2obj.rows.length+numExtraRows,tblobj.rows(0).cells.length);
  ssobj.ViewableRange = range;
  ssobj.setAttribute("unlockedColumns",unlockedColumns);
  ssobj.setAttribute("lockedColumns",lockedColumns);
  i2uiFreezePane(objname, ssobj);
}
function i2uiPopulateSpreadsheet(objname, tblobj, ssobj, firstrow)
{
  // turn off recalc while populating
  //ssobj.activeSheet.EnableAutoCalculate = false;
  //ssobj.Calculation = ssobj.activeSheet.Constants.xlCalculationManual;
  ssobj.activeSheet.Application.ScreenUpdating = false;
  var col = ssobj.getAttribute("editablecolumn")/1;
  if (col == "*" || col == null)
    col = 0;
  var range = null;
  var numrows = tblobj.rows.length;
  var numcols = tblobj.rows(0).cells.length;
  var cellvalue;
  var rowobj;
  var tagName;
  for (var i=firstrow; i<numrows; i++)
  {
    rowobj = tblobj.rows(i);
    for (var j=0; j<numcols; j++)
    {
      if (rowobj.cells(j).childNodes &&
          rowobj.cells(j).childNodes.length > 0)
      {
        tagName = rowobj.cells(j).childNodes[0].tagName;
        if (tagName == "INPUT") 
          cellvalue = rowobj.cells(j).childNodes[0].getAttribute("value");
        else
        if (tagName == "SELECT")
        {
          var selectobj = rowobj.cells(j).childNodes[0];
          cellvalue = selectobj.options[selectobj.selectedIndex].text;
        }
        else
        if (j < col ||
            rowobj.cells(j).getAttribute("x:fmla") != null)
          continue;
        else
          cellvalue = rowobj.cells(j).innerText;
      }
      else
      {
        tagName == "";
        if (j < col ||
            rowobj.cells(j).getAttribute("x:fmla") != null)
          continue;
        else
          cellvalue = rowobj.cells(j).innerText;
      }
      try
      {
        var numExtraRows = 0;
        range = i2uiBuildRange(i+numExtraRows-firstrow,j+1);
        if (j < col && (tagName == "INPUT" || tagName == "SELECT"))
          ssobj.ActiveSheet.Range(range).Locked = false;
        ssobj.ActiveSheet.Range(range).Value = cellvalue;
      }
      catch(e){}
    }
  }                   
  // reenable calculation
  //ssobj.activeSheet.EnableAutoCalculate = true;
  try {ssobj.activeSheet.Application.ScreenUpdating = true;}catch(e){}
}
function i2uiCreateNamedRanges(objname, tblobj, ssobj)
{
  try{
    if (tblobj == null)
      tblobj = document.getElementById(objname);
    if (ssobj == null)
      ssobj = document.getElementById(objname+"Spreadsheet");
    var numrows = tblobj.rows.length;
    var numcols = tblobj.rows(0).cells.length;
    for (var j=0; j<numcols; j++)
    {
      var range1 = i2uiBuildRange("",j+1);
      var range2 = i2uiBuildRange(2,j+1,numrows,j+1,true);
      ssobj.activeSheet.Names.Add("col_"+range1, "="+range2);
    }
  } catch(e){}
}
function i2uiPopulateTable(objname, tblobj, ssobj, firstrow)
{
  try
  {
  var value;
  var range;
  var rowobj;
  var numrows = tblobj.rows.length;
  var numcols = tblobj.rows(0).cells.length;
  var col = ssobj.getAttribute("editablecolumn")/1;
  if (col == "*" || col == null)
    col = 0;
  for (var i=firstrow; i<numrows; i++)
  {
    rowobj = tblobj.rows(i);
    for (var j=col; j<numcols; j++)
    {
      range = i2uiBuildRange(i+2-firstrow,j+1);
      value = ssobj.ActiveSheet.Range(range).Value;
      if (value != null)
      {
        if (rowobj.cells(j).childNodes &&
            rowobj.cells(j).childNodes.length == 1 &&
            rowobj.cells(j).childNodes[0].tagName == "INPUT")
          rowobj.cells(j).childNodes[0].setAttribute("value", value);
        else
          rowobj.cells(j).innerText = value;
      }
    }
  }
  }
  catch(e){}
}
function i2uiSpreadsheetEditHandlerDelayed(id, row, column, numrows, numcolumns)
{
  try 
  {
    if (numrows == null) 
      numrows = 1;
    if (numcolumns == null) 
      numcolumns = 1;
    for (var i=0; i<numrows; i++)
    {
      for (var j=0; j<numcolumns; j++)
        i2uiUpdateRelatedPivot(id,row+i,column+j);
    }
    i2uiSpreadsheetChangeCallback(id);
  }
  catch(e){}
}
function i2uiUpdateRelatedPivot(id, row, column)
{
  try 
  {
     var at = id.indexOf("Spreadsheet");
     if (at != -1)
     {
       var id2 = id.substring(0,at);
       var pivot = document.getElementById(id2);
       if (pivot == null)
         pivot = document.getElementById(id2+"Pivot");
       var ssobj = document.getElementById(id);
       var range = i2uiBuildRange(row,column);
       var value = ssobj.ActiveSheet.Range(range).Value;

       pivot.editedRow.push(row);
       pivot.editedColumn.push(column);
       pivot.editedValue.push(value);
     }
  }
  catch(e){}
}
function i2uiCheckVersion(id, silent)
{
  var rc = true;
  var obj = document.getElementById(id);
  if (obj == null || 
      obj.MajorVersion < 10 || 
      parseInt(obj.BuildNumber) < 4109)
  {
    if (silent == null || !silent)
      alert("You are either missing or have an old version of the Microsoft Office Web Component installed.\nTherefore, this page most likely will not behave as expected.\n\nPlease visit the Microsoft site for download and installation instructions.\n");
    rc = false;
  }
  return rc;
}
var activePivot;
var splits = "";
var domid = "Microsoft.XMLDOM";
var pivotSchemas = 
    "xmlns:s='uuid:BDC6E3F0-6DA3-11d1-A2A3-00AA00C14882' "+
    "xmlns:rs='urn:schemas-microsoft-com:rowset' "+
    "xmlns:z='#RowsetSchema' "+
    "xmlns:dt='uuid:C2F41010-65B3-11d1-A29F-00AA00C14882'";

function i2uiDetermineDomId()
{
//  return;
  var prefixes = ["Msxml2.DOMDocument.5.0","Msxml2.DOMDocument.4.0","Msxml2.DOMDocument.3.0","Msxml2.DOMDocument.2.6","Msxml2.DOMDocument","Microsoft.XMLDOM"];
//  var prefixes = ["Msxml2.DOMDocument.3.0","Msxml2.DOMDocument.2.6","Msxml2.DOMDocument","Microsoft.XMLDOM"];
  for (var i=0; i<prefixes.length; i++) 
  {
    try 
    {
      var x = new ActiveXObject(prefixes[i]);
      domid = prefixes[i];
      return;
    }
    catch (e){}
  }
}
function i2uiInitPivot(id, h, w, pivotdataxml)
{
  if (!i2uiCheckVersion(id+"Pivot"))
    return false;
  i2uiDetermineDomId();
  i2uiToggleItemVisibility(id+"PivotMassEditForm", "hide");
  i2uiToggleItemVisibility(id+"PivotEditForm", "hide");
  i2uiToggleItemVisibility(id+"PivotEditTable", "show");
  try
  {
    i2uiinitMDI(document.getElementById(id+"PivotOwner"),0);
  }
  catch(e){}
  var obj   = document.getElementById(id+"PivotEdit");
  if (obj != null)
    obj.readOnly = true;
  var pivot = document.getElementById(id+"Pivot");
  if (pivot.dirty == null)
    pivot.dirty = false;
  if (pivot.retainFilters == null)
    pivot.retainFilters = false;
  pivot.keyfield = null;
  pivot.charted = (document.getElementById(id+"PivotChart")==null)?true:false;
  pivot.spreadsheet = (document.getElementById(id+"PivotSpreadsheet")==null)?true:false;
  pivot.componentShowing = "pivot";
  pivot.MeasureToAttribute = new Array();
  pivot.retainincludes = new Array();
  pivot.retainexcludes = new Array();
  var rc = true;
  try
  {
    xmlDom = null;
    var view = pivot.ActiveView;
    view.DisplayCellColor = true;
    if (pivotdataxml == null)
      pivotdataxml = i2uiGetPivotDataXML(id+"Pivot_xml");
    pivot.pivotdom = new ActiveXObject(domid);

    pivot.pivotdom.setProperty("SelectionLanguage", "XPath");
    pivot.pivotdom.setProperty("SelectionNamespaces", pivotSchemas);

    splits = " "+domid+"[";
    var tt0 = new Date();
    //var pivotxml = i2uiBuildPivotXML(pivotdataxml, pivot, id);
    i2uiBuildPivotXML(pivotdataxml, pivot, id, null);
    var tt1 = new Date();
    splits += "][";
    //i2uiPopulatePivot(pivot, pivotxml, true);
    //i2uiPopulatePivot(pivot, pivotDom.xml, true);
    i2uiPopulatePivot(pivot, null, true);
    var tt2 = new Date();
    splits += "][";
    var hasTotals = i2uiLayoutPivot(pivot, view);
    var tt3 = new Date();
    splits += "][";
    i2uiStylizePivot(pivot, view, id, h, w, hasTotals);
    var tt4 = new Date();
    splits += "] x"+(tt1-tt0)+" p"+(tt2-tt1)+" l"+(tt3-tt2)+" s"+(tt4-tt3);
    try
    {
      if ("row" == xmlDom.selectSingleNode("/pivot").getAttribute("orientation"))
        view.TotalOrientation = pivot.Constants.plTotalOrientationRow;
    }
    catch(e){}
  }
  catch(e)
  {
    alert("Error during pivot creation. "+e.message);
    rc = false;
  }
  return(rc);
}
function i2uiStylizePivot(pivot, view, id, h, w, hasTotals)
{
  view.RowAxis.DisplayEmptyMembers = false;
  view.ColumnAxis.DisplayEmptyMembers = false;
  // hide only if totals computed in data area
  if (hasTotals == null || hasTotals)
  {
    try {pivot.ActiveData.HideDetails();}
    catch(e){}
  }
  else
  {
    view.ExpandDetails = pivot.Constants.plExpandAlways;
  }
  view.HeaderBackColor = PivotLabelColor;
  view.HeaderFont.Bold = true;
  view.HeaderFont = PivotFontFamily;
  view.TotalFont = PivotFontFamily;
  view.FieldLabelFont = PivotFontFamily;
  view.PropertyValueFont = PivotFontFamily;

  pivot.DisplayOfficeLogo = false;
  view.AllowEdits = true;
  view.TitleBar.Caption = "";
  view.TitleBar.Visible = false;
  view.FieldLabelBackColor = PivotLabelColor;
  var i,len;
  var field;
  var axis;
  axis = view.ColumnAxis;
  len = axis.FieldSets.Count;
  for (i=0; i<len; i++)
  {
    field = axis.FieldSets(i).BoundField;
    field.GroupedBackColor = PivotCellColor;
    field.SubtotalLabelBackColor = PivotCellTotalColor;
    field.DetailFont = PivotFontFamily;
    field.SubtotalFont = PivotFontFamily;
    field.SubtotalLabelFont = PivotFontFamily;
    field.GroupedFont = PivotFontFamily;
  }
  axis = view.FilterAxis;
  len = axis.FieldSets.Count;
  for (i=0; i<len; i++)
  {
    field = axis.FieldSets(i).BoundField;
    field.GroupedBackColor = PivotCellColor;
    field.SubtotalLabelBackColor = PivotCellTotalColor;
    field.DetailFont = PivotFontFamily;
    field.SubtotalFont = PivotFontFamily;
    field.SubtotalLabelFont = PivotFontFamily;
    field.GroupedFont = PivotFontFamily;
  }
  axis = view.RowAxis;
  len = axis.FieldSets.Count;
  for (i=0; i<len; i++)
  {
    field = axis.FieldSets(i).BoundField;
    field.GroupedBackColor = PivotCellColor;
    field.SubtotalLabelBackColor = PivotCellTotalColor;
    field.DetailFont = PivotFontFamily;
    field.SubtotalFont = PivotFontFamily;
    field.SubtotalLabelFont = PivotFontFamily;
    field.GroupedFont = PivotFontFamily;
  }
  axis = view.DataAxis;
  len = axis.FieldSets.Count;
  for (i=0; i<len; i++)
  {
    field = axis.FieldSets(i).BoundField;
    field.GroupedBackColor = PivotCellColor;
    field.SubtotalLabelBackColor = PivotCellTotalColor;
    field.DetailFont = PivotFontFamily;
    field.SubtotalFont = PivotFontFamily;
    field.SubtotalLabelFont = PivotFontFamily;
    field.GroupedFont = PivotFontFamily;
  }
}
function i2uiBestFitPivot(id,h,w,resize)
{
  var pivot = document.getElementById(id+"Pivot");
  var view = pivot.ActiveView;
  if (h==null || w==null)
  {
    var obj3 = document.getElementById(id+"Container");
    if (obj3 != null)
    {
      if (h == null)
        h = Math.max(50,obj3.clientHeight-55);
      if (w == null)
      {
        w = Math.max(50,(obj3.clientWidth-40));
        if (pivot.componentShowing == "pivotchart")
          w = w / 2;
      }
    }
  }
  if (w != null)
  {
    // compute width of filter axis
    var computedWidth = 0;
    var cnt = view.FilterAxis.FieldSets.Count;
    for (var i=0; i<cnt; i++)
      computedWidth += view.FilterAxis.FieldSets(i).Width;
    w = Math.max(w, computedWidth);
    // compute width of row and column axis
    var computedWidth = 0;
    var cnt = view.RowAxis.FieldSets.Count;
    for (var i=0; i<cnt; i++)
      computedWidth += view.RowAxis.FieldSets(i).BoundField.GroupedWidth;
    var cnt = view.ColumnAxis.FieldSets.Count;
    for (var i=0; i<cnt; i++)
      computedWidth += view.ColumnAxis.FieldSets(i).BoundField.GroupedWidth;
    w = Math.max(w, computedWidth);
    pivot.Width = w;
  }
  if (h != null)
  {
    if (pivot.Height < h && resize == null)
    {
      //let component size to what it needs
      pivot.AutoFit = true;
      setTimeout("i2uiBestFitPivot('"+id+"',"+h+","+w+",false)",250);
    }
    else
    {
      pivot.Height = Math.min(pivot.Height, h);
    }
  }
  // handle large number of data axis elements
  view.DetailMaxWidth=8192;
}
function i2uiCollapsePivot(pivot, view)
{
  var i,len;

  len = view.ColumnAxis.FieldSets.Count;
  for (i=0; i<len-1; i++)
    view.ColumnAxis.FieldSets(i).Fields(0).Expanded = true;
  if (len > 0)
    view.ColumnAxis.FieldSets(len-1).Fields(0).Expanded = false;

  len = view.RowAxis.FieldSets.Count;
  for (i=0; i<len-1; i++)
    view.RowAxis.FieldSets(i).Fields(0).Expanded = true;
  if (len > 0)
    view.RowAxis.FieldSets(len-1).Fields(0).Expanded = false;
}
function i2uiLayoutPivot(pivot, view)
{
  var rc = false;
  view.AutoLayout();

  activePivot = pivot;
  // retain style info
  pivot.chartColorInfo = new Array();
  pivot.chartStyleInfo = new Array();
  pivot.chartTypeInfo = new Array();
  pivot.seriesColor = new Array();
  pivot.seriesColorCount = 0;
  pivot.seriesStyle = new Array();
  pivot.seriesStyleCount = 0;
  pivot.editableColumns = new Array();
  pivot.proportionalEditColumns = new Array();
  pivot.lockedColumns = new Array();

  var customSort = new Array();
  try
  {
    pivot.coloring = 1*xmlDom.selectSingleNode("/pivot/chart").getAttribute("coloring");
  }
  catch(e)
  {
    pivot.coloring = 2;
  }
  i2uiPivotAsofName = null;
  i2uiPivotAsofCaption = null;
  i2uiPivotAsofValue = null;
  i2uiPivotAsofColor = null;
  try
  {
    i2uiPivotAsofName = xmlDom.selectSingleNode("/pivot/chart").getAttribute("asofname");
    i2uiPivotAsofValue = xmlDom.selectSingleNode("/pivot/chart").getAttribute("asofvalue");
    i2uiPivotAsofColor = xmlDom.selectSingleNode("/pivot/chart").getAttribute("asofcolor");
  } catch(e){}
  try
  {
    i2uiPivotAsofCaption = xmlDom.selectSingleNode("/pivot/chart").getAttribute("asofcaption");
    var len = i2uiPivotAsofCaption.length;
  } catch(e){i2uiPivotAsofCaption="Asof";}
  try
  {
    pivot.chartThreshold = xmlDom.selectSingleNode("/pivot/chart/threshhold").xml;
  } catch(e){pivot.chartThreshold="";}
  
  var stylenodes = xmlDom.selectNodes("/pivot/chart/*");
  if (stylenodes.length == 0) stylenodes = xmlDom.selectNodes("/PIVOT/CHART/*");
  pivot.chartColorInfo[0] = stylenodes.length;
  for (var i=0; i<stylenodes.length; i++)
  {
    var item = xmlDom.selectSingleNode("/pivot/item[@name='"+stylenodes[i].nodeName+"']");
    if (item == null)
      item = xmlDom.selectSingleNode("/pivot/view"+pivot.viewname+"/item[@name='"+stylenodes[i].nodeName+"']");
    if (item != null) 
    {
      var key = item.text+"_"+stylenodes[i].getAttribute("value");
      pivot.chartColorInfo[key] = stylenodes[i].getAttribute("color");
      pivot.chartStyleInfo[key] = stylenodes[i].getAttribute("style");
      pivot.chartTypeInfo[item.text] = stylenodes[i].getAttribute("type");
      if (stylenodes[i].nodeName != item.text)
      {
        var key = stylenodes[i].nodeName+"_"+stylenodes[i].getAttribute("value");
        pivot.chartColorInfo[key] = stylenodes[i].getAttribute("color");
        pivot.chartStyleInfo[key] = stylenodes[i].getAttribute("style");
        pivot.chartTypeInfo[stylenodes[i].nodeName] = stylenodes[i].getAttribute("type");
      }
    }
  }

  var numberformat;
  var itemnodes = xmlDom.selectNodes("/pivot/item");
  if (itemnodes.length == 0) itemnodes = xmlDom.selectNodes("/pivot/view"+pivot.viewname+"/item");
  var numitems = itemnodes.length;
  for (var i=0; i<numitems; i++)
  {
    var itemName = itemnodes[i].text.substring(0,maxItemNameLength);
    if (itemnodes[i].getAttribute("axis") == "row")
      view.RowAxis.InsertFieldSet(view.FieldSets(itemName));
    else
    if (itemnodes[i].getAttribute("axis") == "data")
    {
      view.DataAxis.InsertFieldSet(view.FieldSets(itemName));
      if (itemnodes[i].getAttribute("editable") == "yes" &&
          itemnodes[i].getAttribute("formula") == null)
      {
        pivot.editableColumns[itemName] = true;
        if (itemnodes[i].getAttribute("proportional") == "no")
          pivot.proportionalEditColumns[itemName] = false;
        else
          pivot.proportionalEditColumns[itemName] = true;
      }
    }
    else
    if (itemnodes[i].getAttribute("axis") == "column")
      view.ColumnAxis.InsertFieldSet(view.FieldSets(itemName));
    else
    if (itemnodes[i].getAttribute("axis") == "filter")
      view.FilterAxis.InsertFieldSet(view.FieldSets(itemName));

    // change caption for long names
    if (itemnodes[i].text.length > maxItemNameLength)
      view.FieldSets(itemName).Fields(0).Caption = itemnodes[i].text;
    // handle unsorted data
    if (itemnodes[i].getAttribute("sorted") == "no")
      view.FieldSets(itemName.substring(0,maxItemNameLength)).Fields(0).SortDirection = pivot.Constants.plSortDirectionCustom;
    
    // set initial filtering
    var filter;
    var filterArray;
    var filterSeparator = itemnodes[i].getAttribute("separator");
    if (filterSeparator == null)
      filterSeparator = ",";
    filter = itemnodes[i].getAttribute("include");
    if (filter == null)
    {
      try
      {
        filter = pivot.retainincludes[itemName].join(filterSeparator);
      }
      catch(e)
      {filter=null;}
    }
    if (filter != null)
    {
      filterArray = filter.split(filterSeparator);
      view.FieldSets(itemName).Fields(0).IncludedMembers = filterArray;
    }
    
    filter = itemnodes[i].getAttribute("exclude");
    if (filter == null)
    {
      try
      {
        filter = pivot.retainexcludes[itemName].join(filterSeparator);
      }
      catch(e)
      {filter=null;}
    }
    if (filter != null)
    {
      filterArray = filter.split(filterSeparator);
      view.FieldSets(itemName).Fields(0).ExcludedMembers = filterArray;
    }

    // set initial formating
    if ((numberformat = itemnodes[i].getAttribute("numberformat")) != null)
    {
      view.FieldSets(itemName).Fields(0).NumberFormat = numberformat;
    }
  }  
  var cleanup = new Array();
  // create totals for data axis
  for (var i=0; i<numitems; i++)
  {
    if (itemnodes[i].getAttribute("axis") == "data")
    {
      var itemName = itemnodes[i].text.substring(0,maxItemNameLength);
      var itemType = itemnodes[i].getAttribute("type");
      if (itemType == "string" ||
          itemType == "date" ||
          itemType == "datetime")
      {
        view.FieldSets(itemName).Fields(0).Subtotals(1) = false;
        view.FieldSets(itemName).Fields(0).Expanded = true;
        rc = false;
      }
      else
      {
        var formula = itemnodes[i].getAttribute("formula");
        if (formula != null)
        {
          var expression;
          var bgcolor = itemnodes[i].getAttribute("bgcolor");
          var forecolor = itemnodes[i].getAttribute("forecolor");

          if (bgcolor != null || forecolor != null)
          {
            expression = "'"+formula+"'"
            if (bgcolor != null)
              expression += ",back_color='"+bgcolor+"'";
            if (forecolor != null)
              expression += ",fore_color='"+forecolor+"'";
          }
          else
            expression = formula;

          if (itemnodes[i].getAttribute("total") == "no")
          {
            var theFieldSet = view.AddFieldSet("fieldset_"+itemName);
            theFieldSet.AddCalculatedField(itemName,
                                           itemName,
                                           "calculated_"+itemName,
                                           formula);
            view.DataAxis.InsertFieldSet(theFieldSet);
            var showAs = itemnodes[i].getAttribute("showas");
            if (showAs == null || showAs == "")
              theTotal = null;
            else
            {
              rc = true;
              theTotal = view.AddTotal(itemName,
                                       theFieldSet.Fields(0),
                                       showAsValues[showAs]);
            }
           view.DataAxis.RemoveFieldSet(view.FieldSets(itemName));
          }
          else
          {
            rc = true;
            theTotal = view.AddCalculatedTotal(itemName,
                                               itemName,
                                               expression);
            if ((numberformat = itemnodes[i].getAttribute("numberformat")) != null)
              theTotal.NumberFormat = numberformat;
          }
        }
        else
        {
          if (itemnodes[i].getAttribute("total") == "no")
            continue;
          rc = true;
          var showAs = itemnodes[i].getAttribute("showas");
          var sourceName = itemnodes[i].getAttribute("source");
          if (sourceName == null)
            sourceName = itemName;
          try
          {
            theTotal = view.AddTotal(itemName, 
                                     view.FieldSets(sourceName).Fields(0),
                                     showAsValues[showAs]);
          }
          catch(e)
          {
            theTotal = view.AddTotal(itemName, 
                                     view.FieldSets(sourceName).Fields(0),
                                     pivot.Constants.plFunctionSum);
          }
        }
        if (theTotal != null)
        {
          var hidden = itemnodes[i].getAttribute("hidden");
          if (hidden == null || hidden == "no")
            view.DataAxis.InsertTotal(theTotal);
          theTotal.View.HeaderBackColor = PivotLabelColor;
          theTotal.View.HeaderFont.Bold = true;
        }
      }
    }
    else
    if (itemnodes[i].getAttribute("axis") != "none")
    {
      var formula = itemnodes[i].getAttribute("formula");
      if (formula != null)
      {
        var itemName = itemnodes[i].text.substring(0,maxItemNameLength);
        var theFieldSet = view.AddFieldSet(itemName+" ");
        theFieldSet.AddCalculatedField(itemName,
                                       itemName,
                                       "calculated_"+itemName,
                                       formula);
        view.FilterAxis.InsertFieldSet(theFieldSet);
        view.FilterAxis.RemoveFieldSet(view.FieldSets(itemName));
        //cleanup.push(itemName+"data");
        view.FilterAxis.RemoveFieldSet(view.FieldSets(itemName+"data"));
      }

      // optionally turn off totaling for rows and columns
      if (itemnodes[i].getAttribute("total") == "no")
      {
        var itemName = itemnodes[i].text.substring(0,maxItemNameLength);
        view.FieldSets(itemName).Fields(0).Subtotals(1) = false;
      }

      if (itemnodes[i].getAttribute("sort") == "fifo")
      {
        customSort[customSort.length] = i;
      }
      else
      if (itemnodes[i].getAttribute("sort") == "lifo")
      {
        customSort[customSort.length] = i;
      }
      else
      if (itemnodes[i].getAttribute("sort") == "descending")
      {
        var displayName = itemnodes[i].text.substring(0,maxItemNameLength);
        view.FieldSets(displayName).Fields(0).SortDirection = pivot.Constants.plSortDirectionDescending;
      }
      else
      if (itemnodes[i].getAttribute("sort") == "ascending")
      {
        var displayName = itemnodes[i].text.substring(0,maxItemNameLength);
        view.FieldSets(displayName).Fields(0).SortDirection = pivot.Constants.plSortDirectionAscending;
      }
    }
  }
  // handle custom display order
  var len = customSort.length;
  for (var i=0; i<len; i++)
  {
    var itemName = itemnodes[customSort[i]].getAttribute("name");
    var displayName = itemnodes[customSort[i]].text.substring(0,maxItemNameLength);

    var sourceOrder = i2uiBuildCustomOrder(itemName, displayName);
    if (itemnodes[customSort[i]].getAttribute("sort") == "fifo")
      view.FieldSets(displayName).Fields(0).OrderedMembers = sourceOrder;
    else
      view.FieldSets(displayName).Fields(0).OrderedMembers = sourceOrder.reverse();
    view.FieldSets(displayName).Fields(0).SortDirection = pivot.Constants.plSortDirectionCustom;
  }

  var groupnodes = xmlDom.selectNodes("/pivot/group");
  if (groupnodes.length == 0) 
    groupnodes = xmlDom.selectNodes("/pivot/view"+pivot.viewname+"/group");
  var numGroups = groupnodes.length;
  for (var i=0; i<numGroups; i++)
  {
    var groupName = groupnodes[i].getAttribute("name");
    var sourceName = groupnodes[i].getAttribute("source");
    var theFieldSet = i2uiPivotFieldFromName(sourceName, view);
    var subField = theFieldSet.AddCustomGroupField(groupName,
                                               groupName);
    subField.GroupedBackColor = PivotCellColor;
    var total = groupnodes[i].getAttribute("total");
    if (total == null || total == "no")
      subField.Subtotals(1) = false;
    else
      subField.SubtotalLabelBackColor = PivotCellTotalColor;
    var membernodes = groupnodes[i].selectNodes("members");
    var numMembers = membernodes.length;
    for (var j=0; j<numMembers; j++)
    {
      var members = membernodes[j].text.split(",");
      // replace each listed value with internal value
      // critical for nested groups since component can generate cryptic names
      var numchildren = theFieldSet.members(0).childmembers.count;
      var numNames = members.length;
      var processed = 0;
      for (k=0; k<numNames; k++)
      {
        for (l=0; l<numchildren; l++)
        {
          if (theFieldSet.members(0).childmembers(l).caption == members[k])
          {
            members[processed] = theFieldSet.members(0).childmembers(l).name;
            processed++;
            break;
          }
        }
      }
      // remove invalid names
      for (var k=processed; k<numNames; k++)
        members.pop();
      try
      {
        if (members.length > 0)
          subField.AddCustomGroupMember(theFieldSet.Name, 
                                        members,
                                        membernodes[j].getAttribute("caption"));
      }
      catch(e)
      {
      }
    }
  }
  var len = cleanup.length;
  for (var i=0; i<len; i++)
  {
      //alert("remove "+cleanup[i]);
      view.FilterAxis.RemoveFieldSet(view.FieldSets(cleanup[i]));
  }
  
  return rc;
}
function i2uiPivotFieldFromName(name, view)
{
  try
  {
    return view.FieldSets(name);
  }
  catch(e)
  {
    var parentNode = xmlDom.selectSingleNode("/pivot/group[@name='"+name+"']");
    if (parentNode == null) 
      parentNode = xmlDom.selectSingleNode("/pivot/view"+pivot.viewname+"/group[@name='"+name+"']");
    if (parentNode == null) 
      return null;
    else
    {
      //alert(name+" missing, trying "+parentNode.getAttribute("source"));
      return i2uiPivotFieldFromName(parentNode.getAttribute("source"), view);
    }
  }
}
function i2uiBuildCustomOrder(itemName, displayName)
{
  var sourceOrder = new Array();
  var uniqueValue = new Array();
  var nodeValues = xmlDom.selectNodes("/pivot/flatdata/*");
  var valuesLen = nodeValues.length;
  if (valuesLen == 0)
  {
    nodeValues = xmlDom.selectNodes("/pivot/treedata/node[@"+itemName+"]");
    valuesLen = nodeValues.length;
  }

  if (valuesLen > 0)
  {
    var j = 0;
    var value;
    for (var k=0; k<valuesLen; k++)
    {
      value = nodeValues[k].getAttribute(itemName);
      if (uniqueValue[value] == null)
      {
        uniqueValue[value] = true;
        sourceOrder[j] = value;
        j++;
      }
    }
  }
  return sourceOrder;
}
function i2uiRestoreCustomOrder(fullid, measureName)
{
  try
  {
    var pivot = document.getElementById(fullid);
    var view = pivot.ActiveView;
    var customSort = new Array();
    var xpath = "/pivot/view"+pivot.viewname+"/item[text()='"+measureName+"']";
    var itemnode = xmlDom.selectSingleNode(xpath);
    if (itemnode == null)
    {
      xpath = "/pivot/item[text()='"+measureName+"']";
      itemnode = xmlDom.selectSingleNode(xpath);
    }
    if (itemnode != null)
    {
      if (itemnode.getAttribute("axis") != "none")
      {
        var displayName = itemnode.text.substring(0,maxItemNameLength);
        if (itemnode.getAttribute("sort") == "fifo")
        {
          var itemName = itemnode.getAttribute("name");
          var sourceOrder = i2uiBuildCustomOrder(itemName, displayName);
          if (sourceOrder.length > 0)
          {
            view.FieldSets(displayName).Fields(0).OrderedMembers = sourceOrder;
            view.FieldSets(displayName).Fields(0).SortDirection = pivot.Constants.plSortDirectionCustom;
          }
        }
        else
        if (itemnode.getAttribute("sort") == "lifo")
        {
          var itemName = itemnode.getAttribute("name");
          var sourceOrder = i2uiBuildCustomOrder(itemName, displayName);
          if (sourceOrder.length > 0)
          {
            view.FieldSets(displayName).Fields(0).OrderedMembers = sourceOrder.reverse();
            view.FieldSets(displayName).Fields(0).SortDirection = pivot.Constants.plSortDirectionCustom;
          }
        }
        else
        if (itemnode.getAttribute("sort") == "descending")
          view.FieldSets(displayName).Fields(0).SortDirection = pivot.Constants.plSortDirectionDescending;
        else
        if (itemnode.getAttribute("sort") == "ascending")
          view.FieldSets(displayName).Fields(0).SortDirection = pivot.Constants.plSortDirectionAscending;
      }
    }
  }
  catch(e){}
}
function i2uiBuildAppMenu(pivot, measureName)
{
  var menus = new Array();
  var xpath = "/pivot/view"+pivot.viewname+"/item[text()='"+measureName+"']";
  var itemnode = xmlDom.selectSingleNode(xpath);
  if (itemnode == null)
  {
    xpath = "/pivot/item[text()='"+measureName+"']";
    itemnode = xmlDom.selectSingleNode(xpath);
  }
  if (itemnode != null)
  {
    xpath = "/pivot/menu[@owner='"+itemnode.getAttribute("name")+"']";
    var menunodes = xmlDom.selectNodes(xpath);
    var len = menunodes.length;
    for (var i=0; i<len; i++)
      menus.push(menunodes[i].getAttribute("text"));
  }
  return menus;
}
function i2uiInvokeAppMenu(pivot, measureName, menuindex, nameValues)
{
  var xpath = "/pivot/view"+pivot.viewname+"/item[text()='"+measureName+"']";
  //alert(xpath);
  var itemnode = xmlDom.selectSingleNode(xpath);
  //alert(itemnode);
  if (itemnode == null)
  {
    xpath = "/pivot/item[text()='"+measureName+"']";
    itemnode = xmlDom.selectSingleNode(xpath);
  }
  if (itemnode != null)
  {
    //var xpath = "/pivot/menu[@owner='"+itemnode.getAttribute("name")+"']["+menuindex+"]";
    var xpath = "/pivot/menu[@owner='"+itemnode.getAttribute("name")+"'][" + ++menuindex + "]";
    var menunode = xmlDom.selectSingleNode(xpath);
    if (menunode != null)
    {
      try 
      {
        var cmd = menunode.getAttribute("cmd");
        if (cmd == "i2uiPivotToggleLock()")
        {
          i2uiPivotToggleLock(pivot, measureName, nameValues);
        }
        else
        {
          cmd = cmd.replace(/%NAMEVALUES/g, nameValues);
          eval(cmd);
        }
      }
      catch(e){}
    }
  }
}
function i2uiPopulatePivot(pivot, xml, bDomPresent)
{
  try
  {
    var t0 = new Date();
    var oRS = new ActiveXObject("ADODB.Recordset");
    var t1 = new Date();
    splits += "rs"+(t1-t0);

    // recent security patch prevents IE when creating stream
    //var oStream = new ActiveXObject("ADODB.Stream");
    //oStream.Open
    //oStream.WriteText(xml);
    //oStream.Position = 0;
    //oRS.Open(oStream);

    // instead use a dom object
    if (bDomPresent != null && bDomPresent)
    {
      var t2 = new Date();
      oRS.Open(pivot.pivotdom);
      var t3 = new Date();
      pivot.xmlData2 = pivotDom.xml;
      var t4 = new Date();
      splits += " open"+(t3-t2)+" cpy"+(t4-t3);
    }
    else
    {
      var t2 = new Date();
      var tempDom = new ActiveXObject(domid);
      if (tempDom.loadXML(xml))
        oRS.Open(tempDom);
      var t3 = new Date();
      pivot.xmlData2 = xml;
      var t4 = new Date();
      splits += " dom"+(t3-t2)+" cpyxml"+(t4-t3);
    }
    var t4 = new Date();
    pivot.DataSource = oRS;    
    var t5 = new Date();
    //splits += " pop"+(t5-t0);
  }
  catch(e)
  {
    alert("Error inside PopulatePivot. "+e+" "+e.message());
  }
}
function i2uiBuildPivotXML(xml, pivot, id, viewname)
{
  if (viewname == null)
    viewname = "[@default='yes']";
  var attr;
  pivotDom = pivot.pivotdom;
  if (xmlDom == null)
  {
    xmlDom = new ActiveXObject(domid);
    xmlDom.setProperty("SelectionLanguage", "XPath");
    xmlDom.setProperty("SelectionNamespaces", pivotSchemas);
    // place namespace on row tags.  allows cloning to occur which 
    // will speed performance dramatically
    var t0 = new Date();
    xml = xml.replace(/<row/g, "<z:row xmlns:z=\"#RowsetSchema\"");
    var t1 = new Date();
    if (!xmlDom.loadXML(xml))
      return "";
    var t2 = new Date();
    splits += "repl"+(t1-t0)+" load"+(t2-t1);
  }
  var startItems = new Date();
  pivot.fieldTypes = new Array();
  pivotDom.preserveWhiteSpace = true;
  var pivotRoot = pivotDom.createElement("xml");
  pivotDom.appendChild(pivotRoot);
  attr = pivotDom.createAttribute("xmlns:x");
  attr.text = "urn:schemas-microsoft-com:office:excel";
  pivotRoot.setAttributeNode(attr);
  attr = pivotDom.createAttribute("xmlns:dt");
  attr.text = "uuid:C2F41010-65B3-11d1-A29F-00AA00C14882";
  pivotRoot.setAttributeNode(attr);
  attr = pivotDom.createAttribute("xmlns:s");
  attr.text = "uuid:BDC6E3F0-6DA3-11d1-A2A3-00AA00C14882";
  pivotRoot.setAttributeNode(attr);
  attr = pivotDom.createAttribute("xmlns:rs");
  attr.text = "urn:schemas-microsoft-com:rowset";
  pivotRoot.setAttributeNode(attr);
  attr = pivotDom.createAttribute("xmlns:z");
  attr.text = "#RowsetSchema";
  pivotRoot.setAttributeNode(attr);
  var cacheNode = pivotDom.createElement("x:PivotCache");
  pivotRoot.appendChild(cacheNode);
  var indexNode = pivotDom.createElement("x:CacheIndex");
  indexNode.text = 1;
  cacheNode.appendChild(indexNode);
  var schemaNode = pivotDom.createElement("s:Schema");
  attr = pivotDom.createAttribute("id");
  attr.text = "RowsetSchema";
  schemaNode.setAttributeNode(attr);
  cacheNode.appendChild(schemaNode);
  var elementNode = pivotDom.createElement("s:ElementType");
  attr = pivotDom.createAttribute("name");
  attr.text = "row";
  elementNode.setAttributeNode(attr);
  attr = pivotDom.createAttribute("content");
  attr.text = "eltOnly";
  elementNode.setAttributeNode(attr);
  schemaNode.appendChild(elementNode);
  var pivotnode = xmlDom.selectSingleNode("/pivot");
  pivot.keyfield = pivotnode.getAttribute("keyfield");
  var itemnodes = pivotnode.selectNodes("item");
  if (itemnodes.length == 0) itemnodes = pivotnode.selectNodes("view"+viewname+"/item");
  if (itemnodes.length == 0)
  {
    // pick first view if no default specified
    var viewnodes = pivotnode.selectNodes("view");
    viewname = "[@name='"+viewnodes[0].getAttribute("name")+"']";
    itemnodes = pivotnode.selectNodes("view"+viewname+"/item");
  }
  // if we reach here with no item nodes, we are in for big trouble
  if (itemnodes.length == 0)
  {
    alert("Could not find any 'item' information in the data.");
    return "";
  }

  var editableItems = new Array();
  pivot.editableItems = editableItems;
  var namemap = new Object();
  pivot.namemap = namemap ;
  pivot.viewname = viewname;
  for (var i=0; i<itemnodes.length; i++)
  {
    var node = itemnodes[i];
    // do not build a fieldset entry
    if (node.getAttribute("axis") == "none")
      continue;
    var name = itemnodes[i].getAttribute("name");;
    if(node.getAttribute("editable") == "yes")
      editableItems[editableItems.length] = name;
    namemap[node.text] = name;
    var attributeNode = pivotDom.createElement("s:attribute");
    attr = pivotDom.createAttribute("type");
    attr.text = name;
    attributeNode.setAttributeNode(attr);
    elementNode.appendChild(attributeNode);
  }
  var endItems = new Date();
  var extendsNode = pivotDom.createElement("s:extends");
  attr = pivotDom.createAttribute("type");
  attr.text = "rs:rowbase";
  extendsNode.setAttributeNode(attr);
  elementNode.appendChild(extendsNode);
  var filter = "";
  var fieldname;
  var measurename;
  for (var i=0; i<itemnodes.length; i++)
  {
    var attributeTypeNode = pivotDom.createElement("s:AttributeType");

    attr = pivotDom.createNode("attribute","rs:name","urn:schemas-microsoft-com:rowset");
    measurename = attr.text = itemnodes[i].text.substring(0,maxItemNameLength);
    attributeTypeNode.setAttributeNode(attr);

    attr = pivotDom.createAttribute("name");
    fieldname = attr.text = itemnodes[i].getAttribute("name");

    pivot.MeasureToAttribute[measurename]=fieldname;

    var render = itemnodes[i].getAttribute("render");
    if (render != null)
    {
      if (render == "blank")
      {
        if (filter.length > 0)
          filter += " and ";
        filter += "not(@"+attr.text+")";
      }
      else
      if (render == "nonblank")
      {
          if (filter.length > 0)
            filter += " and ";
          filter += "@"+attr.text;
      }
    }

    attributeTypeNode.setAttributeNode(attr);
    var dataTypeNode = pivotDom.createElement("s:datatype");
    var dataType = itemnodes[i].getAttribute("type");
    pivot.fieldTypes[fieldname] = dataType;
    if (dataType == null || dataType.length == 0)
    {
      if (itemnodes[i].getAttribute("axis") == "data")
      {
        attr = pivotDom.createAttribute("dt:type");
        attr.text = "float";
        pivot.fieldTypes[fieldname] = "float";
      }
      else
      {
        attr = pivotDom.createAttribute("dt:maxLength");
        attr.text = 255;
        pivot.fieldTypes[fieldname] = "string";
      }
    }
    else
    {
      attr = pivotDom.createAttribute("dt:type");
      attr.text = dataType;
      pivot.fieldTypes[fieldname] = dataType;
    }
    dataTypeNode.setAttributeNode(attr);
    attributeTypeNode.appendChild(dataTypeNode);
    schemaNode.appendChild(attributeTypeNode);
  }
  var dataNode = pivotDom.createElement("rs:data");
  cacheNode.appendChild(dataNode);
  var endItems2 = new Date();

  var flatdatanode = pivotnode.selectSingleNode("flatdata");
  if (flatdatanode != null)
  {
    // if pivot is dirty, merge changed rows
    if (pivot.dirty)
    {                     
      //alert("new view with unsaved changed data");
      i2uiInternallyPersistPivotEdits(pivot, flatdatanode);
    }

    if (filter.length > 0)
      filter = "["+filter+"]";
    var rownodes = flatdatanode.selectNodes("z:row"+filter);
    //var rownodes = flatdatanode.selectNodes("*"+filter);
    var rowlen = rownodes.length;
    var editableLength = editableItems.length;
    for (var i=0; i<rowlen; i++){
      var n = rownodes[i].cloneNode(false);
      for(var j = 0; j < editableLength; ++j){
        var a = editableItems[j];
        n.setAttribute('Old' + a, n.getAttribute(a));
      }
      dataNode.appendChild(n);
    }
  }
  else
  {
    var treenodes = xmlDom.selectNodes("/pivot/treedata/node");
    if (treenodes.length == 0) treenodes = xmlDom.selectNodes("/PIVOT/TREEDATA/NODE");
    var rowNode = pivotDom.createElement("z:row");
    for (var i=0; i<treenodes.length; i++)
    {
      i2uiWalkDataTree(pivotDom, dataNode, rowNode, treenodes[i]);
    }
  }
  var endData = new Date();

  // build views list - only for first execution
  var viewlist = document.getElementById(id+"views");
  if (viewlist != null && viewlist.options.length==0)
  {
    var viewnodes = xmlDom.selectNodes("/pivot/view");
    var len = viewnodes.length;
    if (len > 1)
      for (var i=0; i<len; i++)
      {
        viewlist.options[i] = new Option(viewnodes[i].getAttribute("name"));
        if (viewnodes[i].getAttribute("default") == "yes")
          viewlist.options[i].selected = true;
      }
  }
  var endViews = new Date();
  
  splits += " clone"+(endData-endItems2);
  
//  return(pivotDom.xml);
}
function i2uiWalkDataTree(pivotDom, dataNode, rowNode, treeNode)
{
  for (var j=0; j<treeNode.attributes.length; j++)
  {
    rowNode.setAttributeNode(treeNode.attributes[j].cloneNode(true));
  }
  var childnodes = treeNode.selectNodes("node");
  if (childnodes.length == 0) childnodes = treeNode.selectNodes("NODE");
  for (var i=0; i<childnodes.length; i++)
  {
    i2uiWalkDataTree(pivotDom, dataNode, rowNode, childnodes[i]);
  }
  if (childnodes.length == 0)
  {
    var newNode = pivotDom.createElement("z:row");
    for (var j=0; j<rowNode.attributes.length; j++)
    {
      newNode.setAttributeNode(rowNode.attributes[j].cloneNode(true));
    }
    dataNode.appendChild(newNode);
  }
}
function i2uiGetPivotDataXML(id)
{
  var xml = null;
  var obj = document.getElementById(id);
  if (obj.tagName.toLowerCase() == "div")
    xml = obj.innerHTML;
  else
  {
    try
    {
      xml = obj.XMLDocument.xml;
      if (xml == null || xml.length == 0)
        xml = obj.outerHTML;
    }
    catch(e)
    {
      xml = obj.outerHTML;
    }
  }
  return(xml);
}
function i2uiMassEditPivot(id)
{                           
  var factorObj = document.getElementById(id+"PivotMassEditFactor");
  var constantObj = document.getElementById(id+"PivotMassEditConstant");
  var measureObj = document.getElementById(id+"PivotMassEditMeasure");
  var pivot = document.getElementById(id+"Pivot");

  if (pivot == null ||
      factorObj == null ||
      constantObj == null ||
      measureObj == null)
    return;

  var factorValue = factorObj.value;
  var constantValue = constantObj.value;
  if (factorValue != factorValue * 1 || 
      factorValue == null || 
      factorValue == "" ||
      constantValue != constantValue * 1 || 
      constantValue == null || 
      constantValue == "" ||
      (factorValue == 1 && constantValue == 1))
    return;

  var xmlDom2 = new ActiveXObject(domid);
  if (xmlDom2.loadXML(pivot.xmlData2))
  {
    try
    {
      xmlDom2.setProperty("SelectionLanguage", "XPath");
      xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);
      // build xpath for visible rows
      var attributeName = pivot.MeasureToAttribute[measureObj.value];
      var xpath = i2uiBuildPivotXpath(pivot, pivot.ActiveView);
      var datanodes = xmlDom2.selectNodes("//z:row"+xpath);
      var len = datanodes.length;
      for (var i=0; i<len; i++)
      {
        datanodes[i].setAttribute(attributeName,
            (datanodes[i].getAttribute(attributeName) * factorValue) + constantValue*1);
        datanodes[i].setAttribute("i2uidirty",1);
      }
      pivot.dirty = true;
      pivot.xmlData2 = xmlDom2.xml;
      var xmldata = pivot.XMLData;
      i2uiPopulatePivot(pivot, pivot.xmlData2);
      pivot.XMLData = xmldata;
    }
    catch(e){}
  }
}

function i2uiUpdatePivotDetail(details, pivot, totaledMbr, lowestColMbr, targetMeasure, oldValues)
{
  var numImpacted = totaledMbr.ChildMembers.Count;
  var data = pivot.ActiveData;
  for (var i=0; i<numImpacted; i++)
  {
    var mbr = totaledMbr.ChildMembers.Item(i);
    var cells = data.cells(mbr,lowestColMbr);
    var detailNumRows = cells.DetailRowCount;
    var detailNumCols = cells.DetailColumnCount;
    if (detailNumRows > 0 && detailNumCols > 0)
    {
      var detailCell;
      // find proper column
      for (var j=0; j<detailNumCols; j++)
      {
        detailCell = cells.DetailCells(0,j);
        if (detailCell.Field.Name == targetMeasure)
        {
          for (var k=0; k<detailNumRows; k++)
          {
            detailCell = cells.DetailCells(k,j);
            try
            {
              oldValues[detailCell.Bookmark-1] = detailCell.value;
            }
            catch(e){}
          }
          break;
        }
      }
    }
    if (mbr.ChildMembers.Count > 0)
      details = i2uiUpdatePivotDetail(details, pivot, mbr, lowestColMbr, targetMeasure, oldValues);
  }
  return details;
}
function i2uiLocatePivotDetail(pivot, totaledMbr, lowestColMbr, targetMeasure, bookmarks)
{
  var numImpacted = totaledMbr.ChildMembers.Count;
  var data = pivot.ActiveData;
  for (var i=0; i<numImpacted; i++)
  {
    var mbr = totaledMbr.ChildMembers.Item(i);
    var cells = data.cells(mbr,lowestColMbr);
    var detailNumRows = cells.DetailRowCount;
    var detailNumCols = cells.DetailColumnCount;
    if (detailNumRows > 0 && detailNumCols > 0)
    {
      var detailCell;
      // find proper column
      for (var j=0; j<detailNumCols; j++)
      {
        detailCell = cells.DetailCells(0,j);
        if (detailCell.Field.Name == targetMeasure)
        {
          for (var k=0; k<detailNumRows; k++)
          {
            detailCell = cells.DetailCells(k,j);
            try
            {
              bookmarks[detailCell.Bookmark-1] = true;
            }
            catch(e){}
          }
          break;
        }
      }
    }
    if (mbr.ChildMembers.Count > 0)
      i2uiLocatePivotDetail(pivot, mbr, lowestColMbr, targetMeasure, bookmarks);
  }
}
function i2uiEditPivotCell(id)
{
//alert(foo.bar.zed());
  var obj   = document.getElementById(id+"PivotEdit");
  var temp = obj.value;
  if (obj.value == null || 
      obj.value.length == 0 ||
      temp != obj.value)
  {
    if (obj.value != null)
      obj.readOnly = true;
    return;
  }
  var desiredValue = obj.value;
  var pivot = document.getElementById(id+"Pivot");
  var validedit = false;
  var editname;
  var editrownum;

  if (pivot.SelectionType == "PivotDetailRange")
  {
    validedit = true;
    editname = pivot.Selection.TopLeft.Field.BaseName;
    try
    {
      editrownum = pivot.Selection.TopLeft.Bookmark-1;
    }
    catch(e){}
  }
  else
  if (pivot.SelectionType == "PivotAggregates")
  {   
    if (pivot.Selection.Item(0).Cell.RowMember.UniqueName == "Total" ||
        pivot.Selection.Item(0).Cell.RowMember.UniqueName == "Grand Total")
    {
      var oldWay = false;
      var xmlDom2 = new ActiveXObject(domid);
      if (xmlDom2.loadXML(pivot.xmlData2))
      {
        try
        {
          xmlDom2.setProperty("SelectionLanguage", "XPath");
          xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);

          var factor = null;
          if (pivot.oldTotalValue != 0)
            factor = 1 + (obj.value - pivot.oldTotalValue)/pivot.oldTotalValue;
          //alert("edit a total. old="+pivot.oldTotalValue+" new="+obj.value+" factor="+factor);

          var cell = pivot.Selection.Item(0);
          var details = "UPDATING "+cell.Name+"="+cell.Value;

          var targetMeasure = cell.Name;
          cell = cell.Cell;

          if (oldWay)
          {
              var editrownum = cell.Recordset.Bookmark-1;

              var coreXpath = "//z:row";

              details += " where on ROW ";
              var mbr, totaledMbr = null;
              mbr = cell.RowMember;
              try
              {
                while (mbr != null)
                {
                  if (mbr.Value=="Total")
                    totaledMbr = mbr.ParentMember;
                  else
                  {
                    details += mbr.Field.Name+"="+mbr.Value+" ";
                    coreXpath += '[@'+pivot.MeasureToAttribute[mbr.Field.Name]+'="'+mbr.Value+'"]';
                  }
                  mbr = mbr.ParentMember;
                }
              }
              catch(e){}
              details += " and on COL ";
              mbr = cell.ColumnMember;
              var lowestColMbr = mbr;
              try
              {
                while (mbr != null)
                {
                  if (mbr.Value=="Total")
                    totaledMbr = mbr.ParentMember;
                  else
                  {
                    details += mbr.Field.Name+"="+mbr.Value+" ";             
                    coreXpath += '[@'+pivot.MeasureToAttribute[mbr.Field.Name]+'="'+mbr.Value+'"]';
                  }
                  mbr = mbr.ParentMember;
                }
              }
              catch(e){}

              var miniXpath;
              details += " IMPACTING ";
              var numImpacted = totaledMbr.ChildMembers.Count;
              var data = pivot.object.ActiveData;
              for (var i=0; i<numImpacted; i++)
              {
                mbr = totaledMbr.ChildMembers.Item(i);
                var value, newValue;
                var cells;
                cells = data.cells(mbr,lowestColMbr);
                try
                {
                  var numDataMeasures = cells.aggregates.count;
                  for (var j=0; j<numDataMeasures; j++)
                  {
                    if (cells.aggregates.item(j).Name == targetMeasure)
                    {
                      value = cells.aggregates.item(j).value;

                      if (factor == null) 
                        newValue = desiredValue / numImpacted;
                      else
                        newValue = value * factor;
                      break;
                    }
                  }
                }
                catch(e)
                {
                  value = 0;           
                  var detailNumRows = cells.DetailRowCount;
                  var detailNumCols = cells.DetailColumnCount;
                  for (var m=0; m<detailNumRows; m++)
                    for (var n=0; n<detailNumCols; n++)
                      value += cells.DetailCells(m,n).value;
                  if (factor == null) 
                    newValue = desiredValue / numImpacted;
                  else
                    newValue = value * factor;
                }
                var attrName = pivot.MeasureToAttribute[targetMeasure];
                var miniXpath = '[@'+pivot.MeasureToAttribute[mbr.Field.Name]+'="'+mbr.Value+'"][@'+attrName+'="'+value+'"]';

                var datanodes = xmlDom2.selectNodes(coreXpath+miniXpath);
                var len = datanodes.length;
                //alert(coreXpath+miniXpath+" relates to "+len+" nodes. set attribute "+attrName+" to "+newValue);
                for (var k=0; k<len; k++)
                {
                  datanodes[k].setAttribute(attrName, newValue);
                  //datanodes[k].setAttribute("forecast", newValue);
                  datanodes[k].setAttribute("i2uidirty",1);
                }

                details += mbr.Field.Name+"="+mbr.Value+" CURRENTLY="+value+" BECAME "+newValue+" ";
              }
              //alert(details);
          }
          else
          {         
            details += " where on ROW ";
            var mbr, totaledMbr = null;
            mbr = cell.RowMember;
            try
            {
              while (mbr != null)
              {
                if (mbr.Value=="Total" || 
                    mbr.Value=="Grand Total")
                  totaledMbr = mbr.ParentMember;
                else
                  details += mbr.Field.Name+"="+mbr.Value+" ";
                mbr = mbr.ParentMember;
              }
            }
            catch(e){}
            details += "\nand on COL ";
            mbr = cell.ColumnMember;
            var lowestColMbr = mbr;

            try
            {
              while (mbr != null)
              {
                if (mbr.Value=="Total" ||
                    mbr.Value=="Grand Total")
                  totaledMbr = mbr.ParentMember;
                else
                  details += mbr.Field.Name+"="+mbr.Value+" ";             
                mbr = mbr.ParentMember;
              }
            }
            catch(e){}
            details += "\nIMPACTING ";

            //alert(details);

            var datanodes = xmlDom2.selectNodes("//z:row");



            pivot.lockedValue = 0;
            var oldValues = new Array();
            details = i2uiUpdatePivotDetail(details, pivot, totaledMbr, lowestColMbr, targetMeasure, oldValues)
            //alert(details);
            var attrName = pivot.MeasureToAttribute[targetMeasure];
            var numChanged = 0;
            for (x in oldValues)
            {
              if (pivot.lockedColumns[targetMeasure] != null && 
                  pivot.lockedColumns[targetMeasure][x])  
                pivot.lockedValue += oldValues[x];
              else
                numChanged++;
            }
            //alert("#changed="+numChanged+" lockedValue="+pivot.lockedValue);
            //desiredValue -= pivot.lockedValue;
            var newValue = desiredValue / numChanged;
            if (pivot.oldTotalValue-pivot.lockedValue != 0)
              factor = 1 + (desiredValue - pivot.oldTotalValue)/(pivot.oldTotalValue-pivot.lockedValue);
            //alert("factor="+factor);
            if (!pivot.proportionalEditColumns[targetMeasure])
            {
              factor = null;
              newValue = desiredValue;
            }
            //alert("new="+newValue);
            for (x in oldValues)
            {
              if (pivot.lockedColumns[targetMeasure] &&
                  pivot.lockedColumns[targetMeasure][x]) 
                continue;
              if (factor != null)
                newValue = oldValues[x] * factor;
              datanodes[x].setAttribute(attrName, newValue);
              //alert("for "+pivot.SelectionType+" set row #"+x+" attribute "+attrName+" to "+(oldValues[x]*factor));
              datanodes[x].setAttribute("i2uidirty",1);
            }
          }

          pivot.dirty = true;
          pivot.xmlData2 = xmlDom2.xml;
          var xmldata = pivot.XMLData;
          i2uiPopulatePivot(pivot, pivot.xmlData2);
          pivot.XMLData = xmldata;
        }
        catch(e){}
      }
    }
    else
    {
      validedit = true;
      editname = pivot.Selection.Item(0).Total.name;
      try
      {
        editrownum = pivot.Selection.Item(0).Cell.Recordset.Bookmark-1;
      }
      catch(e){}
    }
  }
  else
  if (pivot.SelectionType == "PivotFields")
  {
    var itemName = pivot.Selection.Item(0).BaseName;
    var test = pivot.activeView.dataaxis.fieldsets(itemName).Name;
    var itemType = pivot.fieldTypes[pivot.MeasureToAttribute[itemName]];

    var xmlDom2 = new ActiveXObject(domid);
    if (xmlDom2.loadXML(pivot.xmlData2))
    {
      try
      {
        xmlDom2.setProperty("SelectionLanguage", "XPath");
        xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);
        // build xpath for visible rows
        var attributeName = pivot.MeasureToAttribute[itemName];
        var xpath = i2uiBuildPivotXpath(pivot, pivot.ActiveView);
        var datanodes = xmlDom2.selectNodes("//z:row"+xpath);
        var len = datanodes.length;
        //alert(xpath+" relates to "+len+" nodes. set attribute "+attributeName+" to "+obj.value);
        for (var i=0; i<len; i++)
        {
          datanodes[i].setAttribute(attributeName, obj.value);
          datanodes[i].setAttribute("i2uidirty",1);
        }
        pivot.dirty = true;
        pivot.xmlData2 = xmlDom2.xml;
        var xmldata = pivot.XMLData;
        i2uiPopulatePivot(pivot, pivot.xmlData2);
        pivot.XMLData = xmldata;
      }
      catch(e){}
    }
  }

  if (validedit)
  {
    // NOTE: value attribute, element.Selection.TopLeft.Value, is readonly
    var xmlDom2 = new ActiveXObject(domid);
    if (xmlDom2.loadXML(pivot.xmlData2))
    {
      try
      {
        xmlDom2.setProperty("SelectionLanguage", "XPath");
        xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);

        var attrnodes = xmlDom2.selectNodes("//s:AttributeType[@rs:name='"+editname+"']");
        // make sure value is numeric for non-string fields
        var datatype=attrnodes[0].selectSingleNode("s:datatype").getAttribute("dt:type");
        if (datatype == "float")
        {
          temp = obj.value * 1;
          if (obj.value == null || 
              obj.value.length == 0 ||
              temp != obj.value)
            return;
        }
        var datanodes = xmlDom2.selectNodes("//z:row");
        datanodes[editrownum].setAttribute(attrnodes[0].getAttribute("name"), obj.value);
        
//        alert("for "+pivot.SelectionType+" set row #"+editrownum+" attribute "+attrnodes[0].getAttribute("name")+" to "+obj.value);
        datanodes[editrownum].setAttribute("i2uidirty",1);
        pivot.dirty = true;

        // is this a faster edit??
        //var editrow = xmlDom2.selectSingleNode("//z:row[editrownum]");
        //editrow.setAttribute(attrnodes[0].getAttribute("name"), obj.value);
        
        pivot.xmlData2 = xmlDom2.xml;

        // get existing layout style
        var xmldata = pivot.XMLData;

        // populate with new data
        i2uiPopulatePivot(pivot, pivot.xmlData2);

        // reapply layout
        pivot.XMLData = xmldata;
      }
      catch(e){}
    }
  }
  try 
  {
    i2uiDelayedPivotChangeCallback(id, pivot);
  }
  catch(e){}
  pivot.focus();
  obj.readOnly = true;
}
function i2uiIsPivotDirty(id)
{
  var pivot = document.getElementById(id+"Pivot");
  return pivot.dirty;
}
function i2uiInternallyPersistPivotEdits(pivot, ownerNode)
{
  if (pivot.dirty && pivot.keyfield != null)
  {                     
    var xmlDom2 = new ActiveXObject(domid);
    if (xmlDom2.loadXML(pivot.xmlData2))
    {
      try
      {
        xmlDom2.setProperty("SelectionLanguage", "XPath");
        xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);
        var rownodes = xmlDom2.selectNodes("//z:row[@i2uidirty=1]");
        var len = rownodes.length;
        var key;
        var datarow;
        var numAttrs;
        for (var i=0; i<len; i++)
        {
          key = rownodes[i].getAttribute(pivot.keyfield);
          numAttrs = rownodes[i].attributes.length;
          var xpath = "[@"+pivot.keyfield+"=\""+key+"\"]";
          datarow = ownerNode.selectSingleNode("z:row"+xpath);
          if (datarow != null)
          {
            for (var j=1; j<numAttrs; j++)
              datarow.setAttribute(rownodes[i].attributes[j].name, 
                                   rownodes[i].getAttribute(rownodes[i].attributes[j].name));
          }
        }
      }
      catch(e)
      {
        alert("some trouble while internally persisting changed rows."+e.message);
      }
    }
  }
}
function i2uiFilterMeasurePivot(id, itemName, itemValues)
{
  try
  {
    var pivot = document.getElementById(id+"Pivot");
    var view = pivot.ActiveView;
    var temp = itemValues.join(",");
    view.FieldSets(itemName).Fields(0).IncludedMembers = temp.split(",");
  }
  catch(e)
  {
  }
}
function i2uiExtractPivot(id)
{
  var result = "<"+id+">";
  var pivot = document.getElementById(id+"Pivot");
  i2uiApplyPivotEdits(id, pivot);
  var xmlDom2 = new ActiveXObject(domid);
  xmlDom2.setProperty("SelectionLanguage", "XPath");
  xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);
  if (xmlDom2.loadXML(pivot.xmlData2))
  {
    pivot.xmlData2 = xmlDom2.xml;
    pivot.xmlDom2 = xmlDom2;
    if (pivot.dirty)
    {
      var datanodes = xmlDom2.selectNodes("//z:row[@i2uidirty=1]");
      var len = datanodes.length;
      for (var i=0; i<len; i++) 
      {
        datanodes[i].removeAttribute("i2uidirty");
        result += datanodes[i].xml;
      }
      // remove OWC schema from xml
      result = result.replace(/z:row/g, "row");
      result = result.replace(/xmlns:z=\"#RowsetSchema\"/g, "");
    }
  }
  result += "</"+id+">";
  pivot.dirty = false;
  return result;
}
function i2uiDetermineSeriesType(seriesname, view, pivot)
{
  var at = Math.max(-1, seriesname.lastIndexOf(":"));
  var lastSeries = seriesname.substring(at+1);
  return pivot.chartTypeInfo[lastSeries];
}
function i2uiDetermineSeriesClass(seriesname, view, pivot)
{
  var classinfo = "";
  if (pivot.multichart)
  {
    var primarykey1 = "*";
    var len = view.ColumnAxis.FieldSets.Count;
    if (len > 1) 
      primarykey1 = view.ColumnAxis.FieldSets(1).Name;

    var at = Math.max(0, seriesname.indexOf(":"));
    // for multi-attribute coloring
    var subkey1 = seriesname.substring(at+1);

    if (len == 1) 
    {
      primarykey1 = subkey1;
      subkey1 = "*";
    }
    else
    if (len == 0)
    {
      primarykey1 = seriesname;
      subkey1 = "*";
    }

    // for single attribute coloring
    if (pivot.coloring == 1 || pivot.chartColorInfo[0] > 0)
    {
      at = Math.max(0, subkey1.indexOf(":"));
      if (at == -1)
        at = seriesname.length;
      subkey1 = subkey1.substring(0,at);
    }

    var at = Math.max(-1, seriesname.lastIndexOf(":"));
    var subkey2 = seriesname.substring(at+1);
    var substyle = i2uiDetermineSeriesStyle(subkey2+"_*",pivot);
    if (pivot.chartTypeInfo[subkey2] == "line" ||
        pivot.chartTypeInfo[subkey2] == "inferstep")
      pivot.chartSubstyle[subkey2] = substyle;
    classinfo = 'classindex="'+i2uiDetermineSeriesColor(primarykey1+"_"+subkey1,pivot)+'" substyle="dash'+substyle+'" ';
  }
  else
  {
    if (pivot.chartColorInfo[0] > 0)
    {
      var at = Math.max(0, seriesname.indexOf(":"));
      if (at == -1)
        at = seriesname.length;
      var primarykey = seriesname.substring(0,at);

      var secondName = "*";
      var tempNames = seriesname.split(":");
      if (tempNames.length > 1)
        secondName = tempNames[1];
      
      var at = Math.max(-1, seriesname.lastIndexOf(":"));
      var subkey2 = seriesname.substring(at+1);
      var fieldvalue;
      try
      {
        fieldvalue = view.ColumnAxis.FieldSets(0).Name;
      }
      catch(e)
      {
        fieldvalue = seriesname;
      }
      
      if (pivot.coloring == 3)
        primarykey += "_"+secondName;

      var substyle = i2uiDetermineSeriesStyle(subkey2+"_*",pivot);
      if (pivot.chartTypeInfo[subkey2] == "line" ||
          pivot.chartTypeInfo[subkey2] == "inferstep")
        pivot.chartSubstyle[subkey2] = substyle;
      classinfo = 'classindex="'+i2uiDetermineSeriesColor(fieldvalue+"_"+primarykey, pivot)+'" substyle="dash'+substyle+'" ';
    }
  }
  return classinfo;
}
function i2uiDetermineSeriesColor(id, pivot)
{
  if (pivot.seriesColor[id] == null)
  {
    // see if xml provided color info
    pivot.seriesColor[id] = pivot.chartColorInfo[id];
    if (pivot.seriesColor[id] == null)
    {
      // check for default value
      var at = id.indexOf("_");
      if (at != -1)
      {
        var id2 = id.substring(0,at+1)+"*";
        pivot.seriesColor[id] = pivot.chartColorInfo[id2];
      }
    }
    if (pivot.seriesColor[id] == null)
      pivot.seriesColor[id] = seriesColorCount++;
  }
  return pivot.seriesColor[id];
}
function i2uiDetermineSeriesStyle(id, pivot)
{
  if (pivot.seriesStyle[id] == null)
  {
    // see if xml provided style exists
    pivot.seriesStyle[id] = pivot.chartStyleInfo[id];
    if (pivot.seriesStyle[id] == null)
      pivot.seriesStyle[id] = "";
      // can't increment styles w/o regard to max style
      //pivot.seriesStyle[id] = seriesStyleCount++;
  }
  return pivot.seriesStyle[id];
}
function i2uiDateValue(dateString)
{
  var dateFields = dateString.split("/");
  var mm = dateFields[0]*1-1;
  var dd = dateFields[1];
  var yyyy = dateFields[2];
  var hh = 0;
  var min = 0;
  var timeFields = yyyy.split(" ");
  if (timeFields.length > 1)
  {
     yyyy = timeFields[0];
     timeFields = timeFields[1].split(":");
     hh = timeFields[0]*1-1;
     min = timeFields[1];
  }
  return new Date(yyyy,mm,dd,hh,min).valueOf();
}

var i2uiChartXAxisName;
var i2uiChartXAxis;
var i2uiChartSeriesName;
var i2uiChartSeriesValues;
var i2uiChartExtraSeries = new Array();

var i2uiChartSeries;
var i2uiChartRowMembers;
var i2uiChartColumnMembers;
var i2uiChartLastSeriesType;
// recurse hierarchy of columns
// at bottom, process all related rows
function i2uiChartPivotColumns(data, columnMember, style)
{
  if (columnMember.caption != "Column")
    i2uiChartSeriesName.push(columnMember.caption);
  var childCount = columnMember.childmembers.count;
  if (childCount == 0)
  {
    // determine number of measures in data axis
    // make a series per data axis measure appending name to series if multiple
    var firstCell = data.cells(i2uiChartRowMembers[0], columnMember);
    // warning : only valid if totaling for data.  otherwise must look at orientation
    // to determine whether to use 
    var seriesType;
    var len = 1;
    //LPM works only if total=yes for measures in data area
    try{len = firstCell.aggregates.count;}catch(e){}
    if (debuger)alert(len);
    for (i=0; i<len; i++)
    {
      i2uiChartSeriesValues = new Array();
      // for fastest speed, data axis measures should have totaling turned on
      i2uiChartPivotRows2(data, columnMember, i);

      if (len > 0) 
        i2uiChartSeriesName.push(firstCell.aggregates.item(i).name);
      
      var seriesName = i2uiChartSeriesName.join(":");
      if (style == null || style =="null")
        seriesType = i2uiDetermineSeriesType(seriesName, data.view, activePivot);
      else
        seriesType = style;
      if (seriesType == null)
        seriesType="line";      
      if (seriesType == "stackbar" &&
          i2uiChartLastSeriesType != "bar" &&
          i2uiChartLastSeriesType != "stackbar")
        seriesType = "bar";
      i2uiChartLastSeriesType = seriesType;
      var classinfo = i2uiDetermineSeriesClass(seriesName, data.view, activePivot);
      i2uiChartSeries.push("<series "+classinfo+" separator=\"|\" type=\""+seriesType+"\" name=\""+seriesName+"\">"+i2uiChartSeriesValues.join("|")+"</series>");
      if (len > 0) 
          i2uiChartSeriesName.pop();
    }
  }
  else
  {
    for (var i=0; i<childCount; i++)
      i2uiChartPivotColumns(data, columnMember.childmembers.item(i), style);
  }
  i2uiChartSeriesName.pop();
}
// recurse hierarchy of rows
// at end, produce an entry into the series array
function i2uiChartPivotRows(data, columnMember, rowMember)
{
  if (rowMember.caption != "Row")
  {
    i2uiChartXAxisName.push(rowMember.caption);
    if (rowMember.uniqueName.indexOf("["+i2uiPivotAsofName+"]") != -1)
    {
      if (rowMember.caption == i2uiPivotAsofValue)
        i2uiChartExtraSeries.push(1);
      else
        i2uiChartExtraSeries.push(0);
    }
  }
  var childCount = rowMember.childmembers.count;
  if (childCount == 0)
  {
    // get value for cell(s)
    var cells = data.cells(rowMember, columnMember);

    try
    {
      value = cells.aggregates.item(0).value;
    }
    catch(e)
    {
      value = 0;
    
      var detailNumRows = cells.DetailRowCount;
      var detailNumCols = cells.DetailColumnCount;
      for (var m=0; m<detailNumRows; m++)
        for (var n=0; n<detailNumCols; n++)
          value += cells.DetailCells(m,n).value;
    }

    i2uiChartSeriesValues.push(value);                
    i2uiChartXAxis.push(i2uiChartXAxisName.join(":"));
  }
  else
    for (var i=0; i<childCount; i++)
      i2uiChartPivotRows(data, columnMember, rowMember.childmembers.item(i));
  i2uiChartXAxisName.pop();
}
var debuger = false;
function i2uiChartPivotRows2(data, columnMember, dataIndex)
{
  var value;
  var len = i2uiChartRowMembers.length;
  for (var i=0; i<len; i++)
  {
    // get value for cell(s)
    var cells = data.cells(i2uiChartRowMembers[i], columnMember);
    if (debuger)
    {
      var view = data.view;
      var fld = view.fieldsets(0).fields(0);
      fld.memberproperties = new Array("detail1","detail2");
    }
    
    try
    {
      value = cells.aggregates.item(dataIndex).value;
    }
    catch(e)
    {
      // if no aggregate, then summarize over all detail cells
      value = 0;
      var detailNumRows = cells.DetailRowCount;
      var detailNumCols = cells.DetailColumnCount;
      for (var m=0; m<detailNumRows; m++)
        for (var n=0; n<detailNumCols; n++)
          value += cells.DetailCells(m,n).value;
    }
    i2uiChartSeriesValues.push(value);                
  }
}
function i2uiChartPivotRowMembers(data, rowMember)
{
  if (rowMember.caption != "Row")
  {
    i2uiChartXAxisName.push(rowMember.caption);
    if (rowMember.uniqueName.indexOf("["+i2uiPivotAsofName+"]") != -1)
    {
      if (rowMember.caption == i2uiPivotAsofValue)
        i2uiChartExtraSeries.push(1);
      else
        i2uiChartExtraSeries.push(0);
    }
  }
  var childCount = rowMember.childmembers.count;
  if (childCount == 0)
  {
    i2uiChartXAxis.push(i2uiChartXAxisName.join(":"));
    i2uiChartRowMembers.push(rowMember);
  }
  else
    for (var i=0; i<childCount; i++)
      i2uiChartPivotRowMembers(data, rowMember.childmembers.item(i));
  i2uiChartXAxisName.pop();
}
function i2uiChartPivotColumnMembers(data, columnMember)
{
  var childCount = columnMember.childmembers.count;
  if (childCount == 0)
  {
    i2uiChartColumnMembers.push(columnMember);
  }
  else
    for (var i=0; i<childCount; i++)
      i2uiChartPivotColumnMembers(data, columnMember.childmembers.item(i));
}
function i2uiChartPivot(id, h, w, multichart, coloring, style)
{
  if (multichart == null) 
    multichart = false;
  if (h == null)
    h = "100%";
  if (w == null)
    w = "100%";
  var result = "<chart height=\""+h+"\" width=\""+w+"\"";
  if (multichart) 
    result += " multi=\"yes\" ";
  var xaxisresult = "";
  var seriesresult = new Array();
  var pivot = document.getElementById(id+"Pivot");
  pivot.chartSubstyle = new Array();
  pivot.multichart = multichart;
  if (coloring != null) 
    pivot.coloring = coloring;
  var view = pivot.ActiveView;
  var rowlen = view.RowAxis.FieldSets.Count;
  var collen = view.ColumnAxis.FieldSets.Count;
  var datalen = view.DataAxis.FieldSets.Count;
  var filterlen = view.FilterAxis.FieldSets.Count;
  var data = pivot.ActiveData;
  // must recompute effect of any filtering changes
  pivot.seriesColor = new Array();
  pivot.seriesColorCount = 0;
  pivot.seriesStyle = new Array();
  pivot.seriesStyleCount = 0;
  seriesColorCount = 0;
  seriesStyleCount = 0;
  i2uiChartXAxisName = new Array();
  i2uiChartExtraSeries = new Array();
  i2uiChartXAxis = new Array();
  i2uiChartRowMembers = new Array();
  i2uiChartColumnMembers = new Array();
  i2uiChartLastSeriesType = "";
  i2uiChartPivotRowMembers(data, data.RowAxis.member);
  i2uiChartSeriesName = new Array();
  i2uiChartSeries = new Array();
  var columnMember = data.ColumnAxis.member;
  i2uiChartPivotColumns(data, columnMember, style);
  try
  {
    if (multichart)
      result += " y_axis_label=\""+columnMember.childmembers.item(0).field.name+"\" ";
  }
  catch(e)
  {
    result += ' y_axis_label=" " ';
  }
  var substyle = "";            
  for (x in pivot.chartSubstyle)
  {
    substyle += '<substyle name="'+x+'" style="dash'+pivot.chartSubstyle[x]+'"/>';
  }
  var extra = "";
  if (i2uiChartExtraSeries.length > 0)
  {
    extra = "<series name=\""+i2uiPivotAsofCaption+"\" type=\"asof\" separator=\"|\"";
    if (i2uiPivotAsofColor != null)
      extra += " classindex=\""+i2uiPivotAsofColor+"\"";
    extra += ">"+i2uiChartExtraSeries.join("|")+"</series>"
  }
  var full = result+"><xaxis separator=\"|\">"+i2uiChartXAxis.join("|")+"</xaxis>"+i2uiChartSeries.join("")+substyle+extra+pivot.chartThreshold+"</chart>";
  return full;
}

function i2uiBuildPivotXpath(pivot, view)
{                        
  var rowlen = view.RowAxis.FieldSets.Count;
  var collen = view.ColumnAxis.FieldSets.Count;
  var datalen = view.DataAxis.FieldSets.Count;
  var filterlen = view.FilterAxis.FieldSets.Count;

  var rowFieldNames = new Array();
  for (var i=0; i<rowlen; i++)
  {
    var name = xmlDom.selectNodes("//item[text()='"+view.RowAxis.FieldSets(i).Name+"']");
    if (name.length > 0)
      rowFieldNames[i] = name(0).getAttribute("name");
    else
      rowFieldNames[i] = view.RowAxis.FieldSets(i).Name;
  }

  var colFieldNames = new Array();
  for (var i=0; i<collen; i++)
  {
    var name = xmlDom.selectNodes("//item[text()='"+view.ColumnAxis.FieldSets(i).Name+"']");
    if (name.length > 0)
      colFieldNames[i] = name(0).getAttribute("name");
    else
      colFieldNames[i] = view.ColumnAxis.FieldSets(i).Name;
  }

  var filterFieldNames = new Array();
  for (var i=0; i<filterlen; i++)
  {
    var name = xmlDom.selectNodes("//item[text()='"+view.FilterAxis.FieldSets(i).Name+"']");
    if (name.length > 0)
      filterFieldNames[i] = name(0).getAttribute("name");
    else
      filterFieldNames[i] = view.FilterAxis.FieldSets(i).Name;
  }

  var dataFieldNames = new Array();
  for (var i=0; i<datalen; i++)
  {
    try
    {
      var name = xmlDom.selectNodes("//item[text()='"+view.DataAxis.FieldSets(i).Name+"']");
      if (name.length > 0)
        dataFieldNames[i] = name(0).getAttribute("name");
      else
      {
        dataFieldNames[i] = view.DataAxis.FieldSets(i).Name;
      }
    }
    catch(e){}
  }
  var xpath = "";
  for (var j=0; j<collen; j++)
  {
    if (view.ColumnAxis.FieldSets(j).Members.count == 0)
      continue;
    var excludedVariant = view.ColumnAxis.FieldSets(j).Members(0).Field.ExcludedMembers;
    if (excludedVariant != null)
    {
      var excludedArray = (new VBArray(excludedVariant)).toArray();
      var excludedArrayCount = excludedArray.length;
      for (var x=0; x<excludedArrayCount; x++)
      {
        xpath += "[not(@"+colFieldNames[j]+"='"+excludedArray[x].name+"')]";
      }
    }
  }
  //alert("after exclude cols. xpath="+xpath);

  for (var j=0; j<filterlen; j++)
  {
    if (view.FilterAxis.FieldSets(j).Members.count == 0)
      continue;
    var excludedVariant = view.FilterAxis.FieldSets(j).Members(0).Field.ExcludedMembers;
    if (excludedVariant != null)
    {
      var excludedArray = (new VBArray(excludedVariant)).toArray();
      var excludedArrayCount = excludedArray.length;
      for (var x=0; x<excludedArrayCount; x++)
      {
        xpath += "[not(@"+filterFieldNames[j]+"='"+excludedArray[x].name+"')]";
      }
    }
  }
  //alert("after exclude filters. xpath="+xpath);

  for (var j=0; j<collen; j++)
  {
    if (view.ColumnAxis.FieldSets(j).Members.count == 0)
      continue;
    var includedVariant = view.ColumnAxis.FieldSets(j).Members(0).Field.IncludedMembers;
    if (includedVariant != null)
    {
      var includedArray = (new VBArray(includedVariant)).toArray();
      var includedArrayCount = includedArray.length;
      var bFirst = true;
      for (var x=0; x<includedArrayCount; x++)
      {
        if (bFirst)
        {
          xpath += "[";
          bFirst = false;
        }
        if (x > 0) 
          xpath += " or ";
        xpath += "@"+colFieldNames[j]+"='"+includedArray[x].name+"'";
      }
      if (!bFirst) 
        xpath += "]";
    }
  }
  //alert("after include cols. xpath="+xpath);

  for (var j=0; j<filterlen; j++)
  {
    if (view.FilterAxis.FieldSets(j).Members.count == 0)
      continue;
    var includedVariant = view.FilterAxis.FieldSets(j).Members(0).Field.IncludedMembers;
    if (includedVariant != null)
    {
      var includedArray = (new VBArray(includedVariant)).toArray();
      var includedArrayCount = includedArray.length;
      var bFirst = true;
      for (var x=0; x<includedArrayCount; x++)
      {
        if (bFirst)
        {
          xpath += "[";
          bFirst = false;
        }
        if (x > 0) 
          xpath += " or ";
        xpath += "@"+filterFieldNames[j]+"='"+includedArray[x].name+"'";
      }
      if (!bFirst) 
        xpath += "]";
    }
  }
  //alert("after include filters. xpath="+xpath);

  for (var j=0; j<rowlen; j++)
  {
    if (view.RowAxis.FieldSets(j).Members.count == 0)
      continue;
    var excludedVariant = view.RowAxis.FieldSets(j).Members(0).Field.ExcludedMembers;
    if (excludedVariant != null)
    {
      var excludedArray = (new VBArray(excludedVariant)).toArray();
      var excludedArrayCount = excludedArray.length;
      for (var x=0; x<excludedArrayCount; x++)
        xpath += "[not(@"+rowFieldNames[j]+"='"+excludedArray[x].name+"')]";
    }
    var includedVariant = view.RowAxis.FieldSets(j).Members(0).Field.IncludedMembers;
    if (includedVariant != null)
    {
      var includedArray = (new VBArray(includedVariant)).toArray();
      var includedArrayCount = includedArray.length;
      var bFirst = true;
      for (var x=0; x<includedArrayCount; x++)
      {
        if (bFirst)
        {
          xpath += "[";
          bFirst = false;
        }
        if (x > 0) 
          xpath += " or ";
        xpath += "@"+rowFieldNames[j]+"='"+includedArray[x].name+"'";
      }
      if (!bFirst) 
        xpath += "]";
    }
  }
  //alert("after rows. xpath="+xpath);

  for (var j=0; j<datalen; j++)
  {             
    if (view.DataAxis.FieldSets(j).Members.count == 0)
      continue;
    var excludedVariant = view.DataAxis.FieldSets(j).Members(0).Field.ExcludedMembers;
    if (excludedVariant != null)
    {
      var excludedArray = (new VBArray(excludedVariant)).toArray();
      var excludedArrayCount = excludedArray.length;
      for (var x=0; x<excludedArrayCount; x++)
        xpath += "[not(@"+dataFieldNames[j]+"='"+excludedArray[x].name+"')]";
    }
    var includedVariant = view.DataAxis.FieldSets(j).Members(0).Field.IncludedMembers;
    if (includedVariant != null)
    {
      var includedArray = (new VBArray(includedVariant)).toArray();
      var includedArrayCount = includedArray.length;
      var bFirst = true;
      for (var x=0; x<includedArrayCount; x++)
      {
        if (bFirst)
        {
          xpath += "[";
          bFirst = false;
        }
        if (x > 0) 
          xpath += " or ";
        xpath += "@"+dataFieldNames[j]+"='"+includedArray[x].name+"'";
      }
      if (!bFirst) 
        xpath += "]";
    }
  }

  //alert("pre-retains xpath "+xpath);

  // here to consider filtering within groups
  pivot.retainincludes = new Array();
  pivot.retainexcludes = new Array();
  if (pivot.retainFilters)
    i2uiBuildPivotRetains(pivot, view);
  for (name in pivot.retainincludes) 
  {
    // change name to xml attribute name
    var name2 = pivot.MeasureToAttribute[name];
    var len = pivot.retainincludes[name].length;
    for (var i=0; i<len; i++)
      xpath += "[@"+name2+"='"+pivot.retainincludes[name][i]+"']";
  }
  for (name in pivot.retainexcludes) 
  {
    // change name to xml attribute name
    var name2 = pivot.MeasureToAttribute[name];
    var len = pivot.retainexcludes[name].length;
    for (var i=0; i<len; i++)
      xpath += "[not(@"+name2+"='"+pivot.retainexcludes[name][i]+"')]";
  }
  pivot.retainincludes = new Array();
  pivot.retainexcludes = new Array();
  //alert("final xpath "+xpath);
  return xpath;
}

function i2uiFindPivotLeafs(pivot, name, caption, obj)
{
  try
  {
    var xpath = "/pivot/group[@name=\""+name+"\"]/members[@caption=\""+caption+"\"]";
    var group = xmlDom.selectSingleNode(xpath);
    if (group != null)
    {
      var source = group.parentNode.getAttribute("source");
      var members = group.text.split(",");
      var len = members.length;
      for (var i=0; i<len; i++)
      {
        // check if name itself is a group
        xpath = "/pivot/group[@name=\""+source+"\"]/members[@caption=\""+members[i]+"\"]";
        if (xmlDom.selectSingleNode(xpath) == null)
          obj.push(members[i]);
        else
          i2uiFindPivotLeafs(pivot, 
                             source,
                             caption,
                             members[i],
                             obj);
      }
    }
  } catch(e){}
}
function i2uiRecursePivotRetains(obj, fieldName, pivot, flag)
{
  var cnt = 0;
  var obj2;
  var excludedVariant = obj.Field.ExcludedMembers;
  if (excludedVariant != null)
  {
    var filteredArray = (new VBArray(excludedVariant)).toArray();
    if (filteredArray.length > 0)
    {
      var arrayCount = filteredArray.length;
      if (pivot.retainexcludes[fieldName] == null)
        pivot.retainexcludes[fieldName]= new Array();
      else
      {
        if (obj.name == obj.caption) // skip if leaf
          arrayCount = 0;
      }
      obj2 = pivot.retainexcludes[fieldName];
      for (var x=0; x<arrayCount; x++)
      {
        if (filteredArray[x].name != filteredArray[x].caption)
        {
          if (filteredArray[x].caption == obj.caption)
          {
            flag = true;
            i2uiFindPivotLeafs(pivot, obj, obj2);
          }
          cnt++;
        }
        else
        {
          obj2.push(filteredArray[x].name);
        }
      }
    }
  }
  var includedVariant = obj.Field.IncludedMembers;
  if (includedVariant != null)
  {
    var filteredArray = (new VBArray(includedVariant)).toArray();
    if (filteredArray.length > 0)
    {
      var arrayCount = filteredArray.length;
      if (pivot.retainincludes[fieldName] == null)
        pivot.retainincludes[fieldName]= new Array();
      else
      {
        if (obj.name == obj.caption) // skip if leaf
          arrayCount = 0;
      }
      obj2 = pivot.retainincludes[fieldName];
      for (var x=0; x<arrayCount; x++)
      {
        if (filteredArray[x].name != filteredArray[x].caption)
        {
          if (filteredArray[x].caption == obj.caption)
          {
            flag = true;
            i2uiFindPivotLeafs(pivot, obj, obj2);
          }
          else
            obj2.push(filteredArray[x].name);
          cnt++;
        }
        else
        {
          obj2.push(filteredArray[x].name);
        }
      }
    }
  }
  if (flag &&
      obj.name == obj.caption)
  {
    pivot.retainexcludes[fieldName].push(obj.name);
  }
  try
  {
    var numChildren = obj.childmembers.count;
    for (var x=0; x<numChildren; x++)
    {
      // here to catch leafs
      if (flag && 
          obj.childmembers(x).name == obj.childmembers(x).caption)
      {
        pivot.retainexcludes[fieldName].push(obj.childmembers(x).name);
      }
      else
      {
        cnt += i2uiRecursePivotRetains(obj.childmembers(x),fieldName, pivot, flag);
      }
    }
  } catch(e){}
  return cnt;
}

function i2uiProcessPivotFieldFilters(pivot,view,fieldSet)
{
  var fieldName = fieldSet.Name;
  // for each field in fieldset
  var fieldCount = fieldSet.Fields.count;
  for (var k=0; k<fieldCount; k++)
  {
    var excludedVariant = fieldSet.Fields(k).ExcludedMembers;
    if (excludedVariant != null)
    {
      var filteredArray = (new VBArray(excludedVariant)).toArray();
      if (filteredArray.length > 0)
      {        
        if (pivot.retainexcludes[fieldName] == null)
          pivot.retainexcludes[fieldName] = new Array();
        obj = pivot.retainexcludes[fieldName];
        var arrayCount = filteredArray.length;
        for (var x=0; x<arrayCount; x++)
        {
          if (filteredArray[x].name == filteredArray[x].caption)
            obj.push(filteredArray[x].name);
          else
          {
            i2uiFindPivotLeafs(pivot,
                               filteredArray[x].field.caption,
                               filteredArray[x].caption,
                               obj);
          }
        }
      }
    }
    var includedVariant = fieldSet.Fields(k).IncludedMembers;
    if (includedVariant != null)
    {
      var filteredArray = (new VBArray(includedVariant)).toArray();
      if (filteredArray.length > 0)
      {        
        if (pivot.retainincludes[fieldName] == null)
          pivot.retainincludes[fieldName] = new Array();
        obj = pivot.retainincludes[fieldName];
        var arrayCount = filteredArray.length;
        for (var x=0; x<arrayCount; x++)
        {
          if (filteredArray[x].name == filteredArray[x].caption)
            obj.push(filteredArray[x].name);
          else
          {
            i2uiFindPivotLeafs(pivot,
                               filteredArray[x].field.caption,
                               filteredArray[x].caption,
                               obj);
          }
        }
      }
    }
  }
}
function i2uiBuildPivotRetains(pivot, view)
{                        
  var len;

  len = view.ColumnAxis.FieldSets.Count;
  for (var i=0; i<len; i++)
    i2uiProcessPivotFieldFilters(pivot, view, view.ColumnAxis.FieldSets(i));

  len = view.FilterAxis.FieldSets.Count;
  for (var i=0; i<len; i++)
    i2uiProcessPivotFieldFilters(pivot, view, view.FilterAxis.FieldSets(i));

  len = view.RowAxis.FieldSets.Count;
  for (var i=0; i<len; i++)
    i2uiProcessPivotFieldFilters(pivot, view, view.RowAxis.FieldSets(i));
}
function i2uiSeries(label, xpath, key)
{
  this.label = label;
  this.xpath = xpath;
  this.key   = key;
}
function i2uiXaxis(label, xpath, key)
{
  this.label = label;
  this.xpath = xpath;
  this.key   = key;
}
function i2uiSort(obj1, obj2)
{
  if (obj1.key < obj2.key)
    return -1;
  else
  if (obj1.key > obj2.key)
    return 1;
  else
    return 0;
}
function i2uiDelayedPivotDataChange(id, pivot)
{
  try 
  {
    // update any related visible chart
    try
    {
      if (pivot==null) 
        pivot=document.getElementById(id);
      if (pivot.componentShowing=="pivotchart" ||
          pivot.componentShowing=="chart")
      {
        var obj = document.getElementById(id+"Chart_xml");
        var obj2 = document.getElementById(id+"Chart");
        if (obj != null && obj2 != null)
        {
          var id2 = id.substring(0,id.length-5);
          obj.loadXML(i2uiChartPivot(id2,obj2.height,obj2.width,false));
          obj2.window.i2uiProcessChart();
        }
      }
      else
      if (pivot.componentShowing=="multichart")
      {
        var obj = document.getElementById(id+"Chart_xml");
        var obj2 = document.getElementById(id+"Chart");
        if (obj != null && obj2 != null)
        {
          var id2 = id.substring(0,id.length-5);
          obj.loadXML(i2uiChartPivot(id2,obj2.height,obj2.width,true));
          obj2.window.i2uiProcessChart();
        }
      }
    }
    catch(e){}
    // inform application
    i2uiPivotChangeCallback(id);
  }
  catch(e){}
}
function i2uiGetPivotCalculationsXML(id, pivot)
{
  var xml = "<pivotcalculations>";
  if (pivot == null)
    pivot = document.getElementById(id+"Pivot");
  var view = pivot.ActiveView;
  var axis = view.DataAxis;
  var len = axis.FieldSets.Count;
  for (var i=0; i<len; i++)
    if (axis.FieldSets(i).Type == 4 &&
        axis.FieldSets(i).BoundField.Expression != "")
    {
      var name = axis.FieldSets(i).Name;
      if (name.indexOf("fieldset_") == 0)
        name = name.substring(9);
      xml += '<field name="'+name+'" expression="'+axis.FieldSets(i).BoundField.Expression+'"/>';
    }
  xml += "</pivotcalculations>"
  return(xml);
}
function i2uiSetPivotCalculations(id, xml, pivot)
{
  //alert(xml);
  if (pivot == null)
    pivot = document.getElementById(id+"Pivot");
  var view = pivot.ActiveView;
  var axis = view.DataAxis;
  var calcdom = new ActiveXObject(domid);
  if (calcdom.loadXML(xml))
  {
    calcdom.setProperty("SelectionLanguage", "XPath");
    calcdom.setProperty("SelectionNamespaces", pivotSchemas);
    var fields = calcdom.selectNodes("/pivotcalculations/field");
    var len = fields.length;
    //alert(foo.bar.zed());
    for (var i=0; i<len; i++)
    {
      var name = fields[i].getAttribute("name");
      var expression = fields[i].getAttribute("expression");
      try
      {
        var theFieldSet = view.AddFieldSet(name);
        theFieldSet.AddCalculatedField(name,
                                       name,
                                       "calculated_"+name,
                                       expression);
        axis.InsertFieldSet(theFieldSet);
        return;
        var theTotal = view.AddTotal(name,
                                     theFieldSet.Fields(0),
                                     showAsValues["sum"]);
        axis.InsertTotal(theTotal);
        theTotal.View.HeaderBackColor = PivotLabelColor;
        theTotal.View.HeaderFont.Bold = true;
      } catch(e){}
    }
  }
}
function i2uiGetPivotLayout(id, which, pivot)
{
  var names = new Array();
  if (pivot == null)
    pivot = document.getElementById(id+"Pivot");
  var view = pivot.ActiveView;
  var axis = null;

  if (which == "rows")
  {   
    axis = view.RowAxis;
  }
  else
  if (which == "columns")
  {
    axis = view.ColumnAxis;
  }
  else
  if (which == "filters")
  {
    axis = view.FilterAxis;
  }
  else
  if (which == "data")
  {
    axis = view.DataAxis;
    //alert(foo.bar.zed());
  }
  else
  if (which == "none")
  {
    try
    {
      var nonenodes = xmlDom.selectNodes('/pivot/view'+pivot.viewname+'/item[@axis="none"]')
      var len = nonenodes.length;
      for (var i=0; i<len; i++)
        names[i] = nonenodes[i].text;
    }
    catch(e)
    {
    }
  }
  if (axis != null) 
  {
    var len = axis.FieldSets.Count;
    for (var i=0; i<len; i++)
      names[i] = axis.FieldSets(i).Name;
  }
  return names;
}
function i2uiGetPivotLayoutXML(id, transpose)
{
  var xml = "<pivotlayout>"
  var pivot = document.getElementById(id+"Pivot");
  var view = pivot.ActiveView;

  var names = new Array();
  var axis = view.RowAxis;
  var len = axis.FieldSets.Count;
  for (var i=0; i<len; i++)
    names[i] = axis.FieldSets(i).Name;
  if (!transpose)
    xml += "<rows>"+names+"</rows>";
  else
    xml += "<columns>"+names+"</columns>";

  var axis = view.ColumnAxis;
  var names = new Array();
  var len = axis.FieldSets.Count;
  for (var i=0; i<len; i++)
    names[i] = axis.FieldSets(i).Name;
  if (!transpose)
    xml += "<columns>"+names+"</columns>";
  else
    xml += "<rows>"+names+"</rows>";
  var axis = view.FilterAxis;
  var names = new Array();
  var len = axis.FieldSets.Count;
  for (var i=0; i<len; i++)
    names[i] = axis.FieldSets(i).Name;
  xml += "<filters>"+names+"</filters>";
  // need to include the none axis
  xml += "</pivotlayout>"
  return xml;
}
function i2uiSetPivotLayout(id, which, itemNames)
{
  var pivot = document.getElementById(id+"Pivot");
  var view = pivot.ActiveView;
  var axis = null;

  if (which == "rows")
  {   
    axis = view.RowAxis;
  }
  else
  if (which == "columns")
  {
    axis = view.ColumnAxis;
  }
  else
  if (which == "filters")
  {
    axis = view.FilterAxis;
  }

  if (axis != null) 
  {
    var names = itemNames.split(",");
    var len   = names.length;
    for (var i=0; i<len; i++)
      try {axis.InsertFieldSet(view.FieldSets(names[i]));}catch(e){}
  }
}
function i2uiSetPivotLayoutXML(id, xml)
{
  var xmlDom2 = new ActiveXObject(domid);
  if (xmlDom2.loadXML(xml))
  {
    xmlDom2.setProperty("SelectionLanguage", "XPath");
    xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);
    var node = xmlDom2.selectSingleNode("//rows");
    if (node != null)
      i2uiSetPivotLayout(id,"rows",node.text);
    node = xmlDom2.selectSingleNode("//columns");
    if (node != null)
      i2uiSetPivotLayout(id,"columns",node.text);
    node = xmlDom2.selectSingleNode("//filters");
    if (node != null)
      i2uiSetPivotLayout(id,"filters",node.text);
  }
}
function i2uiGetPivotRender(id, which)
{
  var names = new Array();
  var pivot = document.getElementById(id+"Pivot");
  var view = pivot.ActiveView;
  var axis = null;

  if (which == "rows")
  {   
    axis = view.RowAxis;
  }
  else
  if (which == "columns")
  {
    axis = view.ColumnAxis;
  }
  else
  if (which == "filters")
  {
    axis = view.FilterAxis;
  }
  if (axis != null) 
  {
    var len = axis.FieldSets.Count;
    for (var i=0; i<len; i++)
      names[i] = axis.FieldSets(i).Name;
  }

  return names;
}
function i2uiPivotRetainFilters(id, flag)
{
  //alert(id+" "+flag+" vs "+pivot.retainFilters);
  try
  {
    var pivot = document.getElementById(id+"Pivot");
    pivot.retainFilters = flag;
    var icon1, icon2;
    if (flag)
    {
      icon1 = document.getElementById(id+"ignoreFiltersPivot");
      icon2 = document.getElementById(id+"retainFiltersPivot");
    }
    else
    {
      icon1 = document.getElementById(id+"retainFiltersPivot");
      icon2 = document.getElementById(id+"ignoreFiltersPivot");
    }
    icon1.style.display="none";
    icon2.style.display="";
  }
  catch(e)
  {
  }
}
function i2uiChangePivotView(id)
{
  try
  {
    var obj = document.getElementById(id+"views");
    var viewname = obj.options[obj.selectedIndex].text;
    var pivot = document.getElementById(id+"Pivot");
    if (pivot.dirty && pivot.keyfield == null)
    {
      alert("One or more values have been changed and need to be saved before switching to a different view.");
      return;
    }
    pivot.retainincludes = new Array();
    pivot.retainexcludes = new Array();
    if (obj != null && pivot.retainFilters)
      i2uiBuildPivotRetains(pivot, pivot.ActiveView);
    pivot.pivotdom = new ActiveXObject(domid);
    pivot.pivotdom.setProperty("SelectionLanguage", "XPath");
    pivot.pivotdom.setProperty("SelectionNamespaces", pivotSchemas);
    //var pivotxml = i2uiBuildPivotXML(null, pivot, id, "[@name='"+viewname+"']");
    //i2uiPopulatePivot(pivot, pivotxml, true);
    i2uiBuildPivotXML(null, pivot, id, "[@name='"+viewname+"']");
    //i2uiPopulatePivot(pivot, pivotDom.xml, true);
    i2uiPopulatePivot(pivot, null, true);
    //

    var view = pivot.ActiveView;
    var rc2 = i2uiLayoutPivot(pivot, view);
    i2uiStylizePivot(pivot, view, id, null, null, rc2);
  }
  catch(e){}
}
// this version produces spreadsheet in data cell order
function orig_i2uiPivotToSpreadsheet(id)
{
  var pivot = document.getElementById(id+"Pivot");
  var xmldoc = new ActiveXObject(domid);
  var xsldoc = new ActiveXObject(domid);
  if (xmldoc != null && xsldoc != null)
  {
    xmldoc.loadXML(pivot.xmlData2);
    xsldoc.loadXML('<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:z="#RowsetSchema" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl" version="1.0"><xsl:template match="/"><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"><Worksheet ss:Name="Sheet1"><Table><xsl:apply-templates select="//z:row"/></Table></Worksheet></Workbook></xsl:template><xsl:template match="z:row"><Row><xsl:for-each select="@*"><Cell><Data ss:Type="String"><xsl:value-of select="."/></Data></Cell></xsl:for-each></Row></xsl:template></xsl:stylesheet>');
    result = xmldoc.transformNode(xsldoc);
    return result;
  }
}

// this version produces spreadsheet in pivot order
// uses raw data values and not totaled numbers
// display order is not necessarily the same as pivot
function i2uiPivotToSpreadsheet(id)
{
  var pivot = document.getElementById(id+"Pivot");
  var xpath = i2uiBuildPivotXpath(pivot, pivot.ActiveView);
  var xmldoc = new ActiveXObject(domid);
  var xsldoc = new ActiveXObject(domid);
  if (xmldoc != null && xsldoc != null)
  {
    xmldoc.setProperty("SelectionNamespaces", pivotSchemas);
    var axis = new Array("filters","rows","columns","data");
    var axislen = axis.length;
    var xslprefix = '<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:z="#RowsetSchema" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl" version="1.0"><xsl:template match="/"><Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:c="urn:schemas-microsoft-com:office:component:spreadsheet" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">'+
                    '<Styles>'+
                    '<Style ss:ID="header"><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/></Borders><Font ss:Size="10" ss:Bold="1"/><Interior ss:Color="#d1d6f0" ss:Pattern="Solid"/><NumberFormat/><Protection/></Style>'+
                    '<Style ss:ID="nondata"><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/></Borders><Interior ss:Color="#eceef8" ss:Pattern="Solid"/><NumberFormat/><Protection/></Style>'+
                    '<Style ss:ID="editabledata"><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/></Borders><Interior ss:Color="#ffffff" ss:Pattern="Solid"/><NumberFormat/><Protection ss:Protected="0"/><Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/></Style>'+
                    '<Style ss:ID="staticdata"><Borders><Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/><Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/></Borders><Interior ss:Color="#e0e0e0" ss:Pattern="Solid"/><NumberFormat/><Protection ss:Protected="1"/><Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/></Style>'+
                    '</Styles>'+
                    '<Worksheet ss:Name="Sheet1" ss:Protected="1"><Table>';
    var xslprefix2 = '<xsl:apply-templates select="//z:row'+xpath+'"/></Table><x:WorksheetOptions><x:AllowFormatCells/><x:AllowSizeCols/><x:AllowSizeRows/><x:AllowFilter/></x:WorksheetOptions><c:WorksheetOptions><AutoFilter x:Range="R1C1:R2C100"/><c:DisplayCustomHeaders/></c:WorksheetOptions></Worksheet></Workbook></xsl:template><xsl:template match="z:row">';
    var xslrowprefix = '<Row>';
    var xslrowsuffix = '</Row>';
    var xslsuffix = '</xsl:template></xsl:stylesheet>';
    var names;
    var nameslen;
    var xmlname;

    xmldoc.loadXML(pivot.xmlData2);
    var xslEvery = "";
    var xslStyle = "";
    var styleinfo;
    pivot.spreadsheetColumnNames = new Array();
    pivot.editedRow = new Array();
    pivot.editedColumn = new Array();
    pivot.editedValue = new Array();

    for (var i=0; i<axislen; i++)
    {
      names = i2uiGetPivotLayout(id, axis[i]);
      nameslen = names.length;
      styleinfo = 'ss:StyleID="nondata"';
      for (var j=0; j<nameslen; j++)
      {
        // must convert display name to xml name
        xmlname = xmldoc.selectSingleNode("//s:AttributeType[@rs:name='"+names[j]+"']");
        if (xmlname != null)
          xmlname = xmlname.getAttribute("name");
        else
          xmlname = names[j];
        pivot.spreadsheetColumnNames.push(xmlname);
        if (axis[i]=="data")
        {
          if (pivot.editableColumns[names[j]] != null)
            styleinfo = 'ss:StyleID="editabledata"';
          else
            styleinfo = 'ss:StyleID="staticdata"';
        }
        xslEvery += '<Cell><Data ss:Type="String"><xsl:value-of select="@'+xmlname+'"/></Data></Cell>';
        xslStyle += '<Column c:Caption="'+names[j]+'" '+styleinfo+' ss:AutoFitWidth="1"/>';
      }
    }
    var xsl = xslprefix+xslStyle+xslprefix2+xslrowprefix+xslEvery+xslrowsuffix+xslsuffix;
    xsldoc.loadXML(xsl);
    return xmldoc.transformNode(xsldoc);
  }
}
function i2uiPivotShowPivot(id)
{
  var pivot = document.getElementById(id+"Pivot");
  pivot.componentShowing = "pivot";
  i2uiToggleItemVisibility(id+"Pivot", "show");
  i2uiToggleItemVisibility(id+"PivotEdit", "show");
  i2uiToggleItemVisibility(id+"PivotChart", "hide");
  i2uiToggleItemVisibility(id+"PivotSpreadsheet", "hide");
  i2uiToggleItemVisibility(id+"showPivot", "hide");
  i2uiToggleItemVisibility(id+"flipPivot", "show");
  i2uiToggleItemVisibility(id+"stretchPivot", "show");
  i2uiToggleItemVisibility(id+"showChart", "show");
  i2uiToggleItemVisibility(id+"showBoth",  "show");
  i2uiToggleItemVisibility(id+"showMultichart", "show");
  i2uiToggleItemVisibility(id+"showSpreadsheet", "show");
  var h = document.body.offsetHeight - 110;
  var w = document.body.offsetWidth - 70;
  var obj3 = document.getElementById(id+"Container");
  if (obj3 != null)
  {
    h = Math.max(50,obj3.clientHeight-30);
    w = Math.max(50,obj3.clientWidth-40);
  }
  i2uiApplyPivotEdits(id, pivot);
  i2uiBestFitPivot(id,h-10,w);
}
function i2uiPivotFlipPivot(id) // transpose measures in rows and columns
{
  var pivot = document.getElementById(id+"Pivot");
  var layout = i2uiGetPivotLayoutXML(id, true);
  i2uiSetPivotLayoutXML(id, layout);
  // apply bestfit
  var h = document.body.offsetHeight - 110;
  var w = document.body.offsetWidth - 70;
  var obj3 = document.getElementById(id+"Container");
  if (obj3 != null)
  {
    h = Math.max(50,obj3.clientHeight-30);
    w = Math.max(50,obj3.clientWidth-40);
  }
  if (pivot.componentShowing == "pivotchart")
    w = w/2;
  i2uiBestFitPivot(id);
}
function i2uiPivotShowChart(id)
{
  var pivot = document.getElementById(id+"Pivot");
  i2uiToggleItemVisibility(id+"Pivot", "hide");
  i2uiToggleItemVisibility(id+"PivotEdit", "hide");
  i2uiToggleItemVisibility(id+"PivotChart", "show");
  i2uiToggleItemVisibility(id+"PivotSpreadsheet", "hide");
  i2uiToggleItemVisibility(id+"showPivot", "show");
  i2uiToggleItemVisibility(id+"stretchPivot", "hide");
  i2uiToggleItemVisibility(id+"showChart", "hide");
  i2uiToggleItemVisibility(id+"showBoth",  "show");
  i2uiToggleItemVisibility(id+"showMultichart", "show");
  i2uiToggleItemVisibility(id+"showSpreadsheet", "show");
  i2uiToggleItemVisibility(id+"PivotEditForm","hide");
  i2uiToggleItemVisibility(id+"PivotMassEditForm","hide");
  var obj = document.getElementById(id+"PivotChart_xml");
  var obj2 = document.getElementById(id+"PivotChart");
  if (obj != null && obj2 != null)
  {
    var h = document.body.offsetHeight - 110; 
    var w = (document.body.offsetWidth - 70);
    var obj3 = document.getElementById(id+"Container");
    if (obj3 != null)
    {
      h = Math.max(50,obj3.clientHeight-50);
      w = Math.max(50,obj3.clientWidth-50);
    }
    if (pivot.componentShowing == "pivotchart")
    {
      obj2.window.i2uiResizeChart(h,w,true);
    }
    else
    {
      i2uiApplyPivotEdits(id, pivot);
      var chartXML = i2uiChartPivot(id,h,w,false);
      obj.loadXML(chartXML);
      obj2.window.i2uiProcessChart();
    }
  }
  pivot.componentShowing = "chart";
}
function i2uiPivotShowBoth(id)
{ 
  var pivot = document.getElementById(id+"Pivot");
  i2uiToggleItemVisibility(id+"Pivot", "show");
  i2uiToggleItemVisibility(id+"PivotEdit", "show");
  i2uiToggleItemVisibility(id+"PivotChart", "show");
  i2uiToggleItemVisibility(id+"PivotSpreadsheet", "hide");
  i2uiToggleItemVisibility(id+"showPivot", "show");
  i2uiToggleItemVisibility(id+"stretchPivot", "show");
  i2uiToggleItemVisibility(id+"showChart", "show");
  i2uiToggleItemVisibility(id+"showBoth",  "hide");
  i2uiToggleItemVisibility(id+"showMultichart", "show");
  i2uiToggleItemVisibility(id+"showSpreadsheet", "show");
  i2uiToggleItemVisibility(id+"PivotEditForm","hide");
  i2uiToggleItemVisibility(id+"PivotMassEditForm","hide");
  var obj = document.getElementById(id+"PivotChart_xml");
  var obj2 = document.getElementById(id+"PivotChart");
  if (obj != null && obj2 != null)
  {
    var h = document.body.offsetHeight - 110;
    var w = (document.body.offsetWidth - 70)/2;
    var obj3 = document.getElementById(id+"Container");
    if (obj3 != null)
    {
      h = Math.max(50,obj3.clientHeight-55);
      w = Math.max(50,(obj3.clientWidth-40)/2);
    }
    i2uiBestFitPivot(id,h-10,w);
    if (pivot.componentShowing == "chart")
    {
      obj2.window.i2uiResizeChart(h,w,true);
    }
    else
    {
      i2uiApplyPivotEdits(id, pivot);
      var chartXML = i2uiChartPivot(id,h,w,false);
      //LPM alert(chartXML);
      obj.loadXML(chartXML);
      obj2.window.i2uiProcessChart();
    }
  }
  pivot.componentShowing = "pivotchart";
}
function i2uiPivotShowMultichart(id)
{
  var pivot = document.getElementById(id+"Pivot");
  pivot.componentShowing = "multichart";
  i2uiToggleItemVisibility(id+"Pivot", "hide");
  i2uiToggleItemVisibility(id+"PivotEdit", "hide");
  i2uiToggleItemVisibility(id+"PivotChart", "show");
  i2uiToggleItemVisibility(id+"PivotSpreadsheet", "hide");
  i2uiToggleItemVisibility(id+"showPivot", "show");
  i2uiToggleItemVisibility(id+"stretchPivot", "hide");
  i2uiToggleItemVisibility(id+"showChart", "show");
  i2uiToggleItemVisibility(id+"showBoth",  "show");
  i2uiToggleItemVisibility(id+"showMultichart", "hide");
  i2uiToggleItemVisibility(id+"showSpreadsheet", "show");
  i2uiToggleItemVisibility(id+"PivotEditForm","hide");
  i2uiToggleItemVisibility(id+"PivotMassEditForm","hide");
  var obj = document.getElementById(id+"PivotChart_xml");
  var obj2 = document.getElementById(id+"PivotChart");
  if (obj != null && obj2 != null)
  {
    i2uiApplyPivotEdits(id, pivot);
    var h = document.body.offsetHeight - 110;
    var w = (document.body.offsetWidth - 70);
    var obj3 = document.getElementById(id+"Container");
    if (obj3 != null)
    {
      h = Math.max(50,obj3.clientHeight-50);
      w = Math.max(50,obj3.clientWidth-50);
    }
    var chartXML = i2uiChartPivot(id,h,w,true);
    obj.loadXML(chartXML);
    obj2.window.i2uiProcessChart();
  }
}
function i2uiPivotShowSpreadsheet(id)
{ 
  var pivot = document.getElementById(id+"Pivot");
  pivot.componentShowing = "spreadsheet";
  i2uiToggleItemVisibility(id+"Pivot", "hide");
  i2uiToggleItemVisibility(id+"PivotEdit", "hide");
  i2uiToggleItemVisibility(id+"PivotChart", "hide");
  i2uiToggleItemVisibility(id+"PivotSpreadsheet", "show");
  i2uiToggleItemVisibility(id+"showPivot", "show");
  i2uiToggleItemVisibility(id+"stretchPivot", "hide");
  i2uiToggleItemVisibility(id+"showChart", "show");
  i2uiToggleItemVisibility(id+"showBoth",  "show");
  i2uiToggleItemVisibility(id+"showMultichart", "show");
  i2uiToggleItemVisibility(id+"showSpreadsheet", "hide");
  i2uiToggleItemVisibility(id+"PivotEditForm","hide");
  i2uiToggleItemVisibility(id+"PivotMassEditForm","hide");
  var result = i2uiPivotToSpreadsheet(id);
  var obj = document.getElementById(id+"PivotSpreadsheet_xml");
  if (obj != null)
  {
    obj.loadXML(result);
    i2uiInitSpreadsheet(id+"Pivot",id+"container",null,100,100,true);
    var w = document.body.offsetWidth - 75;
    var h = document.body.offsetHeight - 40;
    var obj3 = document.getElementById(id+"Container");
    if (obj3 != null)
    {
      h = Math.max(50,obj3.clientHeight-30);
      w = Math.max(50,obj3.clientWidth-40);
    }
    w = Math.max(50,w);
    h = Math.max(50,h);
    i2uiResizeSpreadsheet(id+"Pivot", id+"container", true, null, w);
  }
}
function i2uiGetPivotComponentShowing(id)
{
  var pivot = document.getElementById(id+"Pivot");
  try
  {
    return pivot.componentShowing;
  }
  catch(e)
  {
    return "";
  }
}
function i2uiApplyPivotEdits(id, pivot)
{
  try
  {
    if (pivot == null)
      pivot = document.getElementById(id+"Pivot");
    var len = pivot.editedRow.length;
    if (len == 0)
      return;
    pivot.dirty = true;
    var xmlDom2 = new ActiveXObject(domid);
    xmlDom2.setProperty("SelectionLanguage", "XPath");
    xmlDom2.setProperty("SelectionNamespaces", pivotSchemas);
    if (xmlDom2.loadXML(pivot.xmlData2))
    {
      var ssobj = document.getElementById(id+"PivotSpreadsheet");
      for (var i=0; i<len; i++)
      {
        var row    = pivot.editedRow[i];
        var column = pivot.editedColumn[i];
        var value  = pivot.editedValue[i];
        var xpath = "//z:row";
        for (var j=1; j<column; j++)
        {
          var keyValue = ssobj.ActiveSheet.Range(i2uiBuildRange(row, j)).Value;
          xpath += "[@"+pivot.spreadsheetColumnNames[j-1]+"='"+keyValue+"']";
        }
        var datarow = xmlDom2.selectSingleNode(xpath);
        if (datarow != null)
        {             
          var editname = pivot.spreadsheetColumnNames[column-1];
          // verify new data is numeric for numeric measures
          if (pivot.fieldTypes[editname] == "float" && value * 1 != value)
          {
              continue;
          }
          datarow.setAttribute(editname, value);
          datarow.setAttribute("i2uidirty", 1);
        }
      }

      pivot.xmlData2 = xmlDom2.xml;
      // get existing layout style
      var xmldata = pivot.XMLData;
      // populate with new data
      i2uiPopulatePivot(pivot, pivot.xmlData2);
      // reapply layout
      pivot.XMLData = xmldata;

      pivot.editedRow = new Array();
      pivot.editedColumn = new Array();
      pivot.editedValue = new Array();
    }
  }
  catch(e){}
}
function i2uiPivotStretchPivot(id)
{
  var which = i2uiGetPivotComponentShowing(id);
  if (which == "pivot")
  {
    var h = document.body.offsetHeight - 110;
    var w = document.body.offsetWidth - 70;
    var obj3 = document.getElementById(id+"Container");
    if (obj3 != null)
    {
      h = Math.max(50,obj3.clientHeight-40);
      w = Math.max(50,obj3.clientWidth-40);
    }
    i2uiBestFitPivot(id,h-10,w);
  }
  else
  if (which == "pivotchart")
  {
    var h = document.body.offsetHeight - 110;
    var w = (document.body.offsetWidth - 70)/2;
    var obj3 = document.getElementById(id+"Container");
    if (obj3 != null)
    {
      h = Math.max(50,obj3.clientHeight-55);
      w = Math.max(50,(obj3.clientWidth-40)/2);
    }
    i2uiBestFitPivot(id,h-10,w);
  }
}
function i2uiPivotPopulateLayoutPane(id, path)
{
  try
  {
    var cnt = wins.length;
    var id2 = wins[cnt-1];
    if (id2 != null && id2 != "empty")
    {
      i2uicallMDI(id2, "populate('"+id+"')");
      return;
    }
  }
  catch(e){}
  i2uinewMDIWin(50,200,path+'/i2uipivotlayout.jsp?id='+id,'Pivot Layout',300,500,false,false);
}
function getNames(id)
{
  var names = new Array();

  var temp = i2uiGetPivotLayout(id,"rows");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    names.push(temp[i]);
  }
  var temp = i2uiGetPivotLayout(id,"columns");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    names.push(temp[i]);
  }
  var temp = i2uiGetPivotLayout(id,"filters");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    names.push(temp[i]);
  }
  var temp = i2uiGetPivotLayout(id,"none");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    names.push(temp[i]);
  }
  return names.sort();
}
function getActions(id)
{
  var actions = new Array();

  var temp = i2uiGetPivotLayout(id,"rows");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    actions.push("document.getElementById('AXIS_row_"+temp[i]+"').checked=true;");
    actions.push("document.getElementById('RENDER_all_"+temp[i]+"').checked=true;");
  }
  var temp = i2uiGetPivotLayout(id,"columns");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    actions.push("document.getElementById('AXIS_col_"+temp[i]+"').checked=true;");
    actions.push("document.getElementById('RENDER_all_"+temp[i]+"').checked=true;");
  }
  var temp = i2uiGetPivotLayout(id,"filters");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    actions.push("document.getElementById('AXIS_filter_"+temp[i]+"').checked=true;");
    actions.push("document.getElementById('RENDER_all_"+temp[i]+"').checked=true;");
  }
  var temp = i2uiGetPivotLayout(id,"none");
  var len = temp.length;
  for (var i=0; i<len; i++)
  {
    actions.push("document.getElementById('AXIS_none_"+temp[i]+"').checked=true;");
    actions.push("document.getElementById('RENDER_all_"+temp[i]+"').checked=true;");
  }
  return actions;
}
function i2uiPivotToggleLock(pivot, measureName, nameValuePairs)
{   
  //alert(foo.bar.zed();
  var bookmarks = new Array();
  if (pivot.SelectionType == "PivotDetailRange")
  {
    try
    {
      bookmarks[pivot.Selection.TopLeft.Bookmark-1] = true;
    }
    catch(e){}
  }
  else
  if (pivot.SelectionType == "PivotAggregates")
  {   
    if (pivot.Selection.Item(0).Cell.RowMember.UniqueName == "Total" ||
        pivot.Selection.Item(0).Cell.RowMember.UniqueName == "Grand Total")
    {
      var cell = pivot.Selection.Item(0);
      var targetMeasure = cell.Name;
      cell = cell.Cell;
      var mbr, totaledMbr = null;
      mbr = cell.RowMember;
      try
      {
        while (mbr != null)
        {
          if (mbr.Value=="Total" || 
              mbr.Value=="Grand Total")
            totaledMbr = mbr.ParentMember;
          else
            details += mbr.Field.Name+"="+mbr.Value+" ";
          mbr = mbr.ParentMember;
        }
      }
      catch(e){}
      mbr = cell.ColumnMember;
      var lowestColMbr = mbr;

      try
      {
        while (mbr != null)
        {
          if (mbr.Value=="Total" ||
              mbr.Value=="Grand Total")
            totaledMbr = mbr.ParentMember;
          else
            details += mbr.Field.Name+"="+mbr.Value+" ";             
          mbr = mbr.ParentMember;
        }
      }
      catch(e){}
      i2uiLocatePivotDetail(pivot, totaledMbr, lowestColMbr, targetMeasure, bookmarks);
    }
    else
    {
      try
      {
        bookmarks[pivot.Selection.Item(0).Cell.Recordset.Bookmark-1] = true;
        pivot.Selection.Item(0).BackColor = Color.Maroon;
      }
      catch(e){}
    }
  }
  else
  if (pivot.SelectionType == "PivotFields")
  {
    //??
  }

  if (pivot.lockedColumns[measureName] == null)
      pivot.lockedColumns[measureName] = new Array();
  
  for (x in bookmarks)
  {
    if (pivot.lockedColumns[measureName][x] == null)
      pivot.lockedColumns[measureName][x] = true;
    else
      pivot.lockedColumns[measureName][x] = !pivot.lockedColumns[measureName][x];  
    alert("toggle lock for "+measureName+" type="+pivot.SelectionType+" bookmark="+x+" now="+pivot.lockedColumns[measureName][x]);
  }
}

function excelPivotMapTexts(pivot, values)
{
  var array = values.split(',');
  var l = array.length;
  var map = pivot.namemap;
  var result = [];
  for(var i = 0; i < l; ++i){
    var t = array[i].split('=');
    result[result.length] = [ map[t[0]], t[1] ];
  }
  pivot.popupInfo = result;
}

function excelPivotOpenPopup(url, pivot, values)
{
  trapExcelPivotChanges();
  excelPivotMapTexts(pivot, values);
  popUpWindow(appendExcelPivotPopupInfo(url, pivot.popupInfo));
}

function excelPivotOpenURL(url, pivot, values)
{
  trapExcelPivotChanges();
  excelPivotMapTexts(pivot, values);
  navigateTo(appendExcelPivotPopupInfo(url, pivot.popupInfo));
}

function excelPivotUpdate(action, targetId, pivot, values)
{
  excelPivotMapTexts(pivot, values);
  trapExcelPivotChanges();
  update(action, targetId);
}

function appendExcelPivotPopupInfo(url, info)
{
  var l =info.length;
  var s = url.indexOf('?') > 0 ? '&' : '?';
  for(var i = 0; i < l; ++i){
    var t = info[i];
    url += s + t[0] + '=' + t[1];
    s = '&';
  }
  return url;
}


