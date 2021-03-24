/*
===============================================================
RobotBoundaryLogic.java
implements the business logic  

===============================================================
*/
package it.unibo.resumableWalker;
import it.unibo.interaction.IssCommActorSupport;
import it.unibo.interaction.MsgRobotUtil;



public class RobotResumableBoundaryLogic {
private IssCommActorSupport rs ;
private int stepNum              = 1;
private boolean boundaryWalkDone = false ;
private boolean usearil          = false;
private int moveInterval         = 500;
private RobotMovesInfo robotInfo;
    //public enum robotLang {cril, aril}    //todo

    public RobotResumableBoundaryLogic(IssCommActorSupport support, boolean usearil, boolean doMap){
        rs           = support;
        this.usearil = usearil;
        robotInfo    = new RobotMovesInfo(doMap);
     }

    public void reset(){
        stepNum          = 1;
        boundaryWalkDone = false;
        System.out.println("RobotBoundaryLogic | FINAL MAP:"  );
        robotInfo.showRobotMovesRepresentation();
        robotInfo.getMovesRepresentationAndClean();
        robotInfo.showRobotMovesRepresentation();
    }

    public void doBoundaryGoon(){
        rs.request( usearil ? MsgRobotUtil.wMsg : MsgRobotUtil.forwardMsg  );
        delay(moveInterval ); //to reduce the robot move rate
    }

    public void updateMovesRep (String move ){
        robotInfo.updateRobotMovesRepresentation(move);
    }

 //Business logic in RobotBoundaryLogic
    public synchronized void boundaryStep( String move, boolean obstacle, boolean robotHalted ){
         if (stepNum <= 4) {
            if( move.equals("turnLeft") ){
                updateMovesRep("l");
                //robotInfo.showRobotMovesRepresentation();
                if (stepNum == 4) {
                    System.out.println("RobotBoundaryLogic | boundary ENDS"  );
                    reset();
                    return;
                }
                stepNum++;
                if( ! robotHalted )  doBoundaryGoon();
                return;
            }
            //the move is moveForward

            if( obstacle && ! robotHalted){
                rs.request( usearil ? MsgRobotUtil.lMsg : MsgRobotUtil.turnLeftMsg   );
                delay(moveInterval ); //to reduce the robot move rate
            }else if( ! obstacle ){
                updateMovesRep("w");
                if( ! robotHalted ) doBoundaryGoon();
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
