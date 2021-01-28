/*
WebpageServer.js
*/

const app      = require('express')()
const express  = require('express')
const http     = require('http').Server(app)
const socketIO = require('socket.io')(http)

const sockets   = {}
let socketCount = -1

var alreadyConnected = false

let webpageReady = false

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
    initSocketIOServer(callbacks)
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

	//USING POST: by AN Jan 2021

    	app.post("/api/move", function(req, res,next)  {  
	    var data = ""	    
	    req.on('data', function (chunk) { data += chunk; }); //accumulate data sent by POST
            req.on('end', function () {	//elaborate data received JSon: { robotmove: turnLeft | turnRight | ... }
     		var moveTodo = JSON.parse(data).robotmove
    		console.log('POST moveTodo  ' + moveTodo  );
	   	Object.keys(sockets).forEach( key => sockets[key].emit(moveTodo, 800) );	//execute the command on the scene
		//Configure the answer
    		res.writeHead(200, {
      			'Content-Type': 'text/json'
    		});
    		res.statusCode=200;
    		res.write(JSON.stringify(data));
    		res.end();
  	   });
	});


    http.listen(8090)
}

function initSocketIOServer(callbacks) {
	console.log("WebpageServer | initSocketIOServer socketCount="+socketCount)
    socketIO.on('connection', socket => {
        socketCount++
        console.log("WebpageServer connection | socketCount="+socketCount)
        const key    = socketCount
        sockets[key] = socket
        
        callbacks.onWebpageReady()
        webpageReady = true
        if( socketCount == 0) console.log("WebpageServer | MASTER webpage ready")

        socket.on( 'sonarActivated', callbacks.onSonarActivated )
        socket.on( 'collision',      callbacks.onCollision )
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