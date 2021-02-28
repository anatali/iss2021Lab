/**
 UsageRobot.java
 ===============================================================
 Use the virtual robot with a protocol set with an annotation
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
@RobotMoveTimeSpec( ltime = 1000 )  //if omitted, the move times are set by IssRobotSupport
public class UsageRobot {
private IssRobotSupport robotSupport;

    //Factory method
    public static UsageRobot create(){
        UsageRobot obj               = new UsageRobot();  //appl-object
        IssOperations comms          = new IssCommunications().create( obj  );  //low-level support for appl-object
        IssRobotSupport robotSupport = new IssRobotSupport( obj, comms );    //high-level support for appl-object
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
