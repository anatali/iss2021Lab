/**
 UseVirtualRobot.java
 ===============================================================
 Use the virtual robot by exploiting Java annotation on the class
 to configure the protocol (HTTP or WS) and the move times.

 HOWEVER, the configuration can also be set (WITH PRIORITY over the class)
    by the file IssProtocolConfig.txt for the protocol
 by the file IssRobotConfig.txt       for the move times
 ===============================================================
 */
package it.unibo.interaction;


//the move times can be also set by the file IssRobotConfig.txt

/** //Interaction based on websocket
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

//the move times can be also set by the file IssRobotConfig.txt
@RobotMoveTimeSpec( ltime = 1000 )
public class UseVirtualRobot {

    //private IssOperations commSupport;
    private IssVirtualRobotSupport robotSupport;
    //Factory method
    public static UseVirtualRobot create(){
        UseVirtualRobot obj       = new UseVirtualRobot();  //appl-object
        IssOperations commSupport = new IssCommunications().create( obj  );
        obj.robotSupport          = new IssVirtualRobotSupport( obj, commSupport ); //'inject'
        return obj;  //return the created appl-object
    }

    public void doJob() throws Exception{
        System.out.println("UsageRobot | doJob START"  );
        robotSupport.forward( "r" );
        Thread.sleep(1000);        //required ONLY if we use websockets
        String answer = robotSupport.requestSynch( "r" );
        System.out.println("UsageRobot | doJob answer to r= " + answer);
        Thread.sleep(1000);      //required ONLY if we use websockets
        robotSupport.request( "l" );    //the answer is sent but we do not wait
        Thread.sleep(1000);      //required ONLY if we use websockets
    }

    public static void main(String args[]) throws Exception{
        UseVirtualRobot appl = UseVirtualRobot.create();
        appl.doJob();
    }
}
