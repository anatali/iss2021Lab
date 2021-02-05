/*
webguiServer.js
*/

let express             = require('express');
let path                = require('path');
let fs                  = require('fs');
let bodyParser          = require('body-parser');
let app                 = express();
const WebSocketClient   = require('websocket').client
//const request  = require('request') //deprecated

const { forward, connectAndSend, postTo8090 } = require('./webguiServerutils')
var history    = "";

// view engine setup;
app.set('views', path.join(__dirname, './', 'views'));
app.set('view engine', 'ejs');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.get('/', function (req, res) {
	console.log("webguiServer get " + __dirname );
    res.sendFile(path.join(__dirname, "index.html"));
  });

app.get('/guisimple', function (req, res) {
	console.log("webguiServer guisimple get " + __dirname );
	res.render("indexSimple.ejs", {})
  });

app.post('/guisimple', function (req, res) { res.render("indexSimple.ejs", {})  });

app.get('/guiJquery', function (req, res) {
	console.log("webguiServer guiJquery get " + __dirname );
	res.render("indexJquery.ejs", {})
  });
app.post('/guiJquery', function (req, res) { res.render("indexJquery.ejs", {})    });

app.get('/guisock', function (req, res) {
	console.log("webguiServer guisock get " + __dirname );
	res.render("indexSock.ejs", {})
  });
app.post('/guisock', function (req, res) { res.render("indexSock.ejs", {})  });


app.get('/info', function (req, res) { res.send( history ); });

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
/*
app.post("/w", function(req, res,next)  { handlePostMove("moveForward","moving ahead", req,res,next); });
app.post("/s", function(req, res,next)  { handlePostMove("moveBackward","moving back", req,res,next); });
app.post("/r", function(req, res,next)  { handlePostMove("turnRight","moving right",   req,res,next); });
app.post("/l", function(req, res,next)  { handlePostMove("turnLeft","moving left",     req,res,next); });
app.post("/h", function(req, res,next)  { handlePostMove("alarm","stop",               req,res,next); });
*/
app.post("/w", function(req, res,next)  { req.gui="guisimple"; postTo8090(wenvHost, "moveForward",addToHistory ); next(); });
app.post("/s", function(req, res,next)  { req.gui="guisimple"; postTo8090(wenvHost, "moveBackward",addToHistory); next(); });
app.post("/r", function(req, res,next)  { req.gui="guisimple"; postTo8090(wenvHost, "turnRight",addToHistory);    next(); });
app.post("/l", function(req, res,next)  { req.gui="guisimple"; postTo8090(wenvHost, "turnLeft",addToHistory);     next(); });
app.post("/h", function(req, res,next)  { req.gui="guisimple"; postTo8090(wenvHost, "alarm",addToHistory);        next(); });


//HANDLE  utility commands
app.post("/conns", function(req, res,next)         { connectionHistory(); next()  });
app.post("/clearHistory", function(req, res,next)  { clearDisplayArea(); next()   });

//HANDLE POST from HTML GUI to send a POST to Wenv 8090
app.post("/l8090", function(req, res,next)  { req.gui="guiJquery"; postTo8090(wenvHost,'turnLeft',addToHistory);    next()  });
app.post("/r8090", function(req, res,next)  { req.gui="guiJquery"; postTo8090(wenvHost, 'turnRight',addToHistory);   next()  });
app.post("/w8090", function(req, res,next)  { req.gui="guiJquery"; postTo8090(wenvHost, 'moveForward',addToHistory); next()  });
app.post("/s8090", function(req, res,next)  { req.gui="guiJquery"; postTo8090(wenvHost, 'moveBackward',addToHistory);next()  });
app.post("/h8090", function(req, res,next)  { req.gui="guiJquery"; postTo8090(wenvHost, 'alarm',addToHistory);       next()  });

/*
* ====================== REPRESENTATION ================
*/
app.use( function(req,res){
	console.log("webguiServer | SENDING THE ANSWER " + res  + " json:" + req.accepts('json') )
	try{
      // if (req.accepts('json')) { res.send(history);		//give answer to curl / postman } else
	  //return res.render('index' );  //NO: we loose the message sent via socket.io
	  console.log( "req.gui=" + req.gui )
	  if( req.gui=="guisimple" )       res.render("indexSimple.ejs", {})
	  else if( req.gui=="guiJquery" )  res.render("indexJquery.ejs", {})
      else if( req.gui=="guisock" )    res.render("indexSock.ejs", {})
	  //res.sendFile(path.join(__dirname, "index.html"))    //with robotDisplay area set with history
	}catch(e){
	    console.log("webguiServer | SORRY ..." + e);}
	}
);
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



function handlePostMove( cmd, msg, req, res, next ){
    console.log( "webguiServer |  handlePostMove in webguiServer.js "  + cmd )
    forward(cmd, "localhost");  //via TCP: NO MORE
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
https://medium.com/hackernoon/implementing-a-websocket-webguiServer-with-node-js-d9b78ec5ffa8
See https://www.html.it/pag/54040/websocket-webguiServer-con-node-js/
*/
var clients = 0;
const WebSocket   = require('ws');

const wsServer    = new WebSocket.Server({ server: app.listen(3001) });
//wsServer.on('open', socket => { console.log("webguiServer | socket open ") });

wsServer.on('connection', (ws) => {
    console.log("webguiServer | client connected ")
    displayHistory()
  ws.on('message', message => {
    //console.log("webguiServer | socket-on received : "+message)
    //addToHistory( message )
    if( message=="turnRight" || message=="turnLeft" || message=="alarm" || message=="moveForward" || message=="moveBackward" ){
        postTo8090(wenvHost, message, addToHistory);
        //rimbalzo del comando al
        //wsServer.clients.forEach(client => { client.send(  message ); });
    }else if( message.includes("close")) {
        //connectionHistory();
        console.log("webguiServer | socket-on - the client is disconnected ");
    }
  });
});

/*
     ws.on('close', function () {
        clients--;
        alert("client disconnected");
          wsServer.clients.forEach(client => {
              client.send(  "disconnection_connected=" + clients );
          });
                //io.sockets.emit('broadcast',{ description: clients + ' clients connected!'});
     });
*/
//-------------------------------------- WebSockets SECTION END

function clearDisplayArea(){
    history = ""
    displayHistory()  //done by
}

function updateRobotState(message){
    history = history + "<br/>" + message;
    console.log(history);
    wsServer.clients.forEach(client => {
        client.send( history );
    });
}

function displayHistory(){
        wsServer.clients.forEach(client => {
            client.send(   history )
        })
}

function addToHistory( msg ){
        history     = history + "<br/>" + msg
        displayHistory()
}

function connectionHistory(){
        addToHistory( history + "<br/>" + "connections=" + wsServer.clients.size )
        //var clients =
        //history     = history + "<br/>" + "connections=" + clients
        //socket.emit('broadcast',{ description: clients + ' clients connected!'});
        //displayHistory()
}

/*
============================================================================================
ws8091 SECTION
Is it useful to receive state data, in order to update the GUI and define some business logic
============================================================================================
*/

//var conn8091

var client = new WebSocketClient();
client.on('connectFailed', function(error) {
    console.log('WebSocketClient | Connect Error: ' + error.toString());
});

    client.on('connect', function(connection) {
        console.log('WebSocketClient | Connected')
        //conn8091 = connection

        connection.on('error', function(error) {
            console.log("WebSocketClient | Error: " + error.toString());
        });
        connection.on('close', function() {
            console.log('WebSocketClient | Connection Closed');
        });
        connection.on('message', function(message) {
            if (message.type === 'utf8') {
                const msg = message.utf8Data
                console.log("Received: " + msg  )
                const msgJson = JSON.parse( msg )
                if(msgJson.collision) console.log("WebSocketClient | Received: collision=" + msgJson.collision)
                if(msgJson.sonarName) console.log("WebSocketClient | Received: sonar=" + msgJson.sonarName + " distance=" + msgJson.distance)
                addToHistory( JSON.stringify( msgJson ) )
            }
    });
});

const wenvHost = "wenv" //"localhost"
const url      = "ws://"+wenvHost+":8091"
client.connect( url , ''); //'echo-protocol'
//client.connect('ws://wenv:8091', '');
/*
try{
    client.connect('ws://localhost:8091', ''); //'echo-protocol'
}catch(e){
    client.connect('ws://wenv:8091', '');
}
*/
//-------------------------------------- ws8091 SECTION END



app.listen(3000, function () {
  console.log("app listening on port 3000 with __dirname=" + __dirname);
});