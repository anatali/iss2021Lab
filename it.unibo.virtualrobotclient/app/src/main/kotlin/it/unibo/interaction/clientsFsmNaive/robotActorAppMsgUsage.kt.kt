package it.unibo.interaction.clientsFsmNaive

import it.unibo.fsm.AppMsg
import it.unibo.fsm.AppMsgType
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

val initMsg         = AppMsg.create("init","main","robotActorAppMsg")
val endMsg          = AppMsg.create("end", "main","robotActorAppMsg")
val moveForwardMsg  = AppMsg.create("move","main","robotActorAppMsg","w", AppMsgType.dispatch)
val moveBackwardMsg = AppMsg.create("move","main","robotActorAppMsg","s")
val haltRobotMsg    = AppMsg.create("move","main","robotActorAppMsg","h")

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun forward(dest: SendChannel<AppMsg>, msg : AppMsg ){
    if( msg.isDispatch() ) dest.send( msg   )
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun sendMsgAppCommands(   ) {

    forward( robotActorAppMsg, initMsg )
//    for (i in 1..2) {
    forward( robotActorAppMsg, moveForwardMsg )
    delay(1000)
    forward( robotActorAppMsg, haltRobotMsg )
    delay(1000)

    forward( robotActorAppMsg, moveBackwardMsg )
    delay(1000)
    forward( robotActorAppMsg, haltRobotMsg )
    delay(500)
//   }
    forward( robotActorAppMsg, endMsg )
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    sendMsgAppCommands(   )
    println("BYE")
}
