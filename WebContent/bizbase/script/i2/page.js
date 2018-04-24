i2uiManageTreeTableUserFunction = 'onClickNorgie';
i2uiToggleContentUserFunction = 'onClickNorgie';

var useGeneratedScrollHelper = true;

function onClickNorgie(item, delta)
{
  onResize(); 
}

function getResizeToWidth( specWidth, totalAvailWidth)
{
  var resizeToWidth = 0;
  var lenWidthStr = specWidth.length;
  
  //if width specified
  if ( specWidth.length > 0 )
  {
    // if width specified in percent            
    if ( specWidth.substring(lenWidthStr-1) == '%' )
    {
      resizeToWidth = specWidth.substring(0,lenWidthStr-1)/100;
      resizeToWidth = resizeToWidth * totalAvailWidth;
    }
    //if not percent, take it as is
    else
      resizeToWidth = specWidth;
  }
  else
    //if width not specified, assume it should occupy the maximum width allowed
    resizeToWidth = totalAvailWidth;
    
  return resizeToWidth;
}

function getResizeToHeight( specHeight, totalAvailHeight)
{
  var resizeToHeight = 0;
  var lenHeightStr = specHeight.length;
  
  //if height specified
  if ( specHeight.length > 0 )
  {
    // if height specified in percent            
    if ( specHeight.substring(lenHeightStr-1) == '%' )
    {
      resizeToHeight = specHeight.substring(0,lenHeightStr-1)/100;
      resizeToHeight = resizeToHeight * totalAvailHeight;
    }
    //if not percent, take it as is
    else
      resizeToHeight = totalAvailHeight;
  }
  else
    //if height not specified, assume it should occupy the maximum height allowed
    resizeToHeight = totalAvailWidth;
    
  return resizeToWidth;
}

function isContainer(obj)
{
  var len = obj.getElementsByTagName('TBODY').length;
  var isi2UIContainer = false;

  for (var i=0; i<len; i++)
  {
    var contenttbody = obj.getElementsByTagName('TBODY')[i];
    if (contenttbody.id == '_containerBody' ||
        contenttbody.id == '_containerbody' ||
        contenttbody.id == 'containerBodyIndent' ||
        contenttbody.id == 'containerbody')
    {
      isi2UIContainer = true;
      break;
    }
  }        
  
  //for tabs container
  if (obj.id == 'tabs_container')
    isi2UIContainer = true;

  return isi2UIContainer;
}

function isScrollableContainer( containerId )
{
  //check if scrollable      
  var scroller = document.getElementById( containerId+"_scroller" );
  //for tabbed container
  var tabsScroller = document.getElementById( containerId+"_description_scroller" );
  var isScrollable = false;
  
  if ( scroller != null )
    isScrollable = true;
  else if ( tabsScroller != null )
    isScrollable = true;

  return isScrollable;
}

function isOuterContainer(obj)
{
  if (obj.offsetParent)
  {
    while (obj.offsetParent)
    {
      if ( obj.offsetParent.id.length > 0 && isContainer(obj.offsetParent) )
      {
        return false;
      }
      obj = obj.offsetParent;
    }
  }
  return true;
}

function findPosX(obj)
{
  var curleft = 0;
  if (obj.offsetParent)
  {
    while (obj.offsetParent)
    {
      curleft += obj.offsetLeft
      obj = obj.offsetParent;
    }
  }
  else if (obj.x)
    curleft += obj.x;
  return curleft;
}

function findPosY(obj)
{
  var curtop = 0;
  if (obj.offsetParent)
  {
    while (obj.offsetParent)
    {
      curtop += obj.offsetTop
      obj = obj.offsetParent;
    }
  }
  else if (obj.y)
    curtop += obj.y;
  return curtop;
}

