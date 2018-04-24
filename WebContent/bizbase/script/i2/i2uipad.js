/////////////////////////////////////////////////////////////////////////////
// (c) Copyright 2000 - 2001, i2 Technologies, Inc. (formerly Intellection)//
// ALL RIGHTS RESERVED.                                                    //
//                                                                         //
// This UNPUBLISHED PROPRIETARY software is  subject to the full copyright //
// notice in the COPYRIGHT file in this directory.                         //
/////////////////////////////////////////////////////////////////////////////

i2uiPad.width=160;
i2uiPad.delay1=200; // millisec before starting to repeat scrolling.
i2uiPad.delay2=10;  // millisec between each scrolling.
i2uiPad.barBackground        ="/scrollbar_background.jpg";
i2uiPad.cornerIcon           ="/scrollbar_corner.gif";
i2uiPad.upUpIcon             ="/scrollbar_up.gif";
i2uiPad.downUpIcon           ="/scrollbar_up_active.gif";
i2uiPad.noUpIcon             ="/scrollbar_up_disabled.gif";
i2uiPad.upDownIcon           ="/scrollbar_down.gif";
i2uiPad.downDownIcon         ="/scrollbar_down_active.gif";
i2uiPad.noDownIcon           ="/scrollbar_down_disabled.gif";
i2uiPad.topVerticalStub      ="/scrollbar_vert_slider_top.gif";
i2uiPad.middleVerticalStub   ="/scrollbar_vert_slider_middle.gif";
i2uiPad.bottomVerticalStub   ="/scrollbar_vert_slider_bottom.gif";
i2uiPad.upLeftIcon           ="/scrollbar_left.gif";
i2uiPad.downLeftIcon         ="/scrollbar_left_active.gif";
i2uiPad.noLeftIcon           ="/scrollbar_left_disabled.gif";
i2uiPad.upRightIcon          ="/scrollbar_right.gif";
i2uiPad.downRightIcon        ="/scrollbar_right_active.gif";
i2uiPad.noRightIcon          ="/scrollbar_right_disabled.gif";
i2uiPad.leftHorizontalStub   ="/scrollbar_horz_slider_left.gif";
i2uiPad.middleHorizontalStub ="/scrollbar_horz_slider_middle.gif";
i2uiPad.rightHorizontalStub  ="/scrollbar_horz_slider_right.gif";

i2uiPad.instances = new Array();
i2uiPad.count = 0;

i2uiPad.lastHighlightedPadItem = new Array();
i2uiPad.lastHighlightedPadItemName = new Array();

/* creates a Netscape layer to behave as a scrollable, titled container */
function i2uiPad(obj,title,editaction)
{
  i2uiPad.instances[i2uiPad.count] = this;

  var swidth,sheight;
  var visible='inherit';
  var x=10, y=10;
  var titleHeight = i2uiPadTitleHeight;
  var titleHeight2 = titleHeight-2;
  this.intervalcount = 0;
  this.refreshid = 0;
  this.index = i2uiPad.count;
  this.Vcoef=0.000001;

  swidth=obj.clip.width;
  sheight=obj.clip.height;
  visible=obj.visibility;

  var titlecolor;
  if (i2uiPad.count > 0)
  {
    titlecolor = i2uiApplicationPadTitleBgcolor;
  }
  else
  {
    // first pad is special - never scrolls
    titlecolor = i2uiSolutionPadTitleBgcolor;
    this.hbar = 'never';
    this.vbar = 'never';
  }

  var titleWidth = swidth - 2;
  if (editaction != "")
    editaction = '<a href="'+editaction+'"><img border=0 src='+i2uiImageDirectory+'/dropdown.gif></a>';
  var s=
  "<layer name=i2uipad>"+
    '<layer z-index=1 name=title height='+titleHeight+' width='+i2uiPad.width+' bgcolor='+i2uiPadBorderBgcolor+' visibility=hide>'+
      '<layer name=text top=1 left=1 height='+titleHeight2+' width='+titleWidth+' bgcolor='+titlecolor+'>'+
        '&nbsp;'+
        '<a href="javascript:i2uiPad.instances['+i2uiPad.count+'].toggleContents()">'+
        '<img border=0 name=toggler src='+i2uiImageDirectory+'/container_collapse.gif>'+
        '</a>'+
        '&nbsp;'+
        "<FONT POINT-SIZE='9' FACE='verdana,sans-serif'><B>"+
        title+
        '</B></FONT>'+
        '&nbsp;'+
        editaction+
      '</layer>'+
    '</layer>'+
    "<layer z-index=1 name=h width="+swidth+" visibility=hide>"+
      '<layer name=left top=0 left=0 bgcolor='+i2uiPadBorderBgcolor+'>'+
        '<img name=left src='+i2uiImageDirectory+i2uiPad.upLeftIcon+'>'+
      '</layer>'+
      '<layer name=right top=0 bgcolor='+i2uiPadBorderBgcolor+'>'+
        '<img name=right src='+i2uiImageDirectory+i2uiPad.upRightIcon+'>'+
      '</layer>'+
      '<layer name=empty top=0 left=16 background="'+i2uiImageDirectory+i2uiPad.barBackground+'" >'+
        '<layer name=rect left=0 top=0>'+
    '<layer><img src='+i2uiImageDirectory+i2uiPad.leftHorizontalStub+'></layer>'+
          '<layer left=2 height=16 background='+i2uiImageDirectory+i2uiPad.middleHorizontalStub+' ></layer>'+
    '<layer><img src='+i2uiImageDirectory+i2uiPad.rightHorizontalStub+'></layer>'+
  '</layer>'+
      '</layer>'+
    "</layer>"+
    "<layer top="+titleHeight+" z-index=1 name=v height="+sheight+" width=16 visibility=hide>"+
      '<layer name=up width=16 top=0 left=0 bgcolor='+i2uiPadBorderBgcolor+'>'+
        '<img name=up src='+i2uiImageDirectory+i2uiPad.upUpIcon+'>'+
  '<layer></layer>'+
      '</layer>'+
      '<layer name=down width=16 top=0 left=0 bgcolor='+i2uiPadBorderBgcolor+'>'+
        '<img name=down src='+i2uiImageDirectory+i2uiPad.upDownIcon+'>'+
      '</layer>'+
      '<layer name=empty left=0 top=16 background="'+i2uiImageDirectory+i2uiPad.barBackground+'">'+
        '<layer name=rect top=0>'+
    '<layer><img src='+i2uiImageDirectory+i2uiPad.topVerticalStub+'></layer>'+
    '<layer top=2 width=16 background='+i2uiImageDirectory+i2uiPad.middleVerticalStub+' ></layer>'+
    '<layer><img src='+i2uiImageDirectory+i2uiPad.bottomVerticalStub+'></layer>'+
  '</layer>'+
      '</layer>'+
    "</layer> "+
    '<layer z-index=1 name=resizer visibility=hide><img src="'+i2uiImageDirectory+i2uiPad.cornerIcon+'"></layer>';

  s+= '</layer>';
  document.write(s);

  var layer=document.layers.i2uipad;
  layer.left=x;
  layer.top=y;
  layer.visibility=visible;
  if (obj.constructor==Layer) 
  {
    layer.pageX=obj.pageX;
    layer.pageY=obj.pageY;
    layer.clip.width=obj.clip.width;
    layer.clip.height=obj.clip.height+titleHeight;
    obj.moveBelow(layer.document.layers.title);
    obj.top += titleHeight;
    delete layer.document.layers.main;
    layer.document.layers.main=obj;
  }
  var main=layer.document.layers.main;
  main.i2uipad=this;
  this.layer=layer;
  layer.i2uipad=this;
  this.main=main;

  this.contentHeight = 0;
  main.contentWidth  = i2uiPad.width - 2;
  main.vbarok = false;
  main.hbarok = false;

  var v=layer.document.layers.v;
  
  var vempty=v.document.layers.empty;
  vempty.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP);
  vempty.onmousedown=i2uiPad.v_rectdown;
  vempty.onmousemove=i2uiPad.v_rectmove;
  vempty.onmouseup=i2uiPad.v_rectup;
  vempty.onmouseout=i2uiPad.v_rectup;

  var up=v.document.layers.up;
  up.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP);
  up.document.images.up.layer=up;
  up.document.images.up.onmousedown=i2uiPad.v_updown;
  up.document.images.up.onmouseup=i2uiPad.v_upup;
  up.document.images.up.onmouseout=i2uiPad.v_upup;

  var down=v.document.layers.down;
  down.document.images.down.layer=down;
  down.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP);
  down.document.images.down.onmousedown=i2uiPad.v_downdown;
  down.document.images.down.onmouseup=i2uiPad.v_downup;
  down.document.images.down.onmouseout=i2uiPad.v_downup;

  var h=layer.document.layers.h;
  
  var hempty=h.document.layers.empty;
  hempty.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP);
  hempty.onmousedown=i2uiPad.h_rectdown;
  hempty.onmousemove=i2uiPad.h_rectmove;
  hempty.onmouseup=i2uiPad.h_rectup;

  var left=h.document.layers.left;
  left.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP);
  left.document.images.left.layer=left;
  left.document.images.left.onmousedown=i2uiPad.h_leftdown;
  left.document.images.left.onmouseup=i2uiPad.h_leftup;
  left.document.images.left.onmouseout=i2uiPad.h_leftup;

  var right=h.document.layers.right;
  right.document.images.right.layer=right;
  right.captureEvents(Event.MOUSEDOWN | Event.MOUSEUP);
  right.document.images.right.onmousedown=i2uiPad.h_rightdown;
  right.document.images.right.onmouseup=i2uiPad.h_rightup;
  right.document.images.right.onmouseout=i2uiPad.h_rightup;

  this.refresh();

  i2uiPad.count++;
  return this;
}

i2uiPad.prototype.vincr=5;
i2uiPad.prototype.hincr=5;

i2uiPad.prototype.scrollTo=function (x,y) {
  this.Hupdate(x/this.Hcoef);
  this.Vupdate(y/this.Vcoef);
}

i2uiPad.prototype.scrollBy = function (x,y) {
  this.Hupdate((this.main.clip.left+x)/this.Hcoef);
  this.Vupdate((this.main.clip.top+y)/this.Vcoef);
}

i2uiPad.prototype.tile=function () 
{
  var i = 0;
  var availableHeight = window.innerHeight - 6;
  //i2uitrace(0,"======================================");
  //i2uitrace(0,"avail pre="+availableHeight);

  // reduce availableHeight by height of each closed title and
  // compute neededHeight
  var neededHeight = 0;
  var openCount = 0;
  var scrollingCount = 0;

  for (i=0; i<i2uiPad.count; i++)
  {
    var main=i2uiPad.instances[i].layer.document.layers.main;
    var main2=i2uiPad.instances[i].layer;
    var title=i2uiPad.instances[i].layer.document.layers.title;

    availableHeight -= title.clip.height;

    if (main.visibility != 'hide')
    {
      // reduce availableHeight by height of first item
      if (i == 0)
      {
        if (main2.contentHeight > 0)
          availableHeight -= main2.contentHeight;
        else
          availableHeight -= main.clip.height;
      }   
      else
      {
        openCount++;
        
        if (main2.contentHeight > 0)
          neededHeight += main2.contentHeight;
        else
          neededHeight += main.clip.height;

        //LPM
        if (main.contentWidth > i2uiPad.width - 2)
          neededHeight += 16;
      }
    }
  }
  //i2uitrace(1,"avail post="+availableHeight+" needed="+neededHeight+" opencount="+openCount);

  // compute best average height
  if (openCount > 0)
  {
    var avgHeight = Math.floor(availableHeight / openCount);
    //i2uitrace(1,"resize needed for "+openCount+" items avgheight="+avgHeight);
    var maxavg = avgHeight;
    var used = 0;
    var spread;
    for (var j=0; j<5; j++)
    {
      //i2uitrace(1,"LOOP #"+j+" avgHeight="+avgHeight);
      spread = 0;
      scrollingCount = 0;
      used = 0;
      for (var i=1; i<i2uiPad.count; i++)
      {
        var main=i2uiPad.instances[i].layer.document.layers.main;
        if (main.visibility != 'hide')
        {
          var main2=i2uiPad.instances[i].layer;
          maxavg = Math.max(maxavg, main2.contentHeight);
          //i2uitrace(1,"LOOP #"+j+" pad #"+i+" contentheight="+main2.contentHeight);

          // if more content than can fit in average height
          if (main2.contentHeight > avgHeight)
            scrollingCount++;
          used += Math.min(avgHeight, main2.contentHeight);
        }
      }
      //i2uitrace(1,"LOOP #"+j+" spread="+spread+" scrollingCount="+scrollingCount+" used="+used);
      if (used <= availableHeight && scrollingCount == 0)
      {
        break;
      }
      else
      if (scrollingCount > 0)
      {
        var delta = used - availableHeight;
        spread = Math.floor(delta / scrollingCount);
        avgHeight -= spread;
        //i2uitrace(1,"LOOP #"+j+" scrolling new avg="+avgHeight+" [avail="+availableHeight+" used="+used+" delta="+delta+" spread="+spread+"]");
      }
      else
      if (openCount > 0)
      {
        var delta = used - availableHeight;
        spread = Math.floor(delta / openCount);
        avgHeight -= spread;
        //i2uitrace(1,"LOOP #"+j+" open new avg="+avgHeight+" [delta="+delta+" spread="+spread+"]");
      }
      avgHeight = Math.min(avgHeight, maxavg);
      //i2uitrace(1,"LOOP #"+j+" fixed avg="+avgHeight);
    }
    //i2uitrace(1,"final avg="+avgHeight);


    // resize any open items
    for (i=1; i<i2uiPad.count; i++)
    {
      var main=i2uiPad.instances[i].layer.document.layers.main;
      if (main.visibility != 'hide')
      {
        var main2=i2uiPad.instances[i].layer;
        
        //i2uitrace(1,"test if less than avg. clip="+main.clip.height+" avg="+avgHeight+" contentHeight="+main2.contentHeight+" vbar="+main.vbarok);

        if (main2.contentHeight <= avgHeight)
        {
          if ((main2.contentHeight > 0 && 
               main.clip.height != main2.contentHeight) ||
              main.clip.height != main.document.height)
          {
            //i2uitrace(1,"resize1 pad #"+i+" clip="+main.clip.height+" layer="+main2.clip.height+" content="+main2.contentHeight);
            main.clip.height = main2.contentHeight;
            
            if (main.vbarok)
              main.clip.height+= 16;
            main2.clip.height = main.clip.height + i2uiPadTitleHeight;

            i2uiPad.instances[i].intervalcount = 0;
            setTimeout("i2uiPad.instances["+i+"].refresh()", 250);
          }
          else
          {
            //i2uitrace(1,"resize2 pad #"+i+" clip="+main.clip.height+" layer="+main2.clip.height+" content="+main2.contentHeight);
            //i2uitrace(1,"assign #"+i+" clip="+main.clip.height+" layer="+main2.clip.height+" content="+main2.contentHeight);
            main.clip.height = main2.contentHeight;
            //i2uitrace(1,"assign #"+i+" clip="+main.clip.height+" layer="+main2.clip.height+" content="+main2.contentHeight);

            //i2uitrace(1,"assign #"+i+" to height of document ="+main.clip.height+" layer="+main2.clip.height);
            main2.clip.height = main.clip.height + i2uiPadTitleHeight;
          }
        }
        else
        {
          var newHeight = avgHeight - 16;
          
          //i2uitrace(1,"resize item #"+i+" to "+newHeight+" main.clip="+main.clip.height+" instance.clip="+i2uiPad.instances[i].layer.clip.height);
          
          if (main.clip.height != newHeight+16 || main.vbarok)
          {
            //i2uitrace(1,"resize3 pad #"+i+" clip="+main.clip.height+" layer="+main2.clip.height+" content="+main2.contentHeight);
            main.clip.height = newHeight;
            if (main.vbarok)
              main.clip.height+= 16;
            main2.clip.height = main.clip.height + i2uiPadTitleHeight;

            //i2uitrace(1,"refresh #"+i);
            i2uiPad.instances[i].intervalcount = 0;
            setTimeout("i2uiPad.instances["+i+"].refresh()", 250);
          }
          else
          {
            //i2uitrace(1,"resize4 pad #"+i+" clip="+main.clip.height+" layer="+main2.clip.height+" content="+main2.contentHeight);
            main.clip.height = newHeight + 16;

            main2.clip.height = main.clip.height + i2uiPadTitleHeight;
          }
        }
      }
    }
  }
  //alert('wait');
  var y = i2uiPad.instances[0].layer.top;
  var x = i2uiPad.instances[0].layer.left;

  // now position each pad
  for (i=0; i<i2uiPad.count; i++)
  {
    var main=i2uiPad.instances[i].layer.document.layers.main;

    //i2uitrace(0,"TILE "+i2uiPad.instances[i].layer.name+" aka "+main.name+" move to x="+x+" y="+y);
    i2uiPad.instances[i].layer.moveTo(x,y);
    
    // fix height of pad 0
    if (i == 0)
    {
      i2uiPad.instances[i].layer.clip.height = main.clip.height + i2uiPadTitleHeight;
    }

    if (main.visibility=='hide')
      y += i2uiPadTitleHeight;
    else
    {
      //i2uitrace(0,"   height="+i2uiPad.instances[i].layer.clip.height+" height="+main.clip.height+" contentHeight="+main2.contentHeight);
      y += i2uiPad.instances[i].layer.clip.height;

      // refresh here?
    }
  }
}

i2uiPad.prototype.resizeTo=function (x,y)
{ 
  this.layer.clip.height = y;
  this.layer.clip.width = x;
  var title=this.layer.document.layers.title;
  title.clip.width = x;
  var titletext=this.layer.document.layers.title.document.layers.text;
  titletext.clip.width = x-2;
  this.refresh();
  this.tile();
}

i2uiPad.prototype.resizeBy=function (x,y)
{
  this.layer.clip.height += y;
  this.layer.clip.width  += x;
  var title=this.layer.document.layers.title;
  title.clip.width  += x;
  var titletext=this.layer.document.layers.title.document.layers.text;
  titletext.clip.width  += x;
  
  this.intervalcount = 0;
  this.refresh();

  this.tile();
}

i2uiPad.prototype.moveTo=function (x,y)
{
  this.layer.moveTo(x,y);
  this.refresh();
}

i2uiPad.prototype.placeOnTop=function ()
{
  this.layer.zIndex=99;
  this.refresh();
}

i2uiPad.prototype.toggleContents=function ()
{
  var obj=this.layer.document.layers.main;
  var img=this.layer.document.layers.title.document.layers.text.document.images.toggler;

  if (obj.visibility=='hide')
  {
    obj.visibility='inherit';
    img.src=i2uiImageDirectory+"/container_collapse.gif";
    if (this.vbar != 'never')
      this.vbar = 'auto';
    if (this.hbar != 'never')
      this.hbar = 'auto';
  }
  else
  {
    obj.visibility='hide';
    img.src=i2uiImageDirectory+"/container_expand.gif";
    if (this.vbar != 'never')
      this.vbar = 'hide';
    if (this.hbar != 'never')
      this.hbar = 'hide';
  }
  this.tile();
  
  this.intervalcount = 0;
  setTimeout("i2uiPad.instances["+this.index+"].refresh()", 250);
}

i2uiPad.prototype.refresh=function ()
{
  var main=this.layer.document.layers.main;

  this.intervalcount++;

  main.clip.width=this.layer.clip.width;

  //i2uitrace(1,"***** id="+main.id+" main clip.width="+main.clip.width+" doc.width="+main.document.width+" contentWidth="+main.contentWidth);

  var vbarok=(this.vbar=='show')||
             (main.contentWidth>i2uiPad.width && this.layer.contentHeight-16>main.clip.height+2)||
             (main.contentWidth<i2uiPad.width && this.layer.contentHeight>main.clip.height+2);
  var hbarok=(this.hbar=='show')||
              (vbarok && main.contentWidth>main.clip.width+16)||
              (!vbarok && main.contentWidth>main.clip.width);

  //i2uitrace(1,"id="+main.id+" count="+this.intervalcount+" main h clip="+main.clip.height+" layer clip="+this.layer.clip.height+" contentHeight="+this.layer.contentHeight);
  //i2uitrace(1,"id="+main.id+" count="+this.intervalcount+" main w clip="+main.clip.width+" doc="+main.document.width+" layer clip="+this.layer.clip.width);
  //i2uitrace(1,"id="+main.id+" vbar="+vbarok+" hbar="+hbarok);

  if (this.vbar=='hide'||this.vbar=='never') vbarok=false;
  if (this.hbar=='hide'||this.hbar=='never') hbarok=false;

  main.vbarok = vbarok;
  main.hbarok = hbarok;

  //i2uitrace(1,"id="+main.id+" vbar="+vbarok+" hbar="+hbarok);

  var v=this.layer.document.layers.v;
  var h=this.layer.document.layers.h;
  var title=this.layer.document.layers.title;

  title.visibility='inherit';
  
  if (this.layer.contentHeight > 0 && !vbarok)
  {
    v.clip.height=this.layer.contentHeight-title.clip.height;
  }
  else
  {
    v.clip.height=this.layer.clip.height-title.clip.height;
  }
  h.clip.width=this.layer.clip.width;

  if (vbarok) 
  {
    main.clip.width-=16;
    h.clip.width=main.clip.width;
    v.visibility='inherit';
  } 
  else
  {
    v.visibility='hide';
  }

  if (hbarok) 
  {
    main.clip.height=this.layer.clip.height-16-title.clip.height;
    v.clip.height=main.clip.height;
    h.visibility='inherit';
  } 
  else
  {
    h.visibility='hide';
  }

  if (vbarok && hbarok) 
  {
    var corner=this.layer.document.layers.resizer;
    corner.left=main.clip.width;
    corner.top=main.clip.height+title.clip.height;
    corner.visibility='inherit';
  } 
  else
  {
    this.layer.document.layers.resizer.visibility='hide';
  }

  var empty=v.document.layers.empty;
  var up=v.document.layers.up;
  var down=v.document.layers.down;
  var rect=empty.document.layers.rect;
  
  v.left=this.layer.clip.width-v.clip.width;
  down.top=v.clip.height-down.clip.height;
  empty.top=up.clip.height;
  empty.clip.height=down.top-empty.top;

  if (this.layer.contentHeight > 0)
    this.Vcoef=this.layer.contentHeight/empty.clip.height;
  else
    this.Vcoef=main.document.height/empty.clip.height;

  if (this.Vcoef==0) 
    this.Vcoef=0.000001;
  this.Vupdate(main.clip.top/this.Vcoef);

  // time to compute the bar size..
  var bCompute = false;
  if (this.layer.contentHeight > 0)
  {
    if (this.layer.contentHeight<=main.clip.height) 
    {
      bCompute = true;
    }
  }
  else
  if (main.document.height<=main.clip.height) 
  {
    bCompute = true;
  }

  var barHeight = 0;
  if (bCompute)
  {
    barHeight=empty.clip.height;
    //i2uitrace(1,"$$$ 1 vert scroller height="+barHeight);
    up.document.images.up.src=i2uiImageDirectory+i2uiPad.noUpIcon;
    down.document.images.down.src=i2uiImageDirectory+i2uiPad.noDownIcon;
    v.noscroll=true;
  }
  else 
  {
    //i2uitrace(1,"$$$ 2 vert scroller main.clip="+main.clip.height+" empty.clip="+empty.clip.height+" content="+this.layer.contentHeight);
    if (this.layer.contentHeight > 0)
      barHeight=main.clip.height*empty.clip.height/this.layer.contentHeight;
    else
      barHeight=main.clip.height*empty.clip.height/main.document.height;
    
    //i2uitrace(1,"$$$ 2 vert scroller height="+Math.min(0,barHeight));
    if (up.document.images.up.src.indexOf(i2uiImageDirectory+i2uiPad.noUpIcon)!=-1)
      up.document.images.up.src=i2uiImageDirectory+i2uiPad.upUpIcon;
    if (down.document.images.down.src.indexOf(i2uiImageDirectory+i2uiPad.noDownIcon)!=-1)
      down.document.images.down.src=i2uiImageDirectory+i2uiPad.upDownIcon;
    v.noscroll=false;
  }
  if (barHeight > 0)
  {
    rect.clip.height=barHeight;
    rect.document.layers[0].top=0;
    rect.document.layers[1].top=2;
    rect.document.layers[1].clip.height=barHeight-4;
    rect.document.layers[2].top=barHeight-2;
  }

  var empty=h.document.layers.empty;
  var left=h.document.layers.left;
  var right=h.document.layers.right;
  var rect=empty.document.layers.rect;
  h.top=this.layer.clip.height-h.clip.height;
  right.left=h.clip.width-left.clip.width;
  empty.left=left.clip.width;
  empty.clip.width=right.left-empty.left;
  this.Hcoef=main.contentWidth/empty.clip.width;
  if (this.Hcoef==0) 
    this.Hcoef=0.000001;
  this.Hupdate(main.clip.left/this.Hcoef);

  // time to compute the bar size..
  if (main.contentWidth<=main.clip.width) 
  {
    var barWidth=empty.clip.width;
    //i2uitrace(1,"$$$ horz 1 scroller height="+barHeight);
    left.document.images.left.src=i2uiImageDirectory+i2uiPad.noLeftIcon;
    right.document.images.right.src=i2uiImageDirectory+i2uiPad.noRightIcon;
    h.noscroll=true;
  }
  else 
  {
    var barWidth=main.clip.width*empty.clip.width/main.contentWidth;
    //i2uitrace(1,"$$$ horz 2 scroller height="+barHeight);
    if (left.document.images.left.src==i2uiImageDirectory+i2uiPad.noLeftIcon)
      left.document.images.left.src=i2uiImageDirectory+i2uiPad.upLeftIcon;
    if (right.document.images.right.src==i2uiImageDirectory+i2uiPad.noRightIcon)
      right.document.images.right.src=i2uiImageDirectory+i2uiPad.upRightIcon;
    h.noscroll=false;
  }
  rect.clip.width=barWidth;
  rect.document.layers[0].left=0;
  rect.document.layers[1].left=2;
  rect.document.layers[1].clip.width=barWidth-4;
  rect.document.layers[2].left=barWidth-2;

  if (this.intervalcount <= 2)
    setTimeout("i2uiPad.instances["+this.index+"].refresh()", 250);
}

i2uiPad.v_rectdown= function (e)
{
  var empty=this;
  empty.captureEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  var rect=empty.document.layers.rect;
  if (e.target==empty.document)
  {
    empty.parentLayer.parentLayer.i2uipad.Vupdate(e.layerY);
  }
  rect.curY=e.pageY-rect.top;
  return false;
}

i2uiPad.v_rectup= function () 
{
   this.releaseEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
   return true;
}

i2uiPad.v_rectmove= function(e) 
{
  var empty=this;
  var rect=empty.document.layers.rect;
  var newpos=0;
  if (e.target==empty.document) 
  {
    newpos=e.layerY;
  } 
  else
  {
    newpos=e.pageY-rect.curY;
  }

  empty.parentLayer.parentLayer.i2uipad.Vupdate(newpos);
  return false;
}

i2uiPad.prototype.Vupdate=function (y)
{
  var empty=this.layer.document.layers.v.document.layers.empty
  var rect=empty.document.layers.rect;
  if (y < 0) 
    y = 0;
  if (y > empty.clip.height-rect.clip.height)
    y = empty.clip.height - rect.clip.height;
  var offset=Math.max(0,Math.floor(y*this.Vcoef));
  var sh=this.main.clip.height;
  if (offset < 2000)
    this.main.clip.top=offset;
  offset -= i2uiPadTitleHeight;

  if (offset < 2000)
    this.main.top=-offset;
  this.main.clip.height=sh;
  this.layer.document.layers.v.document.layers.empty.document.layers.rect.top=y;
  rect.top=y;
  this.pageYOffset=offset;
}

i2uiPad.v_updown=function ()
{
  if (this.layer.parentLayer.noscroll)
    return false;
  var that=this.layer;
  var rect=that.parentLayer.document.layers.empty.document.layers.rect;
  this.src=i2uiImageDirectory+i2uiPad.downUpIcon;
  clearTimeout(i2uiPad.timer1);
  clearInterval(i2uiPad.timer2);
  i2uiPad.upinterval(rect);
  i2uiPad.timer1=setTimeout(i2uiPad.uptimeout,i2uiPad.delay1,rect);
  that.captureEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}

i2uiPad.uptimeout= function (rect) 
{
  i2uiPad.timer2=setInterval(i2uiPad.upinterval,i2uiPad.delay2,rect);
}

i2uiPad.upinterval = function (rect)
{
  var thePad=rect.parentLayer.parentLayer.parentLayer.i2uipad;
  thePad.Vupdate(rect.top - thePad.vincr);
}

i2uiPad.v_upup= function () 
{
  if (this.layer.parentLayer.noscroll)
    return false;
  this.src=i2uiImageDirectory+i2uiPad.upUpIcon;
  clearTimeout(i2uiPad.timer1);
  clearInterval(i2uiPad.timer2);
  this.layer.releaseEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}

i2uiPad.v_downdown=function ()
{
  if (this.layer.parentLayer.noscroll)
    return false;
  var that=this.layer;
  var rect=that.parentLayer.document.layers.empty.document.layers.rect;
  this.src=i2uiImageDirectory+i2uiPad.downDownIcon;
  clearTimeout(i2uiPad.timer1);
  clearInterval(i2uiPad.timer2);
  i2uiPad.downinterval(rect);
  i2uiPad.timer1=setTimeout(i2uiPad.downtimeout,i2uiPad.delay1,rect);
  that.captureEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}

i2uiPad.downtimeout= function (rect) 
{
  i2uiPad.timer2=setInterval(i2uiPad.downinterval,i2uiPad.delay2,rect);
}

i2uiPad.downinterval= function (rect) 
{
  var thePad=rect.parentLayer.parentLayer.parentLayer.i2uipad;
  thePad.Vupdate(rect.top + thePad.vincr);
}

i2uiPad.v_downup= function () 
{
  if (this.layer.parentLayer.noscroll)
    return false;
  this.src=i2uiImageDirectory+i2uiPad.upDownIcon;
  clearTimeout(i2uiPad.timer1)
  clearTimeout(i2uiPad.timer2)
  this.layer.releaseEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}


i2uiPad.h_rectdown= function (e) 
{
  var empty=this;
  empty.captureEvents(Event.MOUSEMOVE);
  var rect=empty.document.layers.rect;
  if (e.target==empty.document) 
  {
    empty.parentLayer.parentLayer.i2uipad.Hupdate(e.layerX);
  }
  rect.curX=e.pageX-rect.left;
  return true;
}

i2uiPad.h_rectup= function () 
{
   this.releaseEvents(Event.MOUSEMOVE);
   return true;
}

i2uiPad.h_rectmove= function(e) 
{
  var empty=this;
  var rect=empty.document.layers.rect;
  var newpos=0;
  if (e.target==empty.document) 
  {
    newpos=e.layerX;
  }
  else
  {
    newpos=e.pageX-rect.curX;
  }

  empty.parentLayer.parentLayer.i2uipad.Hupdate(newpos);
  return false;
}

i2uiPad.prototype.Hupdate=function (x)
{
  var empty=this.layer.document.layers.h.document.layers.empty
  var rect=empty.document.layers.rect;
  if (x < 0) 
    x=0;
  if (x > empty.clip.width - rect.clip.width) 
    x=empty.clip.width-rect.clip.width;
  var offset=Math.round(x*this.Hcoef);
  var sh=this.main.clip.width;
  this.main.clip.left=offset;
  this.main.left=-offset;
  this.main.clip.width=sh;
  rect.left=x;
  this.pageXOffset=offset;
}

i2uiPad.h_leftdown=function ()
{
  if (this.layer.parentLayer.noscroll)
    return false;
  var that=this.layer;
  var rect=that.parentLayer.document.layers.empty.document.layers.rect;
  this.src=i2uiImageDirectory+i2uiPad.downLeftIcon;
  clearTimeout(i2uiPad.timer1);
  clearInterval(i2uiPad.timer2);
  i2uiPad.leftinterval(rect);
  i2uiPad.timer1=setTimeout(i2uiPad.lefttimeout,i2uiPad.delay1,rect);
  that.captureEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}

i2uiPad.lefttimeout= function (rect)
{
  i2uiPad.timer2=setInterval(i2uiPad.leftinterval,i2uiPad.delay2,rect);
}

i2uiPad.leftinterval = function (rect)
{
  var thePad=rect.parentLayer.parentLayer.parentLayer.i2uipad;
  thePad.Hupdate(rect.left - thePad.hincr)
}

i2uiPad.h_leftup= function () 
{
  if (this.layer.parentLayer.noscroll)
    return false;
  this.src=i2uiImageDirectory+i2uiPad.upLeftIcon;
  clearTimeout(i2uiPad.timer1);
  clearInterval(i2uiPad.timer2);
  this.layer.releaseEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}

i2uiPad.h_rightdown=function () 
{
  if (this.layer.parentLayer.noscroll)
    return false;
  var that=this.layer;
  var rect=that.parentLayer.document.layers.empty.document.layers.rect;
  this.src=i2uiImageDirectory+i2uiPad.downRightIcon;
  clearTimeout(i2uiPad.timer1);
  clearInterval(i2uiPad.timer2);
  i2uiPad.rightinterval(rect);
  i2uiPad.timer1=setTimeout(i2uiPad.righttimeout,i2uiPad.delay1,rect);
  that.captureEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}

i2uiPad.righttimeout= function (rect) 
{
  i2uiPad.timer2=setInterval(i2uiPad.rightinterval,i2uiPad.delay2,rect);
}

i2uiPad.rightinterval= function (rect) 
{
  var thePad=rect.parentLayer.parentLayer.parentLayer.i2uipad;
  thePad.Hupdate(rect.left + thePad.hincr);
}

i2uiPad.h_rightup= function () 
{
  if (this.layer.parentLayer.noscroll)
    return false;
  this.src=i2uiImageDirectory+i2uiPad.upRightIcon;
  clearTimeout(i2uiPad.timer1)
  clearTimeout(i2uiPad.timer2)
  this.layer.releaseEvents(Event.MOUSEMOVE | Event.MOUSEOUT);
  return false;
}





/*
  Manage the state of the pad's scrollers

  padname - name of pad

  return - none

  Compatibility:  IE5, NS6
*/

function i2uiManagePadScroller(padname)
{
  if (document.layers)
    return;

  //i2uitrace(1,"i2uiManagePadScroller entry pad="+padname);

  var scroller_obj = document.getElementById(padname+"_scroller");
  i2uiComputeScrollHeight(padname+"_scroller",true);
  i2uiComputeScrollWidth(padname+"_scroller",true);
  //i2uitrace(1,"i2uiManagePadScroller pad "+padname+" scroll="+scroller_obj.scrollHeight+" offset="+scroller_obj.offsetHeight);
  
  if (scroller_obj != null)
  {
    if (scroller_obj.scrollHeight <= scroller_obj.offsetHeight &&
        scroller_obj.scrollWidth  <= scroller_obj.offsetWidth    )
    {
      scroller_obj.style.overflow="hidden";
    }
    else
    {
      scroller_obj.style.overflow="auto";   
    }
  }
  //i2uitrace(1,"i2uiManagePadScroller exit");
}

/*
  Tile all pads.

  return - none

  Compatibility:  IE5, NS6, NS4
*/
function i2uiTilePads()
{
  //i2uitrace(1,"clear");
  //i2uitrace(1,"i2uitilepads entry. #pads="+i2uiPad.count+" ============================");
  if (document.layers)
  {
    if (i2uiPad.count > 0)
    {
      // tile each pad's items
      for (var i=1; i<i2uiPad.count-1; i++)
      {
        var layer = i2uiLocateLayer(document, i2uiPad.instances[i].main.id);
        var contentWidth = Math.max(i2uiPad.width-2, i2uiComputePadWidth(layer, 0));
        
        if (contentWidth > i2uiPad.width - 2)
          contentWidth += 16;

        //i2uitrace(1,"$$$$$$$$ #"+i+" pre layer.contentWidth="+layer.contentWidth);
        layer.contentWidth = Math.max(158, contentWidth);
        //i2uitrace(1,"$$$$$$$$ #"+i+" post layer.contentWidth="+layer.contentWidth);

        i2uiTilePadItems(i2uiPad.instances[i].main.id, 0, contentWidth);
      }
      // tile last pad with flag to indicate pads should be tiled
      var idx = i2uiPad.count - 1;

      //i2uiTilePadItems(i2uiPad.instances[idx].main.id, 1);
      // fixes problem where browser state still changing
      setTimeout("i2uiTilePadItems(i2uiPad.instances["+idx+"].main.id, 1)",150);
    }
  }
  else
  {
    var summedApplContentHeight = 0;
    var openCount = 0;
    var scrollingCount = 0;
    var obj;
    var scroller_obj;
    var content_obj;
    var avgHeight;
    var navareaTop = i2uiComputeTop('i2uinavarea');
    // for Netscape 6 we need window height.  body height is only content height
    if (navigator.appName == "Netscape")
      var navareaHeight = window.innerHeight - navareaTop - 3;
    else
      var navareaHeight = document.body.clientHeight - navareaTop - 3;
    var pad0height = 0;

    //i2uitrace(1,"clientHeight="+document.body.clientHeight);
    //i2uitrace(1,"offsetHeight="+document.body.offsetHeight);
    //i2uitrace(1,"offsetHeight="+window.innerHeight);

    //i2uitrace(1,"navarea top="+navareaTop+" height="+navareaHeight+" bodyheight="+document.body.offsetHeight+" or "+document.body.clientHeight+" or "+document.body.scrollHeight);
    
    // handle special case of single pad.  needed for doclib pad type
    if (i2uiPad.count == 1)
    {
      if (navareaTop > 10)
      {
        navareaHeight -= 32;
        //i2uitrace(1,"fixed navareaHeight="+navareaHeight);
      }

      content_obj = document.getElementById(i2uiPad.instances[0]);
      if (content_obj != null)
      {
        i2uiComputeScrollHeight(i2uiPad.instances[0],true);

        scroller_obj = document.getElementById(i2uiPad.instances[0]+"_scroller");
        i2uiComputeScrollHeight(i2uiPad.instances[0]+"_scroller",true);
        i2uiComputeScrollWidth(i2uiPad.instances[0]+"_scroller",true);
        if (content_obj.offsetHeight >= navareaHeight)
          scroller_obj.style.height = navareaHeight;
        else
          scroller_obj.style.height = null;
      }
      i2uiManagePadScroller(i2uiPad.instances[0]);
      return;
    }

    // handle height of first pad
    content_obj = document.getElementById(i2uiPad.instances[0]);
    if (content_obj != null)
    {
      i2uiComputeScrollHeight(i2uiPad.instances[0],true);

      pad0height = content_obj.offsetHeight;
      //i2uitrace(1,"pad #0 content height="+content_obj.offsetHeight+" width="+content_obj.offsetWidth);
      // if first pad is too wide, then adjust height to accomodate for scroller height
      if (content_obj.offsetWidth > i2uiPad.width)
      {
        i2uiManagePadScroller(i2uiPad.instances[0]);
        scroller_obj = document.getElementById(i2uiPad.instances[0]+"_scroller");
        if (scroller_obj != null)
        {
          i2uiComputeScrollHeight(i2uiPad.instances[0]+"_scroller",true);

          //i2uitrace(1,"pad #0 contentHeight="+content_obj.offsetHeight+" contentHeight2="+content_obj.scrollHeight+" scrollerHeight="+scroller_obj.offsetHeight);
          if (content_obj.offsetHeight == scroller_obj.offsetHeight &&
              content_obj.offsetHeight > 0)
          {
            var delta = 16; //LPM should compute
            if (navigator.appName == "Netscape")
              delta += 2;
            //i2uitrace(1,"increase pad #0 height by "+delta);
            // sorry bug in Netscape 6.0,6.01,6.1 causes container to continally grow in size
            i2uiComputeScrollHeight(i2uiPad.instances[0],true);
            scroller_obj.style.height = content_obj.scrollHeight + delta;
          }
          pad0height = scroller_obj.offsetHeight;
        }
      }
      //i2uitrace(1,"pad #0 height="+pad0height);
    }

    // handle non-framed case where navarea sits within shell directly
    if (navareaTop > 10)
    {
      navareaHeight -= 16;
      //i2uitrace(1,"fixed navareaHeight="+navareaHeight);
    }

    //i2uitrace(1,"---- tile "+i2uiPad.count+" pads");
    // compute how much space pads and content are taking
    for (var i=1; i<i2uiPad.count; i++)
    {
      content_obj = document.getElementById(i2uiPad.instances[i]);
      if (content_obj != null)
      {
        i2uiComputeScrollHeight(i2uiPad.instances[i],true);

        scroller_obj = document.getElementById(i2uiPad.instances[i]+"_scroller");
        i2uiComputeScrollHeight(i2uiPad.instances[i]+"_scroller",true);
        i2uiComputeScrollWidth(i2uiPad.instances[i]+"_scroller",true);

        //i2uitrace(1,"$$$$$ pad #"+i+" content.offset="+content_obj.offsetHeight+" .scroller="+content_obj.scrollHeight+"  scroller.offset="+scroller_obj.offsetHeight+" .scroller="+scroller_obj.scrollHeight);
        // if Netscape and offset > scroller, then reset offset
        if (navigator.appName == "Netscape" && 
            scroller_obj.offsetHeight > scroller_obj.scrollHeight)
        {
          scroller_obj.style.height = scroller_obj.scrollHeight;
          //i2uitrace(1,"fixed  pad #"+i+" content.offset="+content_obj.offsetHeight+" .scroller="+content_obj.scrollHeight+"  scroller.offset="+scroller_obj.offsetHeight+" .scroller="+scroller_obj.scrollHeight);
        }

        if (scroller_obj != null && 
            scroller_obj.offsetHeight != content_obj.offsetHeight)
        {
          scrollingCount++;
          //i2uitrace(1,"pad #"+i+" is scrolling");
        }
        //i2uitrace(1,"pad #"+i+" content height="+content_obj.offsetHeight+" scroller height="+scroller_obj.offsetHeight+" width="+content_obj.offsetWidth);
        summedApplContentHeight += content_obj.offsetHeight;

        // if wide text
        if (scroller_obj != null && 
            content_obj.offsetHeight > 0 &&
            scroller_obj.offsetWidth != scroller_obj.scrollWidth &&
            scroller_obj.scrollWidth > i2uiPad.width)
        {
          var delta = 16;  //LPM should be computed
          if (navigator.appName == "Netscape")
            delta += 2;

          //i2uitrace(1,"  increase summedApplContentHeight by "+delta+" scrollerHeight="+scroller_obj.offsetHeight);
          summedApplContentHeight += delta;
          if (content_obj.offsetHeight == scroller_obj.offsetHeight)
          {
            scroller_obj.style.height = content_obj.scrollHeight + delta;
            //i2uitrace(1,"redo pad #"+i+" content height="+content_obj.offsetHeight+" scroller height="+scroller_obj.offsetHeight+" width="+content_obj.offsetWidth);
          }
        }

        if (content_obj.offsetHeight > 0)
          openCount++;
      }
    }

    // will all fit without resizing?
    var titleHeight = document.getElementById("PAD_"+i2uiPad.instances[0]).offsetHeight - pad0height;
    var summedTitleHeight = i2uiPad.count * titleHeight;
    var availContentHeight = navareaHeight - pad0height - summedTitleHeight;
    //i2uitrace(1,"avail("+availContentHeight+")=navarea("+navareaHeight+") - pad0("+pad0height+") - titles("+summedTitleHeight+")");
    //i2uitrace(1,"avail content height="+availContentHeight+" needed="+summedApplContentHeight);

    if (availContentHeight > summedApplContentHeight)
    {
      //i2uitrace(1,"everything fits. #scrollers="+scrollingCount);

      for (var i=1; i<i2uiPad.count; i++)
      {
        content_obj = document.getElementById(i2uiPad.instances[i]);
        scroller_obj = document.getElementById(i2uiPad.instances[i]+"_scroller");
        if (content_obj != null && scroller_obj != null)
        {
          i2uiComputeScrollHeight(i2uiPad.instances[i],true);
          i2uiComputeScrollWidth(i2uiPad.instances[i],true);
          i2uiComputeScrollHeight(i2uiPad.instances[i]+"_scroller",true);
          i2uiComputeScrollWidth(i2uiPad.instances[i]+"_scroller",true);

          //i2uitrace(1,"     pad #"+i+" width="+scroller_obj.offsetWidth+" width2="+scroller_obj.scrollWidth);

          // if scrolling
          if (scroller_obj.offsetHeight < content_obj.offsetHeight)
          {
            scroller_obj.style.height = content_obj.scrollHeight;
          }
          else
          if (scroller_obj.offsetWidth == scroller_obj.scrollWidth)
          {
            scroller_obj.style.height = null;
          }
          else
          {
            scroller_obj.style.height = scroller_obj.offsetHeight;
          }
          i2uiManagePadScroller(i2uiPad.instances[i]);
        }
      }

      // if no scrolling pads, then we are done
      if (scrollingCount == 0)
        return;
    }
    
    avgHeight = Math.floor(availContentHeight / openCount);
    var maxavg = avgHeight;
    var used = 0;
    var spread;
    for (var j=0; j<5; j++)
    {
      //i2uitrace(1,"LOOP #"+j+" avgHeight="+avgHeight);
      spread = 0;
      scrollingCount = 0;
      used = 0;
      for (var i=1; i<i2uiPad.count; i++)
      {
        scroller_obj = document.getElementById(i2uiPad.instances[i]+"_scroller");
        content_obj = document.getElementById(i2uiPad.instances[i]);

        i2uiComputeScrollHeight(i2uiPad.instances[i],true);
        i2uiComputeScrollHeight(i2uiPad.instances[i]+"_scroller",true);

        maxavg = Math.max(maxavg, content_obj.offsetHeight);
        //i2uitrace(1,"LOOP #"+j+" pad #"+i+" contentheight="+content_obj.offsetHeight+" scrollheight="+scroller_obj.offsetHeight);

        // if more content than can fit in average height
        if (content_obj.offsetHeight > avgHeight)
          scrollingCount++;
        used += Math.min(avgHeight, content_obj.offsetHeight);
      }
      //i2uitrace(1,"LOOP #"+j+" spread="+spread+" scrollingCount="+scrollingCount+" used="+used);
      if (used <= availContentHeight && scrollingCount == 0)
      {
        break;
      }
      else
      if (scrollingCount > 0)
      {
        var delta = used - availContentHeight;
        spread = Math.floor(delta / scrollingCount);
        avgHeight -= spread;
        //i2uitrace(1,"LOOP #"+j+" scrolling new avg="+avgHeight+" [avail="+availContentHeight+" used="+used+" delta="+delta+" spread="+spread+"]");
      }
      else
      if (openCount > 0)
      {
        var delta = used - availContentHeight;
        spread = Math.floor(delta / openCount);
        avgHeight -= spread;
        //i2uitrace(1,"LOOP #"+j+" open new avg="+avgHeight+" [delta="+delta+" spread="+spread+"]");
      }
      avgHeight = Math.min(avgHeight, maxavg);
      //i2uitrace(1,"LOOP #"+j+" fixed avg="+avgHeight);
    }
    //i2uitrace(1,"final avgHeight="+avgHeight);

    for (var i=1; i<i2uiPad.count; i++)
    {
      scroller_obj = document.getElementById(i2uiPad.instances[i]+"_scroller");
      if (scroller_obj != null)
      {
        content_obj = document.getElementById(i2uiPad.instances[i]);
        //i2uitrace(1,"RESIZE pad #"+i+" height="+content_obj.offsetHeight+" scroller height="+scroller_obj.offsetHeight+" content height="+content_obj.offsetHeight+" newheight="+avgHeight);

        // if more content than can fit in average height
        if (content_obj.offsetHeight >= avgHeight && avgHeight > 0)
        {
          //i2uitrace(1,"RESIZE pad #"+i+" more content than allowed.  height="+content_obj.offsetHeight+" scroller height="+scroller_obj.offsetHeight+" content height="+content_obj.offsetHeight+" newheight="+avgHeight);
          scroller_obj.style.height = avgHeight;
        }
        else
        {
          //i2uitrace(1,"RESIZE pad #"+i+" content fits.  height="+content_obj.offsetHeight+" scroller height="+scroller_obj.offsetHeight+" content height="+content_obj.offsetHeight+" newheight="+avgHeight);
        
          // if wide text
          if (content_obj.offsetHeight > 0 &&
              scroller_obj.offsetWidth != scroller_obj.scrollWidth &&
              scroller_obj.scrollWidth > i2uiPad.width)
          {
            scroller_obj.style.height = content_obj.scrollHeight + 16;
          }
          else
          {
            // turn scroller height off
            scroller_obj.style.height = null;
          }
        }
      }

      // make scrollers appear only if necessary
      i2uiManagePadScroller(i2uiPad.instances[i]);
    }
  }
  //i2uitrace(1,"i2uitilepads exit");
}

/*
  Tile all items of a pad

  padname - name of pad
  tile    - indicator whether to tile pads after processing items

  return - none

  Compatibility:  NS4
*/
function i2uiTilePadItems(padname, tile, contentWidth)
{
  var layer = i2uiLocateLayer(document, padname);
  if (layer != null)
  {
    if (contentWidth == null)
      contentWidth = layer.contentWidth;

    // compute overall height of pad
    var height = i2uiPositionPadItem(layer, 0, 0, contentWidth);

    // add height of scroller if needed
    var vbarok=(contentWidth>i2uiPad.width && height-16>layer.clip.height+2)||
               (contentWidth<i2uiPad.width && height>layer.clip.height+2);
    //i2uitrace(1,"************ pad="+padname+" vbar="+vbarok+" content="+layer.parentLayer.contentHeight+" clip="+layer.clip.height+" new="+height);

    layer.vbarok = vbarok;

    if (contentWidth > i2uiPad.width && !vbarok)
      height += 16;

    // resize pad to new height
    layer.resizeTo(layer.document.width, height);
    layer.parentLayer.contentHeight = height + 1;
    
    // optionally tile all pads
    if (tile == null || tile == 1)
      i2uiPad.instances[0].tile();
  }
}

/*
  Position a pad item in tiled fashion.  Uses recursion to position 
  visible child items

  layer - owning layer
  top   - top of pad 
  depth - indication of depth within hierarchy of pad items

  return - bottom of pad

  Compatibility:  NS4
*/
function i2uiPositionPadItem(layer, top, depth, contentWidth)
{
  var len = layer.document.layers.length;

  // fix leaf node representation to be bullet image
  if (len == 0)
  {
    var img=layer.document.images.toggler;
    if (img != null)
    {
      if (img.src.indexOf("_bullet") == -1)
      {
        img.src=i2uiImageDirectory+"/tree_bullet.gif";
      }
    }
  }

  for (var i=0; i<len; i++)
  {
    if (layer.document.layers[i].visibility == 'inherit')
    {
      //i2uitrace(1,"  i="+i+" item width="+layer.document.layers[i].clip.width+" or="+layer.document.layers[i].document.width);
      layer.document.layers[i].clip.width = contentWidth;
      if (contentWidth > i2uiPad.width)
        layer.document.layers[i].clip.width -= 2;

      // position layer
      //i2uitrace(0,"position "+layer.layers[i].name+" at "+top+" currently at "+layer.document.layers[i].top+" left="+layer.document.layers[i].left+" height="+layer.document.layers[i].clip.height+" or "+layer.document.layers[i].document.height);
      layer.document.layers[i].moveTo(1,top);
      top += i2uiPadItemHeight;
      var childheight = i2uiPositionPadItem(layer.document.layers[i], i2uiPadItemHeight, depth+1, contentWidth) - i2uiPadItemHeight;
      //i2uitrace(0,"children height="+childheight);
      layer.document.layers[i].clip.height = childheight + i2uiPadItemHeight;
      top += childheight;
    }
  }
  return top;
}

function i2uiComputePadWidth(layer, width)
{
  var len = layer.document.layers.length;
  var localwidth = width;

  //i2uitrace(1,"computepadwidth for "+len+" items");
  for (var i=0; i<len; i++)
  {
    if (layer.document.layers[i].visibility == 'inherit')
    {
      localwidth = Math.max(localwidth, layer.document.layers[i].document.width);
      localwidth = Math.max(localwidth, i2uiComputePadWidth(layer.document.layers[i], localwidth));
    }
  }
  return localwidth;
}

/*
  Expands or collapses a pad item and then redraws the containing pad

  obj - value is this
  padname - name of owning pad
  itemname - name of item

  return - none

  Compatibility:  NS4
*/
function i2uiTogglePadItem(obj, padname, itemname)
{
  var layer = i2uiLocateLayer(document, itemname);
  if (layer != null)
  {
    var img=layer.document.images.toggler;
    if (img != null)
    {

      var padlayer = i2uiLocateLayer(document, padname);

      var state;
      if (img.src.indexOf("_collapse") != -1)
      {
        img.src=i2uiImageDirectory+"/container_expand.gif";
        state = 'hide';

        // this solves potential dead space below last item in pad
        if (padlayer != null && padlayer.i2uipad != null)
          padlayer.i2uipad.scrollTo(0,0);
      }
      else
      if (img.src.indexOf("_expand") != -1)
      {
        img.src=i2uiImageDirectory+"/container_collapse.gif";
        state = 'inherit';
      }

      // now process all children of this layer
      var len = layer.document.layers.length;
      if (len == 0 && img.src.indexOf("bullet") != -1)
      {
        img.src=i2uiImageDirectory+"/tree_bullet.gif";
        state = 'inherit';
      }

      for (var i=0; i<len; i++)
      {
        layer.document.layers[i].visibility = state;
      }

      // compute new clip width
      var contentWidth = Math.max(i2uiPad.width-2, i2uiComputePadWidth(padlayer, 0));
      //i2uitrace(1,"old width="+padlayer.contentWidth+" new width="+contentWidth);
      if (contentWidth > i2uiPad.width)
        contentWidth += 16;
      padlayer.contentWidth = Math.max(158, contentWidth);

      // now tile and redraw items within this pad
      i2uiTilePadItems(padname,1);
    }
  }
}

/*
  Debug routine that walks heirarchy of Netscape layers via recursion

  obj - owner object of layers.  initial call should use a value of document
  depth - initial call should use a value of 0

  return - none

  Compatibility:  NS4
*/
function i2uiLayerWalk(obj, depth)
{
  if (obj.layers)
  {
    var len = obj.layers.length;
    i2uitrace(0,"depth="+depth+" has "+len+" children");
    var num;
    for (var i=0; i<len; i++)
    {
      num = obj.layers[i].document.layers.length;
      i2uitrace(0,"depth "+depth+" layer "+obj.layers[i].name+" has "+num+" children. parent="+obj.layers[i].parentLayer.name);
      i2uiLayerWalk(obj.layers[i].document, depth+1);
    }
  }
}

/*
  Locates a Netscape layer by its name.  Uses recursion to walk layer
  hierarchy

  obj - owner object of layers.  initial call should use a value of document
  name - the layer's name

  return - reference to layer object

  Compatibility:  NS4
*/
function i2uiLocateLayer(obj, name)
{
  if (obj.layers)
  {
    var len = obj.layers.length;
    var num;
    for (var i=0; i<len; i++)
    {
      if (obj.layers[i].name == name)
        return obj.layers[i];
      var found = i2uiLocateLayer(obj.layers[i].document, name);
      if (found != null)
        return found;
    }
  }
  return null;
}

/*
  Toggles the visibility of the entire nav area

  return - none

  Compatibility:  IE, NS4, NS6
*/
function i2uiToggleNavArea()
{
  if (document.layers)
  {
    for (var i=0; i<i2uiPad.count; i++)
    {
      i2uiToggleItemVisibility(i2uiPad.instances[i].layer.id);
    }
  }
  else
    i2uiToggleItemVisibility('i2uinavarea');
}

/*
  Displays the identified pad item in a highlight color as well
  as de-highlighting any previous item in any other applicaiton pad

  id - the id of the pad item to hightlight

  return - none

  Compatibility:  IE, NS4, NS6
*/
function i2uiHighlightPadItem(id, highlightGroup)
{
  if (document.layers)
  {
    var layer = i2uiLocateLayer(document, id);
    if (layer != null)
    {
      if (i2uiPad.lastHighlightedPadItem[highlightGroup] != null)
      {
        var depth = i2uiPad.lastHighlightedPadItemName[highlightGroup].split("_").length;
        if (depth > 2)
          i2uiPad.lastHighlightedPadItem[highlightGroup].bgColor = i2uiApplicationPadContent1Bgcolor;
        else
          i2uiPad.lastHighlightedPadItem[highlightGroup].bgColor = i2uiApplicationPadContent0Bgcolor;
      }
      layer.bgColor = i2uiApplicationPadHighlightedBgcolor;
      i2uiPad.lastHighlightedPadItem[highlightGroup] = layer;
      i2uiPad.lastHighlightedPadItemName[highlightGroup] = id;
    }
  }
  else
  {
    var obj = document.getElementById(id);
    if (obj != null)
    {
      obj = obj.parentNode;
      if (obj != null)
      {
        if (obj.tagName == 'TR')
        {
          // turn off previous
          if (i2uiPad.lastHighlightedPadItem[highlightGroup] != null)
            i2uiPad.lastHighlightedPadItem[highlightGroup].id = null;

          // highlight myself
          obj.id = 'applicationHighlightedPadContent';

          i2uiPad.lastHighlightedPadItem[highlightGroup] = obj;
          i2uiPad.lastHighlightedPadItemName[highlightGroup] = id;
        }
      }
    }
  }
}

/*
  Collapses an expanded pad

  name - the name of the container to collaspe

  return - none

  Compatibility:  IE, NS6, NS4
*/
function i2uiCollapsePad(name)
{
  if (document.layers)
  {
    var layer = i2uiLocateLayer(document, name);
    if (layer != null)
    {
      layer.i2uipad.toggleContents();
    }
  }
  else
  {
    var obj = document.getElementById(name);
    if (obj != null)
    {
      var parentobj = obj.parentElement;
      while (parentobj != null && parentobj.tagName != 'TABLE')
      {
        parentobj = parentobj.parentElement;
      }
      if (parentobj != null)
      {
        var imagelist = parentobj.getElementsByTagName("IMG");
        if (imagelist.length > 0)
        {
          imagelist[0].src = i2uiImageDirectory+"/container_expand.gif";
          i2uiToggleContent(obj,1,'i2uiTilePads()');
        }
      }
    }
  }
  return;
}

