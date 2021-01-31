/*
jscode/jqutils.js
*/

$(document).ready(function(){
  $("pre").click(function(){ $(this).hide(); });

//USED BY SOCKET.IO-BASED GUI
    $( "#rsocket" ).click(function() {  requestTodoTheMove("turnRight") });    //defined in webSocketUtils
    $( "#lsocket" ).click(function() {  requestTodoTheMove("turnLeft") });
    $( "#wsocket" ).click(function() {  requestTodoTheMove("moveForward") });
    $( "#ssocket" ).click(function() {  requestTodoTheMove("moveBackward") });
    $( "#hsocket" ).click(function() {  requestTodoTheMove("alarm") });

});

//USED BY POST with jQuery
$(function () {
/*  USED WITH TCPServer (version before Jan 2021)
    $( "#wjquery" ).click(function() { sendRequestData( "w") });     //defined here
    $( "#sjquery" ).click(function() { sendRequestData( "s") });
    $( "#rjquery" ).click(function() { sendRequestData( "r") });
    $( "#ljquery" ).click(function() { sendRequestData( "l") });
    $( "#hjquery" ).click(function() { sendRequestData( "h") });
*/
    $( "#lpost8090").click(function() { doPostToWenv( "l8090") })   //defined here
    $( "#rpost8090").click(function() { doPostToWenv( "r8090") })
    $( "#wpost8090").click(function() { doPostToWenv( "w8090") })
    $( "#spost8090").click(function() { doPostToWenv( "s8090") })
    $( "#hpost8090").click(function() { doPostToWenv( "h8090") })

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


