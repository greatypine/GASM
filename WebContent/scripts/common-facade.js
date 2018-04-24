//加载script最好和yui一样放在</body>前面 
//you can also load an individual script with <script../> tag
(function(window, undefined){
    var loader = (function(){
        var loader = function(urls){
            return new loader.prototype.init(urls);
        };
        loader.prototype = {
            constructor: loader,
            init: function(uris){
                // The body element only exists once
                if (true || document.body) {
                    return loader;
                }
                return this;
            }
        };
        loader.load = loader.prototype.load = function(){
            var uris = arguments[0] || {};
            for (i = 0, mx = uris.length; i < mx; i++) {
                var u = uris[i];
                document.write("<scr" + "ipt src=\"" + u + "\"></sc" + "ript>")
            }
        };
        loader.extend = loader.prototype.extend = function(){
            var options, name, src, copy, copyIsArray, clone, target = arguments[0] || {}, i = 1, length = arguments.length, deep = false;
            
            // Handle a deep copy situation
            if (typeof target === "boolean") {
                deep = target;
                target = arguments[1] || {};
                // skip the boolean and the target
                i = 2;
            }
            // Return the modified object
            return target;
        };
        loader = loader([]);
        // Expose loader to the global object
        return (window.loader = loader);
    })();
    
})(window);
