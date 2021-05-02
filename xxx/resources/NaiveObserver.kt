package resources

import it.unibo.actor0.ActorBasicKotlin
import it.unibo.actor0.ApplMessage
import it.unibo.actor0.sysUtil
import it.unibo.robotService.ApplMsgs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.web.client.RestTemplate
import kotlinx.coroutines.delay

class NaiveObserver(name: String, scope: CoroutineScope): ActorBasicKotlin(name, scope) {
	
	lateinit var restTemplate: RestTemplate
	
	fun doPostPath(pathTodo: String) : String{
        var answer = ""
        restTemplate = RestTemplate()
 		println(restTemplate)
       val uri    = "http://localhost:8081/dopath?path=$pathTodo"
        val result = restTemplate.postForObject(uri, answer, String::class.java )
        println(result)
        return result
    }
	
    override suspend fun handleInput(info: ApplMessage) {
        println("$name  | msgJson=$info   ")
		val answer = doPostPath("lr")
		println(answer)
		//delay(1000)
		 /*
        var msgJsonStr = info.msgContent
        val msgJson = JSONObject(msgJsonStr)
        println("$name  | msgJson=$msgJson   " )*/
    }
}

fun main( ) {
    println("BEGINS CPU=${sysUtil.cpus} ${sysUtil.curThread()}")
    runBlocking {/*
        val obs = NaiveObserver("obs", this)
		println( ApplMsgs.stepRobot_l("main") )
        obs.send( ApplMsgs.stepRobot_l("main").toString() )*/
		delay(1000)
        println("ENDS runBlocking ${sysUtil.curThread()}")
        //obs.terminate()
    }
    println("ENDS main ${sysUtil.curThread()}")
}