/**
 IssRobotSupport.java
 ===============================================================
 Implements interaction with the virtual robot using the aril
 and the given communication support
 ===============================================================
 */
package it.unibo.interaction;
import java.util.HashMap;

@RobotMoveTimeSpec
public class IssRobotSupport implements IssOperations{
    private IssOperations support;
    private Object supported;
    private static HashMap<String, Integer> timemap ; //= IssAnnotationUtil.getMoveTimes( );  //set 'default' move-times
    private   String forwardMsg   ;
    private   String backwardMsg  ;
    private   String turnLeftMsg  ;
    private   String turnRightMsg ;
    private   String haltMsg      ;

    public IssRobotSupport(Object supportedObj, IssOperations support){
        this.support   = support;
        this.supported = supported;
        IssAnnotationUtil.getMoveTimes( supportedObj, timemap );  //override the 'default' move-times
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
    public void forward( String move ) throws Exception {
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
/*
Using Java reflection

    protected static HashMap<String, Integer> getMoveTimes(  ) {
        HashMap<String, Integer> mvtimeMap = new HashMap<String, Integer>();
        try {
            Class element = Class.forName("it.unibo.interaction.IssRobotSupport");
            Annotation[] annots = element.getAnnotations();
            fillMap(mvtimeMap, annots);
            return mvtimeMap;
        } catch (Exception exception) {
            System.out.println("IssRobotSupport | mvtimeMap ERROR:" + exception.getMessage());
            mvtimeMap.put("w", 600);
            mvtimeMap.put("s", 600);
            mvtimeMap.put("l", 300);
            mvtimeMap.put("r", 300);
            mvtimeMap.put("h", 100);
        }
        return mvtimeMap;
    }

    protected static void fillMap(HashMap<String, Integer> mvtimeMap, Annotation[] annots) {
         for (Annotation annotation : annots) {
            if (annotation instanceof RobotMoveTimeSpec) {
                RobotMoveTimeSpec info = (RobotMoveTimeSpec) annotation;
                mvtimeMap.put("w", info.wtime());
                mvtimeMap.put("s", info.stime());
                mvtimeMap.put("l", info.ltime());
                mvtimeMap.put("r", info.rtime());
                mvtimeMap.put("h", info.htime());
                System.out.println("IssRobotSupport | fillMap  ltime="  + info.ltime());
            }
        }
    }
    protected void setMoveTimes( Object obj, HashMap<String, Integer> mvtimeMap){
            Class<?> clazz = obj.getClass();
            Annotation[] annotations = clazz.getAnnotations();
            fillMap(mvtimeMap, annotations);
    }
 */
}
