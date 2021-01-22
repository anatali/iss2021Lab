//const axios    = require('axios')
const net      = require('net');

const sep      = ";"

var stompClient = null;
var hostAddr = "http://localhost:8080/move";

function forward( cmd, host="localhost" ){
    payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
        //jsonObject = JSON.stringify(payload);
    msg        = sep+payload+sep;
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
}//forward






exports.forward = forward;