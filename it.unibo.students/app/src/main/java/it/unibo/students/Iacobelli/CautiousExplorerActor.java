package it.unibo.students.Iacobelli;

import it.unibo.executors.RobotMovesInfo;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

import java.util.ArrayList;

public class CautiousExplorerActor extends ActorBasicJava {
    final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": 100}";
    private ArrayList<String> listMoves = new ArrayList<>();
    private enum State {start, walking, obstacle, end, stop };
    private IssWsHttpJavaSupport support;
    private State curState       =  State.start ;
    private boolean obstacleFound= false;
    private int stepNum          = 1;
    private RobotMovesInfo moves = new RobotMovesInfo(true);
    private boolean robotDetected = false
            ;

    public CautiousExplorerActor(String name, IssWsHttpJavaSupport support) {
        super(name);
        this.support = support;
    }
/*
//Removed since we want use just the fsm, without any 'external' code
    public void reset(){
        System.out.println("RobotBoundaryLogic | FINAL MAP:"  );
        moves.showRobotMovesRepresentation();
        stepNum        = 1;
        curState       =  State.start;
        moves.getMovesRepresentationAndClean();
        moves.showRobotMovesRepresentation();
    }
*/

    protected void fsm(String move, String endmove){
        System.out.println( myname + " | fsm state=" + curState + " stepNum=" + stepNum + " move=" + move + " endmove=" + endmove);
        switch( curState ) {
            case start:
                moves.showRobotMovesRepresentation();
                doStep();
                curState = State.walking;
                //   moves.updateMovesRep("w");
                break;

            case walking:
                if ((move.equals("moveForward")  && endmove.equals("true"))){
                    //curState = State.walk;
                    if(robotDetected == false) {
                        if (obstacleFound == true) {
                            doReturnDen();
                            curState = State.end;

                        } else {
                            moves.updateMovesRep("w");
                            listMoves.add("w");
                            doStep();
                        }
                    }
                } else if (move.equals("moveForward")  && endmove.equals("false")) {
                    curState = State.obstacle;
                    rotation();
                    System.out.println("Sono quii");

                }else {System.out.println("IGNORE answer ");
                }
                break;
            //walk
            case obstacle:
                if(( move.equals("turnLeft") && endmove.equals("true"))){
                    moves.updateMovesRep("l");
                    moves.showRobotMovesRepresentation();
                    if (robotDetected == false) {
                        curState = State.walking;
                        obstacleFound = true;
                        doStep();
                    }
                }

            case end :
                if( move.equals("moveForward")) {
                    System.out.println("BOUNDARY WALK END");
                    moves.showRobotMovesRepresentation();
                }/*else{
                    //reset();
                    stepNum        = 1;
                    curState       =  State.start;
                    moves.getMovesRepresentationAndClean();
               }*/
                break;

            default:
                System.out.println("error - curState = " + curState);

        }
    }

    protected void doReturnDen() {
        for(String mov: listMoves){
            if(mov.equals("w")){
                moves.updateMovesRep("w");
                doStep();
            }
            else if (mov.equals("r")){
                turnRight();
            }
            else if (mov.equals("l")){
                turnLeft();
            }
        }
        rotation();
    }


    @Override
    protected void handleInput(String msg ) {     //called when a msg is in the queue
        //System.out.println( name + " | input=" + msgJsonStr);
        if( msg.equals("startApp"))  fsm("","");
        else msgDriven( new JSONObject(msg) );
    }

    protected void msgDriven( JSONObject infoJson){
        if( infoJson.has("endmove") )
            fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        else if( infoJson.has("sonarName") )
            handleSonar(infoJson);
        else if( infoJson.has("collision") )
            handleCollision(infoJson);

    }

    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        robotDetected = true;
        try {
            CautiousExplorerActor.sleep(1500);
            robotDetected = false;
            // fsm("resume", "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("RobotApplication | handleSonar:" + sonarname + " distance=" + distance);
    }
    protected void handleCollision( JSONObject collisioninfo ){
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotApplication | handleCollision move=" + move  );
    }



    //------------------------------------------------
    protected void doStep(){
        support.forward( forwardMsg);
        delay(1000); //to avoid too-rapid movement
    }
    protected void turnLeft(){
        support.forward( turnLeftMsg );
        delay(500); //to avoid too-rapid movement
    }
    protected void turnRight(){
        support.forward( turnRightMsg );
        delay(500); //to avoid too-rapid movement
    }
    protected void rotation(){
        turnLeft();
        turnLeft();
    }
}
