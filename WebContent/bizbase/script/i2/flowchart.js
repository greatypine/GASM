var currentScale = 1;
var translateX = 0;
var translateY = 0;
var zoomFactor = 1.2;
var connectingPointXOffset = 7;
var connectingPointYOffset = 7;
var selectedOffset = 5;
var selectedRoundness = 10;
var minTextOffset = 3;
var connectorFromOffset = -2;
var connectorToOffset = 4;
var connectorCurveFactor = 1.5;
var connectorRightAngleFactor = 0.2;
var nextCurveOffset = 15;
var nextCurveLineOffset = 8;
var nextConnectorLoopHeight = 15;
var nextConnectorLoopOffset = 15;
var nextLineOffset = 10;
var nextRightAngleLineOffset = 15;
var nextRightAngleHalfOffset = 30;
var nextRightAngleTurnOffset = 10;
var nextRightAngleLoopOffset = 10;
var connectorLoopWidth = 30;
var connectorLoopHeight = 80;
var connectorStraightHeight = 50;
var connectorRightAngleHeight = 25;
var connectorRightAngleLoop = 20;
var dragElement = null;
var boundingBox = false;
var boundingBoxSelect = false;
var panning = false;
var panX = 0;
var panY = 0;
var popupX = 0;
var popupY = 0;
var popupElement = null;
var hiddenTextElement = null;
var addNodeNumber = 0;
var newConnectorPath = null;
var selectedSet = new Object();
var nodeMap = new Object();
var typeMap = new Object();
var deleted = new Array();
var mainMenu = null;
var connectorTypeMenu = null;
var renameMenu = null;
var visibilityMenu = null;
var showAllMenu = null;
var nextHandleNumber = 0;
var autoLayoutOffsetWidth = 50;
var autoLayoutOffsetHeight = 20;
var bestFitOffsetWidth = 4;
var bestFitOffsetHeight = 4;
var descriptionTimerID = 0;
var descriptionDelay = 1000;
var descriptionText = null;
var descriptionTextFactor = 1.5;
var descriptionTarget = null;
var descriptionOffset = 5;
var descriptionX = 0;
var descriptionY = 0;
var descriptionVisible = false;
var RIGHT_BUTTON = 2;
var DELETE_KEY = 46;
var ALT_KEY_CODE = 18;
var CONNECTION_POINT_INDEX = 3;
var CONNECTOR_NAME_INDEX = 3;
var ADD_MENU_ID = 'addmenu';
var ZOOM_MENU_ID = 'zoommenu';
var AUTO = 0;
var LEFT = 1;
var RIGHT = 2;
var TOP = 3;
var BOTTOM = 4;
var WAVY = 0;
var STRAIGHT = 1;
var RIGHT_ANGLES = 2;
var I_MODIFY_SYMBOLS = 1;
var I_MODIFY_CONNECTORS = 2;
var I_MOVE_SYMBOLS = 4;
var I_MOVE_VIEW = 8;
var I_ALL = I_MODIFY_SYMBOLS | I_MODIFY_CONNECTORS | I_MOVE_SYMBOLS | I_MOVE_VIEW;

function clearDescriptionTimer(e, check)
{
  if(descriptionTimerID){
    if(check && e.clientX() == descriptionX && e.clientY() == descriptionY)
      return false;
    clearTimeout(descriptionTimerID);
    descriptionTimerID = 0;
    return check && descriptionText;
  }
}

function startDescriptionTimer(e, target, description)
{
  if(descriptionVisible)
    return;
  descriptionTarget = target;
  descriptionText = description;
  descriptionX = e.clientX();
  descriptionY = e.clientY();
  descriptionTimerID = setTimeout('showDescription();', descriptionDelay);
}

function showDescription()
{
  descriptionVisible = true;
  var svg = document.getElementById('descriptionText');
  svg.getStyle().setProperty('display', 'inherit');
  svg.setAttribute('x', descriptionX);
  svg.setAttribute('y', descriptionY);
  var rect = svg.firstChild;
  rect.setAttribute('width', 1);
  rect.setAttribute('height', 1);
  var children = svg.childNodes;
  var l = children.length;
  while(--l > 0)
    svg.removeChild(svg.lastChild);
  var text = descriptionText.split('\n');
  l = text.length;
  for(var i = 0; i < l; ++i){
    var t = document.createElement('text');
    t.setAttribute('x', descriptionOffset);
    t.setAttribute('y', (i + 1) * descriptionTextFactor + 'em');
    t.setAttribute('class', 'descriptionText');
    t.appendChild(document.createTextNode(text[i]));
    svg.appendChild(t);
  }
  var r = svg.getBBox();
  rect.setAttribute('width', r.width + descriptionOffset);
  rect.setAttribute('height', r.height + descriptionOffset);
}

function hideDescription()
{
  descriptionVisible = false;
  var svg = document.getElementById('descriptionText');
  svg.getStyle().setProperty('display', 'none');
}

function SetDragger()
{
  var froms = new Object();
  var tos = new Object();
  for(var i in selectedSet)
  {
    var d = selectedSet[i];
    var svg = d.svg;
    var id = svg.id;
    if(id.length > 0){
      svg.moveX = parseFloat(svg.getAttribute('x'));
      svg.moveY = parseFloat(svg.getAttribute('y'));
      addConnectorsToSet(d.froms, froms);
      addConnectorsToSet(d.tos, tos);
    }
  }
  this.froms = setToArray(froms);
  this.tos = setToArray(tos);

  this.getAttribute = function(a)
  {
    return 0;
  }

  this.setAttribute = function(a, v)
  {
    if(a == 'x'){
      v = parseFloat(v);
      for(var i in selectedSet){
        var svg = selectedSet[i].svg;
        if(svg.id.length > 0)
          svg.setAttribute('x', svg.moveX + v);
      }
    }
    else if(a == 'y'){
      v = parseFloat(v);
      for(var i in selectedSet){
        var svg = selectedSet[i].svg;
        if(svg.id.length > 0)
          svg.setAttribute('y', svg.moveY + v);
      }
    }
  }

  nodeMap[this.id] = this;
}

function addConnectorsToSet(c, s)
{
  if(c == null)
    return;
  var l = c.length;
  while(--l >= 0){
    var t = c[l];
    s[t.svg.connectorId] = t;
  }
}

function setToArray(s)
{
  var a = new Array();
  for(var i in s)
    a[a.length] = s[i];
  return a;
}

function onClickNode(e)
{
  var svg = e.currentTarget;
  var selectedRect = svg.firstChild;
  selectedRect.setAttribute('style', 'opacity:1;');
  if(e.ctrlKey == false)
    if(unselectSelected(svg))
      return;
  var id = svg.id;
  selectedSet[id] = nodeMap[id];
}

function onClickConnector(e)
{
  var svg = e.currentTarget.parentNode;
  var selectedPath = svg.firstChild;
  selectedPath.setAttribute('style', 'opacity:1;');
  if(e.ctrlKey == false)
    if(unselectSelected(svg))
      return;
  var id = svg.connectorId;
  selectedSet[id] = nodeMap[id];
}

function onClickConnectingPoint(e)
{
  e.stopPropagation();
}

function onMouseMove(e)
{
  if(descriptionVisible){
    var t = e.target;
    var d = document.getElementById('descriptionText');
    while(t){
      if(t == descriptionTarget || t == d)
        break;
      t = t.parentNode;
    }
    if(t == null){
      hideDescription();
      if(descriptionTarget.id)
        descriptionTarget.childNodes.item(CONNECTION_POINT_INDEX).getStyle().setProperty('display', 'none');
    }
  }
  else if(clearDescriptionTimer(e, true))
    startDescriptionTimer(e, descriptionTarget, descriptionText);
  if(hiddenTextElement){
    var ex = e.clientX() / currentScale - translateX;
    var ey = e.clientY() / currentScale - translateY;
    var lx = hiddenTextElement.lowX;
    var ly = hiddenTextElement.lowY;
    var hx = hiddenTextElement.highX;
    var hy = hiddenTextElement.highY;
    if(ex < lx || ey < ly || ex > hx || ey > hy){
      hiddenTextElement.svg.getStyle().setProperty('display', 'inherit');
      hiddenTextElement = null;
    }
  }
  if(dragElement){
    dragElement.setAttribute('x', dragElement.moveX + e.clientX() / currentScale);
    dragElement.setAttribute('y', dragElement.moveY + e.clientY() / currentScale);
    var d = nodeMap[dragElement.id];
    moveConnectors(d.tos);
    moveConnectors(d.froms);
  }
  else if(boundingBox){
    var nx = e.clientX();
    var ny = e.clientY();
    if(nx < boundingBoxX){
      boundingBox.setAttribute('x', nx);
      boundingBox.setAttribute('width', boundingBoxX - nx);
    }
    else{
      boundingBox.setAttribute('x', boundingBoxX);
      boundingBox.setAttribute('width', nx - boundingBoxX);
    }
    if(ny < boundingBoxY){
      boundingBox.setAttribute('y', ny);
      boundingBox.setAttribute('height', boundingBoxY - ny);
    }
    else{
      boundingBox.setAttribute('y', boundingBoxY);
      boundingBox.setAttribute('height', ny - boundingBoxY);
    }
  }
  else if(newConnectorPath){
    var node = newConnectorPath.from;
    var svg = node.svg;
    var x = e.clientX() / currentScale - translateX;
    var y = e.clientY() / currentScale - translateY;
    var r = svg.getBBox();
    var rx = r.x;
    var ry = r.y;
    if(x >= rx && x < rx + node.nodeWidth && y >= ry && y < ry + r.height ){
      var connectionPoint = svg.childNodes.item(CONNECTION_POINT_INDEX);
      connectionPoint.setAttribute('style', 'display:inherit;');
      newConnectorPath.to = node;
    }
    var da = getConnectorPathToPoint(node, x, y);
    newConnectorPath.setAttribute('d', da[0]);
  }
  else if(panning){
    var panner = document.getElementById('flowchart');
    translateX = e.clientX() / currentScale - panX;
    translateY = e.clientY() / currentScale - panY;
    panner.setAttribute('transform', 'translate(' + translateX + ', ' + translateY + ')');
  }
}

function onMouseDownNode(e)
{
  clearDescriptionTimer(e, false);
  var svg = e.currentTarget;
  if(e.button == RIGHT_BUTTON){
    popupX = e.clientX() / currentScale - translateX;
    popupY = e.clientY() / currentScale - translateY;
    var menu = cloneMenu(mainMenu, [ ADD_MENU_ID, ZOOM_MENU_ID ], [ interactive & I_MODIFY_SYMBOLS, interactive & I_MOVE_VIEW ]);
    if(renameMenu && (interactive & I_MODIFY_SYMBOLS))
      menu.appendChild(cloneMenu(renameMenu));
    addVisibilityMenu(menu, true);
    var typeData = nodeMap[svg.id].typeData;
    var submenu = typeData.menu;
    if(submenu){
      submenu = submenu.cloneNode(true);
      var c = submenu.childNodes;
      if(c){
        var l = c.length;
        if(l > 0){
          menu.appendChild(contextMenu.createElement('separator'));
          for(var i = 0; i < l; ++i){
            var t = c.item(i);
            if(t && t.tagName)
              menu.appendChild(t);
          }
        }
      }
    }
    contextMenu.replaceChild(menu, contextMenu.firstChild);
    popupElement = svg;
  }
  else{
    if(interactive & I_MODIFY_CONNECTORS){
      var connectionPoint = svg.childNodes.item(CONNECTION_POINT_INDEX);
      connectionPoint.setAttribute('style', 'display:none;');
    }
    if(interactive & I_MOVE_SYMBOLS){
      if(hasSelected() && e.ctrlKey)
        dragElement = new SetDragger();
      else
        dragElement = svg;
      dragElement.moveX = dragElement.getAttribute('x') - e.clientX() / currentScale;
      dragElement.moveY = dragElement.getAttribute('y') - e.clientY() / currentScale;
    }
  }
  e.stopPropagation();
}

function onMouseOverConnector(e)
{
  var svg = e.currentTarget.parentNode;
  clearDescriptionTimer(e, false);
  var data = nodeMap[svg.connectorId];
  var d = data.description;
  if(d)
    startDescriptionTimer(e, svg, d);
  e.stopPropagation();
}

function onMouseOutConnector(e)
{
  if(descriptionVisible){
    if(e.clientX() == descriptionX && e.clientY() == descriptionY)
      return;
    hideDescription();
  }
  clearDescriptionTimer(e, false);
  e.stopPropagation();
}

function onMouseDownConnector(e)
{
  clearDescriptionTimer(e, false);
  var svg = e.currentTarget.parentNode;
  if(e.button == RIGHT_BUTTON){
    popupX = e.clientX() / currentScale - translateX;
    popupY = e.clientY() / currentScale - translateY;
    var menu = cloneMenu(mainMenu, [ ADD_MENU_ID, ZOOM_MENU_ID ], [ interactive & I_MODIFY_SYMBOLS, interactive & I_MOVE_VIEW ]);
    if(interactive & I_MODIFY_CONNECTORS){
      if(connectorTypeMenu){
        submenu = cloneMenu(connectorTypeMenu);
        menu.appendChild(submenu);
      }
      if(renameMenu)
        menu.appendChild(cloneMenu(renameMenu));
    }
    addVisibilityMenu(menu);
    var typeData = nodeMap[svg.connectorId].typeData;
    var submenu = typeData.menu;
    if(submenu){
      submenu = submenu.cloneNode(true);
      var c = submenu.childNodes;
      if(c){
        var l = c.length;
        if(l > 0){
          menu.appendChild(contextMenu.createElement('separator'));
          for(var i = 0; i < l; ++i){
            var t = c.item(i);
            if(t && t.tagName)
              menu.appendChild(t);
          }
        }
      }
    }
    contextMenu.replaceChild(menu, contextMenu.firstChild);
    popupElement = svg;
  }
  e.stopPropagation();
}

function onMouseDownConnectingPoint(e)
{
  clearDescriptionTimer(e, false);
  var svg = e.currentTarget;
  svg = svg.parentNode;
  var node = nodeMap[svg.id];
  var da = getConnectorPathToPoint(node, e.clientX() / currentScale - translateX, e.clientY() / currentScale - translateY);
  newConnectorPath = document.createElement('path');
  newConnectorPath.from = node;
  newConnectorPath.setAttribute('d', da[0]);
  newConnectorPath.setAttribute('class', 'Connector');
  newConnectorPath.setAttribute('marker-end', 'url(\#Marker)');
  document.getElementById('flowchart').appendChild(newConnectorPath);
  e.stopPropagation();
}

function onMouseDown(e)
{
  clearDescriptionTimer(e, false);
  if(e.button == RIGHT_BUTTON){
    popupX = e.clientX() / currentScale - translateX;
    popupY = e.clientY() / currentScale - translateY;
    var menu = cloneMenu(mainMenu, [ ADD_MENU_ID, ZOOM_MENU_ID ], [ interactive & I_MODIFY_SYMBOLS, interactive & I_MOVE_VIEW ]);
    addVisibilityMenu(menu);
    contextMenu.replaceChild(menu, contextMenu.firstChild);
    popupElement = null;
  }
  else if(e.altKey){
    if(interactive & I_MOVE_VIEW){
      panning = true;
      panX = e.clientX() / currentScale - translateX;
      panY = e.clientY() / currentScale - translateY;
    }
  }
  else if(e.shiftKey || e.ctrlKey){
    boundingBoxSelect = e.shiftKey;
    boundingBoxX = e.clientX();
    boundingBoxY = e.clientY();
    boundingBox = document.getElementById('boundingbox');
    boundingBox.setAttribute('style', 'display:inherit;');
    boundingBox.setAttribute('x', boundingBoxX);
    boundingBox.setAttribute('y', boundingBoxY);
    boundingBox.setAttribute('width', 0);
    boundingBox.setAttribute('height', 0);
  }
  else
    unselectSelected(null);
}

function onMouseUp(e)
{
  if(dragElement){
    var mx = dragElement.moveX + e.clientX() / currentScale;
    var my = dragElement.moveY + e.clientY() / currentScale;
    if(gridCellWidth > 1)
      mx = Math.round(mx / gridCellWidth) * gridCellWidth;
    if(gridCellHeight > 1)
      my = Math.round(my / gridCellHeight) * gridCellHeight;
    var svg = dragElement;
    svg.setAttribute('x', mx);
    svg.setAttribute('y', my);
    var d = nodeMap[svg.id];
    moveConnectors(d.tos);
    moveConnectors(d.froms);
  }
  if(boundingBox){
    var x = e.clientX();
    var y = e.clientY();
    var w = x - boundingBoxX;
    var h = y - boundingBoxY;
    if(x < boundingBoxX)
      w = -w;
    else
      x = boundingBoxX;
    if(y < boundingBoxY)
      h = -h;
    else
      y = boundingBoxY;
    x /= currentScale;
    y /= currentScale;
    w /= currentScale;
    h /= currentScale;
    if(boundingBoxSelect){
      x -= translateX;
      y -= translateY;
      var ex = x + w;
      var ey = y + h;
      if(e.ctrlKey == false)
        unselectSelected(svg);
      var root = document.getElementById('flowchart');
      var children = root.childNodes;
      var l = children.length;
      for(var i = 0; i < l; ++i){
        var svg = children.item(i);
        var r = svg.getBBox();
        var rx = r.x;
        if(rx > ex || rx + r.width < x)
          continue;
        var ry = r.y;
        if(ry > ey || ry + r.height < y)
          continue;
        svg.firstChild.setAttribute('style', 'display:inherit;');
        var id = svg.id;
        if(id.length>0)
          selectedSet[id] = nodeMap[id];
        else{
          id = svg.connectorId;
          selectedSet[id] = nodeMap[id];
        }
      }
    }
    else{
      var svg = document.getElementById('flowchart');
      if(w > 0 && h > 0 ){
        var embed = parent.document.getElementById(chartId);
        var panner = svg;
        var zoomer = panner.parentNode;
        var zw = embed.width / w;
        var zh = embed.height / h;
        var s = zw > zh ? zh : zw;
        currentScale = s;
        zoomer.setAttribute('transform', 'scale(' + currentScale + ')' );
        translateX -= x;
        translateY -= y;
        panner.setAttribute('transform', 'translate(' + translateX + ', ' + translateY + ')');
      }
    }
    boundingBox.setAttribute('style', 'display:none;');
  }
  if(newConnectorPath){
    var f = newConnectorPath.from.svg;
    var t = newConnectorPath.to;
    var p = newConnectorPath.parentNode;
    if(p)
      p.removeChild(newConnectorPath);
    if(t)
      createConnectorSvg(f, t.svg, '', '', null);
  }
  dragElement = null;
  boundingBox = null;
  newConnectorPath = null;
  panning = false;
}

function onMouseOverNode(e)
{
  var svg = e.currentTarget;
  if(interactive & I_MODIFY_CONNECTORS){
    var cp = svg.childNodes.item(CONNECTION_POINT_INDEX);
    cp.setAttribute('style', 'display:inherit;');
    if(newConnectorPath)
      newConnectorPath.to = nodeMap[svg.id];
  }
  clearDescriptionTimer(e, false);
  var data = nodeMap[svg.id];
  var d = data.description;
  if(d)
    startDescriptionTimer(e, svg, d);
  e.stopPropagation();
}

function onMouseOutNode(e)
{
  if(descriptionVisible){
    if(e.clientX() == descriptionX && e.clientY() == descriptionY)
      return;
    hideDescription();
  }
  clearDescriptionTimer(e, false);
  var svg = e.currentTarget;
  var cp = svg.childNodes.item(CONNECTION_POINT_INDEX);
  cp.setAttribute('style', 'display:none;');
  if(newConnectorPath)
    newConnectorPath.to = null;
  e.stopPropagation();
}

function onMouseOverText(e)
{
  var svg = e.currentTarget;
  if(newConnectorPath){
    svg.getStyle().setProperty('display', 'none');
    hiddenTextElement = new Object();
    var tr = svg.getBBox();
    var pr = svg.parentNode.getBBox();
    var x = tr.x + pr.x;
    var y = tr.y + pr.y;
    hiddenTextElement.lowX = x;
    hiddenTextElement.highX = x + tr.width;
    hiddenTextElement.lowY = y;
    hiddenTextElement.highY = y + tr.height;
    hiddenTextElement.svg = svg;
  }
  else if(dragElement == null){
    var children = svg.childNodes;
    var dotsStyle = children.item(1).getStyle();
    if(dotsStyle.getPropertyValue('display') == 'inherit'){
      var node = nodeMap[svg.parentNode.id];
      var cutSvg = children.item(0);
      cutSvg.removeAttribute('width');
      dotsStyle.setProperty('display', 'none');
    }
  }
}

function onMouseOutText(e)
{
  var svg = e.currentTarget;
  if(newConnectorPath)
    return;
  var children = svg.childNodes;
  var cutSvg = children.item(0);
  if(cutSvg.getAttribute('width') == ''){
    var node = nodeMap[svg.parentNode.id];
    cutSvg.setAttribute('width', node.textWidth);
    children.item(1).getStyle().setProperty('display', 'inherit');
  }
}

function onKeyDown(e)
{
}

function onKeyUp(e)
{
  if(boundingBox){
    if(boundingBoxSelect){
      if(e.ctrlKey == false){
        boundingBox.setAttribute('style', 'display:none;');
        boundingBox = null;
      }
    }
    else{
      if(e.shiftKey == false){
        boundingBox.setAttribute('style', 'display:none;');
        boundingBox = null;
      }
    }
  }
  else if(e.keyCode == DELETE_KEY){
    var symbols = interactive & I_MODIFY_SYMBOLS;
    var connectors = interactive & I_MODIFY_CONNECTORS;
    if(symbols || connectors){
      for(var i in selectedSet){
        var d = selectedSet[i];
        var svg = d.svg;
        var id = svg.id;
        if(id.length > 0){
          if(symbols){
            deleteConnectors(d.froms);
            deleteConnectors(d.tos);
            var n = nodeMap[id];
            if(n.old)
              deleted[deleted.length] = n;
            var p = svg.parentNode;
            if(p)
              p.removeChild(svg);
          }
        }
        else if(connectors){
          id = svg.connectorId;
          var n = nodeMap[id];
          if(n.old)
            deleted[deleted.length] = n;
          nodeMap[id] = null;
          var p = svg.parentNode;
          if(p)
            p.removeChild(svg);
        }
      }
      selectedSet = new Object();
    }
  }
}

function onZoom(factor, absolute)
{
  if(absolute)
    currentScale = factor;
  else
    currentScale *= factor;
  var zoomer = document.getElementById('zoomer');
  zoomer.setAttribute('transform', 'scale(' + currentScale + ')' );
}

function onBestFit()
{
  var svg = document.getElementById('flowchart');
  var embed = parent.document.getElementById(chartId);
  var r = svg.getBBox();
  var panner = svg;
  var zoomer = panner.parentNode;
  var w = embed.width;
  var h = embed.height;
  var zw = w / ( r.width + 2 * bestFitOffsetWidth );
  var zh = h / ( r.height + 2 * bestFitOffsetHeight );
  currentScale = zw > zh ? zh : zw;
  zoomer.setAttribute('transform', 'scale(' + currentScale + ')' );
  translateX = bestFitOffsetWidth - r.x;
  translateY = bestFitOffsetHeight - r.y;
  panner.setAttribute('transform', 'translate(' + translateX + ', ' + translateY + ')');
}

function onRestore()
{
  var panner = document.getElementById('flowchart');
  var zoomer = panner.parentNode;
  zoomer.setAttribute('transform', 'scale(1)');
  panner.setAttribute('transform', 'translate(0, 0)');
  currentScale = 1;
  translateX = 0;
  translateY = 0;
}

function unselectSelected(svg)
{
  var r = false;
  for(var i in selectedSet){
    var s = selectedSet[i].svg;
    if(s == svg)
      r = true;
    s.childNodes.item(0).setAttribute('style','opacity:0;');
  }
  selectedSet = new Object();
  return r;
}

function hasSelected()
{
  for(var i in selectedSet)
    return true;
  return false;
}

function isHidden(svg)
{
  return svg.getStyle().getPropertyValue('display') == 'none';
}

function isVisible(svg)
{
  return isHidden(svg) ? false : true;
}

function isMenuIf(id, ifid, ifcondition)
{
  if(typeof ifid == 'string')
    return id == ifid && !ifcondition;
  var l = ifid.length;
  while(--l >= 0)
    if(id == ifid[l])
      return !ifcondition[l];
  return false;
}

function cloneMenu(menu, ifid, ifcondition)
{
  if(ifid && isMenuIf(menu.id, ifid, ifcondition))
    return null;
  var x = contextMenu.createElement(menu.tagName);
  var a = menu.attributes;
  var l = a.length;
  for(var i = 0; i < l; ++i){
    var t = a.item(i);
    x.setAttribute(t.name, t.value);
  }
  a = menu.childNodes;
  l = a.length;
  for(var i = 0; i < l; ++i){
    var t = a.item(i);
    if(t.tagName)
      t = cloneMenu(t, ifid, ifcondition);
    else
      t = contextMenu.createTextNode(t.nodeValue);
    if(t)
      x.appendChild(t);
  }
  return x;
}

function addVisibilityMenu(menu, node)
{
  if(hasSelected() || node){
    if(visibilityMenu)
      menu.appendChild(cloneMenu(visibilityMenu));
  }
  else if(showAllMenu)
    menu.appendChild(cloneMenu(showAllMenu));
}

function moveConnector(connector)
{
  var svg = connector.svg;
  if(svg.parentNode == null)
    return;
  var da = getConnectorPath(connector.from, connector.to, svg.connectorNumber);
  var d = da[0];
  var c = svg.childNodes;
  c.item(0).firstChild.setAttribute('d', d);
  var path = c.item(1);
  var reversePath = c.item(2);
  path.setAttribute('d', d);
  reversePath.setAttribute('d', da[1]);
  c.item(CONNECTOR_NAME_INDEX).firstChild.setAttributeNS('http://www.w3.org/1999/xlink', 'href', '#' + (da[2] ? reversePath.id : path.id));
}

function moveConnectors(connectors)
{
  if(connectors == null)
    return;
  var l = connectors.length;
  for(var i = 0; i < l; ++i)
    moveConnector(connectors[i]);
}

function deleteConnectors(connectors)
{
  if(connectors == null)
    return;
  var l = connectors.length;
  for(var i = 0; i < l; ++i){
    var svg = connectors[i].svg;
    var n = nodeMap[svg.connectorId];
    if(n.old)
      deleted[deleted.length] = n;
    var p = svg.parentNode;
    if(p)
      p.removeChild(svg);
  }
}

function showConnector(connector)
{
  if(isHidden(connector.to.svg) || isHidden(connector.from.svg))
    return;
  connector.svg.getStyle().setProperty('display', 'inherit');
}

function hideConnectors(connectors)
{
  if(connectors == null)
    return;
  var l = connectors.length;
  for(var i = 0; i < l; ++i)
    connectors[i].svg.getStyle().setProperty('display', 'none');
}

function getConnectorCount(connectors)
{
  if(connectors == null)
    return 0;
  var l = connectors.length;
  var count = l;
  for(var i = 0; i < l; ++i)
    if(isHidden(connectors[i].svg))
      --count;
  return count;
}

function addSymbol(type)
{
  var id;
  for(;;){
    id = 'node' + ++addNodeNumber;
    if(document.getElementById(id) == null)
      break;
  }
  var text = 'Node ' + addNodeNumber;
  createSymbol(id, type, text, null, popupX / gridCellWidth, popupY / gridCellHeight, false);
}

function createSymbol(name, type, text, description, x, y, hidden, saveold)
{
  var data = new Object();
  var typeData = typeMap[type];
  if(typeData == null){
    typeData = new Object();
    var source = document.getElementById(type);
    typeData.svg = source;
    if(source){
      var c = source.childNodes;
      if(c){
        var l = c.length;
        if(l > 1){
          var m = c.item(l - 2);
          if(m.tagName == 'menu')
            typeData.menu = cloneMenu(m);
        }
      }
    }
  }
  data.handleNumber = 0;
  data.typeData = typeData;
  data.description = description;
  nodeMap[name] = data;
  if(saveold){
    data.old = true;
    data.oldX = x;
    data.oldY = y;
    data.oldHidden = hidden ? 'true' : 'false';
    data.oldName = text;
  }
  var svg = document.createElement('svg');
  svg.id = name;
  svg.setAttribute('symbolType', type);
  svg.setAttribute('onclick', 'onClickNode(evt);');
  svg.setAttribute('onmousedown', 'onMouseDownNode(evt);');
  svg.setAttribute('onmouseover', 'onMouseOverNode(evt);');
  svg.setAttribute('onmouseout', 'onMouseOutNode(evt);');
  x *= gridCellWidth;
  y *= gridCellHeight;
  svg.setAttribute('x', x);
  svg.setAttribute('y', y);

  var use = document.createElement('use');
  use.setAttribute('x', selectedOffset);
  use.setAttribute('y', selectedOffset);
  use.setAttributeNS('http://www.w3.org/1999/xlink', 'href', '#' + type);
  svg.appendChild(use);

  document.getElementById('flowchart').appendChild(svg);

  var r = svg.getBBox();
  var w = r.width + r.x - x;
  var h = r.height + r.y - y;
  var selectedRect = document.createElement('rect');
  selectedRect.setAttribute('class', 'selected');
  selectedRect.setAttribute('rx', selectedRoundness);
  selectedRect.setAttribute('ry', selectedRoundness);
  w += 2 * selectedOffset;
  selectedRect.setAttribute('width', w);
  svg.insertBefore(selectedRect, use);
  data.nodeWidth = w;

  var textSvg = document.createElement('svg');
  textSvg.setAttribute('y', h);
  textSvg.setAttribute('onmouseover', 'onMouseOverText(evt);');
  textSvg.setAttribute('onmouseout', 'onMouseOutText(evt);');
  var cutSvg = document.createElement('svg');
  textSvg.appendChild(cutSvg);
  var t = document.createElement('text');
  t.setAttribute('y', '1.5em');
  t.setAttribute('text-anchor', 'middle');
  t.appendChild(document.createTextNode(text));
  cutSvg.appendChild(t);
  t = document.createElement('text');
  t.setAttribute('y', '1.5em');
  t.appendChild(document.createTextNode('...'));
  textSvg.appendChild(t);
  svg.insertBefore(textSvg, use);
  var dw = t.getBBox().width;
  w -= dw;
  data.textWidth = w;
  data.dotsWidth = dw;
  cutSvg.setAttribute('width', w);
  t.setAttribute('x', w);

  r = svg.getBBox();
  h = r.height + r.y - y;
  selectedRect.setAttribute('height', h + 2 * selectedOffset);
  selectedRect.setAttribute('style', 'opacity:0;');

  setTextProperties(w, dw, textSvg);

  use = document.createElement('use');
  use.setAttribute('onclick', 'onClickConnectingPoint(evt);');
  use.setAttribute('onmousedown', 'onMouseDownConnectingPoint(evt);');
  use.setAttribute('x', w - connectingPointXOffset);
  use.setAttribute('y', connectingPointYOffset);
  use.setAttributeNS('http://www.w3.org/1999/xlink', 'href', '#ConnectingPoint');
  use.setAttribute('style', 'display:none;');
  svg.appendChild(use);
  if(hidden == 'true')
    svg.getStyle().setProperty('display', 'none');
  data.svg = svg;
}

function createConnector(from, to, type, name, description, saveold)
{
  var f = document.getElementById(from);
  var t = document.getElementById(to);
  if(f == null || t == null)
    return;
  createConnectorSvg(f, t, type, name, description, saveold);
}

function createConnectorSvg(f, t, type, name, description, saveold)
{
  var x = f.id + '_' + t.id + '_';
  var id = null;
  var c = 0;
  for(;;){
    id = x + c;
    if(nodeMap[id])
      ++c;
    else
      break;
  }
  var data = new Object();
  var typeData = typeMap[type];
  var source = null;
  if(typeData == null){
    typeData = new Object();
    typeMap[type] = typeData;
    source = document.getElementById(type);
    typeData.svg = source;
    if(source){
      var children = source.childNodes;
      if(children){
        var l = children.length;
        if(l > 1){
          var m = children.item(l - 2);
          if(m.tagName == 'menu')
            typeData.menu = cloneMenu(m);
        }
      }
    }
  }
  else
    source = typeData.svg;
  if(source && name.length == 0)
    name = source.getAttribute('text');
  data.typeData = typeData;
  data.description = description;
  data.handleNumber = 0;
  nodeMap[id] = data;
  if(saveold){
    data.old = true;
    data.oldConnectorType = type;
    data.oldName = name;
  }
  var fd = nodeMap[f.id];
  var td = nodeMap[t.id];
  var da = getConnectorPath(fd, td, c);
  var d = da[0];
  var svg = document.createElement('g');
  svg.connectorId = id;
  svg.connectorNumber = c; 
  svg.setAttribute('connectorType', type);
  svg.setAttribute('x', 0);
  svg.setAttribute('y', 0);

  var g = document.createElement('g');
  g.setAttribute('style', 'opacity:0;');
  svg.appendChild(g);
  var selectedPath = document.createElement('path');
  selectedPath.setAttribute('d', d);
  selectedPath.setAttribute('class', type + 'SelectedConnector');
  selectedPath.setAttribute('marker-end', 'url(\#' + type + 'SelectedMarker)');
  g.appendChild(selectedPath);

  var path = document.createElement('path');
  path.id = id + '_path';
  path.setAttribute('d', d);
  path.setAttribute('class', type + 'Connector');
  path.setAttribute('marker-end', 'url(\#' + type + 'Marker)');
  svg.appendChild(path);

  var reversePath = document.createElement('path');
  reversePath.id = id + '_reverse_path';
  reversePath.setAttribute('d', da[1]);
  reversePath.setAttribute('style', 'opacity:0; stroke-width:2;');
  reversePath.setAttribute('onclick', 'onClickConnector(evt);');
  reversePath.setAttribute('onmouseover', 'onMouseOverConnector(evt);');
  reversePath.setAttribute('onmouseout', 'onMouseOutConnector(evt);');
  reversePath.setAttribute('onmousedown', 'onMouseDownConnector(evt);');
  svg.appendChild(reversePath);

  var text = document.createElement('text');
  text.setAttribute('text-anchor', 'middle');
  text.setAttribute('class', type + 'Connector');
  var textPath = document.createElement('textPath');
  textPath.setAttributeNS('http://www.w3.org/1999/xlink', 'href', '#' + (da[2] ? reversePath.id : path.id));
  textPath.setAttribute('startOffset', '50%');
  textPath.appendChild(document.createTextNode(name));
  text.appendChild(textPath);
  svg.appendChild(text);
  if(isHidden(f) || isHidden(t))
    svg.getStyle().setProperty('display', 'none');
  document.getElementById('flowchart').appendChild(svg);

  data.from = fd;
  data.to = td;
  data.svg = svg;
  var fc = fd.froms;
  if(fc)
    fc[fc.length] = data;
  else{
    fc = new Array();
    fc[0] = data;
    fd.froms = fc;
  }
  var tc = td.tos;
  if(tc)
    tc[tc.length] = data;
  else{
    tc = new Array();
    tc[0] = data;
    td.tos = tc;
  }
}

function keepBetween(v, a, b)
{
  if(a <= b){
    if(v < a)
      return a;
    if(v > b)
      return b;
    return v;
  }
  if(v < b)
    return b;
  if(v > a)
    return a;
  return v;
}

function getConnectorPath(fn, tn, i)
{
  var f = fn.svg;
  var t = tn.svg;
  var next = Math.round(i & 1 ? i / 2 : i / -2);
  var self = fn == tn;
  var fr = f.getBBox();
  var tr = t.getBBox();
  var fx = fr.x;
  var fy = fr.y;
  var fw = fn.nodeWidth / 2;
  var fh = fr.height / 2;
  var tx = tr.x;
  var ty = tr.y;
  var tw = tn.nodeWidth / 2;
  var th = tr.height / 2;
  fx += fw;
  fy += fh;
  tx += tw;
  ty += th;
  var dx = fx < tx ? tx - fx : fx - tx;
  var dy = fy < ty ? ty - fy : fy - ty;
  var ox = 0;
  var oy = 0;
  var ix = 0;
  var iy = 0;
  if(outputLoc == AUTO){
    if(dx > dy)
      ox = fx < tx ? 1 : -1;
    else
      oy = fy < ty ? 1 : -1;
  }
  else if(outputLoc == LEFT)
    ox = -1;
  else if(outputLoc == RIGHT)
    ox = 1;
  else if(outputLoc == TOP)
    oy = -1;
  else if(outputLoc == BOTTOM)
    oy = 1;
  if(inputLoc == AUTO){
    if(dx > dy)
      ix = fx < tx ? -1 : 1;
    else
      iy = fy < ty ? -1 : 1;
  }
  else if(inputLoc == LEFT)
    ix = -1;
  else if(inputLoc == RIGHT)
    ix = 1;
  else if(inputLoc == TOP)
    iy = -1;
  else if(inputLoc == BOTTOM)
    iy = 1;
  if(self){
    if(inputLoc == AUTO){
      ix = ox;
      iy = oy;
    }
    else if(outputLoc == AUTO){
      ox = ix;
      oy = iy;
    }
  }
  var rtext = fx > tx;
  dx = fx < tx ? tx - fx : fx - tx;
  dy = fy < ty ? ty - fy : fy - ty;
  var oox = oy ? 1 : 0;
  var ooy = ox ? 1 : 0;
  var iox = iy ? 1 : 0;
  var ioy = ix ? 1 : 0;
  fx += ox * ( fw + connectorFromOffset );
  fy += oy * ( fh + connectorFromOffset );
  tx += ix * ( tw + connectorToOffset );
  ty += iy * ( th + connectorToOffset );
  var selfloop = self && ox == ix && oy == iy;
  if(connectorStyle == STRAIGHT){
    var n = nextLineOffset * next;
    if(selfloop){
      fx += connectorStraightHeight * ox;
      fy += connectorStraightHeight * oy;
    }
    fx += oox * n;
    fy += ooy * n;
    tx += iox * n;
    ty += ioy * n;
    var f = fx + ' ' + fy;
    var t = tx + ' ' + ty;
    nd = 'M' + f + 'L' + t;
    rd = 'M' + t + 'L' + f;
  }
  else if(connectorStyle == RIGHT_ANGLES){
    var n = nextRightAngleLineOffset * next;
    fx += oox * n;
    fy += ooy * n;
    tx += iox * n;
    ty += ioy * n;
    if(selfloop){
      var n = nextRightAngleLoopOffset * next;
      fx += ( connectorRightAngleHeight + n ) * ix;
      fy += ( connectorRightAngleHeight + n ) * iy;
      var h = connectorRightAngleLoop / 2;
      var l1x = fx + h * iy;
      var l1y = fy + h * ix;
      var l1 = l1x + ' ' + l1y;
      var l2x = l1x + connectorRightAngleLoop * ix;
      var l2y = l1y + connectorRightAngleLoop * iy;
      var l2 = l2x + ' ' + l2y;
      var l3x = l2x - connectorRightAngleLoop * iy;
      var l3y = l2y - connectorRightAngleLoop * ix;
      var l3 = l3x + ' ' + l3y;
      var l4x = l3x - connectorRightAngleLoop * ix;
      var l4y = l3y - connectorRightAngleLoop * iy;
      var l4 = l4x + ' ' + l4y;
      var f = fx + ' ' + fy;
      var t = tx + ' ' + ty;
      nd = 'M' + f + 'L' + l1 + ' ' + l2 + ' ' + l3 + ' ' + l4 + ' ' + f + ' ' + t;
      rd = 'M' + t + 'L' + f + ' ' + l4 + ' ' + l3 + ' ' + l2 + ' ' + l1 + ' ' + f;
    }
    else{
      var ftx = fx < tx ? 1 : -1;
      var fty = fy < ty ? 1 : -1;
      n = ftx * fty * 2 * nextRightAngleHalfOffset * next;
      var hx = keepBetween(( tx - fx - n ) / 2 + fx, fx, tx);
      var hy = keepBetween(( ty - fy - n ) / 2 + fy, fy, ty);
      if(self){
        if(ox + oy + ix + iy == 0){
          if(ox)
            hy = fr.y - connectorRightAngleLoop;
          else
            hx = fr.x + connectorRightAngleLoop;
        }
        dx = 2 * connectorRightAngleLoop;
        dy = dx;
      }
      var f = fx + ' ' + fy;
      var t = tx + ' ' + ty;
      var nd = 'M' + f + 'L';
      var rd = 'M' + t + 'L';
      var ne = t;
      var re = f;
      var rfx = ( tx - fx ) * ox < 0 ? 1 : 0;
      var rfy = ( ty - fy ) * oy < 0 ? 1 : 0;
      var rtx = ( fx - tx ) * ix < 0 ? 1 : 0;
      var rty = ( fy - ty ) * iy < 0 ? 1 : 0;
      var ufx = ox & 1;
      var ufy = oy & 1;
      var utx = ix & 1;
      var uty = iy & 1; 
      n = nextRightAngleTurnOffset * next;
      if(rfx || rfy){
        fx += ox * ( connectorRightAngleFactor * dx + n );
        fy += oy * ( connectorRightAngleFactor * dy + n );
      }
      else{
        fx = ufx * hx + ufy * fx;
        fy = ufy * hy + ufx * fy;
      }
      var l = fx + ' ' + fy + ' ';
      nd += l;
      re = l + re;
      if(rtx || rty){
        tx += ix * ( connectorRightAngleFactor * dx - n );
        ty += iy * ( connectorRightAngleFactor * dy - n );
      }
      else{
        tx = utx * hx + uty * tx;
        ty = uty * hy + utx * ty;
      }
      l = tx + ' '+ ty + ' ';
      ne = l + ne;
      rd += l;
      if(ufx == utx){
        fx = ufy * hx + ufx * fx;
        fy = ufx * hy + ufy * fy;
        l = fx + ' ' + fy + ' ';
        nd += l;
        re = l + re;
        tx = uty * hx + utx * tx;
        ty = utx * hy + uty * ty;
        l = tx + ' '+ ty + ' ';
        ne = l + ne;
        rd += l;
      }
      fx = ufx * fx + ufy * tx;
      fy = ufy * fy + ufx * ty;
      l = fx + ' ' + fy + ' ';
      nd += l;
      re = l + re;
      nd += ne;
      rd += re;
    }
  }
  else{
    var n = nextCurveLineOffset * next;
    var cx = dx / connectorCurveFactor;
    var cy = dy / connectorCurveFactor;
    if(self){
      n = i * nextConnectorLoopHeight;
      var fcx = fx + ox * ( connectorLoopHeight + n );
      var fcy = fy + oy * ( connectorLoopHeight + n );
      var tcx = tx + ix * ( connectorLoopHeight + n );
      var tcy = ty + iy * ( connectorLoopHeight + n );
      n = i * nextConnectorLoopOffset;
      if(ox)
        fcy += ox * connectorLoopWidth - n;
      else
        fcx += oy * connectorLoopWidth - n;
      if(ix)
        tcy -= ix * connectorLoopWidth - n;
      else
        tcx -= iy * connectorLoopWidth - n;
    }
    else{
      fx += oy * n;
      fy += ox * n;
      tx -= iy * n;
      ty -= ix * n;
      var ftx = fx < tx ? 1 : -1;
      var fty = fy < ty ? 1 : -1;
      var o = ftx * fty * nextCurveOffset * next;
      var fcx = fx + ox * ( cx - o );
      var fcy = fy + oy * ( cy - o );
      var tcx = tx + ix * ( cx + o );
      var tcy = ty + iy * ( cy + o );
    }
    var f = fx + ' ' + fy;
    var t = tx + ' ' + ty;
    var fc = fcx + ' ' + fcy;
    var tc = tcx + ' ' + tcy;
    nd = 'M' + f + 'C' + fc + ' ' + tc + ' ' + t;
    rd = 'M' + t + 'C' + tc + ' ' + fc + ' ' + f;
  }
  return [ nd, rd, rtext ];
}

function getConnectorPathToPoint(f, x, y)
{
  function r(x, y){ 
    this.x = x;
    this.y = y;
    this.width = 0;
    this.height = 0;
  }

  function b(x, y){
    this.getBBox = function ()
    {
      return new r(x, y);
    }
  }
  var to = new Object();
  to.nodeWidth = 1;
  to.svg = new b(x, y);
  return getConnectorPath(f, to, 0);
}

function setTextProperties(w, dw, textSvg)
{
  var children = textSvg.childNodes;
  var text = children.item(0).firstChild;
  var tx = w / 2;
  text.setAttribute('x', tx);
  var r = textSvg.getBBox();
  if(r.x < minTextOffset)
    text.setAttribute('x', tx - r.x + minTextOffset);
  var dotStyle = children.item(1).getStyle().setProperty('display', r.width <= w + dw ? 'none' : 'inherit');
}

function getNodeNameSvg(svg)
{
  return svg.childNodes.item(1).firstChild.firstChild.firstChild.nodeValue;
}

function setNodeNameSvg(svg, name)
{
  var node = nodeMap[svg.id];
  var textSvg = svg.childNodes.item(1);
  var textNode = textSvg.firstChild.firstChild.firstChild;
  textNode.nodeValue = name;
  setTextProperties(node.textWidth, node.dotsWidth, textSvg);
}

function getConnectorNameSvg(svg)
{
  return svg.childNodes.item(CONNECTOR_NAME_INDEX).firstChild.firstChild.nodeValue;
}

function setConnectorNameSvg(svg, name)
{
  svg.childNodes.item(CONNECTOR_NAME_INDEX).firstChild.firstChild.nodeValue = name;
}

function changeConnectorType(type, svg)
{
  svg.setAttribute('connectorType', type);
  var c = svg.childNodes;
  c.item(0).firstChild.setAttribute('class', type + 'SelectedConnector');
  c.item(1).setAttribute('class', type + 'Connector');
  c.item(3).setAttribute('class', type + 'Connector');
  var name = getConnectorNameSvg(svg);
  if(name.length == 0){
    var source = typeMap[type].svg;
    if(source)
      setConnectorNameSvg(svg, source.getAttribute('text'));
  }
}

function isConnector(svg)
{
  var cid = svg.connectorId;
  return cid && cid.length > 0;
}

function onRename(svg)
{
  if(isConnector(svg)){
    var n = prompt('New name. Use "." to clear name.', getConnectorNameSvg(svg));
    if(n && n.length > 0)
      setConnectorNameSvg(svg, n == '.' ? '' : n);
  }
  else{
    var n = prompt('New name. Use "." to clear name.', getNodeNameSvg(svg));
    if(n && n.length > 0)
      setNodeNameSvg(svg, n == '.' ? '' : n);
  }
}

function onShowAll()
{
  var root = document.getElementById('flowchart');
  var children = root.childNodes;
  var l = children.length;
  var nodes = [];
  var connectors = [];
  for(var i = 0; i < l; ++i){
    var svg = children.item(i);
    svg.getStyle().setProperty('display', 'inherit');
  }
}

function onHideAll()
{
  var root = document.getElementById('flowchart');
  var children = root.childNodes;
  var l = children.length;
  var nodes = [];
  var connectors = [];
  for(var i = 0; i < l; ++i){
    var svg = children.item(i);
    svg.getStyle().setProperty('display', 'none');
  }
}

function hideNode(node)
{
  hideConnectors(node.froms);
  hideConnectors(node.tos);
  node.svg.getStyle().setProperty('display', 'none');
}

function onHide(svg)
{
  if(hasSelected()){
    for(var i in selectedSet){
      var node = selectedSet[i];
      if(node.svg.id.length > 0)
        hideNode(node);
      else{
        hideNode(node.from);
        hideNode(node.to);
      }
    }
    unselectSelected();
  }
  else
    hideNode(nodeMap[svg.id]);
}

function onShowUpstream(svg)
{
  onHideAll();
  ++nextHandleNumber;
  var connectors = new Object();
  if(hasSelected()){
    for(var i in selectedSet){
      var node = selectedSet[i];
      if(node.svg.id.length > 0)
        showUpstream(node, connectors);
      else
        showUpstream(node.from, connectors);
    }
    unselectSelected();
  }
  else{
    var node = nodeMap[svg.id];
    showUpstream(node, connectors);
  }
  for(var i in connectors)
    showConnector(connectors[i]);
}

function showUpstream(node, connectors)
{
  if(node.handleNumber == nextHandleNumber)
    return;
  node.handleNumber = nextHandleNumber;
  node.svg.getStyle().setProperty('display', 'inherit');
  var t = node.tos;
  if(t){
    var l = t.length;
    for(var i = 0; i < l; ++i){
      var connector = t[i];
      connectors[connector.svg.connectorId] = connector;
      var from = connector.from;
      showUpstream(from, connectors);
    }
  }
}

function onShowDownstream(svg)
{
  onHideAll();
  ++nextHandleNumber;
  var connectors = new Object();
  if(hasSelected()){
    for(var i in selectedSet){
      var node = selectedSet[i];
      if(node.svg.id.length > 0)
        showDownstream(node, connectors);
      else
        showDownstream(node.from, connectors);
    }
    unselectSelected();
  }
  else{
    var node = nodeMap[svg.id];
    showDownstream(node, connectors);
  }
  for(var i in connectors)
    showConnector(connectors[i]);
}

function showDownstream(node, connectors)
{
  if(node.handleNumber == nextHandleNumber)
    return;
  node.handleNumber = nextHandleNumber;
  node.svg.getStyle().setProperty('display', 'inherit');
  var f = node.froms;
  if(f){
    var l = f.length;
    for(var i = 0; i < l; ++i){
      var connector = f[i];
      connectors[connector.svg.connectorId] = connector;
      var to = connector.to;
      showDownstream(to, connectors);
    }
  }
}

function toPostData()
{
  var root = document.getElementById('flowchart');
  var children = root.childNodes;
  var l = children.length;
  var nodes = [];
  var connectors = [];
  for(var i = 0; i < l; ++i){
    var svg = children.item(i);
    var id = svg.id;
    if(id.length>0){
      var node = new Object();
      node.id = id;
      node.symbolType = svg.getAttribute('symbolType');
      node.name = getNodeNameSvg(svg);
      node.x = svg.getAttribute('x') / gridCellWidth;
      node.y = svg.getAttribute('y') / gridCellHeight;
      node.hidden = isHidden(svg) ? 'true' : 'false';
      var n = nodeMap[id];
      node.old = n.old;
      node.oldName = n.oldName;
      node.oldX = n.oldX;
      node.oldY = n.oldY;
      node.oldHidden = n.oldHidden;
      nodes[nodes.length] = node;
    }
    else{
      var connector = new Object();
      var c = nodeMap[svg.connectorId];
      connector.from = c.from.svg.id;
      connector.to = c.to.svg.id;
      connector.connectorType = svg.getAttribute('connectorType');
      connector.name = getConnectorNameSvg(svg);
      connector.old = c.old;
      connector.oldName = c.oldName;
      connector.oldConnectorType = c.oldConnectorType;
      connectors[connectors.length] = connector;
    }
  }
  l = deleted.length;
  for(var i = 0; i < l; ++i){
    var n = deleted[i];
    var svg = n.svg;
    var id = svg.id;
    if(id.length>0){
      var node = new Object();
      node.id = id;
      node.deleted = true;
      node.symbolType = svg.getAttribute('symbolType');
      node.name = getNodeNameSvg(svg);
      node.x = svg.getAttribute('x') / gridCellWidth;
      node.y = svg.getAttribute('y') / gridCellHeight;
      if(isHidden(svg))
        node.hidden = 'true';
      node.old = n.old;
      node.oldName = n.oldName;
      node.oldX = n.oldX;
      node.oldY = n.oldY;
      node.oldHidden = n.oldHidden;
      nodes[nodes.length] = node;
    }
    else{
      var connector = new Object();
      connector.deleted = true;
      connector.from = n.from.svg.id;
      connector.to = n.to.svg.id;
      connector.connectorType = svg.getAttribute('connectorType');
      connector.name = getConnectorNameSvg(svg);
      connector.old = n.old;
      connector.oldName = n.oldName;
      connector.oldConnectorType = n.oldConnectorType;
      connectors[connectors.length] = connector;
    }
  }
  var data = new Object();
  data.nodes = nodes;
  data.connectors = connectors;
  return data;
}

function networkDimensions(node, dimensions, level)
{
  node.handleNumber = nextHandleNumber;
  ++level;
  var f = node.froms;
  if(f){
    var l = f.length;
    for(var i = 0; i < l; ++i){
      var connector = f[i];
      var to = connector.to;
      if(to.handleNumber == nextHandleNumber || isHidden(to.svg))
        continue;
      if(dimensions.length == level)
        dimensions[level] = 1;
      else
        ++dimensions[level];
      networkDimensions(to, dimensions, level);
    }
  }
  if(level < 2)
    return;
  level -= 2;
  var t = node.tos;
  if(t){
    var l = t.length;
    for(var i = 0; i < l; ++i){
      var connector = t[i];
      var from = connector.from;
      if(from.handleNumber == nextHandleNumber || isHidden(from.svg))
        continue;
      ++dimensions[level];
      networkDimensions(from, dimensions, level);
    }
  }
}

function layoutNodes(node, dimensions, level, indexes, max, top, width, height)
{
  if(node.handleNumber == nextHandleNumber || isHidden(node.svg))
    return;
  node.handleNumber = nextHandleNumber;
  var svg = node.svg;
  var index = indexes[level]++;
  var y = top;
  var d = dimensions[level] + 1;
  y += Math.round(height * ( max / d * index - 1 ));
  svg.setAttribute('x', level * width * gridCellWidth);
  svg.setAttribute('y', y * gridCellHeight);
  ++level;
  var f = node.froms;
  if(f){
    var l = f.length;
    for(var i = 0; i < l; ++i){
      var connector = f[i];
      var to = connector.to;
      layoutNodes(to, dimensions, level, indexes, max, top, width, height);
    }
  }
  if(level < 2)
    return;
  level -= 2;
  var t = node.tos;
  if(t){
    var l = t.length;
    for(var i = 0; i < l; ++i){
      var connector = t[i];
      var from = connector.from;
      layoutNodes(from, dimensions, level, indexes, max, top, width, height);
    }
  }
}

function layoutNetwork(node, top, width, height)
{
  var dimensions = [1];
  ++nextHandleNumber;
  networkDimensions(node, dimensions, 0);
  var max = 0;
  var l = dimensions.length;
  var indexes = [];
  for(var i = 0; i < l; ++i){
    var t = dimensions[i];
    if(max < t)
      max = t;
    indexes[i] = 1;
  }
  ++nextHandleNumber;
  layoutNodes(node, dimensions, 0, indexes, max + 1, top, width, height);
  return top + max * height;
}

function autoLayout()
{
  var maxWidth = 0;
  var maxHeight = 0;
  var minTo = -1;
  var maxTo = 0;
  var root = document.getElementById('flowchart');
  var nodes = [];
  var connectors = [];
  var previous = nextHandleNumber;
  var children = root.childNodes;
  var l = children.length;
  for(var i = 0; i < l; ++i){
    var svg = children.item(i);
    var id = svg.id;
    if(id.length == 0){
      connectors[connectors.length] = nodeMap[svg.connectorId];
      continue;
    }
    var node = nodeMap[id];
    if(isHidden(svg)){
      node.nextHandleNumber = previous + 1;
      continue;
    }
    nodes[nodes.length] = node;
    var r = svg.getBBox();
    var w = r.width;
    if(w > maxWidth)
      maxWidth = w;
    var h = r.height;
    if(h > maxHeight)
      maxHeight = h;
    var t = getConnectorCount(node.tos);
    if(t > maxTo)
      maxTo = t;
    if(t < minTo || minTo < 0)
      minTo = t;
    node.currentToCount = t;
  }
  l = nodes.length;
  var width = Math.round((maxWidth + autoLayoutOffsetWidth + gridCellWidth - 1) / gridCellWidth);
  var height = Math.round((maxHeight + autoLayoutOffsetHeight + gridCellHeight - 1) / gridCellHeight);
  var top = 0;
  var count = 1;
  while(count > 0){
    count = 0;
    var current = minTo;
    var minTo = maxTo;
    for(var i = 0; i < l; ++i){
      node = nodes[i];
      if(node.handleNumber > previous)
        continue;
      ++count;
      var t = node.currentToCount;
      if(t == current)
        top = layoutNetwork(node, top, width, height);
      else if(t < minTo)
        minTo = t;
    }
  }
  l = connectors.length;
  for(var i = 0; i < l; ++i)
    moveConnector(connectors[i]);
}

function appendSvgInfo(url, svg)
{
  if(svg){
    var s = url.indexOf('?') > 0 ? '&' : '?';
    id = svg.id;
    if(id.length > 0)
      url += s + 'FLOWCHART_POPUP_TYPE=Node&FLOWCHART_POPUP_ID=' + id;
    else
      url += s + 'FLOWCHART_POPUP_TYPE=Connector&FLOWCHART_POPUP_ID=' + svg.connectorId;
  }
  return url;
}

function openURL(url, svg)
{
  parent.navigateTo(appendSvgInfo(url, svg), 'appFrame');
}

function openPopup(popup, svg)
{
  parent.popUpWindow(appendSvgInfo(popup, svg));
}

function submitFlowChartAction(action, targetId, svg)
{
  var type = '';
  var id = '';
  if(svg){
    id = svg.id;
    if(id.length > 0)
      type = 'Node';
    else{
      type = 'Connector';
      id = svg.connectorId;
    }
  }
  parent['submitFlowChart_' + chartId](action, targetId, type, id);
}
