const net = require('net')

const SEPARATOR = ";"

//const client = new Client({ip: readIpFromArguments(), port: readPortNumberFromArguments()})
const client   = new Client({ip: "192.168.1.132", port: 8999})

const ahead = `{ "type": "moveForward",  "arg": 850 }`
const back  = `{ "type": "moveBackward", "arg": 800 }`

//client.send( ahead )	//wait 800
//setTimeout(() => {  client.send( back ); }, 500); 
setTimeout(() => {  client.send( ahead ); }, 800); 
setTimeout(() => {  client.finish( ); },    2500); 
//-------------------------------------------------------------------
function Client({ ip, port }) {
    const self = this
	console.log(`\tClient ` + ip)
    let clientSocket
    const outQueue = []

    connectTo(port, ip)
    
    function connectTo(port, ip) {
        const client = net.Socket()
        clientSocket = client
	console.log(`\tConnecting ... ` + ip)
        client.connect( port, ip , function() { console.log(`\tConnecting...` + ip) } )

        client.on('connect', () => {
            console.log(`\tConnected`)
            flushOutQueue()
        })
 
        client.on('data', message => {	//receiving from 8999
	    //console.log( message)	//message is byteArray	     
            String(message)
                    .split(SEPARATOR)
                    .map( string => string.trim() )
                    .filter( string => string.length != 0  )
                    //.map( JSON.parse )
                    .forEach( msg => console.log( msg ) )
        })
        
        client.on('close', ()  =>  console.log(`\tConnection closed`) )
        client.on('error', (v) =>  console.log(`\tConnection error ` + v) )
    }

    this.send = function(message) {
        if(!clientSocket.connecting){
	    console.log(`\tsend ` + message)
            clientSocket.write(SEPARATOR +message +SEPARATOR)
        }else {
            console.log(`\tSocket not created, message added to queue`)
            outQueue.push(message)
        }
    }

    this.finish = function() {
        if(clientSocket.connecting)
            clientSocket.on('connect', clientSocket.end )
        else
            clientSocket.end()
    }

    function flushOutQueue() {
        while(outQueue.length !== 0) {
            const data = outQueue.shift()
            self.send(data)
        }
    }
}

function readIpFromArguments() {
    const ipAddress = String(process.argv[2])
    if(!ipAddress) {
        console.error("This script expects 2 arguments: server ip address and port number.")
        process.exit()
    }

    return ipAddress
}

function readPortNumberFromArguments() {
    const port = Number(process.argv[3])
    if(!port || port < 0 || port >= 65536) {
        console.error("This script expects a valid port number (>= 0 and < 65536) as argument.")
        process.exit()
    }

    return port
}