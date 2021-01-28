/*
serverUtils.js
*/

const axios    = require('axios')
const net      = require('net');

const sep      = ";"

var host    = "localhost";
var counter = 0;

/*
Invia su TCP
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

/*
*/
function postTo8090(move){
const URL = 'http://localhost:8090/api/move' ;

axios
  .post(URL, {
    robotmove: move
  })
  .then(res => {
    console.log(`statusCode: ${res.statusCode}`)
    //console.log(res)
  })
  .catch(error => {
    console.error(error)
  })
}


//exports.forward   = forward;
module.exports = { forward, connectAndSend, postTo8090 }