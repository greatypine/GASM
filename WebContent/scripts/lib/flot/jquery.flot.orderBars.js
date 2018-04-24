/* 
 * Flot plugin to order bars side by side.
 *
 * This plugin is an alpha version.
 *
 * To activate the plugin you must specify the parameter "order" for the specific serie :
 *
 *  $.plot($("#placeholder"), [{ data: [ ... ], bars :{ order = null or integer }])
 *
 * If 2 series have the same order param, they are ordered by the position in the array;
 *
 * The plugin adjust the point by adding a value depanding of the barwidth
 * Exemple for 3 series (barwidth : 0.1) :
 *
 *          first bar décalage : -0.15
 *          second bar décalage : -0.05
 *          third bar décalage : 0.05
 *
 */

(function($){
    function init(plot){
        var orderedBarSeries;
        var nbOfBarsToOrder;
        var borderWidth;
        var borderWidthInXabsWidth;
        var pixelInXWidthEquivalent = 1;

       
        /*
         * This method add shift to x values
         */
        function reOrderBars(plot, serie, datapoints){
            var shiftedPoints = null;
            if(serieNeedToBeReordered(serie)){
            	// calculate what the value of 1px is (scaled according to xaxis values in the data)
                calculPixel2XWidthConvert(plot);
                // set orderedBarSeries = array of series, nbOfBarsToOrder = number of series
                retrieveBarSeries(plot);
                // calculate the scaled border width
                calculBorderAndBarWidth(serie);

                // if there are more than 2 bars to order
                if(nbOfBarsToOrder >= 2){  
                	// find position of each series (i.e. the order)
                    var position = findPosition(serie);
                    var dx = 0;
                    
                    // determine if there are an odd or even number of bars. If odd, need to do more calculations (see the function below).
                    // if even, it will return 0.
                    var centerBarShift = calculCenterBarShift();

                    // isBarAtLeftOfCenter(position) returns true or false
                    dx = (1/(nbOfBarsToOrder+1))*(position-1);
                    shiftedPoints = shiftPoints(datapoints,serie,dx);
                    
                    //alert("shiftedPoints: " +  shiftedPoints);
                    datapoints.points = shiftedPoints;
               }
           }
           return shiftedPoints;
        }

        function serieNeedToBeReordered(serie){
            return serie.bars != null
                && serie.bars.show
                && serie.bars.order != null;
        }

        function calculPixel2XWidthConvert(plot){
        	// overall grid width
            var gridWidth = plot.getPlaceholder().innerWidth();
            // obtain min and maximum values of data - 1,5
            var minMaxValues = getXabsMinMaxValues(plot.getData());
            // range of data = maximum value minus minimum value - 4
            var XAbscisseWidth = minMaxValues[1] - minMaxValues[0];
            //alert("hi:" + XAbscisseWidth / gridWidth);
            // pixelInXWidthEquivalent is the equivalent value of what 1 px would be
            // e.g. 1 = min value. 10 = max value. 10-1 = 9. grid = 600px wide. 9/600 = 0.015. 0.015 is the value of 1 px (to scale) 
            //pixelInXWidthEquivalent = 0.006666666666666667;
            pixelInXWidthEquivalent = XAbscisseWidth / gridWidth;
            
            //return pixelInXWidthEquivalent;
        }

        function getXabsMinMaxValues(series){
            var minMaxValues = new Array();
            for(var i = 0; i < series.length; i++){
                minMaxValues[0] = series[i].data[0][0];
                minMaxValues[1] = series[i].data[series[i].data.length - 1][0];
            }
            return minMaxValues;
        }

        function retrieveBarSeries(plot){
        	// array of objects
            orderedBarSeries = findOthersBarsToReOrders(plot.getData());
            // shows number of series
            nbOfBarsToOrder = orderedBarSeries.length;
        }

        function findOthersBarsToReOrders(series){
            var retSeries = new Array();

            for(var i = 0; i < series.length; i++){
                if(series[i].bars.order != null && series[i].bars.show){
                    retSeries.push(series[i]);
                }
            }

            return retSeries.sort(sortByOrder);
        }

        function sortByOrder(serie1,serie2){
            var x = serie1.bars.order;
            var y = serie2.bars.order;
            return ((x < y) ? -1 : ((x > y) ? 1 : 0));
        }

        function  calculBorderAndBarWidth(serie){
        	// retrieve border width - if no border width has been set, set it to the default value of 2
            borderWidth = serie.bars.lineWidth ? serie.bars.lineWidth  : 2;
            // pixelInXWidthEquivalent = scale of what 1 px is. Determine what the scaled border width is (see pixelInWidthEquivalent above)
            borderWidthInXabsWidth = borderWidth * pixelInXWidthEquivalent;
            
            //alert("borderWidth " + borderWidth + " pixelInXWidthEquivalent " + pixelInXWidthEquivalent);
        }

        function findPosition(serie){
            var pos = 0
            for (var i = 0; i < orderedBarSeries.length; ++i) {
                if (serie == orderedBarSeries[i]){
                    pos = i;
                    break;
                }
            }

            return pos+1;
        }

        function calculCenterBarShift(){
            var width = 0;

            // if there are an odd number of series
            if(nbOfBarsToOrder%2 != 0)
                width = (orderedBarSeries[Math.ceil(nbOfBarsToOrder / 2)].bars.barWidth)/2;

            return width;
        }

        function isBarAtLeftOfCenter(position){
            return position <= Math.ceil(nbOfBarsToOrder / 2);
        }

        function sumWidth(series,start,end){
        	//alert("series: " +  series + " start: " + start + " end: " + end);
        	// series array, 0 , 1
            var totalWidth = 0;

            for(var i = start; i <= end; i++){
            	//alert(series[i].bars.barWidth);
                totalWidth += series[i].bars.barWidth+borderWidthInXabsWidth*2;
                
                //alert("barWidth: " + series[i].bars.barWidth + " borderWidthInXabsWidth: " + borderWidthInXabsWidth + " totalWidth: " + totalWidth);
                // 414720000 + 17280000 * 2
            }
            
            //alert("totalwidth: " + totalWidth);
            // 449,280,000
            
            
            return totalWidth;
        }

        function shiftPoints(datapoints,serie,dx){
        	//alert("datapoints: " + datapoints + " serie: " + serie[0] + " dx: " + dx);
        	
            var ps = datapoints.pointsize;
            //alert("ps: " + ps);
            var points = datapoints.points;
            
            //alert("first points: " + points + ":end.");
            var j = 0;

            for(var i = 0; i < points.length; i += ps){
                points[i] += dx;
                //alert("points[i]" + points[i]);
                //Adding the new x value in the serie to be able to display the right tooltip value,
                //using the index 3 to not overide the third index.
                serie.data[j][3] = points[i];
                //alert("serie.data[j][0]: " + serie.data[j][0] + ", serie.data[j][1]: " + serie.data[j][1] + ", serie.data[j][2]: " + serie.data[j][2] + ", serie.data[j][3]: " + serie.data[j][3]);
                j++;
            }
            
            //alert("second points: " + points + ":end.");

            return points;
        }

        plot.hooks.processDatapoints.push(reOrderBars);

    }

    var options = {
        series : {
            bars: {order: null} // or number/string
        }
    };

    $.plot.plugins.push({
        init: init,
        options: options,
        name: "orderBars",
        version: "0.2"
    });

})(jQuery)

