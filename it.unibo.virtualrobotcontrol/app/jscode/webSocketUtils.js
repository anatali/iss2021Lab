/*
webSocketUtils (to be loaded within the virtualrobotcontrol web-page index.html)
*/


const wspath  = ("ws://"+location.host).replace("3000","") +'3001';    //+'3001';
//const wspath  = ("ws://localhost:3001';
const socket  = new WebSocket(wspath);
//alert(WebSocket)
socket.addEventListener('connection', () => {
  socket.send('webSocketUtils | Robot WebGui is on');
});
socket.addEventListener('close', () => {
  socket.send('webSocketUtils | Robot WebGui is closed');
});
socket.addEventListener('message', event => {
  var message = event.data;
  document.getElementById("robotDisplay").innerHTML= message;
});


/*

//IMPORTANT: https://www.pegaxchange.com/2018/03/23/websocket-client/
//const WebSocketClient = require('websocket').client;
const wsWenvpath  = ("ws://"+location.host).replace("3000","")+'8091';
//const wspath  = ("ws://localhost:Wenv';
const client = new WebSocket(wsWenvpath);

    client.on('open', function(connection) {
        console.log('WebSocketClient |  Connected')
        conn8091 = connection
        connection.on('error', function(error) {
            console.log("WebSocketClient | Error: " + error.toString());
        });
        connection.on('close', function() {
            console.log('WebSocketClient | Connection Closed');
        });
        connection.on('message', function(message) {
            if (message.type === 'utf8') {
                const msg = message.utf8Data
                console.log("Received: " + msg  )
                const msgJson = JSON.parse( msg )
                if(msgJson.collision) console.log("WebSocketClient | Received: collision=" + msgJson.collision)
                if(msgJson.sonarName) console.log("WebSocketClient | Received: sonar=" + msgJson.sonarName + " distance=" + msgJson.distance)
            }
        });
    });
//client.connect(wsWenvpath, ''); //'echo-protocol'
*/

/*
Called by a click on rsocket|lsocket|... Communicates with server.js by using the socket
*/
function requestTodoTheMove(move){
	console.log("webSocketUtils | requestTodoTheMove in webSocketUtils/utils " + move);
	socket.send( move );     //towards server.js (see
}


//node webSocketUtils
