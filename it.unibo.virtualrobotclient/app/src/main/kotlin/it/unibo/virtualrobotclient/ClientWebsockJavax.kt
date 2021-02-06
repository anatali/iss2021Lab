/**
 * ClientWebsockJavax
 * @author AN - DISI - Unibo
===============================================================
walks along the boundary of the room
and then works as an observer
===============================================================
 */
package it.unibo.virtualrobotclient

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.io.IOException
import java.net.URI
import java.net.URISyntaxException
import javax.websocket.*

@ClientEndpoint
class ClientWebsockJavax(addr: String) {
    var userSession: Session? = null
    private var messageHandler: MessageHandler? = null
    private var simpleparser: JSONParser? = null

    interface MessageHandler {
        @Throws(ParseException::class)
        fun handleMessage(message: String)
    }

    protected fun init(addr: String) {
        try {
            simpleparser = JSONParser()
            val container = ContainerProvider.getWebSocketContainer()
            container.connectToServer(this, URI("ws://$addr"))
        } catch (ex: URISyntaxException) {
            System.err.println("ClientWebsockJavax | URISyntaxException exception: " + ex.message)
        } catch (e: DeploymentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    fun onOpen(userSession: Session?) {
        println("ClientWebsockJavax | opening websocket")
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
        println("ClientWebsockJavax | closing websocket")
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
        println("ClientWebsockJavax | sendMessage $message")
        //userSession!!.getAsyncRemote().sendText(message);
        userSession!!.basicRemote.sendText(message) //synch: blocks until the message has been transmitted
    }

    /*
BUSINESS LOGIC
 */
    interface IGoon {
        @Throws(Exception::class)
        fun nextStep(collision: Boolean)
    }

    protected fun setObserver(goon: IGoon) {
        // add listener
        addMessageHandler(object : MessageHandler {
            override fun handleMessage(message: String) {
                try {
                    //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
                    println("ClientWebsockJavax | handleMessage:$message")
                    val jsonObj = simpleparser!!.parse(message) as JSONObject
                    if (jsonObj["endmove"] != null) {
                        val endmove = jsonObj["endmove"].toString() == "true"
                        val move = jsonObj["move"].toString()
                        println("ClientWebsockJavax | handleMessage $move endmove=$endmove")
                        if (endmove) goon.nextStep(false)
                    } else if (jsonObj["collision"] != null) {
                        val collision = jsonObj["collision"].toString() == "true"
                        val move = jsonObj["move"].toString()
                        println("ClientWebsockJavax | handleMessage collision=$collision move=$move")
                        //if( ! move.equals("unknown") )
                        goon.nextStep(collision)
                    } else if (jsonObj["sonarName"] != null) {
                        val sonarNAme = jsonObj["sonarName"].toString()
                        val distance = jsonObj["distance"].toString()
                        println("ClientWebsockJavax | handleMessage sonaraAme=$sonarNAme distance=$distance")
                    }
                } catch (e: Exception) {
                }
            }
        })
    }

    //setObserver
    private var count = 0

    @Throws(Exception::class)
    fun doJob() {
        setObserver(object : IGoon {
            @Throws(Exception::class)
            override fun nextStep(collision: Boolean) {
                //System.out.println(" %%% nextStep collision=" + collision + " count=" + count);
                if (count > 4) {
                    println("ClientWebsockJavax | BYE (from nextStep)")
                    return
                }
                //Thread.sleep(500) ;   //interval before the next move
                //System.in.read();
                if (collision) {
                    if (count++ <= 4) {
                        //count++;
                        sendMessage("{\"robotmove\":\"turnLeft\"}")
                    }
                } else {  //no collision
                    sendMessage("{\"robotmove\":\"moveForward\"}")
                }
            }
        }) //setObserver
        count = 1
        sendMessage("{\"robotmove\":\"moveForward\"}")
        println("ClientWebsockJavax | doJob ENDS ======================================= ")
    }

    companion object {
/*
MAIN
 */
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                ClientWebsockJavax("localhost:8091").doJob()
                // wait  for messages from websocket
                Thread.sleep(30000)
            } catch (ex: Exception) {
                System.err.println("ClientWebsockJavax | InterruptedException exception: " + ex.message)
            }
        }
    }

    init {
        println("ClientWebsockJavax |  CREATING ...")
        init(addr)
    }
}