/**
 IssRobotSupport.java
 ===============================================================
 Implements interaction with the virtual robot using the aril
 and the given communication support
 ===============================================================
 */
package it.unibo.interaction;


public class IssRobotSupport implements IssOperations{
    private IssOperations support;
    private static final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 1600}";
    private static final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    private static final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";

    public IssRobotSupport(IssOperations support){
        this.support = support;
    }

    @Override
    public void forward( String move ) throws Exception {
        switch( move ){
            case "w" : support.forward(forwardMsg);  break;
            case "l" : support.forward(turnLeftMsg); break;
            case "r" : support.forward(turnRightMsg);break;
        }

    }

    @Override
    public String request( String move ) {
        switch( move ){
            case "w" : return support.request(forwardMsg);
            case "l" : return support.request(turnLeftMsg);
            case "r" : return support.request(turnRightMsg);
        }
        return "";
    }
}
