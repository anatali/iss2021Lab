/*
serverUtils.js
*/

//const axios    = require('axios')
const net      = require('net');

const sep      = ";"

var stompClient = null;
var host    = "localhost";
var counter = 0;

    function connectAndSend( msg ){
    var client = new net.Socket();
    client.connect(8999, host, () => {
          // 'connect' listener
          console.log('connected to server ' + host + " counter=" + ++counter );
          client.write(msg+'\r\n');
          //client.end();
    })

    client.on('error', () => {
      console.log('ERROR with host=' + host);
      //if( host="localhost" ) forward( cmd, "wenv")  //in the case of docker-compose
    });
    client.on('data', (data) => {
      var v = data.toString();
      console.log("from wenv server: "+ v);
      /*
      if( v.includes("webpage-ready")){
            client.write(msg+'\r\n');
            console.log('end the client ???'  );
            client.destroy(); // kill client after server's response
      }*/
    });
    client.on('end', () => {
      console.log('disconnected from server counter=' + counter );
    });
}

function forward( cmd  ){
    payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
    msg        = sep+payload+sep;
    console.log('serverUtils | forward ' + msg ); //+ " client=" + client
    connectAndSend(msg);
}//forward



exports.forward   = forward;
