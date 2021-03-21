package it.unibo.tests

import it.unibo.httpwsSupport.WebSocketKotlinSupport
import it.unibo.httpwsSupport.WebSocketUtilUsage
import it.unibo.observers.ActorRobotObserver
import it.unibo.observers.ControllerObserverOnChannel
import it.unibo.observers.RobotObserver
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("==============================================")
    println("TestSupportJar | main start n_Threads=" + Thread.activeCount());
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
    println("TestSupportJar | main END n_Threads=" + Thread.activeCount());
    println("==============================================")

}