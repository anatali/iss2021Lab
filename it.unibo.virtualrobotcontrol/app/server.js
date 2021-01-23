let express    = require('express');
let path       = require('path');
let fs         = require('fs');
let bodyParser = require('body-parser');
let app        = express();
let utils      = require('./serverutils');

/*
WebSockets
*/
const WebSocket = require('ws');

const server    = new WebSocket.Server({ server: app.listen(8085) });
server.on('connection', socket => {
  //socket.send('Robot state INIT ...');
  socket.on('message', message => {
    console.log(`received from a client: ${message}`);
    if( message=="r" || message=="l" || message=="h" || message=="w" || message=="s" ){
        //utils.forward( message  );   //DISCONNECT
        //rimbalzo del comando al
        server.clients.forEach(client => {
            client.send(  message );
        });
    }
  });
});


//-------------------------------------- WebSockets

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
//app.use(express.static(path.join(__dirname, './node_modules/net')));
app.use(express.static(path.join(__dirname, './jscode')));


function updateRobotState(message){
    server.clients.forEach(client => {
        client.send( message );
    });
}
function handlePostMove( cmd, msg, req, res, next ){
    //result = "Web server done: " + cmd;
    console.log( "handlePostMove in server.js "  + cmd )
    utils.forward(cmd, "localhost");
    updateRobotState(  cmd );
    //res.sendFile(path.join(__dirname, "index.html"));  //
    //res.sendStatus(200);

}

app.post("/w", function(req, res,next)  { handlePostMove("moveForward","moving ahead", req,res,next); });
app.post("/s", function(req, res,next)  { handlePostMove("moveBackward","moving back", req,res,next); });
app.post("/r", function(req, res,next)  { handlePostMove("turnRight","moving right",   req,res,next); });
app.post("/l", function(req, res,next)  { handlePostMove("turnLeft","moving left",     req,res,next); });
app.post("/h", function(req, res,next)  { handlePostMove("alarm","stop",               req,res,next); });


app.listen(3000, function () {
  console.log("app listening on port 3000 with __dirname=" + __dirname);
});

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
