/*
ClientWebsockJavaxUsingCoroutines.kt
===============================================================

See https://www.websocket.org/echo.html
===============================================================
*/

package virtualRobotUsage

import it.unibo.virtualrobotclient.ClientWebsockSupport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.glassfish.tyrus.client.ClientManager
import java.io.BufferedReader
import java.io.IOException
import java.io.PrintWriter
import java.net.URI
import java.net.URISyntaxException
import javax.websocket.ClientEndpoint
import javax.websocket.ContainerProvider
import javax.websocket.DeploymentException
import javax.websocket.Session


//@ServerEndpoint
@ClientEndpoint
object ClientWebsockSupportUsingCoroutines {
    private var hostName = "localhost"
    //private var port = 8091
    private val sep = ";"
    private var outToServer: PrintWriter? = null
    private var inFromServer: BufferedReader? = null

    var userSession: Session? = null


     fun initClientConn(addr: String, hh : ClientWebsockSupport) {
        //scope.launch {
            try {
                 /*
                val container = ContainerProvider.getWebSocketContainer()
                container.connectToServer(this, URI("ws://$addr/"))
                 */
                val endpointURI = URI( "ws://$addr/" )
                println("ClientWebsockSupportUsingCoroutines | initClientConn $endpointURI")
                val client = ClientManager.createClient()
                val v = client.connectToServer(hh, endpointURI)
                println("ClientWebsockSupportUsingCoroutines | after connect v=$v");  //TyrusSession

            } catch (ex: URISyntaxException) {
                println("ClientWebsockSupportUsingCoroutines | URISyntaxException : " + ex.message)
            } catch (e1: DeploymentException) {
                println("ClientWebsockSupportUsingCoroutines | DeploymentException : " + e1.message)
                //e.printStackTrace()
            } catch (e2: IOException) {
                println("ClientWebsockSupportUsingCoroutines | IOException : " + e2.message)
                //e.printStackTrace()
            }
        //}
    }

    fun onOpen(userSession: Session?) {
        println("ClientWebsockSupportUsingCoroutines | opening websocket")
        this.userSession = userSession
    }

     fun sendMessage(message: String, scope : CoroutineScope) {
        //scope.launch {
            println("ClientWebsockSupportUsingCoroutines | sendMessage $message userSession=$userSession")
            //userSession!!.getAsyncRemote().sendText(message);
            if( userSession != null)
                userSession!!.basicRemote.sendText(message) //synch: blocks until the message has been transmitted
            else println("ClientWebsockSupportUsingCoroutines | sorry, no userSession")
        //}
    }

    suspend fun sendSomeCommand( scope : CoroutineScope, hh : ClientWebsockSupport ) {
        var jsonString: String
        //val time = 1300
        for (i in 1..1) {
            //jsonString = "{ 'robotmove': 'moveForward' , 'time' :  600 }"
            jsonString = "{\"robotmove\":\"turnLeft\" , \"time\": 300}"
            //sendMessage(jsonString, scope)
            hh.sendMessage(jsonString)
            delay(1000)

            //jsonString = "{ 'robotmove': 'moveBackward' , 'time' :  600 }"
            jsonString = "{\"robotmove\":\"turnRight\" , \"time\": 300}"
            //sendMessage(jsonString, scope)
            hh.sendMessage(jsonString)
            delay(1000)
        }
    }
}//object ClientWebsockSupportUsingCoroutines

fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    val hh = ClientWebsockSupport( )
    hh.initConn("localhost:8091")       //blocking

    var jsonString = "{\"robotmove\":\"turnLeft\" , \"time\": 300}"
    hh.sendMessage(jsonString)
    delay(1000)
    jsonString = "{\"robotmove\":\"turnRight\" , \"time\": 300}"
    hh.sendMessage(jsonString)
    //ClientWebsockSupportUsingCoroutines.sendSomeCommand( this, hh  )
    println("BYE")
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
