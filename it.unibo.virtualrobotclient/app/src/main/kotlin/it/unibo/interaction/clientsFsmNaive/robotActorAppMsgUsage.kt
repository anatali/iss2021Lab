/*
robotActorAppMsgUsage.kt
===============================================================

===============================================================
*/
package it.unibo.interaction.clientsFsmNaive

import it.unibo.fsm.AppMsg
import it.unibo.fsm.AppMsg.Companion.buildDispatch
import it.unibo.fsm.AppMsgType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
//better avoid static definition
val initMsg         = AppMsg.create("init","main","robotActorAppMsg")
val endMsg          = AppMsg.create("end", "main","robotActorAppMsg")
val moveForwardMsg  = AppMsg.create("move","main","robotActorAppMsg","w", AppMsgType.dispatch)
val moveBackwardMsg = AppMsg.create("move","main","robotActorAppMsg","s" )
val haltRobotMsg    = AppMsg.create("move","main","robotActorAppMsg","h")
*/

lateinit var sysScope: CoroutineScope

fun forward( msgid: String, msgContent: String, dest: robotActorAppMsg){
    val d = buildDispatch( "main", msgid , msgContent, "robotActorAppMsg" )
    sysScope.launch { dest.getActor().send(d) }
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun sendMsgAppCommands( dest: robotActorAppMsg  ) {

    forward( "init" , "", dest  )

//    for (i in 1..2) {
    forward( "move" , "w", dest  )       //dispatch and not request
    delay(2000)
    forward( "move" , "h", dest  )
    delay(500)

    forward( "move" , "s", dest  )
    delay(2000)
    forward( "move" , "h", dest  )
    delay(500)
    forward( "end" , "", dest  )
    //delay(1500)
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    sysScope = this
    val sysactor = robotActorAppMsg(this,"localhost:8091" )
    sendMsgAppCommands(  sysactor  )
    println("MAIN APPL BYE")
}
