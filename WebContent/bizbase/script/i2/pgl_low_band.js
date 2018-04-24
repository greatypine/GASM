/* pgl controls low bandwidth js */

    // Page.xsl js - Start::
    function initFrameToggleGif(path)
    {
      if(window == null ) return;
      if(window.frameElement == null ) return;
      if(window.frameElement.parentNode == null ) return;
      var frameCol = window.frameElement.parentNode.cols;
      if ( frameCol.charAt(0) == "0")
      {
        if (document.all)
        {
          toggle.title = "Show Navigation Frame";
          toggle.innerText = ">";
        }
      }
      else
      {
        if (document.all)
        {
          toggle.title = "Hide Navigation Frame";
          toggle.innerText = "<";
        }
      }
    }

    function togglenav(path)
    {
      if(window == null ) return;
      if(window.frameElement == null ) return;
      if(window.frameElement.parentNode == null ) return;
      var frameCol = window.frameElement.parentNode.cols;
      if ( frameCol.charAt(0) == "0")
      {
        if (document.all)
        {
          toggle.title = "Hide Navigation Frame";
          toggle.innerText = "<";
          window.frameElement.parentNode.cols="170,*";
          tabShow = 0;
          return;
        }
      }
      else
      {
        if (document.all)
        {
          toggle.title = "Show Navigation Frame";
          toggle.innerText = ">";
          window.frameElement.parentNode.cols="0%,100%";
          tabShow = 1;
        }
      }
    }
    // Page.xsl js - End::  

function tabsetScrollerButtons( cells, lt, endScroll )
{
  var cl = cells.length;
  var begin = cells[ cl - 8 ];
  var left = cells[ cl - 6 ];
  var right = cells[ cl - 4 ];
  var end = cells[ cl - 2 ];
  if( lt > 0 ){
    begin.style.cursor = 'hand';
    left.style.cursor = 'hand';
    begin.className = 'tabScrollLowBandTextC';
    left.className = 'tabScrollLowBandTextC';
  }
  else{
    begin.style.cursor = 'default';
    left.style.cursor = 'default';
    begin.className = 'tabScrollLowBandText';
    left.className = 'tabScrollLowBandText';
  }
  if( endScroll > 0 ){
    right.style.cursor = 'hand';
    end.style.cursor = 'hand';
    right.className = 'tabScrollLowBandTextC';
    end.className = 'tabScrollLowBandTextC';
  }
  else{
    right.style.cursor = 'default';
    end.style.cursor = 'default';
    right.className = 'tabScrollLowBandText';
    end.className = 'tabScrollLowBandText';
  }
}

function resizeTabsetScroller( table, w )
{
  if ( isV1() )
    return;

  if( w < 0 )
    w = document.body.clientWidth;
  var cells = table.rows[ 0 ].cells;
  var cl = cells.length;
  var filler = cells[ cl - 10 ];
  var first = 0;
  var last = cl - 10;
  var cw = 0;
  for( var j = 0; j < last; ++j ){
    var c = cells[ j ];
    c.style.display = '';
    cw += c.offsetWidth;
    if( ( j & 1 ) == 1 ){
      c.totalWidth = cw;
      cw = 0;
    }
  }
  while( ++j < cl )
    cells[ j ].style.display = 'none';
  if( filler.offsetWidth > 5 && table.offsetWidth <= w )
    return;
  for( var j = last + 1; j < cl; ++j )
    cells[ j ].style.display = '';
  var endScroll = 0;
  if( table.hasScrollerBegin == null ){
    for( var j = 0; j < last; j += 2 ){
      var c = cells[ j ];
      if( c.id == 'tabSelected' ){
        var e = j + 2;
        while( ( filler.offsetWidth <= 5  || table.offsetWidth > w ) && last > e ){
          cells[ --last ].style.display = 'none';
          cells[ --last ].style.display = 'none';
          ++endScroll;
        }
        while( filler.offsetWidth <= 5 || table.offsetWidth > w ){
          cells[ first++ ].style.display = 'none';
          cells[ first++ ].style.display = 'none';
        }
        table.scrollerBegin = first;
        break;
      }
    }
    table.hasScrollerBegin = true;
  }
  var lt = table.scrollerBegin;
  while( first < lt ){
    cells[ first++ ].style.display = 'none';
    cells[ first++ ].style.display = 'none';
  }
  while( filler.offsetWidth <= 5 || table.offsetWidth > w ){
    cells[ --last ].style.display = 'none';
    cells[ --last ].style.display = 'none';
    ++endScroll;
  }
  table.endScroll = endScroll;
  tabsetScrollerButtons( cells, lt, endScroll );
}

function moveTabsetScroller( table, gotolast )
{
  var w = document.body.clientWidth;
  var cells = table.rows[ 0 ].cells;
  var cl = cells.length;
  var filler = cells[ cl - 10 ];
  var first = 0;
  var last = cl - 10;
  var j = 0;
  while( j < last ){
    cells[ j++ ].style.display = 'none';
    cells[ j++ ].style.display = 'none';
  }
  var endScroll = 0;
  if( gotolast ){
    while( last > 0 ){
      var c = cells[ --last ];
      if( filler.offsetWidth < c.totalWidth )
        break;
      c.style.display = '';
      cells[ --last ].style.display = '';
    }
    table.scrollerBegin = ++last;
  }
  else{
    var first = table.scrollerBegin;
    while( first < last ){
      var c = cells[ first + 1 ];
      if( filler.offsetWidth < c.totalWidth )
        break;
      cells[ first++ ].style.display = '';
      c.style.display = '';
      ++first;
    }
    endScroll = ( last - first ) / 2;
  }
  table.endScroll = endScroll;
  tabsetScrollerButtons( cells, table.scrollerBegin, endScroll );
}

function i2uiScrollTab( td, dir )
{
  var table = td.parentNode.parentNode.parentNode;
  if( dir == 'begin' ){
    if( table.scrollerBegin > 0 ){
      table.scrollerBegin = 0;
      moveTabsetScroller( table );
    }
  }
  else if( dir == 'left' ){
    if( table.scrollerBegin > 0 ){
      table.scrollerBegin -= 2;
      moveTabsetScroller( table );
    }
  }
  else if( dir == 'right' ){
    var es = table.endScroll;
    if( es > 0 ){
      table.scrollerBegin += 2;
      moveTabsetScroller( table );
    }
  }
  else if( dir == 'end' ){
    var es = table.endScroll;
    if( es > 0 )
      moveTabsetScroller( table, true );
  }
}
