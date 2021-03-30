package it.unibo.cautiousExplorer;

import org.json.JSONObject;
import it.unibo.supports2021.*;

public abstract class AbstractRobotActor extends ActorBasicJava {
    final String forwardMsg         = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg        = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String littleBackwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 150}";
    final String turnLeftMsg        = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg       = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg            = "{\"robotmove\":\"alarm\", \"time\": 100}";

    protected int moveInterval = 500;   //to avoid too-rapid movement
    protected IssWsHttpJavaSupport support;



    public AbstractRobotActor( String name ) {
        super(name);
        support = IssWsHttpJavaSupport.createForWs("localhost:8091" );
        support.registerActor(this);

    }


    //------------------------------------------------
    protected void doStep(){
        support.forward( forwardMsg);
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void littleBack(){
        support.forward( littleBackwardMsg );
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void turnLeft(){
        support.forward( turnLeftMsg );
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void turnRight(){
        support.forward( turnRightMsg );
        delay(moveInterval); //to avoid too-rapid movement
    }
/*
======================================================================================
 */
    @Override
    protected void handleInput(String infoJson) {
        System.out.println("AbstractRobotActor | infoJson:" + infoJson);
        msgDriven( new JSONObject(infoJson) );
    }

    abstract protected void  msgDriven( JSONObject infoJson);


}
