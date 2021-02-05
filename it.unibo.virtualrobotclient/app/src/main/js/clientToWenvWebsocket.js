/*
clientToWenvWebsocket.js
===============================================================
    performs forward - backward
    and then works as an observer
        rings a bell if there is a collision
===============================================================
*/
const WebSocketClient = require('websocket').client;

var client = new WebSocketClient();

    function doMove(move, connection) {
        const moveJson = '{"robotmove":"'+ move +'"}'
        console.log("doMove moveJson:" + moveJson);
        if (connection) { connection.send(moveJson) }
    }

function doJob(connection){
      //const moveTodo = "{\"robotmove\":\"turnLeft\"}"
      //console.log("doJob moveTodo:" + moveTodo);
     //doMove("{\"robotmove\":\"turnLeft\"}");
     doMove( "moveForward", connection )
     setTimeout( () => {
        doMove( "moveBackward", connection );
        console.log("now working as an observer  ... " );
     }, 1000 )
}



    client.on('connect', function(connection) {
        console.log('Client Connected')
        doJob(connection)

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
                if(msgJson.endmove) {
                    console.log("Received: endmove=" + msgJson.endmove)
                }
                if(msgJson.collision) {
                   console.log("Received: collision=" + msgJson.collision)
                   console.log('\u0007');  //RING THE BELL
                }
                if(msgJson.sonarName){
                   console.log("Received: sonar=" + msgJson.sonarName + " distance=" + msgJson.distance)
                }
            }
    });
    client.on('connectFailed', function(error) {
        console.log('Connect Error: ' + error.toString());
    });

});


client.connect('ws://localhost:8091', ''); //'echo-protocol'



