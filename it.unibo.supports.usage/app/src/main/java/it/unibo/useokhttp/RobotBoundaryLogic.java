/*
===============================================================
RobotBoundaryLogic.java
implements the business logic  

===============================================================
*/
package it.unibo.useokhttp;
import it.unibo.interaction.IssCommSupport;
import it.unibo.interaction.MsgRobotUtil;
import mapRoomKotlin.mapUtil;

public class RobotBoundaryLogic {
    private IssCommSupport rs ;

private int stepNum              = 1;
private boolean boundaryWalkDone = false ;
private boolean usearil          = false;
private int moveInterval         = 100;
private RobotMovesInfo robotInfo;
    final String forwardMsg  = "{\"robotmove\":\"moveForward\", \"time\": 330}";
    final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 330}";
    final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": 100}";


    public RobotBoundaryLogic(IssCommSupport support, boolean usearil, boolean doMap){
        rs           = support;
        this.usearil = usearil;
        robotInfo    = new RobotMovesInfo(doMap);
        //robotInfo.showRobotMovesRepresentation();
    }

    public void doBoundaryGoon(){
        rs.request( usearil ? MsgRobotUtil.wMsg : forwardMsg  );
        delay(moveInterval ); //to reduce the robot move rate
    }

    public synchronized String doBoundaryInit(){
        System.out.println("RobotBoundaryLogic | doBoundary rs=" + rs + " usearil=" + usearil);
        rs.request( usearil ? MsgRobotUtil.wMsg : forwardMsg  );
        //The reply to the request is sent by WEnv after the wtime defined in issRobotConfig.txt  
        System.out.println( mapUtil.getMapRep() );
        delay(moveInterval ); //to reduce the robot move rate
        while( ! boundaryWalkDone ) {
            try {
                wait();
                //System.out.println("RobotBoundaryLogic | RESUMES " );
                rs.close();
             } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return robotInfo.getMovesRepresentationAndClean();
    }

    public void updateMovesRep (String move ){
        robotInfo.updateRobotMovesRepresentation(move);
    }

 //Business logic in RobotBoundaryLogic
    protected synchronized void boundaryStep( String move, boolean obstacle ){
         if (stepNum <= 4) {
            if( move.equals("turnLeft") ){
                updateMovesRep("l");
                //showRobotMovesRepresentation();
                if (stepNum == 4) {
                    boundaryWalkDone=true;
                    notify(); //to resume the main
                    return;
                }
                stepNum++;
                doBoundaryGoon();
                return;
            }
            //the move is moveForward
            if( obstacle ){
                rs.request( usearil ? MsgRobotUtil.lMsg : MsgRobotUtil.turnLeftMsg   );
                delay(moveInterval ); //to reduce the robot move rate
            }
            if( ! obstacle ){
                updateMovesRep("w");
                doBoundaryGoon();
            }
            robotInfo.showRobotMovesRepresentation();
        }else{ //stepNum > 4
            System.out.println("RobotBoundaryLogic | boundary ENDS"  );
        }
    }

    protected void delay( int dt ){
        try { Thread.sleep(dt); } catch (InterruptedException e) { e.printStackTrace(); }
    }

}
