/** Global Javascript **/
var isIE = document.all?true:false;
var isNS = document.layers?true:false;
var omxContextPath = null;
var returnUrl = null;
var selectedNode = null;

/* Vertical Tree Functions */
function clickNode(node) {
if ( node.open == 'never')
{
//Just sleep

}
else {
  if(node.open == "false") {
    expand(node, true);
  }
  else {
    collapse(node);
  }
 }
  selectedNode = node;
  window.event.cancelBubble = "true";
}
 
function expand(node) {
  var stateImage;
  var oImage
 
  stateImage = document.all["stateImage" + node.id];
  
  oImage = node.childNodes(0).all["image"]
  oImage.src = node.imageOpen
 
  if(stateImage)
  {
  stateImage.src = stateImage._open;
  }
 

  for(i=0; i < node.childNodes.length; i++) {
    if(node.childNodes(i).tagName == "DIV") {
      node.childNodes(i).style.display = "block";
    }
  }
  
   node.open = "true"
}
 
function collapse(node) {
  var stateImage;
  var oImage
  var i
 
  stateImage = document.all["stateImage" + node.id];
 
  oImage = node.childNodes(0).all["image"]
  oImage.src = node.image
 
  if(stateImage)
  {
  stateImage.src = stateImage._closed;
  }
 
  for(i=0; i < node.childNodes.length; i++) {
      if(node.childNodes(i).tagName == "DIV") {
        if(node.id != "folderTree") node.childNodes(i).style.display = "none"
        //collapse(node.childNodes(i))
      }
    }
    
  node.open = "false"
}
 
function expandAll(node) {
  var oImage
  var i
 
  expand(node, false)
 
  for(i=0; i < node.childNodes.length; i++) {
    if(node.childNodes(i).tagName == "DIV") {
      expandAll(node.childNodes(i))
    }
  }
}
 
 /* Vertical Tree related functions */
 
/*
  omxSetContextPath() defines the context path directory 
  that js files would use.
  Compatibility:  IE, NS6, NS4
*/
    
function omxSetContextPath(contextPath)
{
  omxContextPath = contextPath;
}

function validateZipCode(e) 
{
    var _ret = true;
    if (isIE)
    {
        if ((window.event.keyCode == 45)||(window.event.keyCode > 46 && window.event.keyCode < 58)) 
        {
            _ret = true;
        }else
        {
            window.event.keyCode = 0;
            _ret = false;
        }
      }
    if (isNS) 
    {
      if ((e.which == 45 )||(e.which > 46 && e.which < 58)) 
      {
        _ret = true;
      }else
      {
            e.which = 0;
          _ret = false;
      }
    }
    return (_ret);
    }



/** Trim Function **/
function trimString (str) {
  str = this != window? this : str;
  return str.replace(/^\s+/g, '').replace(/\s+$/g, '');
}
String.prototype.trim = trimString;


/** Debug Function **/
function objectToString(obj) 
{  
  var ret = "Object " + obj.name + " is [";
  for (var prop in obj)      
    ret += "  " + prop + " is " + obj[prop] + ";" ;
     return ret + "]"
}

function core_prompt(fieldLabel, pageTitle)
{
  var args = new Array();
  args[0] = ['FIELD_LABEL', fieldLabel];
  args[1] = ['PAGE_TITLE', pageTitle];
  args[2] = ['TYPE', 'success'];
  var url = createEncodedUrl(omxContextPath+"/core/prompt/controller/display.x2c", args);
  var rc = i2uiShowMessageBox(url,150,350);
  return rc ;
}

function core_success(msg)
{
  var args = new Array();
  args[0] = ['MESSAGE', msg];
  var arg_val;
  for(var i=1; i<arguments.length; i++)
  { 
    arg_val = i-1;
    args[i] = ['ARG'+arg_val, arguments[i]];
  }
  args[args.length] = ['TYPE', 'success'];
  var url = createEncodedUrl(omxContextPath+"/core/alert/controller/display.x2c", args);
  var rc = i2uiShowMessageBox(url,150,350);
  return rc ;
}

/*
  Handles extra parameters as arguments to the message string.
  Ex. core_alert(msg, arg0, arg1, arg2, ...);
  */
function core_alert(msg)
{
  var args = new Array();
  args[0] = ['MESSAGE', msg];
  
  var arg_val;
  for(var i=1; i<arguments.length; i++)
  { 
    arg_val = i-1;
    args[i] = ['ARG'+arg_val, arguments[i]];
  }
  var url = createEncodedUrl(omxContextPath+"/core/alert/controller/display.x2c", args);
 
  var rc = i2uiShowMessageBox(url,150,350);
  return rc ;
}

function core_alert_base(msg, i18n, scroll)
{
  if (scroll == null) scroll="false";
  if (i18n == null) i18n="true";
  
  var args = new Array();
  args[0] = ['MESSAGE', msg];
  args[1] = ['SCROLLING', scroll];
  args[2] = ['I18N', i18n];
  var url = createEncodedUrl(omxContextPath+"/core/alert/controller/display.x2c", args);
  var rc = i2uiShowMessageBox(url,150,350);
  return rc ;
}

function core_warning(msg)
{
  var args = new Array();
  args[0] = ['MESSAGE', msg];
  var arg_val;
  for(var i=1; i<arguments.length; i++)
  { 
    arg_val = i-1;
    args[i] = ['ARG'+arg_val, arguments[i]];
  }
  args[args.length] = ['TYPE', 'warning'];
  var url = createEncodedUrl(omxContextPath+"/core/alert/controller/display.x2c", args);

  var rc = i2uiShowMessageBox(url,150,350);
  return rc ;
}


/* This alert msg is used before the user has logged in */
function core_login_alert(msg)
{
  var args = new Array();
  args[0] = ['MESSAGE', msg];
  var arg_val;
  for(var i=1; i<arguments.length; i++)
  { 
    arg_val = i-1;
    args[i] = ['ARG'+arg_val, arguments[i]];
  }
  args[args.length] = ['VALIDATE_LOGIN', 'NO'];
  var url = createEncodedUrl(omxContextPath+"/core/alert/controller/display.x2c", args);
  var rc = i2uiShowMessageBox(url,150,350);
  return rc ;
}

function core_confirm(msg)
{
  var args = new Array();
  args[0] = ['MESSAGE', msg];
  var arg_val;
  for(var i=1; i<arguments.length; i++)
  { 
    arg_val = i-1;
    args[i] = ['ARG'+arg_val, arguments[i]];
  }
  args[args.length] = ['ALERT_TYPE', 'CONFIRM'];
  var url = createEncodedUrl(omxContextPath+"/core/alert/controller/display.x2c", args);
  var rc = i2uiShowMessageBox(url,150,350);
  return rc ;
}


function omx_alert(msg)
{
  core_alert(msg);
}

function omx_confirm(msg,funct)
{
  core_confirm(msg);
}

/*  Void functions */
function onHelp()
{
}
function onImage()
{
}
function onCalendar()
{
}
function onLink()
{
}
/* Void functions */

function onBack()
{
  history.back();
}
function onClose()
{
  window.close();
}


function onRefresh()
{
  javascript:parent.history.go(0);
}

function onCloseAndRefresh(location)
{

  if (location != null && location != '')
  {
     if (parent.opener.top.appFrame != null)
     {
       parent.opener.top.appFrame.location = location;
       top.window.close()
      }
     else if (parent.opener != null)
     {
       parent.opener.location = location;
       top.window.close()
      }
     else
      window.location = location;

  }
  else
  {
    if (parent.opener.top.appFrame != null)
    {
      parent.opener.top.appFrame.history.go(0);  
      top.window.close();
    }
    else if (parent.opener != null)
    {
      parent.opener.history.go(0);
      top.window.close()
    }

  }  
  
  
}

      function centerPopup(w,h)
      {
        resizeTo(w,h);
        moveTo((screen.availWidth-w)/2,(screen.availHeight-h)/2)
      }
      

/** closes the popUp if open, and launches a new popUp window **/
function popUpWindow(url,popUp,queryParams) 
{
  var height = 450;
  var width = 600;
  var isScrollable = 'yes';
  var isResizable = 'yes';
    
  if (popUp=="popUp2") 
  {
    height = 450;
    width = 600;
    isScrollable = 'yes';
    isResizable = 'yes';                
  }       
  else if (popUp=="popUp3") 
  {
    height = 275;
    width = 400;
    isScrollable = 'yes';
    isResizable = 'no';                    
  }
  else if (popUp=="popUp4") 
  {
    height = 600;
    width = 800;
    isScrollable = 'yes';
    isResizable = 'yes';                    
  }
  else if (popUp=="popUp5") 
  {
    height = 130;
    width = 390;
    isScrollable = 'no';
    isResizable = 'no';                    
  }
  else if (popUp=="popUp6") 
  {
    height = 130;
    width = 390;
    isScrollable = 'no';
    isResizable = 'no';                    
  }
  else if (popUp=="orderTree") 
  {
    height = 450;
    width = 600;
    isScrollable = 'no';
    isResizable = 'yes';                    
  }
  else if (popUp=="catalogPopup") 
  {
    height = 450;
    width = 800;
    isScrollable = 'yes';
    isResizable = 'no';                
  }
  else if (popUp=="configurePopup")
  {
    height = 300;
    width = 570;
    isScrollable = 'yes';
    isResizable = 'no';                    
  }
  else if (popUp=="absePopup") 
  { 
    height = 450;
    width = 800;
    isScrollable = 'yes';
    isResizable = 'no';                
  }        
  else if (popUp=="orderPopup") 
  { 
    height = 500;
    width = 800;
    isScrollable = 'no';
    isResizable = 'no';                
  }
  else if (popUp=="alertPopup") 
  {
    height = 120;
    width = 332;
    isScrollable = 'no';
    isResizable = 'no';                    
  }
  else if (popUp=="planPopUp") 
  {
    height = 410;
    width = 800;
    isScrollable = 'yes';
    isResizable = 'yes';                    
  }
  else if (popUp=="lotDetailsPopUp") 
  {
    height = 350;
    width = 680;
    isScrollable = 'yes';
    isResizable = 'yes';                    
  }
  
  popUpWindow_( url, queryParams, popUp, height, width, isScrollable, isResizable );                
}

var popupWindows = new Array();

function popUpWindow_(url, queryParams, popup, height, width, isScrollable, isResizable )
{
  var popupWindow = popupWindows[ popup ];
  
  if ( (popupWindow != null) && (!popupWindow.closed) )  
  { 
    var windowURL = popupWindow.location.href.toString();
    if (windowURL.indexOf(url)>0) 
    {
      popupWindow.focus();
      return;
    } 
    else 
    {
      popupWindow.close();
    }
  }

  var h = screen.availHeight;
  var w = screen.availWidth;
  var left = (w-width)/2;
  var top = (h-height)/2;
  
  var popupParams = 'height=' + height;
  popupParams += ', width=' + width;
  popupParams += ', top=' + top;
  popupParams += ', left=' + left;
  popupParams += ', scrollbars=' + isScrollable;
  popupParams += ', resizable=' + isResizable;  
  
  popupWindow = window.open(createEncodedUrl(escapeUrlParamValues(url), queryParams), popup, popupParams );
  popupWindow.title = 'i2 technologies';
  popupWindows[ popup ] = popupWindow;
}

function popUpWindowSubmit(url, popUp)
{
  var target = document.form.target;
  var action = document.form.action;
  
  popUpWindow(url,popUp);
  
  document.form.action = url;
  document.form.target = popUp;
  document.form.submit();
  
  document.form.action = action;
  document.form.target = target;
  
  return;
}

// 10.2.2008 WJD - Not used in core javascripts
function escapeForUrl(p)
{
  return escapeUrlParamValues(p).replace(/\+/g, '%2B');
}

// 10.2.2008 WJD - Not used in core javascripts
function escapeUrlParamValues(url)
{
  var queryStrIndex = url.indexOf("?");
  var urlPath = url;  
  if (queryStrIndex > -1)
  {
    urlPath = url.substring(0, queryStrIndex);
    urlPath += "?";
    var queryStr = url.substring(queryStrIndex+1, url.length);

    if(queryStr.length > 0)
    {
      var keyValuePairs = queryStr.split("&");
      for(var i=0; i < keyValuePairs.length; i++) 
      {
        keyValuePair = keyValuePairs[i].split("=");

        var param = keyValuePair[0];
        if(keyValuePair.length > 1)
        {
          var value = encodeURIComponent(keyValuePair[1]);
          urlPath += (param + "=" +value);
        }
        else
        {
          urlPath += param;
        }

        if (i+1 < keyValuePairs.length)
        {
          urlPath += "&";
        }
      }
    }
  }
  
  return urlPath;
}

function createEncodedUrl(url, queryParams)
{
  if (queryParams == null)
    return url;

  var urlPath = url;
  var queryStrIndex = url.indexOf("?");
  if (queryStrIndex < 0)
  {
    urlPath += "?";
  }

  var l = queryParams.length;
  for(i = 0; i < l; i++)
  {
    var param = queryParams[i];
    if(param.length > 1)
    {
      var value = encodeURIComponent(param[1]);
      urlPath += (param[0] + "=" +value);
    }
    else
    {
      urlPath += param[0] + "=";
    }
    if (i+1 < l)
    {
      urlPath += "&";
    }
  }
  return urlPath;
}

/** checks to see if any popUp windows are currently open - if so, sets the focus to the popUp **/
function checkForPopUps() {
    if (top.window.popUp2 && !top.window.popUp2.closed) top.window.popUp2.focus();      
    else if (top.window.popUp3 && !top.window.popUp3.closed) top.window.popUp3.focus();
    else if (top.window.lotDetailsPopUp && !top.window.lotDetailsPopUp.closed) top.window.lotDetailsPopUp.focus();
    //  else if (top.window.popUp4 && !top.window.popUp4.closed) top.window.popUp4.focus();
}



/** if the F2 key is pressed, the form is submitted. **/
function mappedKeyCheck() {
    if (event.keyCode==113) {
        // document.inputForm.submit();
        parent.footer.nextTab(parent.footer.array_tabs);
    }
}


/** moves from one horizontal tab to the next, closes the window when last tab is reached **/
function nextTab(array_tabs) {
    for (var i=0; i<array_tabs.length; i++) {
        if (top.content.location.href.indexOf(array_tabs[i])>0) {
            if (i==array_tabs.length-1) {
                top.window.close();
            }
            else {
                top.content.location = array_tabs[i+1];
            }
        }
    }
}


/** switches the application navigation links on the header frame **/
function updateHeader(type) {
    var link1 = '<a href="#" onClick="parent.content.location=\'home_content.jsp\'" class="applicationHeader">OMS Home</a>';
    var link2 = '<a href="#" onClick="parent.content.location=\'saved_trans_orders_content.jsp\'" class="applicationHeader">Saved Transactions</a>';
    var link3 = '<a href="#" onClick="popUpWindow(\'add_book_search_content.jsp\',\'popUp2\')" class="applicationHeader">Address Book</a>';
    var link4 = '<a href="#" onClick="parent.content.location=\'worksheet_content.jsp\'" class="applicationHeader">New Order</a>';  
    var link6 = '<span class="applicationHeader">Help</span>';
    var link7 = '<span class="applicationHeader">User Administration</span>';
    var link8 = '<span class="applicationHeader">Preferences</span>';
    var link9 = '<a href="#" onClick="popUpWindow(\'catalog_content.jsp\',\'popUp2\')" class="applicationHeader">Catalog</a>';
    var spacer = '&nbsp;&nbsp;&nbsp;'   
    if (type=='home' && top.header.document.all['appNav']) top.header.document.all['appNav'].innerHTML = link1+spacer+link7+spacer+link8+spacer+link6;
    else if (type=='review') top.header.document.all['appNav'].innerHTML = link1+spacer+link6;
    else top.header.document.all['appNav'].innerHTML = link1+spacer+link2+spacer+link9+spacer+link3+spacer+link4+spacer+link8+spacer+link6;
}


function goHome(url)
{
    document.location.href = url;
}

//this function needs to be removed once we get rid of frames
function goHomeWithFrames(url)
{
    parent.document.location.href = url;
}


/** Hide/Show Navigation Frame **/
/** Hide/Show Navigation Frame **/
function initFrameToggleGif(path) {
    var frameCol = top.i2ui_shell_content.document.body.cols;
    
    if ( frameCol.charAt(0) == "0") {
        if (document.all) {
            document.header.toggle.alt = "Show Navigation Frame";
            document.header.toggle.src = omxContextPath + "/i2/images/expand.gif";
        }
    }
    else
    {
        if (document.all) {
            document.header.toggle.alt = "Hide Navigation Frame";
            document.header.toggle.src = omxContextPath + "/i2/images/collapse.gif";
        }
    }
}


function togglenav(path) {
    var frameCol = top.i2ui_shell_content.cols;
    
    if ( frameCol.charAt(0) == "0") {
        if (document.all) {
            document.header.toggle.alt = "Hide Navigation Frame";
            document.header.toggle.src = omxContextPath + "/i2/images/collapse.gif";
            top.i2ui_shell_content.cols="170,*";
            tabShow = 0;
            return;
        }
    }
    else
    {    
        if (document.all) {
            document.header.toggle.alt = "Show Navigation Frame";
            document.header.toggle.src = omxContextPath + "/i2/images/expand.gif";
            top.i2ui_shell_content.cols="0%,100%";
            tabShow = 1;
        }
    }
}


/******************** Resizing Containers ******************/
function initContainer(container_id)
{
    i2uiResizeScrollableContainer(container_id,document.body.offsetHeight - 100, null, document.body.offsetWidth - 20, true, 'yes');
    i2uiToggleContentUserFunction = "resizeContainer";
}

function resizeContainer(container_id)
{
    i2uiResizeScrollableContainer(container_id,document.body.offsetHeight - 100, null, document.body.offsetWidth - 20, true, 'yes');
}

/******************** Resizing Containers In Popup******************/
function initContainerPopup(container_id)
{
    i2uiResizeScrollableContainer(container_id,document.body.offsetHeight - 100, null, document.body.offsetWidth - 35, true, 'yes');
}

function resizeContainerPopup(container_id)
{
    i2uiResizeScrollableContainer(container_id,document.body.offsetHeight - 100, null, document.body.offsetWidth - 35, true, 'yes');
}

function toggleCheckboxes(_form, _field, _all)
{
    var numOfAlerts = _field.length;
    var select = _all.checked;
    var selectedAlerts = 0;
    
    if ( numOfAlerts == 1 )
    {
        if ( _field.disabled == false )
        {
            eval("_field.checked = " + select);
        }
    }
    
    if ( numOfAlerts > 1 )
    {
        for(i=0; i<numOfAlerts; i++)
        {
            if ( _field[i].disabled == false )
            {
                eval("_field[" + i + "].checked = " + select);
            }
        }
    }
    else 
    {
        if ( _field.disabled == false )
        {
            _field.checked = select;
        }
    }
    return;
}


function onlyInteger()
{
    onlyValidCharacters(/[0123456789]/);

}

function onlyCurrency()
{
    onlyValidCharacters(/[0123456789.,]/);
}

function onlyValidCharacters(validCharacters)
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


function isBlankField(field)
{
  var charCount=0;
  for (var i=0; i < field.value.length; i++)
  {
    if (field.value.charAt(i) == " ")
       charCount++;
  }
  if (charCount == field.value.length)
     return true;
  else return false;
}

function hideUpdateButton(oForm, sButtonId)
{
    var foundInputField = false;
    
    // See if form has any input field
    var elem = oForm.elements.length;

        for(i = 0; i < elem; i++)
    {
      // ignore hidden and checkbox/radio
      if (oForm.elements[i].type != 'hidden' && oForm.elements[i].type != 'checkbox' &&  oForm.elements[i].type != 'radio')
      {
        // ignore pagination
        if(oForm.elements[i].name != 'pagenum')
        {
          foundInputField = true;
        }  
      } 
        
    }

    if (foundInputField == false)
    {
     i2uiToggleItemVisibility(sButtonId, 'hide');
    }
}


function validate_form(formName)
{
    var bError = false;
    var input = "document." + formName + ".REQ_FIELDS.value";
    var reqFields = new String(eval(input));
    temp = reqFields.split(",");
    var i=0 , j=0;
    var locstr = "?";
    var param = new String("MISSING_FIELD");
    
    for(i=0; i < temp.length; i++)
        {
            var fieldName = new String(temp[i]);
            var docInput = "document." + formName + "." + fieldName + ".value";
        var docInputField = eval("document." + formName + "." + fieldName);
            if((eval(docInput) == "") || (isBlankField(docInputField)))
                {
                    j++;
                    if(fieldName != "UPDATE")
                        {
                            bError = true;
                        }
                    str = param; 
                    if(j == 1)
                        {
                            locstr += str ;
                            locstr += "=" ;
                            locstr += fieldName;
                        }
                    else
                        {
                            locstr += "&" ;
                            locstr += str ;
                            locstr += "=" ;
                            locstr += fieldName;
                        }
                }
                /* locstr += "&NO_OF_PARAM" ;
                                   locstr += "=" ;
                                   locstr += j; */
        }
    
    if(bError == true)
        {
            var action = "document." + formName + ".action";
            action = action + " = \"" + omxContextPath + "/core/alert/redirectToAlert.x2c" + locstr + "\"" + ";";

            eval(action);
            var submit = "document." + formName + ".submit()";
            eval(submit);
                /*locstr = "redirectToAlert.x2c" + locstr;
                                  popUpWindow(locstr,'popUp5')*/
             return false;
        }
    
    return true;
}


function validate_form_new(formName)
{
    var bError = false;
    var input = "document." + formName + ".REQ_FIELDS.value";
    var reqFields = new String(eval(input));
    temp = reqFields.split(",");
    var i=0 , j=0;
    var locstr = "?";
    var param = new String("MISSING_FIELD");
    
    for(i=0; i < temp.length; i++)
        {
            var fieldName = new String(temp[i]);
            var docInput = "document." + formName + "." + fieldName + ".value";
            if(eval(docInput) == "")
                {
                    j++;
                    if(fieldName != "UPDATE")
                        {
                            bError = true;
                        }
                    str = param; 
                    if(j == 1)
                        {
                            locstr += str ;
                            locstr += "=" ;
                            locstr += fieldName;
                        }
                    else
                        {
                            locstr += "&" ;
                            locstr += str ;
                            locstr += "=" ;
                            locstr += fieldName;
                        }
                }
                /* locstr += "&NO_OF_PARAM" ;
                                   locstr += "=" ;
                                   locstr += j; */
        }
    
    if(bError == true)
        {
            var action = "document." + formName + ".action";
            action = action + " = \"" + omxContextPath + "/em/common/reload.x2c" + locstr + "\"" + ";";
            eval(action);
            var submit = "document." + formName + ".submit()";
            eval(submit);
            return false;
        }
    
    return true;
}

// checks if any check box is selected other than one called SELECT_ALL
function ifAnyChecked(form , checkBoxName)
{
    var count;
    var temp =  "document." + form + ".elements.length" ;
    var elementsLen = eval(temp);
    var foundChecked = false;
    
    for(count = 0; count < elementsLen; count++)
        {
            var type = "document."+ form + ".elements[" + count + "].type";
            type = eval(type);
            var checked = "document."+ form + ".elements[" + count + "].checked";
            checked = eval(checked);
            var name = "document."+ form + ".elements[" + count + "].name" ;
            name = eval(name);
            //alert("type = " + type + " checked = " + checked + " name = " + name);
            if( 
               
                 type == "checkbox" &&
                 checked == true &&
                 name != "SELECT_ALL" 
               )
               {  
                 if (checkBoxName == null)
                 {
                   foundChecked = true;break;
                 }
                 else
                 {
                   if (name == checkBoxName)
                   {
                     foundChecked = true;break;
                   }
                 }  
               }
        }
    return foundChecked;
}

function ifAnySelected(form)
{
    var count; 
    var elementsLen = form.elements.length;
    var foundChecked = false;
    
    for(count = 0; count < elementsLen; count++)
        {
            if( 
                 form.elements[count].type == "radio" &&
                 form.elements[count].checked == true
               )
               {  
                  foundChecked = true;
                  break;
               }
        }
    return foundChecked;
}



function redirectToOrgTreeView()
{
  parent.location= omxContextPath + "/em/org/pages/redirectToOrgTreeView.x2c";

}

function changeCountry(formName)
{
  var action = "document." + formName + ".action";
  action = action + "=" + "\"" + omxContextPath + "/omx/common/changeCountry.x2c" + "\"";
  //alert(action);
  eval(action);
  var submit = "document." + formName + ".submit()";
  eval(submit);
  return;
}

function reset(form)
{
  form.action = omxContextPath + "/core/common/reset.x2c"
  form.submit();
}

// call this script on onload of the jsp and it will set the tab indices for your form 
function setTabIndex()
{
    var numberOfForms = document.forms.length ;
    //alert("Number of forms = " + numberOfForms);
    var tabindex=1;
    for (formCount=0;formCount<numberOfForms;formCount++)
    {
        var formName=document.forms[formCount];
        var elementsLen = formName.elements.length;             
        for (count=0;count<elementsLen;count++)
        {
            var elem = formName.elements[count];
            var elemtype = formName.elements[count].type.toLowerCase();
        if (elemtype == "text" || elemtype == "checkbox" || elemtype == "select-one" || elemtype =="image")
            {
                elem.tabIndex=tabindex++;
            }
        }
    }
    return ;
}

// Set Focus
function setFocus()
{
    var numberOfForms = document.forms.length ;
    var flag = "true" ;
    for (formCount=0;formCount<numberOfForms;formCount++)
    {
    if (flag == "true")
    {
        //  alert("Number  = " + formCount);
        var formName1=document.forms[formCount];
        var elementsLen1 = formName1.elements.length;   
        for (count=0;count<elementsLen1;count++)
        {
            //    alert("count  = " + count);
            var elem1 = formName1.elements[count];    
            var elemtype1 = formName1.elements[count].type.toLowerCase();
            //alert("formName1 and element type from focus = " + formName1.name +": " + elemtype1);
            if (elemtype1 == "text" || elemtype1 == "checkbox" || elemtype1 == "select-one" || elemtype1 =="image")
                {
                formName1.elements[count].focus();  
            flag = "false" ;
                break;
            }
        }
        }
    }
    return ;
}


// Pagination
function jumpTo(actionName, startCount, search_form, page_form)
{
     var maxRows = 10;
     if (search_form.MAX_ROWS != null) maxRows = search_form.MAX_ROWS.value;
     if (page_form.MAX_ROWS != null)  maxRows = page_form.MAX_ROWS.value;

  var nextCount = parseInt(startCount) + maxRows;
    var prevCount = 0;

    if ( parseInt(startCount) > 0 )
        prevCount = parseInt(startCount) - maxRows;

  if (startCount == null)
      startCount=page_form.pagenum.value;

    var pagenum= parseInt(startCount);  pagenum--;

    if (actionName == "jump")
        {
            if(
            (page_form.RECORD_COUNT.value == 0 || page_form.RECORD_COUNT.value > pagenum*maxRows)  &&
             (pagenum+1>0) &&
               (page_form.START_COUNT.value != pagenum*maxRows)
               )
                {
                  if (search_form != null)
                  {
                    search_form.reset();
                    search_form.START_COUNT.value=pagenum*maxRows;
                    search_form.submit();
                  }
                  else
                  if (page_form != null)
                  {
                    page_form.START_COUNT.value=pagenum*maxRows;
                    page_form.submit();
                  }
                }
        }
}


// other than SELECT_ALL
function highlightSelectedRows(form)
{
    var count; 
    var elementsLen = form.elements.length;
    var foundChecked = false;
    
    for(count = 0; count < elementsLen; count++)
        {
          obj = form.elements[count];
            if( 
               obj.type == "checkbox" &&
               obj.checked == true  &&
               obj.name != "SELECT_ALL" 
               )
                {  
                    foundChecked = true;
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
                    if (rowobj != null)
                    {
                      rowobj.className = "rowHighlight";
                      rowobj.cells[0].childNodes[0].style.backgroundColor = "";
                    }
                }
        }
    return foundChecked;
}

/* Resizing Helpers - Start::*/
 function getContainerHeight(id)
 {
  tcontainer = document.getElementById(id);
  if (tcontainer == null)
    return 0;
  else
    return tcontainer.offsetHeight;
 }

 function getContainerScrollAreaOffsetHeight(id)
 {
  tcontainer = document.getElementById(id+'_scroller');
  if (tcontainer == null)
    return 0;
  else
  {
    return tcontainer.offsetHeight;
  }  

 }
 function getContainerConstantHeight(id)
 {
  tcontainer = document.getElementById(id);
  if (tcontainer == null)
    return 0;
  else
  {
    scrollerHeight = 0;
    containerScroller = document.getElementById(id+'_scroller');
    if (containerScroller != null) scrollerHeight = containerScroller.offsetHeight;
    var ht = tcontainer.offsetHeight-scrollerHeight;
    // doing a max as if container is stretch then constant ht is not correct.
    // ideally it should sum the container header and footer only
    return   Math.min(ht,40);
  }  
 }

function getContainerMaxHt(id)
 {
    return getContainerConstantHeight(id) + getContainerScrollAreaOffsetHeight(id);
 }
 function getTableMaxHt(tableid,containerid)
 {
    return getTableConstantHeight(tableid,containerid) + getTableScrollHeight(tableid,containerid);
 }
 function getTableConstantHeight(tableid, containerid)
 {
   tableObj =  document.getElementById(containerid);
   if (tableObj == null ) return 0 ;
   tableScrollerObj =  document.getElementById(tableid+'_scroller');
   if (tableScrollerObj == null ) return tableObj.offsetHeight ;
   //alert("here " +tableObj.offsetHeight);
   return (tableObj.offsetHeight - tableScrollerObj.offsetHeight );
 }
 function getTableScrollHeight(tableid, containerid)
 {
   tableScrollerObj =  document.getElementById(tableid + '_scroller');
   if (tableScrollerObj == null ) return 0;
   return (tableScrollerObj.scrollHeight );
 }
/* Resizing Helpers - End.*/


// Form AutoFill JS - Start
function matchFieldSelect (field, select, value) {
//alert(objectToString(document.search_form.ITEM_ID_DISABLED));
//alert(objectToString(document.search_form.ITEM_ID));
  var property = value ? 'value' : 'text';
  var found = false;
  for (var i = 0; i < select.options.length; i++)
    if ((found = select.options[i][property].indexOf(field.value) ==
0))
      break;
  if (found)
    select.selectedIndex = i;
  else
    select.selectedIndex = -1;
  if (field.createTextRange) {
    var cursorKeys ="8;46;37;38;39;40;33;34;35;36;45;"
    if (cursorKeys.indexOf(event.keyCode+";") == -1) {
      var r1 = field.createTextRange()
      var oldValue = r1.text;
      var newValue = found ? select.options[i][property] : oldValue;
      if (newValue != field.value) {
        field.value = newValue
        var rNew = field.createTextRange()
        rNew.moveStart('character', oldValue.length)
        rNew.select()
      }
    }
  }
}
function selectToText2(id)
{
//alert('here');
//id = "ITEM_ID";
      i2uiToggleItemVisibility( id, 'show');
      var obj = document.getElementById(id);
      obj.disabled = false;

      i2uiToggleItemVisibility(id + '_select', 'hide');
      var obj = document.getElementById(id);
      obj.enabled = true;



}
function textToSelect2(id)
 {
    var obj = document.getElementById( id + '_select');
  if (obj.options.length == 0) return;
  var defaultSelectedIndex = 0;
  i2uiToggleItemVisibility( id + '_select', 'show');

    obj.disabled = false;
      obj.focus();
      obj.options[defaultSelectedIndex].selected = true;

     var obj1 = document.getElementById(id);
     obj1.enabled = true;
        obj1.value=obj.options[defaultSelectedIndex].value;
        i2uiToggleItemVisibility( id, 'hide');
 }

function getEnabledField(fieldObjArray)
      {
        var fieldObj = fieldObjArray;
           if (fieldObjArray.length !=null && fieldObjArray.length > 1)
          {
             for(var i=0; i < fieldObjArray.length; i++)
               {    //alert(   fieldObjArray[i].disabled);
                   if (fieldObjArray[i].disabled == false)
                    fieldObj = fieldObjArray[i];
               }
          } else   fieldObj = fieldObjArray;
        return fieldObj;
      }

// Form AutoFill JS - End.


function ui_extractTable(id)
{
  var filename='table.csv';
  
  var anchorobject = null;
  if (event.srcElement.nodeName == 'IMG')
  anchorobject = event.srcElement.parentElement; // <a>
  else if (event.srcElement.nodeName == 'A')
  anchorobject = event.srcElement;

  var oForm = null;
  var parent = anchorobject.parentElement;       // <td> 
  var childLen = parent.childNodes.length;
  var k;
  if ( childLen >= 1)
  {
    for(k = 0; k < childLen; k++)
    {
      sibling = parent.childNodes[k]; 
      if (sibling.tagName == "INPUT")
      {
        oForm = sibling.form;
      }
    }
  }
  if (oForm == null)       
     alert('developer alert: please hide CORE_SAVE_REPORT  in the same TD AS THE download icon');
 
   if(oForm.CORE_SAVE_REPORT == null) 
   {
     alert('developer alert: please hide a parameter named CORE_SAVE_REPORT IN your form');
     return;
  }    
  
  var prevAction = oForm.action;
  var prevTarget = oForm.target;

  var tableTxt = '';
  var scrollable = true;
  if (document.getElementById(id+'_header') == null) scrollable = false;
  
  if (scrollable == true)
  tableTxt = ui_extractTable_worker(id+'_header') + ui_extractTable_worker(id+'_data');
  else
  tableTxt = ui_extractTable_worker(id,null,null);  

  tableTxt = tableTxt.replace(/[,]+/g, "\t");

    if (typeof(oForm.CORE_SAVE_REPORT.length) == 'number') 
    {
      oForm.CORE_SAVE_REPORT[0].value= tableTxt;
    }
    else 
    {
      oForm.CORE_SAVE_REPORT.value= tableTxt;
    }
    
    oForm.target = "i2ui_shell_bottom";        
    oForm.action=omxContextPath+ "/DownloadServlet?DATA_PARAM=CORE_SAVE_REPORT&FILE_NAME="+filename;

    oForm.submit();
    
   oForm.action= prevAction;          
   oForm.target= prevTarget;          
   oForm.CORE_SAVE_REPORT.value= "";          
}


function i2uiMoveOptionOrderedList(olist, direction)
{
  var list = olist;
  if (list == null)
    return;
  var len = list.length;
  var which = list.selectedIndex;
  if (which == -1)
    return;
  var savetext = list.options[which].text;
  var savevalue = list.options[which].value;
  var newindex = which;
  if (direction == 0)
  {
    for (var i=which; i>0; i--)
    {
      list.options[i].text  = list.options[i-1].text;
      list.options[i].value = list.options[i-1].value;
    }
    newindex = 0;
  }
  else
  if (direction == 2)
  {
    for (var i=which; i<len-1; i++)
    {
      list.options[i].text  = list.options[i+1].text;
      list.options[i].value = list.options[i+1].value;
    }
    newindex = len-1;
  }
  else
  if (direction == -1 && which > 0)
  {
    list.options[which].text  = list.options[which-1].text;
    list.options[which].value = list.options[which-1].value;
    newindex = which-1;
  }
  else
  if (direction == 1 && which < len-1)
  {
    list.options[which].text  = list.options[which+1].text;
    list.options[which].value = list.options[which+1].value;
    newindex = which+1;
  }
  list.options[newindex].value = savevalue;
  list.options[newindex].text = savetext;
  list.selectedIndex = newindex;
}

// Table Extracter
function ui_extractTable_removeSpecialChar(value)
{
return  value.replace(/[,]+/g, "");
}
function ui_extractTable_worker(objname, cols, names)
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
    var noOfRows = tblobj.rows.length;
    len2 = tblobj.rows(noOfRows-1).cells.length; // Need a better way to get no of columns
    
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
  var result ='';
 
  for (var i=0; i<len; i++)
  {
    addRow = true;
    for (var j=0; j<len2; j++)
    {
      try
      {
        coltext= '';
        tdobj=tblobj.rows[i].cells[ids[j]];
        coltext = ui_extractTable_removeSpecialChar(tdobj.innerText);
        
         if (tdobj.colSpan > 1)
         { var label = coltext;
            for(k = 2; k <= tdobj.colSpan ; k++)
              { 
               coltext = coltext + "," + label;
              }
         }        
          
            var childLen = tdobj.childNodes.length;
           //alert("childLen " +childLen);
            if ( childLen >= 1)
            {
              // for each child of (TD)
              for(k = 0; k < childLen; k++)
              {
              var input = tdobj.childNodes[k];

              var type =  input.nodeName;
              
              if (type == '#text')
              {
/*                var innerTxt = input.nodeValue;
                if (innerTxt != tdobj.innerText)
                coltext =  coltext +" " + innerText;
*/
              }
               else
                if ( type == 'INPUT' )
                {
                   if ( input.type == 'checkbox')
                   {
//                    coltext = coltext + " " + input.checked; 
                   }
                    else if ( input.type == 'radio')
                   {
  //                  coltext = coltext + " " + input.checked; 
                   }
                  else if (input.type != 'hidden' )
                  {
                    coltext = coltext + " " + ui_extractTable_removeSpecialChar(input.value); 
                  }
                }
                 
                else if (type == 'IMG')
                {
                if (input.style.display !='none')
                 coltext = coltext + " " + input.alt; 
                }
                 else if (type == 'SELECT')
                {
                 // select is overriding any previous text.
                 coltext =  ui_extractTable_removeSpecialChar(input.options[input.selectedIndex].innerText); 
                }
                 else 
                {
                 if (!(tdobj.colSpan > 1))
                 {
                   //if (coltext != tdobj.innerText)
                   if (coltext != ui_extractTable_removeSpecialChar(tdobj.innerText))
                    coltext = coltext + " " + ui_extractTable_removeSpecialChar(tdobj.innerText);
                  } 
                }
               // if ( k == 0 && tdobj.childNodes[k].type == 'checkbox')
                {
                //alert(tdobj.childNodes[k].checked);
                   //  if ( tdobj.childNodes[k].checked == true)
                     {
                     addRow = true;
                     }
                     //coltext = null;
                }
              }
           }

//        alert(coltext == '');
        if (coltext == '' || coltext == null) coltext="-";
        if (result == '') result=" ";
          if (addRow == true)
          result =  result + coltext+",";

     }
      catch(e){}
    }
    result += "\n";

  }
  return(result);
}

function i2uiListTreeCollapseAll()
{

if ( document.layers) return;
  var argv = i2uiListTreeCollapseAll.arguments;
  var argc = argv.length;
var imglist;
if ( argc > 0 )
  imglist = argv[0].getElementsByTagName("IMG");
else
  imglist = document.getElementsByTagName("IMG");
  if ( argc > 1 ) 
    selectedNode = argv[1];
    
  if ( imglist.length > 0 )
  {
  for ( var i = imglist.length -1 ;i>=0;--i)
  {
  
    if (  imglist[i].id.indexOf('IMAGE_') == 0 ) {
       imglist[i].onclick();
      }
  }

}
  
}

function checkChild ( owningtable )
{


var childList = owningtable.childNodes;
     for ( var childIter = 0; childIter < childList.length;++childIter)
     {
      
        if ( childList[childIter].id == selectedNode  )
              return true;
    
        
        if ( childList[childIter].hasChildNodes ) {
            if ( checkChild(childList[childIter]) ) return true;
            }
     }

return  false ;

}

//* This is a copy of i2uiToggleContent for using custom images instead of i2 icons -- RVS
/*
  This routine toggles the visibility of a body of content.
  The content must be enclosed within a TBODY tag.

  item - the element that received the event
  nest - how many levels nested is the item from the enclosing table

  return - none

  Compatibility:  IE, NS6
*/

function i2uiListTreeToggleContent(item, nest, relatedroutine,expandImage,collapseImage)
{

  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return;
  }
  
  if ( collapseImage == null )
    collapseImage = "minus_norgie.gif";
    
  if ( expandImage == null )
    expandImage = "plus_norgie.gif";
    
  // find the owning table for the item which received the event.
  // in this case the item is the expand/collapse image
  // note: the table may be several levels above as indicated by 'nest'
  var owningtable = item;
  //i2uitrace(1,"nest="+nest+" item="+item+" tagname="+item.tagName);
  if (item.tagName == "A")
  {
    item = item.childNodes[0];
    //i2uitrace(1,"new item="+item+" parent="+item.parentNode+" type="+item.tagName);
  }

  while (owningtable != null && nest > 0)
  {
    if (owningtable.parentElement)
    {
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentElement+" level="+nest);
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentElement.tagName+" level="+nest);
      owningtable = owningtable.parentElement;
    }
    else
    {
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentNode+" level="+nest);
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentNode.tagName+" level="+nest);
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
        //i2uitrace(1,"tag="+superowner.tagName+" parent="+superowner.parentElement.tagName+" level="+nest+" id="+superowner.parentElement.id);
        superowner = superowner.parentElement;
      }
      else
      {
        //i2uitrace(1,"tag="+superowner.tagName+" parent="+superowner.parentNode.tagName+" level="+nest+" id="+superowner.parentNode.id);
        superowner = superowner.parentNode;
      }
      if (superowner != null && superowner.tagName == 'TABLE')
      {
        ownerid = superowner.id;
      }
    }
  }
  
  //i2uitrace(1,"final id="+ownerid);
  if ( owningtable != null && selectedNode != null ){
    if ( checkChild(owningtable) ) {
      return;    
      }
    }


  // if found table, find child TBODY with proper id
  if (owningtable != null)
  {
    var pretogglewidth = owningtable.offsetWidth;

    //i2uitrace(1,owningtable.innerHTML);

    // can not simply use getElementById since the container
    // may itself contain containers.

    // determine how many TBODY tags are within the table
    var len = owningtable.getElementsByTagName('TBODY').length;
    //i2uitrace(1,'#TBODY='+len);

    // now find proper TBODY that holds the content
    var contenttbody;
    //alert(owningtable.getElementsByTagName('tbody').length);
    for (var i=0; i<len; i++)
    {
      contenttbody = owningtable.getElementsByTagName('TBODY')[i];
      //i2uitrace(1,'TBODY '+i+' id='+contenttbody.id);
      if (contenttbody.id == '_containerBody' ||
          contenttbody.id == '_containerbody' ||
          contenttbody.id == 'containerBodyIndent' ||
          contenttbody.id == 'containerbody')
      {
        //i2uitrace(1,'picked TBODY #'+i);

        var delta;

        if (contenttbody.style.display == "none")
        {
          contenttbody.style.display = "";
          item.src = i2uiImageDirectory+"/" + collapseImage;
          owningtable.collapsed="false";
          delta = contenttbody.offsetHeight;
        }
        else
        {
          delta = 0 - contenttbody.offsetHeight;
          contenttbody.style.display = "none";
          item.src = i2uiImageDirectory+"/" + expandImage;
          owningtable.collapsed="true";
        }
        if (i2uiToggleContentUserFunction != null)
        {
          eval(i2uiToggleContentUserFunction+"('"+ownerid+"',"+delta+")");
        }
        break;
      }
    }

    // restore width of container
    if (pretogglewidth != owningtable.offsetWidth)
    {
      owningtable.style.width = pretogglewidth;
      owningtable.width = pretogglewidth;
    }

    // call a related routine to handle any follow-up actions
    // intended for internal use.  external use should use the
    // callback mechanism
    //i2uitrace(0,"togglecontent related=["+relatedroutine+"]");
    if (relatedroutine != null)
    {
      // must invoke the routine 'in the future' to allow browser
      // to settle down, that is, finish rendering the effects of
      // the tree element changing state
      setTimeout(relatedroutine,200);
    }
  }
}


//  This is a copy of i2uiToggleContent for using characters instead of icons. -- WJD
/*
  This routine toggles the visibility of a body of content.
  The content must be enclosed within a TBODY tag.

  item - the element that received the event
  nest - how many levels nested is the item from the enclosing table

  return - none

  Compatibility:  IE, NS6
*/
function i2uiDegradedToggleContent(item, nest, relatedroutine)
{
  var originalItem = item;

  // NS4 browser does not reflow document so what is the point anyway?
  if (document.layers)
  {
    return false;
  }
  // find the owning table for the item which received the event.
  // in this case the item is the expand/collapse image
  // note: the table may be several levels above as indicated by 'nest'
  var owningtable = item;
  //i2uitrace(1,"nest="+nest+" item="+item+" tagname="+item.tagName);
  if (item.tagName == "A")
  {
    item = item.childNodes[0];
    //i2uitrace(1,"new item="+item+" parent="+item.parentNode+" type="+item.tagName);
  }
  
  while (owningtable != null && nest > 0)
  {
    if (owningtable.parentElement)
    {
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentElement+" level="+nest);
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentElement.tagName+" level="+nest);
      owningtable = owningtable.parentElement;
    }
    else
    {
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentNode+" level="+nest);
      //i2uitrace(1,"tag="+owningtable.tagName+" parent="+owningtable.parentNode.tagName+" level="+nest);
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
        //i2uitrace(1,"tag="+superowner.tagName+" parent="+superowner.parentElement.tagName+" level="+nest+" id="+superowner.parentElement.id);
        superowner = superowner.parentElement;
      }
      else
      {
        //i2uitrace(1,"tag="+superowner.tagName+" parent="+superowner.parentNode.tagName+" level="+nest+" id="+superowner.parentNode.id);
        superowner = superowner.parentNode;
      }
      if (superowner != null && superowner.tagName == 'TABLE')
      {
        ownerid = superowner.id;
      }
    }
  }
  //i2uitrace(1,"final id="+ownerid);

  // if found table, find child TBODY with proper id
  if (owningtable != null)
  {
    var pretogglewidth = owningtable.offsetWidth;

    //i2uitrace(1,owningtable.innerHTML);

    // can not simply use getElementById since the container
    // may itself contain containers.

    // determine how many TBODY tags are within the table
    var len = owningtable.getElementsByTagName('TBODY').length;
    //i2uitrace(1,'#TBODY='+len);

    // now find proper TBODY that holds the content
    var contenttbody;
    for (var i=0; i<len; i++)
    {
      contenttbody = owningtable.getElementsByTagName('TBODY')[i];
      //i2uitrace(1,'TBODY '+i+' id='+contenttbody.id);
      if (contenttbody.id == '_containerBody' ||
          contenttbody.id == '_containerbody' ||
          contenttbody.id == 'containerBodyIndent' ||
          contenttbody.id == 'containerbody')
      {
        //i2uitrace(1,'picked TBODY #'+i);

        var delta;

        if (contenttbody.style.display == "none")
        {
          contenttbody.style.display = "";
      //    item.src = i2uiImageDirectory+"/container_collapse.gif";
          originalItem.innerText = '-';
          delta = contenttbody.offsetHeight;
        }
        else
        {
          delta = 0 - contenttbody.offsetHeight;
          contenttbody.style.display = "none";
        //  item.src = i2uiImageDirectory+"/container_expand.gif";
          originalItem.innerText = "+";
        }
        if (i2uiToggleContentUserFunction != null)
        {
          eval(i2uiToggleContentUserFunction+"('"+ownerid+"',"+delta+")");
        }
        break;
      }
    }

    // restore width of container
    if (pretogglewidth != owningtable.offsetWidth)
    {
      owningtable.style.width = pretogglewidth;
      owningtable.width = pretogglewidth;
    }

    // call a related routine to handle any follow-up actions
    // intended for internal use.  external use should use the
    // callback mechanism
    //i2uitrace(0,"togglecontent related=["+relatedroutine+"]");
    if (relatedroutine != null)
    {
      // must invoke the routine 'in the future' to allow browser
      // to settle down, that is, finish rendering the effects of
      // the tree element changing state
      setTimeout(relatedroutine,200);
    }
  }
  
}

/*
  Degraded Pad tree table logic

  tablename - <tbd>
  cellname - <tbd>
  column - <tbd>
  relatedtablenames - <tbd>
  name - <tbd>
  recurse - <tbd>

  return - none

  Compatibility:  IE, NS6
*/       
function i2uiDegradedManagePadTree(tablename, cellname, column, relatedtablenames, name, recurse, relatedroutine)
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

var action;
if (img.tagName == "A")
{
 if (img.innerText == "*")
 {
   return;
 }
 else if (img.innerText == "+")
 {
   img.innerText = "-";
   action = "";
 }
 else if (img.innerText == "-")
 {
   img.innerText = "+";
   action = "none";
 }
 
 
}
else
{
    if (img.src.indexOf("bullet") != -1)
    {
      return;
    }

// WJD    var action;
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
            i2uiDegradedManagePadTree(tablename,newcell,column,relatedtablenames,name,1);
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


function toggleDegradedNav(path) {
    var frameCol = top.i2ui_shell_content.document.body.cols;
    
    if ( frameCol.charAt(0) == "0") {
        if (document.all) {
            document.header.toggle.title = "Hide Navigation Frame";
            document.header.toggle.innerText = "&lt;";
            top.i2ui_shell_content.document.body.cols="170,*";
            tabShow = 0;
            return;
        }
    }
    else
    {    
        if (document.all) {
            document.header.toggle.title = "Show Navigation Frame";
            document.header.toggle.title = "&gt;";
            top.i2ui_shell_content.document.body.cols="0%,100%";
            tabShow = 1;
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
function i2uiDegradedManageTreeTable(tablename, cellname, column, relatedtablenames, name, recurse, relatedroutine, startat)
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

//  WJD -- Look for '*' character instead
//    if (img != null && img.src.indexOf("bullet") == -1)
    if (img != null && img.innerText.indexOf("*") == -1)
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

    var action;
    var action2;

    if (img != null)
    {
      //  WJD -- Look for innerText character instead if img is an anchor tag      

      if (img.tagName == "A")
      {
        img.innerText = img.innerText.trim(); // MAB: had a space that screwed this up        
        
  if (img.innerText == "*")
  {
    return;
  }
  else if (img.innerText == "+")
  {
          action2 = "expand";
          if (recurse == null)
          {
            img.innerText = "-";
            action = "";
          }
          else
          {
            action = "none";
          }
  }
  else if (img.innerText == "-")
  {   
          action2 = "collapse";
          if (recurse == null)
          {
            img.innerText = "+";
            action = "none";
          }
          else
          {
            action = "";
          }
  }
      }
      else
      {
        if (img.src != null)
        {
          if (img.src.indexOf("bullet") != -1)
          {
            return;
          }
          else
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

function addToFavorites()
{
  document.form.action = omxContextPath + '/core/search/controller/saveInSession.x2c';
  document.form.submit();
}

/** 
 * Helper function for setting/appending the selections from a search popup
 * to the parent field in the opener window 
 **/
function setSelectionsToParentField(parentFieldName, parentFormName, selectFieldName, append)
{
  var n = parentFormName;
  if( n == '' )
    n = 'form';

  var bAppend = "true";
  if ( append != "true" )
    bAppend = append;

  var fieldName = parentFieldName;
  var selectionName = selectFieldName;
  if( selectionName == '' )
    n = 'ID';

  var previousValue = eval("parent.opener." + n + "." + fieldName + ".value");
  var previousValuesArray = null;
  var items = null;
  var previousLength = 0;

  if ( bAppend == "true" )
  {
    if(previousValue != null && previousValue.indexOf(",") != -1)
    {
       previousValuesArray = previousValue.split(",");
    }
    items = previousValue;

    if(previousValuesArray != null)
    {
      previousLength = previousValuesArray.length;
    }
  }

  var x = document.getElementsByName( selectionName );
  var length = x.length;

  for(i=0; i<length; i++)
  {
    var t = x[ i ];
    if ( t.checked || t.type == "hidden")
    {
      var selectedValue = t.value;
      var found = "false";

      for(j=0; j<previousLength; j++)
      {
        var currentValue = previousValuesArray[j];
        if( selectedValue == currentValue )
        {
          found = "true";
          break;
        }

      }
      if( items )
      {
        if(found == "false")
        {
          if(items != selectedValue)
          {
            items += ',' + selectedValue;
          }
        }
      }
      else
      {
        items = selectedValue;
      }
    }
  }

  eval("parent.opener." + n + "." + fieldName + ".value = items;");
}

// counts number of selected records
function countSelectedRecords()
{
 var count;
 var checked=0;
 var elementsLen = result_form.elements.length;

 for(count = 0; count < elementsLen; count++)
 {
  if ( result_form.elements[count].type == "checkbox" && result_form.elements[count].checked == true  &&
     result_form.elements[count].id != "result_form_table_globalrowselector" && result_form.elements[count].id != '_rowselector_header' )
  {
   checked++;
  }


 }
 //alert("checked= "+checked);
 return checked;
}
// checks if all check box is selected


function ifAllChecked(form)
{
    //alert("hi");
    var count;
    var temp =  "document." + form + ".elements.length" ;
    var elementsLen = eval(temp);
    var foundChecked = false;
      
    for(count = 0; count < elementsLen; count++)
        {
            var type = "document."+ form + ".elements[" + count + "].type";
            type = eval(type);
            var checked = "document."+ form + ".elements[" + count + "].checked";
            checked = eval(checked);
     var nm = "document."+ form + ".elements[" + count + "].id";
     nm = eval(nm);
            if(

                 type == "checkbox" &&
                 checked == true  && (nm=='_rowselector_header' || nm=='result_form_table_globalrowselector')
               )
               {
       foundChecked = true; 
  break;
               }
               
        }
    return foundChecked;
}

function checkifAnySelected(form)
{
    var count;
    var elementsLen = form.elements.length;
    var foundChecked = false;

    for(count = 0; count < elementsLen; count++)
    {
        if(
           form.elements[count].type == "checkbox" &&
           form.elements[count].checked == true  &&
           form.elements[count].name != "SELECT_ALL"
           )
            {
                foundChecked = true;break;
            }
        else if (
                 form.elements[count].type == "radio" &&
                 form.elements[count].checked == true
                )
             {
                foundChecked = true;
                break;

              }
    }
    return foundChecked;
}

// counts number of selected records
function getSelectedRecordsCount(form)
{
 var count;
 var checked=0;
 var elementsLen = form.elements.length;

 for(count = 0; count<elementsLen; count++)
 {
  if ( form.elements[count].type == "checkbox" && form.elements[count].checked == true  &&
     form.elements[count].id != "form_table_globalrowselector" && form.elements[count].id != '_rowselector_header' )
  {
   checked++;
  }
 }
 return checked;
}

function onlyNumber()
{
    onlyValCharWith1Dot(/[0123456789.,-/\u0020/\u00A0]/);
    onlyValidFirstCharacters(/[-]/);
}

function onlyValCharWith1Dot(validCharacters)
{
    var bChanged = false;

    var oField = event.srcElement;
    var sField = oField.value;
    var nStringLen = sField.length;
    var sValidField = "";
    var oneDotFound = false;    

    for(var x = 0; x < nStringLen; x++)
    {
        var cChar = sField.charAt(x);

        if(cChar.search(validCharacters) != -1)
        {
          if(cChar == '.')
          {
            if(oneDotFound == true)
            {
                bChanged = true;
            }
            else
            {
                oneDotFound = true;
                sValidField += cChar;
            }
          }
          else
          {
            if (cChar != '/')
            {          
                  sValidField += cChar;
                }
                else
                {
              bChanged = true;     
                }                 
          }
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

function onlyValidFirstCharacters(validCharacters)
{

    var bChanged = false;

    var oField = event.srcElement;
    var sField = oField.value;
    var nStringLen = sField.length;
    var sValidField = "";

    for(var x = 0; x < nStringLen; x++)
    {
      var cChar = sField.charAt(x);

      if(x > 0 && cChar.search(validCharacters) != -1)
      {
           bChanged = true;
      }
      else
      {
            sValidField += cChar;
      }
    }

    if(bChanged)
    {
      oField.value = sValidField;
    }
}
