/* pgl controls js */

function isV1( )
{
  if( window._pglV2 )
    return false;
  return true;
}
function isV2( )
{
  if( window._pglV2 )
    return true;
  return false;
}

function ui_goTo( url )
{
  document.location.href = url;
}

function ui_submit( userAction )
{
  document.form.action = "view.x2ps";
  document.form.BUTTON_ID.value = userAction;
  document.form.submit();
  return;
}

function ui_url_submit( urlString )
{
  document.form.action = urlString;
  document.form.submit();
  return;
}

function ui_submit_param( userAction,param )
{
  document.form.action = "view.x2ps?" + param;
  document.form.BUTTON_ID.value = userAction;
  document.form.submit();
  return;
}

// collapse rows for two tables
function toggleCollapsableRows(tableId, cellId, startRow, numRows)
{
  var shouldContinue = toggleCollapsableRowsSingleTable(tableId + "_syncslave_data", startRow, numRows, cellId );
  
  if( !shouldContinue )
  {
    return;
  }
  
  shouldContinue = toggleCollapsableRowsSingleTable(tableId + "_syncmaster_data", startRow, numRows );
  
  if( !shouldContinue )
  {
    return;
  }
  
  var imgId = tableId + "." + startRow + ".TOGGLE_IMG";  
  var img = document.getElementById( imgId );
  
  if( img != null )
  {
    var src = img.src;
    var lastSlash = src.lastIndexOf( "/" );
    var path = src.substring( 0, lastSlash );
    var imgName = src.substring( lastSlash+1 );
    
    if( imgName.indexOf( "minus" ) != -1 )
    {
      img.src = path + "/plus_norgie.gif";
    }
    else
    {
      img.src = path + "/minus_norgie.gif";
    }
    
  }
  else  
  {
    //alert( "toggle img is null" );
  }
  
  
  onResize();
}

// MAB: collapse a single table
function toggleCollapsableRowsSingleTable(tableId, startRow, numRows, cellId)
{
  var targetTable = document.getElementById( tableId );
  
  if( targetTable == null )
  {
    //alert( "toggleRows targetTable '" + tableId + "' is null" );
    return false;
  }
 
  startRow = startRow+1; // leave this row
  var endRow = startRow+numRows;
  var toggledAny = false;
  var didCollapse = false;

  for( i=startRow; i < endRow; i++ )
  {
    if( i >= targetTable.rows.length )
    {
      break;
    }

    var row = targetTable.rows[i];
    
    if( row.collapseLock != null && row.collapseLock != startRow )
    {
      // for nested collapsable field groups, don't touch rows already collapsed by someone else
      //alert( "lock held by " + row.collapseLock +": exiting" );
      continue;
    }
    
    //alert( "row.collapseLock = " + row.collapseLock );
            
    if (row.style.display == "none") // currently collapsed, do expand
    {
      row.style.display = "";
      row.style.visibility = "visible";      
      row.collapseLock = null; // clear lock      
      didCollapse = false;
    }
    else // currently expanded, do collapse
    {
      row.style.display = "none";
      row.collapseLock = startRow; // set lock      
      didCollapse = true;
    }
    
    toggledAny = true;
  }
  
  if( toggledAny )
  {    
    for( i=0; i < targetTable.rows.length; i++ )
    {
      var row = targetTable.rows[i];
      var firstCell = row.cells[0];
      
      if( i==(targetTable.rows.length-1) && firstCell.innerHTML == '&nbsp;' && row.cells.length == 1 )
      {
        // mab: ie is adding an extra row is showing up -- it's not in the view->source!        
        targetTable.deleteRow(i);
        // note: this will get called every time because ie doesn't really delete the row!
      }
    }
    
    var collapsedLastRow = (targetTable.rows.length == endRow);
    
    if( collapsedLastRow && cellId != null )
    {
      var lastDot = cellId.lastIndexOf( "." );
      
      if( lastDot != -1 )
      {
        var parentCellId = cellId.substring( 0, lastDot );
        var adjSize = didCollapse ? 0 - numRows : numRows;
        fixRowSpanAfterToggleCollapse( parentCellId, adjSize );
      }
    }    
  }
  
  return toggledAny;
}

function fixRowSpanAfterToggleCollapse( cellId, adjSize ) // adjSize can be pos (expand) or neg (collapse)
{
  
  while( cellId != null && cellId.length > 0 )
  {
    var cell = document.getElementById( cellId + "_td" );
    
    if( cell != null )
    {
      cell.rowSpan += adjSize;        
    }

    var lastDot = cellId.lastIndexOf( "." );
    cellId = (lastDot != -1) ? cellId.substring( 0, lastDot ) : null;  
  }
}

// dummy method, will be overriden
function autoCollapseRows()
{
  executeJavascripts();
}

var jsFunctions = new Array();

function registerJavascript(js)
{
  jsFunctions[jsFunctions.length] = js;
}

function executeJavascripts()
{
  for(var i=0; i<jsFunctions.length; i++)
  {
    eval(jsFunctions[i]);
  }
}

function onTab( tabId, containerId )
{
  var alertPrefix = "onTab(): ";
  var form = document.forms['form' ];
  
  if( form != null )
  {
    var selectedStep = containerId + "_SELECTED_STEP";      
    var selectedStepId = containerId + "_STEP_ID";
    
    for( i=0; i < form.elements.length; i++ )
    {
      var input = form.elements[i];
      
      if( input.name == selectedStep )
      {
        input.value = tabId;           
      }
      else if( input.name == selectedStepId )
      {
        input.value = tabId;           
      }
      else if( input.name == 'BUTTON_ID' )
      {
        input.value = 'SYS_TAB';
      }
      else if( input.name == 'START_COUNT' )
      {
        input.value = 0;
      }
      else if( input.name == 'SYS_ID' )
      {      
        if( form.PAGE_SYS_ID != null )
        {
          input.value = form.PAGE_SYS_ID.value;
        }
      }
    }       
        
    onTabUpdate(containerId, 'SYS_TAB');
  }
  else
  {
    alertPrefix + alert( 'no form!' );
  }
}

function onTabUpdate(containerId, action)
{
  form.submit();
}

function onWizard( stepId, containerId )
{
  var alertPrefix = "onWizard(): ";
  var form = document.forms['form' ];
  
  if( form != null )
  {
    var selectedStep = containerId + "_SELECTED_STEP";      
    var selectedStepId = containerId + "_STEP_ID";
    
    for( i=0; i < form.elements.length; i++ )
    {
      var input = form.elements[i];
      
      if( input.name == selectedStep )
      {
        input.value = stepId;           
      }
      else if( input.name == selectedStepId )
      {
        input.value = stepId;           
      }
      else if( input.name == 'SAVE_PARAMS' )
      {
        input.value = 'true';
      }
      else if( input.name == 'BUTTON_ID' )
      {
        input.value = 'SYS_RELOAD';
      }
      else if( input.name == 'START_COUNT' )
      {
        input.value = 0;
      }
      else if( input.name == 'SYS_ID' )
      {      
        if( form.PAGE_SYS_ID != null )
        {
          input.value = form.PAGE_SYS_ID.value;
        }
      }
    }       
        
    onTabUpdate(containerId, 'SYS_RELOAD');
  }
  else
  {
    alertPrefix + alert( 'no form!' );
  }
}

function onFullResize()
{
  _alltables = null;
  onResize();
}

function hideRenderingMessage()
{
  var all = document.all;
  var bb = all.busy_box;
  if(bb)
    bb.style.display = 'none';
  var bbm = all.busy_box_msg;
  if(bbm)
    bbm.style.display = '';
  var bbr = all.busy_box_render;
  if(bbr)
    bbr.style.display = 'none';
  var ucb = all.update_cancel_button;
  if(ucb)
    ucb.style.display = 'none';
}

function displayProcessingRequestMessage( )
{
  var all = document.all;
  var bb = all.busy_box;
  if(bb){
    var s = bb.style;
    var b = document.body;
    s.top = b.scrollTop + b.clientHeight / 2 - bb.offsetHeight / 2;
    s.left = b.scrollLeft + b.clientWidth / 2 - bb.offsetWidth / 2;
    s.display = '';
  }
  var bbm = all.busy_box_msg;
  if(bbm)
    bbm.style.display = '';
}

function enablePage()
{
  var all = document.all;
  var pd = all.page_disabled;
  if(pd)
    pd.style.display = 'none';
}

function disablePage(cancellable)
{
  var all = document.all;
  var pd = all.page_disabled;
  if(pd)
    pd.style.display = '';
  if(cancellable){
    var ucb = all.update_cancel_button;
    if(ucb)
      ucb.style.display = '';
  }
}

var focusElem; // To set focus on entry field when focusOnLoad="true"

function setPageVisibility( visibility )
{
  var bt = document.all.body_table;
  if(bt)
    bt.style.visibility = visibility;
  if (focusElem != undefined)
  {
    focusElem.focus();
  }
}

function setFocusOnLoad(id)
{
  focusElem = document.getElementById( id );
}

//Caching the images for run-time switching
var ui_filter_disabled_image = new Image();
ui_filter_disabled_image.src = "i2/images/clearfield_disabled.gif";

var ui_filter_enabled_image = new Image();
ui_filter_enabled_image.src = "i2/images/clearfield.gif";

//Table Filter Functions
function ui_switch_image(filterObject,imgObject )
{
  if ( filterObject.length > 0 )
  {
    imgObject.src = ui_filter_enabled_image.src;
  }
  else
  {
    imgObject.src = ui_filter_disabled_image.src;
  }
}


function ui_clear( filterObject,imgObject )
{
  filterObject.value="";
  ui_switch_image(filterObject.value,imgObject );
  return;
}

function ui_switch_image_date_range(fromFilterObjectValue, toFilterObjectValue, imgObject )
{
  if ( ( fromFilterObjectValue.length > 0 ) || ( toFilterObjectValue.length > 0 ) )
  {
    imgObject.src = ui_filter_enabled_image.src;
  }
  else
  {
    imgObject.src = ui_filter_disabled_image.src;
  }
}


function ui_clear_date_range( fromFilterObject, toFilterObject, imgObject )
{
  fromFilterObject.value="";
  toFilterObject.value="";
  ui_switch_image_date_range( fromFilterObject.value, toFilterObject.value, imgObject );
  return;
}

function addInstructionArea(s)
{
  var script = document.all[s];
  var area = script.previousSibling;
  var p = area;
  while(p){
    if(p.vc == 'true'){
      p.area = area;
      return;
    }
    p = p.parentElement;
  }
}

function findInstructionArea(e)
{
  var area = e.area;
  if(area)
    return area;
  while(e && e.tagName != 'BODY'){
    if(e.vc == 'true'){
      var area = e.area;
      if(area){
        e.area = area;
        return area;
      }
    }
    e = e.parentElement;
  }
  return null;
}

function validateFromElement(e, fullContext)
{
  if(fullContext){
    while(e){
      if(e.vc == 'true')
        break;
      e = e.parentElement;
    }
  }
  var stopElement = null;
  var spans = e.getElementsByTagName('SPAN');
  var l = spans.length;
  var changed = false;
  var r = true;
  for(var i = 0; i < l; ++i){
    var oFieldValidator = spans[i];
    var t = oFieldValidator.type;
    if(t == null)
      continue;
    var was = oFieldValidator.isValid;
    if(was == null)
      was = true;
    var v = true;
    var oField = oFieldValidator.parentElement.firstChild;
    for(;;){
      var tag = oField.tagName;
      if(tag == null || tag == 'SPAN' || tag == 'SCRIPT')
        oField = oField.nextSibling;
      else
        break;
    }
    if(oField == stopElement){
      if(was)
        continue;
      oFieldValidator.isValid = true;
      hideValidator(oFieldValidator, t);
      continue;
    }
    savedThis = oField;
    
    switch (t)
    {
      case 'required':
          if(oField.required && !oField.disabled)
            v = oField.value.trim().length > 0 || !fullContext && was;
          break;
        
      case 'number':
          var re = new RegExp(/^[0-9 ,.-]*$/);
          var value = oField.value;
          if(value.length > 0)
            v = value.match(re);
          break;

      case 'date':
      case 'time':
      case 'datetime':
      case 'DC':
      case 'DT':
      case 'DS':
      case 'DM':
      case 'DL':
      case 'DF':
      case 'TS':
      case 'TM':
      case 'TL':
      case 'TF':
      case 'DSTS':
      case 'DSTM':
      case 'DSTL':
      case 'DSTF':
      case 'DMTS':
      case 'DMTM':
      case 'DMTL':
      case 'DMTF':
      case 'DLTS':
      case 'DLTM':
      case 'DLTL':
      case 'DLTF':
      case 'DFTS':
      case 'DFTM':
      case 'DFTL':
      case 'DFTF':
          var value = oField.value;
          if(value.length > 0)
            v = isDate(value, getDatePattern(oField));
          break;
         
      case 'min':
          var value = oField.value.trim();
          if(value.length > 0)
            v = toDouble(value) >= new Number(oField.minValue);
          break;
      
      case 'max':
          var value = oField.value.trim();
          if(value.length > 0)
            v = toDouble(value) <= new Number(oField.maxValue)
          break;
         
      case 'date_range':
          var to = oField.value.trim();
          if(to.length > 0)
          {
            var from = oField.parentElement.previousSibling.firstChild.value.trim();
            if(from.length > 0)
            {
              var format = oField.parentElement.fieldtype == 'Date' ? page_dateFormat : page_dateTimeFormat;
              from = new Date(getDateFromFormat(from, format, true)).getTime();
              to = new Date(getDateFromFormat(to, format, true)).getTime();
              v = from <= to;
            }
          }
          break;
    
      case 'custom':
          var value = oField.value;
          if(value.length > 0)
          {
            v = oFieldValidator.f;
            if(typeof v == 'string')
              v = eval(v);
            if(typeof v == 'function')
              v = v(oFieldValidator, oField);
          }
          break;

      case 'compare':
          var value = oField.value.trim();
          if(value.length > 0)
          {
            var f = oFieldValidator.withField;
            if(f.indexOf('(') > 0)
              f = eval(f);
            else
              f = oField.form[f].value;
            if(f)
              f = f.trim();
            if(f.length > 0)
            {
              var ft = oField.parentElement.fieldtype;
              if(ft == 'Number')
              {
                value = toDouble(value);
                f = toDouble(f);
              }
              else if(ft == 'Date')
              {
                value = new Date(getDateFromFormat(value, page_dateFormat, true)).getTime();
                f = new Date(getDateFromFormat(f, page_dateFormat, true)).getTime();
              }
              else if(ft == 'DateTime')
              {
                value = new Date(getDateFromFormat(value, page_dateTimeFormat, true)).getTime();
                f = new Date(getDateFromFormat(f, page_dateTimeFormat, true)).getTime();
              }
              var op = oFieldValidator.operator;
              if( op == 'GREATER')
                v = value > f;
              else if( op == 'GREATER_EQUAL')
                v = value >= f;
              else if (op == 'LESS')
                v = value < f;
              else if (op == 'LESS_EQUAL')
                v = value <= f;
              else if (op == 'EQUAL')
                v = value == f;
            }
          }
          break;
          
      default:
          break;
    }
    
    
    
    
    stopElement = null;
    if(v){
      if(was)
        continue;
      changed = true;
      oFieldValidator.isValid = true;
      hideValidator(oFieldValidator, t);
    }
    else{
      if(oFieldValidator.s == 'STOP')
        stopElement = oField;
      if(was){
        changed = true;
        oFieldValidator.isValid = false;
        if(oFieldValidator.children.length == 0)
          createValidation(oFieldValidator, t);
        showValidator(oFieldValidator, t);
      }
      r = false;
    }
  }
  if(changed)
    onResize();
  savedThis = null;
  return r;
}

function createValidation(oFieldValidator, id)
{
  if(oFieldValidator.msg == null){
    id = '_vt_' + id;
    var t = document.getElementById(id);
    if(t)
      oFieldValidator.msg = t.msg;
    else{
      var xml = getUpdateHTML_XML(id, 'PAGE_ACTION', null, false);
      var msg = xml.firstChild.getAttribute('Value');
      t = document.getElementById('_vt_');
      var span = document.createElement('<span id="' + id + '" msg="' + msg + '" style="display:none;"></span>');
      t.appendChild(span);
      oFieldValidator.msg = msg;
    }
  }
  oFieldValidator.innerHTML = '&nbsp;<img src="' + omxContextPath + '/i2/images/alert_static_small.gif" border="0" align="middle"/>';
}

function showValidator(oFieldValidator, type)
{
  oFieldValidator.style.display = '';
  var msg = oFieldValidator.msg;
  oFieldValidator.lastChild.alt = msg;
  var instructionArea = findInstructionArea(oFieldValidator);
  if(instructionArea == null)
    return;
  var counts = instructionArea.counts;
  if(counts == null){
    counts = instructionArea.counts = new Object();
    var rows = instructionArea.rows;
    var l = rows.length;
    while(--l >= 0){
      var tr = rows[l];
      var t = tr.instructionType;
      if(t)
        counts[t] = [ 0, l ];
    }
    instructionArea.totalCount = 0;
  }
  if(type == 'required')
    msg = 'required_field_missing';
  ++instructionArea.totalCount;
  var c = counts[msg];
  if(c){
    if(++c[0] == 1)
      instructionArea.rows[c[1]].style.display = '';
    return;
  }
  var rows = instructionArea.rows;
  counts[msg] = [ 1, rows.length ];
  var tr = instructionArea.insertRow();
  tr.instructionType = msg;
  var td = tr.insertCell();
  td.align = 'center';
  td.innerHTML = '<img src="' + omxContextPath + '/i2/images/alert_static_small.gif" alt="Error" border="0" align="middle"/>';
  td = tr.insertCell();
  td.width = '100%';
  td.innerHTML = msg
}

function hideValidator(oFieldValidator, type)
{
  oFieldValidator.style.display = 'none';
  var instructionArea = findInstructionArea(oFieldValidator);
  if(instructionArea == null)
    return;
  var counts = instructionArea.counts;
  if(counts == null)
    return;
  --instructionArea.totalCount;
  var msg = type == 'required' ? 'required_field_missing' : oFieldValidator.lastChild.alt;
  var c = counts[msg];
  if(c && --c[0] == 0)
    instructionArea.rows[c[1]].style.display = 'none';
}

function validation_setFocusOnInstructionArea()
{
  var obj = document.getElementById('instruction_area');
  if (obj != null)
    obj.focus();
  else
  {
    var tables = document.getElementsByTagName();
    var l = tables.length;
    for(var i = 0; i < l; ++i){
      var t = tables[i];
      if(t.className == 'instructionsArea'){
        if(validation_instructionAreaHasErrorMessages(validation_areas[i][0]))
        {
           t.focus();
           return;
        }
      }
    }
  }
}

function getInstructionArea(formName)
{
  return document.getElementById('instruction_area_'+formName);
}

function validation_alert(id)
{
}

function isValid_area(id,pObj)
{
  var obj = pObj;  if (id != null) obj = document.getElementById(id);
  if(obj == null)
    return true;
  var l = obj.length;
  if(typeof l == 'number'){
    var v = true;
    for(var i = 0; i < l; ++i)
      if(!validateFromElement(obj[i], true))
        v = false;
    if(v)
      return true;
  }
  else if(validateFromElement(obj, true))
    return true;
  validation_setFocusOnInstructionArea();
  return false;
}

function isValid_page()
{
  return validateFromElement(document.body, true);
}

function isValid_form(oForm)
{
  var v = validateFromElement(oForm, true);
  if(v)
    return true;
  validation_setFocusOnInstructionArea();
  return false;
}

function isValid_field(id,obj)
{
  if(id)
    obj = document.getElementsByName(id);
  var l = obj.length;
  if(typeof l == 'number'){
    for(var i = 0; i < l; ++i)
      if(!validateFromElement(obj[i], false))
        return false;
    return true;
  }
  return validateFromElement(obj, false);
}

function isValid_field_if_data(oField)
{
  return true;
}

function validator_clear(oFieldValidator)
{
  var v = oFieldValidator.isValid;
  if(v == true || v == null)
    return;
  oFieldValidator.isValid = true;
  hideValidator(oFieldValidator, oFieldValidator.type);
}

function validator_show_alert(oFieldValidator, oField)
{
}

function validator_set_message(oFieldValidator, oField)
{
}

function validator_notify_instruction_area(oFieldValidator, oField)
{
}

function isValid_required(elem)
{
}
function isEntered(elem)
{
}
function isNotEntered(elem)
{
}

function validation_turnOff(oForm)
{
}

function toggleItemVisibility(item,state)
{
  if (item != null)
  {
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

function validation_instruction_area_show_alert(formName)
{
}

function validation_clearMessages(id)
{
  ia = getInstructionArea(id);
  if (ia == null) return;

  var rows = ia.rows;
  var childLen = rows.length;
  for(var i = 0; i < childLen; ++i)
  {
    oMessage = rows[i];
    if(oMessage.added == 'true')
      toggleItemVisibility(oMessage, 'hide');
  }
}

function validation_addMessage(msg, id, severity, server)
{
  var imgName = "/i2/images/alert_static_small.gif";
  severity = severity.toUpperCase();
  if(severity == "WARNING")
  {
    imgName = "/i2/images/alert_yellow_static.gif";
  }
  else if(severity == "SEVERE_ERROR")
  {
    imgName = "/i2/images/alert_ani.gif";
  }
  else if(severity == "INFO" || severity == 'Info')
  {
    imgName = "/i2/images/information_sml.gif";
  }

  ia = getInstructionArea(id);
  if (ia == null) return;
  if (validation_isMessageShown(msg,id) == true)
  {
    return;
  }
  oTR = ia.insertRow();
  oTR.instructionType='custom_error_message';
  oTR.added = server ? 'false': 'true';
  oTD = oTR.insertCell();
  oTD.align = 'center';
  oTD.vAlign = 'top';
  var htm = "<img src=" + omxContextPath + imgName + " alt='Error' border='0' align='middle'>";

  oTD.innerHTML =   htm;

  oTD = oTR.insertCell();
  oTD.width = "100%"
  oTD.innerHTML = msg
}

function validation_instructionAreaHasErrorMessages(id)
{
  var ia = getInstructionArea(id);
  if (ia == null) return;
  if(ia.totalCount > 0)
    return true;
  var rows = ia.rows;
  var childLen = rows.length;
  for(var k = 0; k < childLen; k++)
  {
    oMessageRow =  rows[k];
    if (oMessageRow.added == 'true' && oMessageRow.style.display != 'none')
      return true;
  }
  return false;
}

function validation_isMessageShown(msg, id)
{
  var ia = getInstructionArea(id);
  if (ia == null) return false;
  var rows = ia.rows;
  var childLen = rows.length;
  for(var k = 0; k < childLen; k++)
  {
    oMessageRow = rows[k];
    if (oMessageRow.cells[1] == null) continue;
    // WJD 10.30.2007  eQ #612973 - Removed oMessageRow.added
    if (oMessageRow.cells[1].innerHTML == msg)
    {
      toggleItemVisibility(oMessageRow, 'show');
      return true;
    }
  }
  return false;
}

function validation_removeMessage(msg, id)
{
  ia = getInstructionArea(id);
  if (ia == null) return false;

  var childLen = ia.rows.length;
  if ( childLen >= 1)
  {
    var k = 0;
    for(k = 0; k < childLen; k++)
    {
      oMessageRow =  ia.rows[k];
      if (oMessageRow.cells[1].innerHTML == msg)
      {
        toggleItemVisibility(oMessageRow, 'hide');
        return true;
      }
    }
  }
  return false;
}


function validation_noWildCards(validator, field)
{
  if(validation_areWildCardsEntered(field) == false )
    return true;
  else
  {
    validator.message="Wild Cards are not Allowed.";
    return false;
  }
}

function validation_areWildCardsEntered(oField)
{
  str = (String) (oField.value);
  if (str == null) return false;
  var m = str.match(/[\*,%]+/g); // regex for wildcards
  if (m)
  {
    return true;
  }
  return false;
}

function validation_AreRowsWithSameFieldSelected(form , checkBoxName, fieldName, operator, severity, message)
{
}

function validation_disableCheckBoxIfFieldHasValue(form , checkBoxName, fieldName , fieldValue)
{
  if (form == null) return true;
  var elemLen = form.elements.length;

  for( i = 0; i < elemLen; i++ )
  {
    // process each row
    if(form.elements[i].name == checkBoxName)
    {
      var rowDisabled=false;
      // locate the  constraint field
      for( j = i+1; j < elemLen; j++ )
      {
        if (form.elements[j].name == fieldName )
        {
          if (fieldValue == form.elements[j].value)
          {
            // disabled
            rowDisabled=true;
            form.elements[i].disabled = true;
           }
           break; // break and go to the next row
         }
       }
       // START Disable all fields in the row
       if (rowDisabled == true)
       {
        for( j1 = i+1; j1 < j; j1++ )
        {
          if (form.elements[j1].name == fieldName ) break;
          // disable
          form.elements[j1].disabled = true;
         }
       }
       // END
     }
   }
   return true;
}

function validation_onlyValidData()
{
  var oField = event.srcElement;
  var oFieldName = oField.name;
  var oFieldLength = parseInt(oFieldName.length) -1;
  var oFieldLastChar = oFieldName.substr(parseInt(oFieldLength),1);
  // Now allowing Negative
  if ( oField.parentElement.fieldtype == 'Number' )
    validCharacters = /[-0123456789., ]/;
  else if (oField.parentElement.fieldtype == 'Currency')
    validCharacters  = /[0123456789.,]/;
  else return;
     validation_onlyValidCharacters(validCharacters);
}

function validation_onlyValidCharacters(validCharacters)
{
  var bChanged = false;

  var oField = event.srcElement;
  var sField = oField.value;
  var nStringLen = sField.length;
  var sValidField = "";

  for(var x = 0; x < nStringLen; x++)
  {
    var cChar = sField.charAt(x);
    if(cChar.search(validCharacters) != -1)
    {
      sValidField += cChar;
    }
    else
    {
      bChanged = true;
    }
  }

  if(bChanged)
  {
    oField.value = sValidField;
  }
}

function validation_clearFieldErrors(obj)
{
}

function validation_clearFieldErrors_worker(oField)
{
}

function validation_tableHasRows(id)
{
    var obj = document.getElementById(id+'_data');
    if (obj == null) return false;
    return obj.rows.length > 0;
}

function validaton_getInvalidDateMsg()
{
    return "<i18n:text>Invalid Date or Date Format</i18n:text>";
}

function validaton_getInvalidDateTimeMsg()
{
    return "<i18n:text>Invalid Date Time or Date Time Format</i18n:text>";
}


    // Key Tapping - Start::
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
            event.returnValue = onKeyPress(window.event.srcElement)
            event.returnValue=false;
          }
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
        onKeyPress( e.target )
        return false;
      }
    }

    function onKeyPress( target )
    {
      if (target.disablePageKeyTapping == 'true') return;
      if (target.nodeName == 'INPUT' && target.name == 'pagenum')
      {
        getRecords('jump');
        return false;
      }
      return true;
    }
    // Key Tapping - End::

    // Page.xsl js - Start::
    function initFrameToggleGif(path)
    {
      if(window == null ) return;
      if(window.frameElement == null ) return;
      if(window.frameElement.parentNode == null ) return;

      var frameCol = window.frameElement.parentNode.cols;

      if ( frameCol.charAt(0) == "0")
      {
        var h;
        if (document.all && (h = document.header))
        {
        h.toggle.alt = "Show Navigation Frame";
        h.toggle.src = omxContextPath + "/i2/images/expand.gif";
        }
      }
      else
      {
        var h;
        if (document.all && (h = document.header))
        {
          h.toggle.alt = "Hide Navigation Frame";
          h.toggle.src = omxContextPath + "/i2/images/collapse.gif";
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
          document.header.toggle.alt = "Hide Navigation Frame";
          document.header.toggle.src = omxContextPath + "/i2/images/collapse.gif";
          window.frameElement.parentNode.cols="170,*";
          tabShow = 0;
          return;
        }
      }
      else
      {
        var h;
        if (document.all && (h = document.header))
        {
          h.toggle.alt = "Show Navigation Frame";
          h.toggle.src = omxContextPath + "/i2/images/expand.gif";
          window.frameElement.parentNode.cols="0%,100%";
          tabShow = 1;
        }
      }
    }

    // Form Submit
     function submitForm(formName, action, target)
     {
       var formObj = eval('document.' + formName);
       formObj.action = action;
       if (target != null)
       {
         formObj.target = target;
       }
       formObj.submit();
     }

     // Pagination
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
          search_form.submit();
        }
        else
        {
          core_alert("Please enter a valid page number.");
          page_form[startCountElement].focus();
          page_form[startCountElement].select();
        }
      }
    }

    function disableButtons()
    {
      var buttons = document.all.tags('TABLE');
      for (var i=0; i < buttons.length; i++)
      {
          if (buttons[i].className == "buttonBorder")
          {
              if (buttons[i].id != "")
              {
                  disableButton(buttons[i].id);
              }
          }
       }
       return;
    }
    function disableButton(id)
    {
      i2uiToggleButtonState( id, 'disabled');
      return;
    }
    function enableButton(id)
    {
      i2uiToggleButtonState( id, 'enabled');
      return;
    }
    function collapseNavArea()
    {
      if( top.i2ui_shell_content == null ) // MAB: on notes popup was getting null for this.
      {
          scrollHelper();
          return false;      
      }
      
      var frameCol = window.frameElement.parentNode.cols;
      if ( frameCol.charAt(0) == "1")
      {
          togglenav('');
          return true;
      }
      else
      {
          scrollHelper();
          return false;
      }
    }

    var resizerequest = null;
    function page_onResize()
    {
      if (resizerequest != null)
          clearTimeout(resizerequest);
      setTimeout("onResize()",100);
    }
    function page_submitAsXML(val)
    {
      if (val == null) val = 'true';
      document.form.SUBMIT_AS_XML.value = val;
    }
    // Page.xsl js - End::

    function addField (form, fieldType, fieldName, fieldValue) {
      if (document.getElementById) {
        var input = document.createElement('INPUT');
          if (document.all) { // what follows should work 
                              // with NN6 but doesn't in M14
            input.type = fieldType;
            input.name = fieldName;
            input.value = fieldValue;
          }
          else if (document.getElementById) { // so here is the
                                              // NN6 workaround
            input.setAttribute('type', fieldType);
            input.setAttribute('name', fieldName);
            input.setAttribute('value', fieldValue);
          }
          
        form.appendChild(input);        
      }
    }
    
    function setHiddenField( form, fieldName, fieldValue )
    {      
      var field = eval( 'form.' + fieldName );

      if( field != null )
      {
        field.value = fieldValue;
      }
      else
      {
        addField( form, 'hidden', fieldName, fieldValue );
      }
    }

    // Search.xsl js - Start::
    // Searching
      function onSearch(search_name, targetId)
      {
        if ( validate_search(search_name) == true )
            {
                var buttonId = search_name+'_button';
                setHiddenField( document.form, 'START_COUNT', '0' );
                try
                {
                  document.form.SORT_ORDER.disabled=true;
                  document.form.SORT_BY.disabled=true;
                  document.form.SORT_ORDER2.disabled=true;
                  document.form.SORT_BY2.disabled=true;
                  document.form.SORT_ORDER3.disabled=true;
                  document.form.SORT_BY3.disabled=true;
                } catch(e){}
                document.form.DO_SEARCH.value='Yes';
                if(studioPage){
                  update(targetId, buttonId);
                  try{
                    i2uiToggleButtonState(buttonId, 'enabled', 'Emphasized');
                  }
                  catch(e)
                  {
                  }
                }
                else{
                  disablePage();
                  displayBusyBox();
                  document.form.BUTTON_ID.value = buttonId;
                  document.form.submit();
                }
            }
      }
      
      function onExportSearch(search_name)
      {
        var prevAction = document.form.action;
        
        if ( validate_search(search_name) == true )
        {
          setHiddenField( document.form, 'START_COUNT', '0' );
          setHiddenField( document.form, 'NO_OF_ROWS', page_maxRowsExportable );
          setHiddenField( document.form, 'MAX_ROWS', page_maxRowsExportable );

          document.form.DO_SEARCH.value = 'Yes';          
          document.form.action = omxContextPath + '/CSVServlet';
          document.form.BUTTON_ID.value = search_name+"_button";
          document.form.submit();
        }

        //reset form action and values
        document.form.action = prevAction;
        document.form.reset();
      }      


      function onSearchSubmitXML()
      {
        if ( validate_search(search_name) == true )
        {
          disableButton(search_name+'_button');
          document.form.SUBMIT_AS_XML.value='true';
          setHiddenField( document.form, 'START_COUNT', '0' );
          document.form.DO_SEARCH.value='Yes';
          try
          {
            document.form.SORT_ORDER.disabled=true;
            document.form.SORT_BY.disabled=true;
          } catch(e){}
          document.form.BUTTON_ID.value=search_name+"_button";
          document.form.submit();
        }
      }

      function onChangeSearchResultsBy(action, asXML, search_name)
      {
        var validate = false;
        if (document.form.DO_SEARCH.value == 'Yes')
        validate = true;
        var valid = true;
        if (validate) valid = validate_search(search_name);
        if (valid)
        {
          disableButton(search_name+'_button');
          document.form.action = action;
          setHiddenField( document.form, 'START_COUNT', '0' );
           document.form.SUBMIT_AS_XML.value=asXML;
           try
           {
             document.form.SORT_ORDER.disabled=true;
             document.form.SORT_BY.disabled=true;
           } catch(e){}
          document.form.submit();
        }
      }

      // Saving Search
      function onSaveSearch( search_name, newsearch )
      {
        if ( validate_search(search_name) == true )
            {
                var x = newsearch ? '?NEW_SEARCH=true' : '';                
                disableButton('save_'+search_name+'_button');
                document.form.action=omxContextPath + '/core/search/controller/saveInSession.x2c' + x;
                document.form.BUTTON_ID.value="save_"+search_name+"_button";
                document.form.submit();
            }

      }
    // Advanced Search
    function onAdvancedSearch()
    {
      disableButton('more_options_button');
      document.form.VISIBILITY.value='Advanced';
      document.form.submit();
    }

    // Simple Search
    function onSimpleSearch()
    {
    disableButton('fewer_options_button');
        document.form.VISIBILITY.value='Simple';
        document.form.submit();
    }

    function validate_search(search_name)
    {
      if(typeof( window['isValid_' + search_name] ) != 'function')
      {
        search_name = "search";
      }
      
      var bIsFormValid = eval('isValid_'+search_name+'()');
      return   bIsFormValid;
    }
    // Search.xsl js - End::

var _httpCallbacks = new Array();

function HTTP( )
{
}

HTTP.prototype.init = function( )
{
  if (document.all)
  {
    var prefixes = [ 'MSXML2.XMLHTTP.3.0','MSXML2.XmlHttp','Microsoft.XmlHttp','MSXML.XmlHttp','MSXML3.XmlHttp' ];
    var l = prefixes.length;
    for( var i=0; i < l; ++i) 
    {
      try 
      {
        return this.object = new ActiveXObject( prefixes[ i ] );
      }
      catch (e){};
    }
    alert( 'Sorry, your browser can not handle this request' );
  }
  else
  {
    try 
    {
      return this.object = new XMLHttpRequest( );
    } 
    catch (e) 
    {
      alert( 'Sorry, your Netscape browser can not handle this request' );
    }
  }
}

HTTP.prototype.onreadystatechange = function()
{
  var l = _httpCallbacks.length;
  while(--l >= 0){
    var http = _httpCallbacks[l];
    var readyState = http.object.readyState;
    if(readyState != http.readyState){
      http.readyState = readyState;
      if(readyState == 4){
        _httpCallbacks[l] = null;
        http.callback(http);
      }
      return;
    }
  }
}

HTTP.prototype.getPost = function( gp, url, callback, results, headername, headervalue, postdata )
{
  var object = this.object;
  if( object == null )
    object = this.init( );
  window.http = this;
  try
  {
    this.callback = callback;
    if( callback ){
      var l = _httpCallbacks.length;
      var i = 0;
      while(i < 0){
        if(_httpCallbacks[i] == null)
          break;
      }
      _httpCallbacks[i] = this;
      object.open( gp, url, true );
      object.onreadystatechange = this.onreadystatechange;
    }
    else
      object.open( gp, url, false );
    object.setRequestHeader( 'accept-encoding', 'gzip, deflate' );
    if( headername != null && headervalue != null )
      object.setRequestHeader( headername, headervalue );
    object.setRequestHeader( 'pragma', 'no-cache' );
    object.setRequestHeader( 'cache-control', 'no-store, must-revalidate, private' );
    object.setRequestHeader( 'If-Modified-Since', 'Tue, 11 Jul 2000 17:23:51 GMT' );
    var xml = results == 'xml';
    if(xml && headername != 'Content-type')
      object.setRequestHeader( 'Content-type', 'text/xml' );
    if( gp == 'POST' ){
      if(headername != 'Content-type')
        object.setRequestHeader( 'Content-type', 'application/x-www-form-urlencoded' );
      object.send( postdata );
    }
    else
      object.send( null );
    this.readyState = object.readyState;
    if( callback )
      return;
    if( xml )
      return this.getXML( );
    return this.getText( );
  }
  catch( e ){
    alert( 'Error in get is ' + e.number + ' ' + e.description + ' ' + e.message );
  }
}

HTTP.prototype.get = function( url, callback, results, headername, headervalue )
{
  return this.getPost( 'GET', url, callback, results, headername, headervalue, null );
}

HTTP.prototype.post = function( url, callback, results, postdata, headername, headervalue )
{
  return this.getPost( 'POST', url, callback, results, headername, headervalue, postdata );
}

HTTP.prototype.getText = function( )
{
  return this.object.responseText;
}

HTTP.prototype.getXML = function( )
{
  return this.object.responseXML;
}

HTTP.prototype.getHeaders = function( )
{
  return this.object.getAllResponseHeaders( );
}

HTTP.prototype.request = function( request, service, callback, params, results )
{
  var s = omxContextPath + '/start.x2ps?REQUEST=' + request;
  if( service )
    s += '&SERVICE_NAME=' + service;
  if( params )
    s += '&' + params;
  return this.get( s, callback, results );
}

HTTP.prototype.submit = function( button, params )
{
  var x = 'BUTTON_ID=' + button + '&SYS_ID=' + document.form.SYS_ID.value + '&PAGE=' + document.form.PAGE.value;
  if( params )
    x += '&' + params;
  return this.post( omxContextPath + '/view.x2ps', null, 'text', x );
}

var _xmlCreator;

function getXMLCreator( )
{
  if( _xmlCreator == null )
    _xmlCreator = new ActiveXObject( 'Microsoft.XMLDOM' );
  return _xmlCreator
}

var currentScriptPos = -1;

function getCurrentScriptPos()
{
  return currentScriptPos < 0 ? document.all.length - 1: currentScriptPos;
}

var _scrollBarSize = 0;

function getScrollBarSize( s )
{
  if( _scrollBarSize > 0 )
    return _scrollBarSize;
  _scrollBarSize = s.offsetHeight - s.clientHeight;
  if( _scrollBarSize > 0 )
    return _scrollBarSize;
  _scrollBarSize= s.offsetWidth - s.clientWidth;
  if( _scrollBarSize > 0 )
    return _scrollBarSize;
  return 16;
}

function alignRowHeights( dataitem, headeritem )
{
  var dataItemRows = dataitem.rows;
  var headerItemRows = headeritem.rows;
  var len = headerItemRows.length;
  var hha = new Array( );
  var dha = new Array( );
  for( var i = 0; i < len; ++i ){
    var hh = headerItemRows[ i ].clientHeight;
    var hd = dataItemRows[ i ].clientHeight;
    var h = Math.max( hh, hd );
    if( h != hh )
      hha[ i ] = h;
    if( h != hd )
      dha[ i ] = h;
  }
  for( i = 0; i < len; ++i ){
    var h = hha[ i ];
    if( h > 0 )
      headerItemRows[ i ].height = h;
    h = dha[ i ];
    if( h > 0 )
      dataItemRows[ i ].height = h;
  }
}

function minimizeColumnWidths( dataitem, headeritem, sboffset )
{
  var dataItemRow0Cells = dataitem.rows[ 0 ].cells;
  var headerItemRows = headeritem.rows;
  var lastheaderrowNbr = headerItemRows.length - 1;
  var lastheaderrow = headerItemRows[ lastheaderrowNbr ];
  var lastHeaderRowCells = lastheaderrow.cells;
  var len = lastHeaderRowCells.length;
  var hws = new Array( );
  var dws = new Array( );
  var resizeRow = lastheaderrow.resizeRow == 'true';
  if( resizeRow ){
    lastheaderrow.style.display = '';
    var c = lastheaderrowNbr;
    while( --c >= 0 ){
      var cells = headerItemRows[ c ].cells;
      var l = cells.length;
      while( --l >= 0 ){
        var t = cells[ l ];
        t.style.width = '';
        if( t.wrap )
          t.removeAttribute( 'noWrap' );
      }
    }
  }
  dataitem.style.width = 5 * len;
  headeritem.style.width = 5 * len;
  for( i = 0; i < len; ++i )
  {
    var hc = lastHeaderRowCells[ i ];
    hc.width = '';
    var dc = dataItemRow0Cells[ i ];
    dc.width = '';
    if( hc.wrap ){
      hc.removeAttribute( 'noWrap' );
      dc.removeAttribute( 'noWrap' );
    }
  }
  for( i = 0; i < len; ++i )
  {
    var hc = lastHeaderRowCells[ i ];
    w1 = hc.offsetWidth;
    w2 = dataItemRow0Cells[ i ].offsetWidth;
    w3 = Math.max( w1, w2 );

    if( hc.wrap ){
      hws[ i ] = w3;
      dws[ i ] = w3;
    }
    else{
      if( w1 != w3 )
        hws[ i ] = w3;
      if( w2 != w3 )
        dws[ i ] = w3;
    }
  }
  for( i = 0; i < len; ++i )
  {
    var hc = lastHeaderRowCells[ i ];
    var dc = dataItemRow0Cells[ i ];
    if( hc.wrap ){
      hc.noWrap = true;
      dc.noWrap = true;
    }
    var w = hws[ i ];
    if( w > 0 )
      hc.width = w;
    w = dws[ i ];
    if( w > 0 )
      dc.width = w;
  }
  if( headeritem.useWidths == 'true' ){
    for( i = 0; i < len; ++i )
    {
      var w = lastHeaderRowCells[ i ].preferredWidth;
      if( w && w.indexOf( '%' ) > 0 )
        break;
    }
    var usePercentages = i < len;
    if( usePercentages ){
      headeritem.style.width = '';
      if( sboffset > 0 )
        headeritem.style.width = headeritem.offsetWidth - sboffset;
    }
    var p = 0;
    for( i = 0; i < len; ++i )
    {
      var c = lastHeaderRowCells[ i ];
      var w = c.preferredWidth;
      if( w ){
        c.removeAttribute( 'width' );
        c.style.width = w;
        if( !usePercentages || w.indexOf( '%' ) < 0 )
          dataItemRow0Cells[ i ].width = w;
        dws[ p++ ] = i;
      }
    }
    for( i = 0; i < p; ++i )
    {
      var t = dws[ i ];
      var hw = lastHeaderRowCells[ t ].offsetWidth;
      var dc = dataItemRow0Cells[ t ];
      if( dc.offsetWidth > hw )
        dc.width = hw;
    }
    var shrink = false;
    for( i = 0; i < p; ++i )
    {
      var t = dws[ i ];
      var hc = lastHeaderRowCells[ t ];
      var dw = dataItemRow0Cells[ t ].offsetWidth;
      if( hc.offsetWidth < dw ){
        hc.style.width = dw;
        if( hc.offsetWidth > dw )
          shrink = true;
      }
    }
    if( shrink )
      headeritem.style.width = 5 * len;
    for( i = 0; i < len; ++i )
    {
      w1 = dataItemRow0Cells[ i ].offsetWidth;
      w2 = lastHeaderRowCells[ i ].offsetWidth;
      w3 = Math.max( w1, w2 );
      if( c.preferredWidth )
        hws[ i ] = w3;
      else
        hws[ i ] = 0;
      if( w1 != w3 )
        dws[ i ] = w3;
      else
        dws[ i ] = 0;
    }
    headeritem.style.width = headeritem.offsetWidth;
    for( i = 0; i < len; ++i )
    {
      var w = hws[ i ];
      if( w > 0 )
        lastHeaderRowCells[ i ].width = w;
      w = dws[ i ];
      if( w > 0 )
        dataItemRow0Cells[ i ].width = w;
    }
  }
  if( resizeRow ){
    var c = lastheaderrowNbr;
    while( --c >= 0 ){
      var cells = headerItemRows[ c ].cells;
      var l = cells.length;
      while( --l >= 0 ){
        var t = cells[ l ];
        t.style.width = t.offsetWidth;
        if( t.wrap )
          t.noWrap = true;
      }
    }
    lastheaderrow.style.display = 'none';
  }
}

function stretchColumnWidths( dataitem, headeritem, available )
{
  var dataItemRow0Cells = dataitem.rows[ 0 ].cells;
  var headerItemRows = headeritem.rows;
  var lastheaderrowNbr = headerItemRows.length - 1;
  var lastheaderrow = headerItemRows[ lastheaderrowNbr ];
  var lastHeaderRowCells = lastheaderrow.cells;
  var len = lastHeaderRowCells.length;
  var resizeRow = lastheaderrow.resizeRow == 'true';
  if( resizeRow ){
    lastheaderrow.style.display = '';
    var c = lastheaderrowNbr;
    while( --c >= 0 ){
      var cells = headerItemRows[ c ].cells;
      var l = cells.length;
      while( --l >= 0 ){
        var t = cells[ l ];
        t.style.width = '';
        if( t.wrap )
          t.removeAttribute( 'noWrap' );
      }
    }
  }
  if( headeritem.useWidths == 'true' ){
    var current = 0;
    var less = 0;
    for( var i = 0; i < len; ++i ){
      var hcell = lastHeaderRowCells[ i ];
      if( hcell.preferredWidth )
        continue;
      current += dataItemRow0Cells[ i ].offsetWidth;
    }
    if( current == 0 )
      return;
    for( i = 0; i < len; ++i ){
      var hcell = lastHeaderRowCells[ i ];
      if( hcell.preferredWidth )
        continue;

      var cell = dataItemRow0Cells[ i ];
      var w2 = cell.offsetWidth;
      var w3 = w2 * available / current;
      var wf = Math.floor( w3 );
      less += w3 - wf;
      if( less >= 1 ){
        ++wf;
        --less;
      }
      var w = w2 + wf;
      cell.width = w;
      hcell.width = w;
    }
  }
  else{
    var current = 0;
    var less = 0;
    for( var i = 0; i < len; ++i )
      current += dataItemRow0Cells[ i ].offsetWidth;
    if( current == 0 )
      return;
    for( i = 0; i < len; ++i ){
      var cell = dataItemRow0Cells[ i ];
      var w2 = cell.offsetWidth;
      var w3 = w2 * available / current;
      var wf = Math.floor( w3 );
      less += w3 - wf;
      if( less >= 1 ){
        ++wf;
        --less;
      }
      var w = w2 + wf;
      cell.width = w;
      lastHeaderRowCells[ i ].width = w;
    }
  }
  if( resizeRow ){
    var c = lastheaderrowNbr;
    while( --c >= 0 ){
      var cells = headerItemRows[ c ].cells;
      var l = cells.length;
      while( --l >= 0 ){
        var t = cells[ l ];
        t.style.width = t.offsetWidth;
        if( t.wrap )
          t.noWrap = true;
      }
    }
    lastheaderrow.style.display = 'none';
  }
}

var _noResizeCount = 0;
var _alltables = null;
var _needResize = false;
var _needMinimize = isV1( );
var _resetHeight = _needMinimize;
var _firstTable;
var _firstTableIndex = 2;
var _shrinkTable;
var NO_HEIGHTS = 1;
var USE_HEIGHTS = 2;
useGeneratedScrollHelper = false;

function InvertTable( 
  tbl, syncslave, syncslaveHeader, syncslaveData, syncmaster, syncmasterHeader, syncmasterData, 
  scroller, headerScroller, syncslaveScroller )
{
  alignRowHeights( syncmasterData, syncslaveData );

  this.resetHeight = function( )
  {
    var s = scroller.style;
    s.height = '';
    s.overflowY = 'hidden';
    syncslaveScroller.style.height = '';
    this.hasVerticalScroller = false;
  }

  this.minimizeWidth = function( )
  {
    var s = scroller.style;
    s.width = 20;
    var hs = headerScroller.style;
    hs.width = 20;
  }

  this.reset = function( )
  {
    var s = scroller.style;
    s.width = '';
    headerScroller.style.width = '';
  }

  this.normalWidths = function( )
  {
    var vs = this.hasVerticalScroller ? getScrollBarSize( scroller ) : 0;
    minimizeColumnWidths( syncmasterData, syncmasterHeader, vs );
    if( vs > 0 && scroller.offsetHeight > syncmasterData.offsetHeight ){
      this.resetHeight( );
      minimizeColumnWidths( syncmasterData, syncmasterHeader, 0 );
    }
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    var w = container.availableWidth( ) - totalAvailWidth;
    if( w <= 0 ){
      w = scroller.parentNode.clientWidth - syncmasterData.offsetWidth;
      if( this.hasVerticalScroller )
        w -= getScrollBarSize( scroller );
      if( w > 0 )
        stretchColumnWidths( syncmasterData, syncmasterHeader, w - 1 );
      scroller.style.overflowX = 'hidden';
      return false;
    }
    var s = scroller.style;
    var dw = syncmasterData.offsetWidth - w - 1;
    if(dw > 0){
      var hw = dw;
      if( this.hasVerticalScroller ){
        var sb = getScrollBarSize( scroller );
        dw += sb;
        syncslaveScroller.style.height = scroller.offsetHeight - sb;
      }
      s.width = dw;
      headerScroller.style.width = hw;
      s.overflowX = 'scroll';
    }
    return true;
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    if( h >= 100 || force && h > 0 ){
      this.hasVerticalScroller = true;
      var s = scroller.style;
      s.height = h;
      s.overflowY = 'scroll';
      syncslaveScroller.style.height = h;
      return true;
    }
    return false;
  }

  this.saveScrollPos = function( )
  {
    this.scrollLeft = scroller.scrollLeft;
    this.scrollTop = scroller.scrollTop;
  }

  this.restoreScrollPos = function( )
  {
    scroller.scrollLeft = this.scrollLeft;
    scroller.scrollTop = this.scrollTop;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function FreezeTable( 
  tbl, syncslave, syncslaveHeader, syncslaveData, syncmaster, syncmasterHeader, syncmasterData, 
  scroller, headerScroller, syncslaveScroller, syncslaveHeaderScroller )
{
  this.alignRows = function( )
  {
    alignRowHeights( syncmasterHeader, syncslaveHeader );
    alignRowHeights( syncmasterData, syncslaveData );
  }

  this.resetHeight = function( )
  {
    var s = scroller.style;
    s.height = '';
    s.overflowY = 'hidden';
    s = syncslaveScroller.style;
    s.height = '';
    s.overflowX = 'hidden';
    this.hasVerticalScroller = false;
  }

  this.minimizeWidth = function( )
  {
    var s = scroller.style;
    s.width = 20;
    var hs = headerScroller.style;
    hs.width = 20;
  }

  this.reset = function( )
  {
    scroller.style.width = '';
    headerScroller.style.width = '';
    syncslaveScroller.style.width = '';
    syncslaveHeaderScroller.style.width = '';
  }

  this.normalWidths = function( )
  {
    var vs = this.hasVerticalScroller ? getScrollBarSize( scroller ) : 0;
    minimizeColumnWidths( syncmasterData, syncmasterHeader, vs );
    minimizeColumnWidths( syncslaveData, syncslaveHeader, vs );
    if( vs > 0 && scroller.offsetHeight > syncmasterData.offsetHeight && syncslaveScroller.offsetHeight > syncslaveData.offsetHeight ){
      this.resetHeight( );
      minimizeColumnWidths( syncmasterData, syncmasterHeader, 0 );
      minimizeColumnWidths( syncslaveData, syncslaveHeader, 0 );
    }
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    var w = container.availableWidth( ) - totalAvailWidth;
    if( w <= 0 ){
      w = scroller.parentNode.clientWidth - syncmasterData.offsetWidth;
      if( this.hasVerticalScroller )
        w -= getScrollBarSize( scroller );
      if( w > 0 )
        stretchColumnWidths( syncmasterData, syncmasterHeader, w - 1 );
      scroller.style.overflowX = 'hidden';
      syncslaveScroller.style.overflowX = 'hidden';
      this.alignRows( );
      return false;
    }
    var s = scroller.style;
    var dw = syncmasterData.offsetWidth - w - 1;
    if(dw > 0){
      var hw = dw;
      if( this.hasVerticalScroller )
        dw += getScrollBarSize( scroller );
      s.width = dw;
      headerScroller.style.width = hw;
      s.overflowX = 'scroll';
    }
    this.alignRows( );
    return true;
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    var sh = syncslaveScroller.offsetHeight - shrink;
    if(sh > h)
      h = sh;
    if( h >= 100 || force && h > 0 ){
      this.hasVerticalScroller = true;
      var s = scroller.style;
      s.height = h;
      s.overflowY = 'scroll';
      s = syncslaveScroller.style;
      s.height = h;
      s.overflowX = 'scroll';
      return true;
    }
    return false;
  }

  this.saveScrollPos = function( )
  {
    this.scrollLeft = scroller.scrollLeft;
    this.scrollTop = scroller.scrollTop;
  }

  this.restoreScrollPos = function( )
  {
    scroller.scrollLeft = this.scrollLeft;
    scroller.scrollTop = this.scrollTop;
  }
  
  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function DraggableTable( 
  tbl, syncslave2, syncslave2Header, syncslave2Data, syncslave, syncslaveHeader, syncslaveData, 
  syncmaster, syncmasterHeader, syncmasterData, scroller, headerScroller, syncslaveScroller, 
  syncslave2Scroller, syncslave2HeaderScroller, draggerImg )
{
  this.alignRows = function( )
  {
    alignRowHeights( syncmasterHeader, syncslaveHeader );
    alignRowHeights( syncmasterHeader, syncslave2Header );
    alignRowHeights( syncslaveHeader, syncslave2Header );
    alignRowHeights( syncmasterData, syncslaveData );
    alignRowHeights( syncmasterData, syncslave2Data );
    alignRowHeights( syncslaveData, syncslave2Data );
  }

  var renderEnv = "runtime";
  if(form.RENDER_ENVIRONMENT)
  {
    renderEnv = form.RENDER_ENVIRONMENT.value;
  }
  
  draggerImg.style.cursor = 'move';
  if(renderEnv != "studio")
  {
    draggerImg.ondragenter = function( ){ draggable.dragEnter( ); };
    draggerImg.ondrag = function( ){ draggable.drag( ); };
  }
  
  this.resetHeight = function( )
  {
    var s = scroller.style;
    s.height = '';
    s.overflowY = 'hidden';
    syncslaveScroller.style.height = '';
    syncslave2Scroller.style.height = '';
    this.hasVerticalScroller = false;
  }

  this.minimizeWidth = function( )
  {
    var s = scroller.style;
    s.width = 20;
    var hs = headerScroller.style;
    hs.width = 20;
  }

  this.reset = function( )
  {
    scroller.style.width = '';
    headerScroller.style.width = '';
    syncslave2HeaderScroller.style.width = '';
    syncslave2Scroller.style.width = '';
  }

  this.normalWidths = function( )
  {
    var vs = this.hasVerticalScroller ? getScrollBarSize( scroller ) : 0;
    minimizeColumnWidths( syncslave2Data, syncslave2Header, vs );
    minimizeColumnWidths( syncmasterData, syncmasterHeader, vs );
    if( vs > 0 && scroller.offsetHeight > syncmasterData.offsetHeight && syncslave2Scroller.offsetHeight > syncslave2Data.offsetHeight ){
      this.resetHeight( );
      minimizeColumnWidths( syncmasterData, syncmasterHeader, 0 );
      minimizeColumnWidths( syncslaveData, syncslaveHeader, 0 );
    }
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    var sw = syncslave2.width;
    if( isNaN( sw ) )
      sw = eval( syncslave2.id + '_width' );
    var s2 = syncslave2Scroller.style;
    s2.width = sw;
    syncslave2HeaderScroller.style.width = sw;
    sw -= syncslave2Data.clientWidth;
    if( sw > 0 )
      stretchColumnWidths( syncslave2Data, syncslave2Header, sw - 1 );
    w = container.availableWidth( ) - totalAvailWidth;
    var s = scroller.style;
    if( w > 0 || sw < 0 ){
      s.overflowX = 'scroll';
      s2.overflowX = 'scroll';
      if( this.hasVerticalScroller )
        syncslaveScroller.style.overflowX = 'scroll';
      else
        syncslaveScroller.style.overflowX = 'hidden';
    }
    else{
      s.overflowX = 'hidden';
      s2.overflowX = 'hidden';
      syncslaveScroller.style.overflowX = 'hidden';
    }
    if( w <= 0 ){
      w = scroller.parentNode.clientWidth - syncmasterData.offsetWidth;
      if( this.hasVerticalScroller )
        w -= getScrollBarSize( scroller );
      if( w > 0 )
        stretchColumnWidths( syncmasterData, syncmasterHeader, w - 1 );
      this.alignRows( );
      return false;
    }
    var dw = syncmasterData.offsetWidth - w - 1;
    if(dw > 0){
      var hw = dw;
      if( this.hasVerticalScroller )
        dw += getScrollBarSize( scroller );
      s.width = dw;
      headerScroller.style.width = hw;
    }
    this.alignRows( );
    return true;
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    var sh = syncslaveScroller.offsetHeight - shrink;
    if(sh > h)
      h = sh;
    if( h >= 80 || force && h > 0 ){ 
      this.hasVerticalScroller = true;
      var s = scroller.style;
      s.height = h;
      s.overflowY = 'scroll';
      syncslave2Scroller.style.height = h;
      s = syncslaveScroller.style;
      s.height = h;
      s.overflowX = 'scroll';
      return true;
    }
    return false;
  }

  this.saveScrollPos = function( )
  {
    this.scrollLeft = scroller.scrollLeft;
    this.scrollTop = scroller.scrollTop;
    this.scrollLeft2 = syncslave2Scroller.scrollLeft;
    this.scrollTop2 = syncslave2Scroller.scrollTop;
  }

  this.restoreScrollPos = function( )
  {
    scroller.scrollLeft = this.scrollLeft;
    scroller.scrollTop = this.scrollTop;
    syncslave2Scroller.scrollLeft = this.scrollLeft2;
    syncslave2Scroller.scrollTop = this.scrollTop2;
  }

  var draggable = this;

  this.drag = function( )
  {
    var pos = event.clientX;
    var w = this.startWidth - this.startPos + pos;
    var aw = tbl.offsetWidth;
    if( w > 30 && w < aw - syncslave.offsetWidth - 30 ){
      syncslave2.width = w;
      this.reset( );
      this.normalWidths( );
      this.resizeWidths( this, aw );
    }
  }

  this.dragEnter = function( )
  {
    this.startPos = event.clientX;
    this.startWidth = syncslave2.offsetWidth;
  }

  this.availableWidth = function( )
  {
    return tbl.offsetWidth;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function Table( tbl, header, data, scroller, headerScroller )
{
  this.resetHeight = function( )
  {
    var s = scroller.style;
    if(data.chunked > 0){
      data.chunk = 'skip';
      s.height = 10000;
    }
    else{
      s.height = '';
      s.overflowY = 'hidden';
      this.hasVerticalScroller = false;
    }
  }

  this.minimizeWidth = function( )
  {
    var s = scroller.style;
    s.width = 20;
    var hs = headerScroller.style;
    hs.width = 20;
  }

  this.reset = function( )
  {
    var c = data.chunked;
    if(c > 0){
      this.hasVerticalScroller = true;
      if(data.chunk == 'true'){
        data.chunk = 'create';
        var rows = new Array();
        var tbody = data.children[1];
        var children = tbody.children;
        var i = 2;
        var j = 1;
        rows[0] = data.rows[0];
        while(j < c){
          var t = children[i];
          t.row = j;
          t = t.data;
          rows[j++] = t;
          i += 2;
        }
        data.allRows = rows;
      }
      var s = scroller.style;
      s.height = 10000;
      s.overflowY = 'scroll';

    }
    scroller.style.width = '';
    headerScroller.style.width = '';
  }

  this.normalWidths = function( measureTable, totalAvailWidth )
  {
    var vs = this.hasVerticalScroller ? getScrollBarSize( scroller ) : 0;
    minimizeColumnWidths( data, header, vs );
    if( vs > 0 && scroller.offsetHeight > data.offsetHeight && data.chunked == null){
      this.resetHeight( );
      minimizeColumnWidths( data, header, 0 );
    }
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    var w = container.availableWidth( ) - totalAvailWidth;
    if( w <= 0 ){
      w = scroller.parentNode.clientWidth - data.offsetWidth;
      if( this.hasVerticalScroller )
        w -= getScrollBarSize( scroller );
      if( w > 0 )
        stretchColumnWidths( data, header, w - 1 );
      scroller.style.overflowX = 'hidden';
      return false;
    }
    var s = scroller.style;
    var dw = data.offsetWidth - w - 1;
    if(dw > 0){
      var hw = dw;
      if( this.hasVerticalScroller )
        dw += getScrollBarSize( scroller );
      s.width = dw;
      headerScroller.style.width = hw;
      s.overflowX = 'scroll';
    }
    return true;
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    if( h >= 80 || force && h > 0 ){
      this.hasVerticalScroller = true;
      var s = scroller.style;
      s.height = h;
      s.overflowY = 'scroll';
      if(data.chunked > 0){
        var tbody = data.children[1];
        var children = tbody.children;
        var j = 1;
        var chunk = data.chunk
        var after = document.createElement('DIV');
        if(chunk == 'create'){
          data.chunk = 'done';
          var before = document.createElement('DIV');
          before.style.display = 'none';
          scroller.insertBefore(before, data);
          scroller.appendChild(after);
          scroller.onscroll = function (){ showChunkedLines(data, scroller, header, headerScroller, before, after); }
        }
        else if(chunk == 'skip'){
          data.chunk = 'done';
          return;
        }
        else{
          var before = scroller.firstChild;
          before.style.display = 'none';
          after = scroller.lastChild;
          after.style.height = 0;
          var l = data.visibleRows;
          while(--l >= 0)
            tbody.replaceChild(document.createComment(''), children[l * 2]);
          j = 0;
        }
        var sh = scroller.clientHeight;
        var rows = data.allRows;
        while(sh > data.clientHeight){
          var row = rows[j];
          if(typeof row == 'string'){
            after.innerHTML = '<TABLE>' + row + '</TABLE>';
            row = after.firstChild.rows[0];
            saveOld(row.children, true);
            rows[j] = row;
          }
          tbody.replaceChild(row, children[j * 2]);
          ++j;
        }
        minimizeColumnWidths(data, header, getScrollBarSize(scroller));
        after.innerHTML = '';
        var ch = data.clientHeight;
        var c = data.chunked;
        var h = c / j * ch;
        data.totalHeight = h;
        data.visibleRows = j;
        data.lastPos = 0;
        after.style.height = h - ch;
      }
      return true;
    }
    return false;
  }

  this.saveScrollPos = function( )
  {
    this.scrollLeft = scroller.scrollLeft;
    this.scrollTop = scroller.scrollTop;
  }

  this.restoreScrollPos = function( )
  {
    scroller.scrollLeft = this.scrollLeft;
    scroller.scrollTop = this.scrollTop;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function Tabset( tbl )
{
  this.resetHeight = function( )
  {
  }

  this.minimizeWidth = function( )
  {
  }

  this.reset = function( )
  {
  }

  this.normalWidths = function( )
  {
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    resizeTabsetScroller( tbl, container.scrollWidth( ) );
  }

  this.shrinkHeight = function( shrink, force )
  {
    return false;
  }

  this.availableWidth = function( )
  {
  }

  this.saveScrollPos = function( )
  {
  }

  this.restoreScrollPos = function( )
  {
  }

  this.getPreferredHeight = function( )
  {
    return 0;
  }
}

function FlowChart( tbl, scroller, embed, parent )
{
  this.clear = function( )
  {
    var h = 10000;
    embed.height = h;
  }

  this.resetHeight = function( )
  {
    var s = scroller.style;
    s.height = '';
  }

  this.minimizeWidth = function( )
  {
    var s = scroller.style;
    s.width = 20;
  }

  this.reset = function( )
  {
    scroller.style.width = '20';
  }

  this.normalWidths = function( )
  {
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    var w = tbl.offsetWidth;
    if (w > 0)
      embed.width = w;
    scroller.style.width = '';
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    if( h > 0 ){
      var s = scroller.style;
      embed.height = h;
      return true;
    }
    return false;
  }

  this.availableWidth = function( )
  {
    return parent.clientWidth;
  }

  this.add = function( t )
  {
    var x = this.tables;
    x[ x.length ] = t;
    t.container = this;
  }

  this.saveScrollPos = function( )
  {
  }

  this.restoreScrollPos = function( )
  {
  }

  this.scrollWidth = function( )
  {
    return scroller.scrollWidth;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function PGLChart( tbl, scroller, embed, parent )
{
  this.clear = function( )
  {
    var h = 10000;
    if(tbl.preferredHeight <= 0)
      embed.height = h;
  }

  this.resetHeight = function( )
  {
    var s = scroller.style;
    s.height = '';
    s.overflowY = 'hidden';
    this.hasVerticalScroller = false;
  }

  this.minimizeWidth = function( )
  {
    var s = scroller.style;
    s.width = 20;
  }

  this.reset = function( )
  {
    scroller.style.width = '20';
  }

  this.normalWidths = function( )
  {
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    var w = tbl.offsetWidth;
    var s = scroller.style;
    s.width = '';
    if(tbl.preferredWidth > 0){
      var w = scroller.offsetWidth - totalAvailWidth;
      if( w <= 0 ){
        s.overflowX = 'hidden';
        return false;
      }
      var dw = embed.offsetWidth - w - 1;
      if(dw > 0){
        if( this.hasVerticalScroller )
          dw += getScrollBarSize( scroller );
        s.width = dw;
        s.overflowX = 'scroll';
        return true;
      }
    }
     else if (w > 0)
      embed.width = w;
    scroller.style.width = '';
    return false;
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    if(h > 0){ 
      if(tbl.preferredHeight > 0){
        var s = scroller.style;
        s.height = h;
        s.overflowY = 'scroll';
        this.hasVerticalScroller = true;
      }
      else
        embed.height = h - (scroller.offsetHeight - embed.offsetHeight);
      return true;
    }
    return false;
  }

  this.availableWidth = function( )
  {
    return parent.clientWidth;
  }

  this.add = function( t )
  {
    var x = this.tables;
    x[ x.length ] = t;
    t.container = this;
  }

  this.saveScrollPos = function( )
  {
  }

  this.restoreScrollPos = function( )
  {
  }

  this.scrollWidth = function( )
  {
    return scroller.scrollWidth;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return tbl.offsetHeight;
  }
}

function Workbook( tbl, scroller, embed, parent )
{
  this.clear = function( )
  {
    var h = tbl.preferredHeight;
    if(h == null)
      h = 10000;
    embed.height = h;
  }

  this.resetHeight = function( )
  {
    var s = scroller.style;
    s.height = '';
  }

  this.minimizeWidth = function( )
  {
    var s = scroller.style;
    s.width = 20;
  }

  this.reset = function( )
  {
    scroller.style.width = '20';
  }

  this.normalWidths = function( )
  {
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    var w = tbl.offsetWidth;
    embed.width = w;
    scroller.style.width = '';
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    if( h > 0 ){
      embed.height = h;
      return true;
    }
    return false;
  }

  this.availableWidth = function( )
  {
    return parent.clientWidth;
  }

  this.add = function( t )
  {
    var x = this.tables;
    x[ x.length ] = t;
    t.container = this;
  }

  this.saveScrollPos = function( )
  {
  }

  this.restoreScrollPos = function( )
  {
  }

  this.scrollWidth = function( )
  {
    return scroller.scrollWidth;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function Pivot( tbl, scroller, embed, parent )
{
  this.clear = function( )
  {
    var h = tbl.preferredHeight;
    if(h == null)
      h = 10000;
    scroller.style.height = h;
  }

  this.resetHeight = function( )
  {
  }

  this.minimizeWidth = function( )
  {
  }

  this.reset = function( )
  {
    embed.MaxWidth = 20;
    scroller.style.width = '20';
  }

  this.normalWidths = function( )
  {
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    embed.MaxWidth = parent.clientWidth;
    scroller.style.width = '';
  }

  this.shrinkHeight = function( shrink, force )
  {
    var h = scroller.offsetHeight - shrink;
    if( h > 0 ){
      scroller.style.height = h;
      embed.MaxHeight = h - 30;
      return true;
    }
    return false;
  }

  this.availableWidth = function( )
  {
    return parent.clientWidth;
  }

  this.add = function( t )
  {
    var x = this.tables;
    x[ x.length ] = t;
    t.container = this;
  }

  this.saveScrollPos = function( )
  {
  }

  this.restoreScrollPos = function( )
  {
  }

  this.scrollWidth = function( )
  {
    return scroller.scrollWidth;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function Container( tbl, scroller, parent )
{
  this.tables = new Array( );

  this.clear = function( )
  {
    if( scroller ){
      scroller.removeAttribute( 'containerIndex' );
      var h = tbl.preferredHeight;
      if(h)
        scroller.style.height = h;
    }
  }

  this.resetHeight = function( )
  {
    if( scroller ){
      var s = scroller.style;
      var h = tbl.preferredHeight;
      s.height = h ? h : '';
      s.overflowY = 'hidden';
    }
    this.hasVerticalScroller = false;
  }

  this.minimizeWidth = function( )
  {
    if( scroller ){
      var s = scroller.style;
      s.width = 20;
    }
  }

  this.reset = function( )
  {
    if( scroller )
      scroller.style.width = '';
  }

  this.normalWidths = function( )
  {
  }

  this.resizeWidths = function( container, totalAvailWidth )
  {
    if( scroller ){
      var s = scroller.style;
      var w = container.availableWidth( ) - totalAvailWidth;
      if( w > 0 ){
        if( this.hasVerticalScroller )
          w -= getScrollBarSize( scroller );
        if(scroller.scrollWidth > w){
          s.width = scroller.scrollWidth - w;
          s.overflowX = 'scroll';
        }
      }
      else
        s.overflowX = 'hidden';
    }
  }

  this.shrinkHeight = function( shrink, force )
  {
    var tables = this.tables;
    var j = tables.length;
    while( --j >= 0 ){
      var c = tables[ j ];
      if( c.shrinkHeight( shrink, false ) )
        return true;
    }
    var h = scroller.offsetHeight - shrink;
    if( h >= 300 || force && h > 0 ){
      var s = scroller.style;
      s.height = h;
      s.overflowY = 'scroll';
      this.hasVerticalScroller = true;
      return true;
    }
    return false;
  }

  this.availableWidth = function( )
  {
    if( this.hasVerticalScroller )
      return parent.clientWidth - getScrollBarSize( scroller );
    return parent.clientWidth;
  }

  this.add = function( t )
  {
    var x = this.tables;
    x[ x.length ] = t;
    t.container = this;
  }

  this.saveScrollPos = function( )
  {
    if( scroller ){
      this.scrollLeft = scroller.scrollLeft;
      this.scrollTop = scroller.scrollTop;
    }
  }

  this.restoreScrollPos = function( )
  {
    if( scroller ){
      scroller.scrollLeft = this.scrollLeft;
      scroller.scrollTop = this.scrollTop;
    }
  }

  this.scrollWidth = function( )
  {
    if( scroller )
      return scroller.scrollWidth;
    return -1;
  }

  this.saveScrollPos( );

  this.getPreferredHeight = function( )
  {
    return tbl.preferredHeight;
  }

  this.getCurrentHeight = function( )
  {
    return scroller.offsetHeight;
  }
}

function addToContainer( tbl, t, containers )
{
  var p = tbl.parentNode;
  while( p ){
    var i = p.containerIndex;
    if( i >= 0 ){
      containers[ i ].add( t );
      return;
    }
    p = p.parentNode;
  }
  containers[ 0 ].add( t );
}

function resizeContainer( container, aw )
{
  var tables = container.tables;
  var l = tables.length;
  for( var j = 0; j < l; ++j ){
    var t = tables[ j ];
    t.reset( );
    t.normalWidths( );
    t.resizeWidths( container, aw );
  }
  for( var j = 0; j < l; ++j ){
    var c = tables[ j ];
    if( c.tables )
      resizeContainer( c, c.availableWidth( ) );
  }
}

function getHeightTable( )
{
  return document.getElementById( 'preferredHeightTbl' );
}

function shrinkHeight( container, shrink, force )
{
  var tables = container.tables;
  container.splitHeight = false;
  var l = tables.length;






  var ph = container.preferredHeights;
  if( ph == null ){
    j = l;
    while( --j >= 0 )
    {
      var h = tables[ j ].getPreferredHeight( );
      if( h > 0 || h && h.length > 0)
        break;
    }
    ph = j < 0 ? NO_HEIGHTS : USE_HEIGHTS;
    container.preferredHeights = ph;
  }
  if( ph == USE_HEIGHTS ){
    var bh = document.body.clientHeight + shrink;
    var hs = 0;
    var ht = getHeightTable( );
    var hts = ht.style;
    hts.width = '';
    var tds = ht.rows[ 0 ].cells;
    j = tds.length;
    while( j > l )
      tds[ --j ].style.display = 'none';
    while( --j >= 0 ){
      var t = tables[ j ];
      hs += t.getCurrentHeight( );
      var p = t.getPreferredHeight( );
      t = tds[ j ];
      t = t.style;
      t.display = '';
      if( p )
        t.width = p;
      else
        t.width = '100%';
    }
    if( shrink + l * 30 < hs ){
      hs -= shrink
      hts.width = hs;
      var again = true;
      while( again ){
        again = false;
        j = l;
        while( --j >= 0 ){
          var t = tables[ j ];
          var ch = t.getCurrentHeight( );
          var td = tds[ j ];
          if( td.offsetWidth > ch ){
            hs -= ch;
            hts.width = hs;
            td.style.display = 'none';
            again = true;
          }
        }
      }
      j = l;
      while( --j >= 0 ){
        var t = tables[ j ];
        var ch = t.getCurrentHeight( );
        var nh = tds[ j ].offsetWidth;
        if( nh > 0 && ch > nh )
          t.shrinkHeight( ch - nh, true );
      }
      _shrinkTable = container;
      _shrinkTable.splitHeight = true;
      return true;
    }
  }
  var j = l;  
  while( --j >= 0 ){
    var c = tables[ j ];
    if( c.tables && shrinkHeight( c, shrink, false ) )
      return true;
  }
  while( --l >= 0 ){
    var t = tables[ l ];
    if( t.shrinkHeight( shrink, force ) ){
      if( document.body.clientHeight < _firstTable.offsetHeight )
        t.resetHeight( );
      else{
        _shrinkTable = t;
        return true;
      }
    }
  }
}

function getTableChild( tbl, r, c, tag )
{
  var rows = tbl.rows;
  if( rows.length <= r )
    return null;
  var cells = rows[ r ].cells;
  if( cells.length <= c )
    return null;
  var children = cells[ c ].children;
  var l = children.length;
  for( var i = 0; i < l; ++i ){
    var child = children[ i ];
    var t = child.tagName;
    if( t == tag )
      return child;
    if( t != '!' )
      return null;
  }
  return null;
}

function getScrollerData( s )
{
  return s.children[ 0 ];
}

function resetHeight( )
{
  if( _shrinkTable.preferredHeights == USE_HEIGHTS && _shrinkTable.splitHeight ){
    var tables = _shrinkTable.tables;
    var l = tables.length;
    while( --l >= 0 )
      tables[ l ].resetHeight( );
  }
  else
    _shrinkTable.resetHeight( );
}

function defaultScrollers( )
{
  if( _noResizeCount > 0 ){
    _needResize = true;
    return;
  }
  _needResize = false;
  if( _alltables == null ){
    var tables = document.getElementsByTagName( "TABLE" );
    var noOfTables = tables.length;

    var firstTable = isV2( ) ? _firstTableIndex : 0;
    _alltables = new Array( );
    _alltables[ 0 ] = new Container( null, null, _firstTable = tables[ firstTable ] );
    var ignoreTo = null;
    for( var j = firstTable; j < noOfTables; j++ )
    {
      var tbl = tables[ j ];
      if( ignoreTo ){
        if( ignoreTo == tbl )
          ignoreTo = null;
        continue;
      }
      if( !isTableVisible( tbl ) )
        continue;
      if( tbl.id == 'tabs_container' ){
        var t = new Tabset( tbl );
        _alltables[ _alltables.length ] = t;
        addToContainer( tbl, t, _alltables );
      }
      var tblId = tbl.id;
      if( tblId == 'flowchart_table' ){
        var div = tbl.rows[ 0 ].cells[ 0 ].children[ 0 ];
        var t = new FlowChart( tbl, div, div.children[ 0 ], tbl.parentElement );
        _alltables[ _alltables.length ] = t;
        addToContainer( tbl, t, _alltables );
        continue;
      }
      if( tblId == 'pglchart_table' ){
        var div = tbl.rows[ 0 ].cells[ 0 ].firstChild;
        var t = new PGLChart( tbl, div, div.firstChild, tbl.parentElement );
        _alltables[ _alltables.length ] = t;
        addToContainer( tbl, t, _alltables );
        continue;
      }
      if( tblId == 'workbook_table' ){
        var div = tbl.rows[ 0 ].cells[ 0 ].children[ 0 ];
        var t = new Workbook( tbl, div, div.children[ 0 ], tbl.parentElement );
        _alltables[ _alltables.length ] = t;
        addToContainer( tbl, t, _alltables );
        continue;
      }
      if( tbl.className == 'shadow' ){
        var scroller = getTableChild( tbl, 1, 0, 'DIV' );
        if( scroller ){
          var i = _alltables.length;
          scroller.containerIndex = i;
          var t = new Container( tbl, scroller, scroller.parentNode );
          _alltables[ i ] = t;
          addToContainer( tbl, t, _alltables );
        }
        continue;
      }
      var tbl0 = getTableChild( tbl, 0, 0, 'TABLE' );
      if( tbl0 == null ){
        var obj = getTableChild( tbl, 0, 0, 'OBJECT' );
        if( obj ){
          obj = obj.nextSibling;
          if( obj && obj.component == 'excel-pivot' ){
            var div = tbl.parentElement;
            var t = new Pivot( tbl, div, obj, div.parentElement );
            _alltables[ _alltables.length ] = t;
            addToContainer( tbl, t, _alltables );
            continue;
          }
        }
        if( tblId.substring(0, 4) == '_un_' )
          continue;
        var headerScroller = getTableChild( tbl, 0, 0, 'DIV' );
        if( headerScroller == null || headerScroller.js == 'c')
          continue;
        var masterScroller = getTableChild( tbl, 1, 0, 'DIV' );
        if( masterScroller == null || masterScroller.js == 'c')
          continue;

        var header = getScrollerData( headerScroller );
        var data = getScrollerData( masterScroller );
        var t = new Table( tbl, header, data, masterScroller, headerScroller );
        _alltables[ _alltables.length ] = t;
        addToContainer( tbl, t, _alltables );
        ignoreTo = data;
        continue;
      }
      if( tblId == 'tabs_container' ){
        var scroller = getTableChild( tbl0, 1, 0, 'DIV' );
        if( scroller == null )
          continue;
        var id = scroller.id;
        if( id == 'tabs_container_description_scroller' || id == 'tabs_container_scroller' ){
          var i = _alltables.length;
          scroller.containerIndex = i;
          var t = new Container( tbl, scroller, scroller.parentNode );
          _alltables[ i ] = t;
          addToContainer( tbl, t, _alltables );
        }
      }

      var tbl2 = getTableChild( tbl, 0, 2, 'TABLE' );
      if( tbl2 == null ){
        var syncslave = tbl0;
        var slaveHeaderScroller = getTableChild( syncslave, 0, 0, 'DIV' );
        if( slaveHeaderScroller == null ){
          var syncslaveHeader = getTableChild( syncslave, 0, 0, 'TABLE' );
          if( syncslaveHeader == null ){
            var syncmaster = getTableChild( tbl, 0, 1, 'TABLE' );
            if( syncmaster == null )
              continue;
            var sid = syncslave.id;
            var mid = syncmaster.id;
            var si = sid.indexOf( '_syncslave' );
            var mi = mid.indexOf( '_syncmaster' );
            if( isV2( ) && si > 0 && mi > 0 && si == sid.length - 10 && mi == mid.length - 11 )
              alignRowHeights( syncmaster, syncslave );
            continue;
          }
          var syncslaveScroller = getTableChild( syncslave, 1, 0, 'DIV' );
          if( syncslaveScroller == null )
            continue;
          var syncslaveData = getScrollerData( syncslaveScroller );

          var syncmaster = getTableChild( tbl, 0, 1, 'TABLE' );
          if( syncmaster == null )
            continue;

          var headerScroller = getTableChild( syncmaster, 0, 0, 'DIV' );
          if( headerScroller == null || headerScroller.js == 'c')
            continue;
          var masterScroller = getTableChild( syncmaster, 1, 0, 'DIV' );
          if( masterScroller == null || masterScroller.js == 'c')
            continue;

          var syncmasterHeader = getScrollerData( headerScroller );
          var syncmasterData = getScrollerData( masterScroller );
          var t = new InvertTable(
              tbl, syncslave, syncslaveHeader, syncslaveData, syncmaster, syncmasterHeader, syncmasterData, 
              masterScroller, headerScroller, syncslaveScroller );
          _alltables[ _alltables.length ] = t;
          addToContainer( tbl, t, _alltables );
          ignoreTo = syncmasterData;
          continue;
        }
        var slaveScroller = getTableChild( syncslave, 1, 0, 'DIV' );
        if( slaveScroller == null )
          continue;
        var syncmaster = getTableChild( tbl, 0, 1, 'TABLE' );
        if( syncmaster == null )
          continue;

        var headerScroller = getTableChild( syncmaster, 0, 0, 'DIV' );
        if( headerScroller == null )
          continue;
        var masterScroller = getTableChild( syncmaster, 1, 0, 'DIV' );
        if( masterScroller == null )
          continue;

        var masterScroller = getTableChild( syncmaster, 1, 0, 'DIV' );
        if( masterScroller == null )
          continue;

        var syncslaveHeader = getScrollerData( slaveHeaderScroller );
        var syncslaveData = getScrollerData( slaveScroller );
        var syncmasterHeader = getScrollerData( headerScroller );
        var syncmasterData = getScrollerData( masterScroller );
        t = new FreezeTable(
          tbl, syncslave, syncslaveHeader, syncslaveData, syncmaster, syncmasterHeader, syncmasterData, 
          masterScroller, headerScroller, slaveScroller, slaveHeaderScroller );
        _alltables[ _alltables.length ] = t;
        addToContainer( tbl, t, _alltables );
        ignoreTo = syncmasterData;
        continue;
      }
      var syncslave2 = tbl0;
      var slave2HeaderScroller = getTableChild( syncslave2, 0, 0, 'DIV' );
      if( slave2HeaderScroller == null )
        continue;
      var slave2Scroller = getTableChild( syncslave2, 1, 0, 'DIV' );
      if( slave2Scroller == null )
        continue;
      var syncslave = getTableChild( tbl, 0, 1, 'TABLE' );
      if( syncslave == null )
        continue;

      var syncslaveHeader = getTableChild( syncslave, 0, 0, 'TABLE' );
      if( syncslaveHeader == null )
        continue;
      var slaveScroller = getTableChild( syncslave, 1, 0, 'DIV' );
      if( slaveScroller == null )
        continue;

      var syncmaster = tbl2;
      var headerScroller = getTableChild( syncmaster, 0, 0, 'DIV' );
      if( headerScroller == null )
        continue;
      var masterScroller = getTableChild( syncmaster, 1, 0, 'DIV' );
      if( masterScroller == null )
        continue;

      var draggerImg = null;
      var numRows = syncslaveHeader.rows.length;
      for(var i=0; i<numRows; i++)
      {
        draggerImg = getTableChild( syncslaveHeader, i, 0, 'IMG' );
        if( draggerImg )
          break;
      }
      if( draggerImg == null )
        continue;

      var syncslave2Header = getScrollerData( slave2HeaderScroller );
      var syncslave2Data = getScrollerData( slave2Scroller );
      var syncslaveData = getScrollerData( slaveScroller );
      var syncmasterHeader = getScrollerData( headerScroller );
      var syncmasterData = getScrollerData( masterScroller );

      var t = new DraggableTable( 
        tbl, syncslave2, syncslave2Header, syncslave2Data, syncslave, syncslaveHeader, syncslaveData, syncmaster, syncmasterHeader, syncmasterData, 
        masterScroller, headerScroller, slaveScroller, slave2Scroller, slave2HeaderScroller, draggerImg );
      _alltables[ _alltables.length ] = t;
      addToContainer( tbl, t, _alltables );
      ignoreTo = syncmasterData;
    }
  }
  else{
    var l = _alltables.length;
    while( --l >= 0 )
      _alltables[ l ].saveScrollPos( );
  }
  var noOfTables = _alltables.length;
  for( var j = 0; j < noOfTables; ++j ){
    var c = _alltables[ j ].clear;
    if( c )
      c( );
  }
  if( _resetHeight ){
    for( var j = 0; j < noOfTables; ++j )
      _alltables[ j ].resetHeight( );
    _resetHeight = false;
  }
  if( _shrinkTable ){
    resetHeight( );
    _shrinkTable = null;
  }
  if( _needMinimize ){
    for( var j = 0; j < noOfTables; ++j )
      _alltables[ j ].minimizeWidth( );
  }
  var shrink = _firstTable.offsetHeight - document.body.clientHeight;
  if( shrink > 0 )
    shrinkHeight( _alltables[ 0 ], shrink, true );
  resizeContainer( _alltables[ 0 ], document.body.clientWidth );
  shrink = _firstTable.offsetHeight - document.body.clientHeight;
  if( shrink > 0 ){
    if( _shrinkTable ){
      resetHeight( );
      shrink = _firstTable.offsetHeight - document.body.clientHeight;
      if( shrink > 0 ){
        if( _shrinkTable.preferredHeights == USE_HEIGHTS )
          shrinkHeight( _shrinkTable, shrink, true );
        else
          _shrinkTable.shrinkHeight( shrink, true );
      }
    }
    else
      shrinkHeight( _alltables[ 0 ], shrink, true );
  }
  for( var j = 0; j < noOfTables; ++j )
    _alltables[ j ].restoreScrollPos( );
  _needMinimize = true;
}

function alignTableColumns( sourceId, targetId, reverse )
{
  var sd = document.getElementById( sourceId + '_syncmaster_data' );
  if( sd ){
    if( sd.offsetWidth > 0 ){
      var th = document.getElementById( targetId + '_syncmaster_header' );
      var td = document.getElementById( targetId + '_syncmaster_data' );
      var sc = sd.rows[ 0 ].cells;
      var hc = th.rows[ 0 ].cells;
      var dc = td.rows[ 0 ].cells;
      var l = sc.length;
      if( reverse ){
        var sh = document.getElementById( sourceId + '_syncmaster_header' ).rows[ 0 ].cells;
        for( var i = 0; i < l; ++i ){
          var w = sc[ i ].offsetWidth;
          var rw = dc[ i ].offsetWidth;
          if( rw > w )
            w = rw;
          hc[ i ].style.width = w;
          dc[ i ].style.width = w;
          sh[ i ].style.width = w;
          sc[ i ].style.width = w;
        }
      }
      else{
        for( var i = 0; i < l; ++i ){
          var w = sc[ i ].offsetWidth;
          hc[ i ].style.width = w;
          dc[ i ].style.width = w;
        }
      }
    }
  }
  else{
    sd = document.getElementById( sourceId + '_syncmaster' );
    if( sd.offsetWidth > 0 ){
      var td = document.getElementById( targetId + '_syncmaster' );
      var sc = sd.rows[ 0 ].cells;
      var dc = td.rows[ 0 ].cells;
      var l = sc.length;
      for( var i = 0; i < l; ++i ){
        var w = sc[ i ].offsetWidth;
        if( reverse ){
          var rw = dc[ i ].offsetWidth;
          if( rw > w )
            w = rw;
          sc[ i ].style.width = w;
        }
        dc[ i ].style.width = w;
      }
    }
  }
}

function disableResize( )
{
  ++_noResizeCount;
}

function enableResize( )
{
  if( --_noResizeCount == 0 && _needResize )
    scrollHelper( );
}

function isTableVisible( tbl )
{
  if( tbl.style.display == 'none' )
    return false;
  return true;
}

function openerWindow( )
{
  if( opener )
    return opener;
  if( typeof( dialogArguments ) == 'undefined' ){
    var p = window.parent;
    if(p)
      return p;
    return window;
  }
  return dialogArguments;
}

function getTabUrl( tab )
{
  var x = document.getElementById( 'PAGE_NAME' ).value + '.' + tab;
  var p = parent;
  if( p == window )
    return openerWindow( )[ 'opener_' + x ];
  return p[ 'parent_' + x ];
}

function switchWorkflowTab( tab, workflow, service, params )
{
  var url = getTabUrl( tab );
  var p = new Object( );
  if( params )
    url += '&' + params;
  var i = url.indexOf( '?' );
  if( i < 0 )
    i = url.indexOf( '&' );
  var u;
  if( i > 0 ){
    u = url.substring( 0, i++ );
    for(;;){
      var a = url.indexOf( '=', i );
      var n = url.indexOf( '&', i );
      if( n < 0 ){
        if( a < 0 )
          p[ url.substring( i ) ] = '';
        else
          p[ url.substring( i, a ) ] = url.substring( a + 1 );
        break;
      }
      if( a < 0 || n < a )
        p[ url.substring( i, n ) ] = '';
      else if( a < n )
        p[ url.substring( i, a ) ] = url.substring( a + 1, n );
      i = n + 1;
    }
  }
  else
    u = url;
  if( workflow )
    p[ 'START_WORKFLOW' ] = workflow;
  if( service )
    p[ 'SERVICE_NAME' ] = service;
  url = u;
  var first = true;
  for( var x in p ){
    url += first ? '?' : '&';
    first = false;
    var t = p[ x ];
    url += x;
    if( t.length > 0 )
      url += '=' + t;
  }
  var lpm = disablePageLeaveMessage( );
  getAction( url );
  if( lpm )
    enablePageLeaveMessage( );
}

function saveTabUrl( tab, url )
{
  var x = document.getElementById( 'PAGE_NAME' ).value + '.' + tab;
  var p = parent;
  if( p == window )
    openerWindow( )[ 'opener_' + x ] = url;
  else
    p[ 'parent_' + x ] = url;
}

function getInvertTableInputIndex( t )
{
  var td = t.parentNode;
  return td.cellIndex;
}

function setStyle( t, n, s )
{
  if( n ){
    var l;
    if( typeof( n ) == 'object' && ( l = n.length ) >= 0 ){
      t = t.style;
      for( var i = 0; i < l; ++i )
        t[ n[ i ] ] = s[ i ];
    }
    else if( s )
      t.style[ n ] = s;
    else
      t.style.removeAttribute( n );
  }
  else
    t.removeAttribute( 'style' );
}

var _pageActivity = 0;
var _pageTimeout = 1000000000;
var _lastActivity = new Date( );

function pageActivity( )
{
  ++_pageActivity;
}

var frameDialogDiv = null;

function showDialog( url, width, height, resizeable )
{
  var resize = resizeable ? ';resizable:yes' : '';
  return showModalDialog( url, window, 'dialogWidth:' + width + 'px;dialogheight:' + height + 'px;status:no;unadorned:yes;help:no' + resize );
}

var FD_MOVE = 0;
var FD_DOWN = 1;
var FD_UP = 2;

function moveFrameDialog(type, t)
{
  var e = t.contentWindow.event;
  if(type == FD_DOWN){
    frameDialogDiv.moving = true;
    frameDialogDiv.moveX = frameDialogDiv.offsetLeft - e.x;
    frameDialogDiv.moveY = frameDialogDiv.offsetTop - e.y;
  }
  else if(type == FD_UP){
    frameDialogDiv.moving = false;
    s.left = frameDialogDiv.moveX + e.x;
    s.top = frameDialogDiv.moveY + e.y;
  }
  else if(frameDialogDiv.moving){
    var s = frameDialogDiv.style;
  }
}

function showFrameDialog(url, width, height)
{
  disablePage();
  frameDialogDiv = document.createElement('DIV');
  frameDialogDiv.innerHTML = '<TABLE border="0" cellspacing="0" cellpadding="0"><TR><TD width="' + width + '" class="containerHeader" nowrap="yes">&nbsp;<B>Dialog</B></TD></TR></TABLE><IFRAME onload="hookFrameDialog(this);"/>';
  var frameDialog = frameDialogDiv.lastChild;
  var body = document.body;
  body.appendChild(frameDialogDiv);
  var bw = body.clientWidth;
  var bh = body.clientHeight;
  if(width > bw)
    width = bw;
  if(height > bh)
    height = bh;
  frameDialog.width = width;
  frameDialog.height = height;
  var style = frameDialogDiv.style;
  style.position = 'absolute';
  style.left = (bw - width) / 2;
  style.top = (bh - height) / 2;
  frameDialog.src = url;
}

function closeDialog( value )
{
  returnValue = value;
  window.close( );
  return false;
}

function closeFrameDialog()
{
  document.body.removeChild(frameDialogDiv);
  frameDialogDiv = null;
  enablePage();
}

function hookFrameDialog(t)
{
  var b = t.contentWindow.document.body;
  b.onmousedown = function(event){ moveFrameDialog(FD_DOWN, t); }
  b.onmouseup = function(event){ moveFrameDialog(FD_UP, t); }
  b.onmousemove= function(event) { moveFrameDialog(FD_MOVE, t); }
}

function showTimeoutMessage( )
{
  var rc = showDialog( omxContextPath + '/core/alert/controller/display.x2c?MESSAGE=SESSION_TIMEOUT_MESSAGE&TYPE=warning&CLOSE_TIMEOUT=60000', 350, 150 );
  if( rc == 'timeout' ){
    disablePageLeaveMessage( );
    submitAction( 'Log Out', omxContextPath + '/core/login/controller/logout.x2c?FROM=timeout' );
  }
  else
    pageActivity( );
}

function timeoutCallback( )
{
  var d = new Date( );
  if( _pageActivity ){
    _pageActivity = 0;
    _lastActivity = d;
  }
  else if( _lastActivity.getTime( ) < d.getTime( ) - _pageTimeout * 1000 + 90000 )
    showTimeoutMessage( );
  setTimeout( timeoutCallback, 5000 );
}

function getPageTimeoutCallback( http )
{
  var xml = http.getXML( );
  if( isErrorResponse( xml ) )
    return;
  _pageTimeout = getValueAttribute( getChild( "SESSION_TIMEOUT", xml.firstChild ) );
}

function getPageTimeout( )
{
  var http = new HTTP( );
  var r = http.request( "getSessionTimeout", "HTTP_SESSION", getPageTimeoutCallback );
}

function enablePageTimeout( )
{
  var tables = document.getElementsByTagName( "TABLE" );
  var tbl = null;
  var l = tables.length;
  for( var i = 1; i < l; ++i ){
    tbl = tables[ i ];
    if( isOuterContainer( tbl ) )
      break;
  }
  tbl.onmousemove = pageActivity;
  tbl.onkeydown = pageActivity;
  getPageTimeout( );
  setTimeout( timeoutCallback, 5000 );
}

var _showPageLeaveMessage = true;

function setLeavePageMessage( msg, conditionFunc )
{
  document.body.onbeforeunload = function onUnload( ){ 
    if( _showPageLeaveMessage && conditionFunc( ) ){
      event.returnValue = msg;
      if( i2uiCurrentTab )
        i2uiToggleTab( i2uiCurrentTabset, '', i2uiCurrentTab );
    }
    else
      event.cancelBubble = true;
  }
}

function enablePageLeaveMessage( )
{
  _showPageLeaveMessage = true;
}

function disablePageLeaveMessage( )
{
  var r = _showPageLeaveMessage;
  _showPageLeaveMessage = false;
  return r;
}

function setButtonAttributeById( id, attribute, value )
{
  var btn = document.getElementById( id );
  btn[ attribute ] = value;
}

function submitAction( button, action, sysId )
{
  disableButtons( );
  var f = document.form;
  if( action == null )
    action = 'view.x2ps';
  var old;
  var sid = f.SYS_ID;
  if( sysId ){
    old = sid.value;
    sid.value = sysId;
  }
  f.BUTTON_ID.value = button;
  f.action = action;
  f.submit( );
  if( sysId )
    sid.value = old;
  return false;
}

function getAction( action )
{
  disableButtons( );
  var l = window.location;
  l.href = action;
}

function int( v )
{
  return parseInt( v );
}

function double( v )
{
  return parseFloat( v );
}

function ceiling( v )
{
  return Math.ceil( toDouble( v ) );
}

function floor( v )
{
  return Math.floor( toDouble( v ) );
}

function toDouble( v )
{
  var t = typeof( v );
  if( t == 'number' )
    return v;
  if( t != 'string' )
    v = '' + v;
  v = v.replace(page_groupingReplace, '');
  if(page_decimalSeparator != '.')
    v = v.replace(page_decimalSeparator, '.');
  return parseFloat(v);
}

function minDouble( x, y )
{
  x = toDouble( x );
  y = toDouble( y );
  if( x > y )
    return y;
  return x;
}

function maxDouble( x, y )
{
  x = toDouble( x );
  y = toDouble( y );
  if( x > y )
    return x;
  return y;
}

function modFunction( x, y )
{
  return toDouble( x ) % toDouble( y );
}

function DecimalFormat( s, d )
{
  if( s == null || s == '' )
    s = page_numberFormat;
  else
    d = -1;
  var t = s.indexOf( '.' ) + 1;
  var l = s.length;
  if( t > 0 ){
    var x = t;
    if(d < 0){
      while( x < l && s.charAt( x ) == '0' )
        ++x;
      this.minDecimals = x - t;
      x = l - t;
      this.maxDecimals = x;
    }
    else{
      this.minDecimals = d;
      this.maxDecimals = d;
      x = d;
    }
    var a = '0.';
    while( --x >= 0 )
      a += '0';
    a += '5';
    this.roundAdd = parseFloat( a );
    l = t - 1;
  }
  else{
    this.roundAdd = 0.5;
    this.minDecimals = 0;
    this.maxDecimals = 0;
  }
  var x = l;
  var t = 0;
  var f = true;
  var g = -1;
  while( --x >= 0 ){
    var c = s.charAt( x );
    if( c == '0' ){
      if( f )
        ++t;
    }
    else if( c == '#' )
      f = false;
    else if( c == ',' ){
        g = l - x - 1;
        break;
    }
    else
      break;
  }
  if( x > 0 )
    this.prefix = s.substring( 0, x );
  this.grouping = g;
}

DecimalFormat.prototype.format = function( s )
{
  var v = toDouble( s );
  if( isNaN( v ) )
    return formatNaN( v );
  if( !isFinite( v ) )
    return formatInfinity( v );
  if( v < 0 )
    v -= this.roundAdd;
  else
    v += this.roundAdd;
  s = v.toString();
  var i = s.indexOf( '.' );
  if( i < 0 )
    i = s.length;
  else{
    var l = i + this.maxDecimals;
    var e = i + this.minDecimals;
    while( l > e && s.charAt( l ) == '0' )
      --l;
    if( l == i )
      s = s.substring( 0, l );
    else
      s = s.substring( 0, l + 1 );
    if(page_decimalSeparator != '.')
      s = s.replace('.', page_decimalSeparator);
  }
  var group = this.grouping;
  if(group < 0)
    return s;
  var g = group;
  while(g < i){
    var t = i++ - g;
    s = s.substring( 0, t ) + page_groupingSeparator + s.substring( t );
    g += group + 1;
  }
  return s;
}

function formatNaN( v )
{
  return 'NaN';
}

function formatInfinity( v )
{
  return v > 0 ? 'Infinity' : '-Infinity';
}

function formatNumber( n, f, d )
{
  if(typeof d == 'undefined')
    d = -1;
  else
    d = parseInt(d);
  var fs = '_fs' + d + '_' + f;
  var formatter = this[ fs ];
  if( formatter == null ){
    formatter = new DecimalFormat( f, d );
    this[ fs ] = formatter;
  }
  return formatter.format( n );
}

function strlen( v )
{
  if( v == null )
    return 0;
  if( typeof( v ) == 'string' )
    return v.length;
  v = '' + v;
  return v.length;
}

function stringToDate( date )
{
  var i = date.indexOf( ' ' );
  if( i > 0 )
    date = date.substring( 0, i );
  return getDateFromFormat( date + ' 00:00:00', 'M/d/yyyy HH:mm:ss' )
}

function incrDateByDays( date, days )
{
  var d = stringToDate( date ) + days * 24 * 60 * 60 * 1000 + 2 * 60 * 60 * 1000;
  return formatDate( _formatDate( new Date( d ), 'xs:date' ), 'MM/dd/yyyy' );
}

function dateCompare( x, y )
{
  x = stringToDate( x );
  y = stringToDate( y );
  if( x > y )
    return 1;
  else if( y > x )
    return -1;
  return 0;
}

function isEmptyValue( s )
{
  return s == null || typeof( s ) == 'string' && s == '' || s == '&nbsp;' || typeof( s ) == 'number' && isNaN( s );
}

function addValueRange( array, name, from, to, postfix )
{
  if( postfix )
    name += postfix;
  var f = eval( 'get_' + name + '_WithIndex' );
  var sum = 0;
  while( from < to ){
    sum += toDouble( f( from ) );
    ++from;
  }
  return sum;
}

function averageValue( array, name, from, to, count, postfix )
{
  if( postfix )
    name += postfix;
  var f = eval( 'get_' + name + '_WithIndex' );
  var c = 0;
  var sum = 0;
  while( from < to && count > 0 ){
    var v = f( from );
    if( !isNaN( v ) ){
      sum += toDouble( v );
      ++c;
    }
    ++from;
    --count;
  }
  if( c > 0 )
    return sum / c;
  return 'x' - 0;
}

function isValidValue( )
{
  var l = arguments.length;
  var v = arguments[ 0 ];
  for( var i = 1; i < l; ++i )
    if( arguments[ i ] == v )
      return true;
  return false;
}

function getFirstChild( xml )
{
  return xml.firstChild;
}

function getChild( name, xml )
{
  var children = xml.childNodes;
  var l = children.length;
  for( var i = 0; i < l; ++i ){
    var c = children[ i ];
    if( c.nodeName == name )
      return c;
  }
  return null;
}

function getValueAttribute( xml )
{
  var a = xml.attributes;
  var l = a.length;
  while( --l >= 0 ){
    var x = a[ l ];
    if( x.name == 'Value' )
      return x.value;
  }
  return null;
}

function getAttribute( name, xml )
{
  var a = xml.attributes;
  if( a ){
    var l = a.length;
    while( --l >= 0 ){
      var x = a[ l ];
      if( x.name == name )
        return x.value;
    }
  }
  return null;
}

function isErrorResponse( xml )
{
  var c = xml.firstChild;
  return c == null || getAttribute( 'Status', c ) == 'Error';
}

function addChangeHighlight( name, index )
{
  var remove;
  if( index >= 0 ){
    var s = eval( 'set_' + name + '_StyleWithIndex' );
    s( index, 'backgroundColor', 'orange' );
    remove = 'removeChangeHighlight( \'' + name + '\', ' + index + ' )';
  }
  else{
    var s = eval( 'set_' + name + '_Style' );
    s( 'backgroundColor', 'orange' );
    remove = 'removeChangeHighlight( \'' + name + '\' )';
  }
  setTimeout( remove, 15000 );
}

function removeChangeHighlight( name, index )
{
  if( index >= 0 ){
    var s = eval( 'set_' + name + '_StyleWithIndex' );
    s( index, 'backgroundColor', null );
  }
  else{
    var s = eval( 'set_' + name + '_Style' );
    s( 'backgroundColor', null );
  }
}

function updateNewValues( xml, postfixCallback, highlight )
{
  xml = xml.firstChild;
  var children = xml.childNodes;
  var l = children.length;
  var i = 0;
  var postfix = '';
  var changed = false;
  while( i < l ){
    var child = children[ i++ ];
    var name = child.nodeName;
    var value = getValueAttribute( child );
    if( name == 'UPDATE_NBR' ){
      var set = eval( 'set_UPDATE_NBR' + postfix );
      set( value );
    }
    else if( name == 'UPDATE_ID' ){
      if( postfixCallback )
        postfix = postfixCallback( value );
    }
    else if( name == 'UPDATE_ARRAY' ){
      var data = child.childNodes;
      var dl = data.length;
      for( var j = 0; j < dl; ++j ){
        var values = data[ j ].childNodes;
        var vl = values.length;
        for( var x = 0; x < vl; ++x ){
          child = values[ x ];
          name = child.nodeName;
          value = getValueAttribute( child );
          var get = eval( 'get_' + name + postfix + '_WithIndex' );
          var set = eval( 'set_' + name + postfix + '_WithIndex' );
          var old = get( j );
          set( j, value );
          if( highlight && old != get( j ) ){
            addChangeHighlight( name + postfix, j );
            changed = true;
          }
        }
      }
    }
    else{
      var get = eval( 'get_' + name + postfix );
      var set = eval( 'set_' + name + postfix );
      var old = get( );
      set( value );
      if( highlight && old != get( ) ){
        addChangeHighlight( name + postfix );
        changed = true;
      }
    }
  }
  return changed;
}

function hideTableColumn( tbl, i )
{
  tbl.children[ 0 ].children[ i ].style.display = 'none';
}

function showTableColumn( tbl, i )
{
  tbl.children[ 0 ].children[ i ].style.display = '';
}

var _configuredTableId;
var _configuredTableConfigurationId;
var _configuredTablePostfixed;
var _configuredTablePostfixFlag = new function f( ) { };

function getConfiguredTableId( )
{
  return _configuredTableId;
}

function getConfiguredTableConfigurationId( )
{
  return _configuredTableConfigurationId;
}

function getConfiguredTableConfiguration( )
{
  return eval( 'tableConfiguration_' + _configuredTableId + '( )' );
}

function isConfiguredTablePostfixed( )
{
  return _configuredTablePostfixed;
}

function setConfiguredTablePostfixed( tableId, flag )
{
  _configuredTablePostfixFlag[ tableId ] = flag;
}

var _configureTableButtonIds = new Object( );

function setConfigureTableButtonId( tableId, buttonId )
{
  _configureTableButtonIds[ tableId ] = buttonId;
}

function ui_sortTableV2(tableId)
{
  _configuredTableId = tableId;
  var x = showDialog( omxContextPath + '/start.x2ps?SERVICE_NAME=PGL&POPUP=true&START_WORKFLOW=MultiSort', 450, 200 );
  var sid = document.form.SYS_ID;
  if( x == 'submit' ) 
  {
    if( sid != null )
      setTimeout( function (){ submitAction( 'SYS_RELOAD' ); }, 1 );
    else
      history.go(0);
  }
}

function getSortableCols()
{
  if(_configuredTableId != null)
  {
    return eval( 'sortCols_' + _configuredTableId + '( )' );
  }
}

function ui_configureTableV2( tableId, buttonId )
{
  if( buttonId == null )
    buttonId = _configureTableButtonIds[ tableId ];
  if( buttonId == null )
    buttonId = 'SYS_RELOAD';
  _configuredTableId = tableId;
  _configuredTableConfigurationId = eval( 'tableConfigurationId_' + tableId + '( )' );
  _configuredTablePostfixed = _configuredTablePostfixFlag[ tableId ] == true;
  var configuredTableTitle = eval( 'tableConfigurationTitle_' + tableId + '( )' );
  var d = new Date( );
  var args = new Array();
  args[0] = ['SERVICE_NAME', 'PGL'];
  args[1] = ['POPUP', 'true'];
  args[2] = ['START_WORKFLOW', 'ConfigureTable'];
  args[3] = ['TABLE_TITLE', configuredTableTitle];
  args[4] = ['DUMMY', d.getTime( )];
  var url = createEncodedUrl(omxContextPath+"/start.x2ps", args);
  var x = showDialog( url, 750, 400 );
  var sid = document.form.SYS_ID;
  if( x == 'submit' || x == 'restore' ) 
  {
    if( sid != null )
      setTimeout( function (){ submitAction( buttonId ); }, 1 );
    else
      history.go(0);
  }
}

function getInvertTableHeaderWidthById( tableId )
{
  var tbl = document.getElementById( tableId + '_syncslave' );
  return tbl.offsetWidth;
}

function setInvertTableHeaderWidthById( tableId, width )
{
  var tbl = document.getElementById( tableId + '_syncslave' );
  tbl.style.width = width;
}

function getInvertTableDataWidthById( tableId )
{
  var tbl = document.getElementById( tableId + '_syncmaster' );
  return tbl.offsetWidth;
}

function setInvertTableDataWidthById( tableId, width )
{
  var tbl = document.getElementById( tableId + '_syncmaster' );
  tbl.style.width = width;
}

function setInvertScroller( id, func )
{
  var s = getInvertScroller( id );
  s.onscroll = func;
}

function getInvertScroller( id )
{
  if( false && isV2( ) )
    return document.getElementById( id + '_div' );
  return document.getElementById( id + '_syncmaster_scroller' );
}

function getScrollerTableId( scroller )
{
  var id = scroller.id;
  return id.substring( 0, id.length - 20 );
}

function invertScroll( scroller, left, top )
{
  if( top >= 0 )
    scroller.scrollTop = top;
  else
    top = scroller.scrollTop;
  if( left >= 0 )
    scroller.scrollLeft = left;
  else
    left = scroller.scrollLeft;
  if( false && isV2( ) )
    return;
  var id = getScrollerTableId( scroller );
  var x = document.getElementById( id + '_syncmaster_header_scroller' );
  x.scrollTop = top;
  x.scrollLeft = left;
  x = document.getElementById( id + '_syncslave_scroller' );
  x.scrollTop = top;
}

function invertScrollById( id )
{
  invertScroll( getInvertScroller( id ) );
}

function getScrollerLeft( scroller )
{
  return scroller.scrollLeft;
}

function getScrollerTop( scroller )
{
  return scroller.scrollTop;
}

function getContentToggler( tableId )
{
  if( isV2( ) )
    return document.getElementById( tableId ).rows[ 0 ].cells[ 0 ].children[ 0 ];
  return document.getElementById( tableId ).rows[ 0 ].cells[ 0 ].children[ 0 ].rows[ 0 ].cells[ 0 ].children[ 0 ];
}

function setCollapseTogglerByTableId( tableId, func )
{
  var img = getContentToggler( tableId );
  img.onclick = func;
}

function setCollapseToggler( obj, func )
{
  obj.onclick = func;
}

function isCollapsed( tbl )
{
  return tbl.rows[ 1 ].parentNode.style.display == 'none';
}

function toggleContent( obj )
{
  i2uiToggleContent( obj, 2 );
  var t = getContentTogglerTable( obj );
  return isCollapsed( t );
}

function getContentTogglerTable( obj )
{
  return obj.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
}

var _toggleCollapsableRowsCallback = true;

function toggleCollapsableRowsCallback( tableId, imgPostfix )
{
}

function toggleCollapsableRowsV2( img )
{
  var td = img.parentNode;
  var skip = td.skip;
  var rc = td.collapseRowCount;
  var tr = td.parentNode;
  var src;
  var hidden = td.children[ 0 ];
  var change;
  var state = tr.collapsed;
  var norgieChar;
  
  if( state == 'true' ){
    tr.collapsed = 'false';
    hidden.value = 'false';
    src = 'minus_norgie.gif';
    norgieChar = '-';
    change = 1;
  }
  else{
    tr.collapsed = 'true';
    hidden.value = 'true';
    src = 'plus_norgie.gif';
    norgieChar = '+';
    change = -1;
  }

  var s = img.src;
  
  if (s != null)
  {
    var i = s.lastIndexOf( '/' );
    if( i < 0 )
      img.src = src;
    else
      img.src = s.substring( 0, i + 1 ) + src;
  }
  else // 1/25/2006 WJD - Assume degraded mode and 'img' is an anchor tag
  {
    img.innerText = norgieChar;
  }
    
  var tbl = tr.parentNode.parentNode;
  var mtbl = null;
  var rows = tbl.rows;
  var id = tbl.id;
  var i = id.indexOf( '_syncslave_data' );
  var mrows = null;
  var drows = null;
  
  if( i > 0 ){
    id = id.substring( 0, i );
    mtbl = document.getElementById( id + '_syncmaster_data' );
    mrows = mtbl.rows;
  }
  else{
    i = id.indexOf( '_syncslave' );
    if( i > 0 ){
      id = id.substring( 0, i );
      mrows = document.getElementById( id + '_syncmaster' ).rows;
    }
    else
    {
      //added for freeze table
      i = id.indexOf( '_data' );
      if( i > 0 ){
        id = id.substring( 0, i );
        dragTableId = id + "_dragger";
        if ( document.getElementById( dragTableId + '_data' ) != null )
        {
          drows = document.getElementById( dragTableId + '_data' ).rows;
        }
        if ( document.getElementById( id + '_syncmaster_data' ) != null )
        {
          mrows = document.getElementById( id + '_syncmaster_data' ).rows;
        }
      }
    }
  }
  
  var trIndex = tr.rowIndex;
  if (skip > 1)
  {
    trIndex += skip - 1;
  }

  while( --rc >= 0 ){
    var r = rows[ ++trIndex ];
    var l = r.lvl -= change;
    var display = l > 0 ? 'none' : '';
    r.style.display = display;
    if( mrows )
      mrows[ trIndex ].style.display = display;
    if( drows )
      drows[ trIndex ].style.display = display;
  }
  if( _toggleCollapsableRowsCallback ){
    _toggleCollapsableRowsCallback = false;
    toggleCollapsableRowsCallback( id, img.id.substring( id.length ) );
    _toggleCollapsableRowsCallback = true;
  }
  if( mtbl )
    alignRowHeights( mtbl, tbl );
  onResize( );
}

function toggleCollapsableRowsByImgId( id )
{
  toggleCollapsableRowsV2( document.getElementById( id ) );
}

function toggleCollapsableRowsByImgName( name )
{
  var treeCellImgs = document.getElementsByName( name );
  for (k = 0; k < treeCellImgs.length; k++)
  {
    toggleCollapsableRowsV2(treeCellImgs[k]);
  }
}

function getDropdownById( id )
{
  return document.getElementById( id );
}

function dropdownEmptyOption( )
{
  return new Option( '                                                       ', 'EMPTY_DROPDOWN_OPTION_KEY' );
}

function dropdownFixWidth( dd )
{
  var x = dd.options;
  x[ x.length ] = dropdownEmptyOption( );
  setTimeout( function f( ){ x[x.length-1] = null; }, 0 );
}

// WJD 8.25.2008 - Probably not needed anymore
function dropdownDeleteEmpty( dd )
{
  var e = dropdownEmptyOption( );
  var x = dd.options;
  var l = x.length;
  while( --l >= 0 ){
    if( x[ l ].value == e.value )
      x[ l ] = null;
  }
}

function dropdownAddOption( dd, id, text )
{
  var x = dd.options;
  var y = new Option( text, id );
  x[ x.length ] = y;
  return y;
}

function dropdownCopyAll( from, to, attr, condf )
{
  var fl = from.length;
  var tl = to.length;
  if( condf ){
    var fl = from.length;
    var tl = to.length;
    var l = 0;
    for( var i = 0; i < fl; ++i ){
      var f = from[ i ];
      if( condf( f ) ){
        var o = new Option( f.text, f.value );
        copyOptionAttributes( o, f, attr );
        to[ tl ] = o;
        to[ tl++ ].selected = true;
        from[ i ] = null;
        --i;
        --fl;
      }
    }
  }
  else{
    for( var i = 0; i < fl; ++i ){
      var f = from[ i ];
      var o = new Option( f.text, f.value );
      copyOptionAttributes( o, f, attr );
      to[ tl ] = o
      to[ tl++ ].selected = true;
    }
    from.length = 0;
  }
}

function dropdownCopySelected( from, to )
{
  from = from.options;
  to = to.options;
  var fl = from.length;
  var tl = to.length;
  for( var i = 0; i < fl; ++i ){
    var f = from[ i ];
    if( f.selected ){
      to[ tl ] = new Option( f.text, f.value );
      to[ tl++ ].selected = true;
    }
  }
  dropdownDeleteSelected( from );
}

function dropdownCopySelectedBlocks( from, to, attr, condf )
{
  from = from.options;
  to = to.options;
  var fl = from.length;
  var tl = to.length;
  var copy = false;
  for( var i = 0; i < fl; ++i ){
    var f = from[ i ];
    if( condf( f ) ){
      if( f.selected ){
        var o = new Option( f.text, f.value );
        copyOptionAttributes( o, f, attr );
        to[ tl ] = o;
        to[ tl++ ].selected = true;
        copy = true;
      }
      else
        copy = false;
    }
    else if( copy ){
      var o = new Option( f.text, f.value );
      copyOptionAttributes( o, f, attr );
      to[ tl++ ] = o;
    }
  }
  dropdownDeleteSelectedBlocks( from, condf );
}

function dropdownDeleteSelected( dd )
{
  var x = dd.options;
  var l = x.length;
  while( --l >= 0 )
    if( x[ l ].selected )
      x[ l ] = null;
}

function dropdownDeleteSelectedBlocks( dd, condf )
{
  var x = dd.options;
  var l = x.length;
  var i = 0;
  var remove = false;
  while( i < l ){
    var o = x[ i ];
    if( condf( o ) ){
      if( x[ i ].selected ){
        x[ i ] = null;
        remove = true;
        --l;
      }
      else{
        remove = false;
        ++i;
      }
    }
    else if( remove ){
      x[ i ] = null;
      --l;
    }
    else
      ++i;
  }
}

function dropdownClearAll( dd )
{
  var x = dd.options;
  var l = x.length;
  while( --l >= 0 )
    x[ l ].selected = false;
}

function dropdownAnySelected( dd, condf )
{
  var x = dd.options;
  var l = x.length;
  while( --l >= 0 ){
    var o = x[ l ];
    if( o.selected && ( condf == null || condf( o ) ) )
      return true;
  }
  return false;
}

function copyOptionStyle( to, from )
{
  var fs = from.style;
  var ts = to.style;
  var t = fs.color;
  if( t )
    ts.color = t
  t = from.backgroundColor;
  if( t )
    ts.backgroundColor = t;
}

function copyOptionAttributes( to, from, attr )
{
  if( attr ){
    if( typeof( attr ) == 'object' ){
      var c = attr.length;
      while( --c >= 0 ){
        var z = attr[ c ];
        to[ z ] = from[ z ];
      }
    }
    else 
      to[ attr ] = from[ attr ];
  }
}

function dropdownMoveUp( dd, attr, condf )
{
  var v = dd.options;
  var l = v.length;
  var e = 0;
  var t = null;
  for( var i = 0; i <= l; ++i ){
    var x = i < l ? v[ i ] : null;
    if( x && x.selected && ( condf == null || condf( x ) ) ){
      var p = i - 1;
      var o = new Option( x.text, x.value );
      o.selected = true;
      copyOptionStyle( o, x );
      copyOptionAttributes( o, x, attr );
      v[ p ] = o;
    }
    else{
      if( t && e < i ){
        var o = new Option( t.text, t.value );
        copyOptionStyle( o, t );
        copyOptionAttributes( o, t, attr );
        v[ i - 1 ] = o;
      }
      t = x;
      e = i + 1;
    }
  }
}

function dropdownMoveDown( dd, attr, condf )
{
  var v = dd.options;
  var l = v.length;
  var e = 0;
  var t = null;
  while( l-- >= 0 ){
    var x = l < 0 ? null : v[ l ];
    if( x && x.selected && ( condf == null || condf( x ) ) ){
      var p = l + 1;
      var o = new Option( x.text, x.value );
      copyOptionStyle( o, x );
      copyOptionAttributes( o, x, attr );
      o.selected = true;
      v[ p ] = o;
    }
    else{
      if( t && e > l ){
        var o = new Option( t.text, t.value );
        copyOptionStyle( o, t );
        copyOptionAttributes( o, t, attr );
        v[ l + 1 ] = o;
      }
      t = x;
      e = l - 1;
    }
  }
}

function dropdownMoveUpBlocks( dd, attr, condf )
{
  var v = dd.options;
  var l = v.length;
  var array = new Array( );
  var from = 0;
  var to = 0;
  var upto = 0;
  var move = false;
  for( var i = 0; i < l; ++i ){
    var x = v[ i ];
    var bl = condf( x );
    if( bl <= 0 ){
      if( bl == 0 && x.selected ){
        array[ to++ ] = x;
        move = true;
      }
      else{
        while( from < upto )
          array[ to++ ] = v[ from++ ];
        if( move )
          from = i;
        move = false;
        upto = i + 1;
      }
    }
    else if( move )
      array[ to++ ] = x;
    else
      ++upto;
  }
  while( from < l )
    array[ to++ ] = v[ from++ ];
  for( var i = 0; i < l; ++i ){
    var t = array[ i ];
    var o = new Option( t.text, t.value );
    copyOptionStyle( o, t );
    copyOptionAttributes( o, t, attr );
    if( t.selected )
      o.selected = true;
    v[ i ] = o;
  }
}

function dropdownMoveDownBlocks( dd, attr, condf )
{
  var v = dd.options;
  var l = v.length;
  var array = new Array( );
  var from = 0;
  var to = 0;
  var upto = 0;
  var move = true;
  for( var i = 0; i < l; ++i ){
    var x = v[ i ];
    var bl = condf( x );
    if( bl <= 0 ){
      if( bl == 0 && x.selected ){
        move = false;
        upto = i + 1;
      }
      else{
        if( move ){
          while( from < upto )
            array[ to++ ] = v[ from++ ];
          ++from;
        }
        array[ to++ ] = x;
        move = true;
      }
    }
    else if( move ){
      if( from >= upto )
        ++from;
      array[ to++ ] = x;
    }
    else
      ++upto;
  }
  while( from < l )
    array[ to++ ] = v[ from++ ];
  for( var i = 0; i < l; ++i ){
    var t = array[ i ];
    var o = new Option( t.text, t.value );
    copyOptionStyle( o, t );
    copyOptionAttributes( o, t, attr );
    if( t.selected )
      o.selected = true;
    v[ i ] = o;
  }
}

function dropdownSelectAll( dd )
{
  var x = dd.options;
  var l = x.length;
  while( --l >= 0 )
    x[ l ].selected = true;
}

function dropdownGetPostData( dd, all )
{
  var s = null;
  var id = dd.name;
  var x = dd.options;
  var l = x.length;
  for( var i = 0; i < l; ++i ){
    var t = x[ i ];
    if( all || t.selected ){
      t = id + '=' + t.value;
      if( s )
        s += '&' + t;
      else
        s = t;
    }
  }
  return s;
}

function dropdownSetSelection( dd, id )
{
  var x = dd.options;
  var l = x.length;
  while( --l >= 0 )
  {
    if(x[ l ].value == id)
    {
      x[ l ].selected = true;
    }
  }
    
}

function dropdownGetSelectedValue( id )
{
  var dd = getDropdownById(id);
  var i = dd.selectedIndex;
  return dd.options[i].value;
}

function toggleOnClickButtonImg( buttonImg, state, active, inactive )
{
  if( buttonImg.disabled ){
    if( state == 'disabled' )
      return;
    buttonImg.removeAttribute( 'disabled' );
    buttonImg.style.cursor = 'hand';
    var td = buttonImg.parentNode.parentNode;
    td.className = 'buttonText';
    td.id = 'buttonRegular';
    td.parentNode.parentNode.parentNode.className = 'buttonBorder';
    if( active )
      buttonImg.src = buttonImg.src.replace(/[/][^/]*$/, '/' + active );
  }
  else{
    if( state == 'enabled' )
      return;
    buttonImg.disabled = true;
    buttonImg.style.cursor = '';
    var td = buttonImg.parentNode.parentNode;
    td.className = 'buttonTextDisabled';
    td.id = 'buttonDisabled';
    td.parentNode.parentNode.parentNode.className = 'buttonBorderDisabled';
    if( inactive )
      buttonImg.src = buttonImg.src.replace(/[/][^/]*$/, '/' + inactive );
  }
}

var _callStepScrollCallback = true;

function stepScrollCallback( tableId, current, count, length, step )
{
}
 
function stepScrollButtonStates( tableId, current, count, length )
{
  var state = current > 0 ? 'enabled' : 'disabled';
  toggleOnClickButtonImg( document.getElementById( tableId + '_STEP_FIRST' ), state, 'page_to_beginning.gif', 'page_to_beginning_inact.gif' );
  toggleOnClickButtonImg( document.getElementById( tableId + '_STEP_PREV' ), state, 'previous_active.gif', 'previous_inactive.gif' );
  state = current < length - count ? 'enabled' : 'disabled';
  toggleOnClickButtonImg( document.getElementById( tableId + '_STEP_NEXT' ), state, 'next_active.gif', 'next_inactive.gif' );
  toggleOnClickButtonImg( document.getElementById( tableId + '_STEP_LAST' ), state, 'page_to_end.gif', 'page_to_end_inact.gif' );
}

function stepScrollFirst( tableId )
{
  var start = document.getElementById( tableId + '_STEP_START' );
  var count = document.getElementById( tableId + '_STEP_COUNT' );
  var tbl = document.getElementById( tableId + '_syncmaster' );
  var s = start.value;
  var c = parseInt( count.value );
  var l = tbl.rows[ 0 ].cells.length
  while( --s >= 0 ){
    hideTableColumn( tbl, s + c );
    showTableColumn( tbl, s );
  }
  start.value = 0;
  stepScrollButtonStates( tableId, s, c, l );
  if( _callStepScrollCallback ){
    _callStepScrollCallback = false;
    stepScrollCallback( tableId, s, c, l, 'first' );
    _callStepScrollCallback = true;
  }
}

function stepScrollPrevious( tableId )
{
  var start = document.getElementById( tableId + '_STEP_START' );
  var count = document.getElementById( tableId + '_STEP_COUNT' );
  var tbl = document.getElementById( tableId + '_syncmaster' );
  var s = --start.value;
  var c = parseInt( count.value );
  var l = tbl.rows[ 0 ].cells.length
  hideTableColumn( tbl, s + c );
  showTableColumn( tbl, s );
  stepScrollButtonStates( tableId, s, c, l );
  if( _callStepScrollCallback ){
    _callStepScrollCallback = false;
    stepScrollCallback( tableId, s, c, l, 'previous' );
    _callStepScrollCallback = true;
  }
}

function stepScrollNext( tableId )
{
  var start = document.getElementById( tableId + '_STEP_START' );
  var count = document.getElementById( tableId + '_STEP_COUNT' );
  var tbl = document.getElementById( tableId + '_syncmaster' );
  var s = ++start.value;
  var c = parseInt( count.value );
  var l = tbl.rows[ 0 ].cells.length
  hideTableColumn( tbl, s - 1 );
  showTableColumn( tbl, s + c - 1 );
  stepScrollButtonStates( tableId, s, c, l );
  if( _callStepScrollCallback ){
    _callStepScrollCallback = false;
    stepScrollCallback( tableId, s, c, l, 'next' );
    _callStepScrollCallback = true;
  }
}

function stepScrollLast( tableId )
{
  var start = document.getElementById( tableId + '_STEP_START' );
  var count = document.getElementById( tableId + '_STEP_COUNT' );
  var tbl = document.getElementById( tableId + '_syncmaster' );
  var s = parseInt( start.value );
  var c = parseInt( count.value );
  var l = tbl.rows[ 0 ].cells.length
  var e = l - c;
  while( s < e ){
    hideTableColumn( tbl, s );
    showTableColumn( tbl, s + c );
    ++s;
  }
  start.value = e;
  stepScrollButtonStates( tableId, s, c, l );
  if( _callStepScrollCallback ){
    _callStepScrollCallback = false;
    stepScrollCallback( tableId, s, c, l, 'last' );
    _callStepScrollCallback = true;
  }
}

function stepScroll( tableId, step )
{
  if( step == 'first' )
    stepScrollFirst( tableId );
  else if( step == 'previous' )
    stepScrollPrevious( tableId );
  else if( step == 'next' )
    stepScrollNext( tableId );
  else if( step == 'last' )
    stepScrollLast( tableId );
}

function getPageNameByWindow( w )
{
  return w.document.form.PAGE_NAME.value;
}

function getInvertTableInputIndex( t )
{
  var td = t.parentNode;
  var r = td.parentNode;
  var cells = r.cells;
  var l = cells.length;
  while( --l >= 0 )
    if( cells[ l ] == td )
      return l;
  return -1;
}

function getTableInputIndex( t )
{
  var tr = t.parentNode.parentNode;
  var tbl = tr.parentNode;
  while( tbl.tagName != 'TABLE' )
    tbl = tbl.parentNode;
  var offset = -1;
  var id = tbl.id;
  var x = id.length;
  if( x > 5 && id.substring( x - 5 ) == '_data' ){
    var p = tbl.parentNode;
    if( p && p.tagName == 'DIV' )
      offset = 0;
  }
  var rows = tbl.rows;
  var l = rows.length;
  while( --l >= 0 )
    if( rows[ l ] == tr )
      return l + offset;
  return -1;
}

function stripHTMLTags( v )
{
  if( typeof( v ) == 'string' && ( z = v.indexOf( '<' ) ) >= 0 ){
    for(;;){
      var e = v.indexOf( '>', z );
      if( z > 0 )
        v = v.substring( 0, z ) + ' ' + v.substring( e + 1 );
      else
        v = v.substring( e + 1 );
      z = v.indexOf( '<', z );
      if( z < 0 )
        break;
    }
  }
  return v;
}

function replaceValues( v, vs )
{
  if( typeof( v ) == 'string' ){
    var o = v;
    var l = vs.length;
    for( var i = 0; i < l; i += 2 )
      v = v.replace( vs[ i ], vs[ i + 1 ] );
    if( o == v )
      return v;
  }
  return v;
}

function isColumnExcluded( c, cs )
{
  if( cs == null )
    return false;
  var l = cs.length;
  while( --l >= 0 )
    if( c == cs[ l ] )
      return true;
  return false;
}

function tableToXML( tableId, postfix, rootName, nodeName, full, stripHTML, replaceVs )
{
  var c = getXMLCreator( );
  var xml = c.createElement( rootName );
  var rc = new Array( );
  var rcn = new Array( );
  var rcdn = new Array( );
  var rcti = new Array( );
  if( postfix == null )
    postfix = '';
  else
    postfix = '' + postfix;
  var pfLen = postfix.length;
  var configuration = eval( 'tableConfiguration_' + tableId + postfix )( );
  var l = configuration.length - 4;
  for( var i = 0; i < l; i += 4 ){
    var v = configuration[ i + 2 ];
    if( v >= 0 || full ){
      var n = configuration[ i ];
      rc[ rc.length ] = eval( 'get_' + n + '_WithIndex' );
      if( pfLen > 0 )
        n = n.substring( 0, n.length - pfLen );
      rcn[ rcn.length ] = n;
      rcdn[ rcdn.length ] = configuration[ i + 1 ];
      rcti[ rcti.length ] = eval( 'get_' + n + '_TableIndex' )( );
    }
  }
  var rcl = rc.length;
  var cid = eval( 'tableContainerId_' + tableId + postfix )( );
  var tdf = eval( 'getTD_' + cid );
  var l = eval( 'tableLength_' + cid )( );
  for( var i = 0; i < l; ++i ){
    var node = c.createElement( nodeName );
    xml.appendChild( node );
    for( var j = 0; j < rcl; ++j ){
      var n = c.createElement( rcn[ j ] );
      var dn = rcdn[ j ];
      if( stripHTML ){
        dn = dn.replace( /\[:/, '<' )
        dn = dn.replace( /:\]/, '>' )
        if( replaceVs )
          dn = replaceValues( dn, replaceVs );
        dn = stripHTMLTags( dn );
      }
      n.setAttribute( 'Name', dn );
      var v = rc[ j ]( i );
      if( replaceVs )
        v = replaceValues( v, replaceVs );
      if( stripHTML )
        v = stripHTMLTags( v );
      n.setAttribute( 'Value', isEmptyValue( v ) ? '' : v );
      var ti = rcti[ j ];
      if( ti >= 0 ){
        var td = tdf( ti, i );
        var st = td.style.cssText;
        if( st.length > 0 )
          n.setAttribute( 'Style', st );
      }
      node.appendChild( n );
    }
  }
  return xml;
}

function tableToXMLByRows( tableId, postfix, full, stripHTML, replaceVs, excludedColumns )
{
  var c = getXMLCreator( );
  var xml = c.createElement( 'ROWS' );
  if( postfix == null )
    postfix = '';
  else
    postfix = '' + postfix;
  var pfLen = postfix.length;
  var cid = eval( 'tableContainerId_' + tableId + postfix )( );
  var tdf = eval( 'getTD_' + cid );
  var tl = eval( 'tableLength_' + cid )( );
  var configuration = eval( 'tableConfiguration_' + tableId + postfix )( );
  var l = configuration.length - 4;
  for( var i = 0; i < l; i += 4 ){
    var v = configuration[ i + 2 ];
    if( v >= 0 || full ){
      var n = configuration[ i ];
      if( pfLen > 0 )
        n = n.substring( 0, n.length - pfLen );
      if( isColumnExcluded( n, excludedColumns ) )
        continue;
      var node = c.createElement( 'ROW' );
      xml.appendChild( node );
      node.setAttribute( 'Name', n );
      var dn = configuration[ i + 1 ];
      if( stripHTML ){
        dn = dn.replace( /\[:/, '<' )
        dn = dn.replace( /:\]/, '>' )
        if( replaceVs )
          dn = replaceValues( dn, replaceVs );
        dn = stripHTMLTags( dn );
      }
      node.setAttribute( 'DisplayText', dn );
      var df = eval( 'get_' + n + postfix + '_WithIndex' );
      var ti = eval( 'get_' + n + postfix + '_TableIndex' )( );
      for( var j = 0; j < tl; ++j ){
        var n = c.createElement( 'CELL' );
        node.appendChild( n );
        var v = df( j );
        if( replaceVs )
          v = replaceValues( v, replaceVs );
        if( stripHTML )
          v = stripHTMLTags( v );
        n.setAttribute( 'Value', isEmptyValue( v ) ? '' : v );
        if( ti >= 0 ){
          var td = tdf( ti, j );
          var st = td.style.cssText;
          if( st.length > 0 )
            n.setAttribute( 'Style', st );
        }
      }
    }
  }
  return xml;
}

function addToXML( ids, postfix, xml )
{
  var c = getXMLCreator( );
  if( postfix == null )
    postfix = '';
  else
    postfix = '' + postfix;
  if( typeof( ids ) == 'string' ){
    var n = c.createElement( ids );
    var v = eval( 'get_' + ids + postfix )( );
    n.setAttribute( 'Value', isEmptyValue( v ) ? '' : v );
    xml.appendChild( n );
  }
  else{
    var l = ids.length;
    for( var i = 0; i < l; ++i ){
      var t = ids[ i ];
      var n = c.createElement( t );
      var v = eval( 'get_' + t + postfix )( )
      n.setAttribute( 'Value', isEmptyValue( v ) ? '' : v );
      xml.appendChild( n );
    }
  }
}

function showExcelDocument( name, windowName )
{
  if( windowName == null )
    windowName = 'excel';
  var s = omxContextPath;
  if( name )
    s += '/' + name;
  else
    s += '/ExcelServlet';
  s += '?EXCEL_DOWNLOAD=TRUE&StreamName=cpm_xsl_transform_result';
  return window.open( s, windowName, 'menubar=yes, scrollbars=yes, resizable=yes' );
}

function VariableContainer( )
{
}

var _variables = new VariableContainer( );

function test_date_range(from,to,datatype, search_name)
{
  var x = document.getElementsByName(from);
  var y = document.getElementsByName(to);
  var fromDate = x[0].value;
  var toDate = y[0].value;

  var bValid = "true";
  
  //check for errors
  if (!isValid_field(from,null) || !isValid_field(to,null))
  {
    bValid = "false";
  }
  
  if ( bValid == "true" )
  {
    if (search_name == null)
    {
      search_name = 'search';
    }
    if( typeof(window['isValid_'+search_name]) == 'function' )
    {
      if ( validate_search(search_name) == false )
      {
        bValid = "false";
      }  
    }
  }
  
  if ( bValid == "true" )
  {
    if(toDate != null && trimString(toDate) != '' && fromDate != null && trimString(fromDate) != '')
    {
      var dateFormat = datatype == 'DateTime' ? datatype : null;

      var result = datecompare(fromDate, dateFormat, toDate, dateFormat);
      if(result > 0)
      {
        core_alert('From Date greater than To Date');
        setTimeout("updateToDate('" + to + "','" + x[0].value + "');", 1);
      }
    }
  }
  else
  {
    onResize();
    return;
  }
}

function updateToDate(to,newValue)
{
  var y = document.getElementsByName(to);
  y[0].value = newValue;
}

/* Use for number range validation */
function validRange(numericInput)
{
  var value = numericInput.value.trim() == "" ? null : toDouble(numericInput.value);
  var minValue = new Number(numericInput.minValue);
  var maxValue = new Number(numericInput.maxValue);

  var msg = null;
  if (value == null || value == "NaN")
  {
    msg = "No value present.";
    clearRangeError(numericInput);
  }
  else if (value < minValue)
  {
    fieldValidator = getRangeValidator(numericInput);
    msg = fieldValidator.minMessage;
    displayRangeError(numericInput, msg);
  }
  else if (value > maxValue)
  {
    fieldValidator = getRangeValidator(numericInput);
    msg = fieldValidator.maxMessage;
    displayRangeError(numericInput, msg);
  }
  else
  {
    // Clear error display
    clearRangeError(numericInput);
  }
}

function getRangeValidator(numericInput)
{
  var parent = numericInput.parentElement
  var childLen = parent.childNodes.length;
  var i;

  for(i = 0; i < childLen; i++)
  {

    fieldValidator =  parent.childNodes[i];
    if (fieldValidator.nodeName == 'SPAN')
    {
      if (fieldValidator.type == 'valid-range-validator')
      {
        return fieldValidator;
      }
    }
  }

  return null;
}

function displayRangeError(numericInput, msg)
{
  var fieldValidator = getRangeValidator(numericInput);

  if (fieldValidator != null)
  {
    fieldValidator.style.display = "";
    fieldValidator.style.visibility = "visible";

    fieldValidator.message = msg;
    
    var validatorImg = getRangeImg(fieldValidator);

    if (validatorImg != null)
    {
      validatorImg.alt = msg;
    }
  }
}

function getRangeImg(fieldValidator)
{
  var numFieldValidatorChildren = fieldValidator.childNodes.length;
  for (k=0; k<numFieldValidatorChildren; k++)
  {
    var validatorImg = fieldValidator.childNodes[k];
    if (validatorImg.nodeName == 'IMG')
    {
      return validatorImg;
    }
  }
  return null;
}

function clearRangeError(numericInput)
{
  var fieldValidator = getRangeValidator(numericInput);

  if (fieldValidator != null)
  {
    fieldValidator.style.visibility = "hidden";
  }
}

function trapExcelPivotChanges()
{
  var excelPivotsInPage = document.getElementsByName("excel_pivot_id");
  
  for (var i=0; i < excelPivotsInPage.length; i++)
  {
    var id = excelPivotsInPage[i].value;
    var result = i2uiExtractPivot(id);
    eval("form." + id + "_CHANGES.value = result;");
  }
  return;
}

function tabsetScrollerButtons( cells, lt, endScroll )
{
  var cl = cells.length;
  var begin = cells[ cl - 8 ].children[ 0 ].rows[ 0 ].cells[ 0 ].children[ 0 ].children[ 0 ];
  var left = cells[ cl - 6 ].children[ 0 ].rows[ 0 ].cells[ 0 ].children[ 0 ].children[ 0 ];
  var right = cells[ cl - 4 ].children[ 0 ].rows[ 0 ].cells[ 0 ].children[ 0 ].children[ 0 ];
  var end = cells[ cl - 2 ].children[ 0 ].rows[ 0 ].cells[ 0 ].children[ 0 ].children[ 0 ];
  if( lt > 0 ){
    begin.style.cursor = 'hand';
    left.style.cursor = 'hand';
    begin.src = omxContextPath + '/i2/images/page_to_beginning.gif';
    left.src = omxContextPath + '/i2/images/previous_active.gif';
  }
  else{
    begin.style.cursor = 'default';
    left.style.cursor = 'default';
    begin.src = omxContextPath + '/i2/images/page_to_beginning_inact.gif';
    left.src = omxContextPath + '/i2/images/previous_inactive.gif';
  }
  if( endScroll > 0 ){
    right.style.cursor = 'hand';
    end.style.cursor = 'hand';
    right.src = omxContextPath + '/i2/images/next_active.gif';
    end.src = omxContextPath + '/i2/images/page_to_end.gif';
  }
  else{
    right.style.cursor = 'default';
    end.style.cursor = 'default';
    right.src = omxContextPath + '/i2/images/next_inactive.gif';
    end.src = omxContextPath + '/i2/images/page_to_end_inact.gif';
  }
}

function resizeTabsetScroller( table, w )
{
  if ( isV1() )
    return;
    
  var parent = table.parentElement;
  var s = table.style;
  s.display = 'none';
  w = parent.clientWidth;
  s.display = '';
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
    if( ( j & 3 ) == 3 ){
      c.totalWidth = cw;
      cw = 0;
    }
  }
  while( ++j < cl )
    cells[ j ].style.display = 'none';
  if( filler.offsetWidth > 0 && table.offsetWidth <= w )
    return;
  for( var j = last + 1; j < cl; ++j )
    cells[ j ].style.display = '';
  var endScroll = 0;
  if( table.hasScrollerBegin == null ){
    for( var j = 0; j < last; j += 4 ){
      var c = cells[ j + 1 ];
      if( c.id == 'tabSelected' ){
        var e = j + 4;
        while( ( filler.offsetWidth == 0  || table.offsetWidth > w ) && last > e ){
          cells[ --last ].style.display = 'none';
          cells[ --last ].style.display = 'none';
          cells[ --last ].style.display = 'none';
          cells[ --last ].style.display = 'none';
          ++endScroll;
        }
        while( ( filler.offsetWidth == 0 || table.offsetWidth > w ) && cells.length > first + 4 ){
          cells[ first++ ].style.display = 'none';
          cells[ first++ ].style.display = 'none';
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
    cells[ first++ ].style.display = 'none';
    cells[ first++ ].style.display = 'none';
  }
  while( (filler.offsetWidth == 0 || table.offsetWidth > w) && last - 4 > 0 ){
    cells[ --last ].style.display = 'none';
    cells[ --last ].style.display = 'none';
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
      cells[ --last ].style.display = '';
      cells[ --last ].style.display = '';
    }
    table.scrollerBegin = ++last;
  }
  else{
    var first = table.scrollerBegin;
    while( first < last ){
      var c3 = cells[ first + 3 ];
      if( filler.offsetWidth < c3.totalWidth )
        break;
      cells[ first++ ].style.display = '';
      cells[ first++ ].style.display = '';
      cells[ first++ ].style.display = '';
      c3.style.display = '';
      ++first;
    }
    endScroll = ( last - first ) / 4;
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
      table.scrollerBegin -= 4;
      moveTabsetScroller( table );
    }
  }
  else if( dir == 'right' ){
    var es = table.endScroll;
    if( es > 0 ){
      table.scrollerBegin += 4;
      moveTabsetScroller( table );
    }
  }
  else if( dir == 'end' ){
    var es = table.endScroll;
    if( es > 0 )
      moveTabsetScroller( table, true );
  }
}

function i2uiToggleAllColumnsSelectionState(obj,tableid)
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
        if (checkboxes[i].id == tableid+"_COLUMN_SELECTOR" && checkboxes[i].disabled == false)
      
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

function flowChartDataToXml(input, data)
{
  var c = getXMLCreator();
  var layout = c.createElement('LAYOUT');
  var deleted = null;
  var nodes = data.nodes;
  var l = nodes.length;
  for(var i = 0; i < l; ++i){
    var n = nodes[i];
    var node = c.createElement('NODE');
    node.setAttribute('Name', n.id);
    var name = n.name;
    var x = n.x;
    var y = n.y;
    node.setAttribute('DisplayText', name);
    node.setAttribute('SymbolType', n.symbolType);
    node.setAttribute('X', x);
    node.setAttribute('Y', y);
    var hidden = n.hidden;
    if(hidden == 'true')
      node.setAttribute('Hidden', 'true');
    if(n.deleted){
      if(deleted == null)
        deleted = c.createElement('DELETED');
      deleted.appendChild(node);
    }
    else{
      if(n.old){
        var oldName = n.oldName;
        if(name != oldName)
          node.setAttribute('OldDisplayText', oldName);
        var oldX = n.oldX;
        if(x != oldX)
          node.setAttribute('OldX', oldX);
        var oldY = n.oldY;
        if(y != oldY)
          node.setAttribute('OldY', oldY);
        var oldHidden = n.oldHidden;
        if(oldHidden != hidden)
          node.setAttribute('OldHidden', oldHidden);
      }
      else
        node.setAttribute('New', 'true');
      layout.appendChild(node);
    }
  }
  var connectors = data.connectors;
  l = connectors.length;
  for(var i = 0; i < l; ++i){
    var x = connectors[i];
    var connector = c.createElement('CONNECTOR');
    var name = x.name;
    var connectorType = x.connectorType;
    connector.setAttribute('FromNode', x.from);
    connector.setAttribute('ToNode', x.to);
    connector.setAttribute('DisplayText', name);
    connector.setAttribute('ConnectorType', connectorType);
    if(x.deleted){
      if(deleted == null)
        deleted = c.createElement('DELETED');
      deleted.appendChild(connector);
    }
    else{
      if(x.old){
        var oldName = x.oldName;
        if(name != oldName)
          connector.setAttribute('OldDisplayText', oldName);
        var oldConnectorType = x.oldConnectorType;
        if(connectorType != oldConnectorType)
          connector.setAttribute('OldConnectorType', oldConnectorType);
      }
      else
        connector.setAttribute('New', 'true');
      layout.appendChild(connector);
    }
  }
  if(deleted)
    layout.appendChild(deleted);
  if(input)
    input.value = layout.xml;
  return layout;
}

/* Popup Menu Functions */
var pmiArr = new Array();
var pmiRowId = 0;
function setContextAndShowMenu(id, rowId, params)
{
  pmiRowId = rowId;
  var pmiDiv = document.getElementById(id + "_pmi");

  var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
  xmlDoc.async = "false";
  xmlDoc.loadXML(pmiDiv.innerHTML);

  if(xmlDoc != null)
  {
    var name = xmlDoc.documentElement.attributes[0].value;
    var pmi = xmlDoc.documentElement.childNodes;
    formMenu(name, pmi, params);
    i2uiShowMenu(name);
  }
}

function formMenu(name, pmi, params)
{
  var s = "<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\">";
  s += "<tr class=\"menuUnHighlighted\">";
  s += "<td class=\"menuShadow\">";
  s += "<table border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";

  var i = 0; 
  for(i=0; i<pmi.length; i++)
  {
    var p = pmi[i];
    var id = p.getAttribute("name");
    var url = p.getAttribute("url");
    var text = p.getAttribute("label");
    var divId = p.getAttribute("divId");

    var sub = p.childNodes;

    if(sub.length > 0)
    {
      formMenu(divId, sub, params);
    }
    else
    {
      pmiArr[id] = url;
    }

    s += "<tr class=\"menuUnhighlighted\">";
    s += "<td colspan=\"1\" nowrap=\"yes\" class=\"menuText\">";
    s += "<a onmouseover=\"i2uiHighlightMenuOption(this,'Highlighted','";
    s += id;
    s += "','";
    s += name;

    if(sub.length > 0)
    {
      s += "');i2uiShowSubMenu('" + divId + "', this, event);\" ";
    }
    else
    {
      s += "');\" onclick=\"i2uiHideMenu()\" ";
    }

    s += "onmouseout=\"i2uiHighlightMenuOption(this,'Unhighlighted','";
    s += id;
    s += "','";
    s += name;

    if(sub.length > 0)
    {
      s += "')\" href=\"javascript:doNothing();\">";
    }
    else
    {
      s += "')\" href=\"javascript:invokeMenu('";
      s += id + "'";
      var tid = p.getAttribute('targetId');
      if(tid)
        s += ", " + tid;
      if(params)
        s += ", '" + params + "'";
      s += ")\">";
    }

    s += "&nbsp;&nbsp;" + text + "&nbsp;&nbsp;";
    s += "</a>";

    if(sub.length > 0)
    {
      s += "&nbsp;&nbsp;";
      s += "<img src=\"" + i2uiImageDirectory + "/next_active.gif\"/>";
    }

    s += "</td></tr>";
  }

  s += "</table>";
  s += "</td>";
  s += "</tr>";
  s += "</table>";

  var o = document.getElementById(name);
  o.innerHTML = "";
  o.innerHTML = s;
}

function invokeMenu(id, targetId, params)
{
  var url = pmiArr[id];
  if(url == "")
  {
    document.form.POPUP_MENU_ROW_ID.value = pmiRowId;
    update(targetId, id, undefined, undefined, params);
  }
  else
  {
    //for popup window script
    var i = url.lastIndexOf('javascript:');
    if(i > -1)
    {
      var l = url.length;
      eval(url.substring(11,l));
    }
    else
    {
      document.location.href = pmiArr[id];
    }
  }
}

/* Transfer Boxes */

function registerTransferBox(id)
{
  transferBoxes[id] = id;
}

function selectAllTransferBoxes()
{
  for(var i in transferBoxes)
  {
    var available = getDropdownById( i + '_AVAILABLE' );
    var selected = getDropdownById( i + '_SELECTED' );
    dropdownSelectAll( available );
    dropdownSelectAll( selected );
  }
}

function tbToggleAll(id)
{
  var available = getDropdownById( id + '_AVAILABLE' );
  var selected = getDropdownById( id + '_SELECTED' );
  var a = available.options;
  var s = selected.options;

  var al = a.length;
  var addAllDisabled = al == 0;
  if( addAllDisabled )
    i2uiToggleButtonState( id + '_add_all','disabled' );
  else
    i2uiToggleButtonState( id + '_add_all','enabled' );
  var addSelectedDisabled = al == 0 || dropdownAnySelected( available ) == false;
  if( addSelectedDisabled )
    i2uiToggleButtonState( id + '_add_selected','disabled' );
  else
    i2uiToggleButtonState( id + '_add_selected','enabled' );

  var sl = s.length;          
  var removeAllDisabled = sl == 0;          
  if( removeAllDisabled )
    i2uiToggleButtonState( id + '_remove_all','disabled' );
  else
    i2uiToggleButtonState( id + '_remove_all','enabled' );
  var x = sl == 0 || dropdownAnySelected( selected ) == false;
  var y = x || dropdownAnySelected( selected, tbIsRequired );
  var removeSelectedDisabled = x || y;
  if( removeSelectedDisabled )
    i2uiToggleButtonState( id + '_remove_selected','disabled' );
  else
    i2uiToggleButtonState( id + '_remove_selected','enabled' );
    
  var upDisabled = x || s[ 0 ].selected;
  if( upDisabled )
    i2uiToggleButtonState( id + '_up','disabled' );
  else
    i2uiToggleButtonState( id + '_up','enabled' );
    
  var downDisabled = x || s[ s.length - 1 ].selected;
  if( downDisabled )
    i2uiToggleButtonState( id + '_down','disabled' );
  else
    i2uiToggleButtonState( id + '_down','enabled' );

  dropdownFixWidth( available );
  dropdownFixWidth( selected );
}

function tbAddAll(id)
{
  var available = getDropdownById( id + '_AVAILABLE' );
  var selected = getDropdownById( id + '_SELECTED' );
  var a = available.options;
  var al = a.length;
  var addAllDisabled = al == 0;
  if( addAllDisabled )
    return false;

  dropdownClearAll(available);
  dropdownClearAll(selected);
  dropdownCopyAll(available, selected, 'title');
  tbToggleAll(id);
}

function tbAddSelected(id)
{
  var available = getDropdownById( id + '_AVAILABLE' );
  var selected = getDropdownById( id + '_SELECTED' );
  var a = available.options;
  var al = a.length;
  var addSelectedDisabled = al == 0 || dropdownAnySelected( available ) == false;
  if( addSelectedDisabled )
    return false;
  dropdownCopySelectedBlocks(available, selected, ['required', 'title'], tbIsMainLevel);
  tbToggleAll(id);
}

function tbRemoveSelected(id)
{
  var available = getDropdownById( id + '_AVAILABLE' );
  var selected = getDropdownById( id + '_SELECTED' );
  var s = selected.options;
  var sl = s.length;          
  var x = sl == 0 || dropdownAnySelected( selected ) == false;
  var y = x || dropdownAnySelected( selected, tbIsRequired );
  var removeSelectedDisabled = x || y;
  if( removeSelectedDisabled )
    return false;

  dropdownCopySelectedBlocks(selected, available, ['required', 'title'], tbIsMainLevel);
  tbToggleAll(id);
}

function tbRemoveAll(id)
{
  var available = getDropdownById( id + '_AVAILABLE' );
  var selected = getDropdownById( id + '_SELECTED' );
  var s = selected.options;
  var sl = s.length;          
  var removeAllDisabled = sl == 0;          
  if( removeAllDisabled )
    return false;

  dropdownClearAll(available);
  dropdownClearAll(selected);
  dropdownCopyAll(selected, available, ['required', 'title'], tbNotRequired);
  tbToggleAll(id);
}

function tbSelectActive(id)
{
  var selected = getDropdownById( id + '_SELECTED' );
  dropdownClearAll( selected );
  tbToggleAll(id);
}

function tbSelectVisible(id)
{
  var available = getDropdownById( id + '_AVAILABLE' );
  dropdownClearAll( available );
  tbToggleAll(id);
}

function tbMoveUp(id)
{
  var selected = getDropdownById( id + '_SELECTED' );
  dropdownMoveUp( selected, 'required', tbIsBlockChild );
  dropdownMoveUpBlocks( selected, 'required', tbGetBlockLevel );
  tbToggleAll(id);
}

function tbMoveDown(id)
{
  var selected = getDropdownById( id + '_SELECTED' );
  dropdownMoveDown( selected, 'required', tbIsBlockChild );
  dropdownMoveDownBlocks( selected, 'required', tbGetBlockLevel );
  tbToggleAll(id);
}

function tbIsMainLevel( o )
{
  return true;
}

function tbGetBlockLevel( o )
{
  return 1;
}

function tbIsBlockChild( o )
{
  return true;
}

function tbIsRequired( o )
{
  var r = false;
  if(o.required)
    r = true;
  return r;
}

function tbNotRequired( o )
{
  if( tbIsRequired( o ) )
    return false;
  return true;
}

function addPostNode(parent, name, value, xc)
{
  var n = xc.createElement(name);
  parent.appendChild(n);
  if(value == null)
    return;
  n.setAttribute('Value', value);
}

function buildPostData()
{
  var all = document.all;
  var l = all.length;
  var xc = getXMLCreator();
  var postdata = xc.createElement('REQUEST');
  addPostNode(postdata, 'AJAX_UPDATE', 'true', xc);
  for(var i = 0; i < l; ++i)
  {
    var t = all[i];
    if(t.disabled)
      continue;
    var name = t.name;
    if(name){
      var tag = t.tagName;
      if(tag == 'INPUT'){
        var type = t.type;
        if(type == 'radio' || type == 'checkbox'){
          if(t.checked)
            addPostNode(postdata, name, t.value, xc);
        }
        else
          addPostNode(postdata, name, t.value, xc);
      }
      else if(tag == 'TEXTAREA')
        addPostNode(postdata, name, t.value, xc);
      else if(tag == 'SELECT'){
        var cid = t.containerId;
        var postAll = cid == 'step_available' || cid == 'step_selected';
        var options = t.options;
        var ol = options.length;
        for(var j = 0; j < ol; ++j){
          var o = options[j];
          if(postAll || o.selected){
            addPostNode(postdata, name, o.value, xc);
          }
        }
      }
    }
  }
  xmlData = buildPostXml();
  if (xmlData != null)
    postdata.appendChild(xmlData);
  return postdata.xml;
}

function getNodePath(v)
{
  return v.substring(2);
}

function buildPostXml()
{
  var xmlMap = new Object();
  buildXmlNodes(null, xmlMap, document.all, false, getXMLCreator());
  return xmlMap[xmlMap.root];
}

function buildXmlNodes(currentNode, xmlMap, all, recurse, xc)
{
  var l = all.length;
  var lastPivot = null;
  for(var i = 0; i < l; ++i)
  {
    var t = all[i];
    if(t.disabled)
      continue;
    var tag = t.tagName;
    if(tag == 'OBJECT'){
      if(t.component == 'workbook'){
        if(t.rawData == 'true'){
          var data = t.rawDataType == 'CSV' ? t.CSVData : t.XMLData;
          addNode(currentNode, name, data, data, null, xc);
          while(all[i + 1].tagName == '!')
            ++i;
          continue;
        }
        enablePage();
        var array = t.oldvalue;
        var clear = new Array();
        var w = t.Worksheets;
        var names = t.Names;
        var formulaCells = null;
        var nextFormula = 0;
        var wl = w.Count;
        var count = all[++i].text;
        i += parseInt(count.substring(5, count.length - 4));
        var offset = i + 2 * wl - 1;
        var next = 0;
        for(var j = 0; j < wl; ++j){
          var ws = w(j + 1);
          var wsn = ws.Name;
          var cells = ws.Cells;
          var range = ws.UsedRange;
          var cc = range.Columns.Count;
          var rc = range.Rows.Count;
          var added = null;
          var deleted = null;
          var deletedRows = new Object();
          var sheetPath = getNodePath(all[i + 2 * j + 1].data);
          var sheetNode = buildXml(sheetPath, xmlMap, xc);
          for(var r = 1; r <= rc; ++r){
            var addedRow = null;
            for(var c = 1; c <= cc; ++c){
              var cell = cells(r, c);
              if(typeof cell.Name == 'object'){
                var x = cell.Name.Index;
                while(++next < x){
                  clear[clear.length] = next;
                  var o = array[next];
                  if(o)
                    deleted = addDeletedCell(o, deletedRows, deleted, xc);
                }
                var o = array[x];
                if(o){
                  clear[clear.length] = x;
                  var node = buildXml(sheetPath + getNodePath(all[offset + 2 * x].data), xmlMap, xc);
                  var v = cell.Value;
                  if(typeof v != 'unknown'){
                    node.setAttribute('Value', v);
                    var ov = o.v;
                    if(typeof v == 'date'){
                      if('' + ov != '' + v)
                        node.setAttribute('OldValue', ov);
                    }
                    else if(ov != v)
                      node.setAttribute('OldValue', ov);
                  }
                  var of = o.f;
                  if(cell.HasFormula){
                    if(formulaCells == null){
                      var dom = new ActiveXObject( 'Microsoft.XMLDOM' );
                      dom.loadXML(t.XMLData);
                      formulaCells = dom.selectNodes("//ss:Cell[@ss:Formula]");
                    }
                    var f = formulaCells[nextFormula++].getAttribute('ss:Formula');
                    node.setAttribute('Formula', f);
                    if(of != f)
                      node.setAttribute('OldFormula', of);
                  }
                  else if(of)
                    node.setAttribute('OldFormula', of);
                  var a = cell.Address(true, true, xlR1C1);
                  node.setAttribute('Address', a);
                  var oa = o.a;
                  if(oa != a)
                    node.setAttribute('OldAddress', oa);
                  array[x] = null;
                  delete array[x];
                  continue;
                }
              }
              var v = cell.Value;
              if(typeof v == 'undefined')
                continue;
              if(added == null)
                added = xc.createElement('ADDED');
              if(addedRow == null){
                addedRow = xc.createElement('ROW');
                addedRow.setAttribute('Row', r);
                added.appendChild(addedRow);
              }
              var node = xc.createElement('CELL');
              node.setAttribute('Column', c);
              node.setAttribute('Address', cell.Address(true, true, xlR1C1));
              node.setAttribute('Value', cell.Value);
              addedRow.appendChild(node);
            }
          }
          var sheetData = array['sheet' + wsn];
          var last = sheetData.last;
          while(next < last){
            clear[clear.length] = ++next;
            var o = array[next];
            if(o)
              deleted = addDeletedCell(o, deletedRows, deleted, xc);
          }
          if(added)
            sheetNode.appendChild(added);
          if(deleted)
            sheetNode.appendChild(deleted);
        }
        var cl = clear.length;
        i += 2 * cl;
        while(--cl >= 0 )
          var name = names.Item(clear[cl]).Delete();
        t.oldvalue = null;
        i += 2 * wl;
      }
      else if(t.component == 'excel-pivot'){
        lastPivot = t;
        var popupInfo = t.popupInfo;
        if(currentNode && popupInfo){
          var pl = popupInfo.length;
          var pn = xc.createElement(t.id + 'PopUp');
          for(var pi = 0; pi < pl; ++pi){
            var info = popupInfo[pi];
            var pv = info[1];
            addNode(pn, info[0], pv, pv, null, xc);
          }
          currentNode.appendChild(pn);
        }
      }
      continue;
    }
    else if(tag == 'DIV')
    {
      var cmp = t.component;
      if (cmp == 'tree-node')
      {
        // Trim innerText
        addVerticalTreeNode(currentNode, getTreeNodeText(t), t.id, t.collapsed ? "false" : "true");
      }
      else if(cmp == 'dynamic-tree'){
        if(currentNode)
          addDynamicVerticalTree(currentNode, t.lastChild, xc);
        i = t.nextSibling.sourceIndex - 1;
      }
    }
    var name = t.name;
    if(name){
      if(tag == 'INPUT'){
        if(t.skipxml == 'true')
          continue;
        var type = t.type;
        if(type == 'radio' || type == 'checkbox'){
          if(t.component == 'vertical-tree')
            addVerticalTreeNodeChecked(currentNode, name, t.checked ? 'true' : 'false', t.oldvalue);
          else
            addNodeA(currentNode, name, t.value, t.pf, 'Checked', t.checked ? 'true' : 'false', t.oldvalue, xc);
        }
        else
          addNode(currentNode, name, t.value, t.oldvalue, t.pf, xc);
      }
      else if(tag == 'SELECT'){
        var cid = t.containerId;
        var postAll = cid == 'step_available' || cid == 'step_selected';
        var options = t.options;
        var ol = options.length;
        for(var j = 0; j < ol; ++j){
          var o = options[j];
          if(o.skipxml == 'true')
            continue;
          addNodeA(currentNode, name, o.value, null, 'Selected', o.selected ? 'true' : 'false', o.oldvalue, xc);
        }
      }
      else if(tag == 'EMBED'){
        if(t.component == 'flowchart'){
          if(currentNode)
            currentNode.appendChild(flowChartDataToXml(null, t.window.toPostData()));
        }
      }
      else if(tag == 'APPLET'){
        if(t.component == 'flowchart'){
          if(currentNode){
            var layout = t.toPostData();
            var r = xc.loadXML(layout);
            currentNode.appendChild(xc.firstChild);
          }
        }
      } 
    }
    else if(tag == '!'){
      var v = t.data;
      if(v.substring(0, 2) == 'P+')
        currentNode = buildXml(getNodePath(v), xmlMap, xc);
    }
    else if(tag == 'xml'){
      if(t.component == 'excel-pivot'){
        var dom = lastPivot.xmlDom2;
        if(dom == null)
          continue;
        var editable = lastPivot.editableItems;
        var rows = dom.selectNodes("//z:row");
        var paths = t.XMLDocument.selectNodes("//comment()");
        var el = editable.length;
        var rl = rows.length;
        var pl = paths.length;
        if(pl < rl)
          rl = pl;
        for(var j = 0; j < rl; ++j){
          var cn = buildXml(paths[j].text.substring(2), xmlMap, xc);
          var n = rows[j];
          for(var x = 0; x < el; ++x){
            var name = editable[x];
            addNode(cn, name, n.getAttribute(name), n.getAttribute('Old' + name), null, xc);
          }
        }
      }
    }
    else if(tag == 'TABLE'){
      var chunked = t.chunked;
      if(chunked > 0){
        var tbodyc = t.children[1].children;
        var rows = t.allRows;
        for(var crow = 0; crow < chunked; ++crow){
          currentNode = buildXml(getNodePath(crow > 0 ? tbodyc[crow * 2 - 1].data : t.children[0].data), xmlMap, xc);
          var crowd = rows[crow];
          if(typeof crowd == 'string')
            currentNode.setAttribute('NoChanges', 'true');
          else
            currentNode = buildXmlNodes(currentNode, xmlMap, crowd.children, true, xc);
        }
        i = tbodyc[tbodyc.length - 1].sourceIndex;
      }
    }
    if(recurse)
      currentNode = buildXmlNodes(currentNode, xmlMap, t.children, true, xc);
  }
  return currentNode;
}

var ENTER_KEY = 13;
var ESCAPE_KEY = 27;
var UP_KEY = 38;
var UP_KEY = 38;
var DOWN_KEY = 40;
var CHAR_CODE_0 = '0'.charCodeAt(0);
var CHAR_CODE_9 = '9'.charCodeAt(0);
var xlR1C1 = -4150;

function buildXml(v, xmlMap, xc)
{
  var n = xmlMap[v];
  if(n)
    return n;
  var i = v.lastIndexOf('/');
  if(i > 0){
    var t = v.charCodeAt(i + 1);
    if(t >= CHAR_CODE_0 && t <= CHAR_CODE_9){
      var x = v.substring(0, i);
      i = x.lastIndexOf('/');
      var p = buildXml(x.substring(0, i), xmlMap, xc);
      n = xc.createElement(x.substring(i + 1));
      p.appendChild(n);
      xmlMap[v] = n;
      return n;
    }
    else{
      var p = buildXml(v.substring(0, i), xmlMap, xc);
      n = xc.createElement(v.substring(i + 1));
      p.appendChild(n);
      xmlMap[v] = n;
      return n;
    }
  }
  if(v == '/')
    return null;
  xmlMap.root = v;
  n = xc.createElement(v.substring(1));
  xmlMap[v] = n;
  return n;
}

function addNode(node, name, value, oldvalue, pf, xc)
{
  if(node == null)
    return null;
  if(pf)
    name = name.substring(pf.length);
  var n = xc.createElement(name);
  node.appendChild(n);
  if(oldvalue != value)
    n.setAttribute('OldValue', oldvalue);
  if(value == null)
    return n;
  n.setAttribute('Value', value);
  return n;
}

function addNodeA(node, name, value, pf, aname, avalue, aold, xc)
{
  var n = addNode(node, name, value, value, pf, xc);
  if(n){
    n.setAttribute(aname, avalue);
    if(aold != avalue)
      n.setAttribute('Old' + aname, aold);
  }
}

function getTreeNodeText(treeNode)
{
  var treeNodeChildren = treeNode.children;
  var numChildren = treeNodeChildren.length;
  for(i=0; i<numChildren; i++)
  {
    if (treeNodeChildren[i].tagName == 'A')
    {
      return treeNodeChildren[i].innerText.replace(/^\s*|\s*$/g,'');
    }
  }
  return '';
}


function addVerticalTreeNode(currentNode, displayText, id, expanded)
{
  if(currentNode == null)
    return;
  currentNode.setAttribute('DisplayText', displayText);
  currentNode.setAttribute('ID', id);
  currentNode.setAttribute('Expanded', expanded);
}

function addVerticalTreeNodeChecked(currentNode, name, checked, oldvalue)
{
  if(currentNode == null)
    return;
  currentNode.setAttribute('ID', name);
  currentNode.setAttribute('Checked', checked);
  if(oldvalue != checked)
    currentNode.setAttribute('OldChecked', oldvalue);
}

function addDynamicVerticalTree(currentNode, div, xc)
{
  var tag = div.postId;
  if(tag){
    var node = xc.createElement(tag);
    node.setAttribute('ID', div.treeId);
    var children = div.lastChild;
    var text = children.previousSibling;
    node.setAttribute('DisplayText', text.innerText);
    node.setAttribute('Expanded', div.hc ? div.collapsed ? 'false' : 'true' : 'never');
    var input = text.previousSibling.previousSibling;
    if(input.tagName == 'INPUT'){
      var chk = input.checked;
      node.setAttribute('Checked', chk ? 'true' : 'false');
      if(chk && input.old == null || chk == false && input.old)
        node.setAttribute('OldChecked', chk ? 'false' : 'true');
    }
    if(div.popupId)
      node.setAttribute('PopupId', div.popupId);
    if(div.draggable)
      node.setAttribute('DropToIds', div.draggable);
    if(div.dropId)
      node.setAttribute('DragAndDropId', div.dropId);
    currentNode.appendChild(node);
    currentNode = node;
  }
  div = div.firstChild;
  while(div){
    addDynamicVerticalTree(currentNode, div, xc)
    div = div.nextSibling;
  }
}

function addDeletedCell(o, deletedRows, deleted, xc)
{
  if(deleted == null)
    deleted = xc.createElement('DELETED');
  var dr = o.r;
  var deletedRow = deletedRows[dr];
  if(deletedRow == null){
    deletedRow = xc.createElement('ROW');
    deletedRow.setAttribute('Row', dr);
    deleted.appendChild(deletedRow);
    deletedRows[dr] = deletedRow;
  }
  var node = xc.createElement('CELL');
  node.setAttribute('Column', o.c);
  node.setAttribute('Address', o.a);
  node.setAttribute('Value', o.v);
  var f = o.f;
  if(f)
    node.setAttribute('Formula', f);
  deletedRow.appendChild(node);
  return deleted;
}

function getSaveOldCallbacks(c)
{
  var cbs = c.substring(4, c.length - 4).split(','); 
  cbs[0] = parseInt(cbs[0]);
  cbs[1] = parseInt(cbs[1]);
  cbs[2] = parseInt(cbs[2]);
  cbs[3] = parseInt(cbs[3]);
  return cbs;
}

function saveOldValues()
{
  saveOld(document.all, false);
}

function saveOld(all, recurse)
{
  var l = all.length;
  for(var i = 0; i < l; ++i)
  {
    var t = all[i];
    var name = t.name;
    if(name){
      var tag = t.tagName;
      if(tag == 'INPUT'){
        var type = t.type;
        if(type == 'radio' || type == 'checkbox'){
          if(t.oldvalue == null)
            t.oldvalue = t.checked ? 'true' : 'false';
        }
        else if(t.oldvalue == null)
          t.oldvalue = t.value;
      }
      else if(tag == 'SELECT'){
        var cid = t.containerId;
        var postAll = cid == 'step_available' || cid == 'step_selected';
        var options = t.options;
        var ol = options.length;
        for(var j = 0; j < ol; ++j){
          var o = options[j];
          if(o.oldvalue == null)
            o.oldvalue = o.selected ? 'true' : 'false';
        }
      }
      else if(tag == 'OBJECT'){
        if(t.component == 'workbook'){
          if(t.oldvalue || t.rawData == 'true')
            continue;
          var formulaCells = null;
          var nextFormula = 0;
          var array = new Array();
          t.oldvalue = array;
          var callbacks = new Array();
          t.callbacks = callbacks;
          var callbackOffset = i + 1;
          var count = all[++i].text;
          count = parseInt(count.substring(5, count.length - 4));
          for(var j = 0; j < count; ++j){
            var x = all[++i].text;
            callbacks[j] = x.substring(5, x.length - 4);
          }
          var next = 0;
          var names = t.Names;
          var w = t.Worksheets;
          var wl = w.Count;
          var offset = i + 2 * wl;
          for(var j = 0; j < wl; ++j){
            var ws = w(j + 1);
            var wsn = ws.Name;
            var sheetData = new Object();
            array['sheet' + wsn] = sheetData;
            sheetData.cbs = getSaveOldCallbacks(all[i + 2 + 2 * j].text);
            var cells = ws.Cells;
            var range = ws.UsedRange;
            var cc = range.Columns.Count;
            var rc = range.Rows.Count;
            for(var r = 1; r <= rc; ++r){
              for(var c = 1; c <= cc; ++c){
                var cell = cells(r, c);
                var v = cell.Value;
                if(typeof v == 'undefined')
                  continue;
                names.Add('A..' + (1000000 + ++next), "='" + wsn + "'!" + cell.Address);
                var x = cell.Name.Index;
                var o = new Object();
                o.a = cell.Address(true, true, xlR1C1);
                o.r = r;
                o.c = c;
                o.v = cell.Value;
                if(cell.HasFormula){
                  if(formulaCells == null){
                    var dom = new ActiveXObject( 'Microsoft.XMLDOM' );
                    dom.loadXML(t.XMLData);
                    formulaCells = dom.selectNodes("//ss:Cell[@ss:Formula]");
                  }
                  o.f = formulaCells[nextFormula++].getAttribute('ss:Formula');
                }
                else
                  o.f = '';
                o.cbs = getSaveOldCallbacks(all[offset + 2 * next].text);
                array[x] = o;
              }
            }
            sheetData.last = next;
          }
        }
      }
    }
    if(recurse)
      saveOld(t.children, true);
  }
}

function updateCallback(http)
{
  try{
    if(http.ts == document.body.ts && !cancelledRequests[http.uc])
      updateHTML(http.id, http.getText());
  }
  catch(e)
  {
  }
}

function getUpdateHTML_XML(id, action, url, post, async)
{
  var http = new HTTP( );
  var pageAction = action == 'PAGE_ACTION';
  var separator = '?';
  if(url == null)
    url = omxContextPath + '/view.x2ps';
  else if(url.indexOf('?') > 0)
    separator = '&';
  var prefix = pageAction ? 'PAGE_ACTION_ID' : 'LAF_FILTER_ID';
  if(id){
    if(typeof id == typeof []){
      var l = id.length;
      url += separator + prefix + '=' + id[0];
      for(var i = 1; i < l; ++i)
        url += '&' + prefix + '=' + id[i];
    }
    else
      url += separator + prefix + '=' + id;
  }
  if(pageAction && !post){
    if(url.indexOf('?SYS_ID=') < 0 && url.indexOf('&SYS_ID=') < 0)
      url += '&SYS_ID=' + document.getElementById('SYS_ID').value;
    if(url.indexOf('?PAGE=') < 0 && url.indexOf('&PAGE=') < 0)
      url += '&PAGE=' + document.getElementById('PAGE').value;
    if(url.indexOf('?AJAX_UPDATE=') < 0 && url.indexOf('&AJAX_UPDATE=') < 0)
      url += '&AJAX_UPDATE=true';
    if(url.indexOf('?BUTTON_ID=') < 0 && url.indexOf('&BUTTON_ID=') < 0)
      url += '&BUTTON_ID=PAGE_ACTION';
  }
  var buttonId = document.getElementById('BUTTON_ID');
  
  if(action)
    buttonId.value = action;
  else if(post)
    buttonId.value = 'SYS_REFRESH';
  if(async){
    http.id = id;
    http.ts = document.body.ts;
    http.uc = ++window.updateCount;
    var cb = typeof async == 'function' ? async : updateCallback;
    if(post){
      var postdata = buildPostData();
      return http.post(url, cb, pageAction ? 'xml' : null, postdata, 'Content-type', 'application/xml; charset=UTF-8');
    }
    else
      http.get(url, cb, pageAction ? 'xml' : null);
  }
  else{
    if(post){
      var postdata = post.xml;
      if(postdata){
        var xc = getXMLCreator();
        var postdata = xc.createElement('REQUEST');
        addPostNode(postdata, 'AJAX_UPDATE', 'true', xc);
        addPostNode(postdata, 'PAGE', document.getElementById('PAGE').value, xc);
        addPostNode(postdata, 'SYS_ID', document.getElementById('SYS_ID').value, xc);
        addPostNode(postdata, 'BUTTON_ID', buttonId.value, xc);
        postdata.appendChild(post);
        postdata = postdata.xml;
      }
      else
        postdata = buildPostData();
      var r = http.post(url, null, pageAction ? 'xml' : null, postdata, 'Content-type', 'application/xml; charset=UTF-8');
      if(pageAction)
        checkRedirect(r);
      return r;
    }
    var r = http.get(url, null, pageAction ? 'xml' : null);
    if(pageAction)
      checkRedirect(r);
    return r;
  }
}

function updateNode(id, html, i)
{
  var node = document.getElementById('_un_' + id);
  if(node == null)
    node = document.getElementById(id);
  if(node == null){
    id = id + '_td';
    node = document.getElementById(id);
    if(node){
      var div = document.createElement('DIV');
      div.innerHTML = '<TABLE><TBODY><TR>' + html + '<TR></TBODY></TABLE>';
      var tr = node.parentElement;
      var c = tr.children;
      var p = 0;
      while(c[p] != node)
        ++p;
      var tds = div.firstChild.cells;
      var l = p + tds.length;
      while(p < l)
        tr.replaceChild(tds[0], c[p++]);
      return i;
    }
  }
  else{
    var x = node.sourceIndex;
    node.outerHTML = html;
    if(x < i)
      return x;
  }
  return i;
}

function checkRedirect(xml)
{
  xml = xml.firstChild;
  if(xml && xml.tagName == 'REDIRECT_TO')
    window.location.href = xml.getAttribute('Value');
}

var maxHistory = 64;
var pageHTML = null;
var pageHistory = new Object();
var currentHistory = null;
var updateAsync = true;
var updateDisabled = true;
var updateCancellable = true;
var updateCount = 0;
var navigateAsync = true;
var navigateCancellable = true;
var cancelledRequests = new Object();
var scriptLoadingCount = 0;

function History(p, t)
{
  this.prev = p;
  this.time = t;
}

History.prototype.unlink = function()
{
  var p = this.prev;
  if(p)
    p.next = null;
  var n = this.next;
  if(n)
    n.prev = null;
}

function getHistoryNode(time)
{
  var h = new History(currentHistory, time);
  if(currentHistory)
    currentHistory.next = h;
  pageHistory[time] = h;
  return h;
}

function limitHistory(time)
{
  var array = new Array();
  for(var i in pageHistory){
    var x = pageHistory[i];
    var t = x.time;
    if(time < t){
      x.unlink();
      delete pageHistory[t];
    }
    else
      array[array.length] = t;
  }
  var l = array.length - maxHistory;
  while(--l >= 0){
    var t = array[l];
    pageHistory[t].unlink();
    delete pageHistory[t];
  }
}

function updateHistory(html)
{
  var p = window.parent;
  var f = p.frames['i2ui_shell_bottom'];
  if(f == null)
    return false;
  var ch = p.currentHistory;
  ch.html = html;
  p.limitHistory(ch.time);
  var time = new Date().getTime();
  var h = p.getHistoryNode(time);
  p.loading = true;
  f.document.location.assign(omxContextPath + '/core/history.html?' + window.name + '=' + time);
  return true;
}

function updateScripts(i)
{
  var all = document.all;
  var l = all.length;
  var p = 0;
  var scripts = new Array();
  var texts = new Array();
  while(i < l){
    var script = all[i];
    if(script.tagName == 'SCRIPT'){
      if(script.static == 'true'){
        ++i;
        continue;
      }
      scripts[p] = script;
      if(script.src)
        texts[p] = '';
      else
        texts[p] = script.text;
      p++;
    }
    ++i;
  }
  l = scripts.length;
  i = 0;
  while(i < l){
    var s = scripts[i];
    currentScriptPos = s.sourceIndex;
    s.text = texts[i];
    ++i;
  }
}

function getBodyHTML(html)
{
  var bb = html.indexOf('<body ');
  var bb = html.indexOf('>', bb);
  var be = html.indexOf('</body>');
  return html.substring(bb + 1, be + 7);
}

function cloneBody()
{
  try{
    var b = document.body;
    var c = b.cloneNode(true);
    var ss = b.getElementsByTagName('SCRIPT');
    var ds = c.getElementsByTagName('SCRIPT');
    var l = ss.length;
    for(var i = 0; i < l; ++i)
      ds[i].data = ss[i].text;
    return c;
  }
  catch(e)
  {
    return null;
  }
}

function getPageHTML()
{
  var html = pageHTML;
  if(html){
    pageHTML = null;
    return html;
  }
  return document.body.outerHTML;
}

function savePageHTML()
{
  pageHTML = document.body.outerHTML;
}

function update(id, action, disabled, async, url)
{
  if(form.enctype == 'multipart/form-data')
  {
    ui_submit(action);
  }
  else
  {
    if(typeof async == 'undefined')
      async = updateAsync;
    if(typeof disabled == 'undefined')
      disabled = updateDisabled;
    if(typeof cancellable == 'undefined')
      cancellable = updateCancellable;
    if(frameDialogDiv)
      closeFrameDialog();
    if(disabled){
      displayBusyBox();
      disablePage(cancellable);
    }
    doInternalUpdate(id, action, url, true, async);
  }
}

function cancelUpdate()
{
  cancelledRequests[window.updateCount] = true;
  hideRenderingMessage();
  enablePage();
}

function doInternalUpdate(id, action, url, post, async)
{
  setTimeout(function(){ internalUpdate(id, action, url, post, async); }, 0);
}

function internalUpdate(id, action, url, post, async)
{
  updateHistory(getPageHTML(), false);
  var html = getUpdateHTML_XML(id, action, url, post, async);
  if(async)
    return;
  updateHTML(id, html);
}

function finishUpdateHTML()
{
  showBody(false);
  disablePage();
  rootObject = null;
  setTimeout(enablePage, 0);
}

function updateHTML(id, html)
{
  var redirectTo = html.substring(0,11) == 'REDIRECT_TO';
  if(redirectTo)
  {
    var url = html.substring(11);
    window.location.href = url;
    return true;
  }
  var fullPage = html.indexOf('<html ') >= 0;
  var i = document.all.length;
  if(fullPage){
    html = getBodyHTML(html);
    var body = document.body;
    var i = body.sourceIndex;
    body.innerHTML = html;
  }
  else if(typeof id == typeof []){
    var l = id.length;
    for(var x = 0; x < l; ++x){
      var t = id[x];
      var s = '<<BEGIN ' + t + '>>';
      var b = html.indexOf(s);
      if(b < 0)
        continue;
      b += s.length;
      var e = html.indexOf('<<END>>', b);
      s = html.substring(b, e);
      i = updateNode(t, s, i);
    }
  }
  else
    i = updateNode(id, html, i);
  if(id){
    updateScripts(i);
    saveOldValues();
    enableButtonsIfRowSelected();
    hideRenderingMessage();
    if(fullPage)
      setPageVisibility('visible');
    onFullResize();
    rootObject = null;
    setTimeout(enablePage, 0);
  }
  else{
    scriptLoadingCount = 1;
    updateScripts(0);
    _alltables = null;
    if(--scriptLoadingCount == 0)
      finishUpdateHTML();
  }
  return true;
}

function restorePage(html)
{
  var b = document.body;
  if(html)
    b.innerHTML = html.substring(html.indexOf('>') + 1, html.lastIndexOf('<'));
  else
    b.innerHTML = '';
  updateScripts(0);
  hideRenderingMessage();
  enablePage();
  onFullResize();
}

function windowNavigateTo(url)
{
  displayBusyBox();
  disablePage(navigateCancellable);
  doInternalUpdate(null, null, url, false, navigateAsync);
}

function navigateTo(url, target)
{
  var p = window.parent;
  if(target){
    var f = p.frames[target];
    if(f.studioPage && url.indexOf('start.x2ps') >= 0){
      if(url.indexOf('?') < 0)
        url += '?AJAX_UPDATE=true';
      else
        url += '&AJAX_UPDATE=true';
      f.windowNavigateTo(url);
    }
    else{
      var time = new Date().getTime();
      if(url.indexOf('?') < 0)
        url += '?_TS_' + time;
      else
        url += '&_TS_' + time;
      f.location = url;
    }
  }
  else
    location = url;
}

function onUnloadSuper()
{
  var ch = parent.currentHistory;
  if(ch)
    ch = ch.prev;
  while(ch){
    try{
      var html = ch.html;
      if(html.tagName){
        delete html;
        ch.html = null;
      }
    }
    catch(e)
    {
      return;
    }
    ch = ch.prev;
  }
}

function openPrintWindow()
{
  window.open(omxContextPath + '/print.x2ps', null, 'menubar=no, scrollbars=yes, resizable=yes');
  hideRenderingMessage();
  enablePage();
}

function showPrintPage()
{
  var sysId = document.getElementById('SYS_ID');
  if(sysId){
    displayBusyBox();
    disablePage(updateCancellable);
    getUpdateHTML_XML(null, null, omxContextPath + '/print.x2ps?PRINT_MODE=true&PRINT_MAX_ROWS=' + page_maxRowsExportable, true, openPrintWindow);
  }
  else
    window.print();
}

function closePrint()
{
  var t = document.getElementById('print_preview');
  if(t)
    t.ExecWB(45, 2);
  window.close();
}

var CELL_ON_END_EDIT = 0;
var CELL_ON_SELECT = 1;
var ROW_ON_END_EDIT = 2;
var ROW_ON_SELECT = 3;

function workbookCallback(e, value, cancelled, wb, cellCb, rowCb, wbcb)
{
  var oldvalue = wb.oldvalue;
  if(oldvalue){
    if(typeof e.Name == 'object'){
      var x = e.Name.Index;
      var o = oldvalue[x];
      var cbs = o.cbs;
      var cb = cbs[cellCb];
      if(cb >= 0 && eval(wb.callbacks[cb]))
        return;
      cb = cbs[rowCb];
      if(cb >= 0 && eval(wb.callbacks[cb]))
        return;
    }
    var cbs = oldvalue['sheet' + e.Worksheet.Name].cbs;
    var cb = cbs[0];
    if(cb >= 0 && eval(wb.callbacks[cellCb]))
      return;
  }
  if(wbcb)
    eval(wbcb);
}

function workbookCBCommandExecute(e, wb)
{
}

function workbookCBEndEdit(e, value, cancelled, wb)
{
  workbookCallback(e, value, cancelled, wb, CELL_ON_END_EDIT, ROW_ON_END_EDIT, wb.onEndEdit);
}

function workbookCBSelectionChanging(e, wb)
{
  wb.selectionObject = e;
  workbookCallback(e, null, null, wb, CELL_ON_SELECT, ROW_ON_SELECT, wb.onSelect);
}

function hookWorkbook(wb)
{
  wb.attachEvent("CommandExecute", function(e){ workbookCBCommandExecute(e, wb); return true; });
  wb.attachEvent("EndEdit", function(accept, value){ workbookCBEndEdit(wb.selectionObject, value.Value, !accept, wb);  return true; });
  wb.attachEvent("SelectionChanging", function(e){ workbookCBSelectionChanging(e, wb);  return true; });
}

function isValidData(oField)
{
  if ( oField.parentElement.fieldtype == 'Number' || oField.parentElement.fieldtype == 'Currency')
  {
    if(isValid_field_worker(oField))
    {
      return;
    }
  }
}

var currentRow = 0;
var savedThis = null;

function dynamicURL(url, returnIt)
{
  savedThis = null;
  if(returnIt)
    return url;
  else
    navigateTo(url, window.name);
}

function dynamicValue(name, index)
{
  if(name.indexOf('(') > 0)
    return eval(name);
  var object = document.getElementsByName(name)[index];
  if(object){
    var tag = object.tagName;
    if(tag == 'INPUT'){
      var type = object.type;
      if(type == 'radio' || type == 'checkbox')
        return object.checked ? 'true' : 'false';
    }
    return object.value;
  }
  return '';
}

function saveCurrentRow(row)
{
  currentRow = row;
  return '';
}

function saveThis(t)
{
  savedThis = t;
}

function suggestGet(t)
{
  var suggest = t.suggest;
  if(suggest == null){
    suggest = t.nextSibling;
    suggest.element = t;
    t.suggest = suggest;
  }
  return suggest;
}

function suggestChange(t)
{
  if(event.propertyName == 'value'){
    var v = t.value;
    var suggest = suggestGet(t);
    if(v == suggest.suggest || suggest.ignore)
      return;
    if(v)
      suggestExecute(t);
    else
      suggest.style.display = 'none';
    suggest.suggest = v;
  }
}

function suggestExecute(t, show)
{
  var suggest = suggestGet(t);
  savedThis = t;
  var url = omxContextPath + '/view.x2ps?' + suggest.postName + '=' + t.value + '&SUGGEST_MAX_ROWS=' + suggest.size;
  url += eval(suggest.params);
  savedThis = null;
  var post = suggest.post == 'true';
  getUpdateHTML_XML(suggest.actionId, 'PAGE_ACTION', url, post, function(h){ suggestCallback(t, h, show); });
}

function findpos(p, pos)
{
  while(p){
    pos[0] += p.offsetLeft;
    pos[1] += p.offsetTop;
    var t = p.tagName;
    if(t == 'DIV')
      return;
    else if(t == 'TD')
      pos[0] += p.clientLeft;
    p = p.offsetParent;
  }
}

function suggestCallback(t, h, show)
{
  var suggest = t.suggest;
  var pos = [ 0, t.offsetHeight ];
  findpos(t, pos);
  var s = suggest.style;
  s.display = '';
  s.left = pos[0];
  s.top = pos[1];
  var xml = h.getXML();
  checkRedirect(xml);
  var nodes = xml.selectNodes('/RESPONSES/RESPONSE/OPTION');
  suggest.removeChild(suggest.firstChild);
  var table = document.createElement('<TABLE cellspacing="1" cellpadding="0">');
  table.width = t.offsetWidth;
  table.onmouseover = function(){ suggest.ignore = true; }
  table.onmouseout = function(){ suggest.ignore = false; }
  var tbody = document.createElement('TBODY');
  table.appendChild(tbody);
  var l = nodes.length;
  var size = suggest.size;
  if(l > size)
    l = size;
  if(l > 0){
    for(var i = 0; i < l; ++i){
      var n = nodes[i];
      var v = n.getAttribute('Value');
      var tr = document.createElement('TR');
      tbody.appendChild(tr);
      var td = document.createElement('TD');
      tr.appendChild(td);
      td.noWrap = true;
      td.innerText = v;
      td.className = 'suggest';
      td.onmouseover = function(){ suggestShow(suggest, this); }
      td.onmouseout = function(){ suggestHide(suggest, this); }
      td.onclick = function(){ suggestSelect(suggest, this.innerText); }
    }
  }
  else
    suggestClose(suggest);
  suggest.appendChild(table);
  if(show && l > 0)
    suggestShow(suggest, suggest.firstChild.cells[0]);
  else
    suggest.current = null;
}

function suggestInputBlur(t)
{
  var suggest = t.suggest;
  if(suggest){
    if(suggest.ignore)
      return;
    suggest.style.display = 'none';
  }
}

function suggestInputFocus(t)
{
  var s = t.suggest;
  if(s && s.ignore)
    return;
  if(t.value)
    suggestExecute(t);
}

function suggestInputKeyDown(t)
{
  var suggest = t.suggest;
  if(suggest == null)
    return;
  switch(event.keyCode){
    case DOWN_KEY:
      var s = suggest.style;
      if(s.display == 'none')
        suggestExecute(t, true);
      else{
        var cells = suggest.firstChild.cells;
        var c = suggest.current;
        var i = c ? c.parentElement.rowIndex + 1 : 0;
        if(cells.length > i)
          suggestShow(suggest, cells[i]);
      }
      break;
    case UP_KEY:
      var cells = suggest.firstChild.cells;
      var c = suggest.current;
      if(c){
        var i = c.parentElement.rowIndex - 1;
        if(i >= 0 && i < cells.length)
          suggestShow(suggest, cells[i]);
        else
          suggestClose(suggest);
      }
      break;
    case ENTER_KEY:
      var c = suggest.current;
      if(c){
        suggestSelect(suggest, c.innerText);
        event.cancelBubble = true;
        return false;
      }
      break;
    case ESCAPE_KEY:
      if(suggest.style.display == 'none')
        return true;
      suggestClose(suggest);
      event.cancelBubble = true;
      return false;
  }
}

function suggestShow(suggest, td)
{
  suggestHide(suggest, suggest.current);
  suggest.current = td;
  td.className = 'suggestSelected';
}

function suggestHide(suggest, td)
{
  if(td)
    td.className = 'suggest';
  suggest.current = null;
}

function suggestSelect(suggest, value)
{
  suggest.ignore = true;
  suggestHide(suggest, suggest.current);
  suggest.element.value = value;
  suggest.ignore = false;
  suggest.style.display = 'none';
}

function suggestClose(suggest)
{
  suggest.ignore = true;
  suggestHide(suggest, suggest.current);
  suggest.style.display = 'none';
  suggest.ignore = false;
}

function showChunkedLines(data, scroller, header, headerScroller, before, after)
{
  headerScroller.scrollLeft = scroller.scrollLeft;
  var p = scroller.scrollTop;
  var th = data.totalHeight;
  var as = after.style;
  var h = th - p - data.clientHeight;
  if(h > 0){
    as.height = h;
    as.display = '';
  }
  else{
    p += h;
    as.display = 'none';
  }
  var bs = before.style;
  if(p > 0){
    bs.height = p;
    bs.display = '';
  }
  else
    bs.display = 'none';
  var srows = data.allRows;
  var drows = data.rows;
  var currentRow = Math.floor(data.chunked / th * p);
  var lp = data.lastPos;
  if(currentRow == lp)
    return;
  data.lastPos = currentRow;
  var div = null;
  var i = 0;
  var tbody = data.children[1];
  var children = tbody.children;
  var l = data.visibleRows;
  for(var i = 0; i < l; ++i)
    srows[lp++] = children[i * 2];
  for(var i = 0; i < l; ++i){
    var r = srows[currentRow];
    if(typeof r == 'string'){
      if(div == null)
        div = document.createElement('DIV');
      div.innerHTML = '<TABLE>' + r + '</TABLE>';
      r = div.firstChild.rows[0];
      saveOld(r.children, true);
      srows[currentRow] = r;
    }
    tbody.replaceChild(r, children[i * 2]);
    ++currentRow;
  }
  minimizeColumnWidths(data, header, getScrollBarSize(scroller));
}

/* START : eq 583118 - Type ahead feature for dropdowns */
var timeLastKeyPressed = 0;
var keyPressedBuffer = "";
function dd_doSearchByLetter(obj) 
{
 if (navigator.appVersion.indexOf('MSIE 6') < 0)
 {
   event.returnValue = true;
   return;
 }
 var result = true;
 var now = (new Date()).getTime();
 if(now - timeLastKeyPressed > 1000)
 { 
   keyPressedBuffer = "";
 }
 timeLastKeyPressed = now;

 var newChar = String.fromCharCode(window.event.keyCode);
 if (newChar == '\r')
 {
   obj.fireEvent('onchange', document.createEventObject());
   return;
 }
 var startText = keyPressedBuffer + newChar;

 for(var i=0; i < obj.options.length; i++) 
 {
   var option = obj.options[i];
   if((option.value != "") && (startsWith(option.text, startText))) 
   {
     obj.selectedIndex=i;
     keyPressedBuffer = startText;
     window.event.returnValue = false;
     window.event.cancelBubble = true;
     result = false;
     break;
   }
 }

 if(result)
 {
   keyPressedBuffer = newChar;
 }
 return result;
}
/* END */

function unescapeJS(s, unescape)
{
  return s.replace(/\n+/g, unescape);
}

function toggleTreeNode(img)
{
  var node = img.parentElement;
  if(node.collapsed){
    if(node.load)
      loadTreeNode(node, true);
    else{
      expandTreeNode(node);
      scrollHelper();
    }
  }
  else{
    collapseTreeNode(node);
    scrollHelper();
  }
}

function getTreeNode(node)
{
  if (node.treeData != null)
    return node.treeData;

  if (node.rootTreeId == null)
    return null;
  var rootTreeId = "tree_div_"+node.rootTreeId;
  var divCollection = document.getElementsByTagName("div");
  for (var i=0; i<divCollection.length; i++) {
    if(divCollection[i].getAttribute("id") == rootTreeId) {
      node.treeData = divCollection[i];
      return divCollection[i];
    } 
  }
}

var draggedTreeNode = null;

function expandTreeNode(node)
{
  var img = node.firstChild;
  var treeData = getTreeNode(node);
  img.src = treeData.collapseImg;
  if(img.onclick == null)
    img.onclick= function(){ toggleTreeNode(this); };
  node.lastChild.style.display = '';
  node.collapsed = false;
}

function collapseTreeNode(node)
{
  var treeData = getTreeNode(node);
  node.firstChild.src = treeData.expandImg;
  node.lastChild.style.display = 'none';
  node.collapsed = true;
}

function loadTreeNode(node, scroll)
{
  var ids = getTreeNodeIds(node);
  var div = ids.div;
  ids = ids.ids;
  var url = omxContextPath + '/view.x2ps?PAGE_ACTION_ID=' + div.expandId + ids;
  savedThis = node;
  url += eval(div.params);
  savedThis = null;
  var xml = getUpdateHTML_XML(null, 'PAGE_ACTION', url, div.post == 'true', false);
  var nodes = xml.selectNodes('/RESPONSES/RESPONSE/*');
  var l = nodes.length;
  var children = node.lastChild;
  for(var i = 0; i < l; ++i)
    children.appendChild(createTreeNode(nodes[i], div));
  node.load = false;
  if(scroll){
    expandTreeNode(node);
    scrollHelper();
  }
}

function getTreeNodeIds(div, prefix)
{
  prefix = prefix ? '&' + prefix : '&';
  var r = new Object();
  var treeData = getTreeNode(div);
  if(treeData == null){
    r.ids = prefix + 'TREE_ID=' + div.treeId;
    r.div = div;
    return r;
  }
  var ids = prefix + 'TREE_NODE=' + div.treeId;
  for(;;){
    div = div.parentElement.parentElement;
    treeData = getTreeNode(div);
    if(treeData == null)
      break;
    ids = prefix + 'PARENT_TREE_NODE=' + div.treeId + ids;
  }
  r.ids = prefix + 'TREE_ID=' + div.treeId + ids;
  r.div = div;
  return r;
}

function getTreeNodeUrl(span)
{
  var div = span.parentElement;
  while(div.tagName != 'DIV')
    div = div.parentElement;
  var ids = getTreeNodeIds(div).ids;
  return 'view.x2ps?' + ids.substring(1);
}

function createTreeNode(nodeXml, data)
{
  var div = document.createElement('DIV');
  var id = nodeXml.getAttribute('ID');
  if(id == null)
    id = nodeXml.getAttribute('Id');
  var name = nodeXml.getAttribute('DisplayText');
  if(name == null)
    name = nodeXml.getAttribute('Value');
  var popup = nodeXml.getAttribute('PopupId');
  var expanded = nodeXml.getAttribute('Expanded');
  if(expanded == null)
    expanded = nodeXml.getAttribute('HasChildren') == 'true' ? 'false' : 'never';
  var hc = expanded != 'never';
  var draggable = nodeXml.getAttribute('DropToIds');
  var drop = nodeXml.getAttribute('DragAndDropId');
  var img = hc ? expanded == 'true' ? data.collapseImg : data.expandImg : data.leafImg;
  var html = '<IMG src="'+ img + '"';
  if(hc)
    html += ' onclick="toggleTreeNode(this);"';
  html += '/>&nbsp;';
  if(data.drawCheckbox == 'true'){
    html += '<input type="checkbox" name="tn_' + data.treeId + id + '"';
    if(nodeXml.getAttribute('Checked') == 'true')
      html += ' checked="true" old="true"';
    html += '/>&nbsp;';
  }
  var popupA = null;
  if(popup){
    var popups = data.firstChild.children;
    var l = popups.length;
    for(var i = 0; i < l; ++i){
      var p = popups[i];
      if(p.popupId == popup){
        popupA = p;
        break;
      }
    }
  }
  html += '<SPAN';
  if(draggable)
    html += ' onmousedown="i2uiHideMenu(); if(event.button == 1 && treeNodeCheckDragStart(this)) this.dragDrop();" ondragstart="return treeNodeDragStart(this);" ondragend="treeNodeDragEnd(this);" ondrag="treeNodeDrag(this);"';
  if(drop)
    html += ' ondragenter="return false;" ondragover="return treeNodeDragCheck(this);"';
  if(popupA){
    var pid = data.treeId + '_' + popup
    html += ' oncontextmenu="setContextAndShowMenu(\'' + pid + '_0\', \'0\', getTreeNodeUrl(this)); return false;" onmouseover="i2uiSetMenuCoords(this, event)"';
  }
  else
    html += ' oncontextmenu="return false;"';
  html += '>' + name + '</SPAN><DIV style="MARGIN-LEFT: 15px"></DIV>';
  div.innerHTML = html;
  div.hc = hc;
  div.collapsed = expanded == 'false';
  div.treeData = data;
  div.treeId = id;
  div.dropId = drop;
  div.postId = nodeXml.tagName;
  if(popupA)
    div.popupId = popup;
  if(draggable){
    var ids = draggable.split(',');
    var map = new Object();
    var c = ids.length;
    while(--c >= 0)
      map[ids[c]] = true;
    div.dropIds = map;
    div.draggable = draggable;
  }
  if(expanded == 'true'){
    var childrenDiv = div.lastChild;
    nodeXml = nodeXml.firstChild;
    while(nodeXml){
      childrenDiv.appendChild(createTreeNode(nodeXml, data));
      nodeXml = nodeXml.nextSibling;
    }
    div.load = false;
  }
  else
    div.load = true;
  return div;
}

function treeNodeCheckDragStart(span)
{
  var div = span.parentElement;
  if(div.bdcb)
    return div.bdcb();
  var cb = div.treeData.bdcb;
  if(cb){
    eval('div.bdcb = function(){' + cb + '}');
    return div.bdcb();
  }
  return true;
}

function treeNodeDragStart(span)
{
  draggedTreeNode = span.parentElement;
  var dragger = draggedTreeNode.cloneNode(true);
  var table = document.getElementById('body_table');
  var s= dragger.style;
  s.position = 'absolute';
  s.width = draggedTreeNode.offsetWidth;
  dragger.id = 'dragger';
  var tree = draggedTreeNode.treeData;
  tree.appendChild(dragger);
  draggedTreeNode.dragger = dragger;
  var pos = [ 0, 5 ];
  findpos(tree.parentElement, pos);
  draggedTreeNode.dragLeft = pos[0];
  draggedTreeNode.dragTop = pos[1];
  treeNodeDrag(span);
  return true;
}

function treeNodeDrag(span)
{
  if(draggedTreeNode){
    var s = draggedTreeNode.dragger.style;
    s.left = draggedTreeNode.dragLeft + event.offsetX;
    s.top = draggedTreeNode.dragTop + event.offsetY;
  }
}

function treeNodeDragCheck(span)
{
  var div = span.parentElement;
  var target = div.dropId;
  draggedTreeNode.target = null;
  if(draggedTreeNode.dropIds[target]){
    var d = div;
    for(;;){
      if(d == draggedTreeNode)
        return true;
      d = d.parentElement.parentElement;
      if(d.treeData == null)
        break;
    }
    if(div.cdcb){
      if(!div.cdcb())
        return true;
    }
    else{
      var cb = div.treeData.cdcb;
      if(cb){
        eval('div.cdcb = function(){' + cb + '}');
        if(!div.cdcb())
          return true;
      }
    }
    draggedTreeNode.target = div;
    return false;
  }
  return true;
}

function treeNodeDragEnd(span)
{
  var div = span.parentElement;
  var treeDiv = div.treeData;
  treeDiv.removeChild(treeDiv.lastChild);
  var target = div.target;
  if(target == null || draggedTreeNode == null)
    return;
  if(target.load)
    loadTreeNode(target, false);
  draggedTreeNode = null;
  var notify = treeDiv.notifyId;
  if(notify){
    var ids = getTreeNodeIds(div);
    var toIds = getTreeNodeIds(target, 'TO_');
    ids = ids.ids + toIds.ids;
    var url = omxContextPath + '/view.x2ps?PAGE_ACTION_ID=' + notify + ids;
    savedThis = div;
    url += eval(treeDiv.params);
    savedThis = null;
    var r = getUpdateHTML_XML(null, 'PAGE_ACTION', url, treeDiv.post == 'true', false);
    var button = r.selectSingleNode('/RESPONSES/RESPONSE/BUTTON_ID/@Value');
    var updateTargetId = r.selectSingleNode('/RESPONSES/RESPONSE/UPDATE_TARGET_ID/@Value');
    if(button || updateTargetId){
      if(updateTargetId)
        updateTargetId = updateTargetId.text;
      if(button)
        button = button.text;
      update(updateTargetId, button, undefined, undefined, 'view.x2ps?' + ids);
    }
  }
  var parent = div.parentElement;
  if(parent.children.length == 1){
    parent = parent.parentElement;
    parent.collapsed = true;
    var img = parent.firstChild;
    img.onclick = null;
    img.src = treeDiv.leafImg;
  }
  if(div.treeData != target.treeData)
    setTreeData(div, target.treeData);
  target.lastChild.appendChild(div);
  if(target.collapsed)
    expandTreeNode(target);
  scrollHelper();
}

function setTreeData(div, data)
{
  div.treeData = data;
  var children = div.lastChild.children;
  var l = children.length;
  while(--l >= 0)
    setTreeData(children[l], data);
}

function navigateChartUrl(url, chartId, field, label, value)
{
  if(url.charAt(0) == '/' && url.indexOf(omxContextPath))
    url = omxContextPath + url;
  var c = url.indexOf('?') > 0 ? '&' : '?';
  url = url + c + 'CHART_CLICK=' + chartId + '&CHART_CLICK_FIELD=' + field + '&CHART_CLICK_LABEL=' + label + '&CHART_CLICK_VALUE=' + value;
  navigateTo(url, 'appFrame');
}

function checkJavaVersion(codebase, jar)
{
  if (document.javaLoaded == null)
    document.javaLoaded = document.createElement('<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" codebase="http://java.sun.com/update/1.6.0/jinstall-6-windows-i586.cab\#Version=1,4,2,13"/>');
}

document.writeOriginal = document.write;

document.write = function(s)
{
  var script = document.all[getCurrentScriptPos()];
  var next = script.nextSibling;
  var parent = script.parentElement;
  try{
    var element = document.createElement(s);
    var script = element.tagName == 'SCRIPT';
    if(script){
      ++scriptLoadingCount;
      element.onreadystatechange = function(){ if(element.readyState == 'complete' && --scriptLoadingCount == 0) finishUpdateHTML(); }
    }
    if(next)
      parent.insertBefore(element, next);
    else
      parent.appendChild(element);
  }
  catch(e)
  {
    return;
  }
}

function findLeft(e)
{
  var l = e.offsetLeft;
  e = e.offsetParent;
  while(e){
    l += e.offsetLeft;
    var t = e.tagName;
    if(t == 'DIV')
      return;
    else if(t == 'TD')
      l += e.clientLeft;
    e = e.offsetParent;
  }
  return l;
}

function findTop(e)
{
  var t = e.offsetTop;
  e = e.offsetParent;
  while(e){
    t += e.offsetTop;
    if(e.tagName == 'DIV')
      return;
    e = e.offsetParent;
  }
  return t;
}

function floatHeader(div)
{
  if(div.init)
    return;
  div.init = true;
  div.setExpression('scrollLeft', 'this.previousSibling.scrollLeft');
  var stable = div.previousSibling.firstChild;
  var rows = stable.rows;
  var ttable = div.firstChild;
  var head = ttable.tHead;
  var l = rows.length;
  for(var i = 0; i < l; ++i){
    var sr = rows[i];
    if(sr.className != 'tableColumnHeadings')
      break;
    var tr = sr.cloneNode(true);
    head.appendChild(tr);
    var std = sr.firstChild;
    var ttd = tr.firstChild;
    while(std){
      ttd.std = std;
      var s = ttd.style;
      s.setExpression('width', 'this.std.offsetWidth');
      ttd = ttd.nextSibling;
      std = std.nextSibling;
    }
  }
}
