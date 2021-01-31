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
  var message = event.data;
       document.getElementById("robotDisplay").innerHTML= message;
});

/*
Called by a click on rsocket|lsocket|... Communicates with server.js by using the socket
*/
function requestTodoTheMove(move){
	console.log("webSocketUtils | requestTodoTheMove in webSocketUtils/utils " + move);
	socket.send( move );     //towards server.js (see
}


