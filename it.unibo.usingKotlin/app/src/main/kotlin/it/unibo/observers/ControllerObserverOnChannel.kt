package it.unibo.observers

import it.unibo.httpwsSupport.WebSocketKotlinSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class ControllerObserverOnChannel(val scope: CoroutineScope,
                                  val support: WebSocketKotlinSupport ){

    val robotBehaviorLogic = RobotBoundaryWalkLogic( scope, support )
    val socketMsgChannel  = support.getInputChannel();
    
    init{
        println("ControllerObserverOnChannel | STARTS  ")
        robotBehaviorLogic.doBoundaryInit()
        msgLoop()
    }

    fun msgLoop(){
        scope.launch {
            while ( true ) {
                val v = socketMsgChannel.receive()
                val vJson  = JSONObject( v.toString() )
                println("ControllerObserverOnChannel | receives $v ")  //in ${curThread()}
                if( vJson.has("endmove")  ) handleEndMove( vJson )
            }
        }
    }

    fun handleEndMove( endmove : JSONObject){
        val answer = endmove["endmove"] as String
        val move   = endmove["move"] as String
        when( answer ){
            "true" -> robotBehaviorLogic.boundaryStep(move, false)
            "false"-> robotBehaviorLogic.boundaryStep(move, true)
            "halted" -> println("ControllerObserverOnChannel | handleEndMove to do halt" )
            "notallowed" ->  println("ControllerObserverOnChannel | handleEndMove to do notallowed" )
            else -> println("ControllerObserverOnChannel | handleEndMove IMPOSSIBLE answer=$answer" )
        }
    }

}