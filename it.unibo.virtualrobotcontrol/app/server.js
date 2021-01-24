let express    = require('express');
let path       = require('path');
let fs         = require('fs');
let bodyParser = require('body-parser');
let app        = express();
let utils      = require('./serverutils');

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
/*
wsServer.on('request', function(request) {
    alert(request );
    var connection = request.accept(null, request.origin);

    connection.on('message', function(message) {
        // Metodo eseguito alla ricezione di un messaggio
        console.log(`received from a client: ${message}`);
        if (message.type === 'utf8') {
            // Se il messaggio è una stringa, possiamo leggerlo come segue:
            console.log(`received from a client: ${message.utf8Data}`);
        }
    });
    connection.on('close', function(connection) {
        // Metodo eseguito alla chiusura della connessione
        console.log(`connection closed `);
    });
});
*/
function updateRobotState(message){
    history = history + "<br/"> + message;
    console.log(history);
    wsServer.clients.forEach(client => {
        client.send( history );
    });
}
/*
const server    = new WebSocket.Server({ server: app.listen(8085) });
*/
wsServer.on('connection', socket => {
        var clients = wsServer.clients.size;
        //socket.emit('broadcast',{ description: clients + ' clients connected!'});
        wsServer.clients.forEach(client => {
            client.send(  "connection_connected=" + clients );
        });
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
            wsServer.clients.forEach(client => {
                client.send(  "disconnection_connected=" + clients );
            });
            */
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
  res.send('This is the frontend-Unibo!')
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



/*
* ====================== REPRESENTATION ================
*/
app.use( function(req,res){
	console.info("SENDING THE ANSWER " + res  + " json:" + req.accepts('josn') );
	try{
	    console.log("answer> "+ res  );
	    /*
	   if (req.accepts('json')) {
	       return res.send(result);		//give answer to curl / postman
	   } else {
	       return res.render('index' );
	   };
	   */
	   //return res.render('index' );  //NO: we loose the message sent via socket.io
	   //res.sendFile(path.join(__dirname, "index.html"));
	}catch(e){console.info("SORRY ..." + e);}
	}
);

/*
 * ============ ERROR HANDLING =======
 */



function handlePostMove( cmd, msg, req, res, next ){
    //result = "Web server done: " + cmd;
    console.log( "handlePostMove in server.js "  + cmd )
    utils.forward(cmd, "localhost");
    updateRobotState(  cmd );
    //res.sendFile(path.join(__dirname, "index.html"));  //
    //res.sendStatus(200);
    next();
}




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