/*
WebpageServer.js
*/

const app      = require('express')()
const express  = require('express')
const http     = require('http').Server(app)
const socketIO = require('socket.io')(http)

const sockets       = {}
const wssockets     = {}
let socketCount     = -1
let wssocketCount   = -1

var alreadyConnected = false

let webpageReady = false
const moveTime   = 800
const serverPort = 8090
var target       = "notarget"

/*
Execute a robotmove command and sends the result on the wssockets on 8091
*/
function doMove(moveTodo, res){
	console.log('$$$$$ WebpageServer doMove |  moveTodo=' + moveTodo  );
	//Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, moveTime) );	//execute the command on the scene
	execMoveOnAllConnectedScenes(moveTodo)
			setTimeout(function() { 	//wait for the moveTime before sending the answer (collision or not)
				const collision = target != 'notarget'				
				const answer = '{ "collision" : "'  +  collision +  '", "move": "' + moveTodo + '"}'
				//const answer =  JSON.stringify( "{ \"collision\" : \"" +  collision + "\",  \"move\": \"" + moveTodo + "\"}" )
				const answerJson = JSON.stringify(answer) 		//answer //
				console.log('WebpageServer | doMove  answer= ' + answerJson  );			
				target = "notarget"; 	//reset target
				if( res != null ){
					res.write( answerJson  ); 
					res.end();
				}//else{
					//Object.keys(wssockets).forEach( key => wssockets[key].send( answerJson ) )	
					sendMoveResultToAllConnectedControls(answerJson)					
				//}
			}, moveTime);	 	

}

function execMoveOnAllConnectedScenes(moveTodo){	//connected to 8090
	Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, moveTime) );	 
}

function sendMoveResultToAllConnectedControls(msgJson){	//connected to 8091
    //console.log('WebpageServer | sendMoveResultToAllConnectedControls   '    );		
	Object.keys(wssockets).forEach( key => wssockets[key].send( msgJson ) )	
}

/*
============================================================================================
WebSockets SECTION
============================================================================================
*/
const WebSocket = require('ws');

const wsServer  = new WebSocket.Server({ server: app.listen(8091) });

wsServer.on('connection', (ws) => {
  wssocketCount++
  console.log("$$$$$ WebpageServer socket | client connected wssocketCount=" + wssocketCount)
  const key      = wssocketCount
  wssockets[key] = ws
  ws.on('message', msg => {
    console.log("$$$$$ WebpageServer socket |  wssocketCount=" +  wssocketCount + " received: "  )
	console.log( msg )
	//Object.keys(wssockets).forEach( key => wssockets[key].send( msg ) )		//echo
	var moveTodo = JSON.parse(msg).robotmove
	doMove(moveTodo, null)
  });
  
  ws.onerror = (error) => {
	  console.log("$$$$$ WebpageServer  socket |  error: ${error}")
	  delete sockets[key]; 
	  wssocketCount--
	  console.log( "$$$$$ WebpageServer  socket |  disconnect wssocketCount=" +  wssocketCount )
  }
  
  ws.on('disconnect', ()=>{
	  delete sockets[key]; 
	  wssocketCount--
	  console.log( "$$$$$ WebpageServer  socket |  disconnect wssocketCount=" +  wssocketCount )
  })
  
});

const clientMySock = new WebSocket("ws://localhost:8091")
console.log("WebpageServer | clientMySock="+clientMySock)

//-------------------------------------- WebpageServer socket SECTION END

function WebpageServer(callbacks) {
    startServer(callbacks)
	//console.log(this)	//WebpageServer {}
    this.moveForward   = duration => Object.keys(sockets).forEach( key => sockets[key].emit('moveForward', duration) )
    this.moveBackward  = duration => Object.keys(sockets).forEach( key => sockets[key].emit('moveBackward', duration) )
    this.turnRight     = duration => Object.keys(sockets).forEach( key => sockets[key].emit('turnRight', duration) )
    this.turnLeft      = duration => Object.keys(sockets).forEach( key => sockets[key].emit('turnLeft', duration) )
    this.alarm = () => Object.keys(sockets).forEach( key => sockets[key].emit('alarm') )
//DEC 2019
    this.remove        = name => Object.keys(sockets).forEach( key => sockets[key].emit('remove', name) )
}



function startServer(callbacks) {
    startHttpServer()
    initSocketIOServer(callbacks, clientMySock)
}

function startHttpServer() {
    app.use(express.static('./../../WebGLScene'))
    app.get('/', (req, res) => { 
	     console.log("WebpageServer | GET socketCount="+socketCount + " alreadyConnected =" + alreadyConnected )
             if( ! alreadyConnected ){
		alreadyConnected = true;
		res.sendFile('indexOk.html', { root: './../../WebGLScene' }) 
	     }else{
		res.sendFile('indexNoControl.html', { root: './../../WebGLScene' }) 
                //See socket.on( 'disconnect' ...
	     }
    })

	//USING POST (with axios) : by AN Jan 2021

    	app.post("/api/move", function(req, res,next)  {  
	    var data = ""	    
	    req.on('data', function (chunk) { data += chunk; }); //accumulate data sent by POST
            req.on('end', function () {	//elaborate data received JSon: { robotmove: turnLeft | turnRight | ... }
			console.log('POST /api/move data ' + data  );
     		var moveTodo = JSON.parse(data).robotmove
    		//console.log('POST moveTodo  ' + moveTodo  );
	   	   // Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, moveTime) );	//execute the command on the scene
		    //Configure the answer
    		res.writeHead(200, { 'Content-Type': 'text/json' });
    		res.statusCode=200
			doMove(moveTodo, res)
			 
    		//res.write(JSON.stringify(data));	
			//WE must wait, since we could have a collision			
			/*
			setTimeout(function() { 
				const collision = target != 'notarget'				
				const answer = '{ "collision" : "'  +  collision +  '", "move": "' + moveTodo + '"}'
				//const answer =  JSON.stringify( "{ \"collision\" : \"" +  collision + "\",  \"move\": \"" + moveTodo + "\"}" )
				console.log('WebpageServer | /api/move  answer= ' + JSON.stringify(answer)  );
				res.write(  JSON.stringify(answer)  ); 
				target = "notarget"; 	//reset target
				res.end();
			}, moveTime);	 	
    		*/
  	   });
	});


 




//STARTING
    http.listen(serverPort)
}
 
/*
Used by the scene
*/
function initSocketIOServer(callbacks, clientMySock) {
	console.log("WebpageServer | initSocketIOServer socketCount="+socketCount)
    socketIO.on('connection', socket => {
		//Sockets : interact with 8999 (MASTER and mirror)
        socketCount++
        console.log("WebpageServer connection | socketCount="+socketCount)
        const key    = socketCount
        sockets[key] = socket
        
        callbacks.onWebpageReady()
        webpageReady = true
        if( socketCount == 0) console.log("WebpageServer connection | MASTER webpage ready")

        //socket.on( 'sonarActivated', callbacks.onSonarActivated )  //see main.js
		socket.on( 'sonarActivated', (obj) => {
			console.log( "&&& WebpageServer | sonarActivated " ); 
			console.log(obj) 
			//propagate to the connected 
			//clientMySock.send(  JSON.stringify(obj) )
			sendMoveResultToAllConnectedControls( JSON.stringify(obj) )
			
		})
         socket.on( 'collision',     (obj) => { 
		    console.log( "WebpageServer connection | collision detected " + obj + " numOfSockets=" + Object.keys(sockets).length ); 
		    target = obj;
			callbacks.onCollision(obj)
			
			} )
        socket.on( 'disconnect',     () => { 
        		delete sockets[key]; 
        		webpageReady = false; 
          		socketCount--;
			    alreadyConnected = ( socketCount == 0 )
        		console.log("WebpageServer disconnect | socketCount="+socketCount)
        	})
    })
    

}

function isWebpageRead() {
    return webpageReady;
}

module.exports = { WebpageServer, isWebpageRead }