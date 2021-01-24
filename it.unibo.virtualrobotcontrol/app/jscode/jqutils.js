$(document).ready(function(){
  $("p").click(function(){
    $(this).hide();
  });

//USED BY SOCKET.IO-BASED GUI
    $( "#rsocket" ).click(function() {  requestTodoTheMove("r") });    //defined in webSocketUtils
    $( "#lsocket" ).click(function() {  requestTodoTheMove("l") });


});

$(function () {
    $( "#ww" ).click(function() { sendRequestData( "w") });     //defined in utils
    $( "#ss" ).click(function() { sendRequestData( "s") });
    $( "#rr" ).click(function() { sendRequestData( "r") });
    $( "#ll" ).click(function() { sendRequestData( "l") });
    $( "#zz" ).click(function() { sendRequestData( "z") });
    $( "#xx" ).click(function() { sendRequestData( "x") });
    $( "#pp" ).click(function() { sendRequestData( "p") });
    $( "#hh" ).click(function() { sendRequestData( "h") });




});
