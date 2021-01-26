let express    = require('express');
let path       = require('path');
let fs         = require('fs');
let bodyParser = require('body-parser');
let app        = express();
//let utils      = require('./serverutils');
const net      = require('net');

var history    = "";
/*
WebSockets
See https://www.ably.io/topic/websockets
https://medium.com/hackernoon/implementing-a-websocket-server-with-node-js-d9b78ec5ffa8
See https://www.html.it/pag/54040/websocket-server-con-node-js/
*/
var clients = 0;
const WebSocket   = require('ws');

const wsServer    = new WebSocket.Server({ server: app.listen(8085) });


function updateRobotState(message){
    history = history + "<br/>" + message;
    console.log(history);
    wsServer.clients.forEach(client => {
        client.send( history );
    });
}

function connectionHistory(){
        var clients = wsServer.clients.size
        history     = history + "<br/>" + "connections=" + clients
        //socket.emit('broadcast',{ description: clients + ' clients connected!'});
        displayHistory()
}
function addToHistory( msg ){
        history     = history + "<br/>" + msg
        displayHistory()
}
function displayHistory(){
        wsServer.clients.forEach(client => {
            client.send(   history )
        })
}
/*
const server    = new WebSocket.Server({ server: app.listen(8085) });
*/
wsServer.on('connection', socket => {
        console.log(`client connected `)
        displayHistory()
/*
     socket.on('disconnection', function () {
        clients--;
        alert("client disconnected");
          wsServer.clients.forEach(client => {
              client.send(  "disconnection_connected=" + clients );
          });
                //io.sockets.emit('broadcast',{ description: clients + ' clients connected!'});
     });
*/
  socket.on('message', message => {
    console.log(`server | received from a client: ${message}`);
    if( message=="r" || message=="l" || message=="h" || message=="w" || message=="s" ){
        //utils.forward( message  );   //DISCONNECT
        //rimbalzo del comando al
        wsServer.clients.forEach(client => {
            client.send(  message );
        });
    }else if( message.includes("close")) {
        /*
        var clients = wsServer.clients.size;
        history = history + "<br/>" + "connection_connected=" + (clients-1)  ;
        wsServer.clients.forEach(client => {
              client.send(   history );
        });*/
        //connectionHistory();
        console.log(`client disconnected `);
    }
  });
});
/*
wsServer.on('close', () => {
    alert("disconnection")
});
*/

//-------------------------------------- WebSockets

// view engine setup;
app.set('views', path.join(__dirname, './', 'views'));
app.set('view engine', 'ejs');


app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());

app.get('/', function (req, res) {
	console.log("server get " + __dirname );
    res.sendFile(path.join(__dirname, "index.html"));
  });

app.get('/info', function (req, res) {
  res.send( history );
});

app.get('/picture', function (req, res) {
  let img = fs.readFileSync(path.join(__dirname, "images/profile-1.jpg"));
  res.writeHead(200, {'Content-Type': 'image/jpg' });
  res.end(img, 'binary');
});

/*
THE CODE IN THIS DIR CAN be used in index.html
*/
app.use(express.static(path.join(__dirname, './jscode')));


app.post("/w", function(req, res,next)  { handlePostMove("moveForward","moving ahead", req,res,next); });
app.post("/s", function(req, res,next)  { handlePostMove("moveBackward","moving back", req,res,next); });
app.post("/r", function(req, res,next)  { handlePostMove("turnRight","moving right",   req,res,next); });
app.post("/l", function(req, res,next)  { handlePostMove("turnLeft","moving left",     req,res,next); });
app.post("/h", function(req, res,next)  { handlePostMove("alarm","stop",               req,res,next); });


app.post("/conns", function(req, res,next)  { console.log(" ..... conns"); connectionHistory(); next()  });

/*
* ====================== REPRESENTATION ================
*/
app.use( function(req,res){
	//console.info("SENDING THE ANSWER " + res  + " json:" + req.accepts('json') );
	try{
	    //console.log("answer> "+ Object(res)  );
/*
	   if (req.accepts('json')) {
	       res.send(history);		//give answer to curl / postman
	   } else {
	       res.sendFile(path.join(__dirname, "index.html"));    //with robotDisplay area set with history
	   };
*/
	   //return res.render('index' );  //NO: we loose the message sent via socket.io
	   res.sendFile(path.join(__dirname, "index.html"));    //with robotDisplay area set with history
	}catch(e){console.info("SORRY ..." + e);}
	}
);

/*
 * ============ ERROR HANDLING =======
 */



function handlePostMove( cmd, msg, req, res, next ){
    //result = "Web server done: " + cmd;
    console.log( "handlePostMove in server.js "  + cmd )
    forward(cmd, "localhost");
    updateRobotState(  cmd );
    //res.sendFile(path.join(__dirname, "index.html"));  //
    //res.sendStatus(200);
    next();     //see REPRESENTATION
}

/*
========================================================================
*/
const sep      = ";"

var stompClient = null;
var host    = "localhost";
var counter = 0;

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

      var v = data.toString().replace(";","").replace(";","")
      if( v.includes("webpage-ready") ) return;
      console.log("serverUtils | from wenv server: "+ v);   // {"type":"collision","arg":{"objectName":"bottle1"}}
      const ev     = JSON.parse( v )
      const target = JSON.parse( JSON.stringify( ev.arg ) )
      addToHistory( "EVENT:" + ev.type + " TARGET:" + target.objectName );
    })
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
//useful for HTTP requests
axios
  .post('http://192.168.1.7:8999', {
    todo: msg
  })
  .then(res => {
    console.log(`statusCode: ${res.statusCode}`)
    console.log(res)
  })
  .catch(error => {
    console.error(error)
  })
  		res.end(result, 'text')

*/



app.listen(3000, function () {
  console.log("app listening on port 3000 with __dirname=" + __dirname);
});