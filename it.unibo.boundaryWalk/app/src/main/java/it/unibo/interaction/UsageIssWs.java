package it.unibo.interaction;
import it.unibo.robotUtils.MsgRobotUtil;

@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url="localHost:8091"
)
public class UsageIssWs {
    private IssOperations support;

    public UsageIssWs(){
        support = createCommSupport();
        testuseSupport();
    }

    protected IssOperations createCommSupport(){
        return new IssCommunications().create( this  );
    }

    protected void testuseSupport()  {
        try {
            String answer = "message sent";
            support.forward(MsgRobotUtil.turnLeftMsg);
            Thread.sleep(400);  //give time ...
            System.out.println("UsageIssWs | answer=" + answer);
            support.forward(MsgRobotUtil.turnRightMsg);
            Thread.sleep(400);  //give time ...
            System.out.println("UsageIssWs | answer=" + answer);

            //Thread.sleep(1000);

            answer = support.request(MsgRobotUtil.turnRightMsg);
            System.out.println("UsageIssWs | answer=" + answer);

            answer = support.request(MsgRobotUtil.turnLeftMsg);
            System.out.println("UsageIssWs | answer=" + answer);

            answer = support.request(MsgRobotUtil.forwardMsg);
            System.out.println("UsageIssWs | answer=" + answer);

        }catch( Exception e ){
            System.out.println("UsageIssWs | ERROR " + e);
        }
    }

      public static void main(String args[]){
        new UsageIssWs();
      }
}
