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
  }
});


function doTheMove(move){
	console.log("sendTheMove in webSocketUtils/utils " + move);


	socket.send( move );
	//forward( move );    //ERROR: no-net
    //stompClient.send("/app/move", {}, JSON.stringify({'name': move }));
}