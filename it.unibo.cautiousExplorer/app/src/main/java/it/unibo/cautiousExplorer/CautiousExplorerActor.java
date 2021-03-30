package it.unibo.cautiousExplorer;

import org.json.JSONObject;

public class CautiousExplorerActor extends AbstractRobotActor {

    private enum State {start, exploring, obstacle, end };

    private State curState       = State.start ;
    private boolean tripStopped  = true;
    protected RobotMovesInfo moves = new RobotMovesInfo(false);
    protected RobotMovesInfo map   = new RobotMovesInfo(true);

    public CautiousExplorerActor(String name ) {
        super(name );
    }

    protected void continueWalk(){
        if( ! tripStopped   )  doStep();
        else{ System.out.println("please resume ..."); }
    }

    protected void updateTripInfo(String move){
        moves.updateMovesRep(move);
        map.updateMovesRep(move);
    }

    protected void fsm(String move, String endmove) {
        System.out.println(myname + " | fsm state=" + curState +  " move=" + move + " endmove=" + endmove);
        switch (curState) {
            case start: {
                if( move.equals("resume") ){
                    map.showRobotMovesRepresentation();
                    doStep();
                    curState = State.exploring;
                 };
                break;
            }
            case exploring: {
                if( move.equals("resume")){
                    doStep();
                }else if (move.equals("moveForward") && endmove.equals("true")) {
                    updateTripInfo("w");
                    continueWalk();
                } else if (move.equals("moveForward") && endmove.equals("false")) {
                    curState = State.obstacle;
                    littleBack();
                }
                break;
            }//exploring

            case obstacle : {
                if (move.equals("resume")) {
                    curState = State.exploring;
                    doStep();
                } else if (move.equals("moveBackward") && endmove.equals("true")) {

                    System.out.println("back to home along the same path ...");
                    map.showRobotMovesRepresentation();
                    moves.showRobotMovesRepresentation();
                }
                break;
            }//obstacle

            default: {
                System.out.println("error - curState = " + curState);
            }
        }

    }


/*
======================================================================================
 */
    @Override
    protected void msgDriven( JSONObject infoJson){
        System.out.println("CautiousExplorerActor | infoJson:" + infoJson);
        if( infoJson.has("endmove") )        fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if( infoJson.has("robotcmd") )  handleRobotCmd(infoJson);
    }

    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        System.out.println("CautiousExplorerActor | handleSonar:" + sonarname + " distance=" + distance);
    }
    protected void handleCollision( JSONObject collisioninfo ){
        //System.out.println("RobotApplication | handleCollision move=" + move  );
    }

    protected void handleRobotCmd( JSONObject robotCmd ){
        String cmd = (String)  robotCmd.get("robotcmd");
        System.out.println("===================================================="    );
        System.out.println("CautiousExplorerActor | handleRobotCmd cmd=" + cmd  );
        System.out.println("===================================================="    );
        if( cmd.equals("STOP") ) {
            tripStopped = true;
        }
        if( cmd.equals("RESUME") && tripStopped ){
            tripStopped = false;
            fsm("resume", "");
        }
    }
}
