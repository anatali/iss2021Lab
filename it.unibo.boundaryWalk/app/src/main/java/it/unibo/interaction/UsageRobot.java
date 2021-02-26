/**
 UsageRobot.java
 ===============================================================
 Use the virtual robot with a protocol set with an annotation
 ===============================================================
 */
package it.unibo.interaction;

import it.unibo.robotUtils.MsgRobotUtil;

/*
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url="localHost:8091"
)
 */
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url="http://localHost:8090/api/move"
)
public class UsageRobot {
private IssRobotSupport commSupport;

    public UsageRobot(){
        IssOperations comms = new IssCommunications().create( this  );
        commSupport         = new IssRobotSupport( comms );
    }
    public void doJob() throws Exception{
        System.out.println("UsageRobot | doJob "  );
        commSupport.forward( "l" );
        commSupport.request( "r" );
    }

    public static void main(String args[]) throws Exception{
        UsageRobot appl = new UsageRobot();
        appl.doJob();
    }
}
