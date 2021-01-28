let express    = require('express');
let path       = require('path');
let fs         = require('fs');
let bodyParser = require('body-parser');
let app        = express();
//const net      = require('net');
//const request  = require('request') //deprecated

const { forward, connectAndSend, postTo8090 } = require('./serverutils')
var history    = "";

// view engine setup;
app.set('views', path.join(__dirname, './', 'views'));
app.set('view engine', 'ejs');

app.use(bodyParser.urlencoded({ extended: true }));
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

//HANDLE POST from 'conventional' HTML GUI
app.post("/w", function(req, res,next)  { handlePostMove("moveForward","moving ahead", req,res,next); });
app.post("/s", function(req, res,next)  { handlePostMove("moveBackward","moving back", req,res,next); });
app.post("/r", function(req, res,next)  { handlePostMove("turnRight","moving right",   req,res,next); });
app.post("/l", function(req, res,next)  { handlePostMove("turnLeft","moving left",     req,res,next); });
app.post("/h", function(req, res,next)  { handlePostMove("alarm","stop",               req,res,next); });

//HANDLE  utility commands
app.post("/conns", function(req, res,next)         { connectionHistory(); next()  });
app.post("/clearHistory", function(req, res,next)  { clearDisplayArea(); next()   });

//HANDLE POST from HTML GUI to send a POST to Wenv 8090
app.post("/l8090", function(req, res,next)  { postTo8090('turnLeft'); next()  });
app.post("/r8090", function(req, res,next)  { postTo8090('turnRight'); next()  });
app.post("/w8090", function(req, res,next)  { postTo8090('moveForward'); next()  });
app.post("/s8090", function(req, res,next)  { postTo8090('moveBackward'); next()  });
app.post("/h8090", function(req, res,next)  { postTo8090('alarm'); next()  });


/*
* ====================== REPRESENTATION ================
*/
app.use( function(req,res){
	console.log("server | SENDING THE ANSWER " + res  + " json:" + req.accepts('json') );
	try{
// if (req.accepts('json')) { res.send(history);		//give answer to curl / postman } else
	  //return res.render('index' );  //NO: we loose the message sent via socket.io
	   res.sendFile(path.join(__dirname, "index.html"));    //with robotDisplay area set with history
	}catch(e){
	    console.log("server | SORRY ..." + e);}
	}
);



function clearDisplayArea(){
    history = ""
}

function handlePostMove( cmd, msg, req, res, next ){
    console.log( "server |  handlePostMove in server.js "  + cmd )
    forward(cmd, "localhost");
    updateRobotState(  cmd );
    //res.sendFile(path.join(__dirname, "index.html"));  //
    //res.sendStatus(200);
    next();     //see REPRESENTATION
}

/*
============================================================================================
WebSockets SECTION
============================================================================================
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
        console.log("server | client connected ")
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
    console.log("server | socket-on received from a client: "+message);
    if( message=="r" || message=="l" || message=="h" || message=="w" || message=="s" ){
        forward( message  );   //DISCONNECT
        //rimbalzo del comando al
        //wsServer.clients.forEach(client => { client.send(  message ); });
    }else if( message.includes("close")) {
        /*
        var clients = wsServer.clients.size;
        history = history + "<br/>" + "connection_connected=" + (clients-1)  ;
        wsServer.clients.forEach(client => {
              client.send(   history );
        });*/
        //connectionHistory();
        console.log("server | socket-on - the client is disconnected ");
    }
  });
});
//-------------------------------------- WebSockets SECTION END

/*
========================================================================

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
*/
/*
 * POST request to Wenv server on 8090 using axios
 * since request is deprecated

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
*/

//HTTP POST request to 8090
/*
//DEPRECATED
function postTo8090(move){
request.post(
  'http://localhost:8090/api/'+move,
  {
    json: {
      move: ''+move,
    },
  },
  (error, res, body) => {
    if (error) {
      console.error(error)
      return
    }
    console.log(`statusCode: ${res.statusCode}`)
    console.log(body)
  }
)
}
*/

/*
 * ============ ERROR HANDLING =======
 */
 // catch 404 and forward to error handler;
 app.use(function(req, res, next) {
   var err = new Error('Not Found');
   err.status = 404;
   next(err);
 });

 // error handler;
 app.use(function(err, req, res, next) {
   // set locals, only providing error in development
   res.locals.message = err.message;
   res.locals.error = req.app.get('env') === 'development' ? err : {};

   // render the error page;
   res.status(err.status || 500);
   res.render('error');
 });


app.listen(3000, function () {
  console.log("app listening on port 3000 with __dirname=" + __dirname);
});