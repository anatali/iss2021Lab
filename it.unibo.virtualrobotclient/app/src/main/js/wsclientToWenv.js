/*
wsclientToWenv
*/
const WebSocketClient = require('websocket').client;

var client = new WebSocketClient();

    function doMove(move) {

        const moveJson = '{"robotmove":"'+ move +'"}'
        console.log("doMove moveJson:" + moveJson);
        //const moveJsonStr = JSON.stringify( moveJson )
        //console.log("doMove moveJsonStr:" + moveJsonStr);

        if (conn8091) { conn8091.send(moveJson) }
    }

function doJob(){
      const moveTodo = "{\"robotmove\":\"turnLeft\"}"
      console.log("doJob moveTodo:" + moveTodo);
     //doMove("{\"robotmove\":\"turnLeft\"}");
     doMove( "moveForward" )
}

client.on('connectFailed', function(error) {
    console.log('Connect Error: ' + error.toString());
});
var conn8091
    client.on('connect', function(connection) {
        console.log('WebSocket Client Connected')
        conn8091 = connection
        doJob()

        connection.on('error', function(error) {
            console.log("Connection Error: " + error.toString());

        });
        connection.on('close', function() {
            console.log('echo-protocol Connection Closed');
        });
        connection.on('message', function(message) {
            if (message.type === 'utf8') {
                const msg = message.utf8Data
                console.log("Received: " + msg  )
                const msgJson = JSON.parse( msg )
                console.log("Received: " + msgJson.collision)
            }
    });
/*
    function doMove(move) {
        if (connection.connected) {
            connection.send(move)
        }
    }
*/
    //doMove("{\"robotmove\":\"turnLeft\"}");
});


/*
setTimeout( () => {
        doMove("{\"robotmove\":\"turnLeft\"}");
        conn8091.disconnect()
    } , 1000);
*/


client.connect('ws://localhost:8091', ''); //'echo-protocol'



