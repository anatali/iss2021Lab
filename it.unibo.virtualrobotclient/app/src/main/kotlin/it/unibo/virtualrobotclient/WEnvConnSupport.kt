/**
 * WEnvConnSupport
 * @author AN - DISI - Unibo
===============================================================

===============================================================
 */
package it.unibo.virtualrobotclient

import org.glassfish.tyrus.client.ClientManager
import org.json.JSONObject
import org.json.simple.parser.ParseException
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.websocket.*

@ClientEndpoint
public class WEnvConnSupport() {
    var userSession: Session?                   = null
    private var messageHandler: MessageHandler? = null

    interface MessageHandler {
        @Throws(ParseException::class)
        fun handleMessage(message: String)
    }

    public fun initConn(addr: String) {
        try {
            //val container = ContainerProvider.getWebSocketContainer()
            //container.connectToServer(this, URI("ws://$addr"))

            val endpointURI = URI( "ws://$addr/" )
            println("WEnvConnSupport | initClientConn $endpointURI")
            val client = ClientManager.createClient()
            client.connectToServer(this, endpointURI)

        } catch (ex: URISyntaxException) {
            println("WEnvConnSupport | URISyntaxException exception: " + ex.message)
        } catch (e1: DeploymentException) {
            println("WEnvConnSupport | DeploymentException : " + e1.message)
        } catch (e2: IOException) {
            println("WEnvConnSupport | IOException : " + e2.message)
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    fun onOpen(userSession: Session?) {
        println("WEnvConnSupport | opening websocket")
             this.userSession = userSession
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    fun onClose(userSession: Session?, reason: CloseReason?) {
        println("WEnvConnSupport | closing websocket")
        this.userSession = null
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    @Throws(ParseException::class)
    fun onMessage(message: String) {
        println("WEnvConnSupport | websocket receives: $message ")
        if (messageHandler != null) {
            messageHandler!!.handleMessage(message)
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    fun addMessageHandler(msgHandler: MessageHandler?) {
        messageHandler = msgHandler
    }

    /**
     * Send a message.
     *
     * @param message
     */
    @Throws(Exception::class)
    fun sendMessage(message: String) {
        println("WEnvConnSupport | sendMessage $message")
        //userSession!!.getAsyncRemote().sendText(message);
        if( userSession != null)
            userSession!!.basicRemote.sendText(message) //synch: blocks until the message has been transmitted
        else println("WEnvConnSupport | sorry, no userSession")
    }


    fun translate(cmd: String) : String{ //cmd is written in application-language
        var jsonMsg = "{\"robotmove\":\"MOVE\" , \"time\": DURATION}"  //"{ 'type': 'alarm', 'arg': -1 }"
        when( cmd ){
            "msg(w)", "w" -> jsonMsg = jsonMsg.replace("MOVE","moveForward").replace("DURATION", "600")
            "msg(s)", "s" -> jsonMsg = jsonMsg.replace("MOVE","moveBackward").replace("DURATION", "600")
                    //"{ 'type': 'moveBackward', 'arg': -1 }"
            "msg(a)", "a", "l" -> jsonMsg = jsonMsg.replace("MOVE","turnLeft").replace("DURATION", "300")
                    //"{ 'type': 'turnLeft',  'arg': -1  }"
            "msg(d)", "d", "r" -> jsonMsg = jsonMsg.replace("MOVE","turnRight").replace("DURATION", "300")
            //"{ 'type': 'turnRight', 'arg': -1  }"
            //"msg(l)", "l" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': 300 }"
            //"msg(r)", "r" -> jsonMsg = "{ 'type': 'turnRight', 'arg': 300 }"
            //"msg(z)", "z" -> jsonMsg = "{ 'type': 'turnLeft',  'arg': -1  }"
            //"msg(x)", "x" -> jsonMsg = "{ 'type': 'turnRight', 'arg': -1  }"
            "msg(h)", "h" -> jsonMsg = jsonMsg.replace("MOVE","alarm").replace("DURATION", "100")
            //"{ 'type': 'alarm',     'arg': 100 }"
            else -> println("WEnvConnSupport command $cmd unknown")
        }
        val jsonObject = JSONObject( jsonMsg )
        val msg        =  jsonObject.toString()
        println("WEnvConnSupport translate output= $msg ")
        return msg
    }   

}