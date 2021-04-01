package it.unibo.executor;

import it.unibo.cautiousExplorer.AbstractRobotActor;
import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorMsgs;
import it.unibo.supports2021.TimerActor;
import org.json.JSONObject;

import static it.unibo.executor.ApplMsgs.*;

/*
The map is a singleton object, managed by mapUtil
 */
public class StepRobotActor extends AbstractRobotActor {

    protected enum State {start, moving };

    protected State curState  = State.start ;
    protected IJavaActor ownerActor = null;
    protected TimerActor timer;
    protected int plannedMoveTime = 0;

    public StepRobotActor(String name, IJavaActor ownerActor ) {
        super(name  );
        this.ownerActor = ownerActor;
    }

    private long StartTime = 0;

    protected void fsmstep( String move, String arg) {
        System.out.println(myname + " | state=" +
                curState +  " move=" + move + " arg=" + arg  );
        switch (curState) {
            case start: {
                if( move.equals( stepId )) {
                    StartTime = this.getCurrentTime();
                    timer     = new TimerActor("t0", this);
                    timer.send(ActorMsgs.startTimerMsg.replace("TIME", arg));
                    plannedMoveTime = Integer.parseInt(arg);
                    support.forward(ApplMsgs.forwardLongTimeMsg);
                    curState = State.moving;
                    break;
                }
            }
            case moving: {
                 String dt = ""+this.getDuration( StartTime ); //effective duration of the move
                 if (move.equals(ActorMsgs.endTimerId)){    //time elapsed
                     support.forward(haltMsg); //must avoid to send endmove
                     if(ownerActor!=null)  ownerActor.send(stepDoneMsg);
                 }else{ //"collision" : move interrupted by obstacle
                     timer.kill();
                     //int todoTime = plannedMoveTime - dt;
                     String answer = stepFailMsg.replace("TIME", dt);
                     System.out.println(myname + " | answer="+ answer);
                     if(ownerActor!=null) ownerActor.send(answer);
                 }
                support.removeActor(this);
                terminate();
                break;
            }//moving

            default: {
                System.out.println(myname + " | error - curState = " + curState);
            }
        }
    }


/*
======================================================================================
 */
    @Override
    protected void msgDriven( JSONObject msgJson){
        System.out.println("StepRobotActor |  msgJson:" + msgJson);
         if( msgJson.has(stepId) ) {
             //System.out.println("StepRobotActor |  msgJson:" + msgJson);
             String time = msgJson.getString(stepId);
             fsmstep( stepId, time);
         }else if( msgJson.has("collision") ) {
             //System.out.println("StepRobotActor | msgJson:" + msgJson);
             fsmstep("collision", "");
         }else if( msgJson.has(ActorMsgs.endTimerId) ) {
             fsmstep(ActorMsgs.endTimerId, "");
         }
    }


}
