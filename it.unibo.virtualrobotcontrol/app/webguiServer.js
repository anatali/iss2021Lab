/*
webguiServer.js
*/

let express    = require('express');
let path       = require('path');
let fs         = require('fs');
let bodyParser = require('body-parser');
let app        = express();
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
/*
app.post("/w", function(req, res,next)  { handlePostMove("moveForward","moving ahead", req,res,next); });
app.post("/s", function(req, res,next)  { handlePostMove("moveBackward","moving back", req,res,next); });
app.post("/r", function(req, res,next)  { handlePostMove("turnRight","moving right",   req,res,next); });
app.post("/l", function(req, res,next)  { handlePostMove("turnLeft","moving left",     req,res,next); });
app.post("/h", function(req, res,next)  { handlePostMove("alarm","stop",               req,res,next); });
*/
app.post("/w", function(req, res,next)  { postTo8090("moveForward",addToHistory ); next(); });
app.post("/s", function(req, res,next)  { postTo8090("moveBackward",addToHistory); next(); });
app.post("/r", function(req, res,next)  { postTo8090("turnRight",addToHistory);    next(); });
app.post("/l", function(req, res,next)  { postTo8090("turnLeft",addToHistory);     next(); });
app.post("/h", function(req, res,next)  { postTo8090("alarm",addToHistory);        next(); });


//HANDLE  utility commands
app.post("/conns", function(req, res,next)         { connectionHistory(); next()  });
app.post("/clearHistory", function(req, res,next)  { clearDisplayArea(); next()   });

//HANDLE POST from HTML GUI to send a POST to Wenv 8090
app.post("/l8090", function(req, res,next)  { postTo8090('turnLeft',addToHistory); next()  });
app.post("/r8090", function(req, res,next)  { postTo8090('turnRight',addToHistory); next()  });
app.post("/w8090", function(req, res,next)  { postTo8090('moveForward',addToHistory); next()  });
app.post("/s8090", function(req, res,next)  { postTo8090('moveBackward',addToHistory); next()  });
app.post("/h8090", function(req, res,next)  { postTo8090('alarm',addToHistory); next()  });


/*
* ====================== REPRESENTATION ================
*/
app.use( function(req,res){
	console.log("webguiServer | SENDING THE ANSWER " + res  + " json:" + req.accepts('json') )
	//console.log(req)
	try{
// if (req.accepts('json')) { res.send(history);		//give answer to curl / postman } else
	  //return res.render('index' );  //NO: we loose the message sent via socket.io
	   res.sendFile(path.join(__dirname, "index.html"));    //with robotDisplay area set with history
	}catch(e){
	    console.log("webguiServer | SORRY ..." + e);}
	}
);

function clearDisplayArea(){
    history = ""
}

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

const wsServer    = new WebSocket.Server({ server: app.listen(3001) }); //.listen(3001)
//wsServer.on('open', socket => { console.log("webguiServer | socket open ") });

wsServer.on('connection', (ws) => {
    console.log("webguiServer | client connected ")
    //postTo8090("turnLeft",  addToHistory);
    //postTo8090("turnRight", addToHistory);
    //displayHistory()
    ws.send("welcome")
  ws.on('message', message => {
    console.log("webguiServer | socket-on received : "+message)
    //addToHistory( message )
    if( message=="turnRight" || message=="turnLeft" || message=="alarm" || message=="moveForward" || message=="moveBackward" ){
        postTo8090(message, addToHistory);
        //rimbalzo del comando al
        //wsServer.clients.forEach(client => { client.send(  message ); });
    }else if( message.includes("close")) {

        //connectionHistory();
        console.log("webguiServer | socket-on - the client is disconnected ");
    }
  });
});

/*
     ws.on('disconnection', function () {
        clients--;
        alert("client disconnected");
          wsServer.clients.forEach(client => {
              client.send(  "disconnection_connected=" + clients );
          });
                //io.sockets.emit('broadcast',{ description: clients + ' clients connected!'});
     });
*/
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
//-------------------------------------- WebSockets SECTION END

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