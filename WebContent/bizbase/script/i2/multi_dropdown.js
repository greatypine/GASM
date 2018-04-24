var i2uitracetext;
var i2uitracewindow;
function i2uitrace (text){
   if (text == 'clear'){i2uitracetext="";}
   else{i2uitracetext += text;}
   i2uitracetext += "<BR>";
   i2uitracewindow = window.open("", "i2uitrace", "status=no,scrollbars=yes,resizable=yes");
   i2uitracewindow.document.open();
   i2uitracewindow.document.write("<html><title>Trace Window</title><body>");
   i2uitracewindow.document.write(i2uitracetext);
   i2uitracewindow.document.write("</body></html>");
   i2uitracewindow.document.close();
}


function i2uiComputeTop(obj){
   if (document.layers){return;}
   if (typeof obj == "string"){obj = document.getElementById(obj);}
   var y = 0;
   if (obj != null && obj.offsetTop != null){y = obj.offsetTop;}
   if (obj != null && obj.offsetParent != null){return y + i2uiComputeTop(obj.offsetParent);}
   return y;
}

function startsWith(source, toFind) 
{
    for(var i = 0; i < toFind.length; i++) 
    {
        if(toUpperCase(toFind.charAt(i)) != toUpperCase(source.charAt(i)))
            return false;
    }
    return true;
}

function toUpperCase (s)
{
    var result = s.toUpperCase();
    return result;
}

/**************************************************************************************************
    Support for TM comboboxes with multiselect.
***************************************************************************************************/
// Safe method to get name of combobox control.
function getCombobox(form, name, rowIdx) {
    if (rowIdx == undefined)
    {
        var result = form[name+"_"];
        if (!result)
            result = form[name];
        return result;
    }
    else
    {
        var colobjs = getColumnObjects(name+"_");
        if (colobjs.length == 0)
            colobjs = getColumnObjects(name);
        return colobjs[rowIdx];
    }
}
function showComboboxSelection(combobox) {
    if (combobox.tmcomboboxName)
        window[combobox.tmcomboboxName].showSelected();
}
function comboboxToFocus(combobox) {
    if (combobox.tmcomboboxName) {
        if (combobox.tmfocus) { combobox.tmfocus(); }
        else { combobox.focus(); }
    }
    else
        combobox.focus();
}
function TMComboBoxNative(name) {
    this.name=name;
    this.objectName=name+"_tmcombobox";
    window[this.objectName]=this;
    this.size=11;
    this.minSize=3;
    this.tabIndex=0;
    this.width="180px";
    this.height="14px";
    this.timeLastKeyPressed = 0;
    this.keyPressedBuffer = "";
    this.onchange = null;
    this.maxInTitle = 10;
    this.hasHiddenParam=false;   // If true then defines hidden parameter with comma separated list of values
    this.isDisabled=false;
}
TMComboBoxNative.prototype.error = function (error) {alert(error); throw error;}
TMComboBoxNative.prototype.setHiddenParam = function (hasHiddenParam) {this.hasHiddenParam=hasHiddenParam}
TMComboBoxNative.prototype.setName = function (name) {this.name=name}
TMComboBoxNative.prototype.setTabIndex = function(tabIndex) {this.tabIndex=tabIndex}
TMComboBoxNative.prototype.setSize = function(size) {this.size=size}
TMComboBoxNative.prototype.setWidth = function(width) {this.width=width}
TMComboBoxNative.prototype.setOptions = function(options) {this.options=options}
TMComboBoxNative.prototype.setOnchange = function(onchange) {this.onchange=onchange}
TMComboBoxNative.prototype.isSelected = function(option) { return ((option.length == 3) && (option[2] == 1))}
TMComboBoxNative.prototype.activate = function() { 
    this.head = document.getElementById(this.name+"_id");
    var options = document.getElementById(this.name+"_options");
    this.options = options;
    options.tmcomboboxName=this.objectName;
    this.parentElement = options.parentElement;
    this.optionsShown = 0;
    var childNodes = this.head.childNodes;

    for(var i=0;i<childNodes.length;i++) {
        var child = childNodes[i];
        if (child.nodeType == 1) {
            if(child.tagName == "SPAN") { this.headText = child }
            else if (child.tagName == "IMG") { this.headIcon = child }
        }
    }

    if (this.hasHiddenParam) {
      this.hiddenParam = document.getElementById(this.name+"_value");;
    }
    this.showSelected();
    if (this.onchange) {
        this.prevSelectedOptions = this.getSelectedOptions(false);
    }

    this.options.prevFocus = this.options.focus;
    try {
        this.options.focus = new Function("window[\'"+this.objectName+"\'].optionsFocus()");
    } catch (e) {
        this.options.tmfocus = new Function("window[\'"+this.objectName+"\'].optionsFocus()");
    }
}
TMComboBoxNative.prototype.optionsFocus = function() { 
    if (this.optionsShown == 0) {
        this.headText.focus();
    } else {
        this.options.prevFocus();
    }
}
TMComboBoxNative.prototype.headFocus = function() { 
    this.headText.focus();
}
TMComboBoxNative.prototype.toggleOptions = function () {
   if (this.optionsShown == 0) {this.expandOptions()} else {this.collapseOptions(); this.headFocus();}
}
TMComboBoxNative.prototype.onHeadKeyPress = function () {
    this.doSearchByLetter();
    return false;
}
TMComboBoxNative.prototype.onHeadKeyDown = function () {
  var keyCode = window.event.keyCode;
  var cont = false;
  var isChanged = false;
  var options = this.options.options;
  switch (keyCode) {
      case 40: // Down
          if (window.event.altKey) {
              this.expandOptions();
          } else {
              if (options.selectedIndex == -1) {
                  options.selectedIndex = 0;
                  isChanged = true;
              } else if (options.selectedIndex < options.length - 1) {
                  options.selectedIndex++;
                  isChanged = true;
              }
          }
          break;
      case 38: // Up
          if (window.event.altKey) {
              this.expandOptions();
          } else {
              if (options.selectedIndex == -1) {
                  options.selectedIndex = options.length - 1;
                  isChanged = true;
              } else if (options.selectedIndex > 0) {
                  options.selectedIndex--;
                  isChanged = true;
              } else {
                  options.selectedIndex = 0;
                  isChanged = true;
              }
          }
          break;
      case 34: // PgDown
          if (options.selectedIndex == -1) {
              options.selectedIndex = 0;
              isChanged = true;
          } else if (options.selectedIndex+this.size < options.length - 1) {
              options.selectedIndex += this.size;
              isChanged = true;
          } else {
              options.selectedIndex = options.length - 1;
              isChanged = true;
          }
          break;
      case 33: // PgUp
          if (options.selectedIndex == -1) {
              options.selectedIndex = options.length - 1;
              isChanged = true;
          } else if (options.selectedIndex-this.size > 0) {
              options.selectedIndex -= this.size;
              isChanged = true;
          } else {
              options.selectedIndex = 0;
              isChanged = true;
          }
          break;
      case 36: // Home
          if (options.selectedIndex != null || this.getSelectedOptions(true).length > 1) {
              options.selectedIndex = 0;
              isChanged = true;
          }
          break;
      case 35: // End
          if (options.selectedIndex != options.length - 1) {
              options.selectedIndex = options.length - 1;
              isChanged = true;
          }
          break;
      default:
          cont = this.doSearchByLetter();
          break;
  }
  if (isChanged) {
      this.showSelected();
      this.doOnChange();
  }
  if (!cont) {
      window.event.cancelBubble = true;
      window.event.returnValue = false;
  }
}
TMComboBoxNative.prototype.doSearchByLetter = function () {
    var result = true;
    var now = (new Date()).getTime();
    if(now - this.timeLastKeyPressed > 1000) { this.keyPressedBuffer = ""; }
    this.timeLastKeyPressed = now;

    var startText = this.keyPressedBuffer + String.fromCharCode(window.event.keyCode);

    var options = this.options.options;
    for(var i=0; i<options.length; i++) {
        var option = options[i];
        if((option.value != "") && (startsWith(option.text, startText))) {
            this.options.selectedIndex=i;
            this.keyPressedBuffer = startText;
            window.event.returnValue = false;
            window.event.cancelBubble = true;
            this.showSelected();
            result = false;
            break;
        }
    }
    return result;
}
TMComboBoxNative.prototype.onOptionKeyDown = function () {
  var keyCode = window.event.keyCode;
  if (keyCode == 27) //ESC
  {
      this.headFocus(); // closes options as side-effect of onblur for options
  } 
  else if (keyCode == 9) // Tab 
  {
      window.event.returnValue = false;
      window.event.cancelBubble = true;
      this.headFocus(); // closes options as side-effect of onblur for options
  } 
  else if ((keyCode == 13) || // Enter
          ((keyCode == 40) && window.event.altKey) ||  // Alt+Down
          ((keyCode == 38) && window.event.altKey))    // Alt+Up 
  {
      window.event.returnValue = false;
      window.event.cancelBubble = true;
      this.headFocus(); // closes options as side-effect of onblur for options
  } else if (window.event.shiftKey && keyCode == 119) { // toggle multiselect mode
      this.keystrokeMultiselectMode = !this.keystrokeMultiselectMode;
  }
  return true;
}
TMComboBoxNative.prototype.onOptionKeyPress = function () {
  var keyCode = window.event.keyCode;
  if (keyCode != 32 &&                // space
      !this.keystrokeMultiselectMode) // disable i2 search in multiselect mode
  {
      this.doSearchByLetter();
      return false;
  }
  return true;
}
TMComboBoxNative.prototype.onClick = function () {
  if (!window.event.ctrlKey) {  // If CTRL is not pressed - hide control (like simple select)
     this.headFocus();
  }
  return true;
}
TMComboBoxNative.prototype.filterOptions = function () {
    var selected = 0;
    var withoutvalue = 0;
    if (this.options.selectedIndex != -1) {
        var options = this.options.options;
        for (var i = 0; i < options.length; i++) {
            var option = options[i];
            if (option.selected) {
                selected++;
                if (option.value.length == 0) withoutvalue++;
            }
        }
        if (selected > 0 && selected > withoutvalue) {
            for (var i = 0; i < options.length; i++) {
                var option = options[i];
                if (option.selected && option.value.length == 0) {
                    option.selected = false;
                }
            }
        }
    }
}
TMComboBoxNative.prototype.showSelected = function () {
    this.filterOptions();
    var display = "";
    var codes = "";
    var alt = "";
    var selectedNum = 0;
    if (this.options.selectedIndex != -1) {
        var options = this.options.options;
        for (var i = 0; i < options.length; i++) {
            var option = options[i];
            if (option.selected) {
                selectedNum++;
                if (display.length > 0) {display += ","}
                if (codes.length > 0 && option.value.length > 0) {codes += ","}
                display += option.text;
                codes += option.value;
                if (selectedNum <= this.maxInTitle) {
                    if (alt.length > 0) {alt += "\n"}
                    if (selectedNum == this.maxInTitle) {
                        alt += "...";
                    } else {
                        alt += option.text;
                    }
                }
            }
        }
    }
    if (selectedNum > 1) {
        display = "+" + display;
    }
    this.setHeadText(display);
    this.setTitleText(alt);
    if (this.hiddenParam) {this.hiddenParam.value=codes}
}
TMComboBoxNative.prototype.setHeadText = function (text) {
    var childNodes = this.headText.childNodes;
    for (var i = 0; i < childNodes.length; i++) {
       var wrkChild = childNodes[i];
       if (wrkChild.nodeName == "#text") {
          wrkChild.nodeValue = text;
          break;
       }
    }
}
TMComboBoxNative.prototype.setTitleText = function (text) {
    this.headText.title=text;
    this.headIcon.title=text;
}
TMComboBoxNative.prototype.onHeadMouseDown = function () {this.headMouseDown = true}
TMComboBoxNative.prototype.onHeadMouseUp = function ()   {this.headMouseDown = false}
TMComboBoxNative.prototype.onHeadMove = function () {
    // Head moves in two cases:
    // - when parent windows resizes
    // - sometimes the head is not put into right position by IE when browser is resized and 
    //   combobox head may jump up/down, when options are expanded.
    // This method helps to keep expanded options together with the head.
    if (this.optionsShown == 1) {
        this.options.style.left = this.head.offsetLeft;
        this.options.style.width = this.head.offsetWidth;
        this.options.style.pixelTop = this.calculateOptionsTop();
    }
}
TMComboBoxNative.prototype.calculateOptionsTop = function () {
    // This code compensates for invalid calculation of options position inside of TD elements.
    // Probably compensation code would become more complex when new cases are found.
    // var top = i2uiComputeTop(this.head) + this.head.offsetHeight;
    
    //keeping it simple for now
    var top = this.head.offsetTop + this.head.offsetHeight;
    var parentElement = this.head.parentElement;
    if (parentElement && parentElement.tagName == "TD") {
        if(parentElement.vAlign == "middle" || parentElement.vAlign == "") {
            top -= (parentElement.offsetHeight - this.head.offsetHeight)/2;
        }
    }

    // If element does not fit into the window - change position/size
    var winScrollTop = document.body.scrollTop;
    var winHeight = document.body.clientHeight;
    var listHeight = this.options.offsetHeight;
    while (true && listHeight != 0) {
        var listHeight = this.options.offsetHeight;
        if (top+listHeight <= winScrollTop+winHeight) {
            break;
        } if (top-this.head.offsetHeight-listHeight > winScrollTop || this.options.size <= this.minSize) {
            top = top-this.head.offsetHeight-listHeight;
            break;
        } else {
            this.options.size = parseInt(this.options.size) - 1;
        }
    }
    return top;
}
TMComboBoxNative.prototype.expandOptions = function () {
    this.options.style.left = this.head.offsetLeft;
    this.options.style.width = this.head.offsetWidth;
    this.options.style.pixelTop = this.calculateOptionsTop();

    this.options.style.visibility = "visible";
    this.optionsShown = 1;
    this.keystrokeMultiselectMode = false;
    this.options.focus();
    this.scrollToFirstSelected();
}
TMComboBoxNative.prototype.getSelectedOptions = function (includingNoValue) {
    var selected = new Array();
    if (this.options.selectedIndex != -1) {
        var options = this.options.options;
        for (var i = 0; i < options.length; i++) {
            var option = options[i];
            if (option.selected && (includingNoValue || option.value.length > 0)) {
                selected[selected.length] = i;
            }
        }
    }
    return selected;
}
TMComboBoxNative.prototype.scrollToFirstSelected = function () {
    // IE does not provide explicit functions to scroll multiselect combobox to selected values.
    // But it does it implicitly, options is becoming selected.
    var selectedIndex = this.options.selectedIndex;
    if (selectedIndex != -1) {
        var options = this.options.options;
        options[selectedIndex].selected = false;
        options[selectedIndex].selected = true;
    }
}
TMComboBoxNative.prototype.doOnChange = function () {
    if (this.onchange) {
        var nowSelectedOptions = this.getSelectedOptions(false);
        var isChanged = (nowSelectedOptions.length != this.prevSelectedOptions.length);
        if (!isChanged) {
            for (var i = 0; i < nowSelectedOptions.length; i++) {
                if (nowSelectedOptions[i] != this.prevSelectedOptions[i]) {
                    isChanged = true;
                    break;
                }
            }
        }
        if (isChanged) {
            this.prevSelectedOptions = nowSelectedOptions;
            eval(this.onchange);
        }
    }
}
TMComboBoxNative.prototype.collapseOptions = function () {
    // Click on combobox header will result in the following sequence of events:
    // 1) header.mousedown  --> headMouseDown=true
    // 2) options.onblur    --> invokes collapseOptions
    // 3) header.mouseup    --> headMouseUp=false
    // 4) header.mouseclick --> invokes toggleOptions and it would invoke expandOptions
    // Check the headMouseDown so collapseOptions do not hide and options will be hidden by toggleOptions
    if (!this.headMouseDown) {
        this.options.style.zindex = 0;
        this.options.style.visibility = "hidden";
        this.optionsShown = 0;
        this.doOnChange();
    }
}
TMComboBoxNative.prototype.onFocus = function () {
   this.headText.style.background="navy";
   this.headText.style.color="white";
}
TMComboBoxNative.prototype.onBlur = function () {
   this.headText.style.background="white";
   this.headText.style.color="black";
   this.doOnChange();
}
TMComboBoxNative.prototype.validate = function () {
}
TMComboBoxNative.prototype.toString = function () {
  // - Event keypress is not generated for keys related to movement, like PgUp, etc.
  //   So event keydown is used instead.
  // - Event keydown is used on text, not on head element to avoid problems with passing
  //   focus to text, when options are opened.
  this.validate();
  var EOL="\n";
  var IE50 = true;
  var IE60 = false;

  // Output head
  var html = "<span id=\""+this.name+"_id\""+
             " onmousedown=\"return "+this.objectName+".onHeadMouseDown();\""+
             " onmove=\"return "+this.objectName+".onHeadMove();\""+
             " onmouseup=\"return "+this.objectName+".onHeadMouseUp();\""+
             " onclick=\"return "+this.objectName+".toggleOptions();\"";
  if (this.isDisabled) {
      html +=" disabled";
  }
      html +=" class=tmPullDownHead "+
             " style=\"width:"+this.width+";height:"+this.height+";"+
                      "overflow:none;";
  if (IE50) {
      //html +=         "border:1px solid #999999;"; Moved to css
  } else if (IE60) {
      html +=         "border-bottom:1px solid #d4d0c8;border-right:1px solid #d4d0c8;"+
                      "border-left:1px solid #808080;border-top:1px solid #808080;"+
                      "word-spacing:-4;";
  }
     html +=          "vertical-align:top;"+
                      "white-space:nowrap;\">"+EOL+
                 "<span "+
                   " onkeydown=\"return "+this.objectName+".onHeadKeyDown();\""+
                   " onkeypress=\"return "+this.objectName+".onHeadKeyPress();\""+
                   " onfocus=\"return "+this.objectName+".onFocus();\""+
                   " onblur=\"return "+this.objectName+".onBlur();\""+
                   " tabindex=\""+this.tabIndex+"\""+
                   " style='width:"+this.width+";"+
                   "margin:0px;";
  if (IE50) {
      html +=      "border:0px;";
  } else if (IE60) {
      html +=      "border-right:0px;border-bottom:0px;border-top:1px solid #404040;border-left:1px solid #404040;"+
                   "word-spacing:0;";
  }
      html +=      "padding:1px;"+
                   "overflow-x:hidden;white-space:nowrap;"+
                   "vertical-align:top;"+
                   "'>&nbsp;</span>"+EOL+
                 "<img src=\""+i2uiImageDirectory+"/btn_multibox.gif\""+
                   " onfocus=\"blur();\""+
                   " style=\"border:0;margin:0;padding:0;";
  if (IE60) {
      html +=      "border-top:1px solid #404040;";
  }
      html +=      "vertical-align:top;"+
                   "\">"+EOL+
              "</span>"+EOL;

  // Output body
  var size = this.size;
  if (size > this.options.length) size = this.options.length;
  html += "<select"+
             " name=\""+(this.hasHiddenParam ? (this.name+"_") : this.name)+"\""+
             " size=\""+this.size+"\" multiple id=\""+this.name+"_options\""+
             " onblur=\"return "+this.objectName+".collapseOptions();\""+
             " onclick=\"return "+this.objectName+".onClick();\""+
             " onkeydown=\"return "+this.objectName+".onOptionKeyDown();\"" +
             " onkeypress=\"return "+this.objectName+".onOptionKeyPress();\"" +
             " onchange=\""+this.objectName+".showSelected();\""+
             " class=pulldown" +
             " style=\"position:absolute;top:0px;left:0px;visibility:hidden;z-index:20;\" >"+EOL;
    for (var i = 0; i < this.options.length; i++) {
      var option = this.options[i];
      if (option.length > 0) {
          html += "<option value=\""+option[0]+"\"";
          if (this.isSelected(option)) html+=" selected";
          html += ">"+option[1]+"</option>"+EOL;
      }
    }
  html += "</select>"+EOL;
  if(this.hasHiddenParam) {
    html += "<input id=\""+this.name+"_value\" name=\""+this.name+"\" type=hidden />"+EOL;
  }
  //  i2uitrace(html);
  html += "<script language=\"JavaScript\">"+
          this.objectName+".activate();"+
          "</script>"+EOL;
  return html;
}
