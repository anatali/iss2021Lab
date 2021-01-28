/*
webSocketUtils (within a web-page)
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
  console.log(`webSocketUtils | Message from server: ${message}`);
  if( message=="r" || message=="l" || message=="h" || message=="w" || message=="s" ){
       sendRequestData( message )
  }else{
       document.getElementById("robotDisplay").innerHTML= message;
  }
});

//Called by a click on r|lsocket
function requestTodoTheMove(move){
	console.log("webSocketUtils | requestTodoTheMove in webSocketUtils/utils " + move);
	socket.send( move );     //towards the server
}


