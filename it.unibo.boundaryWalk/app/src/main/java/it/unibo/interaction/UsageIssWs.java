package it.unibo.interaction;


@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url="localHost:8091"
)
public class UsageIssWs {
    private IssOperations support;
    //TODO: robotUtilityClass
    private String forwardMsg      = "{\"robotmove\":\"moveForward\" , \"time\": 600}";
    private String turnLeftMsg     = "{\"robotmove\":\"turnLeft\"    , \"time\": 300}";

    public UsageIssWs(){
        support = createCommSupport();
        testuseSupport();
    }

    protected IssOperations createCommSupport(){
        return new IssCommunications().create( this  );
    }

    protected void testuseSupport()  {
        try {
            support.forward(turnLeftMsg);
            System.out.println("UsageIssWs | forward done");
        }catch( Exception e ){
            System.out.println("UsageIssWs | ERROR " + e);
        }
    }

      public static void main(String args[])   {
        new UsageIssWs();
    }
}
