/////////////////////////////////////////////////////////////////////////////
// (c) Copyright 2000 - 2001, i2 Technologies, Inc. (formerly Intellection)//
// ALL RIGHTS RESERVED.                                                    //
//                                                                         //
// This UNPUBLISHED PROPRIETARY software is  subject to the full copyright //
// notice in the COPYRIGHT file in this directory.                         //
/////////////////////////////////////////////////////////////////////////////

/* set i2uitracelevel to >0 to output trace messages */
var i2uitracelevel = 0;
var i2uitracetext  = "";
var i2uiScrollerWidth= 17;
var i2uiResizeWidthVariable  = new Array();
var i2uiResizeMasterVariable = new Array();
var i2uiResizeSlaveVariable  = new Array();
var i2uiResizeSlave2Variable = new Array();
var i2uiResizeFlagVariable   = new Array();
var i2uiResizeKeyword        = "TABLERESIZE_";
var i2uiResizeKeywordLength  = i2uiResizeKeyword.length;

var i2uiResizeSlaveorigX       = 0;
var i2uiResizeSlavenewX        = 0;
var i2uiResizeSlavewhichEl     = null;
var i2uiResizeSlaveOrigonmouseup;
var i2uiResizeSlaveOrigonmousemove;

var i2uiMenuOrigonmouseup;
var i2uiMenuActiveId = null;
var i2uiSubMenuActiveId = null;
var i2uiMenu_x = null;
var i2uiMenu_y = null;
var i2uiSubMenu_x = null;
var i2uiSubMenu_y = null;
var i2uiSubMenuFlag = null;

var i2uiSubMenuArr = new Array();

var i2uiManageTreeTableUserFunction = null;
var i2uiToggleContentUserFunction = null;

var i2uiImageDirectory = "/skins/i2-standard/images";

var i2uiActiveTreeNode = null;

var i2uiCurrentTabset = null;
var i2uiCurrentTab = null;
var i2uiClickedTab = null;

/*
  The i2uitaglib.js methods dynamically change images
  used by the i2uitaglib components.  i2uiSetImageDirectory()
  defines the directory that i2uitaglib.js will use for
  those images.  This method should be called immediately
  after i2uitaglib.js is linked to the page.  The <i2:dhtml>
  tag creates both the link to i2uitaglib.js and the
  call to i2uiSetImageDirectory() using the image
  directory of the current skin.

  imageDirectory - path to the component image directory

  return - none

  Compatibility:  IE, NS6, NS4
*/
function i2uiSetImageDirectory(imageDirectory)
{
  i2uiImageDirectory = imageDirectory;
}

/*
  This routine toggles the visibility of a body of content.
  The content must be enclosed within a TBODY tag.

  item - the element that received the event
  nest - how many levels nested is the item from the enclosing table

  return - none

  Compatibility:  IE, NS6
*/
function i2uiToggleContent(item, nest, relatedroutine)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }
  // find the owning table for the item which received the event.
  // in this case the item is the expand/collapse image
  // note: the table may be several levels above as indicated by 'nest'
  var owningtable = item;
  if (item.tagName == "A")
  {
    item = item.childNodes[0];
  }

  while (owningtable != null && nest > 0)
  {
    if (owningtable.parentElement)
    {
      owningtable = owningtable.parentElement;
    }
    else
    {
      owningtable = owningtable.parentNode;
    }
    if (owningtable != null && owningtable.tagName == 'TABLE')
    {
      nest--;
    }
  }

  var ownerid = owningtable.id;

  // for tabbed container, the true owning table is higher.
  // continue traversal in order to get the container id.
  if (ownerid == "")
  {
    var superowner = owningtable;
    while (superowner != null && ownerid == "")
    {
      if (superowner.parentElement)
      {
        superowner = superowner.parentElement;
      }
      else
      {
        superowner = superowner.parentNode;
      }
      if (superowner != null && superowner.tagName == 'TABLE')
      {
        ownerid = superowner.id;
      }
    }
  }

  // if found table, find child TBODY with proper id
  if (owningtable != null)
  {
    var pretogglewidth = owningtable.offsetWidth;

    // determine how many TBODY tags are within the table
    var len = owningtable.getElementsByTagName('TBODY').length;

    // now find proper TBODY that holds the content
    var contenttbody;
    for (var i=0; i<len; i++)
    {
      contenttbody = owningtable.getElementsByTagName('TBODY')[i];
      if (contenttbody.id == '_containerBody' ||
          contenttbody.id == '_containerbody' ||
          contenttbody.id == 'containerBodyIndent' ||
          contenttbody.id == 'containerbody')
      {
        var delta;

        if (contenttbody.style.display == "none")
        {
          contenttbody.style.display = "";
          item.src = i2uiImageDirectory+"/container_collapse.gif";
          owningtable.collapsed="false";
          delta = contenttbody.offsetHeight;
        }
        else
        {
          delta = 0 - contenttbody.offsetHeight;
          contenttbody.style.display = "none";
          item.src = i2uiImageDirectory+"/container_expand.gif";
          owningtable.collapsed="true";
        }
        if (i2uiToggleContentUserFunction != null)
        {
          eval(i2uiToggleContentUserFunction+"('"+ownerid+"',"+delta+")");
        }
        break;
      }
    }
  }
}

/*
  Collapses an expanded container.

  id - the id of the container to collaspe

  return - none

  Compatibility:  IE, NS6
*/
function i2uiCollapseContainer(id)
{
  if (document.layers)
  {
    return;
  }
  var nest = 1;
  var obj = document.getElementById(id);
  //i2uitrace(1,'i2uiCollapseContainer obj='+obj);
  if (obj != null)
  {
    // IE allows onlick for IMGs while Netscape 6 does not.
    // therefore look for IMG directly in cell or as a child of
    // an A tag
    var len = obj.rows[0].cells[0].childNodes.length;
    //i2uitrace(1,"#children in first row's first cell="+len);

    // handle complex header in container
    if (len == 1 && obj.tagName == "TABLE")
    {
      //i2uitrace(1,"obj.tagName="+obj.tagName);
      obj = obj.rows[0].cells[0].childNodes[0];
      nest = 2;
      len = obj.rows[0].cells[0].childNodes.length;
      //i2uitrace(1,"COMPLEX HEADER #children in first row's first cell="+len);
    }

    for (var i=0; i<len; i++)
    {
      //i2uitrace(1,'child #'+i+" tag="+obj.rows[0].cells[0].childNodes[i].tagName);
      if (obj.rows[0].cells[0].childNodes[i].tagName == 'IMG' &&
          obj.rows[0].cells[0].childNodes[i].src.indexOf("container_collapse.gif") != -1)
      {
        //i2uitrace(1,'toggle 1 nest='+nest);
        // toggle the container
        i2uiToggleContent(obj.rows[0].cells[0].childNodes[i], nest);

        // now change the expand/collapse icon
        obj.rows[0].cells[0].childNodes[i].src = i2uiImageDirectory+"/container_expand.gif";
        break;
      }
      else
      {
        if (obj.rows[0].cells[0].childNodes[i].tagName == 'A')
        {
          // Detect Degraded mode of collapsable containers
          // Added by WJD
          if (obj.rows[0].cells[0].childNodes[i].innerText == '-')
          {
            i2uiDegradedToggleContent(obj.rows[0].cells[0].childNodes[i], nest);
            break;
          }

          var len2 = obj.rows[0].cells[0].childNodes[i].childNodes.length;
          //i2uitrace(1,"#children in A tag="+len2);
          for (var j=0; j<len2; j++)
          {
            //i2uitrace(1,'child #'+j+" tag="+obj.rows[0].cells[0].childNodes[i].childNodes[j].tagName);
            if (obj.rows[0].cells[0].childNodes[i].childNodes[j].tagName == 'IMG' &&
                obj.rows[0].cells[0].childNodes[i].childNodes[j].src.indexOf("container_collapse.gif") != -1)
            {
              //i2uitrace(1,'toggle 2 nest='+nest);
              // toggle the container
              i2uiToggleContent(obj.rows[0].cells[0].childNodes[i], nest);

              // now change the expand/collapse icon
              obj.rows[0].cells[0].childNodes[i].childNodes[j].src = i2uiImageDirectory+"/container_expand.gif";
              break;
            }
          }
          break;
        }
      }
    }
  }
}

/*
  Expands a collapsed container.

  id - the id of the container to collaspe

  return - none

  Compatibility:  IE, NS6
*/
function i2uiExpandContainer(id)
{
  if (document.layers)
  {
    return;
  }
  var nest = 1;
  var obj = document.getElementById(id);
  //i2uitrace(1,'i2uiCollapseContainer obj='+obj);
  if (obj != null)
  {
    // IE allows onlick for IMGs while Netscape 6 does not.
    // therefore look for IMG directly in cell or as a child of
    // an A tag
    var len = obj.rows[0].cells[0].childNodes.length;

    // handle complex header in container
    if (len == 1 && obj.tagName == "TABLE")
    {
      obj = obj.rows[0].cells[0].childNodes[0];
      nest = 2;
      len = obj.rows[0].cells[0].childNodes.length;
    }

    for (var i=0; i<len; i++)
    {
      if (obj.rows[0].cells[0].childNodes[i].tagName == 'IMG' &&
          obj.rows[0].cells[0].childNodes[i].src.indexOf("container_expand.gif") != -1)
      {
        // toggle the container
        i2uiToggleContent(obj.rows[0].cells[0].childNodes[i], nest);

        // now change the expand/collapse icon
        obj.rows[0].cells[0].childNodes[i].src = i2uiImageDirectory+"/container_collapse.gif";
        break;
      }
      else
      {
        if (obj.rows[0].cells[0].childNodes[i].tagName == 'A')
        {
          var len2 = obj.rows[0].cells[0].childNodes[i].childNodes.length;
          for (var j=0; j<len2; j++)
          {
            if (obj.rows[0].cells[0].childNodes[i].childNodes[j].tagName == 'IMG' &&
                obj.rows[0].cells[0].childNodes[i].childNodes[j].src.indexOf("container_expand.gif") != -1)
            {
              // toggle the container
              i2uiToggleContent(obj.rows[0].cells[0].childNodes[i], nest);

              // now change the expand/collapse icon
              obj.rows[0].cells[0].childNodes[i].childNodes[j].src = i2uiImageDirectory+"/container_collapse.gif";
              break;
            }
          }
          break;
        }
      }
    }
  }
}

/*
  Changes the visibility of an object.
  If state is null, the object visibility is toggled.

  id - the id of the object to hide or show
  state - either 'show', 'hide', or null.

  return - none

  Compatibility:  IE, NS6, NS4
*/
function i2uiToggleItemVisibility(id,state,obj)
{
  var item;
  var docObj;
	
  //fix for eQ :: 530156 - IE loses document after we do save on reports
  if ( obj != null )
  {
    docObj = obj.document;
  }
  else
  {
    docObj = document;
  }
	
  if (docObj.layers)
  {
    item = docObj.layers[id];
    if (item != null)
    {
      if (state == null)
      {
        if (item.visibility == 'hide')
        {
          item.visibility = 'show';
        }
        else
        {
          item.visibility = 'hide';
        }
      }
      else
      {
        item.visibility = state;
      }
    }
    return;
  }

  item = docObj.getElementById(id);
  if (item != null)
  {
    // setting display to none or "" can damage the DOM for
    // Netscape 6.  you may want to consider the visibility
    // attribute instead.
    if (state == null)
    {
      if (item.style.display == "none")
      {
        item.style.display = "";
        item.style.visibility = "visible";
      }
      else
      {
        item.style.display = "none";
      }
    }
    else
    {
      if (state == 'show')
      {
        item.style.display = "";
        item.style.visibility = "visible";
      }
      else
      {
        item.style.display = "none";
      }
    }
  }
}

/*
  Dummy routine to halt the toggling of a tab to its selected state.

  return - <tbd>

  Compatibility:  IE, NS6, NS4
*/
function i2uiToggleTabNoop()
{
}

/*
  Activates a tab in a tabset.

  tabset_id - the id of the tab's tabset
  alttext - the tab description
  tab_element - the name of the tab to activate

  return - none

  Compatibility:  IE, NS6, NS4
*/
function i2uiToggleTab(tabset_id, alttext, tab_element)
{
  i2uiCurrentTabset = tabset_id;
  // handle Netscape 4.x
  if (document.layers)
  {
    var item;

    // display new description
    item = document.layers[tabset_id+"_description"];
    if (item != null)
    {
      var text = '<DIV style="color:#ffffff;background-color:#9999cc;text-align:left;position:absolute;top:0;left:0">'+alttext+'</DIV>';
      item.document.open();
      item.document.write(text);
      item.document.close();
    }
  }
  else
  // handle IE and Netscape 6
  {
    if (tab_element.tagName == 'undefined' ||
        tab_element.tagName == null)
    {
      return;
    }

    i2uiClickedTab = tab_element;
    //i2uitrace(1,"activate="+tab_element.tagName);

    var item = document.getElementById(tabset_id);

    //i2uitrace(1,"item="+item.tagName+"\n"+"TABLE body\n"+item.innerHTML);

    item = item.getElementsByTagName('TBODY')[0];
    //i2uitrace(1,"TBODY body\n"+item.innerHTML);

    var len = item.getElementsByTagName('TR').length;
    //i2uitrace(1,"#TR="+len);
    if (len > 0)
    {
      item = item.getElementsByTagName('TR')[0];
      //i2uitrace(1,"TR body\n"+item.innerHTML);
      len = item.getElementsByTagName('TD').length;
      //i2uitrace(1,"#TD="+len);

      var item2;
      var located = -1;
      var selectedtabid   = "tabSelected";
      var unselectedtabid = "tabUnSelected";
      var unselectedtabid2 = "tabUnSelected";
      if (!document.all)
      {
        selectedtabid += "NS6";
        unselectedtabid2 += "NS6";
      }

      // turn off selected tab
      for (var i=0; i<len; i++)
      {
        item2 = item.getElementsByTagName('TD')[i];
        //i2uitrace(1,"TD #"+i+" body\n"+item2.innerHTML);

        //i2uitrace(1,"TD #"+i+" class="+item2.className+" classid="+item2.id);
        var saveCurrent = false;
        if (item2.id == "tabSelected" ||
            item2.id == "tabSelectedNS6")
        {
          selectedtabid   = item2.id;
          unselectedtabid = "tabUnSelected";
          item2.id = unselectedtabid;
          saveCurrent = true;
        }
        else
        {
          if (item2.id == "powerTabSelected" ||
              item2.id == "powerTabSelectedNS6")
          {
            selectedtabid   = item2.id;
            unselectedtabid = "powerTabUnSelected";
            item2.id = unselectedtabid;
            saveCurrent = true;
          }
        }

        // find any child anchors
        if (item2.getElementsByTagName('A').length > 0)
        {
          item2 = item2.getElementsByTagName('A')[0];
          if (saveCurrent)
            i2uiCurrentTab = item2;
          if (item2 == tab_element)
          {
            //i2uitrace(1,"found A at "+i+" class="+item2.className+" id="+item2.id);
            located = i;
          }
          else
          {
            //i2uitrace(1,"TD #"+i+" other class="+item2.className+" id="+item2.id);
            if (item2.id == "tabSelected" ||
                item2.id == "tabSelectedNS6" ||
                item2.id == "powerTabSelected" ||
                item2.id == "powerTabSelectedNS6")
            {
              item2.id = unselectedtabid2;
            }
          }
        }
      }

      //i2uitrace(1,"new tab at "+located);

      // now turn on the new tab
      if (located > 0)
      {
        for (i=located-1; i<located+2; i++)
        {
          item2 = item.getElementsByTagName('TD')[i];
          //i2uitrace(1,"TD #"+i+" fix this. class="+item2.className+" id="+item2.id);
          item2.id = selectedtabid;
          if (item2.getElementsByTagName('A').length > 0)
          {
            item2 = item2.getElementsByTagName('A')[0];
            //i2uitrace(1,"TD #"+i+" fix this A.  class="+item2.className+" id="+item2.id);
            item2.id = selectedtabid;
          }
        }
      }
    }

    //i2uitrace(1,document.getElementById(tabset_id).innerHTML);

    // set new description
    item = document.getElementById(tabset_id+"_description");
    if (item != null)
    {
      item.innerHTML = alttext;
    }
  }
}

/*
  Activates a vertical tab in a tabset.

  tabset_id - the id of the tab's tabset
  alttext - the tab description
  tab_element - the name of the tab to activate

  return - none

  Compatibility:  IE, NS6, NS4
*/
function i2uiToggleVerticalTab(tabset_id, alttext, tab_element)
{
  // handle Netscape 4.x
  if (document.layers)
  {
    var item;

    // display new description
    item = document.layers[tabset_id+"_description"];
    if (item != null)
    {
      var text = '<DIV style="color:#ffffff;background-color:#9999cc;text-align:left;position:absolute;top:0;left:0">'+alttext+'</DIV>';
      item.document.open();
      item.document.write(text);
      item.document.close();
    }
  }
  else
  // handle IE and Netscape 6
  {
    if (tab_element.tagName == 'undefined' ||
        tab_element.tagName == null)
    {
      return;
    }

    //i2uitrace(1,"activate="+tab_element.tagName);

    var item = document.getElementById(tabset_id);
    //i2uitrace(1,"item="+item.tagName+"\n"+"TABLE body\n"+item.innerHTML);

    var item3 = item.getElementsByTagName('TBODY')[0];
    //i2uitrace(1,"TBODY body\n"+item3.innerHTML);

    var len;
    var len2 = item3.getElementsByTagName('TR').length;
    //i2uitrace(1,"#TR="+len2);
    if (len2 > 0)
    {
      var item2;
      var located = -1;
      var selectedtabid   = "tabSelected";
      var unselectedtabid = "tabUnSelected";

      for (var j=0; j<len2; j++)
      {
        item = item3.getElementsByTagName('TR')[j];
        //i2uitrace(1,"TR #"+j+" body\n"+item.innerHTML);
        len = item.getElementsByTagName('TD').length;
        //i2uitrace(1,"#TD="+len);

        // turn off selected tab
        for (var i=0; i<len; i++)
        {
          item2 = item.getElementsByTagName('TD')[i];
          //i2uitrace(1,"TD #"+i+" body\n"+item2.innerHTML);
          //i2uitrace(1,"TD #"+i+" class="+item2.className+" classid="+item2.id);
          if (item2.id == "tabSelectedVert")
          {
            item2.id = "tabUnSelectedVert";
          }
          else
          {
            if (item2.id == "tabSelectedVert2")
            {
              item2.id = "tabUnSelectedVert2";
            }
          }

          // find any child anchors
          if (item2.getElementsByTagName('A').length > 0)
          {
            item2 = item2.getElementsByTagName('A')[0];
            if (item2 == tab_element)
            {
              //i2uitrace(1,"found A at "+i);
              located = j;
            }
            else
            {
              //i2uitrace(1,"TD #"+i+" other class="+item2.className+" id="+item2.id);
              if (item2.id == "tabSelectedVert")
              {
                item2.id = "tabUnSelectedVert";
              }
            }
          }
        }
      }

      //i2uitrace(1,"new tab at "+located);

      // now turn on the new tab
      if (located > -1)
      {
        item = tab_element.parentElement;
        if (item != null)
        {
          //i2uitrace(1,"located parent="+item.tagName);
          item = item.parentElement;
        }

        if (item != null)
        {
          item2 = item.getElementsByTagName('TD')[0];
          if (item2 != null)
          {
            //i2uitrace(1,"TD 0 fix this. class="+item2.className+" id="+item2.id);
            item2.id = "tabSelectedVert";
          }
          item2 = item.getElementsByTagName('TD')[1];
          if (item2 != null)
          {
            //i2uitrace(1,"TD 1 fix this. class="+item2.className+" id="+item2.id);
            item2.id = "tabSelectedVert2";
            if (item2.getElementsByTagName('A').length > 0)
            {
              item2 = item2.getElementsByTagName('A')[0];
              //i2uitrace(1,"..TD #"+i+" fix this A.  class="+item2.className+" id="+item2.id+" newclass="+selectedtabid);
              item2.id = "tabSelectedVert";
            }
          }
        }

        item = item3.getElementsByTagName('TR')[located+1];
        if (item != null)
        {
          var len = item.getElementsByTagName('TD').length;
          //i2uitrace(1,"located grandparent="+item.tagName+" with "+len+" TDs");
          for (i=0; i<len; i++)
          {
            item2 = item.getElementsByTagName('TD')[i];
            if (item2 != null)
            {
              //i2uitrace(1,"TD #"+i+" fix this. class="+item2.className+" id="+item2.id);
              item2.id = "tabSelectedVert";
            }
          }
        }
      }
    }

    //i2uitrace(1,document.getElementById(tabset_id).innerHTML);

    // set new description
    if (item != null)
    {
      item = document.getElementById(tabset_id+"_description");
    }
    if (item != null)
    {
      item.innerHTML = alttext;
    }
  }
}

/*
  Loads new content into an addressable area

  id - the id of the element to load the content into
  src - the URL of the new content

  return - none

  Compatibility:  IE, NS6
*/
function i2uiLoadContent(id, src)
{
  if (document.layers)
  {
    //layers and ilayers are too limiting in the content that can be loaded
    // therefore disable the feature altogether
    return;
  }
  else
  {
    var item = document.getElementById(id);
    if (item != null)
    {
      //failing under Netscape 6.  However, if delayed, then it works!
      //item.src = src;
      var cmd = "document.getElementById('"+id+"').src='"+src+"'";
      setTimeout(cmd,50);
    }
  }
}

/*
  Check if table contains special alignment row or not.
  This row contains the same contents as the last header row.
  tableid - the table id

  return - true/false

  Compatibility:  IE, NS6
*/
function i2uiCheckForAlignmentRow(tableid)
{
  if (document.layers)
    return false;
  var headeritem    = document.getElementById(tableid+"_header");
  var dataitem      = document.getElementById(tableid+"_data");

  if (headeritem   != null &&
      dataitem     != null &&
      dataitem.rows.length > 0)
  {
    var lastheaderrow = headeritem.rows.length - 1;
    var len = headeritem.rows[lastheaderrow].cells.length;
    for (var i=0; i<len; i++)
    {
      if (headeritem.rows[lastheaderrow].cells[i].innerHTML !=
          dataitem.rows[dataitem.rows.length-1].cells[i].innerHTML)
        return false;
    }
  }
  return true;
}

/*
  Resizes columns of 2 related tables to support scrollable
  rows in one with non-scrollable rows in the other
  (also called freeze pane).
   WARNING: In some cases do not alter last column as doing
            so may try to change overall width of table

  tableid - the table id
  shrink - <tbd>
  copyheader - <tbd>
  slave - <tbd>
  headerheight - desired height of last header row in pixels

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeColumns(tableid, shrink, copyheader, slave, headerheight)
{
  var width = 0;

  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return width;
  }
  var tableitem     = document.getElementById(tableid);
  var headeritem    = document.getElementById(tableid+"_header");
  var dataitem      = document.getElementById(tableid+"_data");
  var scrolleritem  = document.getElementById(tableid+"_scroller");
  var scrolleritem2 = document.getElementById(tableid+"_header_scroller");

  if (tableitem    != null &&
      headeritem   != null &&
      dataitem     != null &&
      scrolleritem != null &&
      dataitem.rows.length > 0)
  {
    var lastheaderrow = headeritem.rows.length - 1;
    var len = headeritem.rows[lastheaderrow].cells.length;
    var len2 = len;

    //i2uitrace(1,"ResizeColumns entry scroller width="+scrolleritem.offsetWidth);

    if (headerheight != null && document.all && len > 1)
      return i2uiResizeColumnsWithFixedHeaderHeight(tableid, headerheight, slave);

    // check first if resize needed
    //i2uitrace(1,"check alignment in ResizeColumns");
    if (i2uiCheckAlignment(tableid))
    {
      //i2uitrace(1,"skip alignment in ResizeColumns");
      return headeritem.clientWidth;
    }

    // insert a new row which is the same as the header row
    // forces very nice alignment whenever scrolling rows alone
    if (copyheader == null || copyheader == 1)
    {
      //i2uitrace(1,"pre copy header into data for "+tableid);
      //i2uitrace(1,"data: width="+dataitem.width+" style.width="+dataitem.style.width+" client="+dataitem.clientWidth+" scroll="+dataitem.scrollWidth+" offset="+dataitem.offsetWidth);
      //i2uitrace(1,"header: width="+headeritem.width+" style.width="+headeritem.style.width+" client="+headeritem.clientWidth+" scroll="+headeritem.scrollWidth+" offset="+headeritem.offsetWidth);
      //i2uitrace(1,"scroller: width="+scrolleritem.width+" style.width="+scrolleritem.style.width+" client="+scrolleritem.clientWidth+" scroll="+scrolleritem.scrollWidth+" offset="+scrolleritem.offsetWidth);

      //i2uitrace(1,"check for bypass");
      for (var i=0; i<len; i++)
      {
        if (headeritem.rows[lastheaderrow].cells[i].innerHTML !=
            dataitem.rows[dataitem.rows.length-1].cells[i].innerHTML)
          break;
      }
      if (i != len)
      {
        //i2uitrace(1,"insert fake row table=["+tableid+"]");
        if (document.all)
        {
          // this is the IE approach
          var newrow = dataitem.insertRow();

          //i2uitrace(1,"newrow.className="+newrow.className);
          newrow.className = "tableColumnHeadings";

          //i2uitrace(1,"newrow="+newrow);
          var newcell;
          if (newrow != null)
          {
            for (var i=0; i<len; i++)
            {
              newcell = newrow.insertCell();
              if (newcell != null)
                newcell.innerHTML = headeritem.rows[lastheaderrow].cells[i].innerHTML;
            }
          }
        }
        else
        {
          // this is the DOM api approach
          var newrow = document.createElement('tr');
          if (newrow != null)
          {
            dataitem.appendChild(newrow);
            var newcell;
            for (var i=0; i<len; i++)
            {
              newcell = document.createElement('td');
              newrow.appendChild(newcell);
              if (newcell != null)
              {
                newcell.innerHTML = headeritem.rows[lastheaderrow].cells[i].innerHTML;
              }
            }
          }
        }
      }

      //i2uitrace(1,"ResizeColumns post copy scroller width="+scrolleritem.offsetWidth);
      //i2uitrace(1,"post copy header into data for "+tableid);
      //i2uitrace(1,"data: width="+dataitem.width+" style.width="+dataitem.style.width+" client="+dataitem.clientWidth+" scroll="+dataitem.scrollWidth+" offset="+dataitem.offsetWidth);
      //i2uitrace(1,"header: width="+headeritem.width+" style.width="+headeritem.style.width+" client="+headeritem.clientWidth+" scroll="+headeritem.scrollWidth+" offset="+headeritem.offsetWidth);
      //i2uitrace(1,"scroller: width="+scrolleritem.width+" style.width="+scrolleritem.style.width+" client="+scrolleritem.clientWidth+" scroll="+scrolleritem.scrollWidth+" offset="+scrolleritem.offsetWidth);

      var newwidth;
      newwidth = dataitem.scrollWidth;

      //i2uitrace(1,"assigned width="+newwidth);
      dataitem.width         = newwidth;
      dataitem.style.width   = newwidth;
      headeritem.width       = newwidth;
      headeritem.style.width = newwidth;

      //i2uitrace(1,"post static width for "+tableid);
      //i2uitrace(1,"data: width="+dataitem.width+" style.width="+dataitem.style.width+" client="+dataitem.clientWidth+" scroll="+dataitem.scrollWidth+" offset="+dataitem.offsetWidth);
      //i2uitrace(1,"header: width="+headeritem.width+" style.width="+headeritem.style.width+" client="+headeritem.clientWidth+" scroll="+headeritem.scrollWidth+" offset="+headeritem.offsetWidth);
      //i2uitrace(1,"scroller: width="+scrolleritem.width+" style.width="+scrolleritem.style.width+" client="+scrolleritem.clientWidth+" scroll="+scrolleritem.scrollWidth+" offset="+scrolleritem.offsetWidth);

      // if processing master table, delete newly insert row and return
      if (scrolleritem2 != null && (slave == null || slave == 0))
      {
        //for (i=0; i<len; i++)
        //  i2uitrace(1,"check5 cell #"+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
        //i2uitrace(1,"check5 header="+headeritem.clientWidth+" data="+dataitem.clientWidth);

        //i2uitrace(1,"delete/hide fake row table="+tableid);
        var lastrow = dataitem.rows[dataitem.rows.length - 1];
        dataitem.deleteRow(dataitem.rows.length - 1);

        //for (i=0; i<len; i++)
        //  i2uitrace(1,"check6 cell #"+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
        //i2uitrace(1,"check6 header="+headeritem.clientWidth+" data="+dataitem.clientWidth);

        // size header columns to match data columns
        for (i=0; i<len; i++)
        {
          headeritem.rows[lastheaderrow].cells[i].style.width = dataitem.rows[0].cells[i].clientWidth;
          headeritem.rows[lastheaderrow].cells[i].width = dataitem.rows[0].cells[i].clientWidth;
        }

        //for (i=0; i<len; i++)
        //  i2uitrace(1,"check7 cell #"+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
        //i2uitrace(1,"check7 header="+headeritem.clientWidth+" data="+dataitem.clientWidth);

        //i2uitrace(1,"post hide width for "+tableid);
        //i2uitrace(1,"data: width="+dataitem.width+" style.width="+dataitem.style.width+" client="+dataitem.clientWidth+" scroll="+dataitem.scrollWidth+" offset="+dataitem.offsetWidth);
        //i2uitrace(1,"header: width="+headeritem.width+" style.width="+headeritem.style.width+" client="+headeritem.clientWidth+" scroll="+headeritem.scrollWidth+" offset="+headeritem.offsetWidth);
        //i2uitrace(1,"scroller: width="+scrolleritem.width+" style.width="+scrolleritem.style.width+" client="+scrolleritem.clientWidth+" scroll="+scrolleritem.scrollWidth+" offset="+scrolleritem.offsetWidth);

        //i2uitrace(1,"check alignment after ResizeColumns short version="+i2uiCheckAlignment(tableid));
        return newwidth;
      }
    }

    //i2uitrace(1,"$$$$$$2 scroller: width="+scrolleritem.offsetWidth);
    //i2uitrace(1,"id="+tableid+" parent is "+tableitem.parentNode.tagName+" owner height="+tableitem.parentNode.offsetHeight);
    //i2uitrace(1,"id="+tableid+" parent is "+tableitem.parentElement.tagName+" owner height="+tableitem.parentElement.clientHeight);
    // may need to adjust width of header to accomodate scroller
    //i2uitrace(1,"data height="+dataitem.clientHeight+" scroller height="+scrolleritem.clientHeight+" header height="+headeritem.clientHeight);
    //i2uitrace(1,"data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth);

    // if horizontal scrolling and scroller needed
    var adjust;
    if ((scrolleritem2 != null &&
         scrolleritem2.clientWidth < headeritem.clientWidth) ||
        headeritem.rows[lastheaderrow].cells.length < 3)
    {
      adjust = 0;
    }
    else
    {
      //i2uitrace(1,"header cellpadding="+headeritem.cellPadding);
      adjust = headeritem.cellPadding * 2;

      // do not alter last column unless it contains a treecell
      if(dataitem.rows[0].cells[len-1].id.indexOf("TREECELL_") == -1)
        len--;
    }
    //i2uitrace(1,"adjust="+adjust+" len="+len+" headeritem #cols="+headeritem.rows[0].cells.length);

    // if window has scroller, must fix header
    // but not if scrolling in both directions
    //i2uitrace(1,"resizecolumns scrolleritem.style.overflowY="+scrolleritem.style.overflowY);
    //i2uitrace(1,"resizecolumns scrolleritem2="+scrolleritem2);
    if(scrolleritem.style.overflowY == 'scroll' ||
       scrolleritem.style.overflowY == 'hidden')
    {
      headeritem.width       = dataitem.clientWidth;
      headeritem.style.width = dataitem.clientWidth;
      dataitem.width         = dataitem.clientWidth;
      dataitem.style.width   = dataitem.clientWidth;
      //i2uitrace(1,"fixed header and data widths");
    }
    else
    {
      if(scrolleritem2 != null)
      {
        headeritem.width       = dataitem.clientWidth;
        headeritem.style.width = dataitem.clientWidth;
        //i2uitrace(1,"fixed header2 shrink="+shrink+" copyheader="+copyheader);
        shrink = 0;
      }
    }

    //i2uitrace(1,"$$$$$$3 scroller: width="+scrolleritem.offsetWidth);

    if (document.body.clientWidth < headeritem.clientWidth)
    {
      len++;
      adjust = 0;
      //i2uitrace(1,"new adjust="+adjust+" new len="+len);
    }

    //i2uitrace(1,"$$$$$$4 scroller: width="+scrolleritem.offsetWidth);

    // shrink each column first
    // note: this may cause the contents to wrap
    // even if nowrap="yes"is specified
    if (shrink != null && shrink == 1)
    {
      dataitem.style.width = 5 * dataitem.rows[0].cells.length;
      headeritem.style.width = 5 * dataitem.rows[0].cells.length;

      //i2uitrace(1,"pre shrink");
      for (i=0; i<headeritem.rows[lastheaderrow].cells.length-1; i++)
      {
        headeritem.rows[lastheaderrow].cells[i].width = 15;
        dataitem.rows[0].cells[i].width = 15;
      }
      //i2uitrace(1,"post shrink");
    }

    //i2uitrace(1,"$$$$$$5 scroller: width="+scrolleritem.offsetWidth);

    // resize each column to max width between the tables
    // NOTE: the computed max may not equal either of the
    //       current widths due to the adjustment
    len = Math.min(len, headeritem.rows[0].cells.length);

    //i2uitrace(1,"start data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth);

    var w1, w2, w3;
    for (var i=0; i<len; i++)
    {
      w1 = headeritem.rows[lastheaderrow].cells[i].clientWidth;
      w2 = dataitem.rows[0].cells[i].clientWidth;
      w3 = Math.max(w1,w2) - adjust;

      //i2uitrace(1,"pre i="+i+" w1="+w1+" w2="+w2+" w3="+w3);
      //i2uitrace(1,"data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth);
      if (w1 != w3)
      {
        headeritem.rows[lastheaderrow].cells[i].width = w3;
      }
      if (w2 != w3)
      {
        dataitem.rows[0].cells[i].width = w3;
      }
      //i2uitrace(1,"post i="+i+" w1="+headeritem.rows[0].cells[i].clientWidth+" w2="+dataitem.rows[0].cells[i].clientWidth+" w3="+w3);
      //i2uitrace(1,"data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth);

      // try to handle tables that need window scrollers
      if (headeritem.clientWidth != dataitem.clientWidth)
      {
        //i2uitrace(1,"whoa things moved. table="+tableid+" i="+i+" len="+len);

        // if window has scroller, resize to window unless slave table
        if (document.body.scrollWidth != document.body.offsetWidth)
        {
          //i2uitrace(1,"window has scroller! slave="+slave);
          if (slave == null || slave == 0)
          {
            dataitem.width = document.body.scrollWidth;
            dataitem.style.width = document.body.scrollWidth;
          }
        }

        //i2uitrace(1,"data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth+" doc offset="+document.body.clientWidth+" doc scroll="+document.body.scrollWidth);
        if (headeritem.clientWidth < dataitem.clientWidth)
        {
          headeritem.width = dataitem.clientWidth;
          headeritem.style.width = dataitem.clientWidth;
        }
        else
        {
          dataitem.width = headeritem.clientWidth;
          dataitem.style.width = headeritem.clientWidth;
        }
      }
    }
    //i2uitrace(1,"$$$$$$6 scroller: width="+scrolleritem.offsetWidth);

    // now delete the fake row
    if (copyheader == null || copyheader == 1)
    {
      //i2uitrace(1,"pre delete row - data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth+" doc offset="+document.body.clientWidth+" doc scroll="+document.body.scrollWidth);

      if (scrolleritem2 == null && (slave == null || slave == 0))
      {
        if (document.all)
        {
          dataitem.deleteRow(dataitem.rows.length-1);
        }
      }

      //i2uitrace(1,"post delete row - data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth+" doc offset="+document.body.clientWidth+" doc scroll="+document.body.scrollWidth);
    }

    width = headeritem.clientWidth;

    //i2uitrace(1,"check alignment after ResizeColumns long version");
    //i2uiCheckAlignment(tableid);
  }
  //i2uitrace(1,"WIDTH="+width);
  return width;
}

/*
  Resizes columns of 2 related tables to support scrollable
  rows in one with non-scrollable rows in the other.
  This function handles master table in a synced tabled scenario.

  tableid - the table id
  column1width - desired width of first column. a value of 1 means narrowest possible
  headerheight - desired upper limit for height of last header row in pixels

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeMasterColumns(tableid, column1width, headerheight)
{
  var width = 0;

  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return width;
  }

  var tableitem     = document.getElementById(tableid);
  var headeritem    = document.getElementById(tableid+"_header");
  var dataitem      = document.getElementById(tableid+"_data");
  var scrolleritem  = document.getElementById(tableid+"_scroller");
  var scrolleritem2 = document.getElementById(tableid+"_header_scroller");

  if (tableitem    != null &&
      headeritem   != null &&
      dataitem     != null &&
      scrolleritem != null &&
      dataitem.rows.length > 0)
  {
    //i2uitrace(1,"i2uiResizeMasterColumns id="+tableid+" parent is "+tableitem.parentElement.tagName+" owner height="+tableitem.parentElement.clientHeight);
    var scrollerWidth = scrolleritem.clientWidth;
    var headeritemcw = headeritem.clientWidth;
    var dataitemcw = dataitem.clientWidth;

    var len, i, w1, w2, w3, adjust, len2;
    var lastheaderrow = headeritem.rows.length - 1;
    len = headeritem.rows[lastheaderrow].cells.length;
    len2 = len;

    // if horizontal scrolling and scroller needed
    if (scrolleritem2 != null &&
        scrolleritem2.clientWidth < headeritem.clientWidth)
    {
      adjust = 0;
      len--;
    }
    else
    {
      adjust = headeritem.cellPadding * 2;
      // do not alter last column
      len--;
    }

    //i2uitrace(1,"adjust="+adjust);

    if (headerheight != null && document.all)
    {
      i2uiResizeColumnsWithFixedHeaderHeight(tableid, headerheight);
    }
    else
    {
      dataitem.style.width = 5 * dataitem.rows[0].cells.length;
      headeritem.style.width = 5 * headeritem.rows[lastheaderrow].cells.length;

      /* -- start -- */
      /*
        Rauli
        Don't shrink columns if there is no need.
        This will prevent one row tables from wrapping.
      */

      if( scrollerWidth >= dataitemcw && scrollerWidth >= headeritemcw ){
        for ( i = 0; i < len; ++i ){
          w1 = headeritem.rows[ lastheaderrow ].cells[ i ].clientWidth;
          w2 = dataitem.rows[ 0 ].cells[ i ].clientWidth;
          w3 = Math.max( w1,w2 );
          if( w1 < w3 )
            headeritem.rows[ lastheaderrow ].cells[ i ].width = w3;
          if( w2 < w3 )
            dataitem.rows[ 0 ].cells[ i ].width = w3;
        }
      }
      if( scrollerWidth < dataitemcw|| scrollerWidth < headeritemcw ){
        // shrink each column first
        // note: this may cause the contents to wrap
        // even if nowrap="yes"is specified
        //i2uitrace(1,"pre shrink");
        for (i=0; i<len; i++)
        {
          headeritem.rows[lastheaderrow].cells[i].removeAttribute("width");
          dataitem.rows[0].cells[i].removeAttribute("width");
        }
      }
      /* -- end -- */
      //i2uitrace(1,"post shrink. adjust="+adjust);

    len = headeritem.rows[lastheaderrow].cells.length;

    // resize each column to max width between the tables
    // NOTE: the computed max may not equal either of the
    //       current widths due to the adjustment
    for (i=0; i<len; i++)
    {
      w1 = headeritem.rows[lastheaderrow].cells[i].clientWidth;
      w2 = dataitem.rows[0].cells[i].clientWidth;
      w3 = Math.max(w1,w2) - adjust;
      //i2uitrace(1,"pre i="+i+" header="+w1+" data="+w2+" max="+w3);
      //i2uitrace(1,"data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth);

      if (w1 != w3)
      {
        headeritem.rows[lastheaderrow].cells[i].width = w3;
      }
      if (w2 != w3)
      {
        dataitem.rows[0].cells[i].width = w3;
      }
      //i2uitrace(1,"post i="+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
      //i2uitrace(1,"data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth);
    }
    //i2uitrace(1,"end data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth+" doc offset="+document.body.clientWidth+" doc scroll="+document.body.scrollWidth);

    //for (i=0; i<len2; i++)
    //  i2uitrace(1,"check1 cell #"+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
    //i2uitrace(1,"check1 header="+headeritem.clientWidth+" data="+dataitem.clientWidth);
    }

    if (dataitem.clientWidth > headeritem.clientWidth)
    {
      headeritem.style.width = dataitem.clientWidth;
      //i2uitrace(1,"header width set to data width");
    }
    else
    if (dataitem.clientWidth < headeritem.clientWidth)
    {
      dataitem.style.width = headeritem.clientWidth;
      //i2uitrace(1,"data width set to header width");
    }

    //for (i=0; i<len2; i++)
    //  i2uitrace(1,"check2 cell #"+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
    //i2uitrace(1,"check2 header="+headeritem.clientWidth+" data="+dataitem.clientWidth+" scroller="+scrolleritem.clientWidth);

    /* -- start -- */
    /*
      Rauli
      Stretch columns to max width using same ratio as they have with current with.
      After this data and header columns will.have same sizes and 250 ms timer
      for resizing columns is not needed. Timer based resizing sometimes gives columns
      too big sizes and results in unnecessary scrollbars.
    */
    var available = scrolleritem.clientWidth - dataitem.clientWidth - 1;
    if( available > 0 ){
      var cells = dataitem.rows[ 0 ].cells;
      // set columns with width 0 to 15 as shrink as would have done.
      for( i = 0; i < len; ++i ){
        var cell = cells[ i ];
        if( cell.clientWidth <= 0 ){
          cell.width = 15;
          available = scrolleritem.clientWidth - dataitem.clientWidth - 1;
        }
      }
      if( available > 0 ){
        w1 = 0;
        for( i = 0; i < len; ++i )
          w1 += cells[ i ].clientWidth;
        for( i = 0; i < len; ++i ){
          var cell = cells[ i ];
          w2 = cell.clientWidth;
          w3 = w2 * available / w1;
          cell.width = w2 + w3;
        }
        available = scrolleritem.clientWidth - dataitem.clientWidth - 1;
        if( available > 0 ){
          var cell = cells[ len - 1 ];
          cell.width = cell.clientWidth + available;
        }
      }
    }
    /* -- end -- */

    if( scrolleritem.clientWidth > dataitem.clientWidth + 1 )
    {
      dataitem.style.width = scrolleritem.clientWidth;
      headeritem.style.width = scrolleritem.clientWidth;
      //i2uitrace(1,"both widths set to scroller width");
    }
    //for (i=0; i<len2; i++)
    //  i2uitrace(1,"check3 cell #"+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
    //i2uitrace(1,"check3 header="+headeritem.clientWidth+" data="+dataitem.clientWidth);

    // one last alignment
    if (!i2uiCheckAlignment(tableid))
    for (i=0; i<len; i++)
    {
      w1 = headeritem.rows[lastheaderrow].cells[i].clientWidth;
      w2 = dataitem.rows[0].cells[i].clientWidth;
      w3 = Math.max(w1,w2) - adjust;
      if (w1 != w3)
        headeritem.rows[lastheaderrow].cells[i].width = w3;
      if (w2 != w3)
        dataitem.rows[0].cells[i].width = w3;
      //LPMremoved
      //if (i2uitracelevel == -1)
      //  dataitem.rows[0].cells[i].width = w1;
      //i2uitrace(1,"post i="+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
    }
    //for (i=0; i<len2; i++)
    //  i2uitrace(1,"check4 cell #"+i+" header="+headeritem.rows[lastheaderrow].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
    //i2uitrace(1,"check4 header="+headeritem.clientWidth+" data="+dataitem.clientWidth);

    //i2uitrace(1,"FINAL data width="+dataitem.clientWidth+" scroller width="+scrolleritem.clientWidth+" header width="+headeritem.clientWidth+" doc offset="+document.body.clientWidth+" doc scroll="+document.body.scrollWidth);

    // if column 1 has a fixed width
    if (column1width != null && column1width > 0 && len > 1)
    {
      // assign both first columns to desired width
      headeritem.rows[lastheaderrow].cells[0].width = column1width;
      dataitem.rows[0].cells[0].width               = column1width;

      // assign both first columns to narrowest
      var narrowest = Math.min(dataitem.rows[0].cells[0].clientWidth,
                               headeritem.rows[lastheaderrow].cells[0].clientWidth);

      headeritem.rows[lastheaderrow].cells[0].width = narrowest;
      dataitem.rows[0].cells[0].width               = narrowest;

      // determine the width difference between the first columns
      var spread = Math.abs(dataitem.rows[0].cells[0].clientWidth -
                            headeritem.rows[lastheaderrow].cells[0].clientWidth);

      // determine how much each non-first column should gain
      spread = Math.ceil(spread/(len-1));

      // spread this difference across all non-first columns
      for (i=1; i<len; i++)
      {
        headeritem.rows[lastheaderrow].cells[i].width = headeritem.rows[lastheaderrow].cells[i].clientWidth + spread;
        dataitem.rows[0].cells[i].width               = dataitem.rows[0].cells[i].clientWidth + spread;
      }

      // if desiring abolsute narrowest possible
      if (column1width == 1)
      {
        // if not aligned, take difference and put in last column of data table
        var diff = dataitem.rows[0].cells[0].clientWidth - headeritem.rows[lastheaderrow].cells[0].clientWidth;
        var loop = 0;
        // well, one call may not be enough
        while (diff > 0)
        {
          dataitem.rows[0].cells[len-1].width = dataitem.rows[0].cells[len-1].clientWidth + diff;
          loop++;

          // only try 4 times then stop
          if (loop > 4)
            break;
          diff = dataitem.rows[0].cells[0].clientWidth - headeritem.rows[lastheaderrow].cells[0].clientWidth;
        }
      }
    }

    width = dataitem.clientWidth;
  }
  //i2uitrace(1,"i2uiResizeMasterColumns exit. return width="+width);
  return width;
}

/*
  Resizes columns of scrollable table considering a fixed height for
  the last header row.

  tableid - the table id
  headerheight - desired height of last header row in pixels

  return - width of table

  Compatibility:  IE
*/
function i2uiResizeColumnsWithFixedHeaderHeight(tableid, headerheight, slave)
{
  if (!document.all)
  {
    return 0;
  }

  var headeritem    = document.getElementById(tableid+"_header");
  var dataitem      = document.getElementById(tableid+"_data");
  var scrolleritem  = document.getElementById(tableid+"_scroller");

  if (headeritem   != null &&
      dataitem     != null &&
      scrolleritem != null &&
      dataitem.rows.length > 0)
  {
    //i2uitrace(1,"i2uiResizeColumnsWithFixedHeaderHeight id="+tableid);

    if (headeritem.style.tableLayout != "fixed")
    {
      var newrow = headeritem.insertRow();
      if (newrow != null)
      {
        newrow.className = "tableColumnHeadings";

        var i;
        var lastheaderrow = headeritem.rows.length - 2;
        var len = headeritem.rows[lastheaderrow].cells.length;
        var newcell;
        var attempts;
        var newcellwidth;
        var widths = new Array();
        var overallwidth = len - 1; // initial width is for cell dividers

        //i2uitrace(1,"pre header width="+headeritem.clientWidth);

        // shrink cells if slave table present
        if (slave != null || slave == 1)
        {
          dataitem.style.width = 5 * dataitem.rows[0].cells.length;
          headeritem.style.width = 5 * dataitem.rows[0].cells.length;
        }

        // don't shrink last cell for performance reasons
        /* -- start -- */
        /*
        Ganesh
        The piece of code below makes the rows to shrink irrespective of long text.
        Commenting it to avoid unnecessary wrapping of data rows in a scrollable table
        with fixed height. This wrapping looks bad especially when the table has only
        one data row.

        Uncommenting it since it causes the column width to be very high.
        */
        for (i=0; i<len-1; i++)
        {
          headeritem.rows[lastheaderrow].cells[i].width = 15;
          
          // 08/04/2004 - Weylin
          // Commented out line below that caused column alignment problems (EQ #527190)
          // dataitem.rows[0].cells[i].width = 15;
        }
        /* -- end -- */

        // insert 1 new cell
        newcell = newrow.insertCell();
        if (newcell != null)
        {
          //Ganesh
          /*
          Increasing growth factor value from 100 to 125, since the i2ui team's assumption of 100 for
          growth factor was not enough for some of the cells causing them to wrap up.
          */
          // make some room in the header for the cells to grow
          var growthfactor;
          if (scrolleritem.offsetWidth > headeritem.clientWidth)
            growthfactor = Math.max(125,scrolleritem.offsetWidth-headeritem.clientWidth);
          else
            growthfactor = 125;

          //i2uitrace(1,"scroller="+scrolleritem.offsetWidth+" client="+headeritem.clientWidth+" growthfactor="+growthfactor);

          headeritem.style.width = headeritem.clientWidth + growthfactor;

          for (i=0; i<len; i++)
          {
            //i2uitrace(1,"cell #"+i+" headerwidth="+headeritem.clientWidth+" newrow width="+newrow.clientWidth);
            newcell.style.width = 15;
            newcell.innerHTML = headeritem.rows[lastheaderrow].cells[i].innerHTML;
            newcellwidth = newcell.clientWidth;
            //i2uitrace(1,"prelimit i="+i+" cell width="+newcellwidth);

            attempts = 0;
            while (attempts < 8 &&
                   newrow.clientHeight > headerheight)
            {
              attempts++;
              newcellwidth = parseInt(newcellwidth * 1.25);
              //i2uitrace(1,"  attempt="+attempts+" width="+newcellwidth);
              newcell.style.width = newcellwidth;
            }
            //i2uitrace(1,"postlimit i="+i+" cell width="+newcell.clientWidth+" attempts="+attempts+" lastcellwidth="+newcellwidth+" rowheight="+newrow.clientHeight);
            widths[i] = Math.max(newcell.clientWidth,
                                 dataitem.rows[0].cells[i].clientWidth);
            overallwidth += widths[i];
          }

          //i2uitrace(1,"post header width="+headeritem.clientWidth+" overall="+overallwidth);

          var spread = parseInt((scrolleritem.clientWidth - overallwidth) / len);
          //i2uitrace(1,"header row height="+headerheight+" header="+headeritem.clientWidth+" data="+dataitem.clientWidth+" scroller="+scrolleritem.clientWidth+" spread="+spread);

          //i2uitrace(1,"pre spread overallwidth="+overallwidth);
          if (spread > 0)
          {
            overallwidth += spread * len;
            for (i=0; i<len; i++)
              widths[i] += spread;
          }
          //i2uitrace(1,"post spread overallwidth="+overallwidth);

          // this solution leverages fixed table layout.  IE uses
          // the width information from the COL tags to determine how
          // wide each column of every row should be.  table rendering is
          // much faster.  more importantly, more deterministic.
          var newitem;
          for (i=0; i<len; i++)
          {
            // create new tag and insert into table
            newitem = document.createElement("<COL width="+widths[i]+">");
            if (newitem != null)
              headeritem.appendChild(newitem);
            newitem = document.createElement("<COL width="+widths[i]+">");
            if (newitem != null)
              dataitem.appendChild(newitem);
          }

          // now that the table has the necessary data, make it fixed layout
          headeritem.style.tableLayout = "fixed";
          dataitem.style.tableLayout = "fixed";

          //apparently not needed
          //headeritem.style.width = overallwidth;
          //dataitem.style.width = overallwidth;

          // make last header row desired height
          headeritem.rows[lastheaderrow].style.height = headerheight;

          // set cell width
          for (i=0; i<len; i++)
          {
            headeritem.rows[lastheaderrow].cells[i].style.width = widths[i];
            dataitem.rows[0].cells[i].style.width = widths[i];
          }
        }
        // delete sizer row
        headeritem.deleteRow(headeritem.rows.length - 1);
      }
    }
  }
  //i2uitrace(1,"returned width="+headeritem.clientWidth);
  return headeritem.clientWidth;
}

/*
  Returns whether table has a horizontal scroller or not.

  tableid - the table id

  return - true if the the table has a scroller and false otherwise

  Compatibility:  IE, NS6, NS4
*/
function i2uiTableHasHorizontalScroller(tableid)
{
  var rc = false;
  var scrolleritem  = document.getElementById(tableid+"_scroller");
  var scrolleritem2 = document.getElementById(tableid+"_header_scroller");
  if (scrolleritem != null && scrolleritem2 != null)
  {
    var adjust = scrolleritem2.clientWidth - scrolleritem.clientWidth;
    if (adjust != 0)
    {
      rc = true;
    }
  }
  return rc;
}


/*
  Manges the visibility of the scroller and special cell for scrollable
  tables in Netscape 6

  return - none

  Compatibility:  NS6
*/
function i2uiManageTableScroller(id,maxheight,newheight)
{
  if (document.layers || document.all)
    return;
  var table_obj    = document.getElementById(id);
  var scroller_obj = document.getElementById(id+"_data");
  if (table_obj != null && scroller_obj != null)
  {
    var len = table_obj.rows[0].cells.length;
    if (len > 1)
    {
      var scrollercell = table_obj.rows[0].cells[len-1];
      if (scrollercell != null && scrollercell.id=="scrollerspacer")
      {
        if (newheight < maxheight)
        {
          if (scroller_obj.style.overflow=="hidden")
          {
            var cmd = "document.getElementById('"+id+"_data').style.overflow='auto'";
            setTimeout(cmd, 50);
          }
          scrollercell.style.display = "";
          scrollercell.style.visibility = "visible";
        }
        else
        {
          scroller_obj.style.overflow="hidden";
          scrollercell.style.display = "none";
        }
      }
    }
  }
}

/*
  Resizes the area for scrollable rows and columns based
  on smaller of specified value and amount currently used.

  mastertableid - the id of the table to resize
  minheight - minimum vertical scroller height
  minwidth -  minimum horizontal scroller width
  slavetableid - the id of the table that is paired with the master table.
    If null, then no synchronized scrolling is defined.
  flag - extra reserved width.  usually the space on the sides of the table.
  slave2width - defines the amount of area taken by the synced table
    which is normally drawn to the left of the master table
  column1width - desired width of first column
  headerheight - desired upper limit for height of last header row in pixels

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeScrollableArea(mastertableid, minheight, minwidth, slavetableid, flag, slave2width, column1width, headerheight)
{

  minwidth = minwidth -1; // sudhir for xp

  /* -- start -- */
  /* Ganesh
    Fixing the header height of all scrollable tables to 25 pixels.
    Fix for header columns wrapping problem in scrollable tables mainly for users with japanese locale.
  */
  //headerheight = 25;
  /* --  end -- */

  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }
  //i2uitrace(1,"RSA height="+minheight+" width="+minwidth+" slave="+slavetableid+" flag="+flag);

  //NS6 handler
  if (!document.all)
  {
    var scroller_obj = document.getElementById(mastertableid+"_data");
    if (scroller_obj != null)
    {
      var newheight;
      var scrollheight = i2uiComputeScrollHeight(mastertableid+"_data");
      var rowcount     = scroller_obj.rows.length;
      var maxheight    = scrollheight+rowcount;
      if (minheight == null)
        newheight = maxheight;
      else
        newheight = Math.max(100, Math.min(minheight, maxheight));

      //needed to force repaint of area
      scroller_obj.style.height = newheight + 1;

      scroller_obj.style.height = newheight;
      i2uiManageTableScroller(mastertableid,maxheight,newheight);
    }
    return;
  }

  var slavetableid2 = null;
  if (slavetableid != null && slavetableid != 'undefined')
  {
    slavetableid2 = slavetableid+"2";
  }

  var tableitem     = document.getElementById(mastertableid);
  var headeritem    = document.getElementById(mastertableid+"_header");
  var dataitem      = document.getElementById(mastertableid+"_data");
  var scrolleritem  = document.getElementById(mastertableid+"_scroller");

  if (tableitem != null &&
      headeritem != null &&
      dataitem != null &&
      scrolleritem != null)
  {
    var s = scrolleritem.style;
    s.overflowX = 'auto';
    if(mastertableid != 'resizabledualsyncslave2')
      s.overflowY = 'auto';
    // make the slave area as narrow as possible
    var slavewidth = 0;  // this is for the margins of the page
    if (flag != null && flag != "undefined")
    {
      if (slave2width != null &&
          slave2width != 'undefined')
      {
        slavewidth = Math.ceil(2 * (flag / 1));
      }
      else
      {
        slavewidth = flag / 1;
      }
    }

    if (slavewidth == 0 &&
        slave2width != null &&
        slave2width != 'undefined')
    {
      slavewidth +=  i2uiScrollerWidth;
    }

    if (window.document.body != null &&
        (window.document.body.scroll == null  ||
         window.document.body.scroll == 'yes' ||
         window.document.body.scroll == 'auto'))
    {
      slavewidth +=  i2uiScrollerWidth;
    }

    if (slavetableid != null && slavetableid != 'undefined')
    {
      //i2uitrace(1,"pre slavetable1 "+slavetableid+" slavewidth="+slavewidth);
      var x = i2uiResizeColumns(slavetableid,1,1,1,headerheight);
      //i2uitrace(1,"post slavetable1 "+slavetableid+" slavewidth="+slavewidth);
      slavewidth += x;
    }

    if (slavetableid2 != null &&
        slavetableid2 != 'undefined' &&
        document.getElementById(slavetableid2) != null)
    {
      var x = i2uiResizeColumns(slavetableid2,1,1,1,headerheight);
      //i2uitrace(1,"slave2width="+x);
      //i2uitrace(1,"slave2width="+slave2width);
      //i2uitrace(1,"slave2width="+document.getElementById(slavetableid2+"_header").clientWidth);
      //i2uitrace(1,"slave2width="+document.getElementById(slavetableid2+"_header").offsetWidth);
      //i2uitrace(1,"slave2width="+document.getElementById(slavetableid2+"_header").scrollWidth);

      //i2uitrace(1,"post slavetable2 "+slavetableid2+" slavewidth="+slavewidth);

      if (slave2width != null && slave2width != 'undefined')
      {
        slavewidth += slave2width;
      }
      else
      {
        slavewidth += x;
      }
      //i2uitrace(1,"post buffer "+slavetableid2+" slavewidth="+slavewidth);
    }

    if (minwidth != null && minwidth != 'undefined')
    {
      var scrolleritem2 = document.getElementById(mastertableid+"_header_scroller");
      if (scrolleritem2 != null)
      {
        var newwidth;
        if (slavetableid != null && slavetableid != 'undefined')
        {
          newwidth  = Math.max(headeritem.clientWidth, dataitem.clientWidth);
          newwidth  = Math.min(newwidth, minwidth);
          newwidth  = Math.max(minwidth, document.body.offsetWidth - slavewidth);
        }
        else
        {
          newwidth = minwidth;
        }

        newwidth = Math.max(1,newwidth);

        //i2uitrace(1,"smarter - minwidth (desired)="+minwidth);

        //i2uitrace(1,"smarter pre - client: header="+headeritem.clientWidth+" data="+dataitem.clientWidth+" scroller="+scrolleritem.clientWidth);
        //i2uitrace(1,"smarter pre - offset: header="+headeritem.offsetWidth+" data="+dataitem.offsetWidth+" scroller="+scrolleritem.offsetWidth);
        //i2uitrace(1,"smarter pre - scroll: header="+headeritem.scrollWidth+" data="+dataitem.scrollWidth+" scroller="+scrolleritem.scrollWidth);

        scrolleritem2.style.width = newwidth;
        scrolleritem2.width       = newwidth;
        scrolleritem.style.width  = newwidth;
        scrolleritem.width        = newwidth;
        tableitem.style.width     = newwidth;
        dataitem.style.width      = newwidth;

        // if scroller present for rows, add extra width to data area
        var adjust = scrolleritem2.clientWidth - scrolleritem.clientWidth;
        //i2uitrace(1,"width adjust="+adjust);
        if (adjust != 0)
        {
          scrolleritem.style.width  = newwidth + adjust;
        }

        //i2uitrace(1,"smarter post - client: header="+headeritem.clientWidth+" data="+dataitem.clientWidth+" scroller="+scrolleritem.clientWidth);
        //i2uitrace(1,"smarter post - offset: header="+headeritem.offsetWidth+" data="+dataitem.offsetWidth+" scroller="+scrolleritem.offsetWidth);
        //i2uitrace(1,"smarter post - scroll: header="+headeritem.scrollWidth+" data="+dataitem.scrollWidth+" scroller="+scrolleritem.scrollWidth);
        //i2uitrace(1,"smarter - newwidth (actual)="+newwidth);
        if (newwidth != scrolleritem.clientWidth)
        {
          newwidth--;
          scrolleritem2.style.width = newwidth;
          scrolleritem.style.width  = newwidth + adjust;
          tableitem.style.width     = newwidth;
          dataitem.style.width      = newwidth;

          //i2uitrace(1,"smarter post3 - client: header="+headeritem.clientWidth+" data="+dataitem.clientWidth+" scroller="+scrolleritem.clientWidth);
          //i2uitrace(1,"smarter post3 - offset: header="+headeritem.offsetWidth+" data="+dataitem.offsetWidth+" scroller="+scrolleritem.offsetWidth);
          //i2uitrace(1,"smarter post3 - scroll: header="+headeritem.scrollWidth+" data="+dataitem.scrollWidth+" scroller="+scrolleritem.scrollWidth);
        }

        // if aligned and scroller present, then skip re-alignment
        // however, doesn't mean table couldn't be more efficient
        var skip = false;
        //i2uitrace(1,"check alignment in ResizeScrollableArea");
        if (headerheight == null &&
            i2uiCheckAlignment(mastertableid) &&
            (headeritem.clientWidth > scrolleritem.clientWidth ||
             dataitem.clientWidth > scrolleritem.clientWidth))
        {
          //i2uitrace(1,"check1 "+scrolleritem.scrollWidth+" != "+scrolleritem.clientWidth);
          //i2uitrace(1,"check2 "+scrolleritem.scrollWidth+" == "+headeritem.scrollWidth);
          //i2uitrace(1,"check3 "+headeritem.scrollWidth+" == "+headeritem.clientWidth);
          //i2uitrace(1,"columns aligned but ResizeTable may be needed");

          skip = true;
          //i2uitrace(1,"skip alignment in ResizeScrollableArea");
        }

        if (!skip)
        {
          // scroll items to far left
          scrolleritem.scrollLeft = 0;
          scrolleritem2.scrollLeft = 0;

          // now resize columns to handle new overall width
          i2uiResizeMasterColumns(mastertableid, column1width, headerheight);

          //i2uitrace(1,"check alignment after ResizeMasterColumns");
          if (!i2uiCheckAlignment(mastertableid))
          {
            //i2uitrace(1,"spawn ResizeColumns");
            var cmd = "i2uiResizeColumns('"+mastertableid+"',1,1)";
            setTimeout(cmd, 250);
          }

          //i2uitrace(1,"post3 header width="+headeritem.clientWidth+" data width="+dataitem.clientWidth+" desired width="+minwidth);
          //i2uitrace(1,"post3 header width="+headeritem.offsetWidth+" data width="+dataitem.offsetWidth+" desired width="+minwidth);
        }
      }
    }

    if (minheight != null &&
        minheight != 'undefined' &&
        (slavetableid == null ||
         slavetableid != 'undefined'))
    {
      var newheight = Math.max(1, Math.min(dataitem.clientHeight, minheight));

      scrolleritem.style.height  = newheight;

      // check if scroller beneath
      var adjust = dataitem.clientHeight - scrolleritem.clientHeight;
      //i2uitrace(1,"setting height. adjust="+adjust);
      if (adjust != 0)
      {
        scrolleritem.style.height = newheight + Math.min( i2uiScrollerWidth,adjust);
      }

      if (slavetableid != null && slavetableid != 'undefined')
      {
        var dataitem2 = document.getElementById(slavetableid+"_data");
        var scrolleritem2 = document.getElementById(slavetableid+"_scroller");
        //i2uitrace(1,"pre data master="+dataitem.clientHeight+" slave="+dataitem2.clientHeight);
        //i2uitrace(1,"pre scroller master="+scrolleritem.clientHeight+" slave="+scrolleritem2.clientHeight);
        if (scrolleritem2 != null &&
            dataitem2 != null)
        {
          scrolleritem2.style.height = newheight;

          var adjust = scrolleritem2.clientHeight - scrolleritem.clientHeight;

          //i2uitrace(1,"post data master="+dataitem.clientHeight+" slave="+dataitem2.clientHeight);
          //i2uitrace(1,"post scroller master="+scrolleritem.clientHeight+" slave="+scrolleritem2.clientHeight);
          //i2uitrace(1,"setting slave height. adjust="+adjust);

          if (adjust != 0)
          {
            scrolleritem2.style.height = newheight - adjust;
          }
        }

        // now fix height of slave2 if defined
        if (slavetableid2 != null && slavetableid2 != 'undefined')
        {
          //i2uitrace(1,"fix slave2");
          var scrolleritem3 = document.getElementById(slavetableid2+"_scroller");
          if (scrolleritem3 != null)
          {
            //i2uitrace(1,"fix scrolleritem3");
            scrolleritem3.style.height = newheight;

            var adjust = scrolleritem3.clientHeight - scrolleritem.clientHeight;

            //i2uitrace(1,"post data master="+dataitem.clientHeight+" slave="+dataitem2.clientHeight);
            //i2uitrace(1,"post scroller master="+scrolleritem.clientHeight+" slave="+scrolleritem2.clientHeight);
            //i2uitrace(1,"adjust="+adjust);

            if (adjust != 0)
            {
              scrolleritem3.style.height = newheight - adjust;
            }
          }
        }
      }
    }

    // now fix each row between the master and slave to be of same height
    if (slavetableid != null && slavetableid != 'undefined')
    {
      //i2uitrace(1,"fix each row");
      var dataitem2 = document.getElementById(slavetableid+"_data");
      if (dataitem2 != null)
      {
        var dataitem3 = null;
        if (slavetableid2 != null && slavetableid2 != 'undefined')
        {
          dataitem3 = document.getElementById(slavetableid2+"_data");
        }

        var len;

        // compute if any row grouping is occuring between slave and master
        var masterRowsPerSlaveRow = 1;
        // fakerow will be 1 if slave has extra row used to align columns
        var fakerow = 0;
        // if 2 slave tables
        if (dataitem3 != null)
        {
          //if (dataitem.rows.length % dataitem3.rows.length > 0)
          if (i2uiCheckForAlignmentRow(slavetableid2))
          {
            fakerow = 1;
          }
          masterRowsPerSlaveRow = Math.max(1,parseInt(dataitem.rows.length / (dataitem3.rows.length - fakerow)));
        }
        else
        {
          //if (dataitem.rows.length % dataitem2.rows.length > 0)
          if (i2uiCheckForAlignmentRow(slavetableid))
          {
            fakerow = 1;
          }
          masterRowsPerSlaveRow = Math.max(1,parseInt(dataitem.rows.length / (dataitem2.rows.length - fakerow)));
        }
        //i2uitrace(0, "table "+mastertableid+" masterRows="+dataitem.rows.length+" slaveRows="+dataitem2.rows.length);
        //i2uitrace(0, "table "+mastertableid+" masterRowsPerSlaveRow="+masterRowsPerSlaveRow);

        // if 2 slave tables and masterRowsPerSlaveRow is not 1,
        // align master and inner slave first row for row
        if (dataitem3 != null && masterRowsPerSlaveRow > 1)
        {
          len = dataitem.rows.length;
          //i2uitrace(1, "fix "+len+" rows between master and inner slave");
          for (var i=0; i<len; i++)
          {
            var h1 = dataitem.rows[i].clientHeight;
            var h2 = dataitem2.rows[i].clientHeight;
            if (h1 != h2)
            {
              var h3 = Math.max(h1,h2);
              if (h1 != h3)
                dataitem.rows[i].style.height  = h3;
              else
              if (h2 != h3)
                dataitem2.rows[i].style.height = h3;
            }
          }
        }

        // align master with sole slave or
        // both slaves when masterRowsPerSlaveRow is 1
        len = dataitem.rows.length / masterRowsPerSlaveRow;
        //i2uitrace(1, "fix "+len+" rows between master and outer slave");
        var j;
        var delta;
        var remainder;
        var k;
        for (var i=0; i<len; i++)
        {
          j = i * masterRowsPerSlaveRow;
          var h1 = dataitem.rows[j].clientHeight;
          for (k=1; k<masterRowsPerSlaveRow; k++)
          {
            h1 += dataitem.rows[j+k].clientHeight;
            h1++;
          }

          var h2 = dataitem2.rows[i].clientHeight;
          var h3 = Math.max(h1,h2);

          //i2uitrace(1, "row i="+i+" j="+j+" h1="+h1+" h2="+h2+" h3="+h3);

          if (masterRowsPerSlaveRow == 1)
          {
            if (dataitem3 != null && dataitem3.rows[i] != null)
            {
              h3 = Math.max(h3, dataitem3.rows[i].clientHeight);
              //i2uitrace(1, "   new h3="+h3);
              if (dataitem3.rows[i].clientHeight != h3)
                dataitem3.rows[i].style.height = h3;
            }
            if (dataitem.rows[i].clientHeight != h3)
              dataitem.rows[i].style.height = h3;
            if (dataitem2.rows[i].clientHeight != h3)
              dataitem2.rows[i].style.height = h3;
          }
          else
          {
            if (dataitem3 != null)
            {
              // if combined height is more than outer slave
              if (h3 > dataitem3.rows[i].clientHeight)
              {
                //i2uitrace(1, "   new h3="+h3);
                dataitem3.rows[i].style.height = h3;
              }
              else
              {
                // increase height of last row in group
                //k = j + masterRowsPerSlaveRow - 1;
                //delta = h3 - h1;
                //dataitem.rows[k].style.height  = dataitem.rows[k].clientHeight + delta;
                //dataitem2.rows[k].style.height = dataitem2.rows[k].clientHeight + delta;

                // increase height of each row in group
                // any remainder is added to last row in group
                k = j + masterRowsPerSlaveRow - 1;
                delta = dataitem3.rows[i].clientHeight - h1;
                remainder = delta % masterRowsPerSlaveRow;
                delta = parseInt(delta / masterRowsPerSlaveRow);
                //i2uitrace(1,"i="+i+" j="+j+" k="+k+" summed="+h1+" slave="+dataitem3.rows[i].clientHeight+" delta="+delta+" remainder="+remainder);
                for (k=0; k<masterRowsPerSlaveRow-1; k++)
                {
                  //x = j+k;
                  //i2uitrace(1,"change inner row "+x+" by "+delta);
                  dataitem.rows[j+k].style.height  = dataitem.rows[j+k].clientHeight + delta;
                  dataitem2.rows[j+k].style.height = dataitem2.rows[j+k].clientHeight + delta;
                }
                k = j + masterRowsPerSlaveRow - 1;
                //x = delta+remainder;
                //i2uitrace(1,"change final row "+k+" by "+x);
                dataitem.rows[k].style.height  = dataitem.rows[k].clientHeight + delta + remainder;
                dataitem2.rows[k].style.height = dataitem2.rows[k].clientHeight + delta + remainder;
              }
            }
            else
            {
              // if combined height is more than slave
              if (h3 > dataitem2.rows[i].clientHeight)
              {
                dataitem2.rows[i].style.height = h3;
              }
              else
              {
                // increase height of last row in group
                //k = j + masterRowsPerSlaveRow - 1;
                //delta = Math.max(0,dataitem2.rows[i].clientHeight - h1);
                //dataitem.rows[k].style.height += delta;

                // increase height of each row in group
                // any remainder is added to last row in group
                k = j + masterRowsPerSlaveRow - 1;
                delta = dataitem2.rows[i].clientHeight - h1;
                remainder = delta % masterRowsPerSlaveRow;
                delta = parseInt(delta / masterRowsPerSlaveRow);
                //i2uitrace(1,"i="+i+" j="+j+" k="+k+" master="+h1+" slave="+dataitem2.rows[i].clientHeight+" delta="+delta+" remainder="+remainder);
                for (k=0; k<masterRowsPerSlaveRow-1; k++)
                {
                  //x = j+k;
                  //i2uitrace(1,"change inner row "+x+" by "+delta);
                  dataitem.rows[j+k].style.height  = dataitem.rows[j+k].clientHeight + delta;
                }
                k = j + masterRowsPerSlaveRow - 1;
                //x = delta +remainder;
                //i2uitrace(1,"change final row "+k+" by "+x);
                dataitem.rows[k].style.height  = dataitem.rows[k].clientHeight + delta + remainder;
              }
            }
          }
        }
        //i2uitrace(1,"row heights all fixed");

        // set minheight after resizing each row
        if (minheight != null && minheight != 'undefined')
        {
          var newheight = Math.max(1, Math.min(Math.max(dataitem.clientHeight,dataitem2.clientHeight), minheight));

          scrolleritem.style.height  = newheight;

          // check if scroller beneath
          var adjust = dataitem.clientHeight - scrolleritem.clientHeight;
          //i2uitrace(1,"setting height. adjust="+adjust);
          if (adjust != 0)
          {
            scrolleritem.style.height = newheight + Math.min( i2uiScrollerWidth,adjust);
          }

          if (slavetableid != null && slavetableid != 'undefined')
          {
            var dataitem2 = document.getElementById(slavetableid+"_data");
            var scrolleritem2 = document.getElementById(slavetableid+"_scroller");
            //i2uitrace(1,"pre data master="+dataitem.clientHeight+" slave="+dataitem2.clientHeight);
            //i2uitrace(1,"pre scroller master="+scrolleritem.clientHeight+" slave="+scrolleritem2.clientHeight);

            scrolleritem2.style.height = newheight;

            var adjust = scrolleritem2.clientHeight - scrolleritem.clientHeight;

            //i2uitrace(1,"post data master="+dataitem.clientHeight+" slave="+dataitem2.clientHeight);
            //i2uitrace(1,"post scroller master="+scrolleritem.clientHeight+" slave="+scrolleritem2.clientHeight);
            //i2uitrace(1,"setting slave height. adjust="+adjust);

            if (adjust != 0)
            {
              scrolleritem2.style.height = newheight - adjust;
            }

            // now fix height of slave2 if defined
            if (slavetableid2 != null && slavetableid2 != 'undefined')
            {
              //i2uitrace(1,"fix slave2");
              var scrolleritem3 = document.getElementById(slavetableid2+"_scroller");
              if (scrolleritem3 != null)
              {
                //i2uitrace(1,"fix scrolleritem3");
                scrolleritem3.style.height = newheight;

                var adjust = scrolleritem3.clientHeight - scrolleritem.clientHeight;

                //i2uitrace(1,"post data master="+dataitem.clientHeight+" slave="+dataitem2.clientHeight);
                //i2uitrace(1,"post scroller master="+scrolleritem.clientHeight+" slave="+scrolleritem2.clientHeight);
                //i2uitrace(1,"adjust="+adjust);

                if (adjust != 0)
                {
                  scrolleritem3.style.height = newheight - adjust;
                }
              }
            }
          }
        }
      }

      var headeritem2 = document.getElementById(slavetableid+"_header");
      if (headeritem2 != null)
      {
        var headeritem3 = null;
        if (slavetableid2 != null && slavetableid2 != 'undefined')
        {
          headeritem3 = document.getElementById(slavetableid2+"_header");
        }

        var h1, h2, h3;
        var headerRows = headeritem.rows.length;

        if (headerRows == 1)
        {
          h1 = headeritem.rows[0].clientHeight;
        }
        else
        {
          h1 = -1;
          for (var k=0; k<headerRows; k++)
          {
            h1 += headeritem.rows[k].clientHeight;
            h1++;
          }
        }
        h2 = headeritem2.rows[0].clientHeight;
        h3 = Math.max(h1,h2);
        //i2uitrace(1,"fix row height for headers. h1="+h1+" h2="+h2+" h3="+h3);
        if (headeritem3 != null)
        {
          var h4 = headeritem3.rows[0].clientHeight;
          var h5 = Math.max(h3,h4);
          if (headerRows == 1)
          {
            headeritem.rows[0].style.height = h5;
          }
          headeritem2.rows[0].style.height = h5;
          headeritem3.rows[0].style.height = h5;
        }
        else
        {
          if (headerRows == 1)
          {
            headeritem.rows[0].style.height = h3;
          }
          headeritem2.rows[0].style.height = h3;
        }
        //i2uitrace(1,"post fix row height for headers. master="+headeritem.rows[0].clientHeight+" slave="+headeritem2.rows[0].clientHeight);
      }
    }
  }
}


/*
  Resizes the width for scrollable table to a minimum size

  tableid - the id of the table to resize

  return - none

  Compatibility:  IE, NS6
*/
function i2uiShrinkScrollableTable(tableid)
{
  if (document.layers || tableid == null)
    return;

  var tableitem     = document.getElementById(tableid);
  var headeritem    = document.getElementById(tableid+"_header");
  var dataitem      = document.getElementById(tableid+"_data");
  var scrolleritem  = document.getElementById(tableid+"_scroller");

  if (tableitem != null &&
      headeritem != null &&
      dataitem != null &&
      scrolleritem != null)
  {
    var newwidth = 100;
    //i2uitrace(1,"==========");
    //i2uitrace(1,"tableitem clientwidth="+tableitem.clientWidth);
    //i2uitrace(1,"tableitem offsetwidth="+tableitem.offsetWidth);
    //i2uitrace(1,"tableitem scrollwidth="+tableitem.scrollWidth);
    //i2uitrace(1,"headeritem clientwidth="+headeritem.clientWidth);
    //i2uitrace(1,"headeritem offsetwidth="+headeritem.offsetWidth);
    //i2uitrace(1,"headeritem scrollwidth="+headeritem.scrollWidth);
    //i2uitrace(1,"dataitem clientwidth="+dataitem.clientWidth);
    //i2uitrace(1,"dataitem offsetwidth="+dataitem.offsetWidth);
    //i2uitrace(1,"dataitem scrollwidth="+dataitem.scrollWidth);
    var scrolleritem2 = document.getElementById(tableid+"_header_scroller");
    if (scrolleritem2 != null)
    {
      scrolleritem2.style.width = newwidth;
      scrolleritem2.width       = newwidth;
    }
    scrolleritem.style.width  = newwidth;
    scrolleritem.width        = newwidth;
    tableitem.style.width     = newwidth;
    tableitem.width     = newwidth;
    dataitem.style.width      = newwidth;
    dataitem.width      = newwidth;
  }
}


/*
  Returns whether table columns are aligned.

  tableid - the table id

  return - true if columns are aligned and false if they are not

  Compatibility:  IE, NS6, NS4
*/
function i2uiCheckAlignment(tableid)
{
  var rc = true;
  var headeritem    = document.getElementById(tableid+"_header");
  var dataitem      = document.getElementById(tableid+"_data");
  //i2uitrace(1,"checkAlignment headeritem="+headeritem);
  //i2uitrace(1,"checkAlignment dataitem="+dataitem);
  if (headeritem != null &&
      dataitem != null)
  {
    // dump element
    //i2uitrace(1,"checkAlignment cell 0 nodetype="+headeritem.rows[0].cells[0].nodeType);
    //i2uitrace(1,"checkAlignment cell 0 nodename="+headeritem.rows[0].cells[0].nodeName);
    //i2uitrace(1,"checkAlignment cell 0 nodevalue="+headeritem.rows[0].cells[0].nodeValue);
    //i2uitrace(1,"checkAlignment cell 0 width="+headeritem.rows[0].cells[0].width);
    //i2uitrace(1,"checkAlignment cell 0 attrs="+headeritem.rows[0].cells[0].attributes.length);
    //i2uitrace(1,"checkAlignment cell 0 childnodes.length="+headeritem.rows[0].cells[0].childNodes.length);
    //i2uitrace(1,"checkAlignment cell 0 childnodes[0].type="+headeritem.rows[0].cells[0].childNodes[0].nodeType);
    //i2uitrace(1,"checkAlignment cell 0 childnodes[0].name="+headeritem.rows[0].cells[0].childNodes[0].nodeName);
    //i2uitrace(1,"checkAlignment cell 0 childnodes[0].value="+headeritem.rows[0].cells[0].childNodes[0].nodeValue);

    //var nnm = headeritem.rows[0].cells[0].attributes;
    //i2uitrace(1,"checkAlignment nnm="+nnm);
    //if (nnm != null)
    //{
    //  var len = nnm.length;
    //  i2uitrace(1,"checkAlignment nnm.length="+len);
    //  var n;
    //  for (var i=0; i<len; i++)
    //  {
    //    n = nnm.item[i];
    //    i2uitrace(1,"checkAlignment n="+n);
    //    if (n != null)
    //      i2uitrace(1,"checkAlignment n.name="+n.nodeName+" n.value="+n.nodeValue);
    //  }
    //}

    if (dataitem.rows.length > 0)
    {
      var len = headeritem.rows[0].cells.length;
      //i2uitrace(1,"checkAlignment headeritem #cells="+len);
      for (var i=0; i<len; i++)
      {
        //i2uitrace(1,"checkAlignment headeritem cell "+i+" ="+headeritem.rows[0].cells[i].clientWidth);
        //i2uitrace(1,"checkAlignment dataitem   cell "+i+" ="+dataitem.rows[0].cells[i].clientWidth);
        //i2uitrace(1,"checkAlignment headeritem cell "+i+" ="+headeritem.rows[0].cells[i].offsetWidth);
        //i2uitrace(1,"checkAlignment dataitem   cell "+i+" ="+dataitem.rows[0].cells[i].offsetWidth);
        //if (headeritem.rows[0].cells[i].offsetWidth !=
        //    dataitem.rows[0].cells[i].offsetWidth)
        if (headeritem.rows[0].cells[i].clientWidth != dataitem.rows[0].cells[i].clientWidth)
        {
          //i2uitrace(1,"checkAlignment failed at row "+i);
          rc = false;
          break;
        }
      }
    }
  }
  //i2uitrace(1,"checkAlignment="+rc);
  return rc;
}

/*
  Resizes a table.

  mastertableid - <tbd>
  minheight - minimum vertical scroller height
  minwidth - minimum horizontal scroller width
  slavetableid - <tbd>
  flag - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeTable(mastertableid, minheight, minwidth, slavetableid, flag)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }

  //i2uitrace(0,"ResizeTable = "+mastertableid);
  var tableitem     = document.getElementById(mastertableid);
  var headeritem    = document.getElementById(mastertableid+"_header");
  var dataitem      = document.getElementById(mastertableid+"_data");
  var scrolleritem  = document.getElementById(mastertableid+"_scroller");
  var scrolleritem2 = document.getElementById(mastertableid+"_header_scroller");

  if (tableitem != null &&
      headeritem != null &&
      dataitem != null &&
      scrolleritem != null &&
      scrolleritem2 != null)
  {
    //i2uitrace(0,"resizetable pre - client: header="+headeritem.clientWidth+" data="+dataitem.clientWidth+" scroller="+scrolleritem.clientWidth);
    //i2uitrace(0,"resizetable pre - offset: header="+headeritem.offsetWidth+" data="+dataitem.offsetWidth+" scroller="+scrolleritem.offsetWidth);
    //i2uitrace(0,"resizetable pre - scroll: header="+headeritem.scrollWidth+" data="+dataitem.scrollWidth+" scroller="+scrolleritem.scrollWidth);

    scrolleritem2.width       = "100px";
    scrolleritem2.style.width = "100px";
    scrolleritem.style.height = "100px";
    scrolleritem.width        = "100px";
    scrolleritem.style.width  = "100px";

    tableitem.style.width  = "100px";
    dataitem.width         = "100px";
    dataitem.style.width   = "100px";
    headeritem.width       = "100px";
    headeritem.style.width = "100px";

    //i2uitrace(0,"resizetable post - client: header="+headeritem.clientWidth+" data="+dataitem.clientWidth+" scroller="+scrolleritem.clientWidth);
    //i2uitrace(0,"resizetable post - offset: header="+headeritem.offsetWidth+" data="+dataitem.offsetWidth+" scroller="+scrolleritem.offsetWidth);
    //i2uitrace(0,"resizetable post - scroll: header="+headeritem.scrollWidth+" data="+dataitem.scrollWidth+" scroller="+scrolleritem.scrollWidth);

    var cmd = "i2uiResizeScrollableArea('"+mastertableid+"', '"+minheight+"', '"+minwidth+"', '"+slavetableid+"', '"+flag+"')";
    //setTimeout(cmd, 250);
    for (var i=0; i<4; i++)
    {
      eval(cmd);
      if (i2uiCheckAlignment(mastertableid))
      {
        break;
      }
      //i2uitrace(0,"realigned after "+i+" attempts");
    }
  }
}

/*
  Keeps scrolled position in scrollable regions in sync

  mastertableid - <tbd>
  slavetableid - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiSyncdScroll(mastertableid, slavetableid)
{
  var masterscrolleritem;
  var slavescrolleritem;

  if (slavetableid == null)
  {
    // keep header and data of same table scrolled to same position
    masterscrolleritem = document.getElementById(mastertableid+"_scroller");
    slavescrolleritem  = document.getElementById(mastertableid+"_header_scroller");
    if (slavescrolleritem != null &&
        masterscrolleritem != null)
    {
      slavescrolleritem.scrollTop  = masterscrolleritem.scrollTop;
      slavescrolleritem.scrollLeft = masterscrolleritem.scrollLeft;
    }
  }
  else
  {
    // keep data in different tables scrolled to same position
    masterscrolleritem = document.getElementById(mastertableid+"_scroller");
    slavescrolleritem  = document.getElementById(slavetableid+"_scroller");
    if (slavescrolleritem != null &&
        masterscrolleritem != null)
    {
      slavescrolleritem.scrollTop  = masterscrolleritem.scrollTop;
    }
  }
}

/*
  Displays various sizes for each scrollable table partition.

  tableid - the table id

  return - none

  Compatibility:  IE, NS6
*/
function i2uiRecap(tableid)
{
  // sorry not supported in Netscape 4.x
  if (document.layers)
  {
    return;
  }

  var tableitem     = document.getElementById(tableid);
  var headeritem    = document.getElementById(tableid+"_header");
  var dataitem      = document.getElementById(tableid+"_data");
  var scrolleritem  = document.getElementById(tableid+"_scroller");

  if (tableitem    != null &&
      headeritem   != null &&
      dataitem     != null &&
      scrolleritem != null &&
      dataitem.rows.length > 0)
  {
    var len = headeritem.rows[0].cells.length;

    i2uitrace (0,"recap for "+tableid);
    i2uitrace (0,"data: width="+dataitem.width+" style.width="+dataitem.style.width+" client="+dataitem.clientWidth+" scroll="+dataitem.scrollWidth+" offset="+dataitem.offsetWidth);
    i2uitrace (0,"header: width="+headeritem.width+" style.width="+headeritem.style.width+" client="+headeritem.clientWidth+" scroll="+headeritem.scrollWidth+" offset="+headeritem.offsetWidth);
    i2uitrace (0,"scroller: width="+scrolleritem.width+" style.width="+scrolleritem.style.width+" client="+scrolleritem.clientWidth+" scroll="+scrolleritem.scrollWidth+" offset="+scrolleritem.offsetWidth);

    for (var i=0; i<len; i++)
    {
      i2uitrace (0,"row i="+i+" header="+headeritem.rows[0].cells[i].clientWidth+" data="+dataitem.rows[0].cells[i].clientWidth);
    }
  }
  return;
}

/*
  Trace facility that prints in a separate window.
  Messages are only printed if 'level' is less than
  the current 'i2uitracelevel'.

  level - trace level
  text - message to print

  return - none

  Compatibility:  IE, NS6
*/
function i2uitrace (level,text)
{

  if (level <= i2uitracelevel)
  {
    if (text == 'clear')
    {
      i2uitracetext="";
    }
    else
    {
      i2uitracetext += text;
    }
    i2uitracetext += "<BR>";

    i2uitracewindow = window.open("",
                                  "i2uitrace",
                                  "status=no,scrollbars=yes,resizable=yes");
    i2uitracewindow.document.open();
    i2uitracewindow.document.write("<html><title>Trace Window</title><body>");
    i2uitracewindow.document.write(i2uitracetext);
    i2uitracewindow.document.write("</body></html>");
    i2uitracewindow.document.close();
  }
}

/*
  Mouse down event handler for resizable master/slave tables

  e - the mouse down event

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeSlaveonmousedown(e)
{
  var name;

  i2uiResizeSlaveOrigonmouseup   = document.onmouseup;
  i2uiResizeSlaveOrigonmousemove = document.onmousemove;

  document.onmousemove = i2uiResizeSlaveonmousemove;
  document.onmouseup   = i2uiResizeSlaveonmouseup;

  i2uiResizeSlavewhichEl = null;

  i2uiResizeSlavewhichEl = event.srcElement;

  name = i2uiResizeSlavewhichEl.id;

  //window.status = "mousedown. target="+name;
  // determine if mousedown on a resize indicator
  while (i2uiResizeSlavewhichEl.id.indexOf(i2uiResizeKeyword) == -1)
  {
    //i2uiResizeSlavewhichEl = i2uiResizeSlavewhichEl.parentElement;
    i2uiResizeSlavewhichEl = i2uiResizeSlavewhichEl.parentNode;
    if (i2uiResizeSlavewhichEl == null)
    {
      return;
    }
  }

  if (i2uiResizeSlavewhichEl == null)
  {
    return;
  }

  //window.status = "mousedown. name="+name;

  i2uiResizeSlavewhichEl.style.cursor = "move";

  // retain mousedown position
  i2uiResizeSlaveorigX = event.clientX + document.body.scrollLeft;
}

/*
  Mouse move event handler for resizable master/slave tables.

  e - the mouse move event

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeSlaveonmousemove(e)
{
  if (i2uiResizeSlavewhichEl == null)
  {
    event.returnValue = true;
  }
  else
  {
    event.returnValue = false;
  }
}

/*
  Mouse up event handler for resizable master/slave tables.

  e - the mouse up event

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeSlaveonmouseup(e)
{
  document.onmousemove = i2uiResizeSlaveOrigonmousemove;
  document.onmouseup   = i2uiResizeSlaveOrigonmouseup;

  if (i2uiResizeSlavewhichEl == null)
  {
    //window.status = "mouseup but no item";
    return;
  }
  //window.status = "mouseup";
  i2uiResizeSlavenewX = event.clientX + document.body.scrollLeft;
  i2uiResizeSlaveresize();
  event.returnValue = false;
  i2uiResizeSlavewhichEl = null;
}

/*
  Mouse over event handler for resizable master/slave tables

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeSlaveonmouseover()
{
  if (i2uiResizeSlavewhichEl == null && event.srcElement.id.indexOf(i2uiResizeKeyword) != -1)
  {
    event.srcElement.style.cursor = "move";
  }
  event.returnValue = true;
}

/*
  Performs the actual resizing for master/slave tables

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeSlaveresize()
{
  var distanceX = i2uiResizeSlavenewX - i2uiResizeSlaveorigX;
  //i2uitrace(1,"resize dist="+distanceX);
  if (distanceX != 0)
  {
    //i2uitrace(1,"resize variable="+i2uiResizeSlavewhichEl.id);

    // test that variable exists by this name
    if (i2uiIsVariableDefined(i2uiResizeSlavewhichEl.id.substring(i2uiResizeKeywordLength)))
    {
      var w = i2uiResizeSlavewhichEl.id.substring(i2uiResizeKeywordLength);
      var newwidth = eval(w) + distanceX;
      //i2uitrace(1,"distance="+distanceX+" old width="+eval(w)+" new width="+newwidth);

      var len = i2uiResizeWidthVariable.length;
      //i2uitrace(1,"array len="+len);
      for (var i=0; i<len; i++)
      {
        if (i2uiResizeWidthVariable[i] == i2uiResizeSlavewhichEl.id.substring(i2uiResizeKeywordLength))
        {
          //i2uitrace(1,"resize slave2 table["+i2uiResizeSlave2Variable[i]+"]");
          //i2uitrace(1,"resize master table["+i2uiResizeMasterVariable[i]+"]");

          // make newwidth the smaller of newwidth and scroll width
          var scrolleritem  = document.getElementById(i2uiResizeSlave2Variable[i]+"_scroller");
          //i2uitrace(0,"scrolleritem="+scrolleritem);
          if (scrolleritem != null)
          {
            //i2uitrace(1,"scrolleritem="+scrolleritem);
            //i2uitrace(1,"scroller: width="+scrolleritem.width+" style.width="+scrolleritem.style.width+" client="+scrolleritem.clientWidth+" scroll="+scrolleritem.scrollWidth+" offset="+scrolleritem.offsetWidth);
            newwidth = Math.min(newwidth,scrolleritem.scrollWidth);
          }

          // must know about row groupings !!
          i2uiResizeScrollableArea(i2uiResizeSlave2Variable[i],null,newwidth);
          i2uiResizeScrollableArea(i2uiResizeMasterVariable[i],null,20,i2uiResizeSlaveVariable[i],i2uiResizeFlagVariable[i],newwidth);
          eval(i2uiResizeSlavewhichEl.id.substring(i2uiResizeKeywordLength)+"="+newwidth);
          break;
        }
      }
    }
    else
    {
      window.status = "ERROR: variable ["+i2uiResizeSlavewhichEl.id.substring(i2uiResizeKeywordLength)+"] not valid";
    }
  }
}

/*
  Makes portions of master/slave table resizable by end user.

  width - <tbd>
  master - <tbd>
  slave - <tbd>
  slave2 - <tbd>
  flag - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizableSlave(width, master, slave, slave2, flag)
{
  if (document.layers)
  {
    return;
  }
  var len = i2uiResizeWidthVariable.length;
  i2uiResizeWidthVariable[len]  = width;
  i2uiResizeMasterVariable[len] = master;
  i2uiResizeSlaveVariable[len]  = slave;
  i2uiResizeSlave2Variable[len] = slave2;
  i2uiResizeFlagVariable[len]   = flag;

  var obj = document.getElementById(i2uiResizeKeyword+width);
  if (obj != null)
  {
    obj.onmouseover      = i2uiResizeSlaveonmouseover;
    obj.onmousedown      = i2uiResizeSlaveonmousedown;
    //document.onmousemove = i2uiResizeSlaveonmousemove;
    //document.onmouseup   = i2uiResizeSlaveonmouseup;
  }
}

/*
  Checks whether a javascript variable exists or not

  name - the javascript variable to check

  return - true if the variable is defined and false othewise

  Compatibility:  IE, NS6, NS4
*/
function i2uiIsVariableDefined(name)
{
  var check;
  // use of window is critical as well as prepending ''
  eval("check = ''+window."+name);
  if (check == "undefined")
  {
    return false;
  }
  else
  {
    return true;
  }
}

/*
  Pad tree table logic

  tablename - <tbd>
  cellname - <tbd>
  column - <tbd>
  relatedtablenames - <tbd>
  name - <tbd>
  recurse - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiManagePadTree(tablename, cellname, column, relatedtablenames, name, recurse, relatedroutine)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }

  // allows related table to adjust to new width due to actions in current table
  if (recurse == null && relatedtablenames != null)
  {
    // only do this if not a leaf node
    var img = document.getElementById("TREECELLIMAGE_"+tablename+"_"+cellname);
    if (img != null && img.src.indexOf("bullet") == -1)
      i2uiShrinkScrollableTable(relatedtablenames);
  }

  //i2uitrace(1,"manage cell=["+cellname+"]");

  var table;
  var savemasterscrolltop;
  var saveslavescrolltop;
  var loadondemand = false;

  //i2uitrace(1,"tablename=["+tablename+"]");

  table = document.getElementById(tablename+"_data");
  if (table == null)
  {
    table = document.getElementById(tablename);
  }

  //i2uitrace(1,"table=["+table+"]");

  if (table != null &&
      table.rows != null)
  {
      var masterscrolleritem = document.getElementById(relatedtablenames+"_scroller");
      var slavescrolleritem  = document.getElementById(tablename+"_scroller");
      if (slavescrolleritem != null &&
          masterscrolleritem != null)
      {
        savemasterscrolltop = masterscrolleritem.scrollTop;
        saveslavescrolltop = slavescrolleritem.scrollTop;
      }

    var relatedtable = null;
    if (relatedtablenames != null &&
        relatedtablenames != 'undefined')
    {
      relatedtable = document.getElementById(relatedtablenames+"_data");
    }

    //i2uitrace(1,document.getElementById("TREECELLIMAGE_"+cellname));
    //i2uitrace(1,document.getElementById("TREECELLIMAGE_"+cellname).src);
    var img = document.getElementById("TREECELLIMAGE_"+tablename+"_"+cellname);
    if (img.src.indexOf("bullet") != -1)
    {
      return;
    }

    var action;
    if (img != null && img.src != null)
    {
      if (img.src.indexOf("_loadondemand") != -1)
      {
        loadondemand = true;
      }
      else
      if (img.src.indexOf("minus") == -1)
      {
        if (recurse == null)
        {
          img.src = i2uiImageDirectory+"/minus_norgie.gif";
          action = "";
        }
        else
        {
          action = "none";
        }
        //i2uitrace(1,"now expand");
      }
      else
      if (img.src.indexOf("plus") == -1)
      {
        if (recurse == null)
        {
          img.src = i2uiImageDirectory+"/plus_norgie.gif";
          action = "none";
        }
        else
        {
          action = "";
        }
        //i2uitrace(1,"now collapse");
      }
    }
    //i2uitrace(1,document.getElementById("TREECELLIMAGE_"+cellname).src);

    // if collapsing, must collapse all children
    // if expanding, expand only immediate children

    var depth1 = cellname.split("_").length;
    //i2uitrace(1,"depth1="+depth1);
    var len = table.rows.length;
    //i2uitrace(1,"len="+len);
    for (var i=1; i<len; i++)
    {
      //i2uitrace(1,"id="+table.rows[i].cells[column].id);
      //i2uitrace(1,"substr="+table.rows[i].cells[column].id.substr(0,cellname.length+10));
      //i2uitrace(1,"test for [TREECELL_"+cellname+"_]");

      if (table.rows[i].cells[column].id.substr(0,cellname.length+10) == "TREECELL_"+cellname+"_")
      {
        //i2uitrace(1,"manage row "+i);
        if (action == "none")
        {
          table.rows[i].style.display = action;
          if (relatedtable != null)
          {
            relatedtable.rows[i].style.display = action;
          }
        }
        else
        {
          var depth2 = table.rows[i].cells[column].id.split("_").length;
          //i2uitrace(1,"depth2="+depth2);
          if (depth2 == depth1 + 2)
          {
            table.rows[i].style.display = action;
            if (relatedtable != null)
            {
              relatedtable.rows[i].style.display = action;
            }

            var newcell = table.rows[i].cells[column].id.substr(9);
            //i2uitrace(1,newcell);
            i2uiManagePadTree(tablename,newcell,column,relatedtablenames,name,1);
          }
        }
      }
    }

    // finally realign tables
    if (recurse == null)
    {
      if (relatedtablenames != null ||
          document.getElementById(tablename+"_data") != null)
      {
        // must scroll to top.  can't use previous scroll position
        // since table height itself could have changed.  as a result,
        // rows become misaligned.
        var masterscrolleritem = document.getElementById(relatedtablenames+"_scroller");
        var slavescrolleritem  = document.getElementById(tablename+"_scroller");
        if (slavescrolleritem != null &&
            masterscrolleritem != null)
        {
          //i2uitrace(1,"savedmasterscrolltop="+savemasterscrolltop);
          //i2uitrace(1,"savedslavescrolltop="+saveslavescrolltop);
          masterscrolleritem.scrollTop = 0;
          slavescrolleritem.scrollTop  = 0;
          //masterscrolleritem.scrollTop = savemasterscrolltop;
          //slavescrolleritem.scrollTop  = masterscrolleritem.scrollTop;
        }
      }

      // call a related routine to handle any follow-up actions
      // intended for internal use.  external use should use the
      // callback mechanism
      //i2uitrace(0,"managetreetable related=["+relatedroutine+"]");
      if (relatedroutine != null)
      {
        // must invoke the routine 'in the future' to allow browser
        // to settle down, that is, finish rendering the effects of
        // the tree element changing state
        setTimeout(relatedroutine,100);
      }

      // call a routine of the invoker's choosing to handle any realignment
      if (i2uiManageTreeTableUserFunction != null)
      {
        if (name == null)
        {
          name = 'undefined';
        }
        if (relatedtablenames != null)
        {
          eval(i2uiManageTreeTableUserFunction+"('"+tablename+"','"+relatedtablenames+"','"+action+"',"+savemasterscrolltop+",'"+name+"',"+loadondemand+")");
        }
        else
        {
          eval(i2uiManageTreeTableUserFunction+"('"+tablename+"','undefined','"+action+"',null,'"+name+"',"+loadondemand+")");
        }
      }
    }
  }
}

/*
  Tree table logic where some column in a table contains a tree

  tablename - <tbd>
  cellname - <tbd>
  column - <tbd>
  relatedtablenames - <tbd>
  name - <tbd>
  recurse - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiManageTreeTable(tablename, cellname, column, relatedtablenames, name, recurse, relatedroutine, startat)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }

  // allows related table to adjust to new width due to actions in current table
  if (recurse == null && relatedtablenames != null)
  {
    // only do this if not a leaf node
    var img = document.getElementById("TREECELLIMAGE_"+tablename+"_"+cellname);
    if (img != null && img.src.indexOf("bullet") == -1)
      i2uiShrinkScrollableTable(relatedtablenames);
  }

  //i2uitrace(1,"manage cell=["+cellname+"]");

  var table;
  var savemasterscrolltop;
  var saveslavescrolltop;
  var loadondemand = false;

  //i2uitrace(1,"tablename=["+tablename+"]");

  table = document.getElementById(tablename+"_data");
  if (table == null)
  {
    table = document.getElementById(tablename);
  }

  //i2uitrace(1,"table=["+table+"]");

  if (table != null &&
      table.rows != null)
  {
      var masterscrolleritem = document.getElementById(relatedtablenames+"_scroller");
      var slavescrolleritem  = document.getElementById(tablename+"_scroller");
      if (slavescrolleritem != null &&
          masterscrolleritem != null)
      {
        savemasterscrolltop = masterscrolleritem.scrollTop;
        saveslavescrolltop = slavescrolleritem.scrollTop;
      }

    var relatedtable = null;
    if (relatedtablenames != null &&
        relatedtablenames != 'undefined')
    {
      relatedtable = document.getElementById(relatedtablenames+"_data");
    }

    //i2uitrace(1,document.getElementById("TREECELLIMAGE_"+cellname));
    //i2uitrace(1,document.getElementById("TREECELLIMAGE_"+cellname).src);
    var img = document.getElementById("TREECELLIMAGE_"+tablename+"_"+cellname);
    if (img != null && img.src != null && img.src.indexOf("bullet") != -1)
    {
      return;
    }

    var action;
    var action2;
    if (img != null && img.src != null)
    {
      if (img.src.indexOf("_loadondemand") != -1)
      {
        loadondemand = true;
        action2 = "none";
      }
      else
      if (img.src.indexOf("minus") == -1)
      {
        action2 = "expand";
        if (recurse == null)
        {
          img.src = i2uiImageDirectory+"/minus_norgie.gif";
          action = "";
        }
        else
        {
          action = "none";
        }
        //i2uitrace(1,"now expand");
      }
      else
      if (img.src.indexOf("plus") == -1)
      {
        action2 = "collapse";
        if (recurse == null)
        {
          img.src = i2uiImageDirectory+"/plus_norgie.gif";
          action = "none";
        }
        else
        {
          action = "";
        }
        //i2uitrace(1,"now collapse");
      }
    }
    //i2uitrace(1,document.getElementById("TREECELLIMAGE_"+cellname).src);

    // if collapsing, must collapse all children
    // if expanding, expand only immediate children

    var depth1 = Math.floor(cellname);
    //i2uitrace(1,"depth1="+depth1);
    var len = table.rows.length;
    //i2uitrace(1,"len="+len);
    if (startat == null)
      startat = 0;

    //i2uitrace(1,"**** startat="+startat+" depth1="+depth1+" cellname="+cellname);
    for (var i=startat; i<len; i++)
    {
      //i2uitrace(1,"id="+table.rows[i].cells[column].id);
      //i2uitrace(1,"test for [TREECELL_"+cellname+"]");

    // Added by sudhir to ignore if table row has no columns
      if (table.rows[i].cells[column] == null) continue;
      if (table.rows[i].cells[column].id == null) continue;
      // Added by sudhir to ignore if table row cell has no id



      // locate desired row in table
      if (table.rows[i].cells[column].id == "TREECELL_"+cellname)
      {
        //i2uitrace(1,"located key at row "+i);

        // now process rest of table with respect to located
        for (var j=i+1; j<len; j++)
        {

    // Added by sudhir to ignore if table row has no columns
      if (table.rows[j].cells[column] == null) continue;
      if (table.rows[j].cells[column].id == null) continue;
      // Added by sudhir to ignore if table row cell has no id

          var newcell = table.rows[j].cells[column].id.substr(9);
          var depth2 = Math.floor(newcell);

          //i2uitrace(1,"row="+j+" id="+table.rows[j].cells[column].id+" newcell="+newcell+" depth2="+depth2+" depth1="+depth1+" action2="+action2);
          if (((depth2 == depth1 + 10 || depth2 == depth1 + 15) && action2 == "expand") ||
              (depth2 > depth1 + 5 && action2 == "collapse"))
          {
            //i2uitrace(1,"perform action ["+action+"] on row "+j);
            table.rows[j].style.display = action;
            if (relatedtable != null)
              relatedtable.rows[j].style.display = action;

            // if expanding and not already recursing, then recurse
            if (depth2 == depth1 + 10 && action2 == "expand" && recurse == null)
            {
              //i2uitrace(0,"recurse with "+newcell+" from row "+j);
              i2uiManageTreeTable(tablename,newcell,column,relatedtablenames,name,1,null,j);
            }
          }
          if (depth2 <= depth1)
          {
            //i2uitrace(1,"*** done with children");
            break;
          }
        }
        break;
      }
    }

    // finally realign tables
    if (recurse == null)
    {
      if (relatedtablenames != null ||
          document.getElementById(tablename+"_data") != null)
      {
        // must scroll to top.  can't use previous scroll position
        // since table height itself could have changed.  as a result,
        // rows become misaligned.
        var masterscrolleritem = document.getElementById(relatedtablenames+"_scroller");
        var slavescrolleritem  = document.getElementById(tablename+"_scroller");
        if (slavescrolleritem != null &&
            masterscrolleritem != null)
        {
          //i2uitrace(1,"savedmasterscrolltop="+savemasterscrolltop);
          //i2uitrace(1,"savedslavescrolltop="+saveslavescrolltop);
          masterscrolleritem.scrollTop = 0;
          slavescrolleritem.scrollTop  = 0;
          //masterscrolleritem.scrollTop = savemasterscrolltop;
          //slavescrolleritem.scrollTop  = masterscrolleritem.scrollTop;
        }
      }

      // call a related routine to handle any follow-up actions
      // intended for internal use.  external use should use the
      // callback mechanism
      //i2uitrace(0,"managetreetable related=["+relatedroutine+"]");
      if (relatedroutine != null)
      {
        // must invoke the routine 'in the future' to allow browser
        // to settle down, that is, finish rendering the effects of
        // the tree element changing state
        setTimeout(relatedroutine,100);
      }

      // call a routine of the invoker's choosing to handle any realignment
      if (i2uiManageTreeTableUserFunction != null)
      {
        if (name == null)
        {
          name = 'undefined';
        }
        if (relatedtablenames != null)
        {
          eval(i2uiManageTreeTableUserFunction+"('"+tablename+"','"+relatedtablenames+"','"+action+"',"+savemasterscrolltop+",'"+name+"',"+loadondemand+")");
        }
        else
        {
          eval(i2uiManageTreeTableUserFunction+"('"+tablename+"','undefined','"+action+"',null,'"+name+"',"+loadondemand+")");
        }
      }
    }
  }
}

/*
  Collapses all nodes of a pad tree to a given depth.
  Topmost should be 0.  Also expands nodes to reach
  the specified depth.

  tablename - name of pad
  depth - depth to which pad should be collapsed

  return - none

  Compatibility:  IE, NS6
*/
function i2uiCollapsePadTree(tablename, depth)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }

  var column = 0;

  //i2uitrace(1,"collapsepadtree depth="+depth);

  var table;
  var savemasterscrolltop;
  var saveslavescrolltop;

  //i2uitrace(1,"tablename=["+tablename+"]");

  table = document.getElementById(tablename+"_data");
  if (table == null)
  {
    table = document.getElementById(tablename);
  }

  //i2uitrace(1,"table=["+table+"]");

  if (table != null &&
      table.rows != null)
  {
    var len = table.rows.length;
    var rowdepth;
    var img;
    var cellname;
    var childkey;
    var childnode;
    //i2uitrace(1,"#rows="+len);
    for (var i=0; i<len; i++)
    {
      rowdepth = table.rows[i].cells[column].id.split("_").length - 2;
      //i2uitrace(1,"rowdepth="+rowdepth);
      if (rowdepth <0)
        continue;

      cellname = table.rows[i].cells[column].id.substr(9);
      //i2uitrace(1,"id="+table.rows[i].cells[column].id+" cellname="+cellname);

      childkey = "TREECELLIMAGE_"+tablename+"_"+cellname + "_1";
      childnode = document.getElementById(childkey);
      //i2uitrace(1,"childnode="+childnode+" key="+childkey);

      img = document.getElementById("TREECELLIMAGE_"+tablename+"_"+cellname);

      if (rowdepth == depth)
      {
        //i2uitrace(1,"COLLAPSE ME id="+table.rows[i].cells[column].id);
        if (img != null)
        {
          // test for children
          if (childnode == null)
          {
            // Added by WJD for degraded mode tree bullets
            if (img.tagName == "A")
	    {
	      img.innerText = "*";
	    }
	    else
            {
              if (img.src.indexOf("plus_loadondemand.gif") == -1)
              {
                img.src = i2uiImageDirectory+"/tree_bullet.gif";
              }
            }
          }
          else
          {
            img.src = i2uiImageDirectory+"/plus_norgie.gif";
          }
        }
        table.rows[i].style.display = "";
      }
      else
      {
        if (rowdepth > depth)
        {
          // test for children
          if (childnode == null)
          {
            if (img != null)
            {
              // Added by WJD for degraded mode tree bullets
              if (img.tagName == "A")
	      {
	        img.innerText = "*";
	      }
	      else
              {
                if (img.src.indexOf("plus_loadondemand.gif") == -1)
                {
                  img.src = i2uiImageDirectory+"/tree_bullet.gif";
                }
              }
            }
          }
          //i2uitrace(1,"HIDE ME id="+table.rows[i].cells[column].id);
          table.rows[i].style.display = "none";
        }
        else
        {
          // test for children
          if (childnode == null)
          {
            if (img != null)
            {
              if (img.src.indexOf("plus_loadondemand.gif") == -1)
                img.src = i2uiImageDirectory+"/tree_bullet.gif";
            }
          }
          else
          {
            if (img != null)
            {
              img.src = i2uiImageDirectory+"/minus_norgie.gif";
            }
          }
          //i2uitrace(1,"EXPAND ME id="+table.rows[i].cells[column].id);
          table.rows[i].style.display = "";
        }
      }
    }
  }
}

/*
  Collapses all nodes of a tree to a given depth.
  Topmost should be 0.  Also expands nodes to reach
  the specified depth.

  tablename - name of table containing treecells
  depth - desired depth
  relatedtablenames - name of any sync'd table
  column - indicates which column contains the tree
  processdescendants - whether to collapse descendants also

  return - none

  Compatibility:  IE, NS6
*/
function i2uiCollapseTreeTable(tablename, depth, relatedtablenames, column, processdescendants)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }

  // column is 0 based.  if not supplied assume first column
  if (column == null)
  {
    column = 0;
  }

  //i2uitrace(1,"collapsetree depth="+depth);

  var table;
  var savemasterscrolltop;
  var saveslavescrolltop;

  //i2uitrace(1,"tablename=["+tablename+"]");

  table = document.getElementById(tablename+"_data");
  if (table == null)
  {
    table = document.getElementById(tablename);
  }

  //i2uitrace(1,"table=["+table+"]");

  if (table != null &&
      table.rows != null)
  {

    var relatedtable = null;
    if (relatedtablenames != null &&
        relatedtablenames != 'undefined')
    {
      relatedtable = document.getElementById(relatedtablenames+"_data");
      var masterscrolleritem = document.getElementById(relatedtablenames+"_scroller");
      if (masterscrolleritem != null)
      {
        savemasterscrolltop = masterscrolleritem.scrollTop;
      }
    }

    var len = table.rows.length;
    var rowdepth;
    var newdepth;
    var node;
    var img;
    var cellname;
    var childkey;
    var childnode;
    //i2uitrace(0,"#rows="+len);
    for (var i=0; i<len; i++)
    {

      // Added by sudhir to ignore if table row has no columns
      if (table.rows[i].cells[column] == null) continue;
      if (table.rows[i].cells[column].id == null) continue;
      // Added by sudhir to ignore if table row cell has no id

      cellname = table.rows[i].cells[column].id.substr(9);
      rowdepth = Math.floor(cellname);
      node = cellname.split(".")[1];

      //i2uitrace(0,"cellname="+cellname+" rowdepth="+rowdepth+" node="+node+" depth="+depth);
      if (rowdepth < 0)
        continue;

      //i2uitrace(1,"id="+table.rows[i].cells[column].id+" cellname="+cellname+" node="+node);
      newdepth = rowdepth + 10;
      node++;
      childkey = "TREECELLIMAGE_"+tablename+"_"+newdepth+"."+node;

      childnode = document.getElementById(childkey);
      //i2uitrace(1,"childnode="+childnode+" key="+childkey);

      img = document.getElementById("TREECELLIMAGE_"+tablename+"_"+cellname);

      if (rowdepth == depth * 10)
      {
        //i2uitrace(0,"COLLAPSE ME id="+table.rows[i].cells[column].id);
        if (img != null)
        {
          // test for children
          if (childnode == null)
          {
            if (img.src.indexOf("plus_loadondemand.gif") == -1)
              img.src = i2uiImageDirectory+"/tree_bullet.gif";
          }
          else
          {
            img.src = i2uiImageDirectory+"/plus_norgie.gif";
          }
        }
        table.rows[i].style.display = "";
        if (relatedtable != null && relatedtable.rows[i] != null)
        {
          relatedtable.rows[i].style.display = "";
        }
      }
      else
      {
        if (rowdepth == (depth * 10) + 5)
        {
          //i2uitrace(0,"SKIP ME id="+table.rows[i].cells[column].id);
          continue;
        }
        else
        if (rowdepth + 5 > depth * 10)
        {
          // test for children
          if (childnode == null)
          {
            if (img != null)
            {
              if (img.src.indexOf("plus_loadondemand.gif") == -1)
                img.src = i2uiImageDirectory+"/tree_bullet.gif";
            }
          }
          else
          {
            // if collapse descendants too
            if (processdescendants != null && processdescendants)
            {
              img.src = i2uiImageDirectory+"/plus_norgie.gif";
            }
          }

         //i2uitrace(0,"HIDE ME id="+table.rows[i].cells[column].id);
          table.rows[i].style.display = "none";
          if (relatedtable != null && relatedtable.rows[i] != null)
          {
            relatedtable.rows[i].style.display = "none";
          }
        }
        else
        {
          // test for children
          if (childnode == null)
          {
            if (img != null)
            {
              if (img.src.indexOf("plus_loadondemand.gif") == -1)
                img.src = i2uiImageDirectory+"/tree_bullet.gif";
            }
          }
          else
          {
            if (img != null)
            {
              img.src = i2uiImageDirectory+"/minus_norgie.gif";
            }
          }
          //i2uitrace(1,"EXPAND ME id="+table.rows[i].cells[column].id);
          table.rows[i].style.display = "";
          if (relatedtable != null && relatedtable.rows[i] != null)
          {
            relatedtable.rows[i].style.display = "";
          }
        }
      }
    }

    // finally realign tables
    if (relatedtablenames != null ||
        document.getElementById(tablename+"_data") != null)
    {
      // must scroll to top.  can't use previous scroll position
      // since table height itself could have changed.  as a result,
      // rows become misaligned.
      var masterscrolleritem = document.getElementById(relatedtablenames+"_scroller");
      var slavescrolleritem  = document.getElementById(tablename+"_scroller");
      if (slavescrolleritem != null &&
          masterscrolleritem != null)
      {
        masterscrolleritem.scrollTop = 0;
        slavescrolleritem.scrollTop  = 0;
      }

      // call a routine of the invoker's choosing to handle any realignment
      if (i2uiManageTreeTableUserFunction != null)
      {
        if (relatedtablenames != null)
        {
          eval(i2uiManageTreeTableUserFunction+"('"+tablename+"','"+relatedtablenames+"','none',"+savemasterscrolltop+")");
        }
        else
        {
          eval(i2uiManageTreeTableUserFunction+"('"+tablename+"','undefined','none')");
        }
      }
    }
  }
}

/*
  Sets the height for a vertical tab filler.

  id - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiSetTabFillerHeight(id)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }

  var filler_obj = document.getElementById(id+"_filler");
  var tab_table_obj = document.getElementById(id);
  if (filler_obj != null && tab_table_obj != null)
  {
    var container_table_obj = tab_table_obj.parentNode;
    while (container_table_obj != null)
    {
      if (container_table_obj.tagName == 'TABLE')
      {
        break;
      }
      container_table_obj = container_table_obj.parentNode;
    }
    if (container_table_obj != null &&
        tab_table_obj != null)
    {
      //i2uitrace(1,"container height="+container_table_obj.offsetHeight);
      //i2uitrace(1,"tab height="+tab_table_obj.offsetHeight);
      filler_obj.style.height = container_table_obj.offsetHeight -
                                tab_table_obj.offsetHeight;
    }
  }
}

/*
  Sets the dimensions for the scrollable area of a container.

  id - <tbd>
  height - <tbd>
  delta - <tbd>
  width - <tbd>
  useminheight - <tbd>
  autoscrollers - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeScrollableContainer(id, height, delta, width, useminheight, autoscrollers)
{
  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }

  // sorry bug in Netscape 6.0,6.01,6.1 causes container to continally grow in size
  if (!i2uiIsSuitableBrowser())
  {
    return;
  }

  var scroller_obj = document.getElementById(id+"_scroller");
  //i2uitrace(1,"resizescrollablecontainer scroller="+scroller_obj);
  if (scroller_obj != null)
  {
    i2uiComputeScrollHeight(id+"_scroller",true);

    if (width != null)
    {
      scroller_obj.style.width = Math.max(1,width);
      var obj = document.getElementById(id);
      if (obj != null)
      {
        obj.style.width = Math.max(1,width);
        // handle case where title width is more than new content width
        // must resize content to be at least the same as the title
        if (width > 0 &&
            width < obj.offsetWidth - 3)
        {
          scroller_obj.style.width = obj.offsetWidth - 3;
        }
      }
    }

    if (height != null)
    {
      // if you want to size the container to the minimum of
      // the content and allowable space
      if (useminheight != null &&
          useminheight == true &&
          scroller_obj.scrollHeight != null)
      {
        //i2uitrace(1,"id="+id+" desire="+height+" now="+scroller_obj.offsetHeight+" need="+scroller_obj.scrollHeight);
        var scrollHeight = scroller_obj.scrollHeight;
        if (scroller_obj.offsetWidth < scroller_obj.scrollWidth)
          scrollHeight +=  i2uiScrollerWidth;
        scroller_obj.style.height = Math.min(Math.max(1,height), scrollHeight);
      }
      else
      {
        scroller_obj.style.height = Math.max(1,height);
      }
    }
    else
    {
      if (delta != null)
      {
        var x = scroller_obj.offsetWidth;
        var y = scroller_obj.offsetHeight + delta;
        if (x > 0 && y > 0)
        {
          if (useminheight != null &&
              useminheight == true &&
              scroller_obj.scrollHeight != null)
          {
            //i2uitrace(1,"id="+id+" desire="+y+" now="+scroller_obj.offsetHeight+" need="+scroller_obj.scrollHeight);
            scroller_obj.style.height = Math.min(y, scroller_obj.scrollHeight);
          }
          else
          {
            scroller_obj.style.height = y;
          }

          //scroller_obj.style.width  = x;
        }
        // bug in Netscape 6.01 causes container to get wider
        // with each call.  resizing container not helping.
      }
    }

    //i2uitrace(1,"overflow="+scroller_obj.style.overflow+" scrollheight="+scroller_obj.scrollHeight+" offsetheight="+scroller_obj.offsetHeight);

    // whether or not the scroller state should track the space needed.
    // if enabled, the dead space reserved for auto scroller is available
    // for use by the container.
    if (autoscrollers != null && autoscrollers == 'yes')
    {
      if (scroller_obj.scrollHeight <= scroller_obj.offsetHeight &&
          scroller_obj.scrollWidth  <= scroller_obj.offsetWidth    )
      {
        scroller_obj.style.overflowX="hidden";
        scroller_obj.style.overflowY="hidden";
      }
      else
      {
        scroller_obj.style.overflowX="auto";
        scroller_obj.style.overflowY="auto";
      }
    }
  }
}

/*
  Changes the visual appearance of popup menu options.

  obj - <tbd>
  flag - <tbd>
  id - <tbd>
  menuid - <tbd>

  return - none

  Compatibility:  IE, NS6, NS4
*/
function i2uiHighlightMenuOption(obj,flag,id,menuid)
{
  i2uiSubMenuFlag = flag;

  // as mouse moves over options in main menu, hide any active child menu
  if (menuid != null &&
      flag == "Highlighted" &&
      i2uiSubMenuActiveId != null)
  {
    //clear all inactive submenu
    var bActive = true;
    for(var m=0; m<i2uiSubMenuArr.length; m++)
    {
      if(bActive == false || menuid == i2uiMenuActiveId)
      {
        i2uiToggleItemVisibility(i2uiSubMenuArr[m][0],'hide');
      }
      if(i2uiSubMenuArr[m][0] == menuid)
      {
        bActive = false;
      }
    }
    i2uiSubMenuActiveId = null;
  }

  if (document.layers)
  {
    //i2uitrace(0,"Highlight flag=["+flag+"] id=["+id+"] menuid=["+menuid+"]");
    var obj2 = document.layers[menuid];
    if (obj2 != null)
    {
      var obj3 = obj2.layers[id];
      if (obj3 != null)
      {
        if (flag == "Highlighted")
        {
          obj3.bgColor = "#d1d6f0";
        }
        else
        {
          obj3.bgColor = "#f2f4fe";
        }
      }
    }
    return;
  }

  // find row tag that contains menu option
  while (obj != null && obj.tagName != 'TR')
  {
    obj = obj.parentNode;
  }
  if (obj != null)
  {
    obj.className = "menu"+flag;
  }
}

/*
  Determines the location at which a popup menu should be placed.

  obj - <tbd>
  e - <tbd>

  return - none

  Compatibility:  IE, NS6, NS4
*/
function i2uiSetMenuCoords(obj, e)
{
  // this works for IE
  if (obj.clientLeft != null && obj.clientTop != null)
  {
    i2uiMenu_x = e.clientX + obj.document.body.scrollLeft;
    i2uiMenu_y = e.clientY + obj.document.body.scrollTop;
    //window.status = "IE popup menu at x="+i2uiMenu_x+" y="+i2uiMenu_y+" or "+e.x+","+e.y;
  }
  else
  {
    // this works for NS 6
    if (obj.offsetLeft != null && obj.offsetTop != null)
    {
      // this is relative to owner not page coords
      i2uiMenu_x = obj.offsetLeft + obj.offsetWidth;
      i2uiMenu_y = obj.offsetTop + obj.offsetHeight;
      //window.status = "NS6 popup menu at x="+i2uiMenu_x+" y="+i2uiMenu_y;
    }
    else
    // this works for NS 4
    {
      // sorry - the best we can do is show it where the mouse event occured
      i2uiMenu_x = e.pageX;
      i2uiMenu_y = e.pageY;
    }
  }
}

/*
  Hides the previous popup menu and then displays the specified popup menu.

  id - the id of the menu to display

  return - none

  Compatibility:  IE, NS6
*/
function i2uiShowMenu(id, obj)
{
  var docObj;
	
  //fix for eQ :: 530156 - IE loses document after we do save on reports
  if ( obj != null )
  {
    docObj = obj.document;
  }
  else
  {
    docObj = document;
  }

  //i2uitrace(0,"showmenu id="+id);
  i2uiHideMenu();

  i2uiMenuActiveId = id;
  i2uiMenuOrigonmouseup = docObj.onmouseup;

  var objwidth = 25;
  if (docObj.layers)
  {
    obj = docObj.layers[id];
    objwidth = obj.clip.width;
    //i2uitrace(0,"width="+obj.offsetWidth+" or "+obj.clip.width);
  }
  else
  {
    obj = docObj.getElementById(id);
  }

  if (obj != null &&
      i2uiMenu_x != null &&
      i2uiMenu_y != null)
  {
    i2uiKeepMenuInWindow(obj, i2uiMenu_x, i2uiMenu_y, id);

    if (!docObj.layers)
    {
      objwidth = obj.offsetWidth;
    }
    //i2uitrace(0,"width="+obj.offsetWidth);

    // shift any child menu to right
    i2uiSubMenu_x = i2uiMenu_x + objwidth - 5;
    i2uiSubMenu_y = i2uiMenu_y + 10;
  }

  docObj.onmouseup = i2uiCancelMenu;
}

/*
  Hides the previous sub-menu and displays the currently selected
  sub-menu.

  id -

  return - none

  Compatibility:  IE, NS6
*/
function i2uiShowSubMenu(id, obj, e)
{
  i2uiSubMenu_x = e.clientX + obj.offsetWidth;
  i2uiSubMenu_y = e.clientY - 5;
  i2uiSubMenuActiveId = id;

  var bNew = true;
  var nSub = i2uiSubMenuArr.length;
  for(var m=0; m<nSub; m++)
  {
    if(i2uiSubMenuArr[m][0] == i2uiSubMenuActiveId)
    {
      bNew = false;
      break;
    }
  }
  
  if(bNew == true)
  {
    i2uiSubMenuArr[nSub] = [i2uiSubMenuActiveId, i2uiSubMenu_x, i2uiSubMenu_y];
  }
  
  var obj;
  if (document.layers)
  {
    obj = document.layers[id];
  }
  else
  {
    obj = document.getElementById(id);
  }

  if (obj != null &&
      i2uiSubMenu_x != null &&
      i2uiSubMenu_y != null)
  {
    var parMenuX = i2uiMenu_x;
    if(nSub > 0)
    {
      parMenuX = i2uiSubMenuArr[nSub-1][1];
    }
    i2uiKeepMenuInWindow(obj, i2uiSubMenu_x, i2uiSubMenu_y, id, parMenuX);
  }
}

/*
  Repositions the menu so that it displays as much as possible within the window.

  obj - <tbd>
  x - <tbd>
  y - <tbd>
  id - <tbd>
  x2 - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiKeepMenuInWindow(obj, x, y, id, x2)
{
  var ExtraSpace = 10;

  var WindowLeftEdge;
  var WindowTopEdge;
  var WindowWidth;
  var WindowHeight;

  if (window.innerWidth != null)
  {
    WindowWidth    = window.innerWidth;
    WindowHeight   = window.innerHeight;
  }
  else
  {
    WindowWidth    = obj.document.body.clientWidth;
    WindowHeight   = obj.document.body.clientHeight;
  }

  if (window.pageXOffset != null)
  {
    WindowLeftEdge = window.pageXOffset;
    WindowTopEdge  = window.pageYOffset;
    //i2uitrace(1,"showmenu pageYOffset="+WindowTopEdge);
  }
  else
  {
    WindowLeftEdge = obj.document.body.scrollLeft;
    WindowTopEdge  = obj.document.body.scrollTop;
    //i2uitrace(1,"showmenu scrollTop="+WindowTopEdge);
  }
  var WindowRightEdge  = (WindowLeftEdge + WindowWidth) - ExtraSpace;
  var WindowBottomEdge = (WindowTopEdge + WindowHeight) - ExtraSpace;

  //i2uitrace(1,"showmenu window l="+WindowLeftEdge+" t="+WindowTopEdge);

  var MenuLeftEdge   = x;
  var MenuTopEdge    = y;
  var MenuRightEdge;
  var MenuBottomEdge;
  if (obj.document.layers)
  {
    MenuRightEdge  = x + obj.clip.width;
    MenuBottomEdge = y + obj.clip.height;
  }
  else
  {
    // must change visibility in order to compute width !!
    i2uiToggleItemVisibility(id,'show',obj);

    MenuRightEdge  = x + obj.offsetWidth;
    MenuBottomEdge = y + obj.offsetHeight;
  }

  //i2uitrace(1,"showmenu menu l="+x+" t="+y);

  var dif;
  if (MenuRightEdge > WindowRightEdge)
  {
    if (x2 == null)
    {
      dif = MenuRightEdge - WindowRightEdge;
    }
    else
    {
      dif = MenuRightEdge - x2;
    }
    x -= dif;
  }
  if (MenuBottomEdge > WindowBottomEdge)
  {
    dif = MenuBottomEdge - WindowBottomEdge;
    y -= dif;
  }

  if (x < WindowLeftEdge)
  {
    x = 5;
  }

  if (y < WindowTopEdge)
  {
    y = 5;
  }

  if (obj.document.layers)
  {
    obj.moveTo(x,y);
    i2uiToggleItemVisibility(id,'show',obj);
  }
  else
  {
    obj.style.left = x;
    obj.style.top  = y;
  }

  //fix 2/6/02 to improve placement of menus near right edge of screen
  // reset i2uiMenu_x to position of placed menu
  if (x2 == null)
    i2uiMenu_x = x;
}

/*
  Hides the active sub-menu and active menu

  return - none

  Compatibility:  IE, NS6
*/
function i2uiHideMenu()
{
  if (i2uiSubMenuActiveId != null)
  {
    //i2uitrace(0,"hidemenu hide child");
    for(var m=0; m<i2uiSubMenuArr.length; m++)
    {
      i2uiToggleItemVisibility(i2uiSubMenuArr[m][0],'hide');
    }
    i2uiSubMenuActiveId = null;
  }
  if (i2uiMenuActiveId != null)
  {
    //i2uitrace(0,"hidemenu hide main");
    i2uiToggleItemVisibility(i2uiMenuActiveId,'hide');
    i2uiMenuActiveId = null;
  }
}

/*
  Causes a currently displaying popup menu to become hidden.

  e - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiCancelMenu(e)
{
  // must pass the event on
  //fyi: for Netscape6, the event's target is text of anchor not the anchor itself
  if (!document.layers &&
      e &&
      e.target.parentNode != null &&
      e.target.parentNode.tagName == 'A')
  {
    event.returnValue = true;
  }

  //window.status = "CancelMenu submenux="+i2uiSubMenu_x+" submenuflag="+i2uiSubMenuFlag;
  //i2uitrace(0,"cancelmenu flag="+i2uiSubMenuFlag);

  // used to determine if mouseup from within a menu vs outside of menu
  if (i2uiSubMenuFlag != "Highlighted")
  {
    if (i2uiSubMenuArr.length > 0)
    {
      //i2uitrace(0,"cancelmenu hide child");
      for(var m=0; m<i2uiSubMenuArr.length; m++)
      {
        i2uiToggleItemVisibility(i2uiSubMenuArr[m][0],'hide');
      }
      i2uiSubMenuActiveId = null;
    }
    if (i2uiMenuActiveId != null)
    {
      //i2uitrace(0,"cancelmenu hide main");
      i2uiToggleItemVisibility(i2uiMenuActiveId,'hide');
      i2uiMenuActiveId = null;
    }
    document.onmouseup = i2uiMenuOrigonmouseup;
  }
}

/*
  Computes an object's top by traversing containership.

  obj - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiComputeTop(obj)
{
  if (document.layers)
  {
    return;
  }

  if (typeof obj == "string")
  {
    obj = document.getElementById(obj);
  }

  var y = 0;
  if (obj != null && obj.offsetTop != null)
  {
    y = obj.offsetTop;
  }

  if (obj != null && obj.offsetParent != null)
  {
    return y + i2uiComputeTop(obj.offsetParent);
  }
  return y;
}

/*
  Computes an object's left by traversing containership.

  obj - <tbd>

  return - none

  Compatibility:  IE, NS6
*/
function i2uiComputeLeft(obj)
{
  if (document.layers)
  {
    return;
  }

  if (typeof obj == "string")
  {
    obj = document.getElementById(obj);
  }

  var x = 0;
  if (obj != null && obj.offsetLeft != null)
  {
    x = obj.offsetLeft;
  }

  if (obj != null && obj.offsetParent != null)
  {
    return x + i2uiComputeLeft(obj.offsetParent);
  }
  return x;
}

function i2uiTreeTableAction(object_name, action)
{
  if (document.layers)
  {
    var item = document.layers[object_name];
    if (item)
    {
      // better if we could  colorize the whole row
      // even better if we could assign the class name
      item.bgColor = '#fff274';
      if (i2uiActiveTreeNode != null)
      {
        i2uiActiveTreeNode.bgColor = '#f7f8fd';
      }
      i2uiActiveTreeNode = item;
    }
  }
  else
  {
    // find owning row and table object
    var owningrow = null;
    var owningtable = null;
    var object = document.getElementById(object_name);
    while (object)
    {
      if (object.tagName == 'TR')
      {
        owningrow = object;
      }
      else
      if (object.tagName == 'TABLE')
      {
        owningtable = object;
        break;
      }
      //object = object.parentElement;
      object = object.parentNode;
    }

    if (owningtable)
    {
      // turn off previous object within tree
      try
      {
        var priorselected = eval(owningtable.id+"ActiveTreeNode");
        var priorclass = eval(owningtable.id+"ActiveTreeNodeClassname");
        priorselected.className=priorclass;
      }
      catch(e)
      {
      }

      // retain newly selected row and active class in private variables per tree
      eval(owningtable.id+"ActiveTreeNodeClassname = owningrow.className");
      owningrow.className = "rowHighlight";
      eval(owningtable.id+"ActiveTreeNode=owningrow");
    }
  }
  eval(action);
  //if (window.parent && window.parent.results)
  //{
  //  window.parent.results.location.href = action;
  //}
}


function i2uiduallistboxcopyit(fromlistbox, tolistbox, picked)
{
  if (picked == null)
    picked = fromlistbox.selectedIndex;
  if (picked >= 0)
  {
    var len = tolistbox.options.length;
    tolistbox.options.length++;
    tolistbox.options[len].text  = fromlistbox.options[picked].text;
    tolistbox.options[len].value = fromlistbox.options[picked].value;
    fromlistbox.options[picked].selected = false;
  }

  // process next selected option if multiple
  if (fromlistbox.selectedIndex >= 0)
    i2uiduallistboxcopyit(fromlistbox, tolistbox);
}
function i2uiduallistboxmoveit(fromlistbox, tolistbox, picked)
{
  if (picked == null)
    picked = fromlistbox.selectedIndex;
  if (picked >= 0)
  {
    var len = tolistbox.options.length;
    tolistbox.options.length++;
    tolistbox.options[len].text = fromlistbox.options[picked].text;
    tolistbox.options[len].value = fromlistbox.options[picked].value;
    fromlistbox.options[picked].selected = false;
    if (document.layers)
    {
      var len2 = fromlistbox.options.length;
      for (var i=picked; i<len2-1; i++)
      {
        fromlistbox.options[i].text     = fromlistbox.options[i+1].text;
        fromlistbox.options[i].value    = fromlistbox.options[i+1].value;
        fromlistbox.options[i].selected = fromlistbox.options[i+1].selected
      }
      if (len2 > 0)
        fromlistbox.options.length--;
    }
    else
    if (document.all)
    {
      // remove not available in Netscape 6.x
      fromlistbox.options.remove(picked);
    }
    else
    {
      fromlistbox.options[picked] = null;
    }

  }

  // process next selected option if multiple
  if (fromlistbox.selectedIndex >= 0)
    i2uiduallistboxmoveit(fromlistbox, tolistbox);
}
function i2uiduallistboxmoveall(fromlistbox, tolistbox)
{
  while(fromlistbox.options.length > 0)
  {
    i2uiduallistboxmoveit(fromlistbox, tolistbox, 0);
  }
}
function i2uiduallistboxcopyall(fromlistbox, tolistbox)
{
  var len = fromlistbox.options.length;
  for (var i=0; i<len; i++)
  {
    i2uiduallistboxcopyit(fromlistbox, tolistbox, i);
  }
}

/*
  Resizes the region to the appropriate width and height

  insideframe - 'nonframedshell' indicates region is placed
                even lower from top of owning window
  return - none

  Compatibility:  IE, NS6
*/
function i2uiResizeRegion(insideframe)
{
  if (document.layers)
    return;

  //i2uitrace(1,"resizeregion ------------");
  var region = document.getElementById('i2uiregion');
  if (region != null)
  {
    var regiontop = i2uiComputeTop('i2uiregion');
    //i2uitrace(1,"resizeregion window height="+document.body.offsetHeight+" top="+regiontop);

    var newheight;
    if (navigator.appName == "Netscape")
    {
      var padding = window.innerWidth - document.body.offsetWidth;
      newheight = Math.max(window.innerHeight - (3 * padding) + 2,
                           document.body.offsetHeight);
      //i2uitrace(1,"innerheight="+window.innerHeight+" offsetheight="+document.body.offsetHeight+" scrollheight="+i2uiComputeScrollHeight('i2uiregion')+" computed="+newheight);
    }
    else
      newheight = document.body.offsetHeight;
    newheight -= regiontop;
    //i2uitrace(1,"resizeregion new height="+newheight);

    // if the shell is not framed,
    // then we must reduce our height by amount of space below the region
    if (insideframe != null && insideframe == 'nonframedshell')
    {
      newheight -= 18;
      //i2uitrace(1,"resizeregion adjusted height="+newheight);
    }

    // only IE makes each scroller addressable
    if (document.all)
    {
      if (newheight < region.scrollHeight)
        region.style.overflowY="auto";
      else
        region.style.overflowY="hidden";

      if (region.scrollWidth < region.offsetWidth)
        region.style.overflowX="hidden";
      else
        region.style.overflowX="auto";
    }
    else
    {
      //Netscape 6.2 acting funny about width when enclosing frame has marginWidth
      // if scrolling, then remove its width from equation
      if (padding > i2uiScrollerWidth)
        padding -= i2uiScrollerWidth;
      // modifier needs to be lesser of 14 and 14-frame's marginWidth
      var modifier = Math.min(14, 14-padding/2);
      var newwidth = document.body.offsetWidth - modifier;

      //i2uitrace(1,"assigned new width. innerwidth="+window.innerWidth+" offsetwidth="+document.body.offsetWidth+" newwidth="+newwidth+" padding="+padding+" modifier="+modifier);
      region.style.width = newwidth;
    }

    region.style.height = newheight;
  }
}


/* constructs a breadcrumbs object */
function i2uiBreadcrumbs() {
  this.speed = 40;
  this.iens6 = document.all||document.getElementById;
  this.ns4 = document.layers;
}
/* initializes a breadcrumb object. called after the breadcrumb html is rendered */
i2uiBreadcrumbs.prototype.init = function () {
  if (this.iens6){
    this.crossobj=document.getElementById?document.getElementById("breadcrumbsContent"):document.all.breadcrumbsContent;
    this.contentwidth=this.crossobj.offsetWidth;
    this.containerobj=document.getElementById?document.getElementById("breadcrumbsContainer"):document.all.breadcrumbsContainer;
    this.appobj=document.getElementById?document.getElementById("breadcrumbsApp"):document.all.breadcrumbsApp;
    this.imgobj=document.getElementById?document.getElementById("breadcrumbsImg"):document.all.breadcrumbsImg;
    this.leftobj=document.getElementById?document.getElementById("breadcrumbsLeft"):document.all.breadcrumbsLeft;
    this.rightobj=document.getElementById?document.getElementById("breadcrumbsRight"):document.all.breadcrumbsRight;

  } else if (this.ns4) {
    this.crossobj=document.breadcrumbsContainer.document.breadcrumbsContent;
    this.contentwidth=this.crossobj.clip.width;
  }
}
i2uiBreadcrumbs.prototype.toString = function () { return "Breadcrumbs"; }
/* Sets the width of the breadcrumbs.  Should be called in an onload handler */
function i2uiSetBreadcrumbsWidth () {

if   (breadcrumbs == null) return;
var breadcrumbswidth = parseInt(document.body.offsetWidth)- parseInt(breadcrumbs.appobj.offsetWidth) - parseInt(breadcrumbs.imgobj.offsetWidth) - parseInt(breadcrumbs.leftobj.offsetWidth) -parseInt(breadcrumbs.rightobj.offsetWidth) - 60;
if (breadcrumbswidth > 0) breadcrumbs.containerobj.style.width =breadcrumbswidth;

//alert("document width " + document.body.offsetWidth);
//alert("app width " + breadcrumbs.appobj.offsetWidth);
//alert("img width " + breadcrumbs.imgobj.offsetWidth);
//alert("left width " + breadcrumbs.leftobj.offsetWidth);
//alert("rig width " + breadcrumbs.rightobj.offsetWidth);

//alert("container width " + breadcrumbs.containerobj.style.width);
//alert("content width " + breadcrumbs.crossobj.offsetWidth);
maxWidth= parseInt(breadcrumbs.containerobj.style.width);

  if (breadcrumbs.iens6) {
    breadcrumbs.contentwidth = breadcrumbs.crossobj.offsetWidth;
    /* Sudhir: To Scroll till the end of bread crumbs
      So that the current is visible.
    */
    if ( breadcrumbs.contentwidth > maxWidth)
    {
      breadcrumbs.speed =   breadcrumbs.contentwidth - maxWidth;

      breadcrumbs.scrollHorizontal(0);
      breadcrumbs.speed = 40;
      i2uiToggleItemVisibility('breadcrumbsRight','show')
    }
    else
    {
      breadcrumbs.crossobj.style.left=0;
      i2uiToggleItemVisibility('breadcrumbsRight','hide')
    }

  }
  //breadcrumbs.speed = initialBreadcrumbsOffset;
  //breadcrumbs.scrollHorizontal(0);
  breadcrumbs.speed = 40;

}
/* Sets the scroll speed of the breadcrumbs tag, i.e., how far the
   breadcrumbs are moved for each mouse click.
 */
function i2uiSetBreadcrumbsScrollSpeed (speed) {
  breadcrumbs.speed = speed;
}
/* scrolls breadcrumbs left and right.  dir=0 for left; dir=1 for right */
i2uiBreadcrumbs.prototype.scrollHorizontal = function (dir) {
  if (dir == 0) {
//alert(this.crossobj.style.left)
//alert((this.crossobj.offsetWidth))
//alert((this.crossobj.contentWidth))

  //  alert(this.contentwidth)
    if (this.iens6 && (parseInt(this.crossobj.style.left) -5 ) > (this.contentwidth*(-1) )) {
   // alert("in")
    ;
      this.crossobj.style.left = parseInt(this.crossobj.style.left) - this.speed;

      i2uiToggleItemVisibility('breadcrumbsRight','show')

      maxWidth= parseInt(breadcrumbs.containerobj.style.width);

      if ( parseInt(this.crossobj.style.left) <= (maxWidth - this.contentwidth))
      {
        i2uiToggleItemVisibility('breadcrumbsLeft','hide')
      }
    }
    else if (this.ns4 && this.crossobj.left > (this.contentwidth*(-1) + this.speed)) {
      this.crossobj.left -= this.speed;
    }
  } else if (dir == 1) {



    if (this.iens6 && parseInt(this.crossobj.style.left) < 0 ) {
      //alert("speed = " + this.speed);
      //alert("left = " + this.crossobj.style.left);
      var maxDelta = Math.min(this.speed, (0 - parseInt(this.crossobj.style.left)));
      //alert("maxDelta = " + maxDelta);
      this.crossobj.style.left = parseInt(this.crossobj.style.left) + maxDelta;
      i2uiToggleItemVisibility('breadcrumbsLeft','show')


      if (maxDelta == 0) i2uiToggleItemVisibility('breadcrumbsRight','hide')
      if (parseInt(this.crossobj.style.left)   == 0)
        i2uiToggleItemVisibility('breadcrumbsRight','hide')
    }
    else if (this.ns4 && this.crossobj.left < 0) {
      var maxDelta = Math.min(this.speed, (0 - this.crossobj.left));
      this.crossobj.left += maxDelta;
    }
    else
    {
        i2uiToggleItemVisibility('breadcrumbsRight','hide')
    }
  }
}

/*
  This routine toggles the visibility of the navarea object itself or
  its containing frame.

  name - the navarea name (i2uinavarea) or the owning frame's name

  return - none

  Compatibility:  IE, NS6
*/

function i2uiToggleNavarea(name)
{
  if (document.layers)
    return;
  var item = document.getElementById(name);
  if (item != null)
  {
    if (item.tagName == "IFRAME" ||
        item.tagName == "DIV")
    {
      if (item.style.display == "none")
      {
        item.style.display = "";
        item.style.visibility = "visible";
      }
      else
      {
        item.style.display = "none";
      }
    }
    else
    if (item.tagName == "FRAME")
    {
      // split owning frameset into array of frame widths
      var colarray = item.parentElement.cols.split(",");

      // build new frameset definition
      var result = "";
      for (var i=0; i<item.parentElement.children.length; i++)
      {
        // if located desired frame
        if (item.parentElement.children[i].name == name)
        {
          if (item.style.display == "none")
          {
            item.style.display = "";
            item.style.visibility = "visible";
            colarray[i] = 170;
          }
          else
          {
            item.style.display = "none";
            colarray[i] = 0;
          }
        }
        if (i > 0)
          result += ",";
        result += colarray[i];
      }

      // write new framset definition
      item.parentElement.cols = result;
    }
  }
}
/*
  This routine scrolls the tabs to the right

  id - the tabset id

  return - none

  Compatibility:  IE,NS6
*/
function i2uiScrollTabsRight(id)
{
  if (document.layers)
    return;
  var obj = document.getElementById(id);
  if (obj != null)
  {
    var tablen = obj.rows[0].cells.length;

    var showid = 0;

    // show last invisible tab from left
    for (var i=1; i<tablen-4; i++)
    {
      if (obj.rows[0].cells[i].style.display == "none")
      {
        showid = i;
        i += 3;
      }
      else
      if (showid > 0)
      {
        if (showid == 1)
          i2uiToggleItemVisibility(id+'_tabscrollerright','hide');
        else
          i2uiToggleItemVisibility(id+'_tabscrollerright','show');

        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid++;
        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid++;
        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid++;
        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid++;

        // show all other tabs
        for (var j=showid; j<tablen-2; j++)
        {
          obj.rows[0].cells[j].style.display = "";
          obj.rows[0].cells[j].style.visibility = "visible";
        }

        // now hide just enough of those on right
        i2uiManageTabs(id,null,"back",showid-4,null);

        break;
      }
    }
  }
}

/*
  This routine scrolls the tabs to the left

  id - the tabset id

  return - none

  Compatibility:  IE,NS6
*/
function i2uiScrollTabsLeft(id)
{
  if (document.layers)
    return;
  var obj = document.getElementById(id);
  if (obj != null)
  {
    var tablen = obj.rows[0].cells.length;

    var showid = 0;
    // show last invisible tab from right
    for (var i=tablen-3; i>4; i--)
    {
      if (obj.rows[0].cells[i].style.display == "none")
      {
        showid = i;
        i -= 3;
      }
      else
      if (showid > 0)
      {
        if (showid == 1)
          i2uiToggleItemVisibility(id+'_tabscrollerright','hide');
        else
          i2uiToggleItemVisibility(id+'_tabscrollerright','show');

        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid--;
        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid--;
        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid--;
        obj.rows[0].cells[showid].style.display = "";
        obj.rows[0].cells[showid].style.visibility = "visible";
        showid--;

        // show all other tabs
        for (var j=showid; j>0; j--)
        {
          obj.rows[0].cells[j].style.display = "";
          obj.rows[0].cells[j].style.visibility = "visible";
        }

        // now hide just enough of those on left
        i2uiManageTabs(id,null,"front",null,showid+4);

        break;
      }
    }
  }
}

/*
  This routine manages the scrolling of tabs

  id - the tabset id
  allowedwidth - width to which tabs should be restricted
  direction - whether manage from the front or back

  return - none

  Compatibility:  IE,NS6
*/
function i2uiManageTabs(id, allowedwidth, direction, hiddenleft, hiddenright)
{
  if (document.layers)
    return;
  //i2uitrace(0,"i2uiManagetabs id="+id+" direction="+direction);

  var obj = document.getElementById(id);
  if (obj != null)
  {
    if (allowedwidth == null)
    {
      eval("allowedwidth = "+id+"_allowed_width");
      if (allowedwidth == 0)
        allowedwidth = document.body.offsetWidth - 40;
    }
    else
    {
      eval(id+"_allowed_width="+allowedwidth);
    }
    //i2uitrace(0,"i2uiManagetabs allowedwidth="+allowedwidth);

    var tablen = obj.rows[0].cells.length;

    var widthbefore = 0;
    if (direction == "front")
    {
      for (var i=1; i<tablen-4; i++)
      {
        // may not reduce as items are hidden
        widthbefore = obj.offsetWidth;

        // if they fit or only 1 left
        if (obj.offsetWidth <= allowedwidth ||
            obj.rows[0].cells[i+4].style.display == "none")
        {
          hiddenleft = i;
          break;
        }
        obj.rows[0].cells[i].style.display = "none";
        i++;
        obj.rows[0].cells[i].style.display = "none";
        i++;
        obj.rows[0].cells[i].style.display = "none";
        i++;
        obj.rows[0].cells[i].style.display = "none";

        // quirk
        if (widthbefore == obj.offsetWidth)
        {
          //i2uitrace(0,"i2uiManagetabs bailing out at i="+i);

          for (var j=i; j>i-4; j--)
          {
            obj.rows[0].cells[j].style.display = "";
            obj.rows[0].cells[j].style.visibility = "visible";
          }
          hiddenleft = j;
          break;
        }
      }
    }
    else
    {
      for (var i=tablen-3; i>3; i--)
      {
        // may not reduce as items are hidden
        widthbefore = obj.offsetWidth;

        // if they fit or only 1 left
        if (obj.offsetWidth <= allowedwidth ||
            obj.rows[0].cells[i-4].style.display == "none")
        {
          hiddenright = i;
          break;
        }
        obj.rows[0].cells[i].style.display = "none";
        i--;
        obj.rows[0].cells[i].style.display = "none";
        i--;
        obj.rows[0].cells[i].style.display = "none";
        i--;
        obj.rows[0].cells[i].style.display = "none";

        // quirk
        if (widthbefore == obj.offsetWidth)
        {
          //i2uitrace(0,"i2uiManagetabs bailing out at i="+i);
          for (var j=i; j<i+4; j++)
          {
            obj.rows[0].cells[j].style.display = "";
            obj.rows[0].cells[j].style.visibility = "visible";
          }
          hiddenright = j;
          break;
        }
      }
    }

    //i2uitrace(0,"&nbsp;&nbsp;tablen="+tablen+" hiddenleft="+hiddenleft+" hiddenright="+hiddenright);

    // if last tab is not visible, then show right scroller
    if (obj.rows[0].cells[tablen-3].style.display == "none")
    {
      i2uiToggleItemVisibility(id+'_tabscrollerright','show');
      var icon = document.getElementById(id+'_tabscrollerright');
      if (icon != null)
        icon.alt = Math.ceil((tablen - hiddenright - 3) / 4) + " \xed\xaf\xec ";
    }
    else
    {
      i2uiToggleItemVisibility(id+'_tabscrollerright','none');
    }

    // if first tab is not visible, then show left scroller
    if (obj.rows[0].cells[1].style.display == "none")
    {
      i2uiToggleItemVisibility(id+'_tabscrollerleft','show');
      var icon = document.getElementById(id+'_tabscrollerleft');
      if (icon != null)
        icon.alt = Math.ceil((hiddenleft - 1) / 4) + " \xed\xaf\xec ";
    }
    else
    {
      i2uiToggleItemVisibility(id+'_tabscrollerleft','none');
    }
  }
}

/*
  This routine resets the scrolling of tabs.  Useful in onresize handlers

  id - the tabset id

  return - none

  Compatibility:  IE
*/
function i2uiResetTabs(id, allowedwidth)
{
  if (document.layers)
    return;

  var obj = document.getElementById(id);
  if (obj != null)
  {
    if (allowedwidth == null)
    {
      eval("allowedwidth = "+id+"_allowed_width");
      if (allowedwidth == 0)
        allowedwidth = document.body.offsetWidth - 40;
    }
    else
    {
      eval(id+"_allowed_width="+allowedwidth);
    }

    var tablen = obj.rows[0].cells.length;

    // turn on all tabs
    for (var i=1; i<tablen-3; i++)
    {
      obj.rows[0].cells[i].style.display = "";
      obj.rows[0].cells[i].style.visibility = "visible";
    }

    i2uiManageTabs(id,allowedwidth,"back");
  }
}

/*
  Computes and optionally assigns the scroll height of an object

  id - the id of some object
  assign - true/flase as to whether the assignment should be made

  return - a number

  Compatibility:  IE, NS6
*/
function i2uiComputeScrollHeight(id, assign)
{
  if (document.layers)
    return 0;

  var height = 0;
  var unknown = 0;
  var obj = document.getElementById(id)
  if (obj != null)
  {
    if (document.all)
      return obj.scrollHeight;

    var kids = obj.childNodes;
    if (kids != null)
    {
      for (var i=0; i<kids.length; i++)
      {
        // some node types have no value for offsetHeight
        if (!(kids[i].offsetHeight == undefined))
        {
          height += kids[i].offsetHeight;
        }
        else
        {
          unknown++;
        }
      }
    }
    if (assign != null && assign == true)
      obj.scrollHeight = height;
  }
  //i2uitrace(1,"ComputeScrollHeight for "+id+" height="+height+" content="+obj.offsetHeight+" unknown="+unknown);
  return height;
}

/*
  Computes and optionally assigns the scroll width of an object

  id - the id of some object
  assign - true/false as to whether the assignment should be made

  return - a number

  Compatibility:  IE, NS6
*/
function i2uiComputeScrollWidth(id, assign)
{
  if (document.layers)
    return 0;

  var width = 0;
  var unknown = 0;
  var obj = document.getElementById(id)
  if (obj != null)
  {
    if (document.all)
      return obj.scrollWidth;

    var kids = obj.childNodes;
    if (kids != null)
    {
      for (var i=0; i<kids.length; i++)
      {
        // some node types have no value for offsetHeight
        if (!(kids[i].offsetWidth == undefined))
        {
          width += kids[i].offsetWidth;
        }
        else
        {
          unknown++;
        }
      }
    }
    if (assign != null && assign == true)
      obj.scrollWidth = width;
  }
  //i2uitrace(1,"ComputeScrollWidth for "+id+" width="+width+" content="+obj.offsetWidth+" unknown="+unknown);
  return width;
}


/*
  Returns whether browser version is suitable or not

  return - true/false

  Compatibility:  IE, NS4, NS6
*/
function i2uiIsSuitableBrowser()
{
  var rc = true;
  var at = navigator.userAgent.indexOf("Netscape6");
  if (at > 0)
  {
    var release = navigator.userAgent.substr(at+10);
    if (release <6.2)
      rc = false;
  }
  return rc;
}


/*
  Renders a modal dialog box
  return - english string according to button pressed

  Compatibility:  IE, NS6
*/
var i2uiMessageBoxRC = null;
function i2uiShowMessageBox(url, height, width)
{
  if (height == null)
    height = 150;
  else
    height = Math.max(150, height);
  if (width == null)
    width = 350;
  else
    width = Math.max(350, width);

  i2uiMessageBoxRC = null;
  if (!document.layers)
  {
    if (document.all)
      i2uiMessageBoxRC = showModalDialog(url,"","dialogWidth:"+width+"px;dialogheight:"+height+"px;status:no;unadorned:yes;help:no;resizable:yes");
    else
    {
      var screenX = (screen.availWidth  - width)  / 2;
      var screenY = (screen.availHeight - height) / 2;
      var dialogWindow = window.open(url,"dlog","screenX="+screenX+"px,screenY="+screenY+"px,width="+width+"px,height="+height+"px,modal=yes");
    }
  }
  return i2uiMessageBoxRC;
}
/* called by modal dialog; do not call directly */
function i2uiCloseMessageBox(returnValue)
{
  if (document.all)
    window.returnValue = returnValue;
  else
    opener.i2uiMessageBoxRC = returnValue;
  window.close();
}
/* set focus to last button in dialog. should be called in onload handler */
function i2uiMessageBoxInit()
{
  var obj = document.getElementsByTagName('button');
  if (obj != null)
  {
    obj[obj.length-1].focus();
  }
}

// make the datepicker dialog width and height locale dependent
// -- for some unfathomable reason IE requires larger dimensions to get the dialog to size properly
var i2uiDatePickerWidthTable = new Object();
i2uiDatePickerWidthTable["en"] = 149;
i2uiDatePickerWidthTable["en-IE"] = 155;
i2uiDatePickerWidthTable["de"] = 161;
i2uiDatePickerWidthTable["de-IE"] = 173;
i2uiDatePickerWidthTable["fr"] = 176;
i2uiDatePickerWidthTable["fr-IE"] = 188;
i2uiDatePickerWidthTable["ja"] = 148;
i2uiDatePickerWidthTable["ja-IE"] = 154;
i2uiDatePickerWidthTable["ko"] = 148;
i2uiDatePickerWidthTable["ko-IE"] = 154;

var i2uiDatePickerHeightTable = new Object();
i2uiDatePickerHeightTable["en"] = 200;
i2uiDatePickerHeightTable["en-IE"] = 225;
i2uiDatePickerHeightTable["de"] = 200;
i2uiDatePickerHeightTable["de-IE"] = 225;
i2uiDatePickerHeightTable["fr"] = 200;
i2uiDatePickerHeightTable["fr-IE"] = 225;
i2uiDatePickerHeightTable["ja"] = 200;
i2uiDatePickerHeightTable["ja-IE"] = 227;
i2uiDatePickerHeightTable["ko"] = 200;
i2uiDatePickerHeightTable["ko-IE"] = 227;

var i2uiDatePickerJspDir = null;
var i2uiDatePickerRC = null;

// ************************************************************************
//  Renders a datepicker in a modal dialog box
//  return - the selected date
function i2uiDatePickerCreateAndPost(refDate, localeStr, screenX, screenY, earliestDate, latestDate)
{
  i2uiDatePickerRC = null;
  if (!document.layers)
  {
    // handle the reference, earliest, and latest dates
    var refDateStr = "null";
    var earliestDateStr = "null";
    var latestDateStr = "null";
    if (refDate != null)
    {
      // capture it as a string
      refDateStr = refDate.getFullYear() + "-" + refDate.getMonth() + "-" + refDate.getDate();
    }
    if (earliestDate != null)
    {
      // capture it as a string
      earliestDateStr = earliestDate.getFullYear() + "-" + earliestDate.getMonth() + "-" + earliestDate.getDate();
    }
    if (latestDate != null)
    {
      // capture it as a string
      latestDateStr = latestDate.getFullYear() + "-" + latestDate.getMonth() + "-" + latestDate.getDate();
    }

    // look up the width and height
    var key = null;
    if (localeStr != null)
    {
      key = localeStr.substr(0,2);
    }
    else
    {
      key = document.all != null ? navigator.userLanguage : navigator.language;
      key = key.substr(0,2);
    }
    if (document.all != null) key += "-IE";
    var width = i2uiDatePickerWidthTable[key] != null ? i2uiDatePickerWidthTable[key] : (document.all != null ? 155 : 149);
    var height = i2uiDatePickerHeightTable[key] != null ? i2uiDatePickerHeightTable[key] : (document.all != null ? 229 : 204);
    //alert("key == " + key + "; width ==" + width + "; height == " + height);

    // handle X and Y
    var xyStr = "";
    if (screenX != null && screenY != null)
    {
      xyStr = document.all ? ";dialogLeft:"+screenX+";dialogTop:"+screenY
                           : ",screenX="+screenX+",screenY="+screenY;
    }

    var url="i2uidatepickerdialog.jsp?id=datepickerDialog&refdate="+refDateStr+
                                    "&earliestdate="+earliestDateStr+
                                    "&latestdate="+latestDateStr+
                                    (localeStr != null ? "&locale="+localeStr : "");
    if (i2uiDatePickerJspDir != null)
    {
      url = i2uiDatePickerJspDir + "/" + url;
    }

    if (document.all)
    {
      i2uiDatePickerRC = showModalDialog(url,"","dialogWidth:"+width+"px;dialogheight:"+height+"px;status:no;unadorned:yes;help:no"+xyStr);
    }
    else
    {
      var dialogWindow = window.open(url,"datepicker","width="+width+",height="+height+",modal=yes"+xyStr);
    }
  }
  // both browsers should return the selected date as a UTC format date string
  return i2uiDatePickerRC != null ? new Date(i2uiDatePickerRC) : null;
}

// called by the modal dialog; do not call directly
function i2uiDatePickerClose(returnValue)
{
  // capture the returned Date as a UTC string
  if (returnValue != null) returnValue = returnValue.toUTCString();

  if (document.all)
  {
    window.returnValue = returnValue;
  }
  else
  {
    opener.i2uiDatePickerRC = returnValue;
  }

  window.close();
}

// register the Jsp directory - called from a code fragment generated by <i2:dhtml>
function i2uiDatePickerSetJspDir (dir)
{
  i2uiDatePickerJspDir = dir;
}

/* support for row selector */
var i2uiActiveRowSelector = null;
var i2uiInvokeCallback = true;
function i2uiToggleRowSelectionState(obj, originalstate, tableid, treecell, multiSelect)
{
  // NS4 browser not supported
  if (document.layers != null)
    return;

  if (i2uiActiveRowSelector != null)
    obj = i2uiActiveRowSelector;
  else
  {
    // turn off any global selector
    var globalselector = document.getElementById(tableid+"_globalrowselector");
    if (globalselector != null)
      globalselector.checked = false;
  }
  
  //pglalert( 'i2uiToggleRowSelectionState() tableId=' + tableid );

  // if single select, turn off previous row
  if (multiSelect == false)
  {
    // test for scrollable table
    var table = document.getElementById(tableid+"_data");
    if (table == null)
      table = document.getElementById(tableid);

    var len = table.rows.length;
    for (var i=0; i<len; i++)
    {
      if (table.rows[i].lastClassName != null)
      {
        table.rows[i].className = table.rows[i].lastClassName;
        if (table.rows[i].cells[0].childNodes &&
            table.rows[i].cells[0].childNodes.length > 0 &&
            table.rows[i].cells[0].childNodes[0].style != null)
          table.rows[i].cells[0].childNodes[0].style.backgroundColor = "";
        table.rows[i].lastClassName = null;
      }
    }
  }

  // find owning row
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
  var rowIndex = -1;
  
  if (rowobj != null)
  {
    var rowTable = null;
    
    if (rowobj.parentElement)
    {
      rowTable = rowobj.parentElement;
    }
    else
    {
      rowTable = rowobj.parentNode;
    
    }
    //pglalert( 'rowTable.id = ' + rowTable.id + ', tableid=' + tableid );
    
    if( rowTable.id == tableid )
    {
      //pglalert( 'rowTable.id == tableid' );
    }
    else
    {  
      //pglalert( 'rowTable.id != tableid, searching for table' );
      var rowIndex = -1;
      
      for( var i=0; i < rowTable.rows.length; i++ )
      {
        if( rowTable.rows[i] == rowobj )
        {
          rowIndex = i;
          break;          
        }
      }
      
      //pglalert( 'rowIndex = ' + rowIndex );
      
      if( rowIndex > -1 )
      {
        var otherTable = document.getElementById(tableid+"_data");
        if (otherTable == null)
        {
          otherTable = document.getElementById(tableid);
        }
        
        rowobj = otherTable.rows[rowIndex];
      }
      else      
      {
        //pglalert( 'could not find matching row' );
      }
    }
  
  
    rowobj.className = obj.checked?"rowHighlight":originalstate;
    rowobj.lastClassName = originalstate;
    //rowobj.cells[0].childNodes[0].style.backgroundColor = "";
  }

  // handle treetable
  if (treecell != null &&
      treecell != '' &&
      i2uiActiveRowSelector == null)
  {
    var cellname = rowobj.cells[treecell].id.substring(9);
    var depth1 = Math.floor(cellname);
    var table = document.getElementById(tableid);
    var len = table.rows.length;
    for (var i=0; i<len; i++)
    {
      // locate desired row in table
      if (table.rows[i].cells[treecell].id == "TREECELL_"+cellname)
      {
        // now process rest of table with respect to located
        for (var j=i+1; j<len; j++)
        {
          var newcell = table.rows[j].cells[treecell].id.substr(9);
          var depth2 = Math.floor(newcell);
          if ((depth2 == depth1 + 10 || depth2 == depth1 + 15) ||
              (depth2 > depth1 + 5))
          {
            table.rows[j].cells[0].childNodes[0].checked = obj.checked;
            if (obj.checked)
            {
              table.rows[j].className = "rowHighlight";
              table.rows[j].cells[0].childNodes[0].style.backgroundColor = "";
            }
            else
            {
              var onclickhandler = table.rows[j].cells[0].childNodes[0].onclick+"!!!";
              var from = onclickhandler.indexOf("{");
              onclickhandler = onclickhandler.substring(from+1);
              var to = onclickhandler.lastIndexOf("}");
              onclickhandler = onclickhandler.substring(0,to);
              i2uiActiveRowSelector = table.rows[j].cells[0].childNodes[0];
              eval(onclickhandler);
              i2uiActiveRowSelector = null;
            }
          }
          if (depth2 <= depth1)
          {
            break;
          }
        }

        var handle = depth1 - 10;
        // now set partial state of all parents of selected node
        for (var j=i-1; j>-1; j--)
        {
          var newcell = table.rows[j].cells[treecell].id.substr(9);
          var depth2 = Math.floor(newcell);
          if (depth2 == handle || depth2 == handle-5)
          {
            var selectedcount = 0;
            var nonselectedcount = 0;
            for (var k=j+1; k<len; k++)
            {
              var newcell2 = table.rows[k].cells[treecell].id.substr(9);
              var depth3 = Math.floor(newcell2);
              if ((depth3 == depth2 + 10 || depth3 == depth2 + 15) ||
                  (depth3 > depth2 + 5))
              {
                if (table.rows[k].cells[0].childNodes[0].checked)
                  selectedcount++;
                else
                  nonselectedcount++;
              }
              if (depth3 <= depth2)
              {
                break;
              }
            }

            if (selectedcount > 0)
            {
              if (nonselectedcount > 0)
              {
                table.rows[j].cells[0].childNodes[0].checked=false;
                var onclickhandler = table.rows[j].cells[0].childNodes[0].onclick+"!!!";
                var from = onclickhandler.indexOf("{");
                onclickhandler = onclickhandler.substring(from+1);
                var to = onclickhandler.lastIndexOf("}");
                onclickhandler = onclickhandler.substring(0,to);
                i2uiActiveRowSelector = table.rows[j].cells[0].childNodes[0];
                eval(onclickhandler);
                i2uiActiveRowSelector = null;
                table.rows[j].cells[0].childNodes[0].style.backgroundColor = "#fff6a6";
              }
              else
              {
                table.rows[j].cells[0].childNodes[0].checked = true;
                table.rows[j].className = "rowHighlight";
                table.rows[j].cells[0].childNodes[0].style.backgroundColor = "";
              }
            }
            else
            {
              table.rows[j].cells[0].childNodes[0].checked=false;
              table.rows[j].cells[0].childNodes[0].style.backgroundColor = "";

              if (nonselectedcount > 0)
              {
              var onclickhandler = table.rows[j].cells[0].childNodes[0].onclick+"!!!";
              var from = onclickhandler.indexOf("{");
              onclickhandler = onclickhandler.substring(from+1);
              var to = onclickhandler.lastIndexOf("}");
              onclickhandler = onclickhandler.substring(0,to);
              i2uiActiveRowSelector = table.rows[j].cells[0].childNodes[0];
              eval(onclickhandler);
              i2uiActiveRowSelector = null;
              }
            }
            // look up one more level
            handle -= 10;
            if (handle < 0)
              break;
          }
        }

        break;
      }
    }
  }
if (i2uiInvokeCallback)
    try{i2uiRowSelectionCallback(tableid)}catch(e){}

}
function i2uiToggleAllRowsSelectionState(obj,tableid)
{
  // NS4 browser not supported
  if (document.layers != null)
    return;

  var tableobj = document.getElementById(tableid);
  if (tableobj != null)
  {
    var checkboxes;

    // IE has a document object per element
    if(tableobj.document)
      checkboxes = tableobj.document.getElementsByTagName("INPUT");
    else
      checkboxes = document.getElementsByTagName("INPUT");

    i2uiInvokeCallback = false;
    if (checkboxes != null)
    {
      var len = checkboxes.length;
      for(var i=0; i<len; i++)
      {
	//eQ : 521353 - selecting only if the checkbox is enabled
        if (checkboxes[i].id == tableid+"_rowselector" && checkboxes[i].disabled == false)
      
        {
          i2uiActiveRowSelector = checkboxes[i];
          checkboxes[i].checked = obj.checked;
          var onclickhandler = checkboxes[i].onclick+"!!!";
          var from = onclickhandler.indexOf("{");
          onclickhandler = onclickhandler.substring(from+1);
          var to = onclickhandler.lastIndexOf("}");
          onclickhandler = onclickhandler.substring(0,to);
          eval(onclickhandler);
        }
      }
      i2uiInvokeCallback = true;
      try{i2uiRowSelectionCallback(tableid)}catch(e){}
      i2uiActiveRowSelector = null;
    }
  }
}

function i2uiGetSelectedRowNums(tableid)
{
  var selected = new Array();
  var rowSelectorName = "ID";
  var noOfRowSelector = 0;

  // NS4 browser not supported
  if (document.layers != null)
    return selected;

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
      
      for(var i=0; i<len; i++)
      {
        if (checkboxes[i].id == tableid+"_rowselector")
        {
          noOfRowSelector++;
          rowSelectorName = checkboxes[i].name;
          if (checkboxes[i].checked)
            selected[selected.length] = noOfRowSelector;
        }
      }
    }
    
    //Ganesh :: this is to handle "remember selections" across pages
    //In case of "remember selections", the previous page selections
    //are stored as hidden variables with the same name as row selector

    // IE has a document object per element
    if(tableobj.document)
      selectionsElements = tableobj.document.getElementsByName(rowSelectorName);
    else
      selectionsElements = document.getElementsByName(rowSelectorName);

    for(j=0; j<selectionsElements.length; j++)
    {
      if (selectionsElements[j].type == "hidden")
        selected[selected.length] = noOfRowSelector++;  
    }
  }
  return selected;
}
function i2uiGetSelectedRowIds(tableid)
{
  var selected = new Array();

  // NS4 browser not supported
  if (document.layers != null)
    return selected;

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
          if (checkboxes[i].checked)
          {
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
            var id = null;
            if (rowobj != null)
              id = rowobj.getAttribute("id");
            if (id != null && id.length > 0)
              selected[selected.length] = id;
            else
              selected[selected.length] = j;
          }
        }
      }
    }
  }
  return selected;
}

// desiredType will default to Regular
// other valid values are : Emphasized, SmallEmphasized, SmallRegular
function i2uiToggleButtonState(id, desiredState, desiredType)
{
  // valid for IE alone
  if (!document.all)  return;
  var obj = document.getElementById(id);
  if (obj == null) return;
  var cell = obj.rows[0].cells[0];
  var anchor = cell.childNodes[0];
  var state = obj.className;
  if (desiredState == null)
  {
    if (state == "buttonBorder")
      desiredState = "disabled";
    else
      desiredState = "enabled";
  }
  if (desiredState == "disabled")
  {
  	//checking if it is already disabled.
  	if (state == "buttonBorder")
  	{
	    obj.className = "buttonBorderDisabled";
	    cell.id = "buttonDisabled";
	    cell.className = "buttonTextDisabled";
	    var href = anchor.getAttribute("href");
	    if (href != null)
	    {
	      anchor.setAttribute("href2", href);
	      anchor.removeAttribute("href");
	    }
  	}
  }
  else
  {
    obj.className = "buttonBorder";
    if (desiredType == null)
      desiredType = "Regular";
    cell.id = "button"+desiredType;
    cell.className = "buttonText";
    var href = anchor.getAttribute("href2");
    if (href != null)
    {
      anchor.setAttribute("href", href);
      anchor.removeAttribute("href2");
    }
  }
}

function pglalert( msg )
{
  var doAlert = 'false';
  
  if( doAlert == 'true' )
  {
    alert( msg );
  }
}
