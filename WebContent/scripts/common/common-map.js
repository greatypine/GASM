/*!
 * Map:
 * 	方法：
 * 	1.put(key,value)
 * 	2.get(key) --> value
 * 	3.size() -->the size of map
 *  4.remove(key)
 *  5.setValue(key, value)
 *  6.getEntry(index) -->{key:xx,value:xx}
 *  7.indexOf(key) -->the index of key
 *  8.isEmpty() --> true/false.
 *  9.clear()
 */
var Map = function(){
    var Entry = function(key, value){
        this.key = key;
        this.value = value;
    };
	this.entries = new Array();
    this.put = function(key, value){
        for (var i = 0; i < this.entries.length; i++) {
            if (this.entries[i].key === key) {
                return false;
            }
        }
        this.entries.push(new Entry(key, value));
		return true;
    };
    this.get = function(key){
        for (var i = 0; i < this.entries.length; i++) {
            if (this.entries[i].key === key) {
                return this.entries[i].value;
            }
        }
        return null;
    };
	this.remove = function(key) {
		var index = this.indexOf(key);
		if(index != -1) {
			this.entries.splice(index,1);
		}
	};
	this.setValue = function(key, value) {
		var index = this.indexOf(key);
		if(index != -1) {
			this.entries[index].value = value;
		}
	};
	this.getEntry = function(index) {
		if(index >= 0 && index < this.size()) {
			return this.entries[index];
		}
		return null;
	};
    this.size = function(){
        return this.entries.length;
    };
    this.isEmpty = function(){
        return this.entries.length <= 0;
    };
	this.clear = function() {
		this.entries = [];
	};
	this.indexOf = function(key) {
		var index = -1;
		for(var i=0; i<this.size(); i++) {
			if(key == this.entries[i].key) {
				index = i;
				break;
			}
		}
		return index;
	};
	this.toString = function() {
		var str = '[';
		for(var i=0; i<this.size(); i++) {
			str += (this.entries[i].key + '=' 
				+ this.entries[i].value + ',')
		}
		str = str.substr(0, str.length-1);
		str += ']';
		return str;
	};
}
