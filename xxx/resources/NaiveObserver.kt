package resources

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

class NaiveObserver(name: String, scope: CoroutineScope)
    : ActorBasicKotlin(name, scope) {

    override suspend fun handleInput(info: ApplMessage) {
        println("$name  | msgJson=$info   ")
        /*
        var msgJsonStr = info.msgContent
        val msgJson = JSONObject(msgJsonStr)
        println("$name  | msgJson=$msgJson   " )*/
    }
}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
        val obs = NaiveObserver("obs", this)
        obs.send( ApplMsgs.startMsgStr )
        println("ENDS runBlocking ${sysUtil.curThread()}")
        obs.terminate()
    }
    println("ENDS main ${sysUtil.curThread()}")
}