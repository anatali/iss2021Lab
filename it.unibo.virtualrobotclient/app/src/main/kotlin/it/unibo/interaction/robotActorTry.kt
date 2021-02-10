/*
robotActorTry.kt
===============================================================
===============================================================
*/

package it.unibo.interaction


//import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel

val showEvents  = { v : String ->  println("showWEnvEvents: $v ")  }//showWEnvEvents

//Actor that includes the business logic; the behavior is message-driven
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val robotActorTry  : SendChannel<String>	= CoroutineScope( Dispatchers.Default ).actor {
    var state    = "working"
    val hh       = WEnvConnSupportNoChannel( "localhost:8091", "400" )
    fun doInit() = { println("robotActorTry INIT" ) }
    fun doEnd()  = { state = "end"  }
    fun doSensor(msg : String){ println("robotActorTry should handle: $msg") }

    suspend fun doCollision(msg : String){
        println("robotActorTry handles $msg going back a little");
        //val goback =  "{ 'type': 'moveBackward', 'arg': 100 }"
        hh.sendMessage( "s"  )  // not for plasticBox : the business logic is more complex ...
        delay(500)
    }

    fun doMove(msgSplitted : List<String> ){
        val cmd = msgSplitted[1].replace(")","")
        hh.sendMessage( cmd )
    }

    while( state == "working" ){
        var msg = channel.receive()
        println("robotActorTry receives: $msg ") //
        val msgSplitted = msg.split('(')
        val msgFunctor  = msgSplitted[0]
        //println("robotActorTry msgFunctor $msgFunctor ")
        when( msgFunctor ){
            "init"      -> doInit()
            "end"       -> doEnd()
            "sensor"    -> doSensor(msg)
            "collision" -> doCollision(msg)
            "move"      -> doMove(msgSplitted)
            else        -> println("NO HANDLE for $msg")
        }
    }
    println("robotActorTry ENDS state=$state")
}


