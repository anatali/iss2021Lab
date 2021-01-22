$(function () {
     $("form").on('submit', function (e) {
         e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });

//USED BY SOCKET.IO-BASED GUI

    $( "#h" ).click(function() {  sendTheMove("h") });
    $( "#w" ).click(function() {  sendTheMove("w") });
    $( "#s" ).click(function() {  sendTheMove("s") });
    $( "#r" ).click(function() {  sendTheMove("r") });
    $( "#l" ).click(function() {  sendTheMove("l") });
    $( "#x" ).click(function() {  sendTheMove("x") });
    $( "#z" ).click(function() {  sendTheMove("z") });
    $( "#p" ).click(function() {  sendTheMove("p") });

    //$( "#rr" ).click(function() { console.log("submit rr"); redirectPost("r") });
    //$( "#rrjo" ).click(function() { console.log("submit rr"); jqueryPost("r") });

//USED BY POST-BASED GUI

    $( "#ww" ).click(function() { sendRequestData( "w") });
    $( "#ss" ).click(function() { sendRequestData( "s") });
    $( "#rr" ).click(function() { sendRequestData( "r") });
    $( "#ll" ).click(function() { sendRequestData( "l") });
    $( "#zz" ).click(function() { sendRequestData( "z") });
    $( "#xx" ).click(function() { sendRequestData( "x") });
    $( "#pp" ).click(function() { sendRequestData( "p") });
    $( "#hh" ).click(function() { sendRequestData( "h") });

//USED BY POST-BASED BOUNDARY
    $( "#start" ).click(function() { sendRequestData( "w") });
    $( "#stop" ).click(function()  { sendRequestData( "h") });

	$( "#update" ).click(function() { sendUpdateRequest(  ) });

});
