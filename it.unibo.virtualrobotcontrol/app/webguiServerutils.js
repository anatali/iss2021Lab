/*
serverUtils.js
*/

const axios             = require('axios')
const net               = require('net');
const WebSocketClient   = require('websocket').client;

const sep      = ";"

var host    = "localhost";
var counter = 0;

/*
POST HTTP a Wenv
*/
async function postTo8090(move, updateHistory){
const URL = 'http://localhost:8090/api/move' ;

await axios({
            url: URL,
            data: { robotmove: move },
            method: 'POST',
            timeout: 1000,
            headers: { 'Content-Type': 'application/json' }
/*
  .post(URL, {
    robotmove: move
*/
    }).then(response => {
    console.log("serverutils postTo8090 | statusCode: " + response.status  )
    console.log(response.data)  //"collision": "false", "move": "turnLeft" }
    const collision = JSON.parse( response.data ).collision
    //console.log("serverutils postTo8090 | statusText: " + response.statusText);
    if( response.status == "200")  updateHistory( move + " | collision=" + collision )
    else updateHistory( move + " | response.status=" + response.status )
 })
  .catch(error => {
    console.error(error)
  })
}

/* */
//Is it useful to receive state data? Yes if this embeds some control logic


var conn8091

var client = new WebSocketClient();
client.on('connectFailed', function(error) {
    console.log('Connect Error: ' + error.toString());
});

    client.on('connect', function(connection) {
        console.log('WebSocket Client Connected')
        conn8091 = connection

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
                if(msgJson.collision) console.log("Received: collision=" + msgJson.collision)
                if(msgJson.sonarName) console.log("Received: sonar=" + msgJson.sonarName + " distance=" + msgJson.distance)
            }
    });
});
client.connect('ws://localhost:8091', ''); //'echo-protocol'

/*
Invia su TCP (obsoleto da Jan 2021)
*/
function connectAndSend( msg  ){
    var client = new net.Socket();
    client.connect(8999, host, () => {
          // 'connect' listener
          console.log('serverUtils | connected to virtual robot server on ' + host + " counter=" + ++counter );
          client.write(msg+'\r\n');
          //client.end();
    })

    client.on('error', () => {
      console.log('serverUtils | ERROR with host=' + host);
      //if( host="localhost" ) forward( cmd, "wenv")  //in the case of docker-compose
    });
    client.on('data', (data) => {
      var v = data.toString();
      console.log("serverUtils | receives from wenv server: "+ v);
      //addToHistory(v);
    });
    client.on('end', () => {
      console.log('serverUtils | disconnected from server counter=' + counter );
    });
}

function forward( cmd  ){
    payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
    msg        = sep+payload+sep;
    console.log('serverUtils | forward ' + msg ); //+ " client=" + client
    connectAndSend(msg);
}//forward

//exports.forward   = forward;
module.exports = { forward, connectAndSend, postTo8090 }