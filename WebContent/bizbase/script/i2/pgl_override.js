
var studioPage = false;
var transferBoxes = new Object();
var zoomFactor = 1.2;

function showBody()
{
  saveOldValues();
  hideRenderingMessage();
  setPageVisibility('visible');
  onLoad();
}

function printBody()
{
  setPageVisibility('visible');
  disablePage(false);
}

function onLoad()
{
  onLoadSuper();
  scrollHelper();
  autoCollapseRows();
}

function onResize()
{
  onResizeSuper();
  scrollHelper();
}

function onUnload()
{
  onUnloadSuper();
}

function onRefresh()
{
  history.go(0);
}

function onPrint()
{
  showPrintPage();
}

function window.onbeforeprint()
{
  var td = document.getElementById('print_options_td');
  if(td)
    td.style.display = 'none';
  enablePage();
}

function window.onafterprint()
{
  var td = document.getElementById('print_options_td');
  if(td){
    td.style.display = '';
    disablePage(false);
  }
}

function scrollHelper()
{
  defaultScrollers();
}

function i2uiRowSelectionCallback(tableId)
{
}

function displayBusyBox()
{
  displayProcessingRequestMessage();
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
