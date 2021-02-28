/**
 IssRobotSupport.java
 ===============================================================
 Implements interaction with the virtual robot using the aril
 and the given communication support
 Adapts the application to the cril
 ===============================================================
 */
package it.unibo.interaction;
import java.util.HashMap;

@RobotMoveTimeSpec
public class IssVirtualRobotSupport implements IssOperations{
    private IssOperations support;
    //private Object supported;
    private static HashMap<String, Integer> timemap = new HashMap<String, Integer>( );
    private   String forwardMsg   ;
    private   String backwardMsg  ;
    private   String turnLeftMsg  ;
    private   String turnRightMsg ;
    private   String haltMsg      ;

    public IssVirtualRobotSupport(Object supportedObj, IssOperations support){
        this.support   = support;
        //this.supported = supportedObj;
        IssAnnotationUtil.getMoveTimes( supportedObj, timemap );
        setCrilMsgs();
    }

    protected void setCrilMsgs(){
        forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": "+ timemap.get("w")+"}";
        backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": "+ timemap.get("s")+"}";
        turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": "+ timemap.get("l")   + "}";
        turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\":"+ timemap.get("r")   + "}";
        haltMsg      = "{\"robotmove\":\"alarm\", \"time\": "+ timemap.get("h")+"}";
    }

    @Override
    public void forward( String move )   {
        switch( move ){
            case "h" : support.forward(haltMsg);     break;
            case "w" : support.forward(forwardMsg);  break;
            case "s" : support.forward(backwardMsg); break;
            case "l" : support.forward(turnLeftMsg); break;
            case "r" : support.forward(turnRightMsg);break;
        }
    }

    @Override
    public String request( String move ) {
        switch( move ){
            case "h" : return support.request(haltMsg);
            case "w" : return support.request(forwardMsg);
            case "s" : return support.request(backwardMsg);
            case "l" : return support.request(turnLeftMsg);
            case "r" : return support.request(turnRightMsg);
        }
        return "";
    }

}
