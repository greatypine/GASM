/**
 * Make divs' width fit the appFrame's width.
 * @param {Array or jQuery Object} anDivList a list that contains div nodes.
 */
function adaptDiv(anDivList){
    var leftFrameWidth;
    try {
        leftFrameWidth = Number(top.document.getElementById("mainFramestId").cols.split(",")[0]) + 40;
    } 
    catch (e) {
        leftFrameWidth = 250;
    }
    jQuery(anDivList).each(function(){
        jQuery(this).css("width", window.screen.availWidth - leftFrameWidth);
    });
}