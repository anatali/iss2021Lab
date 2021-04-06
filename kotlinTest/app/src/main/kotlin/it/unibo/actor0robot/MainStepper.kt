package it.unibo.actor0robot

import it.unibo.actor0.*
import kotlinx.coroutines.*

    fun demoStepper(scope: CoroutineScope){
        val stepperName = "stepper"
        val startmsg = MsgUtil.buildDispatch("main", "start", "ok", stepperName )
        val stepper  = Stepper(stepperName, scope)
        stepper.send(startmsg)
    }



    @ExperimentalCoroutinesApi
    fun main() {
        //sysUtil.trace = true
        val startTime = sysUtil.aboutSystem("applmain")
        println("==============================================")
        println("MainStepper | START ${sysUtil.aboutSystem("MainExecutors")}");
        println("==============================================")

        runBlocking {
            demoStepper( this )

        }

        val endTime = sysUtil.getDuration(startTime)
        println("==============================================")
        println("MainStepper | END TIME=$endTime ${sysUtil.aboutThreads("MainExecutors") }"  );
        println("==============================================")

    }

