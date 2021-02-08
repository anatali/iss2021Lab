package it.unibo.virtualrobotclient

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.*

import okio.ByteString
import java.util.concurrent.TimeUnit

val socketEventChannel: Channel<SocketUpdate> = Channel(10) //our channel buffer is 10 events
var webSocket8091: WebSocket? = null
//@ExperimentalCoroutinesApi
class MyWebSocketListener : WebSocketListener() {
    private val NORMAL_CLOSURE_STATUS = 1000

    override fun onOpen(webSocket: WebSocket, response: Response) {
        println("MyWebSocketListener | onOpen ${webSocket}"  )
        webSocket8091 = webSocket
        //webSocket.send("{\"robotmove\":\"turnLeft\", \"time\": 400}")
        //Thread.sleep(1000)
        //webSocket.send("{\"robotmove\":\"turnRight\", \"time\": 400}")
     }

    override fun onMessage(webSocket: WebSocket, text: String) {
        GlobalScope.launch {
            //println("MyWebSocketListener | onMessage ${text}"  )
            socketEventChannel.send(SocketUpdate(text))
        }
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        GlobalScope.launch {
            socketEventChannel.send(SocketUpdate(exception = SocketAbortedException()))
        }
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
        socketEventChannel.close()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        GlobalScope.launch {
            socketEventChannel.send(SocketUpdate(exception = t))
        }
    }

}

class SocketAbortedException : Exception()

data class SocketUpdate(
        val text: String?           = null,
        val request: Boolean        = false,
        val exception: Throwable?   = null
)

