package it.unibo.interaction;

import org.json.JSONObject;
import javax.websocket.*;
import java.net.URI;
import java.security.Principal;


class AnswerAvailable{
    private String  answer  = null;
    private boolean engaged = false;
    public void engage(){
        engaged = true;
    }
    public synchronized void put(String info) {
        if( engaged ){
            answer = info;
            notify();
        }else{
            System.out.println("        AnswerAvailable | put not engaged: " + answer );
        }
    }
    public synchronized String get( ) {
        while (answer == null){
            try { wait(); }
            catch (InterruptedException e) { }
            finally { }
        }
        String myAnswer = answer;
        answer           = null;
        engaged          = false;
        return myAnswer;
    }
}

@ClientEndpoint     //javax.websocket annotation
public class IssWsSupport implements IssOperations{
    private  String URL            = "unknown";
    private Session userSession    = null;
    private AnswerAvailable answerSupport;

    public IssWsSupport( String url){
        URL = url;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://"+url));
            answerSupport = new AnswerAvailable();
        }catch (Exception e) {
            System.out.println("IssWsSupport | ERROR: " + e.getMessage());
        }
    }

    //Callback hook for Connection open events.
    @OnOpen
    public void onOpen(Session userSession) { //, @PathParam("username") String username, EndpointConfig epConfig
        //ClientEndpointConfig clientConfig = (ClientEndpointConfig) epConfig;
        Principal userPrincipal = userSession.getUserPrincipal();
        System.out.println("        IssWsSupport | onOpen userPrincipal=" + userPrincipal );
        if( userPrincipal != null )  { //there is an authenticated user
            System.out.println("        IssWsSupport | onOpen user=" + userPrincipal.getName());
        }
        this.userSession = userSession;
    }

    //Callback hook for Connection close events.
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("IssWsSupport | closing websocket");
        this.userSession = null;
    }

    //Callback hook for Message Events.
    //THe WENv system sends always an answer for any command sent to it
    @OnMessage
    public void onMessage(String message)   {
        //if (this.sockObserver != null) { this.sockObserver.handleMessage(message); }
        try {
            //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
            System.out.println("        IssWsSupport | onMessage:" + message);
            JSONObject jsonObj = new JSONObject(message) ;
            if (jsonObj.get("endmove") != null) {
                boolean endmove = jsonObj.getBoolean("endmove");
                answerSupport.put(""+endmove);
                //System.out.println("        IssWsSupport | onMessage endmove=" + endmove);
            } else if (jsonObj.get("collision") != null) {
                boolean collision = jsonObj.getBoolean("collision");
                //System.out.println("        IssWsSupport | onMessage collision=" + collision );
            } else if (jsonObj.get("sonarName") != null) {
                String sonarName = jsonObj.getString( "sonarName");
                String distance = jsonObj.getString("distance");
                System.out.println("        IssWsSupport | onMessage sonarName=" + sonarName + " distance=" + distance);
            }
        } catch (Exception e) {}
    }

    @OnError
    public void disconnected(Session session, Throwable error){
        System.out.println("IssWsSupport | disconnected  " + error.getMessage());
    }
    
//------------------------------ IssOperations ----------------------------------
    /*
    Fire and forget
     */
    @Override
    public void forward(String msg)  {
        try {
            //this.userSession.getAsyncRemote().sendText(message);
            this.userSession.getBasicRemote().sendText(msg); //synch: blocks until the message has been transmitted
            //The WEnv receiver sends always an answer. Thus, it does not handle message N+1 before the end of msg N
            //Do we transform the forward in a request or assume that the user put an adeguate interval between messages?
            System.out.println("        IssWsSupport | forward " + msg);
        }catch( Exception e){
            System.out.println("        IssWsSupport | forward ERROR " + e.getMessage());
        }
    }

    @Override
    public String request(String msg) {
        try{
            System.out.println("        IssWsSupport | request " + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            this.userSession.getBasicRemote().sendText(msg);    //synch: blocks until the message has been transmitted
            //wait for the answer received by onMessage
            answerSupport.engage();
            return answerSupport.get(); //blocking
        }catch( Exception e){
            System.out.println("        IssWsSupport | request ERROR " + e.getMessage());
            return "error";
        }
    }

}
