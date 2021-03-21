package it.unibo.httpwsSupport

import it.unibo.observers.ActorRobotObserver
import it.unibo.observers.ControllerObserverOnChannel
import it.unibo.observers.RobotObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object WebSocketUtilUsage {
    //ENTRY after connection
    val workToDo : (CoroutineScope, WebSocketKotlinSupport) -> Unit =  fun(scope, support ){
        println("WebSocketUtilUsage | workToDo ... ")
        /*
        scope.launch {
            support.sendWs( MsgRobotUtil.turnLeftMsg  )
            delay(300)
            support.sendWs( MsgRobotUtil.turnRightMsg  )
            delay(300)
            support.sendWs( MsgRobotUtil.forwardMsg  )
            delay(400)
            support.sendWs( MsgRobotUtil.backwardMsg  )
            delay(400)
            var answer = support.sendHttp(MsgRobotUtil.forwardMsg ,  "localhost:8090/api/move")
            //delay(1000)
            answer = support.sendHttp(MsgRobotUtil.backwardMsg , "localhost:8090/api/move")
        }
         */
    }

}

fun main() = runBlocking {
    println("==============================================")
    println("WebSocketUtilUsage | main start n_Threads=" + Thread.activeCount());
    println("==============================================")
    val support    = WebSocketKotlinSupport(this )
    val ws         = support.connect( "localhost:8091", WebSocketUtilUsage.workToDo)

    val obs1       = RobotObserver()
    val obs2       = ActorRobotObserver()
    support.registerObserver(obs1)
    support.registerObserver(obs2)
    val controller = ControllerObserverOnChannel( this, support  )

    //val ws         = support.connect( "localhost:8091", WebSocketUtilUsage.workToDo)
    //val ws8091    = WebSocket8091ObserverUtil.connectTows(observer,"localhost:8091")



    //give time to see messages ...
    delay(35000)  //CREATE new threads  !!!
    support.disconnect(ws)

    println("==============================================")
    println("WebSocketUtilUsage | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}
