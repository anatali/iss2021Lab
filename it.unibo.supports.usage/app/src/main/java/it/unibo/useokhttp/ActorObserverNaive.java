package it.unibo.useokhttp;

import it.unibo.interaction.IssCommSupport;
import org.json.JSONObject;

public class ActorObserverNaive extends ActorObserverBasic{
    RobotInputController controller;
    public ActorObserverNaive(int counter, RobotInputController controller) {
        super(counter);
        this.controller = controller;
    }

    @Override
    protected void handleInput(String info){
        //System.out.println("ActorObserverNaive " + counter + " | --- " + info + " " + aboutThreads());
        //delay( counter*500 );
        JSONObject jsonInfo = new JSONObject( info );
        if( jsonInfo.has("collision")) simulateUserCmd();
        System.out.println("ActorObserverNaive " + counter + " | " + info + " " + aboutThreads());
    }

    protected void simulateUserCmd(){
        System.out.println("ActorObserverNaive " + counter + " | --- simulateUserCmd   "  );
        controller.handleInfo( "{\"robotcmd\":\"STOP\"}" );
    }
}
