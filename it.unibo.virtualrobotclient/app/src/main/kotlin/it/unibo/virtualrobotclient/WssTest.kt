package it.unibo.virtualrobotclient;

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

fun main() = runBlocking {
    try {
        println("WssTest | START ")
        val sys = WssTest()
        sys.connect()
        sys.receiver(this)
        delay(1000)
        sys.sender(this)
        delay(1000)
        println("WssTest | END ")
    } catch (ex: Exception) {
        println("WssTest | exception: " + ex.message)
    }
}


public class WssTest() {

    suspend fun receiver(scope : CoroutineScope) {
        val receiver = scope.launch {
            while ( true ) {
                val v = socketEventChannel.receive()
                println("RECEIVER | receives $v ")  //in ${curThread()}
                if(v.request ) {
                    if( webSocket8091 != null ) webSocket8091!!.send(v.text!!)
                }else {
                     println("RECEIVER | receives ${v.text} ")
                }
            }
        }
    }//receiver

    suspend fun sender(scope : CoroutineScope  ) {
        val turnLeft  = "{\"robotmove\":\"turnLeft\", \"time\": 400}"
        val turnRight = "{\"robotmove\":\"turnRight\", \"time\": 400}"
        scope.launch {
            socketEventChannel.send( SocketUpdate( turnLeft, true  ) )
            delay(1000)
            socketEventChannel.send( SocketUpdate( turnRight, true  ) )

        }
    }//sender

    fun connect() {
        val client = OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                //.sslSocketFactory() - ? нужно ли его указывать дополнительно
                .build()
        val request = Request.Builder()
                //.url("wss://echo.websocket.org") //see https://www.websocket.org/echo.html
                .url("ws://localhost:8091")
                .build()
        println("WssTest | doJob ${request}"  )
        val wsListener = MyWebSocketListener()
        val webSocket = client.newWebSocket(request, wsListener)  // this provide to make 'Open ws connection'


    }

}
