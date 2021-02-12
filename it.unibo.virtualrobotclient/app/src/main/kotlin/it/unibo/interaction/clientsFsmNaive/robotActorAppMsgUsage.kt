/*
robotActorAppMsgUsage.kt
===============================================================

===============================================================
*/
package it.unibo.interaction.clientsFsmNaive

import it.unibo.fsm.AppMsg
import it.unibo.fsm.AppMsgType
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

val initMsg         = AppMsg.create("init","main","robotActorAppMsg")
val endMsg          = AppMsg.create("end", "main","robotActorAppMsg")
val moveForwardMsg  = AppMsg.create("move","main","robotActorAppMsg","w", AppMsgType.dispatch)
val moveBackwardMsg = AppMsg.create("move","main","robotActorAppMsg","s")
val haltRobotMsg    = AppMsg.create("move","main","robotActorAppMsg","h")

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun sendMsgAppCommands( dest: robotActorAppMsg  ) {

    dest.forward(initMsg )

//    for (i in 1..2) {
    dest.forward(moveForwardMsg )
    delay(2000)
    dest.forward(haltRobotMsg )
    delay(1000)

    dest.forward(moveBackwardMsg )
    delay(1000)
    dest.forward(haltRobotMsg )
    delay(500)
    dest.forward(endMsg )
    //delay(1500)
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    val sysactor = robotActorAppMsg(this,"localhost:8091" )
    sendMsgAppCommands(  sysactor  )
    println("BYE")
}
