/*
robotActorAppMsg.kt
===============================================================
A component that embeds an actor that works as a naive FSM
The component provides a forward operation to send
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


    fun updateMap( move : String, showMap : Boolean = true ){
        if( move == "obstacle")  mapUtil.setObstacle(  )
        else mapUtil.doMove(move)
        if(showMap) mapUtil.showMap()
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

        suspend fun doCollision(msg: AppMsg) {
            //println("\u0007")  println(7.toChar())        //Ring the Terminal BELL ??
            java.awt.Toolkit.getDefaultToolkit().beep();    //Ring the Terminal BELL
            println("myactor | handles ${msg.CONTENT} by going back a little (if going ahead)");
            hh.sendMessage("l")
            delay(400)
            hh.sendMessage("r")
            delay(400)
            if( currentMove  == "w"){
                hh.sendMessage("s")
                delay(100)
                hh.sendMessage("h")
                currentMove  = "h"
                delay(500)
            }
            updateMap( "obstacle" )
        }

        suspend fun handleWEnvEvent(msg: AppMsg) {
            val v           = msg.CONTENT
            val isCollision = v.contains("collision")
            println("myactor | handleWEnvEvent: ${v} ${isCollision}")
            if (isCollision) doCollision(msg)
        }

        fun doMove(move: String) {  //move w | s | ...
            println("myactor | move=$move")
            currentMove = move
            hh.sendMessage(move)        //move in the application-language
            updateMap( move )
        }
        //Behavior
            while (state == "working") {
                var msg = channel.receive() //AppMsg
                println("myactor | receives: $msg ${msg.MSGID}")
                //val applMsg = AppMsg.create(msg)
                //println("robotActor applMsg.MSGID=${applMsg.MSGID} ")
                when (msg.MSGID) {
                    "init"      -> { doInit() }
                    "end"       -> { doEnd() }
                    "wenvevent" -> handleWEnvEvent(msg)
                    "move"      -> doMove(msg.CONTENT)
                    else        -> println("myactor |  $msg unknown")
                }
            }//while
            println("myactor | ENDS state=$state")
            //this.channel.close()
            hh.stopReceiver()
        //}
    }

    fun forward(msg: AppMsg){
        //println("forward myactor=$myactor msg=$msg"  )
        scope.launch { myactor.send(msg) }
    }


    init{
        hh = WEnvConnSupport(scope, hostAddr, "2500")
        val handleWEnvEvents  = { v: String ->
                forward(AppMsg.create("wenvevent", "handleWEnvEvents", "robotActorAppMsg", v))
        }//handleWEnvEvents
        scope.launch { hh.activateReceiver(handleWEnvEvents) }
    }


}