/*
webSocketUtils (within a web-page)
*/

//alert("webSocketUtils");

const wspath  = ("ws://"+location.host).replace("3000","")+'8085';
const socket  = new WebSocket(wspath);

socket.addEventListener('open', () => {
  socket.send('Robot Gui is on');
});
socket.addEventListener('close', () => {
  socket.send('Robot Gui is closed');
});
socket.addEventListener('message', event => {
  var message = event.data;
  console.log(`Message from server: ${message}`);
  if( message=="r" || message=="l" || message=="h" || message=="w" || message=="s" ){
    sendRequestData( message )
  }else{
       //var v = document.getElementById('robotDisplay').innerHTML    ;
       document.getElementById("robotDisplay").innerHTML= message; //v + "<br/>" + message;
  }
});

//Called by a click on r|lsocket
function requestTodoTheMove(move){
	console.log("requestTodoTheMove in webSocketUtils/utils " + move);
	socket.send( move );     //towards the server
}


