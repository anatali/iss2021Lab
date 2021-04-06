package it.unibo.students.Valentini;


import it.unibo.executors.RobotMovesInfo;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class CautiousExplorerActor extends ActorBasicJava {

    final String forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String turnLeftMsg = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg = "{\"robotmove\":\"alarm\", \"time\": 100}";

    private enum State {start, walking, obstacle, sonar, back, end}

    ;
    private IssWsHttpJavaSupport support;
    private State curState = State.start;
    //private int stepNum = 1;
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

    protected void fsm(String move, String endmove) {
        System.out.println(myname + " | fsm state=" + curState  + " move=" + move + " endmove=" + endmove);
        switch (curState) {
            case start: {
                moves.updateMovesRep("d");

                moves.updateMovesRep("w");
                moves.showRobotMovesRepresentation();
                doStep();
                curState = State.walking;
                break;
            }
            case walking: {
                if ( endmove.equals("true")) {
                    //curState = State.walk;
                    moves.updateMovesRep("w");
                    moves.showRobotMovesRepresentation();

                    // doJourney();
                    doStep();
                    // stepNum ++;
                } else if ( endmove.equals("false")) {
                    curState = State.obstacle;
                    turnLeft();

                }
                break;
            }//walk

            case obstacle:
                if(curState==State.obstacle) {
                    turnLeft();

                    moves.updateMovesRep("o");
                    moves.showRobotMovesRepresentation();
                    // curState = State.walking;
                    //doStep();
                    curState = State.back;
                }
                break;
            case back: {
                if (curState==State.back && endmove.equals("true")) {
                    //curState = State.walk;
                    moves.updateMovesRep("w");
                    moves.showRobotMovesRepresentation();
                    doStep();

                } else if(endmove.equals("false")) {
                    curState = State.end;
                }

                /*else if (move.equals("moveForward") && endmove.equals("false")) {
                    curState = State.obstacle;
                    turnLeft();
                } else {
                    System.out.println("IGNORE answer of turnLeft");
                }*/

                break;
            }//walk


            case end: {
                if (curState==State.end) {
                    System.out.println("BOUNDARY WALK END");
                    moves.showRobotMovesRepresentation();
                   /* turnRight();
                   /* turnRight();   //to compensate last turnLeft
                }/* else {
                  //  stepNum = 1;
                    curState = State.start;
                    moves = new RobotMovesInfo(true);
                }*/
                }
                break;
            }
            default: {
                System.out.println("error - curState = " + curState);
            }
        }
    }


    @Override
    protected void handleInput(String msg) {     //called when a msg is in the queue
        //System.out.println( name + " | input=" + msgJsonStr);
        if (msg.equals("startApp")) fsm("", "");
        else msgDriven(new JSONObject(msg));
    }

    protected void msgDriven(JSONObject infoJson) {
        if (infoJson.has("endmove")) fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        else if (infoJson.has("sonarName")) handleSonar(infoJson);
        else if (infoJson.has("collision")) handleCollision(infoJson);
        else if (infoJson.has("robotcmd")) handleRobotCmd(infoJson);
    }

    protected void handleSonar(JSONObject sonarinfo) {
        String sonarname = (String) sonarinfo.get("sonarName");
        int distance = (Integer) sonarinfo.get("distance");
        //System.out.println("RobotApplication | handleSonar:" + sonarname + " distance=" + distance);
    }

    protected void handleCollision(JSONObject collisioninfo) {
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotApplication | handleCollision move=" + move  );
    }

    protected void handleRobotCmd(JSONObject robotCmd) {
        String cmd = (String) robotCmd.get("robotcmd");
        System.out.println("====================================================");
        System.out.println("RobotApplication | handleRobotCmd cmd=" + cmd);
        System.out.println("====================================================");
    }

    //------------------------------------------------
    protected void doStep() {
        support.forward(forwardMsg);
        delay(1000); //to avoid too-rapid movement
        //  delay(500);
    }

    protected void turnLeft() {
        support.forward(turnLeftMsg);
        delay(500); //to avoid too-rapid movement
    }

    protected void turnRight() {
        support.forward(turnRightMsg);
        delay(500); //to avoid too-rapid movement
    }

    protected void doJourney( int stepnum) {

        //  delay(500);
    }


}
