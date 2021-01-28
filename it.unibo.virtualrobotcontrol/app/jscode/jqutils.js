/*
jscode/jqutils.js
*/

$(document).ready(function(){
  $("p").click(function(){
    $(this).hide();
  });

//USED BY SOCKET.IO-BASED GUI
    $( "#rsocket" ).click(function() {  requestTodoTheMove("r") });    //defined in webSocketUtils
    $( "#lsocket" ).click(function() {  requestTodoTheMove("l") });

});

//USED BY POST with jQuery
$(function () {
    $( "#ww" ).click(function() { doPostToWenv( "w") });     //defined here
    $( "#ss" ).click(function() { doPostToWenv( "s") });
    $( "#rr" ).click(function() { doPostToWenv( "r") });
    $( "#ll" ).click(function() { doPostToWenv( "l") });
    $( "#zz" ).click(function() { doPostToWenv( "z") });
    $( "#xx" ).click(function() { doPostToWenv( "x") });
    $( "#pp" ).click(function() { doPostToWenv( "p") });
    $( "#hh" ).click(function() { doPostToWenv( "h") });

    $( "#lpost8090").click(function() { doPostToWenv( "l8090") })
    $( "#rpost8090").click(function() { doPostToWenv( "r8090") })

//USED BY the human user
    $( "#displayconns" ).click(function() {  doPostToWenv( "conns") });  //defined here
    $( "#clear" ).click(function() {  doPostToWenv( "clearHistory") });

});

/*
======================================================================
*/
function doPostToWenv( params, method ) {
     var myip = location.host;
     var url = "http://"+myip+"/"+params
     doPost(params, method, url);
}

function doPost( params, method, url ) {
    console.log("doPostToWenv in jscode/jqutils " + params);
    method = method || "post"; // il metodo POST usato di default
    //console.log(" doPostToWenv  params=" + params + " method=" + method);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    form.setAttribute("action", url);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" doPostToWenv " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );
}


