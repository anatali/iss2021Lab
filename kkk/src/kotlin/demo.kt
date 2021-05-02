package kotlin

import kotlinx.coroutines.runBlocking
import it.unibo.actor0.sysUtil

class demo {
}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {
		/*
        val obs = NaiveObserver("obs", this)
		println( ApplMsgs.stepRobot_l("main") )
        obs.send( ApplMsgs.stepRobot_l("main").toString() )*/
		//delay(1000)
        println("ENDS runBlocking ${sysUtil.curThread()}")
        //obs.terminate()
    }
    println("ENDS main ${sysUtil.curThread()}")
}

/*
Errors occurred during the build. java.lang.ArrayIndexOutOfBoundsException
 		*/