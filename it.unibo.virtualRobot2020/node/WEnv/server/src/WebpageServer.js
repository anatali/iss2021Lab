/*
WebpageServer.js
*/

const app      = require('express')()
const express  = require('express')
const http     = require('http').Server(app)
const socketIO = require('socket.io')(http)

const sockets       = {}    //interaction with WebGLScene
const wssockets     = {}    //interaction with clients
let socketIndex     = -1
let wssocketIndex   = -1

var alreadyConnected = false

//let webpageReady = false
const moveTime   = 800
const serverPort = 8090
var target       = "notarget"   //the current virtual object that collides

//global to be set by startHttpServer
//var doRobotMove
//var updateObservers

    app.use(express.static('./../../WebGLScene'))

    app.get('/', (req, res) => {
	     console.log("WebpageServer | GET socketIndex="+socketIndex + " alreadyConnected =" + alreadyConnected )
             if( ! alreadyConnected ){
		alreadyConnected = true;
		res.sendFile('indexOk.html', { root: './../../WebGLScene' })
	     }else{
		res.sendFile('indexNoControl.html', { root: './../../WebGLScene' })
                //See socket.on( 'disconnect' ...
	     }
    }); //app.get

	//USING POST : by AN Jan 2021
    app.post("/api/move", function(req, res,next)  {
	    var data = ""
	    req.on('data', function (chunk) { data += chunk; }); //accumulate data sent by POST
            req.on('end', function () {	//elaborate data received JSon: { robotmove: turnLeft | turnRight | ... }
			console.log('POST /api/move data ' + data  );
     		var moveTodo = JSON.parse(data).robotmove
			doMove(moveTodo, res)
  	   });
	}); //app.post

//Execute a robotmove command and sends info about collision
function doMove(moveTodo, res){
	console.log('$$$ WebpageServer doMove |  moveTodo=' + moveTodo  );
	execMoveOnAllConnectedScenes(moveTodo)
	setTimeout(function() { //wait for the moveTime before sending the answer (collision or not)
        const collision = target != 'notarget'
        //const answer     = '{ "collision" : "'  +  collision +  '", "move": "' + moveTodo + '"}'
        const answer     = "{ 'collision' : '"  +  collision +  "', 'move': '"  + moveTodo + "'}"
        const answerJson = JSON.stringify(answer) 		//answer //
        console.log('WebpageServer | doMove  answer= ' + answerJson  );
        target = "notarget"; 	//reset target
        if( res != null ){  //give info about collision to the POST sender
    		res.writeHead(200, { 'Content-Type': 'text/json' });
    		res.statusCode=200
            res.write( answerJson  ); res.end();
        }
        //IN ANY CASE update all the controls / observers
        updateObservers(answerJson)
    }, moveTime);
}
//Updates the mirrors
function execMoveOnAllConnectedScenes(moveTodo){
    console.log('$$$ WebpageServer doMove |  updates the mirrors'   );
	Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, moveTime) );
}
//Updates the controls and the observers
function updateObservers(msgJson){
    console.log("WebpageServer | updates the controls: " + msgJson   );
	Object.keys(wssockets).forEach( key => wssockets[key].send( msgJson ) )
}

/*
-----------------------------------------------------------------------------
Defines how to handle GET from browser and POST from external controls
-----------------------------------------------------------------------------
*/
function startHttpServer() {
    //doRobotMove     = doMove
    //updateObservers = sendMoveResultToAllConnectedControls

//STARTS THE SERVER
    http.listen(serverPort)

}//startHttpServer


/*
-------------------------------------------------------------------------------------
Interact with clients over ws (controls that send commands or observers)
-------------------------------------------------------------------------------------
*/
function initWs(){
const WebSocket = require('ws');
const wsServer  = new WebSocket.Server( { port: 8091 } );   //{ server: app.listen(8091) }

wsServer.on('connection', (ws) => {
  wssocketIndex++
  console.log("$$$ WebpageServer wssocket | client connected wssocketIndex=" + wssocketIndex)
  const key      = wssocketIndex
  wssockets[key] = ws
  ws.on('message', msg => {
    console.log("$$$ WebpageServer wssocket |  wssocketIndex=" +  wssocketIndex + " received: "  )
	console.log( msg )
	var moveTodo = JSON.parse(msg).robotmove
	doMove(moveTodo, null)  //doRobotMove
  });

  ws.onerror = (error) => {
	  console.log("$$$ WebpageServer wssocket | error: ${error}")
	  delete sockets[key];
	  wssocketIndex--
	  console.log( "$$$ WebpageServer wssocket | disconnect wssocketIndex=" +  wssocketIndex )
  }

  ws.on('disconnect', ()=>{
	  delete sockets[key];
	  wssocketIndex--
	  console.log( "$$$ WebpageServer wssocket | disconnect wssocketIndex=" +  wssocketIndex )
  })
}); //wsServer.on('connection'
}//initWs
/*
-------------------------------------------------------------------------------------
Interact with the MASTER (the mirrors do not send any info)
-------------------------------------------------------------------------------------
*/
function initSocketIOWebGLScene() {
	console.log("WebpageServer WebGLScene |  socketIndex="+socketIndex)
    socketIO.on('connection', socket => {
        socketIndex++
        console.log("WebpageServer WebGLScene  | connection socketIndex="+socketIndex)
        const key    = socketIndex
        sockets[key] = socket
        if( socketIndex == 0) console.log("WebpageServer WebGLScene | MASTER-webpage ready")

		socket.on( 'sonarActivated', (obj) => {
			console.log( "&&& WebpageServer WebGLScene | sonarActivated " );
			console.log(obj) 
			updateObservers( JSON.stringify(obj) )
		})
        socket.on( 'collision',     (obj) => { 
		    console.log( "WebpageServer WebGLScene  | collision detected " + obj + " numOfSockets=" + Object.keys(sockets).length );
		    target = obj;
		    const info     = "{ 'collision' : 'true ', 'move': 'unknown'}"
		    console.log(info) 
		    updateObservers( JSON.stringify(info) )
 		} )
        socket.on( 'disconnect',     () => { 
        		delete sockets[key]; 
        		//webpageReady = false;
          		socketIndex--;
			    alreadyConnected = ( socketIndex == 0 )
        		console.log("WebpageServer WebGLScene  | disconnect socketIndex="+socketIndex)
        	})
    })
}//initSocketIOWebGLScene

function startServer() {
    //startHttpServer()
    initWs()
    initSocketIOWebGLScene()
    http.listen(serverPort)
}
//MAIN
startServer()

