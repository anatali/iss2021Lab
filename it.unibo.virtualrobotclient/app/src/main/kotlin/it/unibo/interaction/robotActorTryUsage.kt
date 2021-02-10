/*
robotActorTryUSage.kt
===============================================================
Provides
See /it.unibo.kotlinIntro/userDocs/FirstActorRobot.html
===============================================================
*/
package it.unibo.interaction

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun sendCommands(   ) {
    //virtualRobotSupport.setRobotTarget( robotActorTry, appMsg = false ) //Configure - Inject
    robotActorTry.send("init")
    var jsonString  : String
    val time = 2000L	//time = 1000 => collision
//    for (i in 1..2) {
    jsonString = "{ 'type': 'moveForward', 'arg': $time }"
    robotActorTry.send("move($jsonString)")
    delay(time)

    jsonString = "{ 'type': 'moveBackward', 'arg': ${time} }"
    robotActorTry.send("move($jsonString)")
    delay(1000)
//    }
    robotActorTry.send("end")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    sendCommands(   )
    println("BYE")
}
