/*
wsBoundaryReactive.js
https://www.npmjs.com/package/ws
===============================================================
    walks along the boundary of the room
    and then works as an observer
===============================================================
*/
const WebSocket             = require('ws')
const { walkAlongBoundary } = require('./tripBoundaryBusinessLogic')

const url        = 'ws://localhost:8091'    
var tripdone     = false
var stepCount    = 0

const connection = new WebSocket(url)

    function initMoving(connection){
         //stepCount = 0;
         isObserver   = false
         doMove( "moveForward", connection )
    }

    function doMove(move, connection) {
        const moveJson = '{"robotmove":"'+ move +'"}'
        console.log("doMove moveJson:" + moveJson);
        if (connection) { connection.send(moveJson) }
    }

/*
----------------------------------------------------------
REACTIVE PART
----------------------------------------------------------
*/
connection.onopen = () => {
  console.log("wsBoundaryReactive | opened")       
  initMoving( connection )
}
connection.onmessage = (msg) => {
    if( tripdone ) console.log("wsBoundaryReactive as OBSERVER | " + msg.data   )
    else   tripdone = walkAlongBoundary(msg.data, connection, doMove)
 }

connection.onerror = (error) => {
  console.log(`WebSocket error: ${error}`)
  console.log( error.message )
}





