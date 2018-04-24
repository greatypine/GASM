var jsMapping = new Object();
jsMapping.b = [ 'PglButton', 'ui:button' ];
jsMapping.bs = [ 'PglButtons', 'ui:buttons' ];
jsMapping.c = [ 'PglContainer', 'ui:container' ];
jsMapping.cr = [ 'PglCalendar', 'ui:calendar' ];
jsMapping.e = [ 'PglMassEntry', 'ui:mass-entry' ];
jsMapping.f = [ 'PglFilter', 'ui:filter' ];
jsMapping.fa = [ 'PglTextArea', 'ui:textarea' ];
jsMapping.fcb = [ 'PglCheckBox', 'ui:checkbox' ];
jsMapping.fcf = [ 'PglCompoundField', 'ui:compound-field' ];
jsMapping.fd = [ 'PglDropdown', 'ui:dropdown' ];
jsMapping.fdr = [ 'PglDateRange', 'ui:date-range' ];
jsMapping.fdt = [ 'PglFieldDisplayText', 'ui:field-display-text' ];
jsMapping.fe = [ 'PglEntryField', 'ui:entry-field' ];
jsMapping.ff = [ 'PglDisplayField', 'ui:display-field' ];
jsMapping.fg = [ 'PglFieldGroup', 'ui:field-group' ];
jsMapping.fi = [ 'PglImageMap', 'ui:image-map' ];
jsMapping.fl = [ 'PglLinkField', 'ui:link-field' ];
jsMapping.fr = [ 'PglFilterRow', 'ui:filter-row' ];
jsMapping.frb = [ 'PglRadioButton', 'ui:radiobutton' ];
jsMapping.h = [ 'PglHiddenField', 'ui:hidden-field' ];
jsMapping.m = [ 'PglDropdown', 'ui:dropdown' ];
jsMapping.mr = [ 'PglMassEntryRow', 'ui:mass-entry-row' ];
jsMapping.r = [ 'PglTableRow', 'ui:table-row' ];
jsMapping.s = [ 'PglStep', 'ui:step' ];
jsMapping.sh = [ 'PglSearch', 'ui:search' ];
jsMapping.t = [ 'PglTable', 'ui:table' ];
jsMapping.ta = [ 'PglTextArea', 'ui:textarea' ];
jsMapping.tcb = [ 'PglCheckBox', 'ui:checkbox' ];
jsMapping.tcf = [ 'PglCompoundField', 'ui:compound-field' ];
jsMapping.td = [ 'PglDropdown', 'ui:dropdown' ];
jsMapping.tdr = [ 'PglDateRange', 'ui:date-range' ];
jsMapping.te = [ 'PglEntryField', 'ui:display-field' ];
jsMapping.tf = [ 'PglDisplayField', 'ui:display-field' ];
jsMapping.ti = [ 'PglImageMap', 'ui:image-map' ];
jsMapping.tl = [ 'PglLinkField', 'ui:link-field' ];
jsMapping.xd = [ 'PglDropdown', 'ui:dropdown' ];
jsMapping.xe = [ 'PglEntryField', 'ui:display-field' ];
jsMapping.ye = [ 'PglEntryField', 'ui:entry-field' ];

function inherit(p)
{
  function Inherit(){};
  Inherit.prototype = p;
  return new Inherit();
}

function addPglChild(p, c, id)
{
  while(p){
    p[id] = c;
    if(p._nopush)
      return;
    p = p._parentObject;
  }
}

function getPglId()
{
  var e = this._element;
  var i = e.id;
  if(i)
    return i;
  i = e.name;
  if(i)
    return i;
  alert('no id ' + e.outerHTML);
  return 'id';
}

function PglChild(js_type, element)
{
  this.js_type = js_type;
  this.element = element;
}

function PglBase()
{
}
PglBase.prototype._init = function(){ }
PglBase.prototype._createRow = function(){ return false; }
PglBase.prototype._overwrite = function(){ return true; }
PglBase.prototype.getChildren = function()
{
  var c = new Array();
  for(var i in this){
    if(i == '_parentObject')
      continue;
    if(this[i] instanceof PglBase)
      c[c.length] = i;
  }
  return c;
}
PglBase.prototype.getId = function() { return null; }
PglBase.prototype.getIndex = function(){ return -1; }
PglBase.prototype.getParent = function(type)
{
  var p = this._parentObject;
  if(type){
    while(p){
      if(p._pgl_type == type)
        return p;
      p = p._parentObject;
    }
  }
  return p;
}
PglBase.prototype.getType = function(){ return this._pgl_type; }
PglBase.prototype.getUpdateId = function()
{ 
  var e = this._element;
  var id = e.id;
  if(e.tagName == 'TD')
    return id.substring(0, id.length - 3);
  if(id.substring(0, 4) == '_un_')
    return id.substring(4);
  return id;
}
PglBase.prototype.toXML = function(xml)
{
  var id = this.getId();
  if(id){
    var n = getXMLCreator().createElement(id);
    if(xml)
      xml.appendChild(n);
    xml = n;
  }
  else if(xml == null)
  {
    xml = getXMLCreator().createElement('DATA');
  }
  for(var i in this){
    if(i == '_parentObject')
      continue;
    var o = this[i];
    if(o instanceof PglBase)
      o.toXML(xml);
  }
  return xml;
}

PglBase.prototype.update = function(disabled, cancellable, async){ update(this.getUpdateId(), null, disabled, cancellable, async); }

function PglStyle()
{
}
PglStyle.prototype = inherit(PglBase.prototype);
PglStyle.prototype.getStyle = function(style)
{
  return this._element.style[style];
}
PglStyle.prototype.setStyle = function(style, value)
{
  if(value)
    this._element.style[style] = value;
  else
    this._element.style.removeAttribute(style);
}
PglStyle.prototype.getCellStyle = PglStyle.prototype.getStyle;
PglStyle.prototype.setCellStyle = PglStyle.prototype.setStyle;

function PglButton()
{
}
PglButton.prototype = inherit(PglBase.prototype);
PglButton.prototype.click = function()
{
  this._element.cells[0].firstChild.click();
}
PglButton.prototype.deemphasize = function()
{
  var e = this._element;
  e.jse = 'false';
  if(e.className == 'buttonBorder'){
    var td = e.cells[0];
    td.id = 'buttonRegular';
  }
}
PglButton.prototype.disable = function()
{
  var e = this._element;
  e.className= 'buttonBorderDisabled';
  var td = e.cells[0];
  td.id = 'buttonDisabled';
  td.className = 'buttonTextDisabled';
  var a = td.firstChild;
  var href = a.href;
  if(href){
    a.href2 = href;
    a.removeAttribute('href');
  }
}
PglButton.prototype.emphasize = function()
{
  var e = this._element;
  e.jse = 'true';
  if(e.className == 'buttonBorder'){
    var td = e.cells[0];
    td.id = 'buttonEmphasized';
  }
}
PglButton.prototype.enable = function()
{
  var e = this._element;
  e.className= 'buttonBorder';
  var td = e.cells[0];
  var id = e.jse == 'true' ? 'buttonEmphasized' : 'buttonRegular';
  td.id = id;
  td.className = 'buttonText';
  var a = td.firstChild;
  a.href = a.href2;
}
PglButton.prototype.getId = getPglId;
PglButton.prototype.isEnabled = function(){ return this._element.className == 'buttonBorder'; }
PglButton.prototype.isEmphasized = function(){ return this._element.jse == 'true'; }

function PglButtons()
{
}
PglButtons.prototype = inherit(PglBase.prototype);
PglButtons.prototype.getId = function(){ return this._element.id.substring(4); }

function PglCalendar()
{
}
PglCalendar.prototype = inherit(PglBase.prototype);
PglCalendar.prototype.getId = function(){ return 'calendar'; }
PglCalendar.prototype.close = function(){ var c = this._element.calendar; if(c == null || c.closed) return; c.close(); }
PglCalendar.prototype.open = function(){ this._element.click(); }

function PglContainer()
{
}
PglContainer.prototype = inherit(PglBase.prototype);
PglContainer.prototype.getId = function(){ return this._element.id.substring(4); }
PglContainer.prototype.collapse = function()
{
  var fc = this._element.firstChild;
  var s = fc.children[1].style;
  if(s.display == 'none')
    return;
  s.display = 'none';
  var td = fc.cells[0];
  if(td.className == 'containerHeader'){
    var img = td.children[0];
    if(img.tagName == 'IMG')
      img.src = i2uiImageDirectory+"/container_expand.gif";
    else
      img.innerText = '+';
  }
  scrollHelper();
}
PglContainer.prototype.expand = function()
{
  var fc = this._element.firstChild;
  var s = fc.children[1].style;
  if(s.display != 'none')
    return;
  s.display = '';
  var td = fc.cells[0];
  if(td.className == 'containerHeader'){
    var img = td.children[0];
    if(img.tagName == 'IMG')
      img.src = i2uiImageDirectory+"/container_collapse.gif";
    else
      img.innerText = '-';
  }
  scrollHelper();
}
PglContainer.prototype.isExpanded = function(){ return this._element.firstChild.children[1].style.display != 'none'; }

function PglField()
{
}
PglField.prototype = inherit(PglStyle.prototype);
PglField.prototype._init = function()
{
  var td = this._element.previousSibling;
  if(td && this.js_type.charAt(0) == 'f' && td.jo == null){
    if(this._parentObject instanceof PglDateRange)
      return null;
    this._nopush = true;
    return new PglChild('fdt', td);
  }
}
PglField.prototype._createRow = function(){ return typeof this._element.i != 'undefined'; }
PglField.prototype.getId = function(){ return this._element.jsn; }
PglField.prototype.getDecimals = function(){ return this._element.jsd; }
PglField.prototype.getDate = function(){ return new Date(getDateFromFormat(this.getValueAsIs(), page_dateFormat, true)); }
PglField.prototype.getDateTime = function(){ return new Date(getDateFromFormat(this.getValueAsIs(), page_dateTimeFormat, true)); }
PglField.prototype.getFieldType = function(){ return this._element.fieldtype; }
PglField.prototype.getNumber = function(){ return toDouble(this.getValueAsIs()); }
PglField.prototype.getNumericPattern = function(){ return this._element.jsnp; }
PglField.prototype.setNumber = function(v){ this.setValueAsIs(formatNumber(v, this.getNumericPattern(), this.getDecimals())); }
PglField.prototype.setDate = function(v)
{
  if(typeof v == 'string')
    v = new Date(stringToDate(v));
  this.setValueAsIs(formatDate(v, page_dateFormat));
}
PglField.prototype.setDateTime = function(v)
{
  if(typeof v == 'string'){
    var l = v.length;
    var i = v.lastIndexOf(':');
    if(i < 0)
      v += ' 00:00:00';
    else if(i == l - 4)
      v = v.substring(0, i);
    v = new Date(getDateFromFormat(v, 'M/d/yyyy HH:mm:ss'));
  }
  this.setValueAsIs(formatDate(v, page_dateTimeFormat));
}
PglField.prototype.getValue = function(v)
{
  switch(this.getFieldType()){
    case 'Constant':
      return this.getValueAsIs();
    case 'Text':
      return this.getValueAsIs();
    case 'Number':
      return this.getNumber();
    case 'Date':
      return this.getDate();
    case 'DateTime':
      return this.getDateTime();
  }
  return this.getValueAsIs();
}
PglField.prototype.setValue = function(v)
{
  switch(this.getFieldType()){
    case 'Constant':
      return this.setValueAsIs(v);
    case 'Text':
      return this.setValueAsIs(v);
    case 'Number':
      return this.setNumber(v);
    case 'Date':
      return this.setDate(v);
    case 'DateTime':
      return this.setDateTime(v);
  }
  this.setValueAsIs(v);
}
PglField.prototype.callback = function(xml)
{
  var v = xml.selectSingleNode('/RESPONSES/RESPONSE/*/@Value');
  if(v)
    this.setValue(v.text);
}
PglField.prototype.refresh = function(post, async)
{ 
  var field = this;
  var url = omxContextPath + '/view.x2ps?PAGE_ACTION_TYPE=Value&PAGE_ACTION_ID='  + this.getId() + '&PAGE_ACTION_VALUE=' + this.getValue() + '&PAGE_ACTION_DATA_TYPE=' + this.getFieldType();
  if(async)
    getUpdateHTML_XML(null, 'PAGE_ACTION', url, post, function(h){ field.callback(h.getXML()); });
  else
    this.callback(getUpdateHTML_XML(null, 'PAGE_ACTION', url, post, null));
}
PglField.prototype.toXML = function(p)
{
  var xml = getXMLCreator().createElement(this.getId());
  xml.setAttribute('Value', this.getValue());
  if(p)
    p.appendChild(xml);
  return xml;
}

function PglFieldDisplayText()
{
}
PglFieldDisplayText.prototype = inherit(PglStyle.prototype);
PglFieldDisplayText.prototype.getId = function(){ return 'displayText'; }
PglFieldDisplayText.prototype.getText = function(){ var e = this._element; return e.innerHTML.substring(0, e.l); }
PglFieldDisplayText.prototype.setText = function(text){ var e = this._element; e.innerHTML = text + e.innerHTML.substring(e.l); e.l = text.length; }
PglFieldDisplayText.prototype.callback = function(xml)
{
  var v = xml.selectSingleNode('/RESPONSES/RESPONSE/*/@Value');
  if(v)
    this.setText(v.text);
}
PglFieldDisplayText.prototype.refresh = function(post, async)
{ 
  var field = this;
  var t = this.getText();
  var l = t.length;
  while(l > 0 && t.charAt(--l) != ':');
  if (l > 0)
    t = t.substring(0, l);
  var url = omxContextPath + '/view.x2ps?PAGE_ACTION_TYPE=DisplayText&PAGE_ACTION_ID='  + this._parentObject.getId() + '&PAGE_ACTION_VALUE=' + t + '&PAGE_ACTION_DATA_TYPE=Text';
  if(async)
    getUpdateHTML_XML(null, 'PAGE_ACTION', url, post, function(h){ field.callback(h.getXML()); });
  else
    this.callback(getUpdateHTML_XML(null, 'PAGE_ACTION', url, post, null));
}

function PglFieldGroup()
{
}
PglFieldGroup.prototype = inherit(PglStyle.prototype);
PglFieldGroup.prototype.getId = function(){ return this._element.id.substring(4); }

function PglCompoundField()
{
}
PglCompoundField.prototype = inherit(PglField.prototype);
PglCompoundField.prototype.getId = function()
{
  var e = this._element;
  if(e.jsn)
    return e.jsn;
  return e.id.substring(0, e.id.length-3);
}

function PglDateRange()
{
  this._nopush = true;
}
PglDateRange.prototype = inherit(PglField.prototype);
PglDateRange.prototype.getFieldType = function() { return this.from.getFieldType(); }
PglDateRange.prototype.callback = function(xml)
{
  var v = xml.selectNodes('/RESPONSES/RESPONSE/*/@Value');
  var l = v.length;
  if(l > 0)
    this.from.setValue(v[0].text);
  if(l > 1)
    this.to.setValue(v[1].text);
}
PglDateRange.prototype.refresh = function(post, async)
{ 
  var field = this;
  var from = formatDate(this.from.getValue(), 'MM/dd/yyyy HH:mm:ss');
  var to = formatDate(this.to.getValue(), 'MM/dd/yyyy HH:mm:ss');
  var url = omxContextPath + '/view.x2ps?PAGE_ACTION_TYPE=Value&PAGE_ACTION_ID='  + this.getId() + '&PAGE_ACTION_VALUE=' + from + '&PAGE_ACTION_VALUE=' + to + '&PAGE_ACTION_DATA_TYPE=' + this.from.getFieldType();
  if(async)
    getUpdateHTML_XML(null, 'PAGE_ACTION', url, post, function(h){ field.callback(h.getXML()); });
  else
    this.callback(getUpdateHTML_XML(null, 'PAGE_ACTION', url, post, null));
}
PglDateRange.prototype.toXML = function(xml)
{
  var xc = getXMLCreator();
  if(xml == null)
  {
    xml = xc.createElement('DATA');
  }
  var id = this.getId();
  var from = xc.createElement('FROM_' + id);
  from.setAttribute('Value', this.from.getValue());
  xml.appendChild(from);
  var to = xc.createElement('TO_' + id);
  to.setAttribute('Value', this.to.getValue());
  xml.appendChild(to);
  return xml;
}
PglDateRange.prototype.clear = function()
{     
  this.from.clear();
  this.to.clear();
}


function PglDisplayField()
{
}
PglDisplayField.prototype = inherit(PglField.prototype);
PglDisplayField.prototype.getValueAsIs = function(){ return this._element.innerText; }
PglDisplayField.prototype.setValueAsIs = function(value){ this._element.innerText = value; }

function PglEntryField()
{
}
PglEntryField.prototype = inherit(PglField.prototype);
PglEntryField.prototype._init = function(){ var r = PglField.prototype._init.call(this); this._input = this._element.children[0]; return r; }
PglEntryField.prototype.clear = function(){ this.setValueAsIs(''); }
PglEntryField.prototype.fireOnChange = function(){ this._input.fireEvent('onchange', document.createEventObject()); }
PglEntryField.prototype.getValueAsIs = function(){ return this._input.value; }
PglEntryField.prototype.getId = function(){ return this._element.jsn; }
PglEntryField.prototype.getCellStyle = PglEntryField.prototype.getStyle;
PglEntryField.prototype.getOldValue = function(){ return this._input.oldvalue; }
PglEntryField.prototype.getStyle = function(style)
{
  return this._input.style[style];
}
PglEntryField.prototype.setValueAsIs = function(v){
  var i = this._input;
  i.value = v;
  if(i.type == 'hidden'){
    var e = this._element;
    e.lastChild.data = v;
  }
}
PglEntryField.prototype.setCellStyle = PglEntryField.prototype.setStyle;
PglEntryField.prototype.setStyle = function(style, value)
{
  var i = this._input;
  var s = i.type == 'hidden' ? this._element.style : i.style;
  if(value)
    s[style] = value;
  else
    s.removeAttribute(style);
}

function PglDropdown()
{
}
PglDropdown.prototype = inherit(PglEntryField.prototype);
PglDropdown.prototype.callback = function(xml)
{
  var v = xml.selectNodes('/RESPONSES/RESPONSE/*');
  var l = v.length;
  var options = this._input.options;
  options.length = 0;
  for(var i = 0; i < l; ++i)
  {
    var o = v[i];
    var option = new Option(o.getAttribute('Value'), o.getAttribute('Id'));
    options[i] = option;
    if(o.getAttribute('Selected'))
      option.selected = true;
  }
}
PglDropdown.prototype.clear = function(){ this.setValueAsIs(''); }
PglDropdown.prototype.clearOptions = function()
{
  this._input.options.length = 0;
}
PglDropdown.prototype.textSelect = function(text)
{
  var input = this._input;
  var s = input.selectedIndex;
  var options = input.options;
  var l = options.length;
  for(var i = 0; i < l; ++i){
    var o = options[i];
    if(o.text == text){
      o.selected = true;
      if(i != s)
        this.fireOnChange();
      break;
    }
  }
}
PglDropdown.prototype.valueSelect = function(value)
{
  var input = this._input;
  var s = input.selectedIndex;
  var options = input.options;
  var l = options.length;
  for(var i = 0; i < l; ++i){
    var o = options[i];
    if(o.value == value){
      o.selected = true;
      if(i != s)
        this.fireOnChange();
      break;
    }
  }
}
PglDropdown.prototype.indexSelect = function(index)
{
  var input = this._input;
  if(input.selectedIndex != index){
    input.selectedIndex = index;
    this.fireOnChange();
  }
}
PglDropdown.prototype.unselectAll = function()
{
  var input = this._input;
  if(input.selectedIndex < 0)
    return;
  input.selectedIndex = -1;
  this.fireOnChange();
}

function PglRadioButton()
{
}
PglRadioButton.prototype = inherit(PglEntryField.prototype);
PglRadioButton.prototype.isChecked = function(){ return this._input.checked; }
PglRadioButton.prototype.clear = function(){   
  var i = this._input;
  i.checked = false;
 }

function PglCheckBox()
{
}
PglCheckBox.prototype = inherit(PglRadioButton.prototype);

function PglTextArea()
{
}
PglTextArea.prototype = inherit(PglEntryField.prototype);

function PglImageMap()
{
}
PglImageMap.prototype = inherit(PglField.prototype);

function PglLinkField()
{
}
PglLinkField.prototype = inherit(PglField.prototype);
PglLinkField.prototype.click = function(){ this._element.firstChild.click(); }
PglLinkField.prototype.getText = function()
{
  var e = this._element;
  var c = e.firstChild ;
  if(c.tagName) 
    return c.innerText;
  return e.innerText;
}
PglLinkField.prototype.setText = function(t)
{
  var e = this._element;
  var c = e.firstChild ;
  if(c.tagName) 
    return c.innerText = t;
  return e.innerText = t;
}
PglLinkField.prototype.getValue = function(){ return this.getText(); }
PglLinkField.prototype.setValue = function(t){ this.setText(t); }

function PglFilter()
{
  this._nopush = true;
}
PglFilter.prototype = inherit(PglBase.prototype);
PglFilter.prototype.clear = function()
{
  if(this.matchBy)
    this._element.firstChild.cells[1].firstChild.click();
  else
    this._element.firstChild.cells[0].firstChild.click();
}
PglFilter.prototype.getId = function(){ return this._element.jsn; }
PglFilter.prototype.apply = function()
{ 
  if(this.matchBy || this.filter.getType() == 'ui:date-range')
    this._element.firstChild.cells[3].firstChild.click();
  else
    this._element.firstChild.cells[2].firstChild.click();
}

function PglFilterRow()
{
}
PglFilterRow.prototype = inherit(PglBase.prototype);
PglFilterRow.prototype._overwrite = function(){ return false; }
PglFilterRow.prototype.getId = function(){ return 'filters'; }

function PglHiddenField()
{
}
PglHiddenField.prototype = inherit(PglField.prototype);
PglHiddenField.prototype._createRow = function(){ return typeof this._element.i != 'undefined'; }
PglHiddenField.prototype.getId = function(){ return this._element.jsn; }
PglHiddenField.prototype.getValueAsIs = function(){ return this._element.value; }
PglHiddenField.prototype.setValueAsIs = function(v){ this._element.value = v; }

function PglMassEntry()
{
  this._nopush = true;
}
PglMassEntry.prototype = inherit(PglBase.prototype);
PglMassEntry.prototype.getId = function(){ return this._element.jsn; }
PglMassEntry.prototype.massUpdate = function()
{
  var id = this.getId();
  var v = this.value.getValueAsIs();
  var table = this.getParent('ui:table');
  var rows = table.getSelectedRows();
  var l = rows.length;
  for(var i = 0; i < l; ++i)
    rows[i][id].setValueAsIs(v);
}

function PglMassEntryRow()
{
}
PglMassEntryRow.prototype = inherit(PglBase.prototype);
PglMassEntryRow.prototype._overwrite = function(){ return false; }
PglMassEntryRow.prototype.getId = function(){ return 'massEntries'; }

function PglPage()
{
}
PglPage.prototype = inherit(PglBase.prototype);
PglPage.prototype.getId = function(){ return 'page-group'; }

function PglTable()
{
  this._nopush = true;
}
PglTable.prototype = inherit(PglBase.prototype);
PglTable.prototype.collapse = function()
{
  var fc = this._element.cells[0].firstChild;
  var s = fc.children[1].style;
  if(s.display == 'none')
    return;
  s.display = 'none';
  var td = fc.cells[0].firstChild.cells[0];
  if(td.className == 'containerHeaderLeft'){
    var img = td.children[0];
    if(img.tagName == 'IMG')
      img.src = i2uiImageDirectory+"/container_expand.gif";
    else
      img.innerText = '+';
  }
  scrollHelper();
}
PglTable.prototype.expand = function()
{
  var fc = this._element.cells[0].firstChild;
  var s = fc.children[1].style;
  if(s.display != 'none')
    return;
  s.display = '';
  var td = fc.cells[0].firstChild.cells[0];
  if(td.className == 'containerHeaderLeft'){
    var img = td.children[0];
    if(img.tagName == 'IMG')
      img.src = i2uiImageDirectory+"/container_collapse.gif";
    else
      img.innerText = '-';
  }
  scrollHelper();
}
PglTable.prototype.isExpanded = function(){ return this._element.cells[0].firstChild.children[1].style.display != 'none'; }
PglTable.prototype.getId = function(){ return this._element.id.substring(4); }
PglTable.prototype.getSelectedRows = function()
{
  var r = new Array();
  var rs = this.rows;
  var l = rs.length;
  for(var i = 0; i < l; ++i){
    var t = rs[i];
    if(t.isSelected())
      r[r.length] = t;
  }
  return r;
}
PglTable.prototype.getTotalRowCount = function(){ return parseInt(this._element.jsrc); }
PglTable.prototype.unselectAll = function()
{
  var rs = this.getSelectedRows();
  var l = rs.length;
  for(var i = 0; i < l; ++i)
    rs[i].unselect();
}

function PglTableRow()
{
  this._nopush = true;
}
PglTableRow.prototype = inherit(PglBase.prototype);
PglTableRow.prototype.getId = function(){ return 'rows'; }
PglTableRow.prototype.getIndex = function(){ return parseInt(this._element.i); }
PglTableRow.prototype.isSelected = function(){ return this._element.cells[0].firstChild.checked; }
PglTableRow.prototype.nextRow = function(){ return this.getParent().rows[this.getIndex() + 1]; }
PglTableRow.prototype.previousRow = function(){ return this.getParent().rows[this.getIndex() -1]; }
PglTableRow.prototype.select = function()
{
  var i = this._element.cells[0].firstChild;
  if(i && i.tagName == 'INPUT' && i.checked == false)
    i.click();
}
PglTableRow.prototype.unselect = function()
{
  var i = this._element.cells[0].firstChild;
  if(i && i.tagName == 'INPUT' && i.checked)
    i.click();
}

function PglStep()
{
}
PglStep.prototype = inherit(PglBase.prototype);
PglStep.prototype.getId = function()
{
  return this._element.stepid;
}

function PglSearch()
{
}
PglSearch.prototype = inherit(PglBase.prototype);
PglSearch.prototype.getId = function(){ return this._element.id.substring(4); }
PglSearch.prototype.search = function(){ this[this.getId() + '_button'].click(); }
PglSearch.prototype.clear = function()
{
  for(var i in this){
    if(i == '_parentObject')
      continue;
    if (this[i] instanceof PglField)
    {
      var objType = this[i].getType();
      if(objType == 'ui:entry-field' || objType == 'ui:dropdown' || objType == 'ui:date-range' || objType == 'ui:checkbox' || objType == 'ui:radiobutton' || objType == 'ui:textarea')
      {
        this[i].clear();
      }
    }
  }
}

var rootObject = null;

function endIndex(e)
{
  if(e){
    var n = e.nextSibling;
    if(n){
      n = n.sourceIndex;
      if(n)
        return n;
    }
    return endIndex(e.parentElement);
  }
  return document.all.length;
}

function root()
{
  if(rootObject)
    return rootObject;
  function setValues(o, m, j, e, c, x)
  {
    o._pgl_type = m;
    o.js_type = j;
    o._element = e;
    o._parentObject = c;
    o.endIndex = x;
  }
  var all = document.all;
  var l = all.length;
  var b = document.body;
  rootObject = new PglPage();
  rootObject._pgl_type = 'ui:page-group';
  rootObject._element = b;
  rootObject._parentObject = null;
  rootObject.endIndex = l;
  var previous = null;
  var c = rootObject;
  var i = b.sourceIndex;
  while(++i < l){
    while(i >= c.endIndex)
      c = c._parentObject;
    var e = all[i];
    var j = e.js;
    while(j){
      var m = jsMapping[j];
      var o = eval('new ' + m[0] + '();');
      e.jo = o;
      setValues(o, m[1], j, e, c, endIndex(e));
      var x = o.getIndex();
      if(x >= 0){
        var id = o.getId();
        var array = c[id];
        if(array){
          var t = array[x];
          if(t){
            t.endIndex = o.endIndex;
            o = t;
          }
        }
        else
          c[id] = array = new Array();
        array[x] = o;
      }
      else if(o._createRow()){
        var p = o.getParent('ui:table');
        var rows = p.rows;
        if(rows == null)
          p.rows = rows = new Array();
        var x = e.i;
        var row = rows[x];
        if(row == null){
          rows[x] = row = new PglTableRow();
          setValues(row, 'ui:table-row', 'r', e, p, o.endIndex);
          row._init();
        }
        else
          row.endIndex = o.endIndex;
        o._parentObject = row;
        addPglChild(row, o, o.getId());
      }
      else{
        var id = o.getId();
        var old = null;
        if(o._overwrite() || (old = c[id]) == null)
          addPglChild(c, o, o.getId());
        else{
          c = old;
          c.endIndex = endIndex(e);
          break;
        }
      }
      c = o;
      var w = c._init();
      if(w){
        j = w.js_type;
        e = w.element;
        if(previous == null)
          previous = c;
      }
      else{
        if(previous)
          c = previous;
        previous = null;
        break;
      }
    }
  }
  return rootObject;
}

function current()
{
  root();
  var e = this.tagName ? this : event ? event.srcElement : savedThis;
  while(e){
    var o = e.jo;
    if(o)
      return o;
    e = e.parentElement;
  }
  return null;
}
