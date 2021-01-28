/*
webSocketUtils (to be loaded within the virtualrobotcontrol web-page index.html)
*/
const wspath  = ("ws://"+location.host).replace("3000","")+'8085';
const socket  = new WebSocket(wspath);

socket.addEventListener('open', () => {
  socket.send('webSocketUtils | Robot Gui is on');
});
socket.addEventListener('close', () => {
  socket.send('webSocketUtils | Robot Gui is closed');
});
socket.addEventListener('message', event => {
/*
Map a message sent over the socket by the virtualrobotcontrol web-page index.html
into a POST for a move
*/
  var message = event.data;
  console.log(`webSocketUtils | Message from server: ${message}`);

  if( message=="r" || message=="l" || message=="h" || message=="w" || message=="s" ){
       sendRequestData( message )   //defined in jqutils
  }else{
       document.getElementById("robotDisplay").innerHTML= message;
  }
});

/*
Called by a click on rsocket|lsocket|...
Communicates with server.js by using the socket
*/
function requestTodoTheMove(move){
	console.log("webSocketUtils | requestTodoTheMove in webSocketUtils/utils " + move);
	socket.send( move );     //towards server.js (see
}


