/*
ClientWebsockJavaxUsingCoroutines.js
===============================================================

See https://www.websocket.org/echo.html
===============================================================
*/

package virtualRobotUsage
//ClientWebsockJavaxUsingCoroutines.kt in it.unibo.kotlinIntro\src\virtualRobotUsage\ClientWebsockJavaxUsingCoroutines.kt
 
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import org.json.JSONObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.json.simple.parser.JSONParser
import java.net.URI
import java.net.URISyntaxException
import javax.websocket.*


object clientWenvTcpObj {
    private var hostName = "localhost"
    private var port = 8999
    private val sep = ";"
    private var outToServer: PrintWriter? = null
    private var inFromServer: BufferedReader? = null

    var userSession: Session? = null

    suspend fun initClientConn(addr: String) {
        try {
            //simpleparser = JSONParser()
            println("initClientConn $addr")
            val container = ContainerProvider.getWebSocketContainer()
            container.connectToServer(this, URI("ws://$addr"))
        } catch (ex: URISyntaxException) {
            System.err.println("VirtualrobotUsageSupport | URISyntaxException exception: " + ex.message)
        } catch (e: DeploymentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun onOpen(userSession: Session?) {
        println("VirtualrobotUsageSupport | opening websocket")
        this.userSession = userSession
    }

    /*
//Send a message wriiten in JSON on the TCP connection
	fun sendMsg(jsonString: String) {
		val jsonObject = JSONObject(jsonString)
		val msg = "$sep${jsonObject.toString()}$sep"
		outToServer?.println(msg)
	}
*/


/*
//Launch a coroutine that waits for data from the TCP connection
        private fun startTheReader(  ) {
	    val scope : CoroutineScope = CoroutineScope( Dispatchers.Default )
        scope.launch {
                while (true) {
                    try {
                         val inpuStr = inFromServer?.readLine()
                        val jsonMsgStr =
                            inpuStr!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        //println("clientWenvTcp | inpuStr= $jsonMsgStr")
                        val jsonObject = JSONObject(jsonMsgStr)
                        //println( "type: " + jsonObject.getString("type"));
                        when (jsonObject.getString("type")) {
                            "webpage-ready" -> println("webpage-ready ")
                            "sonar-activated" -> {
                                //println("sonar-activated ")
                                val jsonArg = jsonObject.getJSONObject("arg")
                                val sonarName = jsonArg.getString("sonarName")
                                val distance = jsonArg.getInt("distance")
                                println("clientWenvTcp | sonarName=$sonarName distance=$distance")

                            }
                            "collision" -> {
                                //println( "collision"   );
                                val jsonArg = jsonObject.getJSONObject("arg")
                                val objectName = jsonArg.getString("objectName")
                                println("clientWenvTcp | collision objectName=$objectName")
                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
         }//startTheReader
}//clientWenvTcpObj
*/
    suspend fun sendMessage(message: String) {
        println("VirtualrobotUsageSupport | sendMessage $message")
        //userSession!!.getAsyncRemote().sendText(message);
        userSession!!.basicRemote.sendText(message) //synch: blocks until the message has been transmitted
    }

    suspend fun sendSomeCommand() {
        var jsonString: String
        //val time = 1300
        for (i in 1..1) {
            jsonString = "{ 'robotmove': 'moveForward' , 'time' :  600 }"
            sendMessage(jsonString)
            delay(1000)

            jsonString = "{ 'robotmove': 'moveBackward' , 'time' :  600 }"
            sendMessage(jsonString)
            delay(1000)
        }
    }
}//object clientWenvTcpObj

fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    clientWenvTcpObj.initClientConn("localhost:8091")
    clientWenvTcpObj.sendSomeCommand(   )
    println("BYE")
}
