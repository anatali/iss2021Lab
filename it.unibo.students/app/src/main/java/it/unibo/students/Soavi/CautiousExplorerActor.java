package it.unibo.students.Soavi;

import it.unibo.executors.RobotMovesInfo;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class CautiousExplorerActor extends ActorBasicJava {
    final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": 100}";

    private enum State {start, walking, obstacle, end };
    private IssWsHttpJavaSupport support;
    private State curState       =  State.start ;
    private int stepNum          = 1;
    private int movesX           = 1;
    private int movesY           = 1;
    private int times            = 0;
    private boolean firstTime    = false;
    private boolean firstTimeSonar    = true;
    private int indice           = 0;
    private RobotMovesInfo moves = new RobotMovesInfo(true);

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
        System.out.println( myname + " | moves X = " +movesX+ " moves Y = " +movesY);

        switch( curState ) {
            case start: {
                if(!move.equals("turnRight")) {
                    moves.showRobotMovesRepresentation();
                    times = movesX;
                    doStep();
                    curState = State.walking;
                } //else do nothing for stopping the robot after it returned to den after it found an obstacle
                break;
            }
            case walking: {
                if (move.equals("moveForward") && endmove.equals("true")) {
                    //curState = State.walk;
                    moves.updateMovesRep("w");
                    times--;
                    if(stepNum == 4 && times == 0) {
                        curState = State.end;
                        turnLeft();
                    }
                    else {
                        if(times == 0){
                            stepNum++;
                            times = stepNum%2==0 ? movesY : movesX;
                            turnLeft();
                        }else{
                            doStep();
                        }
                    }

                } else if (move.equals("moveForward") && endmove.equals("false")) {
                    System.out.println(myname + " | robot found an obstacle");
                    moves.setObstacle();
                    curState = State.obstacle;
                    firstTime = true;
                    times = stepNum%2==0 ? 0 : 1;
                    turnRight();
                } else if (move.equals("turnLeft") && endmove.equals("true")){
                    System.out.println("turnLeft arrived");
                    moves.updateMovesRep("l");
                    moves.showRobotMovesRepresentation();

                    doStep();
                }

                break;
            }//walk

            case obstacle :
                if (move.equals("turnRight") && endmove.equals("true") && firstTime) {
                    firstTime = false;
                    turnRight();
                }
                else if (endmove.equals("true")){
                    String journey = moves.getStringReverseMovesRepresentation();
                    if (indice < journey.length()) {
                        if (journey.charAt(indice) == 'l')
                            turnRight();
                        else if (journey.charAt(indice) == 'w')
                            doStep();
                        indice++;
                    } else {
                        curState = State.end;
                        turnRight();
                    }
                }
                break;

            case end : {
                if( move.equals("turnLeft") ) {
                    System.out.println("EXPLORATION ROUND ENDED GREAT");
                    moves.showRobotMovesRepresentation();
                    //Increase the path
                    movesX++;
                    movesY++;
                    //Restart the exploration
                    curState = State.walking;
                    times = movesX;
                    stepNum = 1;
                    moves.cleanMovesRepresentation();

                    doStep();
                    //turnRight();    //to compensate last turnLeft
                }else if (move.equals("turnRight")){
                    System.out.println("EXPLORATION ROUND FOUND OBSTACLE");
                    //increase the path
                    if(times == 0) movesX++;
                    else movesY++;

                    stepNum   = 1;
                    curState  = State.start;
                    moves.cleanMovesRepresentation();
                    turnRight();
                }
                break;
            }
            default: {
                System.out.println("error - curState = " + curState);
            }
        }
    }


    @Override
    protected void handleInput(String msg ) {     //called when a msg is in the queue
        //System.out.println( name + " | input=" + msgJsonStr);
        if( msg.equals("startApp"))  fsm("","");
        else msgDriven( new JSONObject(msg) );
    }

    protected void msgDriven( JSONObject infoJson){
        if( infoJson.has("endmove") )        fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if( infoJson.has("robotcmd") )  handleRobotCmd(infoJson);
    }

    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        if(firstTimeSonar){
            firstTimeSonar = false;
            System.out.println("RobotApplication | handleSonar:" + sonarname + " distance=" + distance
                    + " | Wait for 2 seconds");
            try {
                Thread.sleep(2000); //resta fermo 2 secondi
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    protected void handleCollision( JSONObject collisioninfo ){
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotApplication | handleCollision move=" + move  );
    }

    protected void handleRobotCmd( JSONObject robotCmd ){
        String cmd = (String)  robotCmd.get("robotcmd");
        System.out.println("===================================================="    );
        System.out.println("RobotApplication | handleRobotCmd cmd=" + cmd  );
        System.out.println("===================================================="    );
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

}