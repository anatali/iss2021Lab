package it.unibo.interaction;


@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url="http://localHost:8090/api/move"
)
public class UsageIssHttp {
    private IssOperations support;

    public UsageIssHttp(){
        support = createCommSupport();
        testuseSupport();
    }

    protected IssOperations createCommSupport(){
        return new IssCommunications().create( this  );
    }

    protected void testuseSupport(){
        String move  = "turnLeft"  ;
        String time  = "600" ;
        String jsonCmd   = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
        String answer = support.request(jsonCmd);
        System.out.println( "UsageIssHttp | answer=" + answer  );
    }

      public static void main(String args[])   {
        new UsageIssHttp();
    }
}
