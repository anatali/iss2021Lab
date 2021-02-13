/*
robotActorAppMsg.kt
===============================================================
A component that embeds an actor that works as a naive FSM
The component provides a forward operation to send
Uses WEnvConnSupport (time=2500)
===============================================================
*/
package it.unibo.interaction.clientsFsmNaive

import it.unibo.fsm.AppMsg
import it.unibo.interaction.WEnvConnSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mapRoomKotlin.mapUtil
import org.json.JSONObject

class robotActorAppMsg(
        val scope: CoroutineScope,    //scope required for channel
        val hostAddr: String,
        val moveDuration: String = "600") {

    lateinit var hh     : WEnvConnSupport
    var currentMove     = "h"
    //var glued           = false

    fun updateMap( move : String, showMap : Boolean = true ){
        if( move == "obstacle")  mapUtil.setObstacle(  )
        else if( move != "h"){
            mapUtil.doMove(move)
            if(showMap) mapUtil.showMap()
        }
    }

    fun getActor() : SendChannel<AppMsg>{
        return myactor
    }

    val myactor: SendChannel<AppMsg> = CoroutineScope(Dispatchers.Default).actor {
        var state = "working"

        fun doInit()  {
            println("myactor | INIT")
            mapUtil.showMap()
        }

        fun doEnd()  {
            println("myactor | END")
            state = "end"
        }

        suspend fun doCollision(msgContent: String) {
            //println("\u0007")  println(7.toChar())        //Ring the Terminal BELL ??
            //java.awt.Toolkit.getDefaultToolkit().beep();    //Ring the Terminal BELL
            hh.sendMessage("l")
            delay(400)
            hh.sendMessage("r")
            delay(400)
            if( currentMove  == "w"){
                println("myactor | handles ${msgContent} by going back a little ");
                hh.sendMessage("s")
                delay(100)
                hh.sendMessage("h")
                delay(500)
                state = "working"
            }else if( currentMove  == "s"){
                //WARNING: the virtual robot is 'glued' to the obstacle
                state = "glued"
            }
            currentMove  = "h"
            updateMap( "obstacle" )

        }


        fun doMove(move: String) {  //move w | s | ...
            println("myactor | move todo=$move")
            currentMove = move
            hh.sendMessage(move)        //move in the application-language
            updateMap( move )
        }


        suspend fun endOdMove( msgId : String, msgContent : String ){
            when (msgId) {
                "endMoveOk"   ->  state = "working"
                "endMoveFail" ->  doCollision( msgContent )
                 else         -> println("myactor | endOdMove: $msgId unknown")
            }
        }

        //Sate machine loop
        while (state != "end") {    //intially working
                var msg = channel.receive() //AppMsg
                println("myactor | receives: $msg ${msg.MSGID}")
                 when( state ){
                     "working" -> {
                         when( msg.MSGID ){
                             "init"  -> doInit()
                             "end"   -> doEnd()
                             "move"  -> { doMove(msg.CONTENT); state = "endOfMove"}
                         }
                     }
                     //"end" -> doEnd()
                    //"move"        -> doMove(msg.CONTENT)
                     "endOfMove" -> {
                         //endOdMove(msg.MSGID, msg.CONTENT)
                         when (msg.MSGID) {
                             "endMoveOk"   ->  state = "working"
                             "endMoveFail" ->  doCollision( msg.CONTENT )
                             else         -> println("myactor | endOdMove: ${msg.MSGID} unknown")
                         }
                     }
                     "glued" -> {
                         println("SORRY ..."); state = "end"
                     }
                }//when
        }//while
        //println("myactor | ENDS state=$state")
        hh.stopReceiver()

    }

    fun forward(msg: AppMsg){
        //println("selfMsg myactor=$myactor msg=$msg"  )
        scope.launch { myactor.send(msg) }
    }

    /*
   Called when a new  WEnv-event is raised
    */
    fun handleWEnvEvent(msgContent: String) {
        println("myactor | handleWEnvEvent: ${msgContent}  ")
        //{"endmove":false,"move":"alarm"}
        // {"sonarName":"sonar2","distance":19,"axis":"x"} {"collision":true,"move":"unknown"}
        var endMove     = true
        if( msgContent.contains("endmove") ) {
            endMove = JSONObject( msgContent ).getBoolean("endmove")
            if( endMove )  forward(AppMsg.create("endMoveOk", "handleWEnvEvent", "robotActorAppMsg", msgContent))
            else forward(AppMsg.create("endMoveFail", "handleWEnvEvents", "robotActorAppMsg", msgContent))
        }
        //val isCollision = msgContent.contains("collision")
        //  println("myactor | handleWEnvEvent: ${msgContent} isCollision=${isCollision}")
        //if (isCollision && !glued) doCollision(msgContent)
    }

    init{
        hh = WEnvConnSupport(scope, hostAddr, "2500")
        val WEnvEvent_Handler  = { v: String ->
            handleWEnvEvent( v )
                //forward(AppMsg.create("wenvevent", "handleWEnvEvents", "robotActorAppMsg", v))
        }//handleWEnvEvents
        scope.launch { hh.activateReceiver( WEnvEvent_Handler ) }
    }


}