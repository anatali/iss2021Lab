package it.unibo.cautiousExplorer;

import org.json.JSONObject;
/*
The robot cannot be stopped.
The returnPath is obstacle-free
 */
public class RunawayActor extends AbstractRobotActor {

    private enum State {start, moving, end };

    private State curState       = State.start ;

    //protected RobotMovesInfo moves = new RobotMovesInfo(false);
    protected RobotMovesInfo map   ;

    protected String returnPath    = "";

    public RunawayActor(String name, String returnPath, RobotMovesInfo map ) {
        super(name );
        this.returnPath = returnPath;
        this.map        = map;
    }

    protected void updateTripInfo(String move){
        map.updateMovesRep(move);
    }

    protected void doMove(char moveStep){
        System.out.println("RunawayActor | doMove ............... " + moveStep);
        if( moveStep == 'w') doStep();
        else if( moveStep == 'l') turnLeft();
        else if( moveStep == 'r') turnRight();
        else if( moveStep == 's') doBackStep();
    }

    protected void fsm(String move, String endmove) {
        System.out.println(myname + " | fsm state=" +
                curState +  " move=" + move + " endmove=" + endmove + " returnPath="+returnPath );
        switch (curState) {
            case start: {
                //if( move.equals("goback") && returnPath.length() > 0){
                System.out.println("===============================================");
                if( returnPath.length() > 0){
                    //map.showRobotMovesRepresentation();
                    doMove( returnPath.charAt(0) );
                    curState = State.moving;
                 }else curState = State.end;
                break;
            }
            case moving: {
                String moveInfo = MoveStep.get(move);
                if ( endmove.equals("true")) {
                    returnPath = returnPath.substring(1);
                    System.out.println("RunawayActor | moveInfo " + moveInfo + " returnPath="+ returnPath);
                    updateTripInfo(moveInfo);
                    map.showRobotMovesRepresentation();
                    if( returnPath.length() > 0){
                        //map.showRobotMovesRepresentation();
                        doMove( returnPath.charAt(0) );
                        curState = State.moving;
                    }else{ //returnPath.length() == 0
                        //Here does not arrive, since no more moves
                        curState = State.end;
                    }
                } else if ( endmove.equals("false")) {
                    System.out.println("RunawayActor | fatal error - OUT OF HYPOTHESIS in " + curState);
                }
                break;
            }//moving
            case end: {
                System.out.println("RunawayActor | END ---------------- "  );
                map.showRobotMovesRepresentation();
            }//end
            default: {
                System.out.println("RunawayActor | error - curState = " + curState);
            }
        }

    }


/*
======================================================================================
 */
    @Override
    protected void msgDriven( JSONObject infoJson){
        System.out.println("RunawayActor | infoJson:" + infoJson);
        if( infoJson.has("goBack") )    fsm("", "");
        if( infoJson.has("endmove") )   fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        //else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        //else if( infoJson.has("collision") ) handleCollision(infoJson);
    }



}
