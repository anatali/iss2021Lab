/*
serverUtils.js
*/

//const axios    = require('axios')
const net      = require('net');

const sep      = ";"

var stompClient = null;
var host    = "localhost";
var counter = 1;

    function connect( msg ){
    client = net.connect(8999, host, () => {
          // 'connect' listener
          console.log('connected to server ' + host + " counter=" + counter++ );
          client.write(msg+'\r\n');
    })

    client.on('error', () => {
      console.log('ERROR with host=' + host);
      //if( host="localhost" ) forward( cmd, "wenv")  //in the case of docker-compose
    });
    client.on('data', (data) => {
      console.log("from wenv server: "+data.toString());
    });
    client.on('end', () => {
      console.log('disconnected from server counter=' + counter );
    });
}
 


function forward( cmd  ){
    payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
        //jsonObject = JSON.stringify(payload);
    msg        = sep+payload+sep;
    console.log('forward ' + msg ); //+ " client=" + client
    connect(msg);
}//forward

function wsforward( cmd, host="localhost"  ){
    payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
        //jsonObject = JSON.stringify(payload);
    msg        = sep+payload+sep;
    console.log('wsforward ' + msg );
    const clients = net.connect(8999,host, () => {
      // 'connect' listener
      console.log('connected to server to send:' + msg);
      clients.write(msg+'\r\n');
    })

    clients.on('error', () => {
      console.log('ERROR with host=' + host);
      if( host="localhost" ) forward( cmd, "wenv")
    });
    clients.on('data', (data) => {
      console.log("from wenv server: "+data.toString());
      //clients.end();
    });
    clients.on('end', () => {
      console.log('disconnected from server');
    });

}//wsforward

exports.forward   = forward;
exports.wsforward = wsforward;