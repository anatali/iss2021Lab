var outs = ""
var output = function( msg ){
	outs = outs + msg + " | ";
	var el = React.createElement('p', {className:'header', key:'header'},outs);//
 	ReactDOM.render( el, document.getElementById('output') );
 }
output("esJsx.js loaded ")