package it.unibo.interaction;

import javax.websocket.*;
import java.net.URI;
import java.security.Principal;


@ClientEndpoint     //javax.websocket annotation
public class IssWsSupport implements IssOperations{
    private  String URL            = "unknown";
    private Session userSession    = null;

    public IssWsSupport( String url){
        URL = url;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+url));
        }catch (Exception e) {
            System.err.println("IssWsSupport | ERROR: " + e.getMessage());
        }
    }

    //Callback hook for Connection open events.
    //This method level annotation can be used to decorate a Java method that wishes
    //to be called when a new web socket session is open.
    @OnOpen
    public void onOpen(Session userSession) { //, @PathParam("username") String username, EndpointConfig epConfig
        //ClientEndpointConfig clientConfig = (ClientEndpointConfig) epConfig;
        Principal userPrincipal = userSession.getUserPrincipal();
        if( userPrincipal != null )  { //there is an authenticated user
            System.out.println("ClientWebsockJavax | onOpen user=" + userPrincipal.getName());
        }
        this.userSession = userSession;
    }

    @Override
    public void forward(String msg) throws Exception{
        System.out.println("        ClientWebsockJavax | sendMessage " + msg);
        //this.userSession.getAsyncRemote().sendText(message);
        this.userSession.getBasicRemote().sendText(msg);    //synch: blocks until the message has been transmitted
    }

    @Override
    public String request(String msg) {
        return null;
    }

    @Override
    public void doRequest(String msg) {

    }

    @Override
    public void doReply(String msg) {

    }

    //===================================================================

    protected String performrequest( String msg ) {
        return "TODO";
    }
}
