let express    = require('express');
let path       = require('path');
let fs         = require('fs');
let bodyParser = require('body-parser');
let app        = express();
//const axios    = require('axios')
const net      = require('net');

const sep      = ";"

app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json());

app.get('/', function (req, res) {
	console.log("app get " + __dirname );
    res.sendFile(path.join(__dirname, "index.html"));
  });

app.get('/profile-picture', function (req, res) {
  let img = fs.readFileSync(path.join(__dirname, "images/profile-1.jpg"));
  res.writeHead(200, {'Content-Type': 'image/jpg' });
  res.end(img, 'binary');
});

function handlePostMove( cmd, msg, req, res, next ){
        //result = "Web server done: " + cmd;
        payload    =  "{ \"type\": \"" + cmd + "\", \"arg\": 800 }";
        //jsonObject = JSON.stringify(payload);
        msg        = sep+payload+sep;

const clients = net.connect(8999,"wenv", () => {
  // 'connect' listener
  console.log('connected to server to send:' + msg);
  clients.write(msg+'\r\n');
});


clients.on('data', (data) => {
  console.log("from wenv server: "+data.toString());
  //clients.end();
});
clients.on('end', () => {
  console.log('disconnected from server');
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

}

app.post("/w", function(req, res,next) { handlePostMove("moveForward","moving ahead",   req,res,next); });
app.post("/s", function(req, res,next) { handlePostMove("moveBackward","moving back",   req,res,next); });
app.post("/r", function(req, res,next) { handlePostMove("turnRight","moving rigth",   req,res,next); });
app.post("/l", function(req, res,next) { handlePostMove("turnLeft","moving left",     req,res,next); });
app.post("/h", function(req, res,next) { handlePostMove("alarm","stop",               req,res,next); });


app.listen(3000, function () {
  console.log("app listening on port 3000 with __dirname=" + __dirname);
});
