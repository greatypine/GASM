/**
 * start jquery ready function.
 * @author He Wang
 * @since 2011/05/25
 */
$(document).ready(function() {
	var default_ready = ["$$.run", "$form.run"];
	var methods = default_ready;
	if ( typeof PMS_READY != 'undefined') {
		methods = PMS_READY;
	}
	for(var i=0; i<methods.length; i++) {
		if (typeof methods[i] == 'function') {
			methods[i]();
		} else {
			eval(methods[i] + "()");
		}
	}
});
