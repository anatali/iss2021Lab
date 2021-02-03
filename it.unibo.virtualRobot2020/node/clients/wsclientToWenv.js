/*
wsclientToWenv
using websocket :   (see https://www.npmtrends.com/socket.io-vs-websocket-vs-ws)
https://medium.com/@denizozger/finding-the-right-node-js-websocket-implementation-b63bfca0539
https://www.codecademy.com/register
https://www.npmjs.com/package/strong-agent
    performs forward - backward
    and then works as an observer
        rings a bell if there is a collision
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
     setTimeout( () => {
        doMove( "moveBackward" );
        console.log("now workign as an observer  ... " );
     }, 800 )
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
            console.log('Connection Closed');
        });
        connection.on('message', function(message) {
            if (message.type === 'utf8') {
                const msg = message.utf8Data
                console.log("Received: " + msg  )
                const msgJson = JSON.parse( msg )
                if(msgJson.collision) console.log("Received: collision=" + msgJson.collision)
                if(msgJson.sonarName){
                   console.log('\u0007');  //RING THE BELL
                   console.log("Received: sonar=" + msgJson.sonarName + " distance=" + msgJson.distance)
                }
            }
    });

});




client.connect('ws://localhost:8091', ''); //'echo-protocol'



