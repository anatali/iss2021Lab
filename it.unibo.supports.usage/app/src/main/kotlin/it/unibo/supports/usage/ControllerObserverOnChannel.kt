package it.unibo.supports.usage


import it.unibo.supports.WebSocketKotlinSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.json.JSONObject


class ControllerObserverOnChannel(val counter : Int,
                                  val scope: CoroutineScope,
                                  val support: WebSocketKotlinSupport){

    //lateinit var socketMsgChannel : Channel<String>
    
    init{
        //socketMsgChannel  = support.getInputChannel();
        //println("ControllerObserverOnChannel $counter | STARTS on $socketMsgChannel")
        //scope.launch{ msgLoop() }
    }
/*
    suspend fun msgLoop(){
        //scope.launch {
            while ( true ) {
                val v = socketMsgChannel.receive()
                val vJson  = JSONObject( v.toString() )
                println("ControllerObserverOnChannel $counter | receives $v ")  //in ${curThread()}
            }
        //}
    }*/

}