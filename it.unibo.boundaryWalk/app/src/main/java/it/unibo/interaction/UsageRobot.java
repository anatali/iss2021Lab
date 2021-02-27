/**
 UsageRobot.java
 ===============================================================
 Use the virtual robot with a protocol set with an annotation
 ===============================================================
 */
package it.unibo.interaction;


@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url="localHost:8091"
)

/*
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url="http://localHost:8090/api/move"
)*/
public class UsageRobot {
private IssRobotSupport robotSupport;

    //Factory method
    public static UsageRobot create(){
        UsageRobot obj               = new UsageRobot();
        IssOperations comms          = new IssCommunications().create( obj  );
        IssRobotSupport robotSupport = new IssRobotSupport( comms );
        obj.setRobotSupport(robotSupport);
        return obj;
    }

    private UsageRobot(){    }

    private void setRobotSupport(IssRobotSupport support){
        this.robotSupport = support;
    }

    public void doJob() throws Exception{
        System.out.println("UsageRobot | doJob "  );
        robotSupport.forward( "l" );
        Thread.sleep(500);
        robotSupport.request( "r" );
    }

    public static void main(String args[]) throws Exception{
        UsageRobot appl = UsageRobot.create();
        appl.doJob();
    }
}
