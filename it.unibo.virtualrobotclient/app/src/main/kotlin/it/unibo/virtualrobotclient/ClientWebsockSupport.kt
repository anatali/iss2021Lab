/**
 * ClientWebsockSupport
 * @author AN - DISI - Unibo
===============================================================

===============================================================
 */
package it.unibo.virtualrobotclient

import org.glassfish.tyrus.client.ClientManager
import org.json.simple.parser.ParseException
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.websocket.*

@ClientEndpoint
class ClientWebsockSupport() {
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
            println("ClientWebsockSupport | initClientConn $endpointURI")
            val client = ClientManager.createClient()
            client.connectToServer(this, endpointURI)

        } catch (ex: URISyntaxException) {
            println("ClientWebsockSupport | URISyntaxException exception: " + ex.message)
        } catch (e1: DeploymentException) {
            println("ClientWebsockSupport | DeploymentException : " + e1.message)
        } catch (e2: IOException) {
            println("ClientWebsockSupport | IOException : " + e2.message)
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    fun onOpen(userSession: Session?) {
        println("ClientWebsockSupport | opening websocket")
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
        println("ClientWebsockSupport | closing websocket")
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
        println("ClientWebsockSupport | sendMessage $message")
        //userSession!!.getAsyncRemote().sendText(message);
        if( userSession != null)
            userSession!!.basicRemote.sendText(message) //synch: blocks until the message has been transmitted
        else println("ClientWebsockSupport | sorry, no userSession")
    }

}