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
    $( "#ww" ).click(function() { sendRequestData( "w") });     //defined here
    $( "#ss" ).click(function() { sendRequestData( "s") });
    $( "#rr" ).click(function() { sendRequestData( "r") });
    $( "#ll" ).click(function() { sendRequestData( "l") });
    $( "#zz" ).click(function() { sendRequestData( "z") });
    $( "#xx" ).click(function() { sendRequestData( "x") });
    $( "#pp" ).click(function() { sendRequestData( "p") });
    $( "#hh" ).click(function() { sendRequestData( "h") });

//USED BY the human user
    $( "#displayconns" ).click(function() {  sendRequestData( "conns") });  //defined here
    $( "#clear" ).click(function() {  sendRequestData( "clearHistory") });

});


function sendRequestData( params, method) {
    console.log("sendRequestData in jscode/utils " + params);
    method = method || "post"; // il metodo POST usato di default
    //console.log(" sendRequestData  params=" + params + " method=" + method);
    var form = document.createElement("form");
    form.setAttribute("method", method);
    myip = location.host;
    form.setAttribute("action", "http://"+myip+"/"+params);
    var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", "move");
        hiddenField.setAttribute("value", params);
     	//console.log(" sendRequestData " + hiddenField.getAttribute("name") + " " + hiddenField.getAttribute("value"));
        form.appendChild(hiddenField);
    document.body.appendChild(form);
    console.log("body children num= "+document.body.children.length );
    form.submit();
    document.body.removeChild(form);
    console.log("body children num= "+document.body.children.length );

}

