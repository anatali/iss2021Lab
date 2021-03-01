/**
 UsageRobot.java
 ===============================================================

 ===============================================================
 */
package it.unibo.interaction;

//Interaction based on websocket
/*
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url="localHost:8091"
)
*/
//Interaction based on HTTP
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url      = "http://localHost:8090/api/move"
)
@RobotMoveTimeSpec( ltime = 1000 )
//the move times are set by the file IssRobotConfig.txt or (if it does not exists) by IssRobotSupport
public class UsageRobot {
private IssVirtualRobotSupport robotSupport;

    //Factory method
    public static UsageRobot create(){
        UsageRobot obj               = new UsageRobot();  //appl-object
        IssOperations comms          = new IssCommunications().create( obj  );  //low-level support for appl-object
        IssVirtualRobotSupport robotSupport = new IssVirtualRobotSupport( obj, comms );    //high-level support for appl-object
        obj.robotSupport             = robotSupport; //'inject'
        return obj;  //return the created appl-object
    }

    public void doJob() throws Exception{
        System.out.println("UsageRobot | doJob "  );
        robotSupport.forward( "l" );
        //Thread.sleep(500);        //required if we use websockets
        robotSupport.request( "r" );
        Thread.sleep(500);      //required if we use websockets
    }

    public static void main(String args[]) throws Exception{
        UsageRobot appl = UsageRobot.create();
        appl.doJob();
    }
}
