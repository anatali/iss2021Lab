/*
webSocketUtils (within a web-page)
*/


const wspath  = ("ws://"+location.host).replace("3000","");
const socket  = new WebSocket(wspath+'8085');

socket.addEventListener('open', () => {
  socket.send('Robot Gui is on');
});

socket.addEventListener('message', event => {
  var message = event.data;
  console.log(`Message from server: ${message}`);
  if( message=="r" || message=="l" || message=="h" || message=="w" || message=="s" ){
    sendRequestData( message )
  }else{
      //alert( $(document.display) );
      $(document.getElementById('robotDisplay').innerHTML=message);
  }
});

//Called by a click on r|lsocket
function requestTodoTheMove(move){
	console.log("requestTodoTheMove in webSocketUtils/utils " + move);
	socket.send( move );     //towards the server
}