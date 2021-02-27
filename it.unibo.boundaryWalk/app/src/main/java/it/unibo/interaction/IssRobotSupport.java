/**
 IssRobotSupport.java
 ===============================================================
 Implements interaction with the virtual robot using the aril
 and the given communication support
 ===============================================================
 */
package it.unibo.interaction;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;

@RobotMoveTimeSpec
public class IssRobotSupport implements IssOperations{
    private IssOperations support;
    private static final HashMap<String, Integer> timemap = getMoveTimes( );
    private static final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": "+ timemap.get("w")+"}";
    private static final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": "+ timemap.get("s")+"}";
    private static final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": "+ timemap.get("l")   + "}";
    private static final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\":"+ timemap.get("r")   + "}";
    private static final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": "+ timemap.get("h")+"}";

    public IssRobotSupport(IssOperations support){
        this.support = support;
    }

    @Override
    public void forward( String move ) throws Exception {
        switch( move ){
            case "h" : support.forward(haltMsg);  break;
            case "w" : support.forward(forwardMsg);  break;
            case "s" : support.forward(backwardMsg);  break;
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
/*
Using Java reflection
 */
    protected static HashMap<String, Integer> getMoveTimes(  ){
        HashMap<String, Integer> mvtimeMap= new HashMap<String, Integer>();
        try {
                Class element = Class.forName("it.unibo.interaction.IssRobotSupport");
                Annotation[] annots = element.getAnnotations();
                for (Annotation annotation : annots) {
                    if (annotation instanceof RobotMoveTimeSpec) {
                        RobotMoveTimeSpec info = (RobotMoveTimeSpec) annotation;
                        mvtimeMap.put("w", info.wtime() ) ;
                        mvtimeMap.put("s", info.stime() ) ;
                        mvtimeMap.put("l", info.ltime() ) ;
                        mvtimeMap.put("r", info.rtime() ) ;
                        mvtimeMap.put("h", info.htime() ) ;
                    }
                }
            } catch (Exception exception) {
                //exception.printStackTrace();
                System.out.println("IssRobotSupport | getMoveTimes ERROR:" + exception.getMessage());
                mvtimeMap.put("w", 600 ) ;
                mvtimeMap.put("s", 600 ) ;
                mvtimeMap.put("l", 300 ) ;
                mvtimeMap.put("r", 300 ) ;
                mvtimeMap.put("h", 100 ) ;
            }
            return mvtimeMap;
        }

}
