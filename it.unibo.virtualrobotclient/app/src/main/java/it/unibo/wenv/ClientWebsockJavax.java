package it.unibo.wenv;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.*;

/**
 * ClientWebsockJavax
 *
 * @author AN - DISI - Unibo
 */
@ClientEndpoint
public class ClientWebsockJavax {

    Session userSession = null;
    private MessageHandler messageHandler;

    public ClientWebsockJavax(URI endpointURI) {
        try {
            System.out.println("ClientWebsockJavax | opening websocket");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("ClientWebsockJavax | opening websocket");
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("ClientWebsockJavax | closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {

        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {

        this.userSession.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     *
     * @author AN - DISI - Unibo
     */
    public static interface MessageHandler {

        public void handleMessage(String message);
    }


    public static void main(String[] args) {
        try {
            // open websocket
            final ClientWebsockJavax clientEndPoint =
                    new ClientWebsockJavax(new URI("ws://localhost:8091"));

            // add listener
            clientEndPoint.addMessageHandler(new ClientWebsockJavax.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println("ClientWebsockJavax | handleMessage ...");
                    System.out.println(message);
                }
            });

            // send message to websocket
            clientEndPoint.sendMessage("{\"robotmove\":\"turnLeft\"}");

            // wait 5 seconds for messages from websocket
            Thread.sleep(30000);

        } catch (InterruptedException ex) {
            System.err.println("ClientWebsockJavax | InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("ClientWebsockJavax | URISyntaxException exception: " + ex.getMessage());
        }
    }

}