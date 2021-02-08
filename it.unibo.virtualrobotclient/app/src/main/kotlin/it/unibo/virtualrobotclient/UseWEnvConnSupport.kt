/*
UseWEnvConnSupport.kt
===============================================================

See https://www.websocket.org/echo.html
===============================================================
*/

package it.unibo.virtualrobotclient


import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


object UseWEnvConnSupport {
}//object UseWEnvConnSupport

fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
    val hh = WEnvConnSupport( )
    hh.initConn("localhost:8091")       //blocking

    //var jsonString = "{\"robotmove\":\"turnLeft\" , \"time\": 300}"
    hh.sendMessage( hh.translate("l") )
    delay(1000)
    //jsonString = "{\"robotmove\":\"turnRight\" , \"time\": 300}"
    hh.sendMessage( hh.translate("r") )
    //UseWEnvConnSupport.sendSomeCommand( this, hh  )
    delay(30000)    //to show data sent by WEnv
    println("BYE")
}


/*
//Send a message written in JSON on the TCP connection
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
