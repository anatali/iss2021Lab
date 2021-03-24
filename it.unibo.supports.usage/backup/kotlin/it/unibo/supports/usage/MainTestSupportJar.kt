package it.unibo.supports.usage

import it.unibo.interaction.MsgRobotUtil
import it.unibo.supports.*
import kotlinx.coroutines.*

val workToDo : (CoroutineScope, WebSocketKotlinSupport) -> Unit =  fun(scope, support ) {
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

val cpus = Runtime.getRuntime().availableProcessors();

fun curThread() : String {
    return "thread=${Thread.currentThread().name} / nthreads=${Thread.activeCount()}"
}

fun main() {
    lateinit var support : WebSocketKotlinSupport
    runBlocking {
        println("==============================================")
        println("MainTestSupportJar | main START ${curThread()}" );
        println("==============================================")
        support    = WebSocketKotlinSupport(this )
        support.connect( "localhost:8091", workToDo)

        support.registerObserver( ActorlikeController( this, support  ) )
        for( i in 1..3 ){ support.registerObserver( ActorlikeObserver(this, i) ) } //GlobalScope
        //for( i in 1..5 ){ support.registerObserver( NaiveObserver(i) ) }


        println("==============================================")
        println("TestSupportJar | main BEFORE END ${curThread()}" );
        println("==============================================")

        //give time to see messages ... NOT NECESSARY if working in this scope, since runBlocking
        //delay(30000)  //CREATE new threads  !!!
        //support.disconnect()


    }
    println("==============================================")
    println("TestSupportJar | main END ${curThread()}");
    println("==============================================")
    support.disconnect()
}

