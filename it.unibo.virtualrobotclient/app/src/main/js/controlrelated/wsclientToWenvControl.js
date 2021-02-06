/*
wsclientToWenvControl
works as observer
*/
const WebSocketClient = require('websocket').client;

var client = new WebSocketClient();

client.on('connectFailed', function(error) {
    console.log('Connect Error: ' + error.toString());
});

client.on('connect', function(connection) {
    console.log('WebSocket Client Connected');
    connection.on('error', function(error) {
        console.log("Connection Error: " + error.toString());
    });
    connection.on('close', function() {
        console.log('echo-protocol Connection Closed');
    });
    connection.on('message', function(message) {
        //console.log("Received: %s" , message  );
        if (message.type === 'utf8') {
            console.log("Received: '" + message.utf8Data + "'");
        }
    });

    function doMove(move) {
        if (connection.connected) {
            connection.send(move)
        }
    }
    //doMove("turnLeft");
});

client.connect('ws://localhost:3000', ''); //'echo-protocol'


