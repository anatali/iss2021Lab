/**
 IssRobotSupport.java
 ===============================================================

 ===============================================================
 */
package it.unibo.interaction;
import java.util.HashMap;

@RobotMoveTimeSpec
public class IssRobotSupport{ //implements IssOperations{
    private IssOperations support;
    //private Object supported;
    private static HashMap<String, Integer> timemap = new HashMap<String, Integer>( );

    public IssRobotSupport(){ //(Object supportedObj, IssOperations support){
        //this.support   = support;
        //IssAnnotationUtil.getMoveTimes( supportedObj, timemap );
      }


    //@Override
    public void forward( AppMsg  move )   { //AppMsg
    }

    //@Override
    public String request( AppMsg move ) {
        return "";
    }

}
